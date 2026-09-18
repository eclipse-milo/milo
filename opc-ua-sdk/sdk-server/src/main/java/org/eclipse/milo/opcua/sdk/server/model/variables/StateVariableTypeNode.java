package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link StateVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">Model
 *     documentation</a>
 */
public class StateVariableTypeNode extends BaseDataVariableTypeNode implements StateVariableType {
  public StateVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  public StateVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public @Nullable PropertyTypeNode getEffectiveDisplayNameNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "EffectiveDisplayName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getEffectiveDisplayName() {
    return ServerNodeSupport.read(this, getEffectiveDisplayNameNode(), LocalizedText.class, null);
  }

  @Override
  public void setEffectiveDisplayName(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this,
        getEffectiveDisplayNameNode(),
        Namespaces.OPC_UA,
        "EffectiveDisplayName",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Id",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant getId() {
    return ServerNodeSupport.read(this, getIdNode(), Variant.class, null);
  }

  @Override
  public void setId(@Nullable Variant value) {
    ServerNodeSupport.write(this, getIdNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getNameNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Name",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable QualifiedName getName() {
    return ServerNodeSupport.read(this, getNameNode(), QualifiedName.class, null);
  }

  @Override
  public void setName(@Nullable QualifiedName value) {
    ServerNodeSupport.write(
        this, getNameNode(), Namespaces.OPC_UA, "Name", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getNumberNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Number",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getNumber() {
    return ServerNodeSupport.read(this, getNumberNode(), UInteger.class, null);
  }

  @Override
  public void setNumber(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getNumberNode(), Namespaces.OPC_UA, "Number", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEffectiveDisplayNameNode();
    getIdNode();
    getNameNode();
    getNumberNode();
  }

  @Override
  public @Nullable LocalizedText getTypedValue() {
    return ServerNodeSupport.read(this, this, LocalizedText.class, null);
  }

  @Override
  public void setTypedValue(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, this, value, false, false, false);
  }
}
