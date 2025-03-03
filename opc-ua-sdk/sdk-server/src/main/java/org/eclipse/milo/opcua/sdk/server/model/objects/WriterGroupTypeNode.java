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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class WriterGroupTypeNode extends PubSubGroupTypeNode implements WriterGroupType {
  public WriterGroupTypeNode(
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

  public WriterGroupTypeNode(
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
  public PropertyTypeNode getWriterGroupIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(WriterGroupType.WRITER_GROUP_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getWriterGroupId() {
    return getProperty(WriterGroupType.WRITER_GROUP_ID).orElse(null);
  }

  @Override
  public void setWriterGroupId(UShort value) {
    setProperty(WriterGroupType.WRITER_GROUP_ID, value);
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(WriterGroupType.PUBLISHING_INTERVAL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getPublishingInterval() {
    return getProperty(WriterGroupType.PUBLISHING_INTERVAL).orElse(null);
  }

  @Override
  public void setPublishingInterval(Double value) {
    setProperty(WriterGroupType.PUBLISHING_INTERVAL, value);
  }

  @Override
  public PropertyTypeNode getKeepAliveTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(WriterGroupType.KEEP_ALIVE_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getKeepAliveTime() {
    return getProperty(WriterGroupType.KEEP_ALIVE_TIME).orElse(null);
  }

  @Override
  public void setKeepAliveTime(Double value) {
    setProperty(WriterGroupType.KEEP_ALIVE_TIME, value);
  }

  @Override
  public PropertyTypeNode getPriorityNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(WriterGroupType.PRIORITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UByte getPriority() {
    return getProperty(WriterGroupType.PRIORITY).orElse(null);
  }

  @Override
  public void setPriority(UByte value) {
    setProperty(WriterGroupType.PRIORITY, value);
  }

  @Override
  public PropertyTypeNode getLocaleIdsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(WriterGroupType.LOCALE_IDS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getLocaleIds() {
    return getProperty(WriterGroupType.LOCALE_IDS).orElse(null);
  }

  @Override
  public void setLocaleIds(String[] value) {
    setProperty(WriterGroupType.LOCALE_IDS, value);
  }

  @Override
  public PropertyTypeNode getHeaderLayoutUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(WriterGroupType.HEADER_LAYOUT_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getHeaderLayoutUri() {
    return getProperty(WriterGroupType.HEADER_LAYOUT_URI).orElse(null);
  }

  @Override
  public void setHeaderLayoutUri(String value) {
    setProperty(WriterGroupType.HEADER_LAYOUT_URI, value);
  }

  @Override
  public WriterGroupTransportTypeNode getTransportSettingsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "TransportSettings");
    return (WriterGroupTransportTypeNode) component.orElse(null);
  }

  @Override
  public WriterGroupMessageTypeNode getMessageSettingsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "MessageSettings");
    return (WriterGroupMessageTypeNode) component.orElse(null);
  }

  @Override
  public PubSubDiagnosticsWriterGroupTypeNode getDiagnosticsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "Diagnostics");
    return (PubSubDiagnosticsWriterGroupTypeNode) component.orElse(null);
  }

  @Override
  public UaMethodNode getAddDataSetWriterMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddDataSetWriter",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getRemoveDataSetWriterMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "RemoveDataSetWriter",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
