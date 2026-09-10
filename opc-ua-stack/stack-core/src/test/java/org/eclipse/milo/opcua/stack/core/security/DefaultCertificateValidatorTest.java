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

import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_CA_INTERMEDIATE;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_CA_ROOT;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_LEAF_INTERMEDIATE_SIGNED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.CaSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.TestCertificates;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@NullMarked
class DefaultCertificateValidatorTest {

  // A CA-signed chain shared by the revocation tests: leaf issued by the intermediate, which is
  // issued by the root. None of those tests mutate the certificates.
  private static X509Certificate caRoot;
  private static X509Certificate caIntermediate;
  private static X509Certificate caSignedLeaf;
  private static PrivateKey caIntermediateKey;
  private static PrivateKey caRootKey;

  @BeforeAll
  static void generateCaSignedChain() throws Exception {
    TestCertificates certificates = TestCertificateGenerator.generateAll();

    caRoot = certificates.getCertificate(ALIAS_CA_ROOT);
    caIntermediate = certificates.getCertificate(ALIAS_CA_INTERMEDIATE);
    caSignedLeaf = certificates.getCertificate(ALIAS_LEAF_INTERMEDIATE_SIGNED);
    caIntermediateKey = certificates.getPrivateKey(ALIAS_CA_INTERMEDIATE);
    caRootKey = certificates.getPrivateKey(ALIAS_CA_ROOT);
  }

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
    // This fixture has no CRL; allow unknown revocation status to reach the compatibility check.
    Set<ValidationCheck> validationChecks = EnumSet.copyOf(ValidationCheck.ALL_OPTIONAL_CHECKS);
    validationChecks.remove(ValidationCheck.REVOCATION_LISTS);
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificateChain.issuerCertificate()),
            validationChecks,
            new MemoryCertificateQuarantine());

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateCertificateChain(
                    List.of(certificateChain.certificate(), certificateChain.issuerCertificate()),
                    null,
                    null,
                    SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile()));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  // Curve448 has the same certificate/key-agreement split: Ed448 authenticates the application, and
  // X448 is limited to ephemeral OpenSecureChannel/user-token key agreement.
  @Test
  void defaultClientValidatorRejectsX448CertificateForCurve448Profile() throws Exception {
    CertificateChain certificateChain = createX448CertificateChain();
    // This fixture has no CRL; allow unknown revocation status to reach the compatibility check.
    Set<ValidationCheck> validationChecks = EnumSet.copyOf(ValidationCheck.ALL_OPTIONAL_CHECKS);
    validationChecks.remove(ValidationCheck.REVOCATION_LISTS);
    DefaultClientCertificateValidator validator =
        new DefaultClientCertificateValidator(
            trustListManager(certificateChain.issuerCertificate()),
            validationChecks,
            new MemoryCertificateQuarantine());

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateCertificateChain(
                    List.of(certificateChain.certificate(), certificateChain.issuerCertificate()),
                    null,
                    null,
                    SecurityPolicy.ECC_curve448_ChaChaPoly.getProfile()));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
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

  // Certificate path construction and revocation checks must use the same trust-list generation.
  @Test
  void defaultClientValidatorReadsOneTrustListSnapshot() {
    X509Certificate certificate = createRsaCertificate();
    var trustListManager = new SnapshotOnlyTrustListManager(certificate);
    var validator =
        new DefaultClientCertificateValidator(
            trustListManager,
            ValidationCheck.NO_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertDoesNotThrow(() -> validator.validateCertificateChain(List.of(certificate), null, null));
    assertEquals(1, trustListManager.snapshotReads.get());
  }

  // Servers have the same two-phase path and revocation validation boundary as clients.
  @Test
  void defaultServerValidatorReadsOneTrustListSnapshot() {
    X509Certificate certificate = createRsaCertificate();
    var trustListManager = new SnapshotOnlyTrustListManager(certificate);
    var validator =
        new DefaultServerCertificateValidator(
            trustListManager,
            ValidationCheck.NO_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());

    assertDoesNotThrow(() -> validator.validateCertificateChain(List.of(certificate), null, null));
    assertEquals(1, trustListManager.snapshotReads.get());
  }

  // The convenience constructors select NO_OPTIONAL_CHECKS. Part 4 §6.1.3 lets the missing-CRL
  // error be suppressed, so a CA-signed chain must validate with no CRL in the trust list.
  @Test
  void defaultClientValidatorAcceptsCaSignedChainWithoutCrls() {
    var validator =
        new DefaultClientCertificateValidator(caTrustList(), new MemoryCertificateQuarantine());

    assertDoesNotThrow(() -> validator.validateCertificateChain(List.of(caSignedLeaf), null, null));
  }

  // A revocation established by a CRL may not be suppressed (Part 4 §6.1.3), so the default
  // validators enforce a CRL that is present without any revocation check being opted into.
  @Test
  void defaultClientValidatorRejectsRevokedCertificateWithoutOptingIntoRevocationChecks()
      throws Exception {
    MemoryTrustListManager trustListManager = caTrustList();
    trustListManager.setTrustedCrls(List.of(crlRevoking(caSignedLeaf)));
    var validator =
        new DefaultClientCertificateValidator(trustListManager, new MemoryCertificateQuarantine());

    UaException e =
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(caSignedLeaf), null, null));

    assertEquals(StatusCodes.Bad_CertificateRevoked, e.getStatusCode().value());
  }

  // Servers report a revoked peer with the less informative Bad_SecurityChecksFailed.
  @Test
  void defaultServerValidatorRejectsRevokedCertificateWithoutOptingIntoRevocationChecks()
      throws Exception {
    MemoryTrustListManager trustListManager = caTrustList();
    trustListManager.setTrustedCrls(List.of(crlRevoking(caSignedLeaf)));
    var validator =
        new DefaultServerCertificateValidator(trustListManager, new MemoryCertificateQuarantine());

    UaException e =
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(caSignedLeaf), null, null));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, e.getStatusCode().value());
  }

  // Each validation reads the trust list's current snapshot, so a CRL published after a peer was
  // accepted revokes that peer on its next validation without recreating the validator.
  @Test
  void crlAddedToTrustListRevokesCertificateOnSubsequentValidation() throws Exception {
    MemoryTrustListManager trustListManager = caTrustList();
    var validator =
        new DefaultClientCertificateValidator(trustListManager, new MemoryCertificateQuarantine());

    assertDoesNotThrow(() -> validator.validateCertificateChain(List.of(caSignedLeaf), null, null));

    trustListManager.setTrustedCrls(List.of(crlRevoking(caSignedLeaf)));

    UaException e =
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(caSignedLeaf), null, null));

    assertEquals(StatusCodes.Bad_CertificateRevoked, e.getStatusCode().value());
  }

  // Part 4 §6.1.3 and CTT 033: a path-building failure must not expose an untrusted peer's
  // validity status. Part 6 §6.7.7 requires trust to be checked first.
  @ParameterizedTest
  @CsvSource({"-2, -1", "1, 2"})
  void defaultServerValidatorMasksUntrustedCertificateValidity(int notBeforeDays, int notAfterDays)
      throws Exception {
    X509Certificate certificate = createSelfSignedCertificate(notBeforeDays, notAfterDays);
    var quarantine = new MemoryCertificateQuarantine();
    var validator =
        new DefaultServerCertificateValidator(
            caTrustList(), Set.of(ValidationCheck.VALIDITY), quarantine);

    UaException e =
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(certificate), null, null));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, e.getStatusCode().value());
    assertEquals(List.of(certificate), quarantine.getRejectedCertificates());
  }

  // Once trust is established, preserve the specific validity errors expected by CTT 007/008.
  @ParameterizedTest
  @CsvSource({"-2, -1", "1, 2"})
  void defaultServerValidatorPreservesTrustedCertificateValidity(
      int notBeforeDays, int notAfterDays) throws Exception {
    X509Certificate certificate = createSelfSignedCertificate(notBeforeDays, notAfterDays);
    var validator =
        new DefaultServerCertificateValidator(
            trustListManager(certificate),
            Set.of(ValidationCheck.VALIDITY),
            new MemoryCertificateQuarantine());

    UaException e =
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(certificate), null, null));

    assertEquals(StatusCodes.Bad_CertificateTimeInvalid, e.getStatusCode().value());
  }

  // The JDK rejects an expired CA-issued leaf during path construction. A trusted issuer alone
  // does not establish a path, so the server must still report the Part 4 §6.1.3 chain error.
  @Test
  void defaultServerValidatorMasksValidityFailureDuringCaPathConstruction() throws Exception {
    Instant now = Instant.now();
    X509Certificate certificate =
        new CaSignedCertificateBuilder(
                SelfSignedCertificateGenerator.generateRsaKeyPair(2048),
                caIntermediate,
                caIntermediateKey)
            .setCommonName("Expired CA-issued leaf")
            .setValidity(
                Date.from(now.minus(2, ChronoUnit.DAYS)), Date.from(now.minus(1, ChronoUnit.DAYS)))
            .build();
    var validator =
        new DefaultServerCertificateValidator(
            caTrustList(), Set.of(ValidationCheck.VALIDITY), new MemoryCertificateQuarantine());

    UaException e =
        assertThrows(
            UaException.class,
            () -> validator.validateCertificateChain(List.of(certificate), null, null));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, e.getStatusCode().value());
  }

  // Part 4 §6.1.3 recommends masking both missing-CRL errors in server responses (CTT 002/042/043).
  // The client must retain the detailed status so its application can diagnose its trust list.
  @ParameterizedTest
  @MethodSource("missingCrlTrustLists")
  void defaultServerValidatorMasksUnknownRevocationStatus(
      MemoryTrustListManager trustList, long clientStatus) {
    var serverValidator =
        new DefaultServerCertificateValidator(
            trustList, ValidationCheck.ALL_OPTIONAL_CHECKS, new MemoryCertificateQuarantine());
    var clientValidator =
        new DefaultClientCertificateValidator(
            trustList, ValidationCheck.ALL_OPTIONAL_CHECKS, new MemoryCertificateQuarantine());

    UaException serverError =
        assertThrows(
            UaException.class,
            () -> serverValidator.validateCertificateChain(List.of(caSignedLeaf), null, null));
    UaException clientError =
        assertThrows(
            UaException.class,
            () -> clientValidator.validateCertificateChain(List.of(caSignedLeaf), null, null));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, serverError.getStatusCode().value());
    assertEquals(clientStatus, clientError.getStatusCode().value());
  }

  private static Stream<Arguments> missingCrlTrustLists() throws Exception {
    MemoryTrustListManager missingLeafIssuerCrl = caTrustList();
    missingLeafIssuerCrl.setTrustedCrls(List.of(CrlTestUtil.generateCrl(caRoot, caRootKey)));
    MemoryTrustListManager missingRootCrl = caTrustList();
    missingRootCrl.setIssuerCrls(List.of(crlRevoking()));

    return Stream.of(
        Arguments.of(missingLeafIssuerCrl, StatusCodes.Bad_CertificateRevocationUnknown),
        Arguments.of(missingRootCrl, StatusCodes.Bad_CertificateIssuerRevocationUnknown));
  }

  private static X509Certificate createSelfSignedCertificate(int notBeforeDays, int notAfterDays)
      throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    var subject = new X500Name("CN=Validity test");
    Instant now = Instant.now();
    var builder =
        new JcaX509v3CertificateBuilder(
            subject,
            BigInteger.ONE,
            Date.from(now.plus(notBeforeDays, ChronoUnit.DAYS)),
            Date.from(now.plus(notAfterDays, ChronoUnit.DAYS)),
            subject,
            keyPair.getPublic());

    return new JcaX509CertificateConverter()
        .getCertificate(
            builder.build(
                new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate())));
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

  private static final class SnapshotOnlyTrustListManager extends MemoryTrustListManager {

    private final AtomicInteger snapshotReads = new AtomicInteger();

    SnapshotOnlyTrustListManager(X509Certificate trustedCertificate) {
      replaceAll(
          new TrustListSnapshot(
              List.of(), List.of(), List.of(trustedCertificate), List.of(), DateTime.MIN_VALUE));
    }

    @Override
    public TrustListSnapshot getSnapshot() {
      snapshotReads.incrementAndGet();
      return super.getSnapshot();
    }

    @Override
    public List<X509CRL> getIssuerCrls() {
      throw new AssertionError("validator must use getSnapshot()");
    }

    @Override
    public List<X509CRL> getTrustedCrls() {
      throw new AssertionError("validator must use getSnapshot()");
    }

    @Override
    public List<X509Certificate> getIssuerCertificates() {
      throw new AssertionError("validator must use getSnapshot()");
    }

    @Override
    public List<X509Certificate> getTrustedCertificates() {
      throw new AssertionError("validator must use getSnapshot()");
    }
  }

  private record CertificateChain(X509Certificate certificate, X509Certificate issuerCertificate) {}

  /** A trust list that trusts the root and can build a path through the intermediate. */
  private static MemoryTrustListManager caTrustList() {
    MemoryTrustListManager trustListManager = new MemoryTrustListManager();
    trustListManager.addTrustedCertificate(caRoot);
    trustListManager.addIssuerCertificate(caIntermediate);
    return trustListManager;
  }

  /** A CRL from the leaf's issuer listing {@code revoked}. */
  private static X509CRL crlRevoking(X509Certificate... revoked) throws Exception {
    return CrlTestUtil.generateCrl(caIntermediate, caIntermediateKey, revoked);
  }
}
