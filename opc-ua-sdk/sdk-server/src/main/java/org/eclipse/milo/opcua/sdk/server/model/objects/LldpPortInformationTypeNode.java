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
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class LldpPortInformationTypeNode extends BaseObjectTypeNode
    implements LldpPortInformationType {
  public LldpPortInformationTypeNode(
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

  public LldpPortInformationTypeNode(
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
  public PropertyTypeNode getIetfBaseNetworkInterfaceNameNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(LldpPortInformationType.IETF_BASE_NETWORK_INTERFACE_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getIetfBaseNetworkInterfaceName() {
    return getProperty(LldpPortInformationType.IETF_BASE_NETWORK_INTERFACE_NAME).orElse(null);
  }

  @Override
  public void setIetfBaseNetworkInterfaceName(String value) {
    setProperty(LldpPortInformationType.IETF_BASE_NETWORK_INTERFACE_NAME, value);
  }

  @Override
  public PropertyTypeNode getDestMacAddressNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpPortInformationType.DEST_MAC_ADDRESS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UByte[] getDestMacAddress() {
    return getProperty(LldpPortInformationType.DEST_MAC_ADDRESS).orElse(null);
  }

  @Override
  public void setDestMacAddress(UByte[] value) {
    setProperty(LldpPortInformationType.DEST_MAC_ADDRESS, value);
  }

  @Override
  public PropertyTypeNode getPortIdSubtypeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpPortInformationType.PORT_ID_SUBTYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public PortIdSubtype getPortIdSubtype() {
    return getProperty(LldpPortInformationType.PORT_ID_SUBTYPE).orElse(null);
  }

  @Override
  public void setPortIdSubtype(PortIdSubtype value) {
    setProperty(LldpPortInformationType.PORT_ID_SUBTYPE, value);
  }

  @Override
  public PropertyTypeNode getPortIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpPortInformationType.PORT_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getPortId() {
    return getProperty(LldpPortInformationType.PORT_ID).orElse(null);
  }

  @Override
  public void setPortId(String value) {
    setProperty(LldpPortInformationType.PORT_ID, value);
  }

  @Override
  public PropertyTypeNode getPortDescriptionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(LldpPortInformationType.PORT_DESCRIPTION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getPortDescription() {
    return getProperty(LldpPortInformationType.PORT_DESCRIPTION).orElse(null);
  }

  @Override
  public void setPortDescription(String value) {
    setProperty(LldpPortInformationType.PORT_DESCRIPTION, value);
  }

  @Override
  public PropertyTypeNode getManagementAddressTxPortNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(LldpPortInformationType.MANAGEMENT_ADDRESS_TX_PORT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LldpManagementAddressTxPortType[] getManagementAddressTxPort() {
    return getProperty(LldpPortInformationType.MANAGEMENT_ADDRESS_TX_PORT).orElse(null);
  }

  @Override
  public void setManagementAddressTxPort(LldpManagementAddressTxPortType[] value) {
    setProperty(LldpPortInformationType.MANAGEMENT_ADDRESS_TX_PORT, value);
  }

  @Override
  public FolderTypeNode getRemoteSystemsDataNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "RemoteSystemsData");
    return (FolderTypeNode) component.orElse(null);
  }
}
