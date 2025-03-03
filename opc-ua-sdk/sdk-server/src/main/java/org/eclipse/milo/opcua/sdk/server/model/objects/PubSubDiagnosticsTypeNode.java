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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubDiagnosticsTypeNode extends BaseObjectTypeNode implements PubSubDiagnosticsType {
  public PubSubDiagnosticsTypeNode(
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

  public PubSubDiagnosticsTypeNode(
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
  public BaseDataVariableTypeNode getDiagnosticsLevelNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DiagnosticsLevel");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public DiagnosticsLevel getDiagnosticsLevel() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DiagnosticsLevel");
    return component
        .map(node -> (DiagnosticsLevel) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setDiagnosticsLevel(DiagnosticsLevel value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DiagnosticsLevel")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalInformationNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TotalInformation");
    return (PubSubDiagnosticsCounterTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getTotalInformation() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TotalInformation");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTotalInformation(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TotalInformation")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public PubSubDiagnosticsCounterTypeNode getTotalErrorNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TotalError");
    return (PubSubDiagnosticsCounterTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getTotalError() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TotalError");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTotalError(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TotalError")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
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

  @Override
  public BaseDataVariableTypeNode getSubErrorNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SubError");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Boolean getSubError() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SubError");
    return component.map(node -> (Boolean) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSubError(Boolean value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SubError")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseObjectTypeNode getCountersNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Counters");
    return (BaseObjectTypeNode) component.orElse(null);
  }

  @Override
  public BaseObjectTypeNode getLiveValuesNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "LiveValues");
    return (BaseObjectTypeNode) component.orElse(null);
  }
}
