/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionsDiagnosticsSummaryType extends BaseObjectType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionDiagnosticsArrayType getSessionDiagnosticsArrayNode();

  /** Gets the existing node's local value. */
  @Nullable SessionDiagnosticsDataType @Nullable [] getSessionDiagnosticsArray();

  /** Sets the existing node's local value. */
  void setSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionSecurityDiagnosticsArrayType getSessionSecurityDiagnosticsArrayNode();

  /** Gets the existing node's local value. */
  @Nullable SessionSecurityDiagnosticsDataType @Nullable [] getSessionSecurityDiagnosticsArray();

  /** Sets the existing node's local value. */
  void setSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value);
}
