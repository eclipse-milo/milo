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

/**
 * Manages otherwise valid Certificates that have been rejected by the client or server because they
 * do not lead to a Trust Anchor.
 *
 * <p>AKA the "rejected list" and the target of the GetRejectedList methods defined by Part 12.
 */
public interface CertificateQuarantine {

  /**
   * Get the list of rejected Certificates.
   *
   * @return the list of rejected {@link X509Certificate}s.
   */
  List<X509Certificate> getRejectedCertificates();

  /**
   * Add a Certificate to the list of rejected Certificates.
   *
   * @param certificate the {@link X509Certificate} to add.
   */
  void addRejectedCertificate(X509Certificate certificate);

  /**
   * Remove a Certificate from the list of rejected Certificates.
   *
   * @param certificate the {@link X509Certificate} to remove.
   */
  void removeRejectedCertificate(X509Certificate certificate);
}
