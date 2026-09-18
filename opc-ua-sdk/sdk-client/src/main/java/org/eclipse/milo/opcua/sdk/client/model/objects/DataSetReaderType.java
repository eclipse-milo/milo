package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
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
 * Client API for the DataSetReaderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">Model
 *     documentation</a>
 */
public interface DataSetReaderType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15306L);

  QualifiedProperty<Variant> PublisherId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PublisherId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          -1,
          Variant.class);

  QualifiedProperty<MessageSecurityMode> SecurityMode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityMode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 302L),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<UInteger> KeyFrameCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "KeyFrameCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> WriterGroupId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "WriterGroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<DataSetMetaDataType> DataSetMetaData_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetMetaData",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14523L),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<UShort> DataSetWriterId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetWriterId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<String> HeaderLayoutUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HeaderLayoutUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> SecurityGroupId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityGroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<EndpointDescription[]> SecurityKeyServices_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityKeyServices",
          ExpandedNodeId.of(Namespaces.OPC_UA, 312L),
          1,
          EndpointDescription[].class);

  QualifiedProperty<Double> MessageReceiveTimeout_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MessageReceiveTimeout",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<DataSetFieldContentMask> DataSetFieldContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetFieldContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15583L),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<KeyValuePair[]> DataSetReaderProperties_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetReaderProperties",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
          1,
          KeyValuePair[].class);

  /**
   * Resolves the optional Diagnostics child, a PubSubDiagnosticsDataSetReaderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.12">PubSubDiagnosticsDataSetReaderType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsDataSetReaderType getDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsNode()}. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetReaderType>
      getDiagnosticsNodeAsync();

  /**
   * Resolves the mandatory PublisherId child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPublisherIdNode() throws UaException;

  /** Asynchronous form of {@link #getPublisherIdNode()}. */
  CompletableFuture<? extends PropertyType> getPublisherIdNodeAsync();

  /**
   * Reads the Value of the PublisherId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readPublisherId() throws UaException;

  /**
   * Writes the Value of the PublisherId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublisherId(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readPublisherId()}. */
  CompletableFuture<? extends @Nullable Variant> readPublisherIdAsync();

  /** Asynchronous form of {@link #writePublisherId}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublisherIdAsync(@Nullable Variant value);

  /**
   * Resolves the optional SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSecurityModeNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityModeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityModeNodeAsync();

  /**
   * Reads the Value of the SecurityMode child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /**
   * Writes the Value of the SecurityMode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Asynchronous form of {@link #readSecurityMode()}. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Asynchronous form of {@link #writeSecurityMode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Resolves the mandatory KeyFrameCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getKeyFrameCountNode() throws UaException;

  /** Asynchronous form of {@link #getKeyFrameCountNode()}. */
  CompletableFuture<? extends PropertyType> getKeyFrameCountNodeAsync();

  /**
   * Reads the Value of the KeyFrameCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readKeyFrameCount() throws UaException;

  /**
   * Writes the Value of the KeyFrameCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeKeyFrameCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readKeyFrameCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readKeyFrameCountAsync();

  /** Asynchronous form of {@link #writeKeyFrameCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeKeyFrameCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory WriterGroupId child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getWriterGroupIdNode() throws UaException;

  /** Asynchronous form of {@link #getWriterGroupIdNode()}. */
  CompletableFuture<? extends PropertyType> getWriterGroupIdNodeAsync();

  /**
   * Reads the Value of the WriterGroupId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readWriterGroupId() throws UaException;

  /**
   * Writes the Value of the WriterGroupId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeWriterGroupId(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readWriterGroupId()}. */
  CompletableFuture<? extends @Nullable UShort> readWriterGroupIdAsync();

  /** Asynchronous form of {@link #writeWriterGroupId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeWriterGroupIdAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory DataSetMetaData child, a PropertyType with DataType DataSetMetaDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetMetaDataNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetMetaDataNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetMetaDataNodeAsync();

  /**
   * Reads the Value of the DataSetMetaData child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException;

  /**
   * Writes the Value of the DataSetMetaData child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /** Asynchronous form of {@link #readDataSetMetaData()}. */
  CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync();

  /** Asynchronous form of {@link #writeDataSetMetaData}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetMetaDataAsync(@Nullable DataSetMetaDataType value);

  /**
   * Resolves the mandatory DataSetWriterId child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetWriterIdNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetWriterIdNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetWriterIdNodeAsync();

  /**
   * Reads the Value of the DataSetWriterId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readDataSetWriterId() throws UaException;

  /**
   * Writes the Value of the DataSetWriterId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetWriterId(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readDataSetWriterId()}. */
  CompletableFuture<? extends @Nullable UShort> readDataSetWriterIdAsync();

  /** Asynchronous form of {@link #writeDataSetWriterId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetWriterIdAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory HeaderLayoutUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getHeaderLayoutUriNode() throws UaException;

  /** Asynchronous form of {@link #getHeaderLayoutUriNode()}. */
  CompletableFuture<? extends PropertyType> getHeaderLayoutUriNodeAsync();

  /**
   * Reads the Value of the HeaderLayoutUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readHeaderLayoutUri() throws UaException;

  /**
   * Writes the Value of the HeaderLayoutUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHeaderLayoutUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readHeaderLayoutUri()}. */
  CompletableFuture<? extends @Nullable String> readHeaderLayoutUriAsync();

  /** Asynchronous form of {@link #writeHeaderLayoutUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(@Nullable String value);

  /**
   * Resolves the optional MessageSettings child, a DataSetReaderMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.4">DataSetReaderMessageType
   *     documentation</a>
   */
  @Nullable DataSetReaderMessageType getMessageSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getMessageSettingsNode()}. */
  CompletableFuture<? extends @Nullable DataSetReaderMessageType> getMessageSettingsNodeAsync();

  /**
   * Resolves the optional SecurityGroupId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSecurityGroupIdNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityGroupIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityGroupIdNodeAsync();

  /**
   * Reads the Value of the SecurityGroupId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecurityGroupId() throws UaException;

  /**
   * Writes the Value of the SecurityGroupId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityGroupId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecurityGroupId()}. */
  CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync();

  /** Asynchronous form of {@link #writeSecurityGroupId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value);

  /**
   * Resolves the mandatory SubscribedDataSet child, a SubscribedDataSetType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.1">SubscribedDataSetType
   *     documentation</a>
   */
  SubscribedDataSetType getSubscribedDataSetNode() throws UaException;

  /** Asynchronous form of {@link #getSubscribedDataSetNode()}. */
  CompletableFuture<? extends SubscribedDataSetType> getSubscribedDataSetNodeAsync();

  /**
   * Resolves the optional TransportSettings child, a DataSetReaderTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.3">DataSetReaderTransportType
   *     documentation</a>
   */
  @Nullable DataSetReaderTransportType getTransportSettingsNode() throws UaException;

  /** Asynchronous form of {@link #getTransportSettingsNode()}. */
  CompletableFuture<? extends @Nullable DataSetReaderTransportType> getTransportSettingsNodeAsync();

  /**
   * Resolves the optional SecurityKeyServices child, a PropertyType with DataType
   * EndpointDescription.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSecurityKeyServicesNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityKeyServicesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityKeyServicesNodeAsync();

  /**
   * Reads the Value of the SecurityKeyServices child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EndpointDescription @Nullable [] readSecurityKeyServices() throws UaException;

  /**
   * Writes the Value of the SecurityKeyServices child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readSecurityKeyServices()}. */
  CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readSecurityKeyServicesAsync();

  /**
   * Asynchronous form of {@link #writeSecurityKeyServices}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value);

  /**
   * Resolves the mandatory MessageReceiveTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMessageReceiveTimeoutNode() throws UaException;

  /** Asynchronous form of {@link #getMessageReceiveTimeoutNode()}. */
  CompletableFuture<? extends PropertyType> getMessageReceiveTimeoutNodeAsync();

  /**
   * Reads the Value of the MessageReceiveTimeout child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMessageReceiveTimeout() throws UaException;

  /**
   * Writes the Value of the MessageReceiveTimeout child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMessageReceiveTimeout(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMessageReceiveTimeout()}. */
  CompletableFuture<? extends @Nullable Double> readMessageReceiveTimeoutAsync();

  /**
   * Asynchronous form of {@link #writeMessageReceiveTimeout}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMessageReceiveTimeoutAsync(@Nullable Double value);

  /**
   * Resolves the mandatory DataSetFieldContentMask child, a PropertyType with DataType
   * DataSetFieldContentMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetFieldContentMaskNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetFieldContentMaskNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetFieldContentMaskNodeAsync();

  /**
   * Reads the Value of the DataSetFieldContentMask child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DataSetFieldContentMask readDataSetFieldContentMask() throws UaException;

  /**
   * Writes the Value of the DataSetFieldContentMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) throws UaException;

  /** Asynchronous form of {@link #readDataSetFieldContentMask()}. */
  CompletableFuture<? extends @Nullable DataSetFieldContentMask> readDataSetFieldContentMaskAsync();

  /**
   * Asynchronous form of {@link #writeDataSetFieldContentMask}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      @Nullable DataSetFieldContentMask value);

  /**
   * Resolves the mandatory DataSetReaderProperties child, a PropertyType with DataType
   * KeyValuePair.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetReaderPropertiesNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetReaderPropertiesNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetReaderPropertiesNodeAsync();

  /**
   * Reads the Value of the DataSetReaderProperties child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable KeyValuePair @Nullable [] readDataSetReaderProperties() throws UaException;

  /**
   * Writes the Value of the DataSetReaderProperties child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readDataSetReaderProperties()}. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readDataSetReaderPropertiesAsync();

  /**
   * Asynchronous form of {@link #writeDataSetReaderProperties}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDataSetReaderPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Resolves the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusType getStatusNode() throws UaException;

  /** Asynchronous form of {@link #getStatusNode()}. */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Resolves the optional CreateDataSetMirror Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateDataSetMirrorMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCreateDataSetMirrorMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getCreateDataSetMirrorMethodNodeAsync();

  /**
   * Calls the CreateDataSetMirror Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6">Model
   *     documentation</a>
   */
  @Nullable NodeId createDataSetMirror(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException;

  /**
   * Calls the CreateDataSetMirror Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCreateDataSetMirror(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException;

  /**
   * Calls the CreateDataSetMirror Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCreateDataSetMirrorWith(
      MethodCallOptions options,
      @Nullable String parentNodeName,
      @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException;

  /** Asynchronous form of {@link #createDataSetMirror}. */
  CompletableFuture<@Nullable NodeId> createDataSetMirrorAsync(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions);

  /** Asynchronous form of {@link #callCreateDataSetMirror}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDataSetMirrorAsync(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions);

  /** Asynchronous form of {@link #callCreateDataSetMirrorWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDataSetMirrorWithAsync(
      MethodCallOptions options,
      @Nullable String parentNodeName,
      @Nullable RolePermissionType @Nullable [] rolePermissions);

  /**
   * Resolves the optional CreateTargetVariables Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateTargetVariablesMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCreateTargetVariablesMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getCreateTargetVariablesMethodNodeAsync();

  /**
   * Calls the CreateTargetVariables Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5">Model
   *     documentation</a>
   */
  StatusCode @Nullable [] createTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * Calls the CreateTargetVariables Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callCreateTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * Calls the CreateTargetVariables Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callCreateTargetVariablesWith(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /** Asynchronous form of {@link #createTargetVariables}. */
  CompletableFuture<StatusCode @Nullable []> createTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /** Asynchronous form of {@link #callCreateTargetVariables}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callCreateTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /** Asynchronous form of {@link #callCreateTargetVariablesWith}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callCreateTargetVariablesWithAsync(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);
}
