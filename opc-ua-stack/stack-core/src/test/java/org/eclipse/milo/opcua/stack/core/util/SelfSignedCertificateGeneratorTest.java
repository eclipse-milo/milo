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

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.CertificateCompatibility;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
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
  void generatesNistP384ApplicationCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP384KeyPair();

    X509Certificate certificate = buildEccApplicationCertificate(keyPair);

    CertificateCompatibility.checkCompatible(
        SecurityPolicy.ECC_nistP384_AesGcm.getProfile(),
        NodeIds.EccNistP384ApplicationCertificateType,
        certificate);
    assertMinimalEccKeyUsage(certificate);
    assertTrue(certificate.getSigAlgName().contains("ECDSA"));
    assertTrue(certificate.getSigAlgName().contains("384"));
  }

  // Brainpool P-256 certificates require BC-backed key generation while still using the minimal ECC
  // application-certificate key usage expected by endpoint compatibility checks.
  @Test
  void generatesBrainpoolP256ApplicationCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateBrainpoolP256r1KeyPair();

    X509Certificate certificate = buildEccApplicationCertificate(keyPair);

    CertificateCompatibility.checkCompatible(
        SecurityPolicy.ECC_brainpoolP256r1_AesGcm.getProfile(),
        NodeIds.EccBrainpoolP256r1ApplicationCertificateType,
        certificate);
    assertMinimalEccKeyUsage(certificate);
    assertTrue(certificate.getSigAlgName().contains("ECDSA"));
  }

  // Self-signed Brainpool certificates need SHA384/ECDSA signatures and the minimal ECC
  // application-certificate key usage so endpoint compatibility checks can accept them.
  @Test
  void generatesBrainpoolP384ApplicationCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();

    X509Certificate certificate = buildEccApplicationCertificate(keyPair);

    CertificateCompatibility.checkCompatible(
        SecurityPolicy.ECC_brainpoolP384r1_AesGcm.getProfile(),
        NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
        certificate);
    assertMinimalEccKeyUsage(certificate);
    assertTrue(certificate.getSigAlgName().contains("ECDSA"));
    assertTrue(certificate.getSigAlgName().contains("384"));
  }

  // Curve25519 application certificates are Ed25519 certificates. The X25519 key pair is generated
  // later for each OpenSecureChannel/user-token ephemeral agreement.
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

  // Curve448 mirrors Curve25519: Ed448 is the certificate/signature identity and X448 is only the
  // ephemeral agreement key.
  @Test
  void generatesEd448ApplicationCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateEd448KeyPair();

    X509Certificate certificate = buildEccApplicationCertificate(keyPair);

    CertificateCompatibility.checkCompatible(
        SecurityPolicy.ECC_curve448_ChaChaPoly.getProfile(),
        NodeIds.EccCurve448ApplicationCertificateType,
        certificate);
    assertMinimalEccKeyUsage(certificate);
    assertTrue(certificate.getSigAlgName().contains("Ed448"));
  }

  private static X509Certificate buildEccApplicationCertificate(KeyPair keyPair) throws Exception {
    return SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
        .setApplicationUri("urn:eclipse:milo:test")
        .addDnsName("localhost")
        .build();
  }

  private static void assertMinimalEccKeyUsage(X509Certificate certificate) throws Exception {
    boolean[] keyUsage = requireNonNull(certificate.getKeyUsage(), "keyUsage");

    assertTrue(keyUsage[0], "digitalSignature should be set");
    assertFalse(keyUsage[1], "nonRepudiation should not be set");
    assertFalse(keyUsage[2], "keyEncipherment should not be set");
    assertFalse(keyUsage[3], "dataEncipherment should not be set");
    assertTrue(keyUsage[5], "keyCertSign should be set for self-signed certificates");
    assertNull(certificate.getExtendedKeyUsage(), "ExtendedKeyUsage should be omitted");
  }
}
