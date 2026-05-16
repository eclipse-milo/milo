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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis;
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
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@NullMarked
class EccEncryptedSecretTest {

  private static final ByteString NONCE =
      ByteString.of(new byte[] {0x10, 0x11, 0x12, 0x13, 0x14, 0x15});

  private static final ByteString SECRET =
      ByteString.of("correct horse battery staple".getBytes(StandardCharsets.UTF_8));
  private static final byte[] SECRET_LABEL = "opcua-secret".getBytes(StandardCharsets.UTF_8);

  // Sender certificates may be embedded in the opaque secret when no channel certificate is known.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void encryptsAndDecryptsEmbeddedSenderCertificate(SecurityPolicyProfile profile)
      throws Exception {

    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());

    ByteString encryptedSecret =
        EccEncryptedSecret.encrypt(
            profile,
            sender.keyPair(),
            new X509Certificate[] {sender.certificate()},
            receiverPublicKey,
            NONCE,
            SECRET,
            false);

    ByteString decryptedSecret =
        EccEncryptedSecret.decrypt(
            profile, receiverEphemeralKeyPair, receiverPublicKey, null, NONCE, encryptedSecret);

    assertEquals(SECRET, decryptedSecret);
  }

  // SecureChannel already authenticates the sender certificate, so the secret can omit it.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void decryptsWithKnownSenderCertificateWhenCertificateIsOmitted(SecurityPolicyProfile profile)
      throws Exception {

    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());

    ByteString encryptedSecret =
        EccEncryptedSecret.encrypt(
            profile,
            sender.keyPair(),
            new X509Certificate[] {sender.certificate()},
            receiverPublicKey,
            NONCE,
            SECRET,
            true);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccEncryptedSecret.decrypt(
                    profile,
                    receiverEphemeralKeyPair,
                    receiverPublicKey,
                    null,
                    NONCE,
                    encryptedSecret));

    assertEquals(StatusCodes.Bad_CertificateInvalid, exception.getStatusCode().getValue());
    assertEquals(
        SECRET,
        EccEncryptedSecret.decrypt(
            profile,
            receiverEphemeralKeyPair,
            receiverPublicKey,
            sender.certificate(),
            NONCE,
            encryptedSecret));
  }

  // A known sender certificate must match any embedded certificate before the signature is trusted.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsMismatchedKnownSenderCertificate(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    ApplicationIdentity otherSender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccEncryptedSecret.decrypt(
                    profile,
                    receiverEphemeralKeyPair,
                    receiverPublicKey,
                    otherSender.certificate(),
                    NONCE,
                    encryptedSecret));

    assertEquals(StatusCodes.Bad_CertificateInvalid, exception.getStatusCode().getValue());
  }

  // The token secret is an opaque ByteString, so layout regressions are wire-compatibility bugs.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void encodesExpectedOpaquePayloadLayout(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());

    ByteString encryptedSecret =
        EccEncryptedSecret.encrypt(
            profile,
            sender.keyPair(),
            new X509Certificate[] {sender.certificate()},
            receiverPublicKey,
            NONCE,
            SECRET,
            false);

    byte[] bytes = encryptedSecret.bytesOrEmpty();
    ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    decoder.setBuffer(buffer);

    NodeId typeId = decoder.decodeNodeId();
    UByte encodingMask = decoder.decodeByte(null);
    long length = decoder.decodeUInt32().longValue();
    int bodyStart = buffer.readerIndex();
    String securityPolicyUri = decoder.decodeString();
    ByteString senderCertificate = decoder.decodeByteString();
    DateTime signingTime = decoder.decodeDateTime();
    int keyDataLength = decoder.decodeUInt16().intValue();
    int keyDataStart = buffer.readerIndex();
    ByteString senderPublicKey = decoder.decodeByteString();
    ByteString encodedReceiverPublicKey = decoder.decodeByteString();

    assertEquals(NodeIds.EccEncryptedSecret, typeId);
    assertEquals(Unsigned.ubyte(0x01), encodingMask);
    assertEquals(bytes.length - bodyStart, length);
    assertEquals(profile.securityPolicy().getUri(), securityPolicyUri);
    assertFalse(senderCertificate.isNullOrEmpty());
    assertDoesNotThrow(() -> CertificateUtil.decodeCertificate(senderCertificate.bytesOrEmpty()));
    assertFalse(signingTime.isNull());
    assertEquals(buffer.readerIndex() - keyDataStart, keyDataLength);
    assertEquals(ephemeralPublicKeyLength(profile), senderPublicKey.length());
    assertEquals(receiverPublicKey, encodedReceiverPublicKey);
    assertTrue(buffer.readableBytes() > signatureLength(profile));
  }

  // The client trusts the CreateSession receiver key only after the server certificate signs it.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void verifiesSignedEphemeralKey(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    EphemeralKeyType ephemeralKey =
        EccEncryptedSecret.createEphemeralKey(profile, sender.keyPair(), receiverEphemeralKeyPair);

    assertDoesNotThrow(
        () -> EccEncryptedSecret.verifyEphemeralKey(profile, sender.certificate(), ephemeralKey));

    byte[] signature =
        Arrays.copyOf(
            ephemeralKey.getSignature().bytesOrEmpty(), ephemeralKey.getSignature().length());
    signature[signature.length - 1] ^= 0x01;

    assertThrows(
        UaException.class,
        () ->
            EccEncryptedSecret.verifyEphemeralKey(
                profile,
                sender.certificate(),
                new EphemeralKeyType(ephemeralKey.getPublicKey(), ByteString.of(signature))));
  }

  // A valid secret from another session must not authenticate against this session's nonce.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsWrongNonce(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccEncryptedSecret.decrypt(
                    profile,
                    receiverEphemeralKeyPair,
                    receiverPublicKey,
                    sender.certificate(),
                    ByteString.of(new byte[] {0x01}),
                    encryptedSecret));

    assertEquals(StatusCodes.Bad_NonceInvalid, exception.getStatusCode().getValue());
  }

  // Stale token-secret signatures can otherwise be replayed if the session nonce is also replayed.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsStaleSigningTime(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);

    ByteString staleSigningTimeSecret =
        rewriteEncryptedSecret(
            profile,
            sender,
            receiverEphemeralKeyPair,
            encryptedSecret,
            parts ->
                Unpooled.wrappedBuffer(parts.bytes())
                    .setLongLE(
                        parts.signingTimeIndex(),
                        new DateTime(Instant.now().minus(Duration.ofMinutes(10))).getUtcTime()),
            UnaryOperator.identity());

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccEncryptedSecret.decrypt(
                    profile,
                    receiverEphemeralKeyPair,
                    receiverPublicKey,
                    sender.certificate(),
                    NONCE,
                    staleSigningTimeSecret));

    assertEquals(StatusCodes.Bad_InvalidTimestamp, exception.getStatusCode().getValue());
  }

  // The receiver key advertised in CreateSession is part of the encrypted secret's identity.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsWrongReceiverPrivateKey(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);
    KeyPair wrongReceiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);

    assertThrows(
        UaException.class,
        () ->
            EccEncryptedSecret.decrypt(
                profile,
                wrongReceiverEphemeralKeyPair,
                receiverPublicKey,
                sender.certificate(),
                NONCE,
                encryptedSecret));
  }

  // The unencrypted ECC key data is still covered by the sender's application signature.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsTamperedSignature(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);

    byte[] tampered = Arrays.copyOf(encryptedSecret.bytesOrEmpty(), encryptedSecret.length());
    tampered[tampered.length - 1] ^= 0x01;

    assertThrows(
        UaException.class,
        () ->
            EccEncryptedSecret.decrypt(
                profile,
                receiverEphemeralKeyPair,
                receiverPublicKey,
                sender.certificate(),
                NONCE,
                ByteString.of(tampered)));
  }

  // Padding is inside the authenticated ciphertext, so a valid tag with bad padding must still be
  // rejected by payload validation rather than accepted as a password.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsBadPayloadPadding(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);

    ByteString badPaddingSecret =
        rewriteEncryptedSecret(
            profile,
            sender,
            receiverEphemeralKeyPair,
            encryptedSecret,
            parts -> {},
            EccEncryptedSecretTest::corruptPayloadPadding);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccEncryptedSecret.decrypt(
                    profile,
                    receiverEphemeralKeyPair,
                    receiverPublicKey,
                    sender.certificate(),
                    NONCE,
                    badPaddingSecret));

    assertEquals(StatusCodes.Bad_DecodingError, exception.getStatusCode().getValue());
  }

  // A valid signature and AEAD tag do not make a malformed decrypted payload acceptable.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsMalformedPlaintextPayload(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);

    ByteString malformedSecret =
        rewriteEncryptedSecret(
            profile,
            sender,
            receiverEphemeralKeyPair,
            encryptedSecret,
            parts -> {},
            EccEncryptedSecretTest::corruptSecretLength);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccEncryptedSecret.decrypt(
                    profile,
                    receiverEphemeralKeyPair,
                    receiverPublicKey,
                    sender.certificate(),
                    NONCE,
                    malformedSecret));

    assertEquals(StatusCodes.Bad_DecodingError, exception.getStatusCode().getValue());
  }

  // The outer opaque length bounds the authenticated payload and signature.
  @ParameterizedTest
  @MethodSource("supportedEccProfiles")
  void rejectsMalformedLength(SecurityPolicyProfile profile) throws Exception {
    ApplicationIdentity sender = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret = encryptedSecret(profile, sender, receiverPublicKey);
    byte[] malformed = Arrays.copyOf(encryptedSecret.bytesOrEmpty(), encryptedSecret.length());
    ByteBuf buffer = Unpooled.wrappedBuffer(malformed);
    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    decoder.setBuffer(buffer);
    decoder.decodeNodeId();
    decoder.decodeByte(null);
    int lengthIndex = buffer.readerIndex();
    malformed[lengthIndex] ^= 0x01;

    assertThrows(
        UaException.class,
        () ->
            EccEncryptedSecret.decrypt(
                profile,
                receiverEphemeralKeyPair,
                receiverPublicKey,
                sender.certificate(),
                NONCE,
                ByteString.of(malformed)));
  }

  private static ByteString encryptedSecret(
      SecurityPolicyProfile profile, ApplicationIdentity sender, ByteString receiverPublicKey)
      throws UaException {

    return EccEncryptedSecret.encrypt(
        profile,
        sender.keyPair(),
        new X509Certificate[] {sender.certificate()},
        receiverPublicKey,
        NONCE,
        SECRET,
        false);
  }

  private static ByteString rewriteEncryptedSecret(
      SecurityPolicyProfile profile,
      ApplicationIdentity sender,
      KeyPair receiverEphemeralKeyPair,
      ByteString encryptedSecret,
      Consumer<ParsedSecretParts> headerMutator,
      UnaryOperator<byte[]> plaintextMutator)
      throws Exception {

    ParsedSecretParts parts = parseSecretParts(profile, encryptedSecret);
    byte[] bytes = Arrays.copyOf(parts.bytes(), parts.bytes().length);
    byte[] plainText = decryptPayload(profile, receiverEphemeralKeyPair, parts);

    ParsedSecretParts mutableParts =
        new ParsedSecretParts(
            bytes,
            parts.signingTimeIndex(),
            parts.encryptedPayloadStart(),
            parts.signatureStart(),
            parts.senderPublicKey(),
            parts.receiverPublicKey());
    headerMutator.accept(mutableParts);

    byte[] newHeaderBytes = Arrays.copyOfRange(bytes, 0, parts.encryptedPayloadStart());
    byte[] newEncryptedPayload =
        encryptPayload(
            profile,
            parts,
            deriveSecretKeyMaterial(profile, receiverEphemeralKeyPair, parts),
            newHeaderBytes,
            plaintextMutator.apply(plainText));

    System.arraycopy(
        newEncryptedPayload, 0, bytes, parts.encryptedPayloadStart(), newEncryptedPayload.length);

    byte[] signature =
        sign(
            profile,
            sender.keyPair().getPrivate(),
            ByteBuffer.wrap(bytes, 0, parts.signatureStart()));

    System.arraycopy(signature, 0, bytes, parts.signatureStart(), signature.length);

    return ByteString.of(bytes);
  }

  private static byte[] corruptPayloadPadding(byte[] plainText) {
    byte[] corrupted = Arrays.copyOf(plainText, plainText.length);
    int paddingSize = corrupted[corrupted.length - 2] & 0xFF;

    if (paddingSize > 0) {
      corrupted[corrupted.length - 2 - paddingSize] ^= 0x01;
    } else {
      corrupted[corrupted.length - 1] = 0x01;
    }

    return corrupted;
  }

  private static byte[] corruptSecretLength(byte[] plainText) {
    byte[] corrupted = Arrays.copyOf(plainText, plainText.length);
    int nonceLength =
        (corrupted[0] & 0xFF)
            | ((corrupted[1] & 0xFF) << 8)
            | ((corrupted[2] & 0xFF) << 16)
            | ((corrupted[3] & 0xFF) << 24);
    int secretLengthIndex = Integer.BYTES + nonceLength;

    corrupted[secretLengthIndex] = (byte) 0xFF;
    corrupted[secretLengthIndex + 1] = 0x7F;
    corrupted[secretLengthIndex + 2] = 0;
    corrupted[secretLengthIndex + 3] = 0;

    return corrupted;
  }

  private static ParsedSecretParts parseSecretParts(
      SecurityPolicyProfile profile, ByteString encryptedSecret) {

    byte[] bytes = encryptedSecret.bytesOrEmpty();
    ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    decoder.setBuffer(buffer);

    decoder.decodeNodeId();
    decoder.decodeByte(null);
    decoder.decodeUInt32();
    decoder.decodeString();
    decoder.decodeByteString();
    int signingTimeIndex = buffer.readerIndex();
    decoder.decodeDateTime();
    decoder.decodeUInt16();
    ByteString senderPublicKey = decoder.decodeByteString();
    ByteString receiverPublicKey = decoder.decodeByteString();
    int encryptedPayloadStart = buffer.readerIndex();
    int signatureStart = bytes.length - signatureLength(profile);

    return new ParsedSecretParts(
        bytes,
        signingTimeIndex,
        encryptedPayloadStart,
        signatureStart,
        senderPublicKey,
        receiverPublicKey);
  }

  private static byte[] decryptPayload(
      SecurityPolicyProfile profile, KeyPair receiverEphemeralKeyPair, ParsedSecretParts parts)
      throws Exception {

    SecretKeyMaterial keyMaterial =
        deriveSecretKeyMaterial(profile, receiverEphemeralKeyPair, parts);
    byte[] headerBytes = Arrays.copyOfRange(parts.bytes(), 0, parts.encryptedPayloadStart());
    byte[] encryptedPayload =
        Arrays.copyOfRange(parts.bytes(), parts.encryptedPayloadStart(), parts.signatureStart());

    Cipher cipher = initAeadCipher(profile, Cipher.DECRYPT_MODE, keyMaterial, headerBytes);

    return cipher.doFinal(encryptedPayload);
  }

  private static byte[] encryptPayload(
      SecurityPolicyProfile profile,
      ParsedSecretParts parts,
      SecretKeyMaterial keyMaterial,
      byte[] headerBytes,
      byte[] plainText)
      throws Exception {

    Cipher cipher = initAeadCipher(profile, Cipher.ENCRYPT_MODE, keyMaterial, headerBytes);
    byte[] encryptedPayload = cipher.doFinal(plainText);
    int expectedLength = parts.signatureStart() - parts.encryptedPayloadStart();

    assertEquals(expectedLength, encryptedPayload.length);

    return encryptedPayload;
  }

  private static Cipher initAeadCipher(
      SecurityPolicyProfile profile, int mode, SecretKeyMaterial keyMaterial, byte[] headerBytes)
      throws UaException {

    ProviderProfile providerProfile = SecurityProviderResolver.create().resolve(profile);
    ByteBuffer aad = ByteBuffer.wrap(headerBytes);

    return switch (profile.chunkProtectionAxis()) {
      case AES_GCM ->
          AeadCipherUtil.initAesGcm(
              providerProfile,
              mode,
              keyMaterial.encryptionKey(),
              keyMaterial.initializationVector(),
              aad);
      case CHACHA20_POLY1305 ->
          AeadCipherUtil.initChaCha20Poly1305(
              providerProfile,
              mode,
              keyMaterial.encryptionKey(),
              keyMaterial.initializationVector(),
              aad);
      default -> throw new IllegalArgumentException("unsupported chunk axis");
    };
  }

  private static SecretKeyMaterial deriveSecretKeyMaterial(
      SecurityPolicyProfile profile, KeyPair receiverEphemeralKeyPair, ParsedSecretParts parts)
      throws Exception {

    ProviderProfile providerProfile = SecurityProviderResolver.create().resolve(profile);
    PublicKey senderEphemeralPublicKey =
        switch (profile.keyAgreementAxis()) {
          case ECDH_NIST_P256 ->
              EccPublicKeyCodec.decodeNistP256(parts.senderPublicKey(), providerProfile);
          case X25519 -> EccPublicKeyCodec.decodeX25519(parts.senderPublicKey(), providerProfile);
          default -> throw new IllegalArgumentException("unsupported key agreement axis");
        };
    byte[] sharedSecret =
        switch (profile.keyAgreementAxis()) {
          case ECDH_NIST_P256 ->
              EccKeyAgreementUtil.agreeNistP256(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          case X25519 ->
              EccKeyAgreementUtil.agreeX25519(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          default -> throw new IllegalArgumentException("unsupported key agreement axis");
        };

    int totalLength =
        profile.symmetricEncryptionKeySize()
            + EccKeyAgreementUtil.AEAD_INITIALIZATION_VECTOR_LENGTH;
    byte[] salt = secretSalt(totalLength, parts.senderPublicKey(), parts.receiverPublicKey());
    byte[] keyMaterial =
        HkdfUtil.hkdfSha256(sharedSecret, salt, salt, totalLength, hmacProvider(providerProfile));

    return new SecretKeyMaterial(
        Arrays.copyOfRange(keyMaterial, 0, profile.symmetricEncryptionKeySize()),
        Arrays.copyOfRange(keyMaterial, profile.symmetricEncryptionKeySize(), totalLength));
  }

  private static Provider hmacProvider(ProviderProfile providerProfile) throws Exception {
    return SecurityProviderSupport.withProviderProfile(
        providerProfile,
        "HmacSHA256",
        provider -> {
          Mac.getInstance("HmacSHA256", provider);
          return provider;
        });
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

  private static byte[] sign(
      SecurityPolicyProfile profile, java.security.PrivateKey privateKey, ByteBuffer data)
      throws UaException {

    ProviderProfile providerProfile = SecurityProviderResolver.create().resolve(profile);

    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256 ->
          EccSignatureUtil.signEcdsaP256Sha256P1363(providerProfile, privateKey, data);
      case ED25519 -> EccSignatureUtil.signEd25519(providerProfile, privateKey, data);
      default -> throw new IllegalArgumentException("unsupported auth axis");
    };
  }

  private static Stream<Arguments> supportedEccProfiles() {
    return Stream.of(
        Arguments.of(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()),
        Arguments.of(SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile()));
  }

  private static ApplicationIdentity applicationIdentity(SecurityPolicyProfile profile)
      throws Exception {

    KeyPair keyPair =
        switch (profile.authAxis()) {
          case ECDSA_NIST_P256_SHA256 -> SelfSignedCertificateGenerator.generateNistP256KeyPair();
          case ED25519 -> SelfSignedCertificateGenerator.generateEd25519KeyPair();
          default ->
              throw new IllegalArgumentException("unsupported auth axis: " + profile.authAxis());
        };

    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setApplicationUri("urn:eclipse:milo:ecc-secret-test")
            .addDnsName("localhost")
            .build();

    return new ApplicationIdentity(keyPair, certificate);
  }

  private static int ephemeralPublicKeyLength(SecurityPolicyProfile profile) {
    KeyAgreementAxis keyAgreementAxis = profile.keyAgreementAxis();

    return switch (keyAgreementAxis) {
      case ECDH_NIST_P256 -> EccPublicKeyCodec.NIST_P256_PUBLIC_KEY_LENGTH;
      case X25519 -> EccPublicKeyCodec.X25519_PUBLIC_KEY_LENGTH;
      default ->
          throw new IllegalArgumentException("unsupported key agreement axis: " + keyAgreementAxis);
    };
  }

  private static int signatureLength(SecurityPolicyProfile profile) {
    AuthAxis authAxis = profile.authAxis();

    return switch (authAxis) {
      case ECDSA_NIST_P256_SHA256 -> EccSignatureUtil.ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH;
      case ED25519 -> EccSignatureUtil.ED25519_SIGNATURE_LENGTH;
      default -> throw new IllegalArgumentException("unsupported auth axis: " + authAxis);
    };
  }

  private record ApplicationIdentity(KeyPair keyPair, X509Certificate certificate) {}

  private record ParsedSecretParts(
      byte[] bytes,
      int signingTimeIndex,
      int encryptedPayloadStart,
      int signatureStart,
      ByteString senderPublicKey,
      ByteString receiverPublicKey) {}

  private record SecretKeyMaterial(byte[] encryptionKey, byte[] initializationVector) {}
}
