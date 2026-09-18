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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SubscriptionDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.12">Model
 *     documentation</a>
 */
public class SubscriptionDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SubscriptionDiagnosticsType {
  public SubscriptionDiagnosticsTypeNode(
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

  public SubscriptionDiagnosticsTypeNode(
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
  public BaseDataVariableTypeNode getCurrentKeepAliveCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentKeepAliveCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentKeepAliveCount() {
    return ServerNodeSupport.read(this, getCurrentKeepAliveCountNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentKeepAliveCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCurrentKeepAliveCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCurrentLifetimeCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentLifetimeCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentLifetimeCount() {
    return ServerNodeSupport.read(this, getCurrentLifetimeCountNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentLifetimeCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCurrentLifetimeCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getDataChangeNotificationsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataChangeNotificationsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getDataChangeNotificationsCount() {
    return ServerNodeSupport.read(
        this, getDataChangeNotificationsCountNode(), UInteger.class, null);
  }

  @Override
  public void setDataChangeNotificationsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getDataChangeNotificationsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getDisableCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DisableCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getDisableCount() {
    return ServerNodeSupport.read(this, getDisableCountNode(), UInteger.class, null);
  }

  @Override
  public void setDisableCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getDisableCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getDisabledMonitoredItemCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DisabledMonitoredItemCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getDisabledMonitoredItemCount() {
    return ServerNodeSupport.read(this, getDisabledMonitoredItemCountNode(), UInteger.class, null);
  }

  @Override
  public void setDisabledMonitoredItemCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getDisabledMonitoredItemCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getDiscardedMessageCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DiscardedMessageCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getDiscardedMessageCount() {
    return ServerNodeSupport.read(this, getDiscardedMessageCountNode(), UInteger.class, null);
  }

  @Override
  public void setDiscardedMessageCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getDiscardedMessageCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getEnableCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EnableCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getEnableCount() {
    return ServerNodeSupport.read(this, getEnableCountNode(), UInteger.class, null);
  }

  @Override
  public void setEnableCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getEnableCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getEventNotificationsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EventNotificationsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getEventNotificationsCount() {
    return ServerNodeSupport.read(this, getEventNotificationsCountNode(), UInteger.class, null);
  }

  @Override
  public void setEventNotificationsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getEventNotificationsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getEventQueueOverflowCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EventQueueOverflowCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getEventQueueOverflowCount() {
    return ServerNodeSupport.read(this, getEventQueueOverflowCountNode(), UInteger.class, null);
  }

  @Override
  public void setEventQueueOverflowCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getEventQueueOverflowCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getLatePublishRequestCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LatePublishRequestCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getLatePublishRequestCount() {
    return ServerNodeSupport.read(this, getLatePublishRequestCountNode(), UInteger.class, null);
  }

  @Override
  public void setLatePublishRequestCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getLatePublishRequestCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaxKeepAliveCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxKeepAliveCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxKeepAliveCount() {
    return ServerNodeSupport.read(this, getMaxKeepAliveCountNode(), UInteger.class, null);
  }

  @Override
  public void setMaxKeepAliveCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxKeepAliveCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaxLifetimeCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxLifetimeCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxLifetimeCount() {
    return ServerNodeSupport.read(this, getMaxLifetimeCountNode(), UInteger.class, null);
  }

  @Override
  public void setMaxLifetimeCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxLifetimeCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaxNotificationsPerPublishNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxNotificationsPerPublish",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNotificationsPerPublish() {
    return ServerNodeSupport.read(this, getMaxNotificationsPerPublishNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNotificationsPerPublish(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxNotificationsPerPublishNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getModifyCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ModifyCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getModifyCount() {
    return ServerNodeSupport.read(this, getModifyCountNode(), UInteger.class, null);
  }

  @Override
  public void setModifyCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getModifyCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMonitoredItemCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MonitoredItemCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMonitoredItemCount() {
    return ServerNodeSupport.read(this, getMonitoredItemCountNode(), UInteger.class, null);
  }

  @Override
  public void setMonitoredItemCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMonitoredItemCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMonitoringQueueOverflowCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MonitoringQueueOverflowCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMonitoringQueueOverflowCount() {
    return ServerNodeSupport.read(
        this, getMonitoringQueueOverflowCountNode(), UInteger.class, null);
  }

  @Override
  public void setMonitoringQueueOverflowCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getMonitoringQueueOverflowCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getNextSequenceNumberNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NextSequenceNumber",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getNextSequenceNumber() {
    return ServerNodeSupport.read(this, getNextSequenceNumberNode(), UInteger.class, null);
  }

  @Override
  public void setNextSequenceNumber(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getNextSequenceNumberNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getNotificationsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "NotificationsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getNotificationsCount() {
    return ServerNodeSupport.read(this, getNotificationsCountNode(), UInteger.class, null);
  }

  @Override
  public void setNotificationsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getNotificationsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getPriorityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Priority",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UByte getPriority() {
    return ServerNodeSupport.read(this, getPriorityNode(), UByte.class, null);
  }

  @Override
  public void setPriority(@Nullable UByte value) {
    ServerNodeSupport.write(this, getPriorityNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getPublishRequestCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishRequestCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getPublishRequestCount() {
    return ServerNodeSupport.read(this, getPublishRequestCountNode(), UInteger.class, null);
  }

  @Override
  public void setPublishRequestCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getPublishRequestCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getPublishingEnabledNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishingEnabled",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Boolean getPublishingEnabled() {
    return ServerNodeSupport.read(this, getPublishingEnabledNode(), Boolean.class, null);
  }

  @Override
  public void setPublishingEnabled(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getPublishingEnabledNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getPublishingIntervalNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishingInterval",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getPublishingInterval() {
    return ServerNodeSupport.read(this, getPublishingIntervalNode(), Double.class, null);
  }

  @Override
  public void setPublishingInterval(@Nullable Double value) {
    ServerNodeSupport.write(this, getPublishingIntervalNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRepublishMessageCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RepublishMessageCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRepublishMessageCount() {
    return ServerNodeSupport.read(this, getRepublishMessageCountNode(), UInteger.class, null);
  }

  @Override
  public void setRepublishMessageCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRepublishMessageCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRepublishMessageRequestCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RepublishMessageRequestCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRepublishMessageRequestCount() {
    return ServerNodeSupport.read(
        this, getRepublishMessageRequestCountNode(), UInteger.class, null);
  }

  @Override
  public void setRepublishMessageRequestCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getRepublishMessageRequestCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRepublishRequestCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RepublishRequestCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRepublishRequestCount() {
    return ServerNodeSupport.read(this, getRepublishRequestCountNode(), UInteger.class, null);
  }

  @Override
  public void setRepublishRequestCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRepublishRequestCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSessionIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable NodeId getSessionId() {
    return ServerNodeSupport.read(this, getSessionIdNode(), NodeId.class, null);
  }

  @Override
  public void setSessionId(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getSessionIdNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSubscriptionIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SubscriptionId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getSubscriptionId() {
    return ServerNodeSupport.read(this, getSubscriptionIdNode(), UInteger.class, null);
  }

  @Override
  public void setSubscriptionId(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getSubscriptionIdNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getTransferRequestCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TransferRequestCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getTransferRequestCount() {
    return ServerNodeSupport.read(this, getTransferRequestCountNode(), UInteger.class, null);
  }

  @Override
  public void setTransferRequestCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getTransferRequestCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getTransferredToAltClientCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TransferredToAltClientCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getTransferredToAltClientCount() {
    return ServerNodeSupport.read(this, getTransferredToAltClientCountNode(), UInteger.class, null);
  }

  @Override
  public void setTransferredToAltClientCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getTransferredToAltClientCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getTransferredToSameClientCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TransferredToSameClientCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getTransferredToSameClientCount() {
    return ServerNodeSupport.read(
        this, getTransferredToSameClientCountNode(), UInteger.class, null);
  }

  @Override
  public void setTransferredToSameClientCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getTransferredToSameClientCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getUnacknowledgedMessageCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UnacknowledgedMessageCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getUnacknowledgedMessageCount() {
    return ServerNodeSupport.read(this, getUnacknowledgedMessageCountNode(), UInteger.class, null);
  }

  @Override
  public void setUnacknowledgedMessageCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getUnacknowledgedMessageCountNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCurrentKeepAliveCountNode();
    getCurrentLifetimeCountNode();
    getDataChangeNotificationsCountNode();
    getDisableCountNode();
    getDisabledMonitoredItemCountNode();
    getDiscardedMessageCountNode();
    getEnableCountNode();
    getEventNotificationsCountNode();
    getEventQueueOverflowCountNode();
    getLatePublishRequestCountNode();
    getMaxKeepAliveCountNode();
    getMaxLifetimeCountNode();
    getMaxNotificationsPerPublishNode();
    getModifyCountNode();
    getMonitoredItemCountNode();
    getMonitoringQueueOverflowCountNode();
    getNextSequenceNumberNode();
    getNotificationsCountNode();
    getPriorityNode();
    getPublishRequestCountNode();
    getPublishingEnabledNode();
    getPublishingIntervalNode();
    getRepublishMessageCountNode();
    getRepublishMessageRequestCountNode();
    getRepublishRequestCountNode();
    getSessionIdNode();
    getSubscriptionIdNode();
    getTransferRequestCountNode();
    getTransferredToAltClientCountNode();
    getTransferredToSameClientCountNode();
    getUnacknowledgedMessageCountNode();
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType getTypedValue() {
    return ServerNodeSupport.read(this, this, SubscriptionDiagnosticsDataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable SubscriptionDiagnosticsDataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
