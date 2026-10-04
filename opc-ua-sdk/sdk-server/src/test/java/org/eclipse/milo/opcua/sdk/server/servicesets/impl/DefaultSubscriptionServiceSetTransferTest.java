/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionManager;
import org.eclipse.milo.opcua.sdk.server.access.AccessControlManager;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.ReadAccessScope;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionDiagnostics;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SubscriptionDiagnostics;
import org.eclipse.milo.opcua.sdk.server.identity.DefaultUsernameIdentity;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingTestItems;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionManager;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/** How TransferSubscriptions reports transferred DataItems to the AddressSpace. */
class DefaultSubscriptionServiceSetTransferTest {

  private static final UInteger SUBSCRIPTION_ID = uint(7);

  private final Map<UInteger, BaseMonitoredItem<?>> monitoredItems = new LinkedHashMap<>();

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessControlManager accessControlManager = mock(AccessControlManager.class);
  private final AccessController accessController = mock(AccessController.class);
  private final AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
  private final Session newSession = mock(Session.class);
  private final Session oldSession = mock(Session.class);
  private final Subscription subscription = mock(Subscription.class);
  private final ServiceRequestContext context = mock(ServiceRequestContext.class);

  private DefaultSubscriptionServiceSet serviceSet;

  @BeforeEach
  void setUp() throws Exception {
    when(server.getAccessControlManager()).thenReturn(accessControlManager);
    when(server.getAccessController()).thenReturn(accessController);
    SessionManager sessionManager = mock(SessionManager.class);
    when(sessionManager.getSession(eq(context), any())).thenReturn(newSession);

    when(server.getSessionManager()).thenReturn(sessionManager);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getSubscriptions()).thenReturn(Map.of(SUBSCRIPTION_ID, subscription));

    for (Session session : List.of(newSession, oldSession)) {
      when(session.getIdentity()).thenReturn(new DefaultUsernameIdentity("user"));
      when(session.getSubscriptionManager()).thenReturn(mock(SubscriptionManager.class));
      when(session.getSessionDiagnostics())
          .thenReturn(mock(SessionDiagnostics.class, RETURNS_DEEP_STUBS));
    }

    when(subscription.getSession()).thenReturn(oldSession);
    when(subscription.getMonitoredItems()).thenReturn(monitoredItems);
    when(subscription.getAvailableSequenceNumbers()).thenReturn(new UInteger[0]);
    when(subscription.getSubscriptionDiagnostics())
        .thenReturn(mock(SubscriptionDiagnostics.class, RETURNS_DEEP_STUBS));

