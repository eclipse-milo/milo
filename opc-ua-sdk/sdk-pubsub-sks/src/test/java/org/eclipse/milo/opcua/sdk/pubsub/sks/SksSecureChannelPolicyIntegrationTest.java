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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.ServerSocket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.InMemoryKeyCredentialStore;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * SKS pulls over the SecureChannel policy families beyond the RSA-OAEP/Basic256Sha256 baseline: ECC
 * (NIST P-256 and Curve25519), RSA-DH, and RSA-PSS.
 *
 * <p>The pull client opens an ordinary SecureChannel and passes its application instance identity
 * through {@code setCertificateIdentity}, so the client infers the certificate type from the leaf
 * certificate and only offers the identity for policies that type supports. These tests run the
 * real {@link SksTestServer} (real GetSecurityKeys, real authorization) with one SignAndEncrypt
 * endpoint per policy and pull with a username identity, so each run also protects the token secret
 * with the endpoint's own policy.
 *
 * <p>The returned key material is PubSub-Aes256-CTR in every case: PubSub message security is
 * independent of the SecureChannel policy used to fetch the keys.
 */
class SksSecureChannelPolicyIntegrationTest {

  private static final Duration TIMEOUT = Duration.ofSeconds(30);

  private static final String CLIENT_APP_URI = "urn:eclipse:milo:test:sks-channel-policy-client";

  private static final String SECURITY_GROUP = "sks-channel-policy-group";

  private static final PubSubSecurityPolicy KEY_POLICY = PubSubSecurityPolicy.PubSubAes256Ctr;

  // Basic256Sha256 and Aes256_Sha256_RsaPss rank 0x88, above every P-256 and Curve25519 ECC policy
  // (0x84), so this endpoint set is the one where best-available ranking alone picks the wrong
  // family for an ECC client.
  private static final List<SecurityPolicy> SKS_POLICIES =
      List.of(
          SecurityPolicy.Basic256Sha256,
          SecurityPolicy.Aes256_Sha256_RsaPss,
          SecurityPolicy.RSA_DH_AesGcm,
          SecurityPolicy.ECC_nistP256_AesGcm,
          SecurityPolicy.ECC_curve25519_ChaChaPoly);

  private static SksTestServer sks;
  private static SksTestIdentity rsaClient;
  private static SksTestIdentity nistP256Client;
  private static SksTestIdentity curve25519Client;
  private static MemoryTrustListManager clientTrustList;

  @BeforeAll
  static void startSksServer() throws Exception {
    rsaClient =
        SksTestIdentity.generate(
            NodeIds.RsaSha256ApplicationCertificateType, "SKS RSA Client", CLIENT_APP_URI);
    nistP256Client =
        SksTestIdentity.generate(
            NodeIds.EccNistP256ApplicationCertificateType, "SKS P-256 Client", CLIENT_APP_URI);
    curve25519Client =
        SksTestIdentity.generate(
            NodeIds.EccCurve25519ApplicationCertificateType,
            "SKS Curve25519 Client",
            CLIENT_APP_URI);

    sks =
        SksTestServer.create(
            freeTcpPort(),
            List.of(
                rsaClient.certificate(),
                nistP256Client.certificate(),
                curve25519Client.certificate()),
            serverPubSubConfig(),
            SKS_POLICIES);

    clientTrustList = new MemoryTrustListManager();
    sks.getCertificates().forEach(clientTrustList::addTrustedCertificate);
  }

  @AfterAll
  static void stopSksServer() throws Exception {
    if (sks != null) {
      sks.close();
    }
  }

