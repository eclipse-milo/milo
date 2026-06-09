/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator.InsecureCertificateValidator;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
class CertificateValidatorTest {

  @Test
  void defaultProfileAwareOverloadPreservesLegacyValidation() {
    X509Certificate certificate = createRsaCertificate();
    CertificateValidator validator =
        (certificateChain, applicationUri, validHostnames) -> {
          throw new UaException(StatusCodes.Bad_CertificateUntrusted);
        };

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateCertificateChain(
                    List.of(certificate), null, null, SecurityPolicy.None.getProfile()));

    assertEquals(StatusCodes.Bad_CertificateUntrusted, exception.getStatusCode().getValue());
  }

  // Custom validators that only implement the legacy contract still need profile enforcement.
  @Test
  void defaultProfileAwareOverloadAppliesCompatibilityAfterLegacyValidation() {
    X509Certificate certificate = createRsaCertificate();
    CertificateValidator validator = (certificateChain, applicationUri, validHostnames) -> {};

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateCertificateChain(
                    List.of(certificate),
                    null,
                    null,
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile()));

    assertEquals(StatusCodes.Bad_CertificateUseNotAllowed, exception.getStatusCode().getValue());
  }

  // The insecure validator must skip everything, including certificate/profile compatibility. The
  // default 4-arg implementation would otherwise run CertificateCompatibility and reject a legacy
  // RSA cert that lacks a KeyUsage extension; the override must accept it.
  @Test
  void insecureValidatorSkipsCompatibilityChecksOnProfileAwarePath() {
    X509Certificate certificate = createRsaCertificateWithoutKeyUsage();
    CertificateValidator validator = new InsecureCertificateValidator();

    assertDoesNotThrow(
        () ->
            validator.validateCertificateChain(
                List.of(certificate), null, null, SecurityPolicy.Basic256Sha256.getProfile()));
  }

  private static X509Certificate createRsaCertificate() {
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();

    return factory.createRsaSha256CertificateChain(keyPair)[0];
  }

  private static X509Certificate createRsaCertificateWithoutKeyUsage() {
    try {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

      return new SelfSignedCertificateBuilder(keyPair, new NoKeyUsageGenerator())
          .setCommonName("Eclipse Milo OPC UA Test")
          .setOrganization("digitalpetri")
          .setApplicationUri("urn:eclipse:milo:test")
          .addDnsName("localhost")
          .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static final class NoKeyUsageGenerator extends SelfSignedCertificateGenerator {
    @Override
    protected void addKeyUsage(X509v3CertificateBuilder certificateBuilder) {}

    @Override
    protected void addExtendedKeyUsage(X509v3CertificateBuilder certificateBuilder) {}
  }
}
