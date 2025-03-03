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
import org.eclipse.milo.opcua.sdk.client.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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

public class AlarmConditionTypeNode extends AcknowledgeableConditionTypeNode
    implements AlarmConditionType {
  public AlarmConditionTypeNode(
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
  public NodeId getInputNode() throws UaException {
    PropertyTypeNode node = getInputNodeNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setInputNode(NodeId value) throws UaException {
    PropertyTypeNode node = getInputNodeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readInputNode() throws UaException {
    try {
      return readInputNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInputNode(NodeId value) throws UaException {
    try {
      writeInputNodeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readInputNodeAsync() {
    return getInputNodeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeInputNodeAsync(NodeId inputNode) {
    DataValue value = DataValue.valueOnly(new Variant(inputNode));
    return getInputNodeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getInputNodeNode() throws UaException {
    try {
      return getInputNodeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInputNodeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "InputNode", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getSuppressedOrShelved() throws UaException {
    PropertyTypeNode node = getSuppressedOrShelvedNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSuppressedOrShelved(Boolean value) throws UaException {
    PropertyTypeNode node = getSuppressedOrShelvedNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readSuppressedOrShelved() throws UaException {
    try {
      return readSuppressedOrShelvedAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSuppressedOrShelved(Boolean value) throws UaException {
    try {
      writeSuppressedOrShelvedAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readSuppressedOrShelvedAsync() {
    return getSuppressedOrShelvedNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSuppressedOrShelvedAsync(Boolean suppressedOrShelved) {
    DataValue value = DataValue.valueOnly(new Variant(suppressedOrShelved));
    return getSuppressedOrShelvedNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSuppressedOrShelvedNode() throws UaException {
    try {
      return getSuppressedOrShelvedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSuppressedOrShelvedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SuppressedOrShelved",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMaxTimeShelved() throws UaException {
    PropertyTypeNode node = getMaxTimeShelvedNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxTimeShelved(Double value) throws UaException {
    PropertyTypeNode node = getMaxTimeShelvedNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMaxTimeShelved() throws UaException {
    try {
      return readMaxTimeShelvedAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxTimeShelved(Double value) throws UaException {
    try {
      writeMaxTimeShelvedAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMaxTimeShelvedAsync() {
    return getMaxTimeShelvedNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeShelvedAsync(Double maxTimeShelved) {
    DataValue value = DataValue.valueOnly(new Variant(maxTimeShelved));
    return getMaxTimeShelvedNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxTimeShelvedNode() throws UaException {
    try {
      return getMaxTimeShelvedNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxTimeShelvedNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxTimeShelved",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getAudibleEnabled() throws UaException {
    PropertyTypeNode node = getAudibleEnabledNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAudibleEnabled(Boolean value) throws UaException {
    PropertyTypeNode node = getAudibleEnabledNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readAudibleEnabled() throws UaException {
    try {
      return readAudibleEnabledAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAudibleEnabled(Boolean value) throws UaException {
    try {
      writeAudibleEnabledAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readAudibleEnabledAsync() {
    return getAudibleEnabledNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAudibleEnabledAsync(Boolean audibleEnabled) {
    DataValue value = DataValue.valueOnly(new Variant(audibleEnabled));
    return getAudibleEnabledNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAudibleEnabledNode() throws UaException {
    try {
      return getAudibleEnabledNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAudibleEnabledNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AudibleEnabled",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getOnDelay() throws UaException {
    PropertyTypeNode node = getOnDelayNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setOnDelay(Double value) throws UaException {
    PropertyTypeNode node = getOnDelayNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readOnDelay() throws UaException {
    try {
      return readOnDelayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOnDelay(Double value) throws UaException {
    try {
      writeOnDelayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readOnDelayAsync() {
    return getOnDelayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeOnDelayAsync(Double onDelay) {
    DataValue value = DataValue.valueOnly(new Variant(onDelay));
    return getOnDelayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getOnDelayNode() throws UaException {
    try {
      return getOnDelayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOnDelayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "OnDelay", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getOffDelay() throws UaException {
    PropertyTypeNode node = getOffDelayNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setOffDelay(Double value) throws UaException {
    PropertyTypeNode node = getOffDelayNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readOffDelay() throws UaException {
    try {
      return readOffDelayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOffDelay(Double value) throws UaException {
    try {
      writeOffDelayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readOffDelayAsync() {
    return getOffDelayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeOffDelayAsync(Double offDelay) {
    DataValue value = DataValue.valueOnly(new Variant(offDelay));
    return getOffDelayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getOffDelayNode() throws UaException {
    try {
      return getOffDelayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOffDelayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "OffDelay", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getReAlarmTime() throws UaException {
    PropertyTypeNode node = getReAlarmTimeNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setReAlarmTime(Double value) throws UaException {
    PropertyTypeNode node = getReAlarmTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readReAlarmTime() throws UaException {
    try {
      return readReAlarmTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeReAlarmTime(Double value) throws UaException {
    try {
      writeReAlarmTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readReAlarmTimeAsync() {
    return getReAlarmTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeReAlarmTimeAsync(Double reAlarmTime) {
    DataValue value = DataValue.valueOnly(new Variant(reAlarmTime));
    return getReAlarmTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getReAlarmTimeNode() throws UaException {
    try {
      return getReAlarmTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReAlarmTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReAlarmTime",
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
  public LocalizedText getSuppressedState() throws UaException {
    TwoStateVariableTypeNode node = getSuppressedStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setSuppressedState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getSuppressedStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readSuppressedState() throws UaException {
    try {
      return readSuppressedStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSuppressedState(LocalizedText value) throws UaException {
    try {
      writeSuppressedStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readSuppressedStateAsync() {
    return getSuppressedStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSuppressedStateAsync(LocalizedText suppressedState) {
    DataValue value = DataValue.valueOnly(new Variant(suppressedState));
    return getSuppressedStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getSuppressedStateNode() throws UaException {
    try {
      return getSuppressedStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getSuppressedStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SuppressedState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getOutOfServiceState() throws UaException {
    TwoStateVariableTypeNode node = getOutOfServiceStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setOutOfServiceState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getOutOfServiceStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readOutOfServiceState() throws UaException {
    try {
      return readOutOfServiceStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOutOfServiceState(LocalizedText value) throws UaException {
    try {
      writeOutOfServiceStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readOutOfServiceStateAsync() {
    return getOutOfServiceStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeOutOfServiceStateAsync(
      LocalizedText outOfServiceState) {
    DataValue value = DataValue.valueOnly(new Variant(outOfServiceState));
    return getOutOfServiceStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getOutOfServiceStateNode() throws UaException {
    try {
      return getOutOfServiceStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getOutOfServiceStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "OutOfServiceState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public ShelvedStateMachineTypeNode getShelvingStateNode() throws UaException {
    try {
      return getShelvingStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ShelvedStateMachineTypeNode> getShelvingStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ShelvingState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ShelvedStateMachineTypeNode) node);
  }

  @Override
  public ByteString getAudibleSound() throws UaException {
    AudioVariableTypeNode node = getAudibleSoundNode();
    return (ByteString) node.getValue().getValue().getValue();
  }

  @Override
  public void setAudibleSound(ByteString value) throws UaException {
    AudioVariableTypeNode node = getAudibleSoundNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ByteString readAudibleSound() throws UaException {
    try {
      return readAudibleSoundAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAudibleSound(ByteString value) throws UaException {
    try {
      writeAudibleSoundAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ByteString> readAudibleSoundAsync() {
    return getAudibleSoundNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ByteString) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAudibleSoundAsync(ByteString audibleSound) {
    DataValue value = DataValue.valueOnly(new Variant(audibleSound));
    return getAudibleSoundNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public AudioVariableTypeNode getAudibleSoundNode() throws UaException {
    try {
      return getAudibleSoundNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AudioVariableTypeNode> getAudibleSoundNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AudibleSound",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (AudioVariableTypeNode) node);
  }

  @Override
  public LocalizedText getSilenceState() throws UaException {
    TwoStateVariableTypeNode node = getSilenceStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setSilenceState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getSilenceStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readSilenceState() throws UaException {
    try {
      return readSilenceStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSilenceState(LocalizedText value) throws UaException {
    try {
      writeSilenceStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readSilenceStateAsync() {
    return getSilenceStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSilenceStateAsync(LocalizedText silenceState) {
    DataValue value = DataValue.valueOnly(new Variant(silenceState));
    return getSilenceStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getSilenceStateNode() throws UaException {
    try {
      return getSilenceStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getSilenceStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SilenceState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public Boolean getFirstInGroupFlag() throws UaException {
    BaseDataVariableTypeNode node = getFirstInGroupFlagNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setFirstInGroupFlag(Boolean value) throws UaException {
    BaseDataVariableTypeNode node = getFirstInGroupFlagNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readFirstInGroupFlag() throws UaException {
    try {
      return readFirstInGroupFlagAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeFirstInGroupFlag(Boolean value) throws UaException {
    try {
      writeFirstInGroupFlagAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readFirstInGroupFlagAsync() {
    return getFirstInGroupFlagNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeFirstInGroupFlagAsync(Boolean firstInGroupFlag) {
    DataValue value = DataValue.valueOnly(new Variant(firstInGroupFlag));
    return getFirstInGroupFlagNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getFirstInGroupFlagNode() throws UaException {
    try {
      return getFirstInGroupFlagNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getFirstInGroupFlagNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "FirstInGroupFlag",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public AlarmGroupTypeNode getFirstInGroupNode() throws UaException {
    try {
      return getFirstInGroupNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AlarmGroupTypeNode> getFirstInGroupNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "FirstInGroup",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (AlarmGroupTypeNode) node);
  }

  @Override
  public LocalizedText getLatchedState() throws UaException {
    TwoStateVariableTypeNode node = getLatchedStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setLatchedState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getLatchedStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readLatchedState() throws UaException {
    try {
      return readLatchedStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLatchedState(LocalizedText value) throws UaException {
    try {
      writeLatchedStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readLatchedStateAsync() {
    return getLatchedStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLatchedStateAsync(LocalizedText latchedState) {
    DataValue value = DataValue.valueOnly(new Variant(latchedState));
    return getLatchedStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getLatchedStateNode() throws UaException {
    try {
      return getLatchedStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getLatchedStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LatchedState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public Short getReAlarmRepeatCount() throws UaException {
    BaseDataVariableTypeNode node = getReAlarmRepeatCountNode();
    return (Short) node.getValue().getValue().getValue();
  }

  @Override
  public void setReAlarmRepeatCount(Short value) throws UaException {
    BaseDataVariableTypeNode node = getReAlarmRepeatCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Short readReAlarmRepeatCount() throws UaException {
    try {
      return readReAlarmRepeatCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeReAlarmRepeatCount(Short value) throws UaException {
    try {
      writeReAlarmRepeatCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Short> readReAlarmRepeatCountAsync() {
    return getReAlarmRepeatCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Short) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeReAlarmRepeatCountAsync(Short reAlarmRepeatCount) {
    DataValue value = DataValue.valueOnly(new Variant(reAlarmRepeatCount));
    return getReAlarmRepeatCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getReAlarmRepeatCountNode() throws UaException {
    try {
      return getReAlarmRepeatCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getReAlarmRepeatCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ReAlarmRepeatCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
