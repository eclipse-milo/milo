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
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.AES_GCM;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.CHACHA20_POLY1305;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_BRAINPOOL_P256R1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_BRAINPOOL_P384R1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P384;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.FFDH_3072;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.X25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.SequenceNumberMode.NON_LEGACY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.junit.jupiter.params.provider.MethodSource;

@NullMarked
public class SecurityPolicyTest {

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
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP256r1")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_brainpoolP384r1")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe(
                "http://opcfoundation.org/UA/SecurityPolicy#ECC_curve25519_ChaCha20Poly1305")
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
