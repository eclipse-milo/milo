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

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

/** Snapshot edits shared by the built-in {@link TrustListManager} implementations. */
final class TrustListEdits {

  private TrustListEdits() {}

  static List<X509Certificate> append(
      List<X509Certificate> certificates, X509Certificate certificate) {

    return Stream.concat(certificates.stream(), Stream.of(certificate)).toList();
  }

  /**
   * Remove the certificate identified by {@code thumbprint} from one of {@code manager}'s
   * certificate lists via {@link TrustListManager#update}.
   *
   * @param manager the manager to update.
   * @param thumbprint the thumbprint to remove.
   * @param list reads the target list from a snapshot.
   * @param with derives a snapshot with the target list replaced.
   * @return {@code true} if a certificate with a matching thumbprint was removed.
   */
  static boolean remove(
      TrustListManager manager,
      ByteString thumbprint,
      Function<TrustListSnapshot, List<X509Certificate>> list,
      BiFunction<TrustListSnapshot, List<X509Certificate>, TrustListSnapshot> with) {

    // The operator may run more than once if the manager retries; the last run is the one that
    // commits, so the flag it leaves behind is the right one.
    var removed = new AtomicBoolean();

    manager.update(
        current -> {
          var certificates = new ArrayList<>(list.apply(current));
          removed.set(certificates.removeIf(c -> thumbprintMatches(c, thumbprint)));

          return removed.get() ? with.apply(current, certificates) : current;
        });

    return removed.get();
  }

  private static boolean thumbprintMatches(X509Certificate certificate, ByteString thumbprint) {
    try {
      return CertificateUtil.thumbprint(certificate).equals(thumbprint);
    } catch (UaException e) {
      return false;
    }
  }
}
