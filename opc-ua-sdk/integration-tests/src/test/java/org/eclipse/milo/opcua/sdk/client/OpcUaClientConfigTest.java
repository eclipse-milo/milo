/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;

public class OpcUaClientConfigTest {

  private final EndpointDescription endpoint =
      new EndpointDescription(
          "opc.tcp://localhost:62541",
          null,
          null,
          null,
          null,
          new UserTokenPolicy[] {
            new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
          },
          null,
          null);

  @Test
  public void copyPreservesConfiguredValues() throws Exception {
    CertificateManager certificateManager =
        new DefaultCertificateManager(new MemoryCertificateQuarantine());
    CertificateIdentitySelector certificateIdentitySelector = context -> Optional.empty();
    NodeId certificateGroupId =
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup;
    NodeId certificateTypeId = NodeIds.EccNistP256ApplicationCertificateType;

    OpcUaClientConfig original =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setCertificateManager(certificateManager)
            .setCertificateIdentitySelector(certificateIdentitySelector)
            .setCertificateGroupId(certificateGroupId)
            .setCertificateTypeId(certificateTypeId)
            .setSessionEndpointValidationEnabled(true)
            .setSessionName(() -> "testSessionName")
            .setSessionTimeout(uint(60000 * 60))
            .setMaxResponseMessageSize(UInteger.MAX)
            .setMaxPendingPublishRequests(uint(2))
            .setIdentityProvider(new AnonymousProvider())
            .setSessionLocaleIds(new String[] {"en", "es"})
            .build();

    OpcUaClientConfig copy = OpcUaClientConfig.copy(original).build();

    assertEquals(original.getSessionName(), copy.getSessionName());
    assertEquals(original.getSessionTimeout(), copy.getSessionTimeout());
    assertEquals(original.getMaxResponseMessageSize(), copy.getMaxResponseMessageSize());
    assertEquals(original.getMaxPendingPublishRequests(), copy.getMaxPendingPublishRequests());
    assertEquals(original.getIdentityProvider(), copy.getIdentityProvider());
    assertEquals(original.getKeepAliveFailuresAllowed(), copy.getKeepAliveFailuresAllowed());
    assertEquals(original.getKeepAliveInterval(), copy.getKeepAliveInterval());
    assertEquals(original.getKeepAliveTimeout(), copy.getKeepAliveTimeout());
    assertEquals(original.getSessionLocaleIds(), copy.getSessionLocaleIds());
    assertEquals(original.getDiscoveryEndpoints(), copy.getDiscoveryEndpoints());
    assertEquals(
        original.isSessionEndpointValidationEnabled(), copy.isSessionEndpointValidationEnabled());
    assertEquals(original.getCertificateManager(), copy.getCertificateManager());
    assertSame(original.getCertificateIdentitySelector(), copy.getCertificateIdentitySelector());
    assertEquals(original.getCertificateGroupId(), copy.getCertificateGroupId());
    assertEquals(original.getCertificateTypeId(), copy.getCertificateTypeId());
    assertEquals(original.isApplicationUriConfigured(), copy.isApplicationUriConfigured());
    assertTrue(copy.getCertificateIdentity(SecurityPolicy.None.getProfile()).isEmpty());
  }

  // Copying an unset URI must preserve certificate-based derivation, while an explicitly set URI
  // must remain authoritative in the copied config.
  @Test
  public void copyPreservesApplicationUriExplicitness() {
    OpcUaClientConfig derived =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .build();
    OpcUaClientConfig explicit =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setApplicationUri("urn:eclipse:milo:test:explicit")
            .build();

    OpcUaClientConfig derivedCopy = OpcUaClientConfig.copy(derived).build();
    OpcUaClientConfig explicitCopy = OpcUaClientConfig.copy(explicit).build();

    assertFalse(derivedCopy.isApplicationUriConfigured());
    assertTrue(explicitCopy.isApplicationUriConfigured());
    assertEquals("urn:eclipse:milo:test:explicit", explicitCopy.getApplicationUri());
  }

