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

import java.lang.reflect.Method;
import java.security.cert.CertPath;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorException.BasicReason;
import java.security.cert.Certificate;
import java.security.cert.PKIXParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcUaCertificateRevocationChecker extends PKIXRevocationChecker {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(OpcUaCertificateRevocationChecker.class);

  private final PKIXRevocationChecker checker;

  private final CertPath certPath;
  private final TrustAnchor trustAnchor;
  private final PKIXParameters parameters;
  private final Set<ValidationCheck> validationChecks;

  public OpcUaCertificateRevocationChecker(
      CertPath certPath,
      TrustAnchor trustAnchor,
      PKIXParameters parameters,
      Set<ValidationCheck> validationChecks)
      throws Exception {

    this.certPath = certPath;
    this.trustAnchor = trustAnchor;
    this.parameters = parameters;
    this.validationChecks = validationChecks;

    var options = new HashSet<Option>();
    options.add(Option.NO_FALLBACK);
    options.add(Option.PREFER_CRLS);

    // If the REVOCATION_LIST_FOUND check is suppressed, enable SOFT_FAIL.
    // Unfortunately these means we won't be able to log a warning when
    // a CRL is not found, but without SOFT_FAIL *every* CRL in the chain
    // must be available, or the check fails with
    // BasicReason.UNDETERMINED_REVOCATION_STATUS, which could mask a revoked
    // certificate in a CRL we *do* have.
    if (!validationChecks.contains(ValidationCheck.REVOCATION_LISTS)) {
      options.add(Option.SOFT_FAIL);
    }

    CertPathValidator validator = CertPathValidator.getInstance("PKIX", "SUN");
    checker = (PKIXRevocationChecker) validator.getRevocationChecker();
    checker.setOptions(options);

    // bail in constructor if access via reflection will fail.
    initRevocationChecker();
  }

  private void initRevocationChecker() throws Exception {
    Class<?> pkixClass = Class.forName("sun.security.provider.certpath.PKIX");
    Class<?> validatorParamsClass =
        Class.forName("sun.security.provider.certpath.PKIX$ValidatorParams");
    Class<?> revocationCheckerClass =
        Class.forName("sun.security.provider.certpath.RevocationChecker");

    Method checkParams =
        pkixClass.getDeclaredMethod("checkParams", CertPath.class, CertPathParameters.class);
    checkParams.setAccessible(true);
    Object validatorParams = checkParams.invoke(null, certPath, parameters);

    Method init =
        revocationCheckerClass.getDeclaredMethod("init", TrustAnchor.class, validatorParamsClass);
    init.setAccessible(true);
    init.invoke(checker, trustAnchor, validatorParams);
  }

  @Override
  public List<CertPathValidatorException> getSoftFailExceptions() {
    return checker.getSoftFailExceptions();
  }

  @Override
  public void init(boolean forward) throws CertPathValidatorException {
    checker.init(forward);
  }

  @Override
  public boolean isForwardCheckingSupported() {
    return checker.isForwardCheckingSupported();
  }

  @Override
  public Set<String> getSupportedExtensions() {
    return checker.getSupportedExtensions();
  }

  @Override
  public void check(Certificate cert, Collection<String> unresolvedCritExts)
      throws CertPathValidatorException {
    try {
      initRevocationChecker();

      checker.check(cert, unresolvedCritExts);
    } catch (CertPathValidatorException e) {
      CertPath certPath = e.getCertPath();
      CertPathValidatorException.Reason reason = e.getReason();
      int failedAtIndex = e.getIndex();

      if (failedAtIndex < 0) {
        throw e;
      } else {
        X509Certificate failed = (X509Certificate) certPath.getCertificates().get(failedAtIndex);

        if (reason == BasicReason.REVOKED) {
          if (validationChecks.contains(ValidationCheck.REVOCATION)) {
            throw e;
          } else {
            LOGGER.warn(
                "check suppressed: certificate failed revocation check: {}",
                failed.getSubjectX500Principal().getName());
          }
        } else if (reason == BasicReason.UNDETERMINED_REVOCATION_STATUS) {
          // sanity check: this can only happen if we didn't include
          // the Option.SOFT_FAIL option, which only happens when
          // ValidationCheck.REVOCATION_LIST_FOUND is present.
          assert !getOptions().contains(Option.SOFT_FAIL);
          assert validationChecks.contains(ValidationCheck.REVOCATION_LISTS);

          throw e;
        } else {
          throw e;
        }
      }
    } catch (Exception e) {
      throw new CertPathValidatorException("revocation checker initialization failed", e);
    }
  }
}
