/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RevisedDataItemParameters;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.DataItemListener;
import org.eclipse.milo.opcua.sdk.server.EventNotifier;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 * The server's {@link DataItemListener}s hear about every data item before the owning AddressSpace
 * does, so a server-level component can track items without a hook on each AddressSpace.
 */
class SubscriptionManagerDataItemListenerTest {

  private static final UInteger SUBSCRIPTION_ID = uint(1);

  private final Map<UInteger, BaseMonitoredItem<?>> ownedMonitoredItems = new HashMap<>();

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
  private final DataItemListener listener = mock(DataItemListener.class);
  private final Session session = mock(Session.class);
  private final ServiceRequestContext context = mock(ServiceRequestContext.class);
  private final Subscription subscription = mock(Subscription.class);

  private final ReadValueId itemToMonitor =
      new ReadValueId(
          new NodeId(2, "variable"), AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

  private SubscriptionManager manager;

  @BeforeEach
  void setUp() throws Exception {
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(mock(ExecutorService.class));
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});

    AccessController accessController = mock(AccessController.class);
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.ALLOWED));

    when(server.getConfig()).thenReturn(config);
    when(server.getAccessController()).thenReturn(accessController);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getDataItemListener()).thenReturn(listener);
    when(server.getEventNotifier()).thenReturn(mock(EventNotifier.class));
    when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    when(server.getMonitoredItemCount()).thenReturn(new AtomicLong());

    manager = new SubscriptionManager(session, server);

    when(subscription.getId()).thenReturn(SUBSCRIPTION_ID);
    when(subscription.getMonitoredItems()).thenReturn(ownedMonitoredItems);
    when(subscription.nextItemId()).thenReturn(1L);
    doAnswer(
            invocation -> {
              List<BaseMonitoredItem<?>> items = invocation.getArgument(0);
              items.forEach(item -> ownedMonitoredItems.put(item.getId(), item));
              return null;
            })
        .when(subscription)
        .addMonitoredItems(anyList());
    subscriptions(manager).put(SUBSCRIPTION_ID, subscription);

    // NodeClass, EventNotifier, DataType, MinimumSamplingInterval of an Int32 Variable.
    when(addressSpaceManager.read(any(), eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenReturn(
            List.of(
                new DataValue(new Variant(NodeClass.Variable)),
                new DataValue(Variant.NULL_VALUE),
                new DataValue(new Variant(NodeIds.Int32)),
                new DataValue(new Variant(0.0))));
    when(addressSpaceManager.onCreateDataItem(eq(itemToMonitor), any(), any()))
        .thenReturn(new RevisedDataItemParameters(100.0, uint(1)));
  }

  @Test
  void createdItemsReachTheListenersBeforeTheAddressSpace() throws Exception {
    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(createItem()));

    assertEquals(StatusCode.GOOD, response.getResults()[0].getStatusCode());
    List<DataItem> created = List.of((DataItem) ownedMonitoredItems.values().iterator().next());

    InOrder inOrder = inOrder(listener, addressSpaceManager);
    inOrder.verify(listener).onDataItemsCreated(created);
    inOrder.verify(addressSpaceManager).onDataItemsCreated(created);
  }

  @Test
  void deletedItemsReachTheListenersBeforeTheAddressSpace() throws Exception {
    MonitoredDataItem dataItem = dataItem(uint(1));
    ownedMonitoredItems.put(dataItem.getId(), dataItem);

    RequestHeader header = requestHeader();
    manager
        .deleteMonitoredItems(
            context,
            new DeleteMonitoredItemsRequest(
                header, SUBSCRIPTION_ID, new UInteger[] {dataItem.getId()}))
        .get(5, TimeUnit.SECONDS);

    InOrder inOrder = inOrder(listener, addressSpaceManager);
    inOrder.verify(listener).onDataItemsDeleted(List.of(dataItem));
    inOrder.verify(addressSpaceManager).onDataItemsDeleted(List.of(dataItem));
  }

  // The listener is about data items; event items in the same SetMonitoringMode are left out of
  // its notification but still reach the AddressSpace.
  @Test
  void monitoringModeChangesReachTheListenersWithDataItemsOnly() throws Exception {
    MonitoredDataItem dataItem = dataItem(uint(1));
    MonitoredEventItem eventItem = eventItem(uint(2));
    ownedMonitoredItems.put(dataItem.getId(), dataItem);
    ownedMonitoredItems.put(eventItem.getId(), eventItem);

    manager
        .setMonitoringMode(
            context,
            new SetMonitoringModeRequest(
                requestHeader(),
                SUBSCRIPTION_ID,
                MonitoringMode.Disabled,
                new UInteger[] {dataItem.getId(), eventItem.getId()}))
        .get(5, TimeUnit.SECONDS);

    assertEquals(MonitoringMode.Disabled, dataItem.getMonitoringMode());

    InOrder inOrder = inOrder(listener, addressSpaceManager);
    inOrder.verify(listener).onMonitoringModeChanged(List.of(dataItem));
    inOrder.verify(addressSpaceManager).onMonitoringModeChanged(List.of(dataItem, eventItem));
  }

  private MonitoredDataItem dataItem(UInteger id) {
    return new MonitoredDataItem(
        server,
        session,
        id,
        SUBSCRIPTION_ID,
        itemToMonitor,
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
        session,
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

  private MonitoredItemCreateRequest createItem() {
    return new MonitoredItemCreateRequest(
        itemToMonitor,
        MonitoringMode.Reporting,
        new MonitoringParameters(uint(1), 100.0, null, uint(1), true));
  }

  private static CreateMonitoredItemsRequest createRequest(
      MonitoredItemCreateRequest... itemsToCreate) {

    return new CreateMonitoredItemsRequest(
        requestHeader(), SUBSCRIPTION_ID, TimestampsToReturn.Both, itemsToCreate);
  }

  private static RequestHeader requestHeader() {
    return new RequestHeader(
        NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
  }

  @SuppressWarnings("unchecked")
  private static Map<UInteger, Subscription> subscriptions(SubscriptionManager manager)
      throws ReflectiveOperationException {

    Field field = SubscriptionManager.class.getDeclaredField("subscriptions");
    field.setAccessible(true);
    return (Map<UInteger, Subscription>) field.get(manager);
  }
}
