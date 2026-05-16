/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.EndpointCertificateConfig;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfigBuilder;
import org.junit.jupiter.api.Test;

/**
 * End-to-end ECC session coverage through the Milo public client/server APIs.
 *
 * <p>These tests deliberately sit above the helper-unit tests: they prove that endpoint
 * advertisement, SecureChannel issue/renew, CreateSession additional headers, ActivateSession
 * signatures, user-token encryption, and basic service calls all agree across SDK and stack module
 * boundaries.
 */
class EccSessionIntegrationTest {

  private static final String SERVER_URI = "urn:eclipse:milo:ecc-session-test:server";
  private static final String CLIENT_URI = "urn:eclipse:milo:ecc-session-test:client";
  private static final String USERNAME = "user1";
  private static final String PASSWORD = "password";

  // Anonymous activation still exercises CreateSession/ActivateSession over the ECC SecureChannel.
  @Test
  void activatesAnonymousSessionAndReadsWritesWithNistP256AesGcm() throws Exception {
    activatesAnonymousSessionAndReadsWrites(
        SecurityPolicy.ECC_nistP256_AesGcm, NodeIds.EccNistP256ApplicationCertificateType);
  }

  // The Curve25519 profile uses Ed25519, X25519, and ChaCha20-Poly1305 paths that the NIST case
  // does not cover.
  @Test
  void activatesAnonymousSessionAndReadsWritesWithCurve25519ChaChaPoly() throws Exception {
    activatesAnonymousSessionAndReadsWrites(
        SecurityPolicy.ECC_curve25519_ChaChaPoly, NodeIds.EccCurve25519ApplicationCertificateType);
  }

  private static void activatesAnonymousSessionAndReadsWrites(
      SecurityPolicy securityPolicy, NodeId certificateTypeId) throws Exception {

    try (RunningServer running = startSecureServer(securityPolicy, certificateTypeId, b -> {})) {
      CertificateManager clientCertificateManager =
          certificateManager(certificate(certificateTypeId, "client", CLIENT_URI));
      OpcUaClient client =
          connectClient(
              running,
              securityPolicy,
              AnonymousProvider.INSTANCE,
              clientCertificateManager,
              b -> {});

      try {
        readWriteTestNode(client, running.namespaceIndex());
      } finally {
        disconnectQuietly(client);
      }
    }
  }

  // Username activation proves the CreateSession additional header and EccEncryptedSecret path.
  @Test
  void activatesUsernameSessionAndReadsWritesWithNistP256AesGcm() throws Exception {
    activatesUsernameSessionAndReadsWrites(
        SecurityPolicy.ECC_nistP256_AesGcm, NodeIds.EccNistP256ApplicationCertificateType);
  }

  // Username-token encryption must work with the Curve25519 receiver-key and AEAD primitives too.
  @Test
  void activatesUsernameSessionAndReadsWritesWithCurve25519ChaChaPoly() throws Exception {
    activatesUsernameSessionAndReadsWrites(
        SecurityPolicy.ECC_curve25519_ChaChaPoly, NodeIds.EccCurve25519ApplicationCertificateType);
  }

  private static void activatesUsernameSessionAndReadsWrites(
      SecurityPolicy securityPolicy, NodeId certificateTypeId) throws Exception {

    try (RunningServer running = startSecureServer(securityPolicy, certificateTypeId, b -> {})) {
      CertificateManager clientCertificateManager =
          certificateManager(certificate(certificateTypeId, "client", CLIENT_URI));
      OpcUaClient client =
          connectClient(
              running,
              securityPolicy,
              new UsernameProvider(USERNAME, PASSWORD),
              clientCertificateManager,
              b -> {});

      try {
        readWriteTestNode(client, running.namespaceIndex());
      } finally {
        disconnectQuietly(client);
      }
    }
  }

  // Renewal must keep an already activated session usable after a second ECC key exchange.
  @Test
  void renewsSecureChannelWithActiveSessionWithNistP256AesGcm() throws Exception {
    renewsSecureChannelWithActiveSession(
        SecurityPolicy.ECC_nistP256_AesGcm, NodeIds.EccNistP256ApplicationCertificateType);
  }

