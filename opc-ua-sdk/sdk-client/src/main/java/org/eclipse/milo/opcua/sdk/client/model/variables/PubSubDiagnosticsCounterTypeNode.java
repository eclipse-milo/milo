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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubDiagnosticsCounterTypeNode extends BaseDataVariableTypeNode
    implements PubSubDiagnosticsCounterType {
  public PubSubDiagnosticsCounterTypeNode(
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
  public Boolean getActive() throws UaException {
    PropertyTypeNode node = getActiveNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setActive(Boolean value) throws UaException {
    PropertyTypeNode node = getActiveNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readActive() throws UaException {
    try {
      return readActiveAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeActive(Boolean value) throws UaException {
    try {
      writeActiveAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readActiveAsync() {
    return getActiveNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeActiveAsync(Boolean active) {
    DataValue value = DataValue.valueOnly(new Variant(active));
    return getActiveNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getActiveNode() throws UaException {
    try {
      return getActiveNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActiveNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Active", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public PubSubDiagnosticsCounterClassification getClassification() throws UaException {
    PropertyTypeNode node = getClassificationNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return PubSubDiagnosticsCounterClassification.from((Integer) value);
    } else if (value instanceof PubSubDiagnosticsCounterClassification) {
      return (PubSubDiagnosticsCounterClassification) value;
    } else {
      return null;
    }
  }

  @Override
  public void setClassification(PubSubDiagnosticsCounterClassification value) throws UaException {
    PropertyTypeNode node = getClassificationNode();
    node.setValue(new Variant(value));
  }

  @Override
  public PubSubDiagnosticsCounterClassification readClassification() throws UaException {
    try {
      return readClassificationAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeClassification(PubSubDiagnosticsCounterClassification value) throws UaException {
    try {
      writeClassificationAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsCounterClassification>
      readClassificationAsync() {
    return getClassificationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return PubSubDiagnosticsCounterClassification.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClassificationAsync(
      PubSubDiagnosticsCounterClassification classification) {
    DataValue value = DataValue.valueOnly(new Variant(classification));
    return getClassificationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getClassificationNode() throws UaException {
    try {
      return getClassificationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClassificationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Classification",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DiagnosticsLevel getDiagnosticsLevel() throws UaException {
    PropertyTypeNode node = getDiagnosticsLevelNode();
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
    PropertyTypeNode node = getDiagnosticsLevelNode();
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
  public PropertyTypeNode getDiagnosticsLevelNode() throws UaException {
    try {
      return getDiagnosticsLevelNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDiagnosticsLevelNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DiagnosticsLevel",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getTimeFirstChange() throws UaException {
    PropertyTypeNode node = getTimeFirstChangeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setTimeFirstChange(DateTime value) throws UaException {
    PropertyTypeNode node = getTimeFirstChangeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readTimeFirstChange() throws UaException {
    try {
      return readTimeFirstChangeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTimeFirstChange(DateTime value) throws UaException {
    try {
      writeTimeFirstChangeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readTimeFirstChangeAsync() {
    return getTimeFirstChangeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTimeFirstChangeAsync(DateTime timeFirstChange) {
    DataValue value = DataValue.valueOnly(new Variant(timeFirstChange));
    return getTimeFirstChangeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getTimeFirstChangeNode() throws UaException {
    try {
      return getTimeFirstChangeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTimeFirstChangeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TimeFirstChange",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
