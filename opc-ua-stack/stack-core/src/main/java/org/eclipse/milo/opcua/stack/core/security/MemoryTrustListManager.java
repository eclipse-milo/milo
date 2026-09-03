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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;

/** An in-memory {@link TrustListManager} that publishes each update as one immutable snapshot. */
public class MemoryTrustListManager implements TrustListManager {

  private final AtomicReference<TrustListSnapshot> snapshot =
      new AtomicReference<>(TrustListSnapshot.empty());

  @Override
  public TrustListSnapshot getSnapshot() {
    return snapshot.get();
  }

  @Override
  public TrustListSnapshot update(UnaryOperator<TrustListSnapshot> update) {
    return snapshot.updateAndGet(
        current -> {
          TrustListSnapshot updated = Objects.requireNonNull(update.apply(current));

          return updated == current ? current : updated.withLastUpdateTime(DateTime.now());
        });
  }

  @Override
  public List<X509CRL> getIssuerCrls() {
    return snapshot.get().issuerCrls();
  }

  @Override
  public List<X509CRL> getTrustedCrls() {
    return snapshot.get().trustedCrls();
  }

  @Override
  public List<X509Certificate> getIssuerCertificates() {
    return snapshot.get().issuerCertificates();
  }

  @Override
  public List<X509Certificate> getTrustedCertificates() {
    return snapshot.get().trustedCertificates();
  }

  @Override
  public void setIssuerCrls(List<X509CRL> issuerCrls) {
    update(current -> current.withIssuerCrls(issuerCrls));
  }

  @Override
  public void setTrustedCrls(List<X509CRL> trustedCrls) {
    update(current -> current.withTrustedCrls(trustedCrls));
  }

  @Override
  public void setIssuerCertificates(List<X509Certificate> issuerCertificates) {
    update(current -> current.withIssuerCertificates(issuerCertificates));
  }

  @Override
  public void setTrustedCertificates(List<X509Certificate> trustedCertificates) {
    update(current -> current.withTrustedCertificates(trustedCertificates));
  }

  @Override
  public void addIssuerCertificate(X509Certificate certificate) {
    update(
        current ->
            current.withIssuerCertificates(
                TrustListEdits.append(current.issuerCertificates(), certificate)));
  }

  @Override
  public void addTrustedCertificate(X509Certificate certificate) {
    update(
        current ->
            current.withTrustedCertificates(
                TrustListEdits.append(current.trustedCertificates(), certificate)));
  }

  @Override
  public boolean removeIssuerCertificate(ByteString thumbprint) {
    return TrustListEdits.remove(
        this,
        thumbprint,
        TrustListSnapshot::issuerCertificates,
        TrustListSnapshot::withIssuerCertificates);
  }

  @Override
  public boolean removeTrustedCertificate(ByteString thumbprint) {
    return TrustListEdits.remove(
        this,
        thumbprint,
        TrustListSnapshot::trustedCertificates,
        TrustListSnapshot::withTrustedCertificates);
  }

  @Override
  public DateTime getLastUpdateTime() {
    return snapshot.get().lastUpdateTime();
  }
}
