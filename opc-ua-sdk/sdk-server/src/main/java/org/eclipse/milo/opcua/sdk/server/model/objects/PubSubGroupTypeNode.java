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
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubGroupType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.2">Model
 *     documentation</a>
 */
public class PubSubGroupTypeNode extends BaseObjectTypeNode implements PubSubGroupType {
  public PubSubGroupTypeNode(
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

  public PubSubGroupTypeNode(
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
  public PropertyTypeNode getGroupPropertiesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "GroupProperties",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getGroupProperties() {
    return ServerNodeSupport.readArray(this, getGroupPropertiesNode(), KeyValuePair.class, null);
  }

  @Override
  public void setGroupProperties(@Nullable KeyValuePair @Nullable [] value) {
    ServerNodeSupport.write(this, getGroupPropertiesNode(), value, true, false, true);
  }

  @Override
  public PropertyTypeNode getMaxNetworkMessageSizeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxNetworkMessageSize",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNetworkMessageSize() {
    return ServerNodeSupport.read(this, getMaxNetworkMessageSizeNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNetworkMessageSize(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxNetworkMessageSizeNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityGroupIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SecurityGroupId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSecurityGroupId() {
    return ServerNodeSupport.read(this, getSecurityGroupIdNode(), String.class, null);
  }

  @Override
  public void setSecurityGroupId(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getSecurityGroupIdNode(),
        Namespaces.OPC_UA,
        "SecurityGroupId",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityKeyServicesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SecurityKeyServices",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 312L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable EndpointDescription @Nullable [] getSecurityKeyServices() {
    return ServerNodeSupport.readArray(
        this, getSecurityKeyServicesNode(), EndpointDescription.class, null);
  }

  @Override
  public void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getSecurityKeyServicesNode(),
        Namespaces.OPC_UA,
        "SecurityKeyServices",
        value,
        true,
        false,
        true);
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
  public PubSubStatusTypeNode getStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Status",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14643L),
        null,
        -1,
        PubSubStatusTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getGroupPropertiesNode();
    getMaxNetworkMessageSizeNode();
    getSecurityGroupIdNode();
    getSecurityKeyServicesNode();
    getSecurityModeNode();
    getStatusNode();
  }
}
