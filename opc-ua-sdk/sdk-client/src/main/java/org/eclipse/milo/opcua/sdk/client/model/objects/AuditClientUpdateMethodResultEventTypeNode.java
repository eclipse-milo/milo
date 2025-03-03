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

public class AuditClientUpdateMethodResultEventTypeNode extends AuditClientEventTypeNode
    implements AuditClientUpdateMethodResultEventType {
  public AuditClientUpdateMethodResultEventTypeNode(
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
  public ExpandedNodeId getObjectId() throws UaException {
    PropertyTypeNode node = getObjectIdNode();
    return (ExpandedNodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setObjectId(ExpandedNodeId value) throws UaException {
    PropertyTypeNode node = getObjectIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ExpandedNodeId readObjectId() throws UaException {
    try {
      return readObjectIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeObjectId(ExpandedNodeId value) throws UaException {
    try {
      writeObjectIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ExpandedNodeId> readObjectIdAsync() {
    return getObjectIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ExpandedNodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeObjectIdAsync(ExpandedNodeId objectId) {
    DataValue value = DataValue.valueOnly(new Variant(objectId));
    return getObjectIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getObjectIdNode() throws UaException {
    try {
      return getObjectIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getObjectIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ObjectId", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public ExpandedNodeId getMethodId() throws UaException {
    PropertyTypeNode node = getMethodIdNode();
    return (ExpandedNodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setMethodId(ExpandedNodeId value) throws UaException {
    PropertyTypeNode node = getMethodIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ExpandedNodeId readMethodId() throws UaException {
    try {
      return readMethodIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMethodId(ExpandedNodeId value) throws UaException {
    try {
      writeMethodIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ExpandedNodeId> readMethodIdAsync() {
    return getMethodIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ExpandedNodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMethodIdAsync(ExpandedNodeId methodId) {
    DataValue value = DataValue.valueOnly(new Variant(methodId));
    return getMethodIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMethodIdNode() throws UaException {
    try {
      return getMethodIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMethodIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "MethodId", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public StatusCode getStatusCodeId() throws UaException {
    PropertyTypeNode node = getStatusCodeIdNode();
    return (StatusCode) node.getValue().getValue().getValue();
  }

  @Override
  public void setStatusCodeId(StatusCode value) throws UaException {
    PropertyTypeNode node = getStatusCodeIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public StatusCode readStatusCodeId() throws UaException {
    try {
      return readStatusCodeIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStatusCodeId(StatusCode value) throws UaException {
    try {
      writeStatusCodeIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends StatusCode> readStatusCodeIdAsync() {
    return getStatusCodeIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (StatusCode) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeStatusCodeIdAsync(StatusCode statusCodeId) {
    DataValue value = DataValue.valueOnly(new Variant(statusCodeId));
    return getStatusCodeIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getStatusCodeIdNode() throws UaException {
    try {
      return getStatusCodeIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStatusCodeIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "StatusCodeId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Object[] getInputArguments() throws UaException {
    PropertyTypeNode node = getInputArgumentsNode();
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setInputArguments(Object[] value) throws UaException {
    PropertyTypeNode node = getInputArgumentsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object[] readInputArguments() throws UaException {
    try {
      return readInputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInputArguments(Object[] value) throws UaException {
    try {
      writeInputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Object[]> readInputArgumentsAsync() {
    return getInputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeInputArgumentsAsync(Object[] inputArguments) {
    DataValue value = DataValue.valueOnly(new Variant(inputArguments));
    return getInputArgumentsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getInputArgumentsNode() throws UaException {
    try {
      return getInputArgumentsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInputArgumentsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "InputArguments",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Object[] getOutputArguments() throws UaException {
    PropertyTypeNode node = getOutputArgumentsNode();
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setOutputArguments(Object[] value) throws UaException {
    PropertyTypeNode node = getOutputArgumentsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object[] readOutputArguments() throws UaException {
    try {
      return readOutputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOutputArguments(Object[] value) throws UaException {
    try {
      writeOutputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Object[]> readOutputArgumentsAsync() {
    return getOutputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeOutputArgumentsAsync(Object[] outputArguments) {
    DataValue value = DataValue.valueOnly(new Variant(outputArguments));
    return getOutputArgumentsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getOutputArgumentsNode() throws UaException {
    try {
      return getOutputArgumentsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOutputArgumentsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "OutputArguments",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
