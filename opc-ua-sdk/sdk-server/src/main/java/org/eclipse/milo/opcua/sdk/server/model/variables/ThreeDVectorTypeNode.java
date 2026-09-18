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
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ThreeDVectorType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.22">Model
 *     documentation</a>
 */
public class ThreeDVectorTypeNode extends VectorTypeNode implements ThreeDVectorType {
  public ThreeDVectorTypeNode(
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

  public ThreeDVectorTypeNode(
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
  public BaseDataVariableTypeNode getXNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "X",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getX() {
    return ServerNodeSupport.read(this, getXNode(), Double.class, null);
  }

  @Override
  public void setX(@Nullable Double value) {
    ServerNodeSupport.write(this, getXNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getYNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Y",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getY() {
    return ServerNodeSupport.read(this, getYNode(), Double.class, null);
  }

  @Override
  public void setY(@Nullable Double value) {
    ServerNodeSupport.write(this, getYNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getZNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Z",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getZ() {
    return ServerNodeSupport.read(this, getZNode(), Double.class, null);
  }

  @Override
  public void setZ(@Nullable Double value) {
    ServerNodeSupport.write(this, getZNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getXNode();
    getYNode();
    getZNode();
  }

  @Override
  public @Nullable ThreeDVector getThreeDVectorValue() {
    return ServerNodeSupport.read(this, this, ThreeDVector.class, null);
  }

  @Override
  public void setThreeDVectorValue(@Nullable ThreeDVector value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
