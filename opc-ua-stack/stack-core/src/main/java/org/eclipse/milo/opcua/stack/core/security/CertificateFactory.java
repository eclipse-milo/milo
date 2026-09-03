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
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.x500.X500Name;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Creates key pairs, certificate chains, and signing requests for the certificate types a {@link
 * CertificateGroup} supports.
 *
 * <p>A factory is independent of any group. {@link #createMissingCertificates(CertificateGroup)} is
 * the provisioning entry point for applications that want Milo to generate material a group is
 * missing; applications whose certificates are issued externally never call it.
 */
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

  /**
   * Create and install certificate material for every supported certificate type {@code
   * certificateGroup} is missing.
   *
   * <p>For each supported type the group reports absent through {@link
   * CertificateGroup#hasCertificate(NodeId)}, this factory creates a key pair and certificate chain
   * and installs them with {@link CertificateGroup#updateCertificate}. Types that already have an
   * entry are left untouched. The call is idempotent and may be repeated, for example after a
   * failure or after an entry has been removed from the group's store.
   *
   * <p>An entry the group cannot read is never replaced: a {@code hasCertificate} failure
   * propagates rather than being treated as absence.
   *
   * <p>Provisioning is serialized on the group's monitor, so concurrent calls for the same group
   * cannot both find a type absent and install competing key material for it.
   *
   * @param certificateGroup the group to provision.
   * @return the certificate type IDs for which material was created, in supported-type order; empty
   *     if the group already held every supported type.
   * @throws Exception if the group or this factory fails for any certificate type. Material created
   *     for earlier types before the failure remains installed.
   */
  default List<NodeId> createMissingCertificates(CertificateGroup certificateGroup)
      throws Exception {

    var created = new ArrayList<NodeId>();

    // The check/create/update sequence has to be atomic per group: two threads provisioning the
    // same empty group would otherwise both see a type as absent and install different key
    // material for it, leaving an endpoint advertising a certificate the store no longer holds.
    synchronized (certificateGroup) {
      for (NodeId certificateTypeId : certificateGroup.getSupportedCertificateTypeIds()) {
        if (!certificateGroup.hasCertificate(certificateTypeId)) {
          KeyPair keyPair = createKeyPair(certificateTypeId);
          X509Certificate[] certificateChain = createCertificateChain(certificateTypeId, keyPair);

          certificateGroup.updateCertificate(certificateTypeId, keyPair, certificateChain);

          created.add(certificateTypeId);
        }
      }
    }

    return List.copyOf(created);
  }
}
