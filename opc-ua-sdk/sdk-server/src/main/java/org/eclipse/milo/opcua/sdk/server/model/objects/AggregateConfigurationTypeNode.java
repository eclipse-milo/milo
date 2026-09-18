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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AggregateConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2">Model
 *     documentation</a>
 */
public class AggregateConfigurationTypeNode extends BaseObjectTypeNode
    implements AggregateConfigurationType {
  public AggregateConfigurationTypeNode(
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

  public AggregateConfigurationTypeNode(
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
  public PropertyTypeNode getPercentDataBadNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PercentDataBad",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UByte getPercentDataBad() {
    return ServerNodeSupport.read(this, getPercentDataBadNode(), UByte.class, null);
  }

  @Override
  public void setPercentDataBad(@Nullable UByte value) {
    ServerNodeSupport.write(this, getPercentDataBadNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getPercentDataGoodNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PercentDataGood",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UByte getPercentDataGood() {
    return ServerNodeSupport.read(this, getPercentDataGoodNode(), UByte.class, null);
  }

  @Override
  public void setPercentDataGood(@Nullable UByte value) {
    ServerNodeSupport.write(this, getPercentDataGoodNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getTreatUncertainAsBadNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TreatUncertainAsBad",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getTreatUncertainAsBad() {
    return ServerNodeSupport.read(this, getTreatUncertainAsBadNode(), Boolean.class, null);
  }

  @Override
  public void setTreatUncertainAsBad(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getTreatUncertainAsBadNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUseSlopedExtrapolationNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UseSlopedExtrapolation",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getUseSlopedExtrapolation() {
    return ServerNodeSupport.read(this, getUseSlopedExtrapolationNode(), Boolean.class, null);
  }

  @Override
  public void setUseSlopedExtrapolation(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getUseSlopedExtrapolationNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getPercentDataBadNode();
    getPercentDataGoodNode();
    getTreatUncertainAsBadNode();
    getUseSlopedExtrapolationNode();
  }
}
