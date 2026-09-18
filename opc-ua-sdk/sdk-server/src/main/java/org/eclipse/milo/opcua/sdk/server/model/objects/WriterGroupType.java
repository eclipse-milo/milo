package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the WriterGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">Model
 *     documentation</a>
 */
public interface WriterGroupType extends PubSubGroupType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17725L);

  /**
   * Returns the optional Diagnostics child, a PubSubDiagnosticsWriterGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.9">PubSubDiagnosticsWriterGroupType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsWriterGroupTypeNode getDiagnosticsNode();

  /**
   * Returns the mandatory HeaderLayoutUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getHeaderLayoutUriNode();

  /**
   * Returns the Value of the HeaderLayoutUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getHeaderLayoutUri();

  /**
   * Sets the Value of the HeaderLayoutUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHeaderLayoutUri(@Nullable String value);

  /**
   * Returns the mandatory KeepAliveTime child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getKeepAliveTimeNode();

  /**
   * Returns the Value of the KeepAliveTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getKeepAliveTime();

  /**
   * Sets the Value of the KeepAliveTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setKeepAliveTime(@Nullable Double value);

  /**
   * Returns the mandatory LocaleIds child, a PropertyType with DataType LocaleId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLocaleIdsNode();

  /**
   * Returns the Value of the LocaleIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getLocaleIds();

  /**
   * Sets the Value of the LocaleIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLocaleIds(@Nullable String @Nullable [] value);

  /**
   * Returns the optional MessageSettings child, a WriterGroupMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.8">WriterGroupMessageType
   *     documentation</a>
   */
  @Nullable WriterGroupMessageTypeNode getMessageSettingsNode();

  /**
   * Returns the mandatory Priority child, a PropertyType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPriorityNode();

  /**
   * Returns the Value of the Priority child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getPriority();

  /**
   * Sets the Value of the Priority child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriority(@Nullable UByte value);

  /**
   * Returns the mandatory PublishingInterval child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPublishingIntervalNode();

  /**
   * Returns the Value of the PublishingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getPublishingInterval();

  /**
   * Sets the Value of the PublishingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishingInterval(@Nullable Double value);

  /**
   * Returns the optional TransportSettings child, a WriterGroupTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.7">WriterGroupTransportType
   *     documentation</a>
   */
  @Nullable WriterGroupTransportTypeNode getTransportSettingsNode();

  /**
   * Returns the mandatory WriterGroupId child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getWriterGroupIdNode();

  /**
   * Returns the Value of the WriterGroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getWriterGroupId();

  /**
   * Sets the Value of the WriterGroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setWriterGroupId(@Nullable UShort value);

  /**
   * Returns the optional AddDataSetWriter Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddDataSetWriterMethodNode();

  /**
   * Sets this instance's AddDataSetWriter handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddDataSetWriterHandler(@Nullable AddDataSetWriterHandler handler);

  /**
   * Returns the optional RemoveDataSetWriter Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveDataSetWriterMethodNode();

  /**
   * Sets this instance's RemoveDataSetWriter handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveDataSetWriterHandler(@Nullable RemoveDataSetWriterHandler handler);

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
   * Handles calls to the AddDataSetWriter Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddDataSetWriterHandler {
    /**
     * Handles a call to the AddDataSetWriter Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addDataSetWriter(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable DataSetWriterDataType configuration)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveDataSetWriter Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveDataSetWriterHandler {
    /**
     * Handles a call to the RemoveDataSetWriter Method.
     *
     * @throws UaException if the call fails.
     */
    void removeDataSetWriter(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetWriterNodeId)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the AddDataSetWriter Method; see {@link
     * AddDataSetWriterHandler#addDataSetWriter}.
     */
    default @Nullable NodeId addDataSetWriter(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable DataSetWriterDataType configuration)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveDataSetWriter Method; see {@link
     * RemoveDataSetWriterHandler#removeDataSetWriter}.
     */
    default void removeDataSetWriter(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable NodeId dataSetWriterNodeId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
