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
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DatagramConnectionTransportTypeNode extends ConnectionTransportTypeNode
    implements DatagramConnectionTransportType {
  public DatagramConnectionTransportTypeNode(
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
  public UInteger getDiscoveryMaxMessageSize() throws UaException {
    PropertyTypeNode node = getDiscoveryMaxMessageSizeNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDiscoveryMaxMessageSize(UInteger value) throws UaException {
    PropertyTypeNode node = getDiscoveryMaxMessageSizeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readDiscoveryMaxMessageSize() throws UaException {
    try {
      return readDiscoveryMaxMessageSizeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDiscoveryMaxMessageSize(UInteger value) throws UaException {
    try {
      writeDiscoveryMaxMessageSizeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readDiscoveryMaxMessageSizeAsync() {
    return getDiscoveryMaxMessageSizeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDiscoveryMaxMessageSizeAsync(
      UInteger discoveryMaxMessageSize) {
    DataValue value = DataValue.valueOnly(new Variant(discoveryMaxMessageSize));
    return getDiscoveryMaxMessageSizeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDiscoveryMaxMessageSizeNode() throws UaException {
    try {
      return getDiscoveryMaxMessageSizeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDiscoveryMaxMessageSizeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DiscoveryMaxMessageSize",
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
  public QosDataType[] getDatagramQos() throws UaException {
    PropertyTypeNode node = getDatagramQosNode();
    return cast(node.getValue().getValue().getValue(), QosDataType[].class);
  }

  @Override
  public void setDatagramQos(QosDataType[] value) throws UaException {
    PropertyTypeNode node = getDatagramQosNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public QosDataType[] readDatagramQos() throws UaException {
    try {
      return readDatagramQosAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDatagramQos(QosDataType[] value) throws UaException {
    try {
      writeDatagramQosAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends QosDataType[]> readDatagramQosAsync() {
    return getDatagramQosNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), QosDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeDatagramQosAsync(QosDataType[] datagramQos) {
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
  public NetworkAddressTypeNode getDiscoveryAddressNode() throws UaException {
    try {
      return getDiscoveryAddressNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends NetworkAddressTypeNode> getDiscoveryAddressNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DiscoveryAddress",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (NetworkAddressTypeNode) node);
  }
}
