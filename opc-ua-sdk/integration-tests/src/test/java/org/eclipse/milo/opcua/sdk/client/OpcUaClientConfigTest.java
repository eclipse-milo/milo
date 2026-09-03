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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OpcUaClientConfigTest {

  private static final String PLACEHOLDER_URI =
      "urn:eclipse:milo:client:applicationUriNotConfigured";
  private static final String MANAGED_URI = "urn:eclipse:milo:test:managed";

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
  public void copyPreservesConfiguredValues() {
    CertificateGroup certificateGroup = new TestCertificateGroup();
    CertificateIdentitySelector certificateIdentitySelector = context -> Optional.empty();
    NodeId certificateTypeId = NodeIds.EccNistP256ApplicationCertificateType;
    CertificateValidator certificateValidator =
        new CertificateValidator.InsecureCertificateValidator();

    OpcUaClientConfig original =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setCertificateGroup(certificateGroup)
            .setCertificateIdentitySelector(certificateIdentitySelector)
            .setCertificateTypeId(certificateTypeId)
            .setCertificateValidator(certificateValidator)
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
    assertSame(certificateGroup, copy.getCertificateGroup().orElseThrow());
    assertSame(certificateIdentitySelector, copy.getCertificateIdentitySelector());
    assertEquals(Optional.of(certificateTypeId), copy.getCertificateTypeId());
    assertSame(certificateValidator, copy.getCertificateValidator());
    assertEquals(original.getApplicationUri(), copy.getApplicationUri());
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

    assertTrue(derivedCopy.getApplicationUri().isEmpty());
    assertEquals(Optional.of("urn:eclipse:milo:test:explicit"), explicitCopy.getApplicationUri());
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

  @Nested
  class ValidatorDefaults {

    // A client that configures a group expects that group's trust decision to validate servers;
    // silently substituting the insecure validator would bypass the trust list it was given.
    @Test
    public void groupValidatorIsUsedWhenNoValidatorIsSet() {
      CertificateValidator groupValidator = new CertificateValidator.InsecureCertificateValidator();

      OpcUaClientConfig config =
          builder().setCertificateGroup(new TestCertificateGroup(groupValidator)).build();

      assertSame(groupValidator, config.getCertificateValidator());
    }

    @Test
    public void explicitValidatorOverridesGroupValidator() {
      CertificateValidator groupValidator = new CertificateValidator.InsecureCertificateValidator();
      CertificateValidator explicitValidator =
          new CertificateValidator.InsecureCertificateValidator();

      OpcUaClientConfig config =
          builder()
              .setCertificateGroup(new TestCertificateGroup(groupValidator))
              .setCertificateValidator(explicitValidator)
              .build();

      assertSame(explicitValidator, config.getCertificateValidator());
    }

    // Without a group there is no trust list to validate against, so a None-only client keeps the
    // historical default of not validating server certificates.
    @Test
    public void insecureValidatorIsUsedWithoutGroup() {
      OpcUaClientConfig config = builder().build();

      assertInstanceOf(
          CertificateValidator.InsecureCertificateValidator.class,
          config.getCertificateValidator());
    }
  }

  @Nested
  class FixedIdentity {

    // A client with one key pair and certificate on hand must not have to build trust material it
    // never uses: setCertificateIdentity alone yields the identity for a compatible policy.
    @Test
    public void fixedIdentityIsSelectedForCompatiblePolicy() throws Exception {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate = rsaCertificate(keyPair, MANAGED_URI);

      OpcUaClientConfig config = builder().setCertificateIdentity(keyPair, certificate).build();

      CertificateIdentity identity =
          config.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).orElseThrow();
      assertEquals(certificate, identity.certificate());
      assertSame(keyPair.getPrivate(), identity.keyPair().getPrivate());
      assertEquals(NodeIds.RsaSha256ApplicationCertificateType, identity.certificateTypeId());
    }

    // The fixed identity carries no trust list, so server validation is exactly what the caller
    // configured: the historical insecure default, or the explicit validator.
    @Test
    public void fixedIdentityUsesExplicitValidatorOrInsecureDefault() throws Exception {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate = rsaCertificate(keyPair, MANAGED_URI);
      CertificateValidator explicitValidator =
          new CertificateValidator.InsecureCertificateValidator();

      OpcUaClientConfig defaulted = builder().setCertificateIdentity(keyPair, certificate).build();
      OpcUaClientConfig explicit =
          builder()
              .setCertificateIdentity(keyPair, certificate)
              .setCertificateValidator(explicitValidator)
              .build();

      assertInstanceOf(
          CertificateValidator.InsecureCertificateValidator.class,
          defaulted.getCertificateValidator());
      assertSame(explicitValidator, explicit.getCertificateValidator());
    }

    // A client holds one group. The two ways of supplying it are alternatives, and the last one
    // set wins so copy-and-modify can switch between them.
    @Test
    public void lastOfCertificateGroupAndCertificateIdentityWins() throws Exception {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate = rsaCertificate(keyPair, MANAGED_URI);
      CertificateGroup group = new TestCertificateGroup();

      OpcUaClientConfig identityWins =
          builder().setCertificateGroup(group).setCertificateIdentity(keyPair, certificate).build();
      OpcUaClientConfig groupWins =
          builder().setCertificateIdentity(keyPair, certificate).setCertificateGroup(group).build();

      assertEquals(
          certificate,
          identityWins
              .getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile())
              .orElseThrow()
              .certificate());
      assertSame(group, groupWins.getCertificateGroup().orElseThrow());
    }

    // A key pair that does not match the certificate can never complete a handshake; the builder
    // must reject it at build time rather than at the first secured connection.
    @Test
    public void mismatchedFixedIdentityFailsAtBuild() throws Exception {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      KeyPair otherKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate = rsaCertificate(keyPair, MANAGED_URI);

      OpcUaClientConfigBuilder builder =
          builder().setCertificateIdentity(otherKeyPair, certificate);

      assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    public void copyPreservesFixedIdentity() throws Exception {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate = rsaCertificate(keyPair, MANAGED_URI);
      OpcUaClientConfig original =
          builder()
              .setDiscoveryEndpoints(List.of(endpoint))
              .setCertificateIdentity(keyPair, certificate)
              .build();

      OpcUaClientConfig copy = OpcUaClientConfig.copy(original).build();

      assertEquals(
          certificate,
          copy.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile())
              .orElseThrow()
              .certificate());
    }
  }

  @Nested
  class IdentitySelection {

    // One group can hold an identity per key family. The endpoint policy must choose the matching
    // family, never an RSA certificate for an ECC channel or the reverse.
    @Test
    public void endpointProfileSelectsMatchingIdentityFromMixedGroup() throws Exception {
      CertificateMaterial rsa = rsaMaterial(MANAGED_URI);
      CertificateMaterial ecc = eccNistP256Material(MANAGED_URI);
      OpcUaClientConfig config =
          builder().setCertificateGroup(new TestCertificateGroup(rsa, ecc)).build();

      CertificateIdentity rsaIdentity =
          config.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).orElseThrow();
      CertificateIdentity eccIdentity =
          config
              .getCertificateIdentity(SecurityPolicy.ECC_nistP256_AesGcm.getProfile())
              .orElseThrow();

      assertEquals(rsa.certificate(), rsaIdentity.certificate());
      assertEquals(ecc.certificate(), eccIdentity.certificate());
    }

    // Basic256 accepts both RsaSha256 and RsaMin identities and prefers RsaSha256. A configured
    // certificate type must redirect that preference so an operator can pin a specific certificate.
    @Test
    public void configuredCertificateTypeIsPreferredWithinProfile() throws Exception {
      CertificateMaterial rsaSha256 =
          rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, MANAGED_URI);
      CertificateMaterial rsaMin =
          rsaMaterial(NodeIds.RsaMinApplicationCertificateType, MANAGED_URI);
      CertificateGroup certificateGroup = new TestCertificateGroup(rsaSha256, rsaMin);
      SecurityPolicyProfile profile = SecurityPolicy.Basic256.getProfile();

      OpcUaClientConfig pinned =
          builder()
              .setCertificateGroup(certificateGroup)
              .setCertificateTypeId(NodeIds.RsaMinApplicationCertificateType)
              .build();
      OpcUaClientConfig unpinned = builder().setCertificateGroup(certificateGroup).build();

      assertEquals(
          rsaMin.certificate(), pinned.getCertificateIdentity(profile).orElseThrow().certificate());
      assertEquals(
          rsaSha256.certificate(),
          unpinned.getCertificateIdentity(profile).orElseThrow().certificate(),
          "without a configured type the policy-preferred type must win");
    }

    // The configured type is a preference among the policy's compatible identities, not a way to
    // force an incompatible one: a group with nothing for the endpoint policy yields no identity.
    @Test
    public void groupWithoutCompatibleIdentityYieldsEmpty() throws Exception {
      OpcUaClientConfig config =
          builder()
              .setCertificateGroup(new TestCertificateGroup(rsaMaterial(MANAGED_URI)))
              .setCertificateTypeId(NodeIds.EccNistP256ApplicationCertificateType)
              .build();

      assertTrue(
          config.getCertificateIdentity(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()).isEmpty());
    }

    // A secured endpoint cannot be used without an identity. The client must fail fast with
    // Bad_ConfigurationError instead of sending a null certificate for the server to reject with a
    // less actionable error.
    @Test
    public void connectFailsWithConfigurationErrorWhenGroupCannotSatisfyEndpoint()
        throws Exception {

      CertificateMaterial serverMaterial = rsaMaterial("urn:eclipse:milo:test:server");
      CertificateGroup eccOnlyGroup = new TestCertificateGroup(eccNistP256Material(MANAGED_URI));
      OpcUaClientConfig config =
          OpcUaClientConfig.builder()
              .setEndpoint(secureEndpoint(serverMaterial.certificate()))
              .setCertificateGroup(eccOnlyGroup)
              .build();
      CapturingClientTransport transport = new CapturingClientTransport();
      OpcUaClient client = new OpcUaClient(config, transport);

      try {
        ExecutionException e =
            assertThrows(
                ExecutionException.class, () -> client.connectAsync().get(5, TimeUnit.SECONDS));

        UaException uaException = UaException.extract(e).orElseThrow();
        assertEquals(StatusCodes.Bad_ConfigurationError, uaException.getStatusCode().value());
        assertFalse(
            transport.createSessionRequest.isDone(),
            "no CreateSession request may be sent without a certificate identity");
      } finally {
        transport.releasePending();
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      }
    }

    // forIdentity replaces the removed setKeyPair/setCertificate pin: a bare key pair and chain
    // must become a selectable identity whose type is inferred from the certificate.
    @Test
    public void forIdentityBuildsGroupOfOneWithInferredType() throws Exception {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate = rsaCertificate(keyPair, MANAGED_URI);
      DefaultCertificateGroup certificateGroup =
          DefaultCertificateGroup.forIdentity(
              keyPair,
              chain(certificate),
              new MemoryTrustListManager(),
              new MemoryCertificateQuarantine(),
              new CertificateValidator.InsecureCertificateValidator());
      OpcUaClientConfig config = builder().setCertificateGroup(certificateGroup).build();

      CertificateIdentity identity = config.getCertificateIdentity(profile()).orElseThrow();

      assertEquals(certificate, identity.certificate());
      assertEquals(NodeIds.RsaSha256ApplicationCertificateType, identity.certificateTypeId());
      assertSame(certificateGroup, identity.certificateGroup());
    }

    // A private key that cannot sign for the certificate would only fail later at
    // OpenSecureChannel; forIdentity must reject the mismatch up front.
    @Test
    public void forIdentityRejectsMismatchedKeyPair() throws Exception {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      KeyPair otherKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate = rsaCertificate(keyPair, MANAGED_URI);

      assertThrows(
          IllegalArgumentException.class,
          () ->
              DefaultCertificateGroup.forIdentity(
                  otherKeyPair,
                  chain(certificate),
                  new MemoryTrustListManager(),
                  new MemoryCertificateQuarantine(),
                  new CertificateValidator.InsecureCertificateValidator()));
    }
  }

  @Nested
  class IdentityCache {

    // SecureChannel and Session setup must reuse one selected client identity for a connection.
    @Test
    public void clientCachesSelectedCertificateIdentity() throws Exception {
      AtomicInteger selections = new AtomicInteger();
      CertificateIdentitySelector certificateIdentitySelector =
          context -> {
            selections.incrementAndGet();
            return Optional.empty();
          };
      OpcUaClientConfig config =
          builder()
              .setCertificateGroup(new TestCertificateGroup())
              .setCertificateIdentitySelector(certificateIdentitySelector)
              .build();
      OpcUaClient client = client(config);

      assertTrue(client.getCertificateIdentity(profile()).isEmpty());
      assertTrue(client.getCertificateIdentity(profile()).isEmpty());
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

      // Identity equality includes the owning group, so the same key material read back from three
      // groups gives three distinguishable identities.
      CertificateMaterial material = rsaMaterial(MANAGED_URI);
      CertificateIdentity identityA = soleIdentity(new TestCertificateGroup(material));
      CertificateIdentity identityB = soleIdentity(new TestCertificateGroup(material));
      CertificateIdentity identityU = soleIdentity(new TestCertificateGroup(material));

      // A stateful selector: the endpoint profile yields A on its first selection and B on every
      // selection after, so a re-invocation would observably swap the endpoint identity.
      AtomicInteger endpointSelections = new AtomicInteger();
      Map<SecurityPolicyProfile, CertificateIdentity> tokenIdentities =
          Map.of(tokenProfile, identityU);
      CertificateIdentitySelector certificateIdentitySelector =
          context -> {
            CertificateIdentity tokenIdentity =
                tokenIdentities.get(context.securityPolicyProfile());
            if (tokenIdentity != null) {
              return Optional.of(tokenIdentity);
            }
            return Optional.of(endpointSelections.getAndIncrement() == 0 ? identityA : identityB);
          };

      OpcUaClientConfig config =
          builder()
              .setCertificateGroup(identityA.certificateGroup())
              .setCertificateIdentitySelector(certificateIdentitySelector)
              .build();
      OpcUaClient client = client(config);

      assertSame(identityA, client.getCertificateIdentity(endpointProfile).orElseThrow());
      assertSame(identityU, client.getCertificateIdentity(tokenProfile).orElseThrow());
      assertSame(identityA, client.getCertificateIdentity(endpointProfile).orElseThrow());
      assertEquals(1, endpointSelections.get());
    }
  }

  @Nested
  class ApplicationUri {

    // setApplicationUri() is an explicit application identity choice and must override certificate
    // SAN URIs, including the URI on the selected identity.
    @Test
    public void explicitApplicationUriTakesPrecedence() throws Exception {
      CertificateGroup certificateGroup = new TestCertificateGroup(rsaMaterial(MANAGED_URI));
      OpcUaClientConfig config =
          builder()
              .setCertificateGroup(certificateGroup)
              .setApplicationUri("urn:eclipse:milo:test:explicit")
              .build();

      OpcUaClient client = client(config);

      assertEquals("urn:eclipse:milo:test:explicit", client.resolveApplicationUri(null));
      assertEquals(
          "urn:eclipse:milo:test:explicit",
          client.resolveApplicationUri(soleIdentity(certificateGroup)));
    }

    // The certificate presented on the connection defines the application instance; servers
    // compare its SAN URI with the ApplicationDescription (Bad_CertificateUriInvalid).
    @Test
    public void selectedIdentityApplicationUriIsUsed() throws Exception {
      CertificateGroup certificateGroup = new TestCertificateGroup(rsaMaterial(MANAGED_URI));
      OpcUaClientConfig config = builder().setCertificateGroup(certificateGroup).build();

      OpcUaClient client = client(config);

      assertEquals(MANAGED_URI, client.resolveApplicationUri(soleIdentity(certificateGroup)));
    }

    // A selected identity without a SAN URI cannot define the ApplicationUri, and no other source
    // may be substituted for the certificate actually presented.
    @Test
    public void selectedIdentityWithoutSanUriYieldsPlaceholder() throws Exception {
      CertificateGroup certificateGroup = new TestCertificateGroup(rsaMaterialWithoutSanUri());
      OpcUaClientConfig config = builder().setCertificateGroup(certificateGroup).build();

      OpcUaClient client = client(config);

      assertEquals(PLACEHOLDER_URI, client.resolveApplicationUri(soleIdentity(certificateGroup)));
    }

    // The placeholder remains a last resort for a client with no certificate group at all.
    @Test
    public void applicationUriPlaceholderIsUsedLast() {
      OpcUaClientConfig config = builder().build();

      OpcUaClient client = client(config);

      assertEquals(PLACEHOLDER_URI, client.resolveApplicationUri(null));
    }

    // A None endpoint presents no identity, but the client is still the same application. When
    // every identity in the group carries the same URI, that URI must be advertised instead of the
    // placeholder, so servers see one ApplicationUri across secure and None connections.
    @Test
    public void groupApplicationUriIsUsedWhenNoIdentityIsPresented() throws Exception {
      CertificateGroup certificateGroup =
          new TestCertificateGroup(
              rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, MANAGED_URI),
              rsaMaterial(NodeIds.RsaMinApplicationCertificateType, MANAGED_URI));
      OpcUaClientConfig config = builder().setCertificateGroup(certificateGroup).build();

      OpcUaClient client = client(config);

      assertEquals(MANAGED_URI, client.resolveApplicationUri(null));
    }

    // Group identities with differing URIs cannot define the application, so the placeholder
    // remains when no identity is presented.
    @Test
    public void differingGroupApplicationUrisFallBackToPlaceholder() throws Exception {
      CertificateGroup certificateGroup =
          new TestCertificateGroup(
              rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, "urn:eclipse:milo:test:a"),
              rsaMaterial(NodeIds.RsaMinApplicationCertificateType, "urn:eclipse:milo:test:b"));
      OpcUaClientConfig config = builder().setCertificateGroup(certificateGroup).build();

      OpcUaClient client = client(config);

      assertEquals(PLACEHOLDER_URI, client.resolveApplicationUri(null));
    }

    // URI inference is best-effort. A None connection must retain the placeholder when a custom
    // group cannot enumerate its identities, instead of failing session creation.
    @Test
    public void groupFailureFallsBackToPlaceholder() {
      CertificateGroup certificateGroup = mock(CertificateGroup.class);
      when(certificateGroup.getCertificateIdentities())
          .thenThrow(new IllegalStateException("identity store unavailable"));
      when(certificateGroup.getCertificateValidator())
          .thenReturn(new CertificateValidator.InsecureCertificateValidator());
      OpcUaClientConfig config = builder().setCertificateGroup(certificateGroup).build();

      OpcUaClient client = client(config);

      assertEquals(PLACEHOLDER_URI, client.resolveApplicationUri(null));
    }

    // SecureChannel setup selects first. Session creation must derive the URI from that cached
    // identity even when other identities in the group carry a different ApplicationUri.
    @Test
    public void applicationUriUsesCachedSelectedIdentityWhenGroupUrisDiffer() throws Exception {
      CertificateGroup certificateGroup =
          new TestCertificateGroup(
              rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, "urn:eclipse:milo:test:a"),
              rsaMaterial(NodeIds.RsaMinApplicationCertificateType, "urn:eclipse:milo:test:b"));
      CertificateIdentity identityA =
          identity(certificateGroup, NodeIds.RsaSha256ApplicationCertificateType);
      CertificateIdentity identityB =
          identity(certificateGroup, NodeIds.RsaMinApplicationCertificateType);
      AtomicInteger selections = new AtomicInteger();
      CertificateIdentitySelector selector =
          context -> Optional.of(selections.getAndIncrement() == 0 ? identityA : identityB);
      OpcUaClientConfig config =
          builder()
              .setCertificateGroup(certificateGroup)
              .setCertificateIdentitySelector(selector)
              .build();

      OpcUaClient client = client(config);

      assertSame(identityA, client.getCertificateIdentity(profile()).orElseThrow());
      assertEquals(
          "urn:eclipse:milo:test:a",
          client.resolveApplicationUri(client.getCertificateIdentity(profile()).orElse(null)));
      assertEquals(1, selections.get());
    }

    // CreateSession must send the URI from the same effective identity whose certificate it places
    // in the request, otherwise servers reject ActivateSession with Bad_CertificateUriInvalid.
    @Test
    public void createSessionUsesSelectedIdentityApplicationUri() throws Exception {
      CertificateGroup certificateGroup = new TestCertificateGroup(rsaMaterial(MANAGED_URI));
      CertificateIdentity identity = soleIdentity(certificateGroup);
      OpcUaClientConfig config =
          OpcUaClientConfig.builder()
              .setEndpoint(secureEndpoint(identity.certificate()))
              .setCertificateGroup(certificateGroup)
              .build();
      CapturingClientTransport transport = new CapturingClientTransport();
      OpcUaClient client = new OpcUaClient(config, transport);
      CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

      try {
        CreateSessionRequest request = transport.createSessionRequest.get(5, TimeUnit.SECONDS);

        assertEquals(MANAGED_URI, request.getClientDescription().getApplicationUri());
        assertArrayEquals(
            identity.certificate().getEncoded(), request.getClientCertificate().bytes());
      } finally {
        transport.releasePending();
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
        connectFuture.cancel(true);
      }
    }
  }

  private OpcUaClientConfigBuilder builder() {
    return OpcUaClientConfig.builder().setEndpoint(endpoint);
  }

  private static CertificateIdentity soleIdentity(CertificateGroup certificateGroup) {
    List<CertificateIdentity> identities = certificateGroup.getCertificateIdentities();
    assertEquals(1, identities.size(), "expected a group of one");
    return identities.get(0);
  }

  private static CertificateIdentity identity(
      CertificateGroup certificateGroup, NodeId certificateTypeId) {

    return certificateGroup.getCertificateIdentities().stream()
        .filter(identity -> certificateTypeId.equals(identity.certificateTypeId()))
        .findFirst()
        .orElseThrow();
  }

  private static CertificateMaterial rsaMaterial(String applicationUri) throws Exception {
    return rsaMaterial(NodeIds.RsaSha256ApplicationCertificateType, applicationUri);
  }

  private static CertificateMaterial rsaMaterial(NodeId certificateTypeId, String applicationUri)
      throws Exception {

    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    return new CertificateMaterial(
        certificateTypeId, keyPair, chain(rsaCertificate(keyPair, applicationUri)));
  }

  private static CertificateMaterial rsaMaterialWithoutSanUri() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("certificate-group-test")
            .setApplicationUri(null)
            .addDnsName("localhost")
            .build();
    return new CertificateMaterial(
        NodeIds.RsaSha256ApplicationCertificateType, keyPair, chain(certificate));
  }

  private static CertificateMaterial eccNistP256Material(String applicationUri) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setCommonName("certificate-group-test")
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .build();
    return new CertificateMaterial(
        NodeIds.EccNistP256ApplicationCertificateType, keyPair, chain(certificate));
  }

  private static X509Certificate rsaCertificate(KeyPair keyPair, String applicationUri)
      throws Exception {

    return new SelfSignedCertificateBuilder(keyPair)
        .setCommonName("certificate-group-test")
        .setApplicationUri(applicationUri)
        .addDnsName("localhost")
        .build();
  }

  private static X509Certificate[] chain(X509Certificate certificate) {
    return new X509Certificate[] {certificate};
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

  private record CertificateMaterial(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {

    X509Certificate certificate() {
      return certificateChain[0];
    }
  }

  /**
   * A {@link CertificateGroup} over fixed in-memory identities, so a test can compose the client's
   * single group from any mix of certificate types without a store or trust list. Identities are
   * read back through {@link #getCertificateIdentities()} because identity equality includes the
   * owning group.
   */
  private static final class TestCertificateGroup implements CertificateGroup {

    private final CertificateValidator certificateValidator;
    private final List<CertificateMaterial> certificates;

    TestCertificateGroup(CertificateMaterial... certificates) {
      this(new CertificateValidator.InsecureCertificateValidator(), certificates);
    }

    TestCertificateGroup(
        CertificateValidator certificateValidator, CertificateMaterial... certificates) {

      this.certificateValidator = certificateValidator;
      this.certificates = List.of(certificates);
    }

    @Override
    public List<NodeId> getSupportedCertificateTypeIds() {
      return certificates.stream().map(CertificateMaterial::certificateTypeId).distinct().toList();
    }

    @Override
    public TrustListManager getTrustListManager() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateQuarantine getCertificateQuarantine() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Entry> getCertificateEntries() {
      return certificates.stream()
          .map(c -> new Entry(c.certificateTypeId(), c.certificateChain()))
          .toList();
    }

    @Override
    public boolean hasCertificate(NodeId certificateTypeId) {
      return find(certificateTypeId).isPresent();
    }

    @Override
    public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
      return find(certificateTypeId).map(CertificateMaterial::keyPair);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
      return find(certificateTypeId)
          .map(CertificateMaterial::certificateChain)
          .map(X509Certificate[]::clone);
    }

    @Override
    public void updateCertificate(
        NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return certificateValidator;
    }

    private Optional<CertificateMaterial> find(NodeId certificateTypeId) {
      return certificates.stream()
          .filter(c -> certificateTypeId.equals(c.certificateTypeId()))
          .findFirst();
    }
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
