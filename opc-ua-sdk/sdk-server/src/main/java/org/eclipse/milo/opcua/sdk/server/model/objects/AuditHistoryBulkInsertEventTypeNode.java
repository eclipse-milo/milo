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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditHistoryBulkInsertEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.10">Model
 *     documentation</a>
 */
public class AuditHistoryBulkInsertEventTypeNode extends AuditEventTypeNode
    implements AuditHistoryBulkInsertEventType {
  public AuditHistoryBulkInsertEventTypeNode(
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

  public AuditHistoryBulkInsertEventTypeNode(
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
  public PropertyTypeNode getEndTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EndTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getEndTime() {
    return ServerNodeSupport.read(this, getEndTimeNode(), DateTime.class, null);
  }

  @Override
  public void setEndTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getEndTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getStartTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StartTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getStartTime() {
    return ServerNodeSupport.read(this, getStartTimeNode(), DateTime.class, null);
  }

  @Override
  public void setStartTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getStartTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUpdatedNodeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UpdatedNode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getUpdatedNode() {
    return ServerNodeSupport.read(this, getUpdatedNodeNode(), NodeId.class, null);
  }

  @Override
  public void setUpdatedNode(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getUpdatedNodeNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEndTimeNode();
    getStartTimeNode();
    getUpdatedNodeNode();
  }
}
