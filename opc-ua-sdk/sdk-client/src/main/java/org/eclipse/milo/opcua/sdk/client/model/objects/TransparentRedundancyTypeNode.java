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
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class TransparentRedundancyTypeNode extends ServerRedundancyTypeNode
    implements TransparentRedundancyType {
  public TransparentRedundancyTypeNode(
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
  public String getCurrentServerId() throws UaException {
    PropertyTypeNode node = getCurrentServerIdNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentServerId(String value) throws UaException {
    PropertyTypeNode node = getCurrentServerIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readCurrentServerId() throws UaException {
    try {
      return readCurrentServerIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCurrentServerId(String value) throws UaException {
    try {
      writeCurrentServerIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readCurrentServerIdAsync() {
    return getCurrentServerIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentServerIdAsync(String currentServerId) {
    DataValue value = DataValue.valueOnly(new Variant(currentServerId));
    return getCurrentServerIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCurrentServerIdNode() throws UaException {
    try {
      return getCurrentServerIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCurrentServerIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CurrentServerId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public RedundantServerDataType[] getRedundantServerArray() throws UaException {
    PropertyTypeNode node = getRedundantServerArrayNode();
    return cast(node.getValue().getValue().getValue(), RedundantServerDataType[].class);
  }

  @Override
  public void setRedundantServerArray(RedundantServerDataType[] value) throws UaException {
    PropertyTypeNode node = getRedundantServerArrayNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public RedundantServerDataType[] readRedundantServerArray() throws UaException {
    try {
      return readRedundantServerArrayAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeRedundantServerArray(RedundantServerDataType[] value) throws UaException {
    try {
      writeRedundantServerArrayAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends RedundantServerDataType[]> readRedundantServerArrayAsync() {
    return getRedundantServerArrayNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), RedundantServerDataType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeRedundantServerArrayAsync(
      RedundantServerDataType[] redundantServerArray) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), redundantServerArray);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getRedundantServerArrayNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getRedundantServerArrayNode() throws UaException {
    try {
      return getRedundantServerArrayNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRedundantServerArrayNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "RedundantServerArray",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
