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
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public interface CertificateGroup {

  /**
   * Get the {@link NodeId} identifying this {@link CertificateGroup}.
   *
   * @return the {@link NodeId} identifying this {@link CertificateGroup}.
   */
  NodeId getCertificateGroupId();

  /**
   * Get the {@link NodeId}s identifying the types of certificates supported by this {@link
   * CertificateGroup}.
   *
   * @return the {@link NodeId}s identifying the types of certificates supported by this {@link
   *     CertificateGroup}.
   */
  List<NodeId> getSupportedCertificateTypeIds();

  /**
   * Get the {@link TrustListManager} for this {@link CertificateGroup}.
   *
   * @return the {@link TrustListManager} for this {@link CertificateGroup}.
   */
  TrustListManager getTrustListManager();

  /**
   * Get the {@link Entry}s belonging to this {@link CertificateGroup}.
   *
   * @return the {@link Entry}s belonging to this {@link CertificateGroup}.
   */
  List<Entry> getCertificateEntries();

  /**
   * Get the {@link KeyPair} associated with the certificate of the type identified by {@code
   * certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @return the {@link KeyPair} associated with the certificate of the type identified by {@code
   *     certificateTypeId}.
   */
  Optional<KeyPair> getKeyPair(NodeId certificateTypeId);

  /**
   * Get the {@link X509Certificate} chain associated with the certificate of the type identified by
   * {@code certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @return the {@link X509Certificate} chain associated with the certificate of the type
   *     identified by {@code certificateTypeId}.
   */
  Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId);

  /**
   * Update the {@link KeyPair} and {@link X509Certificate} associated with the type identified by
   * {@code certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @param keyPair the new {@link KeyPair}.
   * @param certificateChain the new {@link X509Certificate} chain.
   * @throws Exception if the update fails.
   */
  void updateCertificate(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain)
      throws Exception;

  /**
   * Get the {@link CertificateFactory} for this {@link CertificateGroup}.
   *
   * @return the {@link CertificateFactory} for this {@link CertificateGroup}.
   */
  CertificateFactory getCertificateFactory();

  /**
   * Get the {@link CertificateValidator} for this {@link CertificateGroup}.
   *
   * @return the {@link CertificateValidator} for this {@link CertificateGroup}.
   */
  CertificateValidator getCertificateValidator();

  /** An entry describing a certificate and type belonging to a {@link CertificateGroup}. */
  class Entry {
    public final NodeId certificateGroupId;
    public final NodeId certificateTypeId;
    public final X509Certificate[] certificateChain;

    public Entry(
        NodeId certificateGroupId, NodeId certificateTypeId, X509Certificate[] certificateChain) {

      this.certificateGroupId = certificateGroupId;
      this.certificateTypeId = certificateTypeId;
      this.certificateChain = certificateChain;
    }
  }
}
