/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.concurrent.atomic.LongAdder;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;

public class SessionDiagnostics {

  private final ServiceCounter totalRequestCount = new ServiceCounter();
  private final LongAdder unauthorizedRequestCount = new LongAdder();
  private final ServiceCounter readCount = new ServiceCounter();
  private final ServiceCounter historyReadCount = new ServiceCounter();
  private final ServiceCounter writeCount = new ServiceCounter();
  private final ServiceCounter historyUpdateCount = new ServiceCounter();
  private final ServiceCounter callCount = new ServiceCounter();
  private final ServiceCounter createMonitoredItemsCount = new ServiceCounter();
  private final ServiceCounter modifyMonitoredItemsCount = new ServiceCounter();
  private final ServiceCounter setMonitoringModeCount = new ServiceCounter();
  private final ServiceCounter setTriggeringCount = new ServiceCounter();
  private final ServiceCounter deleteMonitoredItemsCount = new ServiceCounter();
  private final ServiceCounter createSubscriptionCount = new ServiceCounter();
  private final ServiceCounter modifySubscriptionCount = new ServiceCounter();
  private final ServiceCounter setPublishingModeCount = new ServiceCounter();
  private final ServiceCounter publishCount = new ServiceCounter();
  private final ServiceCounter republishCount = new ServiceCounter();
  private final ServiceCounter transferSubscriptionsCount = new ServiceCounter();
  private final ServiceCounter deleteSubscriptionsCount = new ServiceCounter();
  private final ServiceCounter addNodesCount = new ServiceCounter();
  private final ServiceCounter addReferencesCount = new ServiceCounter();
  private final ServiceCounter deleteNodesCount = new ServiceCounter();
  private final ServiceCounter deleteReferencesCount = new ServiceCounter();
  private final ServiceCounter browseCount = new ServiceCounter();
  private final ServiceCounter browseNextCount = new ServiceCounter();
  private final ServiceCounter translateBrowsePathsToNodeIdsCount = new ServiceCounter();
  private final ServiceCounter queryFirstCount = new ServiceCounter();
  private final ServiceCounter queryNextCount = new ServiceCounter();
  private final ServiceCounter registerNodesCount = new ServiceCounter();
  private final ServiceCounter unregisterNodesCount = new ServiceCounter();

  private final Session session;

  public SessionDiagnostics(Session session) {
    this.session = session;
  }

  public NodeId getSessionId() {
    return session.getSessionId();
  }

  public String getSessionName() {
    return session.getSessionName();
  }

  public ApplicationDescription getClientDescription() {
    return session.getClientDescription();
  }

  public String getServerUri() {
    return session.getServerUri();
  }

  public String getEndpointUrl() {
    return session.getEndpoint().getEndpointUrl();
  }

  public String[] getLocaleIds() {
    return session.getLocaleIds();
  }

  public Double getActualSessionTimeout() {
    return session.getSessionTimeout();
  }

  public UInteger getMaxResponseMessageSize() {
    return session.getMaxResponseMessageSize();
  }

  public DateTime getClientConnectionTime() {
    return session.getConnectionTime();
  }

  public DateTime getClientLastContactTime() {
    return session.getLastContactTime();
  }

  public UInteger getCurrentSubscriptionsCount() {
    return uint(session.getSubscriptionManager().getSubscriptions().size());
  }

  public UInteger getCurrentMonitoredItemsCount() {
    return uint(
        session.getSubscriptionManager().getSubscriptions().stream()
            .map(s -> s.getMonitoredItems().size())
            .reduce(Integer::sum)
            .orElse(0));
  }

  public UInteger getCurrentPublishRequestsInQueue() {
    return uint(session.getSubscriptionManager().getPublishQueue().size());
  }

  public ServiceCounter getTotalRequestCount() {
    return totalRequestCount;
  }

  public LongAdder getUnauthorizedRequestCount() {
    return unauthorizedRequestCount;
  }

  public ServiceCounter getReadCount() {
    return readCount;
  }

  public ServiceCounter getHistoryReadCount() {
    return historyReadCount;
  }

  public ServiceCounter getWriteCount() {
    return writeCount;
  }

  public ServiceCounter getHistoryUpdateCount() {
    return historyUpdateCount;
  }

  public ServiceCounter getCallCount() {
    return callCount;
  }

  public ServiceCounter getCreateMonitoredItemsCount() {
    return createMonitoredItemsCount;
  }

