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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableTypeNode;
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

public class FiniteStateMachineTypeNode extends StateMachineTypeNode
    implements FiniteStateMachineType {
  public FiniteStateMachineTypeNode(
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
  public LocalizedText getCurrentState() throws UaException {
    FiniteStateVariableTypeNode node = getCurrentStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentState(LocalizedText value) throws UaException {
    FiniteStateVariableTypeNode node = getCurrentStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readCurrentState() throws UaException {
    try {
      return readCurrentStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCurrentState(LocalizedText value) throws UaException {
    try {
      writeCurrentStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readCurrentStateAsync() {
    return getCurrentStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentStateAsync(LocalizedText currentState) {
    DataValue value = DataValue.valueOnly(new Variant(currentState));
    return getCurrentStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public FiniteStateVariableTypeNode getCurrentStateNode() throws UaException {
    try {
      return getCurrentStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends FiniteStateVariableTypeNode> getCurrentStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CurrentState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (FiniteStateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getLastTransition() throws UaException {
    FiniteTransitionVariableTypeNode node = getLastTransitionNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastTransition(LocalizedText value) throws UaException {
    FiniteTransitionVariableTypeNode node = getLastTransitionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readLastTransition() throws UaException {
    try {
      return readLastTransitionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastTransition(LocalizedText value) throws UaException {
    try {
      writeLastTransitionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readLastTransitionAsync() {
    return getLastTransitionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastTransitionAsync(LocalizedText lastTransition) {
    DataValue value = DataValue.valueOnly(new Variant(lastTransition));
    return getLastTransitionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public FiniteTransitionVariableTypeNode getLastTransitionNode() throws UaException {
    try {
      return getLastTransitionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends FiniteTransitionVariableTypeNode>
      getLastTransitionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastTransition",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (FiniteTransitionVariableTypeNode) node);
  }

  @Override
  public NodeId[] getAvailableStates() throws UaException {
    BaseDataVariableTypeNode node = getAvailableStatesNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setAvailableStates(NodeId[] value) throws UaException {
    BaseDataVariableTypeNode node = getAvailableStatesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readAvailableStates() throws UaException {
    try {
      return readAvailableStatesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAvailableStates(NodeId[] value) throws UaException {
    try {
      writeAvailableStatesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readAvailableStatesAsync() {
    return getAvailableStatesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailableStatesAsync(NodeId[] availableStates) {
    DataValue value = DataValue.valueOnly(new Variant(availableStates));
    return getAvailableStatesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getAvailableStatesNode() throws UaException {
    try {
      return getAvailableStatesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getAvailableStatesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AvailableStates",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public NodeId[] getAvailableTransitions() throws UaException {
    BaseDataVariableTypeNode node = getAvailableTransitionsNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setAvailableTransitions(NodeId[] value) throws UaException {
    BaseDataVariableTypeNode node = getAvailableTransitionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readAvailableTransitions() throws UaException {
    try {
      return readAvailableTransitionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAvailableTransitions(NodeId[] value) throws UaException {
    try {
      writeAvailableTransitionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readAvailableTransitionsAsync() {
    return getAvailableTransitionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAvailableTransitionsAsync(
      NodeId[] availableTransitions) {
    DataValue value = DataValue.valueOnly(new Variant(availableTransitions));
    return getAvailableTransitionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getAvailableTransitionsNode() throws UaException {
    try {
      return getAvailableTransitionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getAvailableTransitionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AvailableTransitions",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
