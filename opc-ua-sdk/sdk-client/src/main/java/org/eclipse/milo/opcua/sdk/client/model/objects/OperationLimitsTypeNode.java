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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class OperationLimitsTypeNode extends FolderTypeNode implements OperationLimitsType {
  public OperationLimitsTypeNode(
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
  public UInteger getMaxNodesPerRead() throws UaException {
    PropertyTypeNode node = getMaxNodesPerReadNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerRead(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerReadNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerRead() throws UaException {
    try {
      return readMaxNodesPerReadAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerRead(UInteger value) throws UaException {
    try {
      writeMaxNodesPerReadAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerReadAsync() {
    return getMaxNodesPerReadNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerReadAsync(UInteger maxNodesPerRead) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerRead));
    return getMaxNodesPerReadNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerReadNode() throws UaException {
    try {
      return getMaxNodesPerReadNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerReadNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerRead",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerHistoryReadData() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadDataNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryReadData(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadDataNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerHistoryReadData() throws UaException {
    try {
      return readMaxNodesPerHistoryReadDataAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerHistoryReadData(UInteger value) throws UaException {
    try {
      writeMaxNodesPerHistoryReadDataAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerHistoryReadDataAsync() {
    return getMaxNodesPerHistoryReadDataNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadDataAsync(
      UInteger maxNodesPerHistoryReadData) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryReadData));
    return getMaxNodesPerHistoryReadDataNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryReadDataNode() throws UaException {
    try {
      return getMaxNodesPerHistoryReadDataNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerHistoryReadDataNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryReadData",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerHistoryReadEvents() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadEventsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryReadEvents(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadEventsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerHistoryReadEvents() throws UaException {
    try {
      return readMaxNodesPerHistoryReadEventsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerHistoryReadEvents(UInteger value) throws UaException {
    try {
      writeMaxNodesPerHistoryReadEventsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerHistoryReadEventsAsync() {
    return getMaxNodesPerHistoryReadEventsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadEventsAsync(
      UInteger maxNodesPerHistoryReadEvents) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryReadEvents));
    return getMaxNodesPerHistoryReadEventsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryReadEventsNode() throws UaException {
    try {
      return getMaxNodesPerHistoryReadEventsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerHistoryReadEventsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryReadEvents",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerWrite() throws UaException {
    PropertyTypeNode node = getMaxNodesPerWriteNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerWrite(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerWriteNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerWrite() throws UaException {
    try {
      return readMaxNodesPerWriteAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerWrite(UInteger value) throws UaException {
    try {
      writeMaxNodesPerWriteAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerWriteAsync() {
    return getMaxNodesPerWriteNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerWriteAsync(UInteger maxNodesPerWrite) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerWrite));
    return getMaxNodesPerWriteNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerWriteNode() throws UaException {
    try {
      return getMaxNodesPerWriteNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerWriteNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerWrite",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerHistoryUpdateData() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateDataNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryUpdateData(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateDataNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerHistoryUpdateData() throws UaException {
    try {
      return readMaxNodesPerHistoryUpdateDataAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerHistoryUpdateData(UInteger value) throws UaException {
    try {
      writeMaxNodesPerHistoryUpdateDataAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerHistoryUpdateDataAsync() {
    return getMaxNodesPerHistoryUpdateDataNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateDataAsync(
      UInteger maxNodesPerHistoryUpdateData) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryUpdateData));
    return getMaxNodesPerHistoryUpdateDataNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryUpdateDataNode() throws UaException {
    try {
      return getMaxNodesPerHistoryUpdateDataNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerHistoryUpdateDataNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryUpdateData",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerHistoryUpdateEvents() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateEventsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryUpdateEvents(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateEventsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerHistoryUpdateEvents() throws UaException {
    try {
      return readMaxNodesPerHistoryUpdateEventsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerHistoryUpdateEvents(UInteger value) throws UaException {
    try {
      writeMaxNodesPerHistoryUpdateEventsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerHistoryUpdateEventsAsync() {
    return getMaxNodesPerHistoryUpdateEventsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateEventsAsync(
      UInteger maxNodesPerHistoryUpdateEvents) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryUpdateEvents));
    return getMaxNodesPerHistoryUpdateEventsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryUpdateEventsNode() throws UaException {
    try {
      return getMaxNodesPerHistoryUpdateEventsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode>
      getMaxNodesPerHistoryUpdateEventsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryUpdateEvents",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerMethodCall() throws UaException {
    PropertyTypeNode node = getMaxNodesPerMethodCallNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerMethodCall(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerMethodCallNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerMethodCall() throws UaException {
    try {
      return readMaxNodesPerMethodCallAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerMethodCall(UInteger value) throws UaException {
    try {
      writeMaxNodesPerMethodCallAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerMethodCallAsync() {
    return getMaxNodesPerMethodCallNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerMethodCallAsync(
      UInteger maxNodesPerMethodCall) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerMethodCall));
    return getMaxNodesPerMethodCallNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerMethodCallNode() throws UaException {
    try {
      return getMaxNodesPerMethodCallNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerMethodCallNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerMethodCall",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerBrowse() throws UaException {
    PropertyTypeNode node = getMaxNodesPerBrowseNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerBrowse(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerBrowseNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerBrowse() throws UaException {
    try {
      return readMaxNodesPerBrowseAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerBrowse(UInteger value) throws UaException {
    try {
      writeMaxNodesPerBrowseAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerBrowseAsync() {
    return getMaxNodesPerBrowseNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerBrowseAsync(UInteger maxNodesPerBrowse) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerBrowse));
    return getMaxNodesPerBrowseNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerBrowseNode() throws UaException {
    try {
      return getMaxNodesPerBrowseNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerBrowseNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerBrowse",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerRegisterNodes() throws UaException {
    PropertyTypeNode node = getMaxNodesPerRegisterNodesNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerRegisterNodes(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerRegisterNodesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerRegisterNodes() throws UaException {
    try {
      return readMaxNodesPerRegisterNodesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerRegisterNodes(UInteger value) throws UaException {
    try {
      writeMaxNodesPerRegisterNodesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerRegisterNodesAsync() {
    return getMaxNodesPerRegisterNodesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerRegisterNodesAsync(
      UInteger maxNodesPerRegisterNodes) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerRegisterNodes));
    return getMaxNodesPerRegisterNodesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerRegisterNodesNode() throws UaException {
    try {
      return getMaxNodesPerRegisterNodesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerRegisterNodesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerRegisterNodes",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException {
    PropertyTypeNode node = getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerTranslateBrowsePathsToNodeIds(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException {
    try {
      return readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerTranslateBrowsePathsToNodeIds(UInteger value) throws UaException {
    try {
      writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync() {
    return getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(
      UInteger maxNodesPerTranslateBrowsePathsToNodeIds) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerTranslateBrowsePathsToNodeIds));
    return getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerTranslateBrowsePathsToNodeIdsNode() throws UaException {
    try {
      return getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode>
      getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerTranslateBrowsePathsToNodeIds",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNodesPerNodeManagement() throws UaException {
    PropertyTypeNode node = getMaxNodesPerNodeManagementNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerNodeManagement(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerNodeManagementNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNodesPerNodeManagement() throws UaException {
    try {
      return readMaxNodesPerNodeManagementAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNodesPerNodeManagement(UInteger value) throws UaException {
    try {
      writeMaxNodesPerNodeManagementAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNodesPerNodeManagementAsync() {
    return getMaxNodesPerNodeManagementNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNodesPerNodeManagementAsync(
      UInteger maxNodesPerNodeManagement) {
    DataValue value = DataValue.valueOnly(new Variant(maxNodesPerNodeManagement));
    return getMaxNodesPerNodeManagementNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNodesPerNodeManagementNode() throws UaException {
    try {
      return getMaxNodesPerNodeManagementNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNodesPerNodeManagementNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerNodeManagement",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxMonitoredItemsPerCall() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerCallNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsPerCall(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerCallNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxMonitoredItemsPerCall() throws UaException {
    try {
      return readMaxMonitoredItemsPerCallAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxMonitoredItemsPerCall(UInteger value) throws UaException {
    try {
      writeMaxMonitoredItemsPerCallAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxMonitoredItemsPerCallAsync() {
    return getMaxMonitoredItemsPerCallNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsPerCallAsync(
      UInteger maxMonitoredItemsPerCall) {
    DataValue value = DataValue.valueOnly(new Variant(maxMonitoredItemsPerCall));
    return getMaxMonitoredItemsPerCallNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsPerCallNode() throws UaException {
    try {
      return getMaxMonitoredItemsPerCallNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxMonitoredItemsPerCallNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsPerCall",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
