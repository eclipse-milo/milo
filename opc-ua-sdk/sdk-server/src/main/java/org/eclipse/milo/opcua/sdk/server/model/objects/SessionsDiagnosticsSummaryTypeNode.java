package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SessionsDiagnosticsSummaryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">Model
 *     documentation</a>
 */
public class SessionsDiagnosticsSummaryTypeNode extends BaseObjectTypeNode
    implements SessionsDiagnosticsSummaryType {
  public SessionsDiagnosticsSummaryTypeNode(
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

  public SessionsDiagnosticsSummaryTypeNode(
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
  public SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionDiagnosticsArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2196L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 865L),
        1,
        SessionDiagnosticsArrayTypeNode.class);
  }

  @Override
  public @Nullable SessionDiagnosticsDataType @Nullable [] getSessionDiagnosticsArray() {
    return ServerNodeSupport.readArray(
        this, getSessionDiagnosticsArrayNode(), SessionDiagnosticsDataType.class, null);
  }

  @Override
  public void setSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value) {
    ServerNodeSupport.write(this, getSessionDiagnosticsArrayNode(), value, true, false, true);
  }

  @Override
  public SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionSecurityDiagnosticsArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2243L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 868L),
        1,
        SessionSecurityDiagnosticsArrayTypeNode.class);
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType @Nullable []
      getSessionSecurityDiagnosticsArray() {
    return ServerNodeSupport.readArray(
        this,
        getSessionSecurityDiagnosticsArrayNode(),
        SessionSecurityDiagnosticsDataType.class,
        null);
  }

  @Override
  public void setSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) {
    ServerNodeSupport.write(
        this, getSessionSecurityDiagnosticsArrayNode(), value, true, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getSessionDiagnosticsArrayNode();
    getSessionSecurityDiagnosticsArrayNode();
  }
}
