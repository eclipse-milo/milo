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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.junit.jupiter.api.Test;

/** The watchdog hand-off between reconnect recovery and a concurrently created Subscription. */
public class SubscriptionWatchdogRegistrationRaceTest {

  /**
   * Recovery re-arms the Subscriptions already in the PublishingManager registry, while creation
   * independently checks whether publishing is still suspended. If recovery resumes after that
   * check but before registration, both paths miss the new Subscription and its watchdog remains
   * unarmed forever. This fixture makes registration the exact point at which recovery finishes:
   * registering before the final check guarantees one of the two paths arms the timer.
   */
  @Test
  void createArmsWatchdogWhenRecoveryResumesAtRegistration() throws Exception {
    OpcUaClient client = mock(OpcUaClient.class);
    PublishingManager publishingManager = mock(PublishingManager.class);
    OpcClientTransport transport = mock(OpcClientTransport.class);
    OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
    ExecutorService executor = mock(ExecutorService.class);
    ScheduledExecutorService scheduledExecutor = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);

    when(client.getPublishingManager()).thenReturn(publishingManager);
    when(client.getTransport()).thenReturn(transport);
    when(transport.getConfig()).thenReturn(transportConfig);
    when(transportConfig.getExecutor()).thenReturn(executor);
    when(transportConfig.getScheduledExecutor()).thenReturn(scheduledExecutor);
    doReturn(scheduledFuture)
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

    var publishingSuspended = new AtomicBoolean(true);
    when(publishingManager.isPublishingSuspended())
        .thenAnswer(invocation -> publishingSuspended.get());
    doAnswer(
            invocation -> {
              // Models resumePublishing() completing its registry sweep immediately before this
              // new entry becomes visible to that sweep.
              publishingSuspended.set(false);
              return null;
            })
        .when(publishingManager)
        .addSubscription(any(OpcUaSubscription.class));

    var subscription = new OpcUaSubscription(client);

    subscription.createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);

    verify(publishingManager).addSubscription(same(subscription));
    verify(scheduledExecutor).schedule(any(Runnable.class), anyLong(), eq(TimeUnit.MILLISECONDS));
  }
}
