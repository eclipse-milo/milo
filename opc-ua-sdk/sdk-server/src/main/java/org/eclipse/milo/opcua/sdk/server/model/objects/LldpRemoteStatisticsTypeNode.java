/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LldpRemoteStatisticsTypeNode extends BaseObjectTypeNode
    implements LldpRemoteStatisticsType {
  public LldpRemoteStatisticsTypeNode(
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

  public LldpRemoteStatisticsTypeNode(
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
  public BaseDataVariableTypeNode getLastChangeTimeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastChangeTime");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getLastChangeTime() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastChangeTime");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastChangeTime(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastChangeTime")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteInsertsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteInserts");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRemoteInserts() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteInserts");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRemoteInserts(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteInserts")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDeletesNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteDeletes");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRemoteDeletes() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteDeletes");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRemoteDeletes(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteDeletes")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDropsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteDrops");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRemoteDrops() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteDrops");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRemoteDrops(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteDrops")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteAgeoutsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteAgeouts");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRemoteAgeouts() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteAgeouts");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRemoteAgeouts(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteAgeouts")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
