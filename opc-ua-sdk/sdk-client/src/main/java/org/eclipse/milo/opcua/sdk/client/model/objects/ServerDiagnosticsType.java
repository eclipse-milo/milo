/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  @Nullable Boolean getEnabledFlag() throws UaException;

  /** Sets the existing node's local value. */
  void setEnabledFlag(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readEnabledFlag() throws UaException;

  /** Writes the value remotely. */
  void writeEnabledFlag(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readEnabledFlagAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnabledFlagAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEnabledFlagNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEnabledFlagNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary() throws UaException;

  /** Sets the existing node's local value. */
  void setServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServerDiagnosticsSummaryDataType readServerDiagnosticsSummary() throws UaException;

  /** Writes the value remotely. */
  void writeServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServerDiagnosticsSummaryDataType>
      readServerDiagnosticsSummaryAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerDiagnosticsSummaryAsync(
      @Nullable ServerDiagnosticsSummaryDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerDiagnosticsSummaryType getServerDiagnosticsSummaryNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ServerDiagnosticsSummaryType> getServerDiagnosticsSummaryNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] getSamplingIntervalDiagnosticsArray()
      throws UaException;

  /** Sets the existing node's local value. */
  void setSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] readSamplingIntervalDiagnosticsArray()
      throws UaException;

  /** Writes the value remotely. */
  void writeSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsDataType @Nullable []>
      readSamplingIntervalDiagnosticsArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsArrayAsync(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable SamplingIntervalDiagnosticsArrayType getSamplingIntervalDiagnosticsArrayNode()
      throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsArrayType>
      getSamplingIntervalDiagnosticsArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray()
      throws UaException;

  /** Sets the existing node's local value. */
  void setSubscriptionDiagnosticsArray(@Nullable SubscriptionDiagnosticsDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] readSubscriptionDiagnosticsArray()
      throws UaException;

  /** Writes the value remotely. */
  void writeSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readSubscriptionDiagnosticsArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SubscriptionDiagnosticsArrayType>
      getSubscriptionDiagnosticsArrayNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionsDiagnosticsSummaryType getSessionsDiagnosticsSummaryNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SessionsDiagnosticsSummaryType>
      getSessionsDiagnosticsSummaryNodeAsync();
}
