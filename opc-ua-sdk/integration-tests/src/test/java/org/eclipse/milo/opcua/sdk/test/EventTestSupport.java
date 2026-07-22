/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;

/**
 * Shared helpers for tests that monitor and assert on client-received events: monitored event items
 * collecting into a list, severity-scoped {@link EventFilter}s, select-clause operands, and
 * deadline-based await/assert-absent loops.
 */
public final class EventTestSupport {

  private static final long AWAIT_TIMEOUT_MS = 5_000;
  private static final long SETTLE_MS = 2_000;

  private EventTestSupport() {}

  /**
   * Create a {@link SimpleAttributeOperand} selecting the Value of the event field at {@code
   * browsePath} relative to {@code typeDefinitionId}.
   */
  public static SimpleAttributeOperand eventField(NodeId typeDefinitionId, String... browsePath) {
    var qualifiedPath = new QualifiedName[browsePath.length];
    for (int i = 0; i < browsePath.length; i++) {
      qualifiedPath[i] = new QualifiedName(0, browsePath[i]);
    }

    return new SimpleAttributeOperand(
        typeDefinitionId, qualifiedPath, AttributeId.Value.uid(), null);
  }

  /**
   * Create an {@link EventFilter} with the given select clauses, matching only events whose
   * Severity equals {@code severity} — scoping a test's items to its own events.
   */
  public static EventFilter severityEventFilter(
      int severity, SimpleAttributeOperand... selectClauses) {

    var whereClause =
        new ContentFilter(
            new ContentFilterElement[] {
              new ContentFilterElement(
                  FilterOperator.Equals,
                  new ExtensionObject[] {
                    ExtensionObject.encode(
                        DefaultEncodingContext.INSTANCE,
                        eventField(NodeIds.BaseEventType, "Severity")),
                    ExtensionObject.encode(
                        DefaultEncodingContext.INSTANCE,
                        new LiteralOperand(new Variant(ushort(severity))))
                  })
            });

    return new EventFilter(selectClauses, whereClause);
  }

  /**
   * Create an event monitored item on {@code nodeId} that appends each received event's fields to
   * {@code events}, and verify it was created successfully.
   */
  public static OpcUaMonitoredItem createEventItem(
      OpcUaSubscription subscription, NodeId nodeId, EventFilter filter, List<Variant[]> events)
      throws Exception {

    OpcUaMonitoredItem item = OpcUaMonitoredItem.newEventItem(nodeId, filter);
    item.setQueueSize(uint(100));
    item.setEventValueListener((monitoredItem, eventFields) -> events.add(eventFields));

    subscription.addMonitoredItem(item);
    subscription.synchronizeMonitoredItems();

    assertTrue(item.getCreateResult().orElseThrow().isGood());

    return item;
  }

  /**
   * Create an event monitored item on {@code nodeId} and return the list its received events'
   * fields are appended to.
   */
  public static List<Variant[]> monitorEvents(
      OpcUaSubscription subscription, NodeId nodeId, EventFilter filter) throws Exception {

    var events = new CopyOnWriteArrayList<Variant[]>();
    createEventItem(subscription, nodeId, filter, events);
    return events;
  }

  /**
   * Wait until an event matching {@code predicate} arrives, returning its fields.
   *
   * @param description identifies the expected event in the failure message.
   */
  public static Variant[] awaitEvent(
      List<Variant[]> events, String description, Predicate<Variant[]> predicate)
      throws InterruptedException {

    long deadline = System.currentTimeMillis() + AWAIT_TIMEOUT_MS;

    while (System.currentTimeMillis() < deadline) {
      for (Variant[] eventFields : events) {
        if (predicate.test(eventFields)) {
          return eventFields;
        }
      }
      //noinspection BusyWait
      Thread.sleep(50);
    }

    fail("expected event was not delivered: " + description);
    throw new IllegalStateException("unreachable");
  }

  /**
   * Assert no event matching {@code predicate} arrives within a settle period.
   *
   * @param description identifies the unexpected event in the failure message.
   */
  public static void assertNoEvent(
      List<Variant[]> events, String description, Predicate<Variant[]> predicate)
      throws InterruptedException {

    Thread.sleep(SETTLE_MS);

    Variant[] match = events.stream().filter(predicate).findFirst().orElse(null);
    assertNull(match, "event should not have been delivered: " + description);
  }
}
