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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;

public class SerializationEntityTypeNode extends BaseObjectTypeNode
    implements SerializationEntityType {
  public SerializationEntityTypeNode(
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
  public NodeId[] getIncludeReferenceTypes() throws UaException {
    PropertyTypeNode node = getIncludeReferenceTypesNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setIncludeReferenceTypes(NodeId[] value) throws UaException {
    PropertyTypeNode node = getIncludeReferenceTypesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readIncludeReferenceTypes() throws UaException {
    try {
      return readIncludeReferenceTypesAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeIncludeReferenceTypes(NodeId[] value) throws UaException {
    try {
      writeIncludeReferenceTypesAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readIncludeReferenceTypesAsync() {
    return getIncludeReferenceTypesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeReferenceTypesAsync(
      NodeId[] includeReferenceTypes) {
    DataValue value = DataValue.valueOnly(new Variant(includeReferenceTypes));
    return getIncludeReferenceTypesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIncludeReferenceTypesNode() throws UaException {
    try {
      return getIncludeReferenceTypesNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIncludeReferenceTypesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IncludeReferenceTypes",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId[] getExcludeReferenceTypes() throws UaException {
    PropertyTypeNode node = getExcludeReferenceTypesNode();
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setExcludeReferenceTypes(NodeId[] value) throws UaException {
    PropertyTypeNode node = getExcludeReferenceTypesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId[] readExcludeReferenceTypes() throws UaException {
    try {
      return readExcludeReferenceTypesAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeExcludeReferenceTypes(NodeId[] value) throws UaException {
    try {
      writeExcludeReferenceTypesAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId[]> readExcludeReferenceTypesAsync() {
    return getExcludeReferenceTypesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeExcludeReferenceTypesAsync(
      NodeId[] excludeReferenceTypes) {
    DataValue value = DataValue.valueOnly(new Variant(excludeReferenceTypes));
    return getExcludeReferenceTypesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getExcludeReferenceTypesNode() throws UaException {
    try {
      return getExcludeReferenceTypesNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getExcludeReferenceTypesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ExcludeReferenceTypes",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getSerializationDepth() throws UaException {
    PropertyTypeNode node = getSerializationDepthNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setSerializationDepth(UShort value) throws UaException {
    PropertyTypeNode node = getSerializationDepthNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readSerializationDepth() throws UaException {
    try {
      return readSerializationDepthAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeSerializationDepth(UShort value) throws UaException {
    try {
      writeSerializationDepthAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readSerializationDepthAsync() {
    return getSerializationDepthNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSerializationDepthAsync(UShort serializationDepth) {
    DataValue value = DataValue.valueOnly(new Variant(serializationDepth));
    return getSerializationDepthNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSerializationDepthNode() throws UaException {
    try {
      return getSerializationDepthNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSerializationDepthNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "SerializationDepth",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getConsiderSubElementSerializationProperties() throws UaException {
    PropertyTypeNode node = getConsiderSubElementSerializationPropertiesNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setConsiderSubElementSerializationProperties(Boolean value) throws UaException {
    PropertyTypeNode node = getConsiderSubElementSerializationPropertiesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readConsiderSubElementSerializationProperties() throws UaException {
    try {
      return readConsiderSubElementSerializationPropertiesAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeConsiderSubElementSerializationProperties(Boolean value) throws UaException {
    try {
      writeConsiderSubElementSerializationPropertiesAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readConsiderSubElementSerializationPropertiesAsync() {
    return getConsiderSubElementSerializationPropertiesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConsiderSubElementSerializationPropertiesAsync(
      Boolean considerSubElementSerializationProperties) {
    DataValue value = DataValue.valueOnly(new Variant(considerSubElementSerializationProperties));
    return getConsiderSubElementSerializationPropertiesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConsiderSubElementSerializationPropertiesNode() throws UaException {
    try {
      return getConsiderSubElementSerializationPropertiesNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode>
      getConsiderSubElementSerializationPropertiesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConsiderSubElementSerializationProperties",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public KeyValuePair[] getCustomMetaDataProperties() throws UaException {
    PropertyTypeNode node = getCustomMetaDataPropertiesNode();
    return cast(node.getValue().getValue().getValue(), KeyValuePair[].class);
  }

  @Override
  public void setCustomMetaDataProperties(KeyValuePair[] value) throws UaException {
    PropertyTypeNode node = getCustomMetaDataPropertiesNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public KeyValuePair[] readCustomMetaDataProperties() throws UaException {
    try {
      return readCustomMetaDataPropertiesAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeCustomMetaDataProperties(KeyValuePair[] value) throws UaException {
    try {
      writeCustomMetaDataPropertiesAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends KeyValuePair[]> readCustomMetaDataPropertiesAsync() {
    return getCustomMetaDataPropertiesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), KeyValuePair[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeCustomMetaDataPropertiesAsync(
      KeyValuePair[] customMetaDataProperties) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), customMetaDataProperties);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getCustomMetaDataPropertiesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCustomMetaDataPropertiesNode() throws UaException {
    try {
      return getCustomMetaDataPropertiesNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCustomMetaDataPropertiesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CustomMetaDataProperties",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public NodeId getCustomMetaDataRef() throws UaException {
    PropertyTypeNode node = getCustomMetaDataRefNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setCustomMetaDataRef(NodeId value) throws UaException {
    PropertyTypeNode node = getCustomMetaDataRefNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readCustomMetaDataRef() throws UaException {
    try {
      return readCustomMetaDataRefAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeCustomMetaDataRef(NodeId value) throws UaException {
    try {
      writeCustomMetaDataRefAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readCustomMetaDataRefAsync() {
    return getCustomMetaDataRefNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeCustomMetaDataRefAsync(NodeId customMetaDataRef) {
    DataValue value = DataValue.valueOnly(new Variant(customMetaDataRef));
    return getCustomMetaDataRefNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getCustomMetaDataRefNode() throws UaException {
    try {
      return getCustomMetaDataRefNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCustomMetaDataRefNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CustomMetaDataRef",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getIncludeStatus() throws UaException {
    PropertyTypeNode node = getIncludeStatusNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setIncludeStatus(Boolean value) throws UaException {
    PropertyTypeNode node = getIncludeStatusNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readIncludeStatus() throws UaException {
    try {
      return readIncludeStatusAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeIncludeStatus(Boolean value) throws UaException {
    try {
      writeIncludeStatusAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readIncludeStatusAsync() {
    return getIncludeStatusNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeStatusAsync(Boolean includeStatus) {
    DataValue value = DataValue.valueOnly(new Variant(includeStatus));
    return getIncludeStatusNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIncludeStatusNode() throws UaException {
    try {
      return getIncludeStatusNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIncludeStatusNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "IncludeStatus", ExpandedNodeId.parse("i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getIncludeSourceTimestamp() throws UaException {
    PropertyTypeNode node = getIncludeSourceTimestampNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setIncludeSourceTimestamp(Boolean value) throws UaException {
    PropertyTypeNode node = getIncludeSourceTimestampNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readIncludeSourceTimestamp() throws UaException {
    try {
      return readIncludeSourceTimestampAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeIncludeSourceTimestamp(Boolean value) throws UaException {
    try {
      writeIncludeSourceTimestampAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readIncludeSourceTimestampAsync() {
    return getIncludeSourceTimestampNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeSourceTimestampAsync(
      Boolean includeSourceTimestamp) {
    DataValue value = DataValue.valueOnly(new Variant(includeSourceTimestamp));
    return getIncludeSourceTimestampNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIncludeSourceTimestampNode() throws UaException {
    try {
      return getIncludeSourceTimestampNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIncludeSourceTimestampNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IncludeSourceTimestamp",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getIncludeDictionaryReference() throws UaException {
    PropertyTypeNode node = getIncludeDictionaryReferenceNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setIncludeDictionaryReference(Boolean value) throws UaException {
    PropertyTypeNode node = getIncludeDictionaryReferenceNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readIncludeDictionaryReference() throws UaException {
    try {
      return readIncludeDictionaryReferenceAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeIncludeDictionaryReference(Boolean value) throws UaException {
    try {
      writeIncludeDictionaryReferenceAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readIncludeDictionaryReferenceAsync() {
    return getIncludeDictionaryReferenceNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeIncludeDictionaryReferenceAsync(
      Boolean includeDictionaryReference) {
    DataValue value = DataValue.valueOnly(new Variant(includeDictionaryReference));
    return getIncludeDictionaryReferenceNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIncludeDictionaryReferenceNode() throws UaException {
    try {
      return getIncludeDictionaryReferenceNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIncludeDictionaryReferenceNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IncludeDictionaryReference",
            ExpandedNodeId.parse("i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Structure getSerializedData() throws UaException {
    BaseDataVariableTypeNode node = getSerializedDataNode();
    return cast(node.getValue().getValue().getValue(), Structure.class);
  }

  @Override
  public void setSerializedData(Structure value) throws UaException {
    BaseDataVariableTypeNode node = getSerializedDataNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public Structure readSerializedData() throws UaException {
    try {
      return readSerializedDataAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeSerializedData(Structure value) throws UaException {
    try {
      writeSerializedDataAsync(value).get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends Structure> readSerializedDataAsync() {
    return getSerializedDataNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), Structure.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeSerializedDataAsync(Structure serializedData) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), serializedData);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getSerializedDataNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getSerializedDataNode() throws UaException {
    try {
      return getSerializedDataNodeAsync().get();
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSerializedDataNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "SerializedData", ExpandedNodeId.parse("i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}
