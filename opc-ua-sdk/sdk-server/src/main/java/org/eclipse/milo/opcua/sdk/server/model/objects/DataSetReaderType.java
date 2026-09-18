package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DataSetReaderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">Model
 *     documentation</a>
 */
public interface DataSetReaderType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15306L);

  /**
   * Returns the mandatory DataSetFieldContentMask child, a PropertyType with DataType
   * DataSetFieldContentMask.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetFieldContentMaskNode();

  /**
   * Returns the Value of the DataSetFieldContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DataSetFieldContentMask getDataSetFieldContentMask();

  /**
   * Sets the Value of the DataSetFieldContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value);

  /**
   * Returns the mandatory DataSetMetaData child, a PropertyType with DataType DataSetMetaDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetMetaDataNode();

  /**
   * Returns the Value of the DataSetMetaData child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DataSetMetaDataType getDataSetMetaData();

  /**
   * Sets the Value of the DataSetMetaData child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetMetaData(@Nullable DataSetMetaDataType value);

  /**
   * Returns the mandatory DataSetReaderProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetReaderPropertiesNode();

  /**
   * Returns the Value of the DataSetReaderProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable KeyValuePair @Nullable [] getDataSetReaderProperties();

  /**
   * Sets the Value of the DataSetReaderProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the mandatory DataSetWriterId child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetWriterIdNode();

  /**
   * Returns the Value of the DataSetWriterId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getDataSetWriterId();

  /**
   * Sets the Value of the DataSetWriterId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetWriterId(@Nullable UShort value);

  /**
   * Returns the optional Diagnostics child, a PubSubDiagnosticsDataSetReaderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.12">PubSubDiagnosticsDataSetReaderType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsDataSetReaderTypeNode getDiagnosticsNode();

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
   * Returns the mandatory KeyFrameCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getKeyFrameCountNode();

  /**
   * Returns the Value of the KeyFrameCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getKeyFrameCount();

  /**
   * Sets the Value of the KeyFrameCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setKeyFrameCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MessageReceiveTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMessageReceiveTimeoutNode();

  /**
   * Returns the Value of the MessageReceiveTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMessageReceiveTimeout();

  /**
   * Sets the Value of the MessageReceiveTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMessageReceiveTimeout(@Nullable Double value);

  /**
   * Returns the optional MessageSettings child, a DataSetReaderMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.4">DataSetReaderMessageType
   *     documentation</a>
   */
  @Nullable DataSetReaderMessageTypeNode getMessageSettingsNode();

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
   * Returns the optional SecurityGroupId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSecurityGroupIdNode();

  /**
   * Returns the Value of the SecurityGroupId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityGroupId();

  /**
   * Sets the Value of the SecurityGroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityGroupId(@Nullable String value);

  /**
   * Returns the optional SecurityKeyServices child, a PropertyType with DataType
   * EndpointDescription.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSecurityKeyServicesNode();

  /**
   * Returns the Value of the SecurityKeyServices child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EndpointDescription @Nullable [] getSecurityKeyServices();

  /**
   * Sets the Value of the SecurityKeyServices child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the optional SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSecurityModeNode();

  /**
   * Returns the Value of the SecurityMode child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable MessageSecurityMode getSecurityMode();

  /**
   * Sets the Value of the SecurityMode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityMode(@Nullable MessageSecurityMode value);

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
   * Returns the mandatory SubscribedDataSet child, a SubscribedDataSetType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.1">SubscribedDataSetType
   *     documentation</a>
   */
  SubscribedDataSetTypeNode getSubscribedDataSetNode();

  /**
   * Returns the optional TransportSettings child, a DataSetReaderTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.3">DataSetReaderTransportType
   *     documentation</a>
   */
  @Nullable DataSetReaderTransportTypeNode getTransportSettingsNode();

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
   * Returns the optional CreateDataSetMirror Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateDataSetMirrorMethodNode();

  /**
   * Sets this instance's CreateDataSetMirror handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCreateDataSetMirrorHandler(@Nullable CreateDataSetMirrorHandler handler);

  /**
   * Returns the optional CreateTargetVariables Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateTargetVariablesMethodNode();

  /**
   * Sets this instance's CreateTargetVariables handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setCreateTargetVariablesHandler(@Nullable CreateTargetVariablesHandler handler);

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
   * Handles calls to the CreateDataSetMirror Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CreateDataSetMirrorHandler {
    /**
     * Handles a call to the CreateDataSetMirror Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId createDataSetMirror(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String parentNodeName,
        @Nullable RolePermissionType @Nullable [] rolePermissions)
        throws UaException;
  }

  /**
   * Handles calls to the CreateTargetVariables Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface CreateTargetVariablesHandler {
    /**
     * Handles a call to the CreateTargetVariables Method.
     *
     * @throws UaException if the call fails.
     */
    StatusCode @Nullable [] createTargetVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the CreateDataSetMirror Method; see {@link
     * CreateDataSetMirrorHandler#createDataSetMirror}.
     */
    default @Nullable NodeId createDataSetMirror(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String parentNodeName,
        @Nullable RolePermissionType @Nullable [] rolePermissions)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the CreateTargetVariables Method; see {@link
     * CreateTargetVariablesHandler#createTargetVariables}.
     */
    default StatusCode @Nullable [] createTargetVariables(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
