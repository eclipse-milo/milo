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
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
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

public class DialogConditionTypeNode extends ConditionTypeNode implements DialogConditionType {
  public DialogConditionTypeNode(
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
  public LocalizedText getPrompt() throws UaException {
    PropertyTypeNode node = getPromptNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setPrompt(LocalizedText value) throws UaException {
    PropertyTypeNode node = getPromptNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readPrompt() throws UaException {
    try {
      return readPromptAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePrompt(LocalizedText value) throws UaException {
    try {
      writePromptAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readPromptAsync() {
    return getPromptNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePromptAsync(LocalizedText prompt) {
    DataValue value = DataValue.valueOnly(new Variant(prompt));
    return getPromptNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPromptNode() throws UaException {
    try {
      return getPromptNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPromptNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Prompt", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText[] getResponseOptionSet() throws UaException {
    PropertyTypeNode node = getResponseOptionSetNode();
    return (LocalizedText[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setResponseOptionSet(LocalizedText[] value) throws UaException {
    PropertyTypeNode node = getResponseOptionSetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText[] readResponseOptionSet() throws UaException {
    try {
      return readResponseOptionSetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeResponseOptionSet(LocalizedText[] value) throws UaException {
    try {
      writeResponseOptionSetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText[]> readResponseOptionSetAsync() {
    return getResponseOptionSetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeResponseOptionSetAsync(
      LocalizedText[] responseOptionSet) {
    DataValue value = DataValue.valueOnly(new Variant(responseOptionSet));
    return getResponseOptionSetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getResponseOptionSetNode() throws UaException {
    try {
      return getResponseOptionSetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getResponseOptionSetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ResponseOptionSet",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Integer getDefaultResponse() throws UaException {
    PropertyTypeNode node = getDefaultResponseNode();
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefaultResponse(Integer value) throws UaException {
    PropertyTypeNode node = getDefaultResponseNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Integer readDefaultResponse() throws UaException {
    try {
      return readDefaultResponseAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDefaultResponse(Integer value) throws UaException {
    try {
      writeDefaultResponseAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Integer> readDefaultResponseAsync() {
    return getDefaultResponseNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Integer) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultResponseAsync(Integer defaultResponse) {
    DataValue value = DataValue.valueOnly(new Variant(defaultResponse));
    return getDefaultResponseNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDefaultResponseNode() throws UaException {
    try {
      return getDefaultResponseNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDefaultResponseNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DefaultResponse",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Integer getOkResponse() throws UaException {
    PropertyTypeNode node = getOkResponseNode();
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setOkResponse(Integer value) throws UaException {
    PropertyTypeNode node = getOkResponseNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Integer readOkResponse() throws UaException {
    try {
      return readOkResponseAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOkResponse(Integer value) throws UaException {
    try {
      writeOkResponseAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Integer> readOkResponseAsync() {
    return getOkResponseNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Integer) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeOkResponseAsync(Integer okResponse) {
    DataValue value = DataValue.valueOnly(new Variant(okResponse));
    return getOkResponseNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getOkResponseNode() throws UaException {
    try {
      return getOkResponseNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOkResponseNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "OkResponse", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Integer getCancelResponse() throws UaException {
    PropertyTypeNode node = getCancelResponseNode();
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setCancelResponse(Integer value) throws UaException {
    PropertyTypeNode node = getCancelResponseNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Integer readCancelResponse() throws UaException {
    try {
      return readCancelResponseAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCancelResponse(Integer value) throws UaException {
    try {
      writeCancelResponseAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Integer> readCancelResponseAsync() {
    return getCancelResponseNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Integer) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCancelResponseAsync(Integer cancelResponse) {
    DataValue value = DataValue.valueOnly(new Variant(cancelResponse));
    return getCancelResponseNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCancelResponseNode() throws UaException {
    try {
      return getCancelResponseNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCancelResponseNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CancelResponse",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Integer getLastResponse() throws UaException {
    PropertyTypeNode node = getLastResponseNode();
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastResponse(Integer value) throws UaException {
    PropertyTypeNode node = getLastResponseNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Integer readLastResponse() throws UaException {
    try {
      return readLastResponseAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastResponse(Integer value) throws UaException {
    try {
      writeLastResponseAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Integer> readLastResponseAsync() {
    return getLastResponseNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Integer) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastResponseAsync(Integer lastResponse) {
    DataValue value = DataValue.valueOnly(new Variant(lastResponse));
    return getLastResponseNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastResponseNode() throws UaException {
    try {
      return getLastResponseNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastResponseNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastResponse",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText getEnabledState() throws UaException {
    TwoStateVariableTypeNode node = getEnabledStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setEnabledState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getEnabledStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readEnabledState() throws UaException {
    try {
      return readEnabledStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEnabledState(LocalizedText value) throws UaException {
    try {
      writeEnabledStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readEnabledStateAsync() {
    return getEnabledStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEnabledStateAsync(LocalizedText enabledState) {
    DataValue value = DataValue.valueOnly(new Variant(enabledState));
    return getEnabledStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() throws UaException {
    try {
      return getEnabledStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getEnabledStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "EnabledState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }

  @Override
  public LocalizedText getDialogState() throws UaException {
    TwoStateVariableTypeNode node = getDialogStateNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setDialogState(LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getDialogStateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readDialogState() throws UaException {
    try {
      return readDialogStateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDialogState(LocalizedText value) throws UaException {
    try {
      writeDialogStateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readDialogStateAsync() {
    return getDialogStateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDialogStateAsync(LocalizedText dialogState) {
    DataValue value = DataValue.valueOnly(new Variant(dialogState));
    return getDialogStateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public TwoStateVariableTypeNode getDialogStateNode() throws UaException {
    try {
      return getDialogStateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends TwoStateVariableTypeNode> getDialogStateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DialogState",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (TwoStateVariableTypeNode) node);
  }
}
