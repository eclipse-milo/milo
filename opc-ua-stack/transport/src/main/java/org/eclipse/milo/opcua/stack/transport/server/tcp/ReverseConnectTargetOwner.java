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

import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Owns the server-side Reverse Connect lifecycle for one configured client endpoint URL.
 *
 * <p>The owner maintains one idle attempt while running, starts a replacement idle attempt when a
 * client opens a SecureChannel, and owns retry timers for rejected or failed attempts.
 */
final class ReverseConnectTargetOwner {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReverseConnectTargetOwner.class);

  enum State {
    Created,
    Running,
    Stopping,
    Stopped
  }

  interface Event {

    record Start(CompletableFuture<Void> future) implements Event {}

    record Stop(CompletableFuture<Void> future) implements Event {}

    record AttemptConnected(long generation, long attemptId, Channel channel) implements Event {}

    record AttemptOutcome(long generation, long attemptId, ReverseConnectAttempt.Outcome outcome)
        implements Event {}

    record RetryDelayElapsed(long generation) implements Event {}

    record StopClosed(
        long generation, CompletableFuture<Void> completion, @Nullable Throwable failure)
        implements Event {}

    record StoppingAttemptClosed(long generation, long attemptId, @Nullable Throwable failure)
        implements Event {}
  }

  @FunctionalInterface
  interface Scheduler {

    Cancellable schedule(Runnable task, long delay, TimeUnit unit);

    static Scheduler fromScheduledExecutor(ScheduledExecutorService executor) {
      return (task, delay, unit) -> {
        var scheduledFuture = executor.schedule(task, delay, unit);

        return () -> scheduledFuture.cancel(false);
      };
    }
  }

  @FunctionalInterface
  interface Cancellable {

    void cancel();
  }

  private static final class AttemptContext {

    private final long generation;
    private final long attemptId;
    private @Nullable Channel channel;
    private @Nullable CompletableFuture<Void> stopFuture;

    private AttemptContext(long generation, long attemptId) {
      this.generation = generation;
      this.attemptId = attemptId;
    }
  }

  private enum RetryKind {
    Reconnect,
    Reject
  }

  private final String endpointUrl;
  private final String serverUri;
  private final OpcTcpReverseConnectServerTransport transport;
  private final @Nullable ServerApplicationContext applicationContext;
  private final ReverseConnectConfig config;
  private final Executor executor;
  private final Scheduler scheduler;
  private final InetSocketAddress clientAddress;
  private final ArrayDeque<Event> mailbox = new ArrayDeque<>();
  private final Map<Long, AttemptContext> activeAttempts = new HashMap<>();

  private boolean draining;
  private long generation;
  private long nextAttemptId;
  private long nextReconnectDelayMs;
  private volatile State state = State.Created;
  private @Nullable AttemptContext idleAttempt;
  private @Nullable Cancellable retryTimer;
  private @Nullable CompletableFuture<Void> stopCompletion;

  ReverseConnectTargetOwner(
      String clientEndpointUrl,
      String endpointUrl,
      String serverUri,
      OpcTcpReverseConnectServerTransport transport,
      @Nullable ServerApplicationContext applicationContext,
      ReverseConnectConfig config,
      Executor executor,
      ScheduledExecutorService scheduler) {

    this(
        clientEndpointUrl,
        endpointUrl,
        serverUri,
        transport,
        applicationContext,
        config,
        executor,
        Scheduler.fromScheduledExecutor(scheduler));
  }

  ReverseConnectTargetOwner(
      String clientEndpointUrl,
      String endpointUrl,
      String serverUri,
      OpcTcpReverseConnectServerTransport transport,
      @Nullable ServerApplicationContext applicationContext,
      ReverseConnectConfig config,
      Executor executor,
      Scheduler scheduler) {

    this.endpointUrl = Objects.requireNonNull(endpointUrl, "endpointUrl");
    this.serverUri = Objects.requireNonNull(serverUri, "serverUri");
    this.transport = Objects.requireNonNull(transport, "transport");
    this.applicationContext = applicationContext;
    this.config = Objects.requireNonNull(config, "config");
    this.executor = Objects.requireNonNull(executor, "executor");
    this.scheduler = Objects.requireNonNull(scheduler, "scheduler");

    String scheme = EndpointUtil.getScheme(clientEndpointUrl);
    if (!Objects.equals(scheme, TransportProfile.TCP_UASC_UABINARY.getScheme())) {
      throw new IllegalArgumentException(
          "Reverse Connect client endpoint URL must use opc.tcp scheme: " + clientEndpointUrl);
    }

    String host = Objects.requireNonNull(EndpointUtil.getHost(clientEndpointUrl), "host");
    int port = EndpointUtil.getPort(clientEndpointUrl);
    clientAddress = new InetSocketAddress(host, port);
    resetReconnectDelay();
  }

  CompletableFuture<Void> start() {
    var future = new CompletableFuture<Void>();

    fireEvent(new Event.Start(future));

    return future;
  }

  CompletableFuture<Void> stop() {
    var future = new CompletableFuture<Void>();

    fireEvent(new Event.Stop(future));

    return future;
  }

  CompletableFuture<Void> remove() {
    return stop();
  }

  State getState() {
    return state;
  }

  boolean hasIdleAttempt() {
    return idleAttempt != null;
  }

  int activeAttemptCount() {
    return activeAttempts.size();
  }

  void fireEvent(Event event) {
    synchronized (mailbox) {
      mailbox.add(event);

      if (draining) {
        return;
      }

      draining = true;
    }

    executor.execute(this::drainMailbox);
  }

  private void drainMailbox() {
    while (true) {
      Event event;

      synchronized (mailbox) {
        event = mailbox.poll();

        if (event == null) {
          draining = false;
          return;
        }
      }

      try {
        handleEvent(event);
      } catch (Throwable t) {
        LOGGER.error("Error handling Reverse Connect target owner event: {}", event, t);
      }
    }
  }

  private void handleEvent(Event event) {
    if (event instanceof Event.Start start) {
      handleStart(start);
    } else if (event instanceof Event.Stop stop) {
      handleStop(stop);
    } else if (event instanceof Event.AttemptConnected connected) {
      handleAttemptConnected(connected);
    } else if (event instanceof Event.AttemptOutcome outcome) {
      handleAttemptOutcome(outcome);
    } else if (event instanceof Event.RetryDelayElapsed retry) {
      handleRetryDelayElapsed(retry);
    } else if (event instanceof Event.StopClosed closed) {
      handleStopClosed(closed);
    } else if (event instanceof Event.StoppingAttemptClosed closed) {
      handleStoppingAttemptClosed(closed);
    } else {
      throw new IllegalArgumentException("unexpected event: " + event);
    }
  }

  private void handleStart(Event.Start event) {
    if (state == State.Running) {
      ensureIdleAttempt();
      event.future().complete(null);
      return;
    }

    if (state == State.Stopping || state == State.Stopped) {
      event.future().completeExceptionally(shutdown("reverse connect target is stopped"));
      return;
    }

    state = State.Running;
    ensureIdleAttempt();
    event.future().complete(null);
  }

  private void handleStop(Event.Stop event) {
    if (state == State.Stopped) {
      event.future().complete(null);
      return;
    }

    if (state == State.Stopping) {
      pipeCompletion(Objects.requireNonNull(stopCompletion), event.future());
      return;
    }

    state = State.Stopping;
    long stopGeneration = generation;

    cancelRetryTimer();

    List<CompletableFuture<Void>> stopWork = new ArrayList<>();
    if (idleAttempt != null) {
      if (idleAttempt.channel != null) {
        stopWork.add(close(idleAttempt.channel));
        idleAttempt = null;
      } else {
        idleAttempt.stopFuture = new CompletableFuture<>();
        stopWork.add(idleAttempt.stopFuture);
      }
    }

    for (AttemptContext attempt : activeAttempts.values()) {
      if (attempt.channel != null) {
        stopWork.add(close(attempt.channel));
      }
    }

    activeAttempts.clear();

    var completion = new CompletableFuture<Void>();
    stopCompletion = completion;
    pipeCompletion(completion, event.future());

    CompletableFuture.allOf(stopWork.toArray(CompletableFuture[]::new))
        .whenComplete(
            (ignored, ex) -> fireEvent(new Event.StopClosed(stopGeneration, completion, ex)));
  }

  private void handleAttemptConnected(Event.AttemptConnected event) {
    AttemptContext context = findAttempt(event.generation(), event.attemptId());
    if (context == null) {
      closeIfStopping(event.channel());
      return;
    }

    context.channel = event.channel();

    if (state != State.Running) {
      closeStoppingAttempt(context, event.channel());
    } else if (idleAttempt == context) {
      resetReconnectDelay();
    }
  }

  private void handleAttemptOutcome(Event.AttemptOutcome event) {
    AttemptContext context = findAttempt(event.generation(), event.attemptId());
    if (context == null) {
      closeIfStopping(channelFromOutcome(event.outcome()));
      return;
    }

    ReverseConnectAttempt.Outcome outcome = event.outcome();

    if (outcome instanceof ReverseConnectAttempt.SecureChannelOpened opened) {
      handleSecureChannelOpened(context, opened.channel());
    } else if (outcome instanceof ReverseConnectAttempt.ClientRejected rejection) {
      handleIdleAttemptEnded(context, rejection.channel(), RetryKind.Reject);
    } else if (outcome instanceof ReverseConnectAttempt.TcpConnectFailure) {
      handleIdleAttemptEnded(context, null, RetryKind.Reconnect);
    } else if (outcome instanceof ReverseConnectAttempt.ReverseHelloWriteFailure failure) {
      handleIdleAttemptEnded(context, failure.channel(), RetryKind.Reconnect);
    } else if (outcome instanceof ReverseConnectAttempt.CloseBeforeSecureChannel close) {
      handleIdleAttemptEnded(context, close.channel(), RetryKind.Reconnect);
    } else if (outcome instanceof ReverseConnectAttempt.CloseAfterSecureChannel close) {
      handleActiveAttemptClosed(context, close.channel());
    }
  }

  private void handleRetryDelayElapsed(Event.RetryDelayElapsed event) {
    if (event.generation() != generation || state != State.Running) {
      return;
    }

    retryTimer = null;
    ensureIdleAttempt();
  }

  private void handleStopClosed(Event.StopClosed event) {
    if (event.generation() != generation || state != State.Stopping) {
      return;
    }

    state = State.Stopped;
    generation++;
    idleAttempt = null;
    stopCompletion = null;

    if (event.failure() != null) {
      event.completion().completeExceptionally(unwrap(event.failure()));
    } else {
      event.completion().complete(null);
    }
  }

  private void handleStoppingAttemptClosed(Event.StoppingAttemptClosed event) {
    AttemptContext context = findAttempt(event.generation(), event.attemptId());
    if (context == null) {
      return;
    }

    completeStoppingAttempt(context, event.failure());
  }

  private void handleSecureChannelOpened(AttemptContext context, Channel channel) {
    if (state != State.Running || idleAttempt != context) {
      closeStoppingAttempt(context, channel);
      return;
    }

    idleAttempt = null;
    context.channel = channel;
    activeAttempts.put(context.attemptId, context);

    resetReconnectDelay();
    ensureIdleAttempt();
  }

  private void handleIdleAttemptEnded(
      AttemptContext context, @Nullable Channel channel, RetryKind retryKind) {

    if (state == State.Stopping) {
      closeStoppingAttempt(context, channel);
      return;
    }

    if (idleAttempt != context) {
      handleActiveAttemptClosed(context, channel);
      return;
    }

    idleAttempt = null;

    if (channel != null) {
      close(channel);
    }

    if (retryKind == RetryKind.Reject) {
      scheduleRetry(rejectBackoffMs());
    } else {
      scheduleRetry(nextReconnectDelay());
    }
  }

  private void handleActiveAttemptClosed(AttemptContext context, @Nullable Channel channel) {
    activeAttempts.remove(context.attemptId);

    if (state != State.Running) {
      if (channel != null) {
        closeStoppingAttempt(context, channel);
      } else {
        completeStoppingAttempt(context, null);
      }
      return;
    }

    ensureIdleAttempt();
  }

  private void ensureIdleAttempt() {
    if (state != State.Running || idleAttempt != null || retryTimer != null) {
      return;
    }

    long attemptGeneration = generation;
    long attemptId = ++nextAttemptId;
    var context = new AttemptContext(attemptGeneration, attemptId);
    idleAttempt = context;

    ReverseConnectAttempt attempt;
    try {
      attempt =
          transport.connectAttempt(
              applicationContext,
              clientAddress,
              serverUri,
              endpointUrl,
              connectTimeoutMs(),
              outcome ->
                  fireEvent(new Event.AttemptOutcome(attemptGeneration, attemptId, outcome)));
    } catch (Throwable t) {
      if (idleAttempt == context) {
        idleAttempt = null;
        scheduleRetry(nextReconnectDelay());
      }
      return;
    }

    attempt
        .connectedFuture()
        .thenAccept(
            channel ->
                fireEvent(new Event.AttemptConnected(attemptGeneration, attemptId, channel)));
  }

  private @Nullable AttemptContext findAttempt(long eventGeneration, long attemptId) {
    if (eventGeneration != generation) {
      return null;
    }

    if (idleAttempt != null && idleAttempt.attemptId == attemptId) {
      return idleAttempt;
    }

    return activeAttempts.get(attemptId);
  }

  private void scheduleRetry(long delayMs) {
    if (state != State.Running || idleAttempt != null || retryTimer != null) {
      return;
    }

    long retryGeneration = generation;
    retryTimer =
        scheduler.schedule(
            () -> fireEvent(new Event.RetryDelayElapsed(retryGeneration)),
            Math.max(0L, delayMs),
            TimeUnit.MILLISECONDS);
  }

  private long nextReconnectDelay() {
    long delay = nextReconnectDelayMs;
    nextReconnectDelayMs = Math.min(saturatedDouble(delay), maxReconnectDelayMs());

    return delay;
  }

  private void resetReconnectDelay() {
    nextReconnectDelayMs = Math.min(connectIntervalMs(), maxReconnectDelayMs());
  }

  private long saturatedDouble(long value) {
    if (value > Long.MAX_VALUE / 2L) {
      return Long.MAX_VALUE;
    }

    return value * 2L;
  }

  private void cancelRetryTimer() {
    Cancellable timer = retryTimer;
    retryTimer = null;

    if (timer != null) {
      timer.cancel();
    }
  }

  private CompletableFuture<Void> close(@Nullable Channel channel) {
    if (channel == null) {
      return CompletableFuture.completedFuture(null);
    }

    var closeFuture = new CompletableFuture<Void>();

    channel
        .close()
        .addListener(
            f -> {
              if (f.isSuccess()) {
                closeFuture.complete(null);
              } else {
                closeFuture.completeExceptionally(f.cause());
              }
            });

    return closeFuture;
  }

  private void closeIfStopping(@Nullable Channel channel) {
    if (state == State.Stopping || state == State.Stopped) {
      close(channel);
    }
  }

  private void closeStoppingAttempt(AttemptContext context, @Nullable Channel channel) {
    if (channel == null) {
      fireEvent(new Event.StoppingAttemptClosed(context.generation, context.attemptId, null));
      return;
    }

    close(channel)
        .whenComplete(
            (ignored, ex) ->
                fireEvent(
                    new Event.StoppingAttemptClosed(context.generation, context.attemptId, ex)));
  }

  private void completeStoppingAttempt(AttemptContext context, @Nullable Throwable failure) {
    if (idleAttempt == context) {
      idleAttempt = null;
    }

    activeAttempts.remove(context.attemptId);

    CompletableFuture<Void> stopFuture = context.stopFuture;
    context.stopFuture = null;

    if (stopFuture == null || stopFuture.isDone()) {
      return;
    }

    if (failure != null) {
      stopFuture.completeExceptionally(unwrap(failure));
    } else {
      stopFuture.complete(null);
    }
  }

  private long connectIntervalMs() {
    return Math.max(0L, config.getConnectInterval().toMillis());
  }

  private long connectTimeoutMs() {
    return Math.max(0L, config.getConnectTimeout().toMillis());
  }

  private long rejectBackoffMs() {
    return Math.max(0L, config.getRejectBackoff().toMillis());
  }

  private long maxReconnectDelayMs() {
    return Math.max(0L, config.getMaxReconnectDelay().toMillis());
  }

  private static @Nullable Channel channelFromOutcome(ReverseConnectAttempt.Outcome outcome) {
    if (outcome instanceof ReverseConnectAttempt.ReverseHelloWriteFailure failure) {
      return failure.channel();
    } else if (outcome instanceof ReverseConnectAttempt.ClientRejected rejection) {
      return rejection.channel();
    } else if (outcome instanceof ReverseConnectAttempt.SecureChannelOpened opened) {
      return opened.channel();
    } else if (outcome instanceof ReverseConnectAttempt.CloseBeforeSecureChannel close) {
      return close.channel();
    } else if (outcome instanceof ReverseConnectAttempt.CloseAfterSecureChannel close) {
      return close.channel();
    }

    return null;
  }

  private static void pipeCompletion(
      CompletableFuture<Void> source, CompletableFuture<Void> target) {

    source.whenComplete(
        (ignored, ex) -> {
          if (ex != null) {
            target.completeExceptionally(unwrap(ex));
          } else {
            target.complete(null);
          }
        });
  }

  private static Throwable unwrap(Throwable failure) {
    if (failure instanceof CompletionException && failure.getCause() != null) {
      return failure.getCause();
    }

    return failure;
  }

  private static UaException shutdown(String message) {
    return new UaException(StatusCodes.Bad_Shutdown, message);
  }
}
