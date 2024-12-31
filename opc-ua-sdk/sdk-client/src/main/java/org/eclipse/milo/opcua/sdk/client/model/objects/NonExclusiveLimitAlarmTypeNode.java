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
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
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

public class NonExclusiveLimitAlarmTypeNode extends LimitAlarmTypeNode
    implements NonExclusiveLimitAlarmType {
  public NonExclusiveLimitAlarmTypeNode(
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
  public LocalizedText getActiveState() throws UaException {
    TwoStateVariableTypeNode node = getActiveStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setActiveState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getActiveStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readActiveState() throws UaException {
    try {
      return readActiveStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeActiveState(LocalizedText value) throws UaException {
    try {
      writeActiveStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readActiveStateAsync() {
    return getActiveStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeActiveStateAsync(LocalizedText activeState) {
    DataValue value = DataValue.valueOnly(new Variant(activeState));
    return getActiveStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getActiveStateNode() throws UaException {
    try {
      return getActiveStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getActiveStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ActiveState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getHighHighState() throws UaException {
    TwoStateVariableTypeNode node = getHighHighStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighHighState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getHighHighStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readHighHighState() throws UaException {
    try {
      return readHighHighStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighHighState(LocalizedText value) throws UaException {
    try {
      writeHighHighStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readHighHighStateAsync() {
    return getHighHighStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighHighStateAsync(LocalizedText highHighState) {
    DataValue value = DataValue.valueOnly(new Variant(highHighState));
    return getHighHighStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getHighHighStateNode() throws UaException {
    try {
      return getHighHighStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getHighHighStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighHighState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getHighState() throws UaException {
    TwoStateVariableTypeNode node = getHighStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getHighStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readHighState() throws UaException {
    try {
      return readHighStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighState(LocalizedText value) throws UaException {
    try {
      writeHighStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readHighStateAsync() {
    return getHighStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighStateAsync(LocalizedText highState) {
    DataValue value = DataValue.valueOnly(new Variant(highState));
    return getHighStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getHighStateNode() throws UaException {
    try {
      return getHighStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getHighStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "HighState", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getLowState() throws UaException {
    TwoStateVariableTypeNode node = getLowStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setLowState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getLowStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readLowState() throws UaException {
    try {
      return readLowStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLowState(LocalizedText value) throws UaException {
    try {
      writeLowStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readLowStateAsync() {
    return getLowStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLowStateAsync(LocalizedText lowState) {
    DataValue value = DataValue.valueOnly(new Variant(lowState));
    return getLowStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getLowStateNode() throws UaException {
    try {
      return getLowStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getLowStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "LowState", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getLowLowState() throws UaException {
    TwoStateVariableTypeNode node = getLowLowStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setLowLowState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getLowLowStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readLowLowState() throws UaException {
    try {
      return readLowLowStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLowLowState(LocalizedText value) throws UaException {
    try {
      writeLowLowStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readLowLowStateAsync() {
    return getLowLowStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLowLowStateAsync(LocalizedText lowLowState) {
    DataValue value = DataValue.valueOnly(new Variant(lowLowState));
    return getLowLowStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getLowLowStateNode() throws UaException {
    try {
      return getLowLowStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getLowLowStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LowLowState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }
}
