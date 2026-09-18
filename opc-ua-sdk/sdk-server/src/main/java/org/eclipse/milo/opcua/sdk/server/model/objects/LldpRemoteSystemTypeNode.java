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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LldpRemoteSystemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">Model
 *     documentation</a>
 */
public class LldpRemoteSystemTypeNode extends BaseObjectTypeNode implements LldpRemoteSystemType {
  public LldpRemoteSystemTypeNode(
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

  public LldpRemoteSystemTypeNode(
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
  public BaseDataVariableTypeNode getChassisIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ChassisId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getChassisId() {
    return ServerNodeSupport.read(this, getChassisIdNode(), String.class, null);
  }

  @Override
  public void setChassisId(@Nullable String value) {
    ServerNodeSupport.write(this, getChassisIdNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getChassisIdSubtypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ChassisIdSubtype",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18947L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ChassisIdSubtype getChassisIdSubtype() {
    return ServerNodeSupport.read(
        this, getChassisIdSubtypeNode(), ChassisIdSubtype.class, ChassisIdSubtype::from);
  }

  @Override
  public void setChassisIdSubtype(@Nullable ChassisIdSubtype value) {
    ServerNodeSupport.write(this, getChassisIdSubtypeNode(), value, false, true, false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getManagementAddressNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ManagementAddress",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18954L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable LldpManagementAddressType @Nullable [] getManagementAddress() {
    return ServerNodeSupport.readArray(
        this, getManagementAddressNode(), LldpManagementAddressType.class, null);
  }

  @Override
  public void setManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getManagementAddressNode(),
        Namespaces.OPC_UA,
        "ManagementAddress",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getPortDescriptionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "PortDescription",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
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
  public BaseDataVariableTypeNode getPortIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PortId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
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
  public BaseDataVariableTypeNode getPortIdSubtypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PortIdSubtype",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18949L),
        -1,
        BaseDataVariableTypeNode.class);
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
  public @Nullable BaseDataVariableTypeNode getRemoteChangesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "RemoteChanges",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Boolean getRemoteChanges() {
    return ServerNodeSupport.read(this, getRemoteChangesNode(), Boolean.class, null);
  }

  @Override
  public void setRemoteChanges(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getRemoteChangesNode(),
        Namespaces.OPC_UA,
        "RemoteChanges",
        value,
        false,
        false,
        false);
  }

  @Override
  public BaseDataVariableTypeNode getRemoteIndexNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RemoteIndex",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRemoteIndex() {
    return ServerNodeSupport.read(this, getRemoteIndexNode(), UInteger.class, null);
  }

  @Override
  public void setRemoteIndex(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRemoteIndexNode(), value, false, false, false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getRemoteTooManyNeighborsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "RemoteTooManyNeighbors",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Boolean getRemoteTooManyNeighbors() {
    return ServerNodeSupport.read(this, getRemoteTooManyNeighborsNode(), Boolean.class, null);
  }

  @Override
  public void setRemoteTooManyNeighbors(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getRemoteTooManyNeighborsNode(),
        Namespaces.OPC_UA,
        "RemoteTooManyNeighbors",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getRemoteUnknownTlvNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "RemoteUnknownTlv",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18955L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable LldpTlvType @Nullable [] getRemoteUnknownTlv() {
    return ServerNodeSupport.readArray(this, getRemoteUnknownTlvNode(), LldpTlvType.class, null);
  }

  @Override
  public void setRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getRemoteUnknownTlvNode(),
        Namespaces.OPC_UA,
        "RemoteUnknownTlv",
        value,
        true,
        false,
        true);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemCapabilitiesEnabledNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SystemCapabilitiesEnabled",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18956L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled() {
    return ServerNodeSupport.read(
        this, getSystemCapabilitiesEnabledNode(), LldpSystemCapabilitiesMap.class, null);
  }

  @Override
  public void setSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value) {
    ServerNodeSupport.write(
        this,
        getSystemCapabilitiesEnabledNode(),
        Namespaces.OPC_UA,
        "SystemCapabilitiesEnabled",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemCapabilitiesSupportedNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SystemCapabilitiesSupported",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18956L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesSupported() {
    return ServerNodeSupport.read(
        this, getSystemCapabilitiesSupportedNode(), LldpSystemCapabilitiesMap.class, null);
  }

  @Override
  public void setSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value) {
    ServerNodeSupport.write(
        this,
        getSystemCapabilitiesSupportedNode(),
        Namespaces.OPC_UA,
        "SystemCapabilitiesSupported",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemDescriptionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SystemDescription",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getSystemDescription() {
    return ServerNodeSupport.read(this, getSystemDescriptionNode(), String.class, null);
  }

  @Override
  public void setSystemDescription(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getSystemDescriptionNode(),
        Namespaces.OPC_UA,
        "SystemDescription",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSystemNameNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SystemName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getSystemName() {
    return ServerNodeSupport.read(this, getSystemNameNode(), String.class, null);
  }

  @Override
  public void setSystemName(@Nullable String value) {
    ServerNodeSupport.write(
        this, getSystemNameNode(), Namespaces.OPC_UA, "SystemName", value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getTimeMarkNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TimeMark",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getTimeMark() {
    return ServerNodeSupport.read(this, getTimeMarkNode(), UInteger.class, null);
  }

  @Override
  public void setTimeMark(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getTimeMarkNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getChassisIdNode();
    getChassisIdSubtypeNode();
    getManagementAddressNode();
    getPortDescriptionNode();
    getPortIdNode();
    getPortIdSubtypeNode();
    getRemoteChangesNode();
    getRemoteIndexNode();
    getRemoteTooManyNeighborsNode();
    getRemoteUnknownTlvNode();
    getSystemCapabilitiesEnabledNode();
    getSystemCapabilitiesSupportedNode();
    getSystemDescriptionNode();
    getSystemNameNode();
    getTimeMarkNode();
  }
}
