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
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditActivateSessionEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.10">Model
 *     documentation</a>
 */
public class AuditActivateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditActivateSessionEventType {
  public AuditActivateSessionEventTypeNode(
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

  public AuditActivateSessionEventTypeNode(
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
  public PropertyTypeNode getClientSoftwareCertificatesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientSoftwareCertificates",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 344L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] getClientSoftwareCertificates() {
    return ServerNodeSupport.readArray(
        this, getClientSoftwareCertificatesNode(), SignedSoftwareCertificate.class, null);
  }

  @Override
  public void setClientSoftwareCertificates(
      @Nullable SignedSoftwareCertificate @Nullable [] value) {
    ServerNodeSupport.write(this, getClientSoftwareCertificatesNode(), value, true, false, true);
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
  public PropertyTypeNode getSecureChannelIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecureChannelId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSecureChannelId() {
    return ServerNodeSupport.read(this, getSecureChannelIdNode(), String.class, null);
  }

  @Override
  public void setSecureChannelId(@Nullable String value) {
    ServerNodeSupport.write(this, getSecureChannelIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUserIdentityTokenNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UserIdentityToken",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 316L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UserIdentityToken getUserIdentityToken() {
    return ServerNodeSupport.read(this, getUserIdentityTokenNode(), UserIdentityToken.class, null);
  }

  @Override
  public void setUserIdentityToken(@Nullable UserIdentityToken value) {
    ServerNodeSupport.write(this, getUserIdentityTokenNode(), value, false, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getClientSoftwareCertificatesNode();
    getCurrentRoleIdsNode();
    getSecureChannelIdNode();
    getUserIdentityTokenNode();
  }
}
