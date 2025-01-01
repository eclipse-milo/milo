/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.validation;

import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OpcUaCertificateUsageChecker extends PKIXCertPathChecker {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpcUaCertificateUsageChecker.class);

  private static final String KEY_USAGE_OID = "2.5.29.15";
  private static final String EXTENDED_KEY_USAGE_OID = "2.5.29.37";

  private final X509Certificate endEntityCert;

  private final CertPath certPath;
  private final Set<ValidationCheck> validationChecks;
  private final boolean endEntityIsClient;

  OpcUaCertificateUsageChecker(
      CertPath certPath, Set<ValidationCheck> validationChecks, boolean endEntityIsClient) {
    this.certPath = certPath;
    this.validationChecks = validationChecks;
    this.endEntityIsClient = endEntityIsClient;

    endEntityCert = (X509Certificate) certPath.getCertificates().get(0);
  }

  @Override
  public void init(boolean forward) throws CertPathValidatorException {
    if (forward) {
      throw new CertPathValidatorException("forward checking not supported");
    }
  }

  @Override
  public boolean isForwardCheckingSupported() {
    return false;
  }

  @Override
  public Set<String> getSupportedExtensions() {
    return Set.of(KEY_USAGE_OID, EXTENDED_KEY_USAGE_OID);
  }

  @Override
  public void check(Certificate cert, Collection<String> unresolvedCritExts)
      throws CertPathValidatorException {
    X509Certificate certificate = (X509Certificate) cert;

    Set<String> criticalExtensions = certificate.getCriticalExtensionOIDs();
    if (criticalExtensions == null) criticalExtensions = Collections.emptySet();

    if (endEntityCert.equals(cert)) {
      try {
        CertificateValidationUtil.checkEndEntityKeyUsage((X509Certificate) cert);

        LOGGER.debug(
            "validated KeyUsage for end entity: {}",
            ((X509Certificate) cert).getSubjectX500Principal().getName());
      } catch (UaException e) {
        if (validationChecks.contains(ValidationCheck.KEY_USAGE_END_ENTITY)
            || criticalExtensions.contains(KEY_USAGE_OID)) {

          throw new CertPathValidatorException(
              e.getMessage(),
              e,
              certPath,
              certPath.getCertificates().indexOf(cert),
              PKIXReason.INVALID_KEY_USAGE);
        } else {
          LOGGER.warn(
              "check suppressed: certificate failed end-entity usage check: {}",
              ((X509Certificate) cert).getSubjectX500Principal().getName());
        }
      }
      try {
        CertificateValidationUtil.checkEndEntityExtendedKeyUsage(certificate, endEntityIsClient);

        LOGGER.debug(
            "validated ExtendedKeyUsage for end entity: {}",
            ((X509Certificate) cert).getSubjectX500Principal().getName());
      } catch (UaException e) {
        if (validationChecks.contains(ValidationCheck.EXTENDED_KEY_USAGE_END_ENTITY)
            || criticalExtensions.contains(EXTENDED_KEY_USAGE_OID)) {

          throw new CertPathValidatorException(
              e.getMessage(),
              e,
              certPath,
              certPath.getCertificates().indexOf(cert),
              PKIXReason.INVALID_KEY_USAGE);
        } else {
          LOGGER.warn(
              "check suppressed: certificate failed end-entity usage check: {}",
              ((X509Certificate) cert).getSubjectX500Principal().getName());
        }
      }
    }

    if (unresolvedCritExts != null && !unresolvedCritExts.isEmpty()) {
      unresolvedCritExts.remove(KEY_USAGE_OID);
      unresolvedCritExts.remove(EXTENDED_KEY_USAGE_OID);
    }
  }
}
