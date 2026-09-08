/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_SubscriptionIdInvalid;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.junit.jupiter.api.Test;

public class ConditionRefreshMethodTest extends AbstractClientServerTest {

  /**
   * ConditionRefresh fires the RefreshStart and RefreshEnd markers through the server's
   * EventNotifier. A listener registered before the subscription's item that throws must not
   * prevent either marker from reaching the item, nor fail the method call.
   */
  @Test
  void markersAreDeliveredWhenAnEarlierListenerThrows() throws Exception {
    EventListener throwingListener =
        event -> {
          throw new IllegalStateException("listener failure");
        };
    server.getEventNotifier().register(throwingListener);

    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      // The test namespace fires its own BaseEventType events; only the markers are collected.
      var markerTypes = new CopyOnWriteArrayList<NodeId>();
      var latch = new CountDownLatch(2);

      OpcUaMonitoredItem item = OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter());
      item.setQueueSize(uint(100));
      item.setEventValueListener(
          (monitoredItem, eventFields) -> {
            if (eventFields[0].value() instanceof NodeId eventType
                && (NodeIds.RefreshStartEventType.equals(eventType)
                    || NodeIds.RefreshEndEventType.equals(eventType))) {
              markerTypes.add(eventType);
              latch.countDown();
            }
          });
      subscription.addMonitoredItem(item);
      subscription.synchronizeMonitoredItems();
      assertTrue(item.getCreateResult().orElseThrow().isGood());

      var request =
          new CallMethodRequest(
              NodeIds.ConditionType,
              NodeIds.ConditionType_ConditionRefresh,
              new Variant[] {new Variant(subscription.getSubscriptionId().orElseThrow())});

      CallResponse response = client.call(List.of(request));
      CallMethodResult result = requireNonNull(response.getResults())[0];

      assertTrue(result.getStatusCode().isGood(), () -> "status: " + result.getStatusCode());
      assertTrue(latch.await(5, TimeUnit.SECONDS), "markers were not delivered");
      assertEquals(
          List.of(NodeIds.RefreshStartEventType, NodeIds.RefreshEndEventType), markerTypes);
    } finally {
      subscription.delete();
      server.getEventNotifier().unregister(throwingListener);
    }
  }

  private static EventFilter eventFilter() {
    SimpleAttributeOperand eventType =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "EventType")},
            AttributeId.Value.uid(),
            null);

    return new EventFilter(new SimpleAttributeOperand[] {eventType}, new ContentFilter(null));
  }

  @Test
  void subscriptionIdInvalid() throws UaException {
    var request =
        new CallMethodRequest(
            NodeIds.ConditionType,
            NodeIds.ConditionType_ConditionRefresh,
            new Variant[] {new Variant(UInteger.valueOf(0))});

    CallResponse response = client.call(List.of(request));
    CallMethodResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCode.of(Bad_SubscriptionIdInvalid), result.getStatusCode());
  }
}
