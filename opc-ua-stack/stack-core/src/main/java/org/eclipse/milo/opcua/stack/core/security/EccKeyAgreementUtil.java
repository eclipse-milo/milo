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
 * Key-agreement and AEAD derivation helpers for enhanced SecureChannel setup.
 *
 * <p>For ECC and RSA-DH policies, the OpenSecureChannel {@code ClientNonce} and {@code ServerNonce}
 * fields carry ephemeral public keys instead of random nonce bytes. Callers generate an ephemeral
 * key pair, encode the public key, decode the peer's public key, compute a shared secret, and then
 * derive directional client/server key material from the same wire public keys that appeared in the
 * request and response.
 *
 * <p>The returned key material keeps OPC UA's direction names: client keys protect chunks sent by
 * the client, and server keys protect chunks sent by the server. A concrete {@code SecureChannel}
 * maps those protocol directions onto local encryption and decryption roles.
 *
 * <p>The profile selects both the key-agreement family and the HKDF hash. P-256 profile families,
 * X25519, and ffdhe3072 derive key material with HKDF-SHA-256; P-384 profile families and X448
 * derive the same directional fields with HKDF-SHA-384. Callers should pass the negotiated {@link
 * SecurityPolicyProfile} through this helper instead of selecting the hash from key length alone.
 */
@NullMarked
public final class EccKeyAgreementUtil {

  /** The IV length used by current OPC UA AEAD profiles. */
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

