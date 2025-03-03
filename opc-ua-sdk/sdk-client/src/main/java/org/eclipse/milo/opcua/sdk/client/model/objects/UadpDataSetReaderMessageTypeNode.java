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

import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

public class UadpDataSetReaderMessageTypeNode extends DataSetReaderMessageTypeNode
    implements UadpDataSetReaderMessageType {
  public UadpDataSetReaderMessageTypeNode(
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
  public UInteger getGroupVersion() throws UaException {
    PropertyTypeNode node = getGroupVersionNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setGroupVersion(UInteger value) throws UaException {
    PropertyTypeNode node = getGroupVersionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readGroupVersion() throws UaException {
    try {
      return readGroupVersionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeGroupVersion(UInteger value) throws UaException {
    try {
      writeGroupVersionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readGroupVersionAsync() {
    return getGroupVersionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeGroupVersionAsync(UInteger groupVersion) {
    DataValue value = DataValue.valueOnly(new Variant(groupVersion));
    return getGroupVersionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getGroupVersionNode() throws UaException {
    try {
      return getGroupVersionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getGroupVersionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "GroupVersion",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getNetworkMessageNumber() throws UaException {
    PropertyTypeNode node = getNetworkMessageNumberNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setNetworkMessageNumber(UShort value) throws UaException {
    PropertyTypeNode node = getNetworkMessageNumberNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readNetworkMessageNumber() throws UaException {
    try {
      return readNetworkMessageNumberAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeNetworkMessageNumber(UShort value) throws UaException {
    try {
      writeNetworkMessageNumberAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readNetworkMessageNumberAsync() {
    return getNetworkMessageNumberNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeNetworkMessageNumberAsync(UShort networkMessageNumber) {
    DataValue value = DataValue.valueOnly(new Variant(networkMessageNumber));
    return getNetworkMessageNumberNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getNetworkMessageNumberNode() throws UaException {
    try {
      return getNetworkMessageNumberNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNetworkMessageNumberNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "NetworkMessageNumber",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getDataSetOffset() throws UaException {
    PropertyTypeNode node = getDataSetOffsetNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetOffset(UShort value) throws UaException {
    PropertyTypeNode node = getDataSetOffsetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readDataSetOffset() throws UaException {
    try {
      return readDataSetOffsetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetOffset(UShort value) throws UaException {
    try {
      writeDataSetOffsetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readDataSetOffsetAsync() {
    return getDataSetOffsetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetOffsetAsync(UShort dataSetOffset) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetOffset));
    return getDataSetOffsetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetOffsetNode() throws UaException {
    try {
      return getDataSetOffsetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetOffsetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetOffset",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UUID getDataSetClassId() throws UaException {
    PropertyTypeNode node = getDataSetClassIdNode();
    return (UUID) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetClassId(UUID value) throws UaException {
    PropertyTypeNode node = getDataSetClassIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UUID readDataSetClassId() throws UaException {
    try {
      return readDataSetClassIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetClassId(UUID value) throws UaException {
    try {
      writeDataSetClassIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UUID> readDataSetClassIdAsync() {
    return getDataSetClassIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UUID) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetClassIdAsync(UUID dataSetClassId) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetClassId));
    return getDataSetClassIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetClassIdNode() throws UaException {
    try {
      return getDataSetClassIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetClassIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetClassId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UadpNetworkMessageContentMask getNetworkMessageContentMask() throws UaException {
    PropertyTypeNode node = getNetworkMessageContentMaskNode();
    return (UadpNetworkMessageContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setNetworkMessageContentMask(UadpNetworkMessageContentMask value) throws UaException {
    PropertyTypeNode node = getNetworkMessageContentMaskNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UadpNetworkMessageContentMask readNetworkMessageContentMask() throws UaException {
    try {
      return readNetworkMessageContentMaskAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeNetworkMessageContentMask(UadpNetworkMessageContentMask value)
      throws UaException {
    try {
      writeNetworkMessageContentMaskAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UadpNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync() {
    return getNetworkMessageContentMaskNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UadpNetworkMessageContentMask) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      UadpNetworkMessageContentMask networkMessageContentMask) {
    DataValue value = DataValue.valueOnly(new Variant(networkMessageContentMask));
    return getNetworkMessageContentMaskNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getNetworkMessageContentMaskNode() throws UaException {
    try {
      return getNetworkMessageContentMaskNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNetworkMessageContentMaskNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "NetworkMessageContentMask",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UadpDataSetMessageContentMask getDataSetMessageContentMask() throws UaException {
    PropertyTypeNode node = getDataSetMessageContentMaskNode();
    return (UadpDataSetMessageContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetMessageContentMask(UadpDataSetMessageContentMask value) throws UaException {
    PropertyTypeNode node = getDataSetMessageContentMaskNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UadpDataSetMessageContentMask readDataSetMessageContentMask() throws UaException {
    try {
      return readDataSetMessageContentMaskAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetMessageContentMask(UadpDataSetMessageContentMask value)
      throws UaException {
    try {
      writeDataSetMessageContentMaskAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UadpDataSetMessageContentMask>
      readDataSetMessageContentMaskAsync() {
    return getDataSetMessageContentMaskNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UadpDataSetMessageContentMask) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      UadpDataSetMessageContentMask dataSetMessageContentMask) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetMessageContentMask));
    return getDataSetMessageContentMaskNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetMessageContentMaskNode() throws UaException {
    try {
      return getDataSetMessageContentMaskNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMessageContentMaskNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetMessageContentMask",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getPublishingInterval() throws UaException {
    PropertyTypeNode node = getPublishingIntervalNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublishingInterval(Double value) throws UaException {
    PropertyTypeNode node = getPublishingIntervalNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readPublishingInterval() throws UaException {
    try {
      return readPublishingIntervalAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePublishingInterval(Double value) throws UaException {
    try {
      writePublishingIntervalAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readPublishingIntervalAsync() {
    return getPublishingIntervalNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingIntervalAsync(Double publishingInterval) {
    DataValue value = DataValue.valueOnly(new Variant(publishingInterval));
    return getPublishingIntervalNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() throws UaException {
    try {
      return getPublishingIntervalNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishingIntervalNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PublishingInterval",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getProcessingOffset() throws UaException {
    PropertyTypeNode node = getProcessingOffsetNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setProcessingOffset(Double value) throws UaException {
    PropertyTypeNode node = getProcessingOffsetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readProcessingOffset() throws UaException {
    try {
      return readProcessingOffsetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeProcessingOffset(Double value) throws UaException {
    try {
      writeProcessingOffsetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readProcessingOffsetAsync() {
    return getProcessingOffsetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeProcessingOffsetAsync(Double processingOffset) {
    DataValue value = DataValue.valueOnly(new Variant(processingOffset));
    return getProcessingOffsetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getProcessingOffsetNode() throws UaException {
    try {
      return getProcessingOffsetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getProcessingOffsetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ProcessingOffset",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getReceiveOffset() throws UaException {
    PropertyTypeNode node = getReceiveOffsetNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setReceiveOffset(Double value) throws UaException {
    PropertyTypeNode node = getReceiveOffsetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readReceiveOffset() throws UaException {
    try {
      return readReceiveOffsetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeReceiveOffset(Double value) throws UaException {
    try {
      writeReceiveOffsetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readReceiveOffsetAsync() {
    return getReceiveOffsetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeReceiveOffsetAsync(Double receiveOffset) {
    DataValue value = DataValue.valueOnly(new Variant(receiveOffset));
    return getReceiveOffsetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getReceiveOffsetNode() throws UaException {
    try {
      return getReceiveOffsetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReceiveOffsetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReceiveOffset",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
