package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link TransactionDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17">Model
 *     documentation</a>
 */
public class TransactionDiagnosticsTypeNode extends BaseObjectTypeNode
    implements TransactionDiagnosticsType {
  public TransactionDiagnosticsTypeNode(
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

  public TransactionDiagnosticsTypeNode(
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
  public PropertyTypeNode getAffectedCertificateGroupsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AffectedCertificateGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getAffectedCertificateGroups() {
    return ServerNodeSupport.readArray(
        this, getAffectedCertificateGroupsNode(), NodeId.class, null);
  }

  @Override
  public void setAffectedCertificateGroups(NodeId @Nullable [] value) {
    ServerNodeSupport.write(this, getAffectedCertificateGroupsNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getAffectedTrustListsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AffectedTrustLists",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getAffectedTrustLists() {
    return ServerNodeSupport.readArray(this, getAffectedTrustListsNode(), NodeId.class, null);
  }

  @Override
  public void setAffectedTrustLists(NodeId @Nullable [] value) {
    ServerNodeSupport.write(this, getAffectedTrustListsNode(), value, true, false, false);
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
  public PropertyTypeNode getErrorsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Errors",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 32285L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable TransactionErrorType @Nullable [] getErrors() {
    return ServerNodeSupport.readArray(this, getErrorsNode(), TransactionErrorType.class, null);
  }

  @Override
  public void setErrors(@Nullable TransactionErrorType @Nullable [] value) {
    ServerNodeSupport.write(this, getErrorsNode(), value, true, false, true);
  }

  @Override
  public PropertyTypeNode getResultNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Result",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable StatusCode getResult() {
    return ServerNodeSupport.read(this, getResultNode(), StatusCode.class, null);
  }

  @Override
  public void setResult(@Nullable StatusCode value) {
    ServerNodeSupport.write(this, getResultNode(), value, false, false, false);
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
  public void validateChildren() {
    super.validateChildren();
    getAffectedCertificateGroupsNode();
    getAffectedTrustListsNode();
    getEndTimeNode();
    getErrorsNode();
    getResultNode();
    getStartTimeNode();
  }
}
