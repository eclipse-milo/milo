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

import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_NIST_P256_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ED25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.AES_GCM;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.CHACHA20_POLY1305;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.X25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.SequenceNumberMode.NON_LEGACY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.junit.jupiter.api.Test;

public class SecurityPolicyTest {

  @Test
  public void testPolicyUriLookupRecognizesTargetEccPolicies() throws UaException {
    assertSame(
        SecurityPolicy.ECC_nistP256_AesGcm,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_nistP256_AesGcm.getUri()));

    assertSame(
        SecurityPolicy.ECC_curve25519_ChaChaPoly,
        SecurityPolicy.fromUri(SecurityPolicy.ECC_curve25519_ChaChaPoly.getUri()));
  }

  @Test
  @SuppressWarnings("HttpUrlsUsage")
  public void testDeprecatedEccPoliciesAreNotExposed() {
    Set<String> eccPolicyNames =
        Arrays.stream(SecurityPolicy.values())
            .map(Enum::name)
            .filter(name -> name.startsWith("ECC_"))
            .collect(Collectors.toSet());

    assertEquals(Set.of("ECC_nistP256_AesGcm", "ECC_curve25519_ChaChaPoly"), eccPolicyNames);

    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP256")
            .isEmpty());
    assertTrue(
        SecurityPolicy.fromUriSafe("http://opcfoundation.org/UA/SecurityPolicy#ECC_curve25519")
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

  @Test
  public void testTargetEccProfileMetadata() {
    SecurityPolicyProfile nistP256 = SecurityPolicyProfiles.get(SecurityPolicy.ECC_nistP256_AesGcm);

    assertEquals(ECDSA_NIST_P256_SHA256, nistP256.authAxis());
    assertEquals(ECDH_NIST_P256, nistP256.keyAgreementAxis());
    assertEquals(AES_GCM, nistP256.chunkProtectionAxis());
    assertEquals(NON_LEGACY, nistP256.sequenceNumberMode());
    assertEquals(SecurityAlgorithm.Sha256, nistP256.certificateThumbprintAlgorithm());
    assertEquals(64, nistP256.secureChannelNonceLength());
    assertEquals(16, nistP256.symmetricSignatureSize());
    assertEquals(0, nistP256.symmetricSignatureKeySize());
    assertEquals(16, nistP256.symmetricEncryptionKeySize());
    assertEquals(1, nistP256.symmetricBlockSize());
    assertEquals((short) 0x84, nistP256.getSecurityLevel(MessageSecurityMode.SignAndEncrypt));
    assertTrue(nistP256.secureChannelEnhancements());
    assertFalse(nistP256.secureChannelSupported());

    SecurityPolicyProfile curve25519 =
        SecurityPolicyProfiles.get(SecurityPolicy.ECC_curve25519_ChaChaPoly);

    assertEquals(ED25519, curve25519.authAxis());
    assertEquals(X25519, curve25519.keyAgreementAxis());
    assertEquals(CHACHA20_POLY1305, curve25519.chunkProtectionAxis());
    assertEquals(NON_LEGACY, curve25519.sequenceNumberMode());
    assertEquals(32, curve25519.secureChannelNonceLength());
    assertEquals(16, curve25519.symmetricSignatureSize());
    assertEquals(0, curve25519.symmetricSignatureKeySize());
    assertEquals(32, curve25519.symmetricEncryptionKeySize());
    assertEquals((short) 0x84, curve25519.getSecurityLevel(MessageSecurityMode.SignAndEncrypt));
    assertTrue(curve25519.secureChannelEnhancements());
    assertFalse(curve25519.secureChannelSupported());
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
  public void testUnsupportedSecureChannelGate() {
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                SecurityPolicyProfiles.requireSecureChannelSupported(
                    SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
    assertTrue(exception.getMessage().contains("SecureChannel runtime"));

    assertDoesNotThrow(
        () -> SecurityPolicyProfiles.requireSecureChannelSupported(SecurityPolicy.Basic256Sha256));
  }
}
