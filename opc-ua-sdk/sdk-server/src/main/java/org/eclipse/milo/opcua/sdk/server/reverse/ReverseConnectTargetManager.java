/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import static java.util.Objects.requireNonNull;

import io.netty.channel.Channel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttempt;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptEvent;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectParameters;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guarded registry and scheduler for server-side Reverse Connect targets.
 *
 * <p>This manager is the SDK bridge between immutable target configuration and the low-level TCP
 * reverse connector. Application code normally reaches it through {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServer}; the manager owns target validation, retry
 * scheduling, listener dispatch, and cleanup of in-flight attempts and reverse-opened channels.
 *
 * <p>For each target the manager tracks whether it is enabled, paused, scheduled, in-flight, in
 * handoff, or represented by one or more active reverse-opened channels. Enabled and unpaused
 * targets schedule at most one attempt at a time. A low-level {@code HANDOFF} event clears the
 * in-flight attempt and is followed by channel registration when the transport future completes.
 * Failed attempts and later active-channel closes consult the target retry policy before a new
 * schedule is created.
 *
 * <p>Updates replace only future scheduling state: scheduled work is cancelled, non-handoff
 * attempts are closed, and active channels already handed to the server path remain open. Removal
 * and shutdown are stronger lifecycle operations; both cancel scheduled work, close in-flight
 * attempts, and close active channels owned by the target.
 */
