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
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class TargetVariablesTypeNode extends SubscribedDataSetTypeNode
    implements TargetVariablesType {
  public TargetVariablesTypeNode(
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

  public TargetVariablesTypeNode(
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
  public PropertyTypeNode getTargetVariablesNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TargetVariablesType.TARGET_VARIABLES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public FieldTargetDataType[] getTargetVariables() {
    return getProperty(TargetVariablesType.TARGET_VARIABLES).orElse(null);
  }

  @Override
  public void setTargetVariables(FieldTargetDataType[] value) {
    setProperty(TargetVariablesType.TARGET_VARIABLES, value);
  }

  @Override
  public UaMethodNode getAddTargetVariablesMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddTargetVariables",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveTargetVariablesMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveTargetVariables",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
