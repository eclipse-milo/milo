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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.DataItemListener;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionManager;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionDiagnostics;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SubscriptionDiagnostics;
import org.eclipse.milo.opcua.sdk.server.identity.DefaultUsernameIdentity;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionManager;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
import org.mockito.InOrder;

/** How TransferSubscriptions reports transferred DataItems to the AddressSpace. */
class DefaultSubscriptionServiceSetTransferTest {

  private static final UInteger SUBSCRIPTION_ID = uint(7);

  private final Map<UInteger, BaseMonitoredItem<?>> monitoredItems = new LinkedHashMap<>();

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
  private final Session newSession = mock(Session.class);
  private final Session oldSession = mock(Session.class);
  private final Subscription subscription = mock(Subscription.class);
  private final ServiceRequestContext context = mock(ServiceRequestContext.class);

  private DefaultSubscriptionServiceSet serviceSet;

  @BeforeEach
  void setUp() throws Exception {
    SessionManager sessionManager = mock(SessionManager.class);
    when(sessionManager.getSession(eq(context), any())).thenReturn(newSession);

    when(server.getSessionManager()).thenReturn(sessionManager);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getDataItemListener()).thenReturn(new DataItemListener() {});
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

  /**
   * A component that refreshes read access results from transfer events needs the items to carry
   * the new Session by the time it hears about the transfer, and must hear about it exactly once.
   * Event items have no read access result to refresh and are left out.
   */
  @Test
  void transferReportsTheDataItemsOnceWithTheNewSessionOnThem() throws Exception {
    MonitoredDataItem dataItem = dataItem(uint(1));
    MonitoredEventItem eventItem = eventItem(uint(2));
    monitoredItems.put(dataItem.getId(), dataItem);
    monitoredItems.put(eventItem.getId(), eventItem);

    var deliveries = new ArrayList<List<DataItem>>();
    var sessionsAtDelivery = new ArrayList<Session>();
    doAnswer(
            invocation -> {
              List<DataItem> items = invocation.getArgument(0);
              deliveries.add(List.copyOf(items));
              items.forEach(item -> sessionsAtDelivery.add(item.getSession()));
              return null;
            })
        .when(addressSpaceManager)
        .onDataItemsTransferred(anyList());

    TransferSubscriptionsResponse response =
        serviceSet.onTransferSubscriptions(context, transferRequest());

    assertEquals(StatusCode.GOOD, response.getResults()[0].getStatusCode());
    assertEquals(1, deliveries.size(), "the AddressSpace hears about a transfer once");
    assertEquals(List.of(dataItem), deliveries.get(0), "only data items are reported");
    assertEquals(
        List.of(newSession),
        sessionsAtDelivery,
        "the items carry the new Session when the AddressSpace hears about the transfer");
  }

  /**
   * The server's DataItemListeners hear about the transfer with both Sessions, before the owning
   * AddressSpace does, so a server-level refresher can re-check the items for the new Session
   * without a hook on the AddressSpace.
   */
  @Test
  void transferNotifiesDataItemListenersWithBothSessionsBeforeTheAddressSpace() throws Exception {
    MonitoredDataItem dataItem = dataItem(uint(1));
    monitoredItems.put(dataItem.getId(), dataItem);
    monitoredItems.put(uint(2), eventItem(uint(2)));

    DataItemListener listener = mock(DataItemListener.class);
    when(server.getDataItemListener()).thenReturn(listener);

    serviceSet.onTransferSubscriptions(context, transferRequest());

    InOrder inOrder = inOrder(listener, addressSpaceManager);
    inOrder.verify(listener).onDataItemsTransferred(List.of(dataItem), oldSession, newSession);
    inOrder.verify(addressSpaceManager).onDataItemsTransferred(List.of(dataItem));
  }

  // A transfer the server refuses moves nothing, so there is nothing to report.
  @Test
  void refusedTransferReportsNothing() throws Exception {
    when(oldSession.getIdentity()).thenReturn(new DefaultUsernameIdentity("someone-else"));
    monitoredItems.put(uint(1), dataItem(uint(1)));

    TransferSubscriptionsResponse response =
        serviceSet.onTransferSubscriptions(context, transferRequest());

    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied), response.getResults()[0].getStatusCode());
    verify(addressSpaceManager, never()).onDataItemsTransferred(anyList());
    verify(server, never()).getDataItemListener();
  }

  // Like the other data item callbacks, this one is never delivered with an empty list.
  @Test
  void transferOfOnlyEventItemsReportsNothing() throws Exception {
    monitoredItems.put(uint(2), eventItem(uint(2)));

    TransferSubscriptionsResponse response =
        serviceSet.onTransferSubscriptions(context, transferRequest());

    assertEquals(StatusCode.GOOD, response.getResults()[0].getStatusCode());
    verify(addressSpaceManager, never()).onDataItemsTransferred(anyList());
  }

  private MonitoredDataItem dataItem(UInteger id) {
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
        uint(1),
        true);
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
    RequestHeader header =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);

    return new TransferSubscriptionsRequest(header, new UInteger[] {SUBSCRIPTION_ID}, false);
  }
}
