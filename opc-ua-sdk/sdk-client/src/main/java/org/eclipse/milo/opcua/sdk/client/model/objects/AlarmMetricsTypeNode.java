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
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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

public class AlarmMetricsTypeNode extends BaseObjectTypeNode implements AlarmMetricsType {
  public AlarmMetricsTypeNode(
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
  public UInteger getAlarmCount() throws UaException {
    BaseDataVariableTypeNode node = getAlarmCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setAlarmCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getAlarmCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readAlarmCount() throws UaException {
    try {
      return readAlarmCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAlarmCount(UInteger value) throws UaException {
    try {
      writeAlarmCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readAlarmCountAsync() {
    return getAlarmCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAlarmCountAsync(UInteger alarmCount) {
    DataValue value = DataValue.valueOnly(new Variant(alarmCount));
    return getAlarmCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getAlarmCountNode() throws UaException {
    try {
      return getAlarmCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getAlarmCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "AlarmCount", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public DateTime getStartTime() throws UaException {
    BaseDataVariableTypeNode node = getStartTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartTime(DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getStartTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readStartTime() throws UaException {
    try {
      return readStartTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStartTime(DateTime value) throws UaException {
    try {
      writeStartTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readStartTimeAsync() {
    return getStartTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeStartTimeAsync(DateTime startTime) {
    DataValue value = DataValue.valueOnly(new Variant(startTime));
    return getStartTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getStartTimeNode() throws UaException {
    try {
      return getStartTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getStartTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "StartTime", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Double getMaximumActiveState() throws UaException {
    BaseDataVariableTypeNode node = getMaximumActiveStateNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaximumActiveState(Double value) throws UaException {
    BaseDataVariableTypeNode node = getMaximumActiveStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMaximumActiveState() throws UaException {
    try {
      return readMaximumActiveStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaximumActiveState(Double value) throws UaException {
    try {
      writeMaximumActiveStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMaximumActiveStateAsync() {
    return getMaximumActiveStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumActiveStateAsync(Double maximumActiveState) {
    DataValue value = DataValue.valueOnly(new Variant(maximumActiveState));
    return getMaximumActiveStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getMaximumActiveStateNode() throws UaException {
    try {
      return getMaximumActiveStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getMaximumActiveStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaximumActiveState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Double getMaximumUnAck() throws UaException {
    BaseDataVariableTypeNode node = getMaximumUnAckNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaximumUnAck(Double value) throws UaException {
    BaseDataVariableTypeNode node = getMaximumUnAckNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMaximumUnAck() throws UaException {
    try {
      return readMaximumUnAckAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaximumUnAck(Double value) throws UaException {
    try {
      writeMaximumUnAckAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMaximumUnAckAsync() {
    return getMaximumUnAckNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumUnAckAsync(Double maximumUnAck) {
    DataValue value = DataValue.valueOnly(new Variant(maximumUnAck));
    return getMaximumUnAckNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getMaximumUnAckNode() throws UaException {
    try {
      return getMaximumUnAckNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getMaximumUnAckNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaximumUnAck",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Double getCurrentAlarmRate() throws UaException {
    AlarmRateVariableTypeNode node = getCurrentAlarmRateNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentAlarmRate(Double value) throws UaException {
    AlarmRateVariableTypeNode node = getCurrentAlarmRateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readCurrentAlarmRate() throws UaException {
    try {
      return readCurrentAlarmRateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCurrentAlarmRate(Double value) throws UaException {
    try {
      writeCurrentAlarmRateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readCurrentAlarmRateAsync() {
    return getCurrentAlarmRateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentAlarmRateAsync(Double currentAlarmRate) {
    DataValue value = DataValue.valueOnly(new Variant(currentAlarmRate));
    return getCurrentAlarmRateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public AlarmRateVariableTypeNode getCurrentAlarmRateNode() throws UaException {
    try {
      return getCurrentAlarmRateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AlarmRateVariableTypeNode> getCurrentAlarmRateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CurrentAlarmRate",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (AlarmRateVariableTypeNode) node);
  }

  @Override
  public Double getMaximumAlarmRate() throws UaException {
    AlarmRateVariableTypeNode node = getMaximumAlarmRateNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaximumAlarmRate(Double value) throws UaException {
    AlarmRateVariableTypeNode node = getMaximumAlarmRateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMaximumAlarmRate() throws UaException {
    try {
      return readMaximumAlarmRateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaximumAlarmRate(Double value) throws UaException {
    try {
      writeMaximumAlarmRateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMaximumAlarmRateAsync() {
    return getMaximumAlarmRateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumAlarmRateAsync(Double maximumAlarmRate) {
    DataValue value = DataValue.valueOnly(new Variant(maximumAlarmRate));
    return getMaximumAlarmRateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public AlarmRateVariableTypeNode getMaximumAlarmRateNode() throws UaException {
    try {
      return getMaximumAlarmRateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AlarmRateVariableTypeNode> getMaximumAlarmRateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaximumAlarmRate",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (AlarmRateVariableTypeNode) node);
  }

  @Override
  public UInteger getMaximumReAlarmCount() throws UaException {
    BaseDataVariableTypeNode node = getMaximumReAlarmCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaximumReAlarmCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getMaximumReAlarmCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaximumReAlarmCount() throws UaException {
    try {
      return readMaximumReAlarmCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaximumReAlarmCount(UInteger value) throws UaException {
    try {
      writeMaximumReAlarmCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaximumReAlarmCountAsync() {
    return getMaximumReAlarmCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaximumReAlarmCountAsync(UInteger maximumReAlarmCount) {
    DataValue value = DataValue.valueOnly(new Variant(maximumReAlarmCount));
    return getMaximumReAlarmCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getMaximumReAlarmCountNode() throws UaException {
    try {
      return getMaximumReAlarmCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getMaximumReAlarmCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaximumReAlarmCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Double getAverageAlarmRate() throws UaException {
    AlarmRateVariableTypeNode node = getAverageAlarmRateNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setAverageAlarmRate(Double value) throws UaException {
    AlarmRateVariableTypeNode node = getAverageAlarmRateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readAverageAlarmRate() throws UaException {
    try {
      return readAverageAlarmRateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAverageAlarmRate(Double value) throws UaException {
    try {
      writeAverageAlarmRateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readAverageAlarmRateAsync() {
    return getAverageAlarmRateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAverageAlarmRateAsync(Double averageAlarmRate) {
    DataValue value = DataValue.valueOnly(new Variant(averageAlarmRate));
    return getAverageAlarmRateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public AlarmRateVariableTypeNode getAverageAlarmRateNode() throws UaException {
    try {
      return getAverageAlarmRateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AlarmRateVariableTypeNode> getAverageAlarmRateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AverageAlarmRate",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (AlarmRateVariableTypeNode) node);
  }
}
