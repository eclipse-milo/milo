/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

public class SubscriptionWatchdogTimerTest extends AbstractClientServerTest {

  @Test
  void subscriptionWatchdogTimerCallback() throws UaException, InterruptedException {
    var subscription = new OpcUaSubscription(client);
    subscription.setTargetKeepAliveInterval(2000.0);
    subscription.setWatchdogMultiplier(1.5);
    subscription.create();

    var monitoredItem = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_State);
    subscription.addMonitoredItem(monitoredItem);
    subscription.synchronizeMonitoredItems();

    var latch = new CountDownLatch(2);
    var start = new AtomicLong(0);

    subscription.setSubscriptionListener(
        new OpcUaSubscription.SubscriptionListener() {
          @Override
          public void onDataReceived(
              OpcUaSubscription subscription,
              List<OpcUaMonitoredItem> items,
              List<DataValue> values) {

            System.out.println(
                "onDataReceived() id=" + subscription.getSubscriptionId().orElseThrow());

            // kill the subscription after the first notification is received
            UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();
            server.getSubscriptions().get(subscriptionId).deleteSubscription();

            latch.countDown();

            start.set(System.currentTimeMillis());
          }

          @Override
          public void onWatchdogTimerElapsed(OpcUaSubscription subscription) {
            var elapsed = System.currentTimeMillis() - start.get();

            System.out.printf(
                "onWatchdogTimerElapsed() id=%s, elapsed=%sms%n",
                subscription.getSubscriptionId().orElseThrow(), elapsed);

            latch.countDown();
          }
        });

    assertTrue(latch.await(10, TimeUnit.SECONDS));

    subscription.delete();
  }
}
