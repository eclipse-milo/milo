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
import java.util.List;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;

/**
 * An immutable view of all certificate trust lists and the time they were last updated.
 *
 * <p>Use a snapshot when an operation must read more than one list consistently. The contained
 * lists are immutable copies of the lists supplied at construction time.
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
    issuerCertificates = List.copyOf(issuerCertificates);
    issuerCrls = List.copyOf(issuerCrls);
    trustedCertificates = List.copyOf(trustedCertificates);
    trustedCrls = List.copyOf(trustedCrls);
    lastUpdateTime = Objects.requireNonNull(lastUpdateTime);
  }
}
