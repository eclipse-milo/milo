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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;

public class UserManagementTypeNode extends BaseObjectTypeNode implements UserManagementType {
  public UserManagementTypeNode(
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
  public UserManagementDataType[] getUsers() throws UaException {
    PropertyTypeNode node = getUsersNode();
    return cast(node.getValue().getValue().getValue(), UserManagementDataType[].class);
  }

  @Override
  public void setUsers(UserManagementDataType[] value) throws UaException {
    PropertyTypeNode node = getUsersNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public UserManagementDataType[] readUsers() throws UaException {
    try {
      return readUsersAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUsers(UserManagementDataType[] value) throws UaException {
    try {
      writeUsersAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UserManagementDataType[]> readUsersAsync() {
    return getUsersNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), UserManagementDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeUsersAsync(UserManagementDataType[] users) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), users);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getUsersNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUsersNode() throws UaException {
    try {
      return getUsersNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUsersNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Users", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Range getPasswordLength() throws UaException {
    PropertyTypeNode node = getPasswordLengthNode();
    return cast(node.getValue().getValue().getValue(), Range.class);
  }

  @Override
  public void setPasswordLength(Range value) throws UaException {
    PropertyTypeNode node = getPasswordLengthNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public Range readPasswordLength() throws UaException {
    try {
      return readPasswordLengthAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePasswordLength(Range value) throws UaException {
    try {
      writePasswordLengthAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Range> readPasswordLengthAsync() {
    return getPasswordLengthNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), Range.class));
  }

  @Override
  public CompletableFuture<StatusCode> writePasswordLengthAsync(Range passwordLength) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), passwordLength);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getPasswordLengthNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPasswordLengthNode() throws UaException {
    try {
      return getPasswordLengthNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPasswordLengthNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PasswordLength",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public PasswordOptionsMask getPasswordOptions() throws UaException {
    PropertyTypeNode node = getPasswordOptionsNode();
    return (PasswordOptionsMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setPasswordOptions(PasswordOptionsMask value) throws UaException {
    PropertyTypeNode node = getPasswordOptionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public PasswordOptionsMask readPasswordOptions() throws UaException {
    try {
      return readPasswordOptionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePasswordOptions(PasswordOptionsMask value) throws UaException {
    try {
      writePasswordOptionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends PasswordOptionsMask> readPasswordOptionsAsync() {
    return getPasswordOptionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (PasswordOptionsMask) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePasswordOptionsAsync(
      PasswordOptionsMask passwordOptions) {
    DataValue value = DataValue.valueOnly(new Variant(passwordOptions));
    return getPasswordOptionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPasswordOptionsNode() throws UaException {
    try {
      return getPasswordOptionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPasswordOptionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PasswordOptions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public LocalizedText getPasswordRestrictions() throws UaException {
    PropertyTypeNode node = getPasswordRestrictionsNode();
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setPasswordRestrictions(LocalizedText value) throws UaException {
    PropertyTypeNode node = getPasswordRestrictionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public LocalizedText readPasswordRestrictions() throws UaException {
    try {
      return readPasswordRestrictionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePasswordRestrictions(LocalizedText value) throws UaException {
    try {
      writePasswordRestrictionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends LocalizedText> readPasswordRestrictionsAsync() {
    return getPasswordRestrictionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (LocalizedText) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePasswordRestrictionsAsync(
      LocalizedText passwordRestrictions) {
    DataValue value = DataValue.valueOnly(new Variant(passwordRestrictions));
    return getPasswordRestrictionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPasswordRestrictionsNode() throws UaException {
    try {
      return getPasswordRestrictionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPasswordRestrictionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PasswordRestrictions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
