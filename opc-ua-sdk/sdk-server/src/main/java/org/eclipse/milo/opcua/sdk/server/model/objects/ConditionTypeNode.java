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
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ConditionTypeNode extends BaseEventTypeNode implements ConditionType {
  public ConditionTypeNode(
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

  public ConditionTypeNode(
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
  public PropertyTypeNode getConditionClassIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.CONDITION_CLASS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getConditionClassId() {
    return getProperty(ConditionType.CONDITION_CLASS_ID).orElse(null);
  }

  @Override
  public void setConditionClassId(NodeId value) {
    setProperty(ConditionType.CONDITION_CLASS_ID, value);
  }

  @Override
  public PropertyTypeNode getConditionClassNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.CONDITION_CLASS_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText getConditionClassName() {
    return getProperty(ConditionType.CONDITION_CLASS_NAME).orElse(null);
  }

  @Override
  public void setConditionClassName(LocalizedText value) {
    setProperty(ConditionType.CONDITION_CLASS_NAME, value);
  }

  @Override
  public PropertyTypeNode getConditionSubClassIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.CONDITION_SUB_CLASS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getConditionSubClassId() {
    return getProperty(ConditionType.CONDITION_SUB_CLASS_ID).orElse(null);
  }

  @Override
  public void setConditionSubClassId(NodeId[] value) {
    setProperty(ConditionType.CONDITION_SUB_CLASS_ID, value);
  }

  @Override
  public PropertyTypeNode getConditionSubClassNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.CONDITION_SUB_CLASS_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public LocalizedText[] getConditionSubClassName() {
    return getProperty(ConditionType.CONDITION_SUB_CLASS_NAME).orElse(null);
  }

  @Override
  public void setConditionSubClassName(LocalizedText[] value) {
    setProperty(ConditionType.CONDITION_SUB_CLASS_NAME, value);
  }

  @Override
  public PropertyTypeNode getConditionNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.CONDITION_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getConditionName() {
    return getProperty(ConditionType.CONDITION_NAME).orElse(null);
  }

  @Override
  public void setConditionName(String value) {
    setProperty(ConditionType.CONDITION_NAME, value);
  }

  @Override
  public PropertyTypeNode getBranchIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.BRANCH_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getBranchId() {
    return getProperty(ConditionType.BRANCH_ID).orElse(null);
  }

  @Override
  public void setBranchId(NodeId value) {
    setProperty(ConditionType.BRANCH_ID, value);
  }

  @Override
  public PropertyTypeNode getRetainNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.RETAIN);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getRetain() {
    return getProperty(ConditionType.RETAIN).orElse(null);
  }

  @Override
  public void setRetain(Boolean value) {
    setProperty(ConditionType.RETAIN, value);
  }

  @Override
  public PropertyTypeNode getSupportsFilteredRetainNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.SUPPORTS_FILTERED_RETAIN);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getSupportsFilteredRetain() {
    return getProperty(ConditionType.SUPPORTS_FILTERED_RETAIN).orElse(null);
  }

  @Override
  public void setSupportsFilteredRetain(Boolean value) {
    setProperty(ConditionType.SUPPORTS_FILTERED_RETAIN, value);
  }

  @Override
  public PropertyTypeNode getClientUserIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ConditionType.CLIENT_USER_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getClientUserId() {
    return getProperty(ConditionType.CLIENT_USER_ID).orElse(null);
  }

  @Override
  public void setClientUserId(String value) {
    setProperty(ConditionType.CLIENT_USER_ID, value);
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EnabledState");
    return (TwoStateVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getEnabledState() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EnabledState");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setEnabledState(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "EnabledState")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public ConditionVariableTypeNode getQualityNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Quality");
    return (ConditionVariableTypeNode) component.orElse(null);
  }

  @Override
  public StatusCode getQuality() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Quality");
    return component.map(node -> (StatusCode) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setQuality(StatusCode value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Quality")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public ConditionVariableTypeNode getLastSeverityNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastSeverity");
    return (ConditionVariableTypeNode) component.orElse(null);
  }

  @Override
  public UShort getLastSeverity() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastSeverity");
    return component.map(node -> (UShort) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastSeverity(UShort value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastSeverity")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public ConditionVariableTypeNode getCommentNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Comment");
    return (ConditionVariableTypeNode) component.orElse(null);
  }

  @Override
  public LocalizedText getComment() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Comment");
    return component
        .map(node -> (LocalizedText) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setComment(LocalizedText value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Comment")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public UaMethodNode getDisableMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Disable",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getEnableMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "Enable",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getAddCommentMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "AddComment",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getConditionRefreshMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ConditionRefresh",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getConditionRefresh2MethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "ConditionRefresh2",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}
