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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.1</a>
 */
public interface UserManagementType extends BaseObjectType {
  QualifiedProperty<UserManagementDataType[]> USERS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Users",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24281"),
          1,
          UserManagementDataType[].class);

  QualifiedProperty<Range> PASSWORD_LENGTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PasswordLength",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=884"),
          -1,
          Range.class);

  QualifiedProperty<PasswordOptionsMask> PASSWORD_OPTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PasswordOptions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24277"),
          -1,
          PasswordOptionsMask.class);

  QualifiedProperty<LocalizedText> PASSWORD_RESTRICTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PasswordRestrictions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  /**
   * Get the local value of the Users Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Users Node.
   * @throws UaException if an error occurs creating or getting the Users Node.
   */
  UserManagementDataType[] getUsers() throws UaException;

  /**
   * Set the local value of the Users Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Users Node.
   * @throws UaException if an error occurs creating or getting the Users Node.
   */
  void setUsers(UserManagementDataType[] value) throws UaException;

  /**
   * Read the value of the Users Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UserManagementDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UserManagementDataType[] readUsers() throws UaException;

  /**
   * Write a new value for the Users Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link UserManagementDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUsers(UserManagementDataType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUsers}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UserManagementDataType[]> readUsersAsync();

  /**
   * An asynchronous implementation of {@link #writeUsers}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUsersAsync(UserManagementDataType[] value);

  /**
   * Get the Users {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Users {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUsersNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUsersNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUsersNodeAsync();

  /**
   * Get the local value of the PasswordLength Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PasswordLength Node.
   * @throws UaException if an error occurs creating or getting the PasswordLength Node.
   */
  Range getPasswordLength() throws UaException;

  /**
   * Set the local value of the PasswordLength Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PasswordLength Node.
   * @throws UaException if an error occurs creating or getting the PasswordLength Node.
   */
  void setPasswordLength(Range value) throws UaException;

  /**
   * Read the value of the PasswordLength Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Range} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Range readPasswordLength() throws UaException;

  /**
   * Write a new value for the PasswordLength Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Range} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePasswordLength(Range value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPasswordLength}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Range> readPasswordLengthAsync();

  /**
   * An asynchronous implementation of {@link #writePasswordLength}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePasswordLengthAsync(Range value);

  /**
   * Get the PasswordLength {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PasswordLength {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPasswordLengthNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPasswordLengthNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPasswordLengthNodeAsync();

  /**
   * Get the local value of the PasswordOptions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PasswordOptions Node.
   * @throws UaException if an error occurs creating or getting the PasswordOptions Node.
   */
  PasswordOptionsMask getPasswordOptions() throws UaException;

  /**
   * Set the local value of the PasswordOptions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PasswordOptions Node.
   * @throws UaException if an error occurs creating or getting the PasswordOptions Node.
   */
  void setPasswordOptions(PasswordOptionsMask value) throws UaException;

  /**
   * Read the value of the PasswordOptions Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link PasswordOptionsMask} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  PasswordOptionsMask readPasswordOptions() throws UaException;

  /**
   * Write a new value for the PasswordOptions Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link PasswordOptionsMask} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePasswordOptions(PasswordOptionsMask value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPasswordOptions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends PasswordOptionsMask> readPasswordOptionsAsync();

  /**
   * An asynchronous implementation of {@link #writePasswordOptions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePasswordOptionsAsync(PasswordOptionsMask value);

  /**
   * Get the PasswordOptions {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PasswordOptions {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPasswordOptionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPasswordOptionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPasswordOptionsNodeAsync();

  /**
   * Get the local value of the PasswordRestrictions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PasswordRestrictions Node.
   * @throws UaException if an error occurs creating or getting the PasswordRestrictions Node.
   */
  LocalizedText getPasswordRestrictions() throws UaException;

  /**
   * Set the local value of the PasswordRestrictions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PasswordRestrictions Node.
   * @throws UaException if an error occurs creating or getting the PasswordRestrictions Node.
   */
  void setPasswordRestrictions(LocalizedText value) throws UaException;

  /**
   * Read the value of the PasswordRestrictions Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readPasswordRestrictions() throws UaException;

  /**
   * Write a new value for the PasswordRestrictions Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePasswordRestrictions(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPasswordRestrictions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readPasswordRestrictionsAsync();

  /**
   * An asynchronous implementation of {@link #writePasswordRestrictions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePasswordRestrictionsAsync(LocalizedText value);

  /**
   * Get the PasswordRestrictions {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PasswordRestrictions {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPasswordRestrictionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPasswordRestrictionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPasswordRestrictionsNodeAsync();
}
