/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class SamplingIntervalDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SamplingIntervalDiagnosticsType {
  public SamplingIntervalDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public Double getSamplingInterval() throws UaException {
    BaseDataVariableTypeNode node = getSamplingIntervalNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setSamplingInterval(Double value) throws UaException {
    BaseDataVariableTypeNode node = getSamplingIntervalNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readSamplingInterval() throws UaException {
    try {
      return readSamplingIntervalAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSamplingInterval(Double value) throws UaException {
    try {
      writeSamplingIntervalAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readSamplingIntervalAsync() {
    return getSamplingIntervalNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingIntervalAsync(Double samplingInterval) {
    DataValue value = DataValue.valueOnly(new Variant(samplingInterval));
    return getSamplingIntervalNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSamplingIntervalNode() throws UaException {
    try {
      return getSamplingIntervalNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSamplingIntervalNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SamplingInterval",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getSampledMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getSampledMonitoredItemsCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSampledMonitoredItemsCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSampledMonitoredItemsCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readSampledMonitoredItemsCount() throws UaException {
    try {
      return readSampledMonitoredItemsCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSampledMonitoredItemsCount(UInteger value) throws UaException {
    try {
      writeSampledMonitoredItemsCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readSampledMonitoredItemsCountAsync() {
    return getSampledMonitoredItemsCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSampledMonitoredItemsCountAsync(
      UInteger sampledMonitoredItemsCount) {
    DataValue value = DataValue.valueOnly(new Variant(sampledMonitoredItemsCount));
    return getSampledMonitoredItemsCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSampledMonitoredItemsCountNode() throws UaException {
    try {
      return getSampledMonitoredItemsCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSampledMonitoredItemsCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SampledMonitoredItemsCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getMaxSampledMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getMaxSampledMonitoredItemsCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSampledMonitoredItemsCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getMaxSampledMonitoredItemsCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxSampledMonitoredItemsCount() throws UaException {
    try {
      return readMaxSampledMonitoredItemsCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxSampledMonitoredItemsCount(UInteger value) throws UaException {
    try {
      writeMaxSampledMonitoredItemsCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxSampledMonitoredItemsCountAsync() {
    return getMaxSampledMonitoredItemsCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxSampledMonitoredItemsCountAsync(
      UInteger maxSampledMonitoredItemsCount) {
    DataValue value = DataValue.valueOnly(new Variant(maxSampledMonitoredItemsCount));
    return getMaxSampledMonitoredItemsCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getMaxSampledMonitoredItemsCountNode() throws UaException {
    try {
      return getMaxSampledMonitoredItemsCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getMaxSampledMonitoredItemsCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxSampledMonitoredItemsCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getDisabledMonitoredItemsSamplingCount() throws UaException {
    BaseDataVariableTypeNode node = getDisabledMonitoredItemsSamplingCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDisabledMonitoredItemsSamplingCount(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getDisabledMonitoredItemsSamplingCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readDisabledMonitoredItemsSamplingCount() throws UaException {
    try {
      return readDisabledMonitoredItemsSamplingCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDisabledMonitoredItemsSamplingCount(UInteger value) throws UaException {
    try {
      writeDisabledMonitoredItemsSamplingCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readDisabledMonitoredItemsSamplingCountAsync() {
    return getDisabledMonitoredItemsSamplingCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDisabledMonitoredItemsSamplingCountAsync(
      UInteger disabledMonitoredItemsSamplingCount) {
    DataValue value = DataValue.valueOnly(new Variant(disabledMonitoredItemsSamplingCount));
    return getDisabledMonitoredItemsSamplingCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getDisabledMonitoredItemsSamplingCountNode() throws UaException {
    try {
      return getDisabledMonitoredItemsSamplingCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getDisabledMonitoredItemsSamplingCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DisabledMonitoredItemsSamplingCount",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
