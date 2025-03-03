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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;

public class PublishedEventsTypeNode extends PublishedDataSetTypeNode
    implements PublishedEventsType {
  public PublishedEventsTypeNode(
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

  public PublishedEventsTypeNode(
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
  public PropertyTypeNode getPubSubEventNotifierNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(PublishedEventsType.PUB_SUB_EVENT_NOTIFIER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getPubSubEventNotifier() {
    return getProperty(PublishedEventsType.PUB_SUB_EVENT_NOTIFIER).orElse(null);
  }

  @Override
  public void setPubSubEventNotifier(NodeId value) {
    setProperty(PublishedEventsType.PUB_SUB_EVENT_NOTIFIER, value);
  }

  @Override
  public PropertyTypeNode getSelectedFieldsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PublishedEventsType.SELECTED_FIELDS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public SimpleAttributeOperand[] getSelectedFields() {
    return getProperty(PublishedEventsType.SELECTED_FIELDS).orElse(null);
  }

  @Override
  public void setSelectedFields(SimpleAttributeOperand[] value) {
    setProperty(PublishedEventsType.SELECTED_FIELDS, value);
  }

  @Override
  public PropertyTypeNode getFilterNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(PublishedEventsType.FILTER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ContentFilter getFilter() {
    return getProperty(PublishedEventsType.FILTER).orElse(null);
  }

  @Override
  public void setFilter(ContentFilter value) {
    setProperty(PublishedEventsType.FILTER, value);
  }

  @Override
  public UaMethodNode getModifyFieldSelectionMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ModifyFieldSelection",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
