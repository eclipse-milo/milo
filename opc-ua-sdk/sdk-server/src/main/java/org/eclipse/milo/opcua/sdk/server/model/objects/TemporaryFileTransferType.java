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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForReadOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForWriteOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TemporaryFileTransferType extends BaseObjectType {
  QualifiedProperty<Double> CLIENT_PROCESSING_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientProcessingTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable Double getClientProcessingTimeout();

  /** Sets the existing node's local value. */
  void setClientProcessingTimeout(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientProcessingTimeoutNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getGenerateFileForReadMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGenerateFileForRead(MethodBindings bindings, GenerateFileForReadHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGenerateFileForReadDetailed(
      MethodBindings bindings, GenerateFileForReadDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getGenerateFileForWriteMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGenerateFileForWrite(
      MethodBindings bindings, GenerateFileForWriteHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGenerateFileForWriteDetailed(
      MethodBindings bindings, GenerateFileForWriteDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCloseAndCommitMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndCommit(MethodBindings bindings, CloseAndCommitHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndCommitDetailed(
      MethodBindings bindings, CloseAndCommitDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3 */
  @FunctionalInterface
  interface GenerateFileForReadHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    TemporaryFileTransferTypeGenerateFileForReadOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Object generateOptions)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3 */
  @FunctionalInterface
  interface GenerateFileForReadDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<TemporaryFileTransferTypeGenerateFileForReadOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Object generateOptions)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4 */
  @FunctionalInterface
  interface GenerateFileForWriteHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    TemporaryFileTransferTypeGenerateFileForWriteOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Object generateOptions)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4 */
  @FunctionalInterface
  interface GenerateFileForWriteDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<TemporaryFileTransferTypeGenerateFileForWriteOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Object generateOptions)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5 */
  @FunctionalInterface
  interface CloseAndCommitHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5 */
  @FunctionalInterface
  interface CloseAndCommitDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle)
        throws UaException;
  }
}
