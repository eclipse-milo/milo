/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;

public class SessionDiagnosticsVariableTypeNode extends BaseDataVariableTypeNode
    implements SessionDiagnosticsVariableType {
  public SessionDiagnosticsVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
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

  public SessionDiagnosticsVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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

  @Override
  public BaseDataVariableTypeNode getSessionIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public NodeId getSessionId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionId");
    return component.map(node -> (NodeId) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSessionId(NodeId value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSessionNameNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionName");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getSessionName() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionName");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSessionName(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionName")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getClientDescriptionNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientDescription");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ApplicationDescription getClientDescription() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientDescription");
    return component
        .map(node -> (ApplicationDescription) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setClientDescription(ApplicationDescription value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ClientDescription")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getServerUriNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ServerUri");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getServerUri() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ServerUri");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setServerUri(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ServerUri")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getEndpointUrlNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EndpointUrl");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getEndpointUrl() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EndpointUrl");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setEndpointUrl(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "EndpointUrl")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLocaleIdsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LocaleIds");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String[] getLocaleIds() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LocaleIds");
    return component.map(node -> (String[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLocaleIds(String[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LocaleIds")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getActualSessionTimeoutNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ActualSessionTimeout");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getActualSessionTimeout() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ActualSessionTimeout");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setActualSessionTimeout(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ActualSessionTimeout")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaxResponseMessageSizeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxResponseMessageSize");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMaxResponseMessageSize() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxResponseMessageSize");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxResponseMessageSize(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxResponseMessageSize")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getClientConnectionTimeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientConnectionTime");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public DateTime getClientConnectionTime() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientConnectionTime");
    return component.map(node -> (DateTime) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setClientConnectionTime(DateTime value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ClientConnectionTime")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getClientLastContactTimeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientLastContactTime");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public DateTime getClientLastContactTime() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ClientLastContactTime");
    return component.map(node -> (DateTime) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setClientLastContactTime(DateTime value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ClientLastContactTime")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSubscriptionsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentSubscriptionsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCurrentSubscriptionsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentSubscriptionsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentSubscriptionsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentSubscriptionsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentMonitoredItemsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentMonitoredItemsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCurrentMonitoredItemsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentMonitoredItemsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentMonitoredItemsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentMonitoredItemsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentPublishRequestsInQueueNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentPublishRequestsInQueue");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCurrentPublishRequestsInQueue() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentPublishRequestsInQueue");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentPublishRequestsInQueue(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentPublishRequestsInQueue")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getTotalRequestCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TotalRequestCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getTotalRequestCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TotalRequestCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setTotalRequestCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TotalRequestCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getUnauthorizedRequestCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "UnauthorizedRequestCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getUnauthorizedRequestCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "UnauthorizedRequestCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setUnauthorizedRequestCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "UnauthorizedRequestCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getReadCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ReadCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getReadCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ReadCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setReadCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ReadCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getHistoryReadCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HistoryReadCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getHistoryReadCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HistoryReadCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setHistoryReadCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "HistoryReadCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getWriteCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "WriteCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getWriteCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "WriteCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setWriteCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "WriteCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getHistoryUpdateCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HistoryUpdateCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getHistoryUpdateCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "HistoryUpdateCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setHistoryUpdateCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "HistoryUpdateCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCallCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CallCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getCallCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CallCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setCallCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CallCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCreateMonitoredItemsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateMonitoredItemsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getCreateMonitoredItemsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateMonitoredItemsCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setCreateMonitoredItemsCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CreateMonitoredItemsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getModifyMonitoredItemsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ModifyMonitoredItemsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getModifyMonitoredItemsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ModifyMonitoredItemsCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setModifyMonitoredItemsCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ModifyMonitoredItemsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSetMonitoringModeCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SetMonitoringModeCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getSetMonitoringModeCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SetMonitoringModeCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSetMonitoringModeCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SetMonitoringModeCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSetTriggeringCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SetTriggeringCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getSetTriggeringCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SetTriggeringCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSetTriggeringCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SetTriggeringCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteMonitoredItemsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteMonitoredItemsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getDeleteMonitoredItemsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteMonitoredItemsCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setDeleteMonitoredItemsCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DeleteMonitoredItemsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCreateSubscriptionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateSubscriptionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getCreateSubscriptionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateSubscriptionCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setCreateSubscriptionCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CreateSubscriptionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getModifySubscriptionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ModifySubscriptionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getModifySubscriptionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ModifySubscriptionCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setModifySubscriptionCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ModifySubscriptionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSetPublishingModeCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SetPublishingModeCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getSetPublishingModeCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SetPublishingModeCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSetPublishingModeCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SetPublishingModeCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPublishCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getPublishCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setPublishCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PublishCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRepublishCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getRepublishCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setRepublishCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RepublishCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getTransferSubscriptionsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferSubscriptionsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getTransferSubscriptionsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferSubscriptionsCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setTransferSubscriptionsCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TransferSubscriptionsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteSubscriptionsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteSubscriptionsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getDeleteSubscriptionsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteSubscriptionsCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setDeleteSubscriptionsCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DeleteSubscriptionsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getAddNodesCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AddNodesCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getAddNodesCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AddNodesCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setAddNodesCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AddNodesCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getAddReferencesCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AddReferencesCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getAddReferencesCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "AddReferencesCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setAddReferencesCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "AddReferencesCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteNodesCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteNodesCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getDeleteNodesCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteNodesCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setDeleteNodesCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DeleteNodesCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDeleteReferencesCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteReferencesCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getDeleteReferencesCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DeleteReferencesCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setDeleteReferencesCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DeleteReferencesCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getBrowseCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "BrowseCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getBrowseCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "BrowseCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setBrowseCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "BrowseCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getBrowseNextCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "BrowseNextCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getBrowseNextCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "BrowseNextCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setBrowseNextCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "BrowseNextCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getTranslateBrowsePathsToNodeIdsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TranslateBrowsePathsToNodeIdsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TranslateBrowsePathsToNodeIdsCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setTranslateBrowsePathsToNodeIdsCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TranslateBrowsePathsToNodeIdsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getQueryFirstCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "QueryFirstCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getQueryFirstCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "QueryFirstCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setQueryFirstCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "QueryFirstCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getQueryNextCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "QueryNextCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getQueryNextCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "QueryNextCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setQueryNextCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "QueryNextCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRegisterNodesCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RegisterNodesCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getRegisterNodesCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RegisterNodesCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setRegisterNodesCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RegisterNodesCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getUnregisterNodesCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "UnregisterNodesCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public ServiceCounterDataType getUnregisterNodesCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "UnregisterNodesCount");
    return component
        .map(node -> (ServiceCounterDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setUnregisterNodesCount(ServiceCounterDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "UnregisterNodesCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
