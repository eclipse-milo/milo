/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;

public class BaseLogEventTypeNode extends BaseEventTypeNode implements BaseLogEventType {
  public BaseLogEventTypeNode(
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
  public NodeId getConditionClassId() throws UaException {
    PropertyTypeNode node = getConditionClassIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionClassId(NodeId value) throws UaException {
    PropertyTypeNode node = getConditionClassIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readConditionClassId() throws UaException {
    try {
      return readConditionClassIdAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeConditionClassId(NodeId value) throws UaException {
    try {
      writeConditionClassIdAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readConditionClassIdAsync() {
    return getConditionClassIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionClassIdAsync(NodeId conditionClassId) {
    DataValue value = DataValue.valueOnly(new Variant(conditionClassId));
    return getConditionClassIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionClassIdNode() throws UaException {
    try {
      return getConditionClassIdNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionClassId",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText getConditionClassName() throws UaException {
    PropertyTypeNode node = getConditionClassNameNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionClassName(LocalizedText value) throws UaException {
    PropertyTypeNode node = getConditionClassNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readConditionClassName() throws UaException {
    try {
      return readConditionClassNameAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeConditionClassName(LocalizedText value) throws UaException {
    try {
      writeConditionClassNameAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readConditionClassNameAsync() {
    return getConditionClassNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionClassNameAsync(
      LocalizedText conditionClassName) {
    DataValue value = DataValue.valueOnly(new Variant(conditionClassName));
    return getConditionClassNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionClassNameNode() throws UaException {
    try {
      return getConditionClassNameNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionClassName",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public StatusCode getErrorCode() throws UaException {
    PropertyTypeNode node = getErrorCodePropertyNode();
    return (StatusCode) node.getValue().getValue().getValue();
  }

  @Override
  public void setErrorCode(StatusCode value) throws UaException {
    PropertyTypeNode node = getErrorCodePropertyNode();
    node.setValue(new Variant(value));
  }

  @Override
  public StatusCode readErrorCode() throws UaException {
    try {
      return readErrorCodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeErrorCode(StatusCode value) throws UaException {
    try {
      writeErrorCodeAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends StatusCode> readErrorCodeAsync() {
    return getErrorCodePropertyNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (StatusCode) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeErrorCodeAsync(StatusCode errorCode) {
    DataValue value = DataValue.valueOnly(new Variant(errorCode));
    return getErrorCodePropertyNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getErrorCodePropertyNode() throws UaException {
    try {
      return getErrorCodePropertyNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getErrorCodePropertyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ErrorCode", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getErrorCodeNode() throws UaException {
    PropertyTypeNode node = getErrorCodeNodeNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setErrorCodeNode(NodeId value) throws UaException {
    PropertyTypeNode node = getErrorCodeNodeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readErrorCodeNode() throws UaException {
    try {
      return readErrorCodeNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeErrorCodeNode(NodeId value) throws UaException {
    try {
      writeErrorCodeNodeAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readErrorCodeNodeAsync() {
    return getErrorCodeNodeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeErrorCodeNodeAsync(NodeId errorCodeNode) {
    DataValue value = DataValue.valueOnly(new Variant(errorCodeNode));
    return getErrorCodeNodeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getErrorCodeNodeNode() throws UaException {
    try {
      return getErrorCodeNodeNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getErrorCodeNodeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ErrorCodeNode", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public TraceContextDataType getTraceContext() throws UaException {
    PropertyTypeNode node = getTraceContextNode();
    return cast(node.getValue().getValue().getValue(), TraceContextDataType.class);
  }

  @Override
  public void setTraceContext(TraceContextDataType value) throws UaException {
    PropertyTypeNode node = getTraceContextNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public TraceContextDataType readTraceContext() throws UaException {
    try {
      return readTraceContextAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeTraceContext(TraceContextDataType value) throws UaException {
    try {
      writeTraceContextAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends TraceContextDataType> readTraceContextAsync() {
    return getTraceContextNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), TraceContextDataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeTraceContextAsync(TraceContextDataType traceContext) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), traceContext);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getTraceContextNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getTraceContextNode() throws UaException {
    try {
      return getTraceContextNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTraceContextNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "TraceContext", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