  // Each policy family takes a different SecureChannel and token-secret path (RSA-PSS signatures,
  // RSA-DH and ECC key agreement, Ed25519 signatures), and the client certificate type must match
  // the family for the identity to be offered at all.
  @ParameterizedTest(name = "{0}")
  @MethodSource("pinnedPolicies")
  void pullsKeysOverPinnedSecurityPolicy(SecurityPolicy securityPolicy, NodeId clientType)
      throws Exception {

    var selected = new AtomicReference<EndpointDescription>();

    try (SksSecurityKeyProvider provider =
        providerBuilder(clientIdentity(clientType))
            .keyServices(List.of(serverEntry(securityPolicy.getUri())))
            .endpointTransform(recordInto(selected))
            .build()) {

      assertKeysPulled(provider);
    }

    assertEquals(securityPolicy.getUri(), selected.get().getSecurityPolicyUri());
  }

  // Regression: with no SecurityPolicyUri constraint the provider ranked every SignAndEncrypt
  // endpoint by securityLevel alone and picked an RSA policy, which a P-256 identity cannot secure.
  // The client then failed with Bad_ConfigurationError on every fetch, although the SKS offered a
  // P-256 endpoint.
  @Test
  void bestAvailableSelectsPolicyTheEccCertificateCanSecure() throws Exception {
    var selected = new AtomicReference<EndpointDescription>();

    try (SksSecurityKeyProvider provider =
        providerBuilder(nistP256Client)
            .keyServices(List.of(serverEntry(null)))
            .endpointTransform(recordInto(selected))
            .build()) {

      assertKeysPulled(provider);
    }

    assertEquals(
        SecurityPolicy.ECC_nistP256_AesGcm.getUri(), selected.get().getSecurityPolicyUri());
  }

  // Control for the filter above: an RSA identity still ranks among the RSA policies and gets the
  // highest-level one rather than being filtered down to nothing.
  @Test
  void bestAvailableSelectsHighestLevelPolicyTheRsaCertificateCanSecure() throws Exception {
    var selected = new AtomicReference<EndpointDescription>();

    try (SksSecurityKeyProvider provider =
        providerBuilder(rsaClient)
            .keyServices(List.of(serverEntry(null)))
            .endpointTransform(recordInto(selected))
            .build()) {

      assertKeysPulled(provider);
    }

    assertTrue(
        Set.of(SecurityPolicy.Basic256Sha256.getUri(), SecurityPolicy.Aes256_Sha256_RsaPss.getUri())
            .contains(selected.get().getSecurityPolicyUri()),
        "selected: " + selected.get().getSecurityPolicyUri());
  }

