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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that the MaxKeepAliveCount and LifetimeCount a Subscription requests are derived from
 * the PublishingInterval the caller actually asked for.
 *
 * <p>{@code OpcUaSubscription} promises a keep-alive roughly every {@code
 * DEFAULT_TARGET_KEEP_ALIVE_INTERVAL} (10,000 ms) and a LifetimeCount of 5x the MaxKeepAliveCount.
 * The counts are therefore a function of the PublishingInterval, and every path that sets the
 * PublishingInterval must recompute them (unless the caller has opted out via {@link
 * OpcUaSubscription#setLifetimeAndKeepAliveCalculated(boolean)}).
 *
 * <p>Assertions are made against the {@link CreateSubscriptionRequest} observed on the wire rather
 * than against the client-side getters, because the requested counts are what actually determine
 * the Server's keep-alive cadence and subscription lifetime.
 */
public class SubscriptionKeepAliveDerivationTest {

  /**
   * PublishingInterval used by the tests. Deliberately 10x faster than {@code
   * DEFAULT_PUBLISHING_INTERVAL} (1000 ms) so a count derived from the default is off by 10x and
   * cannot be mistaken for a rounding difference.
   */
  private static final double PUBLISHING_INTERVAL = 100.0;

  /** ceil(10000 / 100) — a keep-alive every 100 publishing intervals, i.e. every 10 seconds. */
  private static final long EXPECTED_MAX_KEEP_ALIVE_COUNT = 100L;

  /** 5 * 100 — the lifetime must be at least 3x the keep-alive count. */
  private static final long EXPECTED_LIFETIME_COUNT = 500L;

  private TestServer testServer;
  private OpcUaServer server;
  private OpcUaClient client;
  private CapturingSubscriptionServiceSet serviceSet;

  @BeforeEach
  void startClientAndServer() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();

    serviceSet = new CapturingSubscriptionServiceSet(server);
    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), serviceSet);
    }

    server.startup().get();

    client = TestClient.create(server, cfg -> {});
    client.connect();
  }

  @AfterEach
  void stopClientAndServer() throws Exception {
    try {
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
    } finally {
      server.shutdown().get(5, TimeUnit.SECONDS);
    }
  }

  /**
   * The {@code OpcUaSubscription(OpcUaClient, double)} constructor must produce the same requested
   * counts as constructing with the default interval and then calling {@code
   * setPublishingInterval()}. If it does not, a Subscription created with a fast PublishingInterval
   * asks the Server for a keep-alive cadence and a lifetime that are both wrong by the ratio of the
   * requested interval to the 1000 ms default — a 100 ms Subscription gets keep-alives every 1000
   * ms and expires after 5000 ms of client silence instead of 50,000 ms.
   */
  @Test
  void publishingIntervalConstructorDerivesCountsFromThatInterval() throws UaException {
    var subscription = new OpcUaSubscription(client, PUBLISHING_INTERVAL);

    try {
      subscription.create();

      CreateSubscriptionRequest request = serviceSet.lastCreateSubscriptionRequest;
      assertNotNull(request, "no CreateSubscription request was observed");

      assertAll(
          () ->
              assertEquals(
                  PUBLISHING_INTERVAL,
                  request.getRequestedPublishingInterval(),
                  "requested PublishingInterval"),
          () ->
              assertEquals(
                  uint(EXPECTED_MAX_KEEP_ALIVE_COUNT),
                  request.getRequestedMaxKeepAliveCount(),
                  "requested MaxKeepAliveCount must be derived from the constructor's"
                      + " PublishingInterval"),
          () ->
              assertEquals(
                  uint(EXPECTED_LIFETIME_COUNT),
                  request.getRequestedLifetimeCount(),
                  "requested LifetimeCount must be derived from the constructor's"
                      + " PublishingInterval"));
    } finally {
      subscription.delete();
    }
  }

  /**
   * Control for {@link #publishingIntervalConstructorDerivesCountsFromThatInterval()}: the {@code
   * setPublishingInterval()} path already recomputes the counts, so the expected values above are
   * not vacuous. If this test fails too, the expected numbers — not the constructor — are wrong.
   */
  @Test
  void setPublishingIntervalDerivesCountsFromThatInterval() throws UaException {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(PUBLISHING_INTERVAL);

    try {
      subscription.create();

      CreateSubscriptionRequest request = serviceSet.lastCreateSubscriptionRequest;
      assertNotNull(request, "no CreateSubscription request was observed");

      assertAll(
          () ->
              assertEquals(
                  PUBLISHING_INTERVAL,
                  request.getRequestedPublishingInterval(),
                  "requested PublishingInterval"),
          () ->
              assertEquals(
                  uint(EXPECTED_MAX_KEEP_ALIVE_COUNT),
                  request.getRequestedMaxKeepAliveCount(),
                  "requested MaxKeepAliveCount"),
          () ->
              assertEquals(
                  uint(EXPECTED_LIFETIME_COUNT),
                  request.getRequestedLifetimeCount(),
                  "requested LifetimeCount"));
    } finally {
      subscription.delete();
    }
  }

  /** Captures the CreateSubscription request the client puts on the wire. */
  private static class CapturingSubscriptionServiceSet extends DelegatingSubscriptionServiceSet {

    volatile CreateSubscriptionRequest lastCreateSubscriptionRequest;

    CapturingSubscriptionServiceSet(OpcUaServer server) {
      super(server);
    }

    @Override
    public CreateSubscriptionResponse onCreateSubscription(
        ServiceRequestContext context, CreateSubscriptionRequest request) throws UaException {

      lastCreateSubscriptionRequest = request;

      return super.onCreateSubscription(context, request);
    }
  }
}
