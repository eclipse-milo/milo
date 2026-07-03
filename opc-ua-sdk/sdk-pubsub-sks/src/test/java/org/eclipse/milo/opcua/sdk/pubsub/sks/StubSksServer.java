/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultApplicationGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.RsaSha256CertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;

/**
 * An embedded stub SKS for integration tests: a real {@link OpcUaServer} with a SignAndEncrypt
 * endpoint and a canned {@code GetSecurityKeys} handler attached to the well-known ns=0 method
 * node.
 */
final class StubSksServer implements AutoCloseable {

  static final String APPLICATION_URI = "urn:eclipse:milo:test:stub-sks";
  static final String SECURITY_GROUP_ID = "TestGroup";
  static final String DENIED_GROUP_ID = "DeniedGroup";

  static final String USERNAME = "user1";
  static final String PASSWORD = "password1";

  static final String KEY_SET_POLICY_URI =
      "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes256-CTR";
  static final int KEY_DATA_LENGTH = 68;
  static final double TIME_TO_NEXT_KEY_MILLIS = 30_000.0;
  static final double KEY_LIFETIME_MILLIS = 60_000.0;

  private final OpcUaServer server;
  private final X509Certificate certificate;
  private final int port;
  private final AtomicInteger invocationCount = new AtomicInteger();

  private StubSksServer(OpcUaServer server, X509Certificate certificate, int port) {
    this.server = server;
    this.certificate = certificate;
    this.port = port;
  }

  X509Certificate getCertificate() {
    return certificate;
  }

  String getDiscoveryUrl() {
    return "opc.tcp://localhost:" + port + "/sks";
  }

  AtomicInteger getInvocationCount() {
    return invocationCount;
  }

  @Override
  public void close() throws Exception {
    server.shutdown().get();
  }

  static StubSksServer create(int port, X509Certificate trustedClientCertificate) throws Exception {

    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("Milo Stub SKS")
            .setApplicationUri(APPLICATION_URI)
            .addDnsName("localhost")
            .build();

    var trustListManager = new MemoryTrustListManager();
    trustListManager.addTrustedCertificate(trustedClientCertificate);

    var certificateQuarantine = new MemoryCertificateQuarantine();

    var certificateFactory =
        new RsaSha256CertificateFactory() {
          @Override
          protected KeyPair createRsaSha256KeyPair() {
            return keyPair;
          }

          @Override
          protected X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair) {
            return new X509Certificate[] {certificate};
          }
        };

    var certificateValidator =
        new DefaultServerCertificateValidator(trustListManager, certificateQuarantine);

    var defaultGroup =
        DefaultApplicationGroup.createAndInitialize(
            trustListManager,
            new MemoryCertificateStore(),
            certificateFactory,
            certificateValidator);

    var certificateManager = new DefaultCertificateManager(certificateQuarantine, defaultGroup);

    var usernameIdentityValidator =
        new UsernameIdentityValidator(
            authChallenge ->
                USERNAME.equals(authChallenge.getUsername())
                    && PASSWORD.equals(authChallenge.getPassword()));

    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri(APPLICATION_URI)
            .setApplicationName(LocalizedText.english("Milo Stub SKS"))
            .setProductUri("urn:eclipse:milo:test:stub-sks:product")
            .setEndpoints(endpointConfigs(certificate, port))
            .setBuildInfo(
                new BuildInfo(
                    "urn:eclipse:milo:test:stub-sks:product",
                    "eclipse",
                    "milo stub sks",
                    OpcUaServer.SDK_VERSION,
                    "",
                    DateTime.now()))
            .setCertificateManager(certificateManager)
            .setIdentityValidator(
                new CompositeValidator(
                    AnonymousIdentityValidator.INSTANCE, usernameIdentityValidator))
            .build();

