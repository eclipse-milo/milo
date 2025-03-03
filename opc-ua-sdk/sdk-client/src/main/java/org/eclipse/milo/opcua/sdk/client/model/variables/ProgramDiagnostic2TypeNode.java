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
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ProgramDiagnostic2TypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnostic2Type {
  public ProgramDiagnostic2TypeNode(
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
  public NodeId getCreateSessionId() throws UaException {
    BaseDataVariableTypeNode node = getCreateSessionIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateSessionId(NodeId value) throws UaException {
    BaseDataVariableTypeNode node = getCreateSessionIdNode();
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
  public BaseDataVariableTypeNode getCreateSessionIdNode() throws UaException {
    try {
      return getCreateSessionIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getCreateSessionIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CreateSessionId",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getCreateClientName() throws UaException {
    BaseDataVariableTypeNode node = getCreateClientNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreateClientName(String value) throws UaException {
    BaseDataVariableTypeNode node = getCreateClientNameNode();
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
  public BaseDataVariableTypeNode getCreateClientNameNode() throws UaException {
    try {
      return getCreateClientNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getCreateClientNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CreateClientName",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public DateTime getInvocationCreationTime() throws UaException {
    BaseDataVariableTypeNode node = getInvocationCreationTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setInvocationCreationTime(DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getInvocationCreationTimeNode();
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
  public BaseDataVariableTypeNode getInvocationCreationTimeNode() throws UaException {
    try {
      return getInvocationCreationTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getInvocationCreationTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "InvocationCreationTime",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public String getLastMethodCall() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodCallNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodCall(String value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodCallNode();
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
  public BaseDataVariableTypeNode getLastMethodCallNode() throws UaException {
    try {
      return getLastMethodCallNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getLastMethodCallNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodCall",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public NodeId getLastMethodSessionId() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodSessionIdNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodSessionId(NodeId value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodSessionIdNode();
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
  public BaseDataVariableTypeNode getLastMethodSessionIdNode() throws UaException {
    try {
      return getLastMethodSessionIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getLastMethodSessionIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodSessionId",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Argument[] getLastMethodInputArguments() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodInputArgumentsNode();
    return cast(node.getValue().getValue().getValue(), Argument[].class);
  }

  @Override
  public void setLastMethodInputArguments(Argument[] value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodInputArgumentsNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public Argument[] readLastMethodInputArguments() throws UaException {
    try {
      return readLastMethodInputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodInputArguments(Argument[] value) throws UaException {
    try {
      writeLastMethodInputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Argument[]> readLastMethodInputArgumentsAsync() {
    return getLastMethodInputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), Argument[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodInputArgumentsAsync(
      Argument[] lastMethodInputArguments) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), lastMethodInputArguments);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getLastMethodInputArgumentsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodInputArgumentsNode() throws UaException {
    try {
      return getLastMethodInputArgumentsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getLastMethodInputArgumentsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodInputArguments",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Argument[] getLastMethodOutputArguments() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodOutputArgumentsNode();
    return cast(node.getValue().getValue().getValue(), Argument[].class);
  }

  @Override
  public void setLastMethodOutputArguments(Argument[] value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodOutputArgumentsNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public Argument[] readLastMethodOutputArguments() throws UaException {
    try {
      return readLastMethodOutputArgumentsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodOutputArguments(Argument[] value) throws UaException {
    try {
      writeLastMethodOutputArgumentsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Argument[]> readLastMethodOutputArgumentsAsync() {
    return getLastMethodOutputArgumentsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), Argument[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodOutputArgumentsAsync(
      Argument[] lastMethodOutputArguments) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), lastMethodOutputArguments);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getLastMethodOutputArgumentsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodOutputArgumentsNode() throws UaException {
    try {
      return getLastMethodOutputArgumentsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getLastMethodOutputArgumentsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodOutputArguments",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Object[] getLastMethodInputValues() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodInputValuesNode();
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodInputValues(Object[] value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodInputValuesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object[] readLastMethodInputValues() throws UaException {
    try {
      return readLastMethodInputValuesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodInputValues(Object[] value) throws UaException {
    try {
      writeLastMethodInputValuesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Object[]> readLastMethodInputValuesAsync() {
    return getLastMethodInputValuesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodInputValuesAsync(
      Object[] lastMethodInputValues) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodInputValues));
    return getLastMethodInputValuesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodInputValuesNode() throws UaException {
    try {
      return getLastMethodInputValuesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getLastMethodInputValuesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodInputValues",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Object[] getLastMethodOutputValues() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodOutputValuesNode();
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodOutputValues(Object[] value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodOutputValuesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object[] readLastMethodOutputValues() throws UaException {
    try {
      return readLastMethodOutputValuesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodOutputValues(Object[] value) throws UaException {
    try {
      writeLastMethodOutputValuesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Object[]> readLastMethodOutputValuesAsync() {
    return getLastMethodOutputValuesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodOutputValuesAsync(
      Object[] lastMethodOutputValues) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodOutputValues));
    return getLastMethodOutputValuesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodOutputValuesNode() throws UaException {
    try {
      return getLastMethodOutputValuesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getLastMethodOutputValuesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodOutputValues",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public DateTime getLastMethodCallTime() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodCallTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodCallTime(DateTime value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodCallTimeNode();
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
  public BaseDataVariableTypeNode getLastMethodCallTimeNode() throws UaException {
    try {
      return getLastMethodCallTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getLastMethodCallTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodCallTime",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public StatusCode getLastMethodReturnStatus() throws UaException {
    BaseDataVariableTypeNode node = getLastMethodReturnStatusNode();
    return (StatusCode) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastMethodReturnStatus(StatusCode value) throws UaException {
    BaseDataVariableTypeNode node = getLastMethodReturnStatusNode();
    node.setValue(new Variant(value));
  }

  @Override
  public StatusCode readLastMethodReturnStatus() throws UaException {
    try {
      return readLastMethodReturnStatusAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastMethodReturnStatus(StatusCode value) throws UaException {
    try {
      writeLastMethodReturnStatusAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends StatusCode> readLastMethodReturnStatusAsync() {
    return getLastMethodReturnStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (StatusCode) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastMethodReturnStatusAsync(
      StatusCode lastMethodReturnStatus) {
    DataValue value = DataValue.valueOnly(new Variant(lastMethodReturnStatus));
    return getLastMethodReturnStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodReturnStatusNode() throws UaException {
    try {
      return getLastMethodReturnStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getLastMethodReturnStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastMethodReturnStatus",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
