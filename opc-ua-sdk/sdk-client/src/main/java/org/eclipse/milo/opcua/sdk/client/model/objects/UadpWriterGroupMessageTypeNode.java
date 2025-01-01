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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

public class UadpWriterGroupMessageTypeNode extends WriterGroupMessageTypeNode
    implements UadpWriterGroupMessageType {
  public UadpWriterGroupMessageTypeNode(
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
  public DataSetOrderingType getDataSetOrdering() throws UaException {
    PropertyTypeNode node = getDataSetOrderingNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return DataSetOrderingType.from((Integer) value);
    } else if (value instanceof DataSetOrderingType) {
      return (DataSetOrderingType) value;
    } else {
      return null;
    }
  }

  @Override
  public void setDataSetOrdering(DataSetOrderingType value) throws UaException {
    PropertyTypeNode node = getDataSetOrderingNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DataSetOrderingType readDataSetOrdering() throws UaException {
    try {
      return readDataSetOrderingAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetOrdering(DataSetOrderingType value) throws UaException {
    try {
      writeDataSetOrderingAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetOrderingType> readDataSetOrderingAsync() {
    return getDataSetOrderingNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return DataSetOrderingType.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetOrderingAsync(
      DataSetOrderingType dataSetOrdering) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetOrdering));
    return getDataSetOrderingNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetOrderingNode() throws UaException {
    try {
      return getDataSetOrderingNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetOrderingNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetOrdering",
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
  public Double getSamplingOffset() throws UaException {
    PropertyTypeNode node = getSamplingOffsetNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setSamplingOffset(Double value) throws UaException {
    PropertyTypeNode node = getSamplingOffsetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readSamplingOffset() throws UaException {
    try {
      return readSamplingOffsetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSamplingOffset(Double value) throws UaException {
    try {
      writeSamplingOffsetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readSamplingOffsetAsync() {
    return getSamplingOffsetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingOffsetAsync(Double samplingOffset) {
    DataValue value = DataValue.valueOnly(new Variant(samplingOffset));
    return getSamplingOffsetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSamplingOffsetNode() throws UaException {
    try {
      return getSamplingOffsetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSamplingOffsetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SamplingOffset",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double[] getPublishingOffset() throws UaException {
    PropertyTypeNode node = getPublishingOffsetNode();
    return (Double[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublishingOffset(Double[] value) throws UaException {
    PropertyTypeNode node = getPublishingOffsetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double[] readPublishingOffset() throws UaException {
    try {
      return readPublishingOffsetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePublishingOffset(Double[] value) throws UaException {
    try {
      writePublishingOffsetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double[]> readPublishingOffsetAsync() {
    return getPublishingOffsetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingOffsetAsync(Double[] publishingOffset) {
    DataValue value = DataValue.valueOnly(new Variant(publishingOffset));
    return getPublishingOffsetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPublishingOffsetNode() throws UaException {
    try {
      return getPublishingOffsetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishingOffsetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PublishingOffset",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
