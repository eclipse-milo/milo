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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.SecurityConfiguration;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.Nullable;
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

    assertThrows(
        UaException.class,
        () ->
            validator.validateIdentityToken(
                session,
                fixture.token(),
                policy(profile.securityPolicy()),
                new SignatureData(null, null)));
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

    assertThrows(
        UaException.class,
        () ->
            validator.validateIdentityToken(
                session(fixture, SERVER_NONCE),
                malformedToken,
                policy(profile.securityPolicy()),
                new SignatureData(null, null)));
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

    assertEquals(StatusCodes.Bad_UserAccessDenied, exception.getStatusCode().getValue());
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

    assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, exception.getStatusCode().getValue());
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
  void wrongLegacyPasswordWithValidNonceReturnsUserAccessDenied() {
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

    assertEquals(StatusCodes.Bad_UserAccessDenied, exception.getStatusCode().getValue());
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

    private TestUsernameValidator(String expectedPassword) {
      this.expectedPassword = expectedPassword;
    }

    @Override
    protected Identity.UsernameIdentity authenticateUsernamePassword(
        Session session, String username, String password) {

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
    protected @Nullable Identity.UsernameIdentity authenticateUsernamePassword(
        Session session, String username, String password) {

      authenticateCount++;
      this.username = username;
      this.password = password;

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
}
