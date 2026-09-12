/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SamplingIntervalDiagnosticsType extends BaseDataVariableType {
  /** Gets the existing node's local value. */
  @Nullable Double getSamplingInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setSamplingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readSamplingInterval() throws UaException;

  /** Writes the value remotely. */
  void writeSamplingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readSamplingIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSamplingIntervalAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSamplingIntervalNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSamplingIntervalNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getSampledMonitoredItemsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readSampledMonitoredItemsCount() throws UaException;

  /** Writes the value remotely. */
  void writeSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readSampledMonitoredItemsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSampledMonitoredItemsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSampledMonitoredItemsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSampledMonitoredItemsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSampledMonitoredItemsCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxSampledMonitoredItemsCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaxSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSampledMonitoredItemsCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxSampledMonitoredItemsCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxSampledMonitoredItemsCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxSampledMonitoredItemsCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDisabledMonitoredItemsSamplingCount() throws UaException;

  /** Sets the existing node's local value. */
  void setDisabledMonitoredItemsSamplingCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readDisabledMonitoredItemsSamplingCount() throws UaException;

  /** Writes the value remotely. */
  void writeDisabledMonitoredItemsSamplingCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readDisabledMonitoredItemsSamplingCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDisabledMonitoredItemsSamplingCountAsync(
      @Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDisabledMonitoredItemsSamplingCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType>
      getDisabledMonitoredItemsSamplingCountNodeAsync();
}
