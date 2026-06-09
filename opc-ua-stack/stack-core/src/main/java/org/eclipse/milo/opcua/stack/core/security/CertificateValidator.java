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
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
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

  /**
   * Check that trust can be established using the provided certificate chain and policy profile,
   * then validate every certificate in the chain.
   *
   * <p>The default implementation preserves legacy validation behavior and then applies
   * certificate/profile compatibility when {@code securityPolicyProfile} is not {@code null}.
   * Because no {@link ValidationCheck} set is available at this layer, compatibility is applied
   * with {@link ValidationCheck#NO_OPTIONAL_CHECKS}, which leaves the legacy end-entity KeyUsage
   * check suppressible. Implementations that need profile-specific trust or usage checks, or that
   * track an active {@link ValidationCheck} set, should override this method.
   *
   * @param certificateChain the certificate chain to validate.
   * @param applicationUri the applicationUri of the remote endpoint. Ignored if {@code null}.
   * @param validHostnames the valid hostnames for the remote endpoint. Ignored if {@code null}.
   * @param securityPolicyProfile the policy profile the certificate will be used with, or {@code
   *     null} for legacy validation rules.
   * @throws UaException if {@code certificateChain} is not trusted or validation fails.
   */
  default void validateCertificateChain(
      List<X509Certificate> certificateChain,
      @Nullable String applicationUri,
      @Nullable String[] validHostnames,
      @Nullable SecurityPolicyProfile securityPolicyProfile)
      throws UaException {

    validateCertificateChain(certificateChain, applicationUri, validHostnames);

    if (securityPolicyProfile != null) {
      CertificateCompatibility.checkCompatible(
          securityPolicyProfile, certificateChain.get(0), ValidationCheck.NO_OPTIONAL_CHECKS);
    }
  }

  class InsecureCertificateValidator implements CertificateValidator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(InsecureCertificateValidator.class);

    @Override
    public void validateCertificateChain(
        List<X509Certificate> certificateChain, String applicationUri, String[] validHostnames) {

      X509Certificate certificate = certificateChain.get(0);

      LOGGER.warn("Skipping validation for certificate: {}", certificate.getSubjectX500Principal());
    }

    @Override
    public void validateCertificateChain(
        List<X509Certificate> certificateChain,
        @Nullable String applicationUri,
        @Nullable String[] validHostnames,
        @Nullable SecurityPolicyProfile securityPolicyProfile) {

      // Skip validation entirely, including certificate/profile compatibility checks. The default
      // 4-arg implementation would otherwise enforce CertificateCompatibility, which an insecure
      // validator must not do.
      validateCertificateChain(certificateChain, applicationUri, validHostnames);
    }
  }
}
