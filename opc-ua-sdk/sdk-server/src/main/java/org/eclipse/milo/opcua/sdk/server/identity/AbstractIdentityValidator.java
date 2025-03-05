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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.AnonymousIdentity;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.IssuedIdentity;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.UsernameIdentity;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.X509UserIdentity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.IssuedIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.DigestUtil;

public abstract class AbstractIdentityValidator implements IdentityValidator {

  @Override
  public Identity validateIdentityToken(
      Session session, UserIdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException {

    return switch (policy.getTokenType()) {
      case Anonymous -> {
        if (token instanceof AnonymousIdentityToken) {
          yield validateAnonymousToken(session, (AnonymousIdentityToken) token, policy, signature);
        } else {
          throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
        }
      }
      case UserName -> {
        if (token instanceof UserNameIdentityToken) {
          yield validateUsernameToken(session, (UserNameIdentityToken) token, policy, signature);
        } else {
          throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
        }
      }
      case Certificate -> {
        if (token instanceof X509IdentityToken) {
          yield validateX509Token(session, (X509IdentityToken) token, policy, signature);
        } else {
          throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
        }
      }
      case IssuedToken -> {
        if (token instanceof IssuedIdentityToken) {
          yield validateIssuedIdentityToken(
              session, (IssuedIdentityToken) token, policy, signature);
        } else {
          throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
        }
      }
    };
  }

  /**
   * Validate an {@link AnonymousIdentityToken} and return an {@link AnonymousIdentity} that
   * represents the user.
   *
   * <p>This Identity should implement equality in such a way that different instances representing
   * the same user are comparable.
   *
   * @param session the {@link Session} making the request.
   * @param token the {@link AnonymousIdentityToken}.
   * @param policy the {@link UserTokenPolicy} specified by the policyId in {@code token}.
   * @param signature the {@link SignatureData} sent in the {@link ActivateSessionRequest}.
   * @return an {@link AnonymousIdentity} that represents the user identity.
   * @throws UaException if the token is invalid, rejected, or user access is denied.
   */
  protected AnonymousIdentity validateAnonymousToken(
      Session session,
      AnonymousIdentityToken token,
      UserTokenPolicy policy,
      SignatureData signature)
      throws UaException {

    throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
  }

  /**
   * Validate an {@link UserNameIdentityToken} and return a {@link UsernameIdentity} that represents
   * the user.
   *
   * <p>This Identity should implement equality in such a way that different instances representing
   * the same user are comparable.
   *
   * @param session the {@link Session} making the request.
   * @param token the {@link UserNameIdentityToken}.
   * @param policy the {@link UserTokenPolicy} specified by the policyId in {@code token}.
   * @param signature the {@link SignatureData} sent in the {@link ActivateSessionRequest}.
   * @return a {@link UsernameIdentity} that represents the user identity.
   * @throws UaException if the token is invalid, rejected, or user access is denied.
   */
  protected UsernameIdentity validateUsernameToken(
      Session session, UserNameIdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException {

    throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
  }

  /**
   * Validate an {@link X509IdentityToken} and return an {@link X509UserIdentity} that represents
   * the user.
   *
   * <p>This Identity should implement equality in such a way that different instances representing
   * the same user are comparable.
   *
   * @param session the {@link Session} making the request.
   * @param token the {@link X509IdentityToken}.
   * @param policy the {@link UserTokenPolicy} specified by the policyId in {@code token}.
   * @param signature the {@link SignatureData} sent in the {@link ActivateSessionRequest}.
   * @return an {@link X509UserIdentity} that represents the user identity.
   * @throws UaException if the token is invalid, rejected, or user access is denied.
   */
  protected X509UserIdentity validateX509Token(
      Session session, X509IdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException {

    throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
  }

  /**
   * Validate an {@link IssuedIdentityToken} and return an {@link IssuedIdentity} that represents
   * the user.
   *
   * <p>This Identity should implement equality in such a way that different instances representing
   * the same user are comparable.
   *
   * @param session the {@link Session} making the request.
   * @param token the {@link IssuedIdentityToken}.
   * @param policy the {@link UserTokenPolicy} specified by the policyId in {@code token}.
   * @param signature the {@link SignatureData} sent in the {@link ActivateSessionRequest}.
   * @return an {@link IssuedIdentity} that represents the user identity.
   * @throws UaException if the token is invalid, rejected, or user access is denied.
   */
  protected IssuedIdentity validateIssuedIdentityToken(
      Session session, IssuedIdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException {

    throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
  }

  /**
   * Decrypt the data contained in a {@link UserNameIdentityToken} or {@link IssuedIdentityToken}.
   *
   * @param session the current {@link Session}.
   * @param dataBytes the encrypted data.
   * @return the decrypted data.
   * @throws UaException if decryption fails.
   * @see IssuedIdentityToken#getTokenData()
   * @see UserNameIdentityToken#getPassword()
   */
  protected byte[] decryptTokenData(Session session, SecurityAlgorithm algorithm, byte[] dataBytes)
      throws UaException {

    X509Certificate certificate =
        CertificateUtil.decodeCertificate(
            session.getEndpoint().getServerCertificate().bytesOrEmpty());

    int cipherTextBlockSize =
        SecureChannel.getAsymmetricCipherTextBlockSize(certificate, algorithm);
    int blockCount = dataBytes.length / cipherTextBlockSize;

    int plainTextBufferSize = cipherTextBlockSize * blockCount;

    byte[] plainTextBytes = new byte[plainTextBufferSize];
    ByteBuffer plainTextNioBuffer = ByteBuffer.wrap(plainTextBytes);
    ByteBuffer passwordNioBuffer = ByteBuffer.wrap(dataBytes);

    try {
      KeyPair keyPair =
          session
              .getServer()
              .getConfig()
              .getCertificateManager()
              .getKeyPair(ByteString.of(DigestUtil.sha1(certificate.getEncoded())))
              .orElseThrow(() -> new UaException(StatusCodes.Bad_SecurityChecksFailed));

      Cipher cipher = getCipher(algorithm, keyPair);

      for (int blockNumber = 0; blockNumber < blockCount; blockNumber++) {
        ((Buffer) passwordNioBuffer).limit(passwordNioBuffer.position() + cipherTextBlockSize);

        cipher.doFinal(passwordNioBuffer, plainTextNioBuffer);
      }
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }

    return plainTextBytes;
  }

  private Cipher getCipher(SecurityAlgorithm algorithm, KeyPair keyPair) throws UaException {
    try {
      String transformation = algorithm.getTransformation();
      Cipher cipher = Cipher.getInstance(transformation);
      cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
      return cipher;
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
    }
  }
}
