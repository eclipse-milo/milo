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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditOpenSecureChannelEventTypeNode extends AuditChannelEventTypeNode
    implements AuditOpenSecureChannelEventType {
  public AuditOpenSecureChannelEventTypeNode(
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
  public SecurityTokenRequestType getRequestType() throws UaException {
    PropertyTypeNode node = getRequestTypeNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return SecurityTokenRequestType.from((Integer) value);
    } else if (value instanceof SecurityTokenRequestType) {
      return (SecurityTokenRequestType) value;
    } else {
      return null;
    }
  }

  @Override
  public void setRequestType(SecurityTokenRequestType value) throws UaException {
    PropertyTypeNode node = getRequestTypeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public SecurityTokenRequestType readRequestType() throws UaException {
    try {
      return readRequestTypeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRequestType(SecurityTokenRequestType value) throws UaException {
    try {
      writeRequestTypeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends SecurityTokenRequestType> readRequestTypeAsync() {
    return getRequestTypeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return SecurityTokenRequestType.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestTypeAsync(SecurityTokenRequestType requestType) {
    DataValue value = DataValue.valueOnly(new Variant(requestType));
    return getRequestTypeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRequestTypeNode() throws UaException {
    try {
      return getRequestTypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestTypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RequestType",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getSecurityPolicyUri() throws UaException {
    PropertyTypeNode node = getSecurityPolicyUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityPolicyUri(String value) throws UaException {
    PropertyTypeNode node = getSecurityPolicyUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readSecurityPolicyUri() throws UaException {
    try {
      return readSecurityPolicyUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityPolicyUri(String value) throws UaException {
    try {
      writeSecurityPolicyUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readSecurityPolicyUriAsync() {
    return getSecurityPolicyUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(String securityPolicyUri) {
    DataValue value = DataValue.valueOnly(new Variant(securityPolicyUri));
    return getSecurityPolicyUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() throws UaException {
    try {
      return getSecurityPolicyUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityPolicyUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public MessageSecurityMode getSecurityMode() throws UaException {
    PropertyTypeNode node = getSecurityModeNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return MessageSecurityMode.from((Integer) value);
    } else if (value instanceof MessageSecurityMode) {
      return (MessageSecurityMode) value;
    } else {
      return null;
    }
  }

  @Override
  public void setSecurityMode(MessageSecurityMode value) throws UaException {
    PropertyTypeNode node = getSecurityModeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public MessageSecurityMode readSecurityMode() throws UaException {
    try {
      return readSecurityModeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSecurityMode(MessageSecurityMode value) throws UaException {
    try {
      writeSecurityModeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends MessageSecurityMode> readSecurityModeAsync() {
    return getSecurityModeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return MessageSecurityMode.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityModeAsync(MessageSecurityMode securityMode) {
    DataValue value = DataValue.valueOnly(new Variant(securityMode));
    return getSecurityModeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSecurityModeNode() throws UaException {
    try {
      return getSecurityModeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityModeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SecurityMode",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getRequestedLifetime() throws UaException {
    PropertyTypeNode node = getRequestedLifetimeNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setRequestedLifetime(Double value) throws UaException {
    PropertyTypeNode node = getRequestedLifetimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readRequestedLifetime() throws UaException {
    try {
      return readRequestedLifetimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRequestedLifetime(Double value) throws UaException {
    try {
      writeRequestedLifetimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readRequestedLifetimeAsync() {
    return getRequestedLifetimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestedLifetimeAsync(Double requestedLifetime) {
    DataValue value = DataValue.valueOnly(new Variant(requestedLifetime));
    return getRequestedLifetimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRequestedLifetimeNode() throws UaException {
    try {
      return getRequestedLifetimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestedLifetimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RequestedLifetime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getCertificateErrorEventId() throws UaException {
    PropertyTypeNode node = getCertificateErrorEventIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setCertificateErrorEventId(String value) throws UaException {
    PropertyTypeNode node = getCertificateErrorEventIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readCertificateErrorEventId() throws UaException {
    try {
      return readCertificateErrorEventIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCertificateErrorEventId(String value) throws UaException {
    try {
      writeCertificateErrorEventIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readCertificateErrorEventIdAsync() {
    return getCertificateErrorEventIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCertificateErrorEventIdAsync(
      String certificateErrorEventId) {
    DataValue value = DataValue.valueOnly(new Variant(certificateErrorEventId));
    return getCertificateErrorEventIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCertificateErrorEventIdNode() throws UaException {
    try {
      return getCertificateErrorEventIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCertificateErrorEventIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CertificateErrorEventId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
