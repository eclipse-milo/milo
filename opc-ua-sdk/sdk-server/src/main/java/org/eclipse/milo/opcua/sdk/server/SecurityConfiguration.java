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

public final class SecurityConfiguration {

  private final SecurityPolicy securityPolicy;
  private final MessageSecurityMode securityMode;
  private final KeyPair keyPair;
  private final X509Certificate serverCertificate;
  private final List<X509Certificate> serverCertificateChain;
  private final X509Certificate clientCertificate;
  private final List<X509Certificate> clientCertificateChain;

  public SecurityConfiguration(
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      @Nullable KeyPair keyPair,
      @Nullable X509Certificate serverCertificate,
      @Nullable List<X509Certificate> serverCertificateChain,
      @Nullable X509Certificate clientCertificate,
      @Nullable List<X509Certificate> clientCertificateChain) {

    this.securityPolicy = securityPolicy;
    this.securityMode = securityMode;
    this.keyPair = keyPair;
    this.serverCertificate = serverCertificate;
    this.serverCertificateChain = serverCertificateChain;
    this.clientCertificate = clientCertificate;
    this.clientCertificateChain = clientCertificateChain;
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
