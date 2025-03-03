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
import org.eclipse.milo.opcua.sdk.client.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ConditionTypeNode extends BaseEventTypeNode implements ConditionType {
  public ConditionTypeNode(
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
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionClassId(NodeId value) throws UaException {
    try {
      writeConditionClassIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
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
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionClassId",
            ExpandedNodeId.parse("ns=0;i=46"),
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
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionClassName(LocalizedText value) throws UaException {
    try {
      writeConditionClassNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
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
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionClassName",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId[] getConditionSubClassId() throws UaException {
    PropertyTypeNode node = getConditionSubClassIdNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionSubClassId(NodeId[] value) throws UaException {
    PropertyTypeNode node = getConditionSubClassIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readConditionSubClassId() throws UaException {
    try {
      return readConditionSubClassIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionSubClassId(NodeId[] value) throws UaException {
    try {
      writeConditionSubClassIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readConditionSubClassIdAsync() {
    return getConditionSubClassIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassIdAsync(NodeId[] conditionSubClassId) {
    DataValue value = DataValue.valueOnly(new Variant(conditionSubClassId));
    return getConditionSubClassIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionSubClassIdNode() throws UaException {
    try {
      return getConditionSubClassIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionSubClassIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionSubClassId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText[] getConditionSubClassName() throws UaException {
    PropertyTypeNode node = getConditionSubClassNameNode();
    return (LocalizedText[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionSubClassName(LocalizedText[] value) throws UaException {
    PropertyTypeNode node = getConditionSubClassNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText[] readConditionSubClassName() throws UaException {
    try {
      return readConditionSubClassNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionSubClassName(LocalizedText[] value) throws UaException {
    try {
      writeConditionSubClassNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText[]> readConditionSubClassNameAsync() {
    return getConditionSubClassNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassNameAsync(
      LocalizedText[] conditionSubClassName) {
    DataValue value = DataValue.valueOnly(new Variant(conditionSubClassName));
    return getConditionSubClassNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionSubClassNameNode() throws UaException {
    try {
      return getConditionSubClassNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionSubClassNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionSubClassName",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getConditionName() throws UaException {
    PropertyTypeNode node = getConditionNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionName(String value) throws UaException {
    PropertyTypeNode node = getConditionNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readConditionName() throws UaException {
    try {
      return readConditionNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConditionName(String value) throws UaException {
    try {
      writeConditionNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readConditionNameAsync() {
    return getConditionNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionNameAsync(String conditionName) {
    DataValue value = DataValue.valueOnly(new Variant(conditionName));
    return getConditionNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConditionNameNode() throws UaException {
    try {
      return getConditionNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConditionName",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getBranchId() throws UaException {
    PropertyTypeNode node = getBranchIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setBranchId(NodeId value) throws UaException {
    PropertyTypeNode node = getBranchIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readBranchId() throws UaException {
    try {
      return readBranchIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeBranchId(NodeId value) throws UaException {
    try {
      writeBranchIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readBranchIdAsync() {
    return getBranchIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeBranchIdAsync(NodeId branchId) {
    DataValue value = DataValue.valueOnly(new Variant(branchId));
    return getBranchIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getBranchIdNode() throws UaException {
    try {
      return getBranchIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getBranchIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "BranchId", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getRetain() throws UaException {
    PropertyTypeNode node = getRetainNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setRetain(Boolean value) throws UaException {
    PropertyTypeNode node = getRetainNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readRetain() throws UaException {
    try {
      return readRetainAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRetain(Boolean value) throws UaException {
    try {
      writeRetainAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readRetainAsync() {
    return getRetainNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRetainAsync(Boolean retain) {
    DataValue value = DataValue.valueOnly(new Variant(retain));
    return getRetainNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRetainNode() throws UaException {
    try {
      return getRetainNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRetainNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Retain", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getSupportsFilteredRetain() throws UaException {
    PropertyTypeNode node = getSupportsFilteredRetainNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSupportsFilteredRetain(Boolean value) throws UaException {
    PropertyTypeNode node = getSupportsFilteredRetainNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readSupportsFilteredRetain() throws UaException {
    try {
      return readSupportsFilteredRetainAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSupportsFilteredRetain(Boolean value) throws UaException {
    try {
      writeSupportsFilteredRetainAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readSupportsFilteredRetainAsync() {
    return getSupportsFilteredRetainNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportsFilteredRetainAsync(
      Boolean supportsFilteredRetain) {
    DataValue value = DataValue.valueOnly(new Variant(supportsFilteredRetain));
    return getSupportsFilteredRetainNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSupportsFilteredRetainNode() throws UaException {
    try {
      return getSupportsFilteredRetainNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSupportsFilteredRetainNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SupportsFilteredRetain",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getClientUserId() throws UaException {
    PropertyTypeNode node = getClientUserIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientUserId(String value) throws UaException {
    PropertyTypeNode node = getClientUserIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readClientUserId() throws UaException {
    try {
      return readClientUserIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeClientUserId(String value) throws UaException {
    try {
      writeClientUserIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readClientUserIdAsync() {
    return getClientUserIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeClientUserIdAsync(String clientUserId) {
    DataValue value = DataValue.valueOnly(new Variant(clientUserId));
    return getClientUserIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getClientUserIdNode() throws UaException {
    try {
      return getClientUserIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientUserIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ClientUserId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText getEnabledState() throws UaException {
    TwoStateVariableTypeNode node = getEnabledStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setEnabledState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getEnabledStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readEnabledState() throws UaException {
    try {
      return readEnabledStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEnabledState(LocalizedText value) throws UaException {
    try {
      writeEnabledStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readEnabledStateAsync() {
    return getEnabledStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEnabledStateAsync(LocalizedText enabledState) {
    DataValue value = DataValue.valueOnly(new Variant(enabledState));
    return getEnabledStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() throws UaException {
    try {
      return getEnabledStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getEnabledStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "EnabledState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public StatusCode getQuality() throws UaException {
    ConditionVariableTypeNode node = getQualityNode();
    return (StatusCode) node.getValue().getValue().getValue();
  }

  @Override
  public void setQuality(StatusCode value) throws UaException {
    ConditionVariableTypeNode node = getQualityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public StatusCode readQuality() throws UaException {
    try {
      return readQualityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeQuality(StatusCode value) throws UaException {
    try {
      writeQualityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends StatusCode> readQualityAsync() {
    return getQualityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (StatusCode) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeQualityAsync(StatusCode quality) {
    DataValue value = DataValue.valueOnly(new Variant(quality));
    return getQualityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ConditionVariableTypeNode getQualityNode() throws UaException {
    try {
      return getQualityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ConditionVariableTypeNode> getQualityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Quality", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (ConditionVariableTypeNode) node);
  }

  @Override
  public UShort getLastSeverity() throws UaException {
    ConditionVariableTypeNode node = getLastSeverityNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastSeverity(UShort value) throws UaException {
    ConditionVariableTypeNode node = getLastSeverityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readLastSeverity() throws UaException {
    try {
      return readLastSeverityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastSeverity(UShort value) throws UaException {
    try {
      writeLastSeverityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readLastSeverityAsync() {
    return getLastSeverityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastSeverityAsync(UShort lastSeverity) {
    DataValue value = DataValue.valueOnly(new Variant(lastSeverity));
    return getLastSeverityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ConditionVariableTypeNode getLastSeverityNode() throws UaException {
    try {
      return getLastSeverityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ConditionVariableTypeNode> getLastSeverityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastSeverity",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ConditionVariableTypeNode) node);
  }

  @Override
  public LocalizedText getComment() throws UaException {
    ConditionVariableTypeNode node = getCommentNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setComment(LocalizedText value) throws UaException {
    ConditionVariableTypeNode node = getCommentNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readComment() throws UaException {
    try {
      return readCommentAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeComment(LocalizedText value) throws UaException {
    try {
      writeCommentAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readCommentAsync() {
    return getCommentNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCommentAsync(LocalizedText comment) {
    DataValue value = DataValue.valueOnly(new Variant(comment));
    return getCommentNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ConditionVariableTypeNode getCommentNode() throws UaException {
    try {
      return getCommentNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ConditionVariableTypeNode> getCommentNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Comment", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (ConditionVariableTypeNode) node);
  }
}
