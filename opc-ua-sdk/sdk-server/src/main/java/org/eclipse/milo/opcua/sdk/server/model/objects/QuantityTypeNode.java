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
import org.eclipse.milo.opcua.stack.core.types.structured.AnnotationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link QuantityType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.1">Model
 *     documentation</a>
 */
public class QuantityTypeNode extends BaseObjectTypeNode implements QuantityType {
  public QuantityTypeNode(
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

  public QuantityTypeNode(
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
  public @Nullable PropertyTypeNode getAnnotationNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Annotation",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 32434L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable AnnotationDataType @Nullable [] getAnnotation() {
    return ServerNodeSupport.readArray(this, getAnnotationNode(), AnnotationDataType.class, null);
  }

  @Override
  public void setAnnotation(@Nullable AnnotationDataType @Nullable [] value) {
    ServerNodeSupport.write(
        this, getAnnotationNode(), Namespaces.OPC_UA, "Annotation", value, true, false, true);
  }

  @Override
  public @Nullable PropertyTypeNode getConversionServiceNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConversionService",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23751L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getConversionService() {
    return ServerNodeSupport.read(this, getConversionServiceNode(), String.class, null);
  }

  @Override
  public void setConversionService(@Nullable String value) {
    ServerNodeSupport.write(
        this,
        getConversionServiceNode(),
        Namespaces.OPC_UA,
        "ConversionService",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getDimensionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Dimension",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 32438L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable QuantityDimension getDimension() {
    return ServerNodeSupport.read(this, getDimensionNode(), QuantityDimension.class, null);
  }

  @Override
  public void setDimension(@Nullable QuantityDimension value) {
    ServerNodeSupport.write(this, getDimensionNode(), value, false, false, true);
  }

  @Override
  public BaseObjectTypeNode getServerUnitsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerUnits",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 58L),
        null,
        -1,
        BaseObjectTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getSymbolNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Symbol",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getSymbol() {
    return ServerNodeSupport.read(this, getSymbolNode(), LocalizedText.class, null);
  }

  @Override
  public void setSymbol(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this, getSymbolNode(), Namespaces.OPC_UA, "Symbol", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAnnotationNode();
    getConversionServiceNode();
    getDimensionNode();
    getServerUnitsNode();
    getSymbolNode();
  }
}