  @Test
  public void copyAndModifyOverridesConfiguredValues() {
    OpcUaClientConfig original =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setSessionEndpointValidationEnabled(false)
            .setSessionName(() -> "testSessionName")
            .setSessionTimeout(uint(60000 * 60))
            .setMaxResponseMessageSize(UInteger.MAX)
            .setMaxPendingPublishRequests(uint(2))
            .setIdentityProvider(new AnonymousProvider())
            .build();

    EndpointDescription endpoint2 =
        new EndpointDescription(
            "opc.tcp://localhost:4840",
            null,
            null,
            null,
            null,
            new UserTokenPolicy[] {
              new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
            },
            null,
            null);

    OpcUaClientConfig copy =
        OpcUaClientConfig.copy(
            original,
            builder ->
                builder
                    .setSessionName(() -> "foo")
                    .setSessionTimeout(uint(0))
                    .setMaxResponseMessageSize(uint(0))
                    .setMaxPendingPublishRequests(uint(0))
                    .setIdentityProvider(new AnonymousProvider())
                    .setKeepAliveFailuresAllowed(uint(2))
                    .setKeepAliveInterval(uint(10000))
                    .setKeepAliveTimeout(uint(15000))
                    .setSessionLocaleIds(new String[] {"en", "es"})
                    .setDiscoveryEndpoints(List.of(endpoint2))
                    .setSessionEndpointValidationEnabled(true));

    assertNotEquals(original.getSessionName(), copy.getSessionName());
    assertNotEquals(original.getIdentityProvider(), copy.getIdentityProvider());
    assertNotEquals(original.getSessionLocaleIds(), copy.getSessionLocaleIds());

    assertEquals(uint(0), copy.getSessionTimeout());
    assertEquals(uint(0), copy.getMaxResponseMessageSize());
    assertEquals(uint(0), copy.getMaxPendingPublishRequests());
    assertEquals(uint(2), copy.getKeepAliveFailuresAllowed());
    assertEquals(uint(10000), copy.getKeepAliveInterval());
    assertEquals(uint(15000), copy.getKeepAliveTimeout());
    assertArrayEquals(new String[] {"en", "es"}, copy.getSessionLocaleIds());

    assertNotEquals(original.getDiscoveryEndpoints(), copy.getDiscoveryEndpoints());
    assertEquals(List.of(endpoint2), copy.getDiscoveryEndpoints());
    assertNotEquals(
        original.isSessionEndpointValidationEnabled(), copy.isSessionEndpointValidationEnabled());
    assertTrue(copy.isSessionEndpointValidationEnabled());
  }

