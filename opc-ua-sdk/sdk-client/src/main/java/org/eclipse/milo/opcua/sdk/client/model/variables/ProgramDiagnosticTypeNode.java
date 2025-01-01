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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;

public class ProgramDiagnosticTypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnosticType {
  public ProgramDiagnosticTypeNode(
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
  public NodeId getCreateSessionId() throws UaException {
    PropertyTypeNode node = getCreateSessionIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateSessionId(NodeId value) throws UaException {
    PropertyTypeNode node = getCreateSessionIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readCreateSessionId() throws UaException {
    try {
      return readCreateSessionIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCreateSessionId(NodeId value) throws UaException {
    try {
      writeCreateSessionIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readCreateSessionIdAsync() {
    return getCreateSessionIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateSessionIdAsync(NodeId createSessionId) {
    DataValue value = DataValue.valueOnly(new Variant(createSessionId));
    return getCreateSessionIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCreateSessionIdNode() throws UaException {
    try {
      return getCreateSessionIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCreateSessionIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CreateSessionId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getCreateClientName() throws UaException {
    PropertyTypeNode node = getCreateClientNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateClientName(String value) throws UaException {
    PropertyTypeNode node = getCreateClientNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readCreateClientName() throws UaException {
    try {
      return readCreateClientNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCreateClientName(String value) throws UaException {
    try {
      writeCreateClientNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readCreateClientNameAsync() {
    return getCreateClientNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCreateClientNameAsync(String createClientName) {
    DataValue value = DataValue.valueOnly(new Variant(createClientName));
    return getCreateClientNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCreateClientNameNode() throws UaException {
    try {
      return getCreateClientNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCreateClientNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CreateClientName",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getInvocationCreationTime() throws UaException {
    PropertyTypeNode node = getInvocationCreationTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setInvocationCreationTime(DateTime value) throws UaException {
    PropertyTypeNode node = getInvocationCreationTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readInvocationCreationTime() throws UaException {
    try {
      return readInvocationCreationTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInvocationCreationTime(DateTime value) throws UaException {
    try {
      writeInvocationCreationTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readInvocationCreationTimeAsync() {
    return getInvocationCreationTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeInvocationCreationTimeAsync(
      DateTime invocationCreationTime) {
    DataValue value = DataValue.valueOnly(new Variant(invocationCreationTime));
    return getInvocationCreationTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getInvocationCreationTimeNode() throws UaException {
    try {
      return getInvocationCreationTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInvocationCreationTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "InvocationCreationTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getLastTransitionTime() throws UaException {
    PropertyTypeNode node = getLastTransitionTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastTransitionTime(DateTime value) throws UaException {
    PropertyTypeNode node = getLastTransitionTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readLastTransitionTime() throws UaException {
    try {
      return readLastTransitionTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastTransitionTime(DateTime value) throws UaException {
    try {
      writeLastTransitionTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readLastTransitionTimeAsync() {
    return getLastTransitionTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastTransitionTimeAsync(DateTime lastTransitionTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastTransitionTime));
    return getLastTransitionTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastTransitionTimeNode() throws UaException {
    try {
      return getLastTransitionTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastTransitionTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastTransitionTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getLastMethodCall() throws UaException {
    PropertyTypeNode node = getLastMethodCallNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodCall(String value) throws UaException {
    PropertyTypeNode node = getLastMethodCallNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readLastMethodCall() throws UaException {
    try {
      return readLastMethodCallAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodCall(String value) throws UaException {
    try {
      writeLastMethodCallAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readLastMethodCallAsync() {
    return getLastMethodCallNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodCallAsync(String lastMethodCall) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodCall));
    return getLastMethodCallNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastMethodCallNode() throws UaException {
    try {
      return getLastMethodCallNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodCallNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodCall",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getLastMethodSessionId() throws UaException {
    PropertyTypeNode node = getLastMethodSessionIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodSessionId(NodeId value) throws UaException {
    PropertyTypeNode node = getLastMethodSessionIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readLastMethodSessionId() throws UaException {
    try {
      return readLastMethodSessionIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodSessionId(NodeId value) throws UaException {
    try {
      writeLastMethodSessionIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readLastMethodSessionIdAsync() {
    return getLastMethodSessionIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodSessionIdAsync(NodeId lastMethodSessionId) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodSessionId));
    return getLastMethodSessionIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastMethodSessionIdNode() throws UaException {
    try {
      return getLastMethodSessionIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodSessionIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodSessionId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Object[] getLastMethodInputArguments() throws UaException {
    PropertyTypeNode node = getLastMethodInputArgumentsNode();
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodInputArguments(Object[] value) throws UaException {
    PropertyTypeNode node = getLastMethodInputArgumentsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object[] readLastMethodInputArguments() throws UaException {
    try {
      return readLastMethodInputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodInputArguments(Object[] value) throws UaException {
    try {
      writeLastMethodInputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Object[]> readLastMethodInputArgumentsAsync() {
    return getLastMethodInputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      Object[] lastMethodInputArguments) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodInputArguments));
    return getLastMethodInputArgumentsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastMethodInputArgumentsNode() throws UaException {
    try {
      return getLastMethodInputArgumentsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodInputArgumentsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodInputArguments",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Object[] getLastMethodOutputArguments() throws UaException {
    PropertyTypeNode node = getLastMethodOutputArgumentsNode();
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodOutputArguments(Object[] value) throws UaException {
    PropertyTypeNode node = getLastMethodOutputArgumentsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object[] readLastMethodOutputArguments() throws UaException {
    try {
      return readLastMethodOutputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodOutputArguments(Object[] value) throws UaException {
    try {
      writeLastMethodOutputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Object[]> readLastMethodOutputArgumentsAsync() {
    return getLastMethodOutputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      Object[] lastMethodOutputArguments) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodOutputArguments));
    return getLastMethodOutputArgumentsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastMethodOutputArgumentsNode() throws UaException {
    try {
      return getLastMethodOutputArgumentsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodOutputArgumentsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodOutputArguments",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getLastMethodCallTime() throws UaException {
    PropertyTypeNode node = getLastMethodCallTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodCallTime(DateTime value) throws UaException {
    PropertyTypeNode node = getLastMethodCallTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readLastMethodCallTime() throws UaException {
    try {
      return readLastMethodCallTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodCallTime(DateTime value) throws UaException {
    try {
      writeLastMethodCallTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readLastMethodCallTimeAsync() {
    return getLastMethodCallTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodCallTimeAsync(DateTime lastMethodCallTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodCallTime));
    return getLastMethodCallTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastMethodCallTimeNode() throws UaException {
    try {
      return getLastMethodCallTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodCallTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodCallTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public StatusResult getLastMethodReturnStatus() throws UaException {
    PropertyTypeNode node = getLastMethodReturnStatusNode();
    return cast(node.getValue().getValue().getValue(), StatusResult.class);
  }

  @Override
  public void setLastMethodReturnStatus(StatusResult value) throws UaException {
    PropertyTypeNode node = getLastMethodReturnStatusNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public StatusResult readLastMethodReturnStatus() throws UaException {
    try {
      return readLastMethodReturnStatusAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodReturnStatus(StatusResult value) throws UaException {
    try {
      writeLastMethodReturnStatusAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends StatusResult> readLastMethodReturnStatusAsync() {
    return getLastMethodReturnStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), StatusResult.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(
      StatusResult lastMethodReturnStatus) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), lastMethodReturnStatus);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getLastMethodReturnStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastMethodReturnStatusNode() throws UaException {
    try {
      return getLastMethodReturnStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastMethodReturnStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodReturnStatus",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
