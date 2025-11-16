/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.validation;

import static java.util.Collections.emptySet;
import static org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil.buildTrustedCertPath;
import static org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil.checkHostnameOrIpAddress;
import static org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil.validateTrustedCertPath;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_CA_INTERMEDIATE;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_CA_ROOT;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_LEAF_INTERMEDIATE_SIGNED;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_LEAF_SELF_SIGNED;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_NO_KEY_USAGE_NO_CA;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_NO_KEY_USAGE_YES_CA;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_URI_WITH_SPACES;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_YES_KEY_USAGE_NO_CA;
import static org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.ALIAS_YES_KEY_USAGE_YES_CA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.TestCertificates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CertificateValidationUtilTest {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private static TestCertificates testCertificates;
  private static X509Certificate caIntermediate;
  private static X509Certificate caRoot;
  private static X509Certificate leafSelfSigned;
  private static X509Certificate leafIntermediateSigned;
  private static X509Certificate uriWithSpaces;

  @BeforeAll
  public static void generateCertificates() throws Exception {
    testCertificates = TestCertificateGenerator.generateAll();

    caIntermediate = testCertificates.getCertificate(ALIAS_CA_INTERMEDIATE);
    caRoot = testCertificates.getCertificate(ALIAS_CA_ROOT);
    leafSelfSigned = testCertificates.getCertificate(ALIAS_LEAF_SELF_SIGNED);
    leafIntermediateSigned = testCertificates.getCertificate(ALIAS_LEAF_INTERMEDIATE_SIGNED);
    uriWithSpaces = testCertificates.getCertificate(ALIAS_URI_WITH_SPACES);

    assertNotNull(caIntermediate);
    assertNotNull(caRoot);
    assertNotNull(leafSelfSigned);
    assertNotNull(leafIntermediateSigned);
    assertNotNull(uriWithSpaces);
  }

  @Test
  void selfSignedCertificateOpcUa105() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedCertificateBuilder builder =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .addDnsName("hostname")
            .addIpAddress("127.0.0.1");

    X509Certificate certificate = builder.build();

    PKIXCertPathBuilderResult result =
        buildTrustedCertPath(List.of(certificate), Set.of(certificate), emptySet());

    validateTrustedCertPath(
        result.getCertPath(),
        result.getTrustAnchor(),
        emptySet(),
        ValidationCheck.ALL_OPTIONAL_CHECKS,
        false);

    validateTrustedCertPath(
        result.getCertPath(),
        result.getTrustAnchor(),
        emptySet(),
        ValidationCheck.ALL_OPTIONAL_CHECKS,
        true);
  }

  @Test
  public void testBuildTrustedCertPath_LeafSelfSigned() throws Exception {
    List<X509Certificate> certificateChain = List.of(leafSelfSigned);

    buildTrustedCertPath(certificateChain, Set.of(leafSelfSigned), emptySet());
  }

  @Test
  public void testBuildTrustedCertPath_LeafSelfSigned_NotTrusted() {
    List<X509Certificate> certificateChain = List.of(leafSelfSigned);

    assertThrows(
        UaException.class, () -> buildTrustedCertPath(certificateChain, emptySet(), emptySet()));
  }

  @Test
  public void testBuildTrustedCertPath_LeafIntermediateSigned() throws Exception {
    // chain: leaf
    // trusted: ca-intermedate
    // issuers: ca-root
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned);

      buildTrustedCertPath(certificateChain, Set.of(caIntermediate), Set.of(caRoot));
    }

    // chain: leaf, ca-intermediate
    // trusted: ca-intermediate
    // issuers: ca-root
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned, caIntermediate);

      buildTrustedCertPath(certificateChain, Set.of(caIntermediate), Set.of(caRoot));
    }

    // chain: leaf, ca-intermediate
    // trusted: ca-root
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned, caIntermediate);

      buildTrustedCertPath(certificateChain, Set.of(caRoot), emptySet());
    }

    // chain: leaf, ca-intermediate, ca-root
    // trusted: ca-intermediate
    // issuers: ca-root
    {
      List<X509Certificate> certificateChain =
          List.of(leafIntermediateSigned, caIntermediate, caRoot);

      buildTrustedCertPath(certificateChain, Set.of(caIntermediate), Set.of(caRoot));
    }

    // chain: leaf, ca-intermediate, ca-root
    // trusted: ca-root
    {
      List<X509Certificate> certificateChain =
          List.of(leafIntermediateSigned, caIntermediate, caRoot);

      buildTrustedCertPath(certificateChain, Set.of(caRoot), emptySet());
    }

    // chain: leaf, ca-intermediate, ca-root
    // trusted: ca-intermediate, ca-root
    {
      List<X509Certificate> certificateChain =
          List.of(leafIntermediateSigned, caIntermediate, caRoot);

      buildTrustedCertPath(certificateChain, Set.of(caIntermediate, caRoot), emptySet());
    }
  }

  @Test
  public void testBuildAndValidate_LeafIntermediateSigned_Revoked() {
    // chain: leaf
    // trusted: ca-intermediate
    // issuers: ca-root
    // crls: ca-intermediate revokes leaf
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned);

      UaException e =
          assertThrows(
              UaException.class,
              () -> {
                Set<X509CRL> x509CRLS =
                    Set.of(
                        generateCrl(caRoot, testCertificates.getPrivateKey(ALIAS_CA_ROOT)),
                        generateCrl(
                            caIntermediate,
                            testCertificates.getPrivateKey(ALIAS_CA_INTERMEDIATE),
                            leafIntermediateSigned));

                PKIXCertPathBuilderResult pathBuilderResult =
                    buildTrustedCertPath(certificateChain, Set.of(caIntermediate), Set.of(caRoot));

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    EnumSet.of(ValidationCheck.REVOCATION),
                    true);

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    EnumSet.of(ValidationCheck.REVOCATION),
                    false);
              });

      assertEquals(new StatusCode(StatusCodes.Bad_CertificateRevoked), e.getStatusCode());
    }

    // chain: leaf
    // trusted: ca-intermediate
    // issuers: ca-root
    // crls: ca-root revokes ca-intermediate
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned);

      UaException e =
          assertThrows(
              UaException.class,
              () -> {
                Set<X509CRL> x509CRLS =
                    Set.of(
                        generateCrl(
                            caRoot, testCertificates.getPrivateKey(ALIAS_CA_ROOT), caIntermediate),
                        generateCrl(
                            caIntermediate, testCertificates.getPrivateKey(ALIAS_CA_INTERMEDIATE)));

                PKIXCertPathBuilderResult pathBuilderResult =
                    buildTrustedCertPath(certificateChain, Set.of(caIntermediate), Set.of(caRoot));

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    EnumSet.of(ValidationCheck.REVOCATION),
                    true);

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    EnumSet.of(ValidationCheck.REVOCATION),
                    false);
              });

      assertEquals(new StatusCode(StatusCodes.Bad_CertificateIssuerRevoked), e.getStatusCode());
    }
  }

  @Test
  public void testBuildTrustedCertPath_NoTrusted_NoIssuers() {
    assertThrows(
        UaException.class,
        () -> buildTrustedCertPath(List.of(leafSelfSigned), emptySet(), emptySet()));

    assertThrows(
        UaException.class,
        () -> buildTrustedCertPath(List.of(leafIntermediateSigned), emptySet(), emptySet()));

    assertThrows(
        UaException.class,
        () ->
            buildTrustedCertPath(
                List.of(leafIntermediateSigned, caIntermediate), emptySet(), emptySet()));

    assertThrows(
        UaException.class,
        () ->
            buildTrustedCertPath(
                List.of(leafIntermediateSigned, caIntermediate, caRoot), emptySet(), emptySet()));
  }

  @Test
  public void testBuildTrustedCertPath_IntermediateIssuer() throws Exception {
    // chain: leaf
    // trusted: ca-root
    // issuers: ca-intermediate
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned);

      buildTrustedCertPath(certificateChain, Set.of(caRoot), Set.of(caIntermediate));
    }

    // chain: leaf, ca-intermediate
    // trusted: ca-root
    // issuers: ca-intermediate
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned, caIntermediate);

      buildTrustedCertPath(certificateChain, Set.of(caRoot), Set.of(caIntermediate));
    }

    // chain: leaf, ca-intermediate, ca-root
    // trusted: ca-root
    // issuers: ca-intermediate
    {
      List<X509Certificate> certificateChain =
          List.of(leafIntermediateSigned, caIntermediate, caRoot);

      buildTrustedCertPath(certificateChain, Set.of(caRoot), Set.of(caIntermediate));
    }
  }

  @Test
  public void testBuildAndValidate_IssuerRevoked() {
    // chain: leaf
    // trusted: ca-root
    // issuers: ca-intermediate
    // crls: ca-root revokes ca-intermediate
    {
      List<X509Certificate> certificateChain = List.of(leafIntermediateSigned);

      UaException e =
          assertThrows(
              UaException.class,
              () -> {
                Set<X509CRL> x509CRLS =
                    Set.of(
                        generateCrl(
                            caRoot, testCertificates.getPrivateKey(ALIAS_CA_ROOT), caIntermediate));

                PKIXCertPathBuilderResult pathBuilderResult =
                    buildTrustedCertPath(certificateChain, Set.of(caRoot), Set.of(caIntermediate));

                CertificateValidationUtil.validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    EnumSet.of(ValidationCheck.REVOCATION),
                    true);

                CertificateValidationUtil.validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    EnumSet.of(ValidationCheck.REVOCATION),
                    false);
              });

      assertEquals(new StatusCode(StatusCodes.Bad_CertificateIssuerRevoked), e.getStatusCode());
    }
  }

  @Test
  public void testCertificateIsCa() {
    assertTrue(
        CertificateValidationUtil.certificateIsCa(
            testCertificates.getCertificate(ALIAS_YES_KEY_USAGE_YES_CA)));
    assertTrue(
        CertificateValidationUtil.certificateIsCa(
            testCertificates.getCertificate(ALIAS_NO_KEY_USAGE_YES_CA)));
    assertTrue(
        CertificateValidationUtil.certificateIsCa(
            testCertificates.getCertificate(ALIAS_YES_KEY_USAGE_NO_CA)));
    assertFalse(
        CertificateValidationUtil.certificateIsCa(
            testCertificates.getCertificate(ALIAS_NO_KEY_USAGE_NO_CA)));
  }

  @Test
  public void testCertificateIsSelfSigned() throws Exception {
    assertTrue(CertificateValidationUtil.certificateIsSelfSigned(leafSelfSigned));
    assertTrue(CertificateValidationUtil.certificateIsSelfSigned(caRoot));
    assertFalse(CertificateValidationUtil.certificateIsSelfSigned(leafIntermediateSigned));
    assertFalse(CertificateValidationUtil.certificateIsSelfSigned(caIntermediate));
  }

  @Test
  public void testCertificateIsSelfSigned_MatchingPrincipalsWrongKey() throws Exception {
    KeyPair keyPair1 = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    KeyPair keyPair2 = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    // Intentionally use the wrong private key, so the signature will be invalid.
    X509Certificate certificate =
        createCertificateWithKeys(keyPair1.getPublic(), keyPair2.getPrivate());

    assertFalse(CertificateValidationUtil.certificateIsSelfSigned(certificate));
  }

  @Test
  public void testUriWithSpaces() throws Exception {
    CertificateValidationUtil.checkApplicationUri(uriWithSpaces, "this URI has spaces");
  }

  @Test
  public void testHostnameWithCaseInsensitive() throws Exception {
    String hostname = "digitalpetri.com";
    checkHostnameOrIpAddress(createSelfSignedCertificate("digitalpetri.com"), hostname);
    checkHostnameOrIpAddress(createSelfSignedCertificate("DIGITALPETRI.COM"), hostname);
    checkHostnameOrIpAddress(createSelfSignedCertificate("DIGITALPETRI.com"), hostname);
    checkHostnameOrIpAddress(createSelfSignedCertificate("DigitalPetri.com"), hostname);
  }

  private X509CRL generateCrl(
      X509Certificate ca, PrivateKey caPrivateKey, X509Certificate... revoked) throws Exception {
    X509v2CRLBuilder builder =
        new X509v2CRLBuilder(
            X500Name.getInstance(ca.getSubjectX500Principal().getEncoded()), new Date());

    builder.setNextUpdate(new Date(System.currentTimeMillis() + 60_000));

    for (X509Certificate certificate : revoked) {
      builder.addCRLEntry(
          certificate.getSerialNumber(),
          new Date(System.currentTimeMillis() - 60_000),
          CRLReason.privilegeWithdrawn);
    }

    JcaContentSignerBuilder contentSignerBuilder =
        new JcaContentSignerBuilder("SHA256WithRSAEncryption");

    contentSignerBuilder.setProvider("BC");

    X509CRLHolder crlHolder = builder.build(contentSignerBuilder.build(caPrivateKey));

    JcaX509CRLConverter converter = new JcaX509CRLConverter();

    converter.setProvider("BC");

    return converter.getCRL(crlHolder);
  }

  private static X509Certificate createSelfSignedCertificate(String dnsName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedCertificateBuilder builder =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName(dnsName);

    return builder.build();
  }

  private static X509Certificate createCertificateWithKeys(
      PublicKey publicKey, PrivateKey privateKey) throws Exception {

    X500Name subject = new X500Name("CN=Test Certificate");
    BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
    Date notBefore = new Date(System.currentTimeMillis() - 86400000);
    Date notAfter = new Date(System.currentTimeMillis() + 31536000000L);

    SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());

    X509v3CertificateBuilder certBuilder =
        new X509v3CertificateBuilder(
            subject, serialNumber, notBefore, notAfter, subject, publicKeyInfo);

    ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);

    return new JcaX509CertificateConverter().getCertificate(certBuilder.build(signer));
  }
}