  public ServiceCounter getModifyMonitoredItemsCount() {
    return modifyMonitoredItemsCount;
  }

  public ServiceCounter getSetMonitoringModeCount() {
    return setMonitoringModeCount;
  }

  public ServiceCounter getSetTriggeringCount() {
    return setTriggeringCount;
  }

  public ServiceCounter getDeleteMonitoredItemsCount() {
    return deleteMonitoredItemsCount;
  }

  public ServiceCounter getCreateSubscriptionCount() {
    return createSubscriptionCount;
  }

  public ServiceCounter getModifySubscriptionCount() {
    return modifySubscriptionCount;
  }

  public ServiceCounter getSetPublishingModeCount() {
    return setPublishingModeCount;
  }

  public ServiceCounter getPublishCount() {
    return publishCount;
  }

  public ServiceCounter getRepublishCount() {
    return republishCount;
  }

  public ServiceCounter getTransferSubscriptionsCount() {
    return transferSubscriptionsCount;
  }

  public ServiceCounter getDeleteSubscriptionsCount() {
    return deleteSubscriptionsCount;
  }

  public ServiceCounter getAddNodesCount() {
    return addNodesCount;
  }

  public ServiceCounter getAddReferencesCount() {
    return addReferencesCount;
  }

  public ServiceCounter getDeleteNodesCount() {
    return deleteNodesCount;
  }

  public ServiceCounter getDeleteReferencesCount() {
    return deleteReferencesCount;
  }

  public ServiceCounter getBrowseCount() {
    return browseCount;
  }

  public ServiceCounter getBrowseNextCount() {
    return browseNextCount;
  }

  public ServiceCounter getTranslateBrowsePathsToNodeIdsCount() {
    return translateBrowsePathsToNodeIdsCount;
  }

  public ServiceCounter getQueryFirstCount() {
    return queryFirstCount;
  }

  public ServiceCounter getQueryNextCount() {
    return queryNextCount;
  }

  public ServiceCounter getRegisterNodesCount() {
    return registerNodesCount;
  }

  public ServiceCounter getUnregisterNodesCount() {
    return unregisterNodesCount;
  }

  /**
   * @return a {@link SessionDiagnosticsDataType} containing the most current values.
   */
  public SessionDiagnosticsDataType getSessionDiagnosticsDataType() {
    return new SessionDiagnosticsDataType(
        getSessionId(),
        getSessionName(),
        getClientDescription(),
        getServerUri(),
        getEndpointUrl(),
        getLocaleIds(),
        getActualSessionTimeout(),
        getMaxResponseMessageSize(),
        getClientConnectionTime(),
        getClientLastContactTime(),
        getCurrentSubscriptionsCount(),
        getCurrentMonitoredItemsCount(),
        getCurrentPublishRequestsInQueue(),
        getTotalRequestCount().getServiceCounter(),
        uint(getUnauthorizedRequestCount().sum()),
        getReadCount().getServiceCounter(),
        getHistoryReadCount().getServiceCounter(),
        getWriteCount().getServiceCounter(),
        getHistoryUpdateCount().getServiceCounter(),
        getCallCount().getServiceCounter(),
        getCreateMonitoredItemsCount().getServiceCounter(),
        getModifyMonitoredItemsCount().getServiceCounter(),
        getSetMonitoringModeCount().getServiceCounter(),
        getSetTriggeringCount().getServiceCounter(),
        getDeleteMonitoredItemsCount().getServiceCounter(),
        getCreateSubscriptionCount().getServiceCounter(),
        getModifySubscriptionCount().getServiceCounter(),
        getSetPublishingModeCount().getServiceCounter(),
        getPublishCount().getServiceCounter(),
        getRepublishCount().getServiceCounter(),
        getTransferSubscriptionsCount().getServiceCounter(),
        getDeleteSubscriptionsCount().getServiceCounter(),
        getAddNodesCount().getServiceCounter(),
        getAddReferencesCount().getServiceCounter(),
        getDeleteNodesCount().getServiceCounter(),
        getDeleteReferencesCount().getServiceCounter(),
        getBrowseCount().getServiceCounter(),
        getBrowseNextCount().getServiceCounter(),
        getTranslateBrowsePathsToNodeIdsCount().getServiceCounter(),
        getQueryFirstCount().getServiceCounter(),
        getQueryNextCount().getServiceCounter(),
        getRegisterNodesCount().getServiceCounter(),
        getUnregisterNodesCount().getServiceCounter());
  }
}
