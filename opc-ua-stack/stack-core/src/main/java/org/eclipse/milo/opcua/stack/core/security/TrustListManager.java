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

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;

public interface TrustListManager {

  /**
   * Get the list of Issuer CRLs.
   *
   * @return the list of Issuer {@link X509CRL}s.
   */
  List<X509CRL> getIssuerCrls();

  /**
   * Get the list of Trusted CRLs.
   *
   * @return the list of Trusted {@link X509CRL}s.
   */
  List<X509CRL> getTrustedCrls();

  /**
   * Get the list of Issuer Certificates.
   *
   * @return the list of Issuer {@link X509Certificate}s.
   */
  List<X509Certificate> getIssuerCertificates();

  /**
   * Get the list of Trusted Certificates.
   *
   * @return the list of Trusted {@link X509Certificate}s.
   */
  List<X509Certificate> getTrustedCertificates();

  /**
   * Set a new list of Issuer CRLs. This replaces any existing Issuer CRLs.
   *
   * @param issuerCrls a new list of issuer {@link X509CRL}s.
   */
  void setIssuerCrls(List<X509CRL> issuerCrls);

  /**
   * Set a new list of Trusted CRLs. This replaces any existing Trusted CRLs.
   *
   * @param trustedCrls a new list of trusted {@link X509CRL}s.
   */
  void setTrustedCrls(List<X509CRL> trustedCrls);

  /**
   * Set a new list of Issuer Certificates. This replaces any existing Issuer Certificates.
   *
   * @param issuerCertificates a new list of issuer {@link X509Certificate}s.
   */
  void setIssuerCertificates(List<X509Certificate> issuerCertificates);

  /**
   * Set a new list of Trusted Certificates. This replaces any existing Trusted Certificates.
   *
   * @param trustedCertificates a new list of trusted {@link X509Certificate}s.
   */
  void setTrustedCertificates(List<X509Certificate> trustedCertificates);

  /**
   * Add {@code certificate} to the Issuer Certificates list.
   *
   * @param certificate the {@link X509Certificate} to add to the Issuer Certificates list.
   */
  void addIssuerCertificate(X509Certificate certificate);

  /**
   * Add {@code certificate} to the Trusted Certificates list.
   *
   * @param certificate the {@link X509Certificate} to add to the Trusted Certificates list.
   */
  void addTrustedCertificate(X509Certificate certificate);

  /**
   * Remove the certificate identified by {@code thumbprint} from the Issuer Certificates list.
   *
   * @param thumbprint the certificate thumbprint.
   * @return {@code true} if a certificate with a matching thumbprint was found.
   */
  boolean removeIssuerCertificate(ByteString thumbprint);

  /**
   * Remove the certificate identified by {@code thumbprint} from the Trusted Certificates list.
   *
   * @param thumbprint the certificate thumbprint.
   * @return {@code true} if a certificate with a matching thumbprint was found.
   */
  boolean removeTrustedCertificate(ByteString thumbprint);

  /**
   * Get the last time the Trust List was updated.
   *
   * @return the last time the Trust List was updated.
   */
  DateTime getLastUpdateTime();
}
