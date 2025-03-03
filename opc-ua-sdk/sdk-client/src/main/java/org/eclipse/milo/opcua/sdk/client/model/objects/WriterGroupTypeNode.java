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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class WriterGroupTypeNode extends PubSubGroupTypeNode implements WriterGroupType {
  public WriterGroupTypeNode(
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
  public UShort getWriterGroupId() throws UaException {
    PropertyTypeNode node = getWriterGroupIdNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setWriterGroupId(UShort value) throws UaException {
    PropertyTypeNode node = getWriterGroupIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readWriterGroupId() throws UaException {
    try {
      return readWriterGroupIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeWriterGroupId(UShort value) throws UaException {
    try {
      writeWriterGroupIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readWriterGroupIdAsync() {
    return getWriterGroupIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeWriterGroupIdAsync(UShort writerGroupId) {
    DataValue value = DataValue.valueOnly(new Variant(writerGroupId));
    return getWriterGroupIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getWriterGroupIdNode() throws UaException {
    try {
      return getWriterGroupIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getWriterGroupIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "WriterGroupId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getPublishingInterval() throws UaException {
    PropertyTypeNode node = getPublishingIntervalNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublishingInterval(Double value) throws UaException {
    PropertyTypeNode node = getPublishingIntervalNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readPublishingInterval() throws UaException {
    try {
      return readPublishingIntervalAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePublishingInterval(Double value) throws UaException {
    try {
      writePublishingIntervalAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readPublishingIntervalAsync() {
    return getPublishingIntervalNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingIntervalAsync(Double publishingInterval) {
    DataValue value = DataValue.valueOnly(new Variant(publishingInterval));
    return getPublishingIntervalNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() throws UaException {
    try {
      return getPublishingIntervalNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishingIntervalNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "PublishingInterval",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Double getKeepAliveTime() throws UaException {
    PropertyTypeNode node = getKeepAliveTimeNode();
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setKeepAliveTime(Double value) throws UaException {
    PropertyTypeNode node = getKeepAliveTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Double readKeepAliveTime() throws UaException {
    try {
      return readKeepAliveTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeKeepAliveTime(Double value) throws UaException {
    try {
      writeKeepAliveTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Double> readKeepAliveTimeAsync() {
    return getKeepAliveTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Double) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeKeepAliveTimeAsync(Double keepAliveTime) {
    DataValue value = DataValue.valueOnly(new Variant(keepAliveTime));
    return getKeepAliveTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getKeepAliveTimeNode() throws UaException {
    try {
      return getKeepAliveTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getKeepAliveTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "KeepAliveTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UByte getPriority() throws UaException {
    PropertyTypeNode node = getPriorityNode();
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setPriority(UByte value) throws UaException {
    PropertyTypeNode node = getPriorityNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UByte readPriority() throws UaException {
    try {
      return readPriorityAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writePriority(UByte value) throws UaException {
    try {
      writePriorityAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UByte> readPriorityAsync() {
    return getPriorityNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UByte) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityAsync(UByte priority) {
    DataValue value = DataValue.valueOnly(new Variant(priority));
    return getPriorityNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getPriorityNode() throws UaException {
    try {
      return getPriorityNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPriorityNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Priority", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String[] getLocaleIds() throws UaException {
    PropertyTypeNode node = getLocaleIdsNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLocaleIds(String[] value) throws UaException {
    PropertyTypeNode node = getLocaleIdsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readLocaleIds() throws UaException {
    try {
      return readLocaleIdsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLocaleIds(String[] value) throws UaException {
    try {
      writeLocaleIdsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readLocaleIdsAsync() {
    return getLocaleIdsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLocaleIdsAsync(String[] localeIds) {
    DataValue value = DataValue.valueOnly(new Variant(localeIds));
    return getLocaleIdsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLocaleIdsNode() throws UaException {
    try {
      return getLocaleIdsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLocaleIdsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "LocaleIds", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getHeaderLayoutUri() throws UaException {
    PropertyTypeNode node = getHeaderLayoutUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setHeaderLayoutUri(String value) throws UaException {
    PropertyTypeNode node = getHeaderLayoutUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readHeaderLayoutUri() throws UaException {
    try {
      return readHeaderLayoutUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeHeaderLayoutUri(String value) throws UaException {
    try {
      writeHeaderLayoutUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readHeaderLayoutUriAsync() {
    return getHeaderLayoutUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(String headerLayoutUri) {
    DataValue value = DataValue.valueOnly(new Variant(headerLayoutUri));
    return getHeaderLayoutUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getHeaderLayoutUriNode() throws UaException {
    try {
      return getHeaderLayoutUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHeaderLayoutUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "HeaderLayoutUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public WriterGroupTransportTypeNode getTransportSettingsNode() throws UaException {
    try {
      return getTransportSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends WriterGroupTransportTypeNode> getTransportSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TransportSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (WriterGroupTransportTypeNode) node);
  }

  @Override
  public WriterGroupMessageTypeNode getMessageSettingsNode() throws UaException {
    try {
      return getMessageSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends WriterGroupMessageTypeNode> getMessageSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MessageSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (WriterGroupMessageTypeNode) node);
  }

  @Override
  public PubSubDiagnosticsWriterGroupTypeNode getDiagnosticsNode() throws UaException {
    try {
      return getDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsWriterGroupTypeNode>
      getDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Diagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (PubSubDiagnosticsWriterGroupTypeNode) node);
  }
}
