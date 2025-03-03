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

public class HistoryServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements HistoryServerCapabilitiesType {
  public HistoryServerCapabilitiesTypeNode(
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
  public Boolean getAccessHistoryDataCapability() throws UaException {
    PropertyTypeNode node = getAccessHistoryDataCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAccessHistoryDataCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getAccessHistoryDataCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readAccessHistoryDataCapability() throws UaException {
    try {
      return readAccessHistoryDataCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAccessHistoryDataCapability(Boolean value) throws UaException {
    try {
      writeAccessHistoryDataCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readAccessHistoryDataCapabilityAsync() {
    return getAccessHistoryDataCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAccessHistoryDataCapabilityAsync(
      Boolean accessHistoryDataCapability) {
    DataValue value = DataValue.valueOnly(new Variant(accessHistoryDataCapability));
    return getAccessHistoryDataCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAccessHistoryDataCapabilityNode() throws UaException {
    try {
      return getAccessHistoryDataCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAccessHistoryDataCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AccessHistoryDataCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getAccessHistoryEventsCapability() throws UaException {
    PropertyTypeNode node = getAccessHistoryEventsCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAccessHistoryEventsCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getAccessHistoryEventsCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readAccessHistoryEventsCapability() throws UaException {
    try {
      return readAccessHistoryEventsCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAccessHistoryEventsCapability(Boolean value) throws UaException {
    try {
      writeAccessHistoryEventsCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readAccessHistoryEventsCapabilityAsync() {
    return getAccessHistoryEventsCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAccessHistoryEventsCapabilityAsync(
      Boolean accessHistoryEventsCapability) {
    DataValue value = DataValue.valueOnly(new Variant(accessHistoryEventsCapability));
    return getAccessHistoryEventsCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAccessHistoryEventsCapabilityNode() throws UaException {
    try {
      return getAccessHistoryEventsCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAccessHistoryEventsCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AccessHistoryEventsCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxReturnDataValues() throws UaException {
    PropertyTypeNode node = getMaxReturnDataValuesNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReturnDataValues(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxReturnDataValuesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxReturnDataValues() throws UaException {
    try {
      return readMaxReturnDataValuesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxReturnDataValues(UInteger value) throws UaException {
    try {
      writeMaxReturnDataValuesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxReturnDataValuesAsync() {
    return getMaxReturnDataValuesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxReturnDataValuesAsync(UInteger maxReturnDataValues) {
    DataValue value = DataValue.valueOnly(new Variant(maxReturnDataValues));
    return getMaxReturnDataValuesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxReturnDataValuesNode() throws UaException {
    try {
      return getMaxReturnDataValuesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReturnDataValuesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxReturnDataValues",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxReturnEventValues() throws UaException {
    PropertyTypeNode node = getMaxReturnEventValuesNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReturnEventValues(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxReturnEventValuesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxReturnEventValues() throws UaException {
    try {
      return readMaxReturnEventValuesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxReturnEventValues(UInteger value) throws UaException {
    try {
      writeMaxReturnEventValuesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxReturnEventValuesAsync() {
    return getMaxReturnEventValuesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxReturnEventValuesAsync(
      UInteger maxReturnEventValues) {
    DataValue value = DataValue.valueOnly(new Variant(maxReturnEventValues));
    return getMaxReturnEventValuesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxReturnEventValuesNode() throws UaException {
    try {
      return getMaxReturnEventValuesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReturnEventValuesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxReturnEventValues",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getInsertDataCapability() throws UaException {
    PropertyTypeNode node = getInsertDataCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertDataCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getInsertDataCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readInsertDataCapability() throws UaException {
    try {
      return readInsertDataCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInsertDataCapability(Boolean value) throws UaException {
    try {
      writeInsertDataCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readInsertDataCapabilityAsync() {
    return getInsertDataCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeInsertDataCapabilityAsync(
      Boolean insertDataCapability) {
    DataValue value = DataValue.valueOnly(new Variant(insertDataCapability));
    return getInsertDataCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getInsertDataCapabilityNode() throws UaException {
    try {
      return getInsertDataCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertDataCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "InsertDataCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getReplaceDataCapability() throws UaException {
    PropertyTypeNode node = getReplaceDataCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setReplaceDataCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getReplaceDataCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readReplaceDataCapability() throws UaException {
    try {
      return readReplaceDataCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeReplaceDataCapability(Boolean value) throws UaException {
    try {
      writeReplaceDataCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readReplaceDataCapabilityAsync() {
    return getReplaceDataCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeReplaceDataCapabilityAsync(
      Boolean replaceDataCapability) {
    DataValue value = DataValue.valueOnly(new Variant(replaceDataCapability));
    return getReplaceDataCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getReplaceDataCapabilityNode() throws UaException {
    try {
      return getReplaceDataCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReplaceDataCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReplaceDataCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getUpdateDataCapability() throws UaException {
    PropertyTypeNode node = getUpdateDataCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdateDataCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getUpdateDataCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readUpdateDataCapability() throws UaException {
    try {
      return readUpdateDataCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUpdateDataCapability(Boolean value) throws UaException {
    try {
      writeUpdateDataCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readUpdateDataCapabilityAsync() {
    return getUpdateDataCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUpdateDataCapabilityAsync(
      Boolean updateDataCapability) {
    DataValue value = DataValue.valueOnly(new Variant(updateDataCapability));
    return getUpdateDataCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUpdateDataCapabilityNode() throws UaException {
    try {
      return getUpdateDataCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdateDataCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UpdateDataCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getDeleteRawCapability() throws UaException {
    PropertyTypeNode node = getDeleteRawCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteRawCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getDeleteRawCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readDeleteRawCapability() throws UaException {
    try {
      return readDeleteRawCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDeleteRawCapability(Boolean value) throws UaException {
    try {
      writeDeleteRawCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readDeleteRawCapabilityAsync() {
    return getDeleteRawCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteRawCapabilityAsync(Boolean deleteRawCapability) {
    DataValue value = DataValue.valueOnly(new Variant(deleteRawCapability));
    return getDeleteRawCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDeleteRawCapabilityNode() throws UaException {
    try {
      return getDeleteRawCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteRawCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DeleteRawCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getDeleteAtTimeCapability() throws UaException {
    PropertyTypeNode node = getDeleteAtTimeCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteAtTimeCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getDeleteAtTimeCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readDeleteAtTimeCapability() throws UaException {
    try {
      return readDeleteAtTimeCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDeleteAtTimeCapability(Boolean value) throws UaException {
    try {
      writeDeleteAtTimeCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readDeleteAtTimeCapabilityAsync() {
    return getDeleteAtTimeCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteAtTimeCapabilityAsync(
      Boolean deleteAtTimeCapability) {
    DataValue value = DataValue.valueOnly(new Variant(deleteAtTimeCapability));
    return getDeleteAtTimeCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDeleteAtTimeCapabilityNode() throws UaException {
    try {
      return getDeleteAtTimeCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteAtTimeCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DeleteAtTimeCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getInsertEventCapability() throws UaException {
    PropertyTypeNode node = getInsertEventCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertEventCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getInsertEventCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readInsertEventCapability() throws UaException {
    try {
      return readInsertEventCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInsertEventCapability(Boolean value) throws UaException {
    try {
      writeInsertEventCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readInsertEventCapabilityAsync() {
    return getInsertEventCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeInsertEventCapabilityAsync(
      Boolean insertEventCapability) {
    DataValue value = DataValue.valueOnly(new Variant(insertEventCapability));
    return getInsertEventCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getInsertEventCapabilityNode() throws UaException {
    try {
      return getInsertEventCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertEventCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "InsertEventCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getReplaceEventCapability() throws UaException {
    PropertyTypeNode node = getReplaceEventCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setReplaceEventCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getReplaceEventCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readReplaceEventCapability() throws UaException {
    try {
      return readReplaceEventCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeReplaceEventCapability(Boolean value) throws UaException {
    try {
      writeReplaceEventCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readReplaceEventCapabilityAsync() {
    return getReplaceEventCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeReplaceEventCapabilityAsync(
      Boolean replaceEventCapability) {
    DataValue value = DataValue.valueOnly(new Variant(replaceEventCapability));
    return getReplaceEventCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getReplaceEventCapabilityNode() throws UaException {
    try {
      return getReplaceEventCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReplaceEventCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReplaceEventCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getUpdateEventCapability() throws UaException {
    PropertyTypeNode node = getUpdateEventCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdateEventCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getUpdateEventCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readUpdateEventCapability() throws UaException {
    try {
      return readUpdateEventCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUpdateEventCapability(Boolean value) throws UaException {
    try {
      writeUpdateEventCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readUpdateEventCapabilityAsync() {
    return getUpdateEventCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUpdateEventCapabilityAsync(
      Boolean updateEventCapability) {
    DataValue value = DataValue.valueOnly(new Variant(updateEventCapability));
    return getUpdateEventCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUpdateEventCapabilityNode() throws UaException {
    try {
      return getUpdateEventCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdateEventCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UpdateEventCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getDeleteEventCapability() throws UaException {
    PropertyTypeNode node = getDeleteEventCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteEventCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getDeleteEventCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readDeleteEventCapability() throws UaException {
    try {
      return readDeleteEventCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDeleteEventCapability(Boolean value) throws UaException {
    try {
      writeDeleteEventCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readDeleteEventCapabilityAsync() {
    return getDeleteEventCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDeleteEventCapabilityAsync(
      Boolean deleteEventCapability) {
    DataValue value = DataValue.valueOnly(new Variant(deleteEventCapability));
    return getDeleteEventCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDeleteEventCapabilityNode() throws UaException {
    try {
      return getDeleteEventCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteEventCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DeleteEventCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getInsertAnnotationCapability() throws UaException {
    PropertyTypeNode node = getInsertAnnotationCapabilityNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertAnnotationCapability(Boolean value) throws UaException {
    PropertyTypeNode node = getInsertAnnotationCapabilityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readInsertAnnotationCapability() throws UaException {
    try {
      return readInsertAnnotationCapabilityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInsertAnnotationCapability(Boolean value) throws UaException {
    try {
      writeInsertAnnotationCapabilityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readInsertAnnotationCapabilityAsync() {
    return getInsertAnnotationCapabilityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeInsertAnnotationCapabilityAsync(
      Boolean insertAnnotationCapability) {
    DataValue value = DataValue.valueOnly(new Variant(insertAnnotationCapability));
    return getInsertAnnotationCapabilityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getInsertAnnotationCapabilityNode() throws UaException {
    try {
      return getInsertAnnotationCapabilityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertAnnotationCapabilityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "InsertAnnotationCapability",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getServerTimestampSupported() throws UaException {
    PropertyTypeNode node = getServerTimestampSupportedNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerTimestampSupported(Boolean value) throws UaException {
    PropertyTypeNode node = getServerTimestampSupportedNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readServerTimestampSupported() throws UaException {
    try {
      return readServerTimestampSupportedAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServerTimestampSupported(Boolean value) throws UaException {
    try {
      writeServerTimestampSupportedAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readServerTimestampSupportedAsync() {
    return getServerTimestampSupportedNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(
      Boolean serverTimestampSupported) {
    DataValue value = DataValue.valueOnly(new Variant(serverTimestampSupported));
    return getServerTimestampSupportedNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getServerTimestampSupportedNode() throws UaException {
    try {
      return getServerTimestampSupportedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerTimestampSupportedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ServerTimestampSupported",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() throws UaException {
    try {
      return getAggregateFunctionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getAggregateFunctionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AggregateFunctions",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (FolderTypeNode) node);
  }
}
