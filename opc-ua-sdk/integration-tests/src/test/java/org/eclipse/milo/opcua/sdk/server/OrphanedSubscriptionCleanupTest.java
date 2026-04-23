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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.eventbus.Subscribe;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionDeletedEvent;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

public class OrphanedSubscriptionCleanupTest extends AbstractClientServerTest {

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder configBuilder) {
    configBuilder.setMaxPendingPublishRequests(uint(0));
  }

  // A timed-out Session must not leave orphaned Subscriptions behind in server bookkeeping,
  // otherwise `server.getSubscriptions()` grows forever and transfer/cleanup logic observes stale
  // entries.
  @Test
  void orphanedSubscriptionsAreRemovedAfterLifetimeExpires() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(1_000.0);
    subscription.setMaxKeepAliveCount(uint(3));
    subscription.setLifetimeCount(uint(9));
    subscription.create();

    var sessionId = client.getSession().getSessionId();
    UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();
    double revisedPublishingInterval = subscription.getRevisedPublishingInterval().orElseThrow();
    long revisedLifetimeCount = subscription.getRevisedLifetimeCount().orElseThrow().longValue();

    long revisedLifetimeMillis = Math.round(revisedPublishingInterval * revisedLifetimeCount);

    assertTrue(server.getSubscriptions().containsKey(subscriptionId));

    var subscriptionDeleted = new CountDownLatch(1);
    Object subscriber =
        new Object() {
          @Subscribe
          public void onSubscriptionDeleted(SubscriptionDeletedEvent event) {
            if (subscriptionId.equals(event.getSubscription().getId())) {
              subscriptionDeleted.countDown();
            }
          }
        };

    server.getInternalEventBus().register(subscriber);

    // Session.checkTimeout() uses the same close(false) path. Kill the Session directly so this
    // test exercises the orphaned-subscription cleanup deterministically without waiting for the
    // idle timeout.
    try {
      server.getSessionManager().killSession(sessionId, false);

      assertEquals(0, server.getSessionManager().getCurrentSessionCount().longValue());
      assertTrue(
          subscriptionDeleted.await(revisedLifetimeMillis + 5_000, TimeUnit.MILLISECONDS),
          "orphaned subscription was not removed after lifetime expiry");
      assertFalse(server.getSubscriptions().containsKey(subscriptionId));
    } finally {
      server.getInternalEventBus().unregister(subscriber);
    }
  }
}
