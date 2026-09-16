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

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.jspecify.annotations.NullMarked;

/**
 * Signature helpers for ECC authentication axes used by OpenSecureChannel chunks.
 *
 * <p>ECC policy signatures are appended to the unencrypted OpenSecureChannel chunk body in the same
 * place RSA signatures are appended for legacy policies. ECDSA signatures use the fixed-width P1363
 * {@code r || s} wire format instead of DER encoding, while Ed25519 and Ed448 signatures are
 * already fixed width. Callers pass buffers positioned over the exact bytes that are covered by the
 * chunk signature.
 */
@NullMarked
public final class EccSignatureUtil {

  public static final String ECDSA_SHA256_URI =
      "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256";
  public static final String ECDSA_SHA384_URI =
      "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384";
  public static final String ED25519_URI = "http://www.w3.org/2021/04/xmldsig-more#eddsa-ed25519";
  public static final String ED448_URI = "http://www.w3.org/2021/04/xmldsig-more#eddsa-ed448";

  public static final int ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH = 64;
  public static final int ECDSA_P384_SHA384_P1363_SIGNATURE_LENGTH = 96;
  public static final int ED25519_SIGNATURE_LENGTH = 64;
  public static final int ED448_SIGNATURE_LENGTH = 114;

  private static final byte[] EMPTY_MESSAGE = new byte[0];

  private static final SecurityProviderResolver PROVIDER_RESOLVER =
      SecurityProviderResolver.create();

  private EccSignatureUtil() {}

