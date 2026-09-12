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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.UserConfigurationMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable UserManagementDataType @Nullable [] getUsers() throws UaException;

  /** Sets the existing node's local value. */
  void setUsers(@Nullable UserManagementDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UserManagementDataType @Nullable [] readUsers() throws UaException;

  /** Writes the value remotely. */
  void writeUsers(@Nullable UserManagementDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UserManagementDataType @Nullable []> readUsersAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUsersAsync(
      @Nullable UserManagementDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUsersNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUsersNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Range getPasswordLength() throws UaException;

  /** Sets the existing node's local value. */
  void setPasswordLength(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Range readPasswordLength() throws UaException;

  /** Writes the value remotely. */
  void writePasswordLength(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Range> readPasswordLengthAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePasswordLengthAsync(@Nullable Range value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPasswordLengthNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPasswordLengthNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable PasswordOptionsMask getPasswordOptions() throws UaException;

  /** Sets the existing node's local value. */
  void setPasswordOptions(@Nullable PasswordOptionsMask value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PasswordOptionsMask readPasswordOptions() throws UaException;

  /** Writes the value remotely. */
  void writePasswordOptions(@Nullable PasswordOptionsMask value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PasswordOptionsMask> readPasswordOptionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePasswordOptionsAsync(@Nullable PasswordOptionsMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPasswordOptionsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPasswordOptionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getPasswordRestrictions() throws UaException;

  /** Sets the existing node's local value. */
  void setPasswordRestrictions(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readPasswordRestrictions() throws UaException;

  /** Writes the value remotely. */
  void writePasswordRestrictions(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readPasswordRestrictionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePasswordRestrictionsAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getPasswordRestrictionsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getPasswordRestrictionsNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getAddUserMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAddUserMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Invokes <code>AddUser</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAddUser(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Invokes <code>AddUser</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAddUserAsync(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Invokes <code>AddUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddUserDetailed(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Invokes <code>AddUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddUserDetailed(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Invokes <code>AddUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callAddUserDetailedAsync(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Invokes <code>AddUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callAddUserDetailedAsync(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getModifyUserMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getModifyUserMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Invokes <code>ModifyUser</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callModifyUser(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Invokes <code>ModifyUser</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callModifyUserAsync(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Invokes <code>ModifyUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callModifyUserDetailed(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Invokes <code>ModifyUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callModifyUserDetailed(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Invokes <code>ModifyUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callModifyUserDetailedAsync(
          @Nullable String userName,
          @Nullable Boolean modifyPassword,
          @Nullable String password,
          @Nullable Boolean modifyUserConfiguration,
          @Nullable UserConfigurationMask userConfiguration,
          @Nullable Boolean modifyDescription,
          @Nullable String description);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Invokes <code>ModifyUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callModifyUserDetailedAsync(
          MethodCallOptions options,
          @Nullable String userName,
          @Nullable Boolean modifyPassword,
          @Nullable String password,
          @Nullable Boolean modifyUserConfiguration,
          @Nullable UserConfigurationMask userConfiguration,
          @Nullable Boolean modifyDescription,
          @Nullable String description);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getRemoveUserMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getRemoveUserMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Invokes <code>RemoveUser</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveUser(@Nullable String userName) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Invokes <code>RemoveUser</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveUserAsync(@Nullable String userName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Invokes <code>RemoveUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveUserDetailed(@Nullable String userName)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Invokes <code>RemoveUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveUserDetailed(
      MethodCallOptions options, @Nullable String userName) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Invokes <code>RemoveUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveUserDetailedAsync(@Nullable String userName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Invokes <code>RemoveUser</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveUserDetailedAsync(MethodCallOptions options, @Nullable String userName);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getChangePasswordMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getChangePasswordMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Invokes <code>ChangePassword</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callChangePassword(@Nullable String oldPassword, @Nullable String newPassword)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Invokes <code>ChangePassword</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callChangePasswordAsync(
      @Nullable String oldPassword, @Nullable String newPassword);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Invokes <code>ChangePassword</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callChangePasswordDetailed(
      @Nullable String oldPassword, @Nullable String newPassword) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Invokes <code>ChangePassword</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callChangePasswordDetailed(
      MethodCallOptions options, @Nullable String oldPassword, @Nullable String newPassword)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Invokes <code>ChangePassword</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callChangePasswordDetailedAsync(@Nullable String oldPassword, @Nullable String newPassword);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Invokes <code>ChangePassword</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callChangePasswordDetailedAsync(
          MethodCallOptions options, @Nullable String oldPassword, @Nullable String newPassword);
}
