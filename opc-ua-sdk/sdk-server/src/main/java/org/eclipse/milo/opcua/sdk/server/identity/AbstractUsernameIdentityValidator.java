/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.AnonymousIdentity;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.UsernameIdentity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;

public abstract class AbstractUsernameIdentityValidator extends AbstractIdentityValidator {

  @Override
  protected AnonymousIdentity validateAnonymousToken(
      Session session,
      AnonymousIdentityToken token,
      UserTokenPolicy policy,
      SignatureData signature)
      throws UaException {

    return authenticateAnonymousOrThrow(session);
  }

  @Override
  protected UsernameIdentity validateUsernameToken(
      Session session, UserNameIdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException {

    String username = token.getUserName();
    ByteString lastNonce = session.getLastNonce();
    int lastNonceLength = lastNonce.length();

    if (username == null || username.isEmpty()) {
      throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
    }

    SecurityAlgorithm algorithm;

    String algorithmUri = token.getEncryptionAlgorithm();

    if (algorithmUri == null || algorithmUri.isEmpty()) {
      SecurityPolicy securityPolicy = session.getSecurityConfiguration().getSecurityPolicy();

      algorithm = securityPolicy.getAsymmetricEncryptionAlgorithm();
    } else {
      try {
        algorithm = SecurityAlgorithm.fromUri(algorithmUri);
      } catch (UaException e) {
        throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
      }

      SecurityPolicy securityPolicy = SecurityPolicy.fromUri(policy.getSecurityPolicyUri());

      // Don't allow the Client to specify a different algorithm than the one defined by the
      // SecurityPolicy in the UserTokenPolicy.
      if (!securityPolicy.getAsymmetricEncryptionAlgorithm().equals(algorithm)) {
        throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
      }
    }

    byte[] tokenBytes = token.getPassword().bytesOrEmpty();

    if (algorithm != SecurityAlgorithm.None) {
      byte[] plainTextBytes = decryptTokenData(session, algorithm, tokenBytes);

      // @formatter:off
      long length =
          ((plainTextBytes[3] & 0xFFL) << 24)
              | ((plainTextBytes[2] & 0xFFL) << 16)
              | ((plainTextBytes[1] & 0xFFL) << 8)
              | (plainTextBytes[0] & 0xFFL);
      // @formatter:on

      if (length > plainTextBytes.length - 4) {
        throw new UaException(StatusCodes.Bad_IdentityTokenInvalid, "invalid token data");
      }

      int passwordLength = (int) length - lastNonceLength;

      if (passwordLength < 0) {
        throw new UaException(StatusCodes.Bad_IdentityTokenInvalid, "invalid password length");
      }

      if (passwordLength
          > session.getServer().getConfig().getLimits().getMaxPasswordLength().longValue()) {
        throw new UaException(
            StatusCodes.Bad_EncodingLimitsExceeded, "password length exceeds limits");
      }

      byte[] passwordBytes = new byte[passwordLength];
      byte[] nonceBytes = new byte[lastNonceLength];

      System.arraycopy(plainTextBytes, 4, passwordBytes, 0, passwordBytes.length);
      System.arraycopy(plainTextBytes, 4 + passwordBytes.length, nonceBytes, 0, lastNonceLength);

      if (MessageDigest.isEqual(lastNonce.bytes(), nonceBytes)) {
        String password = new String(passwordBytes, StandardCharsets.UTF_8);

        return authenticateUsernameOrThrow(session, username, password);
      } else {
        throw new UaException(StatusCodes.Bad_UserAccessDenied);
      }
    } else {
      String password = new String(tokenBytes, StandardCharsets.UTF_8);

      return authenticateUsernameOrThrow(session, username, password);
    }
  }

  private AnonymousIdentity authenticateAnonymousOrThrow(Session session) throws UaException {
    AnonymousIdentity identity = authenticateAnonymous(session);

    if (identity != null) {
      return identity;
    } else {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }
  }

  private UsernameIdentity authenticateUsernameOrThrow(
      Session session, String username, String password) throws UaException {
    UsernameIdentity identity = authenticateUsernamePassword(session, username, password);

    if (identity != null) {
      return identity;
    } else {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }
  }

  /**
   * Create and return an {@link AnonymousIdentity} for the anonymous user, or {@code null} if
   * anonymous authentication is not allowed.
   *
   * @param session the {@link Session} being activated.
   * @return an {@link AnonymousIdentity}, or {@code null} if anonymous authentication is not
   *     allowed.
   */
  protected abstract @Nullable AnonymousIdentity authenticateAnonymous(Session session);

  /**
   * Authenticate {@code username} with {@code password}, returning a {@link UsernameIdentity} if
   * authentication succeeded, or {@code null} if the authentication failed.
   *
   * @param session the {@link Session} being activated.
   * @param username the username to authenticate.
   * @param password the password to authenticate the user with.
   * @return a {@link UsernameIdentity} if the authentication succeeded, or {@code null} if it
   *     failed.
   */
  protected abstract @Nullable UsernameIdentity authenticateUsernamePassword(
      Session session, String username, String password);
}
