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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RevisedEventItemParameters;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class SubscriptionManagerQuotaTest {

  @Test
  void failedCreationDoesNotLeakMonitoredItemQuota() throws Exception {
    UInteger subscriptionId = uint(1);
    AtomicLong globalMonitoredItemCount = new AtomicLong();

    OpcUaServerConfigLimits limits =
        new OpcUaServerConfigLimits() {
          @Override
          public UInteger getMaxMonitoredItems() {
            return uint(2);
          }

          @Override
          public UInteger getMaxMonitoredItemsPerSession() {
            return uint(2);
          }
        };

    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(mock(ExecutorService.class));
    when(config.getLimits()).thenReturn(limits);

    AccessController accessController = mock(AccessController.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
    Session session = mock(Session.class);
    OpcUaServer server = mock(OpcUaServer.class);
    when(server.getConfig()).thenReturn(config);
    when(server.getAccessController()).thenReturn(accessController);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    when(server.getMonitoredItemCount()).thenReturn(globalMonitoredItemCount);

    SubscriptionManager manager = new SubscriptionManager(session, server);
    Subscription subscription = mock(Subscription.class);
    Map<UInteger, BaseMonitoredItem<?>> ownedMonitoredItems = new HashMap<>();
    when(subscription.getId()).thenReturn(subscriptionId);
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
    doAnswer(
            invocation -> {
              List<BaseMonitoredItem<?>> items = invocation.getArgument(0);
              items.forEach(item -> ownedMonitoredItems.remove(item.getId()));
              return null;
            })
        .when(subscription)
        .removeMonitoredItems(anyList());
    subscriptions(manager).put(subscriptionId, subscription);

    NodeId nodeId = new NodeId(2, "event-source");
    ReadValueId itemToMonitor =
        new ReadValueId(nodeId, AttributeId.EventNotifier.uid(), null, QualifiedName.NULL_VALUE);
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

    var selectClause =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "Message")},
            AttributeId.Value.uid(),
            null);
    ExtensionObject validFilter =
        ExtensionObject.encode(
            DefaultEncodingContext.INSTANCE,
            new EventFilter(new SimpleAttributeOperand[] {selectClause}, new ContentFilter(null)));
    ExtensionObject invalidFilter =
        ExtensionObject.of(ByteString.of(new byte[] {0}), new NodeId(2, 999));
    MonitoringParameters validParameters =
        new MonitoringParameters(uint(1), 0.0, validFilter, uint(1), true);
    MonitoringParameters invalidParameters =
        new MonitoringParameters(uint(1), 0.0, invalidFilter, uint(1), true);
    MonitoredItemCreateRequest validItem =
        new MonitoredItemCreateRequest(itemToMonitor, MonitoringMode.Reporting, validParameters);
    MonitoredItemCreateRequest invalidItem =
        new MonitoredItemCreateRequest(itemToMonitor, MonitoringMode.Reporting, invalidParameters);
    RequestHeader header =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
    CreateMonitoredItemsRequest request =
        new CreateMonitoredItemsRequest(
            header,
            subscriptionId,
            TimestampsToReturn.Both,
            new MonitoredItemCreateRequest[] {validItem, invalidItem});
    ServiceRequestContext context = mock(ServiceRequestContext.class);

    assertThrows(
        UaSerializationException.class, () -> manager.createMonitoredItems(context, request));
    assertEquals(0, globalMonitoredItemCount.get());
    assertEquals(0, monitoredItemCount(manager).get());
    assertEquals(0, ownedMonitoredItems.size());

    EncodingContext failingEncodingContext = mock(EncodingContext.class);
    when(failingEncodingContext.getNamespaceTable())
        .thenThrow(new IllegalStateException("post-append result failure"));
    when(server.getStaticEncodingContext()).thenReturn(failingEncodingContext);

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class, () -> manager.createMonitoredItems(context, request));
    assertEquals("post-append result failure", exception.getMessage());
    assertEquals(0, globalMonitoredItemCount.get());
    assertEquals(0, monitoredItemCount(manager).get());
    assertEquals(0, ownedMonitoredItems.size());
  }

  @SuppressWarnings("unchecked")
  private static Map<UInteger, Subscription> subscriptions(SubscriptionManager manager)
      throws ReflectiveOperationException {

    Field field = SubscriptionManager.class.getDeclaredField("subscriptions");
    field.setAccessible(true);
    return (Map<UInteger, Subscription>) field.get(manager);
  }

  private static AtomicLong monitoredItemCount(SubscriptionManager manager)
      throws ReflectiveOperationException {

    Field field = SubscriptionManager.class.getDeclaredField("monitoredItemCount");
    field.setAccessible(true);
    return (AtomicLong) field.get(manager);
  }
}