  /**
   * Get the SignatureData algorithm URI for a policy using an ECC authentication axis.
   *
   * @param profile the security policy profile.
   * @return the URI used in {@code SignatureData.algorithm}.
   * @throws UaException if {@code profile} does not use a supported ECC signature axis.
   */
  public static String getSignatureAlgorithmUri(SecurityPolicyProfile profile) throws UaException {
    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256, ECDSA_BRAINPOOL_P256R1_SHA256 -> ECDSA_SHA256_URI;
      case ECDSA_NIST_P384_SHA384, ECDSA_BRAINPOOL_P384R1_SHA384 -> ECDSA_SHA384_URI;
      case ED25519 -> ED25519_URI;
      case ED448 -> ED448_URI;
      default ->
          throw new UaException(
              StatusCodes.Bad_SecurityPolicyRejected,
              "unsupported ECC signature axis: " + profile.authAxis());
    };
  }

  /**
   * Sign data with the ECC authentication axis selected by {@code profile}.
   *
   * @param profile the security policy profile.
   * @param privateKey the application instance private key.
   * @param buffers the data buffers to sign from their current positions to limits.
   * @return the fixed-width ECC signature bytes.
   * @throws UaException if signing fails or the profile is unsupported.
   */
  public static byte[] sign(
      SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... buffers)
      throws UaException {

    ProviderProfile providerProfile = PROVIDER_RESOLVER.resolve(profile);

    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256, ECDSA_BRAINPOOL_P256R1_SHA256 ->
          signEcdsaP256Sha256P1363(providerProfile, privateKey, buffers);
      case ECDSA_NIST_P384_SHA384, ECDSA_BRAINPOOL_P384R1_SHA384 ->
          signEcdsaP384Sha384P1363(providerProfile, privateKey, buffers);
      case ED25519 -> signEd25519(providerProfile, privateKey, buffers);
      case ED448 -> signEd448(providerProfile, privateKey, buffers);
      default ->
          throw new UaException(
              StatusCodes.Bad_SecurityPolicyRejected,
              "unsupported ECC signature axis: " + profile.authAxis());
    };
  }

  /**
   * Verify data with the ECC authentication axis selected by {@code profile}.
   *
   * @param profile the security policy profile.
   * @param publicKey the application instance public key.
   * @param signatureBytes the fixed-width ECC signature bytes.
   * @param buffers the signed data buffers from their current positions to limits.
   * @throws UaException if verification fails or the profile is unsupported.
   */
  public static void verify(
      SecurityPolicyProfile profile,
      PublicKey publicKey,
      byte[] signatureBytes,
      ByteBuffer... buffers)
      throws UaException {

    ProviderProfile providerProfile = PROVIDER_RESOLVER.resolve(profile);

    switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256, ECDSA_BRAINPOOL_P256R1_SHA256 ->
          verifyEcdsaP256Sha256P1363(providerProfile, publicKey, signatureBytes, buffers);
      case ECDSA_NIST_P384_SHA384, ECDSA_BRAINPOOL_P384R1_SHA384 ->
          verifyEcdsaP384Sha384P1363(providerProfile, publicKey, signatureBytes, buffers);
      case ED25519 -> verifyEd25519(providerProfile, publicKey, signatureBytes, buffers);
      case ED448 -> verifyEd448(providerProfile, publicKey, signatureBytes, buffers);
      default ->
          throw new UaException(
              StatusCodes.Bad_SecurityPolicyRejected,
              "unsupported ECC signature axis: " + profile.authAxis());
    }
  }

  /**
   * Sign data with ECDSA P-256 SHA-256 and return a P1363 {@code r || s} signature.
   *
   * <p>The returned signature is always 64 bytes: 32 bytes of {@code r} followed by 32 bytes of
   * {@code s}.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the P-256 private key.
   * @param buffers the data buffers to sign from their current positions to limits.
   * @return the 64-byte P1363 signature.
   * @throws UaException if signing fails.
   */
  public static byte[] signEcdsaP256Sha256P1363(
      ProviderProfile providerProfile, PrivateKey privateKey, ByteBuffer... buffers)
      throws UaException {

    byte[] signatureBytes =
        sign(providerProfile, ecdsaTransformation(providerProfile, "SHA256"), privateKey, buffers);

    if (signatureBytes.length != ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_InternalError,
          "ECDSA P-256 P1363 signature was " + signatureBytes.length + " bytes");
    }

    return signatureBytes;
  }

  /**
   * Verify an ECDSA P-256 SHA-256 P1363 signature.
   *
   * <p>Signatures with any length other than 64 bytes are rejected before provider verification.
   *
   * @param providerProfile the provider profile to use.
   * @param publicKey the P-256 public key.
   * @param signatureBytes the 64-byte P1363 signature.
   * @param buffers the signed data buffers from their current positions to limits.
   * @throws UaException if verification fails.
   */
  public static void verifyEcdsaP256Sha256P1363(
      ProviderProfile providerProfile,
      PublicKey publicKey,
      byte[] signatureBytes,
      ByteBuffer... buffers)
      throws UaException {

    verify(
        providerProfile,
        ecdsaTransformation(providerProfile, "SHA256"),
        publicKey,
        signatureBytes,
        ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH,
        buffers);
  }

  /**
   * Sign data with ECDSA P-384 SHA-384 and return a P1363 {@code r || s} signature.
   *
   * <p>The returned signature is always 96 bytes: 48 bytes of {@code r} followed by 48 bytes of
   * {@code s}.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the P-384 private key.
   * @param buffers the data buffers to sign from their current positions to limits.
   * @return the 96-byte P1363 signature.
   * @throws UaException if signing fails.
   */
  public static byte[] signEcdsaP384Sha384P1363(
      ProviderProfile providerProfile, PrivateKey privateKey, ByteBuffer... buffers)
      throws UaException {

    byte[] signatureBytes =
        sign(providerProfile, ecdsaTransformation(providerProfile, "SHA384"), privateKey, buffers);

    if (signatureBytes.length != ECDSA_P384_SHA384_P1363_SIGNATURE_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_InternalError,
          "ECDSA P-384 P1363 signature was " + signatureBytes.length + " bytes");
    }

    return signatureBytes;
  }

  /**
   * Verify an ECDSA P-384 SHA-384 P1363 signature.
   *
   * <p>Signatures with any length other than 96 bytes are rejected before provider verification.
   *
   * @param providerProfile the provider profile to use.
   * @param publicKey the P-384 public key.
   * @param signatureBytes the 96-byte P1363 signature.
   * @param buffers the signed data buffers from their current positions to limits.
   * @throws UaException if verification fails.
   */
  public static void verifyEcdsaP384Sha384P1363(
      ProviderProfile providerProfile,
      PublicKey publicKey,
      byte[] signatureBytes,
      ByteBuffer... buffers)
      throws UaException {

    verify(
        providerProfile,
        ecdsaTransformation(providerProfile, "SHA384"),
        publicKey,
        signatureBytes,
        ECDSA_P384_SHA384_P1363_SIGNATURE_LENGTH,
        buffers);
  }

  /**
   * Sign data with Ed25519.
   *
   * <p>Ed25519 signs the message bytes directly; callers should not hash the data first.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the Ed25519 private key.
   * @param buffers the data buffers to sign from their current positions to limits.
   * @return the 64-byte Ed25519 signature.
   * @throws UaException if signing fails.
   */
  public static byte[] signEd25519(
      ProviderProfile providerProfile, PrivateKey privateKey, ByteBuffer... buffers)
      throws UaException {

    byte[] signatureBytes = sign(providerProfile, "Ed25519", privateKey, buffers);

    if (signatureBytes.length != ED25519_SIGNATURE_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_InternalError,
          "Ed25519 signature was " + signatureBytes.length + " bytes");
    }

    return signatureBytes;
  }

  /**
   * Verify an Ed25519 signature.
   *
   * <p>Signatures with any length other than 64 bytes are rejected before provider verification.
   *
   * @param providerProfile the provider profile to use.
   * @param publicKey the Ed25519 public key.
   * @param signatureBytes the 64-byte Ed25519 signature.
   * @param buffers the signed data buffers from their current positions to limits.
   * @throws UaException if verification fails.
   */
  public static void verifyEd25519(
      ProviderProfile providerProfile,
      PublicKey publicKey,
      byte[] signatureBytes,
      ByteBuffer... buffers)
      throws UaException {

    verify(
        providerProfile, "Ed25519", publicKey, signatureBytes, ED25519_SIGNATURE_LENGTH, buffers);
  }

  /**
   * Sign data with Ed448.
   *
   * <p>Ed448 signs the message bytes directly; callers should not hash the data first.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the Ed448 private key.
   * @param buffers the data buffers to sign from their current positions to limits.
   * @return the 114-byte Ed448 signature.
   * @throws UaException if signing fails.
   */
  public static byte[] signEd448(
      ProviderProfile providerProfile, PrivateKey privateKey, ByteBuffer... buffers)
      throws UaException {

    byte[] signatureBytes = sign(providerProfile, "Ed448", privateKey, buffers);

    if (signatureBytes.length != ED448_SIGNATURE_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_InternalError, "Ed448 signature was " + signatureBytes.length + " bytes");
    }

    return signatureBytes;
  }

  /**
   * Verify an Ed448 signature.
   *
   * <p>Signatures with any length other than 114 bytes are rejected before provider verification.
   *
   * @param providerProfile the provider profile to use.
   * @param publicKey the Ed448 public key.
   * @param signatureBytes the 114-byte Ed448 signature.
   * @param buffers the signed data buffers from their current positions to limits.
   * @throws UaException if verification fails.
   */
  public static void verifyEd448(
      ProviderProfile providerProfile,
      PublicKey publicKey,
      byte[] signatureBytes,
      ByteBuffer... buffers)
      throws UaException {

    verify(providerProfile, "Ed448", publicKey, signatureBytes, ED448_SIGNATURE_LENGTH, buffers);
  }

  private static byte[] sign(
      ProviderProfile providerProfile,
      String transformation,
      PrivateKey privateKey,
      ByteBuffer... buffers)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");
    requireNonNull(privateKey, "privateKey");
    requireNonNull(buffers, "buffers");

    try {
      Signature signature =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              transformation,
              provider -> Signature.getInstance(transformation, provider));

      signature.initSign(privateKey);

      update(signature, buffers);

      return signature.sign();
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  private static void verify(
      ProviderProfile providerProfile,
      String transformation,
      PublicKey publicKey,
      byte[] signatureBytes,
      int signatureLength,
      ByteBuffer... buffers)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");
    requireNonNull(publicKey, "publicKey");
    requireNonNull(signatureBytes, "signatureBytes");
    requireNonNull(buffers, "buffers");

    if (signatureBytes.length != signatureLength) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed, "signature must be " + signatureLength + " bytes");
    }

    try {
      Signature signature =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              transformation,
              provider -> Signature.getInstance(transformation, provider));

      signature.initVerify(publicKey);

      update(signature, buffers);

      if (!signature.verify(signatureBytes)) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
      }
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  private static String ecdsaTransformation(ProviderProfile providerProfile, String digestName) {
    return switch (providerProfile) {
      case BOUNCY_CASTLE -> digestName + "WITHPLAIN-ECDSA";
      case JDK -> digestName + "withECDSAinP1363Format";
    };
  }

  private static void update(Signature signature, ByteBuffer... buffers)
      throws GeneralSecurityException {

    if (buffers.length == 0) {
      signature.update(EMPTY_MESSAGE);
      return;
    }

    for (ByteBuffer buffer : buffers) {
      if (buffer.hasRemaining()) {
        signature.update(buffer);
      } else {
        // SunEC's EdDSA verifier on Java 17 does not initialize an empty message when
        // Signature.update(ByteBuffer) receives a buffer with no remaining bytes. The byte-array
        // overload performs the required zero-length update.
        signature.update(EMPTY_MESSAGE);
      }
    }
  }
}
