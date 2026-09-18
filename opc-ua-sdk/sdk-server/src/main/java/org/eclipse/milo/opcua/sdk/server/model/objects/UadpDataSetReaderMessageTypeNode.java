package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link UadpDataSetReaderMessageType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3">Model
 *     documentation</a>
 */
public class UadpDataSetReaderMessageTypeNode extends DataSetReaderMessageTypeNode
    implements UadpDataSetReaderMessageType {
  public UadpDataSetReaderMessageTypeNode(
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

  public UadpDataSetReaderMessageTypeNode(
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
  public PropertyTypeNode getDataSetClassIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetClassId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UUID getDataSetClassId() {
    return ServerNodeSupport.read(this, getDataSetClassIdNode(), UUID.class, null);
  }

  @Override
  public void setDataSetClassId(@Nullable UUID value) {
    ServerNodeSupport.write(this, getDataSetClassIdNode(), value, false, false, false);
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
  public PropertyTypeNode getProcessingOffsetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ProcessingOffset",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getProcessingOffset() {
    return ServerNodeSupport.read(this, getProcessingOffsetNode(), Double.class, null);
  }

  @Override
  public void setProcessingOffset(@Nullable Double value) {
    ServerNodeSupport.write(this, getProcessingOffsetNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishingInterval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getPublishingInterval() {
    return ServerNodeSupport.read(this, getPublishingIntervalNode(), Double.class, null);
  }

  @Override
  public void setPublishingInterval(@Nullable Double value) {
    ServerNodeSupport.write(this, getPublishingIntervalNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getReceiveOffsetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ReceiveOffset",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getReceiveOffset() {
    return ServerNodeSupport.read(this, getReceiveOffsetNode(), Double.class, null);
  }

  @Override
  public void setReceiveOffset(@Nullable Double value) {
    ServerNodeSupport.write(this, getReceiveOffsetNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDataSetClassIdNode();
    getDataSetMessageContentMaskNode();
    getDataSetOffsetNode();
    getGroupVersionNode();
    getNetworkMessageContentMaskNode();
    getNetworkMessageNumberNode();
    getProcessingOffsetNode();
    getPublishingIntervalNode();
    getReceiveOffsetNode();
  }
}
