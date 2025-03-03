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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LldpRemoteSystemTypeNode extends BaseObjectTypeNode implements LldpRemoteSystemType {
  public LldpRemoteSystemTypeNode(
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

  public LldpRemoteSystemTypeNode(
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
  public BaseDataVariableTypeNode getTimeMarkNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TimeMark");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getTimeMark() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TimeMark");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTimeMark(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TimeMark")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteIndexNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteIndex");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRemoteIndex() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteIndex");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRemoteIndex(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteIndex")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getChassisIdSubtypeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ChassisIdSubtype");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ChassisIdSubtype getChassisIdSubtype() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ChassisIdSubtype");
    return component
        .map(node -> (ChassisIdSubtype) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setChassisIdSubtype(ChassisIdSubtype value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ChassisIdSubtype")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getChassisIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ChassisId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getChassisId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ChassisId");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setChassisId(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ChassisId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPortIdSubtypeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PortIdSubtype");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public PortIdSubtype getPortIdSubtype() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PortIdSubtype");
    return component
        .map(node -> (PortIdSubtype) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setPortIdSubtype(PortIdSubtype value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PortIdSubtype")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPortIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PortId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getPortId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PortId");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPortId(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PortId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPortDescriptionNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PortDescription");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getPortDescription() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PortDescription");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPortDescription(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PortDescription")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSystemNameNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemName");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getSystemName() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemName");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSystemName(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SystemName")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSystemDescriptionNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemDescription");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getSystemDescription() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemDescription");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSystemDescription(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SystemDescription")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSystemCapabilitiesSupportedNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemCapabilitiesSupported");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemCapabilitiesSupported");
    return component
        .map(node -> (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SystemCapabilitiesSupported")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSystemCapabilitiesEnabledNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemCapabilitiesEnabled");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SystemCapabilitiesEnabled");
    return component
        .map(node -> (LldpSystemCapabilitiesMap) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SystemCapabilitiesEnabled")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteChangesNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteChanges");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Boolean getRemoteChanges() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteChanges");
    return component.map(node -> (Boolean) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRemoteChanges(Boolean value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteChanges")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteTooManyNeighborsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteTooManyNeighbors");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Boolean getRemoteTooManyNeighbors() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteTooManyNeighbors");
    return component.map(node -> (Boolean) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRemoteTooManyNeighbors(Boolean value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteTooManyNeighbors")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getManagementAddressNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ManagementAddress");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public LldpManagementAddressType[] getManagementAddress() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ManagementAddress");
    return component
        .map(node -> (LldpManagementAddressType[]) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setManagementAddress(LldpManagementAddressType[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ManagementAddress")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRemoteUnknownTlvNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteUnknownTlv");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public LldpTlvType[] getRemoteUnknownTlv() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RemoteUnknownTlv");
    return component
        .map(node -> (LldpTlvType[]) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setRemoteUnknownTlv(LldpTlvType[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RemoteUnknownTlv")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
