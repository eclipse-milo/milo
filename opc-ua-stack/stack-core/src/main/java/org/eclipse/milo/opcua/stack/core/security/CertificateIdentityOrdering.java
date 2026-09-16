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

import java.util.Comparator;
import org.jspecify.annotations.NullMarked;

/**
 * Ordering applied to the identities within one {@link CertificateGroup}.
 *
 * <p>Precedence across groups is not an ordering concern: a {@link CertificateManager} lists
 * identities in group registration order and a selection context lists candidate groups in
 * precedence order.
 */
@NullMarked
final class CertificateIdentityOrdering {

  /** Orders identities by the parseable string of their certificate type id. */
  static final Comparator<CertificateIdentity> STABLE =
      Comparator.comparing(identity -> identity.certificateTypeId().toParseableString());

  private CertificateIdentityOrdering() {}
}
