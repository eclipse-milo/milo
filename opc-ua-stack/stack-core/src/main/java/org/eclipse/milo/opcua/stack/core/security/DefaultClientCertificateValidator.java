/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultClientCertificateValidator implements CertificateValidator {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DefaultClientCertificateValidator.class);

  private final TrustListManager trustListManager;
  private final Set<ValidationCheck> validationChecks;
  private final CertificateQuarantine certificateQuarantine;

  /**
   * Create a {@link CertificateValidator} that performs no optional validation checks.
   *
   * @param trustListManager the configured {@link TrustListManager}.
   * @param certificateQuarantine the {@link CertificateQuarantine} to use.
   */
  public DefaultClientCertificateValidator(
      TrustListManager trustListManager, CertificateQuarantine certificateQuarantine) {

    this(trustListManager, ValidationCheck.NO_OPTIONAL_CHECKS, certificateQuarantine);
  }

  /**
   * Create a {@link CertificateValidator} that performs a given set of optional validation checks.
   *
   * @param trustListManager the configured {@link TrustListManager}.
   * @param validationChecks the set of optional {@link ValidationCheck}s to perform.
   * @param certificateQuarantine the {@link CertificateQuarantine} to use.
   */
  public DefaultClientCertificateValidator(
      TrustListManager trustListManager,
      Set<ValidationCheck> validationChecks,
      CertificateQuarantine certificateQuarantine) {

    this.trustListManager = trustListManager;
    this.validationChecks = Set.copyOf(validationChecks);
    this.certificateQuarantine = certificateQuarantine;
  }

  @Override
  public void validateCertificateChain(
      List<X509Certificate> certificateChain,
      @Nullable String applicationUri,
      @Nullable String[] validHostNames)
      throws UaException {

    validateCertificateChain(certificateChain, applicationUri, validHostNames, null);
  }

  @Override
  public void validateCertificateChain(
      List<X509Certificate> certificateChain,
      @Nullable String applicationUri,
      @Nullable String[] validHostNames,
      @Nullable SecurityPolicyProfile securityPolicyProfile)
      throws UaException {

    PKIXCertPathBuilderResult certPathResult;

    try {
      certPathResult =
          CertificateValidationUtil.buildTrustedCertPath(
              certificateChain,
              trustListManager.getTrustedCertificates(),
              trustListManager.getIssuerCertificates());
    } catch (UaException e) {
      certificateChain.forEach(certificateQuarantine::addRejectedCertificate);

      LOGGER.debug("validateCertificateChain failed, underlying status: {}", e.getStatusCode(), e);

      throw e;
    }

    try {
      List<X509CRL> crls = new ArrayList<>();
      crls.addAll(trustListManager.getTrustedCrls());
      crls.addAll(trustListManager.getIssuerCrls());

      CertificateValidationUtil.validateTrustedCertPath(
          certPathResult.getCertPath(),
          certPathResult.getTrustAnchor(),
          crls,
          validationChecks,
          false,
          securityPolicyProfile);

      if (securityPolicyProfile != null) {
        CertificateCompatibility.checkCompatible(
            securityPolicyProfile, certificateChain.get(0), validationChecks);
      }
    } catch (UaException e) {
      LOGGER.debug("validateCertificateChain failed, underlying status: {}", e.getStatusCode(), e);

      throw e;
    }

    X509Certificate certificate = certificateChain.get(0);

    if (applicationUri != null) {
      try {
        CertificateValidationUtil.checkApplicationUri(certificate, applicationUri);
      } catch (UaException e) {
        if (validationChecks.contains(ValidationCheck.APPLICATION_URI)) {
          LOGGER.debug(
              "validateCertificateChain failed, underlying status: {}", e.getStatusCode(), e);

          throw e;
        } else {
          LOGGER.warn(
              "check suppressed: certificate failed application uri check: {} != {}",
              applicationUri,
              CertificateValidationUtil.getSubjectAltNameUri(certificate));
        }
      }
    }

    if (validHostNames != null) {
      try {
        CertificateValidationUtil.checkHostnameOrIpAddress(certificate, validHostNames);
      } catch (UaException e) {
        if (validationChecks.contains(ValidationCheck.HOSTNAME)) {
          LOGGER.debug(
              "validateCertificateChain failed, underlying status: {}", e.getStatusCode(), e);

          throw e;
        } else {
          LOGGER.warn(
              "check suppressed: certificate failed hostname check: {}",
              certificate.getSubjectX500Principal().getName());
        }
      }
    }
  }
}
