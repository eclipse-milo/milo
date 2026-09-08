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

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicInteger;
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

  private static EventNotifier newServerEventNotifier() throws Exception {
    Class<?> notifierClass = Class.forName(OpcUaServer.class.getName() + "$ServerEventNotifier");
    Constructor<?> constructor = notifierClass.getDeclaredConstructor();
    constructor.setAccessible(true);

    return (EventNotifier) constructor.newInstance();
  }
}
