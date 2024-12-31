/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5</a>
 */
public interface SessionDiagnosticsObjectType extends BaseObjectType {
  SessionDiagnosticsVariableType getSessionDiagnosticsNode();

  SessionDiagnosticsDataType getSessionDiagnostics();

  void setSessionDiagnostics(SessionDiagnosticsDataType value);

  SessionSecurityDiagnosticsType getSessionSecurityDiagnosticsNode();

  SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics();

  void setSessionSecurityDiagnostics(SessionSecurityDiagnosticsDataType value);

  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode();

  SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray();

  void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value);
}
