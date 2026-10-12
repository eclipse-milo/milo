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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.NullMarked;

/** Selects a usable local certificate identity for a policy-sensitive operation. */
@NullMarked
public interface CertificateIdentitySelector {

  /**
   * Select a local certificate identity for {@code context}.
   *
   * @param context the selection inputs.
   * @return the selected identity, or empty if no usable identity matches.
   * @throws UaException if certificate thumbprint calculation fails while evaluating the context.
   */
  Optional<CertificateIdentity> select(CertificateIdentitySelectionContext context)
      throws UaException;
}
