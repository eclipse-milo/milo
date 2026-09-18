package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeCreateFile;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the FileDirectoryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1">Model
 *     documentation</a>
 */
public interface FileDirectoryType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 13353L);

  /**
   * Returns the mandatory CreateDirectory Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3">Model
   *     documentation</a>
   */
  UaMethodNode getCreateDirectoryMethodNode();

  /**
   * Sets this instance's CreateDirectory handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCreateDirectoryHandler(@Nullable CreateDirectoryHandler handler);

  /**
   * Returns the mandatory CreateFile Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4">Model
   *     documentation</a>
   */
  UaMethodNode getCreateFileMethodNode();

  /**
   * Sets this instance's CreateFile handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCreateFileHandler(@Nullable CreateFileHandler handler);

  /**
   * Returns the mandatory Delete Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5">Model
   *     documentation</a>
   */
  UaMethodNode getDelete_MethodNode();

  /**
   * Sets this instance's Delete handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDelete_Handler(@Nullable Delete_Handler handler);

  /**
   * Returns the mandatory MoveOrCopy Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6">Model
   *     documentation</a>
   */
  UaMethodNode getMoveOrCopyMethodNode();

  /**
   * Sets this instance's MoveOrCopy handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setMoveOrCopyHandler(@Nullable MoveOrCopyHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the CreateDirectory Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CreateDirectoryHandler {
    /**
     * Handles a call to the CreateDirectory Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId createDirectory(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String directoryName)
        throws UaException;
  }

  /**
   * Handles calls to the CreateFile Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CreateFileHandler {
    /**
     * Handles a call to the CreateFile Method.
     *
     * @throws UaException if the call fails.
     */
    FileDirectoryTypeCreateFile.Outputs createFile(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String fileName,
        @Nullable Boolean requestFileOpen)
        throws UaException;
  }

  /**
   * Handles calls to the Delete Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface Delete_Handler {
    /**
     * Handles a call to the Delete Method.
     *
     * @throws UaException if the call fails.
     */
    void delete_(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId objectToDelete)
        throws UaException;
  }

  /**
   * Handles calls to the MoveOrCopy Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface MoveOrCopyHandler {
    /**
     * Handles a call to the MoveOrCopy Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId moveOrCopy(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId objectToMoveOrCopy,
        @Nullable NodeId targetDirectory,
        @Nullable Boolean createCopy,
        @Nullable String newName)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the CreateDirectory Method; see {@link
     * CreateDirectoryHandler#createDirectory}.
     */
    default @Nullable NodeId createDirectory(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String directoryName)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the CreateFile Method; see {@link CreateFileHandler#createFile}. */
    default FileDirectoryTypeCreateFile.Outputs createFile(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String fileName,
        @Nullable Boolean requestFileOpen)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Delete Method; see {@link Delete_Handler#delete_}. */
    default void delete_(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId objectToDelete)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the MoveOrCopy Method; see {@link MoveOrCopyHandler#moveOrCopy}. */
    default @Nullable NodeId moveOrCopy(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId objectToMoveOrCopy,
        @Nullable NodeId targetDirectory,
        @Nullable Boolean createCopy,
        @Nullable String newName)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
