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

public class OperationLimitsTypeNode extends FolderTypeNode implements OperationLimitsType {
  public OperationLimitsTypeNode(
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

  public OperationLimitsTypeNode(
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
  public PropertyTypeNode getMaxNodesPerReadNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(OperationLimitsType.MAX_NODES_PER_READ);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerRead() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_READ).orElse(null);
  }

  @Override
  public void setMaxNodesPerRead(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_READ, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryReadDataNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_HISTORY_READ_DATA);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerHistoryReadData() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_READ_DATA).orElse(null);
  }

  @Override
  public void setMaxNodesPerHistoryReadData(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_READ_DATA, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryReadEventsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_HISTORY_READ_EVENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerHistoryReadEvents() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_READ_EVENTS).orElse(null);
  }

  @Override
  public void setMaxNodesPerHistoryReadEvents(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_READ_EVENTS, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerWriteNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(OperationLimitsType.MAX_NODES_PER_WRITE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerWrite() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_WRITE).orElse(null);
  }

  @Override
  public void setMaxNodesPerWrite(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_WRITE, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryUpdateDataNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_HISTORY_UPDATE_DATA);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerHistoryUpdateData() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_UPDATE_DATA).orElse(null);
  }

  @Override
  public void setMaxNodesPerHistoryUpdateData(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_UPDATE_DATA, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerHistoryUpdateEventsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_HISTORY_UPDATE_EVENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerHistoryUpdateEvents() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_UPDATE_EVENTS).orElse(null);
  }

  @Override
  public void setMaxNodesPerHistoryUpdateEvents(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_HISTORY_UPDATE_EVENTS, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerMethodCallNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_METHOD_CALL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerMethodCall() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_METHOD_CALL).orElse(null);
  }

  @Override
  public void setMaxNodesPerMethodCall(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_METHOD_CALL, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerBrowseNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(OperationLimitsType.MAX_NODES_PER_BROWSE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerBrowse() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_BROWSE).orElse(null);
  }

  @Override
  public void setMaxNodesPerBrowse(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_BROWSE, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerRegisterNodesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_REGISTER_NODES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerRegisterNodes() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_REGISTER_NODES).orElse(null);
  }

  @Override
  public void setMaxNodesPerRegisterNodes(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_REGISTER_NODES, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerTranslateBrowsePathsToNodeIdsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_TRANSLATE_BROWSE_PATHS_TO_NODE_IDS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_TRANSLATE_BROWSE_PATHS_TO_NODE_IDS)
        .orElse(null);
  }

  @Override
  public void setMaxNodesPerTranslateBrowsePathsToNodeIds(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_TRANSLATE_BROWSE_PATHS_TO_NODE_IDS, value);
  }

  @Override
  public PropertyTypeNode getMaxNodesPerNodeManagementNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_NODES_PER_NODE_MANAGEMENT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxNodesPerNodeManagement() {
    return getProperty(OperationLimitsType.MAX_NODES_PER_NODE_MANAGEMENT).orElse(null);
  }

  @Override
  public void setMaxNodesPerNodeManagement(UInteger value) {
    setProperty(OperationLimitsType.MAX_NODES_PER_NODE_MANAGEMENT, value);
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsPerCallNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(OperationLimitsType.MAX_MONITORED_ITEMS_PER_CALL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxMonitoredItemsPerCall() {
    return getProperty(OperationLimitsType.MAX_MONITORED_ITEMS_PER_CALL).orElse(null);
  }

  @Override
  public void setMaxMonitoredItemsPerCall(UInteger value) {
    setProperty(OperationLimitsType.MAX_MONITORED_ITEMS_PER_CALL, value);
  }
}