public final class ReverseConnectTargetManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Object lock = new Object();
  private final Map<UUID, TargetRecord> records = new LinkedHashMap<>();
  private final List<ReverseConnectTargetListener> listeners = new ArrayList<>();

  private final ServerApplicationContext applicationContext;
  private final Supplier<List<EndpointDescription>> endpointDescriptions;
  private final Function<TransportProfile, OpcServerTransport> transportSupplier;
  private final String serverUri;
  private final ExecutorService listenerExecutor;
  private final ScheduledExecutorService scheduler;

  private boolean running = false;
  private boolean shutdown = false;

  /**
   * Construct a target manager for one server instance.
   *
   * @param applicationContext the server application context used by reverse-opened channels.
   * @param endpointDescriptions supplies current endpoint descriptions for target validation.
   * @param transportSupplier supplies the server transport for a transport profile.
   * @param serverUri the server application URI advertised in {@code ReverseHello}.
   * @param listenerExecutor dispatches target and attempt listener callbacks.
   * @param scheduler schedules initial, retry, and reconnect attempts.
   * @param initialTargets the targets registered when the server is constructed.
   */
  public ReverseConnectTargetManager(
      ServerApplicationContext applicationContext,
      Supplier<List<EndpointDescription>> endpointDescriptions,
      Function<TransportProfile, OpcServerTransport> transportSupplier,
      String serverUri,
      ExecutorService listenerExecutor,
      ScheduledExecutorService scheduler,
      Collection<ReverseConnectTarget> initialTargets) {

    this.applicationContext = requireNonNull(applicationContext, "applicationContext");
    this.endpointDescriptions = requireNonNull(endpointDescriptions, "endpointDescriptions");
    this.transportSupplier = requireNonNull(transportSupplier, "transportSupplier");
    this.serverUri = requireNonNull(serverUri, "serverUri");
    this.listenerExecutor = requireNonNull(listenerExecutor, "listenerExecutor");
    this.scheduler = requireNonNull(scheduler, "scheduler");

    requireNonNull(initialTargets, "initialTargets");
    initialTargets.forEach(this::addInitialTarget);
  }

  /**
   * Register a target at runtime.
   *
   * <p>If the server is already running and the target is schedulable, it is validated and
   * scheduled immediately.
   *
   * @param target the target to register.
   * @return a runtime handle for the registered target.
   * @throws IllegalArgumentException if another target with the same id is already registered or
   *     the running server cannot use the target endpoint.
   * @throws IllegalStateException if the manager has been shut down.
   */
  public ReverseConnectTargetHandle addTarget(ReverseConnectTarget target) {
    requireNonNull(target, "target");

    ReverseConnectTargetSnapshot snapshot;
    synchronized (lock) {
      if (shutdown) {
        throw new IllegalStateException("ReverseConnectTargetManager is shut down");
      }
      if (records.containsKey(target.getId())) {
        throw new IllegalArgumentException(
            "duplicate Reverse Connect target id: " + target.getId());
      }
      if (running) {
        validateTarget(target);
      }

      TargetRecord record = new TargetRecord(target);
      records.put(target.getId(), record);

      snapshot = record.snapshot();
      notifyTargetAdded(snapshot);

      if (running && record.isSchedulable()) {
        scheduleLocked(record, 0L);
      }
    }

    return new ReverseConnectTargetHandle(this, target.getId());
  }

  /**
   * Add a listener for target and attempt lifecycle events.
   *
   * <p>Callbacks are dispatched asynchronously on the server executor supplied to the manager.
   *
   * @param listener the listener to register.
   */
  public void addListener(ReverseConnectTargetListener listener) {
    requireNonNull(listener, "listener");
    synchronized (lock) {
      listeners.add(listener);
    }
  }

  /**
   * Remove a previously registered lifecycle listener.
   *
   * @param listener the listener to remove.
   */
  public void removeListener(ReverseConnectTargetListener listener) {
    synchronized (lock) {
      listeners.remove(listener);
    }
  }

  /**
   * Get immutable snapshots for all registered targets.
   *
   * @return a snapshot list ordered by target registration order.
   */
  public List<ReverseConnectTargetSnapshot> snapshots() {
    synchronized (lock) {
      return records.values().stream().map(TargetRecord::snapshot).toList();
    }
  }

  /**
   * Get an immutable snapshot for one target.
   *
   * @param targetId the target id to inspect.
   * @return the target snapshot, or {@link Optional#empty()} if the target is not registered.
   */
  public Optional<ReverseConnectTargetSnapshot> snapshot(UUID targetId) {
    requireNonNull(targetId, "targetId");
    synchronized (lock) {
      TargetRecord record = records.get(targetId);
      return record != null ? Optional.of(record.snapshot()) : Optional.empty();
    }
  }

  /**
   * Check whether any registered target is enabled and not paused.
   *
   * @return true if at least one target may be scheduled.
   */
  public boolean hasSchedulableTargets() {
    synchronized (lock) {
      return records.values().stream().anyMatch(TargetRecord::isSchedulable);
    }
  }

  /**
   * Start scheduling all enabled, unpaused targets.
   *
   * <p>Targets are validated before any attempt is scheduled. Calling this method more than once is
   * harmless; only the first call transitions the manager into the running state.
   */
  public void startup() {
    List<ReverseConnectTargetSnapshot> snapshots = new ArrayList<>();

    synchronized (lock) {
      if (running) {
        return;
      }

      validateTargetsLocked();

      shutdown = false;
      running = true;

      records.values().stream()
          .filter(TargetRecord::isSchedulable)
          .forEach(
              record -> {
                scheduleLocked(record, 0L);
                snapshots.add(record.snapshot());
              });
    }

    snapshots.forEach(this::notifyTargetUpdated);
  }

  /** Validate all enabled, unpaused targets against the current server transports and endpoints. */
  public void validateTargets() {
    synchronized (lock) {
      validateTargetsLocked();
    }
  }

  /**
   * Stop scheduling and close resources owned by all targets.
   *
   * <p>Shutdown cancels scheduled work, closes in-flight attempts, and closes active reverse-opened
   * channels.
   */
  public void shutdown() {
    List<ReverseConnectTargetSnapshot> snapshots = new ArrayList<>();
    List<OpcTcpServerReverseConnectAttempt> attempts = new ArrayList<>();
    List<Channel> channels = new ArrayList<>();

    synchronized (lock) {
      if (shutdown) {
        return;
      }

      shutdown = true;
      running = false;

      records
          .values()
          .forEach(
              record -> {
                cancelScheduledLocked(record);

                if (record.activeAttempt != null) {
                  attempts.add(record.activeAttempt);
                  record.activeAttempt = null;
                }
                record.attemptInProgress = false;

                channels.addAll(record.activeChannels.values());
                record.activeChannels.clear();

                snapshots.add(record.snapshot());
              });
    }

    attempts.forEach(OpcTcpServerReverseConnectAttempt::close);
    channels.forEach(Channel::close);
    snapshots.forEach(this::notifyTargetUpdated);
  }

  CompletableFuture<ReverseConnectTargetSnapshot> pause(UUID targetId) {
    try {
      TargetAction action;
      synchronized (lock) {
        TargetRecord record = requireRecord(targetId);

        record.paused = true;
        cancelScheduledLocked(record);

        action = clearActiveAttemptLocked(record);
      }

      action.closeAttempt();
      notifyTargetUpdated(action.snapshot());

      return CompletableFuture.completedFuture(action.snapshot());
    } catch (RuntimeException e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  CompletableFuture<ReverseConnectTargetSnapshot> resume(UUID targetId) {
    try {
      ReverseConnectTargetSnapshot snapshot;
      synchronized (lock) {
        TargetRecord record = requireRecord(targetId);

        if (running
            && record.target.isEnabled()
            && record.activeChannels.isEmpty()
            && !record.hasPendingAttempt()) {
          validateTarget(record.target);
        }

        record.paused = false;

        if (shouldScheduleLocked(record)) {
          scheduleLocked(record, 0L);
        }

        snapshot = record.snapshot();
      }

      notifyTargetUpdated(snapshot);

      return CompletableFuture.completedFuture(snapshot);
    } catch (RuntimeException e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  CompletableFuture<ReverseConnectTargetSnapshot> trigger(UUID targetId) {
    try {
      ReverseConnectTargetSnapshot snapshot;
      synchronized (lock) {
        TargetRecord record = requireRecord(targetId);

        if (shouldScheduleLocked(record)) {
          validateTarget(record.target);
          scheduleLocked(record, 0L);
        }

        snapshot = record.snapshot();
      }

      notifyTargetUpdated(snapshot);

      return CompletableFuture.completedFuture(snapshot);
    } catch (RuntimeException e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  /**
   * Replace an existing target configuration.
   *
   * <p>Updating a target cancels scheduled work and closes any in-flight attempt owned by the old
   * configuration. Reverse-opened channels already handed to the server path remain open; the
   * replacement applies only to future attempts.
   *
   * @param target the replacement target configuration.
   * @return a completed future containing the updated target snapshot, or a failed future if the
   *     target id is unknown, the running server cannot use the replacement endpoint, or the
   *     manager has been shut down.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> update(ReverseConnectTarget target) {
    try {
      requireNonNull(target, "target");

      TargetAction action;
      synchronized (lock) {
        if (shutdown) {
          throw new IllegalStateException("ReverseConnectTargetManager is shut down");
        }

        TargetRecord record = requireRecord(target.getId());

        if (running && target.isEnabled() && !target.isPaused()) {
          validateTarget(target);
        }

        long attemptGeneration = record.generation;
        OpcTcpServerReverseConnectAttempt attempt = record.activeAttempt;
        boolean handoffAccepted = attempt != null && isSuccessfulHandoff(attempt);

        cancelScheduledLocked(record);

        record.activeAttempt = null;
        record.attemptInProgress = false;
        if (handoffAccepted) {
          record.pendingHandoffAttempts.add(
              new AttemptKey(record.attemptCounter, attemptGeneration));
        }

        record.target = target;
        record.paused = target.isPaused();

        if (running
            && record.isSchedulable()
            && record.activeChannels.isEmpty()
            && !record.hasPendingAttempt()) {

          scheduleLocked(record, 0L);
        } else if (!handoffAccepted) {
          record.generation++;
        }

        action = new TargetAction(record.snapshot(), handoffAccepted ? null : attempt);
      }

      action.closeAttempt();
      notifyTargetUpdated(action.snapshot());

      return CompletableFuture.completedFuture(action.snapshot());
    } catch (RuntimeException e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  /**
   * Remove a target and close resources it owns.
   *
   * @param targetId the target id to remove.
   * @return a completed future containing the final snapshot for the removed target, or a failed
   *     future if the target id is unknown.
   */
  public CompletableFuture<ReverseConnectTargetSnapshot> remove(UUID targetId) {
    try {
      TargetAction action;
      List<Channel> channels;
      synchronized (lock) {
        TargetRecord record = requireRecord(targetId);
        records.remove(targetId);

        cancelScheduledLocked(record);
        OpcTcpServerReverseConnectAttempt attempt = record.activeAttempt;
        record.activeAttempt = null;
        record.attemptInProgress = false;
        if (attempt == null || !isSuccessfulHandoff(attempt)) {
          record.generation++;
        }
        channels = List.copyOf(record.activeChannels.values());
        record.activeChannels.clear();
        action = new TargetAction(record.snapshot(), attempt);
      }

      action.closeAttempt();
      channels.forEach(Channel::close);
      notifyTargetRemoved(action.snapshot());

      return CompletableFuture.completedFuture(action.snapshot());
    } catch (RuntimeException e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  private void addInitialTarget(ReverseConnectTarget target) {
    requireNonNull(target, "target");
    if (records.containsKey(target.getId())) {
      throw new IllegalArgumentException("duplicate Reverse Connect target id: " + target.getId());
    }
    records.put(target.getId(), new TargetRecord(target));
  }

  private TargetRecord requireRecord(UUID targetId) {
    TargetRecord record = records.get(requireNonNull(targetId, "targetId"));
    if (record == null) {
      throw new IllegalArgumentException("unknown Reverse Connect target id: " + targetId);
    }
    return record;
  }

  private TargetAction clearActiveAttemptLocked(TargetRecord record) {
    long attemptGeneration = record.generation;
    OpcTcpServerReverseConnectAttempt attempt = record.activeAttempt;
    boolean handoffAccepted = attempt != null && isSuccessfulHandoff(attempt);

    record.activeAttempt = null;
    record.attemptInProgress = false;
    if (handoffAccepted) {
      record.pendingHandoffAttempts.add(new AttemptKey(record.attemptCounter, attemptGeneration));
    } else {
      record.generation++;
    }

    return new TargetAction(record.snapshot(), handoffAccepted ? null : attempt);
  }

  private void scheduleLocked(TargetRecord record, long delayMillis) {
    cancelScheduledLocked(record);

    long generation = ++record.generation;
    long normalizedDelayMillis = Math.max(0L, delayMillis);
    record.nextAttemptTime = Instant.now().plusMillis(normalizedDelayMillis);
    record.scheduledFuture =
        scheduler.schedule(
            () -> runScheduledAttempt(record.target.getId(), generation),
            normalizedDelayMillis,
            TimeUnit.MILLISECONDS);
  }

  private void cancelScheduledLocked(TargetRecord record) {
    if (record.scheduledFuture != null) {
      record.scheduledFuture.cancel(false);
      record.scheduledFuture = null;
    }
    record.nextAttemptTime = null;
  }

  private boolean shouldScheduleLocked(TargetRecord record) {
    return running
        && record.isSchedulable()
        && record.activeChannels.isEmpty()
        && !record.hasPendingAttempt();
  }

  private void runScheduledAttempt(UUID targetId, long generation) {
    AttemptStart start;
    synchronized (lock) {
      TargetRecord record = records.get(targetId);
      if (record == null
          || generation != record.generation
          || !running
          || shutdown
          || !record.isSchedulable()
          || record.hasPendingAttempt()) {
        return;
      }

      record.scheduledFuture = null;
      record.nextAttemptTime = null;
      record.attemptInProgress = true;
      record.lastAttemptTime = Instant.now();

      start =
          new AttemptStart(record.target, ++record.attemptCounter, generation, record.snapshot());
    }

    notifyTargetUpdated(start.snapshot());
    startTransportAttempt(start);
  }

  private void startTransportAttempt(AttemptStart start) {
    OpcTcpServerReverseConnectAttempt attempt;
    try {
      OpcServerTransport transport = transportSupplier.apply(TransportProfile.TCP_UASC_UABINARY);
      if (transport instanceof OpcTcpServerTransport tcpTransport) {
        attempt =
            tcpTransport.connectReverse(
                OpcTcpServerReverseConnectParameters.fromUrl(
                    applicationContext,
                    start.target().getClientListenerUrl(),
                    start.target().getEndpointUrl(),
                    serverUri,
                    start.target().getConnectTimeout().intValue(),
                    event ->
                        onTransportAttemptEvent(
                            start.target().getId(), start.number(), start.generation(), event)));
      } else {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "Reverse Connect requires OpcTcpServerTransport for opc.tcp targets");
      }
    } catch (Throwable t) {
      onAttemptStartFailure(start, t);
      return;
    }

    attempt
        .channelFuture()
        .whenComplete(
            (channel, ex) -> {
              if (ex == null) {
                onAttemptChannel(
                    start.target().getId(), start.number(), start.generation(), channel);
              }
            });

    boolean shouldClose = false;
    synchronized (lock) {
      TargetRecord record = records.get(start.target().getId());
      if (record != null
          && record.attemptCounter == start.number()
          && record.generation == start.generation()
          && running
          && !shutdown
          && record.isSchedulable()) {
        if (record.attemptInProgress) {
          record.activeAttempt = attempt;
        } else if (!isSuccessfulHandoff(attempt)) {
          shouldClose = true;
        }
      } else {
        shouldClose = true;
      }
    }

    if (shouldClose) {
      attempt.close();
    }
  }

  private void onAttemptStartFailure(AttemptStart start, Throwable cause) {
    StatusCode statusCode =
        cause instanceof UaException uaException
            ? uaException.getStatusCode()
            : new StatusCode(StatusCodes.Bad_UnexpectedError);

    ReverseConnectAttemptEvent event =
        new ReverseConnectAttemptEvent(
            start.target().getId(),
            start.number(),
            ReverseConnectAttemptState.FAILED,
            Instant.now(),
            statusCode,
            cause,
            cause.getMessage());

    RetryContext retryContext = null;
    ReverseConnectTargetSnapshot snapshot = null;
    synchronized (lock) {
      TargetRecord record = records.get(start.target().getId());
      if (record != null
          && record.attemptCounter == start.number()
          && record.generation == start.generation()) {
        record.attemptInProgress = false;
        record.activeAttempt = null;
        record.lastStatusCode = statusCode;
        record.lastError = cause;

        if (running && !shutdown && record.isSchedulable()) {
          retryContext =
              new RetryContext(
                  start.target().getId(), start.number(), start.generation(), record.target, event);
        }

        snapshot = record.snapshot();
      }
    }

    if (retryContext != null) {
      scheduleRetryEvaluation(retryContext, snapshot);
    } else {
      notifyAttemptEvent(event);
      if (snapshot != null) {
        notifyTargetUpdated(snapshot);
      }
    }
  }

  private void onTransportAttemptEvent(
      UUID targetId,
      long attemptNumber,
      long attemptGeneration,
      OpcTcpServerReverseConnectAttemptEvent transportEvent) {

    ReverseConnectAttemptEvent event =
        new ReverseConnectAttemptEvent(
            targetId,
            attemptNumber,
            mapState(transportEvent.state()),
            transportEvent.timestamp(),
            transportEvent.statusCode(),
            transportEvent.exception(),
            transportEvent.message());

    RetryContext retryContext = null;
    ReverseConnectTargetSnapshot snapshot = null;
    boolean notifyAttemptEvent = true;
    synchronized (lock) {
      TargetRecord record = records.get(targetId);
      if (record == null || record.attemptCounter != attemptNumber) {
        // The target or attempt generation was invalidated after this transport attempt started.
        // Preserve observability for the transition, but do not let stale attempts mutate state.
        snapshot = null;
      } else if (isTerminal(event.state())) {
        boolean handoff = event.state() == ReverseConnectAttemptState.HANDOFF;
        if (handoff && shutdown) {
          return;
        }
        if (record.generation == attemptGeneration || handoff) {
          record.activeAttempt = null;
          record.attemptInProgress = false;

          if (handoff) {
            record.pendingHandoffAttempts.add(new AttemptKey(attemptNumber, attemptGeneration));
            record.lastSuccessTime = event.timestamp();
            record.lastStatusCode = null;
            record.lastError = null;
          } else if (event.state() == ReverseConnectAttemptState.FAILED
              || event.state() == ReverseConnectAttemptState.CLIENT_ERROR) {
            record.lastStatusCode = event.statusCode();
            record.lastError = event.exception();
          }

          if (shouldRetry(record, event)) {
            retryContext =
                new RetryContext(targetId, attemptNumber, attemptGeneration, record.target, event);
            notifyAttemptEvent = false;
          }

          snapshot = record.snapshot();
        }
      }
    }

    if (retryContext != null) {
      scheduleRetryEvaluation(retryContext, snapshot);
    } else if (notifyAttemptEvent) {
      notifyAttemptEvent(event);
    }

    if (retryContext == null) {
      if (snapshot != null) {
        notifyTargetUpdated(snapshot);
      }
    }
  }

  private void onAttemptChannel(
      UUID targetId, long attemptNumber, long attemptGeneration, Channel channel) {
    ReverseConnectTargetSnapshot snapshot = null;
    boolean closeChannel = false;

    synchronized (lock) {
      TargetRecord record = records.get(targetId);
      if (record == null || shutdown) {
        closeChannel = true;
      } else {
        var attemptKey = new AttemptKey(attemptNumber, attemptGeneration);
        boolean currentAttempt =
            record.attemptCounter == attemptNumber && record.generation == attemptGeneration;
        boolean pendingHandoff = record.pendingHandoffAttempts.remove(attemptKey);

        if (currentAttempt || pendingHandoff) {
          record.activeChannels.put(channel.id().asLongText(), channel);
          snapshot = record.snapshot();
        } else {
          closeChannel = true;
        }
      }
    }

    if (closeChannel) {
      channel.close();
      return;
    }

    channel.closeFuture().addListener(f -> onActiveChannelClosed(targetId, channel));

    notifyTargetUpdated(snapshot);
  }

  private void onActiveChannelClosed(UUID targetId, Channel channel) {
    PostCloseRetryContext retryContext = null;
    ReverseConnectTargetSnapshot snapshot;

    synchronized (lock) {
      TargetRecord record = records.get(targetId);
      if (record == null) {
        return;
      }

      record.activeChannels.remove(channel.id().asLongText());

      if (running
          && !shutdown
          && record.isSchedulable()
          && record.activeChannels.isEmpty()
          && !record.hasPendingAttempt()) {
        ReverseConnectAttemptEvent event =
            new ReverseConnectAttemptEvent(
                targetId,
                record.attemptCounter,
                ReverseConnectAttemptState.CLOSED,
                Instant.now(),
                null,
                null,
                "reverse-connect active channel closed");

        retryContext = new PostCloseRetryContext(targetId, record.generation, record.target, event);
      }

      snapshot = record.snapshot();
    }

    if (retryContext != null) {
      schedulePostCloseRetryEvaluation(retryContext, snapshot);
    } else {
      notifyTargetUpdated(snapshot);
    }
  }

  private boolean shouldRetry(TargetRecord record, ReverseConnectAttemptEvent event) {
    return running
        && !shutdown
        && record.isSchedulable()
        && (event.state() == ReverseConnectAttemptState.FAILED
            || event.state() == ReverseConnectAttemptState.CLIENT_ERROR);
  }

  private void scheduleRetryEvaluation(
      RetryContext retryContext, @Nullable ReverseConnectTargetSnapshot terminalSnapshot) {

    try {
      scheduler.execute(() -> evaluateRetry(retryContext));
    } catch (RejectedExecutionException e) {
      logger.warn("Unable to evaluate Reverse Connect retry policy.", e);
      notifyAttemptEvent(retryContext.event());
      if (terminalSnapshot != null) {
        notifyTargetUpdated(terminalSnapshot);
      }
    }
  }

  private void schedulePostCloseRetryEvaluation(
      PostCloseRetryContext retryContext, ReverseConnectTargetSnapshot closedSnapshot) {

    try {
      scheduler.execute(() -> evaluatePostCloseRetry(retryContext));
    } catch (RejectedExecutionException e) {
      logger.warn("Unable to evaluate Reverse Connect retry policy.", e);
      notifyTargetUpdated(closedSnapshot);
    }
  }

  private void evaluateRetry(RetryContext retryContext) {
    if (!isRetryCurrent(retryContext)) {
      notifyAttemptEvent(retryContext.event());
      return;
    }

    long delayMillis = retryDelayMillis(retryContext.target(), retryContext.event());

    ReverseConnectTargetSnapshot snapshot = null;
    synchronized (lock) {
      TargetRecord record = records.get(retryContext.targetId());
      if (record != null && isRetryCurrent(record, retryContext)) {
        scheduleLocked(record, delayMillis);
        snapshot = record.snapshot();
      }
    }

    notifyAttemptEvent(retryContext.event());
    if (snapshot != null) {
      notifyTargetUpdated(snapshot);
    }
  }

  private void evaluatePostCloseRetry(PostCloseRetryContext retryContext) {
    if (!isPostCloseRetryCurrent(retryContext)) {
      return;
    }

    long delayMillis = retryDelayMillis(retryContext.target(), retryContext.event());

    ReverseConnectTargetSnapshot snapshot = null;
    synchronized (lock) {
      TargetRecord record = records.get(retryContext.targetId());
      if (record != null && isPostCloseRetryCurrent(record, retryContext)) {
        scheduleLocked(record, delayMillis);
        snapshot = record.snapshot();
      }
    }

    if (snapshot != null) {
      notifyTargetUpdated(snapshot);
    }
  }

  private boolean isRetryCurrent(RetryContext retryContext) {
    synchronized (lock) {
      TargetRecord record = records.get(retryContext.targetId());
      return record != null && isRetryCurrent(record, retryContext);
    }
  }

  private boolean isRetryCurrent(TargetRecord record, RetryContext retryContext) {
    return record.attemptCounter == retryContext.attemptNumber()
        && record.generation == retryContext.generation()
        && shouldRetry(record, retryContext.event());
  }

  private boolean isPostCloseRetryCurrent(PostCloseRetryContext retryContext) {
    synchronized (lock) {
      TargetRecord record = records.get(retryContext.targetId());
      return record != null && isPostCloseRetryCurrent(record, retryContext);
    }
  }

  private boolean isPostCloseRetryCurrent(TargetRecord record, PostCloseRetryContext retryContext) {

    return record.generation == retryContext.generation()
        && running
        && !shutdown
        && record.isSchedulable()
        && record.activeChannels.isEmpty()
        && !record.hasPendingAttempt();
  }

  private long retryDelayMillis(ReverseConnectTarget target, ReverseConnectAttemptEvent event) {
    try {
      return Math.max(0L, target.getRetryPolicy().getRetryDelayMillis(target, event));
    } catch (Throwable t) {
      logger.warn(
          "Reverse Connect retry policy failed for target {}; using registration period.",
          target.getId(),
          t);
      return target.getRegistrationPeriod().longValue();
    }
  }

  private void validateTarget(ReverseConnectTarget target) {
    validateReverseTransport();

    String targetPath = EndpointUtil.getPath(target.getEndpointUrl());

    boolean match =
        endpointDescriptions.get().stream()
            .filter(
                endpoint ->
                    TransportProfile.TCP_UASC_UABINARY
                        .getUri()
                        .equals(endpoint.getTransportProfileUri()))
            .anyMatch(
                endpoint ->
                    target.getEndpointUrl().equals(endpoint.getEndpointUrl())
                        || targetPath.equals(EndpointUtil.getPath(endpoint.getEndpointUrl())));

    if (!match) {
      throw new IllegalArgumentException(
          "Reverse Connect target endpointUrl does not match a configured opc.tcp endpoint: "
              + target.getEndpointUrl());
    }
  }

  private void validateTargetsLocked() {
    records.values().stream()
        .filter(TargetRecord::isSchedulable)
        .map(record -> record.target)
        .forEach(this::validateTarget);
  }

  private void validateReverseTransport() {
    OpcServerTransport transport = transportSupplier.apply(TransportProfile.TCP_UASC_UABINARY);
    if (!(transport instanceof OpcTcpServerTransport)) {
      throw new IllegalArgumentException(
          "Reverse Connect requires OpcTcpServerTransport for opc.tcp targets");
    }
  }

  private void notifyTargetAdded(ReverseConnectTargetSnapshot snapshot) {
    listenersSnapshot()
        .forEach(
            listener ->
                listenerExecutor.execute(
                    () -> safelyNotify(() -> listener.onTargetAdded(snapshot))));
  }

  private void notifyTargetUpdated(ReverseConnectTargetSnapshot snapshot) {
    listenersSnapshot()
        .forEach(
            listener ->
                listenerExecutor.execute(
                    () -> safelyNotify(() -> listener.onTargetUpdated(snapshot))));
  }

  private void notifyTargetRemoved(ReverseConnectTargetSnapshot snapshot) {
    listenersSnapshot()
        .forEach(
            listener ->
                listenerExecutor.execute(
                    () -> safelyNotify(() -> listener.onTargetRemoved(snapshot))));
  }

  private void notifyAttemptEvent(ReverseConnectAttemptEvent event) {
    listenersSnapshot()
        .forEach(
            listener ->
                listenerExecutor.execute(() -> safelyNotify(() -> listener.onAttemptEvent(event))));
  }

  private List<ReverseConnectTargetListener> listenersSnapshot() {
    synchronized (lock) {
      return List.copyOf(listeners);
    }
  }

  private void safelyNotify(Runnable notify) {
    try {
      notify.run();
    } catch (Throwable t) {
      logger.warn("Reverse Connect target listener failed.", t);
    }
  }

  private static boolean isTerminal(ReverseConnectAttemptState state) {
    return switch (state) {
      case HANDOFF, CLIENT_ERROR, FAILED, CANCELLED, CLOSED -> true;
      case CONNECTING, CONNECTED, REVERSE_HELLO_SENT, HELLO_HANDLER_INSTALLED -> false;
    };
  }

  private static boolean isSuccessfulHandoff(OpcTcpServerReverseConnectAttempt attempt) {
    CompletableFuture<Channel> channelFuture = attempt.channelFuture();

    return attempt.state() == OpcTcpServerReverseConnectAttemptState.HANDOFF
        || (channelFuture.isDone()
            && !channelFuture.isCancelled()
            && !channelFuture.isCompletedExceptionally());
  }

  private static ReverseConnectAttemptState mapState(OpcTcpServerReverseConnectAttemptState state) {

    return switch (state) {
      case CONNECTING -> ReverseConnectAttemptState.CONNECTING;
      case CONNECTED -> ReverseConnectAttemptState.CONNECTED;
      case REVERSE_HELLO_SENT -> ReverseConnectAttemptState.REVERSE_HELLO_SENT;
      case HELLO_HANDLER_INSTALLED -> ReverseConnectAttemptState.HELLO_HANDLER_INSTALLED;
      case HANDOFF -> ReverseConnectAttemptState.HANDOFF;
      case CLIENT_ERROR -> ReverseConnectAttemptState.CLIENT_ERROR;
      case FAILED -> ReverseConnectAttemptState.FAILED;
      case CANCELLED -> ReverseConnectAttemptState.CANCELLED;
      case CLOSED -> ReverseConnectAttemptState.CLOSED;
    };
  }

  private static final class TargetRecord {

    private ReverseConnectTarget target;
    private final Map<String, Channel> activeChannels = new LinkedHashMap<>();
    private final Set<AttemptKey> pendingHandoffAttempts = new HashSet<>();

    private boolean paused;
    private long generation = 0L;
    private long attemptCounter = 0L;
    private boolean attemptInProgress = false;
    private @Nullable ScheduledFuture<?> scheduledFuture;
    private @Nullable OpcTcpServerReverseConnectAttempt activeAttempt;
    private @Nullable Instant nextAttemptTime;
    private @Nullable Instant lastAttemptTime;
    private @Nullable Instant lastSuccessTime;
    private @Nullable StatusCode lastStatusCode;
    private @Nullable Throwable lastError;

    private TargetRecord(ReverseConnectTarget target) {
      this.target = target;
      this.paused = target.isPaused();
    }

    private boolean isSchedulable() {
      return target.isEnabled() && !paused;
    }

    private boolean hasPendingAttempt() {
      return attemptInProgress || activeAttempt != null || !pendingHandoffAttempts.isEmpty();
    }

    private ReverseConnectTargetSnapshot snapshot() {
      return new ReverseConnectTargetSnapshot(
          target.getId(),
          target.getClientListenerUrl(),
          target.getEndpointUrl(),
          target.getRegistrationPeriod(),
          target.getConnectTimeout(),
          target.isEnabled(),
          paused,
          nextAttemptTime,
          lastAttemptTime,
          lastSuccessTime,
          activeChannels.size(),
          lastStatusCode,
          lastError);
    }
  }

  private record AttemptStart(
      ReverseConnectTarget target,
      long number,
      long generation,
      ReverseConnectTargetSnapshot snapshot) {}

  private record AttemptKey(long number, long generation) {}

  private record RetryContext(
      UUID targetId,
      long attemptNumber,
      long generation,
      ReverseConnectTarget target,
      ReverseConnectAttemptEvent event) {}

  private record PostCloseRetryContext(
      UUID targetId,
      long generation,
      ReverseConnectTarget target,
      ReverseConnectAttemptEvent event) {}

  private record TargetAction(
      ReverseConnectTargetSnapshot snapshot, @Nullable OpcTcpServerReverseConnectAttempt attempt) {

    void closeAttempt() {
      if (attempt != null) {
        attempt.close();
      }
    }
  }
}
