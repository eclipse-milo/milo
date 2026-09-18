package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ReaderGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9">Model
 *     documentation</a>
 */
public interface ReaderGroupType extends PubSubGroupType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17999L);

  /**
   * Returns the optional Diagnostics child, a PubSubDiagnosticsReaderGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.10">PubSubDiagnosticsReaderGroupType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsReaderGroupTypeNode getDiagnosticsNode();

  /**
   * Returns the optional MessageSettings child, a ReaderGroupMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.14">ReaderGroupMessageType
   *     documentation</a>
   */
  @Nullable ReaderGroupMessageTypeNode getMessageSettingsNode();

  /**
   * Returns the optional TransportSettings child, a ReaderGroupTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.13">ReaderGroupTransportType
   *     documentation</a>
   */
  @Nullable ReaderGroupTransportTypeNode getTransportSettingsNode();

  /**
   * Returns the optional AddDataSetReader Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetReaderMethodNode();

  /**
   * Sets this instance's AddDataSetReader handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddDataSetReaderHandler(@Nullable AddDataSetReaderHandler handler);

  /**
   * Returns the optional RemoveDataSetReader Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetReaderMethodNode();

  /**
   * Sets this instance's RemoveDataSetReader handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveDataSetReaderHandler(@Nullable RemoveDataSetReaderHandler handler);

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
   * Handles calls to the AddDataSetReader Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.10">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddDataSetReaderHandler {
    /**
     * Handles a call to the AddDataSetReader Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addDataSetReader(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable DataSetReaderDataType configuration)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveDataSetReader Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.11">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveDataSetReaderHandler {
    /**
     * Handles a call to the RemoveDataSetReader Method.
     *
     * @throws UaException if the call fails.
     */
    void removeDataSetReader(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetReaderNodeId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddDataSetReader Method; see {@link
     * AddDataSetReaderHandler#addDataSetReader}.
     */
    default @Nullable NodeId addDataSetReader(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable DataSetReaderDataType configuration)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveDataSetReader Method; see {@link
     * RemoveDataSetReaderHandler#removeDataSetReader}.
     */
    default void removeDataSetReader(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetReaderNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
