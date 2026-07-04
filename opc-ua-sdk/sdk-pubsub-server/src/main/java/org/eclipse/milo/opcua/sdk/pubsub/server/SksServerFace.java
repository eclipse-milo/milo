/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyServiceType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The opt-in Security Key Service server face: serves {@code GetSecurityKeys} for the locally
 * configured SecurityGroups by attaching an invocation handler to the ns0 method node ({@code
 * i=15215}) and advertising the SKS capabilities.
 *
 * <p>The ns0 nodes are mutated in place, values and handler only; ns0's node manager is never
 * structurally modified, so the face works independently of {@link
 * ServerPubSubOptions#isExposeInformationModel()}. Because the handler slot on {@code i=15215} is
 * process-global for the server, at most one enabled face per {@link OpcUaServer} is supported;
 * {@link #shutdown()} only restores {@link MethodInvocationHandler#NOT_IMPLEMENTED} if the node
 * still carries the handler this face attached.
 *
 * <p>Keys are generated and rotated by a {@link SecurityGroupKeyStore} seeded from the attach-time
 * {@link PubSubConfig#securityGroups()} and refreshed on every successful configuration apply
 * through the managed runtime — remote CloseAndUpdate edits and owner {@code
 * ServerPubSub.runtime()} reconfigures alike — via {@link #onConfigurationApplied}, registered as a
 * post-apply hook (S15): retained groups keep serving their keys undisturbed, while groups whose
 * SecurityPolicyUri or KeyLifetime changed have all existing keys invalidated and regenerated (Part
 * 14 §6.2.12.2; the engine independently restarts every referencing writer/reader group so
 * consumers re-fetch).
 *
 * <p>Created by {@link ServerPubSub} when {@link ServerPubSubOptions#isSecurityKeyServerEnabled()}
 * is {@code true}; {@link #startup()} and {@link #shutdown()} are driven by the owning {@link
 * ServerPubSub}'s lifecycle.
 */
final class SksServerFace {

  private static final Logger LOGGER = LoggerFactory.getLogger(SksServerFace.class);

  private final OpcUaServer server;
  private final SecurityGroupKeyStore keyStore;
  private final PubSubMethodAuthorizer authorizer;

  /** The configuration whose SecurityGroups the store last observed; swapped per apply. */
  private volatile PubSubConfig lastConfig;

  private @Nullable UaMethodNode methodNode;
  private @Nullable GetSecurityKeysMethodImpl attachedHandler;

  /**
   * Create a face for {@code server} serving keys for the SecurityGroups of {@code config}.
   *
   * @param authorizer the effective {@link PubSubMethodAuthorizer} resolved by {@link
   *     ServerPubSub}: the options-configured authorizer, else the shared {@link
   *     DefaultPubSubMethodAuthorizer} instance.
   * @throws PubSubConfigValidationException if a SecurityGroup configures an unsupported non-null
   *     SecurityPolicy URI, or two SecurityGroups share a SecurityGroupId.
   */
  SksServerFace(OpcUaServer server, PubSubConfig config, PubSubMethodAuthorizer authorizer) {
    this.server = server;
    this.keyStore = new SecurityGroupKeyStore(config.securityGroups());
    this.authorizer = authorizer;
    this.lastConfig = config;
  }

  /**
   * Post-apply hook (S15, hook #3): refresh the key store from the applied configuration's
   * SecurityGroups. The invalidated ids — groups whose SecurityPolicyUri or KeyLifetime changed
   * relative to the last-seen configuration, plus removed groups — are computed here from the
   * retained {@code lastConfig} (§6.2.12.2 "all existing keys of the SecurityGroup are
   * invalidated"); retained groups keep their rotation state. Violations in the replacement set are
   * logged inside {@link SecurityGroupKeyStore#replaceGroups} and never thrown — a post-apply hook
   * must not fail the apply. Runs on the reconfiguring thread, serialized by the managed runtime.
   */
  void onConfigurationApplied(PubSubConfig newConfig, ReconfigureResult result) {
    PubSubConfig oldConfig = this.lastConfig;
    this.lastConfig = newConfig;

    keyStore.replaceGroups(newConfig.securityGroups(), invalidatedGroupIds(oldConfig, newConfig));
  }

  /**
   * The SecurityGroupIds whose existing keys are invalidated by replacing {@code oldConfig}'s
   * groups with {@code newConfig}'s: ids whose SecurityPolicyUri or KeyLifetime changed, plus ids
   * no longer present. Package-private static for tests.
   */
  static Set<String> invalidatedGroupIds(PubSubConfig oldConfig, PubSubConfig newConfig) {
    var oldById = new HashMap<String, SecurityGroupConfig>();
    for (SecurityGroupConfig group : oldConfig.securityGroups()) {
      oldById.putIfAbsent(group.getSecurityGroupId(), group);
    }

    var invalidated = new HashSet<>(oldById.keySet());

    for (SecurityGroupConfig group : newConfig.securityGroups()) {
      SecurityGroupConfig old = oldById.get(group.getSecurityGroupId());
      if (old != null
          && Objects.equals(old.getSecurityPolicyUri(), group.getSecurityPolicyUri())
          && old.getKeyLifeTime().equals(group.getKeyLifeTime())) {
        invalidated.remove(group.getSecurityGroupId());
      }
    }

    return invalidated;
  }

  /** The key store backing this face; package-private for tests. */
  SecurityGroupKeyStore keyStore() {
    return keyStore;
  }

  /**
   * Attach the {@code GetSecurityKeys} handler to the ns0 method node and set the SKS capabilities
   * values ({@code SupportSecurityKeyPull=true}, {@code SupportSecurityKeyPush=false}, {@code
   * SupportSecurityKeyServer=true}).
   */
  void startup() {
    Optional<UaNode> node =
        server.getAddressSpaceManager().getManagedNode(NodeIds.PublishSubscribe_GetSecurityKeys);

    if (node.orElse(null) instanceof UaMethodNode getSecurityKeysNode) {
      if (getSecurityKeysNode.getInvocationHandler() != MethodInvocationHandler.NOT_IMPLEMENTED) {
        LOGGER.warn(
            "ns0 GetSecurityKeys node already has an invocation handler; replacing it."
                + " Only one SKS server face per server is supported.");
      }

      var handler = new GetSecurityKeysMethodImpl(getSecurityKeysNode);
      getSecurityKeysNode.setInvocationHandler(handler);

      this.methodNode = getSecurityKeysNode;
      this.attachedHandler = handler;
    } else {
      LOGGER.warn(
          "ns0 GetSecurityKeys UaMethodNode not found: {}",
          NodeIds.PublishSubscribe_GetSecurityKeys);
    }

    setCapabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyPull, true);
    setCapabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyPush, false);
    setCapabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyServer, true);
  }

  /**
   * Restore {@link MethodInvocationHandler#NOT_IMPLEMENTED} on the ns0 method node, unless another
   * handler was attached after ours, and set {@code SupportSecurityKeyServer=false}.
   */
  void shutdown() {
    UaMethodNode node = this.methodNode;

    if (node != null) {
      if (node.getInvocationHandler() == attachedHandler) {
        node.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED);
      } else {
        LOGGER.warn(
            "ns0 GetSecurityKeys node no longer carries this face's handler; not restoring");
      }
    }

    this.methodNode = null;
    this.attachedHandler = null;

    setCapabilityValue(NodeIds.PublishSubscribe_PubSubCapablities_SupportSecurityKeyServer, false);
  }

  /** Set the value of an existing ns0 capabilities variable node; never creates. */
  private void setCapabilityValue(NodeId nodeId, boolean value) {
    Optional<UaNode> node = server.getAddressSpaceManager().getManagedNode(nodeId);

    if (node.orElse(null) instanceof UaVariableNode variableNode) {
      variableNode.setValue(new DataValue(new Variant(value)));
    } else {
      LOGGER.warn("ns0 capabilities node not found: {}", nodeId);
    }
  }

  /**
   * The {@code GetSecurityKeys} handler; check order per K17.2: session, channel security mode,
   * argument validity, authorization, existence, then serve.
   */
  private final class GetSecurityKeysMethodImpl extends PubSubKeyServiceType.GetSecurityKeysMethod {

    GetSecurityKeysMethodImpl(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(
        InvocationContext context,
        @Nullable String securityGroupId,
        @Nullable UInteger startingTokenId,
        @Nullable UInteger requestedKeyCount,
        Out<String> securityPolicyUri,
        Out<UInteger> firstTokenId,
        Out<ByteString[]> keys,
        Out<Double> timeToNextKey,
        Out<Double> keyLifetime)
        throws UaException {

      Session session =
          context.getSession().orElseThrow(() -> new UaException(StatusCodes.Bad_SessionIdInvalid));

      // belt-and-suspenders: the access controller already denies unencrypted calls via the
      // nodeset's AccessRestrictions, but the handler must not rely on an unmodified nodeset
      if (session.getEndpoint().getSecurityMode() != MessageSecurityMode.SignAndEncrypt) {
        throw new UaException(StatusCodes.Bad_SecurityModeInsufficient);
      }

      // null Variant values pass the base class's type checks; reject explicitly. "empty"
      // carries no existence information, so Bad_NotFound needs no authorization first.
      if (securityGroupId == null || securityGroupId.isEmpty()) {
        throw new UaException(StatusCodes.Bad_NotFound);
      }

      // authorization before existence: the default authorizer makes unknown-group probes
      // indistinguishable from denied known groups for unauthorized callers
      StatusCode checkResult = authorizer.checkKeyAccess(session, securityGroupId);
      if (checkResult.isBad()) {
        throw new UaException(checkResult);
      }

      long starting = startingTokenId != null ? startingTokenId.longValue() : 0L;
      long requested = requestedKeyCount != null ? requestedKeyCount.longValue() : 0L;

      SecurityGroupKeyStore.SecurityKeys served =
          keyStore
              .getSecurityKeys(securityGroupId, starting, requested)
              .orElseThrow(() -> new UaException(StatusCodes.Bad_NotFound));

      securityPolicyUri.set(served.securityPolicyUri());
      firstTokenId.set(UInteger.valueOf(served.firstTokenId()));
      keys.set(served.keys().toArray(new ByteString[0]));
      timeToNextKey.set(served.timeToNextKeyMillis());
      keyLifetime.set(served.keyLifetimeMillis());
    }
  }
}
