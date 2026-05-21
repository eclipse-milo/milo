/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ReverseTcpClientTransportTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:reverse:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/test";

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;

  @AfterEach
  void tearDown() {
    if (executor != null) {
      executor.shutdownNow();
    }
    if (scheduledExecutor != null) {
      scheduledExecutor.shutdownNow();
    }
  }

  @Test
  void directChannelCloseReplacesCompletedChannelFutureWithTerminalFailure() throws Exception {
    EmbeddedChannel channel = new EmbeddedChannel();
    ReverseTcpClientTransport transport = newTransport(channel);
    CompletableFuture<Channel> completedFuture = CompletableFuture.completedFuture(channel);
    CountDownLatch disconnected = new CountDownLatch(1);

    setField(transport, "started", true);
    setField(transport, "disconnecting", false);
    setField(transport, "directConnectionConsumed", true);
    setField(transport, "currentChannel", channel);
    setField(transport, "channelFuture", completedFuture);

    transport.addTransitionListener(
        connected -> {
          if (!connected) {
            disconnected.countDown();
          }
        });

    invokeOnChannelClosed(transport, channel);

    assertNull(transport.getCurrentChannel());
    assertTrue(disconnected.await(5, TimeUnit.SECONDS));

    CompletableFuture<Channel> terminalFuture = channelFuture(transport);
    assertNotSame(completedFuture, terminalFuture);
    assertTerminalConnectionClosed(terminalFuture);
  }

  @Test
  void directDisconnectThenClientReconnectFailsInsteadOfWaitingForChannel() throws Exception {
    EmbeddedChannel channel = new EmbeddedChannel();
    ReverseConnectConnection connection = newConnection(channel);
    OpcUaClient client =
        OpcUaClient.createReverseConnect(
            clientConfig(),
            connection,
            builder -> builder.setExecutor(executor()).setScheduledExecutor(scheduledExecutor()));

    client.disconnectAsync().get(5, TimeUnit.SECONDS);

    CompletableFuture<OpcUaClient> reconnect = client.connectAsync();

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> reconnect.get(5, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, unwrap(exception.getCause()));

    assertEquals(StatusCodes.Bad_ConnectionClosed, cause.getStatusCode().value());
  }

  @Test
  void blankManagerClaimRearmsBeforeFailingOldFuture() throws Exception {
    ReverseConnectManager manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
            .setExecutor(executor())
            .setScheduler(scheduledExecutor())
            .build();
    ReverseTcpClientTransport transport =
        new ReverseTcpClientTransport(transportConfig(), manager, ReverseConnectSelector.any());
    CompletableFuture<Channel> originalFuture = new CompletableFuture<>();
    EmbeddedChannel channel = new EmbeddedChannel();

    try {
      setField(transport, "started", true);
      setField(transport, "disconnecting", false);
      setField(transport, "channelFuture", originalFuture);

      invokeInitializeClaimedConnection(transport, newConnection(channel, " "), originalFuture);

      ExecutionException exception =
          assertThrows(ExecutionException.class, () -> originalFuture.get(5, TimeUnit.SECONDS));
      UaException cause = assertInstanceOf(UaException.class, unwrap(exception.getCause()));

      assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, cause.getStatusCode().value());
      assertFalse(channel.isOpen());

      CompletableFuture<Channel> nextFuture = channelFuture(transport);
      assertNotSame(originalFuture, nextFuture);
      assertFalse(nextFuture.isDone());
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void transitionListenersAreNotReorderedByExecutorDispatch() throws Exception {
    QueuingExecutorService queuedExecutor = new QueuingExecutorService();
    executor = queuedExecutor;
    EmbeddedChannel channel = new EmbeddedChannel();
    ReverseTcpClientTransport transport =
        new ReverseTcpClientTransport(transportConfig(queuedExecutor), newConnection(channel));
    CompletableFuture<Channel> activeFuture = new CompletableFuture<>();
    List<Boolean> transitions = new ArrayList<>();

    setField(transport, "started", true);
    setField(transport, "disconnecting", false);
    setField(transport, "channelFuture", activeFuture);

    transport.addTransitionListener(transitions::add);

    invokeCompleteHandshake(transport, activeFuture, channel);
    invokeOnChannelClosed(transport, channel);

    assertEquals(List.of(true, false), transitions);
    assertEquals(0, queuedExecutor.pendingTaskCount());
  }

  private ReverseTcpClientTransport newTransport(EmbeddedChannel channel) {
    return new ReverseTcpClientTransport(transportConfig(), newConnection(channel));
  }

  private OpcTcpClientTransportConfig transportConfig() {
    return transportConfig(executor());
  }

  private OpcTcpClientTransportConfig transportConfig(ExecutorService executor) {
    return OpcTcpClientTransportConfig.newBuilder()
        .setExecutor(executor)
        .setScheduledExecutor(scheduledExecutor())
        .build();
  }

  private ExecutorService executor() {
    if (executor == null) {
      executor = Executors.newSingleThreadExecutor();
    }
    return executor;
  }

  private ScheduledExecutorService scheduledExecutor() {
    if (scheduledExecutor == null) {
      scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }
    return scheduledExecutor;
  }

  private static ReverseConnectConnection newConnection(Channel channel) {
    return newConnection(channel, ENDPOINT_URL);
  }

  private static ReverseConnectConnection newConnection(Channel channel, String endpointUrl) {
    Instant now = Instant.now();

    return new ReverseConnectConnection(
        channel,
        new ReverseConnectCandidateSnapshot(
            UUID.randomUUID(),
            ReverseConnectCandidateState.CLAIMED,
            SERVER_URI,
            endpointUrl,
            null,
            null,
            now,
            now,
            now,
            null,
            null,
            null));
  }

  private static OpcUaClientConfig clientConfig() {
    return OpcUaClientConfig.builder()
        .setEndpoint(endpoint())
        .setDiscoveryEndpoints(List.of())
        .build();
  }

  private static EndpointDescription endpoint() {
    var server =
        new ApplicationDescription(
            SERVER_URI,
            "urn:eclipse:milo:test:product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            null);

    var anonymousPolicy =
        new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

    return new EndpointDescription(
        ENDPOINT_URL,
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {anonymousPolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static void assertTerminalConnectionClosed(CompletableFuture<?> future) {
    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, unwrap(exception.getCause()));

    assertEquals(StatusCodes.Bad_ConnectionClosed, cause.getStatusCode().value());
  }

  private static void invokeOnChannelClosed(ReverseTcpClientTransport transport, Channel channel)
      throws Exception {

    Method method =
        ReverseTcpClientTransport.class.getDeclaredMethod("onChannelClosed", Channel.class);
    method.setAccessible(true);
    method.invoke(transport, channel);
  }

  private static void invokeCompleteHandshake(
      ReverseTcpClientTransport transport, CompletableFuture<Channel> targetFuture, Channel channel)
      throws Exception {

    Method method =
        ReverseTcpClientTransport.class.getDeclaredMethod(
            "completeHandshake", CompletableFuture.class, Channel.class);
    method.setAccessible(true);
    method.invoke(transport, targetFuture, channel);
  }

  private static void invokeInitializeClaimedConnection(
      ReverseTcpClientTransport transport,
      ReverseConnectConnection connection,
      CompletableFuture<Channel> targetFuture)
      throws Exception {

    Method method =
        ReverseTcpClientTransport.class.getDeclaredMethod(
            "initializeClaimedConnection",
            ReverseConnectConnection.class,
            CompletableFuture.class,
            boolean.class);
    method.setAccessible(true);
    method.invoke(transport, connection, targetFuture, true);
  }

  private static void setField(Object target, String name, Object value) throws Exception {
    Field field = ReverseTcpClientTransport.class.getDeclaredField(name);
    field.setAccessible(true);
    field.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private static CompletableFuture<Channel> channelFuture(ReverseTcpClientTransport transport)
      throws Exception {

    Field field = ReverseTcpClientTransport.class.getDeclaredField("channelFuture");
    field.setAccessible(true);
    return (CompletableFuture<Channel>) field.get(transport);
  }

  private static Throwable unwrap(Throwable t) {
    if (t instanceof CompletionException && t.getCause() != null) {
      return unwrap(t.getCause());
    } else {
      return t;
    }
  }

  private static final class QueuingExecutorService extends AbstractExecutorService {

    private final List<Runnable> tasks = Collections.synchronizedList(new ArrayList<>());

    private volatile boolean shutdown;

    @Override
    public void shutdown() {
      shutdown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
      shutdown = true;

      synchronized (tasks) {
        List<Runnable> queued = new ArrayList<>(tasks);
        tasks.clear();
        return queued;
      }
    }

    @Override
    public boolean isShutdown() {
      return shutdown;
    }

    @Override
    public boolean isTerminated() {
      return shutdown;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      return shutdown;
    }

    @Override
    public void execute(Runnable command) {
      tasks.add(command);
    }

    int pendingTaskCount() {
      return tasks.size();
    }
  }
}
