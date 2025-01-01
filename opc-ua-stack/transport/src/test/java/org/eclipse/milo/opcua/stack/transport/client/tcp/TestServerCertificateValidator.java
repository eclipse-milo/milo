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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;

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

    // noop
  }
}