    return generateEcKeyPair(providerProfile, "secp256r1");
  }

  /**
   * Generate an ephemeral NIST P-384 ECDH key pair.
   *
   * @param providerProfile the provider profile to use.
   * @return an ephemeral P-384 key pair.
   * @throws UaException if the key generation fails.
   */
  public static KeyPair generateNistP384KeyPair(ProviderProfile providerProfile)
      throws UaException {

    return generateEcKeyPair(providerProfile, "secp384r1");
  }

  /**
   * Generate an ephemeral Brainpool P-256r1 ECDH key pair.
   *
   * @param providerProfile the provider profile to use.
   * @return an ephemeral Brainpool P-256r1 key pair.
   * @throws UaException if the key generation fails.
   */
  public static KeyPair generateBrainpoolP256r1KeyPair(ProviderProfile providerProfile)
      throws UaException {

    return generateEcKeyPair(providerProfile, "brainpoolP256r1");
  }

  /**
   * Generate an ephemeral Brainpool P-384r1 ECDH key pair.
   *
   * <p>Current Brainpool P-384 policies require the Bouncy Castle provider profile.
   *
   * @param providerProfile the provider profile to use.
   * @return an ephemeral Brainpool P-384r1 key pair.
   * @throws UaException if the key generation fails.
   */
  public static KeyPair generateBrainpoolP384r1KeyPair(ProviderProfile providerProfile)
      throws UaException {

    return generateEcKeyPair(providerProfile, "brainpoolP384r1");
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
   * Generate an ephemeral X448 key pair.
   *
   * @param providerProfile the provider profile to use.
   * @return an ephemeral X448 key pair.
   * @throws UaException if key generation fails.
   */
  public static KeyPair generateX448KeyPair(ProviderProfile providerProfile) throws UaException {
    requireNonNull(providerProfile, "providerProfile");

    try {
      KeyPairGenerator generator =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              "X448 KeyPairGenerator",
              p -> KeyPairGenerator.getInstance("X448", p));

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
   * Compute a P-384 ECDH shared secret.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the local ephemeral private key.
   * @param peerPublicKey the peer ephemeral public key.
   * @return the shared secret.
   * @throws UaException if agreement fails.
   */
  public static byte[] agreeNistP384(
      ProviderProfile providerProfile, PrivateKey privateKey, PublicKey peerPublicKey)
      throws UaException {

    EccPublicKeyCodec.encodeNistP384(peerPublicKey);

    return agree(providerProfile, "ECDH", privateKey, peerPublicKey);
  }

  /**
   * Compute a Brainpool P-256r1 ECDH shared secret.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the local ephemeral private key.
   * @param peerPublicKey the peer ephemeral public key.
   * @return the shared secret.
   * @throws UaException if agreement fails.
   */
  public static byte[] agreeBrainpoolP256r1(
      ProviderProfile providerProfile, PrivateKey privateKey, PublicKey peerPublicKey)
      throws UaException {

    EccPublicKeyCodec.encodeBrainpoolP256r1(peerPublicKey);

    return agree(providerProfile, "ECDH", privateKey, peerPublicKey);
  }

  /**
   * Compute a Brainpool P-384r1 ECDH shared secret.
   *
   * <p>The peer key is validated as a Brainpool P-384r1 public key before the JCA key agreement is
   * invoked.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the local ephemeral private key.
   * @param peerPublicKey the peer ephemeral public key.
   * @return the shared secret.
   * @throws UaException if agreement fails.
   */
  public static byte[] agreeBrainpoolP384r1(
      ProviderProfile providerProfile, PrivateKey privateKey, PublicKey peerPublicKey)
      throws UaException {

    EccPublicKeyCodec.encodeBrainpoolP384r1(peerPublicKey);

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
   * Compute an X448 shared secret and reject all-zero results.
   *
   * @param providerProfile the provider profile to use.
   * @param privateKey the local ephemeral private key.
   * @param peerPublicKey the peer ephemeral public key.
   * @return the shared secret.
   * @throws UaException if agreement fails or produces an all-zero shared secret.
   */
  public static byte[] agreeX448(
      ProviderProfile providerProfile, PrivateKey privateKey, PublicKey peerPublicKey)
      throws UaException {

    EccPublicKeyCodec.encodeX448(peerPublicKey);

    byte[] sharedSecret = agree(providerProfile, "X448", privateKey, peerPublicKey);

    if (allZero(sharedSecret)) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "X448 shared secret is zero");
    }

    return sharedSecret;
  }

  /**
   * Derive directional AEAD key material for the current ECC policy families.
   *
   * <p>This entry point is retained for callers that are already on the ECC helper path. New shared
   * SecureChannel strategy code that also handles RSA-DH should call {@link #deriveAeadKeyMaterial}
   * so the method name matches the wider set of supported key-agreement families.
   */
  public static DerivedKeyMaterial deriveEccAeadKeyMaterial(
      ProviderProfile providerProfile,
      SecurityPolicyProfile profile,
      byte[] sharedSecret,
      ByteString clientNonce,
      ByteString serverNonce)
      throws UaException {

    return deriveAeadKeyMaterial(providerProfile, profile, sharedSecret, clientNonce, serverNonce);
  }

  /**
   * Derive directional AEAD key material from ephemeral key-agreement input key material.
   *
   * <p>The profile supplies the encryption-key length. The HKDF salt includes the total derived
   * length, a direction label, and both ephemeral public keys in the order required for that
   * direction. The same salt bytes are also used as HKDF {@code info}, matching the OPC UA
   * SecureChannel derivation rules for ECC and RSA-DH AEAD profiles. Initial tokens use the fresh
   * shared secret as the input key material. Renewed tokens use the Part 6 renewal material derived
   * from the current token and the fresh shared secret.
   *
   * @param providerProfile the provider profile to use for the profile's HKDF HMAC.
   * @param profile the security-policy profile.
   * @param inputKeyMaterial the initial shared secret or renewal input key material.
   * @param clientNonce the client ephemeral public key bytes.
   * @param serverNonce the server ephemeral public key bytes.
   * @return the derived client and server key material.
   * @throws UaException if key derivation fails.
   */
  public static DerivedKeyMaterial deriveAeadKeyMaterial(
      ProviderProfile providerProfile,
      SecurityPolicyProfile profile,
      byte[] inputKeyMaterial,
      ByteString clientNonce,
      ByteString serverNonce)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");
    requireNonNull(profile, "profile");
    requireNonNull(inputKeyMaterial, "inputKeyMaterial");
    requireNonNull(clientNonce, "clientNonce");
    requireNonNull(serverNonce, "serverNonce");

    if (profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_NIST_P256
        && profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_NIST_P384
        && profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_BRAINPOOL_P256R1
        && profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_BRAINPOOL_P384R1
        && profile.keyAgreementAxis() != KeyAgreementAxis.X25519
        && profile.keyAgreementAxis() != KeyAgreementAxis.X448
        && profile.keyAgreementAxis() != KeyAgreementAxis.FFDH_3072) {
      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "profile does not use an AEAD HKDF key-agreement axis: "
              + profile.securityPolicy().getUri());
    }

    int totalLength = profile.symmetricEncryptionKeySize() + AEAD_INITIALIZATION_VECTOR_LENGTH;
    byte[] clientSalt = hkdfSalt("opcua-client", totalLength, clientNonce, serverNonce);
    byte[] serverSalt = hkdfSalt("opcua-server", totalLength, serverNonce, clientNonce);
    byte[] clientMaterial =
        hkdf(providerProfile, profile, inputKeyMaterial, clientSalt, clientSalt, totalLength);
    byte[] serverMaterial =
        hkdf(providerProfile, profile, inputKeyMaterial, serverSalt, serverSalt, totalLength);

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

  private static KeyPair generateEcKeyPair(ProviderProfile providerProfile, String curveName)
      throws UaException {

    requireNonNull(providerProfile, "providerProfile");

    try {
      KeyPairGenerator generator =
          SecurityProviderSupport.withProviderProfile(
              providerProfile, "EC KeyPairGenerator", p -> KeyPairGenerator.getInstance("EC", p));

      generator.initialize(new ECGenParameterSpec(curveName));

      return generator.generateKeyPair();
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  private static byte[] hkdf(
      ProviderProfile providerProfile,
      SecurityPolicyProfile profile,
      byte[] ikm,
      byte[] salt,
      byte[] info,
      int length)
      throws UaException {

    String hmacTransformation = hkdfHmacTransformation(profile);

    try {
      Provider provider =
          SecurityProviderSupport.withProviderProfile(
              providerProfile,
              hmacTransformation,
              p -> {
                Mac.getInstance(hmacTransformation, p);
                return p;
              });

      return switch (profile.keyAgreementAxis()) {
        case ECDH_NIST_P384, ECDH_BRAINPOOL_P384R1, X448 ->
            HkdfUtil.hkdfSha384(ikm, salt, info, length, provider);
        case ECDH_NIST_P256, ECDH_BRAINPOOL_P256R1, X25519, FFDH_3072 ->
            HkdfUtil.hkdfSha256(ikm, salt, info, length, provider);
        default ->
            throw new UaException(
                StatusCodes.Bad_SecurityPolicyRejected,
                "profile does not use an AEAD HKDF axis: " + profile.securityPolicy().getUri());
      };
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  private static String hkdfHmacTransformation(SecurityPolicyProfile profile) throws UaException {
    return switch (profile.keyAgreementAxis()) {
      case ECDH_NIST_P384, ECDH_BRAINPOOL_P384R1, X448 -> "HmacSHA384";
      case ECDH_NIST_P256, ECDH_BRAINPOOL_P256R1, X25519, FFDH_3072 -> "HmacSHA256";
      default ->
          throw new UaException(
              StatusCodes.Bad_SecurityPolicyRejected,
              "profile does not use an AEAD HKDF axis: " + profile.securityPolicy().getUri());
    };
  }

  private static boolean allZero(byte[] bytes) {
    byte value = 0;

    for (byte b : bytes) {
      value |= b;
    }

    return value == 0;
  }

  /**
   * Directional symmetric key and IV bytes derived for an AEAD policy.
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
