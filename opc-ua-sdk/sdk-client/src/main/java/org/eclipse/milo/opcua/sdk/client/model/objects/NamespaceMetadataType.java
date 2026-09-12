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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  @Nullable String getNamespaceUri() throws UaException;

  /** Sets the existing node's local value. */
  void setNamespaceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readNamespaceUri() throws UaException;

  /** Writes the value remotely. */
  void writeNamespaceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readNamespaceUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNamespaceUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespaceUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNamespaceUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getNamespaceVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setNamespaceVersion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readNamespaceVersion() throws UaException;

  /** Writes the value remotely. */
  void writeNamespaceVersion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readNamespaceVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNamespaceVersionAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespaceVersionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNamespaceVersionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getNamespacePublicationDate() throws UaException;

  /** Sets the existing node's local value. */
  void setNamespacePublicationDate(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readNamespacePublicationDate() throws UaException;

  /** Writes the value remotely. */
  void writeNamespacePublicationDate(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readNamespacePublicationDateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNamespacePublicationDateAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespacePublicationDateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNamespacePublicationDateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsNamespaceSubset() throws UaException;

  /** Sets the existing node's local value. */
  void setIsNamespaceSubset(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readIsNamespaceSubset() throws UaException;

  /** Writes the value remotely. */
  void writeIsNamespaceSubset(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readIsNamespaceSubsetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIsNamespaceSubsetAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIsNamespaceSubsetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIsNamespaceSubsetNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable IdType @Nullable [] getStaticNodeIdTypes() throws UaException;

  /** Sets the existing node's local value. */
  void setStaticNodeIdTypes(@Nullable IdType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable IdType @Nullable [] readStaticNodeIdTypes() throws UaException;

  /** Writes the value remotely. */
  void writeStaticNodeIdTypes(@Nullable IdType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable IdType @Nullable []> readStaticNodeIdTypesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStaticNodeIdTypesAsync(@Nullable IdType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStaticNodeIdTypesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStaticNodeIdTypesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getStaticNumericNodeIdRange() throws UaException;

  /** Sets the existing node's local value. */
  void setStaticNumericNodeIdRange(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readStaticNumericNodeIdRange() throws UaException;

  /** Writes the value remotely. */
  void writeStaticNumericNodeIdRange(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readStaticNumericNodeIdRangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStaticNumericNodeIdRangeAsync(
      @Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStaticNumericNodeIdRangeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStaticNumericNodeIdRangeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getStaticStringNodeIdPattern() throws UaException;

  /** Sets the existing node's local value. */
  void setStaticStringNodeIdPattern(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readStaticStringNodeIdPattern() throws UaException;

  /** Writes the value remotely. */
  void writeStaticStringNodeIdPattern(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readStaticStringNodeIdPatternAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStaticStringNodeIdPatternAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStaticStringNodeIdPatternNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStaticStringNodeIdPatternNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable RolePermissionType @Nullable [] getDefaultRolePermissions() throws UaException;

  /** Sets the existing node's local value. */
  void setDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable RolePermissionType @Nullable [] readDefaultRolePermissions() throws UaException;

  /** Writes the value remotely. */
  void writeDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultRolePermissionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefaultRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultRolePermissionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultRolePermissionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable RolePermissionType @Nullable [] getDefaultUserRolePermissions() throws UaException;

  /** Sets the existing node's local value. */
  void setDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable RolePermissionType @Nullable [] readDefaultUserRolePermissions() throws UaException;

  /** Writes the value remotely. */
  void writeDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultUserRolePermissionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefaultUserRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultUserRolePermissionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultUserRolePermissionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable AccessRestrictionType getDefaultAccessRestrictions() throws UaException;

  /** Sets the existing node's local value. */
  void setDefaultAccessRestrictions(@Nullable AccessRestrictionType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable AccessRestrictionType readDefaultAccessRestrictions() throws UaException;

  /** Writes the value remotely. */
  void writeDefaultAccessRestrictions(@Nullable AccessRestrictionType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable AccessRestrictionType> readDefaultAccessRestrictionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefaultAccessRestrictionsAsync(
      @Nullable AccessRestrictionType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultAccessRestrictionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultAccessRestrictionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getConfigurationVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setConfigurationVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readConfigurationVersion() throws UaException;

  /** Writes the value remotely. */
  void writeConfigurationVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readConfigurationVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConfigurationVersionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConfigurationVersionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getModelVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setModelVersion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readModelVersion() throws UaException;

  /** Writes the value remotely. */
  void writeModelVersion(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readModelVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeModelVersionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getModelVersionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getModelVersionNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AddressSpaceFileType getNamespaceFileNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable AddressSpaceFileType> getNamespaceFileNodeAsync();
}
