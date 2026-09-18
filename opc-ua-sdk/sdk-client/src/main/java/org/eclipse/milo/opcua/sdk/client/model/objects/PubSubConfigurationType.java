package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeCloseAndUpdate;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeReserveIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">Model
 *     documentation</a>
 */
public interface PubSubConfigurationType extends FileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25482L);

  /**
   * Resolves the mandatory CloseAndUpdate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndUpdateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCloseAndUpdateMethodNode()}. */
  CompletableFuture<UaMethodNode> getCloseAndUpdateMethodNodeAsync();

  /**
   * Calls the CloseAndUpdate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6">Model
   *     documentation</a>
   */
  PubSubConfigurationTypeCloseAndUpdate.Outputs closeAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
      throws UaException;

  /**
   * Calls the CloseAndUpdate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs> callCloseAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
      throws UaException;

  /**
   * Calls the CloseAndUpdate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs> callCloseAndUpdateWith(
      MethodCallOptions options,
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
      throws UaException;

  /** Asynchronous form of {@link #closeAndUpdate}. */
  CompletableFuture<PubSubConfigurationTypeCloseAndUpdate.Outputs> closeAndUpdateAsync(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences);

  /** Asynchronous form of {@link #callCloseAndUpdate}. */
  CompletableFuture<MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateAsync(
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences);

  /** Asynchronous form of {@link #callCloseAndUpdateWith}. */
  CompletableFuture<MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateWithAsync(
          MethodCallOptions options,
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences);

  /**
   * Resolves the mandatory ReserveIds Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5">Model
   *     documentation</a>
   */
  UaMethodNode getReserveIdsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getReserveIdsMethodNode()}. */
  CompletableFuture<UaMethodNode> getReserveIdsMethodNodeAsync();

  /**
   * Calls the ReserveIds Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.5">Model
   *     documentation</a>
   */
  PubSubConfigurationTypeReserveIds.Outputs reserveIds(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException;

  /**
   * Calls the ReserveIds Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs> callReserveIds(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException;

  /**
   * Calls the ReserveIds Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs> callReserveIdsWith(
      MethodCallOptions options,
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException;

  /** Asynchronous form of {@link #reserveIds}. */
  CompletableFuture<PubSubConfigurationTypeReserveIds.Outputs> reserveIdsAsync(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds);

  /** Asynchronous form of {@link #callReserveIds}. */
  CompletableFuture<MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs>>
      callReserveIdsAsync(
          @Nullable String transportProfileUri,
          @Nullable UShort numReqWriterGroupIds,
          @Nullable UShort numReqDataSetWriterIds);

  /** Asynchronous form of {@link #callReserveIdsWith}. */
  CompletableFuture<MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs>>
      callReserveIdsWithAsync(
          MethodCallOptions options,
          @Nullable String transportProfileUri,
          @Nullable UShort numReqWriterGroupIds,
          @Nullable UShort numReqDataSetWriterIds);
}
