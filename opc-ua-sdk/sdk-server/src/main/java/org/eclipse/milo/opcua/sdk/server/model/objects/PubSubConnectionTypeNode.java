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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PubSubConnectionTypeNode extends BaseObjectTypeNode implements PubSubConnectionType {
  public PubSubConnectionTypeNode(
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

  public PubSubConnectionTypeNode(
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
  public PropertyTypeNode getPublisherIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PubSubConnectionType.PUBLISHER_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object getPublisherId() {
    return getProperty(PubSubConnectionType.PUBLISHER_ID).orElse(null);
  }

  @Override
  public void setPublisherId(Object value) {
    setProperty(PubSubConnectionType.PUBLISHER_ID, value);
  }

  @Override
  public PropertyTypeNode getConnectionPropertiesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PubSubConnectionType.CONNECTION_PROPERTIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public KeyValuePair[] getConnectionProperties() {
    return getProperty(PubSubConnectionType.CONNECTION_PROPERTIES).orElse(null);
  }

  @Override
  public void setConnectionProperties(KeyValuePair[] value) {
    setProperty(PubSubConnectionType.CONNECTION_PROPERTIES, value);
  }

  @Override
  public SelectionListTypeNode getTransportProfileUriNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransportProfileUri");
    return (SelectionListTypeNode) component.orElse(null);
  }

  @Override
  public String getTransportProfileUri() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransportProfileUri");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTransportProfileUri(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TransportProfileUri")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public NetworkAddressTypeNode getAddressNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Address");
    return (NetworkAddressTypeNode) component.orElse(null);
  }

  @Override
  public ConnectionTransportTypeNode getTransportSettingsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "TransportSettings");
    return (ConnectionTransportTypeNode) component.orElse(null);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Status");
    return (PubSubStatusTypeNode) component.orElse(null);
  }

  @Override
  public PubSubDiagnosticsConnectionTypeNode getDiagnosticsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "Diagnostics");
    return (PubSubDiagnosticsConnectionTypeNode) component.orElse(null);
  }

  @Override
  public UaMethodNode getAddWriterGroupMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddWriterGroup",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getAddReaderGroupMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddReaderGroup",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveGroupMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveGroup",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
