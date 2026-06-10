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

import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Metadata for an OPC UA security policy.
 *
 * <p>A profile describes the policy axes and sizes that callers need when choosing certificates,
 * advertising endpoints, opening SecureChannels, and deriving symmetric channel state. The public
 * {@link SecurityPolicy} remains the URI lookup surface; callers can use {@link
 * SecurityPolicy#getProfile()} or {@link SecurityPolicyProfiles#get(SecurityPolicy)} to inspect the
 * corresponding profile.
 *
 * <p>The axis fields intentionally keep certificate authentication, OpenSecureChannel secret
 * establishment, and symmetric chunk protection separate. Enhanced policies can share a coordinate
 * size while still requiring different certificate identities or provider support, so callers
 * should compare profile metadata instead of inferring behavior from policy URI strings or nonce
 * lengths.
 *
 * <p>Required object components must be non-null. Size values must be non-negative, and security
 * level values must fit in the OPC UA policy-strength bit range.
 *
 * @param securityPolicy the policy URI identifier represented by this profile.
 * @param authAxis the authentication/signature family used by the policy.
 * @param certificateTypeIds the certificate type IDs compatible with this profile, in preference
 *     order. Empty when the profile does not use application certificates.
 * @param keyAgreementAxis the key-agreement family used during OpenSecureChannel.
 * @param chunkProtectionAxis the symmetric chunk-protection family used after key installation.
 * @param sequenceNumberMode the sequence-number validation mode required by the policy.
 * @param symmetricSignatureAlgorithm the legacy CBC/HMAC symmetric signature algorithm. {@link
 *     SecurityPolicy#None} uses {@link SecurityAlgorithm#None}; secured policies use {@code null}
 *     when they do not use one.
 * @param symmetricEncryptionAlgorithm the legacy CBC symmetric encryption algorithm. {@link
 *     SecurityPolicy#None} uses {@link SecurityAlgorithm#None}; secured policies use {@code null}
 *     when they do not use one.
 * @param asymmetricSignatureAlgorithm the legacy asymmetric signature algorithm. {@link
 *     SecurityPolicy#None} uses {@link SecurityAlgorithm#None}; secured policies use {@link
 *     SecurityAlgorithm#None} when signing is provided by a non-legacy authentication axis.
 * @param asymmetricEncryptionAlgorithm the asymmetric encryption algorithm used for OPN chunks.
 *     {@link SecurityPolicy#None} uses {@link SecurityAlgorithm#None}; secured policies use {@link
 *     SecurityAlgorithm#None} when the policy signs but does not encrypt OPN chunks.
 * @param asymmetricKeyWrapAlgorithm the asymmetric key-wrap algorithm. {@link SecurityPolicy#None}
 *     uses {@link SecurityAlgorithm#None}; secured policies use {@code null} when they do not use
 *     key wrapping.
 * @param keyDerivationAlgorithm the legacy P_SHA key-derivation algorithm. {@link
 *     SecurityPolicy#None} uses {@link SecurityAlgorithm#None}; secured policies use {@code null}
 *     when they use another key-derivation axis.
 * @param certificateSignatureAlgorithm the legacy certificate signature algorithm. {@link
 *     SecurityPolicy#None} uses {@link SecurityAlgorithm#None}; secured policies use {@code null}
 *     when certificate compatibility is described by the authentication axis.
 * @param certificateThumbprintAlgorithm the digest algorithm used by policy-level certificate
 *     thumbprint checks. {@link SecurityPolicy#None} uses {@link SecurityAlgorithm#None}.
 * @param secureChannelNonceLength the required OpenSecureChannel nonce or ephemeral public-key
 *     length in bytes.
 * @param symmetricSignatureSize the symmetric signature or AEAD tag length in bytes.
 * @param symmetricSignatureKeySize the symmetric signature key length in bytes.
 * @param symmetricEncryptionKeySize the symmetric encryption key length in bytes.
 * @param symmetricBlockSize the chunk padding block size, or {@code 1} when chunks are not padded.
 * @param securityLevel the policy-strength bits used in endpoint descriptions.
 * @param secureChannelEnhancements whether the policy requires SecureChannelEnhancements behavior.
 */
@NullMarked
public record SecurityPolicyProfile(
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

  public SecurityPolicyProfile {
    certificateTypeIds = List.copyOf(certificateTypeIds);

    if (secureChannelNonceLength < 0) {
      throw new IllegalArgumentException("secureChannelNonceLength: " + secureChannelNonceLength);
    }
    if (symmetricSignatureSize < 0) {
      throw new IllegalArgumentException("symmetricSignatureSize: " + symmetricSignatureSize);
    }
    if (symmetricSignatureKeySize < 0) {
      throw new IllegalArgumentException("symmetricSignatureKeySize: " + symmetricSignatureKeySize);
    }
    if (symmetricEncryptionKeySize < 0) {
      throw new IllegalArgumentException(
          "symmetricEncryptionKeySize: " + symmetricEncryptionKeySize);
    }
    if (symmetricBlockSize < 1) {
      throw new IllegalArgumentException("symmetricBlockSize: " + symmetricBlockSize);
    }
    if (securityLevel < 0 || securityLevel > 0x0F) {
      throw new IllegalArgumentException("securityLevel: " + securityLevel);
    }
  }

  /**
   * Get the certificate type this profile prefers when more than one compatible identity is
   * available.
   *
   * @return the first compatible certificate type ID, or empty for certificate-less profiles.
   */
  public Optional<NodeId> preferredCertificateTypeId() {
    return certificateTypeIds.stream().findFirst();
  }

  /**
   * Get the endpoint security level for this profile and message security mode.
   *
   * @param securityMode the endpoint message security mode.
   * @return the security level byte value as an unsigned-compatible short.
   * @throws NullPointerException if {@code securityMode} is null.
   */
  public short getSecurityLevel(MessageSecurityMode securityMode) {
    short level = (short) securityLevel;

    switch (securityMode) {
      case SignAndEncrypt -> level |= 0x80;
      case Sign -> level |= 0x40;
      default -> level |= 0x20;
    }

    return level;
  }

  /**
   * Whether OpenSecureChannel carries ephemeral public keys in {@code ClientNonce}/{@code
   * ServerNonce} instead of random nonce bytes.
   *
   * <p>Callers that issue or renew a SecureChannel use this to decide whether to generate and
   * retain an ephemeral key pair for the matching key-agreement axis.
   *
   * @return {@code true} when the key-agreement axis exchanges ephemeral public keys.
   */
  public boolean usesEphemeralKeyAgreement() {
    return switch (keyAgreementAxis) {
      case ECDH_NIST_P256,
          ECDH_NIST_P384,
          ECDH_BRAINPOOL_P256R1,
          ECDH_BRAINPOOL_P384R1,
          X25519,
          X448,
          FFDH_3072 ->
          true;
      case NONE, RSA_NONCE -> false;
    };
  }

  /**
   * Whether symmetric service chunks are protected with an AEAD cipher rather than a separate
   * CBC/HMAC signature.
   *
   * @return {@code true} when the chunk-protection axis is an AEAD family.
   */
  public boolean usesAeadChunkProtection() {
    return switch (chunkProtectionAxis) {
      case AES_GCM, CHACHA20_POLY1305 -> true;
      case NONE, CBC_HMAC -> false;
    };
  }

  /**
   * Whether this profile can open a SecureChannel using the given message security mode.
   *
   * <p>Legacy policies accept any mode. SecureChannelEnhancements policies accept only {@code Sign}
   * and {@code SignAndEncrypt}: OPC UA Part 4 (7.41) disallows pairing ECC and RSA-DH policies with
   * {@code None} because their user-token encryption requires the ephemeral keys negotiated by a
   * secured handshake, and the chunk layer applies no symmetric protection for {@code None} or
   * {@code Invalid}.
   *
   * @param securityMode the message security mode being negotiated.
   * @return {@code true} when the mode is supported for this profile.
   * @throws NullPointerException if {@code securityMode} is null.
   */
  public boolean isMessageSecurityModeSupported(MessageSecurityMode securityMode) {
    if (!secureChannelEnhancements) {
      return true;
    }

    return switch (securityMode) {
      case Sign, SignAndEncrypt -> true;
      default -> false;
    };
  }

  /** The certificate and OpenSecureChannel signature family used by a policy. */
  public enum AuthAxis {
    NONE,
    RSA_PKCS1_SHA1,
    RSA_PKCS1_SHA256,
    RSA_PSS_SHA256,
    ECDSA_NIST_P256_SHA256,
    ECDSA_NIST_P384_SHA384,
    ECDSA_BRAINPOOL_P256R1_SHA256,
    ECDSA_BRAINPOOL_P384R1_SHA384,
    ED25519,
    ED448
  }

  /** The OpenSecureChannel secret establishment family used by a policy. */
  public enum KeyAgreementAxis {
    NONE,
    RSA_NONCE,
    ECDH_NIST_P256,
    ECDH_NIST_P384,
    ECDH_BRAINPOOL_P256R1,
    ECDH_BRAINPOOL_P384R1,
    X25519,
    X448,
    FFDH_3072
  }

  /** The symmetric chunk protection family used after SecureChannel keys are installed. */
  public enum ChunkProtectionAxis {
    NONE,
    CBC_HMAC,
    AES_GCM,
    CHACHA20_POLY1305
  }

  /** The sequence-number validation mode used for secured chunks. */
  public enum SequenceNumberMode {
    LEGACY,
    NON_LEGACY
  }
}
