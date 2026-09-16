/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import java.security.KeyPair;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.Callable;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;

/**
 * Self-signed certificates and CRLs for exercising trust list code with real DER material.
 *
 * <p>Failures are rethrown unchecked so tests can hold the material in {@code static final} fields.
 */
final class TestPki {

  private TestPki() {}

  static KeyPair keyPair() {
    return unchecked(() -> SelfSignedCertificateGenerator.generateRsaKeyPair(2048));
  }

  static X509Certificate certificate(KeyPair keyPair, String commonName) {
    return unchecked(
        () ->
            new SelfSignedCertificateBuilder(keyPair)
                .setCommonName(commonName)
                .setApplicationUri("urn:eclipse:milo:test:" + commonName)
                .build());
  }

  static X509Certificate certificate(String commonName) {
    return certificate(keyPair(), commonName);
  }

  static X509CRL crl(X509Certificate issuer, KeyPair issuerKeyPair) {
    return unchecked(
        () -> {
          var builder =
              new X509v2CRLBuilder(
                  X500Name.getInstance(issuer.getSubjectX500Principal().getEncoded()), new Date());

          builder.setNextUpdate(new Date(System.currentTimeMillis() + 60_000));

          X509CRLHolder holder =
              builder.build(
                  new JcaContentSignerBuilder("SHA256WithRSAEncryption")
                      .build(issuerKeyPair.getPrivate()));

          return new JcaX509CRLConverter().getCRL(holder);
        });
  }

  static X509CRL crl() {
    KeyPair keyPair = keyPair();
    return crl(certificate(keyPair, "crl-issuer"), keyPair);
  }

  static ByteString der(X509Certificate certificate) {
    return unchecked(() -> ByteString.of(certificate.getEncoded()));
  }

  static ByteString der(X509CRL crl) {
    return unchecked(() -> ByteString.of(crl.getEncoded()));
  }

  private static <T> T unchecked(Callable<T> action) {
    try {
      return action.call();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
