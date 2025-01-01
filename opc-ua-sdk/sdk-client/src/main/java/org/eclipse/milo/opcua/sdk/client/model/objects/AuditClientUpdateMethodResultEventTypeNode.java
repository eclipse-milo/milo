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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
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
  public NodeId getObjectId() throws UaException {
    PropertyTypeNode node = getObjectIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setObjectId(NodeId value) throws UaException {
    PropertyTypeNode node = getObjectIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readObjectId() throws UaException {
    try {
      return readObjectIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeObjectId(NodeId value) throws UaException {
    try {
      writeObjectIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readObjectIdAsync() {
    return getObjectIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeObjectIdAsync(NodeId objectId) {
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
  public NodeId getMethodId() throws UaException {
    PropertyTypeNode node = getMethodIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setMethodId(NodeId value) throws UaException {
    PropertyTypeNode node = getMethodIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readMethodId() throws UaException {
    try {
      return readMethodIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMethodId(NodeId value) throws UaException {
    try {
      writeMethodIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readMethodIdAsync() {
    return getMethodIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMethodIdAsync(NodeId methodId) {
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
  public Argument[] getInputArguments() throws UaException {
    PropertyTypeNode node = getInputArgumentsNode();
    return cast(node.getValue().getValue().getValue(), Argument[].class);
  }

  @Override
  public void setInputArguments(Argument[] value) throws UaException {
    PropertyTypeNode node = getInputArgumentsNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public Argument[] readInputArguments() throws UaException {
    try {
      return readInputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInputArguments(Argument[] value) throws UaException {
    try {
      writeInputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Argument[]> readInputArgumentsAsync() {
    return getInputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), Argument[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeInputArgumentsAsync(Argument[] inputArguments) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), inputArguments);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
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
  public Argument[] getOutputArguments() throws UaException {
    PropertyTypeNode node = getOutputArgumentsNode();
    return cast(node.getValue().getValue().getValue(), Argument[].class);
  }

  @Override
  public void setOutputArguments(Argument[] value) throws UaException {
    PropertyTypeNode node = getOutputArgumentsNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public Argument[] readOutputArguments() throws UaException {
    try {
      return readOutputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOutputArguments(Argument[] value) throws UaException {
    try {
      writeOutputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Argument[]> readOutputArgumentsAsync() {
    return getOutputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), Argument[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeOutputArgumentsAsync(Argument[] outputArguments) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), outputArguments);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
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
