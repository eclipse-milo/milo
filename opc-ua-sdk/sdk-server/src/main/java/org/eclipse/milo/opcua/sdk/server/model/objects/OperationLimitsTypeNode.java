package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link OperationLimitsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11">Model
 *     documentation</a>
 */
public class OperationLimitsTypeNode extends FolderTypeNode implements OperationLimitsType {
  public OperationLimitsTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions) {
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

  public OperationLimitsTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions,
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

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerCallNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxMonitoredItemsPerCall",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsPerCall() {
    return ServerNodeSupport.read(this, getMaxMonitoredItemsPerCallNode(), UInteger.class, null);
  }

  @Override
  public void setMaxMonitoredItemsPerCall(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxMonitoredItemsPerCallNode(),
        Namespaces.OPC_UA,
        "MaxMonitoredItemsPerCall",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerBrowseNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerBrowse",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerBrowse() {
    return ServerNodeSupport.read(this, getMaxNodesPerBrowseNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerBrowse(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerBrowseNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerBrowse",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadDataNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryReadData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryReadData() {
    return ServerNodeSupport.read(this, getMaxNodesPerHistoryReadDataNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerHistoryReadData(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerHistoryReadDataNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryReadData",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadEventsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryReadEvents",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryReadEvents() {
    return ServerNodeSupport.read(
        this, getMaxNodesPerHistoryReadEventsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerHistoryReadEvents(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerHistoryReadEventsNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryReadEvents",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateDataNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryUpdateData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryUpdateData() {
    return ServerNodeSupport.read(
        this, getMaxNodesPerHistoryUpdateDataNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerHistoryUpdateData(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerHistoryUpdateDataNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryUpdateData",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateEventsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryUpdateEvents",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryUpdateEvents() {
    return ServerNodeSupport.read(
        this, getMaxNodesPerHistoryUpdateEventsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerHistoryUpdateEventsNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerHistoryUpdateEvents",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerMethodCallNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerMethodCall",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerMethodCall() {
    return ServerNodeSupport.read(this, getMaxNodesPerMethodCallNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerMethodCall(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerMethodCallNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerMethodCall",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerNodeManagementNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerNodeManagement",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerNodeManagement() {
    return ServerNodeSupport.read(this, getMaxNodesPerNodeManagementNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerNodeManagement(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerNodeManagementNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerNodeManagement",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerReadNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerRead",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerRead() {
    return ServerNodeSupport.read(this, getMaxNodesPerReadNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerRead(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerReadNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerRead",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerRegisterNodesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerRegisterNodes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerRegisterNodes() {
    return ServerNodeSupport.read(this, getMaxNodesPerRegisterNodesNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerRegisterNodes(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerRegisterNodesNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerRegisterNodes",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerTranslateBrowsePathsToNodeIdsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerTranslateBrowsePathsToNodeIds",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds() {
    return ServerNodeSupport.read(
        this, getMaxNodesPerTranslateBrowsePathsToNodeIdsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerTranslateBrowsePathsToNodeIdsNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerTranslateBrowsePathsToNodeIds",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerWriteNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNodesPerWrite",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerWrite() {
    return ServerNodeSupport.read(this, getMaxNodesPerWriteNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNodesPerWrite(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNodesPerWriteNode(),
        Namespaces.OPC_UA,
        "MaxNodesPerWrite",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getMaxMonitoredItemsPerCallNode();
    getMaxNodesPerBrowseNode();
    getMaxNodesPerHistoryReadDataNode();
    getMaxNodesPerHistoryReadEventsNode();
    getMaxNodesPerHistoryUpdateDataNode();
    getMaxNodesPerHistoryUpdateEventsNode();
    getMaxNodesPerMethodCallNode();
    getMaxNodesPerNodeManagementNode();
    getMaxNodesPerReadNode();
    getMaxNodesPerRegisterNodesNode();
    getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();
    getMaxNodesPerWriteNode();
  }
}
