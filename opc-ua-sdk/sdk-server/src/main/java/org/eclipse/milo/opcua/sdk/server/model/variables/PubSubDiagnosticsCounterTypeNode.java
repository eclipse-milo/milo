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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubDiagnosticsCounterType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">Model
 *     documentation</a>
 */
public class PubSubDiagnosticsCounterTypeNode extends BaseDataVariableTypeNode
    implements PubSubDiagnosticsCounterType {
  public PubSubDiagnosticsCounterTypeNode(
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

  public PubSubDiagnosticsCounterTypeNode(
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
  public PropertyTypeNode getActiveNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Active",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getActive() {
    return ServerNodeSupport.read(this, getActiveNode(), Boolean.class, null);
  }

  @Override
  public void setActive(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getActiveNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getClassificationNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Classification",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19730L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable PubSubDiagnosticsCounterClassification getClassification() {
    return ServerNodeSupport.read(
        this,
        getClassificationNode(),
        PubSubDiagnosticsCounterClassification.class,
        PubSubDiagnosticsCounterClassification::from);
  }

  @Override
  public void setClassification(@Nullable PubSubDiagnosticsCounterClassification value) {
    ServerNodeSupport.write(this, getClassificationNode(), value, false, true, false);
  }

  @Override
  public PropertyTypeNode getDiagnosticsLevelNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DiagnosticsLevel",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19723L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DiagnosticsLevel getDiagnosticsLevel() {
    return ServerNodeSupport.read(
        this, getDiagnosticsLevelNode(), DiagnosticsLevel.class, DiagnosticsLevel::from);
  }

  @Override
  public void setDiagnosticsLevel(@Nullable DiagnosticsLevel value) {
    ServerNodeSupport.write(this, getDiagnosticsLevelNode(), value, false, true, false);
  }

  @Override
  public @Nullable PropertyTypeNode getTimeFirstChangeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TimeFirstChange",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getTimeFirstChange() {
    return ServerNodeSupport.read(this, getTimeFirstChangeNode(), DateTime.class, null);
  }

  @Override
  public void setTimeFirstChange(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getTimeFirstChangeNode(),
        Namespaces.OPC_UA,
        "TimeFirstChange",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getActiveNode();
    getClassificationNode();
    getDiagnosticsLevelNode();
    getTimeFirstChangeNode();
  }

  @Override
  public @Nullable UInteger getTypedValue() {
    return ServerNodeSupport.read(this, this, UInteger.class, null);
  }

  @Override
  public void setTypedValue(@Nullable UInteger value) {
    ServerNodeSupport.write(this, this, value, false, false, false);
  }
}
