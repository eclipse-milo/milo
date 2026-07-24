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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.ScriptableSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

/**
 * Exercises {@link ScriptableSubscriptionServiceSet} end-to-end against the real client stack.
 *
 * <p>This is a "does the harness work" smoke test, not a bug reproduction. It proves the seam can
 * (a) deliver a scripted keep-alive to the client's {@code SubscriptionListener} and (b) capture
 * the acknowledgements the client sends back after processing a scripted notification. The
 * deterministic bug-reproduction tests built on this harness are enumerated in {@code
 * docs/plans/publishing-manager-reliability-repair.md}.
 */
public class PublishScriptHarnessTest {

  @Test
  void scriptedKeepAliveIsDeliveredAndNotificationIsAcknowledged() throws Exception {
    TestServer testServer = TestServer.create();
    OpcUaServer server = testServer.getServer();

    var scriptable = new ScriptableSubscriptionServiceSet(server);
    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), scriptable);
    }

    server.startup().get();

    // Long request timeout so parked Publish requests do not time out during the test.
    OpcUaClient client = TestClient.create(server, cfg -> cfg.setRequestTimeout(uint(60_000)));
    client.connect();

    try {
      var keepAliveLatch = new CountDownLatch(1);

      var subscription = new OpcUaSubscription(client);
      subscription.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onKeepAliveReceived(OpcUaSubscription subscription) {
              keepAliveLatch.countDown();
            }
          });
      subscription.create();

      UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

      // (a) A scripted keep-alive (sequence 1) reaches the listener.
      scriptable.enqueueKeepAlive(subscriptionId, 1);
      assertTrue(
          keepAliveLatch.await(5, TimeUnit.SECONDS),
          "keep-alive was not delivered to the listener");

      // (b) A scripted notification (sequence 2) advertising available sequence number 2 causes the
      // client to acknowledge sequence 2 on a subsequent Publish request.
      scriptable.enqueueDataChange(subscriptionId, 2, List.of(), uint(2));

      boolean acknowledged =
          awaitTrue(
              () ->
                  scriptable.getReceivedAcknowledgements().stream()
                      .anyMatch(
                          ack ->
                              ack.getSubscriptionId().equals(subscriptionId)
                                  && ack.getSequenceNumber().longValue() == 2L),
              5000);

      assertTrue(acknowledged, "client did not acknowledge sequence 2");
    } finally {
      scriptable.failParkedRequests(StatusCodes.Bad_NoSubscription);
      try {
        client.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }

  private static boolean awaitTrue(BooleanSupplierThrowing condition, long timeoutMillis)
      throws Exception {
    long deadline = System.currentTimeMillis() + timeoutMillis;
    while (System.currentTimeMillis() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(25);
    }
    return condition.get();
  }

  @FunctionalInterface
  private interface BooleanSupplierThrowing {
    boolean get() throws Exception;
  }
}
