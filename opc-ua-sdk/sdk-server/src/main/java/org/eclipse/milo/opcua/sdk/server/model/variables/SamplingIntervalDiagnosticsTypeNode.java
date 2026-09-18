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
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SamplingIntervalDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10">Model
 *     documentation</a>
 */
public class SamplingIntervalDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SamplingIntervalDiagnosticsType {
  public SamplingIntervalDiagnosticsTypeNode(
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

  public SamplingIntervalDiagnosticsTypeNode(
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
  public BaseDataVariableTypeNode getDisabledMonitoredItemsSamplingCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DisabledMonitoredItemsSamplingCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getDisabledMonitoredItemsSamplingCount() {
    return ServerNodeSupport.read(
        this, getDisabledMonitoredItemsSamplingCountNode(), UInteger.class, null);
  }

  @Override
  public void setDisabledMonitoredItemsSamplingCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getDisabledMonitoredItemsSamplingCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaxSampledMonitoredItemsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxSampledMonitoredItemsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxSampledMonitoredItemsCount() {
    return ServerNodeSupport.read(
        this, getMaxSampledMonitoredItemsCountNode(), UInteger.class, null);
  }

  @Override
  public void setMaxSampledMonitoredItemsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getMaxSampledMonitoredItemsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSampledMonitoredItemsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SampledMonitoredItemsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getSampledMonitoredItemsCount() {
    return ServerNodeSupport.read(this, getSampledMonitoredItemsCountNode(), UInteger.class, null);
  }

  @Override
  public void setSampledMonitoredItemsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getSampledMonitoredItemsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSamplingIntervalNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SamplingInterval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getSamplingInterval() {
    return ServerNodeSupport.read(this, getSamplingIntervalNode(), Double.class, null);
  }

  @Override
  public void setSamplingInterval(@Nullable Double value) {
    ServerNodeSupport.write(this, getSamplingIntervalNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDisabledMonitoredItemsSamplingCountNode();
    getMaxSampledMonitoredItemsCountNode();
    getSampledMonitoredItemsCountNode();
    getSamplingIntervalNode();
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsDataType getTypedValue() {
    return ServerNodeSupport.read(this, this, SamplingIntervalDiagnosticsDataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable SamplingIntervalDiagnosticsDataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
