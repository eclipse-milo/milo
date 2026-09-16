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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * The server application certificate a {@link Session} was created with.
 *
 * <p>Part 4 5.7.3.1 computes ActivateSession client signatures over the serverCertificate returned
 * by CreateSession and encrypts token secrets to its public key for the lifetime of the Session, so
 * these inputs must outlive a server certificate rotation and reactivation on a replacement
 * SecureChannel. A Session created on an unsecured channel has no certificate or key pair.
 *
 * @param createSessionCertificate the serverCertificate returned by CreateSession, i.e. the
 *     endpoint's advertised certificate, or {@link ByteString#NULL_VALUE}.
 * @param certificate the server application certificate of the creating channel, or null.
 * @param certificateChain the server certificate chain of the creating channel, or null.
 * @param keyPair the key pair for {@code certificate}, or null.
 */
public record SessionServerCertificate(
    ByteString createSessionCertificate,
    @Nullable X509Certificate certificate,
    @Nullable List<X509Certificate> certificateChain,
    @Nullable KeyPair keyPair) {

  /**
   * Capture the server certificate a Session is being created with.
   *
   * @param endpoint the endpoint CreateSession was received on.
   * @param securityConfiguration the security configuration of the creating SecureChannel.
   * @return the CreateSession server certificate and its key material.
   */
  public static SessionServerCertificate of(
      EndpointDescription endpoint, SecurityConfiguration securityConfiguration) {

    return new SessionServerCertificate(
        Objects.requireNonNullElse(endpoint.getServerCertificate(), ByteString.NULL_VALUE),
        securityConfiguration.getServerCertificate(),
        securityConfiguration.getServerCertificateChain(),
        securityConfiguration.getKeyPair());
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
