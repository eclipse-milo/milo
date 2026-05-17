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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.HkdfUtil;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Encoder and decoder for OPC UA {@code EccEncryptedSecret} token-secret payloads.
 *
 * <p>The helper produces the opaque {@link ByteString} stored in username and issued-token identity
 * tokens when an enhanced ECC or RSA-DH user-token security policy protects the token secret. The
 * flow spans two services: CreateSession uses {@link EccUserTokenAdditionalHeader} to ask the
 * receiver for a signed session ephemeral public key, then ActivateSession sends the encrypted
 * password or issued-token secret in this structure.
 *
 * <p>The structure name and field names come from OPC UA's ECC token-secret definition, but current
 * RSA-DH profiles reuse the same wrapper. The selected policy profile decides whether the ephemeral
 * public keys are EC points, X25519/X448 keys, or ffdhe3072 public values; the rest of the layout
 * is the same. Decryption validates the receiver key, sender certificate or known sender
 * certificate, signature, authenticated payload, and session nonce before returning the plaintext
 * secret.
 */
@NullMarked
public final class EccEncryptedSecret {

  /**
   * Additional-header parameter containing the requested enhanced user-token security policy URI.
   */
  public static final String ECDH_POLICY_URI_PARAMETER = "ECDHPolicyUri";

  /** Additional-header parameter containing the signed {@link EphemeralKeyType}. */
  public static final String ECDH_KEY_PARAMETER = "ECDHKey";

  private static final UByte BINARY_ENCODING_MASK = Unsigned.ubyte(0x01);
  private static final byte[] SECRET_LABEL = "opcua-secret".getBytes(StandardCharsets.UTF_8);
  private static final int AEAD_PADDING_BLOCK_SIZE = 16;

  /*
   * OPC 10000-4, 7.40.2.3 defines SigningTime as the time the EncryptedSecret signature was
   * created, and 7.40.2.5 reuses that field for EccEncryptedSecret:
   *
   *   https://reference.opcfoundation.org/specs/OPC-10000-4/7.40.2.3
   *   https://reference.opcfoundation.org/specs/OPC-10000-4/7.40.2.5
   *
   * The core ActivateSession text does not define a freshness window for that timestamp. OPC
   * 10000-12, 9.5.4 does define one for RequestAccessToken when it uses the same UserIdentityToken
   * secret format: the range is server-configurable, with 5 minutes given as a suitable default.
   * This helper applies that default for the current token-secret validation path.
   *
   *   https://reference.opcfoundation.org/GDS/v104/docs/9.5.4
   */
  private static final long MAX_SIGNING_TIME_SKEW_MILLIS = Duration.ofMinutes(5).toMillis();

  private static final SecurityProviderResolver PROVIDER_RESOLVER =
      SecurityProviderResolver.create();

  private EccEncryptedSecret() {}

  /**
   * Generates a user-token ephemeral key pair for {@code profile}.
   *
   * @param profile the user-token security policy profile.
   * @return the generated ephemeral key pair.
   * @throws UaException if the profile is not supported for token secrets or key generation fails.
   */
  public static KeyPair generateEphemeralKeyPair(SecurityPolicyProfile profile) throws UaException {

    requireEnhancedSecretProfile(profile);

    ProviderProfile providerProfile = PROVIDER_RESOLVER.resolve(profile);

    return switch (profile.keyAgreementAxis()) {
      case ECDH_NIST_P256 -> EccKeyAgreementUtil.generateNistP256KeyPair(providerProfile);
      case ECDH_NIST_P384 -> EccKeyAgreementUtil.generateNistP384KeyPair(providerProfile);
      case ECDH_BRAINPOOL_P256R1 ->
          EccKeyAgreementUtil.generateBrainpoolP256r1KeyPair(providerProfile);
      case ECDH_BRAINPOOL_P384R1 ->
          EccKeyAgreementUtil.generateBrainpoolP384r1KeyPair(providerProfile);
      case X25519 -> EccKeyAgreementUtil.generateX25519KeyPair(providerProfile);
      case X448 -> EccKeyAgreementUtil.generateX448KeyPair(providerProfile);
      case FFDH_3072 -> FiniteFieldDhKeyAgreementUtil.generateFfdhe3072KeyPair(providerProfile);
      default -> throw unsupported(profile, "ephemeral key generation");
    };
  }

  /**
   * Encodes a user-token ephemeral public key for {@code profile}.
   *
   * @param profile the user-token security policy profile.
   * @param publicKey the public key to encode.
   * @return the wire-encoded public key.
   * @throws UaException if the public key is incompatible with {@code profile}.
   */
  public static ByteString encodeEphemeralPublicKey(
      SecurityPolicyProfile profile, PublicKey publicKey) throws UaException {

    requireEnhancedSecretProfile(profile);

    return encodePublicKey(profile, publicKey);
  }

