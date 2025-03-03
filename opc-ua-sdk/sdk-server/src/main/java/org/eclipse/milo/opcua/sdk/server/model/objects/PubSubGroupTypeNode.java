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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubGroupTypeNode extends BaseObjectTypeNode implements PubSubGroupType {
  public PubSubGroupTypeNode(
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

  public PubSubGroupTypeNode(
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
  public PropertyTypeNode getSecurityModeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubGroupType.SECURITY_MODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public MessageSecurityMode getSecurityMode() {
    return getProperty(PubSubGroupType.SECURITY_MODE).orElse(null);
  }

  @Override
  public void setSecurityMode(MessageSecurityMode value) {
    setProperty(PubSubGroupType.SECURITY_MODE, value);
  }

  @Override
  public PropertyTypeNode getSecurityGroupIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubGroupType.SECURITY_GROUP_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSecurityGroupId() {
    return getProperty(PubSubGroupType.SECURITY_GROUP_ID).orElse(null);
  }

  @Override
  public void setSecurityGroupId(String value) {
    setProperty(PubSubGroupType.SECURITY_GROUP_ID, value);
  }

  @Override
  public PropertyTypeNode getSecurityKeyServicesNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubGroupType.SECURITY_KEY_SERVICES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EndpointDescription[] getSecurityKeyServices() {
    return getProperty(PubSubGroupType.SECURITY_KEY_SERVICES).orElse(null);
  }

  @Override
  public void setSecurityKeyServices(EndpointDescription[] value) {
    setProperty(PubSubGroupType.SECURITY_KEY_SERVICES, value);
  }

  @Override
  public PropertyTypeNode getMaxNetworkMessageSizeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubGroupType.MAX_NETWORK_MESSAGE_SIZE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNetworkMessageSize() {
    return getProperty(PubSubGroupType.MAX_NETWORK_MESSAGE_SIZE).orElse(null);
  }

  @Override
  public void setMaxNetworkMessageSize(UInteger value) {
    setProperty(PubSubGroupType.MAX_NETWORK_MESSAGE_SIZE, value);
  }

  @Override
  public PropertyTypeNode getGroupPropertiesNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubGroupType.GROUP_PROPERTIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public KeyValuePair[] getGroupProperties() {
    return getProperty(PubSubGroupType.GROUP_PROPERTIES).orElse(null);
  }

  @Override
  public void setGroupProperties(KeyValuePair[] value) {
    setProperty(PubSubGroupType.GROUP_PROPERTIES, value);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Status");
    return (PubSubStatusTypeNode) component.orElse(null);
  }
}
