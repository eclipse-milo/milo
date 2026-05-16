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
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ECDSA_NIST_P256_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.ED25519;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.NONE;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA1;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis.RSA_PSS_SHA256;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.AES_GCM;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.CBC_HMAC;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis.CHACHA20_POLY1305;
import static org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis.ECDH_NIST_P256;
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
            false,
            true));

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
            false,
            true));

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
            false,
            true));

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
            false,
            true));

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
            false,
            true));

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
            false,
            true));

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
            true,
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
            true,
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
      boolean secureChannelEnhancements,
      boolean secureChannelSupported) {

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
        secureChannelSupported);
  }
}
