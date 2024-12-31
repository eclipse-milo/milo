/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class SamplingIntervalDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SamplingIntervalDiagnosticsType {
  public SamplingIntervalDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  public SamplingIntervalDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  @Override
  public BaseDataVariableTypeNode getSamplingIntervalNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SamplingInterval");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getSamplingInterval() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SamplingInterval");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSamplingInterval(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SamplingInterval")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSampledMonitoredItemsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SampledMonitoredItemsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getSampledMonitoredItemsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SampledMonitoredItemsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSampledMonitoredItemsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SampledMonitoredItemsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaxSampledMonitoredItemsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxSampledMonitoredItemsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMaxSampledMonitoredItemsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxSampledMonitoredItemsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxSampledMonitoredItemsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxSampledMonitoredItemsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDisabledMonitoredItemsSamplingCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DisabledMonitoredItemsSamplingCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getDisabledMonitoredItemsSamplingCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DisabledMonitoredItemsSamplingCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDisabledMonitoredItemsSamplingCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DisabledMonitoredItemsSamplingCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
