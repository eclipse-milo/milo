/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki.snippets;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient;
import org.eclipse.milo.opcua.sdk.client.gds.TrustListApplier;
import org.eclipse.milo.opcua.sdk.client.gds.TrustListReader;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectVerificationResult;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseHelloVerifier;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectAttemptEvent;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetListener;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.WiresharkKeyLogWriter;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Samples from the Wiki's Reverse Connect, alias name, GDS, troubleshooting, and diagnostics pages.
 */
public final class FeatureSnippets {

  private static final Logger LOGGER = LoggerFactory.getLogger(FeatureSnippets.class);

  private FeatureSnippets() {}

  // snippet:reverse-listener:start
  static ReverseConnectManager startReverseListener(
      InetSocketAddress bindAddress, String expectedServerUri) throws Exception {
    // ServerUri is an unauthenticated hint. This check only filters, and the
    // SecureChannel and Session handshakes still validate the server.
    ReverseHelloVerifier verifier =
        candidate -> {
          if (expectedServerUri.equals(candidate.serverUri())) {
            return ReverseConnectVerificationResult.accept();
          }
          return ReverseConnectVerificationResult.reject("Unexpected ServerUri");
        };

    ReverseConnectManager manager =
        ReverseConnectManager.builder()
            .addBindAddress(bindAddress)
            .setReverseHelloVerifier(verifier)
            .build();

    manager.startup();
    return manager;
  }

  // snippet:reverse-listener:end

  // snippet:reverse-client:start
  static OpcUaClient connectReverse(ReverseConnectManager manager, OpcUaClientConfig config)
      throws Exception {
    EndpointDescription endpoint = config.getEndpoint();
    String serverUri = endpoint.getServer().getApplicationUri();
    String endpointUrl = endpoint.getEndpointUrl();

    ReverseConnectSelector selector =
        ReverseConnectSelector.byServerUriAndEndpointUrl(serverUri, endpointUrl);
    OpcUaClient client = OpcUaClient.createReverseConnect(config, manager, selector);

    try {
      client.connectAsync().get(60, TimeUnit.SECONDS);
    } catch (Exception e) {
      // Disconnect removes the selector, so a server that arrives later is not claimed.
      try {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      } catch (Exception disconnectFailure) {
        e.addSuppressed(disconnectFailure);
      }
      throw e;
    }
    return client;
  }

  // snippet:reverse-client:end

  // snippet:reverse-target:start
  static ReverseConnectTargetHandle addClientTarget(
      OpcUaServer server, String clientListenerUrl, String endpointUrl) {
    ReverseConnectTarget target =
        ReverseConnectTarget.builder()
            .setClientListenerUrl(clientListenerUrl)
            .setEndpointUrl(endpointUrl)
            .setConnectTimeout(uint(5_000))
            .setRegistrationPeriod(uint(30_000))
            .build();

    // Registration only. The target manager makes and retries the connection attempts.
    return server.addReverseConnectTarget(target);
  }

  // snippet:reverse-target:end

  // snippet:reverse-target-events:start
  static void logReverseConnectAttempts(OpcUaServer server) {
    server.addReverseConnectTargetListener(
        new ReverseConnectTargetListener() {
          @Override
          public void onAttemptEvent(ReverseConnectAttemptEvent event) {
            LOGGER.info(
                "Reverse Connect target {} attempt {}: {} {}",
                event.targetId(),
                event.attemptNumber(),
                event.state(),
                event.statusCode());
          }
        });
  }

  // snippet:reverse-target-events:end

  // snippet:alias-start:start
  static AliasManager startAliasManager(OpcUaServer server, UShort namespaceIndex) {
    AliasManagerConfig config =
        AliasManagerConfig.builder().nodeNamespaceIndex(namespaceIndex).build();

    // Call this after server.startup() completes, and shut the manager down before the server.
    var aliases = new AliasManager(server, config);
    aliases.startup();
    return aliases;
  }

  // snippet:alias-start:end

