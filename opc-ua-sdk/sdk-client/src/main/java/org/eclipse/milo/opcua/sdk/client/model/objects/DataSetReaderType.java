/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DataSetReaderType extends BaseObjectType {
  QualifiedProperty<Object> PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> DATA_SET_WRITER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<DataSetFieldContentMask> DATA_SET_FIELD_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetFieldContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15583"),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<Double> MESSAGE_RECEIVE_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageReceiveTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UInteger> KEY_FRAME_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyFrameCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<EndpointDescription[]> SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<KeyValuePair[]> DATA_SET_READER_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetReaderProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  /** Gets the existing node's local value. */
  @Nullable Object getPublisherId() throws UaException;

  /** Sets the existing node's local value. */
  void setPublisherId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readPublisherId() throws UaException;

  /** Writes the value remotely. */
  void writePublisherId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readPublisherIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublisherIdAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublisherIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPublisherIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getWriterGroupId() throws UaException;

  /** Sets the existing node's local value. */
  void setWriterGroupId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readWriterGroupId() throws UaException;

  /** Writes the value remotely. */
  void writeWriterGroupId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readWriterGroupIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeWriterGroupIdAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getWriterGroupIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getWriterGroupIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getDataSetWriterId() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetWriterId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readDataSetWriterId() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetWriterId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readDataSetWriterIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetWriterIdAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetWriterIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetWriterIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DataSetMetaDataType getDataSetMetaData() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetMetaDataAsync(@Nullable DataSetMetaDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetMetaDataNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetMetaDataNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DataSetFieldContentMask getDataSetFieldContentMask() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DataSetFieldContentMask readDataSetFieldContentMask() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DataSetFieldContentMask> readDataSetFieldContentMaskAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      @Nullable DataSetFieldContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetFieldContentMaskNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetFieldContentMaskNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMessageReceiveTimeout() throws UaException;

  /** Sets the existing node's local value. */
  void setMessageReceiveTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMessageReceiveTimeout() throws UaException;

  /** Writes the value remotely. */
  void writeMessageReceiveTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMessageReceiveTimeoutAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMessageReceiveTimeoutAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMessageReceiveTimeoutNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMessageReceiveTimeoutNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getKeyFrameCount() throws UaException;

  /** Sets the existing node's local value. */
  void setKeyFrameCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readKeyFrameCount() throws UaException;

  /** Writes the value remotely. */
  void writeKeyFrameCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readKeyFrameCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeKeyFrameCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getKeyFrameCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getKeyFrameCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getHeaderLayoutUri() throws UaException;

  /** Sets the existing node's local value. */
  void setHeaderLayoutUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readHeaderLayoutUri() throws UaException;

  /** Writes the value remotely. */
  void writeHeaderLayoutUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readHeaderLayoutUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHeaderLayoutUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getHeaderLayoutUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityModeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityModeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityGroupId() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityGroupId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecurityGroupId() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityGroupId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityGroupIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityGroupIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable EndpointDescription @Nullable [] getSecurityKeyServices() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EndpointDescription @Nullable [] readSecurityKeyServices() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readSecurityKeyServicesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityKeyServicesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityKeyServicesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getDataSetReaderProperties() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable KeyValuePair @Nullable [] readDataSetReaderProperties() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readDataSetReaderPropertiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetReaderPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetReaderPropertiesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetReaderPropertiesNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetReaderTransportType getTransportSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable DataSetReaderTransportType> getTransportSettingsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetReaderMessageType getMessageSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable DataSetReaderMessageType> getMessageSettingsNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsDataSetReaderType getDiagnosticsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetReaderType>
      getDiagnosticsNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscribedDataSetType getSubscribedDataSetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends SubscribedDataSetType> getSubscribedDataSetNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getCreateTargetVariablesMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getCreateTargetVariablesMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable StatusCode @Nullable [] callCreateTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable StatusCode @Nullable []> callCreateTargetVariablesAsync(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callCreateTargetVariablesDetailed(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callCreateTargetVariablesDetailed(
      MethodCallOptions options,
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callCreateTargetVariablesDetailedAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callCreateTargetVariablesDetailedAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getCreateDataSetMirrorMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getCreateDataSetMirrorMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callCreateDataSetMirror(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callCreateDataSetMirrorAsync(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCreateDataSetMirrorDetailed(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callCreateDataSetMirrorDetailed(
      MethodCallOptions options,
      @Nullable String parentNodeName,
      @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDataSetMirrorDetailedAsync(
          @Nullable String parentNodeName,
          @Nullable RolePermissionType @Nullable [] rolePermissions);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDataSetMirrorDetailedAsync(
          MethodCallOptions options,
          @Nullable String parentNodeName,
          @Nullable RolePermissionType @Nullable [] rolePermissions);
}
