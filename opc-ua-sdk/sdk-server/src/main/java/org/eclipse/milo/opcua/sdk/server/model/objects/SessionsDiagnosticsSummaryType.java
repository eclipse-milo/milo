/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4</a>
 */
public interface SessionsDiagnosticsSummaryType extends BaseObjectType {
  SessionDiagnosticsArrayType getSessionDiagnosticsArrayNode();

  SessionDiagnosticsDataType[] getSessionDiagnosticsArray();

  void setSessionDiagnosticsArray(SessionDiagnosticsDataType[] value);

  SessionSecurityDiagnosticsArrayType getSessionSecurityDiagnosticsArrayNode();

  SessionSecurityDiagnosticsDataType[] getSessionSecurityDiagnosticsArray();

  void setSessionSecurityDiagnosticsArray(SessionSecurityDiagnosticsDataType[] value);
}
