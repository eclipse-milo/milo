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
import org.eclipse.milo.opcua.sdk.client.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TransitionVariableTypeNode;
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

public class TransitionEventTypeNode extends BaseEventTypeNode implements TransitionEventType {
  public TransitionEventTypeNode(
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
  public LocalizedText getTransition() throws UaException {
    TransitionVariableTypeNode node = getTransitionNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setTransition(LocalizedText value) throws UaException {
    TransitionVariableTypeNode node = getTransitionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readTransition() throws UaException {
    try {
      return readTransitionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTransition(LocalizedText value) throws UaException {
    try {
      writeTransitionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readTransitionAsync() {
    return getTransitionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTransitionAsync(LocalizedText transition) {
    DataValue value = DataValue.valueOnly(new Variant(transition));
    return getTransitionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TransitionVariableTypeNode getTransitionNode() throws UaException {
    try {
      return getTransitionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionVariableTypeNode> getTransitionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Transition", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (TransitionVariableTypeNode) node);
  }

  @Override
  public LocalizedText getFromState() throws UaException {
    StateVariableTypeNode node = getFromStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setFromState(LocalizedText value) throws UaException {
    StateVariableTypeNode node = getFromStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readFromState() throws UaException {
    try {
      return readFromStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeFromState(LocalizedText value) throws UaException {
    try {
      writeFromStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readFromStateAsync() {
    return getFromStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeFromStateAsync(LocalizedText fromState) {
    DataValue value = DataValue.valueOnly(new Variant(fromState));
    return getFromStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public StateVariableTypeNode getFromStateNode() throws UaException {
    try {
      return getFromStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateVariableTypeNode> getFromStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "FromState", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getToState() throws UaException {
    StateVariableTypeNode node = getToStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setToState(LocalizedText value) throws UaException {
    StateVariableTypeNode node = getToStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readToState() throws UaException {
    try {
      return readToStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeToState(LocalizedText value) throws UaException {
    try {
      writeToStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readToStateAsync() {
    return getToStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeToStateAsync(LocalizedText toState) {
    DataValue value = DataValue.valueOnly(new Variant(toState));
    return getToStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public StateVariableTypeNode getToStateNode() throws UaException {
    try {
      return getToStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateVariableTypeNode> getToStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ToState", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateVariableTypeNode) node);
  }
}