  // snippet:alias_publish:start
  static NodeId publishSetpointAlias(AliasManager aliases, NodeId setpointId) throws UaException {
    // A null serverUri selects the local server.
    var target = new AliasTarget(setpointId.expanded(), null, NodeIds.AliasFor);

    return aliases.addAlias(NodeIds.TagVariables, "Thermostat.Setpoint", List.of(target));
  }

  // snippet:alias_publish:end

  // snippet:alias_find:start
  static List<AliasNameDataType> findAliases(OpcUaClient client, String pattern)
      throws UaException {
    // A null reference type filter matches aliases of any reference type.
    Variant[] inputs = {new Variant(pattern), new Variant(NodeId.NULL_VALUE)};
    var request = new CallMethodRequest(NodeIds.Aliases, NodeIds.Aliases_FindAlias, inputs);

    CallResponse response = client.call(List.of(request));
    CallMethodResult result = requireNonNull(response.getResults())[0];
    if (!result.getStatusCode().isGood()) {
      throw new UaException(result.getStatusCode());
    }

    // The only output is an array of encoded AliasNameDataType structures.
    Variant[] outputs = requireNonNull(result.getOutputArguments());
    ExtensionObject[] encoded = (ExtensionObject[]) outputs[0].value();

    List<AliasNameDataType> aliases = new ArrayList<>();
    if (encoded != null) {
      for (ExtensionObject entry : encoded) {
        aliases.add((AliasNameDataType) entry.decode(client.getStaticEncodingContext()));
      }
    }
    return aliases;
  }

  // snippet:alias_find:end

  // snippet:alias-read-target:start
  static DataValue readFirstLocalTarget(OpcUaClient client, AliasNameDataType alias)
      throws UaException {
    ExpandedNodeId[] targets = requireNonNull(alias.getReferencedNodes());

    for (ExpandedNodeId target : targets) {
      // Empty for a remote target or a namespace the client's table does not contain.
      Optional<NodeId> localId = target.toNodeId(client.getNamespaceTable());
      if (localId.isPresent()) {
        return client.readValue(0.0, TimestampsToReturn.Both, localId.get());
      }
    }

    String aliasName = alias.getAliasName().name();
    throw new UaException(StatusCodes.Bad_NotFound, "No local target for " + aliasName);
  }

  // snippet:alias-read-target:end

  // snippet:gds_registration:start
  static NodeId findOrRegister(OpcUaClient client, ApplicationRecordDataType record)
      throws UaException {
    GdsClient gds = GdsClient.create(client);
    ApplicationRecordDataType[] found = gds.findApplications(record.getApplicationUri());

    if (found.length == 0) {
      return gds.registerApplication(record);
    } else if (found.length == 1) {
      return found[0].getApplicationId();
    } else {
      throw new IllegalStateException("Ambiguous application URI");
    }
  }

  // snippet:gds_registration:end

  // snippet:gds-start-request:start
  static NodeId startCertificateRequest(
      GdsClient gds, NodeId applicationId, NodeId groupId, ByteString csr) throws UaException {
    // Null asks for the group's default type when it advertises only compatible ancestors.
    NodeId requestType =
        gds.resolveCertificateTypeId(groupId, NodeIds.RsaSha256ApplicationCertificateType);

    return gds.startSigningRequest(applicationId, groupId, requestType, csr);
  }

  // snippet:gds-start-request:end

