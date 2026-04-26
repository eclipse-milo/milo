/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.digitalpetri.fsm.Fsm;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConnectionFsm.Event;
import org.eclipse.milo.opcua.stack.transport.server.tcp.ReverseConnectConnectionFsm.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReverseConnectConnectionFsmTest {

  private static final String CLIENT_ENDPOINT_URL = "opc.tcp://localhost:48060";
  private static final String ENDPOINT_URL = "opc.tcp://server-host:4840";
  private static final String SERVER_URI = "urn:test:server";

  private static final Duration CONNECT_INTERVAL = Duration.ofMillis(100);
  private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
  private static final Duration REJECT_BACKOFF = Duration.ofMillis(500);
  private static final Duration MAX_RECONNECT_DELAY = Duration.ofMillis(800);

  private ExecutorService executor;
  private ScheduledExecutorService scheduler;
  private StubTransport stubTransport;

  @BeforeEach
  void setUp() {
    executor = Executors.newSingleThreadExecutor();
    scheduler = Executors.newSingleThreadScheduledExecutor();
    stubTransport = new StubTransport();
  }

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
    scheduler.shutdownNow();
  }

  @Test
  void happyPathLifecycle() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    // Idle -> Connecting
    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    // Connecting -> RheSent
    CompletableFuture<Channel> connectFuture = stubTransport.pollConnectFuture();
    assertNotNull(connectFuture);
    var channel = new EmbeddedChannel();
    connectFuture.complete(channel);
    awaitState(fsm, State.RheSent);

    // RheSent -> Active
    fsm.fireEvent(new Event.SecureChannelOpened());
    awaitState(fsm, State.Active);

    // Verify backoff was reset
    Long retryDelay = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_DELAY_MS::get);
    assertNull(retryDelay);

    // Active -> ConnectWait (channel closed)
    fsm.fireEvent(new Event.ChannelInactive());
    awaitState(fsm, State.ConnectWait);

    // ConnectWait -> Connecting (retry timer fires)
    awaitState(fsm, State.Connecting, Duration.ofSeconds(5));
  }

  @Test
  void fastSecureChannelOpenedOutcomeIsReplayedAfterConnectSuccess() throws Exception {
    stubTransport.completeWithSecureChannelOpenBeforeReturning();
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());

    awaitState(fsm, State.Active);

    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEvent(new Event.Stop(stopFuture));
    awaitState(fsm, State.Disconnected);
    assertTrue(stopFuture.isDone());
  }

  @Test
  void connectFailureAndExponentialBackoff() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    // First failure
    CompletableFuture<Channel> f1 = stubTransport.pollConnectFuture();
    f1.completeExceptionally(new Exception("connection refused"));
    awaitState(fsm, State.ConnectWait);

    Long delay1 = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_DELAY_MS::get);
    assertNotNull(delay1);

    // Wait for retry -> Connecting
    awaitState(fsm, State.Connecting, Duration.ofSeconds(5));

    // Second failure
    CompletableFuture<Channel> f2 = stubTransport.pollConnectFuture();
    assertNotNull(f2);
    f2.completeExceptionally(new Exception("connection refused"));
    awaitState(fsm, State.ConnectWait);

    Long delay2 = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_DELAY_MS::get);
    assertNotNull(delay2);

    // Verify exponential backoff: delay2 should be double delay1
    assertTrue(delay2 > delay1, "delay2 (" + delay2 + ") should be > delay1 (" + delay1 + ")");

    // Verify max cap
    assertTrue(
        delay2 <= MAX_RECONNECT_DELAY.toMillis(),
        "delay2 (" + delay2 + ") should be <= max (" + MAX_RECONNECT_DELAY.toMillis() + ")");
  }

  @Test
  void clientRejectionOverridesBackoff() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    // Reach RheSent
    CompletableFuture<Channel> f = stubTransport.pollConnectFuture();
    f.complete(new EmbeddedChannel());
    awaitState(fsm, State.RheSent);

    // Client rejects -> ConnectWait with reject backoff
    fsm.fireEvent(new Event.ClientRejected(0x80000000L));
    awaitState(fsm, State.ConnectWait);

    Long delay = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_DELAY_MS::get);
    assertNotNull(delay);
    // The next delay should be based on REJECT_BACKOFF.toMillis() (doubled by backoff logic)
    // The entry action uses the reject backoff as the base, then doubles it for next
    long expectedNextDelay =
        Math.min(REJECT_BACKOFF.toMillis() * 2, MAX_RECONNECT_DELAY.toMillis());
    assertEquals(expectedNextDelay, delay);
  }

  @Test
  void backoffResetsOnSuccessfulConnection() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    // Induce failures to build up backoff
    CompletableFuture<Channel> f1 = stubTransport.pollConnectFuture();
    f1.completeExceptionally(new Exception("fail"));
    awaitState(fsm, State.ConnectWait);

    awaitState(fsm, State.Connecting, Duration.ofSeconds(5));
    CompletableFuture<Channel> f2 = stubTransport.pollConnectFuture();
    f2.completeExceptionally(new Exception("fail"));
    awaitState(fsm, State.ConnectWait);

    // Now succeed
    awaitState(fsm, State.Connecting, Duration.ofSeconds(5));
    CompletableFuture<Channel> f3 = stubTransport.pollConnectFuture();
    f3.complete(new EmbeddedChannel());
    awaitState(fsm, State.RheSent);

    fsm.fireEvent(new Event.SecureChannelOpened());
    awaitState(fsm, State.Active);

    // Backoff should be reset
    Long retryDelay = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_DELAY_MS::get);
    assertNull(retryDelay);

    // Go back to ConnectWait
    fsm.fireEvent(new Event.ChannelInactive());
    awaitState(fsm, State.ConnectWait);

    // Next delay should be back to initial interval (doubled for next)
    Long newDelay = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_DELAY_MS::get);
    assertNotNull(newDelay);
    long expectedNextDelay =
        Math.min(CONNECT_INTERVAL.toMillis() * 2, MAX_RECONNECT_DELAY.toMillis());
    assertEquals(expectedNextDelay, newDelay);
  }

  @Test
  // A late TCP success after shutdown starts must be closed immediately so the
  // manager's stop future means the connection is fully quiesced.
  void stopDuringConnectingWithSuccessClosesChannelAndDoesNotReconnect() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    // Stop while connecting — gets shelved
    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEventBlocking(new Event.Stop(stopFuture));

    // Complete connect — transitions to RheSent, shelved Stop replayed
    CompletableFuture<Channel> connectFuture = stubTransport.pollConnectFuture();
    var channel = new EmbeddedChannel();
    connectFuture.complete(channel);

    awaitState(fsm, State.Disconnected);
    assertTrue(stopFuture.isDone());
    assertTrue(channel.closeFuture().await(5, TimeUnit.SECONDS));
    assertFalse(channel.isOpen());
    stubTransport.assertConnectAttemptCount(1, Duration.ofMillis(CONNECT_INTERVAL.toMillis() * 2));
  }

  @Test
  // A late TCP failure after shutdown starts must not arm the retry loop.
  void stopDuringConnectingWithFailureDoesNotScheduleReconnect() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    // Stop while connecting — gets shelved
    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEventBlocking(new Event.Stop(stopFuture));

    // Fail connect — transitions to ConnectWait, shelved Stop replayed
    CompletableFuture<Channel> connectFuture = stubTransport.pollConnectFuture();
    connectFuture.completeExceptionally(new Exception("refused"));

    awaitState(fsm, State.Disconnected);
    assertTrue(stopFuture.isDone());

    ScheduledFuture<?> retryFuture =
        fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_FUTURE::get);
    assertNull(retryFuture);

    stubTransport.assertConnectAttemptCount(1, Duration.ofMillis(CONNECT_INTERVAL.toMillis() * 2));
  }

  @Test
  void stopDuringRheSent() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    CompletableFuture<Channel> connectFuture = stubTransport.pollConnectFuture();
    connectFuture.complete(new EmbeddedChannel());
    awaitState(fsm, State.RheSent);

    // Stop in RheSent — handled directly
    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEvent(new Event.Stop(stopFuture));

    awaitState(fsm, State.Disconnected);
    assertTrue(stopFuture.isDone());
  }

  @Test
  void stopDuringConnectWait() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    CompletableFuture<Channel> f = stubTransport.pollConnectFuture();
    f.completeExceptionally(new Exception("refused"));
    awaitState(fsm, State.ConnectWait);

    // Verify retry future exists
    ScheduledFuture<?> retryFuture =
        fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_FUTURE::get);
    assertNotNull(retryFuture);

    // Stop in ConnectWait
    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEvent(new Event.Stop(stopFuture));

    awaitState(fsm, State.Disconnected);
    assertTrue(stopFuture.isDone());
    assertTrue(retryFuture.isCancelled());
  }

  @Test
  void stopDuringActive() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    var channel = new EmbeddedChannel();
    reachActive(fsm, channel);

    // Stop in Active
    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEvent(new Event.Stop(stopFuture));

    awaitState(fsm, State.Disconnected);
    assertTrue(stopFuture.isDone());
  }

  @Test
  void channelInactiveDuringRheSent() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    CompletableFuture<Channel> connectFuture = stubTransport.pollConnectFuture();
    connectFuture.complete(new EmbeddedChannel());
    awaitState(fsm, State.RheSent);

    // Simulate TCP disconnect before Hello arrives
    fsm.fireEvent(new Event.ChannelInactive());
    awaitState(fsm, State.ConnectWait);

    // Verify retry timer is scheduled
    ScheduledFuture<?> retryFuture =
        fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_FUTURE::get);
    assertNotNull(retryFuture);
  }

  @Test
  void disconnectedIsTerminal() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    // Go directly to Disconnected
    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEvent(new Event.Stop(stopFuture));
    awaitState(fsm, State.Disconnected);

    // Fire various events — state should remain Disconnected
    fsm.fireEvent(new Event.Start());
    Thread.sleep(100);
    assertEquals(State.Disconnected, fsm.getState());

    fsm.fireEvent(new Event.ConnectSuccess(new EmbeddedChannel()));
    Thread.sleep(100);
    assertEquals(State.Disconnected, fsm.getState());

    fsm.fireEvent(new Event.RetryDelayElapsed());
    Thread.sleep(100);
    assertEquals(State.Disconnected, fsm.getState());
  }

  @Test
  void channelCleanupOnDisconnected() throws Exception {
    Fsm<State, Event> fsm = newFsm();

    var channel = new EmbeddedChannel();
    reachActive(fsm, channel);
    assertTrue(channel.isActive());

    // Stop -> Disconnected should close the channel
    var stopFuture = new CompletableFuture<Void>();
    fsm.fireEvent(new Event.Stop(stopFuture));
    awaitState(fsm, State.Disconnected);

    // Verify context keys are cleaned up
    Channel ctxChannel = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_CHANNEL::get);
    assertNull(ctxChannel);

    ScheduledFuture<?> retryFuture =
        fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_FUTURE::get);
    assertNull(retryFuture);

    Long retryDelay = fsm.getFromContext(ReverseConnectConnectionFsm.KEY_RETRY_DELAY_MS::get);
    assertNull(retryDelay);
  }

  // -- helpers --

  private Fsm<State, Event> newFsm() {
    ReverseConnectConfig config =
        ReverseConnectConfig.newBuilder()
            .setConnectInterval(CONNECT_INTERVAL)
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setRejectBackoff(REJECT_BACKOFF)
            .setMaxReconnectDelay(MAX_RECONNECT_DELAY)
            .build();

    return ReverseConnectConnectionFsm.newFsm(
        CLIENT_ENDPOINT_URL,
        ENDPOINT_URL,
        SERVER_URI,
        stubTransport,
        null, // applicationContext not used by stub
        config,
        executor,
        scheduler,
        null);
  }

  private void reachActive(Fsm<State, Event> fsm, EmbeddedChannel channel) throws Exception {
    fsm.fireEvent(new Event.Start());
    awaitState(fsm, State.Connecting);

    CompletableFuture<Channel> connectFuture = stubTransport.pollConnectFuture();
    connectFuture.complete(channel);
    awaitState(fsm, State.RheSent);

    fsm.fireEvent(new Event.SecureChannelOpened());
    awaitState(fsm, State.Active);
  }

  private static void awaitState(Fsm<State, Event> fsm, State expected) throws Exception {
    awaitState(fsm, expected, Duration.ofSeconds(5));
  }

  private static void awaitState(Fsm<State, Event> fsm, State expected, Duration timeout)
      throws Exception {

    long deadline = System.nanoTime() + timeout.toNanos();
    while (System.nanoTime() < deadline) {
      if (fsm.getState() == expected) {
        return;
      }
      Thread.sleep(50);
    }
    fail("Timed out waiting for state " + expected + ", current: " + fsm.getState());
  }

  /** A stub transport that captures connect calls and lets tests control the returned futures. */
  static class StubTransport extends OpcTcpReverseConnectServerTransport {

    private final ConcurrentLinkedQueue<CompletableFuture<Channel>> connectFutures =
        new ConcurrentLinkedQueue<>();
    private final AtomicInteger connectCalls = new AtomicInteger();
    private volatile boolean completeWithSecureChannelOpenBeforeReturning = false;

    StubTransport() {
      super(null);
    }

    @Override
    public CompletableFuture<Channel> connect(
        ServerApplicationContext applicationContext,
        InetSocketAddress clientAddress,
        String serverUri,
        String endpointUrl,
        long connectTimeoutMs) {

      var future = new CompletableFuture<Channel>();
      connectCalls.incrementAndGet();
      connectFutures.offer(future);
      return future;
    }

    @Override
    protected ReverseConnectAttempt connectAttempt(
        ServerApplicationContext applicationContext,
        InetSocketAddress clientAddress,
        String serverUri,
        String endpointUrl,
        long connectTimeoutMs,
        Consumer<ReverseConnectAttempt.Outcome> outcomeConsumer) {

      var attempt = new ReverseConnectAttempt(outcomeConsumer);
      connectCalls.incrementAndGet();

      if (completeWithSecureChannelOpenBeforeReturning) {
        var channel = new EmbeddedChannel();
        attempt.channelInitialized(channel);
        attempt.secureChannelOpened(1L);
        attempt.tcpConnectSucceeded(channel);
        return attempt;
      }

      var future = new CompletableFuture<Channel>();
      connectFutures.offer(future);
      future.whenComplete(
          (channel, ex) -> {
            if (ex != null) {
              attempt.tcpConnectFailed(ex);
            } else {
              attempt.channelInitialized(channel);
              attempt.tcpConnectSucceeded(channel);
            }
          });

      return attempt;
    }

    void completeWithSecureChannelOpenBeforeReturning() {
      completeWithSecureChannelOpenBeforeReturning = true;
    }

    CompletableFuture<Channel> pollConnectFuture() throws Exception {
      long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
      while (System.nanoTime() < deadline) {
        CompletableFuture<Channel> f = connectFutures.poll();
        if (f != null) {
          return f;
        }
        Thread.sleep(50);
      }
      fail("Timed out waiting for connect() to be called");
      return null; // unreachable
    }

    void assertConnectAttemptCount(int expected, Duration timeout) throws Exception {
      long deadline = System.nanoTime() + timeout.toNanos();
      while (System.nanoTime() < deadline) {
        int actual = connectCalls.get();
        if (actual > expected) {
          fail("connect() should not be called more than " + expected + " time(s)");
        }
        Thread.sleep(10);
      }

      assertEquals(expected, connectCalls.get());
    }
  }
}
