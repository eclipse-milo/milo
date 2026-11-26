/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.SecurityConfiguration;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionServerCertificate;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.UsernameIdentity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.security.AeadCipherUtil;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
import org.eclipse.milo.opcua.stack.core.security.EccKeyAgreementUtil;
import org.eclipse.milo.opcua.stack.core.security.EccPublicKeyCodec;
import org.eclipse.milo.opcua.stack.core.security.EccSignatureUtil;
import org.eclipse.milo.opcua.stack.core.security.FiniteFieldDhKeyAgreementUtil;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.CipherFactory;
import org.eclipse.milo.opcua.stack.core.util.HkdfUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AbstractUsernameIdentityValidatorTest {

  private static final ByteString SERVER_NONCE =
      ByteString.of(
          new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
            0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
          });
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";
  private static final UserTokenPolicy ENCRYPTED_POLICY =
      new UserTokenPolicy(
          "username", UserTokenType.UserName, null, null, SecurityPolicy.Basic256Sha256.getUri());
  private static final SignatureData NO_SIGNATURE = new SignatureData(null, null);

  private static KeyPair rsaKeyPair;
  private static X509Certificate rsaCertificate;

  @BeforeAll
  static void setUpCryptography() throws Exception {
    rsaKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    rsaCertificate =
        new SelfSignedCertificateBuilder(rsaKeyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .build();
  }

  @Test
  void emptyEncryptedPasswordWithoutNonceReturnsIdentityTokenInvalid() {
    TestUsernameIdentityValidator validator =
        new TestUsernameIdentityValidator(tokenData("", new byte[0]), true);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
    assertNull(exception.getCause());
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void emptyEncryptedPasswordWithWrongNonceReturnsIdentityTokenInvalid() {
    TestUsernameIdentityValidator validator =
        new TestUsernameIdentityValidator(tokenData("", new byte[SERVER_NONCE.length()]), true);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
    assertNull(exception.getCause());
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void shortEncryptedTokenDataReturnsIdentityTokenInvalid() {
    TestUsernameIdentityValidator validator = new TestUsernameIdentityValidator(new byte[0], true);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
    assertNull(exception.getCause());
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void oversizedEncryptedPasswordReturnsIdentityTokenInvalid() {
    String password = "x".repeat(1025);
    TestUsernameIdentityValidator validator =
        new TestUsernameIdentityValidator(tokenData(password, SERVER_NONCE.bytesOrEmpty()), true);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
    assertNull(exception.getCause());
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void wrongEncryptedPasswordWithValidNonceReturnsIdentityTokenInvalid() {
    TestUsernameIdentityValidator validator =
        new TestUsernameIdentityValidator(tokenData("wrong", SERVER_NONCE.bytesOrEmpty()), false);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
    assertNull(exception.getCause());
    assertEquals(USERNAME, validator.username);
    assertEquals("wrong", validator.password);
    assertEquals(1, validator.authenticateCount);
  }

  @Test
  void rsa15CiphertextFailuresHaveUniformStatusAndNoCause() throws Exception {
    CryptoUsernameIdentityValidator validator = new CryptoUsernameIdentityValidator(false);
    byte[] validCiphertext =
        encrypt(SecurityPolicy.Basic128Rsa15, tokenData(PASSWORD, SERVER_NONCE.bytesOrEmpty()));
    byte[] wrongPasswordCiphertext =
        encrypt(SecurityPolicy.Basic128Rsa15, tokenData("wrong", SERVER_NONCE.bytesOrEmpty()));

    for (byte[] ciphertext : List.of(wrongPasswordCiphertext, zeroedCopy(validCiphertext))) {
      UaException exception =
          assertThrows(
              UaException.class,
              () ->
                  validator.validate(
                      encryptedSession(SecurityPolicy.Basic128Rsa15),
                      encryptedToken(SecurityPolicy.Basic128Rsa15, ciphertext)));

      assertUniformEncryptedTokenFailure(exception);
    }

    assertEquals(2, validator.decryptCount);
    assertEquals(1, validator.authenticateCount);
  }

  @Test
  void rsa15EncryptedPasswordStillAuthenticates() throws Exception {
    CryptoUsernameIdentityValidator validator = new CryptoUsernameIdentityValidator(true);
    byte[] ciphertext =
        encrypt(SecurityPolicy.Basic128Rsa15, tokenData(PASSWORD, SERVER_NONCE.bytesOrEmpty()));

    UsernameIdentity identity =
        validator.validate(
            encryptedSession(SecurityPolicy.Basic128Rsa15),
            encryptedToken(SecurityPolicy.Basic128Rsa15, ciphertext));

    assertEquals(USERNAME, identity.getUsername());
    assertEquals(1, validator.decryptCount);
    assertEquals(1, validator.authenticateCount);
  }

  @Test
  void oaepCiphertextFailuresHaveUniformStatusAndNoCause() throws Exception {
    CryptoUsernameIdentityValidator validator = new CryptoUsernameIdentityValidator(false);
    byte[] validCiphertext =
        encrypt(SecurityPolicy.Basic256Sha256, tokenData(PASSWORD, SERVER_NONCE.bytesOrEmpty()));
    byte[] wrongPasswordCiphertext =
        encrypt(SecurityPolicy.Basic256Sha256, tokenData("wrong", SERVER_NONCE.bytesOrEmpty()));

    for (byte[] ciphertext : List.of(wrongPasswordCiphertext, zeroedCopy(validCiphertext))) {
      UaException exception =
          assertThrows(
              UaException.class,
              () ->
                  validator.validate(
                      encryptedSession(SecurityPolicy.Basic256Sha256),
                      encryptedToken(SecurityPolicy.Basic256Sha256, ciphertext)));

      assertUniformEncryptedTokenFailure(exception);
    }

    assertEquals(2, validator.decryptCount);
    assertEquals(1, validator.authenticateCount);
  }

  @Test
  void oaepEncryptedPasswordStillAuthenticates() throws Exception {
    CryptoUsernameIdentityValidator validator = new CryptoUsernameIdentityValidator(true);
    byte[] ciphertext =
        encrypt(SecurityPolicy.Basic256Sha256, tokenData(PASSWORD, SERVER_NONCE.bytesOrEmpty()));

    UsernameIdentity identity =
        validator.validate(
            encryptedSession(SecurityPolicy.Basic256Sha256),
            encryptedToken(SecurityPolicy.Basic256Sha256, ciphertext));

    assertEquals(USERNAME, identity.getUsername());
    assertEquals(1, validator.decryptCount);
    assertEquals(1, validator.authenticateCount);
  }

  @Test
  void encryptedPasswordWithValidNonceAuthenticates() throws UaException {
    TestUsernameIdentityValidator validator =
        new TestUsernameIdentityValidator(tokenData(PASSWORD, SERVER_NONCE.bytesOrEmpty()), true);

    UsernameIdentity identity = validator.validate(encryptedSession(), encryptedToken());

    assertInstanceOf(DefaultUsernameIdentity.class, identity);
    assertEquals(USERNAME, identity.getUsername());
    assertEquals(USERNAME, validator.username);
    assertEquals(PASSWORD, validator.password);
    assertEquals(1, validator.authenticateCount);
  }

  // The validator is the server-side bridge from opaque enhanced token bytes to application auth.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void decryptsEnhancedUsernameToken(SecurityPolicyProfile profile) throws Exception {
    TokenFixture fixture = tokenFixture(profile);
    TestUsernameValidator validator = new TestUsernameValidator("password");

    Identity identity =
        validator.validateIdentityToken(
            session(fixture, SERVER_NONCE),
            fixture.token(),
            policy(profile.securityPolicy()),
            new SignatureData(null, null));

    assertEquals(UserTokenType.UserName, identity.getUserTokenType());
    assertEquals("user", ((Identity.UsernameIdentity) identity).getUsername());
  }

  // UA Part 4, Table 188: spec-conformant ECC clients (e.g. UA-.NETStandard) send a null
  // encryptionAlgorithm on enhanced username tokens; the server must accept them.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void acceptsEnhancedUsernameTokenWithNullEncryptionAlgorithm(SecurityPolicyProfile profile)
      throws Exception {
    TokenFixture fixture = tokenFixture(profile);
    UserNameIdentityToken nullAlgorithmToken =
        new UserNameIdentityToken("username", "user", fixture.token().getPassword(), null);
    TestUsernameValidator validator = new TestUsernameValidator("password");

    Identity identity =
        validator.validateIdentityToken(
            session(fixture, SERVER_NONCE),
            nullAlgorithmToken,
            policy(profile.securityPolicy()),
            new SignatureData(null, null));

    assertEquals(UserTokenType.UserName, identity.getUserTokenType());
    assertEquals("user", ((Identity.UsernameIdentity) identity).getUsername());
  }

  // ActivateSession must fail clearly if CreateSession never issued receiver key material.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void rejectsMissingEnhancedSessionKeyMaterial(SecurityPolicyProfile profile) throws Exception {
    TokenFixture fixture = tokenFixture(profile);
    TestUsernameValidator validator = new TestUsernameValidator("password");
    Session session = session(fixture, SERVER_NONCE);
    when(session.getUserTokenEphemeralKeyPair()).thenReturn(Optional.empty());

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session,
                    fixture.token(),
                    policy(profile.securityPolicy()),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
  }

  // The password secret is bound to the exact public key returned to the client.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void rejectsWrongReceiverKey(SecurityPolicyProfile profile) throws Exception {
    TokenFixture fixture = tokenFixture(profile);
    KeyPair wrongReceiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString wrongReceiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(
            profile, wrongReceiverEphemeralKeyPair.getPublic());
    Session session = session(fixture, SERVER_NONCE);
    when(session.getUserTokenEphemeralPublicKey()).thenReturn(Optional.of(wrongReceiverPublicKey));
    TestUsernameValidator validator = new TestUsernameValidator("password");

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session,
                    fixture.token(),
                    policy(profile.securityPolicy()),
                    new SignatureData(null, null)));

    assertUniformEncryptedTokenFailure(exception);
    assertEquals(0, validator.authenticateCount);
  }

  // Part 6 6.8.2 makes the receiver key single-use: once a token consumes it, a replay without
  // rotation must fail because the session no longer retains that key material.
  @Test
  void rejectsStaleEnhancedSessionKeyAfterSuccessfulValidation() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    TokenFixture fixture = tokenFixture(profile);
    MutableSessionKey mutableKey = mutableSessionKey(fixture);
    Session session = session(fixture, SERVER_NONCE, mutableKey);
    TestUsernameValidator validator = new TestUsernameValidator("password");

    validator.validateIdentityToken(
        session, fixture.token(), policy(profile.securityPolicy()), new SignatureData(null, null));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session,
                    fixture.token(),
                    policy(profile.securityPolicy()),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
  }

  // Normal ActivateSession rotation remains valid: after the consumed key is cleared, installing a
  // fresh receiver key lets the next enhanced username token authenticate.
  @Test
  void acceptsFreshEnhancedSessionKeyAfterSuccessfulValidation() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ApplicationIdentity clientIdentity = applicationIdentity(profile);
    TokenFixture first = tokenFixture(profile, "password", clientIdentity);
    TokenFixture rotated = tokenFixture(profile, "password", clientIdentity);
    MutableSessionKey mutableKey = mutableSessionKey(first);
    Session session = session(first, SERVER_NONCE, mutableKey);
    TestUsernameValidator validator = new TestUsernameValidator("password");

    validator.validateIdentityToken(
        session, first.token(), policy(profile.securityPolicy()), new SignatureData(null, null));
    mutableKey.set(rotated);

    Identity identity =
        validator.validateIdentityToken(
            session,
            rotated.token(),
            policy(profile.securityPolicy()),
            new SignatureData(null, null));

    assertEquals("user", ((Identity.UsernameIdentity) identity).getUsername());
  }

  // Malformed enhanced token-secret bytes should fail token validation, not reach authentication.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void rejectsMalformedEnhancedPayload(SecurityPolicyProfile profile) throws Exception {
    TokenFixture fixture = tokenFixture(profile);
    UserNameIdentityToken malformedToken =
        new UserNameIdentityToken(
            "username",
            "user",
            ByteString.of(new byte[] {0x01, 0x02, 0x03}),
            profile.securityPolicy().getUri());
    TestUsernameValidator validator = new TestUsernameValidator("password");

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session(fixture, SERVER_NONCE),
                    malformedToken,
                    policy(profile.securityPolicy()),
                    new SignatureData(null, null)));

    assertUniformEncryptedTokenFailure(exception);
    assertEquals(0, validator.authenticateCount);
  }

  // Enhanced token-secret nonce mismatches should map to the same service result as the legacy RSA
  // username-token path.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void mapsEnhancedNonceMismatchLikeRsaPath(SecurityPolicyProfile profile) throws Exception {
    TokenFixture fixture = tokenFixture(profile);
    TestUsernameValidator validator = new TestUsernameValidator("password");

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session(fixture, ByteString.of(new byte[] {0x01})),
                    fixture.token(),
                    policy(profile.securityPolicy()),
                    new SignatureData(null, null)));

    assertUniformEncryptedTokenFailure(exception);
    assertEquals(0, validator.authenticateCount);
  }

  // Part 4 §7.40.2.1 and §7.40.2.3: every EncryptedSecret validation, decryption, padding, nonce,
  // and credential failure on the enhanced path must collapse to Bad_IdentityTokenInvalid with no
  // cause, clearing the session's ephemeral key pair each time, and only a valid token with a wrong
  // password may reach application authentication.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void enhancedEncryptedSecretFailuresHaveUniformStatusAndNoCause(SecurityPolicyProfile profile)
      throws Exception {
    ApplicationIdentity clientIdentity = applicationIdentity(profile);
    ApplicationIdentity otherClientIdentity = applicationIdentity(profile);
    TokenFixture validFixture = tokenFixture(profile, PASSWORD, clientIdentity);

    ByteString validSecret = validFixture.token().getPassword();
    byte[] validSecretBytes = validSecret.bytesOrEmpty();
    ParsedEnhancedSecretParts validParts = parseEnhancedSecretParts(profile, validSecret);

    // E2: valid EncryptedSecret carrying the wrong password.
    TokenFixture wrongPasswordFixture = tokenFixture(profile, "wrong", clientIdentity);

    // E3: wrong nonce inside the encrypted payload, right password.
    ByteString wrongNonceSecret =
        EccEncryptedSecret.encrypt(
            profile,
            clientIdentity.keyPair(),
            new X509Certificate[] {clientIdentity.certificate()},
            validFixture.receiverPublicKey(),
            ByteString.of(new byte[] {0x01}),
            ByteString.of(PASSWORD.getBytes(StandardCharsets.UTF_8)),
            false);

    // E4: encrypted to a different receiver ephemeral key than the session's.
    KeyPair otherReceiverKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString otherReceiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, otherReceiverKeyPair.getPublic());
    ByteString otherReceiverSecret =
        EccEncryptedSecret.encrypt(
            profile,
            clientIdentity.keyPair(),
            new X509Certificate[] {clientIdentity.certificate()},
            otherReceiverPublicKey,
            SERVER_NONCE,
            ByteString.of(PASSWORD.getBytes(StandardCharsets.UTF_8)),
            false);

    // E5: payload byte flipped, original signature kept.
    byte[] flippedPayloadKeptSigBytes = Arrays.copyOf(validSecretBytes, validSecretBytes.length);
    flippedPayloadKeptSigBytes[validParts.encryptedPayloadStart()] ^= 0x01;
    ByteString flippedPayloadKeptSigSecret = ByteString.of(flippedPayloadKeptSigBytes);

    // E6: payload byte flipped, re-signed so signature passes and AEAD decryption fails.
    byte[] flippedPayloadResignedBytes = Arrays.copyOf(validSecretBytes, validSecretBytes.length);
    flippedPayloadResignedBytes[validParts.encryptedPayloadStart()] ^= 0x01;
    byte[] newSignature =
        signEnhanced(
            profile,
            clientIdentity.keyPair().getPrivate(),
            ByteBuffer.wrap(flippedPayloadResignedBytes, 0, validParts.signatureStart()));
    System.arraycopy(
        newSignature,
        0,
        flippedPayloadResignedBytes,
        validParts.signatureStart(),
        newSignature.length);
    ByteString flippedPayloadResignedSecret = ByteString.of(flippedPayloadResignedBytes);

    // E7: SigningTime one hour old, re-encrypted with updated AAD header and re-signed.
    ByteString staleSigningTimeSecret =
        rewriteEnhancedSecret(
            profile,
            clientIdentity,
            validFixture.receiverEphemeralKeyPair(),
            validSecret,
            parts ->
                Unpooled.wrappedBuffer(parts.bytes())
                    .setLongLE(
                        parts.signingTimeIndex(),
                        new DateTime(Instant.now().minus(Duration.ofHours(1))).getUtcTime()),
            UnaryOperator.identity());

    // E8: embedded certificate does not match the SecureChannel's client certificate.
    ByteString mismatchedCertSecret =
        EccEncryptedSecret.encrypt(
            profile,
            otherClientIdentity.keyPair(),
            new X509Certificate[] {otherClientIdentity.certificate()},
            validFixture.receiverPublicKey(),
            SERVER_NONCE,
            ByteString.of(PASSWORD.getBytes(StandardCharsets.UTF_8)),
            false);

    // E9: last byte truncated.
    ByteString truncatedSecret =
        ByteString.of(Arrays.copyOf(validSecretBytes, validSecretBytes.length - 1));

    // Corrupted payload padding inside a validly signed and AEAD-authenticated payload.
    ByteString badPaddingSecret =
        rewriteEnhancedSecret(
            profile,
            clientIdentity,
            validFixture.receiverEphemeralKeyPair(),
            validSecret,
            parts -> {},
            AbstractUsernameIdentityValidatorTest::corruptEnhancedPayloadPadding);

    // Control assertions: confirm each mutation triggers the intended distinct decrypt failure
    // before asserting that the validator masks all of them uniformly.
    assertEnhancedDecryptFailsWith(
        profile, validFixture, wrongNonceSecret, StatusCodes.Bad_NonceInvalid);
    assertEnhancedDecryptFailsWith(
        profile, validFixture, otherReceiverSecret, StatusCodes.Bad_NonceInvalid);
    assertEnhancedDecryptFailsWith(
        profile, validFixture, flippedPayloadKeptSigSecret, StatusCodes.Bad_SecurityChecksFailed);
    assertEnhancedDecryptFailsWith(
        profile, validFixture, flippedPayloadResignedSecret, StatusCodes.Bad_SecurityChecksFailed);
    assertEnhancedDecryptFailsWith(
        profile, validFixture, staleSigningTimeSecret, StatusCodes.Bad_InvalidTimestamp);
    assertEnhancedDecryptFailsWith(
        profile, validFixture, mismatchedCertSecret, StatusCodes.Bad_CertificateInvalid);
    assertEnhancedDecryptFailsWith(
        profile, validFixture, truncatedSecret, StatusCodes.Bad_DecodingError);
    assertEnhancedDecryptFailsWith(
        profile, validFixture, badPaddingSecret, StatusCodes.Bad_DecodingError);

    TestUsernameValidator validator = new TestUsernameValidator(PASSWORD);
    List<TokenFixture> cases =
        List.of(
            wrongPasswordFixture,
            withSecret(validFixture, wrongNonceSecret),
            withSecret(validFixture, otherReceiverSecret),
            withSecret(validFixture, flippedPayloadKeptSigSecret),
            withSecret(validFixture, flippedPayloadResignedSecret),
            withSecret(validFixture, staleSigningTimeSecret),
            withSecret(validFixture, mismatchedCertSecret),
            withSecret(validFixture, truncatedSecret),
            withSecret(validFixture, badPaddingSecret));

    for (TokenFixture failureCase : cases) {
      MutableSessionKey mutableKey = mutableSessionKey(failureCase);
      Session session = session(failureCase, SERVER_NONCE, mutableKey);

      UaException exception =
          assertThrows(
              UaException.class,
              () ->
                  validator.validateIdentityToken(
                      session,
                      failureCase.token(),
                      policy(profile.securityPolicy()),
                      new SignatureData(null, null)));

      assertUniformEncryptedTokenFailure(exception);
      assertTrue(
          mutableKey.keyPair().isEmpty() && mutableKey.publicKey().isEmpty(),
          "ephemeral key pair must be cleared even when enhanced token validation fails");
    }

    assertEquals(
        1,
        validator.authenticateCount,
        "only the wrong-password token should reach application authentication");
  }

  // Part 4 (7.41): enhanced username secrets require a secured channel because a None channel
  // cannot negotiate the receiver key material needed to protect the password.
  @Test
  void rejectsEnhancedUsernameTokenPolicyOnNoneChannel() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    TokenFixture fixture = tokenFixture(profile);
    Session session = session(fixture, SERVER_NONCE);
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null));
    TestUsernameValidator validator = new TestUsernameValidator("password");

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session,
                    fixture.token(),
                    policy(profile.securityPolicy()),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
  }

  // Part 4 (7.41): an explicit username-token policy must use the same public-key family as the
  // SecureChannel; a mismatched server configuration is rejected before decryption is attempted.
  @Test
  void rejectsExplicitCrossFamilyUsernameTokenPolicy() throws Exception {
    Session session = mock(Session.class);
    when(session.getLastNonce()).thenReturn(SERVER_NONCE);
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                SecurityPolicy.ECC_nistP256_AesGcm,
                MessageSecurityMode.SignAndEncrypt,
                null,
                null,
                null,
                null,
                null));
    TestUsernameValidator validator = new TestUsernameValidator("password");

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session,
                    new UserNameIdentityToken(
                        "username", "user", ByteString.of(new byte[] {0x01}), null),
                    policy(SecurityPolicy.Basic256Sha256),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
  }

  // Enhanced username-token secrets must respect the same configured password limit as the legacy
  // RSA username-token path after decryption succeeds.
  @Test
  void rejectsEnhancedPasswordAboveConfiguredLimit() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    TokenFixture fixture = tokenFixture(profile, "password");
    TestUsernameValidator validator = new TestUsernameValidator("password");
    Session session =
        session(
            fixture,
            SERVER_NONCE,
            new OpcUaServerConfigLimits() {
              @Override
              public UInteger getMaxPasswordLength() {
                return uint(4);
              }
            });

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session,
                    fixture.token(),
                    policy(profile.securityPolicy()),
                    new SignatureData(null, null)));

    assertUniformEncryptedTokenFailure(exception);
    assertEquals(0, validator.authenticateCount);
  }

  // The enhanced path must not disturb the long-standing unencrypted username token behavior.
  @Test
  void preservesUnencryptedUsernameTokenPath() throws Exception {
    TestUsernameValidator validator = new TestUsernameValidator("password");
    Session session = mock(Session.class);
    when(session.getLastNonce()).thenReturn(ByteString.NULL_VALUE);
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null));

    Identity identity =
        validator.validateIdentityToken(
            session,
            new UserNameIdentityToken(
                "username",
                "user",
                ByteString.of("password".getBytes(StandardCharsets.UTF_8)),
                null),
            policy(SecurityPolicy.None),
            new SignatureData(null, null));

    assertEquals("user", ((Identity.UsernameIdentity) identity).getUsername());
  }

  // Malformed RSA ciphertext can decrypt to zero plaintext blocks; token validation must fail with
  // a controlled service error before reading the four-byte length prefix.
  @Test
  void rejectsZeroLengthLegacyPlaintext() {
    assertMalformedLegacyPlaintextRejected(new byte[0]);
  }

  // A partial length prefix is equally malformed and must not reach unchecked byte indexing.
  @Test
  void rejectsTooShortLegacyPlaintext() {
    assertMalformedLegacyPlaintextRejected(new byte[] {0x01, 0x02, 0x03});
  }

  // The new length guard still allows well-formed legacy username plaintext to authenticate.
  @Test
  void acceptsValidLegacyPlaintext() throws Exception {
    FixedPlaintextUsernameValidator validator =
        new FixedPlaintextUsernameValidator("password", legacyPlaintext("password", SERVER_NONCE));

    Identity identity =
        validator.validateIdentityToken(
            legacyEncryptedSession(),
            legacyEncryptedToken(),
            policy(SecurityPolicy.Basic256Sha256),
            new SignatureData(null, null));

    assertEquals("user", ((Identity.UsernameIdentity) identity).getUsername());
  }

  @Test
  void emptyLegacyPasswordWithoutNonceReturnsIdentityTokenInvalid() {
    RecordingPlaintextUsernameValidator validator =
        new RecordingPlaintextUsernameValidator(legacyPlaintext("", ByteString.NULL_VALUE), true);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    legacyEncryptedSession(),
                    legacyEncryptedToken(),
                    policy(SecurityPolicy.Basic256Sha256),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void emptyLegacyPasswordWithWrongNonceReturnsIdentityTokenInvalid() {
    RecordingPlaintextUsernameValidator validator =
        new RecordingPlaintextUsernameValidator(
            legacyPlaintext("", ByteString.of(new byte[SERVER_NONCE.length()])), true);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    legacyEncryptedSession(),
                    legacyEncryptedToken(),
                    policy(SecurityPolicy.Basic256Sha256),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void wrongLegacyPasswordWithValidNonceReturnsIdentityTokenInvalid() {
    RecordingPlaintextUsernameValidator validator =
        new RecordingPlaintextUsernameValidator(legacyPlaintext("wrong", SERVER_NONCE), false);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    legacyEncryptedSession(),
                    legacyEncryptedToken(),
                    policy(SecurityPolicy.Basic256Sha256),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
    assertEquals("user", validator.username);
    assertEquals("wrong", validator.password);
    assertEquals(1, validator.authenticateCount);
  }

  @Test
  void legacyPasswordWithValidNonceAuthenticates() throws Exception {
    RecordingPlaintextUsernameValidator validator =
        new RecordingPlaintextUsernameValidator(legacyPlaintext("password", SERVER_NONCE), true);

    Identity identity =
        validator.validateIdentityToken(
            legacyEncryptedSession(),
            legacyEncryptedToken(),
            policy(SecurityPolicy.Basic256Sha256),
            new SignatureData(null, null));

    assertEquals("user", ((Identity.UsernameIdentity) identity).getUsername());
    assertEquals("user", validator.username);
    assertEquals("password", validator.password);
    assertEquals(1, validator.authenticateCount);
  }

  @Test
  void unencryptedEmptyPasswordStillUsesAuthenticationResult() {
    RecordingPlaintextUsernameValidator validator =
        new RecordingPlaintextUsernameValidator(new byte[0], false);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    noneSession(),
                    new UserNameIdentityToken("username", "user", ByteString.of(new byte[0]), null),
                    policy(SecurityPolicy.None),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_UserAccessDenied, exception.getStatusCode().getValue());
    assertEquals("user", validator.username);
    assertEquals("", validator.password);
    assertEquals(1, validator.authenticateCount);
  }

  private static TokenFixture tokenFixture(SecurityPolicyProfile profile) throws Exception {
    return tokenFixture(profile, "password");
  }

  private static TokenFixture tokenFixture(SecurityPolicyProfile profile, String password)
      throws Exception {
    ApplicationIdentity clientIdentity = applicationIdentity(profile);

    return tokenFixture(profile, password, clientIdentity);
  }

  private static TokenFixture tokenFixture(
      SecurityPolicyProfile profile, String password, ApplicationIdentity clientIdentity)
      throws Exception {

    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());
    ByteString encryptedSecret =
        EccEncryptedSecret.encrypt(
            profile,
            clientIdentity.keyPair(),
            new X509Certificate[] {clientIdentity.certificate()},
            receiverPublicKey,
            SERVER_NONCE,
            ByteString.of(password.getBytes(StandardCharsets.UTF_8)),
            false);

    return new TokenFixture(
        clientIdentity.certificate(),
        receiverEphemeralKeyPair,
        receiverPublicKey,
        new UserNameIdentityToken(
            "username", "user", encryptedSecret, profile.securityPolicy().getUri()));
  }

  private static Session session(TokenFixture fixture, ByteString lastNonce) {
    return session(fixture, lastNonce, new OpcUaServerConfigLimits() {});
  }

  private static Session session(
      TokenFixture fixture, ByteString lastNonce, OpcUaServerConfigLimits limits) {
    Session session = mock(Session.class);
    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getLimits()).thenReturn(limits);
    when(server.getConfig()).thenReturn(config);
    when(session.getServer()).thenReturn(server);
    when(session.getLastNonce()).thenReturn(lastNonce);
    when(session.getUserTokenEphemeralKeyPair())
        .thenReturn(Optional.of(fixture.receiverEphemeralKeyPair()));
    when(session.getUserTokenEphemeralPublicKey())
        .thenReturn(Optional.of(fixture.receiverPublicKey()));
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                fixture.token().getEncryptionAlgorithm() != null
                    ? SecurityPolicy.fromUriSafe(fixture.token().getEncryptionAlgorithm())
                        .orElse(SecurityPolicy.None)
                    : SecurityPolicy.None,
                MessageSecurityMode.SignAndEncrypt,
                null,
                null,
                null,
                fixture.clientCertificate(),
                List.of(fixture.clientCertificate())));

    return session;
  }

  private static Session session(
      TokenFixture fixture, ByteString lastNonce, MutableSessionKey mutableKey) {

    Session session = session(fixture, lastNonce);
    when(session.getUserTokenEphemeralKeyPair()).thenAnswer(ignored -> mutableKey.keyPair());
    when(session.getUserTokenEphemeralPublicKey()).thenAnswer(ignored -> mutableKey.publicKey());
    doAnswer(
            ignored -> {
              mutableKey.clear();
              return null;
            })
        .when(session)
        .clearUserTokenEphemeralKeyPair();

    return session;
  }

  private static Session legacyEncryptedSession() {
    Session session = mock(Session.class);
    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});
    when(server.getConfig()).thenReturn(config);
    when(session.getServer()).thenReturn(server);
    when(session.getLastNonce()).thenReturn(SERVER_NONCE);
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                SecurityPolicy.Basic256Sha256,
                MessageSecurityMode.SignAndEncrypt,
                null,
                null,
                null,
                null,
                null));

    return session;
  }

  private static Session noneSession() {
    Session session = mock(Session.class);
    when(session.getLastNonce()).thenReturn(ByteString.NULL_VALUE);
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null));

    return session;
  }

  private static Session encryptedSession() {
    return session(SecurityPolicy.Basic256Sha256);
  }

  private static Session encryptedSession(SecurityPolicy securityPolicy) throws Exception {
    CertificateManager certificateManager = mock(CertificateManager.class);
    when(certificateManager.getKeyPair(any())).thenReturn(Optional.of(rsaKeyPair));

    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getCertificateManager()).thenReturn(certificateManager);
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});
    when(server.getConfig()).thenReturn(config);

    EndpointDescription endpoint = mock(EndpointDescription.class);
    when(endpoint.getServerCertificate()).thenReturn(ByteString.of(rsaCertificate.getEncoded()));

    Session session = mock(Session.class);
    SecurityConfiguration securityConfiguration =
        new SecurityConfiguration(
            securityPolicy, MessageSecurityMode.SignAndEncrypt, null, null, null, null, null);
    when(session.getEndpoint()).thenReturn(endpoint);
    when(session.getLastNonce()).thenReturn(SERVER_NONCE);
    when(session.getServer()).thenReturn(server);
    when(session.getSecurityConfiguration()).thenReturn(securityConfiguration);
    // Built before the stubbing call: of() reads the endpoint mock.
    SessionServerCertificate originalServerCertificate =
        SessionServerCertificate.of(endpoint, securityConfiguration);
    when(session.getOriginalServerCertificate()).thenReturn(originalServerCertificate);
    return session;
  }

  private static Session session(SecurityPolicy securityPolicy) {
    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});
    when(server.getConfig()).thenReturn(config);

    Session session = mock(Session.class);
    when(session.getLastNonce()).thenReturn(SERVER_NONCE);
    when(session.getServer()).thenReturn(server);
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                securityPolicy, MessageSecurityMode.SignAndEncrypt, null, null, null, null, null));
    return session;
  }

  private static UserNameIdentityToken encryptedToken() {
    return encryptedToken(SecurityPolicy.Basic256Sha256, new byte[0]);
  }

  private static UserNameIdentityToken encryptedToken(
      SecurityPolicy securityPolicy, byte[] ciphertext) {
    SecurityAlgorithm algorithm = securityPolicy.getAsymmetricEncryptionAlgorithm();

    return new UserNameIdentityToken(
        "username", USERNAME, ByteString.of(ciphertext), algorithm.getUri());
  }

  private static byte[] encrypt(SecurityPolicy securityPolicy, byte[] plainText) throws Exception {
    SecurityAlgorithm algorithm = securityPolicy.getAsymmetricEncryptionAlgorithm();
    Cipher cipher = CipherFactory.createForEncryption(algorithm, rsaCertificate.getPublicKey());
    return cipher.doFinal(plainText);
  }

  private static byte[] zeroedCopy(byte[] bytes) {
    byte[] copy = bytes.clone();
    Arrays.fill(copy, (byte) 0);
    return copy;
  }

  private static void assertUniformEncryptedTokenFailure(UaException exception) {
    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
    assertNull(exception.getCause());
  }

  private static byte[] tokenData(String password, byte[] nonce) {
    byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
    byte[] lengthBytes = uint32LittleEndian(passwordBytes.length + nonce.length);

    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      os.write(lengthBytes);
      os.write(passwordBytes);
      os.write(nonce);
      return os.toByteArray();
    } catch (IOException e) {
      throw new AssertionError(e);
    } finally {
      Arrays.fill(passwordBytes, (byte) 0);
    }
  }

  private static byte[] uint32LittleEndian(int value) {
    return new byte[] {
      (byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24)
    };
  }

  private static void assertMalformedLegacyPlaintextRejected(byte[] plaintext) {
    FixedPlaintextUsernameValidator validator =
        new FixedPlaintextUsernameValidator("password", plaintext);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    legacyEncryptedSession(),
                    legacyEncryptedToken(),
                    policy(SecurityPolicy.Basic256Sha256),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
  }

  private static UserNameIdentityToken legacyEncryptedToken() {
    return new UserNameIdentityToken("username", "user", ByteString.of(new byte[] {0x01}), null);
  }

  private static byte[] legacyPlaintext(String password, ByteString nonce) {
    byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
    byte[] nonceBytes = nonce.bytesOrEmpty();
    int length = passwordBytes.length + nonceBytes.length;
    byte[] plainText = new byte[Integer.BYTES + length];

    plainText[0] = (byte) length;
    plainText[1] = (byte) (length >>> 8);
    plainText[2] = (byte) (length >>> 16);
    plainText[3] = (byte) (length >>> 24);
    System.arraycopy(passwordBytes, 0, plainText, Integer.BYTES, passwordBytes.length);
    System.arraycopy(
        nonceBytes, 0, plainText, Integer.BYTES + passwordBytes.length, nonceBytes.length);

    return plainText;
  }

  private static MutableSessionKey mutableSessionKey(TokenFixture fixture) {
    return new MutableSessionKey(
        new AtomicReference<>(fixture.receiverEphemeralKeyPair()),
        new AtomicReference<>(fixture.receiverPublicKey()));
  }

  private static UserTokenPolicy policy(SecurityPolicy securityPolicy) {
    return new UserTokenPolicy(
        "username", UserTokenType.UserName, null, null, securityPolicy.getUri());
  }

  private static Stream<Arguments> representativeEnhancedProfiles() {
    return Stream.of(
        Arguments.of(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()),
        Arguments.of(SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile()),
        Arguments.of(SecurityPolicy.RSA_DH_AesGcm.getProfile()));
  }

  private static ApplicationIdentity applicationIdentity(SecurityPolicyProfile profile)
      throws Exception {

    KeyPair keyPair =
        switch (profile.authAxis()) {
          case ECDSA_NIST_P256_SHA256 -> SelfSignedCertificateGenerator.generateNistP256KeyPair();
          case ECDSA_NIST_P384_SHA384 -> SelfSignedCertificateGenerator.generateNistP384KeyPair();
          case ECDSA_BRAINPOOL_P256R1_SHA256 ->
              SelfSignedCertificateGenerator.generateBrainpoolP256r1KeyPair();
          case ECDSA_BRAINPOOL_P384R1_SHA384 ->
              SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();
          case ED25519 -> SelfSignedCertificateGenerator.generateEd25519KeyPair();
          case ED448 -> SelfSignedCertificateGenerator.generateEd448KeyPair();
          case RSA_PKCS1_SHA256 -> SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
          default ->
              throw new IllegalArgumentException("unsupported auth axis: " + profile.authAxis());
        };

    X509Certificate certificate =
        certificateBuilder(profile, keyPair)
            .setCommonName("client-" + profile.securityPolicy().name())
            .setApplicationUri("urn:eclipse:milo:test:client")
            .addDnsName("localhost")
            .build();

    return new ApplicationIdentity(keyPair, certificate);
  }

  private static SelfSignedCertificateBuilder certificateBuilder(
      SecurityPolicyProfile profile, KeyPair keyPair) {

    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256,
          ECDSA_NIST_P384_SHA384,
          ECDSA_BRAINPOOL_P256R1_SHA256,
          ECDSA_BRAINPOOL_P384R1_SHA384,
          ED25519,
          ED448 ->
          SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair);
      case RSA_PKCS1_SHA256 -> new SelfSignedCertificateBuilder(keyPair);
      default -> throw new IllegalArgumentException("unsupported auth axis: " + profile.authAxis());
    };
  }

  private static class TestUsernameValidator extends AbstractUsernameIdentityValidator {

    private final String expectedPassword;
    private int authenticateCount;

    private TestUsernameValidator(String expectedPassword) {
      this.expectedPassword = expectedPassword;
    }

    @Override
    protected Identity.UsernameIdentity authenticateUsernamePassword(
        Session session, String username, String password) {

      authenticateCount++;
      return expectedPassword.equals(password) ? new DefaultUsernameIdentity(username) : null;
    }
  }

  private static final class FixedPlaintextUsernameValidator extends TestUsernameValidator {

    private final byte[] plaintext;

    private FixedPlaintextUsernameValidator(String expectedPassword, byte[] plaintext) {
      super(expectedPassword);
      this.plaintext = plaintext;
    }

    @Override
    protected byte[] decryptTokenData(
        Session session, SecurityAlgorithm algorithm, byte[] dataBytes) {

      return plaintext;
    }
  }

  private static final class RecordingPlaintextUsernameValidator
      extends AbstractUsernameIdentityValidator {

    private final byte[] plaintext;
    private final boolean authenticate;

    private int authenticateCount;
    private @Nullable String username;
    private @Nullable String password;

    private RecordingPlaintextUsernameValidator(byte[] plaintext, boolean authenticate) {
      this.plaintext = plaintext;
      this.authenticate = authenticate;
    }

    @Override
    protected byte[] decryptTokenData(
        Session session, SecurityAlgorithm algorithm, byte[] dataBytes) {

      return plaintext;
    }

    @Override
    protected @Nullable UsernameIdentity authenticateUsernamePassword(
        Session session, String username, String password) {

      authenticateCount++;
      this.username = username;
      this.password = password;

      return authenticate ? new DefaultUsernameIdentity(username) : null;
    }
  }

  private static final class TestUsernameIdentityValidator
      extends AbstractUsernameIdentityValidator {

    private final byte[] tokenData;
    private final boolean authenticate;

    private int authenticateCount;
    private @Nullable String username;
    private @Nullable String password;

    private TestUsernameIdentityValidator(byte[] tokenData, boolean authenticate) {
      this.tokenData = tokenData;
      this.authenticate = authenticate;
    }

    private UsernameIdentity validate(Session session, UserNameIdentityToken token)
        throws UaException {
      return validateUsernameToken(session, token, ENCRYPTED_POLICY, NO_SIGNATURE);
    }

    @Override
    protected byte[] decryptTokenData(
        Session session, SecurityAlgorithm algorithm, byte[] dataBytes) {

      return tokenData;
    }

    @Override
    protected @Nullable UsernameIdentity authenticateUsernamePassword(
        Session session, String username, String password) {

      authenticateCount++;
      this.username = username;
      this.password = password;

      return authenticate ? new DefaultUsernameIdentity(username) : null;
    }
  }

  private static final class CryptoUsernameIdentityValidator
      extends AbstractUsernameIdentityValidator {

    private final boolean authenticate;

    private int decryptCount;
    private int authenticateCount;

    private CryptoUsernameIdentityValidator(boolean authenticate) {
      this.authenticate = authenticate;
    }

    private UsernameIdentity validate(Session session, UserNameIdentityToken token)
        throws UaException {
      UserTokenPolicy policy =
          new UserTokenPolicy(
              "username",
              UserTokenType.UserName,
              null,
              null,
              session.getSecurityConfiguration().getSecurityPolicy().getUri());

      return validateUsernameToken(session, token, policy, NO_SIGNATURE);
    }

    @Override
    protected byte[] decryptTokenData(
        Session session, SecurityAlgorithm algorithm, byte[] dataBytes) throws UaException {
      decryptCount++;
      return super.decryptTokenData(session, algorithm, dataBytes);
    }

    @Override
    protected @Nullable UsernameIdentity authenticateUsernamePassword(
        Session session, String username, String password) {
      authenticateCount++;
      return authenticate ? new DefaultUsernameIdentity(username) : null;
    }
  }

  private record ApplicationIdentity(KeyPair keyPair, X509Certificate certificate) {}

  private static final class MutableSessionKey {

    private final AtomicReference<KeyPair> keyPair;
    private final AtomicReference<ByteString> publicKey;

    private MutableSessionKey(
        AtomicReference<KeyPair> keyPair, AtomicReference<ByteString> publicKey) {
      this.keyPair = keyPair;
      this.publicKey = publicKey;
    }

    private Optional<KeyPair> keyPair() {
      return Optional.ofNullable(keyPair.get());
    }

    private Optional<ByteString> publicKey() {
      return Optional.ofNullable(publicKey.get());
    }

    private void set(TokenFixture fixture) {
      keyPair.set(fixture.receiverEphemeralKeyPair());
      publicKey.set(fixture.receiverPublicKey());
    }

    private void clear() {
      keyPair.set(null);
      publicKey.set(null);
    }
  }

  private record TokenFixture(
      X509Certificate clientCertificate,
      KeyPair receiverEphemeralKeyPair,
      ByteString receiverPublicKey,
      UserNameIdentityToken token) {}

  private static final byte[] ENHANCED_SECRET_LABEL =
      "opcua-secret".getBytes(StandardCharsets.UTF_8);

  private static TokenFixture withSecret(TokenFixture base, ByteString secret) {
    UserNameIdentityToken original = base.token();
    return new TokenFixture(
        base.clientCertificate(),
        base.receiverEphemeralKeyPair(),
        base.receiverPublicKey(),
        new UserNameIdentityToken(
            original.getPolicyId(),
            original.getUserName(),
            secret,
            original.getEncryptionAlgorithm()));
  }

  private static void assertEnhancedDecryptFailsWith(
      SecurityPolicyProfile profile,
      TokenFixture fixture,
      ByteString encodedSecret,
      long expectedStatusCode) {
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccEncryptedSecret.decrypt(
                    profile,
                    fixture.receiverEphemeralKeyPair(),
                    fixture.receiverPublicKey(),
                    fixture.clientCertificate(),
                    SERVER_NONCE,
                    encodedSecret));

    assertEquals(expectedStatusCode, exception.getStatusCode().value());
  }

  private static ByteString rewriteEnhancedSecret(
      SecurityPolicyProfile profile,
      ApplicationIdentity sender,
      KeyPair receiverEphemeralKeyPair,
      ByteString encryptedSecret,
      Consumer<ParsedEnhancedSecretParts> headerMutator,
      UnaryOperator<byte[]> plaintextMutator)
      throws Exception {

    ParsedEnhancedSecretParts parts = parseEnhancedSecretParts(profile, encryptedSecret);
    byte[] bytes = Arrays.copyOf(parts.bytes(), parts.bytes().length);
    EnhancedSecretKeyMaterial keyMaterial =
        deriveEnhancedSecretKeyMaterial(profile, receiverEphemeralKeyPair, parts);

    byte[] originalHeader = Arrays.copyOfRange(bytes, 0, parts.encryptedPayloadStart());
    byte[] originalCiphertext =
        Arrays.copyOfRange(bytes, parts.encryptedPayloadStart(), parts.signatureStart());
    Cipher decryptCipher =
        initEnhancedAeadCipher(profile, Cipher.DECRYPT_MODE, keyMaterial, originalHeader);
    byte[] plainText = decryptCipher.doFinal(originalCiphertext);

    ParsedEnhancedSecretParts mutableParts =
        new ParsedEnhancedSecretParts(
            bytes,
            parts.signingTimeIndex(),
            parts.encryptedPayloadStart(),
            parts.signatureStart(),
            parts.senderPublicKey(),
            parts.receiverPublicKey());
    headerMutator.accept(mutableParts);

    byte[] newHeader = Arrays.copyOfRange(bytes, 0, parts.encryptedPayloadStart());
    Cipher encryptCipher =
        initEnhancedAeadCipher(profile, Cipher.ENCRYPT_MODE, keyMaterial, newHeader);
    byte[] newEncryptedPayload = encryptCipher.doFinal(plaintextMutator.apply(plainText));
    assertEquals(
        parts.signatureStart() - parts.encryptedPayloadStart(), newEncryptedPayload.length);

    System.arraycopy(
        newEncryptedPayload, 0, bytes, parts.encryptedPayloadStart(), newEncryptedPayload.length);

    byte[] signature =
        signEnhanced(
            profile,
            sender.keyPair().getPrivate(),
            ByteBuffer.wrap(bytes, 0, parts.signatureStart()));
    System.arraycopy(signature, 0, bytes, parts.signatureStart(), signature.length);

    return ByteString.of(bytes);
  }

  private static byte[] corruptEnhancedPayloadPadding(byte[] plainText) {
    byte[] corrupted = Arrays.copyOf(plainText, plainText.length);
    int paddingSize = corrupted[corrupted.length - 2] & 0xFF;

    if (paddingSize > 0) {
      corrupted[corrupted.length - 2 - paddingSize] ^= 0x01;
    } else {
      corrupted[corrupted.length - 1] = 0x01;
    }

    return corrupted;
  }

  private static ParsedEnhancedSecretParts parseEnhancedSecretParts(
      SecurityPolicyProfile profile, ByteString encryptedSecret) {
    byte[] bytes = encryptedSecret.bytesOrEmpty();
    ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
    OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    decoder.setBuffer(buffer);

    decoder.decodeNodeId();
    decoder.decodeByte(null);
    decoder.decodeUInt32();
    decoder.decodeString();
    ByteString embeddedCert = decoder.decodeByteString();
    int signingTimeIndex = buffer.readerIndex();
    decoder.decodeDateTime();
    decoder.decodeUInt16();
    ByteString senderPublicKey = decoder.decodeByteString();
    ByteString receiverPublicKey = decoder.decodeByteString();
    int encryptedPayloadStart = buffer.readerIndex();

    int sigLength = enhancedSignatureLength(profile, embeddedCert);
    int signatureStart = bytes.length - sigLength;

    return new ParsedEnhancedSecretParts(
        bytes,
        signingTimeIndex,
        encryptedPayloadStart,
        signatureStart,
        senderPublicKey,
        receiverPublicKey);
  }

  private static int enhancedSignatureLength(
      SecurityPolicyProfile profile, ByteString embeddedCert) {
    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256, ECDSA_BRAINPOOL_P256R1_SHA256 ->
          EccSignatureUtil.ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH;
      case ECDSA_NIST_P384_SHA384, ECDSA_BRAINPOOL_P384R1_SHA384 ->
          EccSignatureUtil.ECDSA_P384_SHA384_P1363_SIGNATURE_LENGTH;
      case ED25519 -> EccSignatureUtil.ED25519_SIGNATURE_LENGTH;
      case ED448 -> EccSignatureUtil.ED448_SIGNATURE_LENGTH;
      case RSA_PKCS1_SHA256 -> {
        try {
          X509Certificate cert = CertificateUtil.decodeCertificate(embeddedCert.bytesOrEmpty());
          RSAPublicKey rsaKey = (RSAPublicKey) cert.getPublicKey();
          yield (rsaKey.getModulus().bitLength() + 7) / 8;
        } catch (UaException e) {
          throw new AssertionError(e);
        }
      }
      default -> throw new IllegalArgumentException("unsupported auth axis: " + profile.authAxis());
    };
  }

  private static byte[] signEnhanced(
      SecurityPolicyProfile profile, PrivateKey privateKey, ByteBuffer data) throws UaException {
    if (profile.authAxis() == SecurityPolicyProfile.AuthAxis.RSA_PKCS1_SHA256) {
      return SignatureUtil.sign(SecurityAlgorithm.RsaSha256, privateKey, data);
    }
    return EccSignatureUtil.sign(profile, privateKey, data);
  }

  private static Cipher initEnhancedAeadCipher(
      SecurityPolicyProfile profile,
      int mode,
      EnhancedSecretKeyMaterial keyMaterial,
      byte[] headerBytes)
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
      default ->
          throw new IllegalArgumentException(
              "unsupported chunk axis: " + profile.chunkProtectionAxis());
    };
  }

  private static EnhancedSecretKeyMaterial deriveEnhancedSecretKeyMaterial(
      SecurityPolicyProfile profile,
      KeyPair receiverEphemeralKeyPair,
      ParsedEnhancedSecretParts parts)
      throws UaException {
    ProviderProfile providerProfile = SecurityProviderResolver.create().resolve(profile);
    PublicKey senderEphemeralPublicKey =
        switch (profile.keyAgreementAxis()) {
          case ECDH_NIST_P256 ->
              EccPublicKeyCodec.decodeNistP256(parts.senderPublicKey(), providerProfile);
          case ECDH_NIST_P384 ->
              EccPublicKeyCodec.decodeNistP384(parts.senderPublicKey(), providerProfile);
          case ECDH_BRAINPOOL_P256R1 ->
              EccPublicKeyCodec.decodeBrainpoolP256r1(parts.senderPublicKey(), providerProfile);
          case ECDH_BRAINPOOL_P384R1 ->
              EccPublicKeyCodec.decodeBrainpoolP384r1(parts.senderPublicKey(), providerProfile);
          case X25519 -> EccPublicKeyCodec.decodeX25519(parts.senderPublicKey(), providerProfile);
          case X448 -> EccPublicKeyCodec.decodeX448(parts.senderPublicKey(), providerProfile);
          case FFDH_3072 ->
              FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(
                  parts.senderPublicKey(), providerProfile);
          default ->
              throw new IllegalArgumentException(
                  "unsupported key agreement axis: " + profile.keyAgreementAxis());
        };
    byte[] sharedSecret =
        switch (profile.keyAgreementAxis()) {
          case ECDH_NIST_P256 ->
              EccKeyAgreementUtil.agreeNistP256(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          case ECDH_NIST_P384 ->
              EccKeyAgreementUtil.agreeNistP384(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          case ECDH_BRAINPOOL_P256R1 ->
              EccKeyAgreementUtil.agreeBrainpoolP256r1(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          case ECDH_BRAINPOOL_P384R1 ->
              EccKeyAgreementUtil.agreeBrainpoolP384r1(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          case X25519 ->
              EccKeyAgreementUtil.agreeX25519(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          case X448 ->
              EccKeyAgreementUtil.agreeX448(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          case FFDH_3072 ->
              FiniteFieldDhKeyAgreementUtil.agreeFfdhe3072(
                  providerProfile, receiverEphemeralKeyPair.getPrivate(), senderEphemeralPublicKey);
          default ->
              throw new IllegalArgumentException(
                  "unsupported key agreement axis: " + profile.keyAgreementAxis());
        };

    int totalLength =
        profile.symmetricEncryptionKeySize()
            + EccKeyAgreementUtil.AEAD_INITIALIZATION_VECTOR_LENGTH;
    byte[] senderBytes = parts.senderPublicKey().bytesOrEmpty();
    byte[] receiverBytes = parts.receiverPublicKey().bytesOrEmpty();
    byte[] salt =
        new byte[2 + ENHANCED_SECRET_LABEL.length + senderBytes.length + receiverBytes.length];
    salt[0] = (byte) (totalLength & 0xFF);
    salt[1] = (byte) ((totalLength >>> 8) & 0xFF);
    System.arraycopy(ENHANCED_SECRET_LABEL, 0, salt, 2, ENHANCED_SECRET_LABEL.length);
    System.arraycopy(senderBytes, 0, salt, 2 + ENHANCED_SECRET_LABEL.length, senderBytes.length);
    System.arraycopy(
        receiverBytes,
        0,
        salt,
        2 + ENHANCED_SECRET_LABEL.length + senderBytes.length,
        receiverBytes.length);

    byte[] keyMaterial =
        switch (profile.keyAgreementAxis()) {
          case ECDH_NIST_P384, ECDH_BRAINPOOL_P384R1, X448 ->
              HkdfUtil.hkdfSha384(sharedSecret, salt, salt, totalLength);
          case ECDH_NIST_P256, ECDH_BRAINPOOL_P256R1, X25519, FFDH_3072 ->
              HkdfUtil.hkdfSha256(sharedSecret, salt, salt, totalLength);
          default ->
              throw new IllegalArgumentException(
                  "unsupported key agreement axis: " + profile.keyAgreementAxis());
        };

    return new EnhancedSecretKeyMaterial(
        Arrays.copyOfRange(keyMaterial, 0, profile.symmetricEncryptionKeySize()),
        Arrays.copyOfRange(keyMaterial, profile.symmetricEncryptionKeySize(), totalLength));
  }

  private record EnhancedSecretKeyMaterial(byte[] encryptionKey, byte[] initializationVector) {}

  private record ParsedEnhancedSecretParts(
      byte[] bytes,
      int signingTimeIndex,
      int encryptedPayloadStart,
      int signatureStart,
      ByteString senderPublicKey,
      ByteString receiverPublicKey) {}
}
