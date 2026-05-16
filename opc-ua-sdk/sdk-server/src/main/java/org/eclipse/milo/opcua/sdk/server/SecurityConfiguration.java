/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import com.google.common.primitives.Bytes;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.Nullable;

/**
 * Session-facing view of the SecureChannel security material used by the server SDK.
 *
 * <p>CreateSession and ActivateSession validation need a stable snapshot of the policy,
 * certificates, key pair, and SecureChannel-enhancement thumbprint that were active on the channel
 * carrying the service request. Reassociation can present a different channel from the one that
 * originally created the session, so callers pass the candidate configuration into signature
 * verification before moving the session onto that channel.
 */
public final class SecurityConfiguration {

  private final SecurityPolicy securityPolicy;
  private final MessageSecurityMode securityMode;
  private final KeyPair keyPair;
  private final X509Certificate serverCertificate;
  private final List<X509Certificate> serverCertificateChain;
  private final X509Certificate clientCertificate;
  private final List<X509Certificate> clientCertificateChain;
  private final ByteString channelThumbprint;

  public SecurityConfiguration(
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      @Nullable KeyPair keyPair,
      @Nullable X509Certificate serverCertificate,
      @Nullable List<X509Certificate> serverCertificateChain,
      @Nullable X509Certificate clientCertificate,
      @Nullable List<X509Certificate> clientCertificateChain) {

    this(
        securityPolicy,
        securityMode,
        keyPair,
        serverCertificate,
        serverCertificateChain,
        clientCertificate,
        clientCertificateChain,
        ByteString.NULL_VALUE);
  }

  public SecurityConfiguration(
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      @Nullable KeyPair keyPair,
      @Nullable X509Certificate serverCertificate,
      @Nullable List<X509Certificate> serverCertificateChain,
      @Nullable X509Certificate clientCertificate,
      @Nullable List<X509Certificate> clientCertificateChain,
      ByteString channelThumbprint) {

    this.securityPolicy = securityPolicy;
    this.securityMode = securityMode;
    this.keyPair = keyPair;
    this.serverCertificate = serverCertificate;
    this.serverCertificateChain = serverCertificateChain;
    this.clientCertificate = clientCertificate;
    this.clientCertificateChain = clientCertificateChain;
    this.channelThumbprint = channelThumbprint;
  }

  public SecurityPolicy getSecurityPolicy() {
    return securityPolicy;
  }

  public MessageSecurityMode getSecurityMode() {
    return securityMode;
  }

  @Nullable
  public KeyPair getKeyPair() {
    return keyPair;
  }

  @Nullable
  public X509Certificate getServerCertificate() {
    return serverCertificate;
  }

  @Nullable
  public List<X509Certificate> getServerCertificateChain() {
    return serverCertificateChain;
  }

  @Nullable
  public X509Certificate getClientCertificate() {
    return clientCertificate;
  }

  @Nullable
  public List<X509Certificate> getClientCertificateChain() {
    return clientCertificateChain;
  }

  public ByteString getClientCertificateBytes() throws UaException {
    return getCertificateBytes(getClientCertificate());
  }

  public ByteString getClientCertificateChainBytes() throws UaException {
    return getCertificateChainBytes(getClientCertificateChain());
  }

  public ByteString getServerCertificateBytes() throws UaException {
    return getCertificateBytes(getServerCertificate());
  }

  public ByteString getServerCertificateChainBytes() throws UaException {
    return getCertificateChainBytes(getServerCertificateChain());
  }

  /**
   * Returns the server certificate bytes from the SecureChannel context.
   *
   * <p>For OPC UA TCP this is currently the endpoint leaf certificate. The explicit channel name
   * keeps session signature code aligned with the SecureChannel-enhancement terminology, where the
   * channel certificate and endpoint certificate may be reasoned about separately.
   *
   * @return the encoded server channel certificate, or {@link ByteString#NULL_VALUE}.
   * @throws UaException if the certificate cannot be encoded.
   */
  public ByteString getServerChannelCertificateBytes() throws UaException {
    return getServerCertificateBytes();
  }

  /**
   * Returns the client certificate bytes from the SecureChannel context.
   *
   * @return the encoded client channel certificate, or {@link ByteString#NULL_VALUE}.
   * @throws UaException if the certificate cannot be encoded.
   */
  public ByteString getClientChannelCertificateBytes() throws UaException {
    return getClientCertificateBytes();
  }

  /**
   * Returns the SecureChannel-enhancement thumbprint used by channel-bound session signatures.
   *
   * @return the active channel thumbprint, or {@link ByteString#NULL_VALUE}.
   */
  public ByteString getChannelThumbprint() {
    return channelThumbprint;
  }

  private static ByteString getCertificateBytes(@Nullable X509Certificate certificate)
      throws UaException {

    if (certificate == null) {
      return ByteString.NULL_VALUE;
    } else {
      try {
        return ByteString.of(certificate.getEncoded());
      } catch (CertificateEncodingException e) {
        throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
      }
    }
  }

  private static ByteString getCertificateChainBytes(
      @Nullable List<X509Certificate> certificateChain) throws UaException {

    if (certificateChain == null) {
      return ByteString.NULL_VALUE;
    } else {
      List<byte[]> certificates = new ArrayList<>(certificateChain.size());

      for (X509Certificate certificate : certificateChain) {
        try {
          certificates.add(certificate.getEncoded());
        } catch (CertificateEncodingException e) {
          throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
        }
      }

      byte[] encoded = certificates.stream().reduce(new byte[0], Bytes::concat);

      return ByteString.of(encoded);
    }
  }
}