    serviceSet = new DefaultSubscriptionServiceSet(server);
  }

  // The transferred items were checked for the old Session. The cache is keyed by Session, so
  // nothing stale can be read for the new one, but a refresher listening for invalidations has
  // to hear that these Nodes' answers under the new Session are open again.
  @Test
  void transferInvalidatesTheTransferredNodesForTheNewSession() throws Exception {
    MonitoredDataItem dataItem = dataItem(uint(1));
    monitoredItems.put(dataItem.getId(), dataItem);

    serviceSet.onTransferSubscriptions(context, transferRequest());

    ArgumentCaptor<ReadAccessScope> scope = ArgumentCaptor.forClass(ReadAccessScope.class);
    verify(accessControlManager).invalidateReadAccess(scope.capture());
    assertEquals(Optional.of(newSession), scope.getValue().session());
    assertEquals(
        Optional.of(Set.of(dataItem.getReadValueId().getNodeId())), scope.getValue().nodeIds());
  }

  /**
   * Part 4 §5.13.2.1: the last value a transfer re-sends as an initial value was sampled for the
   * old Session. The items are checked for the new Session first, so an item the new Session may
   * not read reports the denial instead of that value.
   */
  @Test
  void transferChecksTheItemsForTheNewSessionBeforeResendingTheLastValue() throws Exception {
    MonitoredDataItem dataItem = publishedDataItem(uint(1), 42);
    monitoredItems.put(dataItem.getId(), dataItem);
    when(accessController.checkReadAccess(eq(newSession), anyList()))
        .thenReturn(Map.of(dataItem.getReadValueId(), AccessResult.DENIED_SECURITY_MODE));

    serviceSet.onTransferSubscriptions(context, transferRequest(true));

    assertEquals(
        List.of(new StatusCode(StatusCodes.Bad_SecurityModeInsufficient)),
        SamplingTestItems.drain(dataItem).stream().map(DataValue::statusCode).toList());
  }

  // The control for the test above: a new Session that may read the item gets the last value.
  @Test
  void transferResendsTheLastValueWhenTheNewSessionMayReadIt() throws Exception {
    MonitoredDataItem dataItem = publishedDataItem(uint(1), 42);
    monitoredItems.put(dataItem.getId(), dataItem);
    when(accessController.checkReadAccess(eq(newSession), anyList()))
        .thenReturn(Map.of(dataItem.getReadValueId(), AccessResult.ALLOWED));

    serviceSet.onTransferSubscriptions(context, transferRequest(true));

    List<DataValue> sent = SamplingTestItems.drain(dataItem);
    assertEquals(1, sent.size());
    assertEquals(42, sent.get(0).value().value());
    verify(accessController, times(1)).checkReadAccess(eq(newSession), anyList());
  }

  /**
   * An identity or endpoint change of the new Session that commits while the transfer checks its
   * items may refresh the new Session before the Subscription is added to it, and the transfer's
   * answers are for the Session as it was. The transfer checks the items again after the move, and
   * the initial values wait for those answers.
   */
  @Test
  void transferOvertakenByAChangeOfTheNewSessionChecksAgainBeforeSendingInitialValues()
      throws Exception {
    MonitoredDataItem dataItem = publishedDataItem(uint(1), 42);
    monitoredItems.put(dataItem.getId(), dataItem);
    var accessEpoch = new AtomicLong();
    when(newSession.getAccessEpoch()).thenAnswer(invocation -> accessEpoch.get());
    when(accessController.checkReadAccess(eq(newSession), anyList()))
        .thenAnswer(
            invocation -> {
              // The change commits during the check, which answers for the Session as it was.
              accessEpoch.incrementAndGet();
              return Map.of(dataItem.getReadValueId(), AccessResult.ALLOWED);
            })
        .thenReturn(Map.of(dataItem.getReadValueId(), AccessResult.DENIED_USER_ACCESS));

    serviceSet.onTransferSubscriptions(context, transferRequest(true));

    assertEquals(AccessResult.DENIED_USER_ACCESS, dataItem.getReadAccessResult());
    assertEquals(
        List.of(new StatusCode(StatusCodes.Bad_UserAccessDenied)),
        SamplingTestItems.drain(dataItem).stream().map(DataValue::statusCode).toList(),
        "the last value checked for the Session as it was is not sent");
  }

  // When the second check fails, the item keeps the answer for the Session as it was, as a failed
  // check keeps the last answer anywhere else, but the last value sampled for that Session is not
  // sent on it.
  @Test
  void transferOvertakenByAChangeWhoseSecondCheckFailsSendsNoInitialValue() throws Exception {
    MonitoredDataItem dataItem = publishedDataItem(uint(1), 42);
    monitoredItems.put(dataItem.getId(), dataItem);
    var accessEpoch = new AtomicLong();
    when(newSession.getAccessEpoch()).thenAnswer(invocation -> accessEpoch.get());
    when(accessController.checkReadAccess(eq(newSession), anyList()))
        .thenAnswer(
            invocation -> {
              accessEpoch.incrementAndGet();
              return Map.of(dataItem.getReadValueId(), AccessResult.ALLOWED);
            })
        .thenThrow(new IllegalStateException("controller failed"));

    TransferSubscriptionsResponse response =
        serviceSet.onTransferSubscriptions(context, transferRequest(true));

    assertEquals(StatusCode.GOOD, requireNonNull(response.getResults())[0].getStatusCode());
    assertTrue(SamplingTestItems.drain(dataItem).isEmpty());
  }

  // Values still queued at transfer time were sampled and checked for the old Session. An item the
  // new Session may not read, for example over a channel that does not meet the Node's
  // AccessRestrictions, drops them, and the client gets the denial in their place.
  @Test
  void transferToASessionThatMayNotReadTheItemDropsItsQueuedValues() throws Exception {
    MonitoredDataItem dataItem = dataItem(uint(1), uint(10));
    dataItem.installFilter(MonitoredDataItem.DEFAULT_FILTER);
    dataItem.setValue(new DataValue(new Variant(1)));
    dataItem.setValue(new DataValue(new Variant(2)));
    monitoredItems.put(dataItem.getId(), dataItem);
    when(accessController.checkReadAccess(eq(newSession), anyList()))
        .thenReturn(Map.of(dataItem.getReadValueId(), AccessResult.DENIED_SECURITY_MODE));

    serviceSet.onTransferSubscriptions(context, transferRequest(false));

    assertEquals(
        List.of(new StatusCode(StatusCodes.Bad_SecurityModeInsufficient)),
        SamplingTestItems.drain(dataItem).stream().map(DataValue::statusCode).toList());
  }

  // A check that throws leaves the items with answers for the old Session only. The transfer is
  // refused before anything moves, so the new Session gets nothing that was checked for the old
  // one, and the Subscription and its queued values stay with the old Session.
  @Test
  void transferWhoseReadAccessCheckThrowsIsRefusedAndMovesNothing() throws Exception {
    MonitoredDataItem dataItem = dataItem(uint(1), uint(10));
    dataItem.installFilter(MonitoredDataItem.DEFAULT_FILTER);
    dataItem.setValue(new DataValue(new Variant(1)));
    monitoredItems.put(dataItem.getId(), dataItem);
    when(accessController.checkReadAccess(eq(newSession), anyList()))
        .thenThrow(new IllegalStateException("controller failed"));

    TransferSubscriptionsResponse response =
        serviceSet.onTransferSubscriptions(context, transferRequest(true));

    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        requireNonNull(response.getResults())[0].getStatusCode());
    assertEquals(oldSession, dataItem.getSession());
    verify(subscription, never()).setSubscriptionManager(any());
    verify(newSession.getSubscriptionManager(), never()).addSubscription(any());
    assertEquals(
        List.of(1),
        SamplingTestItems.drain(dataItem).stream().map(value -> value.value().value()).toList());
  }

  private MonitoredDataItem dataItem(UInteger id) {
    return dataItem(id, uint(1));
  }

  private MonitoredDataItem dataItem(UInteger id, UInteger queueSize) {
    return new MonitoredDataItem(
        server,
        oldSession,
        id,
        SUBSCRIPTION_ID,
        new ReadValueId(new NodeId(2, "variable"), AttributeId.Value.uid(), null, null),
        MonitoringMode.Reporting,
        TimestampsToReturn.Both,
        id,
        100.0,
        queueSize,
        true);
  }

  /** A data item whose last value, {@code value}, was already published to the old Session. */
  private MonitoredDataItem publishedDataItem(UInteger id, int value) throws Exception {
    MonitoredDataItem item = dataItem(id);
    item.installFilter(MonitoredDataItem.DEFAULT_FILTER);
    item.setValue(new DataValue(new Variant(value)));
    SamplingTestItems.drain(item);
    return item;
  }

  private MonitoredEventItem eventItem(UInteger id) {
    return new MonitoredEventItem(
        server,
        oldSession,
        id,
        SUBSCRIPTION_ID,
        new ReadValueId(new NodeId(2, "object"), AttributeId.EventNotifier.uid(), null, null),
        MonitoringMode.Reporting,
        TimestampsToReturn.Both,
        id,
        0.0,
        uint(1),
        true);
  }

  private static TransferSubscriptionsRequest transferRequest() {
    return transferRequest(false);
  }

  private static TransferSubscriptionsRequest transferRequest(boolean sendInitialValues) {
    RequestHeader header =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);

    return new TransferSubscriptionsRequest(
        header, new UInteger[] {SUBSCRIPTION_ID}, sendInitialValues);
  }
}
