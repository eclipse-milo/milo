/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.CipherFactory;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.server.EndpointSelectionKey;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Part 4 §5.7.3.1: a Session created on a {@code SecurityPolicy.None} endpoint whose user token
 * policy encrypts passwords binds token decryption to the serverCertificate returned by
 * CreateSession for the life of the Session, even after an in-place server certificate renewal.
 */
class SessionNoneEndpointCertificateRenewalTest {

  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840/none-renewal";
  private static final String SERVER_URI = "urn:eclipse:milo:test:none-renewal";
  private static final String USERNAME = "alice";
  private static final String PASSWORD = "secret-password";

  private static final OpcServerTransportFactory NO_OP_TRANSPORTS =
      transportProfile ->
          new OpcServerTransport() {
            @Override
            public void bind(
                ServerApplicationContext applicationContext, InetSocketAddress bindAddress) {}

            @Override
            public void unbind() {}
          };

  private final List<String> authenticatedUsers = new CopyOnWriteArrayList<>();
  private final ServiceRequestContext initialChannel = new NoneServiceRequestContext(1L);
  private final ServiceRequestContext replacementChannel = new NoneServiceRequestContext(2L);

  private DefaultCertificateGroup certificateGroup;
  private DefaultCertificateManager certificateManager;
  private KeyPair originalKeyPair;
  private X509Certificate originalCertificate;
  private OpcUaServer server;
  private SessionManager sessions;

  @BeforeEach
  void setUp() throws Exception {
    var trustListManager = new MemoryTrustListManager();
    var quarantine = new MemoryCertificateQuarantine();
    certificateGroup =
        new DefaultCertificateGroup(
            trustListManager,
            new MemoryCertificateStore(),
            quarantine,
            new DefaultServerCertificateValidator(trustListManager, quarantine));

    originalKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    originalCertificate = buildServerCertificate(originalKeyPair);
    certificateGroup.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType,
        originalKeyPair,
        new X509Certificate[] {originalCertificate});

    certificateManager = new DefaultCertificateManager(certificateGroup);

    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setBindPort(4840)
            .setHostname("localhost")
            .setPath("/none-renewal")
            .setEndpointCertificateConfig(
                EndpointCertificateConfig.newBuilder()
                    .setCertificateTypeId(NodeIds.RsaSha256ApplicationCertificateType)
                    .build())
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .addTokenPolicy(USER_TOKEN_POLICY_USERNAME)
            .build();

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri(SERVER_URI)
            .setApplicationName(LocalizedText.english("None renewal test server"))
            .setProductUri("urn:eclipse:milo:test")
            .setCertificateManager(certificateManager)
            .setIdentityValidator(
                new UsernameIdentityValidator(
                    challenge -> {
                      boolean ok =
                          USERNAME.equals(challenge.getUsername())
                              && PASSWORD.equals(challenge.getPassword());
                      if (ok) {
                        authenticatedUsers.add(challenge.getUsername());
                      }
                      return ok;
                    }))
            .setEndpoints(Set.of(endpoint))
            .build();

