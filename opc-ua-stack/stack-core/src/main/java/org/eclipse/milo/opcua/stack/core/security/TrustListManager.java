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
import java.util.Objects;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;

/**
 * Manages the issuer and trusted certificates and CRLs used for certificate validation.
 *
 * <p>Each OPC UA application should create one shared {@link TrustListManager}. Components that
 * receive the manager, such as a certificate validator, borrow it and do not close it.
 *
 * <p>Use {@link #getSnapshot()} when one operation needs multiple lists from the same manager
 * state, and {@link #update(UnaryOperator)} when one operation must read and then change that
 * state.
 *
 * <p>The application must call {@link #close()} when it shuts down. Implementations that do not
 * hold resources may use the default no-op implementation.
 */
public interface TrustListManager extends AutoCloseable {

  /**
   * Release any resources owned by this {@link TrustListManager}.
   *
   * <p>The default implementation does nothing.
   *
   * @throws Exception if an error occurs while releasing resources.
   */
  @Override
  default void close() throws Exception {}

  /**
   * Get an immutable snapshot of all trust lists and their last update time.
   *
   * <p>Built-in implementations return a snapshot captured atomically. The default reads the
   * individual properties in sequence and therefore cannot guarantee a coherent view during a
   * concurrent update.
   *
   * @return the current trust-list snapshot.
   */
  default TrustListSnapshot getSnapshot() {
    return new TrustListSnapshot(
        getIssuerCertificates(),
        getIssuerCrls(),
        getTrustedCertificates(),
        getTrustedCrls(),
        getLastUpdateTime());
  }

  /**
   * Apply {@code update} to the current snapshot and commit the result as one replacement.
   *
   * <p>{@code update} receives the current snapshot and returns the snapshot to commit. Returning
   * the same instance it received commits nothing. The manager sets the committed snapshot's last
   * update time to the commit time; the time carried by the returned snapshot is ignored.
   *
   * <p>Built-in implementations apply {@code update} under their own synchronization, so a
   * read-modify-write expressed through this method cannot lose a concurrent change. {@code update}
   * may be invoked more than once if the manager retries, so it should be free of side effects. The
   * default reads {@link #getSnapshot()} and calls the four individual setters in sequence; other
   * implementations must override this method to provide the same atomicity.
   *
   * @param update the transformation to apply.
   * @return the committed snapshot, or the current snapshot if nothing was committed.
   */
  default TrustListSnapshot update(UnaryOperator<TrustListSnapshot> update) {
    TrustListSnapshot current = getSnapshot();
    TrustListSnapshot updated = Objects.requireNonNull(update.apply(current));

    if (updated == current) {
      return current;
    }

    setIssuerCertificates(updated.issuerCertificates());
    setIssuerCrls(updated.issuerCrls());
    setTrustedCertificates(updated.trustedCertificates());
    setTrustedCrls(updated.trustedCrls());

    return getSnapshot();
  }

  /**
   * Replace all four trust lists with those in {@code snapshot} as one replacement.
   *
   * <p>Equivalent to {@code update(current -> snapshot)}; see {@link #update(UnaryOperator)} for
   * the atomicity and last update time contract.
   *
   * @param snapshot the replacement trust-list state.
   */
  default void replaceAll(TrustListSnapshot snapshot) {
    Objects.requireNonNull(snapshot);

    update(current -> snapshot);
  }

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
