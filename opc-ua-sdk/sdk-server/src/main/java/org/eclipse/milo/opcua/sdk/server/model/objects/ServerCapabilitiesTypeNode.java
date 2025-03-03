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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;

public class ServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements ServerCapabilitiesType {
  public ServerCapabilitiesTypeNode(
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

  public ServerCapabilitiesTypeNode(
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
  public PropertyTypeNode getServerProfileArrayNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.SERVER_PROFILE_ARRAY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getServerProfileArray() {
    return getProperty(ServerCapabilitiesType.SERVER_PROFILE_ARRAY).orElse(null);
  }

  @Override
  public void setServerProfileArray(String[] value) {
    setProperty(ServerCapabilitiesType.SERVER_PROFILE_ARRAY, value);
  }

  @Override
  public PropertyTypeNode getLocaleIdArrayNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerCapabilitiesType.LOCALE_ID_ARRAY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String[] getLocaleIdArray() {
    return getProperty(ServerCapabilitiesType.LOCALE_ID_ARRAY).orElse(null);
  }

  @Override
  public void setLocaleIdArray(String[] value) {
    setProperty(ServerCapabilitiesType.LOCALE_ID_ARRAY, value);
  }

  @Override
  public PropertyTypeNode getMinSupportedSampleRateNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MIN_SUPPORTED_SAMPLE_RATE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMinSupportedSampleRate() {
    return getProperty(ServerCapabilitiesType.MIN_SUPPORTED_SAMPLE_RATE).orElse(null);
  }

  @Override
  public void setMinSupportedSampleRate(Double value) {
    setProperty(ServerCapabilitiesType.MIN_SUPPORTED_SAMPLE_RATE, value);
  }

  @Override
  public PropertyTypeNode getMaxBrowseContinuationPointsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_BROWSE_CONTINUATION_POINTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getMaxBrowseContinuationPoints() {
    return getProperty(ServerCapabilitiesType.MAX_BROWSE_CONTINUATION_POINTS).orElse(null);
  }

  @Override
  public void setMaxBrowseContinuationPoints(UShort value) {
    setProperty(ServerCapabilitiesType.MAX_BROWSE_CONTINUATION_POINTS, value);
  }

  @Override
  public PropertyTypeNode getMaxQueryContinuationPointsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_QUERY_CONTINUATION_POINTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getMaxQueryContinuationPoints() {
    return getProperty(ServerCapabilitiesType.MAX_QUERY_CONTINUATION_POINTS).orElse(null);
  }

  @Override
  public void setMaxQueryContinuationPoints(UShort value) {
    setProperty(ServerCapabilitiesType.MAX_QUERY_CONTINUATION_POINTS, value);
  }

  @Override
  public PropertyTypeNode getMaxHistoryContinuationPointsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_HISTORY_CONTINUATION_POINTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getMaxHistoryContinuationPoints() {
    return getProperty(ServerCapabilitiesType.MAX_HISTORY_CONTINUATION_POINTS).orElse(null);
  }

  @Override
  public void setMaxHistoryContinuationPoints(UShort value) {
    setProperty(ServerCapabilitiesType.MAX_HISTORY_CONTINUATION_POINTS, value);
  }

  @Override
  public PropertyTypeNode getSoftwareCertificatesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.SOFTWARE_CERTIFICATES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public SignedSoftwareCertificate[] getSoftwareCertificates() {
    return getProperty(ServerCapabilitiesType.SOFTWARE_CERTIFICATES).orElse(null);
  }

  @Override
  public void setSoftwareCertificates(SignedSoftwareCertificate[] value) {
    setProperty(ServerCapabilitiesType.SOFTWARE_CERTIFICATES, value);
  }

  @Override
  public PropertyTypeNode getMaxArrayLengthNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerCapabilitiesType.MAX_ARRAY_LENGTH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxArrayLength() {
    return getProperty(ServerCapabilitiesType.MAX_ARRAY_LENGTH).orElse(null);
  }

  @Override
  public void setMaxArrayLength(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_ARRAY_LENGTH, value);
  }

  @Override
  public PropertyTypeNode getMaxStringLengthNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerCapabilitiesType.MAX_STRING_LENGTH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxStringLength() {
    return getProperty(ServerCapabilitiesType.MAX_STRING_LENGTH).orElse(null);
  }

  @Override
  public void setMaxStringLength(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_STRING_LENGTH, value);
  }

