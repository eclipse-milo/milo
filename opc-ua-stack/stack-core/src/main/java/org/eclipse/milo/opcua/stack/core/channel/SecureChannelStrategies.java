/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import static java.util.Objects.requireNonNull;

import io.netty.buffer.ByteBuf;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.AeadCipherUtil;
import org.eclipse.milo.opcua.stack.core.security.EccKeyAgreementUtil;
import org.eclipse.milo.opcua.stack.core.security.EccPublicKeyCodec;
import org.eclipse.milo.opcua.stack.core.security.EccSignatureUtil;
import org.eclipse.milo.opcua.stack.core.security.FiniteFieldDhKeyAgreementUtil;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.PShaUtil;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Resolves a {@link SecurityPolicyProfile} into the cryptographic operations used by SecureChannel
 * chunk codecs.
 *
 * <p>OPC UA Secure Conversation combines several related but distinct security responsibilities.
 * OpenSecureChannel chunks can be authenticated with an application-instance certificate, legacy
 * RSA policies can also encrypt those opening chunks with the peer certificate, nonces are used to
 * derive token-specific symmetric keys, and ordinary service chunks are then signed and optionally
 * encrypted with those symmetric keys. Keeping those responsibilities separate makes the codec flow
 * easier to read and lets each policy family plug into the part of the pipeline it actually uses.
 *
 * <p>Unsupported strategies are deliberate guard rails. A profile may be recognized by the public
 * registry before every SecureChannel operation for that profile is enabled; if such a profile
 * reaches the codec too early, the strategy fails at the boundary with {@code
 * Bad_SecurityPolicyRejected} instead of silently falling back to an RSA assumption.
 */
@NullMarked
final class SecureChannelStrategies {

  private static final SecurityProviderResolver PROVIDER_RESOLVER =
      SecurityProviderResolver.create();

  private static final AuthenticationStrategy NO_AUTHENTICATION = new NoAuthenticationStrategy();
  private static final AuthenticationStrategy RSA_AUTHENTICATION = new RsaAuthenticationStrategy();
  private static final AuthenticationStrategy ECDSA_P256_AUTHENTICATION =
      new EcdsaP256AuthenticationStrategy();
  private static final AuthenticationStrategy ECDSA_P384_AUTHENTICATION =
      new EcdsaP384AuthenticationStrategy();
  private static final AuthenticationStrategy ED25519_AUTHENTICATION =
      new Ed25519AuthenticationStrategy();
  private static final AuthenticationStrategy ED448_AUTHENTICATION =
      new Ed448AuthenticationStrategy();

  @SuppressWarnings("unused")
  private static final AuthenticationStrategy UNSUPPORTED_AUTHENTICATION =
      new UnsupportedAuthenticationStrategy();

  private static final KeyAgreementStrategy NO_KEY_AGREEMENT = new NoKeyAgreementStrategy();
  private static final KeyAgreementStrategy RSA_NONCE_KEY_AGREEMENT =
      new RsaNoncePShaKeyAgreementStrategy();
  private static final KeyAgreementStrategy ECDH_NIST_P256_KEY_AGREEMENT =
      new EcdhNistP256HkdfKeyAgreementStrategy();
  private static final KeyAgreementStrategy ECDH_NIST_P384_KEY_AGREEMENT =
      new EcdhNistP384HkdfKeyAgreementStrategy();
  private static final KeyAgreementStrategy ECDH_BRAINPOOL_P256R1_KEY_AGREEMENT =
      new EcdhBrainpoolP256r1HkdfKeyAgreementStrategy();
  private static final KeyAgreementStrategy ECDH_BRAINPOOL_P384R1_KEY_AGREEMENT =
      new EcdhBrainpoolP384r1HkdfKeyAgreementStrategy();
  private static final KeyAgreementStrategy X25519_KEY_AGREEMENT =
      new X25519HkdfKeyAgreementStrategy();
  private static final KeyAgreementStrategy X448_KEY_AGREEMENT = new X448HkdfKeyAgreementStrategy();
  private static final KeyAgreementStrategy FFDH_3072_KEY_AGREEMENT =
      new Ffdh3072HkdfKeyAgreementStrategy();

  @SuppressWarnings("unused")
  private static final KeyAgreementStrategy UNSUPPORTED_KEY_AGREEMENT =
      new UnsupportedKeyAgreementStrategy();

  private static final ChunkProtectionStrategy NO_CHUNK_PROTECTION =
      new NoChunkProtectionStrategy();
  private static final ChunkProtectionStrategy CBC_HMAC_CHUNK_PROTECTION =
      new CbcHmacChunkProtectionStrategy();
  private static final ChunkProtectionStrategy AES_GCM_CHUNK_PROTECTION =
      new AesGcmChunkProtectionStrategy();
  private static final ChunkProtectionStrategy CHACHA20_POLY1305_CHUNK_PROTECTION =
      new ChaCha20Poly1305ChunkProtectionStrategy();

  private static final AsymmetricEncryptionStrategy NO_ASYMMETRIC_ENCRYPTION =
      new NoAsymmetricEncryptionStrategy();
  private static final AsymmetricEncryptionStrategy RSA_ASYMMETRIC_ENCRYPTION =
      new RsaAsymmetricEncryptionStrategy();

  private SecureChannelStrategies() {}

