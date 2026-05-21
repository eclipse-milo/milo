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

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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
  void partialStartupFailureDoesNotEmitOrphanListenerUnbound() throws Exception {
    RecordingListener listener = new RecordingListener();

    try (ServerSocket occupiedSocket = new ServerSocket()) {
      occupiedSocket.bind(new InetSocketAddress("127.0.0.1", 0));
      InetSocketAddress occupiedAddress =
          assertInstanceOf(InetSocketAddress.class, occupiedSocket.getLocalSocketAddress());

      manager = newManagerBuilder().addBindAddress(occupiedAddress).build();
      manager.addListener(listener);

      assertThrows(Exception.class, () -> manager.startup());

      ReverseConnectListenerSnapshot failedListener =
          manager.snapshot().listeners().stream()
              .filter(snapshot -> occupiedAddress.equals(snapshot.bindAddress()))
              .findFirst()
              .orElseThrow();

      assertFalse(failedListener.bound());
      assertNotNull(failedListener.lastError());
    }

    assertEquals(List.of("bound", "unbound"), listener.listenerEvents);
    assertEquals(1, listener.boundListeners.size());
    assertEquals(1, listener.unboundListeners.size());
    assertTrue(listener.boundListeners.get(0).bound());
    assertFalse(listener.unboundListeners.get(0).bound());
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
  void firstMessageTimeoutDoesNotRejectCandidateAfterReverseHelloReceived() throws Exception {
    RecordingListener listener = new RecordingListener();
    CountDownLatch verifierEntered = new CountDownLatch(1);
    CountDownLatch releaseVerifier = new CountDownLatch(1);

    manager =
        newManagerBuilder()
            .setPendingConnectionHoldTime(Duration.ofSeconds(5))
            .setReverseHelloVerifier(
                candidate -> {
                  verifierEntered.countDown();
                  awaitVerifierRelease(releaseVerifier);
                  return ReverseConnectVerificationResult.accept();
                })
            .build();
    manager.addListener(listener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      assertTrue(verifierEntered.await(3, TimeUnit.SECONDS));

      UUID candidateId = listener.accepted.get(0).id();
      invokeFirstMessageTimeout(manager, candidateId);

      ReverseConnectManagerSnapshot snapshot = manager.snapshot();
      assertEquals(0, snapshot.rejectedCount());
      assertTrue(listener.rejected.isEmpty());

      releaseVerifier.countDown();
      waitUntil(() -> !listener.pending.isEmpty());

      assertEquals(candidateId, listener.pending.get(0).id());
    } finally {
      releaseVerifier.countDown();
    }
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
  void closeWithErrorReleasesEncodedBufferWhenWriteAndFlushThrows() throws Exception {
    manager = newManagerBuilder(new ManualScheduledExecutor()).build();
    ThrowingWriteChannel channel = new ThrowingWriteChannel();

    invokeCloseWithError(
        manager, channel, new StatusCode(StatusCodes.Bad_TcpInternalError), "write failed");

    assertEquals(0, channel.errorBuffer().refCnt());
    assertFalse(channel.isOpen());
  }

  @Test
  void forcedCloseTimeoutIsCancelledAfterErrorCloseCompletes() throws Exception {
    ManualScheduledExecutor manualScheduler = new ManualScheduledExecutor();
    RecordingListener listener = new RecordingListener();

    manager =
        newManagerBuilder(manualScheduler)
            .setFirstMessageTimeout(Duration.ofSeconds(10))
            .setReverseHelloVerifier(
                candidate -> ReverseConnectVerificationResult.reject("blocked"))
            .build();
    manager.addListener(listener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      waitUntil(() -> !listener.rejected.isEmpty());
      waitUntil(
          () ->
              manualScheduler
                  .taskWithDelay(Duration.ofSeconds(2))
                  .map(ManualScheduledFuture::isCancelled)
                  .orElse(false));
    }

    ManualScheduledFuture<?> forcedCloseFuture =
        manualScheduler.taskWithDelay(Duration.ofSeconds(2)).orElseThrow();

    assertFalse(forcedCloseFuture.runIfPending());
    assertEquals(0, forcedCloseFuture.runCount());
  }

  @Test
  void firstMessageTimeoutIsCancelledWhenChannelClosesBeforeReverseHello() throws Exception {
    Duration firstMessageTimeout = Duration.ofSeconds(10);
    ManualScheduledExecutor manualScheduler = new ManualScheduledExecutor();
    RecordingListener listener = new RecordingListener();

    manager =
        newManagerBuilder(manualScheduler).setFirstMessageTimeout(firstMessageTimeout).build();
    manager.addListener(listener);
    manager.startup();

    try (Socket socket = new Socket()) {
      socket.connect(boundAddress(manager));
      waitUntil(() -> manualScheduler.taskWithDelay(firstMessageTimeout).isPresent());
    }

    waitUntil(() -> !listener.rejected.isEmpty());
    waitUntil(
        () ->
            manualScheduler
                .taskWithDelay(firstMessageTimeout)
                .map(ManualScheduledFuture::isCancelled)
                .orElse(false));

    ReverseConnectCandidateSnapshot rejected = listener.rejected.get(0);
    assertEquals(ReverseConnectRejectionReason.CHANNEL_CLOSED, rejected.rejectionReason());

    ManualScheduledFuture<?> timeoutFuture =
        manualScheduler.taskWithDelay(firstMessageTimeout).orElseThrow();

    assertFalse(timeoutFuture.runIfPending());
    assertEquals(0, timeoutFuture.runCount());
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
  void byCandidateIdMatchesOnlyTheRequestedCandidate() {
    UUID candidateId = UUID.randomUUID();
    UUID otherCandidateId = UUID.randomUUID();
    ReverseConnectSelector selector = ReverseConnectSelector.byCandidateId(candidateId);

    assertTrue(selector.matches(candidate(candidateId)));
    assertFalse(selector.matches(candidate(otherCandidateId)));
  }

  @Test
  void claimPendingCandidateTransfersOwnership() throws Exception {
    RecordingListener listener = new RecordingListener();

    manager = newManagerBuilder().setPendingConnectionHoldTime(Duration.ofSeconds(5)).build();
    manager.addListener(listener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      waitUntil(() -> !listener.pending.isEmpty());

      ReverseConnectCandidateSnapshot pending = listener.pending.get(0);
      Optional<ReverseConnectConnection> claimed = manager.claim(pending.id());

      assertTrue(claimed.isPresent());
      assertEquals(pending.id(), claimed.orElseThrow().candidateId());
      waitUntil(() -> !listener.claimed.isEmpty());

      ReverseConnectManagerSnapshot snapshot = manager.snapshot();
      assertEquals(1, snapshot.claimedCount());
      assertTrue(snapshot.pendingCandidates().isEmpty());
      assertEquals(pending.id(), snapshot.acceptedCandidates().get(0).id());

      assertTrue(manager.claim(pending.id()).isEmpty());

      manager.shutdown();
      assertTrue(claimed.orElseThrow().channel().isOpen());

      claimed.orElseThrow().close().sync();
    }
  }

  @Test
  void claimReturnsEmptyForExpiredAndRejectedCandidates() throws Exception {
    RecordingListener expiringListener = new RecordingListener();

    manager = newManagerBuilder().setPendingConnectionHoldTime(Duration.ofMillis(50)).build();
    manager.addListener(expiringListener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      waitUntil(() -> !expiringListener.pending.isEmpty());
      UUID expiredCandidateId = expiringListener.pending.get(0).id();
      waitUntil(() -> !expiringListener.expired.isEmpty());
      assertTrue(manager.claim(expiredCandidateId).isEmpty());
    }

    manager.shutdown();
    eventLoop.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS).sync();
    scheduler.shutdownNow();
    eventLoop = null;
    scheduler = null;
    RecordingListener rejectingListener = new RecordingListener();

    manager =
        newManagerBuilder()
            .setReverseHelloVerifier(
                candidate -> ReverseConnectVerificationResult.reject("blocked"))
            .build();
    manager.addListener(rejectingListener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      waitUntil(() -> !rejectingListener.rejected.isEmpty());
      assertTrue(manager.claim(rejectingListener.rejected.get(0).id()).isEmpty());
    }
  }

  @Test
  void rejectPendingCandidateClosesAndReportsApplicationRejection() throws Exception {
    RecordingListener listener = new RecordingListener();

    manager = newManagerBuilder().setPendingConnectionHoldTime(Duration.ofSeconds(5)).build();
    manager.addListener(listener);
    manager.startup();

    try (Socket ignored = sendReverseHello(boundAddress(manager), endpointUrl())) {
      waitUntil(() -> !listener.pending.isEmpty());
      UUID candidateId = listener.pending.get(0).id();

      assertTrue(
          manager.reject(
              candidateId,
              new StatusCode(StatusCodes.Bad_TcpEndpointUrlInvalid),
              "application rejected"));
      assertFalse(
          manager.reject(
              candidateId,
              new StatusCode(StatusCodes.Bad_TcpEndpointUrlInvalid),
              "already rejected"));

      waitUntil(() -> !listener.rejected.isEmpty());
    }

    ReverseConnectCandidateSnapshot rejected = listener.rejected.get(0);
    assertEquals(ReverseConnectCandidateState.REJECTED, rejected.state());
    assertEquals(ReverseConnectRejectionReason.APPLICATION_REJECTED, rejected.rejectionReason());
    assertEquals(
        StatusCodes.Bad_TcpEndpointUrlInvalid,
        requireNonNull(rejected.rejectionStatusCode()).value());
    assertEquals("application rejected", rejected.diagnostic());
    assertTrue(manager.snapshot().pendingCandidates().isEmpty());
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
    return newManagerBuilder(Executors.newSingleThreadScheduledExecutor());
  }

  private ReverseConnectManagerBuilder newManagerBuilder(
      ScheduledExecutorService scheduledExecutor) {
    eventLoop = new NioEventLoopGroup(1);
    scheduler = scheduledExecutor;

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

  private static ReverseConnectCandidateSnapshot candidate(UUID candidateId) {
    return new ReverseConnectCandidateSnapshot(
        candidateId,
        ReverseConnectCandidateState.PENDING,
        SERVER_URI,
        endpointUrl(),
        null,
        null,
        Instant.now(),
        Instant.now(),
        null,
        null,
        null,
        null);
  }

  private static void invokeFirstMessageTimeout(ReverseConnectManager manager, UUID candidateId)
      throws Exception {

    Method method =
        ReverseConnectManager.class.getDeclaredMethod("onFirstMessageTimeout", UUID.class);
    method.setAccessible(true);
    method.invoke(manager, candidateId);
  }

  private static void invokeCloseWithError(
      ReverseConnectManager manager, Channel channel, StatusCode statusCode, String diagnostic)
      throws Exception {

    Method method =
        ReverseConnectManager.class.getDeclaredMethod(
            "closeWithError", Channel.class, StatusCode.class, String.class);
    method.setAccessible(true);
    method.invoke(manager, channel, statusCode, diagnostic);
  }

  private static void awaitVerifierRelease(CountDownLatch releaseVerifier) {
    try {
      if (!releaseVerifier.await(3, TimeUnit.SECONDS)) {
        throw new AssertionError("verifier was not released");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AssertionError(e);
    }
  }

  private static void waitUntil(BooleanSupplier condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);

    while (!condition.getAsBoolean() && System.nanoTime() < deadline) {
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }

    assertTrue(condition.getAsBoolean());
  }

  private static final class ThrowingWriteChannel extends EmbeddedChannel {

    private @Nullable ByteBuf errorBuffer;

    @Override
    public ChannelFuture writeAndFlush(Object msg) {
      errorBuffer = (ByteBuf) msg;
      throw new IllegalStateException("write failed");
    }

    ByteBuf errorBuffer() {
      ByteBuf captured = errorBuffer;
      assertNotNull(captured);
      return captured;
    }
  }

  private static final class ManualScheduledExecutor extends AbstractExecutorService
      implements ScheduledExecutorService {

    private final List<ManualScheduledFuture<?>> tasks = new CopyOnWriteArrayList<>();
    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    Optional<ManualScheduledFuture<?>> taskWithDelay(Duration delay) {
      long delayNanos = delay.toNanos();

      return tasks.stream().filter(task -> task.delayNanos == delayNanos).findFirst();
    }

    @Override
    public void shutdown() {
      shutdown.set(true);
    }

    @Override
    public List<Runnable> shutdownNow() {
      shutdown.set(true);
      return List.of();
    }

    @Override
    public boolean isShutdown() {
      return shutdown.get();
    }

    @Override
    public boolean isTerminated() {
      return shutdown.get();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      return true;
    }

    @Override
    public void execute(Runnable command) {
      command.run();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
      return schedule(Executors.callable(command, null), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
      ManualScheduledFuture<V> task = new ManualScheduledFuture<>(callable, delay, unit);

      tasks.add(task);

      return task;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(
        Runnable command, long initialDelay, long period, TimeUnit unit) {

      throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(
        Runnable command, long initialDelay, long delay, TimeUnit unit) {

      throw new UnsupportedOperationException();
    }
  }

  private static final class ManualScheduledFuture<V> implements ScheduledFuture<V> {

    private final Callable<V> callable;
    private final long delayNanos;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicBoolean done = new AtomicBoolean(false);
    private final AtomicInteger runCount = new AtomicInteger();

    private @Nullable V value;
    private @Nullable Throwable failure;

    ManualScheduledFuture(Callable<V> callable, long delay, TimeUnit unit) {
      this.callable = callable;
      this.delayNanos = unit.toNanos(delay);
    }

    boolean runIfPending() {
      if (cancelled.get() || !done.compareAndSet(false, true)) {
        return false;
      }

      runCount.incrementAndGet();

      try {
        value = callable.call();
      } catch (Throwable t) {
        failure = t;
      }

      return true;
    }

    int runCount() {
      return runCount.get();
    }

    @Override
    public long getDelay(TimeUnit unit) {
      return unit.convert(delayNanos, TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
      return Long.compare(getDelay(TimeUnit.NANOSECONDS), other.getDelay(TimeUnit.NANOSECONDS));
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
      if (!done.compareAndSet(false, true)) {
        return false;
      }

      cancelled.set(true);

      return true;
    }

    @Override
    public boolean isCancelled() {
      return cancelled.get();
    }

    @Override
    public boolean isDone() {
      return done.get();
    }

    @Override
    public V get() throws ExecutionException {
      if (cancelled.get()) {
        throw new CancellationException();
      }
      if (!done.get()) {
        throw new IllegalStateException("task has not run");
      }
      if (failure != null) {
        throw new ExecutionException(failure);
      }

      return value;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
      if (!done.get()) {
        throw new TimeoutException();
      }

      return get();
    }
  }

  private static final class RecordingListener implements ReverseConnectListener {

    final List<String> events = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> accepted = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> pending = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> claimed = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> rejected = new CopyOnWriteArrayList<>();
    final List<ReverseConnectCandidateSnapshot> expired = new CopyOnWriteArrayList<>();
    final List<String> listenerEvents = new CopyOnWriteArrayList<>();
    final List<ReverseConnectListenerSnapshot> boundListeners = new CopyOnWriteArrayList<>();
    final List<ReverseConnectListenerSnapshot> unboundListeners = new CopyOnWriteArrayList<>();

    @Override
    public void onListenerBound(@NonNull ReverseConnectListenerSnapshot snapshot) {
      listenerEvents.add("bound");
      boundListeners.add(snapshot);
    }

    @Override
    public void onListenerUnbound(@NonNull ReverseConnectListenerSnapshot snapshot) {
      listenerEvents.add("unbound");
      unboundListeners.add(snapshot);
    }

    @Override
    public void onCandidateAccepted(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      events.add("accepted");
      accepted.add(snapshot);
    }

    @Override
    public void onCandidatePending(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      events.add("pending");
      pending.add(snapshot);
    }

    @Override
    public void onCandidateClaimed(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      events.add("claimed");
      claimed.add(snapshot);
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
