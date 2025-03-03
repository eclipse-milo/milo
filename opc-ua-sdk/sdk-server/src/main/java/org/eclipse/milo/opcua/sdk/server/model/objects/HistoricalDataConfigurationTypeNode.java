/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class HistoricalDataConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalDataConfigurationType {
  public HistoricalDataConfigurationTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
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

  public HistoricalDataConfigurationTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
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

  @Override
  public PropertyTypeNode getSteppedNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(HistoricalDataConfigurationType.STEPPED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getStepped() {
    return getProperty(HistoricalDataConfigurationType.STEPPED).orElse(null);
  }

  @Override
  public void setStepped(Boolean value) {
    setProperty(HistoricalDataConfigurationType.STEPPED, value);
  }

  @Override
  public PropertyTypeNode getDefinitionNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.DEFINITION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getDefinition() {
    return getProperty(HistoricalDataConfigurationType.DEFINITION).orElse(null);
  }

  @Override
  public void setDefinition(String value) {
    setProperty(HistoricalDataConfigurationType.DEFINITION, value);
  }

  @Override
  public PropertyTypeNode getMaxTimeIntervalNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.MAX_TIME_INTERVAL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMaxTimeInterval() {
    return getProperty(HistoricalDataConfigurationType.MAX_TIME_INTERVAL).orElse(null);
  }

  @Override
  public void setMaxTimeInterval(Double value) {
    setProperty(HistoricalDataConfigurationType.MAX_TIME_INTERVAL, value);
  }

  @Override
  public PropertyTypeNode getMinTimeIntervalNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.MIN_TIME_INTERVAL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMinTimeInterval() {
    return getProperty(HistoricalDataConfigurationType.MIN_TIME_INTERVAL).orElse(null);
  }

  @Override
  public void setMinTimeInterval(Double value) {
    setProperty(HistoricalDataConfigurationType.MIN_TIME_INTERVAL, value);
  }

  @Override
  public PropertyTypeNode getExceptionDeviationNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.EXCEPTION_DEVIATION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getExceptionDeviation() {
    return getProperty(HistoricalDataConfigurationType.EXCEPTION_DEVIATION).orElse(null);
  }

  @Override
  public void setExceptionDeviation(Double value) {
    setProperty(HistoricalDataConfigurationType.EXCEPTION_DEVIATION, value);
  }

  @Override
  public PropertyTypeNode getExceptionDeviationFormatNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.EXCEPTION_DEVIATION_FORMAT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public ExceptionDeviationFormat getExceptionDeviationFormat() {
    return getProperty(HistoricalDataConfigurationType.EXCEPTION_DEVIATION_FORMAT).orElse(null);
  }

  @Override
  public void setExceptionDeviationFormat(ExceptionDeviationFormat value) {
    setProperty(HistoricalDataConfigurationType.EXCEPTION_DEVIATION_FORMAT, value);
  }

  @Override
  public PropertyTypeNode getStartOfArchiveNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.START_OF_ARCHIVE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getStartOfArchive() {
    return getProperty(HistoricalDataConfigurationType.START_OF_ARCHIVE).orElse(null);
  }

  @Override
  public void setStartOfArchive(DateTime value) {
    setProperty(HistoricalDataConfigurationType.START_OF_ARCHIVE, value);
  }

  @Override
  public PropertyTypeNode getStartOfOnlineArchiveNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.START_OF_ONLINE_ARCHIVE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getStartOfOnlineArchive() {
    return getProperty(HistoricalDataConfigurationType.START_OF_ONLINE_ARCHIVE).orElse(null);
  }

  @Override
  public void setStartOfOnlineArchive(DateTime value) {
    setProperty(HistoricalDataConfigurationType.START_OF_ONLINE_ARCHIVE, value);
  }

  @Override
  public PropertyTypeNode getServerTimestampSupportedNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.SERVER_TIMESTAMP_SUPPORTED);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getServerTimestampSupported() {
    return getProperty(HistoricalDataConfigurationType.SERVER_TIMESTAMP_SUPPORTED).orElse(null);
  }

  @Override
  public void setServerTimestampSupported(Boolean value) {
    setProperty(HistoricalDataConfigurationType.SERVER_TIMESTAMP_SUPPORTED, value);
  }

  @Override
  public PropertyTypeNode getMaxTimeStoredValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.MAX_TIME_STORED_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMaxTimeStoredValues() {
    return getProperty(HistoricalDataConfigurationType.MAX_TIME_STORED_VALUES).orElse(null);
  }

  @Override
  public void setMaxTimeStoredValues(Double value) {
    setProperty(HistoricalDataConfigurationType.MAX_TIME_STORED_VALUES, value);
  }

  @Override
  public PropertyTypeNode getMaxCountStoredValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(HistoricalDataConfigurationType.MAX_COUNT_STORED_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxCountStoredValues() {
    return getProperty(HistoricalDataConfigurationType.MAX_COUNT_STORED_VALUES).orElse(null);
  }

  @Override
  public void setMaxCountStoredValues(UInteger value) {
    setProperty(HistoricalDataConfigurationType.MAX_COUNT_STORED_VALUES, value);
  }

  @Override
  public AggregateConfigurationTypeNode getAggregateConfigurationNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "AggregateConfiguration");
    return (AggregateConfigurationTypeNode) component.orElse(null);
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "AggregateFunctions");
    return (FolderTypeNode) component.orElse(null);
  }
}
