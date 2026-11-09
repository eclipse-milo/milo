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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Part 4 §5.14.5.1: a Server limits the Publish requests it queues for a Session. A request that
 * exceeds the limit de-queues the oldest one, which is answered Bad_TooManyPublishRequests, and the
 * Server accepts more queued requests than the Session has Subscriptions.
 */
class SubscriptionManagerPublishLimitTest {

  private final ServiceRequestContext context = mock(ServiceRequestContext.class);

  /** Without a limit, the requests a Session can leave queued are bounded only by memory. */
  @Test
  void requestOverTheLimitReturnsTheOldestQueuedRequest() throws Exception {
    SubscriptionManager manager = newSubscriptionManager(3, 1);

    List<CompletableFuture<PublishResponse>> queued = publish(manager, 3);

    queued.forEach(
        future -> assertFalse(future.isDone(), "requests within the limit must stay queued"));

    CompletableFuture<PublishResponse> overLimit = manager.publish(context, newPublishRequest(4));

    assertFailedWith(StatusCodes.Bad_TooManyPublishRequests, queued.get(0));
    assertFalse(queued.get(1).isDone(), "only the oldest request is returned");
    assertFalse(queued.get(2).isDone(), "only the oldest request is returned");
    assertFalse(overLimit.isDone(), "the new request takes the place of the oldest one");
    assertEquals(3, manager.getPublishQueue().size());
  }

  /**
   * A client keeps at least one Publish request queued per Subscription. A configured limit below
   * that would starve Subscriptions of requests, so the Subscription count plus one wins.
   */
  @Test
  void sessionCanQueueOneMoreRequestThanItHasSubscriptions() throws Exception {
    SubscriptionManager manager = newSubscriptionManager(1, 3);

    List<CompletableFuture<PublishResponse>> queued = publish(manager, 4);

    queued.forEach(
        future ->
            assertFalse(
                future.isDone(), "subscription count + 1 requests must fit despite the limit"));

    CompletableFuture<PublishResponse> overLimit = manager.publish(context, newPublishRequest(5));

    assertFailedWith(StatusCodes.Bad_TooManyPublishRequests, queued.get(0));
    assertFalse(overLimit.isDone());
    assertEquals(4, manager.getPublishQueue().size());
  }

  private List<CompletableFuture<PublishResponse>> publish(SubscriptionManager manager, int count) {
    var futures = new ArrayList<CompletableFuture<PublishResponse>>();

    for (int i = 1; i <= count; i++) {
      futures.add(manager.publish(context, newPublishRequest(i)));
    }

    return futures;
  }

  private static void assertFailedWith(long statusCode, CompletableFuture<PublishResponse> future) {
    ExecutionException e =
        assertThrows(ExecutionException.class, () -> future.get(0, TimeUnit.SECONDS));

    UaException cause = assertInstanceOf(UaException.class, e.getCause());

    assertEquals(statusCode, cause.getStatusCode().value());
  }

  private static SubscriptionManager newSubscriptionManager(
      int maxPublishRequestsPerSession, int subscriptionCount) throws Exception {

    OpcUaServerConfigLimits limits =
        new OpcUaServerConfigLimits() {
          @Override
          public UInteger getMaxPublishRequestsPerSession() {
            return uint(maxPublishRequestsPerSession);
          }
        };

    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(mock(ExecutorService.class));
    when(config.getLimits()).thenReturn(limits);

    OpcUaServer server = mock(OpcUaServer.class);
    when(server.getConfig()).thenReturn(config);

    var manager = new SubscriptionManager(mock(Session.class), server);

    for (int i = 1; i <= subscriptionCount; i++) {
      subscriptions(manager).put(uint(i), mock(Subscription.class));
    }

    return manager;
  }

  @SuppressWarnings("unchecked")
  private static Map<UInteger, Subscription> subscriptions(SubscriptionManager manager)
      throws ReflectiveOperationException {

    Field field = SubscriptionManager.class.getDeclaredField("subscriptions");
    field.setAccessible(true);

    return (Map<UInteger, Subscription>) field.get(manager);
  }

  private static PublishRequest newPublishRequest(long requestHandle) {
    var requestHeader =
        new RequestHeader(
            NodeId.NULL_VALUE, DateTime.now(), uint(requestHandle), uint(0), null, uint(0), null);

    return new PublishRequest(requestHeader, null);
  }
}
