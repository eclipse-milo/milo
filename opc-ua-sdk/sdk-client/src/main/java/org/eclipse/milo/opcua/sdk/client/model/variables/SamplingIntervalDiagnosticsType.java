package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SamplingIntervalDiagnosticsType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10">Model
 *     documentation</a>
 */
public interface SamplingIntervalDiagnosticsType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2165L);

  /**
   * Resolves the mandatory SamplingInterval child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSamplingIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getSamplingIntervalNode()}. */
  CompletableFuture<? extends VariableNode> getSamplingIntervalNodeAsync();

  /**
   * Reads the Value of the SamplingInterval child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readSamplingInterval() throws UaException;

  /**
   * Writes the Value of the SamplingInterval child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSamplingInterval(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readSamplingInterval()}. */
  CompletableFuture<? extends @Nullable Double> readSamplingIntervalAsync();

  /** Asynchronous form of {@link #writeSamplingInterval}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSamplingIntervalAsync(@Nullable Double value);

  /**
   * Resolves the mandatory SampledMonitoredItemsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSampledMonitoredItemsCountNode() throws UaException;

  /** Asynchronous form of {@link #getSampledMonitoredItemsCountNode()}. */
  CompletableFuture<? extends VariableNode> getSampledMonitoredItemsCountNodeAsync();

  /**
   * Reads the Value of the SampledMonitoredItemsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSampledMonitoredItemsCount() throws UaException;

  /**
   * Writes the Value of the SampledMonitoredItemsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSampledMonitoredItemsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readSampledMonitoredItemsCountAsync();

  /**
   * Asynchronous form of {@link #writeSampledMonitoredItemsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSampledMonitoredItemsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxSampledMonitoredItemsCount child, a BaseDataVariableType with
   * DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getMaxSampledMonitoredItemsCountNode() throws UaException;

  /** Asynchronous form of {@link #getMaxSampledMonitoredItemsCountNode()}. */
  CompletableFuture<? extends VariableNode> getMaxSampledMonitoredItemsCountNodeAsync();

  /**
   * Reads the Value of the MaxSampledMonitoredItemsCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxSampledMonitoredItemsCount() throws UaException;

  /**
   * Writes the Value of the MaxSampledMonitoredItemsCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxSampledMonitoredItemsCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSampledMonitoredItemsCountAsync();

  /**
   * Asynchronous form of {@link #writeMaxSampledMonitoredItemsCount}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxSampledMonitoredItemsCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DisabledMonitoredItemsSamplingCount child, a BaseDataVariableType with
   * DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getDisabledMonitoredItemsSamplingCountNode() throws UaException;

  /** Asynchronous form of {@link #getDisabledMonitoredItemsSamplingCountNode()}. */
  CompletableFuture<? extends VariableNode> getDisabledMonitoredItemsSamplingCountNodeAsync();

  /**
   * Reads the Value of the DisabledMonitoredItemsSamplingCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDisabledMonitoredItemsSamplingCount() throws UaException;

  /**
   * Writes the Value of the DisabledMonitoredItemsSamplingCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDisabledMonitoredItemsSamplingCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDisabledMonitoredItemsSamplingCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readDisabledMonitoredItemsSamplingCountAsync();

  /**
   * Asynchronous form of {@link #writeDisabledMonitoredItemsSamplingCount}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeDisabledMonitoredItemsSamplingCountAsync(
      @Nullable UInteger value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SamplingIntervalDiagnosticsDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable SamplingIntervalDiagnosticsDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(
      @Nullable SamplingIntervalDiagnosticsDataType value);
}
