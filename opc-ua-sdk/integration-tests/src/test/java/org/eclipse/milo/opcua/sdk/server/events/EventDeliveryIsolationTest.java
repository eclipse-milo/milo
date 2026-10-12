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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.EventTestSupport;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests that event delivery to a monitored item is independent of other registered listeners. */
public class EventDeliveryIsolationTest extends AbstractClientServerTest {

  /** Severity used by all events fired by this test; the where clause matches on it. */
  private static final int TEST_SEVERITY = 43;

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
    List<Variant[]> events =
        EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter());

    assertDoesNotThrow(() -> fireEvent("after throwing listener"));

    EventTestSupport.awaitEvent(
        events,
        "message \"after throwing listener\"",
        eventFields ->
            eventFields[0].value() instanceof LocalizedText text
                && "after throwing listener".equals(text.text()));
  }

  private void fireEvent(String message) throws Exception {
    try (TransientEvent event = server.newEvent(NodeIds.BaseEventType)) {
      BaseEventTypeNode eventNode = event.getNode();
      eventNode.setBrowseName(newQualifiedName("EventDeliveryIsolationTest"));
      eventNode.setDisplayName(LocalizedText.english("EventDeliveryIsolationTest"));
      eventNode.setSourceNode(NodeIds.Server);
      eventNode.setSourceName("Server");
      eventNode.setMessage(LocalizedText.english(message));
      eventNode.setSeverity(ushort(TEST_SEVERITY));
      event.fire();
    }
  }

  private static EventFilter eventFilter() {
    return EventTestSupport.severityEventFilter(
        TEST_SEVERITY, EventTestSupport.eventField(NodeIds.BaseEventType, "Message"));
  }
}
