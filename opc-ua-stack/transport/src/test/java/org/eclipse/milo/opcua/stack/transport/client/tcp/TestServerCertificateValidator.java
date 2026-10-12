/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;

/**
 * Test fixture certificate validator used by transport handshake tests.
 *
 * <p>The transport tests exercise TCP/UASC connection behavior with fixed fixture certificates.
 * Profile-specific certificate compatibility is covered by stack-core security tests, so this
 * validator keeps both validator entry points focused on fixture trust behavior instead of applying
 * production certificate checks.
 */
public class TestServerCertificateValidator implements CertificateValidator {

  private final Set<X509Certificate> trustedCertificates = ConcurrentHashMap.newKeySet();

  public TestServerCertificateValidator(X509Certificate certificate) {
    trustedCertificates.add(certificate);
  }

  public TestServerCertificateValidator(X509Certificate... certificates) {
    Collections.addAll(trustedCertificates, certificates);
  }

  @Override
  public void validateCertificateChain(
      List<X509Certificate> certificateChain, String applicationUri, String[] validHostnames)
      throws UaException {

    if (certificateChain.isEmpty() || !trustedCertificates.contains(certificateChain.get(0))) {
      throw new UaException(StatusCodes.Bad_CertificateUntrusted);
    }
  }

  @Override
  public void validateCertificateChain(
      List<X509Certificate> certificateChain,
      String applicationUri,
      String[] validHostnames,
      SecurityPolicyProfile securityPolicyProfile)
      throws UaException {

    validateCertificateChain(certificateChain, applicationUri, validHostnames);
  }
}
