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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditOpenSecureChannelEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6">Model
 *     documentation</a>
 */
public class AuditOpenSecureChannelEventTypeNode extends AuditChannelEventTypeNode
    implements AuditOpenSecureChannelEventType {
  public AuditOpenSecureChannelEventTypeNode(
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

  public AuditOpenSecureChannelEventTypeNode(
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
  public @Nullable PropertyTypeNode getCertificateErrorEventIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "CertificateErrorEventId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ByteString getCertificateErrorEventId() {
    return ServerNodeSupport.read(this, getCertificateErrorEventIdNode(), ByteString.class, null);
  }

  @Override
  public void setCertificateErrorEventId(@Nullable ByteString value) {
    ServerNodeSupport.write(
        this,
        getCertificateErrorEventIdNode(),
        Namespaces.OPC_UA,
        "CertificateErrorEventId",
        value,
        false,
        false,
        false);
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
  public PropertyTypeNode getRequestTypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RequestType",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 315L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable SecurityTokenRequestType getRequestType() {
    return ServerNodeSupport.read(
        this, getRequestTypeNode(), SecurityTokenRequestType.class, SecurityTokenRequestType::from);
  }

  @Override
  public void setRequestType(@Nullable SecurityTokenRequestType value) {
    ServerNodeSupport.write(this, getRequestTypeNode(), value, false, true, false);
  }

  @Override
  public PropertyTypeNode getRequestedLifetimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RequestedLifetime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getRequestedLifetime() {
    return ServerNodeSupport.read(this, getRequestedLifetimeNode(), Double.class, null);
  }

  @Override
  public void setRequestedLifetime(@Nullable Double value) {
    ServerNodeSupport.write(this, getRequestedLifetimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSecurityModeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityMode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 302L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable MessageSecurityMode getSecurityMode() {
    return ServerNodeSupport.read(
        this, getSecurityModeNode(), MessageSecurityMode.class, MessageSecurityMode::from);
  }

  @Override
  public void setSecurityMode(@Nullable MessageSecurityMode value) {
    ServerNodeSupport.write(this, getSecurityModeNode(), value, false, true, false);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityPolicyUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSecurityPolicyUri() {
    return ServerNodeSupport.read(this, getSecurityPolicyUriNode(), String.class, null);
  }

  @Override
  public void setSecurityPolicyUri(@Nullable String value) {
    ServerNodeSupport.write(this, getSecurityPolicyUriNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCertificateErrorEventIdNode();
    getClientCertificateNode();
    getClientCertificateThumbprintNode();
    getRequestTypeNode();
    getRequestedLifetimeNode();
    getSecurityModeNode();
    getSecurityPolicyUriNode();
  }
}
