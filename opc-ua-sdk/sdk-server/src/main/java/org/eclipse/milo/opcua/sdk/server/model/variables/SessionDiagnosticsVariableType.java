package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SessionDiagnosticsVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.14">Model
 *     documentation</a>
 */
public interface SessionDiagnosticsVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2197L);

  /**
   * Returns the mandatory ActualSessionTimeout child, a BaseDataVariableType with DataType
   * Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getActualSessionTimeoutNode();

  /**
   * Returns the Value of the ActualSessionTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getActualSessionTimeout();

  /**
   * Sets the Value of the ActualSessionTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActualSessionTimeout(@Nullable Double value);

  /**
   * Returns the mandatory AddNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getAddNodesCountNode();

  /**
   * Returns the Value of the AddNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getAddNodesCount();

  /**
   * Sets the Value of the AddNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAddNodesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory AddReferencesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getAddReferencesCountNode();

  /**
   * Returns the Value of the AddReferencesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getAddReferencesCount();

  /**
   * Sets the Value of the AddReferencesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAddReferencesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory BrowseCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getBrowseCountNode();

  /**
   * Returns the Value of the BrowseCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getBrowseCount();

  /**
   * Sets the Value of the BrowseCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBrowseCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory BrowseNextCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getBrowseNextCountNode();

  /**
   * Returns the Value of the BrowseNextCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getBrowseNextCount();

  /**
   * Sets the Value of the BrowseNextCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBrowseNextCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory CallCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCallCountNode();

  /**
   * Returns the Value of the CallCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getCallCount();

  /**
   * Sets the Value of the CallCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCallCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory ClientConnectionTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getClientConnectionTimeNode();

  /**
   * Returns the Value of the ClientConnectionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getClientConnectionTime();

  /**
   * Sets the Value of the ClientConnectionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientConnectionTime(@Nullable DateTime value);

  /**
   * Returns the mandatory ClientDescription child, a BaseDataVariableType with DataType
   * ApplicationDescription.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getClientDescriptionNode();

  /**
   * Returns the Value of the ClientDescription child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ApplicationDescription getClientDescription();

  /**
   * Sets the Value of the ClientDescription child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientDescription(@Nullable ApplicationDescription value);

  /**
   * Returns the mandatory ClientLastContactTime child, a BaseDataVariableType with DataType
   * UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getClientLastContactTimeNode();

  /**
   * Returns the Value of the ClientLastContactTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getClientLastContactTime();

  /**
   * Sets the Value of the ClientLastContactTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientLastContactTime(@Nullable DateTime value);

  /**
   * Returns the mandatory CreateMonitoredItemsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCreateMonitoredItemsCountNode();

  /**
   * Returns the Value of the CreateMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getCreateMonitoredItemsCount();

  /**
   * Sets the Value of the CreateMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCreateMonitoredItemsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory CreateSubscriptionCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCreateSubscriptionCountNode();

  /**
   * Returns the Value of the CreateSubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getCreateSubscriptionCount();

  /**
   * Sets the Value of the CreateSubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCreateSubscriptionCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory CurrentMonitoredItemsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentMonitoredItemsCountNode();

  /**
   * Returns the Value of the CurrentMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentMonitoredItemsCount();

  /**
   * Sets the Value of the CurrentMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentMonitoredItemsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory CurrentPublishRequestsInQueue child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentPublishRequestsInQueueNode();

  /**
   * Returns the Value of the CurrentPublishRequestsInQueue child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentPublishRequestsInQueue();

  /**
   * Sets the Value of the CurrentPublishRequestsInQueue child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentPublishRequestsInQueue(@Nullable UInteger value);

  /**
   * Returns the mandatory CurrentSubscriptionsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getCurrentSubscriptionsCountNode();

  /**
   * Returns the Value of the CurrentSubscriptionsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getCurrentSubscriptionsCount();

  /**
   * Sets the Value of the CurrentSubscriptionsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentSubscriptionsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory DeleteMonitoredItemsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDeleteMonitoredItemsCountNode();

  /**
   * Returns the Value of the DeleteMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getDeleteMonitoredItemsCount();

  /**
   * Sets the Value of the DeleteMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeleteMonitoredItemsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory DeleteNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDeleteNodesCountNode();

  /**
   * Returns the Value of the DeleteNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getDeleteNodesCount();

  /**
   * Sets the Value of the DeleteNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeleteNodesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory DeleteReferencesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDeleteReferencesCountNode();

  /**
   * Returns the Value of the DeleteReferencesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getDeleteReferencesCount();

  /**
   * Sets the Value of the DeleteReferencesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeleteReferencesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory DeleteSubscriptionsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDeleteSubscriptionsCountNode();

  /**
   * Returns the Value of the DeleteSubscriptionsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getDeleteSubscriptionsCount();

  /**
   * Sets the Value of the DeleteSubscriptionsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeleteSubscriptionsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory EndpointUrl child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getEndpointUrlNode();

  /**
   * Returns the Value of the EndpointUrl child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getEndpointUrl();

  /**
   * Sets the Value of the EndpointUrl child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndpointUrl(@Nullable String value);

  /**
   * Returns the mandatory HistoryReadCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getHistoryReadCountNode();

  /**
   * Returns the Value of the HistoryReadCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getHistoryReadCount();

  /**
   * Sets the Value of the HistoryReadCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHistoryReadCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory HistoryUpdateCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getHistoryUpdateCountNode();

  /**
   * Returns the Value of the HistoryUpdateCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getHistoryUpdateCount();

  /**
   * Sets the Value of the HistoryUpdateCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHistoryUpdateCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory LocaleIds child, a BaseDataVariableType with DataType LocaleId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLocaleIdsNode();

  /**
   * Returns the Value of the LocaleIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getLocaleIds();

  /**
   * Sets the Value of the LocaleIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLocaleIds(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory MaxResponseMessageSize child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxResponseMessageSizeNode();

  /**
   * Returns the Value of the MaxResponseMessageSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxResponseMessageSize();

  /**
   * Sets the Value of the MaxResponseMessageSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxResponseMessageSize(@Nullable UInteger value);

  /**
   * Returns the mandatory ModifyMonitoredItemsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getModifyMonitoredItemsCountNode();

  /**
   * Returns the Value of the ModifyMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getModifyMonitoredItemsCount();

  /**
   * Sets the Value of the ModifyMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setModifyMonitoredItemsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory ModifySubscriptionCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getModifySubscriptionCountNode();

  /**
   * Returns the Value of the ModifySubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getModifySubscriptionCount();

  /**
   * Sets the Value of the ModifySubscriptionCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setModifySubscriptionCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory PublishCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPublishCountNode();

  /**
   * Returns the Value of the PublishCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getPublishCount();

  /**
   * Sets the Value of the PublishCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory QueryFirstCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getQueryFirstCountNode();

  /**
   * Returns the Value of the QueryFirstCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getQueryFirstCount();

  /**
   * Sets the Value of the QueryFirstCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setQueryFirstCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory QueryNextCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getQueryNextCountNode();

  /**
   * Returns the Value of the QueryNextCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getQueryNextCount();

  /**
   * Sets the Value of the QueryNextCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setQueryNextCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory ReadCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getReadCountNode();

  /**
   * Returns the Value of the ReadCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getReadCount();

  /**
   * Sets the Value of the ReadCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReadCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory RegisterNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRegisterNodesCountNode();

  /**
   * Returns the Value of the RegisterNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getRegisterNodesCount();

  /**
   * Sets the Value of the RegisterNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRegisterNodesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory RepublishCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRepublishCountNode();

  /**
   * Returns the Value of the RepublishCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getRepublishCount();

  /**
   * Sets the Value of the RepublishCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRepublishCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory ServerUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getServerUriNode();

  /**
   * Returns the Value of the ServerUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getServerUri();

  /**
   * Sets the Value of the ServerUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerUri(@Nullable String value);

  /**
   * Returns the mandatory SessionId child, a BaseDataVariableType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSessionIdNode();

  /**
   * Returns the Value of the SessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getSessionId();

  /**
   * Sets the Value of the SessionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionId(@Nullable NodeId value);

  /**
   * Returns the mandatory SessionName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSessionNameNode();

  /**
   * Returns the Value of the SessionName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSessionName();

  /**
   * Sets the Value of the SessionName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSessionName(@Nullable String value);

  /**
   * Returns the mandatory SetMonitoringModeCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSetMonitoringModeCountNode();

  /**
   * Returns the Value of the SetMonitoringModeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getSetMonitoringModeCount();

  /**
   * Sets the Value of the SetMonitoringModeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSetMonitoringModeCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory SetPublishingModeCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSetPublishingModeCountNode();

  /**
   * Returns the Value of the SetPublishingModeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getSetPublishingModeCount();

  /**
   * Sets the Value of the SetPublishingModeCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSetPublishingModeCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory SetTriggeringCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSetTriggeringCountNode();

  /**
   * Returns the Value of the SetTriggeringCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getSetTriggeringCount();

  /**
   * Sets the Value of the SetTriggeringCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSetTriggeringCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory TotalRequestCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTotalRequestCountNode();

  /**
   * Returns the Value of the TotalRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getTotalRequestCount();

  /**
   * Sets the Value of the TotalRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTotalRequestCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory TransferSubscriptionsCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTransferSubscriptionsCountNode();

  /**
   * Returns the Value of the TransferSubscriptionsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getTransferSubscriptionsCount();

  /**
   * Sets the Value of the TransferSubscriptionsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransferSubscriptionsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory TranslateBrowsePathsToNodeIdsCount child, a BaseDataVariableType with
   * DataType ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTranslateBrowsePathsToNodeIdsCountNode();

  /**
   * Returns the Value of the TranslateBrowsePathsToNodeIdsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getTranslateBrowsePathsToNodeIdsCount();

  /**
   * Sets the Value of the TranslateBrowsePathsToNodeIdsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTranslateBrowsePathsToNodeIdsCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory UnauthorizedRequestCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getUnauthorizedRequestCountNode();

  /**
   * Returns the Value of the UnauthorizedRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getUnauthorizedRequestCount();

  /**
   * Sets the Value of the UnauthorizedRequestCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUnauthorizedRequestCount(@Nullable UInteger value);

  /**
   * Returns the mandatory UnregisterNodesCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getUnregisterNodesCountNode();

  /**
   * Returns the Value of the UnregisterNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getUnregisterNodesCount();

  /**
   * Sets the Value of the UnregisterNodesCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUnregisterNodesCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns the mandatory WriteCount child, a BaseDataVariableType with DataType
   * ServiceCounterDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getWriteCountNode();

  /**
   * Returns the Value of the WriteCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServiceCounterDataType getWriteCount();

  /**
   * Sets the Value of the WriteCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setWriteCount(@Nullable ServiceCounterDataType value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable SessionDiagnosticsDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable SessionDiagnosticsDataType value);
}
