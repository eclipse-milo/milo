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

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.HkdfUtil;
import org.jspecify.annotations.NullMarked;

/**
 * Key-agreement helpers for ECC SecureChannel setup.
 *
 * <p>For ECC policies, the OpenSecureChannel {@code ClientNonce} and {@code ServerNonce} fields
 * carry ephemeral public keys instead of random nonce bytes. Callers generate an ephemeral key
 * pair, encode the public key with {@link EccPublicKeyCodec}, decode the peer's public key, compute
 * a shared secret, and then derive directional client/server key material from the same wire public
 * keys that appeared in the request and response.
 *
 * <p>The returned key material keeps OPC UA's direction names: client keys protect chunks sent by
 * the client, and server keys protect chunks sent by the server. A concrete {@code SecureChannel}
 * maps those protocol directions onto local encryption and decryption roles.
 */
@NullMarked
public final class EccKeyAgreementUtil {

  /** The IV length used by current OPC UA ECC AEAD profiles. */
  public static final int AEAD_INITIALIZATION_VECTOR_LENGTH = 12;

  private EccKeyAgreementUtil() {}

  /**
   * Generate an ephemeral NIST P-256 ECDH key pair.
   *
   * @param providerProfile the provider profile to use.
   * @return an ephemeral P-256 key pair.
   * @throws UaException if the key generation fails.
   */
  public static KeyPair generateNistP256KeyPair(ProviderProfile providerProfile)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");

    try {
      KeyPairGenerator generator =
          SecurityProviderSupport.withProviderProfile(
              providerProfile, "EC KeyPairGenerator", p -> KeyPairGenerator.getInstance("EC", p));

      generator.initialize(new ECGenParameterSpec("secp256r1"));

      return generator.generateKeyPair();
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  /**
   * Generate an ephemeral X25519 key pair.
   *
   * @param providerProfile the provider profile to use.
   * @return an ephemeral X25519 key pair.
   * @throws UaException if key generation fails.
   */
  public static KeyPair generateX25519KeyPair(ProviderProfile providerProfile) throws UaException {
    requireNonNull(providerProfile, "providerProfile");

    try {
      KeyPairGenerator generator =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              "X25519 KeyPairGenerator",
              p -> KeyPairGenerator.getInstance("X25519", p));

      return generator.generateKeyPair();
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  /**
   * Compute a P-256 ECDH shared secret.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the local ephemeral private key.
   * @param peerPublicKey the peer ephemeral public key.
   * @return the shared secret.
   * @throws UaException if agreement fails.
   */
  public static byte[] agreeNistP256(
      ProviderProfile providerProfile, PrivateKey privateKey, PublicKey peerPublicKey)
      throws UaException {

    EccPublicKeyCodec.encodeNistP256(peerPublicKey);

    return agree(providerProfile, "ECDH", privateKey, peerPublicKey);
  }

  /**
   * Compute an X25519 shared secret and reject all-zero results.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the local ephemeral private key.
   * @param peerPublicKey the peer ephemeral public key.
   * @return the shared secret.
   * @throws UaException if agreement fails or produces an all-zero shared secret.
   */
  public static byte[] agreeX25519(
      ProviderProfile providerProfile, PrivateKey privateKey, PublicKey peerPublicKey)
      throws UaException {

    EccPublicKeyCodec.encodeX25519(peerPublicKey);

    byte[] sharedSecret = agree(providerProfile, "X25519", privateKey, peerPublicKey);

    if (allZero(sharedSecret)) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "X25519 shared secret is zero");
    }

    return sharedSecret;
  }

  /**
   * Derive directional ECC AEAD key material from an ECC shared secret.
   *
   * <p>The profile supplies the encryption-key length. The HKDF salt includes the total derived
   * length, a direction label, and both ephemeral public keys in the order required for that
   * direction. The same salt bytes are also used as HKDF {@code info}, matching the OPC UA
   * SecureChannel derivation rules for these profiles.
   *
   * @param providerProfile the provider profile to use for HMAC-SHA-256.
   * @param profile the ECC security-policy profile.
   * @param sharedSecret the ECDH or X25519 shared secret.
   * @param clientNonce the client ephemeral public key bytes.
   * @param serverNonce the server ephemeral public key bytes.
   * @return the derived client and server key material.
   * @throws UaException if key derivation fails.
   */
  public static DerivedKeyMaterial deriveEccAeadKeyMaterial(
      ProviderProfile providerProfile,
      SecurityPolicyProfile profile,
      byte[] sharedSecret,
      ByteString clientNonce,
      ByteString serverNonce)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");
    requireNonNull(profile, "profile");
    requireNonNull(sharedSecret, "sharedSecret");
    requireNonNull(clientNonce, "clientNonce");
    requireNonNull(serverNonce, "serverNonce");

