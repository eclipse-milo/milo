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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Verifies that Subject Alternative Name parsing reads every entry independently through Bouncy
 * Castle and that {@link SelfSignedCertificateBuilder} does not write an empty URI by default.
 *
 * <p>OPC 10000-6 §6.2.2 places the application URI, DNS names, and IP addresses together in {@code
 * subjectAltName}, and OPC 10000-4 §6.1.3 validates the HostName step against those DNS names and
 * IP addresses. The JDK parser rejects the whole extension when a single URI is unparseable (such
 * as an empty string from an older certificate or an application URI containing spaces).
 */
@NullMarked
class SubjectAltNameParsingTest {

  private static final String DNS_NAME = "plc-1.local";
  private static final String IPV4_ADDRESS = "10.0.0.5";
  private static final String IPV6_ADDRESS = "2001:db8::1";
  private static final String IPV6_CANONICAL = "2001:db8:0:0:0:0:0:1";

  private static KeyPair keyPair;

  @BeforeAll
  static void generateKeyPair() throws Exception {
    keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
  }

  static Stream<Arguments> applicationUriCases() {
    return Stream.of(
        arguments(
            "valid URN",
            certificateFactory(
                () ->
                    SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
                        .setCommonName("Test")
                        .setApplicationUri("urn:eclipse:milo:test:server")
                        .addDnsName(DNS_NAME)
                        .addIpAddress(IPV4_ADDRESS)
                        .addIpAddress(IPV6_ADDRESS)
                        .build()),
            "urn:eclipse:milo:test:server"),
        arguments(
            "URI with spaces",
            certificateFactory(
                () ->
                    SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
                        .setCommonName("Test")
                        .setApplicationUri("this URI has spaces")
                        .addDnsName(DNS_NAME)
                        .addIpAddress(IPV4_ADDRESS)
                        .addIpAddress(IPV6_ADDRESS)
                        .build()),
            "this URI has spaces"),
        arguments(
            "legacy empty URI",
            certificateFactory(
                () ->
                    new SelfSignedCertificateBuilder(keyPair, new RawUriCertificateGenerator(""))
                        .setCommonName("Test")
                        .addDnsName(DNS_NAME)
                        .addIpAddress(IPV4_ADDRESS)
                        .addIpAddress(IPV6_ADDRESS)
                        .build()),
            null),
        arguments(
            "legacy blank URI",
            certificateFactory(
                () ->
                    new SelfSignedCertificateBuilder(keyPair, new RawUriCertificateGenerator("   "))
                        .setCommonName("Test")
                        .addDnsName(DNS_NAME)
                        .addIpAddress(IPV4_ADDRESS)
                        .addIpAddress(IPV6_ADDRESS)
                        .build()),
            null),
        arguments(
            "unset URI",
            certificateFactory(
                () ->
                    SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
                        .setCommonName("Test")
                        .addDnsName(DNS_NAME)
                        .addIpAddress(IPV4_ADDRESS)
                        .addIpAddress(IPV6_ADDRESS)
                        .build()),
            null));
  }

