/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;

public class SerializationEntityTypeNode extends BaseObjectTypeNode
    implements SerializationEntityType {
  public SerializationEntityTypeNode(
      UaNodeContext context,
      NodeId nodeId,
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
        context,
        nodeId,
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

  public SerializationEntityTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public PropertyTypeNode getIncludeReferenceTypesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.INCLUDE_REFERENCE_TYPES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getIncludeReferenceTypes() {
    return getProperty(SerializationEntityType.INCLUDE_REFERENCE_TYPES).orElse(null);
  }

  @Override
  public void setIncludeReferenceTypes(NodeId[] value) {
    setProperty(SerializationEntityType.INCLUDE_REFERENCE_TYPES, value);
  }

  @Override
  public PropertyTypeNode getExcludeReferenceTypesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.EXCLUDE_REFERENCE_TYPES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getExcludeReferenceTypes() {
    return getProperty(SerializationEntityType.EXCLUDE_REFERENCE_TYPES).orElse(null);
  }

  @Override
  public void setExcludeReferenceTypes(NodeId[] value) {
    setProperty(SerializationEntityType.EXCLUDE_REFERENCE_TYPES, value);
  }

  @Override
  public PropertyTypeNode getSerializationDepthNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.SERIALIZATION_DEPTH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getSerializationDepth() {
    return getProperty(SerializationEntityType.SERIALIZATION_DEPTH).orElse(null);
  }

  @Override
  public void setSerializationDepth(UShort value) {
    setProperty(SerializationEntityType.SERIALIZATION_DEPTH, value);
  }

  @Override
  public PropertyTypeNode getConsiderSubElementSerializationPropertiesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.CONSIDER_SUB_ELEMENT_SERIALIZATION_PROPERTIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getConsiderSubElementSerializationProperties() {
    return getProperty(SerializationEntityType.CONSIDER_SUB_ELEMENT_SERIALIZATION_PROPERTIES)
        .orElse(null);
  }

  @Override
  public void setConsiderSubElementSerializationProperties(Boolean value) {
    setProperty(SerializationEntityType.CONSIDER_SUB_ELEMENT_SERIALIZATION_PROPERTIES, value);
  }

  @Override
  public PropertyTypeNode getCustomMetaDataPropertiesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.CUSTOM_META_DATA_PROPERTIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public KeyValuePair[] getCustomMetaDataProperties() {
    return getProperty(SerializationEntityType.CUSTOM_META_DATA_PROPERTIES).orElse(null);
  }

  @Override
  public void setCustomMetaDataProperties(KeyValuePair[] value) {
    setProperty(SerializationEntityType.CUSTOM_META_DATA_PROPERTIES, value);
  }

  @Override
  public PropertyTypeNode getCustomMetaDataRefNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.CUSTOM_META_DATA_REF);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getCustomMetaDataRef() {
    return getProperty(SerializationEntityType.CUSTOM_META_DATA_REF).orElse(null);
  }

  @Override
  public void setCustomMetaDataRef(NodeId value) {
    setProperty(SerializationEntityType.CUSTOM_META_DATA_REF, value);
  }

  @Override
  public PropertyTypeNode getIncludeStatusNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(SerializationEntityType.INCLUDE_STATUS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getIncludeStatus() {
    return getProperty(SerializationEntityType.INCLUDE_STATUS).orElse(null);
  }

  @Override
  public void setIncludeStatus(Boolean value) {
    setProperty(SerializationEntityType.INCLUDE_STATUS, value);
  }

  @Override
  public PropertyTypeNode getIncludeSourceTimestampNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.INCLUDE_SOURCE_TIMESTAMP);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getIncludeSourceTimestamp() {
    return getProperty(SerializationEntityType.INCLUDE_SOURCE_TIMESTAMP).orElse(null);
  }

  @Override
  public void setIncludeSourceTimestamp(Boolean value) {
    setProperty(SerializationEntityType.INCLUDE_SOURCE_TIMESTAMP, value);
  }

  @Override
  public PropertyTypeNode getIncludeDictionaryReferenceNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(SerializationEntityType.INCLUDE_DICTIONARY_REFERENCE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getIncludeDictionaryReference() {
    return getProperty(SerializationEntityType.INCLUDE_DICTIONARY_REFERENCE).orElse(null);
  }

  @Override
  public void setIncludeDictionaryReference(Boolean value) {
    setProperty(SerializationEntityType.INCLUDE_DICTIONARY_REFERENCE, value);
  }

  @Override
  public BaseDataVariableTypeNode getSerializedDataNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SerializedData");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Structure getSerializedData() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SerializedData");
    return component.map(node -> (Structure) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSerializedData(Structure value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SerializedData")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public UaMethodNode getConfigureSerializationMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ConfigureSerialization",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
