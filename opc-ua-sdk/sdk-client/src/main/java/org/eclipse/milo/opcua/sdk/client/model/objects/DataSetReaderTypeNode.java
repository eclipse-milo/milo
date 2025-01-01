/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DataSetReaderTypeNode extends BaseObjectTypeNode implements DataSetReaderType {
  public DataSetReaderTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        client,
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  @Override
  public Object getPublisherId() throws UaException {
    PropertyTypeNode node = getPublisherIdNode();
    return (Object) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublisherId(Object value) throws UaException {
    PropertyTypeNode node = getPublisherIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object readPublisherId() throws UaException {
    try {
      return readPublisherIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePublisherId(Object value) throws UaException {
    try {
      writePublisherIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<?> readPublisherIdAsync() {
    return getPublisherIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePublisherIdAsync(Object publisherId) {
    DataValue value = DataValue.valueOnly(new Variant(publisherId));
    return getPublisherIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPublisherIdNode() throws UaException {
    try {
      return getPublisherIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublisherIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PublisherId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getWriterGroupId() throws UaException {
    PropertyTypeNode node = getWriterGroupIdNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setWriterGroupId(UShort value) throws UaException {
    PropertyTypeNode node = getWriterGroupIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readWriterGroupId() throws UaException {
    try {
      return readWriterGroupIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeWriterGroupId(UShort value) throws UaException {
    try {
      writeWriterGroupIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readWriterGroupIdAsync() {
    return getWriterGroupIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeWriterGroupIdAsync(UShort writerGroupId) {
    DataValue value = DataValue.valueOnly(new Variant(writerGroupId));
    return getWriterGroupIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getWriterGroupIdNode() throws UaException {
    try {
      return getWriterGroupIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getWriterGroupIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "WriterGroupId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getDataSetWriterId() throws UaException {
    PropertyTypeNode node = getDataSetWriterIdNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetWriterId(UShort value) throws UaException {
    PropertyTypeNode node = getDataSetWriterIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readDataSetWriterId() throws UaException {
    try {
      return readDataSetWriterIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetWriterId(UShort value) throws UaException {
    try {
      writeDataSetWriterIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readDataSetWriterIdAsync() {
    return getDataSetWriterIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetWriterIdAsync(UShort dataSetWriterId) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetWriterId));
    return getDataSetWriterIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetWriterIdNode() throws UaException {
    try {
      return getDataSetWriterIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetWriterId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DataSetMetaDataType getDataSetMetaData() throws UaException {
    PropertyTypeNode node = getDataSetMetaDataNode();
    return cast(node.getValue().getValue().getValue(), DataSetMetaDataType.class);
  }

  @Override
  public void setDataSetMetaData(DataSetMetaDataType value) throws UaException {
    PropertyTypeNode node = getDataSetMetaDataNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public DataSetMetaDataType readDataSetMetaData() throws UaException {
    try {
      return readDataSetMetaDataAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetMetaData(DataSetMetaDataType value) throws UaException {
    try {
      writeDataSetMetaDataAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetMetaDataType> readDataSetMetaDataAsync() {
    return getDataSetMetaDataNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), DataSetMetaDataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetMetaDataAsync(
      DataSetMetaDataType dataSetMetaData) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), dataSetMetaData);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getDataSetMetaDataNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetMetaDataNode() throws UaException {
    try {
      return getDataSetMetaDataNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMetaDataNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetMetaData",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DataSetFieldContentMask getDataSetFieldContentMask() throws UaException {
    PropertyTypeNode node = getDataSetFieldContentMaskNode();
    return (DataSetFieldContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetFieldContentMask(DataSetFieldContentMask value) throws UaException {
    PropertyTypeNode node = getDataSetFieldContentMaskNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DataSetFieldContentMask readDataSetFieldContentMask() throws UaException {
    try {
      return readDataSetFieldContentMaskAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetFieldContentMask(DataSetFieldContentMask value) throws UaException {
    try {
      writeDataSetFieldContentMaskAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetFieldContentMask> readDataSetFieldContentMaskAsync() {
    return getDataSetFieldContentMaskNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DataSetFieldContentMask) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      DataSetFieldContentMask dataSetFieldContentMask) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetFieldContentMask));
    return getDataSetFieldContentMaskNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetFieldContentMaskNode() throws UaException {
    try {
      return getDataSetFieldContentMaskNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetFieldContentMaskNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetFieldContentMask",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMessageReceiveTimeout() throws UaException {
    PropertyTypeNode node = getMessageReceiveTimeoutNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessageReceiveTimeout(Double value) throws UaException {
    PropertyTypeNode node = getMessageReceiveTimeoutNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMessageReceiveTimeout() throws UaException {
    try {
      return readMessageReceiveTimeoutAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMessageReceiveTimeout(Double value) throws UaException {
    try {
      writeMessageReceiveTimeoutAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMessageReceiveTimeoutAsync() {
    return getMessageReceiveTimeoutNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMessageReceiveTimeoutAsync(
      Double messageReceiveTimeout) {
    DataValue value = DataValue.valueOnly(new Variant(messageReceiveTimeout));
    return getMessageReceiveTimeoutNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMessageReceiveTimeoutNode() throws UaException {
    try {
      return getMessageReceiveTimeoutNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMessageReceiveTimeoutNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MessageReceiveTimeout",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getKeyFrameCount() throws UaException {
    PropertyTypeNode node = getKeyFrameCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setKeyFrameCount(UInteger value) throws UaException {
    PropertyTypeNode node = getKeyFrameCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readKeyFrameCount() throws UaException {
    try {
      return readKeyFrameCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeKeyFrameCount(UInteger value) throws UaException {
    try {
      writeKeyFrameCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readKeyFrameCountAsync() {
    return getKeyFrameCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeKeyFrameCountAsync(UInteger keyFrameCount) {
    DataValue value = DataValue.valueOnly(new Variant(keyFrameCount));
    return getKeyFrameCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getKeyFrameCountNode() throws UaException {
    try {
      return getKeyFrameCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getKeyFrameCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "KeyFrameCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getHeaderLayoutUri() throws UaException {
    PropertyTypeNode node = getHeaderLayoutUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setHeaderLayoutUri(String value) throws UaException {
    PropertyTypeNode node = getHeaderLayoutUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readHeaderLayoutUri() throws UaException {
    try {
      return readHeaderLayoutUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHeaderLayoutUri(String value) throws UaException {
    try {
      writeHeaderLayoutUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readHeaderLayoutUriAsync() {
    return getHeaderLayoutUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(String headerLayoutUri) {
    DataValue value = DataValue.valueOnly(new Variant(headerLayoutUri));
    return getHeaderLayoutUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHeaderLayoutUriNode() throws UaException {
    try {
      return getHeaderLayoutUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHeaderLayoutUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HeaderLayoutUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public MessageSecurityMode getSecurityMode() throws UaException {
    PropertyTypeNode node = getSecurityModeNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return MessageSecurityMode.from((Integer) value);
    } else if (value instanceof MessageSecurityMode) {
      return (MessageSecurityMode) value;
    } else {
      return null;
    }
  }

  @Override
  public void setSecurityMode(MessageSecurityMode value) throws UaException {
    PropertyTypeNode node = getSecurityModeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public MessageSecurityMode readSecurityMode() throws UaException {
    try {
      return readSecurityModeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityMode(MessageSecurityMode value) throws UaException {
    try {
      writeSecurityModeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends MessageSecurityMode> readSecurityModeAsync() {
    return getSecurityModeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return MessageSecurityMode.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityModeAsync(MessageSecurityMode securityMode) {
    DataValue value = DataValue.valueOnly(new Variant(securityMode));
    return getSecurityModeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityModeNode() throws UaException {
    try {
      return getSecurityModeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityModeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityMode",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getSecurityGroupId() throws UaException {
    PropertyTypeNode node = getSecurityGroupIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityGroupId(String value) throws UaException {
    PropertyTypeNode node = getSecurityGroupIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSecurityGroupId() throws UaException {
    try {
      return readSecurityGroupIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityGroupId(String value) throws UaException {
    try {
      writeSecurityGroupIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSecurityGroupIdAsync() {
    return getSecurityGroupIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityGroupIdAsync(String securityGroupId) {
    DataValue value = DataValue.valueOnly(new Variant(securityGroupId));
    return getSecurityGroupIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityGroupIdNode() throws UaException {
    try {
      return getSecurityGroupIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityGroupIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityGroupId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public EndpointDescription[] getSecurityKeyServices() throws UaException {
    PropertyTypeNode node = getSecurityKeyServicesNode();
    return cast(node.getValue().getValue().getValue(), EndpointDescription[].class);
  }

  @Override
  public void setSecurityKeyServices(EndpointDescription[] value) throws UaException {
    PropertyTypeNode node = getSecurityKeyServicesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public EndpointDescription[] readSecurityKeyServices() throws UaException {
    try {
      return readSecurityKeyServicesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityKeyServices(EndpointDescription[] value) throws UaException {
    try {
      writeSecurityKeyServicesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends EndpointDescription[]> readSecurityKeyServicesAsync() {
    return getSecurityKeyServicesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), EndpointDescription[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(
      EndpointDescription[] securityKeyServices) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), securityKeyServices);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSecurityKeyServicesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityKeyServicesNode() throws UaException {
    try {
      return getSecurityKeyServicesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityKeyServicesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityKeyServices",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public KeyValuePair[] getDataSetReaderProperties() throws UaException {
    PropertyTypeNode node = getDataSetReaderPropertiesNode();
    return cast(node.getValue().getValue().getValue(), KeyValuePair[].class);
  }

  @Override
  public void setDataSetReaderProperties(KeyValuePair[] value) throws UaException {
    PropertyTypeNode node = getDataSetReaderPropertiesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public KeyValuePair[] readDataSetReaderProperties() throws UaException {
    try {
      return readDataSetReaderPropertiesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetReaderProperties(KeyValuePair[] value) throws UaException {
    try {
      writeDataSetReaderPropertiesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends KeyValuePair[]> readDataSetReaderPropertiesAsync() {
    return getDataSetReaderPropertiesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), KeyValuePair[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetReaderPropertiesAsync(
      KeyValuePair[] dataSetReaderProperties) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), dataSetReaderProperties);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getDataSetReaderPropertiesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetReaderPropertiesNode() throws UaException {
    try {
      return getDataSetReaderPropertiesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetReaderPropertiesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetReaderProperties",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DataSetReaderTransportTypeNode getTransportSettingsNode() throws UaException {
    try {
      return getTransportSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetReaderTransportTypeNode>
      getTransportSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TransportSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (DataSetReaderTransportTypeNode) node);
  }

  @Override
  public DataSetReaderMessageTypeNode getMessageSettingsNode() throws UaException {
    try {
      return getMessageSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetReaderMessageTypeNode> getMessageSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MessageSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (DataSetReaderMessageTypeNode) node);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() throws UaException {
    try {
      return getStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubStatusTypeNode> getStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Status", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (PubSubStatusTypeNode) node);
  }

  @Override
  public PubSubDiagnosticsDataSetReaderTypeNode getDiagnosticsNode() throws UaException {
    try {
      return getDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsDataSetReaderTypeNode>
      getDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Diagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (PubSubDiagnosticsDataSetReaderTypeNode) node);
  }

  @Override
  public SubscribedDataSetTypeNode getSubscribedDataSetNode() throws UaException {
    try {
      return getSubscribedDataSetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SubscribedDataSetTypeNode> getSubscribedDataSetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SubscribedDataSet",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SubscribedDataSetTypeNode) node);
  }
}
