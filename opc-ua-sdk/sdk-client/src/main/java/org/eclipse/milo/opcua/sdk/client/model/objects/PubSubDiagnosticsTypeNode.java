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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PubSubDiagnosticsCounterTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubDiagnosticsTypeNode extends BaseObjectTypeNode implements PubSubDiagnosticsType {
  public PubSubDiagnosticsTypeNode(
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
  public DiagnosticsLevel getDiagnosticsLevel() throws UaException {
    BaseDataVariableTypeNode node = getDiagnosticsLevelNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return DiagnosticsLevel.from((Integer) value);
    } else if (value instanceof DiagnosticsLevel) {
      return (DiagnosticsLevel) value;
    } else {
      return null;
    }
  }

  @Override
  public void setDiagnosticsLevel(DiagnosticsLevel value) throws UaException {
    BaseDataVariableTypeNode node = getDiagnosticsLevelNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DiagnosticsLevel readDiagnosticsLevel() throws UaException {
    try {
      return readDiagnosticsLevelAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDiagnosticsLevel(DiagnosticsLevel value) throws UaException {
    try {
      writeDiagnosticsLevelAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DiagnosticsLevel> readDiagnosticsLevelAsync() {
    return getDiagnosticsLevelNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return DiagnosticsLevel.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(
      DiagnosticsLevel diagnosticsLevel) {
    DataValue value = DataValue.valueOnly(new Variant(diagnosticsLevel));
    return getDiagnosticsLevelNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getDiagnosticsLevelNode() throws UaException {
    try {
      return getDiagnosticsLevelNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getDiagnosticsLevelNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DiagnosticsLevel",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getTotalInformation() throws UaException {
    PubSubDiagnosticsCounterTypeNode node = getTotalInformationNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setTotalInformation(UInteger value) throws UaException {
    PubSubDiagnosticsCounterTypeNode node = getTotalInformationNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readTotalInformation() throws UaException {
    try {
      return readTotalInformationAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTotalInformation(UInteger value) throws UaException {
    try {
      writeTotalInformationAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readTotalInformationAsync() {
    return getTotalInformationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTotalInformationAsync(UInteger totalInformation) {
    DataValue value = DataValue.valueOnly(new Variant(totalInformation));
    return getTotalInformationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalInformationNode() throws UaException {
    try {
      return getTotalInformationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsCounterTypeNode>
      getTotalInformationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TotalInformation",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (PubSubDiagnosticsCounterTypeNode) node);
  }

  @Override
  public UInteger getTotalError() throws UaException {
    PubSubDiagnosticsCounterTypeNode node = getTotalErrorNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setTotalError(UInteger value) throws UaException {
    PubSubDiagnosticsCounterTypeNode node = getTotalErrorNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readTotalError() throws UaException {
    try {
      return readTotalErrorAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTotalError(UInteger value) throws UaException {
    try {
      writeTotalErrorAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readTotalErrorAsync() {
    return getTotalErrorNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTotalErrorAsync(UInteger totalError) {
    DataValue value = DataValue.valueOnly(new Variant(totalError));
    return getTotalErrorNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalErrorNode() throws UaException {
    try {
      return getTotalErrorNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsCounterTypeNode> getTotalErrorNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "TotalError", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (PubSubDiagnosticsCounterTypeNode) node);
  }

  @Override
  public Boolean getSubError() throws UaException {
    BaseDataVariableTypeNode node = getSubErrorNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSubError(Boolean value) throws UaException {
    BaseDataVariableTypeNode node = getSubErrorNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readSubError() throws UaException {
    try {
      return readSubErrorAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSubError(Boolean value) throws UaException {
    try {
      writeSubErrorAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readSubErrorAsync() {
    return getSubErrorNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSubErrorAsync(Boolean subError) {
    DataValue value = DataValue.valueOnly(new Variant(subError));
    return getSubErrorNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSubErrorNode() throws UaException {
    try {
      return getSubErrorNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSubErrorNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "SubError", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public BaseObjectTypeNode getCountersNode() throws UaException {
    try {
      return getCountersNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseObjectTypeNode> getCountersNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Counters", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseObjectTypeNode) node);
  }

  @Override
  public BaseObjectTypeNode getLiveValuesNode() throws UaException {
    try {
      return getLiveValuesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseObjectTypeNode> getLiveValuesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "LiveValues", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseObjectTypeNode) node);
  }
}
