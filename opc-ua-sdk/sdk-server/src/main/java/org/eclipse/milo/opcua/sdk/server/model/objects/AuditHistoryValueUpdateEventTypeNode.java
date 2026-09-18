package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditHistoryValueUpdateEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.3">Model
 *     documentation</a>
 */
public class AuditHistoryValueUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryValueUpdateEventType {
  public AuditHistoryValueUpdateEventTypeNode(
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

  public AuditHistoryValueUpdateEventTypeNode(
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
  public PropertyTypeNode getNewValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NewValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public DataValue @Nullable [] getNewValues() {
    return ServerNodeSupport.readArray(this, getNewValuesNode(), DataValue.class, null);
  }

  @Override
  public void setNewValues(DataValue @Nullable [] value) {
    ServerNodeSupport.write(this, getNewValuesNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getOldValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "OldValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public DataValue @Nullable [] getOldValues() {
    return ServerNodeSupport.readArray(this, getOldValuesNode(), DataValue.class, null);
  }

  @Override
  public void setOldValues(DataValue @Nullable [] value) {
    ServerNodeSupport.write(this, getOldValuesNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getPerformInsertReplaceNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PerformInsertReplace",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11293L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable PerformUpdateType getPerformInsertReplace() {
    return ServerNodeSupport.read(
        this, getPerformInsertReplaceNode(), PerformUpdateType.class, PerformUpdateType::from);
  }

  @Override
  public void setPerformInsertReplace(@Nullable PerformUpdateType value) {
    ServerNodeSupport.write(this, getPerformInsertReplaceNode(), value, false, true, false);
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
    getNewValuesNode();
    getOldValuesNode();
    getPerformInsertReplaceNode();
    getUpdatedNodeNode();
  }
}
