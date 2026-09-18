package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForRead;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForWrite;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TemporaryFileTransferType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1">Model
 *     documentation</a>
 */
public interface TemporaryFileTransferType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15744L);

  /**
   * Returns the mandatory ClientProcessingTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getClientProcessingTimeoutNode();

  /**
   * Returns the Value of the ClientProcessingTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getClientProcessingTimeout();

  /**
   * Sets the Value of the ClientProcessingTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientProcessingTimeout(@Nullable Double value);

  /**
   * Returns the mandatory CloseAndCommit Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndCommitMethodNode();

  /**
   * Sets this instance's CloseAndCommit handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCloseAndCommitHandler(@Nullable CloseAndCommitHandler handler);

  /**
   * Returns the mandatory GenerateFileForRead Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3">Model
   *     documentation</a>
   */
  UaMethodNode getGenerateFileForReadMethodNode();

  /**
   * Sets this instance's GenerateFileForRead handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGenerateFileForReadHandler(@Nullable GenerateFileForReadHandler handler);

  /**
   * Returns the mandatory GenerateFileForWrite Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4">Model
   *     documentation</a>
   */
  UaMethodNode getGenerateFileForWriteMethodNode();

  /**
   * Sets this instance's GenerateFileForWrite handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGenerateFileForWriteHandler(@Nullable GenerateFileForWriteHandler handler);

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
   * Handles calls to the CloseAndCommit Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CloseAndCommitHandler {
    /**
     * Handles a call to the CloseAndCommit Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId closeAndCommit(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException;
  }

  /**
   * Handles calls to the GenerateFileForRead Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GenerateFileForReadHandler {
    /**
     * Handles a call to the GenerateFileForRead Method.
     *
     * @throws UaException if the call fails.
     */
    TemporaryFileTransferTypeGenerateFileForRead.Outputs generateFileForRead(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Variant generateOptions)
        throws UaException;
  }

  /**
   * Handles calls to the GenerateFileForWrite Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GenerateFileForWriteHandler {
    /**
     * Handles a call to the GenerateFileForWrite Method.
     *
     * @throws UaException if the call fails.
     */
    TemporaryFileTransferTypeGenerateFileForWrite.Outputs generateFileForWrite(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Variant generateOptions)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the CloseAndCommit Method; see {@link
     * CloseAndCommitHandler#closeAndCommit}.
     */
    default @Nullable NodeId closeAndCommit(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable UInteger fileHandle)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the GenerateFileForRead Method; see {@link
     * GenerateFileForReadHandler#generateFileForRead}.
     */
    default TemporaryFileTransferTypeGenerateFileForRead.Outputs generateFileForRead(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Variant generateOptions)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the GenerateFileForWrite Method; see {@link
     * GenerateFileForWriteHandler#generateFileForWrite}.
     */
    default TemporaryFileTransferTypeGenerateFileForWrite.Outputs generateFileForWrite(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable Variant generateOptions)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
