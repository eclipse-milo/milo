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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDFrame;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ThreeDFrameType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.28">Model
 *     documentation</a>
 */
public class ThreeDFrameTypeNode extends FrameTypeNode implements ThreeDFrameType {
  public ThreeDFrameTypeNode(
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

  public ThreeDFrameTypeNode(
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
  public ThreeDCartesianCoordinatesTypeNode getCartesianCoordinatesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CartesianCoordinates",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18774L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18810L),
        -1,
        ThreeDCartesianCoordinatesTypeNode.class);
  }

  @Override
  public @Nullable ThreeDCartesianCoordinates getThreeDFrameTypeCartesianCoordinates() {
    return ServerNodeSupport.read(
        this, getCartesianCoordinatesNode(), ThreeDCartesianCoordinates.class, null);
  }

  @Override
  public void setThreeDFrameTypeCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value) {
    ServerNodeSupport.write(this, getCartesianCoordinatesNode(), value, false, false, true);
  }

  @Override
  public ThreeDOrientationTypeNode getOrientationNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Orientation",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18781L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 18812L),
        -1,
        ThreeDOrientationTypeNode.class);
  }

  @Override
  public @Nullable ThreeDOrientation getThreeDFrameTypeOrientation() {
    return ServerNodeSupport.read(this, getOrientationNode(), ThreeDOrientation.class, null);
  }

  @Override
  public void setThreeDFrameTypeOrientation(@Nullable ThreeDOrientation value) {
    ServerNodeSupport.write(this, getOrientationNode(), value, false, false, true);
  }

  @Override
  public @Nullable ThreeDFrame getThreeDFrameValue() {
    return ServerNodeSupport.read(this, this, ThreeDFrame.class, null);
  }

  @Override
  public void setThreeDFrameValue(@Nullable ThreeDFrame value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
