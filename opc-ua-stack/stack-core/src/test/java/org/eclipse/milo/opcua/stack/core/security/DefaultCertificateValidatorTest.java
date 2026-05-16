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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.CaSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.junit.jupiter.api.Test;

class DefaultCertificateValidatorTest {

  @Test
  void defaultClientValidatorCanUseProfileAwareEccUsageChecks() throws Exception {
    X509Certificate certificate = createMinimalEccCertificate();
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.ALL_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertThrows(
        UaException.class,
        () -> validator.validateCertificateChain(List.of(certificate), null, null));
    assertDoesNotThrow(
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
  }

  @Test
  void defaultClientValidatorRejectsRsaCertificateForEccProfile() {
    X509Certificate certificate = createRsaCertificate();
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.ALL_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertThrows(
        UaException.class,
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
  }

  @Test
  void defaultClientValidatorRejectsX25519CertificateForCurve25519Profile() throws Exception {
    CertificateChain certificateChain = createX25519CertificateChain();
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificateChain.issuerCertificate()),
            ValidationCheck.ALL_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertThrows(
        UaException.class,
        () ->
            validator.validateCertificateChain(
                List.of(certificateChain.certificate(), certificateChain.issuerCertificate()),
                null,
                null,
                SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile()));
  }

  @Test
  void defaultServerValidatorCanUseProfileAwareEccUsageChecks() throws Exception {
    X509Certificate certificate = createMinimalEccCertificate();
    DefaultServerCertificateValidator validator =
        new DefaultServerCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.ALL_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertThrows(
        UaException.class,
        () -> validator.validateCertificateChain(List.of(certificate), null, null));
    assertDoesNotThrow(
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
  }

  @Test
  void defaultServerValidatorRejectsRsaCertificateForEccProfile() {
    X509Certificate certificate = createRsaCertificate();
    DefaultServerCertificateValidator validator =
        new DefaultServerCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.ALL_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertThrows(
        UaException.class,
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));
  }

  private static X509Certificate createMinimalEccCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();

    return SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
        .setApplicationUri("urn:eclipse:milo:test")
        .addDnsName("localhost")
        .build();
  }

  private static X509Certificate createRsaCertificate() {
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();

    return factory.createRsaSha256CertificateChain(keyPair)[0];
  }

  private static CertificateChain createX25519CertificateChain() throws Exception {
    KeyPair issuerKeyPair = SelfSignedCertificateGenerator.generateEd25519KeyPair();
    X509Certificate issuerCertificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(issuerKeyPair)
            .setApplicationUri("urn:eclipse:milo:test:issuer")
            .addDnsName("localhost")
            .build();
    KeyPair x25519KeyPair = KeyPairGenerator.getInstance("X25519").generateKeyPair();
    X509Certificate certificate =
        new CaSignedCertificateBuilder(x25519KeyPair, issuerCertificate, issuerKeyPair.getPrivate())
            .setCommonName("X25519 Application Certificate")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:eclipse:milo:test:x25519")
            .setKeyUsage(KeyUsage.digitalSignature)
            .setSignatureAlgorithm(SelfSignedCertificateBuilder.SA_ED25519)
            .build();

    return new CertificateChain(certificate, issuerCertificate);
  }

  private static TrustListManager trustListManager(X509Certificate certificate) {
    MemoryTrustListManager trustListManager = new MemoryTrustListManager();
    trustListManager.addTrustedCertificate(certificate);
    return trustListManager;
  }

  private record CertificateChain(X509Certificate certificate, X509Certificate issuerCertificate) {}
}
