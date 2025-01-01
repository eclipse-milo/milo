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

public class IPriorityMappingEntryTypeNode extends BaseInterfaceTypeNode
    implements IPriorityMappingEntryType {
  public IPriorityMappingEntryTypeNode(
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

  public IPriorityMappingEntryTypeNode(
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
  public BaseDataVariableTypeNode getMappingUriNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MappingUri");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getMappingUri() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MappingUri");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMappingUri(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MappingUri")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPriorityLabelNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityLabel");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getPriorityLabel() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityLabel");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPriorityLabel(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PriorityLabel")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPriorityValue_PCPNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityValue_PCP");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UByte getPriorityValuePcp() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityValue_PCP");
    return component.map(node -> (UByte) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPriorityValuePcp(UByte value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PriorityValue_PCP")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPriorityValue_DSCPNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityValue_DSCP");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getPriorityValueDscp() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PriorityValue_DSCP");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPriorityValueDscp(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PriorityValue_DSCP")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