  @Override
  public PropertyTypeNode getMaxByteStringLengthNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_BYTE_STRING_LENGTH);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxByteStringLength() {
    return getProperty(ServerCapabilitiesType.MAX_BYTE_STRING_LENGTH).orElse(null);
  }

  @Override
  public void setMaxByteStringLength(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_BYTE_STRING_LENGTH, value);
  }

  @Override
  public PropertyTypeNode getMaxSessionsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerCapabilitiesType.MAX_SESSIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxSessions() {
    return getProperty(ServerCapabilitiesType.MAX_SESSIONS).orElse(null);
  }

  @Override
  public void setMaxSessions(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_SESSIONS, value);
  }

  @Override
  public PropertyTypeNode getMaxSubscriptionsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerCapabilitiesType.MAX_SUBSCRIPTIONS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxSubscriptions() {
    return getProperty(ServerCapabilitiesType.MAX_SUBSCRIPTIONS).orElse(null);
  }

  @Override
  public void setMaxSubscriptions(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_SUBSCRIPTIONS, value);
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_MONITORED_ITEMS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxMonitoredItems() {
    return getProperty(ServerCapabilitiesType.MAX_MONITORED_ITEMS).orElse(null);
  }

  @Override
  public void setMaxMonitoredItems(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_MONITORED_ITEMS, value);
  }

  @Override
  public PropertyTypeNode getMaxSubscriptionsPerSessionNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_SUBSCRIPTIONS_PER_SESSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxSubscriptionsPerSession() {
    return getProperty(ServerCapabilitiesType.MAX_SUBSCRIPTIONS_PER_SESSION).orElse(null);
  }

  @Override
  public void setMaxSubscriptionsPerSession(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_SUBSCRIPTIONS_PER_SESSION, value);
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsPerSubscriptionNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_MONITORED_ITEMS_PER_SUBSCRIPTION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxMonitoredItemsPerSubscription() {
    return getProperty(ServerCapabilitiesType.MAX_MONITORED_ITEMS_PER_SUBSCRIPTION).orElse(null);
  }

  @Override
  public void setMaxMonitoredItemsPerSubscription(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_MONITORED_ITEMS_PER_SUBSCRIPTION, value);
  }

  @Override
  public PropertyTypeNode getMaxSelectClauseParametersNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_SELECT_CLAUSE_PARAMETERS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxSelectClauseParameters() {
    return getProperty(ServerCapabilitiesType.MAX_SELECT_CLAUSE_PARAMETERS).orElse(null);
  }

  @Override
  public void setMaxSelectClauseParameters(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_SELECT_CLAUSE_PARAMETERS, value);
  }

  @Override
  public PropertyTypeNode getMaxWhereClauseParametersNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_WHERE_CLAUSE_PARAMETERS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxWhereClauseParameters() {
    return getProperty(ServerCapabilitiesType.MAX_WHERE_CLAUSE_PARAMETERS).orElse(null);
  }

  @Override
  public void setMaxWhereClauseParameters(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_WHERE_CLAUSE_PARAMETERS, value);
  }

  @Override
  public PropertyTypeNode getMaxMonitoredItemsQueueSizeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ServerCapabilitiesType.MAX_MONITORED_ITEMS_QUEUE_SIZE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getMaxMonitoredItemsQueueSize() {
    return getProperty(ServerCapabilitiesType.MAX_MONITORED_ITEMS_QUEUE_SIZE).orElse(null);
  }

  @Override
  public void setMaxMonitoredItemsQueueSize(UInteger value) {
    setProperty(ServerCapabilitiesType.MAX_MONITORED_ITEMS_QUEUE_SIZE, value);
  }

  @Override
  public PropertyTypeNode getConformanceUnitsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerCapabilitiesType.CONFORMANCE_UNITS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public QualifiedName[] getConformanceUnits() {
    return getProperty(ServerCapabilitiesType.CONFORMANCE_UNITS).orElse(null);
  }

  @Override
  public void setConformanceUnits(QualifiedName[] value) {
    setProperty(ServerCapabilitiesType.CONFORMANCE_UNITS, value);
  }

  @Override
  public OperationLimitsTypeNode getOperationLimitsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "OperationLimits");
    return (OperationLimitsTypeNode) component.orElse(null);
  }

  @Override
  public FolderTypeNode getModellingRulesNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "ModellingRules");
    return (FolderTypeNode) component.orElse(null);
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "AggregateFunctions");
    return (FolderTypeNode) component.orElse(null);
  }

  @Override
  public RoleSetTypeNode getRoleSetNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "RoleSet");
    return (RoleSetTypeNode) component.orElse(null);
  }
}
