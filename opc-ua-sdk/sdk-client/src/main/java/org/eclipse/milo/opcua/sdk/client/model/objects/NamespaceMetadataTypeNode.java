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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class NamespaceMetadataTypeNode extends BaseObjectTypeNode implements NamespaceMetadataType {
  public NamespaceMetadataTypeNode(
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
  public String getNamespaceUri() throws UaException {
    PropertyTypeNode node = getNamespaceUriNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceUri(String value) throws UaException {
    PropertyTypeNode node = getNamespaceUriNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readNamespaceUri() throws UaException {
    try {
      return readNamespaceUriAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeNamespaceUri(String value) throws UaException {
    try {
      writeNamespaceUriAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readNamespaceUriAsync() {
    return getNamespaceUriNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespaceUriAsync(String namespaceUri) {
    DataValue value = DataValue.valueOnly(new Variant(namespaceUri));
    return getNamespaceUriNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getNamespaceUriNode() throws UaException {
    try {
      return getNamespaceUriNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceUriNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "NamespaceUri",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getNamespaceVersion() throws UaException {
    PropertyTypeNode node = getNamespaceVersionNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceVersion(String value) throws UaException {
    PropertyTypeNode node = getNamespaceVersionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readNamespaceVersion() throws UaException {
    try {
      return readNamespaceVersionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeNamespaceVersion(String value) throws UaException {
    try {
      writeNamespaceVersionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readNamespaceVersionAsync() {
    return getNamespaceVersionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespaceVersionAsync(String namespaceVersion) {
    DataValue value = DataValue.valueOnly(new Variant(namespaceVersion));
    return getNamespaceVersionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getNamespaceVersionNode() throws UaException {
    try {
      return getNamespaceVersionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceVersionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "NamespaceVersion",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getNamespacePublicationDate() throws UaException {
    PropertyTypeNode node = getNamespacePublicationDateNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespacePublicationDate(DateTime value) throws UaException {
    PropertyTypeNode node = getNamespacePublicationDateNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readNamespacePublicationDate() throws UaException {
    try {
      return readNamespacePublicationDateAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeNamespacePublicationDate(DateTime value) throws UaException {
    try {
      writeNamespacePublicationDateAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readNamespacePublicationDateAsync() {
    return getNamespacePublicationDateNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeNamespacePublicationDateAsync(
      DateTime namespacePublicationDate) {
    DataValue value = DataValue.valueOnly(new Variant(namespacePublicationDate));
    return getNamespacePublicationDateNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getNamespacePublicationDateNode() throws UaException {
    try {
      return getNamespacePublicationDateNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNamespacePublicationDateNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "NamespacePublicationDate",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getIsNamespaceSubset() throws UaException {
    PropertyTypeNode node = getIsNamespaceSubsetNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setIsNamespaceSubset(Boolean value) throws UaException {
    PropertyTypeNode node = getIsNamespaceSubsetNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readIsNamespaceSubset() throws UaException {
    try {
      return readIsNamespaceSubsetAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeIsNamespaceSubset(Boolean value) throws UaException {
    try {
      writeIsNamespaceSubsetAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readIsNamespaceSubsetAsync() {
    return getIsNamespaceSubsetNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeIsNamespaceSubsetAsync(Boolean isNamespaceSubset) {
    DataValue value = DataValue.valueOnly(new Variant(isNamespaceSubset));
    return getIsNamespaceSubsetNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getIsNamespaceSubsetNode() throws UaException {
    try {
      return getIsNamespaceSubsetNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIsNamespaceSubsetNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "IsNamespaceSubset",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public IdType[] getStaticNodeIdTypes() throws UaException {
    PropertyTypeNode node = getStaticNodeIdTypesNode();
    Object value = node.getValue().getValue().getValue();

    if (value instanceof Integer[]) {
      Integer[] values = (Integer[]) value;
      IdType[] staticNodeIdTypes = new IdType[values.length];
      for (int i = 0; i < values.length; i++) {
        staticNodeIdTypes[i] = IdType.from(values[i]);
      }
      return staticNodeIdTypes;
    } else if (value instanceof IdType[]) {
      return (IdType[]) value;
    } else {
      return null;
    }
  }

  @Override
  public void setStaticNodeIdTypes(IdType[] value) throws UaException {
    PropertyTypeNode node = getStaticNodeIdTypesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public IdType[] readStaticNodeIdTypes() throws UaException {
    try {
      return readStaticNodeIdTypesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStaticNodeIdTypes(IdType[] value) throws UaException {
    try {
      writeStaticNodeIdTypesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends IdType[]> readStaticNodeIdTypesAsync() {
    return getStaticNodeIdTypesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(
            v -> {
              Object value = v.getValue().getValue();
              if (value instanceof Integer[]) {
                Integer[] values = (Integer[]) value;
                IdType[] staticNodeIdTypes = new IdType[values.length];
                for (int i = 0; i < values.length; i++) {
                  staticNodeIdTypes[i] = IdType.from(values[i]);
                }
                return staticNodeIdTypes;
              } else if (value instanceof IdType[]) {
                return (IdType[]) value;
              } else {
                return null;
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStaticNodeIdTypesAsync(IdType[] staticNodeIdTypes) {
    DataValue value = DataValue.valueOnly(new Variant(staticNodeIdTypes));
    return getStaticNodeIdTypesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getStaticNodeIdTypesNode() throws UaException {
    try {
      return getStaticNodeIdTypesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticNodeIdTypesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "StaticNodeIdTypes",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String[] getStaticNumericNodeIdRange() throws UaException {
    PropertyTypeNode node = getStaticNumericNodeIdRangeNode();
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setStaticNumericNodeIdRange(String[] value) throws UaException {
    PropertyTypeNode node = getStaticNumericNodeIdRangeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String[] readStaticNumericNodeIdRange() throws UaException {
    try {
      return readStaticNumericNodeIdRangeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStaticNumericNodeIdRange(String[] value) throws UaException {
    try {
      writeStaticNumericNodeIdRangeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String[]> readStaticNumericNodeIdRangeAsync() {
    return getStaticNumericNodeIdRangeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String[]) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeStaticNumericNodeIdRangeAsync(
      String[] staticNumericNodeIdRange) {
    DataValue value = DataValue.valueOnly(new Variant(staticNumericNodeIdRange));
    return getStaticNumericNodeIdRangeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getStaticNumericNodeIdRangeNode() throws UaException {
    try {
      return getStaticNumericNodeIdRangeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticNumericNodeIdRangeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "StaticNumericNodeIdRange",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getStaticStringNodeIdPattern() throws UaException {
    PropertyTypeNode node = getStaticStringNodeIdPatternNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setStaticStringNodeIdPattern(String value) throws UaException {
    PropertyTypeNode node = getStaticStringNodeIdPatternNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readStaticStringNodeIdPattern() throws UaException {
    try {
      return readStaticStringNodeIdPatternAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeStaticStringNodeIdPattern(String value) throws UaException {
    try {
      writeStaticStringNodeIdPatternAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readStaticStringNodeIdPatternAsync() {
    return getStaticStringNodeIdPatternNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeStaticStringNodeIdPatternAsync(
      String staticStringNodeIdPattern) {
    DataValue value = DataValue.valueOnly(new Variant(staticStringNodeIdPattern));
    return getStaticStringNodeIdPatternNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getStaticStringNodeIdPatternNode() throws UaException {
    try {
      return getStaticStringNodeIdPatternNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStaticStringNodeIdPatternNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "StaticStringNodeIdPattern",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public RolePermissionType[] getDefaultRolePermissions() throws UaException {
    PropertyTypeNode node = getDefaultRolePermissionsNode();
    return cast(node.getValue().getValue().getValue(), RolePermissionType[].class);
  }

  @Override
  public void setDefaultRolePermissions(RolePermissionType[] value) throws UaException {
    PropertyTypeNode node = getDefaultRolePermissionsNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public RolePermissionType[] readDefaultRolePermissions() throws UaException {
    try {
      return readDefaultRolePermissionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDefaultRolePermissions(RolePermissionType[] value) throws UaException {
    try {
      writeDefaultRolePermissionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends RolePermissionType[]> readDefaultRolePermissionsAsync() {
    return getDefaultRolePermissionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), RolePermissionType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultRolePermissionsAsync(
      RolePermissionType[] defaultRolePermissions) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), defaultRolePermissions);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getDefaultRolePermissionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDefaultRolePermissionsNode() throws UaException {
    try {
      return getDefaultRolePermissionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDefaultRolePermissionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DefaultRolePermissions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public RolePermissionType[] getDefaultUserRolePermissions() throws UaException {
    PropertyTypeNode node = getDefaultUserRolePermissionsNode();
    return cast(node.getValue().getValue().getValue(), RolePermissionType[].class);
  }

  @Override
  public void setDefaultUserRolePermissions(RolePermissionType[] value) throws UaException {
    PropertyTypeNode node = getDefaultUserRolePermissionsNode();
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public RolePermissionType[] readDefaultUserRolePermissions() throws UaException {
    try {
      return readDefaultUserRolePermissionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDefaultUserRolePermissions(RolePermissionType[] value) throws UaException {
    try {
      writeDefaultUserRolePermissionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends RolePermissionType[]> readDefaultUserRolePermissionsAsync() {
    return getDefaultUserRolePermissionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), RolePermissionType[].class));
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultUserRolePermissionsAsync(
      RolePermissionType[] defaultUserRolePermissions) {
    ExtensionObject[] encoded =
        ExtensionObject.encodeArray(client.getStaticEncodingContext(), defaultUserRolePermissions);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getDefaultUserRolePermissionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDefaultUserRolePermissionsNode() throws UaException {
    try {
      return getDefaultUserRolePermissionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDefaultUserRolePermissionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DefaultUserRolePermissions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public AccessRestrictionType getDefaultAccessRestrictions() throws UaException {
    PropertyTypeNode node = getDefaultAccessRestrictionsNode();
    return (AccessRestrictionType) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefaultAccessRestrictions(AccessRestrictionType value) throws UaException {
    PropertyTypeNode node = getDefaultAccessRestrictionsNode();
    node.setValue(new Variant(value));
  }

  @Override
  public AccessRestrictionType readDefaultAccessRestrictions() throws UaException {
    try {
      return readDefaultAccessRestrictionsAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeDefaultAccessRestrictions(AccessRestrictionType value) throws UaException {
    try {
      writeDefaultAccessRestrictionsAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends AccessRestrictionType> readDefaultAccessRestrictionsAsync() {
    return getDefaultAccessRestrictionsNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (AccessRestrictionType) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultAccessRestrictionsAsync(
      AccessRestrictionType defaultAccessRestrictions) {
    DataValue value = DataValue.valueOnly(new Variant(defaultAccessRestrictions));
    return getDefaultAccessRestrictionsNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getDefaultAccessRestrictionsNode() throws UaException {
    try {
      return getDefaultAccessRestrictionsNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDefaultAccessRestrictionsNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "DefaultAccessRestrictions",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getConfigurationVersion() throws UaException {
    PropertyTypeNode node = getConfigurationVersionNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setConfigurationVersion(UInteger value) throws UaException {
    PropertyTypeNode node = getConfigurationVersionNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readConfigurationVersion() throws UaException {
    try {
      return readConfigurationVersionAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConfigurationVersion(UInteger value) throws UaException {
    try {
      writeConfigurationVersionAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readConfigurationVersionAsync() {
    return getConfigurationVersionNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConfigurationVersionAsync(
      UInteger configurationVersion) {
    DataValue value = DataValue.valueOnly(new Variant(configurationVersion));
    return getConfigurationVersionNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConfigurationVersionNode() throws UaException {
    try {
      return getConfigurationVersionNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConfigurationVersionNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "ConfigurationVersion",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public AddressSpaceFileTypeNode getNamespaceFileNode() throws UaException {
    try {
      return getNamespaceFileNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends AddressSpaceFileTypeNode> getNamespaceFileNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "NamespaceFile",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (AddressSpaceFileTypeNode) node);
  }
}
