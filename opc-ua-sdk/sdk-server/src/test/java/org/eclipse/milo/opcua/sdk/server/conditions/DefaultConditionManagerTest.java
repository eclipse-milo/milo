/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.events.TransientEvent;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/** Tests runtime-resource ownership and refresh delivery in {@link DefaultConditionManager}. */
public class DefaultConditionManagerTest {

  /**
   * Unregistering must release the Condition's runtime resources (otherwise shelving timers leak
   * and keep firing against a removed Condition), but only when the call actually removes the
   * Condition: a stale or repeated unregister must be a no-op, not a second shutdown() call.
   */
  @Test
  void unregisterReleasesConditionResourcesExactlyOnce() {
    OpcUaServer server = mock(OpcUaServer.class);
    var manager = new DefaultConditionManager(server);
    Condition condition = mock(Condition.class);
    NodeId conditionId = new NodeId(1, "ManagedCondition");
    when(condition.getConditionId()).thenReturn(conditionId);

    manager.register(condition);
    manager.unregister(condition);
    manager.unregister(condition);

    verify(condition).shutdown();
    assertTrue(manager.findCondition(conditionId).isEmpty());
  }

  /**
   * A Condition displaced by registering a different instance under the same ConditionId leaves the
   * registry without ever being unregistered, so register() itself must release the displaced
   * instance's resources — otherwise its shelving timers leak for the server's lifetime and fire
   * against an abandoned Condition.
   */
  @Test
  void registerReleasesDisplacedConditionResources() {
    OpcUaServer server = mock(OpcUaServer.class);
    var manager = new DefaultConditionManager(server);
    NodeId conditionId = new NodeId(1, "ManagedCondition");
    Condition original = mock(Condition.class);
    when(original.getConditionId()).thenReturn(conditionId);
    Condition replacement = mock(Condition.class);
    when(replacement.getConditionId()).thenReturn(conditionId);

    manager.register(original);
    manager.register(replacement);

    verify(original).shutdown();
    verify(replacement, never()).shutdown();
    assertTrue(manager.findCondition(conditionId).isPresent());
  }

  /**
   * ConditionRefresh delivers a RefreshStart marker, every retained Condition snapshot, and a
   * RefreshEnd marker to each event item of the Subscription. Delivery to one item must be
   * independent of the others (Part 9 §4.5).
   */
  @Nested
  class RefreshDelivery {

    private static final UInteger SUBSCRIPTION_ID = uint(7);

    private final OpcUaServer server = mock(OpcUaServer.class);
    private final Session session = mock(Session.class);
    private final Subscription subscription = mock(Subscription.class);

    private final BaseEventTypeNode refreshStart = mock(BaseEventTypeNode.class);
    private final BaseEventTypeNode refreshEnd = mock(BaseEventTypeNode.class);
    private final BaseEventTypeNode snapshotNode = mock(BaseEventTypeNode.class);

    private final MonitoredEventItem itemA = eventItem(1);
    private final MonitoredEventItem itemB = eventItem(2);

    private DefaultConditionManager manager;

    @BeforeEach
    void setUp() throws Exception {
      NodeId sessionId = new NodeId(1, "session");
      when(session.getSessionId()).thenReturn(sessionId);
      when(subscription.getSession()).thenReturn(session);
      Map<UInteger, BaseMonitoredItem<?>> monitoredItems = Map.of(uint(1), itemA, uint(2), itemB);
      when(subscription.getMonitoredItems()).thenReturn(monitoredItems);
      when(server.getSubscriptions()).thenReturn(Map.of(SUBSCRIPTION_ID, subscription));

      when(server.newEvent(NodeIds.RefreshStartEventType))
          .thenReturn(new TransientEvent(server, refreshStart));
      when(server.newEvent(NodeIds.RefreshEndEventType))
          .thenReturn(new TransientEvent(server, refreshEnd));

      Condition condition = mock(Condition.class);
      when(condition.getConditionId()).thenReturn(new NodeId(1, "condition"));
      when(condition.createRefreshSnapshots())
          .thenReturn(List.of(new ConditionEventSnapshot(snapshotNode)));

      manager = new DefaultConditionManager(server);
      manager.register(condition);
    }

    // An item whose filtered replay delivery throws must still have its bracket closed, and must
    // not affect the full bracket delivered to the other item.
    @Test
    void replayFailureOnOneItemDoesNotAffectOtherItemsOrItsOwnRefreshEnd() throws Exception {
      doThrow(new IllegalStateException("replay failure")).when(itemA).onEvent(any());

      assertDoesNotThrow(() -> manager.conditionRefresh(session, SUBSCRIPTION_ID));

      assertFullBracket(itemB);
      InOrder inOrderA = inOrder(itemA);
      inOrderA.verify(itemA).onRefreshMarker(refreshStart);
      inOrderA.verify(itemA).onRefreshMarker(refreshEnd);
    }

    // An item whose RefreshStart delivery throws never opened a bracket, so it receives nothing
    // further; the other item's bracket is unaffected.
    @Test
    void refreshStartFailureOnOneItemDoesNotAffectOtherItems() throws Exception {
      doThrow(new IllegalStateException("marker failure"))
          .when(itemA)
          .onRefreshMarker(refreshStart);

      assertDoesNotThrow(() -> manager.conditionRefresh(session, SUBSCRIPTION_ID));

      assertFullBracket(itemB);
      verify(itemA, never()).onEvent(any());
      verify(itemA, never()).onRefreshMarker(refreshEnd);
    }

    // The per-Subscription refresh guard must be released after a delivery failure, otherwise
    // every later refresh of the Subscription fails with Bad_RefreshInProgress.
    @Test
    void deliveryFailureReleasesRefreshGuard() throws Exception {
      doThrow(new IllegalStateException("marker failure")).when(itemA).onRefreshMarker(any());

      manager.conditionRefresh(session, SUBSCRIPTION_ID);

      assertDoesNotThrow(() -> manager.conditionRefresh(session, SUBSCRIPTION_ID));
    }

    private void assertFullBracket(MonitoredEventItem item) {
      InOrder inOrder = inOrder(item);
      inOrder.verify(item).onRefreshMarker(refreshStart);
      inOrder.verify(item).onEvent(snapshotNode);
      inOrder.verify(item).onRefreshMarker(refreshEnd);
    }

    private static MonitoredEventItem eventItem(int id) {
      MonitoredEventItem item = mock(MonitoredEventItem.class);
      when(item.getId()).thenReturn(uint(id));
      when(item.isSamplingEnabled()).thenReturn(true);
      when(item.getReadValueId())
          .thenReturn(new ReadValueId(NodeIds.Server, AttributeId.EventNotifier.uid(), null, null));
      return item;
    }
  }
}
