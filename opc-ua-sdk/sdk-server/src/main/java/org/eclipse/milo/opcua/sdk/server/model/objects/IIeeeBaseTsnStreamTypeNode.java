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
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class IIeeeBaseTsnStreamTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnStreamType {
  public IIeeeBaseTsnStreamTypeNode(
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

  public IIeeeBaseTsnStreamTypeNode(
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
  public BaseDataVariableTypeNode getStreamIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "StreamId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UByte[] getStreamId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "StreamId");
    return component.map(node -> (UByte[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setStreamId(UByte[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "StreamId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getStreamNameNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "StreamName");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getStreamName() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "StreamName");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setStreamName(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "StreamName")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "State");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public TsnStreamState getState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "State");
    return component
        .map(node -> (TsnStreamState) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setState(TsnStreamState value) {
    getVariableComponent("http://opcfoundation.org/UA/", "State")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getAccumulatedLatencyNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AccumulatedLatency");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getAccumulatedLatency() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AccumulatedLatency");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setAccumulatedLatency(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AccumulatedLatency")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSrClassIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SrClassId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UByte getSrClassId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SrClassId");
    return component.map(node -> (UByte) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSrClassId(UByte value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SrClassId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
