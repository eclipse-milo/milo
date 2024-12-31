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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3</a>
 */
public interface ServerDiagnosticsType extends BaseObjectType {
  QualifiedProperty<Boolean> ENABLED_FLAG =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnabledFlag",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  Boolean getEnabledFlag();

  void setEnabledFlag(Boolean value);

  PropertyType getEnabledFlagNode();

  ServerDiagnosticsSummaryType getServerDiagnosticsSummaryNode();

  ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary();

  void setServerDiagnosticsSummary(ServerDiagnosticsSummaryDataType value);

  SamplingIntervalDiagnosticsArrayType getSamplingIntervalDiagnosticsArrayNode();

  SamplingIntervalDiagnosticsDataType[] getSamplingIntervalDiagnosticsArray();

  void setSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsDataType[] value);

  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode();

  SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray();

  void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value);

  SessionsDiagnosticsSummaryType getSessionsDiagnosticsSummaryNode();
}
