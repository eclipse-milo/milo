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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
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
    long revisedLifetimeCount = subscription.getRevisedLifetimeCount().orElseThrow().longValue();

    assertTrue(server.getSubscriptions().containsKey(subscriptionId));

    // Session.checkTimeout() uses the same close(false) path. Kill the Session directly so this
    // test exercises the orphaned-subscription cleanup deterministically without waiting for the
    // idle timeout.
    server.getSessionManager().killSession(sessionId, false);

    assertEquals(0, server.getSessionManager().getCurrentSessionCount().longValue());

    Subscription serverSubscription = server.getSubscriptions().get(subscriptionId);

    assertNotNull(serverSubscription);

    // Drive the same lifetime-expiry path the scheduler would normally trigger, but do it
    // synchronously so the test only depends on the orphaned-subscription cleanup behavior.
    expireSubscription(serverSubscription, revisedLifetimeCount);

    assertFalse(server.getSubscriptions().containsKey(subscriptionId));
  }

  private static void expireSubscription(Subscription subscription, long lifetimeCount) {
    for (long i = 0; i < lifetimeCount; i++) {
      subscription.onPublishingTimer();
    }
  }
}
