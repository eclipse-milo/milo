package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubConnectionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.2">Model
 *     documentation</a>
 */
public interface PubSubConnectionType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14209L);

  /**
   * Returns the mandatory Address child, a NetworkAddressType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">NetworkAddressType
   *     documentation</a>
   */
  NetworkAddressTypeNode getAddressNode();

  /**
   * Returns the mandatory ConnectionProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConnectionPropertiesNode();

  /**
   * Returns the Value of the ConnectionProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable KeyValuePair @Nullable [] getConnectionProperties();

  /**
   * Sets the Value of the ConnectionProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConnectionProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the optional Diagnostics child, a PubSubDiagnosticsConnectionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.8">PubSubDiagnosticsConnectionType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsConnectionTypeNode getDiagnosticsNode();

  /**
   * Returns the mandatory PublisherId child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPublisherIdNode();

  /**
   * Returns the Value of the PublisherId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getPublisherId();

  /**
   * Sets the Value of the PublisherId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublisherId(@Nullable Variant value);

  /**
   * Returns the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusTypeNode getStatusNode();

  /**
   * Returns the mandatory TransportProfileUri child, a SelectionListType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">SelectionListType
   *     documentation</a>
   */
  SelectionListTypeNode getTransportProfileUriNode();

  /**
   * Returns the Value of the TransportProfileUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getTransportProfileUri();

  /**
   * Sets the Value of the TransportProfileUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransportProfileUri(@Nullable String value);

  /**
   * Returns the optional TransportSettings child, a ConnectionTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.8">ConnectionTransportType
   *     documentation</a>
   */
  @Nullable ConnectionTransportTypeNode getTransportSettingsNode();

  /**
   * Returns the optional AddReaderGroup Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddReaderGroupMethodNode();

  /**
   * Sets this instance's AddReaderGroup handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddReaderGroupHandler(@Nullable AddReaderGroupHandler handler);

  /**
   * Returns the optional AddWriterGroup Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddWriterGroupMethodNode();

  /**
   * Sets this instance's AddWriterGroup handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddWriterGroupHandler(@Nullable AddWriterGroupHandler handler);

  /**
   * Returns the optional RemoveGroup Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveGroupMethodNode();

  /**
   * Sets this instance's RemoveGroup handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveGroupHandler(@Nullable RemoveGroupHandler handler);

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
   * Handles calls to the AddReaderGroup Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddReaderGroupHandler {
    /**
     * Handles a call to the AddReaderGroup Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addReaderGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ReaderGroupDataType configuration)
        throws UaException;
  }

  /**
   * Handles calls to the AddWriterGroup Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddWriterGroupHandler {
    /**
     * Handles a call to the AddWriterGroup Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addWriterGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable WriterGroupDataType configuration)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveGroup Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveGroupHandler {
    /**
     * Handles a call to the RemoveGroup Method.
     *
     * @throws UaException if the call fails.
     */
    void removeGroup(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId groupId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddReaderGroup Method; see {@link
     * AddReaderGroupHandler#addReaderGroup}.
     */
    default @Nullable NodeId addReaderGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ReaderGroupDataType configuration)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the AddWriterGroup Method; see {@link
     * AddWriterGroupHandler#addWriterGroup}.
     */
    default @Nullable NodeId addWriterGroup(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable WriterGroupDataType configuration)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the RemoveGroup Method; see {@link RemoveGroupHandler#removeGroup}. */
    default void removeGroup(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId groupId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
