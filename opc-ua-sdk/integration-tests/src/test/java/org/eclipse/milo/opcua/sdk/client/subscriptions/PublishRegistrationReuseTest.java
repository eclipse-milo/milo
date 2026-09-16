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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.util.HashedWheelTimer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;

/** A response belongs to the subscription registration that existed when its request was sent. */
class PublishRegistrationReuseTest {
  @Test
  void delayedTimeoutCannotResetRecreatedObjectWithSameId() throws Exception {
    assertTimeoutOwnership(true, true);
  }

  @Test
  void delayedTimeoutCannotResetReplacementObjectWithSameId() throws Exception {
    assertTimeoutOwnership(true, false);
  }

  @Test
  void currentTimeoutStillResetsItsSubscription() throws Exception {
    assertTimeoutOwnership(false, true);
  }

  @Test
  void requestQueuedBeforeFirstRegistrationCanServeThatSubscription() throws Exception {
    assertRegistrationChurnDoesNotDiscardResponse(false);
  }

  @Test
  void unrelatedRegistrationDoesNotDiscardExistingSubscriptionsResponse() throws Exception {
    assertRegistrationChurnDoesNotDiscardResponse(true);
  }

  private void assertRegistrationChurnDoesNotDiscardResponse(boolean originalAlreadyRegistered)
      throws Exception {
    try (var transport = new Transport()) {
      var client = new Client(transport);
      var original = new OpcUaSubscription(client);
      if (originalAlreadyRegistered)
        original.createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);
      var session =
          new OpcUaSession(
              new NodeId(1, "token"),
              new NodeId(1, "session"),
              "session",
              60000,
              uint(0),
              ByteString.NULL_VALUE,
              new SignedSoftwareCertificate[0]);
      var pendingCount = new AtomicLong(1);
      client.getPublishingManager().sendPublishRequest(session, pendingCount);
      var response = client.pending;
      if (originalAlreadyRegistered) {
        client.nextSubscriptionId = 8;
        new OpcUaSubscription(client).createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);
      } else {
        original.createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);
      }
      var status = new StatusChangeNotification(new StatusCode(StatusCodes.Bad_Timeout), null);
      response.complete(
          new PublishResponse(
              header(),
              uint(7),
              new UInteger[] {uint(1)},
              false,
              new NotificationMessage(
                  uint(1),
                  DateTime.now(),
                  new ExtensionObject[] {
                    ExtensionObject.encode(client.getStaticEncodingContext(), status)
                  }),
              new StatusCode[0],
              null));
      transport.executor.drain();
      assertEquals(0, pendingCount.get());
      assertTrue(
          original.getSubscriptionId().isEmpty(),
          "valid timeout must reach its owner despite a registration after the request was sent");
    }
  }

  private void assertTimeoutOwnership(boolean replace, boolean reuseObject) throws Exception {
    try (var transport = new Transport()) {
      var client = new Client(transport);
      var statuses = new ArrayList<StatusCode>();
      var original = new OpcUaSubscription(client);
      original.setSubscriptionListener(
          new OpcUaSubscription.SubscriptionListener() {
            @Override
            public void onStatusChanged(OpcUaSubscription s, StatusCode status) {
              statuses.add(status);
            }
          });
      original.createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);
      var session =
          new OpcUaSession(
              new NodeId(1, "token"),
              new NodeId(1, "session"),
              "session",
              60000,
              uint(0),
              ByteString.NULL_VALUE,
              new SignedSoftwareCertificate[0]);
      var pendingCount = new AtomicLong(1);
      client.getPublishingManager().sendPublishRequest(session, pendingCount);
      var response = client.pending;
      OpcUaSubscription current = original;
      if (replace) {
        original.reset();
        if (!reuseObject) {
          current = new OpcUaSubscription(client);
          current.setSubscriptionListener(
              new OpcUaSubscription.SubscriptionListener() {
                @Override
                public void onStatusChanged(OpcUaSubscription s, StatusCode status) {
                  statuses.add(status);
                }
              });
        }
        current.createAsync().toCompletableFuture().get(5, TimeUnit.SECONDS);
      }
      long incarnation = current.getIncarnation();
      var status = new StatusChangeNotification(new StatusCode(StatusCodes.Bad_Timeout), null);
      response.complete(
          new PublishResponse(
              header(),
              uint(7),
              new UInteger[] {uint(1)},
              false,
              new NotificationMessage(
                  uint(1),
                  DateTime.now(),
                  new ExtensionObject[] {
                    ExtensionObject.encode(client.getStaticEncodingContext(), status)
                  }),
              new StatusCode[0],
              null));
      transport.executor.drain();
      assertEquals(
          0, pendingCount.get(), "discarding a response must still release its pipeline slot");
      if (replace) {
        assertEquals(uint(7), current.getSubscriptionId().orElseThrow());
        assertEquals(incarnation, current.getIncarnation());
        assertTrue(
            statuses.isEmpty(), "the replacement must not receive its predecessor's timeout");
      } else {
        assertTrue(current.getSubscriptionId().isEmpty());
        assertEquals(List.of(new StatusCode(StatusCodes.Bad_Timeout)), statuses);
      }
    }
  }

  private static ResponseHeader header() {
    return new ResponseHeader(DateTime.now(), uint(1), StatusCode.GOOD, null, null, null);
  }

  private static class ManualExecutor extends AbstractExecutorService {
    final Deque<Runnable> tasks = new ArrayDeque<>();

    @Override
    public void execute(Runnable command) {
      tasks.addLast(command);
    }

    void drain() {
      int count = 0;
      while (!tasks.isEmpty()) {
        assertTrue(++count < 1000, "executor did not quiesce");
        tasks.removeFirst().run();
      }
    }

    @Override
    public void shutdown() {}

    @Override
    public List<Runnable> shutdownNow() {
      return List.of();
    }

    @Override
    public boolean isShutdown() {
      return false;
    }

    @Override
    public boolean isTerminated() {
      return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      return false;
    }
  }

  private static class Transport implements OpcClientTransport, AutoCloseable {
    final ManualExecutor executor = new ManualExecutor();
    final HashedWheelTimer timer = new HashedWheelTimer();
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder()
            .setExecutor(executor)
            .setWheelTimer(timer)
            .setScheduledExecutor(scheduler)
            .build();

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext c) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(UaRequestMessageType r) {
      throw new AssertionError(r);
    }

    @Override
    public void close() {
      scheduler.shutdownNow();
      timer.stop();
    }
  }

  private static class Client extends OpcUaClient {
    CompletableFuture<UaResponseMessageType> pending;
    int nextSubscriptionId = 7;

    Client(Transport transport) {
      super(
          OpcUaClientConfig.builder()
              .setEndpoint(
                  new EndpointDescription(
                      "opc.tcp://localhost:4840",
                      null,
                      ByteString.NULL_VALUE,
                      MessageSecurityMode.None,
                      SecurityPolicy.None.getUri(),
                      new UserTokenPolicy[0],
                      Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
                      ubyte(0)))
              .setDiscoveryEndpoints(List.of())
              .build(),
          transport);
    }

    // Automatic Publish replenishment waits here. Each test sends exactly one controlled request.
    @Override
    public CompletableFuture<OpcUaSession> getSessionAsync() {
      return new CompletableFuture<>();
    }

    @Override
    public CompletableFuture<CreateSubscriptionResponse> createSubscriptionAsync(
        double p, UInteger l, UInteger k, UInteger n, boolean enabled, UByte priority) {
      return CompletableFuture.completedFuture(
          new CreateSubscriptionResponse(header(), uint(nextSubscriptionId), p, l, k));
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestAsync(UaRequestMessageType request) {
      assertInstanceOf(PublishRequest.class, request);
      pending = new CompletableFuture<>();
      return pending;
    }
  }
}
