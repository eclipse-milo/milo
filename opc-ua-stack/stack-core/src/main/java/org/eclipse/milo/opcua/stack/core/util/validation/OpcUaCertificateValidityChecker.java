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

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link PKIXCertPathChecker} that does the same basic checks as
 * sun.security.provider.certpath.BasicChecker but allows for the validation check to be suppressed.
 *
 * <p>Not currently used because the BasicChecker isn't replaceable. Needs to be used in conjuction
 * with {@link OpcUaCertPathValidator} if/when it's implemented.
 */
public class OpcUaCertificateValidityChecker extends PKIXCertPathChecker {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(OpcUaCertificateValidityChecker.class);

  private X500Principal previousSubject;
  private PublicKey previousPublicKey;

  private final CertPath certPath;
  private final TrustAnchor trustAnchor;
  private final Set<ValidationCheck> validationChecks;

  public OpcUaCertificateValidityChecker(
      CertPath certPath, TrustAnchor trustAnchor, Set<ValidationCheck> validationChecks) {

    this.certPath = certPath;
    this.trustAnchor = trustAnchor;
    this.validationChecks = validationChecks;
  }

  @Override
  public void init(boolean forward) throws CertPathValidatorException {
    if (!forward) {
      if (trustAnchor.getTrustedCert() != null) {
        previousPublicKey = trustAnchor.getTrustedCert().getPublicKey();
        previousSubject = trustAnchor.getTrustedCert().getSubjectX500Principal();
      } else {
        previousPublicKey = trustAnchor.getCAPublicKey();
        previousSubject = trustAnchor.getCA();
      }
    } else {
      throw new CertPathValidatorException("forward checking not supported");
    }
  }

  @Override
  public boolean isForwardCheckingSupported() {
    return false;
  }

  @Override
  public Set<String> getSupportedExtensions() {
    return null;
  }

  @Override
  public void check(Certificate cert, Collection<String> unresolvedCritExts)
      throws CertPathValidatorException {
    X509Certificate certificate = (X509Certificate) cert;

    try {
      verifyValidity(certificate);
      verifyNameChaining(certificate);
      verifySignature(certificate);
    } catch (CertPathValidatorException e) {
      if (validationChecks.contains(ValidationCheck.VALIDITY)) {
        throw e;
      } else {
        LOGGER.warn(
            "check suppressed: certificate failed validity check: {}",
            certificate.getSubjectX500Principal().getName());
      }
    }

    updateInternalState(certificate);
  }

  private void updateInternalState(X509Certificate certificate) {
    previousPublicKey = certificate.getPublicKey();
    previousSubject = certificate.getSubjectX500Principal();
  }

  private void verifyValidity(X509Certificate certificate) throws CertPathValidatorException {
    try {
      certificate.checkValidity(new Date());
    } catch (CertificateExpiredException e) {
      throw new CertPathValidatorException(
          "validity check failed (expired)",
          e,
          null,
          certPath.getCertificates().indexOf(certificate),
          CertPathValidatorException.BasicReason.EXPIRED);
    } catch (CertificateNotYetValidException e) {
      throw new CertPathValidatorException(
          "validity check failed (not yet valid)",
          e,
          null,
          certPath.getCertificates().indexOf(certificate),
          CertPathValidatorException.BasicReason.NOT_YET_VALID);
    }
  }

  private void verifyNameChaining(X509Certificate certificate) throws CertPathValidatorException {
    if (previousSubject != null) {
      X500Principal issuer = certificate.getIssuerX500Principal();

      if (issuer.getName() == null || issuer.getName().isEmpty()) {
        throw new CertPathValidatorException(
            "subject/issuer name chaining check failed: null/empty issuer DN",
            null,
            null,
            certPath.getCertificates().indexOf(certificate),
            PKIXReason.NAME_CHAINING);
      }

      if (!Objects.equals(issuer, previousSubject)) {
        throw new CertPathValidatorException(
            "subject/issuer name chaining check failed",
            null,
            null,
            certPath.getCertificates().indexOf(certificate),
            PKIXReason.NAME_CHAINING);
      }
    }
  }

  private void verifySignature(X509Certificate certificate) throws CertPathValidatorException {
    try {
      certificate.verify(previousPublicKey);
    } catch (SignatureException e) {
      throw new CertPathValidatorException(
          "signature check failed",
          e,
          null,
          certPath.getCertificates().indexOf(certificate),
          CertPathValidatorException.BasicReason.INVALID_SIGNATURE);
    } catch (GeneralSecurityException e) {
      throw new CertPathValidatorException("signature check failed", e);
    }
  }
}
