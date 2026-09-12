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

import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.13</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionDiagnosticsArrayType extends BaseDataVariableType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionDiagnosticsVariableType getSessionDiagnosticsNode();

  /** Gets the existing node's local value. */
  @Nullable SessionDiagnosticsDataType getSessionDiagnostics();

  /** Sets the existing node's local value. */
  void setSessionDiagnostics(@Nullable SessionDiagnosticsDataType value);
}
