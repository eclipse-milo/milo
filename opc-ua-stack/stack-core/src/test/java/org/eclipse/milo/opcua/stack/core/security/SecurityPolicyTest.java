/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_BRAINPOOL_P256R1_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_BRAINPOOL_P384R1_SHA384;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_NIST_P256_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_NIST_P384_SHA384;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ED25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ED448;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.AES_GCM;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.CHACHA20_POLY1305;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_BRAINPOOL_P256R1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_BRAINPOOL_P384R1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P384;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.FFDH_3072;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.X25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.X448;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.SequenceNumberMode.NON_LEGACY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@NullMarked
public class SecurityPolicyTest {

  // The original RSA-era policies are the only ones whose legacy algorithm getters are non-null.
  private static final Set<SecurityPolicy> LEGACY_POLICIES =
      EnumSet.of(
          SecurityPolicy.None,
          SecurityPolicy.Basic128Rsa15,
          SecurityPolicy.Basic256,
          SecurityPolicy.Basic256Sha256,
          SecurityPolicy.Aes128_Sha256_RsaOaep,
          SecurityPolicy.Aes256_Sha256_RsaPss);

  // RSA-DH is enhanced (non-legacy) but still uses RSA application certificates, so unlike the ECC
  // policies it keeps a non-null certificate-signature axis.
  private static final Set<SecurityPolicy> RSA_DH_POLICIES =
      EnumSet.of(SecurityPolicy.RSA_DH_AesGcm, SecurityPolicy.RSA_DH_ChaChaPoly);

  // Every current enhanced policy carries ephemeral public keys in ClientNonce/ServerNonce (ECDH,
  // XDH, or ffdhe3072) and protects symmetric chunks with an AEAD cipher. The two facts coincide
  // for the 14 policies that exist today, but they are pinned independently so a future policy that
  // breaks the coincidence (e.g. an ephemeral CBC policy) is caught rather than assumed.
  private static final Set<SecurityPolicy> EPHEMERAL_POLICIES =
      EnumSet.of(
          SecurityPolicy.ECC_nistP256_AesGcm,
          SecurityPolicy.ECC_nistP256_ChaChaPoly,
          SecurityPolicy.ECC_nistP384_AesGcm,
          SecurityPolicy.ECC_nistP384_ChaChaPoly,
          SecurityPolicy.ECC_brainpoolP256r1_AesGcm,
          SecurityPolicy.ECC_brainpoolP256r1_ChaChaPoly,
          SecurityPolicy.ECC_curve25519_AesGcm,
          SecurityPolicy.ECC_curve25519_ChaChaPoly,
          SecurityPolicy.ECC_curve448_AesGcm,
          SecurityPolicy.ECC_curve448_ChaChaPoly,
          SecurityPolicy.ECC_brainpoolP384r1_AesGcm,
          SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly,
          SecurityPolicy.RSA_DH_AesGcm,
          SecurityPolicy.RSA_DH_ChaChaPoly);

  private static final Set<SecurityPolicy> AEAD_POLICIES = EnumSet.copyOf(EPHEMERAL_POLICIES);

  @Test
  public void testPolicyUriLookupRecognizesCurrentEnhancedPolicies() throws UaException {
    assertSame(
        SecurityPolicy.ECC_nistP256_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_nistP256_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.ECC_nistP256_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_nistP256_ChaChaPoly.getUri()));

