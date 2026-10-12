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

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.bouncycastle.crypto.agreement.DHStandardGroups;
import org.bouncycastle.crypto.params.DHParameters;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NullMarked;

/**
 * Finite-field Diffie-Hellman helpers for RSA-DH SecureChannel setup.
 *
 * <p>The current RSA-DH OPC UA profiles use RFC 7919 {@code ffdhe3072}. The OpenSecureChannel
 * {@code ClientNonce} and {@code ServerNonce} fields carry the DH public value as a 384-byte
 * zero-padded, unsigned, big-endian integer. Application certificates remain RSA signing
 * certificates; this class only owns the ephemeral finite-field DH exchange.
 *
 * <p>Transport code normally reaches this helper through {@link
 * org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity} and the channel strategy layer.
 * Keeping the ffdhe3072 formatting and range checks here gives both OpenSecureChannel and
 * user-token encryption the same validation boundary for values received from the wire.
 */
@NullMarked
public final class FiniteFieldDhKeyAgreementUtil {

  public static final int FFDHE_3072_PUBLIC_KEY_LENGTH = 384;

  /** The ffdhe3072 private exponent length required by OPC UA RSA-DH profiles. */
  public static final int FFDHE_3072_PRIVATE_KEY_LENGTH_BITS = 275;

  private static final DHParameters FFDHE_3072 = DHStandardGroups.rfc7919_ffdhe3072;
  private static final BigInteger FFDHE_3072_P = FFDHE_3072.getP();
  private static final BigInteger FFDHE_3072_G = FFDHE_3072.getG();
  private static final BigInteger TWO = BigInteger.TWO;

  private FiniteFieldDhKeyAgreementUtil() {}

  /**
   * Generate an ephemeral RFC 7919 {@code ffdhe3072} key pair.
   *
   * @param providerProfile the provider profile to use.
   * @return an ephemeral finite-field DH key pair.
   * @throws UaException if key generation fails.
   */
  public static KeyPair generateFfdhe3072KeyPair(ProviderProfile providerProfile)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");

    try {
      KeyPairGenerator generator =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              "DiffieHellman KeyPairGenerator",
              FiniteFieldDhKeyAgreementUtil::dhKeyPairGenerator);

      generator.initialize(ffdhe3072ParameterSpec());

