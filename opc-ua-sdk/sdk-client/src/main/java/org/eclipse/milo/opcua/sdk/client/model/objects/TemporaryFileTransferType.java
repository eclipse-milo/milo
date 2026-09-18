package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForRead;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForWrite;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the TemporaryFileTransferType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1">Model
 *     documentation</a>
 */
public interface TemporaryFileTransferType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15744L);

  QualifiedProperty<Double> ClientProcessingTimeout_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientProcessingTimeout",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  /**
   * Resolves the mandatory ClientProcessingTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClientProcessingTimeoutNode() throws UaException;

  /** Asynchronous form of {@link #getClientProcessingTimeoutNode()}. */
  CompletableFuture<? extends PropertyType> getClientProcessingTimeoutNodeAsync();

  /**
   * Reads the Value of the ClientProcessingTimeout child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readClientProcessingTimeout() throws UaException;

  /**
   * Writes the Value of the ClientProcessingTimeout child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientProcessingTimeout(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readClientProcessingTimeout()}. */
  CompletableFuture<? extends @Nullable Double> readClientProcessingTimeoutAsync();

  /**
   * Asynchronous form of {@link #writeClientProcessingTimeout}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeClientProcessingTimeoutAsync(@Nullable Double value);

  /**
   * Resolves the mandatory CloseAndCommit Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndCommitMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCloseAndCommitMethodNode()}. */
  CompletableFuture<UaMethodNode> getCloseAndCommitMethodNodeAsync();

  /**
   * Calls the CloseAndCommit Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5">Model
   *     documentation</a>
   */
  @Nullable NodeId closeAndCommit(@Nullable UInteger fileHandle) throws UaException;

  /**
   * Calls the CloseAndCommit Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCloseAndCommit(@Nullable UInteger fileHandle)
      throws UaException;

  /**
   * Calls the CloseAndCommit Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCloseAndCommitWith(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException;

  /** Asynchronous form of {@link #closeAndCommit}. */
  CompletableFuture<@Nullable NodeId> closeAndCommitAsync(@Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callCloseAndCommit}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCloseAndCommitAsync(
      @Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callCloseAndCommitWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCloseAndCommitWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle);

  /**
   * Resolves the mandatory GenerateFileForRead Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3">Model
   *     documentation</a>
   */
  UaMethodNode getGenerateFileForReadMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGenerateFileForReadMethodNode()}. */
  CompletableFuture<UaMethodNode> getGenerateFileForReadMethodNodeAsync();

  /**
   * Calls the GenerateFileForRead Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3">Model
   *     documentation</a>
   */
  TemporaryFileTransferTypeGenerateFileForRead.Outputs generateFileForRead(
      @Nullable Variant generateOptions) throws UaException;

  /**
   * Calls the GenerateFileForRead Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs> callGenerateFileForRead(
      @Nullable Variant generateOptions) throws UaException;

  /**
   * Calls the GenerateFileForRead Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs>
      callGenerateFileForReadWith(MethodCallOptions options, @Nullable Variant generateOptions)
          throws UaException;

  /** Asynchronous form of {@link #generateFileForRead}. */
  CompletableFuture<TemporaryFileTransferTypeGenerateFileForRead.Outputs> generateFileForReadAsync(
      @Nullable Variant generateOptions);

  /** Asynchronous form of {@link #callGenerateFileForRead}. */
  CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs>>
      callGenerateFileForReadAsync(@Nullable Variant generateOptions);

  /** Asynchronous form of {@link #callGenerateFileForReadWith}. */
  CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs>>
      callGenerateFileForReadWithAsync(
          MethodCallOptions options, @Nullable Variant generateOptions);

  /**
   * Resolves the mandatory GenerateFileForWrite Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4">Model
   *     documentation</a>
   */
  UaMethodNode getGenerateFileForWriteMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGenerateFileForWriteMethodNode()}. */
  CompletableFuture<UaMethodNode> getGenerateFileForWriteMethodNodeAsync();

  /**
   * Calls the GenerateFileForWrite Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4">Model
   *     documentation</a>
   */
  TemporaryFileTransferTypeGenerateFileForWrite.Outputs generateFileForWrite(
      @Nullable Variant generateOptions) throws UaException;

  /**
   * Calls the GenerateFileForWrite Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs> callGenerateFileForWrite(
      @Nullable Variant generateOptions) throws UaException;

  /**
   * Calls the GenerateFileForWrite Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>
      callGenerateFileForWriteWith(MethodCallOptions options, @Nullable Variant generateOptions)
          throws UaException;

  /** Asynchronous form of {@link #generateFileForWrite}. */
  CompletableFuture<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>
      generateFileForWriteAsync(@Nullable Variant generateOptions);

  /** Asynchronous form of {@link #callGenerateFileForWrite}. */
  CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>>
      callGenerateFileForWriteAsync(@Nullable Variant generateOptions);

  /** Asynchronous form of {@link #callGenerateFileForWriteWith}. */
  CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>>
      callGenerateFileForWriteWithAsync(
          MethodCallOptions options, @Nullable Variant generateOptions);
}
