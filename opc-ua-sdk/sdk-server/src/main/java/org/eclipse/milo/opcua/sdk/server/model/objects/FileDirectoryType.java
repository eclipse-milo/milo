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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeCreateFileOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FileDirectoryType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCreateDirectoryMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateDirectory(MethodBindings bindings, CreateDirectoryHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateDirectoryDetailed(
      MethodBindings bindings, CreateDirectoryDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCreateFileMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateFile(MethodBindings bindings, CreateFileHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateFileDetailed(MethodBindings bindings, CreateFileDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getDeleteFileSystemObjectMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteFileSystemObject(
      MethodBindings bindings, DeleteFileSystemObjectHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeleteFileSystemObjectDetailed(
      MethodBindings bindings, DeleteFileSystemObjectDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getMoveOrCopyMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindMoveOrCopy(MethodBindings bindings, MoveOrCopyHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindMoveOrCopyDetailed(MethodBindings bindings, MoveOrCopyDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3 */
  @FunctionalInterface
  interface CreateDirectoryHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String directoryName)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.3 */
  @FunctionalInterface
  interface CreateDirectoryDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String directoryName)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4 */
  @FunctionalInterface
  interface CreateFileHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    FileDirectoryTypeCreateFileOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String fileName,
        @Nullable Boolean requestFileOpen)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.4 */
  @FunctionalInterface
  interface CreateFileDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<FileDirectoryTypeCreateFileOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String fileName,
        @Nullable Boolean requestFileOpen)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5 */
  @FunctionalInterface
  interface DeleteFileSystemObjectHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId objectToDelete)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.5 */
  @FunctionalInterface
  interface DeleteFileSystemObjectDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId objectToDelete)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6 */
  @FunctionalInterface
  interface MoveOrCopyHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId objectToMoveOrCopy,
        @Nullable NodeId targetDirectory,
        @Nullable Boolean createCopy,
        @Nullable String newName)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.6 */
  @FunctionalInterface
  interface MoveOrCopyDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId objectToMoveOrCopy,
        @Nullable NodeId targetDirectory,
        @Nullable Boolean createCopy,
        @Nullable String newName)
        throws UaException;
  }
}