    var server =
        new OpcUaServer(
            serverConfig,
            transportProfile -> {
              if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
                return new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build());
              } else {
                throw new RuntimeException("unexpected TransportProfile: " + transportProfile);
              }
            });

    server.startup().get();

    var stub = new StubSksServer(server, certificate, port);
    stub.installGetSecurityKeysHandler();
    return stub;
  }

  private static Set<EndpointConfig> endpointConfigs(X509Certificate certificate, int port) {
    EndpointConfig.Builder base =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setBindPort(port)
            .setHostname("localhost")
            .setPath("/sks")
            .setCertificate(certificate)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .addTokenPolicies(
                OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS,
                OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME);

    EndpointConfig none =
        base.copy()
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .build();

    EndpointConfig signAndEncrypt =
        base.copy()
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build();

    return Set.of(none, signAndEncrypt);
  }

  private void installGetSecurityKeysHandler() {
    UaNode node =
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.PublishSubscribe_GetSecurityKeys)
            .orElseThrow(() -> new IllegalStateException("GetSecurityKeys node not found in ns=0"));

    UaMethodNode methodNode = (UaMethodNode) node;
    methodNode.setExecutable(true);
    methodNode.setUserExecutable(true);
    methodNode.setInvocationHandler(new GetSecurityKeysStubHandler(methodNode, invocationCount));
  }

  private static final class GetSecurityKeysStubHandler extends AbstractMethodInvocationHandler {

    private final AtomicInteger invocationCount;

    private GetSecurityKeysStubHandler(UaMethodNode node, AtomicInteger invocationCount) {
      super(node);
      this.invocationCount = invocationCount;
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {
        new Argument(
            "SecurityGroupId", NodeIds.String, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE),
        new Argument(
            "StartingTokenId", NodeIds.UInt32, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE),
        new Argument(
            "RequestedKeyCount", NodeIds.UInt32, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE)
      };
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[] {
        new Argument(
            "SecurityPolicyUri", NodeIds.String, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE),
        new Argument(
            "FirstTokenId", NodeIds.UInt32, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE),
        new Argument(
            "Keys",
            NodeIds.ByteString,
            ValueRanks.OneDimension,
            new UInteger[] {uint(0)},
            LocalizedText.NULL_VALUE),
        new Argument(
            "TimeToNextKey", NodeIds.Double, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE),
        new Argument(
            "KeyLifetime", NodeIds.Double, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE)
      };
    }

    @Override
    protected Variant[] invoke(InvocationContext invocationContext, Variant[] inputValues)
        throws UaException {

      invocationCount.incrementAndGet();

      // Part 14 posture: GetSecurityKeys requires an encrypted channel. Enforcing it here means
      // every successful pull in these tests proves the provider selected the SignAndEncrypt
      // endpoint over the None endpoint offered at the same discovery URL.
      EndpointDescription sessionEndpoint =
          invocationContext.getSession().map(Session::getEndpoint).orElse(null);
      if (sessionEndpoint == null
          || sessionEndpoint.getSecurityMode() != MessageSecurityMode.SignAndEncrypt) {
        throw new UaException(StatusCodes.Bad_SecurityModeInsufficient);
      }

      String securityGroupId = (String) inputValues[0].value();

      if (DENIED_GROUP_ID.equals(securityGroupId)) {
        throw new UaException(StatusCodes.Bad_UserAccessDenied);
      }
      if (!SECURITY_GROUP_ID.equals(securityGroupId)) {
        throw new UaException(StatusCodes.Bad_NotFound);
      }

      var keyData = new byte[KEY_DATA_LENGTH];
      for (int i = 0; i < keyData.length; i++) {
        keyData[i] = (byte) i;
      }

      return new Variant[] {
        Variant.ofString(KEY_SET_POLICY_URI),
        Variant.ofUInt32(uint(1)),
        Variant.ofByteStringArray(new ByteString[] {ByteString.of(keyData)}),
        Variant.ofDouble(TIME_TO_NEXT_KEY_MILLIS),
        Variant.ofDouble(KEY_LIFETIME_MILLIS)
      };
    }
  }
}
