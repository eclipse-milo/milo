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

import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/** Builds X.509 CRLs for tests that need real, decodable CRL bytes. */
public final class CrlTestUtil {

  private CrlTestUtil() {}

  /**
   * Generate a CRL issued by {@code issuer} that revokes {@code revoked}.
   *
   * @param issuer the issuing certificate.
   * @param issuerKey the private key matching {@code issuer}.
   * @param revoked the certificates to list as revoked; may be empty.
   * @return a signed {@link X509CRL}.
   * @throws Exception if building or signing the CRL fails.
   */
  public static X509CRL generateCrl(
      X509Certificate issuer, PrivateKey issuerKey, X509Certificate... revoked) throws Exception {

    var builder =
        new X509v2CRLBuilder(
            X500Name.getInstance(issuer.getSubjectX500Principal().getEncoded()), new Date());

    builder.setNextUpdate(new Date(System.currentTimeMillis() + 60_000));

    for (X509Certificate certificate : revoked) {
      builder.addCRLEntry(
          certificate.getSerialNumber(),
          new Date(System.currentTimeMillis() - 60_000),
          CRLReason.privilegeWithdrawn);
    }

    X509CRLHolder holder =
        builder.build(new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(issuerKey));

    return new JcaX509CRLConverter().getCRL(holder);
  }
}
