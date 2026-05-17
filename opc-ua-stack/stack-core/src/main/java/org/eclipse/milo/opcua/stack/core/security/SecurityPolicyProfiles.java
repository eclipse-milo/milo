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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_BRAINPOOL_P256R1_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_BRAINPOOL_P384R1_SHA384;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_NIST_P256_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_NIST_P384_SHA384;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ED25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.NONE;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PSS_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.AES_GCM;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.CBC_HMAC;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.CHACHA20_POLY1305;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_BRAINPOOL_P256R1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_BRAINPOOL_P384R1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P384;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.FFDH_3072;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.RSA_NONCE;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.X25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.SequenceNumberMode.LEGACY;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.SequenceNumberMode.NON_LEGACY;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.SequenceNumberMode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Registry for security policy profiles known to the stack.
 *
 * <p>Callers use this registry when they need policy metadata or when they need to verify that the
 * stack can open a SecureChannel using a known policy.
 */
@NullMarked
public final class SecurityPolicyProfiles {

  private static final Map<SecurityPolicy, SecurityPolicyProfile> PROFILES = buildProfiles();

  private SecurityPolicyProfiles() {}

  /**
   * Get the profile for a security policy.
   *
   * @param securityPolicy the security policy to resolve.
   * @return the registered profile for {@code securityPolicy}.
   * @throws IllegalArgumentException if no profile is registered for {@code securityPolicy}.
   * @throws NullPointerException if {@code securityPolicy} is null.
   */
  public static SecurityPolicyProfile get(SecurityPolicy securityPolicy) {
    SecurityPolicyProfile profile = PROFILES.get(requireNonNull(securityPolicy, "securityPolicy"));

    if (profile == null) {
      throw new IllegalArgumentException("no profile registered for " + securityPolicy);
    }

    return profile;
  }

  /**
   * Get all registered security policy profiles.
   *
   * @return the registered profiles.
   */
  public static List<SecurityPolicyProfile> values() {
    return PROFILES.values().stream().toList();
  }

  /**
   * Require a security policy to be supported by the SecureChannel runtime.
   *
   * @param securityPolicy the security policy to check.
   * @throws UaException if the policy is known but not supported by the current SecureChannel
   *     runtime.
   * @throws NullPointerException if {@code securityPolicy} is null.
   */
  public static void requireSecureChannelSupported(SecurityPolicy securityPolicy)
      throws UaException {
    get(securityPolicy).requireSecureChannelSupported();
  }

  private static Map<SecurityPolicy, SecurityPolicyProfile> buildProfiles() {
    EnumMap<SecurityPolicy, SecurityPolicyProfile> profiles = new EnumMap<>(SecurityPolicy.class);

    register(
        profiles,
        profile(
            SecurityPolicy.None,
            NONE,
            List.of(),
            KeyAgreementAxis.NONE,
            ChunkProtectionAxis.NONE,
            LEGACY,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            0,
            0,
            0,
            0,
            1,
            0x00,
            false));

    register(
        profiles,
        profile(
            SecurityPolicy.Basic128Rsa15,
            RSA_PKCS1_SHA1,
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.RsaMinApplicationCertificateType),
            RSA_NONCE,
            CBC_HMAC,
            LEGACY,
            SecurityAlgorithm.HmacSha1,
            SecurityAlgorithm.Aes128,
            SecurityAlgorithm.RsaSha1,
            SecurityAlgorithm.Rsa15,
            SecurityAlgorithm.KwRsa15,
            SecurityAlgorithm.PSha1,
            SecurityAlgorithm.Sha1,
            SecurityAlgorithm.Sha1,
            16,
            20,
            16,
            16,
            16,
            0x01,
            false));

