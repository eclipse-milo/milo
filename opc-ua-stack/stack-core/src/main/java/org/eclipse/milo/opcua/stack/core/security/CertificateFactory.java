/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.asn1.x500.X500Name;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public interface CertificateFactory {

  /**
   * Create a {@link KeyPair} for the certificate of the type identified by {@code
   * certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @return the new {@link KeyPair}.
   */
  KeyPair createKeyPair(NodeId certificateTypeId) throws Exception;

  /**
   * Create a {@link X509Certificate} chain for the certificate of the type identified by {@code
   * certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @param keyPair the {@link KeyPair} to use when creating the certificate chain.
   * @return the new {@link X509Certificate} chain.
   */
  X509Certificate[] createCertificateChain(NodeId certificateTypeId, KeyPair keyPair)
      throws Exception;

  /**
   * Create a PKCS10 certificate signing request for the certificate of the type identified by
   * {@code certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @param keyPair the {@link KeyPair} to use when creating the signing request.
   * @param subjectName the {@link X500Name} to request.
   * @param sanUri the URI to request in the Subject Alternative Name of the CSR.
   * @param dnsNames the DNS names to request in the Subject Alternative Name of the CSR.
   * @param ipAddresses the IP addresses to request in the Subject Alternative Name of the CSR.
   * @return the new {@link ByteString} containing the DER-encoded PKCS10 signing request.
   * @throws Exception if an error occurs while creating the signing request.
   */
  ByteString createSigningRequest(
      NodeId certificateTypeId,
      KeyPair keyPair,
      X500Name subjectName,
      String sanUri,
      List<String> dnsNames,
      List<String> ipAddresses)
      throws Exception;
}
