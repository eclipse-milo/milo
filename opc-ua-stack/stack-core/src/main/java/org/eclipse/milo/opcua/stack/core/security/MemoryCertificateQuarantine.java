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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MemoryCertificateQuarantine implements CertificateQuarantine {

  static final int DEFAULT_MAX_REJECTED_CERTIFICATES = 128;

  private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

  private final Deque<X509Certificate> certificates = new ArrayDeque<>();

  public MemoryCertificateQuarantine() {
    this(DEFAULT_MAX_REJECTED_CERTIFICATES);
  }

  public MemoryCertificateQuarantine(int maxRejectedCertificates) {
    this.maxRejectedCertificates = maxRejectedCertificates;
  }

  private final int maxRejectedCertificates;

  @Override
  public List<X509Certificate> getRejectedCertificates() {
    try {
      lock.readLock().lock();

      return List.copyOf(certificates);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public void addRejectedCertificate(X509Certificate certificate) {
    try {
      lock.writeLock().lock();

      certificates.add(certificate);

      while (certificates.size() > maxRejectedCertificates) {
        certificates.removeFirst();
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void removeRejectedCertificate(X509Certificate certificate) {
    try {
      lock.writeLock().lock();

      certificates.removeIf(c -> Objects.equals(c, certificate));
    } finally {
      lock.writeLock().unlock();
    }
  }
}
