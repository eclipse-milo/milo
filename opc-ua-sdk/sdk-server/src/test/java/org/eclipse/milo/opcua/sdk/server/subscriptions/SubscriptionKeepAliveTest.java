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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.subscriptions.PublishQueue.PendingPublish;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class SubscriptionKeepAliveTest {

  @Test
  void secondQueuedPublishCompletesWhenMaxKeepAliveCountIsOne() {
    Subscription subscription = newSubscription();
    PendingPublish firstPublish = newPendingPublish(1);
    PendingPublish secondPublish = newPendingPublish(2);

    subscription.onPublish(firstPublish);
    subscription.onPublish(secondPublish);

    subscription.onPublishingTimer();

    assertTrue(firstPublish.responseFuture.isDone());
    assertFalse(secondPublish.responseFuture.isDone());

    subscription.onPublishingTimer();

    assertTrue(secondPublish.responseFuture.isDone());
    assertTrue(secondPublish.responseFuture.join().getResponseHeader().getServiceResult().isGood());
  }

  private static Subscription newSubscription() {
    OpcUaServerConfigLimits limits = new OpcUaServerConfigLimits() {};

    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getLimits()).thenReturn(limits);

    ScheduledExecutorService scheduledExecutor = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> scheduledFuture = mock(ScheduledFuture.class);
    doReturn(scheduledFuture)
        .when(scheduledExecutor)
        .schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));

    OpcUaServer server = mock(OpcUaServer.class);
    when(server.getConfig()).thenReturn(config);
    when(server.getScheduledExecutorService()).thenReturn(scheduledExecutor);

    ExecutorService executor = mock(ExecutorService.class);
    PublishQueue publishQueue = new PublishQueue(executor);

    SubscriptionManager subscriptionManager = mock(SubscriptionManager.class);
    when(subscriptionManager.getServer()).thenReturn(server);
    when(subscriptionManager.getPublishQueue()).thenReturn(publishQueue);

    return new Subscription(subscriptionManager, uint(1), 1_000.0, 1, 60, 0, true, 0);
  }

  private static PendingPublish newPendingPublish(long requestHandle) {
    ServiceRequestContext context = mock(ServiceRequestContext.class);
    when(context.receivedAtNanos()).thenReturn(System.nanoTime());

    RequestHeader requestHeader =
        new RequestHeader(
            NodeId.NULL_VALUE, DateTime.now(), uint(requestHandle), uint(0), null, uint(0), null);

    PublishRequest request = new PublishRequest(requestHeader, new SubscriptionAcknowledgement[0]);

    return new PendingPublish(context, request, new StatusCode[0]);
  }
}