  // Renewal is profile-sensitive because nonces, sequence numbers, and AEAD keys are all
  // profile-specific.
  @Test
  void renewsSecureChannelWithActiveSessionWithCurve25519ChaChaPoly() throws Exception {
    renewsSecureChannelWithActiveSession(
        SecurityPolicy.ECC_curve25519_ChaChaPoly, NodeIds.EccCurve25519ApplicationCertificateType);
  }

  private static void renewsSecureChannelWithActiveSession(
      SecurityPolicy securityPolicy, NodeId certificateTypeId) throws Exception {

    CountDownLatch clientKeysCreated = new CountDownLatch(2);

    try (RunningServer running =
        startSecureServer(
            securityPolicy,
            certificateTypeId,
            b ->
                b.setMinimumSecureChannelLifetime(uint(300))
                    .setMaximumSecureChannelLifetime(uint(300)))) {

      CertificateManager clientCertificateManager =
          certificateManager(certificate(certificateTypeId, "client", CLIENT_URI));
      OpcUaClient client =
          connectClient(
              running,
              securityPolicy,
              clientCertificateManager,
              b -> b.setChannelLifetime(uint(300)),
              b -> b.setSecurityKeysListener(keyset -> clientKeysCreated.countDown()));

      try {
        assertTrue(
            clientKeysCreated.await(5, TimeUnit.SECONDS),
            "client did not observe SecureChannel renewal");
        readWriteTestNode(client, running.namespaceIndex());
      } finally {
        disconnectQuietly(client);
      }
    }
  }

  // Reconnection reactivates the existing Session on a new SecureChannel; the ActivateSession
  // signature must be verified against the new channel thumbprint, not the stale channel.
  @Test
  void reactivatesUsernameSessionOnNewSecureChannelWithNistP256AesGcm() throws Exception {
    reactivatesUsernameSessionOnNewSecureChannel(
        SecurityPolicy.ECC_nistP256_AesGcm, NodeIds.EccNistP256ApplicationCertificateType);
  }

  // The Curve25519 reactivation path protects against accidentally hard-coding NIST-only session
  // signature or token-secret behavior.
  @Test
  void reactivatesUsernameSessionOnNewSecureChannelWithCurve25519ChaChaPoly() throws Exception {
    reactivatesUsernameSessionOnNewSecureChannel(
        SecurityPolicy.ECC_curve25519_ChaChaPoly, NodeIds.EccCurve25519ApplicationCertificateType);
  }

