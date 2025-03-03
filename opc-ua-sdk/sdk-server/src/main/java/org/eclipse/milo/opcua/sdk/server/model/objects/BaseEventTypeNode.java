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
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;

public class BaseEventTypeNode extends BaseObjectTypeNode implements BaseEventType {
  public BaseEventTypeNode(
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

  public BaseEventTypeNode(
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
  public PropertyTypeNode getEventIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.EVENT_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ByteString getEventId() {
    return getProperty(BaseEventType.EVENT_ID).orElse(null);
  }

  @Override
  public void setEventId(ByteString value) {
    setProperty(BaseEventType.EVENT_ID, value);
  }

  @Override
  public PropertyTypeNode getEventTypeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.EVENT_TYPE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getEventType() {
    return getProperty(BaseEventType.EVENT_TYPE).orElse(null);
  }

  @Override
  public void setEventType(NodeId value) {
    setProperty(BaseEventType.EVENT_TYPE, value);
  }

  @Override
  public PropertyTypeNode getSourceNodeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.SOURCE_NODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getSourceNode() {
    return getProperty(BaseEventType.SOURCE_NODE).orElse(null);
  }

  @Override
  public void setSourceNode(NodeId value) {
    setProperty(BaseEventType.SOURCE_NODE, value);
  }

  @Override
  public PropertyTypeNode getSourceNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.SOURCE_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSourceName() {
    return getProperty(BaseEventType.SOURCE_NAME).orElse(null);
  }

  @Override
  public void setSourceName(String value) {
    setProperty(BaseEventType.SOURCE_NAME, value);
  }

  @Override
  public PropertyTypeNode getTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getTime() {
    return getProperty(BaseEventType.TIME).orElse(null);
  }

  @Override
  public void setTime(DateTime value) {
    setProperty(BaseEventType.TIME, value);
  }

  @Override
  public PropertyTypeNode getReceiveTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.RECEIVE_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getReceiveTime() {
    return getProperty(BaseEventType.RECEIVE_TIME).orElse(null);
  }

  @Override
  public void setReceiveTime(DateTime value) {
    setProperty(BaseEventType.RECEIVE_TIME, value);
  }

  @Override
  public PropertyTypeNode getLocalTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.LOCAL_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public TimeZoneDataType getLocalTime() {
    return getProperty(BaseEventType.LOCAL_TIME).orElse(null);
  }

  @Override
  public void setLocalTime(TimeZoneDataType value) {
    setProperty(BaseEventType.LOCAL_TIME, value);
  }

  @Override
  public PropertyTypeNode getMessageNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.MESSAGE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getMessage() {
    return getProperty(BaseEventType.MESSAGE).orElse(null);
  }

  @Override
  public void setMessage(LocalizedText value) {
    setProperty(BaseEventType.MESSAGE, value);
  }

  @Override
  public PropertyTypeNode getSeverityNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.SEVERITY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getSeverity() {
    return getProperty(BaseEventType.SEVERITY).orElse(null);
  }

  @Override
  public void setSeverity(UShort value) {
    setProperty(BaseEventType.SEVERITY, value);
  }

  @Override
  public PropertyTypeNode getConditionClassIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.CONDITION_CLASS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getConditionClassId() {
    return getProperty(BaseEventType.CONDITION_CLASS_ID).orElse(null);
  }

  @Override
  public void setConditionClassId(NodeId value) {
    setProperty(BaseEventType.CONDITION_CLASS_ID, value);
  }

  @Override
  public PropertyTypeNode getConditionClassNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.CONDITION_CLASS_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getConditionClassName() {
    return getProperty(BaseEventType.CONDITION_CLASS_NAME).orElse(null);
  }

  @Override
  public void setConditionClassName(LocalizedText value) {
    setProperty(BaseEventType.CONDITION_CLASS_NAME, value);
  }

  @Override
  public PropertyTypeNode getConditionSubClassIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.CONDITION_SUB_CLASS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getConditionSubClassId() {
    return getProperty(BaseEventType.CONDITION_SUB_CLASS_ID).orElse(null);
  }

  @Override
  public void setConditionSubClassId(NodeId[] value) {
    setProperty(BaseEventType.CONDITION_SUB_CLASS_ID, value);
  }

  @Override
  public PropertyTypeNode getConditionSubClassNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(BaseEventType.CONDITION_SUB_CLASS_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText[] getConditionSubClassName() {
    return getProperty(BaseEventType.CONDITION_SUB_CLASS_NAME).orElse(null);
  }

  @Override
  public void setConditionSubClassName(LocalizedText[] value) {
    setProperty(BaseEventType.CONDITION_SUB_CLASS_NAME, value);
  }
}
