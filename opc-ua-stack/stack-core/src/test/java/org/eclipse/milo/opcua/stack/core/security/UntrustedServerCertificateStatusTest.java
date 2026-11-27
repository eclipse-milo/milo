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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.CaSignedCertificateBuilder;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Verifies the status codes reported by {@link DefaultClientCertificateValidator} and {@link
 * DefaultServerCertificateValidator} for untrusted versus unbuildable certificate chains.
 *
 * <p>Per OPC 10000-4 §6.1.3 Table 100, a client reports {@code Bad_CertificateUntrusted} for a
 * complete, validly signed chain that contains no trusted certificate, while a server masks every
 * path-building failure to {@code Bad_SecurityChecksFailed}. Chains that cannot be built (missing
 * issuers, invalid signatures, expired intermediates, or non-CA roots) preserve {@code
 * Bad_SecurityChecksFailed} and the underlying cause on both sides.
 */
@NullMarked
class UntrustedServerCertificateStatusTest {

  private static X509Certificate selfSigned;
  private static X509Certificate otherSelfSigned;
  private static X509Certificate tamperedSelfSigned;

  private static X509Certificate caRoot;
  private static X509Certificate caIntermediate;
  private static X509Certificate caSignedLeaf;
  private static X509Certificate tamperedCaSignedLeaf;
  private static X509Certificate tamperedCaRoot;
  private static X509Certificate expiredIntermediate;
  private static X509Certificate leafUnderExpiredIntermediate;
  private static X509Certificate leafUnderNonCaRoot;

  private static X509Certificate brainpoolSelfSigned;
  private static X509Certificate brainpoolRoot;
  private static X509Certificate brainpoolLeaf;

  @BeforeAll
  static void generateCertificates() throws Exception {
    KeyPair selfSignedKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    selfSigned =
        new SelfSignedCertificateBuilder(selfSignedKeyPair)
            .setCommonName("Self-Signed Server")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:milo:test:self-signed")
            .build();
    tamperedSelfSigned = corruptSignature(selfSigned);

    otherSelfSigned =
        new SelfSignedCertificateBuilder(SelfSignedCertificateGenerator.generateRsaKeyPair(2048))
            .setCommonName("Other Self-Signed")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:milo:test:other")
            .build();

    KeyPair rootKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    caRoot = buildCaRoot(rootKeyPair, "Test Root CA");
    tamperedCaRoot = corruptSignature(caRoot);

    KeyPair intermediateKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    caIntermediate =
        new CaSignedCertificateBuilder(intermediateKeyPair, caRoot, rootKeyPair.getPrivate())
            .setCommonName("Test Intermediate CA")
            .setOrganization("Eclipse Milo")
            .setIsCa(true)
            .setKeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign)
            .build();

    caSignedLeaf =
        new CaSignedCertificateBuilder(
                SelfSignedCertificateGenerator.generateRsaKeyPair(2048),
                caIntermediate,
                intermediateKeyPair.getPrivate())
            .setCommonName("CA-Signed Server")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:milo:test:ca-signed")
            .setIsCa(false)
            .setKeyUsage(
                KeyUsage.digitalSignature
                    | KeyUsage.nonRepudiation
                    | KeyUsage.keyEncipherment
                    | KeyUsage.dataEncipherment)
            .build();
    tamperedCaSignedLeaf = corruptSignature(caSignedLeaf);

