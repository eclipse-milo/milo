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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerDiagnosticsType extends BaseObjectType {
  QualifiedProperty<Boolean> ENABLED_FLAG =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EnabledFlag",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getEnabledFlag();

  /** Sets the existing node's local value. */
  void setEnabledFlag(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEnabledFlagNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerDiagnosticsSummaryType getServerDiagnosticsSummaryNode();

  /** Gets the existing node's local value. */
  @Nullable ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary();

  /** Sets the existing node's local value. */
  void setServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable SamplingIntervalDiagnosticsArrayType getSamplingIntervalDiagnosticsArrayNode();

  /** Gets the existing node's local value. */
  @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] getSamplingIntervalDiagnosticsArray();

  /** Sets the existing node's local value. */
  void setSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode();

  /** Gets the existing node's local value. */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray();

  /** Sets the existing node's local value. */
  void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionsDiagnosticsSummaryType getSessionsDiagnosticsSummaryNode();
}
