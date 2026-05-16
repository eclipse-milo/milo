/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static java.util.Objects.requireNonNull;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

/**
 * A usable local application certificate identity selected from a {@link CertificateManager}.
 *
 * <p>The identity keeps the certificate group and certificate type together with the key pair and
 * certificate chain so endpoint advertisement and SecureChannel setup can use the same selected
 * material.
 *
 * @param certificateGroupId the certificate group containing this identity.
 * @param certificateTypeId the certificate type represented by this identity.
 * @param keyPair the key pair belonging to the leaf certificate.
 * @param certificateChain the leaf certificate and any issuer certificates.
 */
public record CertificateIdentity(
    NodeId certificateGroupId,
    NodeId certificateTypeId,
    KeyPair keyPair,
    X509Certificate[] certificateChain) {

  public CertificateIdentity {
    requireNonNull(certificateGroupId, "certificateGroupId");
    requireNonNull(certificateTypeId, "certificateTypeId");
    requireNonNull(keyPair, "keyPair");
    requireNonNull(certificateChain, "certificateChain");

    if (certificateChain.length == 0) {
      throw new IllegalArgumentException("certificateChain must not be empty");
    }

    certificateChain = certificateChain.clone();
  }

  @Override
  public X509Certificate[] certificateChain() {
    return certificateChain.clone();
  }

  /**
   * Get the leaf certificate for this identity.
   *
   * @return the leaf certificate.
   */
  public X509Certificate certificate() {
    return certificateChain[0];
  }

  /**
   * Get the SHA-1 certificate thumbprint used by OPC UA asymmetric security headers.
   *
   * @return the SHA-1 thumbprint of the leaf certificate.
   * @throws UaException if the certificate cannot be encoded.
   */
  public ByteString thumbprint() throws UaException {
    return CertificateUtil.thumbprint(certificate());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CertificateIdentity that)) {
      return false;
    }
    return Objects.equals(certificateGroupId, that.certificateGroupId)
        && Objects.equals(certificateTypeId, that.certificateTypeId)
        && Objects.equals(keyPair, that.keyPair)
        && Arrays.equals(certificateChain, that.certificateChain);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(certificateGroupId, certificateTypeId, keyPair);
    result = 31 * result + Arrays.hashCode(certificateChain);
    return result;
  }
}
