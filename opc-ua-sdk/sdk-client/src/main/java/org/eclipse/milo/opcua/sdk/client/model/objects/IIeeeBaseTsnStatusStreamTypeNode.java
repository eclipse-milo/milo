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
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class IIeeeBaseTsnStatusStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStatusStreamType {
  public IIeeeBaseTsnStatusStreamTypeNode(
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
  public TsnTalkerStatus getTalkerStatus() throws UaException {
    BaseDataVariableTypeNode node = getTalkerStatusNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return TsnTalkerStatus.from((Integer) value);
    } else if (value instanceof TsnTalkerStatus) {
      return (TsnTalkerStatus) value;
    } else {
      return null;
    }
  }

  @Override
  public void setTalkerStatus(TsnTalkerStatus value) throws UaException {
    BaseDataVariableTypeNode node = getTalkerStatusNode();
    node.setValue(new Variant(value));
  }

  @Override
  public TsnTalkerStatus readTalkerStatus() throws UaException {
    try {
      return readTalkerStatusAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTalkerStatus(TsnTalkerStatus value) throws UaException {
    try {
      writeTalkerStatusAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends TsnTalkerStatus> readTalkerStatusAsync() {
    return getTalkerStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return TsnTalkerStatus.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTalkerStatusAsync(TsnTalkerStatus talkerStatus) {
    DataValue value = DataValue.valueOnly(new Variant(talkerStatus));
    return getTalkerStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getTalkerStatusNode() throws UaException {
    try {
      return getTalkerStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getTalkerStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TalkerStatus",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public TsnListenerStatus getListenerStatus() throws UaException {
    BaseDataVariableTypeNode node = getListenerStatusNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return TsnListenerStatus.from((Integer) value);
    } else if (value instanceof TsnListenerStatus) {
      return (TsnListenerStatus) value;
    } else {
      return null;
    }
  }

  @Override
  public void setListenerStatus(TsnListenerStatus value) throws UaException {
    BaseDataVariableTypeNode node = getListenerStatusNode();
    node.setValue(new Variant(value));
  }

  @Override
  public TsnListenerStatus readListenerStatus() throws UaException {
    try {
      return readListenerStatusAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeListenerStatus(TsnListenerStatus value) throws UaException {
    try {
      writeListenerStatusAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends TsnListenerStatus> readListenerStatusAsync() {
    return getListenerStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return TsnListenerStatus.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeListenerStatusAsync(TsnListenerStatus listenerStatus) {
    DataValue value = DataValue.valueOnly(new Variant(listenerStatus));
    return getListenerStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getListenerStatusNode() throws UaException {
    try {
      return getListenerStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getListenerStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ListenerStatus",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public TsnFailureCode getFailureCode() throws UaException {
    BaseDataVariableTypeNode node = getFailureCodeNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return TsnFailureCode.from((Integer) value);
    } else if (value instanceof TsnFailureCode) {
      return (TsnFailureCode) value;
    } else {
      return null;
    }
  }

  @Override
  public void setFailureCode(TsnFailureCode value) throws UaException {
    BaseDataVariableTypeNode node = getFailureCodeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public TsnFailureCode readFailureCode() throws UaException {
    try {
      return readFailureCodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeFailureCode(TsnFailureCode value) throws UaException {
    try {
      writeFailureCodeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends TsnFailureCode> readFailureCodeAsync() {
    return getFailureCodeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return TsnFailureCode.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeFailureCodeAsync(TsnFailureCode failureCode) {
    DataValue value = DataValue.valueOnly(new Variant(failureCode));
    return getFailureCodeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getFailureCodeNode() throws UaException {
    try {
      return getFailureCodeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getFailureCodeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "FailureCode",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public Object getFailureSystemIdentifier() throws UaException {
    BaseDataVariableTypeNode node = getFailureSystemIdentifierNode();
    return (Object) node.getValue().getValue().getValue();
  }

  @Override
  public void setFailureSystemIdentifier(Object value) throws UaException {
    BaseDataVariableTypeNode node = getFailureSystemIdentifierNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object readFailureSystemIdentifier() throws UaException {
    try {
      return readFailureSystemIdentifierAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeFailureSystemIdentifier(Object value) throws UaException {
    try {
      writeFailureSystemIdentifierAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<?> readFailureSystemIdentifierAsync() {
    return getFailureSystemIdentifierNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeFailureSystemIdentifierAsync(
      Object failureSystemIdentifier) {
    DataValue value = DataValue.valueOnly(new Variant(failureSystemIdentifier));
    return getFailureSystemIdentifierNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getFailureSystemIdentifierNode() throws UaException {
    try {
      return getFailureSystemIdentifierNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getFailureSystemIdentifierNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "FailureSystemIdentifier",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
