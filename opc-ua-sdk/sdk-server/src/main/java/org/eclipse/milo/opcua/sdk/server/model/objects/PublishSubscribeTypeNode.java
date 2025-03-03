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
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class PublishSubscribeTypeNode extends PubSubKeyServiceTypeNode
    implements PublishSubscribeType {
  public PublishSubscribeTypeNode(
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

  public PublishSubscribeTypeNode(
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
  public PropertyTypeNode getSupportedTransportProfilesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PublishSubscribeType.SUPPORTED_TRANSPORT_PROFILES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getSupportedTransportProfiles() {
    return getProperty(PublishSubscribeType.SUPPORTED_TRANSPORT_PROFILES).orElse(null);
  }

  @Override
  public void setSupportedTransportProfiles(String[] value) {
    setProperty(PublishSubscribeType.SUPPORTED_TRANSPORT_PROFILES, value);
  }

  @Override
  public PropertyTypeNode getDefaultDatagramPublisherIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PublishSubscribeType.DEFAULT_DATAGRAM_PUBLISHER_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ULong getDefaultDatagramPublisherId() {
    return getProperty(PublishSubscribeType.DEFAULT_DATAGRAM_PUBLISHER_ID).orElse(null);
  }

  @Override
  public void setDefaultDatagramPublisherId(ULong value) {
    setProperty(PublishSubscribeType.DEFAULT_DATAGRAM_PUBLISHER_ID, value);
  }

  @Override
  public PropertyTypeNode getConfigurationVersionNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PublishSubscribeType.CONFIGURATION_VERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getConfigurationVersion() {
    return getProperty(PublishSubscribeType.CONFIGURATION_VERSION).orElse(null);
  }

  @Override
  public void setConfigurationVersion(UInteger value) {
    setProperty(PublishSubscribeType.CONFIGURATION_VERSION, value);
  }

  @Override
  public PropertyTypeNode getDefaultSecurityKeyServicesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PublishSubscribeType.DEFAULT_SECURITY_KEY_SERVICES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EndpointDescription[] getDefaultSecurityKeyServices() {
    return getProperty(PublishSubscribeType.DEFAULT_SECURITY_KEY_SERVICES).orElse(null);
  }

  @Override
  public void setDefaultSecurityKeyServices(EndpointDescription[] value) {
    setProperty(PublishSubscribeType.DEFAULT_SECURITY_KEY_SERVICES, value);
  }

  @Override
  public PropertyTypeNode getConfigurationPropertiesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PublishSubscribeType.CONFIGURATION_PROPERTIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public KeyValuePair[] getConfigurationProperties() {
    return getProperty(PublishSubscribeType.CONFIGURATION_PROPERTIES).orElse(null);
  }

  @Override
  public void setConfigurationProperties(KeyValuePair[] value) {
    setProperty(PublishSubscribeType.CONFIGURATION_PROPERTIES, value);
  }

  @Override
  public UaMethodNode getSetSecurityKeysMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "SetSecurityKeys",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getAddConnectionMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddConnection",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveConnectionMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveConnection",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public DataSetFolderTypeNode getPublishedDataSetsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "PublishedDataSets");
    return (DataSetFolderTypeNode) component.orElse(null);
  }

  @Override
  public SubscribedDataSetFolderTypeNode getSubscribedDataSetsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "SubscribedDataSets");
    return (SubscribedDataSetFolderTypeNode) component.orElse(null);
  }

  @Override
  public PubSubConfigurationTypeNode getPubSubConfigurationNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "PubSubConfiguration");
    return (PubSubConfigurationTypeNode) component.orElse(null);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Status");
    return (PubSubStatusTypeNode) component.orElse(null);
  }

  @Override
  public PubSubDiagnosticsRootTypeNode getDiagnosticsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "Diagnostics");
    return (PubSubDiagnosticsRootTypeNode) component.orElse(null);
  }

  @Override
  public PubSubCapabilitiesTypeNode getPubSubCapablitiesNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "PubSubCapablities");
    return (PubSubCapabilitiesTypeNode) component.orElse(null);
  }

  @Override
  public FolderTypeNode getDataSetClassesNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "DataSetClasses");
    return (FolderTypeNode) component.orElse(null);
  }
}
