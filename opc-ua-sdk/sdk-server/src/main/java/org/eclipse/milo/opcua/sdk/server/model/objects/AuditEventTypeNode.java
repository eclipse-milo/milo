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
 * Node implementation of {@link AuditEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3">Model
 *     documentation</a>
 */
public class AuditEventTypeNode extends BaseEventTypeNode implements AuditEventType {
  public AuditEventTypeNode(
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

  public AuditEventTypeNode(
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
  public PropertyTypeNode getActionTimeStampNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ActionTimeStamp",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getActionTimeStamp() {
    return ServerNodeSupport.read(this, getActionTimeStampNode(), DateTime.class, null);
  }

  @Override
  public void setActionTimeStamp(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getActionTimeStampNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getClientApplicationUriNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ClientApplicationUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getClientApplicationUri() {
    return ServerNodeSupport.read(this, getClientApplicationUriNode(), String.class, null);
  }

  @Override
  public void setClientApplicationUri(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getClientApplicationUriNode(),
        Namespaces.OPC_UA,
        "ClientApplicationUri",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getClientAuditEntryIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientAuditEntryId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getClientAuditEntryId() {
    return ServerNodeSupport.read(this, getClientAuditEntryIdNode(), String.class, null);
  }

  @Override
  public void setClientAuditEntryId(@Nullable String value) {
    ServerNodeSupport.write(this, getClientAuditEntryIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getClientUserIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientUserId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getClientUserId() {
    return ServerNodeSupport.read(this, getClientUserIdNode(), String.class, null);
  }

  @Override
  public void setClientUserId(@Nullable String value) {
    ServerNodeSupport.write(this, getClientUserIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getServerIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getServerId() {
    return ServerNodeSupport.read(this, getServerIdNode(), String.class, null);
  }

  @Override
  public void setServerId(@Nullable String value) {
    ServerNodeSupport.write(this, getServerIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Status",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getStatus() {
    return ServerNodeSupport.read(this, getStatusNode(), Boolean.class, null);
  }

  @Override
  public void setStatus(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getStatusNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getActionTimeStampNode();
    getClientApplicationUriNode();
    getClientAuditEntryIdNode();
    getClientUserIdNode();
    getServerIdNode();
    getStatusNode();
  }
}
