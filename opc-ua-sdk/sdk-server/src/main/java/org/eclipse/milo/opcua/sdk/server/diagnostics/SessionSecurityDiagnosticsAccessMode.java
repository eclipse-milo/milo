/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics;

/** Controls authorization for Session security diagnostics and the diagnostics enabled flag. */
public enum SessionSecurityDiagnosticsAccessMode {

  /**
   * Derive access from the standard diagnostics nodes' RolePermissions. Session security
   * diagnostics are readable only by SecurityAdmin or an explicitly configured equivalent role,
   * while diagnostics may be enabled or disabled only by ConfigureAdmin, SecurityAdmin, or an
   * explicitly configured equivalent role.
   */
  RESTRICTED,

  /**
   * Preserve the previous authorization behavior without requiring role mapping. All Sessions may
   * read Session security diagnostics and enable or disable diagnostics.
   *
   * <p>AccessRestrictions, including signing and encryption requirements, remain enforced. This
   * mode is provided for compatibility and is less secure than {@link #RESTRICTED}.
   */
  LEGACY
}