    assertSame(
        SecurityPolicy.ECC_nistP384_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_nistP384_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.ECC_nistP384_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_nistP384_ChaChaPoly.getUri()));

    assertSame(
        SecurityPolicy.ECC_brainpoolP256r1_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_brainpoolP256r1_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.ECC_brainpoolP256r1_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_brainpoolP256r1_ChaChaPoly.getUri()));

    assertSame(
        SecurityPolicy.ECC_curve25519_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_curve25519_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.ECC_curve25519_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_curve25519_ChaChaPoly.getUri()));

    assertSame(
        SecurityPolicy.ECC_curve448_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_curve448_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.ECC_curve448_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_curve448_ChaChaPoly.getUri()));

    assertSame(
        SecurityPolicy.ECC_brainpoolP384r1_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_brainpoolP384r1_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly.getUri()));

    assertSame(
        SecurityPolicy.RSA_DH_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.RSA_DH_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.RSA_DH_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.RSA_DH_ChaChaPoly.getUri()));
  }

  // The public enum intentionally exposes only executable target policies. Deprecated umbrella URIs
  // remain unknown so endpoint discovery and policy selection cannot silently choose incomplete
  // tuples.
  @Test
  @SuppressWarnings("HttpUrlsUsage")
  public void testDeprecatedEccPoliciesAreNotExposed() {
    Set<String> eccPolicyNames =
        Arrays.stream(SecurityPolicy.values())
            .map(Enum::name)
            .filter(name -> name.startsWith("ECC_"))
            .collect(Collectors.toSet());

    assertEquals(
        Set.of(
            "ECC_nistP256_AesGcm",
            "ECC_nistP256_ChaChaPoly",
            "ECC_nistP384_AesGcm",
            "ECC_nistP384_ChaChaPoly",
            "ECC_brainpoolP256r1_AesGcm",
            "ECC_brainpoolP256r1_ChaChaPoly",
            "ECC_curve25519_AesGcm",
            "ECC_curve25519_ChaChaPoly",
            "ECC_curve448_AesGcm",
            "ECC_curve448_ChaChaPoly",
            "ECC_brainpoolP384r1_AesGcm",
            "ECC_brainpoolP384r1_ChaChaPoly"),
        eccPolicyNames);

    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP256")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP384")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_curve25519")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_curve448")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP256r1")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP384r1")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe(
                "http://opcfoundation.org/UA/SecurityPolicy#ECC_curve25519_ChaCha20Poly1305")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe(
                "http://opcfoundation.org/UA/SecurityPolicy#ECC_curve448_ChaCha20Poly1305")
            .isEmpty());
  }

  @Test
  public void testAllPublicPoliciesHaveProfiles() {
    Set<SecurityPolicy> profiledPolicies =
        SecurityPolicyProfiles.values().stream()
            .map(SecurityPolicyProfile::securityPolicy)
            .collect(Collectors.toSet());

    assertEquals(EnumSet.allOf(SecurityPolicy.class), profiledPolicies);
  }

  @Test
  public void testExistingRsaPolicyDelegatesToEquivalentProfileMetadata() {
    SecurityPolicyProfile profile =
        SecurityPolicyProfiles.get(SecurityPolicy.Aes128_Sha256_RsaOaep);

    assertSame(
        profile.symmetricSignatureAlgorithm(),
        SecurityPolicy.Aes128_Sha256_RsaOaep.getSymmetricSignatureAlgorithm());
    assertSame(
        profile.symmetricEncryptionAlgorithm(),
        SecurityPolicy.Aes128_Sha256_RsaOaep.getSymmetricEncryptionAlgorithm());
    assertSame(
        profile.asymmetricSignatureAlgorithm(),
        SecurityPolicy.Aes128_Sha256_RsaOaep.getAsymmetricSignatureAlgorithm());
    assertSame(
        profile.asymmetricEncryptionAlgorithm(),
        SecurityPolicy.Aes128_Sha256_RsaOaep.getAsymmetricEncryptionAlgorithm());
    assertSame(
        profile.asymmetricKeyWrapAlgorithm(),
        SecurityPolicy.Aes128_Sha256_RsaOaep.getAsymmetricKeyWrapAlgorithm());
    assertSame(
        profile.keyDerivationAlgorithm(),
        SecurityPolicy.Aes128_Sha256_RsaOaep.getKeyDerivationAlgorithm());
    assertSame(
        profile.certificateSignatureAlgorithm(),
        SecurityPolicy.Aes128_Sha256_RsaOaep.getCertificateSignatureAlgorithm());

    assertEquals(32, profile.secureChannelNonceLength());
    assertEquals(32, profile.symmetricSignatureSize());
    assertEquals(32, profile.symmetricSignatureKeySize());
    assertEquals(16, profile.symmetricEncryptionKeySize());
    assertEquals(16, profile.symmetricBlockSize());
    assertFalse(profile.secureChannelEnhancements());
    assertTrue(profile.secureChannelSupported());
  }

  @Test
  public void testNonePolicyKeepsNoneSentinelAlgorithms() {
    assertSame(SecurityAlgorithm.None, SecurityPolicy.None.getSymmetricSignatureAlgorithm());
    assertSame(SecurityAlgorithm.None, SecurityPolicy.None.getSymmetricEncryptionAlgorithm());
    assertSame(SecurityAlgorithm.None, SecurityPolicy.None.getAsymmetricSignatureAlgorithm());
    assertSame(SecurityAlgorithm.None, SecurityPolicy.None.getAsymmetricEncryptionAlgorithm());
    assertSame(SecurityAlgorithm.None, SecurityPolicy.None.getAsymmetricKeyWrapAlgorithm());
    assertSame(SecurityAlgorithm.None, SecurityPolicy.None.getKeyDerivationAlgorithm());
    assertSame(SecurityAlgorithm.None, SecurityPolicy.None.getCertificateSignatureAlgorithm());
    assertSame(
        SecurityAlgorithm.None, SecurityPolicy.None.getProfile().certificateThumbprintAlgorithm());
  }

  // isLegacy() must partition the enum exactly into the six RSA-era policies and the enhanced
  // ECC/RSA-DH policies, so it stays in sync with which constants expose non-null legacy getters.
  @ParameterizedTest
  @EnumSource(SecurityPolicy.class)
  public void testIsLegacyMatchesRsaEraPolicies(SecurityPolicy policy) {
    assertEquals(LEGACY_POLICIES.contains(policy), policy.isLegacy());
  }

  // Legacy policies must keep their legacy algorithm axes non-null; this is the API contract
  // callers
  // relied on before the enhanced policies were added, and the getters/optionals must agree.
  @ParameterizedTest
  @MethodSource("legacyPolicies")
  public void testLegacyPolicyAlgorithmGettersAreNonNull(SecurityPolicy policy) {
    assertNotNull(policy.getSymmetricSignatureAlgorithm());
    assertNotNull(policy.getSymmetricEncryptionAlgorithm());
    assertNotNull(policy.getKeyDerivationAlgorithm());
    assertNotNull(policy.getCertificateSignatureAlgorithm());

    assertEquals(
        policy.getSymmetricSignatureAlgorithm(),
        policy.findSymmetricSignatureAlgorithm().orElseThrow());
    assertEquals(
        policy.getSymmetricEncryptionAlgorithm(),
        policy.findSymmetricEncryptionAlgorithm().orElseThrow());
    assertEquals(
        policy.getKeyDerivationAlgorithm(), policy.findKeyDerivationAlgorithm().orElseThrow());
    assertEquals(
        policy.getCertificateSignatureAlgorithm(),
        policy.findCertificateSignatureAlgorithm().orElseThrow());
  }

  // The enhanced policies report null for the legacy symmetric/key-derivation axes, so embedders
  // that enumerate values() and call these getters would NPE without first filtering on isLegacy().
  @ParameterizedTest
  @MethodSource("enhancedPolicies")
  public void testEnhancedPolicyLegacyAlgorithmGettersAreNull(SecurityPolicy policy) {
    assertNull(policy.getSymmetricSignatureAlgorithm());
    assertNull(policy.getSymmetricEncryptionAlgorithm());
    assertNull(policy.getKeyDerivationAlgorithm());

    assertTrue(policy.findSymmetricSignatureAlgorithm().isEmpty());
    assertTrue(policy.findSymmetricEncryptionAlgorithm().isEmpty());
    assertTrue(policy.findKeyDerivationAlgorithm().isEmpty());
  }

  // The ECC policies describe certificate compatibility through profile metadata, so the
  // certificate-signature getter is null for them. This is the only legacy axis where the enhanced
  // families diverge, so pinning it keeps the documented nullability matrix honest.
  @ParameterizedTest
  @MethodSource("eccPolicies")
  public void testEccPolicyCertificateSignatureGetterIsNull(SecurityPolicy policy) {
    assertNull(policy.getCertificateSignatureAlgorithm());
    assertTrue(policy.findCertificateSignatureAlgorithm().isEmpty());
  }

  // RSA-DH still authenticates with RSA application certificates, so its certificate-signature
  // getter reports Sha256 (non-null) even though it is an enhanced, non-legacy policy. The Javadoc
  // nullability matrix documents exactly this exception.
  @ParameterizedTest
  @MethodSource("rsaDhPolicies")
  public void testRsaDhPolicyCertificateSignatureGetterIsSha256(SecurityPolicy policy) {
    assertSame(SecurityAlgorithm.Sha256, policy.getCertificateSignatureAlgorithm());
    assertSame(SecurityAlgorithm.Sha256, policy.findCertificateSignatureAlgorithm().orElseThrow());
  }

  // The whole point of isLegacy(): enumerating every constant and reading the nullable legacy
  // getters only on the legacy ones must never throw, even as the enum grows.
  @Test
  public void testEnumerationFilteredByIsLegacyNeverThrows() {
    assertDoesNotThrow(
        () ->
            Arrays.stream(SecurityPolicy.values())
                .filter(SecurityPolicy::isLegacy)
                .forEach(
                    policy -> {
                      assertNotNull(policy.getSymmetricSignatureAlgorithm());
                      assertNotNull(policy.getSymmetricEncryptionAlgorithm());
                      assertNotNull(policy.getKeyDerivationAlgorithm());
                      assertNotNull(policy.getCertificateSignatureAlgorithm());
                    }));
  }

  // These tuples are the public policy contract that endpoint advertisement, certificate
  // selection, OpenSecureChannel, chunk codecs, and username-token encryption all share.
  @ParameterizedTest
  @MethodSource("currentEccProfileExpectations")
  void testCurrentEccProfileMetadata(PolicyProfileExpectation expectation) {
    SecurityPolicyProfile profile = SecurityPolicyProfiles.get(expectation.securityPolicy());

    assertEquals(expectation.authAxis(), profile.authAxis());
    assertEquals(List.of(expectation.certificateTypeId()), profile.certificateTypeIds());
    assertEquals(
        expectation.certificateTypeId(), profile.preferredCertificateTypeId().orElseThrow());
    assertEquals(expectation.keyAgreementAxis(), profile.keyAgreementAxis());
    assertEquals(expectation.chunkProtectionAxis(), profile.chunkProtectionAxis());
    assertEquals(NON_LEGACY, profile.sequenceNumberMode());
    assertEquals(
        expectation.certificateThumbprintAlgorithm(), profile.certificateThumbprintAlgorithm());
    assertEquals(expectation.secureChannelNonceLength(), profile.secureChannelNonceLength());
    assertEquals(16, profile.symmetricSignatureSize());
    assertEquals(0, profile.symmetricSignatureKeySize());
    assertEquals(expectation.symmetricEncryptionKeySize(), profile.symmetricEncryptionKeySize());
    assertEquals(1, profile.symmetricBlockSize());
    assertEquals(
        expectation.signAndEncryptSecurityLevel(),
        profile.getSecurityLevel(MessageSecurityMode.SignAndEncrypt));
    assertTrue(profile.secureChannelEnhancements());
    assertTrue(profile.secureChannelSupported());
  }

  @ParameterizedTest
  @MethodSource("rsaDhProfileExpectations")
  void testRsaDhProfileMetadata(PolicyProfileExpectation expectation) {
    SecurityPolicyProfile profile = SecurityPolicyProfiles.get(expectation.securityPolicy());

    assertEquals(expectation.authAxis(), profile.authAxis());
    assertEquals(List.of(expectation.certificateTypeId()), profile.certificateTypeIds());
    assertEquals(
        expectation.certificateTypeId(), profile.preferredCertificateTypeId().orElseThrow());
    assertEquals(expectation.keyAgreementAxis(), profile.keyAgreementAxis());
    assertEquals(expectation.chunkProtectionAxis(), profile.chunkProtectionAxis());
    assertEquals(NON_LEGACY, profile.sequenceNumberMode());
    assertEquals(SecurityAlgorithm.RsaSha256, profile.asymmetricSignatureAlgorithm());
    assertEquals(SecurityAlgorithm.None, profile.asymmetricEncryptionAlgorithm());
    assertEquals(SecurityAlgorithm.Sha256, profile.certificateSignatureAlgorithm());
    assertEquals(SecurityAlgorithm.Sha256, profile.certificateThumbprintAlgorithm());
    assertEquals(expectation.secureChannelNonceLength(), profile.secureChannelNonceLength());
    assertEquals(16, profile.symmetricSignatureSize());
    assertEquals(0, profile.symmetricSignatureKeySize());
    assertEquals(expectation.symmetricEncryptionKeySize(), profile.symmetricEncryptionKeySize());
    assertEquals(1, profile.symmetricBlockSize());
    assertEquals(
        expectation.signAndEncryptSecurityLevel(),
        profile.getSecurityLevel(MessageSecurityMode.SignAndEncrypt));
    assertTrue(profile.secureChannelEnhancements());
    assertTrue(profile.secureChannelSupported());
  }

  @Test
  public void testSecurityLevelMetadata() {
    assertEquals(
        (short) 0x20, SecurityPolicy.None.getProfile().getSecurityLevel(MessageSecurityMode.None));
    assertEquals(
        (short) 0x41,
        SecurityPolicy.Basic128Rsa15.getProfile().getSecurityLevel(MessageSecurityMode.Sign));
    assertEquals(
        (short) 0x84,
        SecurityPolicy.Aes128_Sha256_RsaOaep.getProfile()
            .getSecurityLevel(MessageSecurityMode.SignAndEncrypt));
    assertEquals(
        (short) 0x88,
        SecurityPolicy.Aes256_Sha256_RsaPss.getProfile()
            .getSecurityLevel(MessageSecurityMode.SignAndEncrypt));
  }

  @Test
  public void testSecureChannelGateAllowsExecutablePolicies() {
    assertDoesNotThrow(
        () -> SecurityPolicyProfiles.requireSecureChannelSupported(SecurityPolicy.Basic256Sha256));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_nistP256_AesGcm));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_nistP256_ChaChaPoly));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_nistP384_AesGcm));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_nistP384_ChaChaPoly));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_brainpoolP256r1_AesGcm));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_brainpoolP256r1_ChaChaPoly));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_curve25519_AesGcm));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_curve25519_ChaChaPoly));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_curve448_AesGcm));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_curve448_ChaChaPoly));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_brainpoolP384r1_AesGcm));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(
                SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly));
    assertDoesNotThrow(
        () -> SecurityPolicyProfiles.requireSecureChannelSupported(SecurityPolicy.RSA_DH_AesGcm));
    assertDoesNotThrow(
        () ->
            SecurityPolicyProfiles.requireSecureChannelSupported(SecurityPolicy.RSA_DH_ChaChaPoly));
  }

  // These ephemeral-key-agreement and AEAD facts back the capability gates that endpoint
  // advertisement, the client OPN, and the server OPN all consult. Pinning the exact set of
  // ephemeral/AEAD policies means a profile edit (or a future policy) that flips either fact has to
  // change this expectation, instead of silently disappearing from advertisement or being treated
  // as a random-nonce policy by one of the handlers.
  @ParameterizedTest
  @EnumSource(SecurityPolicy.class)
  public void testEphemeralAndAeadProfileFactsArePinned(SecurityPolicy policy) {
    SecurityPolicyProfile profile = policy.getProfile();

    assertEquals(EPHEMERAL_POLICIES.contains(policy), profile.usesEphemeralKeyAgreement());
    assertEquals(AEAD_POLICIES.contains(policy), profile.usesAeadChunkProtection());
  }

  // The single capability gate must answer identically for every policy x mode. Pinning the exact
  // supported-mode set per policy keeps advertisement, the client OPN, and the server OPN agreeing
  // on which modes a policy accepts; a profile edit that changed the answer would have to update
  // this matrix rather than silently diverging one call site from the others.
  @ParameterizedTest
  @EnumSource(SecurityPolicy.class)
  public void testMessageSecurityModeSupportMatchesProfileCapabilities(SecurityPolicy policy) {
    SecurityPolicyProfile profile = policy.getProfile();
    Set<MessageSecurityMode> supportedModes = supportedModes(policy);

    for (MessageSecurityMode mode : MessageSecurityMode.values()) {
      assertEquals(
          supportedModes.contains(mode),
          profile.isMessageSecurityModeSupported(mode),
          policy + " / " + mode);
    }
  }

  // Legacy policies accept every mode; enhanced policies advertise SignAndEncrypt plus Sign (their
  // AEAD tag doubles as the chunk signature) and reject None/Invalid. All 14 current enhanced
  // policies are AEAD, so each accepts exactly {Sign, SignAndEncrypt}.
  private static Set<MessageSecurityMode> supportedModes(SecurityPolicy policy) {
    if (LEGACY_POLICIES.contains(policy)) {
      return EnumSet.allOf(MessageSecurityMode.class);
    }

    return EnumSet.of(MessageSecurityMode.Sign, MessageSecurityMode.SignAndEncrypt);
  }

  private static Stream<SecurityPolicy> legacyPolicies() {
    return LEGACY_POLICIES.stream();
  }

  private static Stream<SecurityPolicy> enhancedPolicies() {
    return EnumSet.complementOf(EnumSet.copyOf(LEGACY_POLICIES)).stream();
  }

  private static Stream<SecurityPolicy> rsaDhPolicies() {
    return RSA_DH_POLICIES.stream();
  }

  private static Stream<SecurityPolicy> eccPolicies() {
    EnumSet<SecurityPolicy> ecc = EnumSet.complementOf(EnumSet.copyOf(LEGACY_POLICIES));
    ecc.removeAll(RSA_DH_POLICIES);
    return ecc.stream();
  }

  private static Stream<PolicyProfileExpectation> currentEccProfileExpectations() {
    return Stream.of(
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_nistP256_AesGcm,
            ECDSA_NIST_P256_SHA256,
            NodeIds.EccNistP256ApplicationCertificateType,
            ECDH_NIST_P256,
            AES_GCM,
            64,
            16,
            SecurityAlgorithm.Sha256,
            (short) 0x84),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_nistP256_ChaChaPoly,
            ECDSA_NIST_P256_SHA256,
            NodeIds.EccNistP256ApplicationCertificateType,
            ECDH_NIST_P256,
            CHACHA20_POLY1305,
            64,
            32,
            SecurityAlgorithm.Sha256,
            (short) 0x84),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_nistP384_AesGcm,
            ECDSA_NIST_P384_SHA384,
            NodeIds.EccNistP384ApplicationCertificateType,
            ECDH_NIST_P384,
            AES_GCM,
            96,
            32,
            SecurityAlgorithm.Sha384,
            (short) 0x88),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_nistP384_ChaChaPoly,
            ECDSA_NIST_P384_SHA384,
            NodeIds.EccNistP384ApplicationCertificateType,
            ECDH_NIST_P384,
            CHACHA20_POLY1305,
            96,
            32,
            SecurityAlgorithm.Sha384,
            (short) 0x88),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_brainpoolP256r1_AesGcm,
            ECDSA_BRAINPOOL_P256R1_SHA256,
            NodeIds.EccBrainpoolP256r1ApplicationCertificateType,
            ECDH_BRAINPOOL_P256R1,
            AES_GCM,
            64,
            16,
            SecurityAlgorithm.Sha256,
            (short) 0x84),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_brainpoolP256r1_ChaChaPoly,
            ECDSA_BRAINPOOL_P256R1_SHA256,
            NodeIds.EccBrainpoolP256r1ApplicationCertificateType,
            ECDH_BRAINPOOL_P256R1,
            CHACHA20_POLY1305,
            64,
            32,
            SecurityAlgorithm.Sha256,
            (short) 0x84),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_curve25519_AesGcm,
            ED25519,
            NodeIds.EccCurve25519ApplicationCertificateType,
            X25519,
            AES_GCM,
            32,
            16,
            SecurityAlgorithm.Sha256,
            (short) 0x84),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_curve25519_ChaChaPoly,
            ED25519,
            NodeIds.EccCurve25519ApplicationCertificateType,
            X25519,
            CHACHA20_POLY1305,
            32,
            32,
            SecurityAlgorithm.Sha256,
            (short) 0x84),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_curve448_AesGcm,
            ED448,
            NodeIds.EccCurve448ApplicationCertificateType,
            X448,
            AES_GCM,
            56,
            32,
            SecurityAlgorithm.Sha384,
            (short) 0x88),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_curve448_ChaChaPoly,
            ED448,
            NodeIds.EccCurve448ApplicationCertificateType,
            X448,
            CHACHA20_POLY1305,
            56,
            32,
            SecurityAlgorithm.Sha384,
            (short) 0x88),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_brainpoolP384r1_AesGcm,
            ECDSA_BRAINPOOL_P384R1_SHA384,
            NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
            ECDH_BRAINPOOL_P384R1,
            AES_GCM,
            96,
            32,
            SecurityAlgorithm.Sha384,
            (short) 0x88),
        new PolicyProfileExpectation(
            SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly,
            ECDSA_BRAINPOOL_P384R1_SHA384,
            NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
            ECDH_BRAINPOOL_P384R1,
            CHACHA20_POLY1305,
            96,
            32,
            SecurityAlgorithm.Sha384,
            (short) 0x88));
  }

  private static Stream<PolicyProfileExpectation> rsaDhProfileExpectations() {
    return Stream.of(
        new PolicyProfileExpectation(
            SecurityPolicy.RSA_DH_AesGcm,
            RSA_PKCS1_SHA256,
            NodeIds.RsaSha256ApplicationCertificateType,
            FFDH_3072,
            AES_GCM,
            384,
            16,
            SecurityAlgorithm.Sha256,
            (short) 0x84),
        new PolicyProfileExpectation(
            SecurityPolicy.RSA_DH_ChaChaPoly,
            RSA_PKCS1_SHA256,
            NodeIds.RsaSha256ApplicationCertificateType,
            FFDH_3072,
            CHACHA20_POLY1305,
            384,
            32,
            SecurityAlgorithm.Sha256,
            (short) 0x84));
  }

  private record PolicyProfileExpectation(
      SecurityPolicy securityPolicy,
      SecurityPolicyProfile.AuthAxis authAxis,
      NodeId certificateTypeId,
      SecurityPolicyProfile.KeyAgreementAxis keyAgreementAxis,
      SecurityPolicyProfile.ChunkProtectionAxis chunkProtectionAxis,
      int secureChannelNonceLength,
      int symmetricEncryptionKeySize,
      SecurityAlgorithm certificateThumbprintAlgorithm,
      short signAndEncryptSecurityLevel) {}
}