  // SecureChannel and Session setup must reuse one selected client identity for a connection.
  @Test
  public void clientCachesSelectedCertificateIdentity() throws Exception {
    CertificateManager certificateManager =
        new DefaultCertificateManager(new MemoryCertificateQuarantine());
    AtomicInteger selections = new AtomicInteger();
    CertificateIdentitySelector certificateIdentitySelector =
        context -> {
          selections.incrementAndGet();
          return Optional.empty();
        };
    OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
    when(transportConfig.getExecutor()).thenReturn(Stack.sharedExecutor());
    OpcClientTransport transport = mock(OpcClientTransport.class);
    when(transport.getConfig()).thenReturn(transportConfig);

    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(certificateManager)
            .setCertificateIdentitySelector(certificateIdentitySelector)
            .build();
    OpcUaClient client = new OpcUaClient(config, transport);

    assertTrue(client.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).isEmpty());
    assertTrue(client.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).isEmpty());
    assertEquals(1, selections.get());
  }

  // The ActivateSession flow looks up the user-token profile identity between the channel/session
  // certificate lookups. A per-profile cache must keep that interleaving from re-invoking a
  // stateful selector for the endpoint profile, otherwise the session signature key could diverge
  // from the channel certificate (Bad_ApplicationSignatureInvalid).
  @Test
  public void certificateIdentityCacheSurvivesInterleavedProfileLookups() throws Exception {
    SecurityPolicyProfile endpointProfile = SecurityPolicy.Basic256Sha256.getProfile();
    SecurityPolicyProfile tokenProfile = SecurityPolicy.Aes128_Sha256_RsaOaep.getProfile();

    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate = rsaCertificate(keyPair);

    CertificateIdentity identityA = identity(keyPair, certificate, "groupA");
    CertificateIdentity identityB = identity(keyPair, certificate, "groupB");
    CertificateIdentity identityU = identity(keyPair, certificate, "groupU");

    // A stateful selector: the endpoint profile yields A on its first selection and B on every
    // selection after, so a re-invocation would observably swap the endpoint identity.
    AtomicInteger endpointSelections = new AtomicInteger();
    Map<SecurityPolicyProfile, CertificateIdentity> tokenIdentities =
        Map.of(tokenProfile, identityU);
    CertificateIdentitySelector certificateIdentitySelector =
        context -> {
          CertificateIdentity tokenIdentity = tokenIdentities.get(context.securityPolicyProfile());
          if (tokenIdentity != null) {
            return Optional.of(tokenIdentity);
          }
          return Optional.of(endpointSelections.getAndIncrement() == 0 ? identityA : identityB);
        };

    OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
    when(transportConfig.getExecutor()).thenReturn(Stack.sharedExecutor());
    OpcClientTransport transport = mock(OpcClientTransport.class);
    when(transport.getConfig()).thenReturn(transportConfig);

    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
            .setCertificateIdentitySelector(certificateIdentitySelector)
            .build();
    OpcUaClient client = new OpcUaClient(config, transport);

    assertSame(identityA, client.getCertificateIdentity(endpointProfile).orElseThrow());
    assertSame(identityU, client.getCertificateIdentity(tokenProfile).orElseThrow());
    assertSame(identityA, client.getCertificateIdentity(endpointProfile).orElseThrow());
    assertEquals(1, endpointSelections.get());
  }

  // setCertificate() configures an explicit client certificate. When a CertificateManager holds
  // multiple compatible identities, the client must present the explicitly configured one rather
  // than whatever the default selector would otherwise prefer, matching the server-side contract
  // where an explicit certificate is a selection preference. Both the SecureChannel and the
  // CreateSession identity lookups must resolve that configured certificate.
  @Test
  public void clientPrefersExplicitlyConfiguredCertificate() throws Exception {
    KeyPair keyPairA = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    KeyPair keyPairB = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificateA = rsaCertificate(keyPairA);
    X509Certificate certificateB = rsaCertificate(keyPairB);

    // Both identities share the same group/type, so the default selection order treats them as
    // equal and would otherwise pick the first (identity A).
    CertificateIdentity identityA = identity(keyPairA, certificateA, "group");
    CertificateIdentity identityB = identity(keyPairB, certificateB, "group");

    CertificateManager certificateManager = multiIdentityManager(List.of(identityA, identityB));

    OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
    when(transportConfig.getExecutor()).thenReturn(Stack.sharedExecutor());
    OpcClientTransport transport = mock(OpcClientTransport.class);
    when(transport.getConfig()).thenReturn(transportConfig);

    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(certificateManager)
            .setKeyPair(keyPairB)
            .setCertificate(certificateB)
            .build();
    OpcUaClient client = new OpcUaClient(config, transport);

    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();
    CertificateIdentity selected = client.getCertificateIdentity(profile).orElseThrow();

    assertEquals(certificateB, selected.certificate());
    // Repeated lookups (SecureChannel open and CreateSession) must resolve the same identity.
    assertEquals(certificateB, client.getCertificateIdentity(profile).orElseThrow().certificate());
  }

  // setApplicationUri() is an explicit application identity choice and must override certificate
  // SAN URIs, including the URI on the manager-selected identity.
  @Test
  public void explicitApplicationUriTakesPrecedence() throws Exception {
    CertificateIdentity identity = identity("urn:eclipse:milo:test:managed", "group");
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(multiIdentityManager(List.of(identity)))
            .setApplicationUri("urn:eclipse:milo:test:explicit")
            .build();

    OpcUaClient client = client(config);

    assertEquals("urn:eclipse:milo:test:explicit", client.resolveApplicationUri(Optional.empty()));
  }

  // The certificate presented for the endpoint defines the application instance, so its URI must
  // win over a legacy fixed certificate that is only retained as a compatibility fallback.
  @Test
  public void selectedIdentityApplicationUriTakesPrecedenceOverFixedCertificate() throws Exception {
    CertificateIdentity identity = identity("urn:eclipse:milo:test:managed", "group");
    X509Certificate fixedCertificate = certificate("urn:eclipse:milo:test:fixed");
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(multiIdentityManager(List.of(identity)))
            .setCertificateIdentitySelector(context -> Optional.of(identity))
            .setCertificate(fixedCertificate)
            .build();

    OpcUaClient client = client(config);

    assertEquals(
        "urn:eclipse:milo:test:managed",
        client.resolveApplicationUri(client.getCertificateIdentity(profile())));
  }

  // An explicit certificate outside the manager makes selection empty. Its SAN URI must still be
  // used with the fixed key material selected by the compatibility fallback.
  @Test
  public void fixedCertificateApplicationUriIsCompatibilityFallback() throws Exception {
    X509Certificate fixedCertificate = certificate("urn:eclipse:milo:test:fixed");
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(multiIdentityManager(List.of()))
            .setCertificate(fixedCertificate)
            .build();

    OpcUaClient client = client(config);

    assertEquals(
        "urn:eclipse:milo:test:fixed",
        client.resolveApplicationUri(client.getCertificateIdentity(profile())));
  }

  // The placeholder remains a last resort for certificate-less clients and certificates without a
  // SAN URI.
  @Test
  public void applicationUriPlaceholderIsUsedLast() throws Exception {
    OpcUaClientConfig config = OpcUaClientConfig.builder().setEndpoint(endpoint).build();

    OpcUaClient client = client(config);

    assertEquals(
        "urn:eclipse:milo:client:applicationUriNotConfigured",
        client.resolveApplicationUri(Optional.empty()));
  }

  // A selected identity without a SAN URI cannot define the ApplicationUri. The fixed certificate
  // remains the next compatibility source before the placeholder.
  @Test
  public void fixedCertificateApplicationUriFollowsSelectedIdentityWithoutSanUri()
      throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificateWithoutSanUri =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri(null)
            .addDnsName("localhost")
            .build();
    CertificateIdentity identity = identity(keyPair, certificateWithoutSanUri, "group");
    X509Certificate fixedCertificate = certificate("urn:eclipse:milo:test:fixed");
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(multiIdentityManager(List.of(identity)))
            .setCertificate(fixedCertificate)
            .build();

    OpcUaClient client = client(config);

    assertEquals(
        "urn:eclipse:milo:test:fixed", client.resolveApplicationUri(Optional.of(identity)));
  }

  // SecureChannel setup selects first. Session creation must derive the URI from that cached
  // identity even when other manager identities carry a different ApplicationUri.
  @Test
  public void applicationUriUsesCachedSelectedIdentityWhenManagerUrisDiffer() throws Exception {
    CertificateIdentity identityA = identity("urn:eclipse:milo:test:a", "groupA");
    CertificateIdentity identityB = identity("urn:eclipse:milo:test:b", "groupB");
    AtomicInteger selections = new AtomicInteger();
    CertificateIdentitySelector selector =
        context -> Optional.of(selections.getAndIncrement() == 0 ? identityA : identityB);
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(multiIdentityManager(List.of(identityA, identityB)))
            .setCertificateIdentitySelector(selector)
            .build();

    OpcUaClient client = client(config);
    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();

    assertSame(identityA, client.getCertificateIdentity(profile).orElseThrow());
    assertEquals(
        "urn:eclipse:milo:test:a",
        client.resolveApplicationUri(client.getCertificateIdentity(profile)));
    assertEquals(1, selections.get());
  }

  // CreateSession must send the URI from the same effective identity whose certificate it places in
  // the request, otherwise servers reject ActivateSession with Bad_CertificateUriInvalid.
  @Test
  public void createSessionUsesSelectedIdentityApplicationUri() throws Exception {
    CertificateIdentity identity = identity("urn:eclipse:milo:test:managed", "group");
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(secureEndpoint(identity.certificate()))
            .setCertificateManager(multiIdentityManager(List.of(identity)))
            .build();
    CapturingClientTransport transport = new CapturingClientTransport();
    OpcUaClient client = new OpcUaClient(config, transport);
    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    try {
      CreateSessionRequest request = transport.createSessionRequest.get(5, TimeUnit.SECONDS);

      assertEquals(
          "urn:eclipse:milo:test:managed", request.getClientDescription().getApplicationUri());
      assertArrayEquals(
          identity.certificate().getEncoded(), request.getClientCertificate().bytes());
    } finally {
      transport.releasePending();
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
      connectFuture.cancel(true);
    }
  }

  private static CertificateManager multiIdentityManager(List<CertificateIdentity> identities) {
    return new CertificateManager() {
      @Override
      public List<CertificateIdentity> getCertificateIdentities() {
        return identities;
      }

      @Override
      public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
        return Optional.empty();
      }

      @Override
      public Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId) {
        return Optional.empty();
      }

      @Override
      public List<CertificateGroup> getCertificateGroups() {
        return List.of();
      }

      @Override
      public CertificateQuarantine getCertificateQuarantine() {
        throw new UnsupportedOperationException();
      }
    };
  }

  private static CertificateIdentity identity(
      KeyPair keyPair, X509Certificate certificate, String group) {

    return new CertificateIdentity(
        new NodeId(0, group),
        NodeIds.RsaSha256ApplicationCertificateType,
        keyPair,
        new X509Certificate[] {certificate});
  }

  private static CertificateIdentity identity(String applicationUri, String group)
      throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    return identity(keyPair, certificate(keyPair, applicationUri), group);
  }

  private static X509Certificate rsaCertificate(KeyPair keyPair) throws Exception {
    return certificate(keyPair, "urn:eclipse:milo:test:certificate-identity-cache");
  }

  private static X509Certificate certificate(String applicationUri) throws Exception {
    return certificate(SelfSignedCertificateGenerator.generateRsaKeyPair(2048), applicationUri);
  }

  private static X509Certificate certificate(KeyPair keyPair, String applicationUri)
      throws Exception {
    return new SelfSignedCertificateBuilder(keyPair)
        .setCommonName("certificate-identity-cache-test")
        .setApplicationUri(applicationUri)
        .addDnsName("localhost")
        .build();
  }

  private static OpcUaClient client(OpcUaClientConfig config) {
    OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
    when(transportConfig.getExecutor()).thenReturn(Stack.sharedExecutor());
    OpcClientTransport transport = mock(OpcClientTransport.class);
    when(transport.getConfig()).thenReturn(transportConfig);

    return new OpcUaClient(config, transport);
  }

  private static SecurityPolicyProfile profile() {
    return SecurityPolicy.Basic256Sha256.getProfile();
  }

  private static EndpointDescription secureEndpoint(X509Certificate serverCertificate)
      throws Exception {
    ApplicationDescription server =
        new ApplicationDescription(
            "urn:eclipse:milo:test:server",
            "urn:eclipse:milo:test:product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            null);

    return new EndpointDescription(
        "opc.tcp://localhost:62541",
        server,
        ByteString.of(serverCertificate.getEncoded()),
        MessageSecurityMode.SignAndEncrypt,
        SecurityPolicy.Basic256Sha256.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
        },
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        null);
  }

  private static final class CapturingClientTransport implements OpcClientTransport {
    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();
    private final CompletableFuture<CreateSessionRequest> createSessionRequest =
        new CompletableFuture<>();
    private final CompletableFuture<UaResponseMessageType> pendingResponse =
        new CompletableFuture<>();

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      if (requestMessage instanceof CreateSessionRequest request) {
        createSessionRequest.complete(request);
      }

      return pendingResponse;
    }

    void releasePending() {
      pendingResponse.completeExceptionally(new RuntimeException("test cleanup"));
    }
  }
}
