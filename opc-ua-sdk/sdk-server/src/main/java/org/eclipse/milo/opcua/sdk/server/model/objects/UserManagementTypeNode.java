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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PasswordOptionsMask;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;

public class UserManagementTypeNode extends BaseObjectTypeNode implements UserManagementType {
  public UserManagementTypeNode(
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

  public UserManagementTypeNode(
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
  public PropertyTypeNode getUsersNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UserManagementType.USERS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UserManagementDataType[] getUsers() {
    return getProperty(UserManagementType.USERS).orElse(null);
  }

  @Override
  public void setUsers(UserManagementDataType[] value) {
    setProperty(UserManagementType.USERS, value);
  }

  @Override
  public PropertyTypeNode getPasswordLengthNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UserManagementType.PASSWORD_LENGTH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Range getPasswordLength() {
    return getProperty(UserManagementType.PASSWORD_LENGTH).orElse(null);
  }

  @Override
  public void setPasswordLength(Range value) {
    setProperty(UserManagementType.PASSWORD_LENGTH, value);
  }

  @Override
  public PropertyTypeNode getPasswordOptionsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UserManagementType.PASSWORD_OPTIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public PasswordOptionsMask getPasswordOptions() {
    return getProperty(UserManagementType.PASSWORD_OPTIONS).orElse(null);
  }

  @Override
  public void setPasswordOptions(PasswordOptionsMask value) {
    setProperty(UserManagementType.PASSWORD_OPTIONS, value);
  }

  @Override
  public PropertyTypeNode getPasswordRestrictionsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UserManagementType.PASSWORD_RESTRICTIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getPasswordRestrictions() {
    return getProperty(UserManagementType.PASSWORD_RESTRICTIONS).orElse(null);
  }

  @Override
  public void setPasswordRestrictions(LocalizedText value) {
    setProperty(UserManagementType.PASSWORD_RESTRICTIONS, value);
  }

  @Override
  public UaMethodNode getAddUserMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddUser",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getModifyUserMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ModifyUser",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveUserMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveUser",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getChangePasswordMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ChangePassword",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
