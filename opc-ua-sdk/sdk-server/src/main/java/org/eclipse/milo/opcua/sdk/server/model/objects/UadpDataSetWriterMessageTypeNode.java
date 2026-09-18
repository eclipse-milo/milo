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
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link UadpDataSetWriterMessageType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.2">Model
 *     documentation</a>
 */
public class UadpDataSetWriterMessageTypeNode extends DataSetWriterMessageTypeNode
    implements UadpDataSetWriterMessageType {
  public UadpDataSetWriterMessageTypeNode(
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

  public UadpDataSetWriterMessageTypeNode(
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
  public PropertyTypeNode getConfiguredSizeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ConfiguredSize",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getConfiguredSize() {
    return ServerNodeSupport.read(this, getConfiguredSizeNode(), UShort.class, null);
  }

  @Override
  public void setConfiguredSize(@Nullable UShort value) {
    ServerNodeSupport.write(this, getConfiguredSizeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDataSetMessageContentMaskNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetMessageContentMask",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15646L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UadpDataSetMessageContentMask getDataSetMessageContentMask() {
    return ServerNodeSupport.read(
        this, getDataSetMessageContentMaskNode(), UadpDataSetMessageContentMask.class, null);
  }

  @Override
  public void setDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value) {
    ServerNodeSupport.write(this, getDataSetMessageContentMaskNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDataSetOffsetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetOffset",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getDataSetOffset() {
    return ServerNodeSupport.read(this, getDataSetOffsetNode(), UShort.class, null);
  }

  @Override
  public void setDataSetOffset(@Nullable UShort value) {
    ServerNodeSupport.write(this, getDataSetOffsetNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getNetworkMessageNumberNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NetworkMessageNumber",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getNetworkMessageNumber() {
    return ServerNodeSupport.read(this, getNetworkMessageNumberNode(), UShort.class, null);
  }

  @Override
  public void setNetworkMessageNumber(@Nullable UShort value) {
    ServerNodeSupport.write(this, getNetworkMessageNumberNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getConfiguredSizeNode();
    getDataSetMessageContentMaskNode();
    getDataSetOffsetNode();
    getNetworkMessageNumberNode();
  }
}