  // snippet:gds-poll:start
  static Optional<List<X509Certificate>> pollIssuedChain(
      GdsClient gds, NodeId applicationId, NodeId requestId) throws UaException {
    GdsClient.FinishRequestResult issued;
    try {
      issued = gds.finishRequest(applicationId, requestId);
    } catch (UaMethodException e) {
      if (e.getStatusCode().value() == StatusCodes.Bad_NothingToDo) {
        // Still pending. Keep the RequestId and poll again later.
        return Optional.empty();
      }
      throw e;
    }

    // The issued certificate comes first, followed by the issuers the GDS returned.
    List<X509Certificate> chain = new ArrayList<>();
    chain.add(CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty()));
    for (ByteString issuer : issued.issuerCertificates()) {
      chain.add(CertificateUtil.decodeCertificate(issuer.bytesOrEmpty()));
    }
    return Optional.of(chain);
  }

  // snippet:gds-poll:end

  // snippet:gds-install:start
  static void installIssuedChain(
      List<X509Certificate> chain,
      KeyPair keyPair,
      String applicationUri,
      X509Certificate trustedCa,
      CertificateGroup group)
      throws Exception {
    X509Certificate certificate = chain.get(0);
    GdsClient.verifyIssuedCertificate(certificate, keyPair.getPublic(), applicationUri);
    certificate.checkValidity();

    // Each certificate must be signed by the next one in the chain.
    for (int i = 0; i < chain.size() - 1; i++) {
      chain.get(i).verify(chain.get(i + 1).getPublicKey());
    }

    // The last certificate must be signed by a CA the application already trusts.
    X509Certificate last = chain.get(chain.size() - 1);
    last.verify(trustedCa.getPublicKey());

    X509Certificate[] certificates = chain.toArray(X509Certificate[]::new);
    group.updateCertificate(NodeIds.RsaSha256ApplicationCertificateType, keyPair, certificates);
  }

  // snippet:gds-install:end

  // snippet:gds_trust:start
  static void pullTrustList(
      OpcUaClient client, NodeId applicationId, NodeId groupId, TrustListManager trust)
      throws UaException {
    GdsClient gds = GdsClient.create(client);
    NodeId trustListId = gds.getTrustList(applicationId, groupId);
    TrustListDataType update = TrustListReader.read(client, trustListId);
    TrustListApplier.apply(update, trust);
  }

  // snippet:gds_trust:end

  // snippet:describe-failure:start
  static String describeFailure(Throwable failure) {
    Optional<StatusCode> status = UaException.extractStatusCode(failure);
    if (status.isEmpty()) {
      return "no StatusCode in the cause chain";
    }

    long code = status.get().getValue();
    Optional<String[]> nameAndDescription = StatusCodes.lookup(code);
    if (nameAndDescription.isEmpty()) {
      return String.format("unknown StatusCode 0x%08X", code);
    }

    String name = nameAndDescription.get()[0];
    String description = nameAndDescription.get()[1];
    return String.format("%s (0x%08X): %s", name, code, description);
  }

  // snippet:describe-failure:end

  // snippet:diagnostics-enable:start
  static void setDiagnosticsEnabled(OpcUaServer server, boolean enabled) {
    AddressSpaceManager addressSpace = server.getAddressSpaceManager();
    UaNode node = addressSpace.getManagedNode(NodeIds.Server_ServerDiagnostics).orElseThrow();
    var diagnostics = (ServerDiagnosticsTypeNode) node;

    diagnostics.setEnabledFlag(enabled);
  }

  // snippet:diagnostics-enable:end

  // snippet:diagnostics-read:start
  static UInteger readSessionCount(OpcUaClient client) throws UaException {
    NodeId countId = NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount;
    DataValue count = client.readValue(0, TimestampsToReturn.Neither, countId);

    StatusCode status = count.statusCode();
    if (!status.isGood()) {
      throw new UaException(status);
    }

    return (UInteger) count.value().value();
  }

  // snippet:diagnostics-read:end

  // snippet:key-log:start
  static DataValue readWithKeyLog(
      Path keyLogPath,
      String discoveryUrl,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      CertificateValidator validator)
      throws Exception {
    Function<List<EndpointDescription>, Optional<EndpointDescription>> selectEncrypted =
        endpoints ->
            endpoints.stream()
                .filter(
                    e -> SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
                .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                .findFirst();

    try (var writer = new WiresharkKeyLogWriter(keyLogPath)) {
      OpcUaClient client =
          OpcUaClient.create(
              discoveryUrl,
              selectEncrypted,
              transport -> {},
              config ->
                  config
                      .setCertificateIdentity(keyPair, certificateChain)
                      .setCertificateValidator(validator)
                      .setSecurityKeysListener(writer));
      try {
        client.connectAsync().get(10, TimeUnit.SECONDS);
        return client.readValue(
            0, TimestampsToReturn.Both, NodeIds.Server_ServerStatus_CurrentTime);
      } finally {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      }
    }
  }
  // snippet:key-log:end
}
