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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link UadpWriterGroupMessageType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1">Model
 *     documentation</a>
 */
public class UadpWriterGroupMessageTypeNode extends WriterGroupMessageTypeNode
    implements UadpWriterGroupMessageType {
  public UadpWriterGroupMessageTypeNode(
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

  public UadpWriterGroupMessageTypeNode(
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
  public PropertyTypeNode getDataSetOrderingNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetOrdering",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20408L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DataSetOrderingType getDataSetOrdering() {
    return ServerNodeSupport.read(
        this, getDataSetOrderingNode(), DataSetOrderingType.class, DataSetOrderingType::from);
  }

  @Override
  public void setDataSetOrdering(@Nullable DataSetOrderingType value) {
    ServerNodeSupport.write(this, getDataSetOrderingNode(), value, false, true, false);
  }

  @Override
  public PropertyTypeNode getGroupVersionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "GroupVersion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getGroupVersion() {
    return ServerNodeSupport.read(this, getGroupVersionNode(), UInteger.class, null);
  }

  @Override
  public void setGroupVersion(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getGroupVersionNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getNetworkMessageContentMaskNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NetworkMessageContentMask",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15642L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UadpNetworkMessageContentMask getNetworkMessageContentMask() {
    return ServerNodeSupport.read(
        this, getNetworkMessageContentMaskNode(), UadpNetworkMessageContentMask.class, null);
  }

  @Override
  public void setNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value) {
    ServerNodeSupport.write(this, getNetworkMessageContentMaskNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getPublishingOffsetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishingOffset",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public Double @Nullable [] getPublishingOffset() {
    return ServerNodeSupport.readArray(this, getPublishingOffsetNode(), Double.class, null);
  }

  @Override
  public void setPublishingOffset(Double @Nullable [] value) {
    ServerNodeSupport.write(this, getPublishingOffsetNode(), value, true, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSamplingOffsetNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SamplingOffset",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getSamplingOffset() {
    return ServerNodeSupport.read(this, getSamplingOffsetNode(), Double.class, null);
  }

  @Override
  public void setSamplingOffset(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getSamplingOffsetNode(),
        Namespaces.OPC_UA,
        "SamplingOffset",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDataSetOrderingNode();
    getGroupVersionNode();
    getNetworkMessageContentMaskNode();
    getPublishingOffsetNode();
    getSamplingOffsetNode();
  }
}
