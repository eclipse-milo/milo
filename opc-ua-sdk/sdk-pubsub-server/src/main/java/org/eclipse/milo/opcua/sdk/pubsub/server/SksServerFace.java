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
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
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
 * {@link PubSubConfig#securityGroups()}; reconfiguring via {@code ServerPubSub.runtime()} does not
 * rebuild the store (the same documented v1 limitation as the information model fragment).
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

  private @Nullable UaMethodNode methodNode;
  private @Nullable GetSecurityKeysMethodImpl attachedHandler;

  /**
   * Create a face for {@code server} serving keys for the SecurityGroups of {@code config}.
   *
   * @throws PubSubConfigValidationException if a SecurityGroup configures an unsupported non-null
   *     SecurityPolicy URI, or two SecurityGroups share a SecurityGroupId.
   */
  SksServerFace(OpcUaServer server, PubSubConfig config, ServerPubSubOptions options) {
    this.server = server;
    this.keyStore = new SecurityGroupKeyStore(config.securityGroups());

    var groupsById = new HashMap<String, SecurityGroupConfig>();
    for (SecurityGroupConfig group : config.securityGroups()) {
      groupsById.put(group.getSecurityGroupId(), group);
    }

    PubSubMethodAuthorizer configured = options.getMethodAuthorizer();
    this.authorizer = configured != null ? configured : new DefaultAuthorizer(groupsById::get);
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

  /**
   * The default {@link PubSubMethodAuthorizer} posture (K17.3): with a {@code RoleMapper}
   * configured the well-known roles apply; without one, encrypted callers are allowed unless a
   * SecurityGroup carries explicit non-empty RolePermissions (fail closed on explicit
   * restrictions).
   */
  static final class DefaultAuthorizer implements PubSubMethodAuthorizer {

    private static final StatusCode DENIED = new StatusCode(StatusCodes.Bad_UserAccessDenied);

    private final Function<String, @Nullable SecurityGroupConfig> groupLookup;

    DefaultAuthorizer(Function<String, @Nullable SecurityGroupConfig> groupLookup) {
      this.groupLookup = groupLookup;
    }

    @Override
    public StatusCode checkConfigure(Session session) {
      Optional<List<NodeId>> roleIds = session.getRoleIds();

      // no RoleMapper: allow, consistent with the core posture (D10)
      return roleIds
          .map(ids -> ids.contains(NodeIds.WellKnownRole_ConfigureAdmin) ? StatusCode.GOOD : DENIED)
          .orElse(StatusCode.GOOD);
    }

    @Override
    public StatusCode checkSksAdmin(Session session) {
      Optional<List<NodeId>> roleIds = session.getRoleIds();

      // no RoleMapper: allow, consistent with the core posture (D10)
      return roleIds
          .map(
              ids ->
                  ids.contains(NodeIds.WellKnownRole_SecurityKeyServerAdmin)
                      ? StatusCode.GOOD
                      : DENIED)
          .orElse(StatusCode.GOOD);
    }

    @Override
    public StatusCode checkKeyAccess(Session session, String securityGroupId) {
      // getRoleIds() is recomputed per call; call it exactly once per decision
      Optional<List<NodeId>> maybeRoleIds = session.getRoleIds();

      @Nullable SecurityGroupConfig group = groupLookup.apply(securityGroupId);
      List<RolePermissionType> rolePermissions =
          group != null ? group.getRolePermissions() : List.of();

      if (maybeRoleIds.isEmpty()) {
        // no RoleMapper configured: allow encrypted callers, but fail closed on groups
        // carrying explicit restrictions; unknown groups are allowed through so the
        // handler's existence check can return Bad_NotFound
        return rolePermissions.isEmpty() ? StatusCode.GOOD : DENIED;
      }

      // a RoleMapper returning an empty list is still "mapper configured": the role rows
      // apply and deny
      List<NodeId> roleIds = maybeRoleIds.get();

      if (!rolePermissions.isEmpty()) {
        boolean allowed =
            rolePermissions.stream()
                .anyMatch(rp -> roleIds.contains(rp.getRoleId()) && canCall(rp));

        return allowed ? StatusCode.GOOD : DENIED;
      }

      // no explicit per-group restrictions, or the group is unknown: require the default
      // pull roles; unauthorized callers cannot distinguish unknown from denied groups
      boolean allowed =
          roleIds.contains(NodeIds.WellKnownRole_SecurityKeyServerAccess)
              || roleIds.contains(NodeIds.WellKnownRole_SecurityKeyServerAdmin);

      return allowed ? StatusCode.GOOD : DENIED;
    }

    private static boolean canCall(RolePermissionType rolePermission) {
      PermissionType permissions = rolePermission.getPermissions();
      return permissions != null && permissions.getCall();
    }
  }
}
