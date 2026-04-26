/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcUaClientLifecycleTest {

  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private NioEventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;
  private OpcClientTransportConfig transportConfig;

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    eventLoop = new NioEventLoopGroup(1);
    wheelTimer = new HashedWheelTimer();
    transportConfig =
        OpcTcpClientTransportConfig.newBuilder()
            .setExecutor(executor)
            .setScheduledExecutor(scheduledExecutor)
            .setEventLoop(eventLoop)
            .setWheelTimer(wheelTimer)
            .build();
  }

  @AfterEach
  void tearDown() throws Exception {
    executor.shutdownNow();
    scheduledExecutor.shutdownNow();
    eventLoop.shutdownGracefully().get(5, TimeUnit.SECONDS);
    wheelTimer.stop();
  }

  @Test
  void connectAsyncPropagatesTransportConnectFailure() {
    var failure = new UaException(StatusCodes.Bad_ConnectionRejected, "bind failed");
    var transport = new FakeTransport(transportConfig, CompletableFuture.failedFuture(failure));
    var client = new OpcUaClient(newClientConfig(5_000), transport);

    ExecutionException ex =
        assertThrows(
            ExecutionException.class, () -> client.connectAsync().get(1, TimeUnit.SECONDS));

    assertSame(failure, ex.getCause());
    assertEquals(0, transport.requestCount());
  }

  @Test
  void disconnectAsyncReachesTransportDisconnectWhenSessionCloseCannotComplete() throws Exception {
    var transport =
        new FakeTransport(transportConfig, CompletableFuture.completedFuture(Unit.VALUE));
    var client = new OpcUaClient(newClientConfig(50), transport);

    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();
    assertTrue(transport.awaitRequest());
    assertFalse(connectFuture.isDone());

    CompletableFuture<OpcUaClient> disconnectFuture = client.disconnectAsync();

    assertSame(client, disconnectFuture.get(5, TimeUnit.SECONDS));
    assertTrue(transport.disconnectCalled());
  }

  private static OpcUaClientConfig newClientConfig(long requestTimeout) {
    return OpcUaClientConfig.builder()
        .setEndpoint(endpoint())
        .setRequestTimeout(uint(requestTimeout))
        .build();
  }

  private static EndpointDescription endpoint() {
    var server =
        new ApplicationDescription(
            "urn:eclipse:milo:test-server",
            "urn:eclipse:milo:test-product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            new String[0]);

    return new EndpointDescription(
        ENDPOINT_URL,
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
        },
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  private static final class FakeTransport implements OpcClientTransport {

    private final OpcClientTransportConfig config;
    private final CompletableFuture<Unit> connectFuture;
    private final CompletableFuture<UaResponseMessageType> requestFuture =
        new CompletableFuture<>();
    private final CountDownLatch requestSent = new CountDownLatch(1);
    private final AtomicInteger requestCount = new AtomicInteger();
    private final AtomicBoolean disconnectCalled = new AtomicBoolean();

    private FakeTransport(OpcClientTransportConfig config, CompletableFuture<Unit> connectFuture) {

      this.config = config;
      this.connectFuture = connectFuture;
    }

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      return connectFuture;
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      disconnectCalled.set(true);

      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      requestCount.incrementAndGet();
      requestSent.countDown();

      return requestFuture;
    }

    private boolean awaitRequest() throws InterruptedException {
      return requestSent.await(5, TimeUnit.SECONDS);
    }

    private int requestCount() {
      return requestCount.get();
    }

    private boolean disconnectCalled() {
      return disconnectCalled.get();
    }
  }
}
