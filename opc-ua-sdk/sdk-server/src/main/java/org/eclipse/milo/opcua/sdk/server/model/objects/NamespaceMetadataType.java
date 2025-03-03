/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13</a>
 */
public interface NamespaceMetadataType extends BaseObjectType {
  QualifiedProperty<String> NAMESPACE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespaceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> NAMESPACE_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespaceVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<DateTime> NAMESPACE_PUBLICATION_DATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespacePublicationDate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<Boolean> IS_NAMESPACE_SUBSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsNamespaceSubset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<IdType[]> STATIC_NODE_ID_TYPES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StaticNodeIdTypes",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=256"),
          1,
          IdType[].class);

  QualifiedProperty<String[]> STATIC_NUMERIC_NODE_ID_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StaticNumericNodeIdRange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=291"),
          1,
          String[].class);

  QualifiedProperty<String> STATIC_STRING_NODE_ID_PATTERN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StaticStringNodeIdPattern",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<RolePermissionType[]> DEFAULT_ROLE_PERMISSIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultRolePermissions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=96"),
          1,
          RolePermissionType[].class);

  QualifiedProperty<RolePermissionType[]> DEFAULT_USER_ROLE_PERMISSIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultUserRolePermissions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=96"),
          1,
          RolePermissionType[].class);

  QualifiedProperty<AccessRestrictionType> DEFAULT_ACCESS_RESTRICTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultAccessRestrictions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=95"),
          -1,
          AccessRestrictionType.class);

  QualifiedProperty<UInteger> CONFIGURATION_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<String> MODEL_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ModelVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24263"),
          -1,
          String.class);

  String getNamespaceUri();

  void setNamespaceUri(String value);

  PropertyType getNamespaceUriNode();

  String getNamespaceVersion();

  void setNamespaceVersion(String value);

  PropertyType getNamespaceVersionNode();

  DateTime getNamespacePublicationDate();

  void setNamespacePublicationDate(DateTime value);

  PropertyType getNamespacePublicationDateNode();

  Boolean getIsNamespaceSubset();

  void setIsNamespaceSubset(Boolean value);

  PropertyType getIsNamespaceSubsetNode();

  IdType[] getStaticNodeIdTypes();

  void setStaticNodeIdTypes(IdType[] value);

  PropertyType getStaticNodeIdTypesNode();

  String[] getStaticNumericNodeIdRange();

  void setStaticNumericNodeIdRange(String[] value);

  PropertyType getStaticNumericNodeIdRangeNode();

  String getStaticStringNodeIdPattern();

  void setStaticStringNodeIdPattern(String value);

  PropertyType getStaticStringNodeIdPatternNode();

  RolePermissionType[] getDefaultRolePermissions();

  void setDefaultRolePermissions(RolePermissionType[] value);

  PropertyType getDefaultRolePermissionsNode();

  RolePermissionType[] getDefaultUserRolePermissions();

  void setDefaultUserRolePermissions(RolePermissionType[] value);

  PropertyType getDefaultUserRolePermissionsNode();

  AccessRestrictionType getDefaultAccessRestrictions();

  void setDefaultAccessRestrictions(AccessRestrictionType value);

  PropertyType getDefaultAccessRestrictionsNode();

  UInteger getConfigurationVersion();

  void setConfigurationVersion(UInteger value);

  PropertyType getConfigurationVersionNode();

  String getModelVersion();

  void setModelVersion(String value);

  PropertyType getModelVersionNode();

  AddressSpaceFileType getNamespaceFileNode();
}
