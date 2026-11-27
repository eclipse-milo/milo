/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.jspecify.annotations.Nullable;

/**
 * The server application certificate a {@link Session} was created with.
 *
 * <p>Part 4 §5.7.3.1 computes ActivateSession client signatures over the serverCertificate returned
 * by CreateSession and encrypts token secrets to its public key for the lifetime of the Session, so
 * these inputs must outlive a server certificate rotation and reactivation on a replacement
 * SecureChannel. When the creating channel is secured, the certificate, chain, and key pair come
 * from the channel's {@link SecurityConfiguration}. When the creating channel is unsecured ({@code
 * SecurityPolicy.None}) and CreateSession returns a server certificate, its certificate, chain, and
 * key pair are resolved from the {@link CertificateManager} at CreateSession time. A Session
 * created without a server certificate has no certificate, chain, or key pair.
 *
 * @param createSessionCertificate the serverCertificate returned by CreateSession, i.e. the
 *     endpoint's advertised certificate, or {@link ByteString#NULL_VALUE}.
 * @param certificate the server application certificate captured for the Session, or null.
 * @param certificateChain the server certificate chain captured for the Session, or null.
 * @param keyPair the key pair for {@code certificate}, or null.
 */
public record SessionServerCertificate(
    ByteString createSessionCertificate,
    @Nullable X509Certificate certificate,
    @Nullable List<X509Certificate> certificateChain,
    @Nullable KeyPair keyPair) {

  /**
   * Capture the server certificate a Session is being created with from the creating SecureChannel.
   *
   * @param endpoint the endpoint CreateSession was received on.
   * @param securityConfiguration the security configuration of the creating SecureChannel.
   * @return the CreateSession server certificate and any key material from the creating channel.
   */
  public static SessionServerCertificate of(
      EndpointDescription endpoint, SecurityConfiguration securityConfiguration) {

    return of(endpoint, securityConfiguration, null);
  }

  /**
   * Capture the server certificate a Session is being created with, resolving the advertised
   * certificate's key pair and chain from {@code certificateManager} when the creating
   * SecureChannel carries none (for example, on a {@code SecurityPolicy.None} endpoint whose user
   * token policy encrypts secrets).
   *
   * @param endpoint the endpoint CreateSession was received on.
   * @param securityConfiguration the security configuration of the creating SecureChannel.
   * @param certificateManager the server certificate manager used to resolve key material for the
   *     advertised certificate when the creating channel has none, or null.
   * @return the CreateSession server certificate and its captured key material.
   */
  public static SessionServerCertificate of(
      EndpointDescription endpoint,
      SecurityConfiguration securityConfiguration,
      @Nullable CertificateManager certificateManager) {

    ByteString createSessionCertificate =
        Objects.requireNonNullElse(endpoint.getServerCertificate(), ByteString.NULL_VALUE);
    X509Certificate certificate = securityConfiguration.getServerCertificate();
    List<X509Certificate> certificateChain = securityConfiguration.getServerCertificateChain();
    KeyPair keyPair = securityConfiguration.getKeyPair();

    if (keyPair == null
        && certificate == null
        && certificateManager != null
        && !createSessionCertificate.isNullOrEmpty()) {
      try {
        List<X509Certificate> decoded =
            CertificateUtil.decodeCertificates(createSessionCertificate.bytesOrEmpty());

        if (!decoded.isEmpty()) {
          X509Certificate leaf = decoded.get(0);
          ByteString thumbprint = CertificateUtil.thumbprint(leaf);
          KeyPair resolvedKeyPair = certificateManager.getKeyPair(thumbprint).orElse(null);

          if (resolvedKeyPair != null) {
            keyPair = resolvedKeyPair;
            certificate = certificateManager.getCertificate(thumbprint).orElse(leaf);
            certificateChain =
                certificateManager
                    .getCertificateChain(thumbprint)
                    .filter(chain -> chain.length > 0)
                    .map(List::of)
                    .orElseGet(() -> List.copyOf(decoded));
          }
        }
      } catch (UaException ignored) {
        // Leave channel key material absent when the advertised certificate cannot be decoded.
      }
    }

    return new SessionServerCertificate(
        createSessionCertificate, certificate, certificateChain, keyPair);
  }

  /**
   * @return the DER encoding of {@link #certificate()}, or {@link ByteString#NULL_VALUE}.
   * @throws UaException if the certificate cannot be encoded.
   */
  public ByteString certificateBytes() throws UaException {
    return SecurityConfiguration.getCertificateBytes(certificate);
  }

  /**
   * @return the concatenated DER encoding of {@link #certificateChain()}, or {@link
   *     ByteString#NULL_VALUE}.
   * @throws UaException if a certificate cannot be encoded.
   */
  public ByteString certificateChainBytes() throws UaException {
    return SecurityConfiguration.getCertificateChainBytes(certificateChain);
  }
}
