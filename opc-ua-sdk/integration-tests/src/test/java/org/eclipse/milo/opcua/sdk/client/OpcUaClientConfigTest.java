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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
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
    assertTrue(copy.getCertificateIdentity(SecurityPolicy.None.getProfile()).isEmpty());
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

  private static X509Certificate rsaCertificate(KeyPair keyPair) throws Exception {
    return new SelfSignedCertificateBuilder(keyPair)
        .setCommonName("certificate-identity-cache-test")
        .setApplicationUri("urn:eclipse:milo:test:certificate-identity-cache")
        .addDnsName("localhost")
        .build();
  }
}
