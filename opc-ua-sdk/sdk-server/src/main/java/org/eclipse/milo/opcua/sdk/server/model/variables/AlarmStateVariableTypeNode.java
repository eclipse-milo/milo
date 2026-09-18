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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.AlarmMask;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AlarmStateVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">Model
 *     documentation</a>
 */
public class AlarmStateVariableTypeNode extends BaseDataVariableTypeNode
    implements AlarmStateVariableType {
  public AlarmStateVariableTypeNode(
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

  public AlarmStateVariableTypeNode(
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
  public PropertyTypeNode getActiveCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ActiveCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getActiveCount() {
    return ServerNodeSupport.read(this, getActiveCountNode(), UInteger.class, null);
  }

  @Override
  public void setActiveCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getActiveCountNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getFilterNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Filter",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 586L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ContentFilter getFilter() {
    return ServerNodeSupport.read(this, getFilterNode(), ContentFilter.class, null);
  }

  @Override
  public void setFilter(@Nullable ContentFilter value) {
    ServerNodeSupport.write(this, getFilterNode(), value, false, false, true);
  }

  @Override
  public PropertyTypeNode getHighestActiveSeverityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "HighestActiveSeverity",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getHighestActiveSeverity() {
    return ServerNodeSupport.read(this, getHighestActiveSeverityNode(), UShort.class, null);
  }

  @Override
  public void setHighestActiveSeverity(@Nullable UShort value) {
    ServerNodeSupport.write(this, getHighestActiveSeverityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getHighestUnackSeverityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "HighestUnackSeverity",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getHighestUnackSeverity() {
    return ServerNodeSupport.read(this, getHighestUnackSeverityNode(), UShort.class, null);
  }

  @Override
  public void setHighestUnackSeverity(@Nullable UShort value) {
    ServerNodeSupport.write(this, getHighestUnackSeverityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUnacknowledgedCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UnacknowledgedCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getUnacknowledgedCount() {
    return ServerNodeSupport.read(this, getUnacknowledgedCountNode(), UInteger.class, null);
  }

  @Override
  public void setUnacknowledgedCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getUnacknowledgedCountNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUnconfirmedCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UnconfirmedCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getUnconfirmedCount() {
    return ServerNodeSupport.read(this, getUnconfirmedCountNode(), UInteger.class, null);
  }

  @Override
  public void setUnconfirmedCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getUnconfirmedCountNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getActiveCountNode();
    getFilterNode();
    getHighestActiveSeverityNode();
    getHighestUnackSeverityNode();
    getUnacknowledgedCountNode();
    getUnconfirmedCountNode();
  }

  @Override
  public @Nullable AlarmMask getTypedValue() {
    return ServerNodeSupport.read(this, this, AlarmMask.class, null);
  }

  @Override
  public void setTypedValue(@Nullable AlarmMask value) {
    ServerNodeSupport.write(this, this, value, false, false, false);
  }
}
