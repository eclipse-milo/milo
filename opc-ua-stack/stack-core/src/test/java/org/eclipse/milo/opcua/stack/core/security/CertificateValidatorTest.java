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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

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

  private static X509Certificate createRsaCertificate() {
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();

    return factory.createRsaSha256CertificateChain(keyPair)[0];
  }
}
