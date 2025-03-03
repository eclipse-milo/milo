/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class SubscriptionDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SubscriptionDiagnosticsType {
  public SubscriptionDiagnosticsTypeNode(
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

  public SubscriptionDiagnosticsTypeNode(
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
  public BaseDataVariableTypeNode getSubscriptionIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SubscriptionId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getSubscriptionId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SubscriptionId");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSubscriptionId(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SubscriptionId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPriorityNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Priority");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UByte getPriority() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "Priority");
    return component.map(node -> (UByte) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPriority(UByte value) {
    getVariableComponent("http://opcfoundation.org/UA/", "Priority")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPublishingIntervalNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishingInterval");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Double getPublishingInterval() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishingInterval");
    return component.map(node -> (Double) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPublishingInterval(Double value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PublishingInterval")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaxKeepAliveCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxKeepAliveCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMaxKeepAliveCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxKeepAliveCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxKeepAliveCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxKeepAliveCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaxLifetimeCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxLifetimeCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMaxLifetimeCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxLifetimeCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxLifetimeCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxLifetimeCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMaxNotificationsPerPublishNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxNotificationsPerPublish");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMaxNotificationsPerPublish() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MaxNotificationsPerPublish");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMaxNotificationsPerPublish(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MaxNotificationsPerPublish")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPublishingEnabledNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishingEnabled");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Boolean getPublishingEnabled() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishingEnabled");
    return component.map(node -> (Boolean) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPublishingEnabled(Boolean value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PublishingEnabled")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getModifyCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ModifyCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getModifyCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ModifyCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setModifyCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ModifyCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getEnableCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EnableCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getEnableCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EnableCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setEnableCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "EnableCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDisableCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DisableCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getDisableCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DisableCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDisableCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DisableCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRepublishRequestCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishRequestCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRepublishRequestCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishRequestCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRepublishRequestCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RepublishRequestCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRepublishMessageRequestCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishMessageRequestCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRepublishMessageRequestCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishMessageRequestCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRepublishMessageRequestCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RepublishMessageRequestCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRepublishMessageCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishMessageCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRepublishMessageCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RepublishMessageCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRepublishMessageCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RepublishMessageCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getTransferRequestCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferRequestCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getTransferRequestCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferRequestCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTransferRequestCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TransferRequestCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getTransferredToAltClientCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferredToAltClientCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getTransferredToAltClientCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferredToAltClientCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTransferredToAltClientCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TransferredToAltClientCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getTransferredToSameClientCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferredToSameClientCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getTransferredToSameClientCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "TransferredToSameClientCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setTransferredToSameClientCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "TransferredToSameClientCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPublishRequestCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishRequestCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getPublishRequestCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishRequestCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPublishRequestCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PublishRequestCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDataChangeNotificationsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DataChangeNotificationsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getDataChangeNotificationsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DataChangeNotificationsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDataChangeNotificationsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DataChangeNotificationsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getEventNotificationsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EventNotificationsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getEventNotificationsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EventNotificationsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setEventNotificationsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "EventNotificationsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getNotificationsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "NotificationsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getNotificationsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "NotificationsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setNotificationsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "NotificationsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLatePublishRequestCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LatePublishRequestCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getLatePublishRequestCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LatePublishRequestCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLatePublishRequestCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LatePublishRequestCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentKeepAliveCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentKeepAliveCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCurrentKeepAliveCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentKeepAliveCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentKeepAliveCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentKeepAliveCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentLifetimeCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentLifetimeCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCurrentLifetimeCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentLifetimeCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentLifetimeCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentLifetimeCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getUnacknowledgedMessageCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "UnacknowledgedMessageCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getUnacknowledgedMessageCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "UnacknowledgedMessageCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setUnacknowledgedMessageCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "UnacknowledgedMessageCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDiscardedMessageCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DiscardedMessageCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getDiscardedMessageCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DiscardedMessageCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDiscardedMessageCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DiscardedMessageCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMonitoredItemCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MonitoredItemCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMonitoredItemCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MonitoredItemCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMonitoredItemCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MonitoredItemCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getDisabledMonitoredItemCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DisabledMonitoredItemCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getDisabledMonitoredItemCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "DisabledMonitoredItemCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setDisabledMonitoredItemCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "DisabledMonitoredItemCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getMonitoringQueueOverflowCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MonitoringQueueOverflowCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getMonitoringQueueOverflowCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "MonitoringQueueOverflowCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setMonitoringQueueOverflowCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "MonitoringQueueOverflowCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getNextSequenceNumberNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "NextSequenceNumber");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getNextSequenceNumber() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "NextSequenceNumber");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setNextSequenceNumber(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "NextSequenceNumber")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getEventQueueOverflowCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EventQueueOverflowCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getEventQueueOverflowCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "EventQueueOverflowCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setEventQueueOverflowCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "EventQueueOverflowCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}
