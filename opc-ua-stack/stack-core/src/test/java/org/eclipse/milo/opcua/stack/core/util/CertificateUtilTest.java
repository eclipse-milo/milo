/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.testng.annotations.Test;

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

    assertEquals(uri, "urn:eclipse:milo:test");
    assertEquals(dnsName, "localhost");
    assertEquals(ipAddress, "127.0.0.1");
  }

  @Test
  public void testGetSanUri() {
    assertEquals(CertificateUtil.getSanUri(certificate).orElse(null), "urn:eclipse:milo:test");
  }

  @Test
  public void testGetSanDnsNames() {
    List<String> sanDnsNames = CertificateUtil.getSanDnsNames(certificate);
    assertEquals(sanDnsNames, List.of("localhost", "hostname"));
  }

  @Test
  public void testGetSanIpAddresses() {
    List<String> sanDnsNames = CertificateUtil.getSanIpAddresses(certificate);
    assertEquals(sanDnsNames, List.of("127.0.0.1", "127.0.0.2"));
  }
}
