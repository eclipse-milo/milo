package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.LogObjectTypeGetRecords;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the LogObjectType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2">Model
 *     documentation</a>
 */
public interface LogObjectType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19352L);

  QualifiedProperty<UInteger> MaxRecords_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxRecords",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> MinimumSeverity_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MinimumSeverity",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<Double> MaxStorageDuration_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxStorageDuration",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  /**
   * Resolves the optional MaxRecords child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxRecordsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxRecordsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxRecordsNodeAsync();

  /**
   * Reads the Value of the MaxRecords child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxRecords() throws UaException;

  /**
   * Writes the Value of the MaxRecords child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxRecords(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxRecords()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxRecordsAsync();

  /** Asynchronous form of {@link #writeMaxRecords}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxRecordsAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MinimumSeverity child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMinimumSeverityNode() throws UaException;

  /** Asynchronous form of {@link #getMinimumSeverityNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMinimumSeverityNodeAsync();

  /**
   * Reads the Value of the MinimumSeverity child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMinimumSeverity() throws UaException;

  /**
   * Writes the Value of the MinimumSeverity child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMinimumSeverity(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMinimumSeverity()}. */
  CompletableFuture<? extends @Nullable UShort> readMinimumSeverityAsync();

  /** Asynchronous form of {@link #writeMinimumSeverity}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMinimumSeverityAsync(@Nullable UShort value);

  /**
   * Resolves the optional MaxStorageDuration child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxStorageDurationNode() throws UaException;

  /** Asynchronous form of {@link #getMaxStorageDurationNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxStorageDurationNodeAsync();

  /**
   * Reads the Value of the MaxStorageDuration child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMaxStorageDuration() throws UaException;

  /**
   * Writes the Value of the MaxStorageDuration child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxStorageDuration(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMaxStorageDuration()}. */
  CompletableFuture<? extends @Nullable Double> readMaxStorageDurationAsync();

  /** Asynchronous form of {@link #writeMaxStorageDuration}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxStorageDurationAsync(@Nullable Double value);

  /**
   * Resolves the mandatory GetRecords Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3">Model
   *     documentation</a>
   */
  UaMethodNode getGetRecordsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetRecordsMethodNode()}. */
  CompletableFuture<UaMethodNode> getGetRecordsMethodNodeAsync();

  /**
   * Calls the GetRecords Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3">Model
   *     documentation</a>
   */
  LogObjectTypeGetRecords.Outputs getRecords(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException;

  /**
   * Calls the GetRecords Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<LogObjectTypeGetRecords.Outputs> callGetRecords(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException;

  /**
   * Calls the GetRecords Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<LogObjectTypeGetRecords.Outputs> callGetRecordsWith(
      MethodCallOptions options,
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn)
      throws UaException;

  /** Asynchronous form of {@link #getRecords}. */
  CompletableFuture<LogObjectTypeGetRecords.Outputs> getRecordsAsync(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn);

  /** Asynchronous form of {@link #callGetRecords}. */
  CompletableFuture<MethodCallResult<LogObjectTypeGetRecords.Outputs>> callGetRecordsAsync(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn);

  /** Asynchronous form of {@link #callGetRecordsWith}. */
  CompletableFuture<MethodCallResult<LogObjectTypeGetRecords.Outputs>> callGetRecordsWithAsync(
      MethodCallOptions options,
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn);

  /**
   * Resolves the optional ReleaseContinuationPoint Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getReleaseContinuationPointMethodNode() throws UaException;

  /** Asynchronous form of {@link #getReleaseContinuationPointMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getReleaseContinuationPointMethodNodeAsync();

  /**
   * Calls the ReleaseContinuationPoint Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4">Model
   *     documentation</a>
   */
  void releaseContinuationPoint(@Nullable ByteString continuationPointIn) throws UaException;

  /**
   * Calls the ReleaseContinuationPoint Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReleaseContinuationPoint(@Nullable ByteString continuationPointIn)
      throws UaException;

  /**
   * Calls the ReleaseContinuationPoint Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReleaseContinuationPointWith(
      MethodCallOptions options, @Nullable ByteString continuationPointIn) throws UaException;

  /** Asynchronous form of {@link #releaseContinuationPoint}. */
  CompletableFuture<Void> releaseContinuationPointAsync(@Nullable ByteString continuationPointIn);

  /** Asynchronous form of {@link #callReleaseContinuationPoint}. */
  CompletableFuture<MethodCallResult<Void>> callReleaseContinuationPointAsync(
      @Nullable ByteString continuationPointIn);

  /** Asynchronous form of {@link #callReleaseContinuationPointWith}. */
  CompletableFuture<MethodCallResult<Void>> callReleaseContinuationPointWithAsync(
      MethodCallOptions options, @Nullable ByteString continuationPointIn);
}
