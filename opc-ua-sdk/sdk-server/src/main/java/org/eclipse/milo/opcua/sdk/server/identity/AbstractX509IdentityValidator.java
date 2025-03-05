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

import com.google.common.primitives.Bytes;
import java.security.cert.X509Certificate;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.X509UserIdentity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.jspecify.annotations.Nullable;

public abstract class AbstractX509IdentityValidator extends AbstractIdentityValidator {

  @Override
  public Set<UserTokenType> getSupportedTokenTypes() {
    return Set.of(UserTokenType.Certificate);
  }

  @Override
  protected X509UserIdentity validateX509Token(
      Session session, X509IdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException {

    ByteString clientCertificateBs = token.getCertificateData();
    X509Certificate certificate =
        CertificateUtil.decodeCertificate(clientCertificateBs.bytesOrEmpty());

    // verify the algorithm matches the one specified by the tokenPolicy or else the channel itself
    if (policy.getSecurityPolicyUri() != null) {
      SecurityPolicy securityPolicy = SecurityPolicy.fromUri(policy.getSecurityPolicyUri());

      if (!securityPolicy
          .getAsymmetricSignatureAlgorithm()
          .getUri()
          .equals(signature.getAlgorithm())) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "algorithm in token signature did not match algorithm specified by token policy");
      }
    } else {
      SecurityPolicy securityPolicy = session.getSecurityConfiguration().getSecurityPolicy();

      if (!securityPolicy
          .getAsymmetricSignatureAlgorithm()
          .getUri()
          .equals(signature.getAlgorithm())) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "algorithm in token signature did not match algorithm specified by secure channel");
      }
    }

    SecurityAlgorithm algorithm = SecurityAlgorithm.fromUri(signature.getAlgorithm());

    if (algorithm != SecurityAlgorithm.None) {
      verifySignature(session, signature, certificate, algorithm);
    }

    return authenticateCertificateOrThrow(session, certificate);
  }

  private X509UserIdentity authenticateCertificateOrThrow(
      Session session, X509Certificate certificate) throws UaException {

    X509UserIdentity identity = authenticateCertificate(session, certificate);

    if (identity != null) {
      return identity;
    } else {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }
  }

  /**
   * Create and return an {@link X509UserIdentity} for the user identified by {@code certificate}.
   *
   * <p>Possession of the private key associated with this certificate has been verified prior to
   * this call.
   *
   * @param session the {@link Session} being activated.
   * @param certificate the {@link X509Certificate} identifying the user.
   * @return an {@link X509UserIdentity} if the authentication succeeded, or {@code null} if it
   *     failed.
   */
  protected abstract @Nullable X509UserIdentity authenticateCertificate(
      Session session, X509Certificate certificate);

  private static void verifySignature(
      Session session,
      SignatureData tokenSignature,
      X509Certificate certificate,
      SecurityAlgorithm algorithm)
      throws UaException {

    ByteString serverCertificateBs = session.getEndpoint().getServerCertificate();

    ByteString lastNonceBs = session.getLastNonce();

    try {
      byte[] dataBytes =
          Bytes.concat(serverCertificateBs.bytesOrEmpty(), lastNonceBs.bytesOrEmpty());
      byte[] signatureBytes = tokenSignature.getSignature().bytesOrEmpty();

      SignatureUtil.verify(algorithm, certificate, dataBytes, signatureBytes);
    } catch (UaException e) {
      // Maybe try again using the full certificate chain bytes instead

      ByteString serverCertificateChainBs =
          session.getSecurityConfiguration().getServerCertificateChainBytes();

      if (serverCertificateBs.equals(serverCertificateChainBs)) {
        throw e;
      } else {
        byte[] dataBytes =
            Bytes.concat(serverCertificateChainBs.bytesOrEmpty(), lastNonceBs.bytesOrEmpty());
        byte[] signatureBytes = tokenSignature.getSignature().bytesOrEmpty();

        SignatureUtil.verify(algorithm, certificate, dataBytes, signatureBytes);
      }
    }
  }
}
