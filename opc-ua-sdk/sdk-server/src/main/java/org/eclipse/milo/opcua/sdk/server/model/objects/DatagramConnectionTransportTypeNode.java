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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DatagramConnectionTransportTypeNode extends ConnectionTransportTypeNode
    implements DatagramConnectionTransportType {
  public DatagramConnectionTransportTypeNode(
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

  public DatagramConnectionTransportTypeNode(
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
  public PropertyTypeNode getDiscoveryAnnounceRateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramConnectionTransportType.DISCOVERY_ANNOUNCE_RATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getDiscoveryAnnounceRate() {
    return getProperty(DatagramConnectionTransportType.DISCOVERY_ANNOUNCE_RATE).orElse(null);
  }

  @Override
  public void setDiscoveryAnnounceRate(UInteger value) {
    setProperty(DatagramConnectionTransportType.DISCOVERY_ANNOUNCE_RATE, value);
  }

  @Override
  public PropertyTypeNode getDiscoveryMaxMessageSizeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramConnectionTransportType.DISCOVERY_MAX_MESSAGE_SIZE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getDiscoveryMaxMessageSize() {
    return getProperty(DatagramConnectionTransportType.DISCOVERY_MAX_MESSAGE_SIZE).orElse(null);
  }

  @Override
  public void setDiscoveryMaxMessageSize(UInteger value) {
    setProperty(DatagramConnectionTransportType.DISCOVERY_MAX_MESSAGE_SIZE, value);
  }

  @Override
  public PropertyTypeNode getQosCategoryNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramConnectionTransportType.QOS_CATEGORY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getQosCategory() {
    return getProperty(DatagramConnectionTransportType.QOS_CATEGORY).orElse(null);
  }

  @Override
  public void setQosCategory(String value) {
    setProperty(DatagramConnectionTransportType.QOS_CATEGORY, value);
  }

  @Override
  public PropertyTypeNode getDatagramQosNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramConnectionTransportType.DATAGRAM_QOS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public QosDataType[] getDatagramQos() {
    return getProperty(DatagramConnectionTransportType.DATAGRAM_QOS).orElse(null);
  }

  @Override
  public void setDatagramQos(QosDataType[] value) {
    setProperty(DatagramConnectionTransportType.DATAGRAM_QOS, value);
  }

  @Override
  public NetworkAddressTypeNode getDiscoveryAddressNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "DiscoveryAddress");
    return (NetworkAddressTypeNode) component.orElse(null);
  }
}
