package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoricalEventConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3">Model
 *     documentation</a>
 */
public class HistoricalEventConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalEventConfigurationType {
  public HistoricalEventConfigurationTypeNode(
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

  public HistoricalEventConfigurationTypeNode(
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
  public FolderTypeNode getEventTypesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EventTypes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 61L),
        null,
        -1,
        FolderTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getSortByEventFieldsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SortByEventFields",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 601L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable SimpleAttributeOperand @Nullable [] getSortByEventFields() {
    return ServerNodeSupport.readArray(
        this, getSortByEventFieldsNode(), SimpleAttributeOperand.class, null);
  }

  @Override
  public void setSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getSortByEventFieldsNode(),
        Namespaces.OPC_UA,
        "SortByEventFields",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfArchiveNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "StartOfArchive",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getStartOfArchive() {
    return ServerNodeSupport.read(this, getStartOfArchiveNode(), DateTime.class, null);
  }

  @Override
  public void setStartOfArchive(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getStartOfArchiveNode(),
        Namespaces.OPC_UA,
        "StartOfArchive",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfOnlineArchiveNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "StartOfOnlineArchive",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getStartOfOnlineArchive() {
    return ServerNodeSupport.read(this, getStartOfOnlineArchiveNode(), DateTime.class, null);
  }

  @Override
  public void setStartOfOnlineArchive(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getStartOfOnlineArchiveNode(),
        Namespaces.OPC_UA,
        "StartOfOnlineArchive",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEventTypesNode();
    getSortByEventFieldsNode();
    getStartOfArchiveNode();
    getStartOfOnlineArchiveNode();
  }
}