  /**
   * Creates an {@link EphemeralKeyType} signed by the application instance certificate key.
   *
   * @param profile the user-token security policy profile.
   * @param signingKeyPair the application instance key pair used to sign the key.
   * @param ephemeralKeyPair the ephemeral key pair whose public key is returned.
   * @return a signed ephemeral key structure.
   * @throws UaException if signing fails or the keys are incompatible with {@code profile}.
   */
  public static EphemeralKeyType createEphemeralKey(
      SecurityPolicyProfile profile, KeyPair signingKeyPair, KeyPair ephemeralKeyPair)
      throws UaException {

    requireNonNull(signingKeyPair, "signingKeyPair");
    requireNonNull(ephemeralKeyPair, "ephemeralKeyPair");

    ByteString publicKey = encodeEphemeralPublicKey(profile, ephemeralKeyPair.getPublic());
    byte[] signature =
        sign(profile, signingKeyPair.getPrivate(), ByteBuffer.wrap(publicKey.bytesOrEmpty()));

    return new EphemeralKeyType(publicKey, ByteString.of(signature));
  }

  /**
   * Verifies a signed {@link EphemeralKeyType}.
   *
   * @param profile the user-token security policy profile.
   * @param signingCertificate the application instance certificate that signed the key.
   * @param ephemeralKey the signed ephemeral key structure.
   * @throws UaException if the signature is invalid or the key is incompatible with {@code
   *     profile}.
   */
  public static void verifyEphemeralKey(
      SecurityPolicyProfile profile,
      X509Certificate signingCertificate,
      EphemeralKeyType ephemeralKey)
      throws UaException {

    requireNonNull(signingCertificate, "signingCertificate");
    requireNonNull(ephemeralKey, "ephemeralKey");

    ByteString publicKey = ephemeralKey.getPublicKey();
    decodePublicKey(profile, PROVIDER_RESOLVER.resolve(profile), publicKey);

    verify(
        profile,
        signingCertificate.getPublicKey(),
        ephemeralKey.getSignature().bytesOrEmpty(),
        ByteBuffer.wrap(publicKey.bytesOrEmpty()));
  }

  /**
   * Encrypts a token secret using the receiver's session ephemeral public key.
   *
   * @param profile the user-token security policy profile.
   * @param senderApplicationKeyPair the sender application instance key pair.
   * @param senderCertificateChain the sender application instance certificate chain.
   * @param receiverPublicKey the receiver's encoded session ephemeral public key.
   * @param nonce the session nonce that must be echoed inside the encrypted payload.
   * @param secret the token secret bytes.
   * @param omitSenderCertificate whether to omit the sender certificate chain from the payload.
   * @return the encoded {@code EccEncryptedSecret} payload.
   * @throws UaException if encryption or signing fails.
   */
  public static ByteString encrypt(
      SecurityPolicyProfile profile,
      KeyPair senderApplicationKeyPair,
      X509Certificate[] senderCertificateChain,
      ByteString receiverPublicKey,
      ByteString nonce,
      ByteString secret,
      boolean omitSenderCertificate)
      throws UaException {

    requireEnhancedSecretProfile(profile);
    requireNonNull(senderApplicationKeyPair, "senderApplicationKeyPair");
    requireNonNull(senderCertificateChain, "senderCertificateChain");
    requireNonNull(receiverPublicKey, "receiverPublicKey");
    requireNonNull(nonce, "nonce");
    requireNonNull(secret, "secret");

    ProviderProfile providerProfile = PROVIDER_RESOLVER.resolve(profile);
    KeyPair senderEphemeralKeyPair = generateEphemeralKeyPair(profile);
    ByteString senderPublicKey = encodePublicKey(profile, senderEphemeralKeyPair.getPublic());

    PublicKey receiverEphemeralPublicKey =
        decodePublicKey(profile, providerProfile, receiverPublicKey);
    byte[] sharedSecret =
        agree(
            profile,
            providerProfile,
            senderEphemeralKeyPair.getPrivate(),
            receiverEphemeralPublicKey);

    try {
      SecretKeyMaterial keyMaterial =
          deriveSecretKeyMaterial(
              profile, providerProfile, sharedSecret, senderPublicKey, receiverPublicKey);

      return encrypt(
          profile,
          providerProfile,
          senderApplicationKeyPair.getPrivate(),
          senderApplicationPublicKey(senderApplicationKeyPair, senderCertificateChain),
          certificateChainBytes(senderCertificateChain, omitSenderCertificate),
          senderPublicKey,
          receiverPublicKey,
          nonce,
          secret,
          keyMaterial);
    } finally {
      Arrays.fill(sharedSecret, (byte) 0);
    }
  }

