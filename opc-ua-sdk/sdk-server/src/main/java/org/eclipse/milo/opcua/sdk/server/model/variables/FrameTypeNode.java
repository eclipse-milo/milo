package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Frame;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link FrameType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.27">Model
 *     documentation</a>
 */
public class FrameTypeNode extends BaseDataVariableTypeNode implements FrameType {
  public FrameTypeNode(
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

  public FrameTypeNode(
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
  public @Nullable BaseDataVariableTypeNode getBaseFrameNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "BaseFrame",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable NodeId getBaseFrame() {
    return ServerNodeSupport.read(this, getBaseFrameNode(), NodeId.class, null);
  }

  @Override
  public void setBaseFrame(@Nullable NodeId value) {
    ServerNodeSupport.write(
        this, getBaseFrameNode(), Namespaces.OPC_UA, "BaseFrame", value, false, false, false);
  }

  @Override
  public CartesianCoordinatesTypeNode getCartesianCoordinatesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CartesianCoordinates",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18772L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18809L),
        -1,
        CartesianCoordinatesTypeNode.class);
  }

  @Override
  public @Nullable CartesianCoordinates getCartesianCoordinates() {
    return ServerNodeSupport.read(
        this, getCartesianCoordinatesNode(), CartesianCoordinates.class, null);
  }

  @Override
  public void setCartesianCoordinates(@Nullable CartesianCoordinates value) {
    ServerNodeSupport.write(this, getCartesianCoordinatesNode(), value, false, false, true);
  }

  @Override
  public @Nullable PropertyTypeNode getConstantNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Constant",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getConstant() {
    return ServerNodeSupport.read(this, getConstantNode(), Boolean.class, null);
  }

  @Override
  public void setConstant(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this, getConstantNode(), Namespaces.OPC_UA, "Constant", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getFixedBaseNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "FixedBase",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getFixedBase() {
    return ServerNodeSupport.read(this, getFixedBaseNode(), Boolean.class, null);
  }

  @Override
  public void setFixedBase(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this, getFixedBaseNode(), Namespaces.OPC_UA, "FixedBase", value, false, false, false);
  }

  @Override
  public OrientationTypeNode getOrientationNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Orientation",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18779L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18811L),
        -1,
        OrientationTypeNode.class);
  }

  @Override
  public @Nullable Orientation getOrientation() {
    return ServerNodeSupport.read(this, getOrientationNode(), Orientation.class, null);
  }

  @Override
  public void setOrientation(@Nullable Orientation value) {
    ServerNodeSupport.write(this, getOrientationNode(), value, false, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getBaseFrameNode();
    getCartesianCoordinatesNode();
    getConstantNode();
    getFixedBaseNode();
    getOrientationNode();
  }

  @Override
  public @Nullable Frame getTypedValue() {
    return ServerNodeSupport.read(this, this, Frame.class, null);
  }

  @Override
  public void setTypedValue(@Nullable Frame value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
