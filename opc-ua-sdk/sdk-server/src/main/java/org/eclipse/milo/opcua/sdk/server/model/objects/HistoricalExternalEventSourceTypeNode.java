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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoricalExternalEventSourceType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2">Model
 *     documentation</a>
 */
public class HistoricalExternalEventSourceTypeNode extends BaseObjectTypeNode
    implements HistoricalExternalEventSourceType {
  public HistoricalExternalEventSourceTypeNode(
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

  public HistoricalExternalEventSourceTypeNode(
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
  public @Nullable PropertyTypeNode getEndpointUrlNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "EndpointUrl",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getEndpointUrl() {
    return ServerNodeSupport.read(this, getEndpointUrlNode(), String.class, null);
  }

  @Override
  public void setEndpointUrl(@Nullable String value) {
    ServerNodeSupport.write(
        this, getEndpointUrlNode(), Namespaces.OPC_UA, "EndpointUrl", value, false, false, false);
  }

  @Override
  public PropertyTypeNode getHistoricalEventFilterNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "HistoricalEventFilter",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 725L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable EventFilter getHistoricalEventFilter() {
    return ServerNodeSupport.read(this, getHistoricalEventFilterNode(), EventFilter.class, null);
  }

  @Override
  public void setHistoricalEventFilter(@Nullable EventFilter value) {
    ServerNodeSupport.write(this, getHistoricalEventFilterNode(), value, false, false, true);
  }

  @Override
  public @Nullable PropertyTypeNode getIdentityTokenPolicyNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "IdentityTokenPolicy",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 304L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UserTokenPolicy getIdentityTokenPolicy() {
    return ServerNodeSupport.read(this, getIdentityTokenPolicyNode(), UserTokenPolicy.class, null);
  }

  @Override
  public void setIdentityTokenPolicy(@Nullable UserTokenPolicy value) {
    ServerNodeSupport.write(
        this,
        getIdentityTokenPolicyNode(),
        Namespaces.OPC_UA,
        "IdentityTokenPolicy",
        value,
        false,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityModeNode() {
    return ServerNodeSupport.optionalChild(
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
    ServerNodeSupport.write(
        this, getSecurityModeNode(), Namespaces.OPC_UA, "SecurityMode", value, false, true, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityPolicyUriNode() {
    return ServerNodeSupport.optionalChild(
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
    ServerNodeSupport.write(
        this,
        getSecurityPolicyUriNode(),
        Namespaces.OPC_UA,
        "SecurityPolicyUri",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getServerNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Server",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getServer() {
    return ServerNodeSupport.read(this, getServerNode(), String.class, null);
  }

  @Override
  public void setServer(@Nullable String value) {
    ServerNodeSupport.write(
        this, getServerNode(), Namespaces.OPC_UA, "Server", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getTransportProfileUriNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TransportProfileUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getTransportProfileUri() {
    return ServerNodeSupport.read(this, getTransportProfileUriNode(), String.class, null);
  }

  @Override
  public void setTransportProfileUri(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getTransportProfileUriNode(),
        Namespaces.OPC_UA,
        "TransportProfileUri",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEndpointUrlNode();
    getHistoricalEventFilterNode();
    getIdentityTokenPolicyNode();
    getSecurityModeNode();
    getSecurityPolicyUriNode();
    getServerNode();
    getTransportProfileUriNode();
  }
}
