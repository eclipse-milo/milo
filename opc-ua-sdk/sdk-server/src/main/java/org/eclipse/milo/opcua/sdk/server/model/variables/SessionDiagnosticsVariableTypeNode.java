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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SessionDiagnosticsVariableType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">Model
 *     documentation</a>
 */
public class SessionDiagnosticsVariableTypeNode extends BaseDataVariableTypeNode
    implements SessionDiagnosticsVariableType {
  public SessionDiagnosticsVariableTypeNode(
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

  public SessionDiagnosticsVariableTypeNode(
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
  public BaseDataVariableTypeNode getActualSessionTimeoutNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ActualSessionTimeout",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Double getActualSessionTimeout() {
    return ServerNodeSupport.read(this, getActualSessionTimeoutNode(), Double.class, null);
  }

  @Override
  public void setActualSessionTimeout(@Nullable Double value) {
    ServerNodeSupport.write(this, getActualSessionTimeoutNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getAddNodesCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AddNodesCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getAddNodesCount() {
    return ServerNodeSupport.read(this, getAddNodesCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setAddNodesCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getAddNodesCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getAddReferencesCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AddReferencesCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getAddReferencesCount() {
    return ServerNodeSupport.read(
        this, getAddReferencesCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setAddReferencesCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getAddReferencesCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getBrowseCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "BrowseCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getBrowseCount() {
    return ServerNodeSupport.read(this, getBrowseCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setBrowseCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getBrowseCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getBrowseNextCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "BrowseNextCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getBrowseNextCount() {
    return ServerNodeSupport.read(
        this, getBrowseNextCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setBrowseNextCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getBrowseNextCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getCallCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CallCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getCallCount() {
    return ServerNodeSupport.read(this, getCallCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setCallCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getCallCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getClientConnectionTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientConnectionTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getClientConnectionTime() {
    return ServerNodeSupport.read(this, getClientConnectionTimeNode(), DateTime.class, null);
  }

  @Override
  public void setClientConnectionTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getClientConnectionTimeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getClientDescriptionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientDescription",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 308L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ApplicationDescription getClientDescription() {
    return ServerNodeSupport.read(
        this, getClientDescriptionNode(), ApplicationDescription.class, null);
  }

  @Override
  public void setClientDescription(@Nullable ApplicationDescription value) {
    ServerNodeSupport.write(this, getClientDescriptionNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getClientLastContactTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ClientLastContactTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getClientLastContactTime() {
    return ServerNodeSupport.read(this, getClientLastContactTimeNode(), DateTime.class, null);
  }

  @Override
  public void setClientLastContactTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getClientLastContactTimeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCreateMonitoredItemsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CreateMonitoredItemsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getCreateMonitoredItemsCount() {
    return ServerNodeSupport.read(
        this, getCreateMonitoredItemsCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getCreateMonitoredItemsCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getCreateSubscriptionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CreateSubscriptionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getCreateSubscriptionCount() {
    return ServerNodeSupport.read(
        this, getCreateSubscriptionCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setCreateSubscriptionCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getCreateSubscriptionCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getCurrentMonitoredItemsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentMonitoredItemsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentMonitoredItemsCount() {
    return ServerNodeSupport.read(this, getCurrentMonitoredItemsCountNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentMonitoredItemsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCurrentMonitoredItemsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCurrentPublishRequestsInQueueNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentPublishRequestsInQueue",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentPublishRequestsInQueue() {
    return ServerNodeSupport.read(
        this, getCurrentPublishRequestsInQueueNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentPublishRequestsInQueue(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getCurrentPublishRequestsInQueueNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSubscriptionsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentSubscriptionsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentSubscriptionsCount() {
    return ServerNodeSupport.read(this, getCurrentSubscriptionsCountNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentSubscriptionsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCurrentSubscriptionsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getDeleteMonitoredItemsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DeleteMonitoredItemsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteMonitoredItemsCount() {
    return ServerNodeSupport.read(
        this, getDeleteMonitoredItemsCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getDeleteMonitoredItemsCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getDeleteNodesCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DeleteNodesCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteNodesCount() {
    return ServerNodeSupport.read(
        this, getDeleteNodesCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setDeleteNodesCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getDeleteNodesCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getDeleteReferencesCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DeleteReferencesCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteReferencesCount() {
    return ServerNodeSupport.read(
        this, getDeleteReferencesCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setDeleteReferencesCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getDeleteReferencesCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getDeleteSubscriptionsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DeleteSubscriptionsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getDeleteSubscriptionsCount() {
    return ServerNodeSupport.read(
        this, getDeleteSubscriptionsCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getDeleteSubscriptionsCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getEndpointUrlNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EndpointUrl",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getEndpointUrl() {
    return ServerNodeSupport.read(this, getEndpointUrlNode(), String.class, null);
  }

  @Override
  public void setEndpointUrl(@Nullable String value) {
    ServerNodeSupport.write(this, getEndpointUrlNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getHistoryReadCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "HistoryReadCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getHistoryReadCount() {
    return ServerNodeSupport.read(
        this, getHistoryReadCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setHistoryReadCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getHistoryReadCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getHistoryUpdateCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "HistoryUpdateCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getHistoryUpdateCount() {
    return ServerNodeSupport.read(
        this, getHistoryUpdateCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setHistoryUpdateCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getHistoryUpdateCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getLocaleIdsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LocaleIds",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 295L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getLocaleIds() {
    return ServerNodeSupport.readArray(this, getLocaleIdsNode(), String.class, null);
  }

  @Override
  public void setLocaleIds(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getLocaleIdsNode(), value, true, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getMaxResponseMessageSizeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxResponseMessageSize",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxResponseMessageSize() {
    return ServerNodeSupport.read(this, getMaxResponseMessageSizeNode(), UInteger.class, null);
  }

  @Override
  public void setMaxResponseMessageSize(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxResponseMessageSizeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getModifyMonitoredItemsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ModifyMonitoredItemsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getModifyMonitoredItemsCount() {
    return ServerNodeSupport.read(
        this, getModifyMonitoredItemsCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getModifyMonitoredItemsCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getModifySubscriptionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ModifySubscriptionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getModifySubscriptionCount() {
    return ServerNodeSupport.read(
        this, getModifySubscriptionCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setModifySubscriptionCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getModifySubscriptionCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getPublishCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getPublishCount() {
    return ServerNodeSupport.read(this, getPublishCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setPublishCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getPublishCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getQueryFirstCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "QueryFirstCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getQueryFirstCount() {
    return ServerNodeSupport.read(
        this, getQueryFirstCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setQueryFirstCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getQueryFirstCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getQueryNextCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "QueryNextCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getQueryNextCount() {
    return ServerNodeSupport.read(
        this, getQueryNextCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setQueryNextCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getQueryNextCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getReadCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ReadCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getReadCount() {
    return ServerNodeSupport.read(this, getReadCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setReadCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getReadCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getRegisterNodesCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RegisterNodesCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getRegisterNodesCount() {
    return ServerNodeSupport.read(
        this, getRegisterNodesCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setRegisterNodesCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getRegisterNodesCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getRepublishCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RepublishCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getRepublishCount() {
    return ServerNodeSupport.read(
        this, getRepublishCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setRepublishCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getRepublishCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getServerUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getServerUri() {
    return ServerNodeSupport.read(this, getServerUriNode(), String.class, null);
  }

  @Override
  public void setServerUri(@Nullable String value) {
    ServerNodeSupport.write(this, getServerUriNode(), value, false, false, false);
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
  public BaseDataVariableTypeNode getSessionNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getSessionName() {
    return ServerNodeSupport.read(this, getSessionNameNode(), String.class, null);
  }

  @Override
  public void setSessionName(@Nullable String value) {
    ServerNodeSupport.write(this, getSessionNameNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSetMonitoringModeCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SetMonitoringModeCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getSetMonitoringModeCount() {
    return ServerNodeSupport.read(
        this, getSetMonitoringModeCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setSetMonitoringModeCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getSetMonitoringModeCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getSetPublishingModeCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SetPublishingModeCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getSetPublishingModeCount() {
    return ServerNodeSupport.read(
        this, getSetPublishingModeCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setSetPublishingModeCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getSetPublishingModeCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getSetTriggeringCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SetTriggeringCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getSetTriggeringCount() {
    return ServerNodeSupport.read(
        this, getSetTriggeringCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setSetTriggeringCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getSetTriggeringCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getTotalRequestCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TotalRequestCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getTotalRequestCount() {
    return ServerNodeSupport.read(
        this, getTotalRequestCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setTotalRequestCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getTotalRequestCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getTransferSubscriptionsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TransferSubscriptionsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getTransferSubscriptionsCount() {
    return ServerNodeSupport.read(
        this, getTransferSubscriptionsCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setTransferSubscriptionsCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getTransferSubscriptionsCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getTranslateBrowsePathsToNodeIdsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "TranslateBrowsePathsToNodeIdsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount() {
    return ServerNodeSupport.read(
        this, getTranslateBrowsePathsToNodeIdsCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(
        this, getTranslateBrowsePathsToNodeIdsCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getUnauthorizedRequestCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UnauthorizedRequestCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getUnauthorizedRequestCount() {
    return ServerNodeSupport.read(this, getUnauthorizedRequestCountNode(), UInteger.class, null);
  }

  @Override
  public void setUnauthorizedRequestCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getUnauthorizedRequestCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getUnregisterNodesCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UnregisterNodesCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getUnregisterNodesCount() {
    return ServerNodeSupport.read(
        this, getUnregisterNodesCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setUnregisterNodesCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getUnregisterNodesCountNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getWriteCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "WriteCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 871L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServiceCounterDataType getWriteCount() {
    return ServerNodeSupport.read(this, getWriteCountNode(), ServiceCounterDataType.class, null);
  }

  @Override
  public void setWriteCount(@Nullable ServiceCounterDataType value) {
    ServerNodeSupport.write(this, getWriteCountNode(), value, false, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getActualSessionTimeoutNode();
    getAddNodesCountNode();
    getAddReferencesCountNode();
    getBrowseCountNode();
    getBrowseNextCountNode();
    getCallCountNode();
    getClientConnectionTimeNode();
    getClientDescriptionNode();
    getClientLastContactTimeNode();
    getCreateMonitoredItemsCountNode();
    getCreateSubscriptionCountNode();
    getCurrentMonitoredItemsCountNode();
    getCurrentPublishRequestsInQueueNode();
    getCurrentSubscriptionsCountNode();
    getDeleteMonitoredItemsCountNode();
    getDeleteNodesCountNode();
    getDeleteReferencesCountNode();
    getDeleteSubscriptionsCountNode();
    getEndpointUrlNode();
    getHistoryReadCountNode();
    getHistoryUpdateCountNode();
    getLocaleIdsNode();
    getMaxResponseMessageSizeNode();
    getModifyMonitoredItemsCountNode();
    getModifySubscriptionCountNode();
    getPublishCountNode();
    getQueryFirstCountNode();
    getQueryNextCountNode();
    getReadCountNode();
    getRegisterNodesCountNode();
    getRepublishCountNode();
    getServerUriNode();
    getSessionIdNode();
    getSessionNameNode();
    getSetMonitoringModeCountNode();
    getSetPublishingModeCountNode();
    getSetTriggeringCountNode();
    getTotalRequestCountNode();
    getTransferSubscriptionsCountNode();
    getTranslateBrowsePathsToNodeIdsCountNode();
    getUnauthorizedRequestCountNode();
    getUnregisterNodesCountNode();
    getWriteCountNode();
  }

  @Override
  public @Nullable SessionDiagnosticsDataType getTypedValue() {
    return ServerNodeSupport.read(this, this, SessionDiagnosticsDataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable SessionDiagnosticsDataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
