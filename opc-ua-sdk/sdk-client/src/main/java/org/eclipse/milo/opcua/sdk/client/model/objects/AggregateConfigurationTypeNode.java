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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AggregateConfigurationTypeNode extends BaseObjectTypeNode
    implements AggregateConfigurationType {
  public AggregateConfigurationTypeNode(
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
  public Boolean getTreatUncertainAsBad() throws UaException {
    PropertyTypeNode node = getTreatUncertainAsBadNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setTreatUncertainAsBad(Boolean value) throws UaException {
    PropertyTypeNode node = getTreatUncertainAsBadNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readTreatUncertainAsBad() throws UaException {
    try {
      return readTreatUncertainAsBadAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTreatUncertainAsBad(Boolean value) throws UaException {
    try {
      writeTreatUncertainAsBadAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readTreatUncertainAsBadAsync() {
    return getTreatUncertainAsBadNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTreatUncertainAsBadAsync(Boolean treatUncertainAsBad) {
    DataValue value = DataValue.valueOnly(new Variant(treatUncertainAsBad));
    return getTreatUncertainAsBadNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getTreatUncertainAsBadNode() throws UaException {
    try {
      return getTreatUncertainAsBadNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTreatUncertainAsBadNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TreatUncertainAsBad",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UByte getPercentDataBad() throws UaException {
    PropertyTypeNode node = getPercentDataBadNode();
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setPercentDataBad(UByte value) throws UaException {
    PropertyTypeNode node = getPercentDataBadNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte readPercentDataBad() throws UaException {
    try {
      return readPercentDataBadAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePercentDataBad(UByte value) throws UaException {
    try {
      writePercentDataBadAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte> readPercentDataBadAsync() {
    return getPercentDataBadNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePercentDataBadAsync(UByte percentDataBad) {
    DataValue value = DataValue.valueOnly(new Variant(percentDataBad));
    return getPercentDataBadNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPercentDataBadNode() throws UaException {
    try {
      return getPercentDataBadNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPercentDataBadNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PercentDataBad",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UByte getPercentDataGood() throws UaException {
    PropertyTypeNode node = getPercentDataGoodNode();
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setPercentDataGood(UByte value) throws UaException {
    PropertyTypeNode node = getPercentDataGoodNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte readPercentDataGood() throws UaException {
    try {
      return readPercentDataGoodAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePercentDataGood(UByte value) throws UaException {
    try {
      writePercentDataGoodAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte> readPercentDataGoodAsync() {
    return getPercentDataGoodNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePercentDataGoodAsync(UByte percentDataGood) {
    DataValue value = DataValue.valueOnly(new Variant(percentDataGood));
    return getPercentDataGoodNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPercentDataGoodNode() throws UaException {
    try {
      return getPercentDataGoodNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPercentDataGoodNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PercentDataGood",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getUseSlopedExtrapolation() throws UaException {
    PropertyTypeNode node = getUseSlopedExtrapolationNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUseSlopedExtrapolation(Boolean value) throws UaException {
    PropertyTypeNode node = getUseSlopedExtrapolationNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readUseSlopedExtrapolation() throws UaException {
    try {
      return readUseSlopedExtrapolationAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUseSlopedExtrapolation(Boolean value) throws UaException {
    try {
      writeUseSlopedExtrapolationAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readUseSlopedExtrapolationAsync() {
    return getUseSlopedExtrapolationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUseSlopedExtrapolationAsync(
      Boolean useSlopedExtrapolation) {
    DataValue value = DataValue.valueOnly(new Variant(useSlopedExtrapolation));
    return getUseSlopedExtrapolationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUseSlopedExtrapolationNode() throws UaException {
    try {
      return getUseSlopedExtrapolationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUseSlopedExtrapolationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UseSlopedExtrapolation",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
