/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;

/**
 * A self-signed application instance identity of one OPC UA certificate type, for SKS SecureChannel
 * tests.
 *
 * @param certificateTypeId the certificate type the identity was generated for.
 * @param keyPair the key pair belonging to {@code certificate}.
 * @param certificate the self-signed application instance certificate.
 */
record SksTestIdentity(NodeId certificateTypeId, KeyPair keyPair, X509Certificate certificate) {

  /**
   * Generate an identity of {@code certificateTypeId}, valid for {@code localhost}.
   *
   * @param certificateTypeId an RsaSha256, EccNistP256, or EccCurve25519 application certificate
   *     type.
   * @param commonName the certificate common name.
   * @param applicationUri the application URI placed in the certificate SAN.
   * @return the generated identity.
   * @throws IllegalArgumentException if {@code certificateTypeId} is not supported.
   */
  static SksTestIdentity generate(
      NodeId certificateTypeId, String commonName, String applicationUri) throws Exception {

    KeyPair keyPair;
    SelfSignedCertificateBuilder builder;
    if (NodeIds.RsaSha256ApplicationCertificateType.equals(certificateTypeId)) {
      keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      builder = new SelfSignedCertificateBuilder(keyPair);
    } else if (NodeIds.EccNistP256ApplicationCertificateType.equals(certificateTypeId)) {
      keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
      builder = SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair);
    } else if (NodeIds.EccCurve25519ApplicationCertificateType.equals(certificateTypeId)) {
      keyPair = SelfSignedCertificateGenerator.generateEd25519KeyPair();
      builder = SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair);
    } else {
      throw new IllegalArgumentException("certificateTypeId: " + certificateTypeId);
    }

    X509Certificate certificate =
        builder
            .setCommonName(commonName)
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .build();

    return new SksTestIdentity(certificateTypeId, keyPair, certificate);
  }
}
