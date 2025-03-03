/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AlarmStateVariableTypeNode extends BaseDataVariableTypeNode
    implements AlarmStateVariableType {
  public AlarmStateVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public UShort getHighestActiveSeverity() throws UaException {
    PropertyTypeNode node = getHighestActiveSeverityNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighestActiveSeverity(UShort value) throws UaException {
    PropertyTypeNode node = getHighestActiveSeverityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readHighestActiveSeverity() throws UaException {
    try {
      return readHighestActiveSeverityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighestActiveSeverity(UShort value) throws UaException {
    try {
      writeHighestActiveSeverityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readHighestActiveSeverityAsync() {
    return getHighestActiveSeverityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighestActiveSeverityAsync(
      UShort highestActiveSeverity) {
    DataValue value = DataValue.valueOnly(new Variant(highestActiveSeverity));
    return getHighestActiveSeverityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHighestActiveSeverityNode() throws UaException {
    try {
      return getHighestActiveSeverityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighestActiveSeverityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighestActiveSeverity",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getHighestUnackSeverity() throws UaException {
    PropertyTypeNode node = getHighestUnackSeverityNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighestUnackSeverity(UShort value) throws UaException {
    PropertyTypeNode node = getHighestUnackSeverityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readHighestUnackSeverity() throws UaException {
    try {
      return readHighestUnackSeverityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHighestUnackSeverity(UShort value) throws UaException {
    try {
      writeHighestUnackSeverityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readHighestUnackSeverityAsync() {
    return getHighestUnackSeverityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHighestUnackSeverityAsync(UShort highestUnackSeverity) {
    DataValue value = DataValue.valueOnly(new Variant(highestUnackSeverity));
    return getHighestUnackSeverityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHighestUnackSeverityNode() throws UaException {
    try {
      return getHighestUnackSeverityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighestUnackSeverityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HighestUnackSeverity",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getActiveCount() throws UaException {
    PropertyTypeNode node = getActiveCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setActiveCount(UInteger value) throws UaException {
    PropertyTypeNode node = getActiveCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readActiveCount() throws UaException {
    try {
      return readActiveCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeActiveCount(UInteger value) throws UaException {
    try {
      writeActiveCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readActiveCountAsync() {
    return getActiveCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeActiveCountAsync(UInteger activeCount) {
    DataValue value = DataValue.valueOnly(new Variant(activeCount));
    return getActiveCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getActiveCountNode() throws UaException {
    try {
      return getActiveCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActiveCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ActiveCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getUnacknowledgedCount() throws UaException {
    PropertyTypeNode node = getUnacknowledgedCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUnacknowledgedCount(UInteger value) throws UaException {
    PropertyTypeNode node = getUnacknowledgedCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readUnacknowledgedCount() throws UaException {
    try {
      return readUnacknowledgedCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUnacknowledgedCount(UInteger value) throws UaException {
    try {
      writeUnacknowledgedCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readUnacknowledgedCountAsync() {
    return getUnacknowledgedCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUnacknowledgedCountAsync(UInteger unacknowledgedCount) {
    DataValue value = DataValue.valueOnly(new Variant(unacknowledgedCount));
    return getUnacknowledgedCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUnacknowledgedCountNode() throws UaException {
    try {
      return getUnacknowledgedCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUnacknowledgedCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UnacknowledgedCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getUnconfirmedCount() throws UaException {
    PropertyTypeNode node = getUnconfirmedCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUnconfirmedCount(UInteger value) throws UaException {
    PropertyTypeNode node = getUnconfirmedCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readUnconfirmedCount() throws UaException {
    try {
      return readUnconfirmedCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUnconfirmedCount(UInteger value) throws UaException {
    try {
      writeUnconfirmedCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readUnconfirmedCountAsync() {
    return getUnconfirmedCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUnconfirmedCountAsync(UInteger unconfirmedCount) {
    DataValue value = DataValue.valueOnly(new Variant(unconfirmedCount));
    return getUnconfirmedCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUnconfirmedCountNode() throws UaException {
    try {
      return getUnconfirmedCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUnconfirmedCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UnconfirmedCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public ContentFilter getFilter() throws UaException {
    PropertyTypeNode node = getFilterNode();
    return cast(node.getValue().getValue().getValue(), ContentFilter.class);
  }

  @Override
  public void setFilter(ContentFilter value) throws UaException {
    PropertyTypeNode node = getFilterNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public ContentFilter readFilter() throws UaException {
    try {
      return readFilterAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeFilter(ContentFilter value) throws UaException {
    try {
      writeFilterAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ContentFilter> readFilterAsync() {
    return getFilterNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), ContentFilter.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeFilterAsync(ContentFilter filter) {
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), filter);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getFilterNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getFilterNode() throws UaException {
    try {
      return getFilterNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getFilterNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Filter", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
