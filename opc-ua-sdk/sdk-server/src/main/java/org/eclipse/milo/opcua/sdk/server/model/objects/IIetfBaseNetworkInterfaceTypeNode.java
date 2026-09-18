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
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link IIetfBaseNetworkInterfaceType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1">Model
 *     documentation</a>
 */
public class IIetfBaseNetworkInterfaceTypeNode extends BaseInterfaceTypeNode
    implements IIetfBaseNetworkInterfaceType {
  public IIetfBaseNetworkInterfaceTypeNode(
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

  public IIetfBaseNetworkInterfaceTypeNode(
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
  public BaseDataVariableTypeNode getAdminStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AdminStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24212L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable InterfaceAdminStatus getAdminStatus() {
    return ServerNodeSupport.read(
        this, getAdminStatusNode(), InterfaceAdminStatus.class, InterfaceAdminStatus::from);
  }

  @Override
  public void setAdminStatus(@Nullable InterfaceAdminStatus value) {
    ServerNodeSupport.write(this, getAdminStatusNode(), value, false, true, false);
  }

  @Override
  public BaseDataVariableTypeNode getOperStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "OperStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24214L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable InterfaceOperStatus getOperStatus() {
    return ServerNodeSupport.read(
        this, getOperStatusNode(), InterfaceOperStatus.class, InterfaceOperStatus::from);
  }

  @Override
  public void setOperStatus(@Nullable InterfaceOperStatus value) {
    ServerNodeSupport.write(this, getOperStatusNode(), value, false, true, false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getPhysAddressNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PhysAddress",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getPhysAddress() {
    return ServerNodeSupport.read(this, getPhysAddressNode(), String.class, null);
  }

  @Override
  public void setPhysAddress(@Nullable String value) {
    ServerNodeSupport.write(
        this, getPhysAddressNode(), Namespaces.OPC_UA, "PhysAddress", value, false, false, false);
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
    getAdminStatusNode();
    getOperStatusNode();
    getPhysAddressNode();
    getSpeedNode();
  }
}
