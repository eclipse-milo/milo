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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;

public class AuditActivateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditActivateSessionEventType {
  public AuditActivateSessionEventTypeNode(
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
  public SignedSoftwareCertificate[] getClientSoftwareCertificates() throws UaException {
    PropertyTypeNode node = getClientSoftwareCertificatesNode();
    return cast(node.getValue().getValue().getValue(), SignedSoftwareCertificate[].class);
  }

  @Override
  public void setClientSoftwareCertificates(SignedSoftwareCertificate[] value) throws UaException {
    PropertyTypeNode node = getClientSoftwareCertificatesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public SignedSoftwareCertificate[] readClientSoftwareCertificates() throws UaException {
    try {
      return readClientSoftwareCertificatesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeClientSoftwareCertificates(SignedSoftwareCertificate[] value)
      throws UaException {
    try {
      writeClientSoftwareCertificatesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SignedSoftwareCertificate[]>
      readClientSoftwareCertificatesAsync() {
    return getClientSoftwareCertificatesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), SignedSoftwareCertificate[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientSoftwareCertificatesAsync(
      SignedSoftwareCertificate[] clientSoftwareCertificates) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), clientSoftwareCertificates);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getClientSoftwareCertificatesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getClientSoftwareCertificatesNode() throws UaException {
    try {
      return getClientSoftwareCertificatesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientSoftwareCertificatesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ClientSoftwareCertificates",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UserIdentityToken getUserIdentityToken() throws UaException {
    PropertyTypeNode node = getUserIdentityTokenNode();
    return cast(node.getValue().getValue().getValue(), UserIdentityToken.class);
  }

  @Override
  public void setUserIdentityToken(UserIdentityToken value) throws UaException {
    PropertyTypeNode node = getUserIdentityTokenNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public UserIdentityToken readUserIdentityToken() throws UaException {
    try {
      return readUserIdentityTokenAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUserIdentityToken(UserIdentityToken value) throws UaException {
    try {
      writeUserIdentityTokenAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UserIdentityToken> readUserIdentityTokenAsync() {
    return getUserIdentityTokenNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), UserIdentityToken.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeUserIdentityTokenAsync(
      UserIdentityToken userIdentityToken) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), userIdentityToken);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getUserIdentityTokenNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUserIdentityTokenNode() throws UaException {
    try {
      return getUserIdentityTokenNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserIdentityTokenNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UserIdentityToken",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getSecureChannelId() throws UaException {
    PropertyTypeNode node = getSecureChannelIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecureChannelId(String value) throws UaException {
    PropertyTypeNode node = getSecureChannelIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSecureChannelId() throws UaException {
    try {
      return readSecureChannelIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecureChannelId(String value) throws UaException {
    try {
      writeSecureChannelIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSecureChannelIdAsync() {
    return getSecureChannelIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecureChannelIdAsync(String secureChannelId) {
    DataValue value = DataValue.valueOnly(new Variant(secureChannelId));
    return getSecureChannelIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecureChannelIdNode() throws UaException {
    try {
      return getSecureChannelIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecureChannelIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecureChannelId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId[] getCurrentRoleIds() throws UaException {
    PropertyTypeNode node = getCurrentRoleIdsNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentRoleIds(NodeId[] value) throws UaException {
    PropertyTypeNode node = getCurrentRoleIdsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readCurrentRoleIds() throws UaException {
    try {
      return readCurrentRoleIdsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCurrentRoleIds(NodeId[] value) throws UaException {
    try {
      writeCurrentRoleIdsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readCurrentRoleIdsAsync() {
    return getCurrentRoleIdsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(NodeId[] currentRoleIds) {
    DataValue value = DataValue.valueOnly(new Variant(currentRoleIds));
    return getCurrentRoleIdsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCurrentRoleIdsNode() throws UaException {
    try {
      return getCurrentRoleIdsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCurrentRoleIdsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CurrentRoleIds",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
