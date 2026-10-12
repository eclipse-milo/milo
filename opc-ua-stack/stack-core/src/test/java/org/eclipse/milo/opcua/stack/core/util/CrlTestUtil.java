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

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
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

    return generateCrl(
        issuer, issuerKey, new Date(), new Date(System.currentTimeMillis() + 60_000), revoked);
  }

  /**
   * Generate a CRL issued by {@code issuer} with an explicit validity window.
   *
   * <p>Use this to produce CRLs that are expired or not yet valid. The JDK's PKIX revocation
   * checker allows 15 minutes of clock skew, so offset the window by more than that.
   *
   * @param issuer the issuing certificate.
   * @param issuerKey the private key matching {@code issuer}.
   * @param thisUpdate the CRL's thisUpdate time.
   * @param nextUpdate the CRL's nextUpdate time.
   * @param revoked the certificates to list as revoked; may be empty.
   * @return a signed {@link X509CRL}.
   * @throws Exception if building or signing the CRL fails.
   */
  public static X509CRL generateCrl(
      X509Certificate issuer,
      PrivateKey issuerKey,
      Date thisUpdate,
      Date nextUpdate,
      X509Certificate... revoked)
      throws Exception {

    var builder =
        new X509v2CRLBuilder(
            X500Name.getInstance(issuer.getSubjectX500Principal().getEncoded()), thisUpdate);

    builder.setNextUpdate(nextUpdate);

    // Real CAs identify their signing key on the CRL, and PKIX uses it to choose a signer when
    // more than one certificate carries the issuer name.
    var issuerName =
        new GeneralNames(
            new GeneralName(X500Name.getInstance(issuer.getSubjectX500Principal().getEncoded())));
    builder.addExtension(
        Extension.authorityKeyIdentifier,
        false,
        new AuthorityKeyIdentifier(
            new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(issuer).getKeyIdentifier(),
            issuerName,
            issuer.getSerialNumber()));
    builder.addExtension(Extension.cRLNumber, false, new CRLNumber(BigInteger.ONE));

    for (X509Certificate certificate : revoked) {
      builder.addCRLEntry(
          certificate.getSerialNumber(),
          new Date(System.currentTimeMillis() - 60_000),
          CRLReason.privilegeWithdrawn);
    }

    String signatureAlgorithm =
        "EC".equals(issuerKey.getAlgorithm()) ? "SHA256withECDSA" : "SHA256WithRSAEncryption";

    X509CRLHolder holder =
        builder.build(new JcaContentSignerBuilder(signatureAlgorithm).build(issuerKey));

    return new JcaX509CRLConverter().getCRL(holder);
  }
}