    register(
        profiles,
        profile(
            SecurityPolicy.Basic256,
            RSA_PKCS1_SHA1,
            List.of(
                NodeIds.RsaSha256ApplicationCertificateType,
                NodeIds.RsaMinApplicationCertificateType),
            RSA_NONCE,
            CBC_HMAC,
            LEGACY,
            SecurityAlgorithm.HmacSha1,
            SecurityAlgorithm.Aes256,
            SecurityAlgorithm.RsaSha1,
            SecurityAlgorithm.RsaOaepSha1,
            SecurityAlgorithm.KwRsaOaep,
            SecurityAlgorithm.PSha1,
            SecurityAlgorithm.Sha1,
            SecurityAlgorithm.Sha1,
            32,
            20,
            24,
            32,
            16,
            0x01,
            false));

    register(
        profiles,
        profile(
            SecurityPolicy.Basic256Sha256,
            RSA_PKCS1_SHA256,
            List.of(NodeIds.RsaSha256ApplicationCertificateType),
            RSA_NONCE,
            CBC_HMAC,
            LEGACY,
            SecurityAlgorithm.HmacSha256,
            SecurityAlgorithm.Aes256,
            SecurityAlgorithm.RsaSha256,
            SecurityAlgorithm.RsaOaepSha1,
            SecurityAlgorithm.KwRsaOaep,
            SecurityAlgorithm.PSha256,
            SecurityAlgorithm.Sha256,
            SecurityAlgorithm.Sha256,
            32,
            32,
            32,
            32,
            16,
            0x08,
            false));

    register(
        profiles,
        profile(
            SecurityPolicy.Aes128_Sha256_RsaOaep,
            RSA_PKCS1_SHA256,
            List.of(NodeIds.RsaSha256ApplicationCertificateType),
            RSA_NONCE,
            CBC_HMAC,
            LEGACY,
            SecurityAlgorithm.HmacSha256,
            SecurityAlgorithm.Aes128,
            SecurityAlgorithm.RsaSha256,
            SecurityAlgorithm.RsaOaepSha1,
            null,
            SecurityAlgorithm.PSha256,
            SecurityAlgorithm.Sha256,
            SecurityAlgorithm.Sha256,
            32,
            32,
            32,
            16,
            16,
            0x04,
            false));

    register(
        profiles,
        profile(
            SecurityPolicy.Aes256_Sha256_RsaPss,
            RSA_PSS_SHA256,
            List.of(NodeIds.RsaSha256ApplicationCertificateType),
            RSA_NONCE,
            CBC_HMAC,
            LEGACY,
            SecurityAlgorithm.HmacSha256,
            SecurityAlgorithm.Aes256,
            SecurityAlgorithm.RsaSha256Pss,
            SecurityAlgorithm.RsaOaepSha256,
            null,
            SecurityAlgorithm.PSha256,
            SecurityAlgorithm.Sha256,
            SecurityAlgorithm.Sha256,
            32,
            32,
            32,
            32,
            16,
            0x08,
            false));

    // The executable ECC policies below combine two certificate/key-agreement families with two
    // AEAD chunk protectors. NIST P-256 uses ECDSA certificates plus P-256 ECDH with a 64-byte
    // ephemeral public key in ClientNonce/ServerNonce. Curve25519 uses Ed25519 certificates plus
    // X25519 with a 32-byte ephemeral public key. AES-GCM uses a 16-byte symmetric key;
    // ChaCha20-Poly1305 uses a 32-byte symmetric key. Both use a 16-byte AEAD tag instead of a
    // separate HMAC signature.
    register(
        profiles,
        profile(
            SecurityPolicy.ECC_nistP256_AesGcm,
            ECDSA_NIST_P256_SHA256,
            List.of(NodeIds.EccNistP256ApplicationCertificateType),
            ECDH_NIST_P256,
            AES_GCM,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha256,
            64,
            16,
            0,
            16,
            1,
            0x04,
            true));

    register(
        profiles,
        profile(
            SecurityPolicy.ECC_nistP256_ChaChaPoly,
            ECDSA_NIST_P256_SHA256,
            List.of(NodeIds.EccNistP256ApplicationCertificateType),
            ECDH_NIST_P256,
            CHACHA20_POLY1305,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha256,
            64,
            16,
            0,
            32,
            1,
            0x04,
            true));

