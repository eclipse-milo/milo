/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.embedded.EmbeddedChannel;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReverseConnectTargetOwnerTest {

  private static final String CLIENT_ENDPOINT_URL = "opc.tcp://localhost:48060";
  private static final String ENDPOINT_URL = "opc.tcp://server-host:4840";
  private static final String SERVER_URI = "urn:test:server";

  private static final Duration CONNECT_INTERVAL = Duration.ofMillis(100);
  private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
  private static final Duration REJECT_BACKOFF = Duration.ofMillis(700);
  private static final Duration MAX_RECONNECT_DELAY = Duration.ofMillis(250);

  private ManualScheduler scheduler;
  private StubTransport transport;
  private ReverseConnectTargetOwner owner;

  @BeforeEach
  void setUp() {
    scheduler = new ManualScheduler();
    transport = new StubTransport();
    owner = newOwner();
  }

  @Test
  void startBeginsIdleAttemptWhenNoIdleAttemptExists() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);

    assertEquals(ReverseConnectTargetOwner.State.Running, owner.getState());
    assertTrue(owner.hasIdleAttempt());
    assertEquals(1, transport.attemptCount());
  }

  @Test
  void rejectsNonTcpClientEndpointUrl() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> newOwner("opc.wss://localhost:48060"));

    assertTrue(exception.getMessage().contains("opc.tcp"));
    assertEquals(0, transport.attemptCount());
  }

  @Test
  void connectFailureUsesExponentialReconnectBackoff() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);

    transport.attempt(0).failTcpConnect();

    assertEquals(CONNECT_INTERVAL.toMillis(), scheduler.nextDelayMs());

    scheduler.runNext();
    assertEquals(2, transport.attemptCount());

    transport.attempt(1).failTcpConnect();

    assertEquals(200L, scheduler.nextDelayMs());

    scheduler.runNext();
    transport.attempt(2).failTcpConnect();

    assertEquals(MAX_RECONNECT_DELAY.toMillis(), scheduler.nextDelayMs());
  }

  @Test
  void closeBeforeSecureChannelUsesReconnectBackoff() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);

    var firstChannel = new EmbeddedChannel();
    transport.attempt(0).connect(firstChannel);
    transport.attempt(0).closeBeforeSecureChannel();

    assertEquals(CONNECT_INTERVAL.toMillis(), scheduler.nextDelayMs());
    assertFalse(firstChannel.isOpen());
  }

  @Test
  void tcpConnectSuccessResetsReconnectBackoffBeforePlainClose() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);
    transport.attempt(0).failTcpConnect();
    scheduler.runNext();

    var secondChannel = new EmbeddedChannel();
    transport.attempt(1).connect(secondChannel);
    transport.attempt(1).closeBeforeSecureChannel();

    assertEquals(CONNECT_INTERVAL.toMillis(), scheduler.nextDelayMs());
    assertFalse(secondChannel.isOpen());
  }

  @Test
  void clientRejectionUsesRejectBackoff() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);

    var channel = new EmbeddedChannel();
    transport.attempt(0).connect(channel);
    transport.attempt(0).rejectClient();

    assertEquals(REJECT_BACKOFF.toMillis(), scheduler.nextDelayMs());
    assertFalse(channel.isOpen());
  }

  @Test
  void secureChannelOpenedMarksAttemptActiveAndStartsReplacementIdleAttempt() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);

    var activeChannel = new EmbeddedChannel();
    transport.attempt(0).connect(activeChannel);
    transport.attempt(0).openSecureChannel();

    assertEquals(1, owner.activeAttemptCount());
    assertTrue(activeChannel.isOpen());
    assertTrue(owner.hasIdleAttempt());
    assertEquals(2, transport.attemptCount());
  }

  @Test
  void stopDuringConnectClosesLateIdleAttemptAndDoesNotRetry() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);
    StartedAttempt attempt = transport.attempt(0);

    CompletableFuture<Void> stopFuture = owner.stop();
    assertFalse(stopFuture.isDone());

    var lateChannel = new EmbeddedChannel();
    attempt.connect(lateChannel);

    stopFuture.get(1, TimeUnit.SECONDS);

    assertEquals(ReverseConnectTargetOwner.State.Stopped, owner.getState());
    assertFalse(lateChannel.isOpen());
    assertEquals(1, transport.attemptCount());
    assertEquals(0, scheduler.taskCount());
  }

  @Test
  void stopDuringConnectCompletesAfterLateConnectFailure() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);
    StartedAttempt attempt = transport.attempt(0);

    CompletableFuture<Void> stopFuture = owner.stop();
    assertFalse(stopFuture.isDone());

    attempt.failTcpConnect();

    stopFuture.get(1, TimeUnit.SECONDS);

    assertEquals(ReverseConnectTargetOwner.State.Stopped, owner.getState());
    assertEquals(1, transport.attemptCount());
    assertEquals(0, scheduler.taskCount());
  }

  @Test
  void stopDuringRetryWaitCancelsTimerAndDoesNotReconnect() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);
    transport.attempt(0).failTcpConnect();

    ManualScheduler.ScheduledTask retry = scheduler.nextTask();

    owner.stop().get(1, TimeUnit.SECONDS);
    scheduler.runAll();

    assertTrue(retry.isCancelled());
    assertEquals(ReverseConnectTargetOwner.State.Stopped, owner.getState());
    assertEquals(1, transport.attemptCount());
  }

  @Test
  void stopClosesConnectedIdleAttempt() throws Exception {
    owner.start().get(1, TimeUnit.SECONDS);

    var idleChannel = new EmbeddedChannel();
    transport.attempt(0).connect(idleChannel);

    owner.stop().get(1, TimeUnit.SECONDS);

    assertFalse(idleChannel.isOpen());
    assertEquals(ReverseConnectTargetOwner.State.Stopped, owner.getState());
  }

  private ReverseConnectTargetOwner newOwner() {
    return newOwner(CLIENT_ENDPOINT_URL);
  }

  private ReverseConnectTargetOwner newOwner(String clientEndpointUrl) {
    ReverseConnectConfig config =
        ReverseConnectConfig.newBuilder()
            .setConnectInterval(CONNECT_INTERVAL)
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setRejectBackoff(REJECT_BACKOFF)
            .setMaxReconnectDelay(MAX_RECONNECT_DELAY)
            .build();

    return new ReverseConnectTargetOwner(
        clientEndpointUrl,
        ENDPOINT_URL,
        SERVER_URI,
        transport,
        null,
        config,
        Runnable::run,
        scheduler);
  }

  private static final class StubTransport extends OpcTcpReverseConnectServerTransport {

    private final List<StartedAttempt> attempts = new ArrayList<>();

    private StubTransport() {
      super(null);
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
      attempts.add(new StartedAttempt(attempt));

      return attempt;
    }

    int attemptCount() {
      return attempts.size();
    }

    StartedAttempt attempt(int index) {
      return attempts.get(index);
    }
  }

  private static final class StartedAttempt {

    private final ReverseConnectAttempt attempt;

    private StartedAttempt(ReverseConnectAttempt attempt) {
      this.attempt = attempt;
    }

    void connect(EmbeddedChannel channel) {
      attempt.channelInitialized(channel);
      attempt.tcpConnectSucceeded(channel);
    }

    void failTcpConnect() {
      attempt.tcpConnectFailed(new Exception("connection refused"));
    }

    void rejectClient() {
      attempt.clientRejected(new ErrorMessage(StatusCodes.Bad_ServerNotConnected, "rejected"));
    }

    void closeBeforeSecureChannel() {
      attempt.channelInactive();
    }

    void openSecureChannel() {
      attempt.secureChannelOpened(1L);
    }
  }

  private static final class ManualScheduler implements ReverseConnectTargetOwner.Scheduler {

    private final ArrayDeque<ScheduledTask> tasks = new ArrayDeque<>();

    @Override
    public ReverseConnectTargetOwner.Cancellable schedule(
        Runnable task, long delay, TimeUnit unit) {

      var scheduledTask = new ScheduledTask(task, unit.toMillis(delay));
      tasks.add(scheduledTask);

      return scheduledTask::cancel;
    }

    long nextDelayMs() {
      return nextTask().delayMs();
    }

    ScheduledTask nextTask() {
      return tasks.peek();
    }

    int taskCount() {
      return tasks.size();
    }

    void runNext() {
      ScheduledTask task = tasks.remove();

      if (!task.isCancelled()) {
        task.command().run();
      }
    }

    void runAll() {
      while (!tasks.isEmpty()) {
        runNext();
      }
    }

    private static final class ScheduledTask {

      private final Runnable command;
      private final long delayMs;
      private boolean cancelled;

      private ScheduledTask(Runnable command, long delayMs) {
        this.command = command;
        this.delayMs = delayMs;
      }

      Runnable command() {
        return command;
      }

      long delayMs() {
        return delayMs;
      }

      boolean isCancelled() {
        return cancelled;
      }

      void cancel() {
        cancelled = true;
      }
    }
  }
}
