/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests that event delivery to a monitored item is independent of other registered listeners. */
public class EventDeliveryIsolationTest extends AbstractClientServerTest {

  private final EventListener throwingListener =
      event -> {
        throw new IllegalStateException("listener failure");
      };

  private OpcUaSubscription subscription;

  @BeforeEach
  void setUp() throws Exception {
    // Registered before the monitored item so it is notified first.
    server.getEventNotifier().register(throwingListener);

    subscription = new OpcUaSubscription(client);
    subscription.create();
  }

  @AfterEach
  void tearDown() throws Exception {
    subscription.delete();
    server.getEventNotifier().unregister(throwingListener);
  }

  @Test
  void eventIsDeliveredToItemRegisteredAfterAThrowingListener() throws Exception {
    var events = new CopyOnWriteArrayList<Variant[]>();
    var latch = new CountDownLatch(1);

    OpcUaMonitoredItem item = OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter());
    item.setQueueSize(uint(100));
    item.setEventValueListener(
        (monitoredItem, eventFields) -> {
          events.add(eventFields);
          // The test namespace also emits events; wait for this test's message.
          if (eventFields[0].value() instanceof LocalizedText text
              && "after throwing listener".equals(text.text())) {
            latch.countDown();
          }
        });
    subscription.addMonitoredItem(item);
    subscription.synchronizeMonitoredItems();
    assertTrue(item.getCreateResult().orElseThrow().isGood());

    assertDoesNotThrow(() -> fireEvent("after throwing listener"));

    assertTrue(latch.await(5, TimeUnit.SECONDS), "event was not delivered");
    assertTrue(
        events.stream()
            .anyMatch(
                eventFields ->
                    eventFields[0].value() instanceof LocalizedText text
                        && "after throwing listener".equals(text.text())));
  }

  private void fireEvent(String message) throws Exception {
    BaseEventTypeNode eventNode =
        server
            .getEventFactory()
            .createEvent(new NodeId(1, "EventDeliveryIsolationTest"), NodeIds.BaseEventType);

    try {
      eventNode.setBrowseName(new QualifiedName(1, "EventDeliveryIsolationTest"));
      eventNode.setDisplayName(LocalizedText.english("EventDeliveryIsolationTest"));
      eventNode.setEventId(ByteString.of(new byte[] {1, 2, 3, 4}));
      eventNode.setEventType(NodeIds.BaseEventType);
      eventNode.setSourceNode(NodeIds.Server);
      eventNode.setSourceName("Server");
      eventNode.setTime(DateTime.now());
      eventNode.setReceiveTime(DateTime.NULL_VALUE);
      eventNode.setMessage(LocalizedText.english(message));
      eventNode.setSeverity(ushort(1));

      server.getEventNotifier().fire(eventNode);
    } finally {
      eventNode.delete();
    }
  }

  private static EventFilter eventFilter() {
    SimpleAttributeOperand message =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "Message")},
            AttributeId.Value.uid(),
            null);

    return new EventFilter(new SimpleAttributeOperand[] {message}, new ContentFilter(null));
  }
}
