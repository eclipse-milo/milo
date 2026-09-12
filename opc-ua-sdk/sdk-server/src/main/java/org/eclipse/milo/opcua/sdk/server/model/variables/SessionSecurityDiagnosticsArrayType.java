/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.15</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionSecurityDiagnosticsArrayType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionSecurityDiagnosticsType getSessionSecurityDiagnosticsNode();

  /** Gets the existing node's local value. */
  @Nullable SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics();

  /** Sets the existing node's local value. */
  void setSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value);
}
