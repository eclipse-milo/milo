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

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bouncycastle.cert.X509v3CertificateBuilder;

public class SelfSignedHttpsCertificateBuilder {

  /** Signature Algorithm for SHA256 with RSA. */
  private static final String SA_SHA256_RSA = "SHA256withRSA";

  private final HttpsCertificateGenerator generator = new HttpsCertificateGenerator();

  private Period validityPeriod = Period.ofYears(3);
  private String commonName;
  private List<String> dnsNames = new ArrayList<>();
  private List<String> ipAddresses = new ArrayList<>();

  private final KeyPair keyPair;

  public SelfSignedHttpsCertificateBuilder(KeyPair keyPair) {
    this.keyPair = keyPair;
  }

  public SelfSignedHttpsCertificateBuilder setValidityPeriod(Period validityPeriod) {
    this.validityPeriod = validityPeriod;
    return this;
  }

  /**
   * Set the Common Name, which should be the hostname when building an SSL/TLS certificate.
   *
   * @param commonName the hostname to place in the Common Name field.
   * @return {@code this} {@link SelfSignedHttpsCertificateBuilder}.
   */
  public SelfSignedHttpsCertificateBuilder setCommonName(String commonName) {
    this.commonName = commonName;
    return this;
  }

  public SelfSignedHttpsCertificateBuilder addDnsName(String dnsName) {
    dnsNames.add(dnsName);
    return this;
  }

  public SelfSignedHttpsCertificateBuilder addIpAddress(String ipAddress) {
    ipAddresses.add(ipAddress);
    return this;
  }

  public X509Certificate build() throws Exception {
    // Calculate start and end date based on validity period
    LocalDate now = LocalDate.now();
    LocalDate expiration = now.plus(validityPeriod);

    Date notBefore = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date notAfter = Date.from(expiration.atStartOfDay(ZoneId.systemDefault()).toInstant());

    return generator.generateSelfSigned(
        keyPair,
        notBefore,
        notAfter,
        commonName,
        null,
        null,
        null,
        null,
        null,
        null,
        dnsNames,
        ipAddresses,
        SA_SHA256_RSA);
  }

  private static class HttpsCertificateGenerator extends SelfSignedCertificateGenerator {

    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) {
      // Don't set any KU fields
    }

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {
      // Don't set any EKU fields
    }
  }
}
