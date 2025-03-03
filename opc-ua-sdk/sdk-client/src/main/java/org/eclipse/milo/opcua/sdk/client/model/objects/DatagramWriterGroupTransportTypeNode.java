/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;

public class DatagramWriterGroupTransportTypeNode extends WriterGroupTransportTypeNode
    implements DatagramWriterGroupTransportType {
  public DatagramWriterGroupTransportTypeNode(
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
  public UByte getMessageRepeatCount() throws UaException {
    PropertyTypeNode node = getMessageRepeatCountNode();
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessageRepeatCount(UByte value) throws UaException {
    PropertyTypeNode node = getMessageRepeatCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte readMessageRepeatCount() throws UaException {
    try {
      return readMessageRepeatCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMessageRepeatCount(UByte value) throws UaException {
    try {
      writeMessageRepeatCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte> readMessageRepeatCountAsync() {
    return getMessageRepeatCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMessageRepeatCountAsync(UByte messageRepeatCount) {
    DataValue value = DataValue.valueOnly(new Variant(messageRepeatCount));
    return getMessageRepeatCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMessageRepeatCountNode() throws UaException {
    try {
      return getMessageRepeatCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMessageRepeatCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MessageRepeatCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMessageRepeatDelay() throws UaException {
    PropertyTypeNode node = getMessageRepeatDelayNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessageRepeatDelay(Double value) throws UaException {
    PropertyTypeNode node = getMessageRepeatDelayNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMessageRepeatDelay() throws UaException {
    try {
      return readMessageRepeatDelayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMessageRepeatDelay(Double value) throws UaException {
    try {
      writeMessageRepeatDelayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMessageRepeatDelayAsync() {
    return getMessageRepeatDelayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMessageRepeatDelayAsync(Double messageRepeatDelay) {
    DataValue value = DataValue.valueOnly(new Variant(messageRepeatDelay));
    return getMessageRepeatDelayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMessageRepeatDelayNode() throws UaException {
    try {
      return getMessageRepeatDelayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMessageRepeatDelayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MessageRepeatDelay",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getQosCategory() throws UaException {
    PropertyTypeNode node = getQosCategoryNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setQosCategory(String value) throws UaException {
    PropertyTypeNode node = getQosCategoryNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readQosCategory() throws UaException {
    try {
      return readQosCategoryAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeQosCategory(String value) throws UaException {
    try {
      writeQosCategoryAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readQosCategoryAsync() {
    return getQosCategoryNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeQosCategoryAsync(String qosCategory) {
    DataValue value = DataValue.valueOnly(new Variant(qosCategory));
    return getQosCategoryNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getQosCategoryNode() throws UaException {
    try {
      return getQosCategoryNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getQosCategoryNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "QosCategory",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public TransmitQosDataType[] getDatagramQos() throws UaException {
    PropertyTypeNode node = getDatagramQosNode();
    return cast(node.getValue().getValue().getValue(), TransmitQosDataType[].class);
  }

  @Override
  public void setDatagramQos(TransmitQosDataType[] value) throws UaException {
    PropertyTypeNode node = getDatagramQosNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public TransmitQosDataType[] readDatagramQos() throws UaException {
    try {
      return readDatagramQosAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDatagramQos(TransmitQosDataType[] value) throws UaException {
    try {
      writeDatagramQosAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends TransmitQosDataType[]> readDatagramQosAsync() {
    return getDatagramQosNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), TransmitQosDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeDatagramQosAsync(TransmitQosDataType[] datagramQos) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), datagramQos);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getDatagramQosNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDatagramQosNode() throws UaException {
    try {
      return getDatagramQosNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDatagramQosNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DatagramQos",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getDiscoveryAnnounceRate() throws UaException {
    PropertyTypeNode node = getDiscoveryAnnounceRateNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDiscoveryAnnounceRate(UInteger value) throws UaException {
    PropertyTypeNode node = getDiscoveryAnnounceRateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readDiscoveryAnnounceRate() throws UaException {
    try {
      return readDiscoveryAnnounceRateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDiscoveryAnnounceRate(UInteger value) throws UaException {
    try {
      writeDiscoveryAnnounceRateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readDiscoveryAnnounceRateAsync() {
    return getDiscoveryAnnounceRateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDiscoveryAnnounceRateAsync(
      UInteger discoveryAnnounceRate) {
    DataValue value = DataValue.valueOnly(new Variant(discoveryAnnounceRate));
    return getDiscoveryAnnounceRateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDiscoveryAnnounceRateNode() throws UaException {
    try {
      return getDiscoveryAnnounceRateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDiscoveryAnnounceRateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DiscoveryAnnounceRate",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getTopic() throws UaException {
    PropertyTypeNode node = getTopicNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setTopic(String value) throws UaException {
    PropertyTypeNode node = getTopicNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readTopic() throws UaException {
    try {
      return readTopicAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTopic(String value) throws UaException {
    try {
      writeTopicAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readTopicAsync() {
    return getTopicNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTopicAsync(String topic) {
    DataValue value = DataValue.valueOnly(new Variant(topic));
    return getTopicNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getTopicNode() throws UaException {
    try {
      return getTopicNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTopicNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Topic", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NetworkAddressTypeNode getAddressNode() throws UaException {
    try {
      return getAddressNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends NetworkAddressTypeNode> getAddressNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Address", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (NetworkAddressTypeNode) node);
  }
}
