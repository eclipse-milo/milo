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

public class PubSubCapabilitiesTypeNode extends BaseObjectTypeNode
    implements PubSubCapabilitiesType {
  public PubSubCapabilitiesTypeNode(
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
  public UInteger getMaxPubSubConnections() throws UaException {
    PropertyTypeNode node = getMaxPubSubConnectionsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxPubSubConnections(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxPubSubConnectionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxPubSubConnections() throws UaException {
    try {
      return readMaxPubSubConnectionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxPubSubConnections(UInteger value) throws UaException {
    try {
      writeMaxPubSubConnectionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxPubSubConnectionsAsync() {
    return getMaxPubSubConnectionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxPubSubConnectionsAsync(
      UInteger maxPubSubConnections) {
    DataValue value = DataValue.valueOnly(new Variant(maxPubSubConnections));
    return getMaxPubSubConnectionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxPubSubConnectionsNode() throws UaException {
    try {
      return getMaxPubSubConnectionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxPubSubConnectionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxPubSubConnections",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxWriterGroups() throws UaException {
    PropertyTypeNode node = getMaxWriterGroupsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxWriterGroups(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxWriterGroupsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxWriterGroups() throws UaException {
    try {
      return readMaxWriterGroupsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxWriterGroups(UInteger value) throws UaException {
    try {
      writeMaxWriterGroupsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxWriterGroupsAsync() {
    return getMaxWriterGroupsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxWriterGroupsAsync(UInteger maxWriterGroups) {
    DataValue value = DataValue.valueOnly(new Variant(maxWriterGroups));
    return getMaxWriterGroupsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxWriterGroupsNode() throws UaException {
    try {
      return getMaxWriterGroupsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxWriterGroupsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxWriterGroups",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxReaderGroups() throws UaException {
    PropertyTypeNode node = getMaxReaderGroupsNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReaderGroups(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxReaderGroupsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxReaderGroups() throws UaException {
    try {
      return readMaxReaderGroupsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxReaderGroups(UInteger value) throws UaException {
    try {
      writeMaxReaderGroupsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxReaderGroupsAsync() {
    return getMaxReaderGroupsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxReaderGroupsAsync(UInteger maxReaderGroups) {
    DataValue value = DataValue.valueOnly(new Variant(maxReaderGroups));
    return getMaxReaderGroupsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxReaderGroupsNode() throws UaException {
    try {
      return getMaxReaderGroupsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReaderGroupsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxReaderGroups",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxDataSetWriters() throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxDataSetWriters(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxDataSetWriters() throws UaException {
    try {
      return readMaxDataSetWritersAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxDataSetWriters(UInteger value) throws UaException {
    try {
      writeMaxDataSetWritersAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxDataSetWritersAsync() {
    return getMaxDataSetWritersNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxDataSetWritersAsync(UInteger maxDataSetWriters) {
    DataValue value = DataValue.valueOnly(new Variant(maxDataSetWriters));
    return getMaxDataSetWritersNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxDataSetWritersNode() throws UaException {
    try {
      return getMaxDataSetWritersNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxDataSetWritersNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxDataSetWriters",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxDataSetReaders() throws UaException {
    PropertyTypeNode node = getMaxDataSetReadersNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxDataSetReaders(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxDataSetReadersNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxDataSetReaders() throws UaException {
    try {
      return readMaxDataSetReadersAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxDataSetReaders(UInteger value) throws UaException {
    try {
      writeMaxDataSetReadersAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxDataSetReadersAsync() {
    return getMaxDataSetReadersNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxDataSetReadersAsync(UInteger maxDataSetReaders) {
    DataValue value = DataValue.valueOnly(new Variant(maxDataSetReaders));
    return getMaxDataSetReadersNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxDataSetReadersNode() throws UaException {
    try {
      return getMaxDataSetReadersNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxDataSetReadersNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxDataSetReaders",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxFieldsPerDataSet() throws UaException {
    PropertyTypeNode node = getMaxFieldsPerDataSetNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxFieldsPerDataSet(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxFieldsPerDataSetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxFieldsPerDataSet() throws UaException {
    try {
      return readMaxFieldsPerDataSetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxFieldsPerDataSet(UInteger value) throws UaException {
    try {
      writeMaxFieldsPerDataSetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxFieldsPerDataSetAsync() {
    return getMaxFieldsPerDataSetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFieldsPerDataSetAsync(UInteger maxFieldsPerDataSet) {
    DataValue value = DataValue.valueOnly(new Variant(maxFieldsPerDataSet));
    return getMaxFieldsPerDataSetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxFieldsPerDataSetNode() throws UaException {
    try {
      return getMaxFieldsPerDataSetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxFieldsPerDataSetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxFieldsPerDataSet",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxDataSetWritersPerGroup() throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersPerGroupNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxDataSetWritersPerGroup(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersPerGroupNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxDataSetWritersPerGroup() throws UaException {
    try {
      return readMaxDataSetWritersPerGroupAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxDataSetWritersPerGroup(UInteger value) throws UaException {
    try {
      writeMaxDataSetWritersPerGroupAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxDataSetWritersPerGroupAsync() {
    return getMaxDataSetWritersPerGroupNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxDataSetWritersPerGroupAsync(
      UInteger maxDataSetWritersPerGroup) {
    DataValue value = DataValue.valueOnly(new Variant(maxDataSetWritersPerGroup));
    return getMaxDataSetWritersPerGroupNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxDataSetWritersPerGroupNode() throws UaException {
    try {
      return getMaxDataSetWritersPerGroupNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxDataSetWritersPerGroupNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxDataSetWritersPerGroup",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNetworkMessageSizeDatagram() throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeDatagramNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNetworkMessageSizeDatagram(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeDatagramNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNetworkMessageSizeDatagram() throws UaException {
    try {
      return readMaxNetworkMessageSizeDatagramAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNetworkMessageSizeDatagram(UInteger value) throws UaException {
    try {
      writeMaxNetworkMessageSizeDatagramAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNetworkMessageSizeDatagramAsync() {
    return getMaxNetworkMessageSizeDatagramNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNetworkMessageSizeDatagramAsync(
      UInteger maxNetworkMessageSizeDatagram) {
    DataValue value = DataValue.valueOnly(new Variant(maxNetworkMessageSizeDatagram));
    return getMaxNetworkMessageSizeDatagramNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNetworkMessageSizeDatagramNode() throws UaException {
    try {
      return getMaxNetworkMessageSizeDatagramNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNetworkMessageSizeDatagramNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNetworkMessageSizeDatagram",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxNetworkMessageSizeBroker() throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeBrokerNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNetworkMessageSizeBroker(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeBrokerNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxNetworkMessageSizeBroker() throws UaException {
    try {
      return readMaxNetworkMessageSizeBrokerAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxNetworkMessageSizeBroker(UInteger value) throws UaException {
    try {
      writeMaxNetworkMessageSizeBrokerAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxNetworkMessageSizeBrokerAsync() {
    return getMaxNetworkMessageSizeBrokerNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxNetworkMessageSizeBrokerAsync(
      UInteger maxNetworkMessageSizeBroker) {
    DataValue value = DataValue.valueOnly(new Variant(maxNetworkMessageSizeBroker));
    return getMaxNetworkMessageSizeBrokerNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxNetworkMessageSizeBrokerNode() throws UaException {
    try {
      return getMaxNetworkMessageSizeBrokerNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxNetworkMessageSizeBrokerNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxNetworkMessageSizeBroker",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getSupportSecurityKeyPull() throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPullNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSupportSecurityKeyPull(Boolean value) throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPullNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readSupportSecurityKeyPull() throws UaException {
    try {
      return readSupportSecurityKeyPullAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSupportSecurityKeyPull(Boolean value) throws UaException {
    try {
      writeSupportSecurityKeyPullAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readSupportSecurityKeyPullAsync() {
    return getSupportSecurityKeyPullNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportSecurityKeyPullAsync(
      Boolean supportSecurityKeyPull) {
    DataValue value = DataValue.valueOnly(new Variant(supportSecurityKeyPull));
    return getSupportSecurityKeyPullNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSupportSecurityKeyPullNode() throws UaException {
    try {
      return getSupportSecurityKeyPullNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSupportSecurityKeyPullNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SupportSecurityKeyPull",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getSupportSecurityKeyPush() throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPushNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSupportSecurityKeyPush(Boolean value) throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPushNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readSupportSecurityKeyPush() throws UaException {
    try {
      return readSupportSecurityKeyPushAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSupportSecurityKeyPush(Boolean value) throws UaException {
    try {
      writeSupportSecurityKeyPushAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readSupportSecurityKeyPushAsync() {
    return getSupportSecurityKeyPushNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportSecurityKeyPushAsync(
      Boolean supportSecurityKeyPush) {
    DataValue value = DataValue.valueOnly(new Variant(supportSecurityKeyPush));
    return getSupportSecurityKeyPushNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSupportSecurityKeyPushNode() throws UaException {
    try {
      return getSupportSecurityKeyPushNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSupportSecurityKeyPushNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SupportSecurityKeyPush",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}
