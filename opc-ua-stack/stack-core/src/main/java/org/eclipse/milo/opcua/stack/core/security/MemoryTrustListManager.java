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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

public class MemoryTrustListManager implements TrustListManager {

  private final AtomicReference<DateTime> lastUpdateTime =
      new AtomicReference<>(DateTime.MIN_VALUE);

  private final List<X509Certificate> issuerCertificates =
      Collections.synchronizedList(new ArrayList<>());
  private final List<X509Certificate> trustedCertificates =
      Collections.synchronizedList(new ArrayList<>());

  private final List<X509CRL> issuerCrls = Collections.synchronizedList(new ArrayList<>());
  private final List<X509CRL> trustedCrls = Collections.synchronizedList(new ArrayList<>());

  @Override
  public List<X509CRL> getIssuerCrls() {
    synchronized (issuerCrls) {
      return List.copyOf(issuerCrls);
    }
  }

  @Override
  public List<X509CRL> getTrustedCrls() {
    synchronized (trustedCrls) {
      return List.copyOf(trustedCrls);
    }
  }

  @Override
  public List<X509Certificate> getIssuerCertificates() {
    synchronized (issuerCertificates) {
      return List.copyOf(issuerCertificates);
    }
  }

  @Override
  public List<X509Certificate> getTrustedCertificates() {
    synchronized (trustedCertificates) {
      return List.copyOf(trustedCertificates);
    }
  }

  @Override
  public void setIssuerCrls(List<X509CRL> issuerCrls) {
    synchronized (this.issuerCrls) {
      this.issuerCrls.clear();
      this.issuerCrls.addAll(issuerCrls);

      lastUpdateTime.set(DateTime.now());
    }
  }

  @Override
  public void setTrustedCrls(List<X509CRL> trustedCrls) {
    synchronized (this.trustedCrls) {
      this.trustedCrls.clear();
      this.trustedCrls.addAll(trustedCrls);

      lastUpdateTime.set(DateTime.now());
    }
  }

  @Override
  public void setIssuerCertificates(List<X509Certificate> issuerCertificates) {
    synchronized (this.issuerCertificates) {
      this.issuerCertificates.clear();
      this.issuerCertificates.addAll(issuerCertificates);

      lastUpdateTime.set(DateTime.now());
    }
  }

  @Override
  public void setTrustedCertificates(List<X509Certificate> trustedCertificates) {
    synchronized (this.trustedCertificates) {
      this.trustedCertificates.clear();
      this.trustedCertificates.addAll(trustedCertificates);

      lastUpdateTime.set(DateTime.now());
    }
  }

  @Override
  public void addIssuerCertificate(X509Certificate certificate) {
    issuerCertificates.add(certificate);

    lastUpdateTime.set(DateTime.now());
  }

  @Override
  public void addTrustedCertificate(X509Certificate certificate) {
    trustedCertificates.add(certificate);

    lastUpdateTime.set(DateTime.now());
  }

  @Override
  public boolean removeIssuerCertificate(ByteString thumbprint) {
    boolean removed = issuerCertificates.removeIf(c -> thumbprintMatches(c, thumbprint));

    if (removed) {
      lastUpdateTime.set(DateTime.now());
    }

    return removed;
  }

  @Override
  public boolean removeTrustedCertificate(ByteString thumbprint) {
    boolean removed = trustedCertificates.removeIf(c -> thumbprintMatches(c, thumbprint));

    if (removed) {
      lastUpdateTime.set(DateTime.now());
    }

    return removed;
  }

  @Override
  public DateTime getLastUpdateTime() {
    return lastUpdateTime.get();
  }

  private static boolean thumbprintMatches(X509Certificate certificate, ByteString thumbprint) {
    try {
      return CertificateUtil.thumbprint(certificate).equals(thumbprint);
    } catch (UaException e) {
      return false;
    }
  }
}
