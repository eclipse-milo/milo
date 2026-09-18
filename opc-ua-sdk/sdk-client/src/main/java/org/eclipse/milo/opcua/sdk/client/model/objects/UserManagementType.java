package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the UserManagementType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.1">Model
 *     documentation</a>
 */
public interface UserManagementType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24264L);

  QualifiedProperty<Range> PasswordLength_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PasswordLength",
          ExpandedNodeId.of(Namespaces.OPC_UA, 884L),
          -1,
          Range.class);

  QualifiedProperty<PasswordOptionsMask> PasswordOptions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PasswordOptions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24277L),
          -1,
          PasswordOptionsMask.class);

  QualifiedProperty<LocalizedText> PasswordRestrictions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PasswordRestrictions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<UserManagementDataType[]> Users_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Users",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24281L),
          1,
          UserManagementDataType[].class);

  /**
   * Resolves the mandatory PasswordLength child, a PropertyType with DataType Range.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPasswordLengthNode() throws UaException;

  /** Asynchronous form of {@link #getPasswordLengthNode()}. */
  CompletableFuture<? extends PropertyType> getPasswordLengthNodeAsync();

  /**
   * Reads the Value of the PasswordLength child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Range readPasswordLength() throws UaException;

  /**
   * Writes the Value of the PasswordLength child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePasswordLength(@Nullable Range value) throws UaException;

  /** Asynchronous form of {@link #readPasswordLength()}. */
  CompletableFuture<? extends @Nullable Range> readPasswordLengthAsync();

  /** Asynchronous form of {@link #writePasswordLength}; completes with the operation status. */
  CompletableFuture<StatusCode> writePasswordLengthAsync(@Nullable Range value);

  /**
   * Resolves the mandatory PasswordOptions child, a PropertyType with DataType PasswordOptionsMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPasswordOptionsNode() throws UaException;

  /** Asynchronous form of {@link #getPasswordOptionsNode()}. */
  CompletableFuture<? extends PropertyType> getPasswordOptionsNodeAsync();

  /**
   * Reads the Value of the PasswordOptions child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PasswordOptionsMask readPasswordOptions() throws UaException;

  /**
   * Writes the Value of the PasswordOptions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePasswordOptions(@Nullable PasswordOptionsMask value) throws UaException;

  /** Asynchronous form of {@link #readPasswordOptions()}. */
  CompletableFuture<? extends @Nullable PasswordOptionsMask> readPasswordOptionsAsync();

  /** Asynchronous form of {@link #writePasswordOptions}; completes with the operation status. */
  CompletableFuture<StatusCode> writePasswordOptionsAsync(@Nullable PasswordOptionsMask value);

  /**
   * Resolves the optional PasswordRestrictions child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getPasswordRestrictionsNode() throws UaException;

  /** Asynchronous form of {@link #getPasswordRestrictionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getPasswordRestrictionsNodeAsync();

  /**
   * Reads the Value of the PasswordRestrictions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readPasswordRestrictions() throws UaException;

  /**
   * Writes the Value of the PasswordRestrictions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePasswordRestrictions(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readPasswordRestrictions()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readPasswordRestrictionsAsync();

  /**
   * Asynchronous form of {@link #writePasswordRestrictions}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writePasswordRestrictionsAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory Users child, a PropertyType with DataType UserManagementDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUsersNode() throws UaException;

  /** Asynchronous form of {@link #getUsersNode()}. */
  CompletableFuture<? extends PropertyType> getUsersNodeAsync();

  /**
   * Reads the Value of the Users child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UserManagementDataType @Nullable [] readUsers() throws UaException;

  /**
   * Writes the Value of the Users child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUsers(@Nullable UserManagementDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readUsers()}. */
  CompletableFuture<? extends @Nullable UserManagementDataType @Nullable []> readUsersAsync();

  /** Asynchronous form of {@link #writeUsers}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUsersAsync(
      @Nullable UserManagementDataType @Nullable [] value);

  /**
   * Resolves the mandatory AddUser Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5">Model
   *     documentation</a>
   */
  UaMethodNode getAddUserMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddUserMethodNode()}. */
  CompletableFuture<UaMethodNode> getAddUserMethodNodeAsync();

  /**
   * Calls the AddUser Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5">Model
   *     documentation</a>
   */
  void addUser(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException;

  /**
   * Calls the AddUser Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddUser(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException;

  /**
   * Calls the AddUser Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddUserWith(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description)
      throws UaException;

  /** Asynchronous form of {@link #addUser}. */
  CompletableFuture<Void> addUserAsync(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description);

  /** Asynchronous form of {@link #callAddUser}. */
  CompletableFuture<MethodCallResult<Void>> callAddUserAsync(
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description);

  /** Asynchronous form of {@link #callAddUserWith}. */
  CompletableFuture<MethodCallResult<Void>> callAddUserWithAsync(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable String password,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable String description);

  /**
   * Resolves the mandatory ChangePassword Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8">Model
   *     documentation</a>
   */
  UaMethodNode getChangePasswordMethodNode() throws UaException;

  /** Asynchronous form of {@link #getChangePasswordMethodNode()}. */
  CompletableFuture<UaMethodNode> getChangePasswordMethodNodeAsync();

  /**
   * Calls the ChangePassword Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8">Model
   *     documentation</a>
   */
  void changePassword(@Nullable String oldPassword, @Nullable String newPassword)
      throws UaException;

  /**
   * Calls the ChangePassword Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callChangePassword(
      @Nullable String oldPassword, @Nullable String newPassword) throws UaException;

  /**
   * Calls the ChangePassword Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callChangePasswordWith(
      MethodCallOptions options, @Nullable String oldPassword, @Nullable String newPassword)
      throws UaException;

  /** Asynchronous form of {@link #changePassword}. */
  CompletableFuture<Void> changePasswordAsync(
      @Nullable String oldPassword, @Nullable String newPassword);

  /** Asynchronous form of {@link #callChangePassword}. */
  CompletableFuture<MethodCallResult<Void>> callChangePasswordAsync(
      @Nullable String oldPassword, @Nullable String newPassword);

  /** Asynchronous form of {@link #callChangePasswordWith}. */
  CompletableFuture<MethodCallResult<Void>> callChangePasswordWithAsync(
      MethodCallOptions options, @Nullable String oldPassword, @Nullable String newPassword);

  /**
   * Resolves the mandatory ModifyUser Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6">Model
   *     documentation</a>
   */
  UaMethodNode getModifyUserMethodNode() throws UaException;

  /** Asynchronous form of {@link #getModifyUserMethodNode()}. */
  CompletableFuture<UaMethodNode> getModifyUserMethodNodeAsync();

  /**
   * Calls the ModifyUser Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6">Model
   *     documentation</a>
   */
  void modifyUser(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException;

  /**
   * Calls the ModifyUser Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callModifyUser(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException;

  /**
   * Calls the ModifyUser Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callModifyUserWith(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description)
      throws UaException;

  /** Asynchronous form of {@link #modifyUser}. */
  CompletableFuture<Void> modifyUserAsync(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description);

  /** Asynchronous form of {@link #callModifyUser}. */
  CompletableFuture<MethodCallResult<Void>> callModifyUserAsync(
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description);

  /** Asynchronous form of {@link #callModifyUserWith}. */
  CompletableFuture<MethodCallResult<Void>> callModifyUserWithAsync(
      MethodCallOptions options,
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description);

  /**
   * Resolves the mandatory RemoveUser Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveUserMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveUserMethodNode()}. */
  CompletableFuture<UaMethodNode> getRemoveUserMethodNodeAsync();

  /**
   * Calls the RemoveUser Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7">Model
   *     documentation</a>
   */
  void removeUser(@Nullable String userName) throws UaException;

  /**
   * Calls the RemoveUser Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveUser(@Nullable String userName) throws UaException;

  /**
   * Calls the RemoveUser Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveUserWith(MethodCallOptions options, @Nullable String userName)
      throws UaException;

  /** Asynchronous form of {@link #removeUser}. */
  CompletableFuture<Void> removeUserAsync(@Nullable String userName);

  /** Asynchronous form of {@link #callRemoveUser}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveUserAsync(@Nullable String userName);

  /** Asynchronous form of {@link #callRemoveUserWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveUserWithAsync(
      MethodCallOptions options, @Nullable String userName);
}
