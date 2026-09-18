package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeCreateFile;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the FileDirectoryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1">Model
 *     documentation</a>
 */
public interface FileDirectoryType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 13353L);

  /**
   * Resolves the mandatory CreateDirectory Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3">Model
   *     documentation</a>
   */
  UaMethodNode getCreateDirectoryMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCreateDirectoryMethodNode()}. */
  CompletableFuture<UaMethodNode> getCreateDirectoryMethodNodeAsync();

  /**
   * Calls the CreateDirectory Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3">Model
   *     documentation</a>
   */
  @Nullable NodeId createDirectory(@Nullable String directoryName) throws UaException;

  /**
   * Calls the CreateDirectory Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCreateDirectory(@Nullable String directoryName)
      throws UaException;

  /**
   * Calls the CreateDirectory Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCreateDirectoryWith(
      MethodCallOptions options, @Nullable String directoryName) throws UaException;

  /** Asynchronous form of {@link #createDirectory}. */
  CompletableFuture<@Nullable NodeId> createDirectoryAsync(@Nullable String directoryName);

  /** Asynchronous form of {@link #callCreateDirectory}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDirectoryAsync(
      @Nullable String directoryName);

  /** Asynchronous form of {@link #callCreateDirectoryWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDirectoryWithAsync(
      MethodCallOptions options, @Nullable String directoryName);

  /**
   * Resolves the mandatory CreateFile Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4">Model
   *     documentation</a>
   */
  UaMethodNode getCreateFileMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCreateFileMethodNode()}. */
  CompletableFuture<UaMethodNode> getCreateFileMethodNodeAsync();

  /**
   * Calls the CreateFile Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4">Model
   *     documentation</a>
   */
  FileDirectoryTypeCreateFile.Outputs createFile(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException;

  /**
   * Calls the CreateFile Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<FileDirectoryTypeCreateFile.Outputs> callCreateFile(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException;

  /**
   * Calls the CreateFile Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<FileDirectoryTypeCreateFile.Outputs> callCreateFileWith(
      MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen)
      throws UaException;

  /** Asynchronous form of {@link #createFile}. */
  CompletableFuture<FileDirectoryTypeCreateFile.Outputs> createFileAsync(
      @Nullable String fileName, @Nullable Boolean requestFileOpen);

  /** Asynchronous form of {@link #callCreateFile}. */
  CompletableFuture<MethodCallResult<FileDirectoryTypeCreateFile.Outputs>> callCreateFileAsync(
      @Nullable String fileName, @Nullable Boolean requestFileOpen);

  /** Asynchronous form of {@link #callCreateFileWith}. */
  CompletableFuture<MethodCallResult<FileDirectoryTypeCreateFile.Outputs>> callCreateFileWithAsync(
      MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen);

  /**
   * Resolves the mandatory Delete Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5">Model
   *     documentation</a>
   */
  UaMethodNode getDelete_MethodNode() throws UaException;

  /** Asynchronous form of {@link #getDelete_MethodNode()}. */
  CompletableFuture<UaMethodNode> getDelete_MethodNodeAsync();

  /**
   * Calls the Delete Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5">Model
   *     documentation</a>
   */
  void delete_(@Nullable NodeId objectToDelete) throws UaException;

  /**
   * Calls the Delete Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDelete_(@Nullable NodeId objectToDelete) throws UaException;

  /**
   * Calls the Delete Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDelete_With(MethodCallOptions options, @Nullable NodeId objectToDelete)
      throws UaException;

  /** Asynchronous form of {@link #delete_}. */
  CompletableFuture<Void> delete_Async(@Nullable NodeId objectToDelete);

  /** Asynchronous form of {@link #callDelete_}. */
  CompletableFuture<MethodCallResult<Void>> callDelete_Async(@Nullable NodeId objectToDelete);

  /** Asynchronous form of {@link #callDelete_With}. */
  CompletableFuture<MethodCallResult<Void>> callDelete_WithAsync(
      MethodCallOptions options, @Nullable NodeId objectToDelete);

  /**
   * Resolves the mandatory MoveOrCopy Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6">Model
   *     documentation</a>
   */
  UaMethodNode getMoveOrCopyMethodNode() throws UaException;

  /** Asynchronous form of {@link #getMoveOrCopyMethodNode()}. */
  CompletableFuture<UaMethodNode> getMoveOrCopyMethodNodeAsync();

  /**
   * Calls the MoveOrCopy Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6">Model
   *     documentation</a>
   */
  @Nullable NodeId moveOrCopy(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException;

  /**
   * Calls the MoveOrCopy Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callMoveOrCopy(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException;

  /**
   * Calls the MoveOrCopy Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callMoveOrCopyWith(
      MethodCallOptions options,
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException;

  /** Asynchronous form of {@link #moveOrCopy}. */
  CompletableFuture<@Nullable NodeId> moveOrCopyAsync(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName);

  /** Asynchronous form of {@link #callMoveOrCopy}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callMoveOrCopyAsync(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName);

  /** Asynchronous form of {@link #callMoveOrCopyWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callMoveOrCopyWithAsync(
      MethodCallOptions options,
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName);
}
