package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
 * Node implementation of {@link TransitionVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4">Model
 *     documentation</a>
 */
public class TransitionVariableTypeNode extends BaseDataVariableTypeNode
    implements TransitionVariableType {
  public TransitionVariableTypeNode(
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

  public TransitionVariableTypeNode(
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
  public @Nullable PropertyTypeNode getEffectiveTransitionTimeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "EffectiveTransitionTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getEffectiveTransitionTime() {
    return ServerNodeSupport.read(this, getEffectiveTransitionTimeNode(), DateTime.class, null);
  }

  @Override
  public void setEffectiveTransitionTime(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getEffectiveTransitionTimeNode(),
        Namespaces.OPC_UA,
        "EffectiveTransitionTime",
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
  public @Nullable PropertyTypeNode getTransitionTimeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TransitionTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getTransitionTime() {
    return ServerNodeSupport.read(this, getTransitionTimeNode(), DateTime.class, null);
  }

  @Override
  public void setTransitionTime(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getTransitionTimeNode(),
        Namespaces.OPC_UA,
        "TransitionTime",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEffectiveTransitionTimeNode();
    getIdNode();
    getNameNode();
    getNumberNode();
    getTransitionTimeNode();
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
