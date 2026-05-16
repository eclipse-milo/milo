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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.CaSignedCertificateBuilder;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
class CertificateCompatibilityTest {

  @Test
  void acceptsNistP256ApplicationCertificate() throws Exception {
    X509Certificate certificate =
        buildEccApplicationCertificate(SelfSignedCertificateGenerator.generateNistP256KeyPair());

    assertDoesNotThrow(
        () ->
            CertificateCompatibility.checkCompatible(
                SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                NodeIds.EccNistP256ApplicationCertificateType,
                certificate));
  }

  @Test
  void acceptsEd25519ApplicationCertificateForCurve25519Policy() throws Exception {
    X509Certificate certificate =
        buildEccApplicationCertificate(SelfSignedCertificateGenerator.generateEd25519KeyPair());

    assertDoesNotThrow(
        () ->
            CertificateCompatibility.checkCompatible(
                SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile(),
                NodeIds.EccCurve25519ApplicationCertificateType,
                certificate));
  }

  @Test
  void rejectsX25519ApplicationCertificateForCurve25519Policy() throws Exception {
    X509Certificate certificate = buildX25519Certificate();

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                CertificateCompatibility.checkCompatible(
                    SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile(),
                    NodeIds.EccCurve25519ApplicationCertificateType,
                    certificate));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  @Test
  void rejectsRsaCertificateForEccPolicy() {
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate certificate = factory.createRsaSha256CertificateChain(keyPair)[0];

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                CertificateCompatibility.checkCompatible(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    NodeIds.EccNistP256ApplicationCertificateType,
                    certificate));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  @Test
  void rejectsEccCertificateForRsaPolicy() throws Exception {
    X509Certificate certificate =
        buildEccApplicationCertificate(SelfSignedCertificateGenerator.generateNistP256KeyPair());

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                CertificateCompatibility.checkCompatible(
                    SecurityPolicy.Basic256Sha256.getProfile(),
                    NodeIds.RsaSha256ApplicationCertificateType,
                    certificate));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  @Test
  void rejectsCertificateTypeMismatch() throws Exception {
    X509Certificate certificate =
        buildEccApplicationCertificate(SelfSignedCertificateGenerator.generateNistP256KeyPair());

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                CertificateCompatibility.checkCompatible(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    NodeIds.EccNistP384ApplicationCertificateType,
                    certificate));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  @Test
  void rejectsEccCertificateMissingDigitalSignature() throws Exception {
    X509Certificate certificate =
        buildEccApplicationCertificate(
            SelfSignedCertificateGenerator.generateNistP256KeyPair(),
            new KeyUsageCertificateGenerator(KeyUsage.keyCertSign));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                CertificateCompatibility.checkCompatible(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    NodeIds.EccNistP256ApplicationCertificateType,
                    certificate));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  @Test
  void rejectsSelfSignedEccCertificateMissingKeyCertSign() throws Exception {
    X509Certificate certificate =
        buildEccApplicationCertificate(
            SelfSignedCertificateGenerator.generateNistP256KeyPair(),
            new KeyUsageCertificateGenerator(KeyUsage.digitalSignature));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                CertificateCompatibility.checkCompatible(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    NodeIds.EccNistP256ApplicationCertificateType,
                    certificate));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  @Test
  void acceptsExtraEccKeyUsageBits() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .build();

    assertDoesNotThrow(
        () ->
            CertificateCompatibility.checkCompatible(
                SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                NodeIds.EccNistP256ApplicationCertificateType,
                certificate));
  }

  private static X509Certificate buildEccApplicationCertificate(KeyPair keyPair) throws Exception {
    return buildEccApplicationCertificate(
        keyPair, SelfSignedCertificateGenerator.eccApplicationCertificateGenerator());
  }

  private static X509Certificate buildEccApplicationCertificate(
      KeyPair keyPair, SelfSignedCertificateGenerator generator) throws Exception {

    return new SelfSignedCertificateBuilder(keyPair, generator)
        .setApplicationUri("urn:eclipse:milo:test")
        .addDnsName("localhost")
        .build();
  }

  private static X509Certificate buildX25519Certificate() throws Exception {
    KeyPair issuerKeyPair = SelfSignedCertificateGenerator.generateEd25519KeyPair();
    X509Certificate issuerCertificate = buildEccApplicationCertificate(issuerKeyPair);
    KeyPair x25519KeyPair = KeyPairGenerator.getInstance("X25519").generateKeyPair();

    return new CaSignedCertificateBuilder(
            x25519KeyPair, issuerCertificate, issuerKeyPair.getPrivate())
        .setCommonName("X25519 Application Certificate")
        .setOrganization("Eclipse Milo")
        .setApplicationUri("urn:eclipse:milo:test:x25519")
        .setKeyUsage(KeyUsage.digitalSignature)
        .setSignatureAlgorithm(SelfSignedCertificateBuilder.SA_ED25519)
        .build();
  }

  private static final class KeyUsageCertificateGenerator extends SelfSignedCertificateGenerator {
    private final int keyUsage;

    KeyUsageCertificateGenerator(int keyUsage) {
      this.keyUsage = keyUsage;
    }

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {}

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {

      certificateBuilder.addExtension(Extension.keyUsage, false, new KeyUsage(keyUsage));
    }
  }
}
