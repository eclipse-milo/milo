package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseTsnTrafficSpecificationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8">Model
 *     documentation</a>
 */
public class IIeeeBaseTsnTrafficSpecificationTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnTrafficSpecificationType {
  public IIeeeBaseTsnTrafficSpecificationTypeNode(
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

  public IIeeeBaseTsnTrafficSpecificationTypeNode(
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
  public BaseDataVariableTypeNode getIntervalNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Interval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24107L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UnsignedRationalNumber getInterval() {
    return ServerNodeSupport.read(this, getIntervalNode(), UnsignedRationalNumber.class, null);
  }

  @Override
  public void setInterval(@Nullable UnsignedRationalNumber value) {
    ServerNodeSupport.write(this, getIntervalNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getMaxFrameSizeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxFrameSize",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxFrameSize() {
    return ServerNodeSupport.read(this, getMaxFrameSizeNode(), UInteger.class, null);
  }

  @Override
  public void setMaxFrameSize(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxFrameSizeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaxIntervalFramesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxIntervalFrames",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxIntervalFrames() {
    return ServerNodeSupport.read(this, getMaxIntervalFramesNode(), UShort.class, null);
  }

  @Override
  public void setMaxIntervalFrames(@Nullable UShort value) {
    ServerNodeSupport.write(this, getMaxIntervalFramesNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getIntervalNode();
    getMaxFrameSizeNode();
    getMaxIntervalFramesNode();
  }
}