    // NIST P-384 policies are ECC B profiles. They use NIST P-384 ECDSA certificates and
    // P-384 ECDH ephemeral public keys encoded as 96-byte x||y values. The profile tuple follows
    // the NIST P-384 certificate identity even where published registry metadata is inconsistent,
    // so certificate selection, provider checks, and nonce decoding all use the same curve family.
    register(
        profiles,
        profile(
            SecurityPolicy.ECC_nistP384_AesGcm,
            ECDSA_NIST_P384_SHA384,
            List.of(NodeIds.EccNistP384ApplicationCertificateType),
            ECDH_NIST_P384,
            AES_GCM,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha384,
            96,
            16,
            0,
            32,
            1,
            0x08,
            true));

    register(
        profiles,
        profile(
            SecurityPolicy.ECC_nistP384_ChaChaPoly,
            ECDSA_NIST_P384_SHA384,
            List.of(NodeIds.EccNistP384ApplicationCertificateType),
            ECDH_NIST_P384,
            CHACHA20_POLY1305,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha384,
            96,
            16,
            0,
            32,
            1,
            0x08,
            true));

    // Brainpool P-256 policies are ECC A profiles. They require Brainpool P-256r1 ECDSA
    // certificates and P-256r1 ECDH ephemeral public keys encoded as 64-byte x||y values.
    register(
        profiles,
        profile(
            SecurityPolicy.ECC_brainpoolP256r1_AesGcm,
            ECDSA_BRAINPOOL_P256R1_SHA256,
            List.of(NodeIds.EccBrainpoolP256r1ApplicationCertificateType),
            ECDH_BRAINPOOL_P256R1,
            AES_GCM,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha256,
            64,
            16,
            0,
            16,
            1,
            0x04,
            true));

    register(
        profiles,
        profile(
            SecurityPolicy.ECC_brainpoolP256r1_ChaChaPoly,
            ECDSA_BRAINPOOL_P256R1_SHA256,
            List.of(NodeIds.EccBrainpoolP256r1ApplicationCertificateType),
            ECDH_BRAINPOOL_P256R1,
            CHACHA20_POLY1305,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha256,
            64,
            16,
            0,
            32,
            1,
            0x04,
            true));

    register(
        profiles,
        profile(
            SecurityPolicy.ECC_curve25519_AesGcm,
            ED25519,
            List.of(NodeIds.EccCurve25519ApplicationCertificateType),
            X25519,
            AES_GCM,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha256,
            32,
            16,
            0,
            16,
            1,
            0x04,
            true));

    register(
        profiles,
        profile(
            SecurityPolicy.ECC_curve25519_ChaChaPoly,
            ED25519,
            List.of(NodeIds.EccCurve25519ApplicationCertificateType),
            X25519,
            CHACHA20_POLY1305,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha256,
            32,
            16,
            0,
            32,
            1,
            0x04,
            true));

    // Brainpool P-384 policies are ECC B profiles. They use Brainpool P-384r1 ECDSA certificates
    // and P-384r1 ECDH ephemeral public keys encoded as 96-byte x||y values, HKDF-SHA-384, and
    // 256-bit AEAD encryption keys.
    register(
        profiles,
        profile(
            SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly,
            ECDSA_BRAINPOOL_P384R1_SHA384,
            List.of(NodeIds.EccBrainpoolP384r1ApplicationCertificateType),
            ECDH_BRAINPOOL_P384R1,
            CHACHA20_POLY1305,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha384,
            96,
            16,
            0,
            32,
            1,
            0x08,
            true));

    register(
        profiles,
        profile(
            SecurityPolicy.ECC_brainpoolP384r1_AesGcm,
            ECDSA_BRAINPOOL_P384R1_SHA384,
            List.of(NodeIds.EccBrainpoolP384r1ApplicationCertificateType),
            ECDH_BRAINPOOL_P384R1,
            AES_GCM,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.None,
            SecurityAlgorithm.None,
            null,
            null,
            null,
            SecurityAlgorithm.Sha384,
            96,
            16,
            0,
            32,
            1,
            0x08,
            true));

