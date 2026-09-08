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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RevisedEventItemParameters;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.EventNotifier;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElementResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the handling of event items whose filter validates with errors, through the
 * CreateMonitoredItems registration path and the SetMonitoringMode registration toggle.
 */
class SubscriptionManagerEventItemTest {

  private static final UInteger SUBSCRIPTION_ID = uint(1);

  private final Map<UInteger, BaseMonitoredItem<?>> ownedMonitoredItems = new HashMap<>();

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessController accessController = mock(AccessController.class);
  private final AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
  private final EventNotifier eventNotifier = mock(EventNotifier.class);
  private final Session session = mock(Session.class);
  private final ServiceRequestContext context = mock(ServiceRequestContext.class);

  private final ReadValueId itemToMonitor =
      new ReadValueId(
          NodeIds.Server, AttributeId.EventNotifier.uid(), null, QualifiedName.NULL_VALUE);

  private SubscriptionManager manager;

  @BeforeEach
  void setUp() throws Exception {
    OpcUaServerConfigLimits limits =
        new OpcUaServerConfigLimits() {
          @Override
          public UInteger getMaxMonitoredItems() {
            return uint(100);
          }

          @Override
          public UInteger getMaxMonitoredItemsPerSession() {
            return uint(100);
          }
        };

    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(mock(ExecutorService.class));
    when(config.getLimits()).thenReturn(limits);

    when(server.getConfig()).thenReturn(config);
    when(server.getAccessController()).thenReturn(accessController);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getEventNotifier()).thenReturn(eventNotifier);
    when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    when(server.getMonitoredItemCount()).thenReturn(new AtomicLong());

    manager = new SubscriptionManager(session, server);

    Subscription subscription = mock(Subscription.class);
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

    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(Map.of(itemToMonitor, AccessResult.ALLOWED));
    when(addressSpaceManager.read(any(), eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenReturn(
            List.of(
                new DataValue(new Variant(NodeClass.Object)),
                new DataValue(new Variant(ubyte(1))),
                new DataValue(new Variant(NodeIds.BaseDataType)),
                new DataValue(new Variant(0.0))));
    when(addressSpaceManager.onCreateEventItem(itemToMonitor, uint(1)))
        .thenReturn(new RevisedEventItemParameters(uint(1)));
  }

  // A filter that validates with an operand error is reported in the EventFilterResult of a Good
  // item result; the item is registered with the EventNotifier like any other but delivers nothing.
  @Test
  void itemWithRejectedFilterOperandIsCreatedRegisteredAndDeliversNothing() throws Exception {
    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(createItem(badOperandEventFilter())));

    MonitoredItemCreateResult result = response.getResults()[0];
    assertEquals(StatusCode.GOOD, result.getStatusCode());

    EventFilterResult filterResult =
        (EventFilterResult) result.getFilterResult().decode(DefaultEncodingContext.INSTANCE);
    ContentFilterElementResult elementResult =
        filterResult.getWhereClauseResult().getElementResults()[0];
    assertEquals(
        StatusCodes.Bad_FilterOperandInvalid, elementResult.getOperandStatusCodes()[0].value());

    MonitoredEventItem item =
        assertInstanceOf(
            MonitoredEventItem.class, ownedMonitoredItems.get(result.getMonitoredItemId()));
    verify(eventNotifier).register(item);

    item.onEvent(mock(BaseEventTypeNode.class));

    var notifications = new ArrayList<UaStructuredType>();
    item.getNotifications(notifications, Integer.MAX_VALUE);
    assertEquals(0, notifications.size());
  }

  // SetMonitoringMode toggles EventNotifier registration by sampling state regardless of the
  // filter's validation result.
  @Test
  void monitoringModeTogglesRegistrationOfItemWithRejectedFilterOperand() throws Exception {
    CreateMonitoredItemsResponse response =
        manager.createMonitoredItems(context, createRequest(createItem(badOperandEventFilter())));
    UInteger itemId = response.getResults()[0].getMonitoredItemId();
    MonitoredEventItem item =
        assertInstanceOf(MonitoredEventItem.class, ownedMonitoredItems.get(itemId));

    manager
        .setMonitoringMode(context, setMonitoringModeRequest(MonitoringMode.Disabled, itemId))
        .get();
    verify(eventNotifier).unregister(item);

    manager
        .setMonitoringMode(context, setMonitoringModeRequest(MonitoringMode.Reporting, itemId))
        .get();
    verify(eventNotifier, times(2)).register(item);
  }

  private static ExtensionObject badOperandEventFilter() {
    var selectClause =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "Message")},
            AttributeId.Value.uid(),
            null);

    ExtensionObject notAFilterOperand =
        ExtensionObject.encode(
            DefaultEncodingContext.INSTANCE,
            new ReadValueId(NodeIds.Server, AttributeId.Value.uid(), null, null));

    var whereClause =
        new ContentFilter(
            new ContentFilterElement[] {
              new ContentFilterElement(
                  FilterOperator.IsNull, new ExtensionObject[] {notAFilterOperand})
            });

    return ExtensionObject.encode(
        DefaultEncodingContext.INSTANCE,
        new EventFilter(new SimpleAttributeOperand[] {selectClause}, whereClause));
  }

  private MonitoredItemCreateRequest createItem(ExtensionObject filter) {
    return new MonitoredItemCreateRequest(
        itemToMonitor,
        MonitoringMode.Reporting,
        new MonitoringParameters(uint(1), 0.0, filter, uint(1), true));
  }

  private static CreateMonitoredItemsRequest createRequest(
      MonitoredItemCreateRequest... itemsToCreate) {

    return new CreateMonitoredItemsRequest(
        requestHeader(), SUBSCRIPTION_ID, TimestampsToReturn.Both, itemsToCreate);
  }

  private static SetMonitoringModeRequest setMonitoringModeRequest(
      MonitoringMode monitoringMode, UInteger... itemIds) {

    return new SetMonitoringModeRequest(requestHeader(), SUBSCRIPTION_ID, monitoringMode, itemIds);
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
