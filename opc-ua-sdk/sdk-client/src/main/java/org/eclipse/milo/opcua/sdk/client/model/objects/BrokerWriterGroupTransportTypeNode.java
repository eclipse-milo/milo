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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class BrokerWriterGroupTransportTypeNode extends WriterGroupTransportTypeNode
    implements BrokerWriterGroupTransportType {
  public BrokerWriterGroupTransportTypeNode(
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
  public String getQueueName() throws UaException {
    PropertyTypeNode node = getQueueNameNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setQueueName(String value) throws UaException {
    PropertyTypeNode node = getQueueNameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readQueueName() throws UaException {
    try {
      return readQueueNameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeQueueName(String value) throws UaException {
    try {
      writeQueueNameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readQueueNameAsync() {
    return getQueueNameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeQueueNameAsync(String queueName) {
    DataValue value = DataValue.valueOnly(new Variant(queueName));
    return getQueueNameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getQueueNameNode() throws UaException {
    try {
      return getQueueNameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getQueueNameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "QueueName", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getResourceUri() throws UaException {
    PropertyTypeNode node = getResourceUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setResourceUri(String value) throws UaException {
    PropertyTypeNode node = getResourceUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readResourceUri() throws UaException {
    try {
      return readResourceUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeResourceUri(String value) throws UaException {
    try {
      writeResourceUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readResourceUriAsync() {
    return getResourceUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeResourceUriAsync(String resourceUri) {
    DataValue value = DataValue.valueOnly(new Variant(resourceUri));
    return getResourceUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getResourceUriNode() throws UaException {
    try {
      return getResourceUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getResourceUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ResourceUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getAuthenticationProfileUri() throws UaException {
    PropertyTypeNode node = getAuthenticationProfileUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setAuthenticationProfileUri(String value) throws UaException {
    PropertyTypeNode node = getAuthenticationProfileUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readAuthenticationProfileUri() throws UaException {
    try {
      return readAuthenticationProfileUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeAuthenticationProfileUri(String value) throws UaException {
    try {
      writeAuthenticationProfileUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readAuthenticationProfileUriAsync() {
    return getAuthenticationProfileUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeAuthenticationProfileUriAsync(
      String authenticationProfileUri) {
    DataValue value = DataValue.valueOnly(new Variant(authenticationProfileUri));
    return getAuthenticationProfileUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getAuthenticationProfileUriNode() throws UaException {
    try {
      return getAuthenticationProfileUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAuthenticationProfileUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "AuthenticationProfileUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() throws UaException {
    PropertyTypeNode node = getRequestedDeliveryGuaranteeNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer) {
      return BrokerTransportQualityOfService.from((Integer) value);
    } else if (value instanceof BrokerTransportQualityOfService) {
      return (BrokerTransportQualityOfService) value;
    } else {
      return null;
    }
  }

  @Override
  public void setRequestedDeliveryGuarantee(BrokerTransportQualityOfService value)
      throws UaException {
    PropertyTypeNode node = getRequestedDeliveryGuaranteeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public BrokerTransportQualityOfService readRequestedDeliveryGuarantee() throws UaException {
    try {
      return readRequestedDeliveryGuaranteeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRequestedDeliveryGuarantee(BrokerTransportQualityOfService value)
      throws UaException {
    try {
      writeRequestedDeliveryGuaranteeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends BrokerTransportQualityOfService>
      readRequestedDeliveryGuaranteeAsync() {
    return getRequestedDeliveryGuaranteeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer) {
                return BrokerTransportQualityOfService.from((Integer) value);
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestedDeliveryGuaranteeAsync(
      BrokerTransportQualityOfService requestedDeliveryGuarantee) {
    DataValue value = DataValue.valueOnly(new Variant(requestedDeliveryGuarantee));
    return getRequestedDeliveryGuaranteeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRequestedDeliveryGuaranteeNode() throws UaException {
    try {
      return getRequestedDeliveryGuaranteeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestedDeliveryGuaranteeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RequestedDeliveryGuarantee",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
