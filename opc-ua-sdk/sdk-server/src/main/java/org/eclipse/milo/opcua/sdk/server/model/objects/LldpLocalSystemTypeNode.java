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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link LldpLocalSystemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public class LldpLocalSystemTypeNode extends BaseObjectTypeNode implements LldpLocalSystemType {
  public LldpLocalSystemTypeNode(
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

  public LldpLocalSystemTypeNode(
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
  public PropertyTypeNode getChassisIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ChassisId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
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
  public PropertyTypeNode getChassisIdSubtypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ChassisIdSubtype",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18947L),
        -1,
        PropertyTypeNode.class);
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
  public @Nullable PropertyTypeNode getSystemCapabilitiesEnabledNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SystemCapabilitiesEnabled",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18956L),
        -1,
        PropertyTypeNode.class);
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
  public @Nullable PropertyTypeNode getSystemCapabilitiesSupportedNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SystemCapabilitiesSupported",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18956L),
        -1,
        PropertyTypeNode.class);
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
  public PropertyTypeNode getSystemDescriptionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SystemDescription",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSystemDescription() {
    return ServerNodeSupport.read(this, getSystemDescriptionNode(), String.class, null);
  }

  @Override
  public void setSystemDescription(@Nullable String value) {
    ServerNodeSupport.write(this, getSystemDescriptionNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSystemNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SystemName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSystemName() {
    return ServerNodeSupport.read(this, getSystemNameNode(), String.class, null);
  }

  @Override
  public void setSystemName(@Nullable String value) {
    ServerNodeSupport.write(this, getSystemNameNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getChassisIdNode();
    getChassisIdSubtypeNode();
    getSystemCapabilitiesEnabledNode();
    getSystemCapabilitiesSupportedNode();
    getSystemDescriptionNode();
    getSystemNameNode();
  }
}