  // A pinned policy is the operator's choice, so resolution does not second-guess it. When the
  // identity cannot secure that policy the client rejects it before OpenSecureChannel with
  // Bad_ConfigurationError, rather than the SKS rejecting the certificate.
  @ParameterizedTest(name = "{0}")
  @MethodSource("mismatchedPolicies")
  void pinnedPolicyTheCertificateCannotSecureFailsClientSide(
      SecurityPolicy securityPolicy, NodeId clientType) throws Exception {

    try (SksSecurityKeyProvider provider =
        providerBuilder(clientIdentity(clientType))
            .keyServices(List.of(serverEntry(securityPolicy.getUri())))
            .build()) {

      ExecutionException e =
          assertThrows(
              ExecutionException.class,
              () ->
                  provider
                      .getKeys(SECURITY_GROUP, uint(0), uint(1))
                      .get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));

      UaException cause = assertInstanceOf(UaException.class, e.getCause());
      assertEquals(
          StatusCodes.Bad_ConfigurationError,
          cause.getStatusCode().value(),
          "status: " + cause.getStatusCode());

      // The transport wraps the client-side rejection in a pipeline exception, so the reason
      // naming the policy is further down the cause chain.
      List<String> messages = causeChainMessages(cause);
      assertTrue(
          messages.stream()
              .anyMatch(
                  m ->
                      m.contains("no certificate identity for security policy")
                          && m.contains(securityPolicy.getUri())),
          String.join("\n", messages));
    }
  }

  private static Stream<Arguments> pinnedPolicies() {
    return Stream.of(
        Arguments.of(
            SecurityPolicy.Aes256_Sha256_RsaPss, NodeIds.RsaSha256ApplicationCertificateType),
        Arguments.of(SecurityPolicy.RSA_DH_AesGcm, NodeIds.RsaSha256ApplicationCertificateType),
        Arguments.of(
            SecurityPolicy.ECC_nistP256_AesGcm, NodeIds.EccNistP256ApplicationCertificateType),
        Arguments.of(
            SecurityPolicy.ECC_curve25519_ChaChaPoly,
            NodeIds.EccCurve25519ApplicationCertificateType));
  }

  private static Stream<Arguments> mismatchedPolicies() {
    return Stream.of(
        Arguments.of(
            SecurityPolicy.ECC_nistP256_AesGcm, NodeIds.RsaSha256ApplicationCertificateType),
        Arguments.of(
            SecurityPolicy.Aes256_Sha256_RsaPss, NodeIds.EccNistP256ApplicationCertificateType));
  }

  private static void assertKeysPulled(SksSecurityKeyProvider provider) throws Exception {
    SecurityKeySet keySet =
        provider
            .getKeys(SECURITY_GROUP, uint(0), uint(1))
            .get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    assertEquals(KEY_POLICY.getUri(), keySet.securityPolicyUri());
    assertFalse(keySet.keys().isEmpty(), "no keys returned");
  }

  private static List<String> causeChainMessages(Throwable throwable) {
    var messages = new ArrayList<String>();
    for (Throwable t = throwable; t != null; t = t.getCause()) {
      messages.add(String.valueOf(t.getMessage()));
    }
    return messages;
  }

  private static SksTestIdentity clientIdentity(NodeId certificateTypeId) {
    return Stream.of(rsaClient, nistP256Client, curve25519Client)
        .filter(identity -> identity.certificateTypeId().equals(certificateTypeId))
        .findFirst()
        .orElseThrow();
  }

  private static UnaryOperator<EndpointDescription> recordInto(
      AtomicReference<EndpointDescription> selected) {

    return endpoint -> {
      assertNull(selected.getAndSet(endpoint), "endpoint resolved more than once");
      return endpoint;
    };
  }

  /** The server-side config: the SecurityGroup the real face generates keys for. */
  private static PubSubConfig serverPubSubConfig() {
    return PubSubConfig.builder()
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP)
                .securityPolicyUri(KEY_POLICY.getUri())
                .keyLifeTime(Duration.ofMinutes(10))
                .maxFutureKeyCount(uint(1))
                .maxPastKeyCount(uint(1))
                .build())
        .build();
  }

  private static SksSecurityKeyProvider.Builder providerBuilder(SksTestIdentity client) {
    var credentialStore = new InMemoryKeyCredentialStore();
    credentialStore.put(
        SksTestServer.APPLICATION_URI,
        SksTestServer.AUTHORIZED_USERNAME,
        SksTestServer.AUTHORIZED_PASSWORD.toCharArray());

    return SksSecurityKeyProvider.builder()
        .keyPair(client.keyPair())
        .certificate(client.certificate())
        .applicationUri(CLIENT_APP_URI)
        .trustListManager(clientTrustList)
        .keyCredentialStore(credentialStore)
        .requestTimeout(Duration.ofSeconds(15));
  }

  /**
   * A Table 40 identity record for the SKS, using a username identity.
   *
   * @param securityPolicyUri the SecurityPolicyUri constraint, or null for "best available".
   */
  private static EndpointDescription serverEntry(@Nullable String securityPolicyUri) {
    return new EndpointDescription(
        null,
        new ApplicationDescription(
            SksTestServer.APPLICATION_URI,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {sks.getDiscoveryUrl()}),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        securityPolicyUri,
        new UserTokenPolicy[] {
          new UserTokenPolicy("username", UserTokenType.UserName, null, null, null)
        },
        null,
        ubyte(0));
  }

  private static int freeTcpPort() throws Exception {
    try (var serverSocket = new ServerSocket(0)) {
      return serverSocket.getLocalPort();
    }
  }
}
