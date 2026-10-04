/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.examples.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Compares actual JDK 17 revocation behavior with local CRLs and no network distribution points.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RevocationMigrationTest {
  private static final boolean LEGACY = true;
  private X509Certificate ca;
  private X509Certificate leaf;
  private X509CRL revoked;
  private X509CRL clear;

  @BeforeAll
  void issueLocalCertificatesAndCrls() throws Exception {
    KeyPair caKeys = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    KeyPair leafKeys = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X500Name issuer = new X500Name("CN=Wiki revocation CA");
    Date before = Date.from(Instant.now().minusSeconds(3600));
    Date after = Date.from(Instant.now().plusSeconds(86400));
    var signer = new JcaContentSignerBuilder("SHA256withRSA").build(caKeys.getPrivate());
    var extensionUtils = new JcaX509ExtensionUtils();
    var root =
        new JcaX509v3CertificateBuilder(
            issuer, BigInteger.ONE, before, after, issuer, caKeys.getPublic());
    root.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
    root.addExtension(
        Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
    root.addExtension(
        Extension.subjectKeyIdentifier,
        false,
        extensionUtils.createSubjectKeyIdentifier(caKeys.getPublic()));
    ca = new JcaX509CertificateConverter().getCertificate(root.build(signer));
    var child =
        new JcaX509v3CertificateBuilder(
            issuer,
            BigInteger.TWO,
            before,
            after,
            new X500Name("CN=Wiki revocation peer"),
            leafKeys.getPublic());
    child.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
    child.addExtension(
        Extension.keyUsage,
        true,
        new KeyUsage(
            KeyUsage.digitalSignature
                | KeyUsage.keyEncipherment
                | KeyUsage.dataEncipherment
                | KeyUsage.nonRepudiation));
    child.addExtension(
        Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(ca));
    leaf = new JcaX509CertificateConverter().getCertificate(child.build(signer));
    var crl = new X509v2CRLBuilder(issuer, before);
    crl.setNextUpdate(after);
    crl.addExtension(
        Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(ca));
    clear = new JcaX509CRLConverter().getCRL(crl.build(signer));
    crl.addCRLEntry(leaf.getSerialNumber(), before, CRLReason.keyCompromise);
    revoked = new JcaX509CRLConverter().getCRL(crl.build(signer));
  }

  static Stream<Set<ValidationCheck>> policies() {
    return Stream.of(
        Set.of(),
        Set.of(ValidationCheck.REVOCATION),
        Set.of(ValidationCheck.REVOCATION_LISTS),
        Set.of(ValidationCheck.REVOCATION, ValidationCheck.REVOCATION_LISTS));
  }

  // A valid local CRL, a revoked peer, and a missing CRL distinguish the real policy paths.
  @ParameterizedTest
  @MethodSource("policies")
  void defaultRevocationAndStrictCrlRequirements(Set<ValidationCheck> checks) throws Exception {
    boolean opened =
        Object.class.getModule().isOpen("sun.security.provider.certpath", getClass().getModule());
    boolean checksRevocation = !LEGACY || opened || checks.contains(ValidationCheck.REVOCATION);
    boolean requiresCrl =
        LEGACY && !opened
            ? checks.contains(ValidationCheck.REVOCATION)
            : checks.contains(ValidationCheck.REVOCATION_LISTS);
    validate(checks, Set.of(clear));
    if (checksRevocation) {
      UaException failure =
          assertThrows(UaException.class, () -> validate(checks, Set.of(revoked)));
      assertEquals(StatusCodes.Bad_CertificateRevoked, failure.getStatusCode().getValue());
    } else {
      validate(checks, Set.of(revoked));
    }
    if (requiresCrl) {
      UaException failure = assertThrows(UaException.class, () -> validate(checks, Set.of()));
      assertEquals(
          StatusCodes.Bad_CertificateRevocationUnknown, failure.getStatusCode().getValue());
    } else {
      validate(checks, Set.of());
    }
  }

  private void validate(Set<ValidationCheck> checks, Set<X509CRL> crls) throws Exception {
    var path = CertificateValidationUtil.buildTrustedCertPath(List.of(leaf), Set.of(ca), Set.of());
    CertificateValidationUtil.validateTrustedCertPath(
        path.getCertPath(), path.getTrustAnchor(), crls, checks, false);
  }
}
