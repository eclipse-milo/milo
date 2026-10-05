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
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient;
import org.eclipse.milo.opcua.sdk.client.gds.TrustListApplier;
import org.eclipse.milo.opcua.sdk.client.gds.TrustListReader;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.WiresharkKeyLogWriter;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

/** Samples from the Wiki's Reverse Connect, alias name, GDS, and diagnostics pages. */
public final class FeatureSnippets {

  private FeatureSnippets() {}

  // snippet:reverse:start
  static DataValue reverseRead(OpcUaServer server, OpcUaClientConfig config) throws Exception {
    try (var manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
            .build()) {
      manager.startup();

      // The listener binds an ephemeral port. The server needs its URL to connect.
      var listenerAddress =
          (InetSocketAddress) manager.snapshot().listeners().get(0).boundAddress();
      String listenerUrl = "opc.tcp://127.0.0.1:" + listenerAddress.getPort();

      String endpointUrl = config.getEndpoint().getEndpointUrl();
      String serverUri = config.getEndpoint().getServer().getApplicationUri();
      OpcUaClient client =
          OpcUaClient.createReverseConnect(
              config,
              manager,
              ReverseConnectSelector.byServerUriAndEndpointUrl(serverUri, endpointUrl));

      ReverseConnectTargetHandle target =
          server.addReverseConnectTarget(
              ReverseConnectTarget.builder()
                  .setClientListenerUrl(listenerUrl)
                  .setEndpointUrl(endpointUrl)
                  .setRegistrationPeriod(uint(1_000))
                  .setConnectTimeout(uint(5_000))
                  .build());

      try {
        client.connectAsync().get(10, TimeUnit.SECONDS);
        DataValue value =
            client.readValue(0.0, TimestampsToReturn.Both, NodeIds.Server_ServerStatus_CurrentTime);
        if (!value.statusCode().isGood()) {
          throw new UaException(value.statusCode());
        }
        return value;
      } finally {
        try {
          client.disconnectAsync().get(10, TimeUnit.SECONDS);
        } finally {
          target.remove().get(10, TimeUnit.SECONDS);
        }
      }
    }
  }

  // snippet:reverse:end

  // snippet:alias_publish:start
  static NodeId publishCounterAlias(AliasManager aliases, NodeId targetId) throws UaException {
    return aliases.addAlias(
        NodeIds.TagVariables,
        "Demo.Counter",
        List.of(new AliasTarget(targetId.expanded(), null, NodeIds.AliasFor)));
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

  // snippet:gds_signing:start
  static X509Certificate requestAndInstall(
      GdsClient gds,
      NodeId applicationId,
      NodeId groupId,
      KeyPair keyPair,
      String applicationUri,
      ByteString csr,
      X509Certificate issuingCa,
      DefaultCertificateGroup group)
      throws Exception {
    NodeId requestType =
        gds.resolveCertificateTypeId(groupId, NodeIds.RsaSha256ApplicationCertificateType);
    NodeId requestId = gds.startSigningRequest(applicationId, groupId, requestType, csr);

    GdsClient.FinishRequestResult issued;
    try {
      issued = gds.finishRequest(applicationId, requestId);
    } catch (UaMethodException e) {
      if (e.getStatusCode().value() != StatusCodes.Bad_NothingToDo) {
        throw e;
      }
      // Bad_NothingToDo means the request is still pending. This example retries once;
      // production code persists the RequestId and polls on a schedule.
      issued = gds.finishRequest(applicationId, requestId);
    }

    X509Certificate certificate =
        CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty());
    GdsClient.verifyIssuedCertificate(certificate, keyPair.getPublic(), applicationUri);
    certificate.checkValidity();
    certificate.verify(issuingCa.getPublicKey());

    group.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType,
        keyPair,
        new X509Certificate[] {certificate, issuingCa});
    return certificate;
  }

  // snippet:gds_signing:end

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

  // snippet:diagnostics-enable:start
  static UInteger enableAndReadSessionCount(OpcUaServer server, OpcUaClient client)
      throws UaException {
    var diagnostics =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    diagnostics.setEnabledFlag(true);
    DataValue count =
        client.readValue(
            0,
            TimestampsToReturn.Neither,
            NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount);
    if (!count.statusCode().isGood()) {
      throw new UaException(count.statusCode());
    }
    return (UInteger) count.value().value();
  }

  // snippet:diagnostics-enable:end

  // snippet:key-log:start
  static DataValue readWithKeyLog(
      Path keyLogPath,
      String discoveryUrl,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      CertificateValidator validator)
      throws Exception {
    try (var writer = new WiresharkKeyLogWriter(keyLogPath)) {
      OpcUaClient client =
          OpcUaClient.create(
              discoveryUrl,
              endpoints ->
                  endpoints.stream()
                      .filter(
                          e ->
                              SecurityPolicy.Basic256Sha256.getUri()
                                  .equals(e.getSecurityPolicyUri()))
                      .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                      .findFirst(),
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
