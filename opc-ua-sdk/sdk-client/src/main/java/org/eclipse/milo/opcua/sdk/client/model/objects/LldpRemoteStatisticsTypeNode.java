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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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

public class LldpRemoteStatisticsTypeNode extends BaseObjectTypeNode
    implements LldpRemoteStatisticsType {
  public LldpRemoteStatisticsTypeNode(
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
  public UInteger getLastChangeTime() throws UaException {
    BaseDataVariableTypeNode node = getLastChangeTimeNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastChangeTime(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getLastChangeTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readLastChangeTime() throws UaException {
    try {
      return readLastChangeTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastChangeTime(UInteger value) throws UaException {
    try {
      writeLastChangeTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readLastChangeTimeAsync() {
    return getLastChangeTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastChangeTimeAsync(UInteger lastChangeTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastChangeTime));
    return getLastChangeTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getLastChangeTimeNode() throws UaException {
    try {
      return getLastChangeTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getLastChangeTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastChangeTime",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getRemoteInserts() throws UaException {
    BaseDataVariableTypeNode node = getRemoteInsertsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteInserts(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteInsertsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readRemoteInserts() throws UaException {
    try {
      return readRemoteInsertsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteInserts(UInteger value) throws UaException {
    try {
      writeRemoteInsertsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readRemoteInsertsAsync() {
    return getRemoteInsertsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteInsertsAsync(UInteger remoteInserts) {
    DataValue value = DataValue.valueOnly(new Variant(remoteInserts));
    return getRemoteInsertsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteInsertsNode() throws UaException {
    try {
      return getRemoteInsertsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteInsertsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteInserts",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getRemoteDeletes() throws UaException {
    BaseDataVariableTypeNode node = getRemoteDeletesNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteDeletes(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteDeletesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readRemoteDeletes() throws UaException {
    try {
      return readRemoteDeletesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteDeletes(UInteger value) throws UaException {
    try {
      writeRemoteDeletesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readRemoteDeletesAsync() {
    return getRemoteDeletesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteDeletesAsync(UInteger remoteDeletes) {
    DataValue value = DataValue.valueOnly(new Variant(remoteDeletes));
    return getRemoteDeletesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDeletesNode() throws UaException {
    try {
      return getRemoteDeletesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteDeletesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteDeletes",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getRemoteDrops() throws UaException {
    BaseDataVariableTypeNode node = getRemoteDropsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteDrops(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteDropsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readRemoteDrops() throws UaException {
    try {
      return readRemoteDropsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteDrops(UInteger value) throws UaException {
    try {
      writeRemoteDropsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readRemoteDropsAsync() {
    return getRemoteDropsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteDropsAsync(UInteger remoteDrops) {
    DataValue value = DataValue.valueOnly(new Variant(remoteDrops));
    return getRemoteDropsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDropsNode() throws UaException {
    try {
      return getRemoteDropsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteDropsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteDrops",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getRemoteAgeouts() throws UaException {
    BaseDataVariableTypeNode node = getRemoteAgeoutsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteAgeouts(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteAgeoutsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readRemoteAgeouts() throws UaException {
    try {
      return readRemoteAgeoutsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRemoteAgeouts(UInteger value) throws UaException {
    try {
      writeRemoteAgeoutsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readRemoteAgeoutsAsync() {
    return getRemoteAgeoutsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRemoteAgeoutsAsync(UInteger remoteAgeouts) {
    DataValue value = DataValue.valueOnly(new Variant(remoteAgeouts));
    return getRemoteAgeoutsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteAgeoutsNode() throws UaException {
    try {
      return getRemoteAgeoutsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteAgeoutsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RemoteAgeouts",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
