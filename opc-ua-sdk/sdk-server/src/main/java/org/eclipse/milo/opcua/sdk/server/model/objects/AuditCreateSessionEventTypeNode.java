package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 * Node implementation of {@link AuditCreateSessionEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8">Model
 *     documentation</a>
 */
public class AuditCreateSessionEventTypeNode extends AuditSessionEventTypeNode
    implements AuditCreateSessionEventType {
  public AuditCreateSessionEventTypeNode(
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

  public AuditCreateSessionEventTypeNode(
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
  public PropertyTypeNode getClientCertificateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientCertificate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ByteString getClientCertificate() {
    return ServerNodeSupport.read(this, getClientCertificateNode(), ByteString.class, null);
  }

  @Override
  public void setClientCertificate(@Nullable ByteString value) {
    ServerNodeSupport.write(this, getClientCertificateNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getClientCertificateThumbprintNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientCertificateThumbprint",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getClientCertificateThumbprint() {
    return ServerNodeSupport.read(this, getClientCertificateThumbprintNode(), String.class, null);
  }

  @Override
  public void setClientCertificateThumbprint(@Nullable String value) {
    ServerNodeSupport.write(this, getClientCertificateThumbprintNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getRevisedSessionTimeoutNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RevisedSessionTimeout",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getRevisedSessionTimeout() {
    return ServerNodeSupport.read(this, getRevisedSessionTimeoutNode(), Double.class, null);
  }

  @Override
  public void setRevisedSessionTimeout(@Nullable Double value) {
    ServerNodeSupport.write(this, getRevisedSessionTimeoutNode(), value, false, false, false);
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
  public void validateChildren() {
    super.validateChildren();
    getClientCertificateNode();
    getClientCertificateThumbprintNode();
    getRevisedSessionTimeoutNode();
    getSecureChannelIdNode();
  }
}