    server = new OpcUaServer(config, NO_OP_TRANSPORTS);
    sessions = server.getSessionManager();
  }

  @AfterEach
  void tearDown() {
    sessions.shutdown();
  }

  // Part 4 §5.7.3.1: after an in-place certificate renewal (with either the same key or a new
  // key), a Session created on a None endpoint must still reactivate on a replacement None channel
  // when the client encrypts its password to the certificate CreateSession returned.
  @ParameterizedTest(name = "sameKey={0}")
  @ValueSource(booleans = {true, false})
  void reactivatesNoneSessionAfterInPlaceCertificateRenewal(boolean sameKey) throws Exception {
    CreateSessionResponse created = sessions.createSession(initialChannel, createSessionRequest());
    NodeId authToken = created.getAuthenticationToken();
    ByteString createSessionCertBytes = created.getServerCertificate();

    assertEquals(ByteString.of(originalCertificate.getEncoded()), createSessionCertBytes);

    ActivateSessionResponse firstActivation =
        sessions.activateSession(
            initialChannel,
            activateEncryptedUsername(authToken, originalCertificate, created.getServerNonce()));

    X509Certificate renewedCertificate = renewCertificateInPlace(sameKey);

    assertNotEquals(originalCertificate, renewedCertificate);
    assertTrue(
        certificateManager.getKeyPair(CertificateUtil.thumbprint(originalCertificate)).isEmpty(),
        "renewal must evict the original certificate thumbprint from the manager");

    sessions.activateSession(
        replacementChannel,
        activateEncryptedUsername(
            authToken, originalCertificate, firstActivation.getServerNonce()));

    Session session = sessions.getAllSessions().iterator().next();
    assertEquals(2L, session.getSecureChannelId());
    assertEquals(List.of(USERNAME, USERNAME), authenticatedUsers);
    assertEquals(
        ByteString.of(renewedCertificate.getEncoded()),
        session.getEndpoint().getServerCertificate());
    assertEquals(
        createSessionCertBytes, session.getOriginalServerCertificate().createSessionCertificate());
  }

  // Part 4 §5.7.3.1: the client encrypts its password to the serverCertificate CreateSession
  // returns. On a None channel opened before an in-place renewal (with either the same key or a new
  // key), CreateSession must return the renewed certificate, whose key the server still holds, not
  // the certificate the channel's endpoint advertised when the channel opened.
  @ParameterizedTest(name = "sameKey={0}")
  @ValueSource(booleans = {true, false})
  void newSessionOnNoneChannelOpenedBeforeRenewalUsesRenewedCertificate(boolean sameKey)
      throws Exception {

    ServiceRequestContext channelOpenedBeforeRenewal =
        new NoneServiceRequestContext(3L, selectNoneEndpoint());

    X509Certificate renewedCertificate = renewCertificateInPlace(sameKey);

    assertEquals(
        ByteString.of(originalCertificate.getEncoded()),
        channelOpenedBeforeRenewal.getEndpoint().orElseThrow().getServerCertificate(),
        "the channel's selected endpoint must still advertise the original certificate");

    CreateSessionResponse created =
        sessions.createSession(channelOpenedBeforeRenewal, createSessionRequest());

    assertEquals(ByteString.of(renewedCertificate.getEncoded()), created.getServerCertificate());

    sessions.activateSession(
        channelOpenedBeforeRenewal,
        activateEncryptedUsername(
            created.getAuthenticationToken(), renewedCertificate, created.getServerNonce()));

    assertEquals(List.of(USERNAME), authenticatedUsers);
  }

  // Control check: after a new-key renewal, decrypting with the retained CreateSession key pair
  // must reject a token encrypted to the replacement certificate's new public key.
  @Test
  void reactivationRejectsPasswordEncryptedToRenewedKeyAfterNewKeyRenewal() throws Exception {
    CreateSessionResponse created = sessions.createSession(initialChannel, createSessionRequest());
    NodeId authToken = created.getAuthenticationToken();

    ActivateSessionResponse firstActivation =
        sessions.activateSession(
            initialChannel,
            activateEncryptedUsername(authToken, originalCertificate, created.getServerNonce()));

    X509Certificate renewedCertificate = renewCertificateInPlace(false);

    UaException failure =
        assertThrows(
            UaException.class,
            () ->
                sessions.activateSession(
                    replacementChannel,
                    activateEncryptedUsername(
                        authToken, renewedCertificate, firstActivation.getServerNonce())));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, failure.getStatusCode().value());
    assertEquals(List.of(USERNAME), authenticatedUsers);
  }

  // On a None endpoint that advertises a server certificate, SessionServerCertificate.of captures
  // the leaf, full certificate chain, and key pair from the CertificateManager at CreateSession
  // time so they remain available after the certificate is replaced.
  @Test
  void capturesCertificateChainAndKeyPairForNoneEndpointWithAdvertisedCertificate()
      throws Exception {
    X509Certificate issuerCertificate =
        buildServerCertificate(SelfSignedCertificateGenerator.generateRsaKeyPair(2048));
    certificateGroup.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType,
        originalKeyPair,
        new X509Certificate[] {originalCertificate, issuerCertificate});

    EndpointDescription endpoint =
        noneEndpointDescription(ByteString.of(originalCertificate.getEncoded()));
    SecurityConfiguration noneConfig =
        new SecurityConfiguration(
            SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null);

    SessionServerCertificate captured =
        SessionServerCertificate.of(endpoint, noneConfig, certificateManager);

    assertEquals(
        ByteString.of(originalCertificate.getEncoded()), captured.createSessionCertificate());
    assertEquals(originalCertificate, captured.certificate());
    assertEquals(List.of(originalCertificate, issuerCertificate), captured.certificateChain());
    assertNotNull(captured.keyPair());
    assertEquals(originalKeyPair.getPublic(), captured.keyPair().getPublic());
    assertEquals(originalKeyPair.getPrivate(), captured.keyPair().getPrivate());
  }

  // A None endpoint with no advertised certificate, or whose certificate is not in the manager,
  // leaves certificate, chain, and key pair absent without failing Session construction.
  @Test
  void leavesKeyMaterialAbsentWhenNoneEndpointHasNoManagedCertificate() throws Exception {
    SecurityConfiguration noneConfig =
        new SecurityConfiguration(
            SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null);

    SessionServerCertificate withoutCert =
        SessionServerCertificate.of(
            noneEndpointDescription(ByteString.NULL_VALUE), noneConfig, certificateManager);
    assertEquals(ByteString.NULL_VALUE, withoutCert.createSessionCertificate());
    assertNull(withoutCert.certificate());
    assertNull(withoutCert.certificateChain());
    assertNull(withoutCert.keyPair());

    X509Certificate unmanagedCert =
        buildServerCertificate(SelfSignedCertificateGenerator.generateRsaKeyPair(2048));
    SessionServerCertificate unmanaged =
        SessionServerCertificate.of(
            noneEndpointDescription(ByteString.of(unmanagedCert.getEncoded())),
            noneConfig,
            certificateManager);
    assertEquals(ByteString.of(unmanagedCert.getEncoded()), unmanaged.createSessionCertificate());
    assertNull(unmanaged.certificate());
    assertNull(unmanaged.certificateChain());
    assertNull(unmanaged.keyPair());
  }

  // Secured creating channels already carry their own certificate and key pair in
  // SecurityConfiguration and must not overwrite them from the CertificateManager.
  @Test
  void preservesCreatingChannelKeyMaterialOnSecuredChannel() throws Exception {
    KeyPair channelKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate channelCert = buildServerCertificate(channelKeyPair);
    EndpointDescription endpoint =
        noneEndpointDescription(ByteString.of(originalCertificate.getEncoded()));
    SecurityConfiguration securedConfig =
        new SecurityConfiguration(
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt,
            channelKeyPair,
            channelCert,
            List.of(channelCert),
            null,
            null);

    SessionServerCertificate captured =
        SessionServerCertificate.of(endpoint, securedConfig, certificateManager);

    assertEquals(channelCert, captured.certificate());
    assertEquals(List.of(channelCert), captured.certificateChain());
    assertSame(channelKeyPair, captured.keyPair());
  }

  private static EndpointDescription noneEndpointDescription(ByteString serverCertificate) {
    return new EndpointDescription(
        ENDPOINT_URL,
        new ApplicationDescription(
            SERVER_URI,
            "urn:eclipse:milo:test",
            LocalizedText.english("None renewal test server"),
            ApplicationType.Server,
            null,
            null,
            null),
        serverCertificate,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        null,
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  /** Select the None endpoint the way the transport does when a None SecureChannel opens. */
  private EndpointDescription selectNoneEndpoint() {
    EndpointSelectionKey selectionKey =
        EndpointSelectionKey.of(
            TransportProfile.TCP_UASC_UABINARY,
            ENDPOINT_URL,
            SecurityPolicy.None,
            MessageSecurityMode.None,
            null);

    return server.getApplicationContext().selectEndpoint(selectionKey, ENDPOINT_URL).orElseThrow();
  }

  private X509Certificate renewCertificateInPlace(boolean sameKey) throws Exception {
    KeyPair nextKeyPair =
        sameKey ? originalKeyPair : SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate renewed = buildServerCertificate(nextKeyPair);
    certificateGroup.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType, nextKeyPair, new X509Certificate[] {renewed});
    server.resetEndpointDescriptionCache();
    return renewed;
  }

  private static X509Certificate buildServerCertificate(KeyPair keyPair) throws Exception {
    return SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
        .setCommonName("None renewal test server")
        .setApplicationUri(SERVER_URI)
        .addDnsName("localhost")
        .build();
  }

  private static CreateSessionRequest createSessionRequest() {
    return new CreateSessionRequest(
        requestHeader(NodeId.NULL_VALUE),
        new ApplicationDescription(
            "urn:test:client",
            "urn:test:client-product",
            LocalizedText.english("client"),
            ApplicationType.Client,
            null,
            null,
            null),
        null,
        ENDPOINT_URL,
        "none-renewal-session",
        NonceUtil.generateNonce(32),
        ByteString.NULL_VALUE,
        60_000.0,
        uint(0));
  }

  private ActivateSessionRequest activateEncryptedUsername(
      NodeId authToken, X509Certificate serverCertificate, ByteString serverNonce)
      throws Exception {

    SecurityPolicy tokenPolicy =
        SecurityPolicy.fromUri(USER_TOKEN_POLICY_USERNAME.getSecurityPolicyUri());
    SecurityAlgorithm algorithm = tokenPolicy.getAsymmetricEncryptionAlgorithm();

    byte[] passwordBytes = PASSWORD.getBytes(StandardCharsets.UTF_8);
    byte[] nonceBytes = serverNonce.bytesOrEmpty();
    int totalLength = passwordBytes.length + nonceBytes.length;

    ByteArrayOutputStream plain = new ByteArrayOutputStream(Integer.BYTES + totalLength);
    plain.write(totalLength & 0xFF);
    plain.write((totalLength >>> 8) & 0xFF);
    plain.write((totalLength >>> 16) & 0xFF);
    plain.write((totalLength >>> 24) & 0xFF);
    plain.write(passwordBytes);
    plain.write(nonceBytes);
    byte[] plainBytes = plain.toByteArray();

    int plainTextBlockSize =
        SecureChannel.getAsymmetricPlainTextBlockSize(serverCertificate, algorithm);
    Cipher cipher = CipherFactory.createForEncryption(algorithm, serverCertificate.getPublicKey());
    ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
    for (int offset = 0; offset < plainBytes.length; offset += plainTextBlockSize) {
      int length = Math.min(plainTextBlockSize, plainBytes.length - offset);
      encrypted.write(cipher.doFinal(plainBytes, offset, length));
    }

    var token =
        new UserNameIdentityToken(
            USER_TOKEN_POLICY_USERNAME.getPolicyId(),
            USERNAME,
            ByteString.of(encrypted.toByteArray()),
            algorithm.getUri());

    return new ActivateSessionRequest(
        requestHeader(authToken),
        new SignatureData(null, null),
        null,
        null,
        ExtensionObject.encode(server.getStaticEncodingContext(), token),
        new SignatureData(null, null));
  }

  private static RequestHeader requestHeader(NodeId authToken) {
    return new RequestHeader(authToken, DateTime.now(), uint(1), uint(0), null, uint(10_000), null);
  }

  private static final class NoneServiceRequestContext implements ServiceRequestContext {

    private final ServerSecureChannel secureChannel = new ServerSecureChannel();
    private final @Nullable EndpointDescription selectedEndpoint;

    NoneServiceRequestContext(long channelId) {
      this(channelId, null);
    }

    /**
     * @param channelId the SecureChannel id.
     * @param selectedEndpoint the endpoint the transport selected when the channel opened, or null
     *     for a channel with no propagated selection.
     */
    NoneServiceRequestContext(long channelId, @Nullable EndpointDescription selectedEndpoint) {
      this.selectedEndpoint = selectedEndpoint;

      secureChannel.setChannelId(channelId);
      secureChannel.setSecurityPolicy(SecurityPolicy.None);
      secureChannel.setMessageSecurityMode(MessageSecurityMode.None);
    }

    @Override
    public String getEndpointUrl() {
      return ENDPOINT_URL;
    }

    @Override
    public TransportProfile getTransportProfile() {
      return TransportProfile.TCP_UASC_UABINARY;
    }

    @Override
    public Channel getChannel() {
      throw new UnsupportedOperationException();
    }

    @Override
    public SecureChannel getSecureChannel() {
      return secureChannel;
    }

    @Override
    public Optional<EndpointDescription> getEndpoint() {
      return Optional.ofNullable(selectedEndpoint);
    }

    @Override
    public Long receivedAtNanos() {
      return System.nanoTime();
    }

    @Override
    public InetAddress clientAddress() {
      return InetAddress.getLoopbackAddress();
    }
  }
}
