/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static java.util.Objects.requireNonNullElse;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;

/**
 * An {@link IdentityProvider} that authenticates with a certificate user-token policy.
 *
 * <p>The provider sends the configured certificate chain as the user identity token and signs the
 * server certificate plus nonce when the selected user-token security policy requires a signature.
 * Certificate-token policies do not use the ECC username-secret additional-header exchange, even
 * when the certificate-token policy itself uses an ECC security policy.
 */
public class X509IdentityProvider implements IdentityProvider {

  private final List<X509Certificate> certificateChain =
      Collections.synchronizedList(new ArrayList<>());

  private final Supplier<PrivateKey> privateKeySupplier;

  /**
   * Construct an {@link X509IdentityProvider} with a single certificate.
   *
   * @param certificate the {@link X509Certificate} to authenticate with.
   * @param privateKey the {@link PrivateKey} corresponding to the certificate.
   */
  public X509IdentityProvider(X509Certificate certificate, PrivateKey privateKey) {
    this(certificate, () -> privateKey);
  }

  /**
   * Construct an {@link X509IdentityProvider} with a certificate chain.
   *
   * @param certificateChain the certificate chain to authenticate with.
   * @param privateKey the {@link PrivateKey} corresponding to the certificate.
   */
  public X509IdentityProvider(List<X509Certificate> certificateChain, PrivateKey privateKey) {
    this(certificateChain, () -> privateKey);
  }

  /**
   * Construct an {@link X509IdentityProvider} with a single certificate.
   *
   * @param certificate the {@link X509Certificate} to authenticate with.
   * @param privateKeySupplier a supplier providing the {@link PrivateKey} corresponding to the
   *     certificate.
   */
  public X509IdentityProvider(
      X509Certificate certificate, Supplier<PrivateKey> privateKeySupplier) {
    this.privateKeySupplier = privateKeySupplier;

    certificateChain.add(certificate);
  }

  /**
   * Construct an {@link X509IdentityProvider} with a certificate chain.
   *
   * @param certificateChain the certificate chain to authenticate with.
   * @param privateKeySupplier a supplier providing the {@link PrivateKey} corresponding to the
   *     certificate.
   */
  public X509IdentityProvider(
      List<X509Certificate> certificateChain, Supplier<PrivateKey> privateKeySupplier) {
    this.privateKeySupplier = privateKeySupplier;

    this.certificateChain.addAll(certificateChain);
  }

  @Override
  public Optional<SecurityPolicy> getUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    UserTokenPolicy tokenPolicy = selectTokenPolicy(endpoint);

    return Optional.of(resolveSecurityPolicy(endpoint, tokenPolicy));
  }

  @Override
  public Optional<SecurityPolicy> getEccUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    selectTokenPolicy(endpoint);

    return Optional.empty();
  }

  @Override
  public SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception {

    UserTokenPolicy tokenPolicy = selectTokenPolicy(endpoint);
    SecurityPolicy securityPolicy = resolveSecurityPolicy(endpoint, tokenPolicy);

    X509IdentityToken token =
        new X509IdentityToken(
            tokenPolicy.getPolicyId(), CertificateUtil.getCertificateChainBytes(certificateChain));

    SignatureData signatureData;

    if (securityPolicy == SecurityPolicy.None) {
      signatureData = new SignatureData(null, null);
    } else {
      NonceUtil.validateNonce(serverNonce);

      List<X509Certificate> serverCertificates =
          CertificateUtil.decodeCertificates(endpoint.getServerCertificate().bytesOrEmpty());
      byte[] serverCertificateBytes = serverCertificates.get(0).getEncoded();

      byte[] serverNonceBytes = serverNonce.bytes();
      if (serverNonceBytes == null) serverNonceBytes = new byte[0];

      byte[] signature =
          SignatureUtil.sign(
              securityPolicy.getAsymmetricSignatureAlgorithm(),
              privateKeySupplier.get(),
              ByteBuffer.wrap(serverCertificateBytes),
              ByteBuffer.wrap(serverNonceBytes));

      signatureData =
          new SignatureData(
              securityPolicy.getAsymmetricSignatureAlgorithm().getUri(), ByteString.of(signature));
    }

    return new SignedIdentityToken(token, signatureData);
  }

  private static UserTokenPolicy selectTokenPolicy(EndpointDescription endpoint) throws Exception {
    UserTokenPolicy[] userIdentityTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    return Stream.of(userIdentityTokens)
        .filter(t -> t.getTokenType() == UserTokenType.Certificate)
        .findFirst()
        .orElseThrow(() -> new Exception("no x509 certificate token policy found"));
  }

  private static SecurityPolicy resolveSecurityPolicy(
      EndpointDescription endpoint, UserTokenPolicy tokenPolicy) throws UaException {

    String securityPolicyUri = tokenPolicy.getSecurityPolicyUri();

    try {
      if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
        securityPolicyUri = endpoint.getSecurityPolicyUri();
      }
      return SecurityPolicy.fromUri(securityPolicyUri);
    } catch (Throwable t) {
      throw new UaException(StatusCodes.Bad_SecurityPolicyRejected, t);
    }
  }
}
