/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class NamespaceMetadataTypeNode extends BaseObjectTypeNode implements NamespaceMetadataType {
  public NamespaceMetadataTypeNode(
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

  public NamespaceMetadataTypeNode(
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
  public PropertyTypeNode getNamespaceUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(NamespaceMetadataType.NAMESPACE_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getNamespaceUri() {
    return getProperty(NamespaceMetadataType.NAMESPACE_URI).orElse(null);
  }

  @Override
  public void setNamespaceUri(String value) {
    setProperty(NamespaceMetadataType.NAMESPACE_URI, value);
  }

  @Override
  public PropertyTypeNode getNamespaceVersionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(NamespaceMetadataType.NAMESPACE_VERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getNamespaceVersion() {
    return getProperty(NamespaceMetadataType.NAMESPACE_VERSION).orElse(null);
  }

  @Override
  public void setNamespaceVersion(String value) {
    setProperty(NamespaceMetadataType.NAMESPACE_VERSION, value);
  }

  @Override
  public PropertyTypeNode getNamespacePublicationDateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.NAMESPACE_PUBLICATION_DATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getNamespacePublicationDate() {
    return getProperty(NamespaceMetadataType.NAMESPACE_PUBLICATION_DATE).orElse(null);
  }

  @Override
  public void setNamespacePublicationDate(DateTime value) {
    setProperty(NamespaceMetadataType.NAMESPACE_PUBLICATION_DATE, value);
  }

  @Override
  public PropertyTypeNode getIsNamespaceSubsetNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.IS_NAMESPACE_SUBSET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getIsNamespaceSubset() {
    return getProperty(NamespaceMetadataType.IS_NAMESPACE_SUBSET).orElse(null);
  }

  @Override
  public void setIsNamespaceSubset(Boolean value) {
    setProperty(NamespaceMetadataType.IS_NAMESPACE_SUBSET, value);
  }

  @Override
  public PropertyTypeNode getStaticNodeIdTypesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.STATIC_NODE_ID_TYPES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public IdType[] getStaticNodeIdTypes() {
    return getProperty(NamespaceMetadataType.STATIC_NODE_ID_TYPES).orElse(null);
  }

  @Override
  public void setStaticNodeIdTypes(IdType[] value) {
    setProperty(NamespaceMetadataType.STATIC_NODE_ID_TYPES, value);
  }

  @Override
  public PropertyTypeNode getStaticNumericNodeIdRangeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.STATIC_NUMERIC_NODE_ID_RANGE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getStaticNumericNodeIdRange() {
    return getProperty(NamespaceMetadataType.STATIC_NUMERIC_NODE_ID_RANGE).orElse(null);
  }

  @Override
  public void setStaticNumericNodeIdRange(String[] value) {
    setProperty(NamespaceMetadataType.STATIC_NUMERIC_NODE_ID_RANGE, value);
  }

  @Override
  public PropertyTypeNode getStaticStringNodeIdPatternNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.STATIC_STRING_NODE_ID_PATTERN);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getStaticStringNodeIdPattern() {
    return getProperty(NamespaceMetadataType.STATIC_STRING_NODE_ID_PATTERN).orElse(null);
  }

  @Override
  public void setStaticStringNodeIdPattern(String value) {
    setProperty(NamespaceMetadataType.STATIC_STRING_NODE_ID_PATTERN, value);
  }

  @Override
  public PropertyTypeNode getDefaultRolePermissionsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.DEFAULT_ROLE_PERMISSIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public RolePermissionType[] getDefaultRolePermissions() {
    return getProperty(NamespaceMetadataType.DEFAULT_ROLE_PERMISSIONS).orElse(null);
  }

  @Override
  public void setDefaultRolePermissions(RolePermissionType[] value) {
    setProperty(NamespaceMetadataType.DEFAULT_ROLE_PERMISSIONS, value);
  }

  @Override
  public PropertyTypeNode getDefaultUserRolePermissionsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.DEFAULT_USER_ROLE_PERMISSIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public RolePermissionType[] getDefaultUserRolePermissions() {
    return getProperty(NamespaceMetadataType.DEFAULT_USER_ROLE_PERMISSIONS).orElse(null);
  }

  @Override
  public void setDefaultUserRolePermissions(RolePermissionType[] value) {
    setProperty(NamespaceMetadataType.DEFAULT_USER_ROLE_PERMISSIONS, value);
  }

  @Override
  public PropertyTypeNode getDefaultAccessRestrictionsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.DEFAULT_ACCESS_RESTRICTIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public AccessRestrictionType getDefaultAccessRestrictions() {
    return getProperty(NamespaceMetadataType.DEFAULT_ACCESS_RESTRICTIONS).orElse(null);
  }

  @Override
  public void setDefaultAccessRestrictions(AccessRestrictionType value) {
    setProperty(NamespaceMetadataType.DEFAULT_ACCESS_RESTRICTIONS, value);
  }

  @Override
  public PropertyTypeNode getConfigurationVersionNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(NamespaceMetadataType.CONFIGURATION_VERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getConfigurationVersion() {
    return getProperty(NamespaceMetadataType.CONFIGURATION_VERSION).orElse(null);
  }

  @Override
  public void setConfigurationVersion(UInteger value) {
    setProperty(NamespaceMetadataType.CONFIGURATION_VERSION, value);
  }

  @Override
  public AddressSpaceFileTypeNode getNamespaceFileNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "NamespaceFile");
    return (AddressSpaceFileTypeNode) component.orElse(null);
  }
}
