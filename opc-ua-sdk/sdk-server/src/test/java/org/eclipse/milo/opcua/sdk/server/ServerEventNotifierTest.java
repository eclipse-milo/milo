/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.items.EventItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.Test;

public class ServerEventNotifierTest {

  @Test
  public void duplicateRegistrationsAreIgnored() throws Exception {
    EventNotifier notifier = newServerEventNotifier();
    AtomicInteger eventCount = new AtomicInteger();
    EventListener listener = event -> eventCount.incrementAndGet();

    notifier.register(listener);
    notifier.register(listener);
    notifier.register(listener);

    notifier.fire(null);

    assertEquals(1, eventCount.get());
  }

  @Test
  public void unregisterRemovesDuplicateRegistration() throws Exception {
    EventNotifier notifier = newServerEventNotifier();
    AtomicInteger eventCount = new AtomicInteger();
    EventListener listener = event -> eventCount.incrementAndGet();

    notifier.register(listener);
    notifier.register(listener);
    notifier.unregister(listener);

    notifier.fire(null);

    assertEquals(0, eventCount.get());
  }

  // Listeners are notified in registration order; an exception from one listener must not stop
  // delivery to the listeners registered after it, and must not propagate to the caller of fire().
  @Test
  public void throwingListenerDoesNotBlockLaterListeners() throws Exception {
    EventNotifier notifier = newServerEventNotifier();
    AtomicInteger eventCount = new AtomicInteger();

    notifier.register(
        event -> {
          throw new IllegalStateException("listener failure");
        });
    notifier.register(event -> eventCount.incrementAndGet());

    assertDoesNotThrow(() -> notifier.fire(null));

    assertEquals(1, eventCount.get());
  }

  // Notifier-scope filtering is applied per listener after the scope is resolved once per event;
  // an in-scope listener that throws must neither stop later in-scope listeners nor let an
  // out-of-scope listener receive the event.
  @Test
  public void throwingListenerDoesNotAffectScopeFilteringOfLaterListeners() throws Exception {
    NodeId sourceNodeId = new NodeId(2, "source");
    NodeId unrelatedNodeId = new NodeId(2, "unrelated");

    OpcUaServer server = mock(OpcUaServer.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    when(server.getReferenceTypeTree()).thenReturn(mock(ReferenceTypeTree.class));
    when(server.getNamespaceTable()).thenReturn(new NamespaceTable());
    when(addressSpaceManager.getManagedReferences(any(NodeId.class), any())).thenReturn(List.of());

    BaseEventTypeNode event = mock(BaseEventTypeNode.class);
    when(event.getSourceNode()).thenReturn(sourceNodeId);

    EventItem throwingInScopeItem = eventItem(sourceNodeId);
    doThrow(new IllegalStateException("listener failure")).when(throwingInScopeItem).onEvent(any());
    EventItem inScopeItem = eventItem(sourceNodeId);
    EventItem outOfScopeItem = eventItem(unrelatedNodeId);

    EventNotifier notifier = newServerEventNotifier(server);
    notifier.register(throwingInScopeItem);
    notifier.register(inScopeItem);
    notifier.register(outOfScopeItem);

    assertDoesNotThrow(() -> notifier.fire(event));

    verify(inScopeItem).onEvent(event);
    verify(outOfScopeItem, never()).onEvent(any());
  }

  private static EventItem eventItem(NodeId monitoredNodeId) {
    EventItem item = mock(EventItem.class);
    when(item.getReadValueId())
        .thenReturn(new ReadValueId(monitoredNodeId, AttributeId.EventNotifier.uid(), null, null));
    return item;
  }

  private static EventNotifier newServerEventNotifier() throws Exception {
    return newServerEventNotifier(null);
  }

  private static EventNotifier newServerEventNotifier(OpcUaServer server) throws Exception {
    Class<?> notifierClass = Class.forName(OpcUaServer.class.getName() + "$ServerEventNotifier");
    Constructor<?> constructor = notifierClass.getDeclaredConstructor(OpcUaServer.class);
    constructor.setAccessible(true);

    // a null OpcUaServer is sufficient when firing a null event: it resolves an empty notifier
    // scope without consulting the server, and non-item listeners are always in scope.
    return (EventNotifier) constructor.newInstance(new Object[] {server});
  }
}
