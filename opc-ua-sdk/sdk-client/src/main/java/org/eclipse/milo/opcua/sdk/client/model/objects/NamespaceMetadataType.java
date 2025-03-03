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

  /**
   * Get the local value of the NamespaceUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NamespaceUri Node.
   * @throws UaException if an error occurs creating or getting the NamespaceUri Node.
   */
  String getNamespaceUri() throws UaException;

  /**
   * Set the local value of the NamespaceUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NamespaceUri Node.
   * @throws UaException if an error occurs creating or getting the NamespaceUri Node.
   */
  void setNamespaceUri(String value) throws UaException;

  /**
   * Read the value of the NamespaceUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readNamespaceUri() throws UaException;

  /**
   * Write a new value for the NamespaceUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNamespaceUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNamespaceUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readNamespaceUriAsync();

  /**
   * An asynchronous implementation of {@link #writeNamespaceUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNamespaceUriAsync(String value);

  /**
   * Get the NamespaceUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NamespaceUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNamespaceUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNamespaceUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNamespaceUriNodeAsync();

  /**
   * Get the local value of the NamespaceVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NamespaceVersion Node.
   * @throws UaException if an error occurs creating or getting the NamespaceVersion Node.
   */
  String getNamespaceVersion() throws UaException;

  /**
   * Set the local value of the NamespaceVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NamespaceVersion Node.
   * @throws UaException if an error occurs creating or getting the NamespaceVersion Node.
   */
  void setNamespaceVersion(String value) throws UaException;

  /**
   * Read the value of the NamespaceVersion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readNamespaceVersion() throws UaException;

  /**
   * Write a new value for the NamespaceVersion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNamespaceVersion(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNamespaceVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readNamespaceVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeNamespaceVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNamespaceVersionAsync(String value);

  /**
   * Get the NamespaceVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NamespaceVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNamespaceVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNamespaceVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNamespaceVersionNodeAsync();

  /**
   * Get the local value of the NamespacePublicationDate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NamespacePublicationDate Node.
   * @throws UaException if an error occurs creating or getting the NamespacePublicationDate Node.
   */
  DateTime getNamespacePublicationDate() throws UaException;

  /**
   * Set the local value of the NamespacePublicationDate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NamespacePublicationDate Node.
   * @throws UaException if an error occurs creating or getting the NamespacePublicationDate Node.
   */
  void setNamespacePublicationDate(DateTime value) throws UaException;

  /**
   * Read the value of the NamespacePublicationDate Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readNamespacePublicationDate() throws UaException;

  /**
   * Write a new value for the NamespacePublicationDate Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNamespacePublicationDate(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNamespacePublicationDate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readNamespacePublicationDateAsync();

  /**
   * An asynchronous implementation of {@link #writeNamespacePublicationDate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNamespacePublicationDateAsync(DateTime value);

  /**
   * Get the NamespacePublicationDate {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NamespacePublicationDate {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNamespacePublicationDateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNamespacePublicationDateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNamespacePublicationDateNodeAsync();

  /**
   * Get the local value of the IsNamespaceSubset Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IsNamespaceSubset Node.
   * @throws UaException if an error occurs creating or getting the IsNamespaceSubset Node.
   */
  Boolean getIsNamespaceSubset() throws UaException;

  /**
   * Set the local value of the IsNamespaceSubset Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IsNamespaceSubset Node.
   * @throws UaException if an error occurs creating or getting the IsNamespaceSubset Node.
   */
  void setIsNamespaceSubset(Boolean value) throws UaException;

  /**
   * Read the value of the IsNamespaceSubset Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readIsNamespaceSubset() throws UaException;

  /**
   * Write a new value for the IsNamespaceSubset Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIsNamespaceSubset(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIsNamespaceSubset}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readIsNamespaceSubsetAsync();

  /**
   * An asynchronous implementation of {@link #writeIsNamespaceSubset}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIsNamespaceSubsetAsync(Boolean value);

  /**
   * Get the IsNamespaceSubset {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IsNamespaceSubset {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIsNamespaceSubsetNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIsNamespaceSubsetNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIsNamespaceSubsetNodeAsync();

  /**
   * Get the local value of the StaticNodeIdTypes Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StaticNodeIdTypes Node.
   * @throws UaException if an error occurs creating or getting the StaticNodeIdTypes Node.
   */
  IdType[] getStaticNodeIdTypes() throws UaException;

  /**
   * Set the local value of the StaticNodeIdTypes Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StaticNodeIdTypes Node.
   * @throws UaException if an error occurs creating or getting the StaticNodeIdTypes Node.
   */
  void setStaticNodeIdTypes(IdType[] value) throws UaException;

  /**
   * Read the value of the StaticNodeIdTypes Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link IdType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  IdType[] readStaticNodeIdTypes() throws UaException;

  /**
   * Write a new value for the StaticNodeIdTypes Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link IdType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStaticNodeIdTypes(IdType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStaticNodeIdTypes}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends IdType[]> readStaticNodeIdTypesAsync();

  /**
   * An asynchronous implementation of {@link #writeStaticNodeIdTypes}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStaticNodeIdTypesAsync(IdType[] value);

  /**
   * Get the StaticNodeIdTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StaticNodeIdTypes {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStaticNodeIdTypesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStaticNodeIdTypesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStaticNodeIdTypesNodeAsync();

  /**
   * Get the local value of the StaticNumericNodeIdRange Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StaticNumericNodeIdRange Node.
   * @throws UaException if an error occurs creating or getting the StaticNumericNodeIdRange Node.
   */
  String[] getStaticNumericNodeIdRange() throws UaException;

  /**
   * Set the local value of the StaticNumericNodeIdRange Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StaticNumericNodeIdRange Node.
   * @throws UaException if an error occurs creating or getting the StaticNumericNodeIdRange Node.
   */
  void setStaticNumericNodeIdRange(String[] value) throws UaException;

  /**
   * Read the value of the StaticNumericNodeIdRange Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readStaticNumericNodeIdRange() throws UaException;

  /**
   * Write a new value for the StaticNumericNodeIdRange Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStaticNumericNodeIdRange(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStaticNumericNodeIdRange}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readStaticNumericNodeIdRangeAsync();

  /**
   * An asynchronous implementation of {@link #writeStaticNumericNodeIdRange}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStaticNumericNodeIdRangeAsync(String[] value);

  /**
   * Get the StaticNumericNodeIdRange {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StaticNumericNodeIdRange {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStaticNumericNodeIdRangeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStaticNumericNodeIdRangeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStaticNumericNodeIdRangeNodeAsync();

  /**
   * Get the local value of the StaticStringNodeIdPattern Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StaticStringNodeIdPattern Node.
   * @throws UaException if an error occurs creating or getting the StaticStringNodeIdPattern Node.
   */
  String getStaticStringNodeIdPattern() throws UaException;

  /**
   * Set the local value of the StaticStringNodeIdPattern Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StaticStringNodeIdPattern Node.
   * @throws UaException if an error occurs creating or getting the StaticStringNodeIdPattern Node.
   */
  void setStaticStringNodeIdPattern(String value) throws UaException;

  /**
   * Read the value of the StaticStringNodeIdPattern Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readStaticStringNodeIdPattern() throws UaException;

  /**
   * Write a new value for the StaticStringNodeIdPattern Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStaticStringNodeIdPattern(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStaticStringNodeIdPattern}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readStaticStringNodeIdPatternAsync();

  /**
   * An asynchronous implementation of {@link #writeStaticStringNodeIdPattern}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStaticStringNodeIdPatternAsync(String value);

  /**
   * Get the StaticStringNodeIdPattern {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StaticStringNodeIdPattern {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStaticStringNodeIdPatternNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStaticStringNodeIdPatternNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStaticStringNodeIdPatternNodeAsync();

  /**
   * Get the local value of the DefaultRolePermissions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DefaultRolePermissions Node.
   * @throws UaException if an error occurs creating or getting the DefaultRolePermissions Node.
   */
  RolePermissionType[] getDefaultRolePermissions() throws UaException;

  /**
   * Set the local value of the DefaultRolePermissions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DefaultRolePermissions Node.
   * @throws UaException if an error occurs creating or getting the DefaultRolePermissions Node.
   */
  void setDefaultRolePermissions(RolePermissionType[] value) throws UaException;

  /**
   * Read the value of the DefaultRolePermissions Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link RolePermissionType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  RolePermissionType[] readDefaultRolePermissions() throws UaException;

  /**
   * Write a new value for the DefaultRolePermissions Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link RolePermissionType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefaultRolePermissions(RolePermissionType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefaultRolePermissions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends RolePermissionType[]> readDefaultRolePermissionsAsync();

  /**
   * An asynchronous implementation of {@link #writeDefaultRolePermissions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefaultRolePermissionsAsync(RolePermissionType[] value);

  /**
   * Get the DefaultRolePermissions {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultRolePermissions {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefaultRolePermissionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultRolePermissionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefaultRolePermissionsNodeAsync();

  /**
   * Get the local value of the DefaultUserRolePermissions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DefaultUserRolePermissions Node.
   * @throws UaException if an error occurs creating or getting the DefaultUserRolePermissions Node.
   */
  RolePermissionType[] getDefaultUserRolePermissions() throws UaException;

  /**
   * Set the local value of the DefaultUserRolePermissions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DefaultUserRolePermissions Node.
   * @throws UaException if an error occurs creating or getting the DefaultUserRolePermissions Node.
   */
  void setDefaultUserRolePermissions(RolePermissionType[] value) throws UaException;

  /**
   * Read the value of the DefaultUserRolePermissions Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link RolePermissionType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  RolePermissionType[] readDefaultUserRolePermissions() throws UaException;

  /**
   * Write a new value for the DefaultUserRolePermissions Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link RolePermissionType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefaultUserRolePermissions(RolePermissionType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefaultUserRolePermissions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends RolePermissionType[]> readDefaultUserRolePermissionsAsync();

  /**
   * An asynchronous implementation of {@link #writeDefaultUserRolePermissions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefaultUserRolePermissionsAsync(RolePermissionType[] value);

  /**
   * Get the DefaultUserRolePermissions {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultUserRolePermissions {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefaultUserRolePermissionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultUserRolePermissionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefaultUserRolePermissionsNodeAsync();

  /**
   * Get the local value of the DefaultAccessRestrictions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DefaultAccessRestrictions Node.
   * @throws UaException if an error occurs creating or getting the DefaultAccessRestrictions Node.
   */
  AccessRestrictionType getDefaultAccessRestrictions() throws UaException;

  /**
   * Set the local value of the DefaultAccessRestrictions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DefaultAccessRestrictions Node.
   * @throws UaException if an error occurs creating or getting the DefaultAccessRestrictions Node.
   */
  void setDefaultAccessRestrictions(AccessRestrictionType value) throws UaException;

  /**
   * Read the value of the DefaultAccessRestrictions Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link AccessRestrictionType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  AccessRestrictionType readDefaultAccessRestrictions() throws UaException;

  /**
   * Write a new value for the DefaultAccessRestrictions Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link AccessRestrictionType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefaultAccessRestrictions(AccessRestrictionType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefaultAccessRestrictions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends AccessRestrictionType> readDefaultAccessRestrictionsAsync();

  /**
   * An asynchronous implementation of {@link #writeDefaultAccessRestrictions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefaultAccessRestrictionsAsync(AccessRestrictionType value);

  /**
   * Get the DefaultAccessRestrictions {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultAccessRestrictions {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefaultAccessRestrictionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultAccessRestrictionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefaultAccessRestrictionsNodeAsync();

  /**
   * Get the local value of the ConfigurationVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConfigurationVersion Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationVersion Node.
   */
  UInteger getConfigurationVersion() throws UaException;

  /**
   * Set the local value of the ConfigurationVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConfigurationVersion Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationVersion Node.
   */
  void setConfigurationVersion(UInteger value) throws UaException;

  /**
   * Read the value of the ConfigurationVersion Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readConfigurationVersion() throws UaException;

  /**
   * Write a new value for the ConfigurationVersion Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConfigurationVersion(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConfigurationVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readConfigurationVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeConfigurationVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(UInteger value);

  /**
   * Get the ConfigurationVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConfigurationVersion {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConfigurationVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConfigurationVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConfigurationVersionNodeAsync();

  /**
   * Get the local value of the ModelVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ModelVersion Node.
   * @throws UaException if an error occurs creating or getting the ModelVersion Node.
   */
  String getModelVersion() throws UaException;

  /**
   * Set the local value of the ModelVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ModelVersion Node.
   * @throws UaException if an error occurs creating or getting the ModelVersion Node.
   */
  void setModelVersion(String value) throws UaException;

  /**
   * Read the value of the ModelVersion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readModelVersion() throws UaException;

  /**
   * Write a new value for the ModelVersion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeModelVersion(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readModelVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readModelVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeModelVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeModelVersionAsync(String value);

  /**
   * Get the ModelVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ModelVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getModelVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getModelVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getModelVersionNodeAsync();

  /**
   * Get the NamespaceFile {@link AddressSpaceFileType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NamespaceFile {@link AddressSpaceFileType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AddressSpaceFileType getNamespaceFileNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNamespaceFileNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AddressSpaceFileType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AddressSpaceFileType> getNamespaceFileNodeAsync();
}
