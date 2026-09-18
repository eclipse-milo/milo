package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SessionDiagnosticsObjectType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">Model
 *     documentation</a>
 */
public class SessionDiagnosticsObjectTypeNode extends BaseObjectTypeNode
    implements SessionDiagnosticsObjectType {
  public SessionDiagnosticsObjectTypeNode(
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

  public SessionDiagnosticsObjectTypeNode(
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
  public @Nullable PropertyTypeNode getCurrentRoleIdsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "CurrentRoleIds",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getCurrentRoleIds() {
    return ServerNodeSupport.readArray(this, getCurrentRoleIdsNode(), NodeId.class, null);
  }

  @Override
  public void setCurrentRoleIds(NodeId @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getCurrentRoleIdsNode(),
        Namespaces.OPC_UA,
        "CurrentRoleIds",
        value,
        true,
        false,
        false);
  }

  @Override
  public SessionDiagnosticsVariableTypeNode getSessionDiagnosticsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionDiagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2197L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 865L),
        -1,
        SessionDiagnosticsVariableTypeNode.class);
  }

  @Override
  public @Nullable SessionDiagnosticsDataType getSessionDiagnostics() {
    return ServerNodeSupport.read(
        this, getSessionDiagnosticsNode(), SessionDiagnosticsDataType.class, null);
  }

  @Override
  public void setSessionDiagnostics(@Nullable SessionDiagnosticsDataType value) {
    ServerNodeSupport.write(this, getSessionDiagnosticsNode(), value, false, false, true);
  }

  @Override
  public SessionSecurityDiagnosticsTypeNode getSessionSecurityDiagnosticsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionSecurityDiagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2244L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 868L),
        -1,
        SessionSecurityDiagnosticsTypeNode.class);
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics() {
    return ServerNodeSupport.read(
        this, getSessionSecurityDiagnosticsNode(), SessionSecurityDiagnosticsDataType.class, null);
  }

  @Override
  public void setSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value) {
    ServerNodeSupport.write(this, getSessionSecurityDiagnosticsNode(), value, false, false, true);
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SubscriptionDiagnosticsArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2171L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 874L),
        1,
        SubscriptionDiagnosticsArrayTypeNode.class);
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray() {
    return ServerNodeSupport.readArray(
        this, getSubscriptionDiagnosticsArrayNode(), SubscriptionDiagnosticsDataType.class, null);
  }

  @Override
  public void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) {
    ServerNodeSupport.write(this, getSubscriptionDiagnosticsArrayNode(), value, true, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCurrentRoleIdsNode();
    getSessionDiagnosticsNode();
    getSessionSecurityDiagnosticsNode();
    getSubscriptionDiagnosticsArrayNode();
  }
}
