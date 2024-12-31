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
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubConnectionTypeNode extends BaseObjectTypeNode implements PubSubConnectionType {
  public PubSubConnectionTypeNode(
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
  public Object getPublisherId() throws UaException {
    PropertyTypeNode node = getPublisherIdNode();
    return (Object) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublisherId(Object value) throws UaException {
    PropertyTypeNode node = getPublisherIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Object readPublisherId() throws UaException {
    try {
      return readPublisherIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePublisherId(Object value) throws UaException {
    try {
      writePublisherIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<?> readPublisherIdAsync() {
    return getPublisherIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Object) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePublisherIdAsync(Object publisherId) {
    DataValue value = DataValue.valueOnly(new Variant(publisherId));
    return getPublisherIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPublisherIdNode() throws UaException {
    try {
      return getPublisherIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublisherIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PublisherId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public KeyValuePair[] getConnectionProperties() throws UaException {
    PropertyTypeNode node = getConnectionPropertiesNode();
    return cast(node.getValue().getValue().getValue(), KeyValuePair[].class);
  }

  @Override
  public void setConnectionProperties(KeyValuePair[] value) throws UaException {
    PropertyTypeNode node = getConnectionPropertiesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public KeyValuePair[] readConnectionProperties() throws UaException {
    try {
      return readConnectionPropertiesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConnectionProperties(KeyValuePair[] value) throws UaException {
    try {
      writeConnectionPropertiesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends KeyValuePair[]> readConnectionPropertiesAsync() {
    return getConnectionPropertiesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), KeyValuePair[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeConnectionPropertiesAsync(
      KeyValuePair[] connectionProperties) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), connectionProperties);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getConnectionPropertiesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConnectionPropertiesNode() throws UaException {
    try {
      return getConnectionPropertiesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConnectionPropertiesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConnectionProperties",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getTransportProfileUri() throws UaException {
    SelectionListTypeNode node = getTransportProfileUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setTransportProfileUri(String value) throws UaException {
    SelectionListTypeNode node = getTransportProfileUriNode();
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
  public SelectionListTypeNode getTransportProfileUriNode() throws UaException {
    try {
      return getTransportProfileUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends SelectionListTypeNode> getTransportProfileUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TransportProfileUri",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (SelectionListTypeNode) node);
  }

  @Override
  public NetworkAddressTypeNode getAddressNode() throws UaException {
    try {
      return getAddressNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends NetworkAddressTypeNode> getAddressNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Address", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (NetworkAddressTypeNode) node);
  }

  @Override
  public ConnectionTransportTypeNode getTransportSettingsNode() throws UaException {
    try {
      return getTransportSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ConnectionTransportTypeNode> getTransportSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TransportSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ConnectionTransportTypeNode) node);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() throws UaException {
    try {
      return getStatusNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubStatusTypeNode> getStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Status", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (PubSubStatusTypeNode) node);
  }

  @Override
  public PubSubDiagnosticsConnectionTypeNode getDiagnosticsNode() throws UaException {
    try {
      return getDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsConnectionTypeNode>
      getDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Diagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (PubSubDiagnosticsConnectionTypeNode) node);
  }
}
