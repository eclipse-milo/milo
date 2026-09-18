package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIeeeBaseEthernetPortType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2">Model
 *     documentation</a>
 */
public class IIeeeBaseEthernetPortTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseEthernetPortType {
  public IIeeeBaseEthernetPortTypeNode(
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

  public IIeeeBaseEthernetPortTypeNode(
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
  public BaseDataVariableTypeNode getDuplexNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Duplex",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24210L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Duplex getDuplex() {
    return ServerNodeSupport.read(this, getDuplexNode(), Duplex.class, Duplex::from);
  }

  @Override
  public void setDuplex(@Nullable Duplex value) {
    ServerNodeSupport.write(this, getDuplexNode(), value, false, true, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaxFrameLengthNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxFrameLength",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxFrameLength() {
    return ServerNodeSupport.read(this, getMaxFrameLengthNode(), UShort.class, null);
  }

  @Override
  public void setMaxFrameLength(@Nullable UShort value) {
    ServerNodeSupport.write(this, getMaxFrameLengthNode(), value, false, false, false);
  }

  @Override
  public AnalogUnitTypeNode getSpeedNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Speed",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17497L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 9L),
        -1,
        AnalogUnitTypeNode.class);
  }

  @Override
  public @Nullable ULong getSpeed() {
    return ServerNodeSupport.read(this, getSpeedNode(), ULong.class, null);
  }

  @Override
  public void setSpeed(@Nullable ULong value) {
    ServerNodeSupport.write(this, getSpeedNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDuplexNode();
    getMaxFrameLengthNode();
    getSpeedNode();
  }
}
