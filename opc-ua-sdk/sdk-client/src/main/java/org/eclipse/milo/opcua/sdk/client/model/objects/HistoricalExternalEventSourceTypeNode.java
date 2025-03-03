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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public class HistoricalExternalEventSourceTypeNode extends BaseObjectTypeNode
    implements HistoricalExternalEventSourceType {
  public HistoricalExternalEventSourceTypeNode(
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
  public String getServer() throws UaException {
    PropertyTypeNode node = getServerNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setServer(String value) throws UaException {
    PropertyTypeNode node = getServerNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readServer() throws UaException {
    try {
      return readServerAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeServer(String value) throws UaException {
    try {
      writeServerAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readServerAsync() {
    return getServerNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeServerAsync(String server) {
    DataValue value = DataValue.valueOnly(new Variant(server));
    return getServerNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getServerNode() throws UaException {
    try {
      return getServerNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Server", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getEndpointUrl() throws UaException {
    PropertyTypeNode node = getEndpointUrlNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setEndpointUrl(String value) throws UaException {
    PropertyTypeNode node = getEndpointUrlNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readEndpointUrl() throws UaException {
    try {
      return readEndpointUrlAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeEndpointUrl(String value) throws UaException {
    try {
      writeEndpointUrlAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readEndpointUrlAsync() {
    return getEndpointUrlNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointUrlAsync(String endpointUrl) {
    DataValue value = DataValue.valueOnly(new Variant(endpointUrl));
    return getEndpointUrlNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getEndpointUrlNode() throws UaException {
    try {
      return getEndpointUrlNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEndpointUrlNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "EndpointUrl",
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
  public UserTokenPolicy getIdentityTokenPolicy() throws UaException {
    PropertyTypeNode node = getIdentityTokenPolicyNode();
    return cast(node.getValue().getValue().getValue(), UserTokenPolicy.class);
  }

  @Override
  public void setIdentityTokenPolicy(UserTokenPolicy value) throws UaException {
    PropertyTypeNode node = getIdentityTokenPolicyNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public UserTokenPolicy readIdentityTokenPolicy() throws UaException {
    try {
      return readIdentityTokenPolicyAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeIdentityTokenPolicy(UserTokenPolicy value) throws UaException {
    try {
      writeIdentityTokenPolicyAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UserTokenPolicy> readIdentityTokenPolicyAsync() {
    return getIdentityTokenPolicyNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), UserTokenPolicy.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeIdentityTokenPolicyAsync(
      UserTokenPolicy identityTokenPolicy) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), identityTokenPolicy);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getIdentityTokenPolicyNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIdentityTokenPolicyNode() throws UaException {
    try {
      return getIdentityTokenPolicyNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIdentityTokenPolicyNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IdentityTokenPolicy",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getTransportProfileUri() throws UaException {
    PropertyTypeNode node = getTransportProfileUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setTransportProfileUri(String value) throws UaException {
    PropertyTypeNode node = getTransportProfileUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readTransportProfileUri() throws UaException {
    try {
      return readTransportProfileUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeTransportProfileUri(String value) throws UaException {
    try {
      writeTransportProfileUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readTransportProfileUriAsync() {
    return getTransportProfileUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeTransportProfileUriAsync(String transportProfileUri) {
    DataValue value = DataValue.valueOnly(new Variant(transportProfileUri));
    return getTransportProfileUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getTransportProfileUriNode() throws UaException {
    try {
      return getTransportProfileUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTransportProfileUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TransportProfileUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public EventFilter getHistoricalEventFilter() throws UaException {
    PropertyTypeNode node = getHistoricalEventFilterNode();
    return cast(node.getValue().getValue().getValue(), EventFilter.class);
  }

  @Override
  public void setHistoricalEventFilter(EventFilter value) throws UaException {
    PropertyTypeNode node = getHistoricalEventFilterNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public EventFilter readHistoricalEventFilter() throws UaException {
    try {
      return readHistoricalEventFilterAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHistoricalEventFilter(EventFilter value) throws UaException {
    try {
      writeHistoricalEventFilterAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends EventFilter> readHistoricalEventFilterAsync() {
    return getHistoricalEventFilterNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), EventFilter.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeHistoricalEventFilterAsync(
      EventFilter historicalEventFilter) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), historicalEventFilter);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getHistoricalEventFilterNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHistoricalEventFilterNode() throws UaException {
    try {
      return getHistoricalEventFilterNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHistoricalEventFilterNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HistoricalEventFilter",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
