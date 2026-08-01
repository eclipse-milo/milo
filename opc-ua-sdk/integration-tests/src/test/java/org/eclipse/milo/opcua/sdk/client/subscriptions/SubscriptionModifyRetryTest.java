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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription.SyncState;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Verifies that a failed {@link OpcUaSubscription#modify()} leaves the pending modifications intact
 * so the next {@code modify()} retries them.
 *
 * <p>{@code modify()} takes the pending {@code Modifications} and clears the field <i>before</i>
 * calling the ModifySubscription service. If the service call fails the Subscription is left in
 * {@code UNSYNCHRONIZED} state with nothing left to synchronize, so the caller's requested
 * parameters are lost even though the Subscription still advertises that it has changes to push. A
 * transient service failure is exactly the case a caller is expected to retry, so the retry must
 * send the same parameters that the first attempt sent.
 */
public class SubscriptionModifyRetryTest {

  /**
   * A {@link DelegatingSubscriptionServiceSet} that fails the first {@code n} ModifySubscription
   * calls with a service fault and records every request it receives.
   */
  private static class FlakyModifySubscriptionServiceSet extends DelegatingSubscriptionServiceSet {

    private final List<ModifySubscriptionRequest> requests =
        Collections.synchronizedList(new ArrayList<>());

    private final AtomicInteger failuresRemaining;

    FlakyModifySubscriptionServiceSet(OpcUaServer server, int failureCount) {
      super(server);

      this.failuresRemaining = new AtomicInteger(failureCount);
    }

    List<ModifySubscriptionRequest> getRequests() {
      return List.copyOf(requests);
    }

    @Override
    public ModifySubscriptionResponse onModifySubscription(
        ServiceRequestContext context, ModifySubscriptionRequest request) throws UaException {

      requests.add(request);

      if (failuresRemaining.getAndDecrement() > 0) {
        throw new UaException(
            StatusCodes.Bad_TooManyOperations, "scripted ModifySubscription failure");
      }

      return super.onModifySubscription(context, request);
    }
  }

  @Test
  void modifyAfterFailedModifyRetriesTheSamePendingModifications() throws Exception {
    TestServer testServer = TestServer.create();
    OpcUaServer server = testServer.getServer();

    var serviceSet = new FlakyModifySubscriptionServiceSet(server, 1);
    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), serviceSet);
    }

    server.startup().get();

    OpcUaClient client = TestClient.create(server, cfg -> {});
    client.connect();

    try {
      var subscription = new OpcUaSubscription(client);
      subscription.create();

      double originalInterval = subscription.getRevisedPublishingInterval().orElseThrow();
      double requestedInterval = originalInterval * 2.0;

      subscription.setPublishingInterval(requestedInterval);
      assertEquals(SyncState.UNSYNCHRONIZED, subscription.getSyncState());

      // First attempt: the server rejects ModifySubscription with a service fault.
      UaException failure = assertThrows(UaException.class, subscription::modify);
      assertEquals(
          StatusCodes.Bad_TooManyOperations,
          failure.getStatusCode().value(),
          "first modify() should have failed with the scripted fault");

      assertEquals(
          1, serviceSet.getRequests().size(), "server should have seen exactly one modify attempt");
      assertEquals(
          requestedInterval,
          serviceSet.getRequests().get(0).getRequestedPublishingInterval().doubleValue(),
          "control: the first attempt did request the new PublishingInterval");

      assertEquals(
          SyncState.UNSYNCHRONIZED,
          subscription.getSyncState(),
          "a failed modify() must leave the Subscription unsynchronized");

      // Second attempt: succeeds, and must re-send the modifications the first attempt lost.
      subscription.modify();

      assertEquals(SyncState.SYNCHRONIZED, subscription.getSyncState());
      assertEquals(
          2,
          serviceSet.getRequests().size(),
          "server should have seen exactly two modify attempts");
      assertEquals(
          requestedInterval,
          serviceSet.getRequests().get(1).getRequestedPublishingInterval().doubleValue(),
          "the retry must re-send the pending PublishingInterval, not fall back to the server"
              + " value");
    } finally {
      try {
        client.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }
}
