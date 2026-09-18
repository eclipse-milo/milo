package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoricalDataConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2">Model
 *     documentation</a>
 */
public class HistoricalDataConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalDataConfigurationType {
  public HistoricalDataConfigurationTypeNode(
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

  public HistoricalDataConfigurationTypeNode(
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
  public AggregateConfigurationTypeNode getAggregateConfigurationNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AggregateConfiguration",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11187L),
        null,
        -1,
        AggregateConfigurationTypeNode.class);
  }

  @Override
  public @Nullable FolderTypeNode getAggregateFunctionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "AggregateFunctions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 61L),
        null,
        -1,
        FolderTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getDefinitionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Definition",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getDefinition() {
    return ServerNodeSupport.read(this, getDefinitionNode(), String.class, null);
  }

  @Override
  public void setDefinition(@Nullable String value) {
    ServerNodeSupport.write(
        this, getDefinitionNode(), Namespaces.OPC_UA, "Definition", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getExceptionDeviationNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ExceptionDeviation",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getExceptionDeviation() {
    return ServerNodeSupport.read(this, getExceptionDeviationNode(), Double.class, null);
  }

  @Override
  public void setExceptionDeviation(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getExceptionDeviationNode(),
        Namespaces.OPC_UA,
        "ExceptionDeviation",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getExceptionDeviationFormatNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ExceptionDeviationFormat",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 890L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ExceptionDeviationFormat getExceptionDeviationFormat() {
    return ServerNodeSupport.read(
        this,
        getExceptionDeviationFormatNode(),
        ExceptionDeviationFormat.class,
        ExceptionDeviationFormat::from);
  }

  @Override
  public void setExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value) {
    ServerNodeSupport.write(
        this,
        getExceptionDeviationFormatNode(),
        Namespaces.OPC_UA,
        "ExceptionDeviationFormat",
        value,
        false,
        true,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxCountStoredValuesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxCountStoredValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxCountStoredValues() {
    return ServerNodeSupport.read(this, getMaxCountStoredValuesNode(), UInteger.class, null);
  }

  @Override
  public void setMaxCountStoredValues(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxCountStoredValuesNode(),
        Namespaces.OPC_UA,
        "MaxCountStoredValues",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeIntervalNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxTimeInterval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getMaxTimeInterval() {
    return ServerNodeSupport.read(this, getMaxTimeIntervalNode(), Double.class, null);
  }

  @Override
  public void setMaxTimeInterval(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getMaxTimeIntervalNode(),
        Namespaces.OPC_UA,
        "MaxTimeInterval",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeStoredValuesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxTimeStoredValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getMaxTimeStoredValues() {
    return ServerNodeSupport.read(this, getMaxTimeStoredValuesNode(), Double.class, null);
  }

  @Override
  public void setMaxTimeStoredValues(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getMaxTimeStoredValuesNode(),
        Namespaces.OPC_UA,
        "MaxTimeStoredValues",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMinTimeIntervalNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MinTimeInterval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getMinTimeInterval() {
    return ServerNodeSupport.read(this, getMinTimeIntervalNode(), Double.class, null);
  }

  @Override
  public void setMinTimeInterval(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getMinTimeIntervalNode(),
        Namespaces.OPC_UA,
        "MinTimeInterval",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getServerTimestampSupportedNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ServerTimestampSupported",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getServerTimestampSupported() {
    return ServerNodeSupport.read(this, getServerTimestampSupportedNode(), Boolean.class, null);
  }

  @Override
  public void setServerTimestampSupported(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getServerTimestampSupportedNode(),
        Namespaces.OPC_UA,
        "ServerTimestampSupported",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfArchiveNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "StartOfArchive",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getStartOfArchive() {
    return ServerNodeSupport.read(this, getStartOfArchiveNode(), DateTime.class, null);
  }

  @Override
  public void setStartOfArchive(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getStartOfArchiveNode(),
        Namespaces.OPC_UA,
        "StartOfArchive",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfOnlineArchiveNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "StartOfOnlineArchive",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getStartOfOnlineArchive() {
    return ServerNodeSupport.read(this, getStartOfOnlineArchiveNode(), DateTime.class, null);
  }

  @Override
  public void setStartOfOnlineArchive(@Nullable DateTime value) {
    ServerNodeSupport.write(
        this,
        getStartOfOnlineArchiveNode(),
        Namespaces.OPC_UA,
        "StartOfOnlineArchive",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getSteppedNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Stepped",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getStepped() {
    return ServerNodeSupport.read(this, getSteppedNode(), Boolean.class, null);
  }

  @Override
  public void setStepped(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getSteppedNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAggregateConfigurationNode();
    getAggregateFunctionsNode();
    getDefinitionNode();
    getExceptionDeviationNode();
    getExceptionDeviationFormatNode();
    getMaxCountStoredValuesNode();
    getMaxTimeIntervalNode();
    getMaxTimeStoredValuesNode();
    getMinTimeIntervalNode();
    getServerTimestampSupportedNode();
    getStartOfArchiveNode();
    getStartOfOnlineArchiveNode();
    getSteppedNode();
  }
}
