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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509IdentityProvider implements IdentityProvider {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<X509Certificate> certificateChain =
      Collections.synchronizedList(new ArrayList<>());

  private final PrivateKey privateKey;

  public X509IdentityProvider(X509Certificate certificate, PrivateKey privateKey) {
    this.privateKey = privateKey;

    certificateChain.add(certificate);
  }

  public X509IdentityProvider(List<X509Certificate> certificateChain, PrivateKey privateKey) {
    this.privateKey = privateKey;

    this.certificateChain.addAll(certificateChain);
  }

  @Override
  public SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception {

    UserTokenPolicy[] userIdentityTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);

    UserTokenPolicy tokenPolicy =
        Stream.of(userIdentityTokens)
            .filter(t -> t.getTokenType() == UserTokenType.Certificate)
            .findFirst()
            .orElseThrow(() -> new Exception("no x509 certificate token policy found"));

    SecurityPolicy securityPolicy;

    String securityPolicyUri = tokenPolicy.getSecurityPolicyUri();

    try {
      if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
        securityPolicyUri = endpoint.getSecurityPolicyUri();
      }
      securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);
    } catch (Throwable t) {
      throw new UaException(StatusCodes.Bad_SecurityPolicyRejected, t);
    }

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
              privateKey,
              ByteBuffer.wrap(serverCertificateBytes),
              ByteBuffer.wrap(serverNonceBytes));

      signatureData =
          new SignatureData(
              securityPolicy.getAsymmetricSignatureAlgorithm().getUri(), ByteString.of(signature));
    }

    return new SignedIdentityToken(token, signatureData);
  }
}