      return generator.generateKeyPair();
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  /**
   * Encode an RFC 7919 {@code ffdhe3072} public key as a 384-byte big-endian public value.
   *
   * @param publicKey the public key to encode.
   * @return the OPC UA nonce/public-value wire bytes.
   * @throws UaException if the key is not an {@code ffdhe3072} public key.
   */
  public static ByteString encodeFfdhe3072(PublicKey publicKey) throws UaException {
    requireNonNull(publicKey, "publicKey");

    if (!(publicKey instanceof DHPublicKey dhPublicKey)) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed, "expected finite-field DH public key");
    }

    DHParameterSpec params = dhPublicKey.getParams();
    if (!FFDHE_3072_P.equals(params.getP()) || !FFDHE_3072_G.equals(params.getG())) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, "expected ffdhe3072 public key");
    }

    BigInteger y = dhPublicKey.getY();
    validateFfdhe3072PublicValue(y);

    return ByteString.of(fixedWidthUnsigned(y));
  }

  /**
   * Decode a 384-byte RFC 7919 {@code ffdhe3072} public value into a JCA public key.
   *
   * @param wire the OPC UA nonce/public-value wire bytes.
   * @param providerProfile the provider profile used to create the returned key.
   * @return a DH public key suitable for agreement.
   * @throws UaException if the bytes are the wrong length, out of range, or unsupported.
   */
  public static PublicKey decodeFfdhe3072(ByteString wire, ProviderProfile providerProfile)
      throws UaException {

    requireNonNull(wire, "wire");
    requireNonNull(providerProfile, "providerProfile");

    byte[] bytes = wire.bytesOrEmpty();
    if (bytes.length != FFDHE_3072_PUBLIC_KEY_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_NonceInvalid,
          "ffdhe3072 public value must be " + FFDHE_3072_PUBLIC_KEY_LENGTH + " bytes");
    }

    BigInteger y = new BigInteger(1, bytes);
    validateFfdhe3072PublicValue(y);

    try {
      KeyFactory keyFactory =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              "DiffieHellman KeyFactory",
              FiniteFieldDhKeyAgreementUtil::dhKeyFactory);

      return keyFactory.generatePublic(new DHPublicKeySpec(y, FFDHE_3072_P, FFDHE_3072_G));
    } catch (InvalidKeySpecException e) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, e);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  /**
   * Compute an RFC 7919 {@code ffdhe3072} shared secret.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the local ephemeral private key.
   * @param peerPublicKey the peer ephemeral public key.
   * @return the shared secret as a 384-byte unsigned big-endian integer.
   * @throws UaException if agreement fails.
   */
  public static byte[] agreeFfdhe3072(
      ProviderProfile providerProfile, PrivateKey privateKey, PublicKey peerPublicKey)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");
    requireNonNull(privateKey, "privateKey");
    requireNonNull(peerPublicKey, "peerPublicKey");

    encodeFfdhe3072(peerPublicKey);

    try {
      KeyAgreement keyAgreement =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              "DiffieHellman KeyAgreement",
              FiniteFieldDhKeyAgreementUtil::dhKeyAgreement);

      keyAgreement.init(privateKey);
      keyAgreement.doPhase(peerPublicKey, true);

      // Capture the raw provider secret in a local and the BigInteger's magnitude copy so both
      // can be zeroed; the BigInteger itself is immutable and cannot be cleared, but its byte
      // copies must not linger on the heap.
      byte[] secretBytes = keyAgreement.generateSecret();
      byte[] secretMagnitude = null;
      try {
        secretMagnitude = new BigInteger(1, secretBytes).toByteArray();

        return fixedWidthUnsignedSecret(secretMagnitude);
      } finally {
        Arrays.fill(secretBytes, (byte) 0);
        if (secretMagnitude != null) {
          Arrays.fill(secretMagnitude, (byte) 0);
        }
      }
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  static DHParameterSpec ffdhe3072ParameterSpec() {
    return new DHParameterSpec(FFDHE_3072_P, FFDHE_3072_G, FFDHE_3072_PRIVATE_KEY_LENGTH_BITS);
  }

  static BigInteger ffdhe3072Prime() {
    return FFDHE_3072_P;
  }

  private static void validateFfdhe3072PublicValue(BigInteger y) throws UaException {
    BigInteger max = FFDHE_3072_P.subtract(TWO);

    if (y.compareTo(TWO) < 0 || y.compareTo(max) > 0) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, "ffdhe3072 public value out of range");
    }
  }

  private static byte[] fixedWidthUnsigned(BigInteger value) throws UaException {
    return fixedWidthUnsignedSecret(value.toByteArray());
  }

  /**
   * Left-pad a {@code BigInteger.toByteArray()} magnitude to the fixed ffdhe3072 wire width.
   *
   * <p>The caller owns {@code magnitude} and is responsible for zeroing it when it holds secret
   * material; this method only reads from it and never retains a reference.
   */
  private static byte[] fixedWidthUnsignedSecret(byte[] magnitude) throws UaException {
    byte[] bytes = magnitude;
    int from = 0;

    if (bytes.length > 1 && bytes[0] == 0) {
      from = 1;
    }

    int len = bytes.length - from;
    if (len > FFDHE_3072_PUBLIC_KEY_LENGTH) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, "public value does not fit wire width");
    }

    byte[] fixed = new byte[FFDHE_3072_PUBLIC_KEY_LENGTH];

    System.arraycopy(bytes, from, fixed, FFDHE_3072_PUBLIC_KEY_LENGTH - len, len);

    return fixed;
  }

  private static KeyPairGenerator dhKeyPairGenerator(Provider provider)
      throws GeneralSecurityException {

    try {
      return KeyPairGenerator.getInstance("DiffieHellman", provider);
    } catch (NoSuchAlgorithmException e) {
      return KeyPairGenerator.getInstance("DH", provider);
    }
  }

  private static KeyFactory dhKeyFactory(Provider provider) throws GeneralSecurityException {
    try {
      return KeyFactory.getInstance("DiffieHellman", provider);
    } catch (NoSuchAlgorithmException e) {
      return KeyFactory.getInstance("DH", provider);
    }
  }

  private static KeyAgreement dhKeyAgreement(Provider provider) throws GeneralSecurityException {
    try {
      return KeyAgreement.getInstance("DiffieHellman", provider);
    } catch (NoSuchAlgorithmException e) {
      return KeyAgreement.getInstance("DH", provider);
    }
  }
}
