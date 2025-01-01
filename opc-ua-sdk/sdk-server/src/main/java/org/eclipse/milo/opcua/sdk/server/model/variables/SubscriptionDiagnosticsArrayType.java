/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11</a>
 */
public interface SubscriptionDiagnosticsArrayType extends BaseDataVariableType {
  SubscriptionDiagnosticsType getSubscriptionDiagnosticsNode();

  SubscriptionDiagnosticsDataType getSubscriptionDiagnostics();

  void setSubscriptionDiagnostics(SubscriptionDiagnosticsDataType value);
}
