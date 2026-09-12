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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.UserConfigurationMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;
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
  @Nullable UserManagementDataType @Nullable [] getUsers();

  /** Sets the existing node's local value. */
  void setUsers(@Nullable UserManagementDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUsersNode();

  /** Gets the existing node's local value. */
  @Nullable Range getPasswordLength();

  /** Sets the existing node's local value. */
  void setPasswordLength(@Nullable Range value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPasswordLengthNode();

  /** Gets the existing node's local value. */
  @Nullable PasswordOptionsMask getPasswordOptions();

  /** Sets the existing node's local value. */
  void setPasswordOptions(@Nullable PasswordOptionsMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPasswordOptionsNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getPasswordRestrictions();

  /** Sets the existing node's local value. */
  void setPasswordRestrictions(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getPasswordRestrictionsNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getAddUserMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddUser(MethodBindings bindings, AddUserHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddUserDetailed(MethodBindings bindings, AddUserDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getModifyUserMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindModifyUser(MethodBindings bindings, ModifyUserHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindModifyUserDetailed(MethodBindings bindings, ModifyUserDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getRemoveUserMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveUser(MethodBindings bindings, RemoveUserHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveUserDetailed(MethodBindings bindings, RemoveUserDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getChangePasswordMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindChangePassword(MethodBindings bindings, ChangePasswordHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindChangePasswordDetailed(
      MethodBindings bindings, ChangePasswordDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5 */
  @FunctionalInterface
  interface AddUserHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String userName,
        @Nullable String password,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable String description)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5 */
  @FunctionalInterface
  interface AddUserDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String userName,
        @Nullable String password,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable String description)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6 */
  @FunctionalInterface
  interface ModifyUserHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String userName,
        @Nullable Boolean modifyPassword,
        @Nullable String password,
        @Nullable Boolean modifyUserConfiguration,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable Boolean modifyDescription,
        @Nullable String description)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6 */
  @FunctionalInterface
  interface ModifyUserDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String userName,
        @Nullable Boolean modifyPassword,
        @Nullable String password,
        @Nullable Boolean modifyUserConfiguration,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable Boolean modifyDescription,
        @Nullable String description)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7 */
  @FunctionalInterface
  interface RemoveUserHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String userName)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7 */
  @FunctionalInterface
  interface RemoveUserDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String userName)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8 */
  @FunctionalInterface
  interface ChangePasswordHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String oldPassword,
        @Nullable String newPassword)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8 */
  @FunctionalInterface
  interface ChangePasswordDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String oldPassword,
        @Nullable String newPassword)
        throws UaException;
  }
}
