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

public class LimitAlarmTypeNode extends AlarmConditionTypeNode implements LimitAlarmType {
  public LimitAlarmTypeNode(
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
  public Double getHighHighLimit() throws UaException {
    PropertyTypeNode node = getHighHighLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighHighLimit(Double value) throws UaException {
    PropertyTypeNode node = getHighHighLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readHighHighLimit() throws UaException {
    try {
      return readHighHighLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighHighLimit(Double value) throws UaException {
    try {
      writeHighHighLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readHighHighLimitAsync() {
    return getHighHighLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighHighLimitAsync(Double highHighLimit) {
    DataValue value = DataValue.valueOnly(new Variant(highHighLimit));
    return getHighHighLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHighHighLimitNode() throws UaException {
    try {
      return getHighHighLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighHighLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighHighLimit",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getHighLimit() throws UaException {
    PropertyTypeNode node = getHighLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighLimit(Double value) throws UaException {
    PropertyTypeNode node = getHighLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readHighLimit() throws UaException {
    try {
      return readHighLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighLimit(Double value) throws UaException {
    try {
      writeHighLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readHighLimitAsync() {
    return getHighLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighLimitAsync(Double highLimit) {
    DataValue value = DataValue.valueOnly(new Variant(highLimit));
    return getHighLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHighLimitNode() throws UaException {
    try {
      return getHighLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "HighLimit", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getLowLimit() throws UaException {
    PropertyTypeNode node = getLowLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setLowLimit(Double value) throws UaException {
    PropertyTypeNode node = getLowLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readLowLimit() throws UaException {
    try {
      return readLowLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLowLimit(Double value) throws UaException {
    try {
      writeLowLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readLowLimitAsync() {
    return getLowLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLowLimitAsync(Double lowLimit) {
    DataValue value = DataValue.valueOnly(new Variant(lowLimit));
    return getLowLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLowLimitNode() throws UaException {
    try {
      return getLowLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLowLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "LowLimit", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getLowLowLimit() throws UaException {
    PropertyTypeNode node = getLowLowLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setLowLowLimit(Double value) throws UaException {
    PropertyTypeNode node = getLowLowLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readLowLowLimit() throws UaException {
    try {
      return readLowLowLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLowLowLimit(Double value) throws UaException {
    try {
      writeLowLowLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readLowLowLimitAsync() {
    return getLowLowLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLowLowLimitAsync(Double lowLowLimit) {
    DataValue value = DataValue.valueOnly(new Variant(lowLowLimit));
    return getLowLowLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLowLowLimitNode() throws UaException {
    try {
      return getLowLowLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLowLowLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LowLowLimit",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getBaseHighHighLimit() throws UaException {
    PropertyTypeNode node = getBaseHighHighLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setBaseHighHighLimit(Double value) throws UaException {
    PropertyTypeNode node = getBaseHighHighLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readBaseHighHighLimit() throws UaException {
    try {
      return readBaseHighHighLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeBaseHighHighLimit(Double value) throws UaException {
    try {
      writeBaseHighHighLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readBaseHighHighLimitAsync() {
    return getBaseHighHighLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseHighHighLimitAsync(Double baseHighHighLimit) {
    DataValue value = DataValue.valueOnly(new Variant(baseHighHighLimit));
    return getBaseHighHighLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getBaseHighHighLimitNode() throws UaException {
    try {
      return getBaseHighHighLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getBaseHighHighLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "BaseHighHighLimit",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getBaseHighLimit() throws UaException {
    PropertyTypeNode node = getBaseHighLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setBaseHighLimit(Double value) throws UaException {
    PropertyTypeNode node = getBaseHighLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readBaseHighLimit() throws UaException {
    try {
      return readBaseHighLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeBaseHighLimit(Double value) throws UaException {
    try {
      writeBaseHighLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readBaseHighLimitAsync() {
    return getBaseHighLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseHighLimitAsync(Double baseHighLimit) {
    DataValue value = DataValue.valueOnly(new Variant(baseHighLimit));
    return getBaseHighLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getBaseHighLimitNode() throws UaException {
    try {
      return getBaseHighLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getBaseHighLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "BaseHighLimit",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getBaseLowLimit() throws UaException {
    PropertyTypeNode node = getBaseLowLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setBaseLowLimit(Double value) throws UaException {
    PropertyTypeNode node = getBaseLowLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readBaseLowLimit() throws UaException {
    try {
      return readBaseLowLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeBaseLowLimit(Double value) throws UaException {
    try {
      writeBaseLowLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readBaseLowLimitAsync() {
    return getBaseLowLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseLowLimitAsync(Double baseLowLimit) {
    DataValue value = DataValue.valueOnly(new Variant(baseLowLimit));
    return getBaseLowLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getBaseLowLimitNode() throws UaException {
    try {
      return getBaseLowLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getBaseLowLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "BaseLowLimit",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getBaseLowLowLimit() throws UaException {
    PropertyTypeNode node = getBaseLowLowLimitNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setBaseLowLowLimit(Double value) throws UaException {
    PropertyTypeNode node = getBaseLowLowLimitNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readBaseLowLowLimit() throws UaException {
    try {
      return readBaseLowLowLimitAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeBaseLowLowLimit(Double value) throws UaException {
    try {
      writeBaseLowLowLimitAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readBaseLowLowLimitAsync() {
    return getBaseLowLowLimitNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseLowLowLimitAsync(Double baseLowLowLimit) {
    DataValue value = DataValue.valueOnly(new Variant(baseLowLowLimit));
    return getBaseLowLowLimitNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getBaseLowLowLimitNode() throws UaException {
    try {
      return getBaseLowLowLimitNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getBaseLowLowLimitNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "BaseLowLowLimit",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getSeverityHighHigh() throws UaException {
    PropertyTypeNode node = getSeverityHighHighNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setSeverityHighHigh(UShort value) throws UaException {
    PropertyTypeNode node = getSeverityHighHighNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readSeverityHighHigh() throws UaException {
    try {
      return readSeverityHighHighAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSeverityHighHigh(UShort value) throws UaException {
    try {
      writeSeverityHighHighAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readSeverityHighHighAsync() {
    return getSeverityHighHighNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSeverityHighHighAsync(UShort severityHighHigh) {
    DataValue value = DataValue.valueOnly(new Variant(severityHighHigh));
    return getSeverityHighHighNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSeverityHighHighNode() throws UaException {
    try {
      return getSeverityHighHighNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSeverityHighHighNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SeverityHighHigh",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getSeverityHigh() throws UaException {
    PropertyTypeNode node = getSeverityHighNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setSeverityHigh(UShort value) throws UaException {
    PropertyTypeNode node = getSeverityHighNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readSeverityHigh() throws UaException {
    try {
      return readSeverityHighAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSeverityHigh(UShort value) throws UaException {
    try {
      writeSeverityHighAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readSeverityHighAsync() {
    return getSeverityHighNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSeverityHighAsync(UShort severityHigh) {
    DataValue value = DataValue.valueOnly(new Variant(severityHigh));
    return getSeverityHighNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSeverityHighNode() throws UaException {
    try {
      return getSeverityHighNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSeverityHighNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SeverityHigh",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getSeverityLow() throws UaException {
    PropertyTypeNode node = getSeverityLowNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setSeverityLow(UShort value) throws UaException {
    PropertyTypeNode node = getSeverityLowNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readSeverityLow() throws UaException {
    try {
      return readSeverityLowAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSeverityLow(UShort value) throws UaException {
    try {
      writeSeverityLowAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readSeverityLowAsync() {
    return getSeverityLowNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSeverityLowAsync(UShort severityLow) {
    DataValue value = DataValue.valueOnly(new Variant(severityLow));
    return getSeverityLowNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSeverityLowNode() throws UaException {
    try {
      return getSeverityLowNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSeverityLowNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SeverityLow",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getSeverityLowLow() throws UaException {
    PropertyTypeNode node = getSeverityLowLowNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setSeverityLowLow(UShort value) throws UaException {
    PropertyTypeNode node = getSeverityLowLowNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readSeverityLowLow() throws UaException {
    try {
      return readSeverityLowLowAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSeverityLowLow(UShort value) throws UaException {
    try {
      writeSeverityLowLowAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readSeverityLowLowAsync() {
    return getSeverityLowLowNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSeverityLowLowAsync(UShort severityLowLow) {
    DataValue value = DataValue.valueOnly(new Variant(severityLowLow));
    return getSeverityLowLowNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSeverityLowLowNode() throws UaException {
    try {
      return getSeverityLowLowNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSeverityLowLowNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SeverityLowLow",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getHighHighDeadband() throws UaException {
    PropertyTypeNode node = getHighHighDeadbandNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighHighDeadband(Double value) throws UaException {
    PropertyTypeNode node = getHighHighDeadbandNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readHighHighDeadband() throws UaException {
    try {
      return readHighHighDeadbandAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighHighDeadband(Double value) throws UaException {
    try {
      writeHighHighDeadbandAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readHighHighDeadbandAsync() {
    return getHighHighDeadbandNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighHighDeadbandAsync(Double highHighDeadband) {
    DataValue value = DataValue.valueOnly(new Variant(highHighDeadband));
    return getHighHighDeadbandNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHighHighDeadbandNode() throws UaException {
    try {
      return getHighHighDeadbandNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighHighDeadbandNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighHighDeadband",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getHighDeadband() throws UaException {
    PropertyTypeNode node = getHighDeadbandNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighDeadband(Double value) throws UaException {
    PropertyTypeNode node = getHighDeadbandNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readHighDeadband() throws UaException {
    try {
      return readHighDeadbandAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighDeadband(Double value) throws UaException {
    try {
      writeHighDeadbandAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readHighDeadbandAsync() {
    return getHighDeadbandNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighDeadbandAsync(Double highDeadband) {
    DataValue value = DataValue.valueOnly(new Variant(highDeadband));
    return getHighDeadbandNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHighDeadbandNode() throws UaException {
    try {
      return getHighDeadbandNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighDeadbandNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighDeadband",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getLowDeadband() throws UaException {
    PropertyTypeNode node = getLowDeadbandNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setLowDeadband(Double value) throws UaException {
    PropertyTypeNode node = getLowDeadbandNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readLowDeadband() throws UaException {
    try {
      return readLowDeadbandAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLowDeadband(Double value) throws UaException {
    try {
      writeLowDeadbandAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readLowDeadbandAsync() {
    return getLowDeadbandNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLowDeadbandAsync(Double lowDeadband) {
    DataValue value = DataValue.valueOnly(new Variant(lowDeadband));
    return getLowDeadbandNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLowDeadbandNode() throws UaException {
    try {
      return getLowDeadbandNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLowDeadbandNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LowDeadband",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getLowLowDeadband() throws UaException {
    PropertyTypeNode node = getLowLowDeadbandNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setLowLowDeadband(Double value) throws UaException {
    PropertyTypeNode node = getLowLowDeadbandNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readLowLowDeadband() throws UaException {
    try {
      return readLowLowDeadbandAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLowLowDeadband(Double value) throws UaException {
    try {
      writeLowLowDeadbandAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readLowLowDeadbandAsync() {
    return getLowLowDeadbandNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLowLowDeadbandAsync(Double lowLowDeadband) {
    DataValue value = DataValue.valueOnly(new Variant(lowLowDeadband));
    return getLowLowDeadbandNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLowLowDeadbandNode() throws UaException {
    try {
      return getLowLowDeadbandNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLowLowDeadbandNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LowLowDeadband",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
