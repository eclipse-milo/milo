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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DataSetWriterTypeNode extends BaseObjectTypeNode implements DataSetWriterType {
  public DataSetWriterTypeNode(
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
  public UShort getDataSetWriterId() throws UaException {
    PropertyTypeNode node = getDataSetWriterIdNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetWriterId(UShort value) throws UaException {
    PropertyTypeNode node = getDataSetWriterIdNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readDataSetWriterId() throws UaException {
    try {
      return readDataSetWriterIdAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetWriterId(UShort value) throws UaException {
    try {
      writeDataSetWriterIdAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readDataSetWriterIdAsync() {
    return getDataSetWriterIdNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetWriterIdAsync(UShort dataSetWriterId) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetWriterId));
    return getDataSetWriterIdNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetWriterIdNode() throws UaException {
    try {
      return getDataSetWriterIdNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterIdNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetWriterId",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DataSetFieldContentMask getDataSetFieldContentMask() throws UaException {
    PropertyTypeNode node = getDataSetFieldContentMaskNode();
    return (DataSetFieldContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetFieldContentMask(DataSetFieldContentMask value) throws UaException {
    PropertyTypeNode node = getDataSetFieldContentMaskNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DataSetFieldContentMask readDataSetFieldContentMask() throws UaException {
    try {
      return readDataSetFieldContentMaskAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetFieldContentMask(DataSetFieldContentMask value) throws UaException {
    try {
      writeDataSetFieldContentMaskAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetFieldContentMask> readDataSetFieldContentMaskAsync() {
    return getDataSetFieldContentMaskNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DataSetFieldContentMask) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      DataSetFieldContentMask dataSetFieldContentMask) {
    DataValue value = DataValue.valueOnly(new Variant(dataSetFieldContentMask));
    return getDataSetFieldContentMaskNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetFieldContentMaskNode() throws UaException {
    try {
      return getDataSetFieldContentMaskNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetFieldContentMaskNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetFieldContentMask",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getKeyFrameCount() throws UaException {
    PropertyTypeNode node = getKeyFrameCountNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setKeyFrameCount(UInteger value) throws UaException {
    PropertyTypeNode node = getKeyFrameCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readKeyFrameCount() throws UaException {
    try {
      return readKeyFrameCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeKeyFrameCount(UInteger value) throws UaException {
    try {
      writeKeyFrameCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readKeyFrameCountAsync() {
    return getKeyFrameCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeKeyFrameCountAsync(UInteger keyFrameCount) {
    DataValue value = DataValue.valueOnly(new Variant(keyFrameCount));
    return getKeyFrameCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getKeyFrameCountNode() throws UaException {
    try {
      return getKeyFrameCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getKeyFrameCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "KeyFrameCount",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public KeyValuePair[] getDataSetWriterProperties() throws UaException {
    PropertyTypeNode node = getDataSetWriterPropertiesNode();
    return cast(node.getValue().getValue().getValue(), KeyValuePair[].class);
  }

  @Override
  public void setDataSetWriterProperties(KeyValuePair[] value) throws UaException {
    PropertyTypeNode node = getDataSetWriterPropertiesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public KeyValuePair[] readDataSetWriterProperties() throws UaException {
    try {
      return readDataSetWriterPropertiesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDataSetWriterProperties(KeyValuePair[] value) throws UaException {
    try {
      writeDataSetWriterPropertiesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends KeyValuePair[]> readDataSetWriterPropertiesAsync() {
    return getDataSetWriterPropertiesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), KeyValuePair[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetWriterPropertiesAsync(
      KeyValuePair[] dataSetWriterProperties) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), dataSetWriterProperties);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getDataSetWriterPropertiesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDataSetWriterPropertiesNode() throws UaException {
    try {
      return getDataSetWriterPropertiesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterPropertiesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DataSetWriterProperties",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DataSetWriterTransportTypeNode getTransportSettingsNode() throws UaException {
    try {
      return getTransportSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetWriterTransportTypeNode>
      getTransportSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "TransportSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (DataSetWriterTransportTypeNode) node);
  }

  @Override
  public DataSetWriterMessageTypeNode getMessageSettingsNode() throws UaException {
    try {
      return getMessageSettingsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends DataSetWriterMessageTypeNode> getMessageSettingsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MessageSettings",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (DataSetWriterMessageTypeNode) node);
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
  public PubSubDiagnosticsDataSetWriterTypeNode getDiagnosticsNode() throws UaException {
    try {
      return getDiagnosticsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PubSubDiagnosticsDataSetWriterTypeNode>
      getDiagnosticsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Diagnostics",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (PubSubDiagnosticsDataSetWriterTypeNode) node);
  }
}
