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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AlternativeUnitType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4">Model
 *     documentation</a>
 */
public class AlternativeUnitTypeNode extends UnitTypeNode implements AlternativeUnitType {
  public AlternativeUnitTypeNode(
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

  public AlternativeUnitTypeNode(
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
  public @Nullable PropertyTypeNode getLinearConversionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LinearConversion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 32435L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LinearConversionDataType getLinearConversion() {
    return ServerNodeSupport.read(
        this, getLinearConversionNode(), LinearConversionDataType.class, null);
  }

  @Override
  public void setLinearConversion(@Nullable LinearConversionDataType value) {
    ServerNodeSupport.write(
        this,
        getLinearConversionNode(),
        Namespaces.OPC_UA,
        "LinearConversion",
        value,
        false,
        false,
        true);
  }

  @Override
  public @Nullable PropertyTypeNode getMathMLConversionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MathMLConversion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getMathMLConversion() {
    return ServerNodeSupport.read(this, getMathMLConversionNode(), String.class, null);
  }

  @Override
  public void setMathMLConversion(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getMathMLConversionNode(),
        Namespaces.OPC_UA,
        "MathMLConversion",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMathMLInverseConversionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MathMLInverseConversion",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getMathMLInverseConversion() {
    return ServerNodeSupport.read(this, getMathMLInverseConversionNode(), String.class, null);
  }

  @Override
  public void setMathMLInverseConversion(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getMathMLInverseConversionNode(),
        Namespaces.OPC_UA,
        "MathMLInverseConversion",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getLinearConversionNode();
    getMathMLConversionNode();
    getMathMLInverseConversionNode();
  }
}
