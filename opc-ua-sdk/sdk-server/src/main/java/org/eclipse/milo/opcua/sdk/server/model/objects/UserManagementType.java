package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.UserConfigurationMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the UserManagementType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.1">Model
 *     documentation</a>
 */
public interface UserManagementType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24264L);

  /**
   * Returns the mandatory PasswordLength child, a PropertyType with DataType Range.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPasswordLengthNode();

  /**
   * Returns the Value of the PasswordLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Range getPasswordLength();

  /**
   * Sets the Value of the PasswordLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPasswordLength(@Nullable Range value);

  /**
   * Returns the mandatory PasswordOptions child, a PropertyType with DataType PasswordOptionsMask.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPasswordOptionsNode();

  /**
   * Returns the Value of the PasswordOptions child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PasswordOptionsMask getPasswordOptions();

  /**
   * Sets the Value of the PasswordOptions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPasswordOptions(@Nullable PasswordOptionsMask value);

  /**
   * Returns the optional PasswordRestrictions child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getPasswordRestrictionsNode();

  /**
   * Returns the Value of the PasswordRestrictions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getPasswordRestrictions();

  /**
   * Sets the Value of the PasswordRestrictions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPasswordRestrictions(@Nullable LocalizedText value);

  /**
   * Returns the mandatory Users child, a PropertyType with DataType UserManagementDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUsersNode();

  /**
   * Returns the Value of the Users child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UserManagementDataType @Nullable [] getUsers();

  /**
   * Sets the Value of the Users child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUsers(@Nullable UserManagementDataType @Nullable [] value);

  /**
   * Returns the mandatory AddUser Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5">Model
   *     documentation</a>
   */
  UaMethodNode getAddUserMethodNode();

  /**
   * Sets this instance's AddUser handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddUserHandler(@Nullable AddUserHandler handler);

  /**
   * Returns the mandatory ChangePassword Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8">Model
   *     documentation</a>
   */
  UaMethodNode getChangePasswordMethodNode();

  /**
   * Sets this instance's ChangePassword handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setChangePasswordHandler(@Nullable ChangePasswordHandler handler);

  /**
   * Returns the mandatory ModifyUser Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6">Model
   *     documentation</a>
   */
  UaMethodNode getModifyUserMethodNode();

  /**
   * Sets this instance's ModifyUser handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setModifyUserHandler(@Nullable ModifyUserHandler handler);

  /**
   * Returns the mandatory RemoveUser Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveUserMethodNode();

  /**
   * Sets this instance's RemoveUser handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveUserHandler(@Nullable RemoveUserHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the AddUser Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddUserHandler {
    /**
     * Handles a call to the AddUser Method.
     *
     * @throws UaException if the call fails.
     */
    void addUser(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String userName,
        @Nullable String password,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable String description)
        throws UaException;
  }

  /**
   * Handles calls to the ChangePassword Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.8">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ChangePasswordHandler {
    /**
     * Handles a call to the ChangePassword Method.
     *
     * @throws UaException if the call fails.
     */
    void changePassword(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String oldPassword,
        @Nullable String newPassword)
        throws UaException;
  }

  /**
   * Handles calls to the ModifyUser Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ModifyUserHandler {
    /**
     * Handles a call to the ModifyUser Method.
     *
     * @throws UaException if the call fails.
     */
    void modifyUser(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String userName,
        @Nullable Boolean modifyPassword,
        @Nullable String password,
        @Nullable Boolean modifyUserConfiguration,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable Boolean modifyDescription,
        @Nullable String description)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveUser Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveUserHandler {
    /**
     * Handles a call to the RemoveUser Method.
     *
     * @throws UaException if the call fails.
     */
    void removeUser(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String userName)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the AddUser Method; see {@link AddUserHandler#addUser}. */
    default void addUser(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String userName,
        @Nullable String password,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable String description)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the ChangePassword Method; see {@link
     * ChangePasswordHandler#changePassword}.
     */
    default void changePassword(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String oldPassword,
        @Nullable String newPassword)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the ModifyUser Method; see {@link ModifyUserHandler#modifyUser}. */
    default void modifyUser(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String userName,
        @Nullable Boolean modifyPassword,
        @Nullable String password,
        @Nullable Boolean modifyUserConfiguration,
        @Nullable UserConfigurationMask userConfiguration,
        @Nullable Boolean modifyDescription,
        @Nullable String description)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the RemoveUser Method; see {@link RemoveUserHandler#removeUser}. */
    default void removeUser(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable String userName)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}
