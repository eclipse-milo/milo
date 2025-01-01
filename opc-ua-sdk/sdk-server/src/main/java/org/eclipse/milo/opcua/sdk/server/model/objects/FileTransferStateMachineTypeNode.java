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
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class FileTransferStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements FileTransferStateMachineType {
  public FileTransferStateMachineTypeNode(
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

  public FileTransferStateMachineTypeNode(
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
  public InitialStateTypeNode getIdleNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Idle");
    return (InitialStateTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getReadPrepareNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadPrepare");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getReadTransferNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadTransfer");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getApplyWriteNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ApplyWrite");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public StateTypeNode getErrorNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Error");
    return (StateTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getIdleToReadPrepareNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "IdleToReadPrepare");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getReadPrepareToReadTransferNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadPrepareToReadTransfer");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getReadTransferToIdleNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadTransferToIdle");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getIdleToApplyWriteNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "IdleToApplyWrite");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getApplyWriteToIdleNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ApplyWriteToIdle");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getReadPrepareToErrorNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadPrepareToError");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getReadTransferToErrorNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ReadTransferToError");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getApplyWriteToErrorNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ApplyWriteToError");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public TransitionTypeNode getErrorToIdleNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ErrorToIdle");
    return (TransitionTypeNode) component.orElse(null);
  }

  @Override
  public UaMethodNode getResetMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Reset",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
