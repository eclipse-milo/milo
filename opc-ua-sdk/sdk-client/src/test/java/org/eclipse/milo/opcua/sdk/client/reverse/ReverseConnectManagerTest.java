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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ReverseConnectManagerTest {

  private static final String SERVER_URI = "urn:server";

  private EventLoopGroup eventLoop;
  private ScheduledExecutorService scheduler;
  private ReverseConnectManager manager;

  @AfterEach
  void tearDown() throws Exception {
    if (manager != null) {
      manager.shutdown();
    }
    if (eventLoop != null) {
      eventLoop.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS).sync();
    }
    if (scheduler != null) {
      scheduler.shutdownNow();
    }
  }

  @Test
  void startupAndShutdownBindAndUnbindListeners() throws Exception {
    manager = newManagerBuilder().build();

    manager.startup();

    ReverseConnectManagerSnapshot started = manager.snapshot();
    assertTrue(started.running());
    assertEquals(1, started.listeners().size());
    assertTrue(started.listeners().get(0).bound());
    assertInstanceOf(InetSocketAddress.class, started.listeners().get(0).boundAddress());

    manager.shutdown();

    ReverseConnectManagerSnapshot stopped = manager.snapshot();
    assertFalse(stopped.running());
    assertFalse(stopped.listeners().get(0).bound());
  }

  @Test
  void shutdownCancelsPreStartupRegistrations() {
    manager = newManagerBuilder().build();

    ReverseConnectRegistration registration =
        manager.registerSelector(ReverseConnectSelector.any());

    manager.shutdown();

    assertTrue(registration.connectionFuture().isCompletedExceptionally());
  }

  @Test
  void shutdownClosesListenerBeforeReturning() throws Exception {
    manager = newManagerBuilder().build();
    manager.startup();
    InetSocketAddress address = boundAddress(manager);

    manager.shutdown();

    try (ServerSocket socket = new ServerSocket()) {
      socket.bind(address);
    }
  }

  @Test
  void malformedFirstMessageIsRejected() throws Exception {
    RecordingListener listener = new RecordingListener();

    manager = newManagerBuilder().build();
    manager.addListener(listener);
    manager.startup();

    try (Socket socket = new Socket()) {
      socket.connect(boundAddress(manager));
      writeMessage(socket, TcpMessageEncoder.encode(newHelloMessage()));

      waitUntil(() -> !listener.rejected.isEmpty());
    }

    ReverseConnectCandidateSnapshot rejected = listener.rejected.get(0);
    assertEquals(ReverseConnectCandidateState.REJECTED, rejected.state());
    assertEquals(ReverseConnectRejectionReason.MALFORMED_REVERSE_HELLO, rejected.rejectionReason());
    assertNotNull(rejected.rejectionStatusCode());
    assertEquals(StatusCodes.Bad_TcpMessageTypeInvalid, rejected.rejectionStatusCode().value());
  }

  @Test
  void lateFirstMessageIsRejected() throws Exception {
    RecordingListener listener = new RecordingListener();

    manager = newManagerBuilder().setFirstMessageTimeout(Duration.ofMillis(50)).build();
    manager.addListener(listener);
    manager.startup();

    try (Socket ignored = new Socket()) {
      ignored.connect(boundAddress(manager));

      waitUntil(() -> !listener.rejected.isEmpty());
    }

    ReverseConnectCandidateSnapshot rejected = listener.rejected.get(0);
    assertEquals(ReverseConnectCandidateState.REJECTED, rejected.state());
    assertEquals(ReverseConnectRejectionReason.FIRST_MESSAGE_TIMEOUT, rejected.rejectionReason());
  }

  @Test
  void verifierRejectionClosesCandidate() throws Exception {
    RecordingListener listener = new RecordingListener();

    manager =
        newManagerBuilder()
            .setReverseHelloVerifier(
                candidate ->
                    ReverseConnectVerificationResult.reject(
                        ReverseConnectRejectionReason.VERIFIER_REJECTED,
                        new StatusCode(StatusCodes.Bad_TcpEndpointUrlInvalid),
                        "blocked"))
            .build();
    manager.addListener(listener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      waitUntil(() -> !listener.rejected.isEmpty());
    }

    ReverseConnectCandidateSnapshot rejected = listener.rejected.get(0);
    assertEquals(ReverseConnectRejectionReason.VERIFIER_REJECTED, rejected.rejectionReason());
    assertEquals(SERVER_URI, rejected.serverUri());
    assertEquals(endpointUrl(), rejected.endpointUrl());
  }

  @Test
  void terminalCandidateHistoryIsBounded() throws Exception {
    manager =
        newManagerBuilder()
            .setMaxRetainedCandidateSnapshots(1)
            .setReverseHelloVerifier(
                candidate -> ReverseConnectVerificationResult.reject("blocked"))
            .build();
    manager.startup();

    try (Socket ignoredFirst = sendReverseHello(boundAddress(manager), endpointUrl());
        Socket ignoredSecond = sendReverseHello(boundAddress(manager), endpointUrl())) {

      waitUntil(() -> manager.snapshot().rejectedCount() == 2);
    }

    ReverseConnectManagerSnapshot snapshot = manager.snapshot();
    assertEquals(2, snapshot.rejectedCount());
    assertEquals(1, snapshot.rejectedCandidates().size());
  }

  @Test
  void unclaimedCandidateExpiresAndSnapshotsAreImmutable() throws Exception {
    RecordingListener listener = new RecordingListener();

    manager = newManagerBuilder().setPendingConnectionHoldTime(Duration.ofMillis(500)).build();
    manager.addListener(listener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      waitUntil(() -> !listener.pending.isEmpty());

      List<ReverseConnectCandidateSnapshot> pending = manager.snapshot().pendingCandidates();
      assertEquals(1, pending.size());
      assertThrows(UnsupportedOperationException.class, () -> pending.add(pending.get(0)));

      waitUntil(() -> !listener.expired.isEmpty());
    }

    ReverseConnectCandidateSnapshot expired = listener.expired.get(0);
    assertEquals(ReverseConnectCandidateState.EXPIRED, expired.state());
    assertEquals(ReverseConnectRejectionReason.PENDING_EXPIRED, expired.rejectionReason());

    assertTrue(listener.events.indexOf("accepted") < listener.events.indexOf("pending"));
    assertTrue(listener.events.indexOf("pending") < listener.events.indexOf("expired"));
  }

  @Test
  void concurrentArrivalsOnlyClaimOneSelector() throws Exception {
    RecordingListener listener = new RecordingListener();
    ExecutorService arrivals = Executors.newFixedThreadPool(2);

    manager = newManagerBuilder().setPendingConnectionHoldTime(Duration.ofSeconds(1)).build();
    manager.addListener(listener);
    manager.startup();

    ReverseConnectRegistration registration =
        manager.registerSelector(ReverseConnectSelector.any());

    CyclicBarrier barrier = new CyclicBarrier(3);
    Callable<Socket> arrival =
        () -> {
          barrier.await();
          return sendReverseHello(boundAddress(manager), endpointUrl());
        };

    Future<Socket> first = arrivals.submit(arrival);
    Future<Socket> second = arrivals.submit(arrival);

    barrier.await();

    try (Socket ignoredFirst = first.get(2, TimeUnit.SECONDS);
        Socket ignoredSecond = second.get(2, TimeUnit.SECONDS)) {

      ReverseConnectConnection connection =
          registration.connectionFuture().get(2, TimeUnit.SECONDS);

      waitUntil(() -> manager.snapshot().acceptedCount() == 2);

      assertEquals(1, manager.snapshot().claimedCount());
      assertEquals(1, manager.snapshot().acceptedCandidates().size());
      assertEquals(connection.candidateId(), manager.snapshot().acceptedCandidates().get(0).id());

      connection.close().sync();
    } finally {
      arrivals.shutdownNow();
    }
  }

  private ReverseConnectManagerBuilder newManagerBuilder() {
    eventLoop = new NioEventLoopGroup(1);
    scheduler = Executors.newSingleThreadScheduledExecutor();

    return ReverseConnectManager.builder()
        .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
        .setExecutor(Runnable::run)
        .setScheduler(scheduler)
        .setEventLoop(eventLoop)
        .setFirstMessageTimeout(Duration.ofSeconds(1))
        .setPendingConnectionHoldTime(Duration.ofSeconds(1));
  }

  private static InetSocketAddress boundAddress(ReverseConnectManager manager) {
    SocketAddress boundAddress = manager.snapshot().listeners().get(0).boundAddress();

    return assertInstanceOf(InetSocketAddress.class, boundAddress);
  }

  private static Socket sendReverseHello(InetSocketAddress bindAddress, String endpointUrl)
      throws Exception {

    Socket socket = new Socket();
    socket.connect(bindAddress);
    writeMessage(
        socket, TcpMessageEncoder.encode(new ReverseHelloMessage(SERVER_URI, endpointUrl)));
    return socket;
  }

  private static void writeMessage(Socket socket, ByteBuf buffer) throws Exception {
    try {
      byte[] bytes = new byte[buffer.readableBytes()];
      buffer.readBytes(bytes);

      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(bytes);
      outputStream.flush();
    } finally {
      buffer.release();
    }
  }

  private static HelloMessage newHelloMessage() {
    return new HelloMessage(0L, 8192L, 8192L, 8192L, 8L, endpointUrl());
  }

  private static String endpointUrl() {
    return "opc.tcp://localhost:12685/milo";
  }

  private static void waitUntil(BooleanSupplier condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);

    while (!condition.getAsBoolean() && System.nanoTime() < deadline) {
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }

    assertTrue(condition.getAsBoolean());
  }

  private static final class RecordingListener implements ReverseConnectListener {

    final List<String> events = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> pending = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> rejected = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> expired = new CopyOnWriteArrayList<>();

    @Override
    public void onCandidateAccepted(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      events.add("accepted");
    }

    @Override
    public void onCandidatePending(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      events.add("pending");
      pending.add(snapshot);
    }

    @Override
    public void onCandidateRejected(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      events.add("rejected");
      rejected.add(snapshot);
    }

    @Override
    public void onCandidateExpired(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      events.add("expired");
      expired.add(snapshot);
    }
  }
}
