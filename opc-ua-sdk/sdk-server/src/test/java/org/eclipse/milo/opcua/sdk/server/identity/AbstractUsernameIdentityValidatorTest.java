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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.SecurityConfiguration;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.UsernameIdentity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class AbstractUsernameIdentityValidatorTest {

  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";
  private static final ByteString SERVER_NONCE =
      ByteString.of(
          new byte[] {
            0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, 26, 27, 28, 29, 30, 31
          });
  private static final UserTokenPolicy ENCRYPTED_POLICY =
      new UserTokenPolicy(
          "username", UserTokenType.UserName, null, null, SecurityPolicy.Basic256Sha256.getUri());
  private static final SignatureData NO_SIGNATURE = new SignatureData(null, null);

  @Test
  void emptyEncryptedPasswordWithoutNonceReturnsIdentityTokenInvalid() {
    TestUsernameIdentityValidator validator =
        new TestUsernameIdentityValidator(tokenData("", new byte[0]), true);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
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
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void shortEncryptedTokenDataReturnsIdentityTokenInvalid() {
    TestUsernameIdentityValidator validator = new TestUsernameIdentityValidator(new byte[0], true);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().value());
    assertEquals(0, validator.authenticateCount);
  }

  @Test
  void wrongEncryptedPasswordWithValidNonceReturnsUserAccessDenied() {
    TestUsernameIdentityValidator validator =
        new TestUsernameIdentityValidator(tokenData("wrong", SERVER_NONCE.bytesOrEmpty()), false);

    UaException exception =
        assertThrows(
            UaException.class, () -> validator.validate(encryptedSession(), encryptedToken()));

    assertEquals(StatusCodes.Bad_UserAccessDenied, exception.getStatusCode().value());
    assertEquals(USERNAME, validator.username);
    assertEquals("wrong", validator.password);
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

  @Test
  void unencryptedEmptyPasswordStillUsesAuthenticationResult() {
    TestUsernameIdentityValidator validator = new TestUsernameIdentityValidator(new byte[0], false);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validate(
                    noneSession(),
                    new UserNameIdentityToken(
                        "username", USERNAME, ByteString.of(new byte[0]), null)));

    assertEquals(StatusCodes.Bad_UserAccessDenied, exception.getStatusCode().value());
    assertEquals(USERNAME, validator.username);
    assertEquals("", validator.password);
    assertEquals(1, validator.authenticateCount);
  }

  private static Session encryptedSession() {
    return session(SecurityPolicy.Basic256Sha256);
  }

  private static Session noneSession() {
    return session(SecurityPolicy.None);
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
    SecurityAlgorithm algorithm = SecurityPolicy.Basic256Sha256.getAsymmetricEncryptionAlgorithm();

    return new UserNameIdentityToken(
        "username", USERNAME, ByteString.of(new byte[0]), algorithm.getUri());
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
}
