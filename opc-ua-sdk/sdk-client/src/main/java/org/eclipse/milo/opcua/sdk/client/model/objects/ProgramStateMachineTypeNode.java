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
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnostic2TypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ProgramStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ProgramStateMachineType {
  public ProgramStateMachineTypeNode(
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
  public Boolean getCreatable() throws UaException {
    PropertyTypeNode node = getCreatableNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreatable(Boolean value) throws UaException {
    PropertyTypeNode node = getCreatableNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readCreatable() throws UaException {
    try {
      return readCreatableAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCreatable(Boolean value) throws UaException {
    try {
      writeCreatableAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readCreatableAsync() {
    return getCreatableNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCreatableAsync(Boolean creatable) {
    DataValue value = DataValue.valueOnly(new Variant(creatable));
    return getCreatableNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCreatableNode() throws UaException {
    try {
      return getCreatableNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCreatableNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Creatable", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getDeletable() throws UaException {
    PropertyTypeNode node = getDeletableNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeletable(Boolean value) throws UaException {
    PropertyTypeNode node = getDeletableNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readDeletable() throws UaException {
    try {
      return readDeletableAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDeletable(Boolean value) throws UaException {
    try {
      writeDeletableAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readDeletableAsync() {
    return getDeletableNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDeletableAsync(Boolean deletable) {
    DataValue value = DataValue.valueOnly(new Variant(deletable));
    return getDeletableNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDeletableNode() throws UaException {
    try {
      return getDeletableNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeletableNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Deletable", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getAutoDelete() throws UaException {
    PropertyTypeNode node = getAutoDeleteNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAutoDelete(Boolean value) throws UaException {
    PropertyTypeNode node = getAutoDeleteNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readAutoDelete() throws UaException {
    try {
      return readAutoDeleteAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAutoDelete(Boolean value) throws UaException {
    try {
      writeAutoDeleteAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readAutoDeleteAsync() {
    return getAutoDeleteNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAutoDeleteAsync(Boolean autoDelete) {
    DataValue value = DataValue.valueOnly(new Variant(autoDelete));
    return getAutoDeleteNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAutoDeleteNode() throws UaException {
    try {
      return getAutoDeleteNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAutoDeleteNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "AutoDelete", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Integer getRecycleCount() throws UaException {
    PropertyTypeNode node = getRecycleCountNode();
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setRecycleCount(Integer value) throws UaException {
    PropertyTypeNode node = getRecycleCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Integer readRecycleCount() throws UaException {
    try {
      return readRecycleCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRecycleCount(Integer value) throws UaException {
    try {
      writeRecycleCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Integer> readRecycleCountAsync() {
    return getRecycleCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Integer) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRecycleCountAsync(Integer recycleCount) {
    DataValue value = DataValue.valueOnly(new Variant(recycleCount));
    return getRecycleCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRecycleCountNode() throws UaException {
    try {
      return getRecycleCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRecycleCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RecycleCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getInstanceCount() throws UaException {
    PropertyTypeNode node = getInstanceCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setInstanceCount(UInteger value) throws UaException {
    PropertyTypeNode node = getInstanceCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readInstanceCount() throws UaException {
    try {
      return readInstanceCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInstanceCount(UInteger value) throws UaException {
    try {
      writeInstanceCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readInstanceCountAsync() {
    return getInstanceCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeInstanceCountAsync(UInteger instanceCount) {
    DataValue value = DataValue.valueOnly(new Variant(instanceCount));
    return getInstanceCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getInstanceCountNode() throws UaException {
    try {
      return getInstanceCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInstanceCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "InstanceCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxInstanceCount() throws UaException {
    PropertyTypeNode node = getMaxInstanceCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxInstanceCount(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxInstanceCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxInstanceCount() throws UaException {
    try {
      return readMaxInstanceCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxInstanceCount(UInteger value) throws UaException {
    try {
      writeMaxInstanceCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxInstanceCountAsync() {
    return getMaxInstanceCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxInstanceCountAsync(UInteger maxInstanceCount) {
    DataValue value = DataValue.valueOnly(new Variant(maxInstanceCount));
    return getMaxInstanceCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxInstanceCountNode() throws UaException {
    try {
      return getMaxInstanceCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxInstanceCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxInstanceCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxRecycleCount() throws UaException {
    PropertyTypeNode node = getMaxRecycleCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxRecycleCount(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxRecycleCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxRecycleCount() throws UaException {
    try {
      return readMaxRecycleCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxRecycleCount(UInteger value) throws UaException {
    try {
      writeMaxRecycleCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxRecycleCountAsync() {
    return getMaxRecycleCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxRecycleCountAsync(UInteger maxRecycleCount) {
    DataValue value = DataValue.valueOnly(new Variant(maxRecycleCount));
    return getMaxRecycleCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxRecycleCountNode() throws UaException {
    try {
      return getMaxRecycleCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxRecycleCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxRecycleCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
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
  public ProgramDiagnostic2DataType getProgramDiagnostic() throws UaException {
    ProgramDiagnostic2TypeNode node = getProgramDiagnosticNode();
    return cast(node.getValue().getValue().getValue(), ProgramDiagnostic2DataType.class);
  }

  @Override
  public void setProgramDiagnostic(ProgramDiagnostic2DataType value) throws UaException {
    ProgramDiagnostic2TypeNode node = getProgramDiagnosticNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public ProgramDiagnostic2DataType readProgramDiagnostic() throws UaException {
    try {
      return readProgramDiagnosticAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeProgramDiagnostic(ProgramDiagnostic2DataType value) throws UaException {
    try {
      writeProgramDiagnosticAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ProgramDiagnostic2DataType> readProgramDiagnosticAsync() {
    return getProgramDiagnosticNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), ProgramDiagnostic2DataType.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeProgramDiagnosticAsync(
      ProgramDiagnostic2DataType programDiagnostic) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), programDiagnostic);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getProgramDiagnosticNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ProgramDiagnostic2TypeNode getProgramDiagnosticNode() throws UaException {
    try {
      return getProgramDiagnosticNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ProgramDiagnostic2TypeNode> getProgramDiagnosticNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ProgramDiagnostic",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ProgramDiagnostic2TypeNode) node);
  }

  @Override
  public BaseObjectTypeNode getFinalResultDataNode() throws UaException {
    try {
      return getFinalResultDataNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseObjectTypeNode> getFinalResultDataNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "FinalResultData",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseObjectTypeNode) node);
  }

  @Override
  public StateTypeNode getHaltedNode() throws UaException {
    try {
      return getHaltedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getHaltedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Halted", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getReadyNode() throws UaException {
    try {
      return getReadyNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getReadyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Ready", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getRunningNode() throws UaException {
    try {
      return getRunningNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getRunningNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Running", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public StateTypeNode getSuspendedNode() throws UaException {
    try {
      return getSuspendedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getSuspendedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Suspended", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (StateTypeNode) node);
  }

  @Override
  public TransitionTypeNode getHaltedToReadyNode() throws UaException {
    try {
      return getHaltedToReadyNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getHaltedToReadyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HaltedToReady",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getReadyToRunningNode() throws UaException {
    try {
      return getReadyToRunningNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadyToRunningNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadyToRunning",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getRunningToHaltedNode() throws UaException {
    try {
      return getRunningToHaltedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getRunningToHaltedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RunningToHalted",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getRunningToReadyNode() throws UaException {
    try {
      return getRunningToReadyNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getRunningToReadyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RunningToReady",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getRunningToSuspendedNode() throws UaException {
    try {
      return getRunningToSuspendedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getRunningToSuspendedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RunningToSuspended",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getSuspendedToRunningNode() throws UaException {
    try {
      return getSuspendedToRunningNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getSuspendedToRunningNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SuspendedToRunning",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getSuspendedToHaltedNode() throws UaException {
    try {
      return getSuspendedToHaltedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getSuspendedToHaltedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SuspendedToHalted",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getSuspendedToReadyNode() throws UaException {
    try {
      return getSuspendedToReadyNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getSuspendedToReadyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SuspendedToReady",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }

  @Override
  public TransitionTypeNode getReadyToHaltedNode() throws UaException {
    try {
      return getReadyToHaltedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadyToHaltedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReadyToHalted",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TransitionTypeNode) node);
  }
}
