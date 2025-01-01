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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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

public class ISrClassTypeNode extends BaseInterfaceTypeNode implements ISrClassType {
  public ISrClassTypeNode(
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
  public UByte getId() throws UaException {
    BaseDataVariableTypeNode node = getIdNode();
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setId(UByte value) throws UaException {
    BaseDataVariableTypeNode node = getIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte readId() throws UaException {
    try {
      return readIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeId(UByte value) throws UaException {
    try {
      writeIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte> readIdAsync() {
    return getIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeIdAsync(UByte id) {
    DataValue value = DataValue.valueOnly(new Variant(id));
    return getIdNodeAsync().thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getIdNode() throws UaException {
    try {
      return getIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Id", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UByte getPriority() throws UaException {
    BaseDataVariableTypeNode node = getPriorityNode();
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setPriority(UByte value) throws UaException {
    BaseDataVariableTypeNode node = getPriorityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte readPriority() throws UaException {
    try {
      return readPriorityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePriority(UByte value) throws UaException {
    try {
      writePriorityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte> readPriorityAsync() {
    return getPriorityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityAsync(UByte priority) {
    DataValue value = DataValue.valueOnly(new Variant(priority));
    return getPriorityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getPriorityNode() throws UaException {
    try {
      return getPriorityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getPriorityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Priority", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UShort getVid() throws UaException {
    BaseDataVariableTypeNode node = getVidNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setVid(UShort value) throws UaException {
    BaseDataVariableTypeNode node = getVidNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readVid() throws UaException {
    try {
      return readVidAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeVid(UShort value) throws UaException {
    try {
      writeVidAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readVidAsync() {
    return getVidNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeVidAsync(UShort vid) {
    DataValue value = DataValue.valueOnly(new Variant(vid));
    return getVidNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getVidNode() throws UaException {
    try {
      return getVidNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getVidNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Vid", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
