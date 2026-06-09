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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.SecurityConfiguration;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
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

  private static TokenFixture tokenFixture(SecurityPolicyProfile profile) throws Exception {
    return tokenFixture(profile, "password");
  }

  private static TokenFixture tokenFixture(SecurityPolicyProfile profile, String password)
      throws Exception {
    ApplicationIdentity clientIdentity = applicationIdentity(profile);
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

  private static final class TestUsernameValidator extends AbstractUsernameIdentityValidator {

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

  private record ApplicationIdentity(KeyPair keyPair, X509Certificate certificate) {}

  private record TokenFixture(
      X509Certificate clientCertificate,
      KeyPair receiverEphemeralKeyPair,
      ByteString receiverPublicKey,
      UserNameIdentityToken token) {}
}