    if (profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_NIST_P256
        && profile.keyAgreementAxis() != KeyAgreementAxis.X25519) {
      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "profile does not use an ECC key-agreement axis: " + profile.securityPolicy().getUri());
    }

    int totalLength = profile.symmetricEncryptionKeySize() + AEAD_INITIALIZATION_VECTOR_LENGTH;
    byte[] clientSalt = hkdfSalt("opcua-client", totalLength, clientNonce, serverNonce);
    byte[] serverSalt = hkdfSalt("opcua-server", totalLength, serverNonce, clientNonce);
    byte[] clientMaterial =
        hkdfSha256(providerProfile, sharedSecret, clientSalt, clientSalt, totalLength);
    byte[] serverMaterial =
        hkdfSha256(providerProfile, sharedSecret, serverSalt, serverSalt, totalLength);

    return new DerivedKeyMaterial(
        Arrays.copyOfRange(clientMaterial, 0, profile.symmetricEncryptionKeySize()),
        Arrays.copyOfRange(clientMaterial, profile.symmetricEncryptionKeySize(), totalLength),
        Arrays.copyOfRange(serverMaterial, 0, profile.symmetricEncryptionKeySize()),
        Arrays.copyOfRange(serverMaterial, profile.symmetricEncryptionKeySize(), totalLength));
  }

  static byte[] hkdfSalt(
      String label, int totalLength, ByteString firstNonce, ByteString secondNonce) {
    byte[] labelBytes = label.getBytes(StandardCharsets.UTF_8);
    byte[] firstBytes = firstNonce.bytesOrEmpty();
    byte[] secondBytes = secondNonce.bytesOrEmpty();
    byte[] salt = new byte[2 + labelBytes.length + firstBytes.length + secondBytes.length];

    salt[0] = (byte) (totalLength & 0xFF);
    salt[1] = (byte) ((totalLength >>> 8) & 0xFF);
    System.arraycopy(labelBytes, 0, salt, 2, labelBytes.length);
    System.arraycopy(firstBytes, 0, salt, 2 + labelBytes.length, firstBytes.length);
    System.arraycopy(
        secondBytes, 0, salt, 2 + labelBytes.length + firstBytes.length, secondBytes.length);

    return salt;
  }

  private static byte[] agree(
      ProviderProfile providerProfile,
      String transformation,
      PrivateKey privateKey,
      PublicKey peerPublicKey)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");
    requireNonNull(privateKey, "privateKey");
    requireNonNull(peerPublicKey, "peerPublicKey");

    try {
      KeyAgreement keyAgreement =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              transformation + " KeyAgreement",
              p -> KeyAgreement.getInstance(transformation, p));

      keyAgreement.init(privateKey);
      keyAgreement.doPhase(peerPublicKey, true);

      return keyAgreement.generateSecret();
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  private static byte[] hkdfSha256(
      ProviderProfile providerProfile, byte[] ikm, byte[] salt, byte[] info, int length)
      throws UaException {

    try {
      Provider provider =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              "HmacSHA256",
              p -> {
                Mac.getInstance("HmacSHA256", p);
                return p;
              });

      return HkdfUtil.hkdfSha256(ikm, salt, info, length, provider);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  private static boolean allZero(byte[] bytes) {
    byte value = 0;

    for (byte b : bytes) {
      value |= b;
    }

    return value == 0;
  }

  /**
   * Directional symmetric key and IV bytes derived for an ECC AEAD policy.
   *
   * <p>The record defensively copies all arrays on construction and access so callers can clear or
   * reuse their own buffers without changing installed channel material.
   */
  public record DerivedKeyMaterial(
      byte[] clientEncryptionKey,
      byte[] clientInitializationVector,
      byte[] serverEncryptionKey,
      byte[] serverInitializationVector) {

    public DerivedKeyMaterial {
      clientEncryptionKey = clientEncryptionKey.clone();
      clientInitializationVector = clientInitializationVector.clone();
      serverEncryptionKey = serverEncryptionKey.clone();
      serverInitializationVector = serverInitializationVector.clone();
    }

    @Override
    public byte[] clientEncryptionKey() {
      return clientEncryptionKey.clone();
    }

    @Override
    public byte[] clientInitializationVector() {
      return clientInitializationVector.clone();
    }

    @Override
    public byte[] serverEncryptionKey() {
      return serverEncryptionKey.clone();
    }

    @Override
    public byte[] serverInitializationVector() {
      return serverInitializationVector.clone();
    }
  }
}
