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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public class PubSubKeyPushTargetTypeNode extends BaseObjectTypeNode
    implements PubSubKeyPushTargetType {
  public PubSubKeyPushTargetTypeNode(
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
  public String getApplicationUri() throws UaException {
    PropertyTypeNode node = getApplicationUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setApplicationUri(String value) throws UaException {
    PropertyTypeNode node = getApplicationUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readApplicationUri() throws UaException {
    try {
      return readApplicationUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeApplicationUri(String value) throws UaException {
    try {
      writeApplicationUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readApplicationUriAsync() {
    return getApplicationUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeApplicationUriAsync(String applicationUri) {
    DataValue value = DataValue.valueOnly(new Variant(applicationUri));
    return getApplicationUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getApplicationUriNode() throws UaException {
    try {
      return getApplicationUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ApplicationUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
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
  public UserTokenPolicy getUserTokenType() throws UaException {
    PropertyTypeNode node = getUserTokenTypeNode();
    return cast(node.getValue().getValue().getValue(), UserTokenPolicy.class);
  }

  @Override
  public void setUserTokenType(UserTokenPolicy value) throws UaException {
    PropertyTypeNode node = getUserTokenTypeNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public UserTokenPolicy readUserTokenType() throws UaException {
    try {
      return readUserTokenTypeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUserTokenType(UserTokenPolicy value) throws UaException {
    try {
      writeUserTokenTypeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UserTokenPolicy> readUserTokenTypeAsync() {
    return getUserTokenTypeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), UserTokenPolicy.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeUserTokenTypeAsync(UserTokenPolicy userTokenType) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), userTokenType);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getUserTokenTypeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUserTokenTypeNode() throws UaException {
    try {
      return getUserTokenTypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserTokenTypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UserTokenType",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getRequestedKeyCount() throws UaException {
    PropertyTypeNode node = getRequestedKeyCountNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setRequestedKeyCount(UShort value) throws UaException {
    PropertyTypeNode node = getRequestedKeyCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readRequestedKeyCount() throws UaException {
    try {
      return readRequestedKeyCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRequestedKeyCount(UShort value) throws UaException {
    try {
      writeRequestedKeyCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readRequestedKeyCountAsync() {
    return getRequestedKeyCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestedKeyCountAsync(UShort requestedKeyCount) {
    DataValue value = DataValue.valueOnly(new Variant(requestedKeyCount));
    return getRequestedKeyCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRequestedKeyCountNode() throws UaException {
    try {
      return getRequestedKeyCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestedKeyCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RequestedKeyCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getRetryInterval() throws UaException {
    PropertyTypeNode node = getRetryIntervalNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setRetryInterval(Double value) throws UaException {
    PropertyTypeNode node = getRetryIntervalNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readRetryInterval() throws UaException {
    try {
      return readRetryIntervalAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRetryInterval(Double value) throws UaException {
    try {
      writeRetryIntervalAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readRetryIntervalAsync() {
    return getRetryIntervalNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeRetryIntervalAsync(Double retryInterval) {
    DataValue value = DataValue.valueOnly(new Variant(retryInterval));
    return getRetryIntervalNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRetryIntervalNode() throws UaException {
    try {
      return getRetryIntervalNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRetryIntervalNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RetryInterval",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getLastPushExecutionTime() throws UaException {
    PropertyTypeNode node = getLastPushExecutionTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastPushExecutionTime(DateTime value) throws UaException {
    PropertyTypeNode node = getLastPushExecutionTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readLastPushExecutionTime() throws UaException {
    try {
      return readLastPushExecutionTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastPushExecutionTime(DateTime value) throws UaException {
    try {
      writeLastPushExecutionTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readLastPushExecutionTimeAsync() {
    return getLastPushExecutionTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastPushExecutionTimeAsync(
      DateTime lastPushExecutionTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastPushExecutionTime));
    return getLastPushExecutionTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastPushExecutionTimeNode() throws UaException {
    try {
      return getLastPushExecutionTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastPushExecutionTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastPushExecutionTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getLastPushErrorTime() throws UaException {
    PropertyTypeNode node = getLastPushErrorTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastPushErrorTime(DateTime value) throws UaException {
    PropertyTypeNode node = getLastPushErrorTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readLastPushErrorTime() throws UaException {
    try {
      return readLastPushErrorTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastPushErrorTime(DateTime value) throws UaException {
    try {
      writeLastPushErrorTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readLastPushErrorTimeAsync() {
    return getLastPushErrorTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastPushErrorTimeAsync(DateTime lastPushErrorTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastPushErrorTime));
    return getLastPushErrorTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastPushErrorTimeNode() throws UaException {
    try {
      return getLastPushErrorTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastPushErrorTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastPushErrorTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
