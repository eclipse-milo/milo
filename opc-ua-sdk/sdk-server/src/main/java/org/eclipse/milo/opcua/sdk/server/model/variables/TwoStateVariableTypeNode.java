package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link TwoStateVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">Model
 *     documentation</a>
 */
public class TwoStateVariableTypeNode extends StateVariableTypeNode
    implements TwoStateVariableType {
  public TwoStateVariableTypeNode(
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

  public TwoStateVariableTypeNode(
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
  public @Nullable PropertyTypeNode getFalseStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "FalseState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getFalseState() {
    return ServerNodeSupport.read(this, getFalseStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setFalseState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getFalseStateNode(), Namespaces.OPC_UA, "FalseState", value, false, false, false);
  }

  @Override
  public PropertyTypeNode getIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Id",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getTwoStateVariableTypeId() {
    return ServerNodeSupport.read(this, getIdNode(), Boolean.class, null);
  }

  @Override
  public void setTwoStateVariableTypeId(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getIdNode(), value, false, false, false);
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
  public @Nullable PropertyTypeNode getTrueStateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TrueState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getTrueState() {
    return ServerNodeSupport.read(this, getTrueStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setTrueState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getTrueStateNode(), Namespaces.OPC_UA, "TrueState", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEffectiveTransitionTimeNode();
    getFalseStateNode();
    getTransitionTimeNode();
    getTrueStateNode();
  }
}