  /** Returns the strategy that authenticates OpenSecureChannel chunks for {@code profile}. */
  static AuthenticationStrategy authentication(SecurityPolicyProfile profile) {
    AuthAxis authAxis = profile.authAxis();

    return switch (authAxis) {
      case NONE -> NO_AUTHENTICATION;
      case RSA_PKCS1_SHA1, RSA_PKCS1_SHA256, RSA_PSS_SHA256 -> RSA_AUTHENTICATION;
      case ECDSA_NIST_P256_SHA256, ECDSA_BRAINPOOL_P256R1_SHA256 -> ECDSA_P256_AUTHENTICATION;
      case ECDSA_NIST_P384_SHA384, ECDSA_BRAINPOOL_P384R1_SHA384 -> ECDSA_P384_AUTHENTICATION;
      case ED25519 -> ED25519_AUTHENTICATION;
      case ED448 -> ED448_AUTHENTICATION;
    };
  }

  /** Returns the strategy that derives symmetric keys from negotiated channel nonces. */
  @SuppressWarnings("DuplicatedCode")
  static KeyAgreementStrategy keyAgreement(SecurityPolicyProfile profile) {
    KeyAgreementAxis keyAgreementAxis = profile.keyAgreementAxis();

    return switch (keyAgreementAxis) {
      case NONE -> NO_KEY_AGREEMENT;
      case RSA_NONCE -> RSA_NONCE_KEY_AGREEMENT;
      case ECDH_NIST_P256 -> ECDH_NIST_P256_KEY_AGREEMENT;
      case ECDH_NIST_P384 -> ECDH_NIST_P384_KEY_AGREEMENT;
      case ECDH_BRAINPOOL_P256R1 -> ECDH_BRAINPOOL_P256R1_KEY_AGREEMENT;
      case ECDH_BRAINPOOL_P384R1 -> ECDH_BRAINPOOL_P384R1_KEY_AGREEMENT;
      case X25519 -> X25519_KEY_AGREEMENT;
      case X448 -> X448_KEY_AGREEMENT;
      case FFDH_3072 -> FFDH_3072_KEY_AGREEMENT;
    };
  }

  /** Returns the strategy that signs, verifies, pads, and encrypts symmetric service chunks. */
  static ChunkProtectionStrategy chunkProtection(SecurityPolicyProfile profile) {
    ChunkProtectionAxis chunkProtectionAxis = profile.chunkProtectionAxis();

    return switch (chunkProtectionAxis) {
      case NONE -> NO_CHUNK_PROTECTION;
      case CBC_HMAC -> CBC_HMAC_CHUNK_PROTECTION;
      case AES_GCM -> AES_GCM_CHUNK_PROTECTION;
      case CHACHA20_POLY1305 -> CHACHA20_POLY1305_CHUNK_PROTECTION;
    };
  }

  /**
   * Returns the strategy for asymmetric OpenSecureChannel encryption.
   *
   * <p>This is intentionally independent from {@link #authentication(SecurityPolicyProfile)}
   * because certificate authentication does not always imply certificate encryption.
   */
  static AsymmetricEncryptionStrategy asymmetricEncryption(SecurityPolicyProfile profile) {
    return switch (profile.asymmetricEncryptionAlgorithm()) {
      case None -> NO_ASYMMETRIC_ENCRYPTION;
      case Rsa15, RsaOaepSha1, RsaOaepSha256 -> RSA_ASYMMETRIC_ENCRYPTION;
      default ->
          throw new UaRuntimeException(
              StatusCodes.Bad_SecurityPolicyRejected,
              "unsupported asymmetric encryption algorithm: "
                  + profile.asymmetricEncryptionAlgorithm());
    };
  }

  /**
   * Authenticates OpenSecureChannel chunks with the local private key and the peer certificate.
   *
   * <p>The signed input is accepted as one or more buffers because SecureChannel-enhancement
   * profiles bind the first response signature to both the response bytes and the first request
   * signature.
   */
  interface AuthenticationStrategy {

    /** Produces the signature bytes appended to an outgoing OpenSecureChannel chunk. */
    byte[] sign(SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes)
        throws UaException;

    /** Verifies the signature bytes read from an incoming OpenSecureChannel chunk. */
    void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes)
        throws UaException;

