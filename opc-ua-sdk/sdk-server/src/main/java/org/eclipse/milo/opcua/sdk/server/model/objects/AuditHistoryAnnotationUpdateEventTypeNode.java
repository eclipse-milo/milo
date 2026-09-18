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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Annotation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditHistoryAnnotationUpdateEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.4">Model
 *     documentation</a>
 */
public class AuditHistoryAnnotationUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryAnnotationUpdateEventType {
  public AuditHistoryAnnotationUpdateEventTypeNode(
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

  public AuditHistoryAnnotationUpdateEventTypeNode(
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
        ExpandedNodeId.of(Namespaces.OPC_UA, 891L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Annotation @Nullable [] getNewValues() {
    return ServerNodeSupport.readArray(this, getNewValuesNode(), Annotation.class, null);
  }

  @Override
  public void setNewValues(@Nullable Annotation @Nullable [] value) {
    ServerNodeSupport.write(this, getNewValuesNode(), value, true, false, true);
  }

  @Override
  public PropertyTypeNode getOldValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "OldValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 891L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Annotation @Nullable [] getOldValues() {
    return ServerNodeSupport.readArray(this, getOldValuesNode(), Annotation.class, null);
  }

  @Override
  public void setOldValues(@Nullable Annotation @Nullable [] value) {
    ServerNodeSupport.write(this, getOldValuesNode(), value, true, false, true);
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
  public void validateChildren() {
    super.validateChildren();
    getNewValuesNode();
    getOldValuesNode();
    getPerformInsertReplaceNode();
  }
}
