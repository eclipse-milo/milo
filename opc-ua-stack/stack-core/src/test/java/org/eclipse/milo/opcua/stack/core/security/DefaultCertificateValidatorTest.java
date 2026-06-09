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
import java.util.Set;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.CaSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
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

  // The policy name says Curve25519, but the peer's application certificate must be Ed25519; X25519
  // is only valid as the ephemeral key-agreement public key carried in nonce fields.
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

  // Curve448 has the same certificate/key-agreement split: Ed448 authenticates the application, and
  // X448 is limited to ephemeral OpenSecureChannel/user-token key agreement.
  @Test
  void defaultClientValidatorRejectsX448CertificateForCurve448Profile() throws Exception {
    CertificateChain certificateChain = createX448CertificateChain();
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
                SecurityPolicy.ECC_curve448_ChaChaPoly.getProfile()));
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

  // Legacy RSA dev certs (e.g. openssl defaults) commonly omit the KeyUsage extension. The
  // KEY_USAGE_END_ENTITY check is not part of NO_OPTIONAL_CHECKS, so the profile-aware path must
  // suppress it for legacy profiles rather than rejecting with Bad_CertificateUseNotAllowed.
  @Test
  void defaultClientValidatorAcceptsRsaCertificateWithoutKeyUsageUnderNoOptionalChecks() {
    X509Certificate certificate = createRsaCertificateWithoutKeyUsage();
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.NO_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertDoesNotThrow(
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.Basic256Sha256.getProfile()));
  }

  // A non-critical KeyUsage that omits nonRepudiation/dataEncipherment is the other common legacy
  // shape; it must also be accepted by default since KEY_USAGE_END_ENTITY is suppressible.
  @Test
  void defaultClientValidatorAcceptsRsaCertificateMissingLegacyKeyUsageBitsUnderNoOptionalChecks() {
    X509Certificate certificate = createRsaCertificateMissingLegacyKeyUsageBits();
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.NO_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertDoesNotThrow(
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.Basic256Sha256.getProfile()));
  }

  // Enabling KEY_USAGE_END_ENTITY must restore strict enforcement: a legacy RSA cert without a
  // KeyUsage extension is rejected.
  @Test
  void defaultClientValidatorRejectsRsaCertificateWithoutKeyUsageWhenKeyUsageCheckEnabled() {
    X509Certificate certificate = createRsaCertificateWithoutKeyUsage();
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificate),
            Set.of(ValidationCheck.KEY_USAGE_END_ENTITY),
            new MemoryCertificateQuarantine());

    assertThrows(
        UaException.class,
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.Basic256Sha256.getProfile()));
  }

  @Test
  void defaultServerValidatorAcceptsRsaCertificateWithoutKeyUsageUnderNoOptionalChecks() {
    X509Certificate certificate = createRsaCertificateWithoutKeyUsage();
    DefaultServerCertificateValidator validator =
        new DefaultServerCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.NO_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertDoesNotThrow(
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.Basic256Sha256.getProfile()));
  }

  @Test
  void defaultServerValidatorAcceptsRsaCertificateMissingLegacyKeyUsageBitsUnderNoOptionalChecks() {
    X509Certificate certificate = createRsaCertificateMissingLegacyKeyUsageBits();
    DefaultServerCertificateValidator validator =
        new DefaultServerCertificateValidator(
            trustListManager(certificate),
            ValidationCheck.NO_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertDoesNotThrow(
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.Basic256Sha256.getProfile()));
  }

  @Test
  void defaultServerValidatorRejectsRsaCertificateWithoutKeyUsageWhenKeyUsageCheckEnabled() {
    X509Certificate certificate = createRsaCertificateWithoutKeyUsage();
    DefaultServerCertificateValidator validator =
        new DefaultServerCertificateValidator(
            trustListManager(certificate),
            Set.of(ValidationCheck.KEY_USAGE_END_ENTITY),
            new MemoryCertificateQuarantine());

    assertThrows(
        UaException.class,
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.Basic256Sha256.getProfile()));
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

  private static X509Certificate createRsaCertificateWithoutKeyUsage() {
    return buildSelfSignedRsaCertificate(new NoKeyUsageGenerator());
  }

  private static X509Certificate createRsaCertificateMissingLegacyKeyUsageBits() {
    // digitalSignature + keyEncipherment + keyCertSign, omitting nonRepudiation and
    // dataEncipherment, written as a non-critical KeyUsage extension so the failure is
    // suppressible.
    return buildSelfSignedRsaCertificate(
        new NonCriticalKeyUsageGenerator(
            KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.keyCertSign));
  }

  private static X509Certificate buildSelfSignedRsaCertificate(
      SelfSignedCertificateGenerator generator) {
    try {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

      return new SelfSignedCertificateBuilder(keyPair, generator)
          .setCommonName("Eclipse Milo OPC UA Test")
          .setOrganization("digitalpetri")
          .setApplicationUri("urn:eclipse:milo:test")
          .addDnsName("localhost")
          .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static final class NoKeyUsageGenerator extends SelfSignedCertificateGenerator {
    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) {}

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {}
  }

  private static final class NonCriticalKeyUsageGenerator extends SelfSignedCertificateGenerator {
    private final int keyUsage;

    NonCriticalKeyUsageGenerator(int keyUsage) {
      this.keyUsage = keyUsage;
    }

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {
      certificateBuilder.addExtension(Extension.keyUsage, false, new KeyUsage(keyUsage));
    }

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {}
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

  private static CertificateChain createX448CertificateChain() throws Exception {
    KeyPair issuerKeyPair = SelfSignedCertificateGenerator.generateEd448KeyPair();
    X509Certificate issuerCertificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(issuerKeyPair)
            .setApplicationUri("urn:eclipse:milo:test:issuer")
            .addDnsName("localhost")
            .build();
    KeyPair x448KeyPair = KeyPairGenerator.getInstance("X448").generateKeyPair();
    X509Certificate certificate =
        new CaSignedCertificateBuilder(x448KeyPair, issuerCertificate, issuerKeyPair.getPrivate())
            .setCommonName("X448 Application Certificate")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:eclipse:milo:test:x448")
            .setKeyUsage(KeyUsage.digitalSignature)
            .setSignatureAlgorithm(SelfSignedCertificateBuilder.SA_ED448)
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
