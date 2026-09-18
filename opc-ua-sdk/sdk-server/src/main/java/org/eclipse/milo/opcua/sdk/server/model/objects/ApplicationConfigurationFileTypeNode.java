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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ApplicationConfigurationFileType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">Model
 *     documentation</a>
 */
public class ApplicationConfigurationFileTypeNode extends ConfigurationFileTypeNode
    implements ApplicationConfigurationFileType {
  public ApplicationConfigurationFileTypeNode(
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

  public ApplicationConfigurationFileTypeNode(
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
  public PropertyTypeNode getAvailableNetworksNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AvailableNetworks",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getAvailableNetworks() {
    return ServerNodeSupport.readArray(this, getAvailableNetworksNode(), String.class, null);
  }

  @Override
  public void setAvailableNetworks(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getAvailableNetworksNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getAvailablePortsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AvailablePorts",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 291L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getAvailablePorts() {
    return ServerNodeSupport.read(this, getAvailablePortsNode(), String.class, null);
  }

  @Override
  public void setAvailablePorts(@Nullable String value) {
    ServerNodeSupport.write(this, getAvailablePortsNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getCertificateGroupPurposesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CertificateGroupPurposes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getCertificateGroupPurposes() {
    return ServerNodeSupport.readArray(this, getCertificateGroupPurposesNode(), NodeId.class, null);
  }

  @Override
  public void setCertificateGroupPurposes(NodeId @Nullable [] value) {
    ServerNodeSupport.write(this, getCertificateGroupPurposesNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getCertificateTypesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CertificateTypes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getCertificateTypes() {
    return ServerNodeSupport.readArray(this, getCertificateTypesNode(), NodeId.class, null);
  }

  @Override
  public void setCertificateTypes(NodeId @Nullable [] value) {
    ServerNodeSupport.write(this, getCertificateTypesNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getMaxCertificateGroupsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxCertificateGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxCertificateGroups() {
    return ServerNodeSupport.read(this, getMaxCertificateGroupsNode(), UShort.class, null);
  }

  @Override
  public void setMaxCertificateGroups(@Nullable UShort value) {
    ServerNodeSupport.write(this, getMaxCertificateGroupsNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getMaxEndpointsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxEndpoints",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxEndpoints() {
    return ServerNodeSupport.read(this, getMaxEndpointsNode(), UShort.class, null);
  }

  @Override
  public void setMaxEndpoints(@Nullable UShort value) {
    ServerNodeSupport.write(this, getMaxEndpointsNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUrisNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityPolicyUris",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getSecurityPolicyUris() {
    return ServerNodeSupport.readArray(this, getSecurityPolicyUrisNode(), String.class, null);
  }

  @Override
  public void setSecurityPolicyUris(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getSecurityPolicyUrisNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getUserTokenTypesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UserTokenTypes",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 304L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UserTokenPolicy @Nullable [] getUserTokenTypes() {
    return ServerNodeSupport.readArray(this, getUserTokenTypesNode(), UserTokenPolicy.class, null);
  }

  @Override
  public void setUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value) {
    ServerNodeSupport.write(this, getUserTokenTypesNode(), value, true, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAvailableNetworksNode();
    getAvailablePortsNode();
    getCertificateGroupPurposesNode();
    getCertificateTypesNode();
    getMaxCertificateGroupsNode();
    getMaxEndpointsNode();
    getSecurityPolicyUrisNode();
    getUserTokenTypesNode();
  }

  @Override
  public void setMethods(ApplicationConfigurationFileType.@Nullable Methods methods) {
    setCloseHandler(methods == null ? null : methods::close);
    setCloseAndUpdateHandler(methods == null ? null : methods::closeAndUpdate);
    setConfirmUpdateHandler(methods == null ? null : methods::confirmUpdate);
    setGetPositionHandler(methods == null ? null : methods::getPosition);
    setOpenHandler(methods == null ? null : methods::open);
    setReadHandler(methods == null ? null : methods::read);
    setSetPositionHandler(methods == null ? null : methods::setPosition);
    setWriteHandler(methods == null ? null : methods::write);
  }
}
