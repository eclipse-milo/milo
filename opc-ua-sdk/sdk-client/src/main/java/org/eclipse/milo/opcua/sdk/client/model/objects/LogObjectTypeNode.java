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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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

public class LogObjectTypeNode extends BaseObjectTypeNode implements LogObjectType {
  public LogObjectTypeNode(
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
  public UInteger getMaxRecords() throws UaException {
    PropertyTypeNode node = getMaxRecordsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxRecords(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxRecordsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxRecords() throws UaException {
    try {
      return readMaxRecordsAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeMaxRecords(UInteger value) throws UaException {
    try {
      writeMaxRecordsAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxRecordsAsync() {
    return getMaxRecordsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxRecordsAsync(UInteger maxRecords) {
    DataValue value = DataValue.valueOnly(new Variant(maxRecords));
    return getMaxRecordsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxRecordsNode() throws UaException {
    try {
      return getMaxRecordsNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxRecordsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "MaxRecords", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getMaxStorageDuration() throws UaException {
    PropertyTypeNode node = getMaxStorageDurationNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxStorageDuration(Double value) throws UaException {
    PropertyTypeNode node = getMaxStorageDurationNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readMaxStorageDuration() throws UaException {
    try {
      return readMaxStorageDurationAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeMaxStorageDuration(Double value) throws UaException {
    try {
      writeMaxStorageDurationAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends Double> readMaxStorageDurationAsync() {
    return getMaxStorageDurationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxStorageDurationAsync(Double maxStorageDuration) {
    DataValue value = DataValue.valueOnly(new Variant(maxStorageDuration));
    return getMaxStorageDurationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxStorageDurationNode() throws UaException {
    try {
      return getMaxStorageDurationNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxStorageDurationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxStorageDuration",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getMinimumSeverity() throws UaException {
    PropertyTypeNode node = getMinimumSeverityNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMinimumSeverity(UShort value) throws UaException {
    PropertyTypeNode node = getMinimumSeverityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readMinimumSeverity() throws UaException {
    try {
      return readMinimumSeverityAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeMinimumSeverity(UShort value) throws UaException {
    try {
      writeMinimumSeverityAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readMinimumSeverityAsync() {
    return getMinimumSeverityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMinimumSeverityAsync(UShort minimumSeverity) {
    DataValue value = DataValue.valueOnly(new Variant(minimumSeverity));
    return getMinimumSeverityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMinimumSeverityNode() throws UaException {
    try {
      return getMinimumSeverityNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMinimumSeverityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "MinimumSeverity", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