    Instant now = Instant.now();
    KeyPair expiredIntKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    expiredIntermediate =
        new CaSignedCertificateBuilder(expiredIntKeyPair, caRoot, rootKeyPair.getPrivate())
            .setCommonName("Expired Intermediate CA")
            .setOrganization("Eclipse Milo")
            .setIsCa(true)
            .setKeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign)
            .setValidity(
                Date.from(now.minus(30, ChronoUnit.DAYS)), Date.from(now.minus(1, ChronoUnit.DAYS)))
            .build();

    leafUnderExpiredIntermediate =
        new CaSignedCertificateBuilder(
                SelfSignedCertificateGenerator.generateRsaKeyPair(2048),
                expiredIntermediate,
                expiredIntKeyPair.getPrivate())
            .setCommonName("Leaf Under Expired Intermediate")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:milo:test:expired-intermediate-leaf")
            .setIsCa(false)
            .setKeyUsage(KeyUsage.digitalSignature)
            .build();

    leafUnderNonCaRoot =
        new CaSignedCertificateBuilder(
                SelfSignedCertificateGenerator.generateRsaKeyPair(2048),
                selfSigned,
                selfSignedKeyPair.getPrivate())
            .setCommonName("Leaf Under Non-CA Root")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:milo:test:non-ca-root-leaf")
            .setIsCa(false)
            .setKeyUsage(KeyUsage.digitalSignature)
            .build();

    // Brainpool P-256r1 certificates exercise the Bouncy Castle fallback in certificateIsSelfSigned
    // and buildCertPath when evaluating untrusted chains.
    KeyPair bpSelfSignedKeyPair = generateBrainpoolKeyPair("brainpoolP256r1");
    brainpoolSelfSigned =
        new SelfSignedCertificateBuilder(bpSelfSignedKeyPair)
            .setCommonName("Brainpool Self-Signed")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:milo:test:bp-self-signed")
            .setSignatureAlgorithm("SHA256withECDSA")
            .build();

    KeyPair bpRootKeyPair = generateBrainpoolKeyPair("brainpoolP256r1");
    brainpoolRoot = buildEcCaRoot(bpRootKeyPair, "Brainpool Root CA");
    brainpoolLeaf =
        new CaSignedCertificateBuilder(
                generateBrainpoolKeyPair("brainpoolP256r1"),
                brainpoolRoot,
                bpRootKeyPair.getPrivate())
            .setCommonName("Brainpool CA-Signed Leaf")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:milo:test:bp-leaf")
            .setIsCa(false)
            .setKeyUsage(KeyUsage.digitalSignature)
            .setSignatureAlgorithm(CaSignedCertificateBuilder.SA_SHA256_ECDSA)
            .build();
  }

  static Stream<Arguments> untrustedCompleteChains() {
    return Stream.of(
        arguments("self-signed with empty trust list", List.of(selfSigned), trustList()),
        arguments(
            "self-signed with unrelated cert trusted",
            List.of(selfSigned),
            trustList(otherSelfSigned)),
        arguments(
            "complete CA-issued chain with empty trust list",
            List.of(caSignedLeaf, caIntermediate, caRoot),
            trustList()),
        arguments(
            "complete CA-issued chain with unrelated cert trusted",
            List.of(caSignedLeaf, caIntermediate, caRoot),
            trustList(otherSelfSigned)),
        arguments(
            "CA-issued chain with root in issuer list only",
            List.of(caSignedLeaf, caIntermediate),
            issuerList(caRoot)),
        arguments(
            "Brainpool self-signed with empty trust list",
            List.of(brainpoolSelfSigned),
            trustList()),
        arguments(
            "Brainpool complete CA-issued chain with empty trust list",
            List.of(brainpoolLeaf, brainpoolRoot),
            trustList()));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("untrustedCompleteChains")
  void clientReportsBadCertificateUntrustedAndQuarantines(
      String label, List<X509Certificate> chain, TrustListManager trustListManager) {

    var quarantine = new MemoryCertificateQuarantine();
    var validator = new DefaultClientCertificateValidator(trustListManager, quarantine);

    UaException error =
        assertThrows(
            UaException.class, () -> validator.validateCertificateChain(chain, null, null));

    assertEquals(StatusCodes.Bad_CertificateUntrusted, error.getStatusCode().value());
    assertTrue(quarantine.getRejectedCertificates().containsAll(chain));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("untrustedCompleteChains")
  void serverMasksUntrustedChainToBadSecurityChecksFailed(
      String label, List<X509Certificate> chain, TrustListManager trustListManager) {

    var quarantine = new MemoryCertificateQuarantine();
    var validator = new DefaultServerCertificateValidator(trustListManager, quarantine);

    UaException error =
        assertThrows(
            UaException.class, () -> validator.validateCertificateChain(chain, null, null));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, error.getStatusCode().value());
    assertTrue(quarantine.getRejectedCertificates().containsAll(chain));
  }

  @Test
  void trustedChainsSucceedWithoutQuarantine() {
    var selfSignedQuarantine = new MemoryCertificateQuarantine();
    var selfSignedValidator =
        new DefaultClientCertificateValidator(trustList(selfSigned), selfSignedQuarantine);
    assertDoesNotThrow(
        () -> selfSignedValidator.validateCertificateChain(List.of(selfSigned), null, null));
    assertTrue(selfSignedQuarantine.getRejectedCertificates().isEmpty());

    var caQuarantine = new MemoryCertificateQuarantine();
    var caValidator = new DefaultClientCertificateValidator(trustList(caRoot), caQuarantine);
    assertDoesNotThrow(
        () ->
            caValidator.validateCertificateChain(
                List.of(caSignedLeaf, caIntermediate, caRoot), null, null));
    assertTrue(caQuarantine.getRejectedCertificates().isEmpty());
  }

  static Stream<Arguments> unbuildableChains() {
    return Stream.of(
        arguments("CA-issued leaf with missing issuers", List.of(caSignedLeaf), trustList()),
        arguments(
            "CA-issued leaf and intermediate with missing root",
            List.of(caSignedLeaf, caIntermediate),
            trustList()),
        arguments(
            "self-signed leaf with corrupted signature", List.of(tamperedSelfSigned), trustList()),
        arguments(
            "CA-issued chain with corrupted leaf signature",
            List.of(tamperedCaSignedLeaf, caIntermediate, caRoot),
            trustList()),
        arguments(
            "CA-issued chain with corrupted root signature",
            List.of(caSignedLeaf, caIntermediate, tamperedCaRoot),
            trustList()),
        arguments(
            "CA-issued chain with expired intermediate CA",
            List.of(leafUnderExpiredIntermediate, expiredIntermediate, caRoot),
            trustList()),
        arguments(
            "multi-cert chain anchored by non-CA self-signed cert",
            List.of(leafUnderNonCaRoot, selfSigned),
            trustList()),
        arguments(
            "CA-issued chain with trusted leaf but unconfigured root",
            List.of(caSignedLeaf, caIntermediate, caRoot),
            trustList(caSignedLeaf)),
        arguments(
            "CA-issued chain with trusted intermediate but unconfigured root",
            List.of(caSignedLeaf, caIntermediate, caRoot),
            trustList(caIntermediate)));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("unbuildableChains")
  void unbuildableChainsKeepBadSecurityChecksFailedAndCause(
      String label, List<X509Certificate> chain, TrustListManager trustListManager) {

    var clientQuarantine = new MemoryCertificateQuarantine();
    var clientValidator = new DefaultClientCertificateValidator(trustListManager, clientQuarantine);

    UaException clientError =
        assertThrows(
            UaException.class, () -> clientValidator.validateCertificateChain(chain, null, null));
    assertEquals(StatusCodes.Bad_SecurityChecksFailed, clientError.getStatusCode().value());
    assertNotNull(clientError.getCause());
    assertTrue(clientQuarantine.getRejectedCertificates().containsAll(chain));

    var serverQuarantine = new MemoryCertificateQuarantine();
    var serverValidator = new DefaultServerCertificateValidator(trustListManager, serverQuarantine);

    UaException serverError =
        assertThrows(
            UaException.class, () -> serverValidator.validateCertificateChain(chain, null, null));
    assertEquals(StatusCodes.Bad_SecurityChecksFailed, serverError.getStatusCode().value());
  }

  private static X509Certificate buildCaRoot(KeyPair keyPair, String commonName) throws Exception {
    return new SelfSignedCertificateBuilder(keyPair, new CaRootGenerator())
        .setCommonName(commonName)
        .setOrganization("Eclipse Milo")
        .build();
  }

  private static X509Certificate buildEcCaRoot(KeyPair keyPair, String commonName)
      throws Exception {
    return new SelfSignedCertificateBuilder(keyPair, new CaRootGenerator())
        .setCommonName(commonName)
        .setOrganization("Eclipse Milo")
        .setSignatureAlgorithm("SHA256withECDSA")
        .build();
  }

  private static KeyPair generateBrainpoolKeyPair(String curveName) throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
    generator.initialize(new ECGenParameterSpec(curveName));
    return generator.generateKeyPair();
  }

  private static X509Certificate corruptSignature(X509Certificate certificate) throws Exception {
    byte[] encoded = certificate.getEncoded();
    encoded[encoded.length - 1] ^= 0xFF;
    return (X509Certificate)
        CertificateFactory.getInstance("X.509")
            .generateCertificate(new ByteArrayInputStream(encoded));
  }

  private static TrustListManager trustList(X509Certificate... trusted) {
    var manager = new MemoryTrustListManager();
    manager.setTrustedCertificates(List.of(trusted));
    return manager;
  }

  private static TrustListManager issuerList(X509Certificate... issuers) {
    var manager = new MemoryTrustListManager();
    manager.setIssuerCertificates(List.of(issuers));
    return manager;
  }

  private static final class CaRootGenerator extends SelfSignedCertificateGenerator {
    @Override
    protected void addBasicConstraints(
        X509v3CertificateBuilder certificateBuilder, BasicConstraints basicConstraints)
        throws CertIOException {
      certificateBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
    }

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {
      certificateBuilder.addExtension(
          Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
    }
  }
}
