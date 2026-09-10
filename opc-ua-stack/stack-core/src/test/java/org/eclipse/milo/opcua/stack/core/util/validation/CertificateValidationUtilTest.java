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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertPathBuilderException;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.CrlTestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.TestCertificateGenerator.TestCertificates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CertificateValidationUtilTest {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private static final long ONE_DAY_MS = 24L * 60 * 60 * 1000;

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
  void profileAwareEccValidationAcceptsMinimalSelfSignedCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .build();

    PKIXCertPathBuilderResult result =
        buildTrustedCertPath(List.of(certificate), Set.of(certificate), emptySet());

    validateTrustedCertPath(
        result.getCertPath(),
        result.getTrustAnchor(),
        emptySet(),
        ValidationCheck.ALL_OPTIONAL_CHECKS,
        false,
        SecurityPolicy.ECC_nistP256_AesGcm.getProfile());
  }

  @Test
  void legacyValidationRejectsMinimalEccCertificateWithoutProfile() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .build();

    PKIXCertPathBuilderResult result =
        buildTrustedCertPath(List.of(certificate), Set.of(certificate), emptySet());

    assertThrows(
        UaException.class,
        () ->
            validateTrustedCertPath(
                result.getCertPath(),
                result.getTrustAnchor(),
                emptySet(),
                ValidationCheck.ALL_OPTIONAL_CHECKS,
                false));
  }

  @Test
  void profileAwareEccValidationRejectsMissingDigitalSignature() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(
                keyPair, new KeyUsageCertificateGenerator(KeyUsage.keyCertSign))
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .build();

    PKIXCertPathBuilderResult result =
        buildTrustedCertPath(List.of(certificate), Set.of(certificate), emptySet());

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validateTrustedCertPath(
                    result.getCertPath(),
                    result.getTrustAnchor(),
                    emptySet(),
                    ValidationCheck.ALL_OPTIONAL_CHECKS,
                    false,
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  // Part 4 §6.1.3 allows the validity-period error to be suppressed, and VALIDITY is absent from
  // NO_OPTIONAL_CHECKS, so an expired but trusted self-signed certificate is accepted by default.
  @Test
  void expiredTrustedSelfSignedCertificateIsAcceptedWhenValidityCheckIsSuppressed()
      throws Exception {
    X509Certificate expired = createExpiredSelfSignedCertificate();

    PKIXCertPathBuilderResult result =
        buildTrustedCertPath(List.of(expired), Set.of(expired), emptySet());

    validateTrustedCertPath(
        result.getCertPath(),
        result.getTrustAnchor(),
        emptySet(),
        ValidationCheck.NO_OPTIONAL_CHECKS,
        true);
  }

  @Test
  void expiredTrustedSelfSignedCertificateIsRejectedWhenValidityCheckIsEnabled() throws Exception {
    X509Certificate expired = createExpiredSelfSignedCertificate();

    PKIXCertPathBuilderResult result =
        buildTrustedCertPath(List.of(expired), Set.of(expired), emptySet());

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                validateTrustedCertPath(
                    result.getCertPath(),
                    result.getTrustAnchor(),
                    emptySet(),
                    Set.of(ValidationCheck.VALIDITY),
                    true));

    assertEquals(StatusCodes.Bad_CertificateTimeInvalid, e.getStatusCode().value());
  }

  // Suppression stops at the trust anchor. The JDK checks the validity period of every certificate
  // inside a path while building it and offers no way to relax that, so an expired CA-issued
  // certificate never reaches the VALIDITY decision. Preserve the path-building failure because
  // the builder does not establish that validity is the only reason no path could be built.
  @Test
  void expiredCaIssuedCertificateIsRejectedWhilePathIsBuilt() throws Exception {
    long now = System.currentTimeMillis();
    X509Certificate expiredLeaf =
        new CaSignedCertificateBuilder(
                SelfSignedCertificateGenerator.generateRsaKeyPair(2048),
                caIntermediate,
                testCertificates.getPrivateKey(ALIAS_CA_INTERMEDIATE))
            .setCommonName("Test Expired Leaf")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:eclipse:milo:test:expired-leaf")
            .setValidity(new Date(now - 2 * ONE_DAY_MS), new Date(now - ONE_DAY_MS))
            .setIsCa(false)
            .setKeyUsage(
                KeyUsage.digitalSignature
                    | KeyUsage.nonRepudiation
                    | KeyUsage.keyEncipherment
                    | KeyUsage.dataEncipherment)
            .setExtendedKeyUsage(
                List.of(KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth))
            .build();

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                buildTrustedCertPath(List.of(expiredLeaf), Set.of(caIntermediate), Set.of(caRoot)));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, e.getStatusCode().value());
    assertInstanceOf(CertPathBuilderException.class, e.getCause());
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

  // A revocation established by an available CRL is enforced even under the default policy, which
  // tolerates missing CRLs but never ignores one that is present (Part 4 §6.1.3).
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
                        CrlTestUtil.generateCrl(
                            caRoot, testCertificates.getPrivateKey(ALIAS_CA_ROOT)),
                        CrlTestUtil.generateCrl(
                            caIntermediate,
                            testCertificates.getPrivateKey(ALIAS_CA_INTERMEDIATE),
                            leafIntermediateSigned));

                PKIXCertPathBuilderResult pathBuilderResult =
                    buildTrustedCertPath(certificateChain, Set.of(caIntermediate), Set.of(caRoot));

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    ValidationCheck.NO_OPTIONAL_CHECKS,
                    true);

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    ValidationCheck.NO_OPTIONAL_CHECKS,
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
                        CrlTestUtil.generateCrl(
                            caRoot, testCertificates.getPrivateKey(ALIAS_CA_ROOT), caIntermediate),
                        CrlTestUtil.generateCrl(
                            caIntermediate, testCertificates.getPrivateKey(ALIAS_CA_INTERMEDIATE)));

                PKIXCertPathBuilderResult pathBuilderResult =
                    buildTrustedCertPath(certificateChain, Set.of(caIntermediate), Set.of(caRoot));

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    ValidationCheck.NO_OPTIONAL_CHECKS,
                    true);

                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    ValidationCheck.NO_OPTIONAL_CHECKS,
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
                        CrlTestUtil.generateCrl(
                            caRoot, testCertificates.getPrivateKey(ALIAS_CA_ROOT), caIntermediate));

                PKIXCertPathBuilderResult pathBuilderResult =
                    buildTrustedCertPath(certificateChain, Set.of(caRoot), Set.of(caIntermediate));

                CertificateValidationUtil.validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    ValidationCheck.NO_OPTIONAL_CHECKS,
                    true);

                CertificateValidationUtil.validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    x509CRLS,
                    ValidationCheck.NO_OPTIONAL_CHECKS,
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

  @Test
  public void testIpv4Address() throws Exception {
    X509Certificate certificate = createSelfSignedCertificateWithIpAddress("192.168.0.1");

    checkHostnameOrIpAddress(certificate, "192.168.0.1");
  }

  // EndpointUtil.getHost() returns IPv6 hosts in the bracketed form they have in the endpoint URL,
  // and that value is what the client passes here (SessionFsmFactory, UsernameProvider). The
  // certificate stores the address unbracketed.
  @Test
  public void testIpv6AddressInBracketedUriForm() throws Exception {
    X509Certificate certificate = createSelfSignedCertificateWithIpAddress("2001:db8::1");

    checkHostnameOrIpAddress(certificate, "[2001:db8::1]");
  }

  // The JDK renders a SubjectAltName IPAddress entry from its raw bytes, so the text in the
  // certificate need not use the same notation the caller does.
  @Test
  public void testIpv6AddressNotationDiffers() throws Exception {
    X509Certificate certificate = createSelfSignedCertificateWithIpAddress("2001:db8::1");

    checkHostnameOrIpAddress(certificate, "2001:db8::1");
    checkHostnameOrIpAddress(certificate, "2001:0db8:0000:0000:0000:0000:0000:0001");
    checkHostnameOrIpAddress(certificate, "2001:DB8::1");
  }

  @Test
  public void testNonMatchingIpAddressIsRejected() throws Exception {
    X509Certificate certificate = createSelfSignedCertificateWithIpAddress("2001:db8::1");

    assertThrows(UaException.class, () -> checkHostnameOrIpAddress(certificate, "[2001:db8::2]"));
    assertThrows(UaException.class, () -> checkHostnameOrIpAddress(certificate, "192.168.0.1"));
  }

  // A host name is matched against DNSName entries only; it must never be resolved to an address
  // and compared against IPAddress entries. Names containing ':' are included because
  // InetAddress.getByName() would hand those to the system resolver.
  @Test
  public void testHostNameIsNotResolvedToIpAddress() throws Exception {
    X509Certificate certificate = createSelfSignedCertificateWithIpAddress("127.0.0.1");

    assertThrows(UaException.class, () -> checkHostnameOrIpAddress(certificate, "localhost"));
    assertThrows(UaException.class, () -> checkHostnameOrIpAddress(certificate, "[not:an:ip]"));
    assertThrows(UaException.class, () -> checkHostnameOrIpAddress(certificate, "not:an:ip"));
  }

  /**
   * A Global Discovery Server certificate group that offers more than one certificate type issues
   * one CA per type, all under the group's single configured subject name, and publishes a CRL for
   * each. The trust list a client pulls then holds several same-named CAs, and PKIX chooses
   * candidate CRLs by issuer name.
   */
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void revocationResolvesWhenSeveralCasShareASubjectName(boolean requireCrls) throws Exception {
    String sharedSubject = "Plant Default CA";

    KeyPair issuingCaKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate issuingCa = createSharedNameCa(issuingCaKeyPair, sharedSubject);

    // The second CA of the group is ECC, as it would be for a group that offers an ECC certificate
    // type alongside RSA.
    KeyPair sameNameCaKeyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate sameNameCa = createSharedNameCa(sameNameCaKeyPair, sharedSubject);

    KeyPair leafKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate leaf =
        new CaSignedCertificateBuilder(leafKeyPair, issuingCa, issuingCaKeyPair.getPrivate())
            .setCommonName("Ignition OPC UA Client")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:eclipse:milo:test:shared-name-ca-client")
            .addDnsName("localhost")
            .setIsCa(false)
            .setKeyUsage(
                KeyUsage.digitalSignature
                    | KeyUsage.nonRepudiation
                    | KeyUsage.keyEncipherment
                    | KeyUsage.dataEncipherment)
            .setExtendedKeyUsage(
                List.of(KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth))
            .build();

    Set<X509CRL> crls =
        Set.of(
            CrlTestUtil.generateCrl(issuingCa, issuingCaKeyPair.getPrivate()),
            CrlTestUtil.generateCrl(sameNameCa, sameNameCaKeyPair.getPrivate()));

    PKIXCertPathBuilderResult pathBuilderResult =
        buildTrustedCertPath(List.of(leaf), Set.of(issuingCa, sameNameCa), emptySet());

    // Both CRLs are candidates by issuer name and only one of them verifies. Passing both to PKIX
    // unfiltered yields Bad_CertificateRevocationUnknown instead of a successful validation.
    validateTrustedCertPath(
        pathBuilderResult.getCertPath(),
        pathBuilderResult.getTrustAnchor(),
        crls,
        revocationPolicy(requireCrls),
        true);
  }

  /**
   * The revoking CRL must still be honored when a same-named CA's CRL is alongside it. This matters
   * most when unknown status is tolerated: handed both CRLs unfiltered, PKIX reports the status as
   * unknown, and the tolerated unknown status would hide the revocation.
   */
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void revocationIsDetectedWhenSeveralCasShareASubjectName(boolean requireCrls) throws Exception {
    String sharedSubject = "Plant Default CA";

    KeyPair issuingCaKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate issuingCa = createSharedNameCa(issuingCaKeyPair, sharedSubject);

    // The second CA of the group is ECC, as it would be for a group that offers an ECC certificate
    // type alongside RSA.
    KeyPair sameNameCaKeyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate sameNameCa = createSharedNameCa(sameNameCaKeyPair, sharedSubject);

    KeyPair leafKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate leaf =
        new CaSignedCertificateBuilder(leafKeyPair, issuingCa, issuingCaKeyPair.getPrivate())
            .setCommonName("Ignition OPC UA Client")
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:eclipse:milo:test:shared-name-ca-client-revoked")
            .addDnsName("localhost")
            .setIsCa(false)
            .setKeyUsage(
                KeyUsage.digitalSignature
                    | KeyUsage.nonRepudiation
                    | KeyUsage.keyEncipherment
                    | KeyUsage.dataEncipherment)
            .setExtendedKeyUsage(
                List.of(KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth))
            .build();

    Set<X509CRL> crls =
        Set.of(
            CrlTestUtil.generateCrl(issuingCa, issuingCaKeyPair.getPrivate(), leaf),
            CrlTestUtil.generateCrl(sameNameCa, sameNameCaKeyPair.getPrivate()));

    PKIXCertPathBuilderResult pathBuilderResult =
        buildTrustedCertPath(List.of(leaf), Set.of(issuingCa, sameNameCa), emptySet());

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                validateTrustedCertPath(
                    pathBuilderResult.getCertPath(),
                    pathBuilderResult.getTrustAnchor(),
                    crls,
                    revocationPolicy(requireCrls),
                    true));

    assertEquals(new StatusCode(StatusCodes.Bad_CertificateRevoked), e.getStatusCode());
  }

  private static Set<ValidationCheck> revocationPolicy(boolean requireCrls) {
    return requireCrls
        ? Set.of(ValidationCheck.REVOCATION_LISTS)
        : ValidationCheck.NO_OPTIONAL_CHECKS;
  }

  private static X509Certificate createSharedNameCa(KeyPair keyPair, String commonName)
      throws Exception {

    return new SelfSignedCertificateBuilder(keyPair, new CaCertificateGenerator())
        .setCommonName(commonName)
        .setOrganization("Ignition QA")
        .setApplicationUri("urn:eclipse:milo:test:" + commonName.toLowerCase().replace(" ", "-"))
        .build();
  }

  private static X509Certificate createSelfSignedCertificate(String dnsName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedCertificateBuilder builder =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName(dnsName);

    return builder.build();
  }

  private static X509Certificate createSelfSignedCertificateWithIpAddress(String ipAddress)
      throws Exception {

    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedCertificateBuilder builder =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addIpAddress(ipAddress);

    return builder.build();
  }

  private static X509Certificate createExpiredSelfSignedCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    long now = System.currentTimeMillis();

    return createCertificateWithKeys(
        keyPair.getPublic(),
        keyPair.getPrivate(),
        new Date(now - 2 * ONE_DAY_MS),
        new Date(now - ONE_DAY_MS));
  }

  private static X509Certificate createCertificateWithKeys(
      PublicKey publicKey, PrivateKey privateKey) throws Exception {

    return createCertificateWithKeys(
        publicKey,
        privateKey,
        new Date(System.currentTimeMillis() - ONE_DAY_MS),
        new Date(System.currentTimeMillis() + 365 * ONE_DAY_MS));
  }

  private static X509Certificate createCertificateWithKeys(
      PublicKey publicKey, PrivateKey privateKey, Date notBefore, Date notAfter) throws Exception {

    X500Name subject = new X500Name("CN=Test Certificate");
    BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

    SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());

    X509v3CertificateBuilder certBuilder =
        new X509v3CertificateBuilder(
            subject, serialNumber, notBefore, notAfter, subject, publicKeyInfo);

    JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SHA256WithRSA");
    signerBuilder.setProvider("BC");
    ContentSigner signer = signerBuilder.build(privateKey);

    JcaX509CertificateConverter converter = new JcaX509CertificateConverter();
    converter.setProvider("BC");
    return converter.getCertificate(certBuilder.build(signer));
  }

  /** Generates a self-signed CA certificate that can sign certificates and CRLs. */
  private static final class CaCertificateGenerator extends SelfSignedCertificateGenerator {

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {}

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) throws CertIOException {
      certificateBuilder.addExtension(
          Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
    }

    @Override
    protected void addBasicConstraints(
        X509v3CertificateBuilder certificateBuilder, BasicConstraints basicConstraints)
        throws CertIOException {

      certificateBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
    }
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
