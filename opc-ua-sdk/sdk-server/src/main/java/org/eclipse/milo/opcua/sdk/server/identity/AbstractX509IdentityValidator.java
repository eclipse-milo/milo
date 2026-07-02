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

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.SecurityConfiguration;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.Identity.X509UserIdentity;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.security.UserTokenSecurityPolicyRules;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
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

    SecurityPolicy securityPolicy = resolveSecurityPolicy(session, policy);

    if (securityPolicy == SecurityPolicy.None) {
      // A Certificate user token must prove possession of the private key. A None-resolved policy
      // carries no signature, so reject it rather than authenticating on certificate trust alone.
      throw new UaException(
          StatusCodes.Bad_IdentityTokenInvalid,
          "certificate user token requires a security policy other than None");
    }

    // Reject an enhanced (ECC/RSA-DH) user-token policy carried over a legacy secured channel
    // cleanly, before attempting verification (Part 4 §6.1.8 Table 101).
    ChannelBoundSignatureData.checkUserTokenChannelCompatibility(
        securityPolicy.getProfile(), session.getSecurityConfiguration().getSecurityPolicy());

    validateSignatureAlgorithm(securityPolicy, signature);

    try {
      verifySignature(session, securityPolicy, signature, certificate);
    } catch (UaException e) {
      throw new UaException(
          StatusCodes.Bad_IdentityTokenInvalid,
          "signature verification failed: " + e.getMessage(),
          e);
    }

    return authenticateCertificateOrThrow(session, certificate);
  }

  /**
   * Resolve the effective user-token security policy: the policy's own SecurityPolicy URI when set,
   * otherwise the SecureChannel's SecurityPolicy (Part 4 §7.41).
   */
  private static SecurityPolicy resolveSecurityPolicy(Session session, UserTokenPolicy policy)
      throws UaException {

    String securityPolicyUri = policy.getSecurityPolicyUri();
    SecurityPolicy securityPolicy;

    if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
      securityPolicy = session.getSecurityConfiguration().getSecurityPolicy();
    } else {
      securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);
    }

    UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
        session.getSecurityConfiguration().getSecurityMode(),
        session.getSecurityConfiguration().getSecurityPolicy(),
        securityPolicy,
        securityPolicyUri != null && !securityPolicyUri.isEmpty());

    return securityPolicy;
  }

  /**
   * Validate the {@code SignatureData.algorithm} URI against the user-token policy. Legacy policies
   * carry the URI on the wire and it must match the policy's algorithm; enhanced policies (ECC,
   * RSA-DH) leave it empty and the algorithm is implied by the SecurityPolicy (Part 4 §7.36), so
   * the URI is not consulted.
   */
  private static void validateSignatureAlgorithm(
      SecurityPolicy securityPolicy, SignatureData signature) throws UaException {

    if (securityPolicy.isLegacy()
        && !securityPolicy
            .getAsymmetricSignatureAlgorithm()
            .getUri()
            .equals(signature.getAlgorithm())) {

      throw new UaException(
          StatusCodes.Bad_IdentityTokenInvalid,
          "algorithm in token signature did not match algorithm specified by token policy");
    }
  }

  private X509UserIdentity authenticateCertificateOrThrow(
      Session session, X509Certificate certificate) throws UaException {

    X509UserIdentity identity = authenticateCertificate(session, certificate);

    if (identity != null) {
      return identity;
    } else {
      throw new UaException(StatusCodes.Bad_IdentityTokenRejected);
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
      SecurityPolicy securityPolicy,
      SignatureData tokenSignature,
      X509Certificate certificate)
      throws UaException {

    SecurityPolicyProfile profile = securityPolicy.getProfile();
    SecurityConfiguration sc = session.getSecurityConfiguration();

    // Select the layout from whether the channel actually binds (a thumbprint exists), keeping the
    // server discriminator on the same physical signal as the client. The compatibility of an
    // enhanced user-token policy with a legacy secured channel was already checked in
    // validateX509Token.
    ByteString channelThumbprint = sc.getChannelThumbprint();
    boolean secureChannelSecured = !channelThumbprint.isNullOrEmpty();

    // Hoist the candidate-invariant inputs out of the loop: only the CreateSession server
    // certificate varies across candidates. Milo uses one application certificate for both the
    // CreateSession ClientCertificate and the channel ClientChannelCertificate, so encode it once
    // and fill both Table 101 slots.
    ByteString serverChannelCertificate = sc.getServerChannelCertificateBytes();
    ByteString clientCertificate = sc.getClientCertificateBytes();
    ByteString serverNonce = session.getLastNonce();
    ByteString clientNonce = session.getClientNonce();

    // The hashed/signed "ServerCertificate" is the CreateSession server certificate. Depending on
    // the peer and SecurityMode this is the endpoint (leaf) bytes, the server application
    // certificate bytes, or the full chain bytes; try each candidate to match a chain-returning
    // peer. A forged signature verifies against none of them, so trying several is safe.
    List<ByteString> serverCertificateCandidates = new ArrayList<>();
    addCandidate(serverCertificateCandidates, session.getEndpoint().getServerCertificate());
    addCandidate(serverCertificateCandidates, sc.getServerCertificateBytes());
    addCandidate(serverCertificateCandidates, sc.getServerCertificateChainBytes());

    UaException failure = null;

    for (ByteString serverCertificate : serverCertificateCandidates) {
      byte[] dataBytes =
          ChannelBoundSignatureData.userTokenSignatureData(
              profile,
              secureChannelSecured,
              channelThumbprint,
              serverNonce,
              serverCertificate,
              serverChannelCertificate,
              clientCertificate,
              clientCertificate,
              clientNonce);

      try {
        // ECC policies verify with ECDSA/EdDSA; RSA-DH and legacy policies with the policy's
        // algorithm. validateSignatureAlgorithm already rejected a legacy wire URI that disagrees
        // with the policy.
        ChannelBoundSignatureData.verify(securityPolicy, certificate, dataBytes, tokenSignature);
        return;
      } catch (UaException e) {
        failure = e;
      }
    }

    throw failure != null
        ? failure
        : new UaException(
            StatusCodes.Bad_SecurityChecksFailed, "no server certificate available to verify");
  }

  private static void addCandidate(List<ByteString> candidates, @Nullable ByteString candidate) {
    if (candidate != null && !candidate.isNullOrEmpty() && !candidates.contains(candidate)) {
      candidates.add(candidate);
    }
  }
}
