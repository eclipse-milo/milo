/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CertificateValidator {

  /**
   * Check that trust can be established using the provided certificate chain and then validate
   * every certificate in the chain.
   *
   * <p>The chain must begin with the end-entity certificate at index 0 followed by the remaining
   * certificates in the chain, if any, in the correct order.
   *
   * @param certificateChain the certificate chain to validate.
   * @param applicationUri the applicationUri of the remote endpoint. Ignored if {@code null}.
   * @param validHostnames the valid hostnames for the remote endpoint. Ignored if {@code null}.
   * @throws UaException if {@code certificateChain} is not trusted or validation fails.
   */
  void validateCertificateChain(
      List<X509Certificate> certificateChain,
      @Nullable String applicationUri,
      @Nullable String[] validHostnames)
      throws UaException;

  class InsecureCertificateValidator implements CertificateValidator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(InsecureCertificateValidator.class);

    @Override
    public void validateCertificateChain(
        List<X509Certificate> certificateChain, String applicationUri, String[] validHostnames) {

      X509Certificate certificate = certificateChain.get(0);

      LOGGER.warn("Skipping validation for certificate: {}", certificate.getSubjectX500Principal());
    }
  }
}