    // RSA-DH policies use RSA application certificates for OpenSecureChannel authentication but
    // exchange RFC 7919 ffdhe3072 ephemeral public values in ClientNonce/ServerNonce. OPN chunks
    // are signed only; ordinary service chunks use the same AEAD chunk-protection paths as the
    // current ECC profiles.
    register(
        profiles,
        profile(
            SecurityPolicy.RSA_DH_AesGcm,
            RSA_PKCS1_SHA256,
            List.of(NodeIds.RsaSha256ApplicationCertificateType),
            FFDH_3072,
            AES_GCM,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.RsaSha256,
            SecurityAlgorithm.None,
            null,
            null,
            SecurityAlgorithm.Sha256,
            SecurityAlgorithm.Sha256,
            384,
            16,
            0,
            16,
            1,
            0x04,
            true));

    register(
        profiles,
        profile(
            SecurityPolicy.RSA_DH_ChaChaPoly,
            RSA_PKCS1_SHA256,
            List.of(NodeIds.RsaSha256ApplicationCertificateType),
            FFDH_3072,
            CHACHA20_POLY1305,
            NON_LEGACY,
            null,
            null,
            SecurityAlgorithm.RsaSha256,
            SecurityAlgorithm.None,
            null,
            null,
            SecurityAlgorithm.Sha256,
            SecurityAlgorithm.Sha256,
            384,
            16,
            0,
            32,
            1,
            0x04,
            true));

    return Map.copyOf(profiles);
  }

  private static void register(
      EnumMap<SecurityPolicy, SecurityPolicyProfile> profiles, SecurityPolicyProfile profile) {

    SecurityPolicyProfile previous = profiles.put(profile.securityPolicy(), profile);

    if (previous != null) {
      throw new IllegalStateException("duplicate profile: " + profile.securityPolicy());
    }
  }

  private static SecurityPolicyProfile profile(
      SecurityPolicy securityPolicy,
      AuthAxis authAxis,
      List<NodeId> certificateTypeIds,
      KeyAgreementAxis keyAgreementAxis,
      ChunkProtectionAxis chunkProtectionAxis,
      SequenceNumberMode sequenceNumberMode,
      @Nullable SecurityAlgorithm symmetricSignatureAlgorithm,
      @Nullable SecurityAlgorithm symmetricEncryptionAlgorithm,
      SecurityAlgorithm asymmetricSignatureAlgorithm,
      SecurityAlgorithm asymmetricEncryptionAlgorithm,
      @Nullable SecurityAlgorithm asymmetricKeyWrapAlgorithm,
      @Nullable SecurityAlgorithm keyDerivationAlgorithm,
      @Nullable SecurityAlgorithm certificateSignatureAlgorithm,
      SecurityAlgorithm certificateThumbprintAlgorithm,
      int secureChannelNonceLength,
      int symmetricSignatureSize,
      int symmetricSignatureKeySize,
      int symmetricEncryptionKeySize,
      int symmetricBlockSize,
      int securityLevel,
      boolean secureChannelEnhancements) {

    return new SecurityPolicyProfile(
        securityPolicy,
        authAxis,
        certificateTypeIds,
        keyAgreementAxis,
        chunkProtectionAxis,
        sequenceNumberMode,
        symmetricSignatureAlgorithm,
        symmetricEncryptionAlgorithm,
        asymmetricSignatureAlgorithm,
        asymmetricEncryptionAlgorithm,
        asymmetricKeyWrapAlgorithm,
        keyDerivationAlgorithm,
        certificateSignatureAlgorithm,
        certificateThumbprintAlgorithm,
        secureChannelNonceLength,
        symmetricSignatureSize,
        symmetricSignatureKeySize,
        symmetricEncryptionKeySize,
        symmetricBlockSize,
        securityLevel,
        secureChannelEnhancements,
        true);
  }
}
