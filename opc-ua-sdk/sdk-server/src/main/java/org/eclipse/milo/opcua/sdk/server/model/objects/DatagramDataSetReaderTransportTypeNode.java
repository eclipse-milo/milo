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
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DatagramDataSetReaderTransportTypeNode extends DataSetReaderTransportTypeNode
    implements DatagramDataSetReaderTransportType {
  public DatagramDataSetReaderTransportTypeNode(
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

  public DatagramDataSetReaderTransportTypeNode(
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
  public PropertyTypeNode getQosCategoryNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramDataSetReaderTransportType.QOS_CATEGORY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getQosCategory() {
    return getProperty(DatagramDataSetReaderTransportType.QOS_CATEGORY).orElse(null);
  }

  @Override
  public void setQosCategory(String value) {
    setProperty(DatagramDataSetReaderTransportType.QOS_CATEGORY, value);
  }

  @Override
  public PropertyTypeNode getDatagramQosNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DatagramDataSetReaderTransportType.DATAGRAM_QOS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ReceiveQosDataType[] getDatagramQos() {
    return getProperty(DatagramDataSetReaderTransportType.DATAGRAM_QOS).orElse(null);
  }

  @Override
  public void setDatagramQos(ReceiveQosDataType[] value) {
    setProperty(DatagramDataSetReaderTransportType.DATAGRAM_QOS, value);
  }

  @Override
  public PropertyTypeNode getTopicNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DatagramDataSetReaderTransportType.TOPIC);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getTopic() {
    return getProperty(DatagramDataSetReaderTransportType.TOPIC).orElse(null);
  }

  @Override
  public void setTopic(String value) {
    setProperty(DatagramDataSetReaderTransportType.TOPIC, value);
  }

  @Override
  public NetworkAddressTypeNode getAddressNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Address");
    return (NetworkAddressTypeNode) component.orElse(null);
  }
}
