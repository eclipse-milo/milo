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
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerRedundancyType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">Model
 *     documentation</a>
 */
public class ServerRedundancyTypeNode extends BaseObjectTypeNode implements ServerRedundancyType {
  public ServerRedundancyTypeNode(
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

  public ServerRedundancyTypeNode(
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
  public PropertyTypeNode getRedundancySupportNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RedundancySupport",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 851L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable RedundancySupport getRedundancySupport() {
    return ServerNodeSupport.read(
        this, getRedundancySupportNode(), RedundancySupport.class, RedundancySupport::from);
  }

  @Override
  public void setRedundancySupport(@Nullable RedundancySupport value) {
    ServerNodeSupport.write(this, getRedundancySupportNode(), value, false, true, false);
  }

  @Override
  public @Nullable PropertyTypeNode getRedundantServerArrayNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "RedundantServerArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 853L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable RedundantServerDataType @Nullable [] getRedundantServerArray() {
    return ServerNodeSupport.readArray(
        this, getRedundantServerArrayNode(), RedundantServerDataType.class, null);
  }

  @Override
  public void setRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getRedundantServerArrayNode(),
        Namespaces.OPC_UA,
        "RedundantServerArray",
        value,
        true,
        false,
        true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getRedundancySupportNode();
    getRedundantServerArrayNode();
  }
}
