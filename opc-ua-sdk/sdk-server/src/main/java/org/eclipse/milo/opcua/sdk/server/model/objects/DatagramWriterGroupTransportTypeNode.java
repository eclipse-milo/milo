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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;

public class DatagramWriterGroupTransportTypeNode extends WriterGroupTransportTypeNode
    implements DatagramWriterGroupTransportType {
  public DatagramWriterGroupTransportTypeNode(
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

  public DatagramWriterGroupTransportTypeNode(
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
  public PropertyTypeNode getMessageRepeatCountNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramWriterGroupTransportType.MESSAGE_REPEAT_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UByte getMessageRepeatCount() {
    return getProperty(DatagramWriterGroupTransportType.MESSAGE_REPEAT_COUNT).orElse(null);
  }

  @Override
  public void setMessageRepeatCount(UByte value) {
    setProperty(DatagramWriterGroupTransportType.MESSAGE_REPEAT_COUNT, value);
  }

  @Override
  public PropertyTypeNode getMessageRepeatDelayNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramWriterGroupTransportType.MESSAGE_REPEAT_DELAY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMessageRepeatDelay() {
    return getProperty(DatagramWriterGroupTransportType.MESSAGE_REPEAT_DELAY).orElse(null);
  }

  @Override
  public void setMessageRepeatDelay(Double value) {
    setProperty(DatagramWriterGroupTransportType.MESSAGE_REPEAT_DELAY, value);
  }

  @Override
  public PropertyTypeNode getQosCategoryNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramWriterGroupTransportType.QOS_CATEGORY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getQosCategory() {
    return getProperty(DatagramWriterGroupTransportType.QOS_CATEGORY).orElse(null);
  }

  @Override
  public void setQosCategory(String value) {
    setProperty(DatagramWriterGroupTransportType.QOS_CATEGORY, value);
  }

  @Override
  public PropertyTypeNode getDatagramQosNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramWriterGroupTransportType.DATAGRAM_QOS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public TransmitQosDataType[] getDatagramQos() {
    return getProperty(DatagramWriterGroupTransportType.DATAGRAM_QOS).orElse(null);
  }

  @Override
  public void setDatagramQos(TransmitQosDataType[] value) {
    setProperty(DatagramWriterGroupTransportType.DATAGRAM_QOS, value);
  }

  @Override
  public PropertyTypeNode getDiscoveryAnnounceRateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramWriterGroupTransportType.DISCOVERY_ANNOUNCE_RATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getDiscoveryAnnounceRate() {
    return getProperty(DatagramWriterGroupTransportType.DISCOVERY_ANNOUNCE_RATE).orElse(null);
  }

  @Override
  public void setDiscoveryAnnounceRate(UInteger value) {
    setProperty(DatagramWriterGroupTransportType.DISCOVERY_ANNOUNCE_RATE, value);
  }

  @Override
  public PropertyTypeNode getTopicNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DatagramWriterGroupTransportType.TOPIC);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getTopic() {
    return getProperty(DatagramWriterGroupTransportType.TOPIC).orElse(null);
  }

  @Override
  public void setTopic(String value) {
    setProperty(DatagramWriterGroupTransportType.TOPIC, value);
  }

  @Override
  public NetworkAddressTypeNode getAddressNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Address");
    return (NetworkAddressTypeNode) component.orElse(null);
  }
}
