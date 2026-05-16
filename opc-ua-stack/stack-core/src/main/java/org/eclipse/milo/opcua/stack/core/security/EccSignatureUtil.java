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
 * {@code r || s} wire format instead of DER encoding, while Ed25519 signatures are already fixed
 * width. Callers pass buffers positioned over the exact bytes that are covered by the chunk
 * signature.
 */
@NullMarked
public final class EccSignatureUtil {

  public static final int ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH = 64;
  public static final int ED25519_SIGNATURE_LENGTH = 64;

  private EccSignatureUtil() {}

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
        sign(providerProfile, ecdsaTransformation(providerProfile), privateKey, buffers);

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
        ecdsaTransformation(providerProfile),
        publicKey,
        signatureBytes,
        ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH,
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

      for (ByteBuffer buffer : buffers) {
        signature.update(buffer);
      }

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

      for (ByteBuffer buffer : buffers) {
        signature.update(buffer);
      }

      if (!signature.verify(signatureBytes)) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
      }
    } catch (UaException e) {
      throw e;
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  private static String ecdsaTransformation(ProviderProfile providerProfile) {
    return switch (providerProfile) {
      case BOUNCY_CASTLE -> "SHA256WITHPLAIN-ECDSA";
      case JDK -> "SHA256withECDSAinP1363Format";
    };
  }
}
