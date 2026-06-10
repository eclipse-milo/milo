/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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

public class ConfigurationFileTypeNode extends FileTypeNode implements ConfigurationFileType {
  public ConfigurationFileTypeNode(
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
  public DateTime getLastUpdateTime() throws UaException {
    PropertyTypeNode node = getLastUpdateTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastUpdateTime(DateTime value) throws UaException {
    PropertyTypeNode node = getLastUpdateTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readLastUpdateTime() throws UaException {
    try {
      return readLastUpdateTimeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeLastUpdateTime(DateTime value) throws UaException {
    try {
      writeLastUpdateTimeAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readLastUpdateTimeAsync() {
    return getLastUpdateTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastUpdateTimeAsync(DateTime lastUpdateTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastUpdateTime));
    return getLastUpdateTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastUpdateTimeNode() throws UaException {
    try {
      return getLastUpdateTimeNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastUpdateTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "LastUpdateTime", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getCurrentVersion() throws UaException {
    PropertyTypeNode node = getCurrentVersionNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentVersion(UInteger value) throws UaException {
    PropertyTypeNode node = getCurrentVersionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readCurrentVersion() throws UaException {
    try {
      return readCurrentVersionAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeCurrentVersion(UInteger value) throws UaException {
    try {
      writeCurrentVersionAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readCurrentVersionAsync() {
    return getCurrentVersionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentVersionAsync(UInteger currentVersion) {
    DataValue value = DataValue.valueOnly(new Variant(currentVersion));
    return getCurrentVersionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCurrentVersionNode() throws UaException {
    try {
      return getCurrentVersionNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCurrentVersionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "CurrentVersion", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getActivityTimeout() throws UaException {
    PropertyTypeNode node = getActivityTimeoutNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setActivityTimeout(Double value) throws UaException {
    PropertyTypeNode node = getActivityTimeoutNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readActivityTimeout() throws UaException {
    try {
      return readActivityTimeoutAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeActivityTimeout(Double value) throws UaException {
    try {
      writeActivityTimeoutAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends Double> readActivityTimeoutAsync() {
    return getActivityTimeoutNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeActivityTimeoutAsync(Double activityTimeout) {
    DataValue value = DataValue.valueOnly(new Variant(activityTimeout));
    return getActivityTimeoutNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getActivityTimeoutNode() throws UaException {
    try {
      return getActivityTimeoutNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActivityTimeoutNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "ActivityTimeout", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getSupportedDataType() throws UaException {
    PropertyTypeNode node = getSupportedDataTypeNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setSupportedDataType(NodeId value) throws UaException {
    PropertyTypeNode node = getSupportedDataTypeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readSupportedDataType() throws UaException {
    try {
      return readSupportedDataTypeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeSupportedDataType(NodeId value) throws UaException {
    try {
      writeSupportedDataTypeAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readSupportedDataTypeAsync() {
    return getSupportedDataTypeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportedDataTypeAsync(NodeId supportedDataType) {
    DataValue value = DataValue.valueOnly(new Variant(supportedDataType));
    return getSupportedDataTypeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSupportedDataTypeNode() throws UaException {
    try {
      return getSupportedDataTypeNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSupportedDataTypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SupportedDataType",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
