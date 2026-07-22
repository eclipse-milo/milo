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
import org.slf4j.LoggerFactory;

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
   * Get the usable certificate identities belonging to this group.
   *
   * <p>An identity is usable when the group has both a non-empty certificate chain and a key pair
   * for the certificate type.
   *
   * <p>The certificate chain and the key pair are read from the backing store independently, so a
   * concurrent {@link #updateCertificate} can interleave between the two reads and pair the old
   * chain with the rotated key pair. Such a mismatch is detected by comparing the resolved key
   * pair's public key against the leaf certificate's public key; mismatched entries are omitted
   * rather than emitted as a {@link CertificateIdentity} that violates its own public-key
   * invariant.
   *
   * @return the usable certificate identities belonging to this group.
   */
  default List<CertificateIdentity> getCertificateIdentities() {
    return getCertificateEntries().stream()
        .filter(entry -> entry.certificateChain != null && entry.certificateChain.length > 0)
        .flatMap(
            entry ->
                getKeyPair(entry.certificateTypeId)
                    .filter(
                        keyPair -> {
                          boolean matches =
                              keyPair.getPublic().equals(entry.certificateChain[0].getPublicKey());
                          if (!matches) {
                            // A rotation raced between the chain and key-pair reads; omit the
                            // entry so callers never observe a mispaired identity. The condition
                            // is transient and self-healing on the next read.
                            LoggerFactory.getLogger(CertificateGroup.class)
                                .warn(
                                    "Omitting certificate identity for certificateTypeId={}: key"
                                        + " pair public key does not match leaf certificate public"
                                        + " key (likely a concurrent certificate rotation)",
                                    entry.certificateTypeId);
                          }
                          return matches;
                        })
                    .map(
                        keyPair ->
                            new CertificateIdentity(
                                entry.certificateGroupId,
                                entry.certificateTypeId,
                                keyPair,
                                entry.certificateChain))
                    .stream())
        .sorted(CertificateIdentityOrdering.STABLE)
        .toList();
  }

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
  @SuppressWarnings("ClassCanBeRecord")
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
