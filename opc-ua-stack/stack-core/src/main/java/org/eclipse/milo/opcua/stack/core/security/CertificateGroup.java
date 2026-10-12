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

/**
 * A unit of local identity and trust: the application certificates of one or more certificate
 * types, the trust list used to validate peers, the validator that applies it, and the quarantine
 * that holds what the validator rejected.
 *
 * <p>A group has no identity of its own. A server registers each group under the {@link NodeId} of
 * its {@code CertificateGroupType} node through a {@link CertificateManager}; a client configures
 * exactly one group and never names it.
 *
 * <p>Provisioning is not a group responsibility. Applications that want Milo to generate missing
 * self-signed material call {@link CertificateFactory#createMissingCertificates(CertificateGroup)}
 * after constructing the group.
 */
public interface CertificateGroup {

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
   * Get the {@link CertificateQuarantine} holding certificates this group's validator rejected.
   *
   * <p>A rejection is a per-group trust decision: a certificate rejected while validating against
   * this group's trust list lands here and nowhere else. A server-wide rejected list is the union
   * of the quarantines of every registered group.
   *
   * @return the {@link CertificateQuarantine} for this {@link CertificateGroup}.
   */
  CertificateQuarantine getCertificateQuarantine();

  /**
   * Get the {@link Entry}s belonging to this {@link CertificateGroup}.
   *
   * @return the {@link Entry}s belonging to this {@link CertificateGroup}.
   */
  List<Entry> getCertificateEntries();

  /**
   * Get the usable certificate identities belonging to this group, ordered by certificate type id.
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
        .filter(entry -> entry.certificateChain() != null && entry.certificateChain().length > 0)
        .flatMap(
            entry ->
                getKeyPair(entry.certificateTypeId())
                    .filter(
                        keyPair -> {
                          boolean matches =
                              keyPair
                                  .getPublic()
                                  .equals(entry.certificateChain()[0].getPublicKey());
                          if (!matches) {
                            // A rotation raced between the chain and key-pair reads; omit the
                            // entry so callers never observe a mispaired identity. The condition
                            // is transient and self-healing on the next read.
                            LoggerFactory.getLogger(CertificateGroup.class)
                                .warn(
                                    "Omitting certificate identity for certificateTypeId={}: key"
                                        + " pair public key does not match leaf certificate public"
                                        + " key (likely a concurrent certificate rotation)",
                                    entry.certificateTypeId());
                          }
                          return matches;
                        })
                    .map(
                        keyPair ->
                            new CertificateIdentity(
                                this, entry.certificateTypeId(), keyPair, entry.certificateChain()))
                    .stream())
        .sorted(CertificateIdentityOrdering.STABLE)
        .toList();
  }

  /**
   * Check whether this group holds certificate material for the type identified by {@code
   * certificateTypeId}.
   *
   * <p>This is a presence check that distinguishes "absent" from "unreadable": it returns {@code
   * false} only when the group is certain there is no entry, and throws when an entry exists but
   * cannot be read. Provisioning relies on that distinction so an unreadable entry is never
   * silently replaced.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @return {@code true} if material for {@code certificateTypeId} is present.
   * @throws Exception if the presence of the entry cannot be determined.
   */
  boolean hasCertificate(NodeId certificateTypeId) throws Exception;

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
   * Get the {@link CertificateValidator} for this {@link CertificateGroup}.
   *
   * @return the {@link CertificateValidator} for this {@link CertificateGroup}.
   */
  CertificateValidator getCertificateValidator();

  /**
   * An entry describing a certificate chain of one certificate type belonging to a {@link
   * CertificateGroup}.
   *
   * @param certificateTypeId the {@link NodeId} identifying the type of certificate.
   * @param certificateChain the leaf certificate and any issuer certificates.
   */
  record Entry(NodeId certificateTypeId, X509Certificate[] certificateChain) {}
}
