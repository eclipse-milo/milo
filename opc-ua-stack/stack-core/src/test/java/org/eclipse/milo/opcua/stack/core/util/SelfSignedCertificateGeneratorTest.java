/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.CertificateCompatibility;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.junit.jupiter.api.Test;

class SelfSignedCertificateGeneratorTest {

  @Test
  void generatesNistP256ApplicationCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();

    X509Certificate certificate = buildEccApplicationCertificate(keyPair);

    CertificateCompatibility.checkCompatible(
        SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
        NodeIds.EccNistP256ApplicationCertificateType,
        certificate);
    assertMinimalEccKeyUsage(certificate);
    assertTrue(certificate.getSigAlgName().contains("ECDSA"));
  }

  @Test
  void generatesEd25519ApplicationCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateEd25519KeyPair();

    X509Certificate certificate = buildEccApplicationCertificate(keyPair);

    CertificateCompatibility.checkCompatible(
        SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile(),
        NodeIds.EccCurve25519ApplicationCertificateType,
        certificate);
    assertMinimalEccKeyUsage(certificate);
    assertTrue(certificate.getSigAlgName().contains("Ed25519"));
  }

  private static X509Certificate buildEccApplicationCertificate(KeyPair keyPair) throws Exception {
    return SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
        .setApplicationUri("urn:eclipse:milo:test")
        .addDnsName("localhost")
        .build();
  }

  private static void assertMinimalEccKeyUsage(X509Certificate certificate) throws Exception {
    boolean[] keyUsage = certificate.getKeyUsage();

    assertTrue(keyUsage[0], "digitalSignature should be set");
    assertFalse(keyUsage[1], "nonRepudiation should not be set");
    assertFalse(keyUsage[2], "keyEncipherment should not be set");
    assertFalse(keyUsage[3], "dataEncipherment should not be set");
    assertTrue(keyUsage[5], "keyCertSign should be set for self-signed certificates");
    assertNull(certificate.getExtendedKeyUsage(), "ExtendedKeyUsage should be omitted");
  }
}
