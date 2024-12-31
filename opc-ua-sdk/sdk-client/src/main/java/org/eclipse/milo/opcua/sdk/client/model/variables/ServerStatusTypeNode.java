/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ServerStatusTypeNode extends BaseDataVariableTypeNode implements ServerStatusType {
  public ServerStatusTypeNode(
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
  public DateTime getCurrentTime() throws UaException {
    BaseDataVariableTypeNode node = getCurrentTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentTime(DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getCurrentTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readCurrentTime() throws UaException {
    try {
      return readCurrentTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCurrentTime(DateTime value) throws UaException {
    try {
      writeCurrentTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readCurrentTimeAsync() {
    return getCurrentTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentTimeAsync(DateTime currentTime) {
    DataValue value = DataValue.valueOnly(new Variant(currentTime));
    return getCurrentTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentTimeNode() throws UaException {
    try {
      return getCurrentTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getCurrentTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CurrentTime",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public ServerState getState() throws UaException {
    BaseDataVariableTypeNode node = getStateNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return ServerState.from((Integer) value);
    } else if (value instanceof ServerState) {
      return (ServerState) value;
    } else {
      return null;
    }
  }

  @Override
  public void setState(ServerState value) throws UaException {
    BaseDataVariableTypeNode node = getStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ServerState readState() throws UaException {
    try {
      return readStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeState(ServerState value) throws UaException {
    try {
      writeStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ServerState> readStateAsync() {
    return getStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return ServerState.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStateAsync(ServerState state) {
    DataValue value = DataValue.valueOnly(new Variant(state));
    return getStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getStateNode() throws UaException {
    try {
      return getStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "State", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public BuildInfo getBuildInfo() throws UaException {
    BuildInfoTypeNode node = getBuildInfoNode();
    return cast(node.getValue().getValue().getValue(), BuildInfo.class);
  }

  @Override
  public void setBuildInfo(BuildInfo value) throws UaException {
    BuildInfoTypeNode node = getBuildInfoNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public BuildInfo readBuildInfo() throws UaException {
    try {
      return readBuildInfoAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeBuildInfo(BuildInfo value) throws UaException {
    try {
      writeBuildInfoAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends BuildInfo> readBuildInfoAsync() {
    return getBuildInfoNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), BuildInfo.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeBuildInfoAsync(BuildInfo buildInfo) {
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), buildInfo);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getBuildInfoNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BuildInfoTypeNode getBuildInfoNode() throws UaException {
    try {
      return getBuildInfoNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BuildInfoTypeNode> getBuildInfoNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "BuildInfo", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BuildInfoTypeNode) node);
  }

  @Override
  public UInteger getSecondsTillShutdown() throws UaException {
    BaseDataVariableTypeNode node = getSecondsTillShutdownNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecondsTillShutdown(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSecondsTillShutdownNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readSecondsTillShutdown() throws UaException {
    try {
      return readSecondsTillShutdownAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecondsTillShutdown(UInteger value) throws UaException {
    try {
      writeSecondsTillShutdownAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readSecondsTillShutdownAsync() {
    return getSecondsTillShutdownNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecondsTillShutdownAsync(UInteger secondsTillShutdown) {
    DataValue value = DataValue.valueOnly(new Variant(secondsTillShutdown));
    return getSecondsTillShutdownNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSecondsTillShutdownNode() throws UaException {
    try {
      return getSecondsTillShutdownNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSecondsTillShutdownNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecondsTillShutdown",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public LocalizedText getShutdownReason() throws UaException {
    BaseDataVariableTypeNode node = getShutdownReasonNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setShutdownReason(LocalizedText value) throws UaException {
    BaseDataVariableTypeNode node = getShutdownReasonNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readShutdownReason() throws UaException {
    try {
      return readShutdownReasonAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeShutdownReason(LocalizedText value) throws UaException {
    try {
      writeShutdownReasonAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readShutdownReasonAsync() {
    return getShutdownReasonNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeShutdownReasonAsync(LocalizedText shutdownReason) {
    DataValue value = DataValue.valueOnly(new Variant(shutdownReason));
    return getShutdownReasonNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getShutdownReasonNode() throws UaException {
    try {
      return getShutdownReasonNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getShutdownReasonNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ShutdownReason",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
