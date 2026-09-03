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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

/** An in-memory {@link TrustListManager} that publishes each update as one immutable snapshot. */
public class MemoryTrustListManager implements TrustListManager {

  private final AtomicReference<TrustListSnapshot> snapshot =
      new AtomicReference<>(
          new TrustListSnapshot(List.of(), List.of(), List.of(), List.of(), DateTime.MIN_VALUE));

  @Override
  public TrustListSnapshot getSnapshot() {
    return snapshot.get();
  }

  @Override
  public void replaceAll(TrustListSnapshot snapshot) {
    this.snapshot.set(Objects.requireNonNull(snapshot));
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
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                current.issuerCertificates(),
                issuerCrls,
                current.trustedCertificates(),
                current.trustedCrls(),
                DateTime.now()));
  }

  @Override
  public void setTrustedCrls(List<X509CRL> trustedCrls) {
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                current.issuerCertificates(),
                current.issuerCrls(),
                current.trustedCertificates(),
                trustedCrls,
                DateTime.now()));
  }

  @Override
  public void setIssuerCertificates(List<X509Certificate> issuerCertificates) {
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                issuerCertificates,
                current.issuerCrls(),
                current.trustedCertificates(),
                current.trustedCrls(),
                DateTime.now()));
  }

  @Override
  public void setTrustedCertificates(List<X509Certificate> trustedCertificates) {
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                current.issuerCertificates(),
                current.issuerCrls(),
                trustedCertificates,
                current.trustedCrls(),
                DateTime.now()));
  }

  @Override
  public void addIssuerCertificate(X509Certificate certificate) {
    updateSnapshot(
        current -> {
          var issuerCertificates = new ArrayList<>(current.issuerCertificates());
          issuerCertificates.add(certificate);

          return new TrustListSnapshot(
              issuerCertificates,
              current.issuerCrls(),
              current.trustedCertificates(),
              current.trustedCrls(),
              DateTime.now());
        });
  }

  @Override
  public void addTrustedCertificate(X509Certificate certificate) {
    updateSnapshot(
        current -> {
          var trustedCertificates = new ArrayList<>(current.trustedCertificates());
          trustedCertificates.add(certificate);

          return new TrustListSnapshot(
              current.issuerCertificates(),
              current.issuerCrls(),
              trustedCertificates,
              current.trustedCrls(),
              DateTime.now());
        });
  }

  @Override
  public boolean removeIssuerCertificate(ByteString thumbprint) {
    return removeCertificate(thumbprint, true);
  }

  @Override
  public boolean removeTrustedCertificate(ByteString thumbprint) {
    return removeCertificate(thumbprint, false);
  }

  @Override
  public DateTime getLastUpdateTime() {
    return snapshot.get().lastUpdateTime();
  }

  private boolean removeCertificate(ByteString thumbprint, boolean issuer) {
    while (true) {
      TrustListSnapshot current = snapshot.get();
      List<X509Certificate> certificates =
          issuer ? current.issuerCertificates() : current.trustedCertificates();
      var updatedCertificates = new ArrayList<>(certificates);

      if (!updatedCertificates.removeIf(c -> thumbprintMatches(c, thumbprint))) {
        return false;
      }

      TrustListSnapshot updated =
          issuer
              ? new TrustListSnapshot(
                  updatedCertificates,
                  current.issuerCrls(),
                  current.trustedCertificates(),
                  current.trustedCrls(),
                  DateTime.now())
              : new TrustListSnapshot(
                  current.issuerCertificates(),
                  current.issuerCrls(),
                  updatedCertificates,
                  current.trustedCrls(),
                  DateTime.now());

      if (snapshot.compareAndSet(current, updated)) {
        return true;
      }
    }
  }

  private void updateSnapshot(UnaryOperator<TrustListSnapshot> update) {
    snapshot.updateAndGet(update);
  }

  private static boolean thumbprintMatches(X509Certificate certificate, ByteString thumbprint) {
    try {
      return CertificateUtil.thumbprint(certificate).equals(thumbprint);
    } catch (UaException e) {
      return false;
    }
  }
}