  /**
   * Decrypts and validates an {@code EccEncryptedSecret} payload.
   *
   * @param profile the expected user-token security policy profile.
   * @param receiverEphemeralKeyPair the receiver ephemeral key pair issued for the session.
   * @param receiverPublicKey the encoded receiver public key that was advertised to the sender.
   * @param senderCertificate the sender application instance certificate known from the
   *     SecureChannel, or {@code null} to require an embedded certificate.
   * @param expectedNonce the expected session nonce.
   * @param encodedSecret the encoded {@code EccEncryptedSecret} bytes.
   * @return the decrypted token secret.
   * @throws UaException if the payload is malformed, fails validation, or cannot be decrypted.
   */
  public static ByteString decrypt(
      SecurityPolicyProfile profile,
      KeyPair receiverEphemeralKeyPair,
      ByteString receiverPublicKey,
      @Nullable X509Certificate senderCertificate,
      ByteString expectedNonce,
      ByteString encodedSecret)
      throws UaException {

    requireEnhancedSecretProfile(profile);
    requireNonNull(receiverEphemeralKeyPair, "receiverEphemeralKeyPair");
    requireNonNull(receiverPublicKey, "receiverPublicKey");
    requireNonNull(expectedNonce, "expectedNonce");
    requireNonNull(encodedSecret, "encodedSecret");

    ParsedSecret parsed = parse(profile, senderCertificate, receiverPublicKey, encodedSecret);

    ProviderProfile providerProfile = PROVIDER_RESOLVER.resolve(profile);
    PublicKey senderEphemeralPublicKey =
        decodePublicKey(profile, providerProfile, parsed.senderPublicKey());
    byte[] sharedSecret =
        agree(
            profile,
            providerProfile,
            receiverEphemeralKeyPair.getPrivate(),
            senderEphemeralPublicKey);

    try {
      SecretKeyMaterial keyMaterial =
          deriveSecretKeyMaterial(
              profile,
              providerProfile,
              sharedSecret,
              parsed.senderPublicKey(),
              parsed.receiverPublicKey());

      byte[] plainText =
          decryptPayload(
              profile,
              providerProfile,
              keyMaterial,
              parsed.headerBytes(),
              parsed.encryptedPayload());

      return parsePlaintextPayload(plainText, expectedNonce);
    } finally {
      Arrays.fill(sharedSecret, (byte) 0);
    }
  }

  private static ByteString encrypt(
      SecurityPolicyProfile profile,
      ProviderProfile providerProfile,
      PrivateKey senderApplicationPrivateKey,
      PublicKey senderApplicationPublicKey,
      ByteString senderCertificateBytes,
      ByteString senderPublicKey,
      ByteString receiverPublicKey,
      ByteString nonce,
      ByteString secret,
      SecretKeyMaterial keyMaterial)
      throws UaException {

    /*
     * Part 4 models EncryptedSecret as an ExtensionObject-like opaque ByteString:
     *
     *   TypeId, EncodingMask, Length, common header, KeyData, encrypted payload, signature.
     *
     * EccEncryptedSecret KeyData is not encrypted. It holds two UA Binary ByteStrings: the sender's
     * fresh ephemeral public key and the receiver's advertised session ephemeral public key.
     */
    ByteBuf buffer = Unpooled.buffer();
    OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE);
    encoder.setBuffer(buffer);

    encoder.encodeNodeId(NodeIds.EccEncryptedSecret);
    encoder.encodeByte(null, BINARY_ENCODING_MASK);

    int lengthIndex = buffer.writerIndex();
    encoder.encodeUInt32(null, Unsigned.uint(0));

    encoder.encodeString(profile.securityPolicy().getUri());
    encoder.encodeByteString(senderCertificateBytes);
    encoder.encodeDateTime(DateTime.now());

    int keyDataLengthIndex = buffer.writerIndex();
    encoder.encodeUInt16(null, Unsigned.ushort(0));

    int keyDataStart = buffer.writerIndex();
    encoder.encodeByteString(senderPublicKey);
    encoder.encodeByteString(receiverPublicKey);
    int keyDataLength = buffer.writerIndex() - keyDataStart;
    buffer.setShortLE(keyDataLengthIndex, keyDataLength);

    int payloadStart = buffer.writerIndex();
    byte[] plainText = createPlaintextPayload(nonce, secret);

    /*
     * Length is part of the AEAD additional authenticated data, and it includes the bytes after the
     * Length field through the final signature. Set it before taking the header snapshot used as
     * AAD.
     */
    int encryptedPayloadLength = plainText.length + profile.symmetricSignatureSize();
    int length =
        payloadStart
            - (lengthIndex + Integer.BYTES)
            + encryptedPayloadLength
            + signatureSize(profile, senderApplicationPublicKey);
    buffer.setIntLE(lengthIndex, length);

    byte[] headerBytes = copyBytes(buffer, payloadStart);

    byte[] encryptedPayload =
        encryptPayload(profile, providerProfile, keyMaterial, headerBytes, plainText);
    buffer.writeBytes(encryptedPayload);

