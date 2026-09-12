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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable String getNamespaceUri();

  /** Sets the existing node's local value. */
  void setNamespaceUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespaceUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getNamespaceVersion();

  /** Sets the existing node's local value. */
  void setNamespaceVersion(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespaceVersionNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getNamespacePublicationDate();

  /** Sets the existing node's local value. */
  void setNamespacePublicationDate(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespacePublicationDateNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsNamespaceSubset();

  /** Sets the existing node's local value. */
  void setIsNamespaceSubset(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIsNamespaceSubsetNode();

  /** Gets the existing node's local value. */
  @Nullable IdType @Nullable [] getStaticNodeIdTypes();

  /** Sets the existing node's local value. */
  void setStaticNodeIdTypes(@Nullable IdType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStaticNodeIdTypesNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getStaticNumericNodeIdRange();

  /** Sets the existing node's local value. */
  void setStaticNumericNodeIdRange(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStaticNumericNodeIdRangeNode();

  /** Gets the existing node's local value. */
  @Nullable String getStaticStringNodeIdPattern();

  /** Sets the existing node's local value. */
  void setStaticStringNodeIdPattern(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStaticStringNodeIdPatternNode();

  /** Gets the existing node's local value. */
  @Nullable RolePermissionType @Nullable [] getDefaultRolePermissions();

  /** Sets the existing node's local value. */
  void setDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultRolePermissionsNode();

  /** Gets the existing node's local value. */
  @Nullable RolePermissionType @Nullable [] getDefaultUserRolePermissions();

  /** Sets the existing node's local value. */
  void setDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultUserRolePermissionsNode();

  /** Gets the existing node's local value. */
  @Nullable AccessRestrictionType getDefaultAccessRestrictions();

  /** Sets the existing node's local value. */
  void setDefaultAccessRestrictions(@Nullable AccessRestrictionType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultAccessRestrictionsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getConfigurationVersion();

  /** Sets the existing node's local value. */
  void setConfigurationVersion(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConfigurationVersionNode();

  /** Gets the existing node's local value. */
  @Nullable String getModelVersion();

  /** Sets the existing node's local value. */
  void setModelVersion(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getModelVersionNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AddressSpaceFileType getNamespaceFileNode();
}
