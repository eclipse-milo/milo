/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.primitives.Bytes;
import java.security.KeyPair;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

public class CertificateUtilTest {

  private final KeyPair keyPair;
  private final X509Certificate certificate;

  public CertificateUtilTest() throws Exception {
    keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedCertificateBuilder builder =
        new SelfSignedCertificateBuilder(keyPair)
            .setApplicationUri("urn:eclipse:milo:test")
            .addDnsName("localhost")
            .addDnsName("hostname")
            .addIpAddress("127.0.0.1")
            .addIpAddress("127.0.0.2");

    certificate = builder.build();
  }

  @Test
  public void testGenerateCsr() throws Exception {
    assertNotNull(CertificateUtil.generateCsr(keyPair, certificate));
  }

  @Test
  public void testGenerateCsrPem() throws Exception {
    PKCS10CertificationRequest csr = CertificateUtil.generateCsr(keyPair, certificate);

    assertNotNull(CertificateUtil.getCsrPem(csr));
  }

  @Test
  public void testGetSubjectAltNameField() {
    Object uri =
        CertificateUtil.getSubjectAltNameField(certificate, CertificateUtil.SUBJECT_ALT_NAME_URI)
            .get(0);

    Object dnsName =
        CertificateUtil.getSubjectAltNameField(
                certificate, CertificateUtil.SUBJECT_ALT_NAME_DNS_NAME)
            .get(0);

    Object ipAddress =
        CertificateUtil.getSubjectAltNameField(
                certificate, CertificateUtil.SUBJECT_ALT_NAME_IP_ADDRESS)
            .get(0);

    assertEquals("urn:eclipse:milo:test", uri);
    assertEquals("localhost", dnsName);
    assertEquals("127.0.0.1", ipAddress);
  }

  @Test
  public void testGetSanUri() {
    assertEquals("urn:eclipse:milo:test", CertificateUtil.getSanUri(certificate).orElse(null));
  }

  @Test
  public void testGetSanDnsNames() {
    List<String> sanDnsNames = CertificateUtil.getSanDnsNames(certificate);
    assertEquals(List.of("localhost", "hostname"), sanDnsNames);
  }

  @Test
  public void testGetSanIpAddresses() {
    List<String> sanDnsNames = CertificateUtil.getSanIpAddresses(certificate);
    assertEquals(List.of("127.0.0.1", "127.0.0.2"), sanDnsNames);
  }

  @Test
  public void testGenerateCsrContainsRequiredExtensions() throws Exception {
    PKCS10CertificationRequest csr = CertificateUtil.generateCsr(keyPair, certificate);

    Attribute[] attributes = csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);
    assertNotNull(attributes, "extension request attribute should be present");
    assertEquals(1, attributes.length, "should have exactly one extension request attribute");

    Extensions extensions = Extensions.getInstance(attributes[0].getAttrValues().getObjectAt(0));

    Extension keyUsageExt = extensions.getExtension(Extension.keyUsage);
    assertNotNull(keyUsageExt, "keyUsage extension should be present");
    assertTrue(keyUsageExt.isCritical(), "keyUsage extension should be critical");

    KeyUsage keyUsage = KeyUsage.getInstance(keyUsageExt.getParsedValue());
    assertTrue(
        keyUsage.hasUsages(KeyUsage.digitalSignature), "keyUsage should include digitalSignature");
    assertTrue(
        keyUsage.hasUsages(KeyUsage.nonRepudiation), "keyUsage should include nonRepudiation");
    assertTrue(
        keyUsage.hasUsages(KeyUsage.keyEncipherment), "keyUsage should include keyEncipherment");
    assertTrue(
        keyUsage.hasUsages(KeyUsage.dataEncipherment), "keyUsage should include dataEncipherment");

    Extension extendedKeyUsageExt = extensions.getExtension(Extension.extendedKeyUsage);
    assertNotNull(extendedKeyUsageExt, "extendedKeyUsage extension should be present");
    assertFalse(
        extendedKeyUsageExt.isCritical(), "extendedKeyUsage extension should not be critical");

    ExtendedKeyUsage extendedKeyUsage =
        ExtendedKeyUsage.getInstance(extendedKeyUsageExt.getParsedValue());
    assertTrue(
        extendedKeyUsage.hasKeyPurposeId(KeyPurposeId.id_kp_clientAuth),
        "extendedKeyUsage should include clientAuth");
    assertTrue(
        extendedKeyUsage.hasKeyPurposeId(KeyPurposeId.id_kp_serverAuth),
        "extendedKeyUsage should include serverAuth");

    Extension basicConstraintsExt = extensions.getExtension(Extension.basicConstraints);
    assertNotNull(basicConstraintsExt, "basicConstraints extension should be present");
    assertTrue(basicConstraintsExt.isCritical(), "basicConstraints extension should be critical");

    BasicConstraints basicConstraints =
        BasicConstraints.getInstance(basicConstraintsExt.getParsedValue());
    assertFalse(basicConstraints.isCA(), "basicConstraints should have cA=false");
  }

  // A GDS TrustList delivers CRLs as bare DER ByteStrings (Part 12 §7.8.2.8); the decoded CRL
  // must carry the revocation entries of the original so validation can act on them.
  @Test
  public void decodeCrlRoundTripsDerEncodedCrl() throws Exception {
    X509CRL crl = CrlTestUtil.generateCrl(certificate, keyPair.getPrivate(), certificate);

    X509CRL decoded = CertificateUtil.decodeCrl(crl.getEncoded());

    assertEquals(crl, decoded);
    assertTrue(decoded.isRevoked(certificate), "revocation entry survives decode");
  }

  @Test
  public void decodeCrlsDecodesConcatenatedDerCrls() throws Exception {
    X509CRL crl1 = CrlTestUtil.generateCrl(certificate, keyPair.getPrivate());
    X509CRL crl2 = CrlTestUtil.generateCrl(certificate, keyPair.getPrivate(), certificate);
    byte[] concatenated = Bytes.concat(crl1.getEncoded(), crl2.getEncoded());

    List<X509CRL> decoded = CertificateUtil.decodeCrls(concatenated);

    assertEquals(List.of(crl1, crl2), decoded);
  }

  // Trust list application must fail loudly on a corrupt CRL rather than silently dropping it,
  // otherwise a revoked certificate could stay trusted.
  @Test
  public void decodeCrlRejectsMalformedBytesWithBadDecodingError() {
    byte[] garbage = {0x30, 0x03, 0x02, 0x01};

    UaException e = assertThrows(UaException.class, () -> CertificateUtil.decodeCrl(garbage));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  @Test
  public void decodeCrlRejectsEmptyInput() {
    UaException e = assertThrows(UaException.class, () -> CertificateUtil.decodeCrl(new byte[0]));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }
}