  // OPC 10000-4 §6.1.3 checks HostName against DNSName and IPAddress entries; an unparseable URI
  // in subjectAltName must never hide valid host names, IP addresses, or CSR SAN fields.
  @ParameterizedTest(name = "{0}")
  @MethodSource("applicationUriCases")
  void dnsNamesIpAddressesAndCsrAreReadWhateverTheApplicationUri(
      String label, CertificateFactory factory, @Nullable String expectedUri) throws Exception {

    X509Certificate certificate = factory.create();

    List<String> dnsNames = CertificateUtil.getSanDnsNames(certificate);
    List<String> ipAddresses = CertificateUtil.getSanIpAddresses(certificate);
    Optional<String> sanUri = CertificateUtil.getSanUri(certificate);
    String validationUri = CertificateValidationUtil.getSubjectAltNameUri(certificate);
    GeneralNames csrSans = csrSubjectAltNames(CertificateUtil.generateCsr(keyPair, certificate));

    assertAll(
        label,
        () -> assertEquals(List.of(DNS_NAME), dnsNames, "getSanDnsNames"),
        () -> assertEquals(List.of(IPV4_ADDRESS, IPV6_CANONICAL), ipAddresses, "getSanIpAddresses"),
        () -> assertEquals(Optional.ofNullable(expectedUri), sanUri, "getSanUri"),
        () -> assertEquals(expectedUri, validationUri, "getSubjectAltNameUri"),
        () ->
            assertDoesNotThrow(
                () -> CertificateValidationUtil.checkHostnameOrIpAddress(certificate, DNS_NAME)),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkHostnameOrIpAddress(
                        certificate, DNS_NAME.toUpperCase())),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkHostnameOrIpAddress(certificate, IPV4_ADDRESS)),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkHostnameOrIpAddress(certificate, IPV6_ADDRESS)),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkHostnameOrIpAddress(
                        certificate, "[" + IPV6_ADDRESS + "]")),
        () -> {
          UaException e =
              assertThrows(
                  UaException.class,
                  () ->
                      CertificateValidationUtil.checkHostnameOrIpAddress(
                          certificate, "other.local"));
          assertEquals(StatusCodes.Bad_CertificateHostNameInvalid, e.getStatusCode().value());
        },
        () -> {
          if (expectedUri != null) {
            assertDoesNotThrow(
                () -> CertificateValidationUtil.checkApplicationUri(certificate, expectedUri));
          } else {
            UaException emptyError =
                assertThrows(
                    UaException.class,
                    () -> CertificateValidationUtil.checkApplicationUri(certificate, ""));
            assertEquals(StatusCodes.Bad_CertificateUriInvalid, emptyError.getStatusCode().value());
          }
          UaException e =
              assertThrows(
                  UaException.class,
                  () ->
                      CertificateValidationUtil.checkApplicationUri(
                          certificate, "urn:eclipse:milo:test:other"));
          assertEquals(StatusCodes.Bad_CertificateUriInvalid, e.getStatusCode().value());
        },
        () -> {
          assertNotNull(csrSans, "CSR should contain subjectAltName");
          List<GeneralName> expectedNames = new ArrayList<>();
          if (expectedUri != null) {
            expectedNames.add(new GeneralName(GeneralName.uniformResourceIdentifier, expectedUri));
          }
          expectedNames.add(new GeneralName(GeneralName.dNSName, DNS_NAME));
          expectedNames.add(new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS));
          expectedNames.add(new GeneralName(GeneralName.iPAddress, IPV6_ADDRESS));
          assertEquals(expectedNames, Arrays.asList(csrSans.getNames()), "CSR subjectAltName");
        });
  }

  static Stream<Arguments> omittedUriConfigurations() {
    return Stream.of(
        arguments("unset", builder(b -> b)),
        arguments("null", builder(b -> b.setApplicationUri(null))),
        arguments("empty", builder(b -> b.setApplicationUri(""))),
        arguments("blank", builder(b -> b.setApplicationUri("   "))));
  }

  // SelfSignedCertificateBuilder must not emit an empty URI entry by default or for blank input,
  // because the JDK's X509Certificate.getSubjectAlternativeNames() rejects an empty URI as invalid.
  @ParameterizedTest(name = "applicationUri {0}")
  @MethodSource("omittedUriConfigurations")
  void selfSignedCertificateBuilderOmitsEmptyAndBlankApplicationUri(
      String label, UnaryOperator<SelfSignedCertificateBuilder> configure) throws Exception {

    X509Certificate certificate =
        configure
            .apply(
                SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
                    .setCommonName("Test")
                    .addDnsName(DNS_NAME)
                    .addIpAddress(IPV4_ADDRESS))
            .build();

    GeneralNames encodedNames = certificateSubjectAltNames(certificate);

    assertAll(
        label,
        () -> assertNotNull(encodedNames, "subjectAltName extension should exist for DNS/IP"),
        () ->
            assertTrue(
                Arrays.stream(encodedNames.getNames())
                    .noneMatch(n -> n.getTagNo() == GeneralName.uniformResourceIdentifier),
                "no URI GeneralName should be written"),
        () -> assertNotNull(certificate.getSubjectAlternativeNames(), "JDK SAN parser succeeds"),
        () -> assertEquals(Optional.empty(), CertificateUtil.getSanUri(certificate)),
        () -> assertNull(CertificateValidationUtil.getSubjectAltNameUri(certificate)),
        () -> {
          UaException e =
              assertThrows(
                  UaException.class,
                  () ->
                      CertificateValidationUtil.checkApplicationUri(
                          certificate, "urn:eclipse:milo:test"));
          assertEquals(StatusCodes.Bad_CertificateUriInvalid, e.getStatusCode().value());
        });
  }

  // RFC 5280 §4.2.1.6 defines GeneralNames as SEQUENCE SIZE (1..MAX). When no SAN entries are
  // configured, the extension must be omitted rather than writing an empty sequence.
  @Test
  void selfSignedCertificateBuilderOmitsSubjectAltNameExtensionWhenNoNamesAreConfigured()
      throws Exception {

    X509Certificate certificate =
        SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
            .setCommonName("No SAN")
            .build();

    assertNull(
        certificateSubjectAltNames(certificate), "subjectAltName extension should be absent");
    assertNull(certificate.getSubjectAlternativeNames(), "JDK SAN parser should return null");
    assertEquals(Optional.empty(), CertificateUtil.getSanUri(certificate));
    assertEquals(List.of(), CertificateUtil.getSanDnsNames(certificate));
    assertEquals(List.of(), CertificateUtil.getSanIpAddresses(certificate));

    PKCS10CertificationRequest csr = CertificateUtil.generateCsr(keyPair, certificate);
    assertNull(csrSubjectAltNames(csr), "CSR should omit empty subjectAltName extension");
  }

  // A malformed IPAddress length (neither 4 nor 16 bytes) must skip only that entry across both
  // public readers and CSR generation so valid DNS names, valid IPs, and URIs remain intact.
  @Test
  void malformedIpAddressEntryDoesNotHideValidSubjectAltNames() throws Exception {
    GeneralNames generalNames =
        new GeneralNames(
            new GeneralName[] {
              new GeneralName(GeneralName.uniformResourceIdentifier, "urn:eclipse:milo:test:mixed"),
              new GeneralName(GeneralName.dNSName, DNS_NAME),
              new GeneralName(GeneralName.iPAddress, new DEROctetString(new byte[] {1, 2, 3})),
              new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)
            });

    X509Certificate certificate = buildCertificateWithSanExtension(generalNames.getEncoded());
    GeneralNames csrSans = csrSubjectAltNames(CertificateUtil.generateCsr(keyPair, certificate));

    assertEquals(
        Optional.of("urn:eclipse:milo:test:mixed"), CertificateUtil.getSanUri(certificate));
    assertEquals(List.of(DNS_NAME), CertificateUtil.getSanDnsNames(certificate));
    assertEquals(List.of(IPV4_ADDRESS), CertificateUtil.getSanIpAddresses(certificate));
    assertDoesNotThrow(
        () -> CertificateValidationUtil.checkHostnameOrIpAddress(certificate, IPV4_ADDRESS));
    assertNotNull(csrSans, "CSR should contain valid subjectAltName entries");
    assertEquals(
        List.of(
            new GeneralName(GeneralName.uniformResourceIdentifier, "urn:eclipse:milo:test:mixed"),
            new GeneralName(GeneralName.dNSName, DNS_NAME),
            new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)),
        Arrays.asList(csrSans.getNames()),
        "CSR should omit malformed IPAddress entries");
  }

  // Each GeneralName in the SAN sequence must be decoded independently so an unknown ASN.1 tag or
  // malformed directoryName (which can throw IllegalArgumentException or IllegalStateException in
  // Bouncy Castle) skips only that entry and never hides valid sibling DNS, IP, or URI names.
  @Test
  void unknownTagAndMalformedDirectoryNameDoNotHideSiblingSubjectAltNames() throws Exception {
    var sanSequence =
        new DERSequence(
            new ASN1Encodable[] {
              new DERTaggedObject(false, 9, new DEROctetString(new byte[] {1, 2, 3})),
              new DERTaggedObject(
                  true, GeneralName.directoryName, new DEROctetString(new byte[] {4, 5, 6})),
              new DERTaggedObject(
                  false, GeneralName.directoryName, new DEROctetString(new byte[] {7, 8, 9})),
              new GeneralName(
                  GeneralName.uniformResourceIdentifier, "urn:eclipse:milo:test:mixed-tags"),
              new GeneralName(GeneralName.dNSName, DNS_NAME),
              new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)
            });

    X509Certificate certificate = buildCertificateWithSanExtension(sanSequence.getEncoded());
    GeneralNames csrSans = csrSubjectAltNames(CertificateUtil.generateCsr(keyPair, certificate));

    assertAll(
        () ->
            assertEquals(
                Optional.of("urn:eclipse:milo:test:mixed-tags"),
                CertificateUtil.getSanUri(certificate)),
        () ->
            assertEquals(
                "urn:eclipse:milo:test:mixed-tags",
                CertificateValidationUtil.getSubjectAltNameUri(certificate)),
        () -> assertEquals(List.of(DNS_NAME), CertificateUtil.getSanDnsNames(certificate)),
        () -> assertEquals(List.of(IPV4_ADDRESS), CertificateUtil.getSanIpAddresses(certificate)),
        () ->
            assertEquals(
                List.of(),
                CertificateUtil.getSubjectAltNameField(certificate, GeneralName.directoryName)),
        () ->
            assertDoesNotThrow(
                () -> CertificateValidationUtil.checkHostnameOrIpAddress(certificate, DNS_NAME)),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkHostnameOrIpAddress(certificate, IPV4_ADDRESS)),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkApplicationUri(
                        certificate, "urn:eclipse:milo:test:mixed-tags")),
        () -> {
          assertNotNull(csrSans, "CSR should contain valid subjectAltName entries");
          assertEquals(
              List.of(
                  new GeneralName(
                      GeneralName.uniformResourceIdentifier, "urn:eclipse:milo:test:mixed-tags"),
                  new GeneralName(GeneralName.dNSName, DNS_NAME),
                  new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)),
              Arrays.asList(csrSans.getNames()));
        });
  }

  // RFC 5280 defines rfc822Name, dNSName, and uniformResourceIdentifier as IA5String (7-bit
  // ASCII). Non-IA5 octets (> 0x7F) must be rejected consistently across readers, validation, and
  // CSR generation while pure-ASCII URIs with spaces remain readable.
  @Test
  void nonIa5EntriesAreRejectedAcrossReadersAndCsrWhileAsciiSpaceUriStillWorks() throws Exception {
    byte[] nonIa5DnsBytes = "plc-\u00e9.local".getBytes(StandardCharsets.ISO_8859_1);
    byte[] nonIa5EmailBytes = "ops-\u00e9@example.com".getBytes(StandardCharsets.ISO_8859_1);

    var sanSequence =
        new DERSequence(
            new ASN1Encodable[] {
              new DERTaggedObject(false, GeneralName.dNSName, new DEROctetString(nonIa5DnsBytes)),
              new DERTaggedObject(
                  false, GeneralName.rfc822Name, new DEROctetString(nonIa5EmailBytes)),
              new GeneralName(GeneralName.uniformResourceIdentifier, "urn:example:my plc server"),
              new GeneralName(GeneralName.dNSName, DNS_NAME),
              new GeneralName(GeneralName.rfc822Name, "ops@example.com"),
              new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)
            });

    X509Certificate certificate = buildCertificateWithSanExtension(sanSequence.getEncoded());
    GeneralNames csrSans = csrSubjectAltNames(CertificateUtil.generateCsr(keyPair, certificate));

    assertAll(
        () ->
            assertEquals(
                Optional.of("urn:example:my plc server"), CertificateUtil.getSanUri(certificate)),
        () -> assertEquals(List.of(DNS_NAME), CertificateUtil.getSanDnsNames(certificate)),
        () ->
            assertEquals(
                List.of("ops@example.com"),
                CertificateUtil.getSubjectAltNameField(certificate, GeneralName.rfc822Name)),
        () -> assertEquals(List.of(IPV4_ADDRESS), CertificateUtil.getSanIpAddresses(certificate)),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkApplicationUri(
                        certificate, "urn:example:my plc server")),
        () ->
            assertDoesNotThrow(
                () -> CertificateValidationUtil.checkHostnameOrIpAddress(certificate, DNS_NAME)),
        () -> {
          UaException hostError =
              assertThrows(
                  UaException.class,
                  () ->
                      CertificateValidationUtil.checkHostnameOrIpAddress(
                          certificate, "plc-\u00e9.local"));
          assertEquals(
              StatusCodes.Bad_CertificateHostNameInvalid, hostError.getStatusCode().value());
        },
        () -> {
          assertNotNull(csrSans, "CSR should contain only valid IA5/IP subjectAltName entries");
          assertEquals(
              List.of(
                  new GeneralName(
                      GeneralName.uniformResourceIdentifier, "urn:example:my plc server"),
                  new GeneralName(GeneralName.dNSName, DNS_NAME),
                  new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)),
              Arrays.asList(csrSans.getNames()));
        });

    // A sole non-IA5 URI must be ignored by URI readers and CSR generation while sibling DNS and
    // IP entries remain readable.
    byte[] nonIa5UriBytes = "urn:example:caf\u00e9".getBytes(StandardCharsets.ISO_8859_1);
    var nonIa5UriSequence =
        new DERSequence(
            new ASN1Encodable[] {
              new DERTaggedObject(
                  false, GeneralName.uniformResourceIdentifier, new DEROctetString(nonIa5UriBytes)),
              new GeneralName(GeneralName.dNSName, DNS_NAME),
              new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)
            });

    X509Certificate nonIa5UriCert =
        buildCertificateWithSanExtension(nonIa5UriSequence.getEncoded());
    GeneralNames nonIa5CsrSans =
        csrSubjectAltNames(CertificateUtil.generateCsr(keyPair, nonIa5UriCert));

    assertAll(
        () -> assertEquals(Optional.empty(), CertificateUtil.getSanUri(nonIa5UriCert)),
        () -> assertNull(CertificateValidationUtil.getSubjectAltNameUri(nonIa5UriCert)),
        () -> {
          UaException uriError =
              assertThrows(
                  UaException.class,
                  () ->
                      CertificateValidationUtil.checkApplicationUri(
                          nonIa5UriCert, "urn:example:caf\u00e9"));
          assertEquals(StatusCodes.Bad_CertificateUriInvalid, uriError.getStatusCode().value());
        },
        () -> assertEquals(List.of(DNS_NAME), CertificateUtil.getSanDnsNames(nonIa5UriCert)),
        () -> assertEquals(List.of(IPV4_ADDRESS), CertificateUtil.getSanIpAddresses(nonIa5UriCert)),
        () -> {
          assertNotNull(nonIa5CsrSans, "CSR should retain valid sibling DNS/IP entries");
          assertEquals(
              List.of(
                  new GeneralName(GeneralName.dNSName, DNS_NAME),
                  new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)),
              Arrays.asList(nonIa5CsrSans.getNames()));
        });
  }

  static Stream<Arguments> multiUriCases() {
    byte[] nonIa5UriBytes = "urn:example:caf\u00e9".getBytes(StandardCharsets.ISO_8859_1);

    return Stream.of(
        arguments(
            "space URI then matching URI",
            new ASN1Encodable[] {
              new GeneralName(GeneralName.uniformResourceIdentifier, "this URI has spaces"),
              new GeneralName(
                  GeneralName.uniformResourceIdentifier, "urn:eclipse:milo:test:expected"),
              new GeneralName(GeneralName.dNSName, DNS_NAME),
              new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)
            }),
        arguments(
            "non-IA5 URI then matching URI",
            new ASN1Encodable[] {
              new DERTaggedObject(
                  false, GeneralName.uniformResourceIdentifier, new DEROctetString(nonIa5UriBytes)),
              new GeneralName(
                  GeneralName.uniformResourceIdentifier, "urn:eclipse:milo:test:expected"),
              new GeneralName(GeneralName.dNSName, DNS_NAME),
              new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)
            }),
        arguments(
            "empty URI then matching URI",
            new ASN1Encodable[] {
              new GeneralName(GeneralName.uniformResourceIdentifier, ""),
              new GeneralName(
                  GeneralName.uniformResourceIdentifier, "urn:eclipse:milo:test:expected"),
              new GeneralName(GeneralName.dNSName, DNS_NAME),
              new GeneralName(GeneralName.iPAddress, IPV4_ADDRESS)
            }));
  }

  // OPC 10000-6 §6.2.2 requires subjectAltName to contain strictly one uniformResourceIdentifier.
  // Counting raw URI-tagged entries before decoding ensures a malformed or blank sibling URI
  // cannot bypass the single-URI rule, while DNS and IP host-name checks remain independent.
  @ParameterizedTest(name = "{0}")
  @MethodSource("multiUriCases")
  void checkApplicationUriRejectsCertificateWithMultipleUrisEvenWhenOneIsMalformed(
      String label, ASN1Encodable[] sanElements) throws Exception {

    X509Certificate certificate =
        buildCertificateWithSanExtension(new DERSequence(sanElements).getEncoded());

    UaException expectedMatchError =
        assertThrows(
            UaException.class,
            () ->
                CertificateValidationUtil.checkApplicationUri(
                    certificate, "urn:eclipse:milo:test:expected"));
    UaException firstMatchError =
        assertThrows(
            UaException.class,
            () ->
                CertificateValidationUtil.checkApplicationUri(certificate, "this URI has spaces"));

    assertAll(
        label,
        () ->
            assertEquals(
                StatusCodes.Bad_CertificateUriInvalid, expectedMatchError.getStatusCode().value()),
        () ->
            assertEquals(
                StatusCodes.Bad_CertificateUriInvalid, firstMatchError.getStatusCode().value()),
        () -> assertEquals(List.of(DNS_NAME), CertificateUtil.getSanDnsNames(certificate)),
        () -> assertEquals(List.of(IPV4_ADDRESS), CertificateUtil.getSanIpAddresses(certificate)),
        () ->
            assertDoesNotThrow(
                () -> CertificateValidationUtil.checkHostnameOrIpAddress(certificate, DNS_NAME)),
        () ->
            assertDoesNotThrow(
                () ->
                    CertificateValidationUtil.checkHostnameOrIpAddress(certificate, IPV4_ADDRESS)));
  }

  // Corrupt ASN.1 bytes inside subjectAltName must degrade to empty results rather than propagating
  // an uncaught ASN.1 decoding exception from readers or validation helpers.
  @Test
  void corruptSubjectAltNameExtensionDegradesToEmptyWithoutUncaughtError() throws Exception {
    X509Certificate certificate =
        buildCertificateWithSanExtension(new byte[] {0x30, 0x05, 0x02, 0x01});

    assertEquals(Optional.empty(), CertificateUtil.getSanUri(certificate));
    assertEquals(List.of(), CertificateUtil.getSanDnsNames(certificate));
    assertEquals(List.of(), CertificateUtil.getSanIpAddresses(certificate));
    assertNull(CertificateValidationUtil.getSubjectAltNameUri(certificate));

    UaException hostError =
        assertThrows(
            UaException.class,
            () -> CertificateValidationUtil.checkHostnameOrIpAddress(certificate, DNS_NAME));
    assertEquals(StatusCodes.Bad_CertificateHostNameInvalid, hostError.getStatusCode().value());

    UaException uriError =
        assertThrows(
            UaException.class,
            () ->
                CertificateValidationUtil.checkApplicationUri(
                    certificate, "urn:eclipse:milo:test"));
    assertEquals(StatusCodes.Bad_CertificateUriInvalid, uriError.getStatusCode().value());
  }

  // GeneralName tags beyond URI/DNS/IP must still decode through getSubjectAltNameField even when
  // an unparseable URI is present in the same extension.
  @Test
  void nonOpcUaSubjectAltNameFieldsStillDecodeAlongsideUnparseableUri() throws Exception {
    var otherNameValue =
        new DERSequence(
            new ASN1Encodable[] {
              new ASN1ObjectIdentifier("1.2.3.4.5"), new DERUTF8String("custom-other-name")
            });
    var directoryName = new X500Name("CN=Directory Entry,O=Eclipse Milo");

    GeneralNames generalNames =
        new GeneralNames(
            new GeneralName[] {
              new GeneralName(GeneralName.uniformResourceIdentifier, "this URI has spaces"),
              new GeneralName(GeneralName.rfc822Name, "ops@example.com"),
              new GeneralName(GeneralName.directoryName, directoryName),
              new GeneralName(GeneralName.registeredID, "1.2.840.113549.1.1.11"),
              new GeneralName(GeneralName.otherName, otherNameValue)
            });

    X509Certificate certificate = buildCertificateWithSanExtension(generalNames.getEncoded());

    String expectedDirectoryName =
        new X500Principal(directoryName.getEncoded(ASN1Encoding.DER))
            .getName(X500Principal.RFC2253);

    assertEquals(
        List.of("ops@example.com"),
        CertificateUtil.getSubjectAltNameField(certificate, GeneralName.rfc822Name));
    assertEquals(
        List.of(expectedDirectoryName),
        CertificateUtil.getSubjectAltNameField(certificate, GeneralName.directoryName));
    assertEquals(
        List.of("1.2.840.113549.1.1.11"),
        CertificateUtil.getSubjectAltNameField(certificate, GeneralName.registeredID));

    List<Object> otherNames =
        CertificateUtil.getSubjectAltNameField(certificate, GeneralName.otherName);
    assertEquals(1, otherNames.size());
    assertArrayEquals(otherNameValue.getEncoded(ASN1Encoding.DER), (byte[]) otherNames.get(0));
  }

  // When APPLICATION_URI validation is suppressed, both default validators log the certificate's
  // SAN URI via getSubjectAltNameUri(). A space-containing URI must be read cleanly instead of
  // throwing Bad_CertificateInvalid during log formatting, and must not break HOSTNAME validation
  // when ALL_OPTIONAL_CHECKS is active.
  @Test
  void defaultValidatorsHandleUriWithSpacesForBothEnforcedAndSuppressedChecks() throws Exception {
    X509Certificate certificate =
        SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
            .setCommonName("Space URI Peer")
            .setApplicationUri("urn:example:my plc server")
            .addDnsName(DNS_NAME)
            .build();

    var trustListManager = new MemoryTrustListManager();
    trustListManager.addTrustedCertificate(certificate);

    var strictClientValidator =
        new DefaultClientCertificateValidator(
            trustListManager,
            ValidationCheck.ALL_OPTIONAL_CHECKS,
            new MemoryCertificateQuarantine());
    assertDoesNotThrow(
        () ->
            strictClientValidator.validateCertificateChain(
                List.of(certificate), "urn:example:my plc server", new String[] {DNS_NAME}));

    var suppressedClientValidator =
        new DefaultClientCertificateValidator(
            trustListManager, Set.of(), new MemoryCertificateQuarantine());
    var suppressedServerValidator =
        new DefaultServerCertificateValidator(
            trustListManager, Set.of(), new MemoryCertificateQuarantine());

    assertDoesNotThrow(
        () ->
            suppressedClientValidator.validateCertificateChain(
                List.of(certificate), "urn:example:different", new String[] {DNS_NAME}));
    assertDoesNotThrow(
        () ->
            suppressedServerValidator.validateCertificateChain(
                List.of(certificate), "urn:example:different", null));
  }

  @FunctionalInterface
  private interface CertificateFactory {
    X509Certificate create() throws Exception;
  }

  private static CertificateFactory certificateFactory(CertificateFactory factory) {
    return factory;
  }

  private static UnaryOperator<SelfSignedCertificateBuilder> builder(
      UnaryOperator<SelfSignedCertificateBuilder> configure) {
    return configure;
  }

  private static X509Certificate buildCertificateWithSanExtension(byte[] sanExtensionBytes)
      throws Exception {

    var subject = new X500Name("CN=SAN Edge Case");
    Instant now = Instant.now();

    var builder =
        new JcaX509v3CertificateBuilder(
            subject,
            BigInteger.ONE,
            Date.from(now.minus(1, ChronoUnit.DAYS)),
            Date.from(now.plus(30, ChronoUnit.DAYS)),
            subject,
            keyPair.getPublic());

    builder.addExtension(Extension.subjectAlternativeName, false, sanExtensionBytes);

    return new JcaX509CertificateConverter()
        .getCertificate(
            builder.build(
                new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate())));
  }

  private static @Nullable GeneralNames certificateSubjectAltNames(X509Certificate certificate)
      throws Exception {

    var holder = new X509CertificateHolder(certificate.getEncoded());
    Extensions extensions = holder.getExtensions();
    if (extensions == null) {
      return null;
    }

    return GeneralNames.fromExtensions(extensions, Extension.subjectAlternativeName);
  }

  private static @Nullable GeneralNames csrSubjectAltNames(PKCS10CertificationRequest csr) {
    Attribute[] attributes = csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);
    if (attributes.length == 0) {
      return null;
    }

    Extensions extensions = Extensions.getInstance(attributes[0].getAttrValues().getObjectAt(0));
    return GeneralNames.fromExtensions(extensions, Extension.subjectAlternativeName);
  }

  /**
   * Simulates certificates generated by earlier versions of Milo that wrote a raw (possibly empty)
   * uniformResourceIdentifier entry directly into subjectAltName.
   */
  private static final class RawUriCertificateGenerator extends SelfSignedCertificateGenerator {
    private final String rawUri;

    RawUriCertificateGenerator(String rawUri) {
      this.rawUri = rawUri;
    }

    @Override
    protected void addSubjectAlternativeNames(
        X509v3CertificateBuilder certificateBuilder,
        KeyPair keyPair,
        @Nullable String applicationUri,
        List<String> dnsNames,
        List<String> ipAddresses)
        throws CertIOException, NoSuchAlgorithmException {

      List<GeneralName> generalNames = new ArrayList<>();
      generalNames.add(new GeneralName(GeneralName.uniformResourceIdentifier, rawUri));

      dnsNames.stream()
          .distinct()
          .map(s -> new GeneralName(GeneralName.dNSName, s))
          .forEach(generalNames::add);

      ipAddresses.stream()
          .distinct()
          .map(s -> new GeneralName(GeneralName.iPAddress, s))
          .forEach(generalNames::add);

      certificateBuilder.addExtension(
          Extension.subjectAlternativeName,
          false,
          new GeneralNames(generalNames.toArray(new GeneralName[0])));

      certificateBuilder.addExtension(
          Extension.subjectKeyIdentifier,
          false,
          new JcaX509ExtensionUtils().createSubjectKeyIdentifier(keyPair.getPublic()));
    }
  }
}