    /** Returns the fixed signature length used by this profile and certificate. */
    int signatureSize(Certificate certificate);
  }

  /**
   * Derives the pair of directional symmetric key sets installed on a SecureChannel token.
   *
   * <p>The returned keys are directional rather than local/remote. Client implementations send with
   * client keys, and servers send with server keys; {@link SecureChannel} maps those directions to
   * encryption and decryption for the current role.
   */
  interface KeyAgreementStrategy {

    default KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      throw unsupported(profile, "ephemeral key generation");
    }

    default ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      throw unsupported(profile, "public-key encoding");
    }

    default PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      throw unsupported(profile, "public-key decoding");
    }

    default byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      throw unsupported(profile, "key agreement");
    }

    @SuppressWarnings("unused")
    default EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      throw unsupported(profile, "HKDF key derivation");
    }

    ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize);
  }

  /**
   * Protects symmetric service chunks after OpenSecureChannel has installed token keys.
   *
   * <p>The strategy owns every profile-dependent byte-level detail around symmetric chunks:
   * ciphertext and plaintext sizing, signatures or authentication tags, padding, and cipher
   * creation. CBC/HMAC policies express those details as block padding plus an HMAC; other profile
   * families can use the same boundary without leaking their framing rules into the generic codecs.
   */
  interface ChunkProtectionStrategy {

    int cipherTextBlockSize(SecurityPolicyProfile profile);

    int plainTextBlockSize(SecurityPolicyProfile profile);

    int signatureSize(SecurityPolicyProfile profile);

    int paddingOverhead(int cipherTextBlockSize);

    void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer);

    int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer);

    void verifyPadding(
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer)
        throws UaException;

    byte[] sign(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys, ByteBuffer chunkNioBuffer)
        throws UaException;

    default byte[] sign(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuffer aad)
        throws UaException {

      return sign(channel, securityKeys, aad);
    }

    void verify(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys, ByteBuf chunkBuffer)
        throws UaException;

    default void verify(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuf chunkBuffer)
        throws UaException {

      verify(channel, securityKeys, chunkBuffer);
    }

    Cipher getEncryptionCipher(SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys)
        throws UaException;

    Cipher getDecryptionCipher(SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys)
        throws UaException;

    default boolean isAead() {
      return false;
    }

    default Cipher getEncryptionCipher(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuffer aad)
        throws UaException {

      return getEncryptionCipher(channel, securityKeys);
    }

    default Cipher getDecryptionCipher(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuffer aad)
        throws UaException {

      return getDecryptionCipher(channel, securityKeys);
    }
  }

  /**
   * Encrypts and decrypts OpenSecureChannel chunks that use certificate-based asymmetric
   * encryption.
   *
   * <p>This strategy is only about RSA-style OpenSecureChannel encryption and its padding rules.
   * OpenSecureChannel authentication is handled separately by {@link AuthenticationStrategy}.
   */
  interface AsymmetricEncryptionStrategy {

    int cipherTextBlockSize(Certificate certificate);

    int plainTextBlockSize(SecurityPolicyProfile profile, Certificate certificate);

    int paddingOverhead(int cipherTextBlockSize);

    void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer);

    int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer);

    void verifyPadding(
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer)
        throws UaException;

    Cipher getEncryptionCipher(SecurityPolicyProfile profile, Certificate remoteCertificate)
        throws UaException;

    Cipher getDecryptionCipher(SecurityPolicyProfile profile, PrivateKey privateKey)
        throws UaException;
  }

  private static final class NoAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public byte[] sign(
        SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes) {
      return new byte[0];
    }

    @Override
    public void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes) {}

    @Override
    public int signatureSize(Certificate certificate) {
      return 0;
    }
  }

  private static final class RsaAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public byte[] sign(
        SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes)
        throws UaException {

      return SignatureUtil.sign(profile.asymmetricSignatureAlgorithm(), privateKey, signedBytes);
    }

    @Override
    public void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes)
        throws UaException {

      try {
        Signature signature =
            Signature.getInstance(profile.asymmetricSignatureAlgorithm().getTransformation());

        signature.initVerify(certificate);

        for (ByteBuffer signedByteBuffer : signedBytes) {
          signature.update(signedByteBuffer);
        }

        if (!signature.verify(signatureBytes)) {
          throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
        }
      } catch (java.security.NoSuchAlgorithmException e) {
        throw new UaException(StatusCodes.Bad_InternalError, e);
      } catch (SignatureException e) {
        throw new UaException(StatusCodes.Bad_ApplicationSignatureInvalid, e);
      } catch (InvalidKeyException e) {
        throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
      }
    }

    @Override
    public int signatureSize(Certificate certificate) {
      return (getRsaKeyLength(certificate) + 7) / 8;
    }
  }

  private static final class EcdsaP256AuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public byte[] sign(
        SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes)
        throws UaException {

      return EccSignatureUtil.signEcdsaP256Sha256P1363(
          resolveProviderProfile(profile), privateKey, signedBytes);
    }

    @Override
    public void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes)
        throws UaException {

      EccSignatureUtil.verifyEcdsaP256Sha256P1363(
          resolveProviderProfile(profile), certificate.getPublicKey(), signatureBytes, signedBytes);
    }

    @Override
    public int signatureSize(Certificate certificate) {
      return EccSignatureUtil.ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH;
    }
  }

  private static final class EcdsaP384AuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public byte[] sign(
        SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes)
        throws UaException {

      return EccSignatureUtil.signEcdsaP384Sha384P1363(
          resolveProviderProfile(profile), privateKey, signedBytes);
    }

    @Override
    public void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes)
        throws UaException {

      EccSignatureUtil.verifyEcdsaP384Sha384P1363(
          resolveProviderProfile(profile), certificate.getPublicKey(), signatureBytes, signedBytes);
    }

    @Override
    public int signatureSize(Certificate certificate) {
      return EccSignatureUtil.ECDSA_P384_SHA384_P1363_SIGNATURE_LENGTH;
    }
  }

  private static final class Ed25519AuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public byte[] sign(
        SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes)
        throws UaException {

      return EccSignatureUtil.signEd25519(resolveProviderProfile(profile), privateKey, signedBytes);
    }

    @Override
    public void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes)
        throws UaException {

      EccSignatureUtil.verifyEd25519(
          resolveProviderProfile(profile), certificate.getPublicKey(), signatureBytes, signedBytes);
    }

    @Override
    public int signatureSize(Certificate certificate) {
      return EccSignatureUtil.ED25519_SIGNATURE_LENGTH;
    }
  }

  private static final class Ed448AuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public byte[] sign(
        SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes)
        throws UaException {

      return EccSignatureUtil.signEd448(resolveProviderProfile(profile), privateKey, signedBytes);
    }

    @Override
    public void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes)
        throws UaException {

      EccSignatureUtil.verifyEd448(
          resolveProviderProfile(profile), certificate.getPublicKey(), signatureBytes, signedBytes);
    }

    @Override
    public int signatureSize(Certificate certificate) {
      return EccSignatureUtil.ED448_SIGNATURE_LENGTH;
    }
  }

  @SuppressWarnings("unused")
  private static final class UnsupportedAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public byte[] sign(
        SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer... signedBytes)
        throws UaException {

      throw unsupported(profile, "authentication axis");
    }

    @Override
    public void verify(
        SecurityPolicyProfile profile,
        Certificate certificate,
        byte[] signatureBytes,
        ByteBuffer... signedBytes)
        throws UaException {

      throw unsupported(profile, "authentication axis");
    }

    @Override
    public int signatureSize(Certificate certificate) {
      return 0;
    }
  }

  private static final class NoKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      byte[] empty = new byte[0];

      return new ChannelSecurity.SecurityKeys(
          new ChannelSecurity.SecretKeys(empty, empty, empty),
          new ChannelSecurity.SecretKeys(empty, empty, empty));
    }
  }

  private static final class RsaNoncePShaKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      SecurityAlgorithm keyDerivation =
          requireNonNull(
              profile.keyDerivationAlgorithm(),
              "keyDerivationAlgorithm: " + profile.securityPolicy().getUri());

      int signatureKeySize = profile.symmetricSignatureKeySize();
      int encryptionKeySize = profile.symmetricEncryptionKeySize();
      byte[] clientNonceBytes = requireNonceBytes(clientNonce, "clientNonce");
      byte[] serverNonceBytes = requireNonceBytes(serverNonce, "serverNonce");

      byte[] clientSignatureKey =
          createPShaKey(keyDerivation, serverNonceBytes, clientNonceBytes, 0, signatureKeySize);

      byte[] clientEncryptionKey =
          createPShaKey(
              keyDerivation,
              serverNonceBytes,
              clientNonceBytes,
              signatureKeySize,
              encryptionKeySize);

      /*
       * The caller supplies the IV size because Sign-only channels do not write a CBC IV, while
       * SignAndEncrypt channels need a profile-sized IV. Keeping that choice at the channel layer
       * keeps key derivation aligned with the chunk framing that will consume the keys.
       */
      byte[] clientInitializationVector =
          createPShaKey(
              keyDerivation,
              serverNonceBytes,
              clientNonceBytes,
              signatureKeySize + encryptionKeySize,
              initializationVectorSize);

      byte[] serverSignatureKey =
          createPShaKey(keyDerivation, clientNonceBytes, serverNonceBytes, 0, signatureKeySize);

      byte[] serverEncryptionKey =
          createPShaKey(
              keyDerivation,
              clientNonceBytes,
              serverNonceBytes,
              signatureKeySize,
              encryptionKeySize);

      byte[] serverInitializationVector =
          createPShaKey(
              keyDerivation,
              clientNonceBytes,
              serverNonceBytes,
              signatureKeySize + encryptionKeySize,
              initializationVectorSize);

      return new ChannelSecurity.SecurityKeys(
          new ChannelSecurity.SecretKeys(
              clientSignatureKey, clientEncryptionKey, clientInitializationVector),
          new ChannelSecurity.SecretKeys(
              serverSignatureKey, serverEncryptionKey, serverInitializationVector));
    }
  }

  private static final class EcdhNistP256HkdfKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      return EccKeyAgreementUtil.generateNistP256KeyPair(resolveProviderProfile(profile));
    }

    @Override
    public ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      return EccPublicKeyCodec.encodeNistP256(publicKey);
    }

    @Override
    public PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      return EccPublicKeyCodec.decodeNistP256(wirePublicKey, resolveProviderProfile(profile));
    }

    @Override
    public byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      return EccKeyAgreementUtil.agreeNistP256(
          resolveProviderProfile(profile), privateKey, peerPublicKey);
    }

    @Override
    public EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      return EccKeyAgreementUtil.deriveAeadKeyMaterial(
          resolveProviderProfile(profile), profile, inputKeyMaterial, clientNonce, serverNonce);
    }

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ECC key agreement requires an ephemeral private key: "
              + profile.securityPolicy().getUri());
    }
  }

  private static final class EcdhNistP384HkdfKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      return EccKeyAgreementUtil.generateNistP384KeyPair(resolveProviderProfile(profile));
    }

    @Override
    public ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      return EccPublicKeyCodec.encodeNistP384(publicKey);
    }

    @Override
    public PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      return EccPublicKeyCodec.decodeNistP384(wirePublicKey, resolveProviderProfile(profile));
    }

    @Override
    public byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      return EccKeyAgreementUtil.agreeNistP384(
          resolveProviderProfile(profile), privateKey, peerPublicKey);
    }

    @Override
    public EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      return EccKeyAgreementUtil.deriveAeadKeyMaterial(
          resolveProviderProfile(profile), profile, inputKeyMaterial, clientNonce, serverNonce);
    }

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ECC key agreement requires an ephemeral private key: "
              + profile.securityPolicy().getUri());
    }
  }

  private static final class EcdhBrainpoolP256r1HkdfKeyAgreementStrategy
      implements KeyAgreementStrategy {

    @Override
    public KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      return EccKeyAgreementUtil.generateBrainpoolP256r1KeyPair(resolveProviderProfile(profile));
    }

    @Override
    public ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      return EccPublicKeyCodec.encodeBrainpoolP256r1(publicKey);
    }

    @Override
    public PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      return EccPublicKeyCodec.decodeBrainpoolP256r1(
          wirePublicKey, resolveProviderProfile(profile));
    }

    @Override
    public byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      return EccKeyAgreementUtil.agreeBrainpoolP256r1(
          resolveProviderProfile(profile), privateKey, peerPublicKey);
    }

    @Override
    public EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      return EccKeyAgreementUtil.deriveAeadKeyMaterial(
          resolveProviderProfile(profile), profile, inputKeyMaterial, clientNonce, serverNonce);
    }

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ECC key agreement requires an ephemeral private key: "
              + profile.securityPolicy().getUri());
    }
  }

  private static final class EcdhBrainpoolP384r1HkdfKeyAgreementStrategy
      implements KeyAgreementStrategy {

    @Override
    public KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      return EccKeyAgreementUtil.generateBrainpoolP384r1KeyPair(resolveProviderProfile(profile));
    }

    @Override
    public ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      return EccPublicKeyCodec.encodeBrainpoolP384r1(publicKey);
    }

    @Override
    public PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      return EccPublicKeyCodec.decodeBrainpoolP384r1(
          wirePublicKey, resolveProviderProfile(profile));
    }

    @Override
    public byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      return EccKeyAgreementUtil.agreeBrainpoolP384r1(
          resolveProviderProfile(profile), privateKey, peerPublicKey);
    }

    @Override
    public EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      return EccKeyAgreementUtil.deriveAeadKeyMaterial(
          resolveProviderProfile(profile), profile, inputKeyMaterial, clientNonce, serverNonce);
    }

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ECC key agreement requires an ephemeral private key: "
              + profile.securityPolicy().getUri());
    }
  }

  private static final class X25519HkdfKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      return EccKeyAgreementUtil.generateX25519KeyPair(resolveProviderProfile(profile));
    }

    @Override
    public ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      return EccPublicKeyCodec.encodeX25519(publicKey);
    }

    @Override
    public PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      return EccPublicKeyCodec.decodeX25519(wirePublicKey, resolveProviderProfile(profile));
    }

    @Override
    public byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      return EccKeyAgreementUtil.agreeX25519(
          resolveProviderProfile(profile), privateKey, peerPublicKey);
    }

    @Override
    public EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      return EccKeyAgreementUtil.deriveAeadKeyMaterial(
          resolveProviderProfile(profile), profile, inputKeyMaterial, clientNonce, serverNonce);
    }

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ECC key agreement requires an ephemeral private key: "
              + profile.securityPolicy().getUri());
    }
  }

  private static final class X448HkdfKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      return EccKeyAgreementUtil.generateX448KeyPair(resolveProviderProfile(profile));
    }

    @Override
    public ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      return EccPublicKeyCodec.encodeX448(publicKey);
    }

    @Override
    public PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      return EccPublicKeyCodec.decodeX448(wirePublicKey, resolveProviderProfile(profile));
    }

    @Override
    public byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      return EccKeyAgreementUtil.agreeX448(
          resolveProviderProfile(profile), privateKey, peerPublicKey);
    }

    @Override
    public EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      return EccKeyAgreementUtil.deriveAeadKeyMaterial(
          resolveProviderProfile(profile), profile, inputKeyMaterial, clientNonce, serverNonce);
    }

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ECC key agreement requires an ephemeral private key: "
              + profile.securityPolicy().getUri());
    }
  }

  private static final class Ffdh3072HkdfKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public KeyPair generateEphemeral(SecurityPolicyProfile profile) throws UaException {
      return FiniteFieldDhKeyAgreementUtil.generateFfdhe3072KeyPair(
          resolveProviderProfile(profile));
    }

    @Override
    public ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
        throws UaException {

      return FiniteFieldDhKeyAgreementUtil.encodeFfdhe3072(publicKey);
    }

    @Override
    public PublicKey decodePublicKey(SecurityPolicyProfile profile, ByteString wirePublicKey)
        throws UaException {

      return FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(
          wirePublicKey, resolveProviderProfile(profile));
    }

    @Override
    public byte[] agree(
        SecurityPolicyProfile profile, PrivateKey privateKey, PublicKey peerPublicKey)
        throws UaException {

      return FiniteFieldDhKeyAgreementUtil.agreeFfdhe3072(
          resolveProviderProfile(profile), privateKey, peerPublicKey);
    }

    @Override
    public EccKeyAgreementUtil.DerivedKeyMaterial deriveKeyMaterial(
        SecurityPolicyProfile profile,
        byte[] inputKeyMaterial,
        ByteString clientNonce,
        ByteString serverNonce)
        throws UaException {

      return EccKeyAgreementUtil.deriveAeadKeyMaterial(
          resolveProviderProfile(profile), profile, inputKeyMaterial, clientNonce, serverNonce);
    }

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "ephemeral key agreement requires a retained private key: "
              + profile.securityPolicy().getUri());
    }
  }

  @SuppressWarnings("unused")
  private static final class UnsupportedKeyAgreementStrategy implements KeyAgreementStrategy {

    @Override
    public ChannelSecurity.SecurityKeys deriveKeys(
        SecurityPolicyProfile profile,
        ByteString clientNonce,
        ByteString serverNonce,
        int initializationVectorSize) {

      throw new UaRuntimeException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "unsupported key-agreement axis: " + profile.securityPolicy().getUri());
    }
  }

  private static final class NoChunkProtectionStrategy implements ChunkProtectionStrategy {

    @Override
    public int cipherTextBlockSize(SecurityPolicyProfile profile) {
      return 1;
    }

    @Override
    public int plainTextBlockSize(SecurityPolicyProfile profile) {
      return 1;
    }

    @Override
    public int signatureSize(SecurityPolicyProfile profile) {
      return 0;
    }

    @Override
    public int paddingOverhead(int cipherTextBlockSize) {
      return 0;
    }

    @Override
    public void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {}

    @Override
    public int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {
      return 0;
    }

    @Override
    public void verifyPadding(
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer) {}

    @Override
    public byte[] sign(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        ByteBuffer chunkNioBuffer) {
      return new byte[0];
    }

    @Override
    public void verify(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys, ByteBuf chunkBuffer) {}

    @Override
    public Cipher getEncryptionCipher(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys) {
      throw new UaRuntimeException(StatusCodes.Bad_InternalError, "encryption is not enabled");
    }

    @Override
    public Cipher getDecryptionCipher(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys) {
      throw new UaRuntimeException(StatusCodes.Bad_InternalError, "encryption is not enabled");
    }
  }

  private static final class CbcHmacChunkProtectionStrategy implements ChunkProtectionStrategy {

    @Override
    public int cipherTextBlockSize(SecurityPolicyProfile profile) {
      return profile.symmetricBlockSize();
    }

    @Override
    public int plainTextBlockSize(SecurityPolicyProfile profile) {
      return profile.symmetricBlockSize();
    }

    @Override
    public int signatureSize(SecurityPolicyProfile profile) {
      return profile.symmetricSignatureSize();
    }

    @Override
    public int paddingOverhead(int cipherTextBlockSize) {
      return cipherTextBlockSize > 256 ? 2 : 1;
    }

    @Override
    public void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {
      SecureChannelStrategies.writeLegacyPadding(cipherTextBlockSize, paddingSize, buffer);
    }

    @Override
    public int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {
      return SecureChannelStrategies.getLegacyPaddingSize(
          cipherTextBlockSize, signatureSize, buffer);
    }

    @Override
    public void verifyPadding(
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer)
        throws UaException {

      SecureChannelStrategies.verifyLegacyPadding(
          signatureSize, paddingOverhead, paddingSize, buffer);
    }

    @Override
    public byte[] sign(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys, ByteBuffer chunkNioBuffer)
        throws UaException {

      SecurityAlgorithm signatureAlgorithm =
          requireNonNull(
              channel.getSecurityPolicyProfile().symmetricSignatureAlgorithm(),
              "symmetricSignatureAlgorithm: " + channel.getSecurityPolicy().getUri());

      byte[] signatureKey = channel.getEncryptionKeys(securityKeys).getSignatureKey();

      return SignatureUtil.hmac(signatureAlgorithm, signatureKey, chunkNioBuffer);
    }

    @Override
    public void verify(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys, ByteBuf chunkBuffer)
        throws UaException {

      SecurityAlgorithm signatureAlgorithm =
          requireNonNull(
              channel.getSecurityPolicyProfile().symmetricSignatureAlgorithm(),
              "symmetricSignatureAlgorithm: " + channel.getSecurityPolicy().getUri());

      byte[] secretKey = channel.getDecryptionKeys(securityKeys).getSignatureKey();
      int signatureSize = channel.getSymmetricSignatureSize();

      ByteBuffer chunkNioBuffer = chunkBuffer.nioBuffer(0, chunkBuffer.writerIndex());
      ((Buffer) chunkNioBuffer).position(0);
      ((Buffer) chunkNioBuffer).limit(chunkBuffer.writerIndex() - signatureSize);

      byte[] signature = SignatureUtil.hmac(signatureAlgorithm, secretKey, chunkNioBuffer);

      byte[] signatureBytes = new byte[signatureSize];
      ((Buffer) chunkNioBuffer).limit(chunkNioBuffer.position() + signatureSize);
      chunkNioBuffer.get(signatureBytes);

      if (!java.security.MessageDigest.isEqual(signature, signatureBytes)) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
      }
    }

    @Override
    public Cipher getEncryptionCipher(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys) throws UaException {

      SecurityAlgorithm encryptionAlgorithm =
          requireNonNull(
              channel.getSecurityPolicyProfile().symmetricEncryptionAlgorithm(),
              "symmetricEncryptionAlgorithm: " + channel.getSecurityPolicy().getUri());

      try {
        ChannelSecurity.SecretKeys secretKeys = channel.getEncryptionKeys(securityKeys);

        SecretKeySpec keySpec = new SecretKeySpec(secretKeys.getEncryptionKey(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(secretKeys.getInitializationVector());

        Cipher cipher = Cipher.getInstance(encryptionAlgorithm.getTransformation());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        assert (cipher.getBlockSize() == channel.getSymmetricBlockSize());

        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      }
    }

    @Override
    public Cipher getDecryptionCipher(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys) throws UaException {

      SecurityAlgorithm encryptionAlgorithm =
          requireNonNull(
              channel.getSecurityPolicyProfile().symmetricEncryptionAlgorithm(),
              "symmetricEncryptionAlgorithm: " + channel.getSecurityPolicy().getUri());

      try {
        ChannelSecurity.SecretKeys decryptionKeys = channel.getDecryptionKeys(securityKeys);

        SecretKeySpec keySpec = new SecretKeySpec(decryptionKeys.getEncryptionKey(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(decryptionKeys.getInitializationVector());

        Cipher cipher = Cipher.getInstance(encryptionAlgorithm.getTransformation());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_InternalError, e);
      }
    }
  }

  /*
   * AEAD profiles append the cipher's authentication tag where legacy profiles append an HMAC.
   * SignAndEncrypt encrypts the SequenceHeader/body and verifies the tag during decryption. Sign
   * leaves those bytes plaintext and uses the same tag construction as a signature footer over the
   * complete symmetric chunk.
   */
  private abstract static class AeadChunkProtectionStrategy implements ChunkProtectionStrategy {

    @Override
    public int cipherTextBlockSize(SecurityPolicyProfile profile) {
      return 1;
    }

    @Override
    public int plainTextBlockSize(SecurityPolicyProfile profile) {
      return 1;
    }

    @Override
    public int signatureSize(SecurityPolicyProfile profile) {
      return profile.symmetricSignatureSize();
    }

    @Override
    public int paddingOverhead(int cipherTextBlockSize) {
      return 0;
    }

    @Override
    public void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {}

    @Override
    public int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {
      return 0;
    }

    @Override
    public void verifyPadding(
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer) {}

    @Override
    public byte[] sign(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys, ByteBuffer chunkNioBuffer)
        throws UaException {

      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "AEAD chunk protection requires token-bound nonce context: "
              + channel.getSecurityPolicy().getUri());
    }

    @Override
    public byte[] sign(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuffer aad)
        throws UaException {

      try {
        ChannelSecurity.SecretKeys secretKeys = channel.getEncryptionKeys(securityKeys);
        byte[] nonce = secretKeys.reserveAeadNonce(tokenId, aeadSequenceNumber);

        Cipher cipher =
            initCipher(
                resolveProviderProfile(channel.getSecurityPolicyProfile()),
                Cipher.ENCRYPT_MODE,
                secretKeys.getEncryptionKey(),
                nonce,
                aad);

        return cipher.doFinal(new byte[0]);
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      }
    }

    @Override
    public void verify(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys, ByteBuf chunkBuffer)
        throws UaException {

      throw new UaException(
          StatusCodes.Bad_SecurityPolicyRejected,
          "AEAD chunk protection verifies tags during decryption: "
              + channel.getSecurityPolicy().getUri());
    }

    @Override
    public void verify(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuf chunkBuffer)
        throws UaException {

      try {
        ChannelSecurity.SecretKeys secretKeys = channel.getDecryptionKeys(securityKeys);
        byte[] nonce = secretKeys.createAeadNonce(tokenId, aeadSequenceNumber);

        int tagSize = signatureSize(channel.getSecurityPolicyProfile());
        int aadSize = chunkBuffer.writerIndex() - tagSize;

        if (aadSize < 0) {
          throw new UaException(
              StatusCodes.Bad_SecurityChecksFailed,
              "AEAD signature footer is missing: " + channel.getSecurityPolicy().getUri());
        }

        ByteBuffer aad = chunkBuffer.nioBuffer(0, aadSize);
        byte[] tag = new byte[tagSize];
        chunkBuffer.getBytes(aadSize, tag);

        Cipher cipher =
            initCipher(
                resolveProviderProfile(channel.getSecurityPolicyProfile()),
                Cipher.DECRYPT_MODE,
                secretKeys.getEncryptionKey(),
                nonce,
                aad);

        cipher.doFinal(tag);
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      }
    }

    @Override
    public Cipher getEncryptionCipher(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys) {

      throw new UaRuntimeException(
          StatusCodes.Bad_InternalError, "AEAD encryption requires a per-chunk nonce");
    }

    @Override
    public Cipher getDecryptionCipher(
        SecureChannel channel, ChannelSecurity.SecurityKeys securityKeys) {

      throw new UaRuntimeException(
          StatusCodes.Bad_InternalError, "AEAD decryption requires a per-chunk nonce");
    }

    @Override
    public boolean isAead() {
      return true;
    }

    @Override
    public Cipher getEncryptionCipher(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuffer aad)
        throws UaException {

      ChannelSecurity.SecretKeys secretKeys = channel.getEncryptionKeys(securityKeys);
      byte[] nonce = secretKeys.reserveAeadNonce(tokenId, aeadSequenceNumber);

      return initCipher(
          resolveProviderProfile(channel.getSecurityPolicyProfile()),
          Cipher.ENCRYPT_MODE,
          secretKeys.getEncryptionKey(),
          nonce,
          aad);
    }

    @Override
    public Cipher getDecryptionCipher(
        SecureChannel channel,
        ChannelSecurity.SecurityKeys securityKeys,
        long tokenId,
        long aeadSequenceNumber,
        ByteBuffer aad)
        throws UaException {

      ChannelSecurity.SecretKeys secretKeys = channel.getDecryptionKeys(securityKeys);
      byte[] nonce = secretKeys.createAeadNonce(tokenId, aeadSequenceNumber);

      return initCipher(
          resolveProviderProfile(channel.getSecurityPolicyProfile()),
          Cipher.DECRYPT_MODE,
          secretKeys.getEncryptionKey(),
          nonce,
          aad);
    }

    protected abstract Cipher initCipher(
        ProviderProfile providerProfile, int mode, byte[] key, byte[] nonce, ByteBuffer aad)
        throws UaException;
  }

  private static final class AesGcmChunkProtectionStrategy extends AeadChunkProtectionStrategy {

    @Override
    protected Cipher initCipher(
        ProviderProfile providerProfile, int mode, byte[] key, byte[] nonce, ByteBuffer aad)
        throws UaException {

      return AeadCipherUtil.initAesGcm(providerProfile, mode, key, nonce, aad);
    }
  }

  private static final class ChaCha20Poly1305ChunkProtectionStrategy
      extends AeadChunkProtectionStrategy {

    @Override
    protected Cipher initCipher(
        ProviderProfile providerProfile, int mode, byte[] key, byte[] nonce, ByteBuffer aad)
        throws UaException {

      return AeadCipherUtil.initChaCha20Poly1305(providerProfile, mode, key, nonce, aad);
    }
  }

  private static final class NoAsymmetricEncryptionStrategy
      implements AsymmetricEncryptionStrategy {

    @Override
    public int cipherTextBlockSize(Certificate certificate) {
      return 1;
    }

    @Override
    public int plainTextBlockSize(SecurityPolicyProfile profile, Certificate certificate) {
      return 1;
    }

    @Override
    public int paddingOverhead(int cipherTextBlockSize) {
      return 0;
    }

    @Override
    public void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {}

    @Override
    public int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {
      return 0;
    }

    @Override
    public void verifyPadding(
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer) {}

    @Override
    public Cipher getEncryptionCipher(
        SecurityPolicyProfile profile, Certificate remoteCertificate) {
      throw new UaRuntimeException(
          StatusCodes.Bad_InternalError, "asymmetric encryption is not enabled");
    }

    @Override
    public Cipher getDecryptionCipher(SecurityPolicyProfile profile, PrivateKey privateKey) {
      throw new UaRuntimeException(
          StatusCodes.Bad_InternalError, "asymmetric encryption is not enabled");
    }
  }

  private static final class RsaAsymmetricEncryptionStrategy
      implements AsymmetricEncryptionStrategy {

    @Override
    public int cipherTextBlockSize(Certificate certificate) {
      return rsaCipherTextBlockSize(certificate);
    }

    @Override
    public int plainTextBlockSize(SecurityPolicyProfile profile, Certificate certificate) {
      return rsaPlainTextBlockSize(certificate, profile.asymmetricEncryptionAlgorithm());
    }

    @Override
    public int paddingOverhead(int cipherTextBlockSize) {
      return cipherTextBlockSize > 256 ? 2 : 1;
    }

    @Override
    public void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {
      SecureChannelStrategies.writeLegacyPadding(cipherTextBlockSize, paddingSize, buffer);
    }

    @Override
    public int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {
      return SecureChannelStrategies.getLegacyPaddingSize(
          cipherTextBlockSize, signatureSize, buffer);
    }

    @Override
    public void verifyPadding(
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer)
        throws UaException {

      SecureChannelStrategies.verifyLegacyPadding(
          signatureSize, paddingOverhead, paddingSize, buffer);
    }

    @Override
    public Cipher getEncryptionCipher(SecurityPolicyProfile profile, Certificate remoteCertificate)
        throws UaException {

      try {
        Cipher cipher =
            Cipher.getInstance(profile.asymmetricEncryptionAlgorithm().getTransformation());
        cipher.init(Cipher.ENCRYPT_MODE, remoteCertificate.getPublicKey());
        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      }
    }

    @Override
    public Cipher getDecryptionCipher(SecurityPolicyProfile profile, PrivateKey privateKey)
        throws UaException {

      try {
        Cipher cipher =
            Cipher.getInstance(profile.asymmetricEncryptionAlgorithm().getTransformation());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_InternalError, e);
      }
    }
  }

  private static byte[] createPShaKey(
      SecurityAlgorithm keyDerivation, byte[] secret, byte[] seed, int offset, int length) {

    return switch (keyDerivation) {
      case PSha1 -> PShaUtil.createPSha1Key(secret, seed, offset, length);
      case PSha256 -> PShaUtil.createPSha256Key(secret, seed, offset, length);
      default ->
          throw new UaRuntimeException(
              StatusCodes.Bad_SecurityPolicyRejected,
              "unsupported legacy P_SHA key derivation algorithm: " + keyDerivation);
    };
  }

  static int getRsaKeyLength(@Nullable Certificate certificate) {
    if (certificate == null || !(certificate.getPublicKey() instanceof RSAPublicKey publicKey)) {
      return 0;
    }

    return publicKey.getModulus().bitLength();
  }

  static int rsaSignatureSize(@Nullable Certificate certificate) {
    return rsaCipherTextBlockSize(certificate);
  }

  static int rsaCipherTextBlockSize(@Nullable Certificate certificate) {
    return (getRsaKeyLength(certificate) + 7) / 8;
  }

  static int rsaPlainTextBlockSize(@Nullable Certificate certificate, SecurityAlgorithm algorithm) {
    return switch (algorithm) {
      case Rsa15 -> rsaCipherTextBlockSize(certificate) - 11;
      case RsaOaepSha1 -> rsaCipherTextBlockSize(certificate) - 42;
      case RsaOaepSha256 -> rsaCipherTextBlockSize(certificate) - 66;
      default -> 1;
    };
  }

  private static void writeLegacyPadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {
    buffer.writeByte(paddingSize);

    for (int i = 0; i < paddingSize; i++) {
      buffer.writeByte(paddingSize);
    }

    if (cipherTextBlockSize > 256) {
      int paddingLengthMsb = (paddingSize >> 8) & 0xFF;
      buffer.writeByte(paddingLengthMsb);
    }
  }

  private static int getLegacyPaddingSize(
      int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {

    int lastPaddingByteOffset = buffer.readableBytes() - signatureSize - 1;

    return cipherTextBlockSize <= 256
        ? buffer.getUnsignedByte(lastPaddingByteOffset)
        : buffer.getUnsignedShortLE(lastPaddingByteOffset - 1);
  }

  private static void verifyLegacyPadding(
      int signatureSize, int paddingOverhead, int paddingSize, ByteBuf buffer) throws UaException {

    int expectedPaddingSize = buffer.readableBytes() - signatureSize - paddingOverhead;
    if (paddingSize != expectedPaddingSize) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "bad padding size");
    }

    byte expectedPaddingByte = (byte) (paddingSize & 0xFF);
    for (int i = buffer.readerIndex(); i < buffer.readerIndex() + paddingSize + 1; i++) {
      if (buffer.getByte(i) != expectedPaddingByte) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "bad padding sequence");
      }
    }
  }

  private static UaException unsupported(SecurityPolicyProfile profile, String strategyName) {
    return new UaException(
        StatusCodes.Bad_SecurityPolicyRejected,
        "unsupported " + strategyName + ": " + profile.securityPolicy().getUri());
  }

  private static byte[] requireNonceBytes(ByteString nonce, String name) {
    if (nonce.isNull()) {
      throw new UaRuntimeException(StatusCodes.Bad_NonceInvalid, name + " is null");
    }

    return nonce.bytesOrEmpty();
  }

  private static ProviderProfile resolveProviderProfile(SecurityPolicyProfile profile)
      throws UaException {

    return PROVIDER_RESOLVER.resolve(profile);
  }
}
