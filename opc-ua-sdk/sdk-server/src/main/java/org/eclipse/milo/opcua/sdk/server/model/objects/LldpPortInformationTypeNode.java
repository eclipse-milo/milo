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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LldpPortInformationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5">Model
 *     documentation</a>
 */
public class LldpPortInformationTypeNode extends BaseObjectTypeNode
    implements LldpPortInformationType {
  public LldpPortInformationTypeNode(
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

  public LldpPortInformationTypeNode(
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
  public PropertyTypeNode getDestMacAddressNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DestMacAddress",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public UByte @Nullable [] getDestMacAddress() {
    return ServerNodeSupport.readArray(this, getDestMacAddressNode(), UByte.class, null);
  }

  @Override
  public void setDestMacAddress(UByte @Nullable [] value) {
    ServerNodeSupport.write(this, getDestMacAddressNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getIetfBaseNetworkInterfaceNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "IetfBaseNetworkInterfaceName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getIetfBaseNetworkInterfaceName() {
    return ServerNodeSupport.read(this, getIetfBaseNetworkInterfaceNameNode(), String.class, null);
  }

  @Override
  public void setIetfBaseNetworkInterfaceName(@Nullable String value) {
    ServerNodeSupport.write(
        this, getIetfBaseNetworkInterfaceNameNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getManagementAddressTxPortNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ManagementAddressTxPort",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18953L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LldpManagementAddressTxPortType @Nullable [] getManagementAddressTxPort() {
    return ServerNodeSupport.readArray(
        this, getManagementAddressTxPortNode(), LldpManagementAddressTxPortType.class, null);
  }

  @Override
  public void setManagementAddressTxPort(
      @Nullable LldpManagementAddressTxPortType @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getManagementAddressTxPortNode(),
        Namespaces.OPC_UA,
        "ManagementAddressTxPort",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getPortDescriptionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PortDescription",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getPortDescription() {
    return ServerNodeSupport.read(this, getPortDescriptionNode(), String.class, null);
  }

  @Override
  public void setPortDescription(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getPortDescriptionNode(),
        Namespaces.OPC_UA,
        "PortDescription",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getPortIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PortId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getPortId() {
    return ServerNodeSupport.read(this, getPortIdNode(), String.class, null);
  }

  @Override
  public void setPortId(@Nullable String value) {
    ServerNodeSupport.write(this, getPortIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getPortIdSubtypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PortIdSubtype",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18949L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable PortIdSubtype getPortIdSubtype() {
    return ServerNodeSupport.read(
        this, getPortIdSubtypeNode(), PortIdSubtype.class, PortIdSubtype::from);
  }

  @Override
  public void setPortIdSubtype(@Nullable PortIdSubtype value) {
    ServerNodeSupport.write(this, getPortIdSubtypeNode(), value, false, true, false);
  }

  @Override
  public @Nullable FolderTypeNode getRemoteSystemsDataNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "RemoteSystemsData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 61L),
        null,
        -1,
        FolderTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDestMacAddressNode();
    getIetfBaseNetworkInterfaceNameNode();
    getManagementAddressTxPortNode();
    getPortDescriptionNode();
    getPortIdNode();
    getPortIdSubtypeNode();
    getRemoteSystemsDataNode();
  }
}