  private static void reactivatesUsernameSessionOnNewSecureChannel(
      SecurityPolicy securityPolicy, NodeId certificateTypeId) throws Exception {

    CountDownLatch sessionActive = new CountDownLatch(2);

    try (RunningServer running = startSecureServer(securityPolicy, certificateTypeId, b -> {})) {
      CertificateManager clientCertificateManager =
          certificateManager(certificate(certificateTypeId, "client", CLIENT_URI));
      OpcUaClient client =
          createClient(
              running,
              securityPolicy,
              new UsernameProvider(USERNAME, PASSWORD),
              clientCertificateManager,
              b -> {},
              b -> {});
      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              sessionActive.countDown();
            }
          });

      try {
        client.connect();
        closeActiveTcpChannel(client);

        assertTrue(
            sessionActive.await(10, TimeUnit.SECONDS),
            "session did not reactivate after channel drop");
        readWriteTestNode(client, running.namespaceIndex());
      } finally {
        disconnectQuietly(client);
      }
    }
  }

  // Malformed CreateSession additional-header data must be rejected by the server session layer.
  @Test
  void rejectsMalformedCreateSessionAdditionalHeader() throws Exception {
    SecurityPolicy securityPolicy = SecurityPolicy.ECC_nistP256_AesGcm;
    NodeId certificateTypeId = NodeIds.EccNistP256ApplicationCertificateType;

    try (RunningServer running = startSecureServer(securityPolicy, certificateTypeId, b -> {})) {
      CertificateManager clientCertificateManager =
          certificateManager(certificate(certificateTypeId, "client", CLIENT_URI));
      OpcUaClient client =
          createClient(
              running,
              securityPolicy,
              new MalformedHeaderUsernameProvider(),
              clientCertificateManager,
              b -> {},
              b -> {});

      try {
        UaException exception = assertThrows(UaException.class, client::connect);

        assertEquals(StatusCodes.Bad_DecodingError, exception.getStatusCode().getValue());
      } finally {
        disconnectQuietly(client);
      }
    }
  }

  // None endpoints using ECC username-token encryption must advertise a certificate for the signed
  // receiver key; otherwise the client cannot anchor the password encryption target.
  @Test
  void noneEndpointWithEccUsernameTokenRequiresCertificate() throws Exception {
    SecurityPolicy tokenPolicy = SecurityPolicy.ECC_nistP256_AesGcm;

    try (RunningServer running = startNoneEndpointWithoutCertificate(tokenPolicy)) {
      CertificateManager clientCertificateManager =
          certificateManager(
              certificate(NodeIds.EccNistP256ApplicationCertificateType, "client", CLIENT_URI));
      OpcUaClient client =
          createClient(
              running,
              SecurityPolicy.None,
              new UsernameProvider(USERNAME, PASSWORD),
              clientCertificateManager,
              b -> {},
              b -> {});

      try {
        UaException exception = assertThrows(UaException.class, client::connect);

        assertEquals(StatusCodes.Bad_ConfigurationError, exception.getStatusCode().getValue());
      } finally {
        disconnectQuietly(client);
      }
    }
  }

  private static OpcUaClient connectClient(
      RunningServer running,
      SecurityPolicy securityPolicy,
      IdentityProvider identityProvider,
      CertificateManager clientCertificateManager,
      java.util.function.Consumer<OpcTcpClientTransportConfigBuilder> configureTransport)
      throws UaException {

    OpcUaClient client =
        createClient(
            running,
            securityPolicy,
            identityProvider,
            clientCertificateManager,
            configureTransport,
            b -> {});
    client.connect();

    return client;
  }

  private static OpcUaClient connectClient(
      RunningServer running,
      SecurityPolicy securityPolicy,
      CertificateManager clientCertificateManager,
      java.util.function.Consumer<OpcTcpClientTransportConfigBuilder> configureTransport,
      java.util.function.Consumer<OpcUaClientConfigBuilder> configureClient)
      throws UaException {

    OpcUaClient client =
        createClient(
            running,
            securityPolicy,
            AnonymousProvider.INSTANCE,
            clientCertificateManager,
            configureTransport,
            configureClient);
    client.connect();

    return client;
  }

  private static OpcUaClient createClient(
      RunningServer running,
      SecurityPolicy securityPolicy,
      IdentityProvider identityProvider,
      CertificateManager clientCertificateManager,
      java.util.function.Consumer<OpcTcpClientTransportConfigBuilder> configureTransport,
      java.util.function.Consumer<OpcUaClientConfigBuilder> configureClient)
      throws UaException {

    return OpcUaClient.create(
        running.endpointUrl(),
        endpoints ->
            endpoints.stream()
                .filter(e -> securityPolicy.getUri().equals(e.getSecurityPolicyUri()))
                .filter(
                    e -> {
                      String endpointUrl = e.getEndpointUrl();
                      return endpointUrl == null || !endpointUrl.endsWith("/discovery");
                    })
                .findFirst(),
        configureTransport,
        b -> {
          b.setApplicationName(LocalizedText.english("Eclipse Milo ECC test client"))
              .setApplicationUri(CLIENT_URI)
              .setRequestTimeout(uint(5_000))
              .setCertificateManager(clientCertificateManager)
              .setIdentityProvider(identityProvider);

          configureClient.accept(b);
        });
  }

  private static RunningServer startSecureServer(
      SecurityPolicy securityPolicy,
      NodeId certificateTypeId,
      java.util.function.Consumer<OpcTcpServerTransportConfigBuilder> configureTransport)
      throws Exception {

    int port = freePort();
    CertificateMaterial serverCertificate = certificate(certificateTypeId, "server", SERVER_URI);
    CertificateManager certificateManager = certificateManager(serverCertificate);
    EndpointConfig secureEndpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/ecc")
            .setEndpointCertificateConfig(
                EndpointCertificateConfig.newBuilder()
                    .setCertificateTypeId(certificateTypeId)
                    .build())
            .setSecurityPolicy(securityPolicy)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .addTokenPolicies(anonymousPolicy(), usernamePolicy(securityPolicy))
            .build();
    EndpointConfig discoveryEndpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/ecc/discovery")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .addTokenPolicy(anonymousPolicy())
            .build();

    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    endpoints.add(secureEndpoint);
    endpoints.add(discoveryEndpoint);

    return startServer(endpoints, certificateManager, configureTransport);
  }

  private static RunningServer startNoneEndpointWithoutCertificate(SecurityPolicy tokenPolicy)
      throws Exception {

    int port = freePort();
    CertificateManager certificateManager =
        certificateManager(
            certificate(
                NodeIds.EccNistP256ApplicationCertificateType, "unused-server", SERVER_URI));
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/ecc")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .addTokenPolicy(usernamePolicy(tokenPolicy))
            .build();

    return startServer(Set.of(endpoint), certificateManager, b -> {});
  }

  private static RunningServer startServer(
      Set<EndpointConfig> endpoints,
      CertificateManager certificateManager,
      java.util.function.Consumer<OpcTcpServerTransportConfigBuilder> configureTransport)
      throws Exception {

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri(SERVER_URI)
            .setApplicationName(LocalizedText.english("Eclipse Milo ECC test server"))
            .setProductUri("urn:eclipse:milo:ecc-session-test")
            .setEndpoints(endpoints)
            .setCertificateManager(certificateManager)
            .setIdentityValidator(
                new CompositeValidator(
                    AnonymousIdentityValidator.INSTANCE,
                    new UsernameIdentityValidator(
                        auth ->
                            USERNAME.equals(auth.getUsername())
                                && PASSWORD.equals(auth.getPassword()))))
            .build();

    OpcTcpServerTransportConfigBuilder transportBuilder = OpcTcpServerTransportConfig.newBuilder();
    configureTransport.accept(transportBuilder);
    OpcTcpServerTransportConfig transportConfig = transportBuilder.build();
    OpcUaServer server =
        new OpcUaServer(
            config,
            transportProfile -> {
              if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
                return new OpcTcpServerTransport(transportConfig);
              } else {
                throw new IllegalArgumentException(
                    "unexpected transport profile: " + transportProfile);
              }
            });

    TestNamespace namespace = new TestNamespace(server);
    namespace.startup();
    server.startup().get();

    String endpointUrl =
        endpoints.stream()
            .filter(endpoint -> !endpoint.getEndpointUrl().endsWith("/discovery"))
            .findFirst()
            .orElseThrow()
            .getEndpointUrl();

    return new RunningServer(
        server, namespace, endpointUrl, namespace.getNamespaceIndex().intValue());
  }

  private static void readWriteTestNode(OpcUaClient client, int namespaceIndex) throws UaException {
    UaVariableNode testNode =
        (UaVariableNode) client.getAddressSpace().getNode(new NodeId(namespaceIndex, "TestInt32"));

    Number before =
        Objects.requireNonNull(
            assertInstanceOf(Integer.class, testNode.readValue().value().value()));
    int after = before.intValue() + 1;

    testNode.writeValue(new Variant(after));

    assertEquals(after, testNode.readValue().value().value());
  }

  private static CertificateManager certificateManager(CertificateMaterial... certificates) {
    return new DefaultCertificateManager(
        new MemoryCertificateQuarantine(),
        List.of(
            new TestCertificateGroup(
                NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
                List.of(certificates))));
  }

  private static CertificateMaterial certificate(
      NodeId certificateTypeId, String commonName, String applicationUri) throws Exception {

    KeyPair keyPair;
    if (NodeIds.EccNistP256ApplicationCertificateType.equals(certificateTypeId)) {
      keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    } else if (NodeIds.EccCurve25519ApplicationCertificateType.equals(certificateTypeId)) {
      keyPair = SelfSignedCertificateGenerator.generateEd25519KeyPair();
    } else {
      throw new IllegalArgumentException("certificateTypeId: " + certificateTypeId);
    }

    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setCommonName(commonName)
            .setOrganization("Eclipse Milo")
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .build();

    return new CertificateMaterial(certificateTypeId, keyPair, new X509Certificate[] {certificate});
  }

  private static UserTokenPolicy anonymousPolicy() {
    return new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);
  }

  private static UserTokenPolicy usernamePolicy(SecurityPolicy securityPolicy) {
    return new UserTokenPolicy(
        "username", UserTokenType.UserName, null, null, securityPolicy.getUri());
  }

  private static int freePort() throws Exception {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }

  private static void disconnectQuietly(OpcUaClient client) {
    try {
      client.disconnectAsync().get(2, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      // Best-effort cleanup; the test failure, if any, is already being reported.
    }
  }

  private static void closeActiveTcpChannel(OpcUaClient client) throws Exception {
    OpcTcpClientTransport transport = (OpcTcpClientTransport) client.getTransport();
    Channel channel = transport.getChannelFsm().getChannel().get(5, TimeUnit.SECONDS);

    channel.close().sync();
  }

  private record RunningServer(
      OpcUaServer server, TestNamespace namespace, String endpointUrl, int namespaceIndex)
      implements AutoCloseable {

    @Override
    public void close() throws Exception {
      namespace.shutdown();
      server.shutdown().get(2, TimeUnit.SECONDS);
    }
  }

  private record CertificateMaterial(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {}

  private record TestCertificateGroup(
      NodeId certificateGroupId, Map<NodeId, CertificateMaterial> certificates)
      implements CertificateGroup {

    private TestCertificateGroup(
        NodeId certificateGroupId, List<CertificateMaterial> certificates) {
      this(certificateGroupId, toCertificateMap(certificates));
    }

    private static Map<NodeId, CertificateMaterial> toCertificateMap(
        List<CertificateMaterial> certificates) {

      Map<NodeId, CertificateMaterial> certificateMap =
          certificates.stream()
              .collect(
                  Collectors.toMap(
                      CertificateMaterial::certificateTypeId,
                      Function.identity(),
                      (left, right) -> right,
                      LinkedHashMap::new));

      return Collections.unmodifiableMap(certificateMap);
    }

    @Override
    public NodeId getCertificateGroupId() {
      return certificateGroupId;
    }

    @Override
    public List<NodeId> getSupportedCertificateTypeIds() {
      return List.copyOf(certificates.keySet());
    }

    @Override
    public TrustListManager getTrustListManager() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Entry> getCertificateEntries() {
      return certificates.values().stream()
          .map(
              certificate ->
                  new CertificateGroup.Entry(
                      certificateGroupId,
                      certificate.certificateTypeId(),
                      certificate.certificateChain()))
          .toList();
    }

    @Override
    public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::keyPair);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::certificateChain)
          .map(X509Certificate[]::clone);
    }

    @Override
    public void updateCertificate(
        NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateFactory getCertificateFactory() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return new CertificateValidator.InsecureCertificateValidator();
    }
  }

  private static final class MalformedHeaderUsernameProvider extends UsernameProvider {

    private MalformedHeaderUsernameProvider() {
      super(USERNAME, PASSWORD);
    }

    @Override
    public org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
        getCreateSessionAdditionalHeader(
            org.eclipse.milo.opcua.stack.core.encoding.EncodingContext context,
            org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription endpoint) {

      return org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.of(
          ByteString.of(new byte[] {0x01}), NodeIds.AnonymousIdentityToken_Encoding_DefaultBinary);
    }
  }
}
