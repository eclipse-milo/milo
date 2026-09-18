package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SubscribedDataSetFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1">Model
 *     documentation</a>
 */
public interface SubscribedDataSetFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23795L);

  /**
   * Returns the optional AddDataSetFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetFolderMethodNode();

  /**
   * Sets this instance's AddDataSetFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddDataSetFolderHandler(@Nullable AddDataSetFolderHandler handler);

  /**
   * Returns the optional AddSubscribedDataSet Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddSubscribedDataSetMethodNode();

  /**
   * Sets this instance's AddSubscribedDataSet handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddSubscribedDataSetHandler(@Nullable AddSubscribedDataSetHandler handler);

  /**
   * Returns the optional RemoveDataSetFolder Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetFolderMethodNode();

  /**
   * Sets this instance's RemoveDataSetFolder handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveDataSetFolderHandler(@Nullable RemoveDataSetFolderHandler handler);

  /**
   * Returns the optional RemoveSubscribedDataSet Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveSubscribedDataSetMethodNode();

  /**
   * Sets this instance's RemoveSubscribedDataSet handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveSubscribedDataSetHandler(@Nullable RemoveSubscribedDataSetHandler handler);

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
   * Handles calls to the AddDataSetFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddDataSetFolderHandler {
    /**
     * Handles a call to the AddDataSetFolder Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException;
  }

  /**
   * Handles calls to the AddSubscribedDataSet Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddSubscribedDataSetHandler {
    /**
     * Handles a call to the AddSubscribedDataSet Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addSubscribedDataSet(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveDataSetFolder Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveDataSetFolderHandler {
    /**
     * Handles a call to the RemoveDataSetFolder Method.
     *
     * @throws UaException if the call fails.
     */
    void removeDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetFolderNodeId)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveSubscribedDataSet Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveSubscribedDataSetHandler {
    /**
     * Handles a call to the RemoveSubscribedDataSet Method.
     *
     * @throws UaException if the call fails.
     */
    void removeSubscribedDataSet(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId subscribedDataSetNodeId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddDataSetFolder Method; see {@link
     * AddDataSetFolderHandler#addDataSetFolder}.
     */
    default @Nullable NodeId addDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String name)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddSubscribedDataSet Method; see {@link
     * AddSubscribedDataSetHandler#addSubscribedDataSet}.
     */
    default @Nullable NodeId addSubscribedDataSet(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveDataSetFolder Method; see {@link
     * RemoveDataSetFolderHandler#removeDataSetFolder}.
     */
    default void removeDataSetFolder(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetFolderNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveSubscribedDataSet Method; see {@link
     * RemoveSubscribedDataSetHandler#removeSubscribedDataSet}.
     */
    default void removeSubscribedDataSet(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId subscribedDataSetNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
