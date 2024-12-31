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
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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

public class AuditCreateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditCreateSessionEventType {
  public AuditCreateSessionEventTypeNode(
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
  public ByteString getClientCertificate() throws UaException {
    PropertyTypeNode node = getClientCertificateNode();
    return (ByteString) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientCertificate(ByteString value) throws UaException {
    PropertyTypeNode node = getClientCertificateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ByteString readClientCertificate() throws UaException {
    try {
      return readClientCertificateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeClientCertificate(ByteString value) throws UaException {
    try {
      writeClientCertificateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ByteString> readClientCertificateAsync() {
    return getClientCertificateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ByteString) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeClientCertificateAsync(ByteString clientCertificate) {
    DataValue value = DataValue.valueOnly(new Variant(clientCertificate));
    return getClientCertificateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getClientCertificateNode() throws UaException {
    try {
      return getClientCertificateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientCertificateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ClientCertificate",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getClientCertificateThumbprint() throws UaException {
    PropertyTypeNode node = getClientCertificateThumbprintNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientCertificateThumbprint(String value) throws UaException {
    PropertyTypeNode node = getClientCertificateThumbprintNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readClientCertificateThumbprint() throws UaException {
    try {
      return readClientCertificateThumbprintAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeClientCertificateThumbprint(String value) throws UaException {
    try {
      writeClientCertificateThumbprintAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readClientCertificateThumbprintAsync() {
    return getClientCertificateThumbprintNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeClientCertificateThumbprintAsync(
      String clientCertificateThumbprint) {
    DataValue value = DataValue.valueOnly(new Variant(clientCertificateThumbprint));
    return getClientCertificateThumbprintNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getClientCertificateThumbprintNode() throws UaException {
    try {
      return getClientCertificateThumbprintNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientCertificateThumbprintNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ClientCertificateThumbprint",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getRevisedSessionTimeout() throws UaException {
    PropertyTypeNode node = getRevisedSessionTimeoutNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setRevisedSessionTimeout(Double value) throws UaException {
    PropertyTypeNode node = getRevisedSessionTimeoutNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readRevisedSessionTimeout() throws UaException {
    try {
      return readRevisedSessionTimeoutAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRevisedSessionTimeout(Double value) throws UaException {
    try {
      writeRevisedSessionTimeoutAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readRevisedSessionTimeoutAsync() {
    return getRevisedSessionTimeoutNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRevisedSessionTimeoutAsync(
      Double revisedSessionTimeout) {
    DataValue value = DataValue.valueOnly(new Variant(revisedSessionTimeout));
    return getRevisedSessionTimeoutNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRevisedSessionTimeoutNode() throws UaException {
    try {
      return getRevisedSessionTimeoutNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRevisedSessionTimeoutNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RevisedSessionTimeout",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
