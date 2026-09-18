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
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ThreeDOrientationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.26">Model
 *     documentation</a>
 */
public class ThreeDOrientationTypeNode extends OrientationTypeNode
    implements ThreeDOrientationType {
  public ThreeDOrientationTypeNode(
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

  public ThreeDOrientationTypeNode(
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
  public BaseDataVariableTypeNode getANode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "A",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getA() {
    return ServerNodeSupport.read(this, getANode(), Double.class, null);
  }

  @Override
  public void setA(@Nullable Double value) {
    ServerNodeSupport.write(this, getANode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getBNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "B",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getB() {
    return ServerNodeSupport.read(this, getBNode(), Double.class, null);
  }

  @Override
  public void setB(@Nullable Double value) {
    ServerNodeSupport.write(this, getBNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "C",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getC() {
    return ServerNodeSupport.read(this, getCNode(), Double.class, null);
  }

  @Override
  public void setC(@Nullable Double value) {
    ServerNodeSupport.write(this, getCNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getANode();
    getBNode();
    getCNode();
  }

  @Override
  public @Nullable ThreeDOrientation getThreeDOrientationValue() {
    return ServerNodeSupport.read(this, this, ThreeDOrientation.class, null);
  }

  @Override
  public void setThreeDOrientationValue(@Nullable ThreeDOrientation value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