    // Part 4 requires the signature after encryption; it signs all bytes before Signature.
    byte[] bytesToSign = copyBytes(buffer, buffer.writerIndex());
    byte[] signature = sign(profile, senderApplicationPrivateKey, ByteBuffer.wrap(bytesToSign));
    buffer.writeBytes(signature);

    return ByteString.of(copyBytes(buffer, buffer.writerIndex()));
  }

  private static ParsedSecret parse(
      SecurityPolicyProfile expectedProfile,
      @Nullable X509Certificate knownSenderCertificate,
      ByteString expectedReceiverPublicKey,
      ByteString encodedSecret)
      throws UaException {

    byte[] encodedBytes = encodedSecret.bytesOrEmpty();
    ByteBuf buffer = Unpooled.wrappedBuffer(encodedBytes);
    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    decoder.setBuffer(buffer);

    try {
      /*
       * The TypeId and EncodingMask are decoded before any policy-specific logic so a caller can
       * distinguish unsupported/malformed EccEncryptedSecret data from legacy token-secret bytes.
       */
      NodeId typeId = decoder.decodeNodeId();
      if (!NodeIds.EccEncryptedSecret.equals(typeId)) {
        throw new UaException(StatusCodes.Bad_DataTypeIdUnknown, "expected EccEncryptedSecret");
      }

      UByte encodingMask = decoder.decodeByte(null);
      if (!BINARY_ENCODING_MASK.equals(encodingMask)) {
        throw new UaException(StatusCodes.Bad_DataEncodingUnsupported);
      }

      long length = decoder.decodeUInt32().longValue();
      int bodyStart = buffer.readerIndex();
      long bodyEnd = bodyStart + length;
      if (length > Integer.MAX_VALUE || bodyEnd != encodedBytes.length || bodyEnd < bodyStart) {
        throw new UaException(StatusCodes.Bad_DecodingError, "invalid EccEncryptedSecret length");
      }
      int bodyEndIndex = (int) bodyEnd;

      // SecurityPolicyUri selects the key derivation, AEAD cipher, and application signature axis.
      String securityPolicyUri = decoder.decodeString();
      if (!expectedProfile.securityPolicy().getUri().equals(securityPolicyUri)) {
        throw new UaException(StatusCodes.Bad_SecurityPolicyRejected, securityPolicyUri);
      }

      ByteString senderCertificateBytes = decoder.decodeByteString();
      X509Certificate senderCertificate =
          resolveSenderCertificate(senderCertificateBytes, knownSenderCertificate);

      DateTime signingTime = decoder.decodeDateTime();
      if (signingTime.isNull()) {
        throw new UaException(StatusCodes.Bad_InvalidTimestamp);
      }

      // Bound replay exposure if a receiver ephemeral key and session nonce are reused.
      validateSigningTime(signingTime);

      int keyDataLength = decoder.decodeUInt16().intValue();
      int keyDataStart = buffer.readerIndex();
      ByteString senderPublicKey = decoder.decodeByteString();
      ByteString receiverPublicKey = decoder.decodeByteString();
      int actualKeyDataLength = buffer.readerIndex() - keyDataStart;

      if (keyDataLength != actualKeyDataLength) {
        throw new UaException(StatusCodes.Bad_DecodingError, "unexpected key data length");
      }

      if (!expectedReceiverPublicKey.equals(receiverPublicKey)) {
        throw new UaException(StatusCodes.Bad_NonceInvalid, "unexpected receiver public key");
      }

      int encryptedPayloadStart = buffer.readerIndex();
      int signatureSize = signatureSize(expectedProfile, senderCertificate.getPublicKey());
      int signatureStart = bodyEndIndex - signatureSize;

      if (signatureStart <= encryptedPayloadStart) {
        throw new UaException(StatusCodes.Bad_DecodingError, "missing encrypted payload");
      }

      /*
       * KeyData is unencrypted, so Part 4/6 require validating the signing certificate and
       * signature before decrypting the payload.
       */
      byte[] signature = Arrays.copyOfRange(encodedBytes, signatureStart, bodyEndIndex);
      verify(
          expectedProfile,
          senderCertificate.getPublicKey(),
          signature,
          ByteBuffer.wrap(encodedBytes, 0, signatureStart));

      return new ParsedSecret(
          senderPublicKey,
          receiverPublicKey,
          copyBytes(encodedBytes, 0, encryptedPayloadStart),
          copyBytes(encodedBytes, encryptedPayloadStart, signatureStart),
          senderCertificate,
          signingTime);
    } catch (IndexOutOfBoundsException | UaSerializationException e) {
      throw new UaException(StatusCodes.Bad_DecodingError, "malformed EccEncryptedSecret", e);
    }
  }

  /**
   * Builds the payload bytes that will be encrypted after the unencrypted KeyData.
   *
   * <p>The nonce and secret are UA Binary ByteStrings. Padding bytes follow the Secret, then the
   * UInt16 PayloadPaddingSize. The AEAD tag is not written here; the cipher appends it while
   * encrypting this plaintext payload.
   */
  private static byte[] createPlaintextPayload(ByteString nonce, ByteString secret) {

    ByteBuf payload = Unpooled.buffer();
    OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE);
    encoder.setBuffer(payload);

    encoder.encodeByteString(nonce);
    encoder.encodeByteString(secret);

    int paddingSize = getPaddingSize(secret.length(), payload.writerIndex());
    for (int i = 0; i < paddingSize; i++) {
      payload.writeByte(paddingSize);
    }

    payload.writeByte(paddingSize & 0xFF);
    payload.writeByte(0);

    return copyBytes(payload, payload.writerIndex());
  }

  /**
   * Validates and extracts the decrypted payload.
   *
   * <p>The receiver's session nonce check happens before returning the secret so callers do not
   * accidentally accept a validly encrypted token secret from another session activation.
   */
  private static ByteString parsePlaintextPayload(byte[] plainText, ByteString expectedNonce)
      throws UaException {

    ByteBuf buffer = Unpooled.wrappedBuffer(plainText);
    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    decoder.setBuffer(buffer);

    try {
      ByteString actualNonce = decoder.decodeByteString();
      if (!expectedNonce.equals(actualNonce)) {
        throw new UaException(StatusCodes.Bad_NonceInvalid);
      }

      ByteString secret = decoder.decodeByteString();

      if (!buffer.isReadable()) {
        throw new UaException(StatusCodes.Bad_DecodingError, "missing payload padding size");
      }

      int paddingSize = buffer.readUnsignedByte();

      if (paddingSize > buffer.readableBytes() - 1) {
        throw new UaException(StatusCodes.Bad_DecodingError, "invalid payload padding size");
      }

      for (int i = 0; i < paddingSize; i++) {
        int paddingByte = buffer.readUnsignedByte();
        if (paddingByte != paddingSize) {
          throw new UaException(StatusCodes.Bad_DecodingError, "invalid payload padding");
        }
      }

      int paddingSizeHighByte = buffer.readUnsignedByte();
      if (paddingSizeHighByte != 0 || buffer.isReadable()) {
        throw new UaException(StatusCodes.Bad_DecodingError, "invalid payload padding size");
      }

      return secret;
    } catch (RuntimeException e) {
      throw new UaException(StatusCodes.Bad_DecodingError, e);
    }
  }

  private static void validateSigningTime(DateTime signingTime) throws UaException {
    try {
      long deltaMillis =
          Math.abs(Math.subtractExact(DateTime.now().getJavaTime(), signingTime.getJavaTime()));

      if (deltaMillis > MAX_SIGNING_TIME_SKEW_MILLIS) {
        throw new UaException(
            StatusCodes.Bad_InvalidTimestamp, "stale EccEncryptedSecret signing time");
      }
    } catch (ArithmeticException e) {
      throw new UaException(StatusCodes.Bad_InvalidTimestamp, e);
    }
  }

  /** Encrypts the payload and lets the AEAD cipher append the authentication tag. */
  private static byte[] encryptPayload(
      SecurityPolicyProfile profile,
      ProviderProfile providerProfile,
      SecretKeyMaterial keyMaterial,
      byte[] headerBytes,
      byte[] plainText)
      throws UaException {

    Cipher cipher =
        initAeadCipher(
            profile,
            providerProfile,
            Cipher.ENCRYPT_MODE,
            keyMaterial.encryptionKey(),
            keyMaterial.initializationVector(),
            headerBytes);

    try {
      return cipher.doFinal(plainText);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  /** Decrypts the payload; AEAD tag verification is performed by {@link Cipher#doFinal(byte[])}. */
  private static byte[] decryptPayload(
      SecurityPolicyProfile profile,
      ProviderProfile providerProfile,
      SecretKeyMaterial keyMaterial,
      byte[] headerBytes,
      byte[] encryptedPayload)
      throws UaException {

    Cipher cipher =
        initAeadCipher(
            profile,
            providerProfile,
            Cipher.DECRYPT_MODE,
            keyMaterial.encryptionKey(),
            keyMaterial.initializationVector(),
            headerBytes);

    try {
      return cipher.doFinal(encryptedPayload);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  /**
   * Creates the AEAD cipher with EncryptedSecret header bytes as additional authenticated data.
   *
   * <p>Part 6 requires all headers in the EncryptedSecret to be AEAD additional data. In this
   * helper, {@code headerBytes} covers TypeId through the unencrypted KeyData, including the final
   * Length value.
   */
  private static Cipher initAeadCipher(
      SecurityPolicyProfile profile,
      ProviderProfile providerProfile,
      int mode,
      byte[] encryptionKey,
      byte[] initializationVector,
      byte[] headerBytes)
      throws UaException {

    ByteBuffer aad = ByteBuffer.wrap(headerBytes);

    return switch (profile.chunkProtectionAxis()) {
      case AES_GCM ->
          AeadCipherUtil.initAesGcm(
              providerProfile, mode, encryptionKey, initializationVector, aad);
      case CHACHA20_POLY1305 ->
          AeadCipherUtil.initChaCha20Poly1305(
              providerProfile, mode, encryptionKey, initializationVector, aad);
      default -> throw unsupported(profile, "AEAD payload encryption");
    };
  }

  /**
   * Derives the encrypting key and initialization vector from the profile-selected shared secret.
   *
   * <p>Part 6 defines both HKDF salt and info for token secrets as {@code UInt16LE(totalLength) ||
   * UTF8("opcua-secret") || SenderPublicKey || ReceiverPublicKey}. The derived bytes are then split
   * into EncryptingKey and InitializationVector.
   */
  private static SecretKeyMaterial deriveSecretKeyMaterial(
      SecurityPolicyProfile profile,
      ProviderProfile providerProfile,
      byte[] sharedSecret,
      ByteString senderPublicKey,
      ByteString receiverPublicKey)
      throws UaException {

    int totalLength =
        profile.symmetricEncryptionKeySize()
            + EccKeyAgreementUtil.AEAD_INITIALIZATION_VECTOR_LENGTH;

    byte[] salt = secretSalt(totalLength, senderPublicKey, receiverPublicKey);
    byte[] keyMaterial = hkdf(providerProfile, profile, sharedSecret, salt, salt, totalLength);

    return new SecretKeyMaterial(
        Arrays.copyOfRange(keyMaterial, 0, profile.symmetricEncryptionKeySize()),
        Arrays.copyOfRange(keyMaterial, profile.symmetricEncryptionKeySize(), totalLength));
  }

  private static byte[] secretSalt(
      int totalLength, ByteString senderPublicKey, ByteString receiverPublicKey) {

    byte[] senderBytes = senderPublicKey.bytesOrEmpty();
    byte[] receiverBytes = receiverPublicKey.bytesOrEmpty();
    byte[] salt = new byte[2 + SECRET_LABEL.length + senderBytes.length + receiverBytes.length];

    salt[0] = (byte) (totalLength & 0xFF);
    salt[1] = (byte) ((totalLength >>> 8) & 0xFF);
    System.arraycopy(SECRET_LABEL, 0, salt, 2, SECRET_LABEL.length);
    System.arraycopy(senderBytes, 0, salt, 2 + SECRET_LABEL.length, senderBytes.length);
    System.arraycopy(
        receiverBytes, 0, salt, 2 + SECRET_LABEL.length + senderBytes.length, receiverBytes.length);

    return salt;
  }

  /** Runs profile-selected HKDF through the provider selected for the user-token profile. */
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
        default -> throw unsupported(profile, "HKDF key derivation");
      };
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  private static String hkdfHmacTransformation(SecurityPolicyProfile profile) throws UaException {
    return switch (profile.keyAgreementAxis()) {
      case ECDH_NIST_P384, ECDH_BRAINPOOL_P384R1, X448 -> "HmacSHA384";
      case ECDH_NIST_P256, ECDH_BRAINPOOL_P256R1, X25519, FFDH_3072 -> "HmacSHA256";
      default -> throw unsupported(profile, "HKDF key derivation");
    };
  }

  /** Performs profile-specific agreement for the token-secret ephemeral keys. */
  private static byte[] agree(
      SecurityPolicyProfile profile,
      ProviderProfile providerProfile,
      PrivateKey privateKey,
      PublicKey peerPublicKey)
      throws UaException {

    return switch (profile.keyAgreementAxis()) {
      case ECDH_NIST_P256 ->
          EccKeyAgreementUtil.agreeNistP256(providerProfile, privateKey, peerPublicKey);
      case ECDH_NIST_P384 ->
          EccKeyAgreementUtil.agreeNistP384(providerProfile, privateKey, peerPublicKey);
      case ECDH_BRAINPOOL_P256R1 ->
          EccKeyAgreementUtil.agreeBrainpoolP256r1(providerProfile, privateKey, peerPublicKey);
      case ECDH_BRAINPOOL_P384R1 ->
          EccKeyAgreementUtil.agreeBrainpoolP384r1(providerProfile, privateKey, peerPublicKey);
      case X25519 -> EccKeyAgreementUtil.agreeX25519(providerProfile, privateKey, peerPublicKey);
      case X448 -> EccKeyAgreementUtil.agreeX448(providerProfile, privateKey, peerPublicKey);
      case FFDH_3072 ->
          FiniteFieldDhKeyAgreementUtil.agreeFfdhe3072(providerProfile, privateKey, peerPublicKey);
      default -> throw unsupported(profile, "key agreement");
    };
  }

  /** Encodes the public-key bytes used in the KeyData SenderPublicKey/ReceiverPublicKey fields. */
  private static ByteString encodePublicKey(SecurityPolicyProfile profile, PublicKey publicKey)
      throws UaException {

    return switch (profile.keyAgreementAxis()) {
      case ECDH_NIST_P256 -> EccPublicKeyCodec.encodeNistP256(publicKey);
      case ECDH_NIST_P384 -> EccPublicKeyCodec.encodeNistP384(publicKey);
      case ECDH_BRAINPOOL_P256R1 -> EccPublicKeyCodec.encodeBrainpoolP256r1(publicKey);
      case ECDH_BRAINPOOL_P384R1 -> EccPublicKeyCodec.encodeBrainpoolP384r1(publicKey);
      case X25519 -> EccPublicKeyCodec.encodeX25519(publicKey);
      case X448 -> EccPublicKeyCodec.encodeX448(publicKey);
      case FFDH_3072 -> FiniteFieldDhKeyAgreementUtil.encodeFfdhe3072(publicKey);
      default -> throw unsupported(profile, "public-key encoding");
    };
  }

  /** Decodes the public-key bytes used in the KeyData SenderPublicKey/ReceiverPublicKey fields. */
  private static PublicKey decodePublicKey(
      SecurityPolicyProfile profile, ProviderProfile providerProfile, ByteString publicKey)
      throws UaException {

    return switch (profile.keyAgreementAxis()) {
      case ECDH_NIST_P256 -> EccPublicKeyCodec.decodeNistP256(publicKey, providerProfile);
      case ECDH_NIST_P384 -> EccPublicKeyCodec.decodeNistP384(publicKey, providerProfile);
      case ECDH_BRAINPOOL_P256R1 ->
          EccPublicKeyCodec.decodeBrainpoolP256r1(publicKey, providerProfile);
      case ECDH_BRAINPOOL_P384R1 ->
          EccPublicKeyCodec.decodeBrainpoolP384r1(publicKey, providerProfile);
      case X25519 -> EccPublicKeyCodec.decodeX25519(publicKey, providerProfile);
      case X448 -> EccPublicKeyCodec.decodeX448(publicKey, providerProfile);
      case FFDH_3072 -> FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(publicKey, providerProfile);
      default -> throw unsupported(profile, "public-key decoding");
    };
  }

  /** Signs EccEncryptedSecret bytes with the profile's application-certificate signature axis. */
  private static byte[] sign(SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer data)
      throws UaException {
    try {
      if (profile.authAxis() == SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA256) {
        return SignatureUtil.sign(SecurityAlgorithm.RsaSha256, privateKey, data);
      }

      return EccSignatureUtil.sign(profile, privateKey, data);
    } catch (UaException e) {
      if (e.getStatusCode().getValue() == StatusCodes.Bad_SecurityPolicyRejected) {
        throw unsupported(profile, "signature algorithm");
      }

      throw e;
    }
  }

  /** Verifies an EccEncryptedSecret or EphemeralKey signature with the profile's signature axis. */
  private static void verify(
      SecurityPolicyProfile profile, PublicKey publicKey, byte[] signature, ByteBuffer data)
      throws UaException {
    try {
      if (profile.authAxis() == SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA256) {
        Signature verifier = Signature.getInstance(SecurityAlgorithm.RsaSha256.getTransformation());
        verifier.initVerify(publicKey);
        verifier.update(data);

        if (!verifier.verify(signature)) {
          throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
        }

        return;
      }

      EccSignatureUtil.verify(profile, publicKey, signature, data);
    } catch (UaException e) {
      if (e.getStatusCode().getValue() == StatusCodes.Bad_SecurityPolicyRejected) {
        throw unsupported(profile, "signature algorithm");
      }

      throw e;
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }

  private static int signatureSize(SecurityPolicyProfile profile, PublicKey publicKey)
      throws UaException {
    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256, ECDSA_BRAINPOOL_P256R1_SHA256 ->
          EccSignatureUtil.ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH;
      case ECDSA_NIST_P384_SHA384, ECDSA_BRAINPOOL_P384R1_SHA384 ->
          EccSignatureUtil.ECDSA_P384_SHA384_P1363_SIGNATURE_LENGTH;
      case ED25519 -> EccSignatureUtil.ED25519_SIGNATURE_LENGTH;
      case ED448 -> EccSignatureUtil.ED448_SIGNATURE_LENGTH;
      case RSA_PKCS1_SHA256 -> {
        if (publicKey instanceof RSAPublicKey rsaPublicKey) {
          yield (rsaPublicKey.getModulus().bitLength() + 7) / 8;
        }

        throw new UaException(StatusCodes.Bad_CertificateInvalid, "expected RSA public key");
      }
      default -> throw unsupported(profile, "signature size");
    };
  }

  /**
   * Resolves the signing certificate embedded in the payload or supplied by the SecureChannel.
   *
   * <p>Part 4 allows the Certificate field to be null/empty when the signing certificate is already
   * known to the receiver, such as a client application certificate on the active SecureChannel.
   * When both are present they must describe the same leaf certificate, otherwise the payload could
   * be signed by one identity while validated against another.
   */
  private static X509Certificate resolveSenderCertificate(
      ByteString senderCertificateBytes, @Nullable X509Certificate knownSenderCertificate)
      throws UaException {

    if (senderCertificateBytes.isNullOrEmpty()) {
      if (knownSenderCertificate == null) {
        throw new UaException(StatusCodes.Bad_CertificateInvalid, "sender certificate omitted");
      }

      return knownSenderCertificate;
    }

    X509Certificate embeddedSenderCertificate =
        CertificateUtil.decodeCertificate(senderCertificateBytes.bytesOrEmpty());

    if (knownSenderCertificate != null) {
      try {
        if (!Arrays.equals(
            embeddedSenderCertificate.getEncoded(), knownSenderCertificate.getEncoded())) {
          throw new UaException(StatusCodes.Bad_CertificateInvalid, "sender certificate mismatch");
        }
      } catch (CertificateEncodingException e) {
        throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
      }
    }

    return embeddedSenderCertificate;
  }

  /** Encodes the sender certificate chain for the EccEncryptedSecret Certificate field. */
  private static ByteString certificateChainBytes(
      X509Certificate[] certificateChain, boolean omitSenderCertificate) throws UaException {

    if (omitSenderCertificate) {
      return ByteString.NULL_VALUE;
    }

    byte[] bytes = new byte[0];

    for (X509Certificate certificate : certificateChain) {
      try {
        bytes = com.google.common.primitives.Bytes.concat(bytes, certificate.getEncoded());
      } catch (CertificateEncodingException e) {
        throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
      }
    }

    return ByteString.of(bytes);
  }

  private static PublicKey senderApplicationPublicKey(
      KeyPair senderApplicationKeyPair, X509Certificate[] senderCertificateChain)
      throws UaException {

    if (senderCertificateChain.length > 0) {
      return senderCertificateChain[0].getPublicKey();
    }

    PublicKey publicKey = senderApplicationKeyPair.getPublic();

    if (publicKey == null) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError, "sender application public key is unavailable");
    }

    return publicKey;
  }

  /**
   * Calculates Part 6 payload padding.
   *
   * <p>For authenticated encryption the block size is fixed at 16 bytes. {@code
   * dataLengthWithoutPaddingSize} is the UA Binary encoded nonce and secret length; the formula
   * adds the two-byte PayloadPaddingSize field before rounding to the block size.
   */
  private static int getPaddingSize(int secretLength, int dataLengthWithoutPaddingSize) {
    int dataLength = dataLengthWithoutPaddingSize + Short.BYTES;
    int paddingSize =
        dataLength % AEAD_PADDING_BLOCK_SIZE == 0
            ? 0
            : AEAD_PADDING_BLOCK_SIZE - (dataLength % AEAD_PADDING_BLOCK_SIZE);

    if (paddingSize + secretLength < AEAD_PADDING_BLOCK_SIZE) {
      paddingSize += AEAD_PADDING_BLOCK_SIZE;
    }

    return paddingSize;
  }

  private static void requireEnhancedSecretProfile(SecurityPolicyProfile profile)
      throws UaException {
    requireNonNull(profile, "profile");

    if ((profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_NIST_P256
            && profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_NIST_P384
            && profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_BRAINPOOL_P256R1
            && profile.keyAgreementAxis() != KeyAgreementAxis.ECDH_BRAINPOOL_P384R1
            && profile.keyAgreementAxis() != KeyAgreementAxis.X25519
            && profile.keyAgreementAxis() != KeyAgreementAxis.X448
            && profile.keyAgreementAxis() != KeyAgreementAxis.FFDH_3072)
        || (profile.chunkProtectionAxis() != ChunkProtectionAxis.AES_GCM
            && profile.chunkProtectionAxis() != ChunkProtectionAxis.CHACHA20_POLY1305)) {

      throw unsupported(profile, "enhanced token-secret profile");
    }
  }

  private static UaException unsupported(SecurityPolicyProfile profile, String operation) {
    return new UaException(
        StatusCodes.Bad_SecurityPolicyRejected,
        "unsupported " + operation + ": " + profile.securityPolicy().getUri());
  }

  private static byte[] copyBytes(ByteBuf buffer, int end) {
    byte[] bytes = new byte[end];
    buffer.getBytes(0, bytes);
    return bytes;
  }

  private static byte[] copyBytes(byte[] bytes, int start, int end) {
    return Arrays.copyOfRange(bytes, start, end);
  }

  private record SecretKeyMaterial(byte[] encryptionKey, byte[] initializationVector) {}

  private record ParsedSecret(
      ByteString senderPublicKey,
      ByteString receiverPublicKey,
      byte[] headerBytes,
      byte[] encryptedPayload,
      X509Certificate senderCertificate,
      DateTime signingTime) {}
}
