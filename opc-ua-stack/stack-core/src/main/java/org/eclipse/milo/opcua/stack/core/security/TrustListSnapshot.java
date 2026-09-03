/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;

/**
 * An immutable view of all certificate trust lists and the time they were last updated.
 *
 * <p>Use a snapshot when an operation must read more than one list consistently. Each list is an
 * immutable copy of the list supplied at construction time, with duplicate entries removed while
 * preserving the first occurrence.
 *
 * <p>The {@code with*} methods derive a new snapshot with one list replaced. They keep {@code
 * lastUpdateTime} unchanged; a {@link TrustListManager} stamps the time when it commits.
 *
 * @param issuerCertificates the issuer certificates.
 * @param issuerCrls the issuer certificate revocation lists.
 * @param trustedCertificates the trusted certificates.
 * @param trustedCrls the trusted certificate revocation lists.
 * @param lastUpdateTime the time the trust lists were last updated.
 */
public record TrustListSnapshot(
    List<X509Certificate> issuerCertificates,
    List<X509CRL> issuerCrls,
    List<X509Certificate> trustedCertificates,
    List<X509CRL> trustedCrls,
    DateTime lastUpdateTime) {

  public TrustListSnapshot {
    issuerCertificates = distinct(issuerCertificates);
    issuerCrls = distinct(issuerCrls);
    trustedCertificates = distinct(trustedCertificates);
    trustedCrls = distinct(trustedCrls);
    lastUpdateTime = Objects.requireNonNull(lastUpdateTime);
  }

  /**
   * @return a snapshot with all lists empty and {@code lastUpdateTime} set to {@link
   *     DateTime#MIN_VALUE}.
   */
  public static TrustListSnapshot empty() {
    return new TrustListSnapshot(List.of(), List.of(), List.of(), List.of(), DateTime.MIN_VALUE);
  }

  /**
   * @param issuerCertificates the replacement issuer certificates.
   * @return a copy of this snapshot with {@code issuerCertificates} replaced.
   */
  public TrustListSnapshot withIssuerCertificates(List<X509Certificate> issuerCertificates) {
    return new TrustListSnapshot(
        issuerCertificates, issuerCrls, trustedCertificates, trustedCrls, lastUpdateTime);
  }

  /**
   * @param issuerCrls the replacement issuer CRLs.
   * @return a copy of this snapshot with {@code issuerCrls} replaced.
   */
  public TrustListSnapshot withIssuerCrls(List<X509CRL> issuerCrls) {
    return new TrustListSnapshot(
        issuerCertificates, issuerCrls, trustedCertificates, trustedCrls, lastUpdateTime);
  }

  /**
   * @param trustedCertificates the replacement trusted certificates.
   * @return a copy of this snapshot with {@code trustedCertificates} replaced.
   */
  public TrustListSnapshot withTrustedCertificates(List<X509Certificate> trustedCertificates) {
    return new TrustListSnapshot(
        issuerCertificates, issuerCrls, trustedCertificates, trustedCrls, lastUpdateTime);
  }

  /**
   * @param trustedCrls the replacement trusted CRLs.
   * @return a copy of this snapshot with {@code trustedCrls} replaced.
   */
  public TrustListSnapshot withTrustedCrls(List<X509CRL> trustedCrls) {
    return new TrustListSnapshot(
        issuerCertificates, issuerCrls, trustedCertificates, trustedCrls, lastUpdateTime);
  }

  /**
   * @param lastUpdateTime the replacement last update time.
   * @return a copy of this snapshot with {@code lastUpdateTime} replaced.
   */
  public TrustListSnapshot withLastUpdateTime(DateTime lastUpdateTime) {
    return new TrustListSnapshot(
        issuerCertificates, issuerCrls, trustedCertificates, trustedCrls, lastUpdateTime);
  }

  private static <T> List<T> distinct(List<T> list) {
    return List.copyOf(new LinkedHashSet<>(list));
  }
}
