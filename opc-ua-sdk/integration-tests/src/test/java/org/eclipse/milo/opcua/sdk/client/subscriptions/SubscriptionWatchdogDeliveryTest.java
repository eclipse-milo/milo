/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.junit.jupiter.api.Test;

class SubscriptionWatchdogDeliveryTest {

  // Positive control: holding the delivery queue alone must not discard a valid expiry.
  @Test
  void queuedWatchdogExpiryIsDeliveredOnce() throws Exception {
    var fixture = new Fixture();
    fixture.createSubscription();

    fixture.expireWatchdog();
    verify(fixture.listener, never()).onWatchdogTimerElapsed(fixture.subscription);

    fixture.deliverQueuedNotification();

    verify(fixture.listener).onWatchdogTimerElapsed(fixture.subscription);
  }

  // An expiry queued before reset must not tell the application to recover a discarded lifecycle.
  @Test
  void resetDiscardsQueuedWatchdogExpiry() throws Exception {
    var fixture = new Fixture();
    fixture.createSubscription();
    fixture.expireWatchdog();

    fixture.subscription.reset();
    fixture.deliverQueuedNotification();

    verify(fixture.listener, never()).onWatchdogTimerElapsed(fixture.subscription);
  }

  // Reusing the Subscription object must not let its old watchdog report on the replacement.
  @Test
  void recreationDiscardsOldExpiryAndDeliversNewExpiry() throws Exception {
    var fixture = new Fixture();
    fixture.createSubscription();
    fixture.expireWatchdog();

    fixture.subscription.reset();
    fixture.createSubscription();
    fixture.deliverQueuedNotification();

    verify(fixture.listener, never()).onWatchdogTimerElapsed(fixture.subscription);

    fixture.expireWatchdog();
    fixture.deliverQueuedNotification();

    verify(fixture.listener).onWatchdogTimerElapsed(fixture.subscription);
  }

  // Session unavailability invalidates queued expiries without permanently disabling the timer.
  @Test
  void pauseDiscardsQueuedExpiryAndAllowsRearming() throws Exception {
    var fixture = new Fixture();
    fixture.createSubscription();
    fixture.expireWatchdog();

    fixture.subscription.pauseWatchdogTimer();
    fixture.deliverQueuedNotification();

    verify(fixture.listener, never()).onWatchdogTimerElapsed(fixture.subscription);

    fixture.subscription.resetWatchdogTimer();
    fixture.expireWatchdog();
    fixture.deliverQueuedNotification();

    verify(fixture.listener).onWatchdogTimerElapsed(fixture.subscription);
  }

  // A Publish response can rearm the timer while an earlier expiry is waiting for delivery.
  @Test
  void rearmingDiscardsQueuedExpiryAndDeliversNewExpiry() throws Exception {
    var fixture = new Fixture();
    fixture.createSubscription();
    fixture.expireWatchdog();

    fixture.subscription.resetWatchdogTimer();
    fixture.deliverQueuedNotification();

    verify(fixture.listener, never()).onWatchdogTimerElapsed(fixture.subscription);

    fixture.expireWatchdog();
    fixture.deliverQueuedNotification();

    verify(fixture.listener).onWatchdogTimerElapsed(fixture.subscription);
  }

  private static final class Fixture {

    private final Queue<Runnable> scheduledTasks = new ArrayDeque<>();
    private final Queue<Runnable> deliveryTasks = new ArrayDeque<>();

    private final OpcUaSubscription subscription;
    private final OpcUaSubscription.SubscriptionListener listener =
        mock(OpcUaSubscription.SubscriptionListener.class);

    Fixture() {
      OpcUaClient client = mock(OpcUaClient.class);
      PublishingManager publishingManager = mock(PublishingManager.class);
      OpcClientTransport transport = mock(OpcClientTransport.class);
      OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
      ExecutorService executor = mock(ExecutorService.class);
      ScheduledExecutorService scheduledExecutor = mock(ScheduledExecutorService.class);

      when(client.getPublishingManager()).thenReturn(publishingManager);
      when(client.getTransport()).thenReturn(transport);
      when(transport.getConfig()).thenReturn(transportConfig);
      when(transportConfig.getExecutor()).thenReturn(executor);
      when(transportConfig.getScheduledExecutor()).thenReturn(scheduledExecutor);

      doAnswer(
              invocation -> {
                deliveryTasks.add(invocation.getArgument(0, Runnable.class));
                return null;
              })
          .when(executor)
          .execute(any(Runnable.class));

      doAnswer(
              invocation -> {
                scheduledTasks.add(invocation.getArgument(0, Runnable.class));
                return mock(ScheduledFuture.class);
              })
          .when(scheduledExecutor)
          .schedule(any(Runnable.class), anyLong(), eq(TimeUnit.MILLISECONDS));

      var response =
          new CreateSubscriptionResponse(
              mock(ResponseHeader.class), uint(1), 1_000.0, uint(50), uint(10));
      when(client.createSubscriptionAsync(
              anyDouble(),
              any(UInteger.class),
              any(UInteger.class),
              any(UInteger.class),
              anyBoolean(),
              any(UByte.class)))
          .thenReturn(CompletableFuture.completedFuture(response));

      subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(listener);
    }

    void createSubscription() throws Exception {
      subscription.createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    void expireWatchdog() {
      assertEquals(1, scheduledTasks.size(), "one watchdog expiry must be armed");
      scheduledTasks.remove().run();
      assertEquals(
          1, deliveryTasks.size(), "expiry must enqueue a notification before invalidation");
    }

    void deliverQueuedNotification() {
      assertEquals(1, deliveryTasks.size(), "the queued notification must actually execute");
      deliveryTasks.remove().run();
    }
  }
}
