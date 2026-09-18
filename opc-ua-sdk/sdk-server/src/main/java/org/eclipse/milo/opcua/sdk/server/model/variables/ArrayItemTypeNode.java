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
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ArrayItemType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.1">Model
 *     documentation</a>
 */
public class ArrayItemTypeNode extends DataItemTypeNode implements ArrayItemType {
  public ArrayItemTypeNode(
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

  public ArrayItemTypeNode(
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
  public PropertyTypeNode getAxisScaleTypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AxisScaleType",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12077L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable AxisScaleEnumeration getAxisScaleType() {
    return ServerNodeSupport.read(
        this, getAxisScaleTypeNode(), AxisScaleEnumeration.class, AxisScaleEnumeration::from);
  }

  @Override
  public void setAxisScaleType(@Nullable AxisScaleEnumeration value) {
    ServerNodeSupport.write(this, getAxisScaleTypeNode(), value, false, true, false);
  }

  @Override
  public PropertyTypeNode getEURangeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EURange",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 884L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Range getEURange() {
    return ServerNodeSupport.read(this, getEURangeNode(), Range.class, null);
  }

  @Override
  public void setEURange(@Nullable Range value) {
    ServerNodeSupport.write(this, getEURangeNode(), value, false, false, true);
  }

  @Override
  public PropertyTypeNode getEngineeringUnits_Node() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EngineeringUnits",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 887L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable EUInformation getEngineeringUnits_() {
    return ServerNodeSupport.read(this, getEngineeringUnits_Node(), EUInformation.class, null);
  }

  @Override
  public void setEngineeringUnits_(@Nullable EUInformation value) {
    ServerNodeSupport.write(this, getEngineeringUnits_Node(), value, false, false, true);
  }

  @Override
  public @Nullable PropertyTypeNode getInstrumentRangeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "InstrumentRange",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 884L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Range getInstrumentRange() {
    return ServerNodeSupport.read(this, getInstrumentRangeNode(), Range.class, null);
  }

  @Override
  public void setInstrumentRange(@Nullable Range value) {
    ServerNodeSupport.write(
        this,
        getInstrumentRangeNode(),
        Namespaces.OPC_UA,
        "InstrumentRange",
        value,
        false,
        false,
        true);
  }

  @Override
  public PropertyTypeNode getTitleNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Title",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getTitle() {
    return ServerNodeSupport.read(this, getTitleNode(), LocalizedText.class, null);
  }

  @Override
  public void setTitle(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getTitleNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAxisScaleTypeNode();
    getEURangeNode();
    getEngineeringUnits_Node();
    getInstrumentRangeNode();
    getTitleNode();
  }

  @Override
  public @Nullable Variant getTypedValue() {
    return ServerNodeSupport.readVariant(this, this, 0, Variant.class, null);
  }

  @Override
  public void setTypedValue(@Nullable Variant value) {
    ServerNodeSupport.writeVariant(this, this, null, "Value", value, 0, Variant.class, null);
  }
}
