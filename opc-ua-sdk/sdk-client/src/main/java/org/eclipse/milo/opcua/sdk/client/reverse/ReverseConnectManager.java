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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side listener and matching manager for OPC UA Reverse Connect.
 *
 * <p>The manager owns only the pre-UASC control plane: listener bind/unbind, first-message
 * validation, synchronous verifier and selector matching, pending candidate expiry, and immutable
 * observability. A claimed {@link ReverseConnectConnection} still needs later transport code to
 * attach the standard Milo client pipeline and continue with {@code Hello}, {@code Acknowledge},
 * SecureChannel, and Session negotiation.
 */
public final class ReverseConnectManager implements AutoCloseable {

  private static final int UACP_HEADER_LENGTH = 8;
  private static final int MAX_REVERSE_HELLO_MESSAGE_SIZE = UACP_HEADER_LENGTH + (2 * (4 + 4096));

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /*
   * Threading model:
   *
   * A ReentrantLock is used instead of a monitor, so callers running on virtual threads can park
   * without carrier-thread pinning on runtimes where synchronized blocks still have that behavior.
   *
   * lock guards manager lifecycle (running, stopped), listener state, selector
   * registrations, candidate maps and history, counters, lastError, and every mutable
   * field on Candidate and ListenerState. Code that needs to inspect or mutate those
   * fields must either hold lock or work from immutable snapshots copied while holding it.
   *
   * Do not call user code while holding the lock. Listener callbacks, verifiers, selectors,
   * future completions, and Netty channel I/O must happen after leaving the critical
   * section. Listener notifications are always sent through listenerQueue from copied
   * snapshots; callback ordering is serialized, but callbacks must not observe mutable
   * manager internals.
   *
   * Timers run on the manager scheduler. They re-enter the manager, validate the candidate
   * state under lock, capture any channels or snapshots they need, then close, write, or
   * notify outside the lock. A claimed candidate transfers channel ownership out of the
   * manager; after CLAIMED, manager snapshots remain immutable history and shutdown does
   * not close that child channel.
   */
  private final ReentrantLock lock = new ReentrantLock();

  private final List<ListenerState> listenerStates;
  private final ScheduledExecutorService scheduler;
  private final EventLoopGroup eventLoop;
  private final Duration firstMessageTimeout;
  private final Duration pendingConnectionHoldTime;
  private final int maxPendingCandidates;
  private final int maxRetainedCandidateSnapshots;
  private final ReverseHelloVerifier reverseHelloVerifier;
  private final Consumer<ServerBootstrap> bootstrapCustomizer;
  private final ExecutionQueue listenerQueue;

  private final Map<UUID, Candidate> candidates = new LinkedHashMap<>();
  private final Map<UUID, Candidate> pendingCandidates = new LinkedHashMap<>();
  private final Map<UUID, SelectorRegistration> selectorRegistrations = new LinkedHashMap<>();
  private final Deque<ReverseConnectCandidateSnapshot> acceptedCandidates = new ArrayDeque<>();
  private final Deque<ReverseConnectCandidateSnapshot> rejectedCandidates = new ArrayDeque<>();
  private final List<ReverseConnectListener> listeners = new ArrayList<>();

  private boolean running = false;
  private boolean stopped = false;

  private long acceptedCount = 0;
  private long claimedCount = 0;
  private long rejectedCount = 0;
  private long expiredCount = 0;
  private @Nullable String lastError = null;

  ReverseConnectManager(
      List<InetSocketAddress> bindAddresses,
      Executor callbackExecutor,
      ScheduledExecutorService scheduler,
      EventLoopGroup eventLoop,
      Duration firstMessageTimeout,
      Duration pendingConnectionHoldTime,
      int maxPendingCandidates,
      int maxRetainedCandidateSnapshots,
      ReverseHelloVerifier reverseHelloVerifier,
      Consumer<ServerBootstrap> bootstrapCustomizer) {

    this.listenerStates = bindAddresses.stream().map(ListenerState::new).toList();
    this.scheduler = scheduler;
    this.eventLoop = eventLoop;
    this.firstMessageTimeout = firstMessageTimeout;
    this.pendingConnectionHoldTime = pendingConnectionHoldTime;
    this.maxPendingCandidates = maxPendingCandidates;
    this.maxRetainedCandidateSnapshots = maxRetainedCandidateSnapshots;
    this.reverseHelloVerifier = reverseHelloVerifier;
    this.bootstrapCustomizer = bootstrapCustomizer;

    this.listenerQueue = new ExecutionQueue(callbackExecutor);
  }

  /**
   * Create a new manager builder.
   *
   * @return a new builder.
   */
  public static ReverseConnectManagerBuilder builder() {
    return new ReverseConnectManagerBuilder();
  }

  /**
   * Bind all configured listener addresses synchronously.
   *
   * <p>If one listener fails to bind, its snapshot records the bind diagnostic, any listeners that
   * were already bound are shut down, and the startup exception is rethrown to the caller.
   *
   * @throws Exception if any listener fails to bind.
   */
  public void startup() throws Exception {
    lock.lock();
    try {
      if (running) {
        throw new IllegalStateException("ReverseConnectManager is already running");
      }
      if (stopped) {
        throw new IllegalStateException("ReverseConnectManager cannot be restarted");
      }

      running = true;
    } finally {
      lock.unlock();
    }

    try {
      for (ListenerState listenerState : listenerStates) {
        ReverseConnectListenerSnapshot boundSnapshot;

        try {
          ServerBootstrap bootstrap = newServerBootstrap(listenerState);

          bootstrapCustomizer.accept(bootstrap);

          ChannelFuture bindFuture = bootstrap.bind(listenerState.bindAddress).sync();

          lock.lock();
          try {
            listenerState.bindChannel = bindFuture.channel();
            listenerState.boundAddress = bindFuture.channel().localAddress();
            listenerState.lastError = null;

            boundSnapshot = listenerSnapshotLocked(listenerState);
          } finally {
            lock.unlock();
          }
        } catch (Exception e) {
          recordListenerError(listenerState, e);
          throw e;
        }

        fireListenerBound(boundSnapshot);
      }
    } catch (Exception e) {
      recordError(e);

      try {
        shutdown();
      } catch (Exception shutdownError) {
        e.addSuppressed(shutdownError);
      }

      throw e;
    }
  }

  /** Stop all listeners synchronously and close unclaimed candidates. */
  public void shutdown() {
    List<SelectorRegistration> registrationsToCancel;
    List<Channel> channelsToClose = new ArrayList<>();
    List<ReverseConnectListenerSnapshot> unboundSnapshots = new ArrayList<>();
    List<ReverseConnectCandidateSnapshot> rejectedSnapshots = new ArrayList<>();

    lock.lock();
    try {
      stopped = true;

      if (running) {
        running = false;

        for (ListenerState listenerState : listenerStates) {
          Channel bindChannel = listenerState.bindChannel;
          if (bindChannel != null) {
            channelsToClose.add(bindChannel);

            listenerState.bindChannel = null;
            listenerState.boundAddress = null;

            unboundSnapshots.add(listenerSnapshotLocked(listenerState));
          } else {
            listenerState.boundAddress = null;
          }
        }

        for (Candidate candidate : candidates.values()) {
          if (candidate.state == ReverseConnectCandidateState.WAITING_FOR_REVERSE_HELLO
              || candidate.state == ReverseConnectCandidateState.PENDING) {

            cancelPendingExpiry(candidate);
            pendingCandidates.remove(candidate.id);

            candidate.state = ReverseConnectCandidateState.REJECTED;
            candidate.completedAt = Instant.now();
            candidate.rejectionReason = ReverseConnectRejectionReason.MANAGER_STOPPED;
            candidate.rejectionStatusCode = new StatusCode(StatusCodes.Bad_Shutdown);
            candidate.diagnostic = "ReverseConnectManager stopped";

            rejectedCount++;
            candidate.listenerState.rejectedCount++;

            channelsToClose.add(candidate.channel);
            ReverseConnectCandidateSnapshot rejectedSnapshot = candidate.snapshot();
            retainRejectedCandidateLocked(rejectedSnapshot);
            rejectedSnapshots.add(rejectedSnapshot);
          }
        }

        candidates.clear();
        pendingCandidates.clear();
      }

      registrationsToCancel = new ArrayList<>(selectorRegistrations.values());
      selectorRegistrations.clear();
    } finally {
      lock.unlock();
    }

    channelsToClose.forEach(ReverseConnectManager::closeSynchronously);

    CancellationException cancellationException =
        new CancellationException("ReverseConnectManager stopped");

    registrationsToCancel.forEach(
        r -> r.connectionFuture.completeExceptionally(cancellationException));

    unboundSnapshots.forEach(this::fireListenerUnbound);
    rejectedSnapshots.forEach(this::fireCandidateRejected);
  }

  @Override
  public void close() {
    shutdown();
  }

  /**
   * Register a one-shot selector.
   *
   * <p>If an existing pending candidate matches, the registration may complete immediately. If not,
   * the registration remains active until a future candidate matches, the registration is closed,
   * or the manager shuts down.
   *
   * @param selector the selector used to claim a candidate.
   * @return the one-shot registration.
   */
  public ReverseConnectRegistration registerSelector(ReverseConnectSelector selector) {
    SelectorRegistration registration = new SelectorRegistration(selector);
    IllegalStateException stoppedException = null;

    lock.lock();
    try {
      if (stopped) {
        stoppedException = new IllegalStateException("ReverseConnectManager is stopped");
      } else {
        selectorRegistrations.put(registration.id, registration);
      }
    } finally {
      lock.unlock();
    }

    if (stoppedException != null) {
      registration.connectionFuture.completeExceptionally(stoppedException);
    }

    if (!registration.connectionFuture.isDone()) {
      tryClaimPendingForRegistration(registration);
    }

    return new ReverseConnectRegistration(this, registration);
  }

  /**
   * Claim a specific pending reverse-connect candidate.
   *
   * <p>This is the primitive used by dynamic inbound-client factories. The application observes a
   * pending candidate, claims that exact candidate by id, and then either resolves an {@link
   * org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig} for the returned {@link
   * ReverseConnectConnection} or closes the connection itself. A successful claim cancels pending
   * expiry, records the candidate as claimed, and transfers channel ownership out of the manager;
   * later manager shutdown will not close the claimed channel.
   *
   * @param candidateId the manager-assigned candidate id.
   * @return the claimed connection, or {@link Optional#empty()} if the candidate is unknown or is
   *     no longer pending.
   */
  public Optional<ReverseConnectConnection> claim(UUID candidateId) {
    requireNonNull(candidateId, "candidateId");

    ClaimedCandidate claimedCandidate;

    lock.lock();
    try {
      claimedCandidate = claimCandidateLocked(candidateId);
      if (claimedCandidate == null) {
        return Optional.empty();
      }
    } finally {
      lock.unlock();
    }

    fireCandidateClaimed(claimedCandidate.snapshot());

    return Optional.of(claimedCandidate.connection());
  }

  /**
   * Explicitly reject an unclaimed candidate.
   *
   * <p>Applications can use this to fail an unknown or unauthorized inbound server immediately,
   * including a candidate that is still waiting for its first {@code ReverseHello}, instead of
   * letting the candidate remain pending until its hold time expires. A successful rejection writes
   * an OPC UA TCP {@code ErrorMessage} when the channel is still open, closes the channel, records
   * the diagnostic in retained snapshots, and emits a rejected-candidate event.
   *
   * @param candidateId the manager-assigned candidate id.
   * @param statusCode the TCP error status sent to the peer when possible.
   * @param diagnostic the diagnostic sent to the peer and retained in snapshots.
   * @return true if a waiting or pending candidate was rejected.
   */
  public boolean reject(UUID candidateId, StatusCode statusCode, String diagnostic) {
    requireNonNull(candidateId, "candidateId");
    requireNonNull(statusCode, "statusCode");
    requireNonNull(diagnostic, "diagnostic");

    return rejectCandidate(
        candidateId, ReverseConnectRejectionReason.APPLICATION_REJECTED, statusCode, diagnostic);
  }

  void unregisterSelector(UUID registrationId) {
    SelectorRegistration registration;

    lock.lock();
    try {
      registration = selectorRegistrations.remove(registrationId);
    } finally {
      lock.unlock();
    }

    if (registration != null) {
      registration.connectionFuture.completeExceptionally(
          new CancellationException("ReverseConnect selector registration closed"));
    }
  }

  /**
   * Add a listener for serialized manager events.
   *
   * @param listener the listener to add.
   */
  public void addListener(ReverseConnectListener listener) {
    lock.lock();
    try {
      listeners.add(listener);
    } finally {
      lock.unlock();
    }
  }

  /**
   * Remove a previously added listener.
   *
   * @param listener the listener to remove.
   * @return true if the listener was registered.
   */
  public boolean removeListener(ReverseConnectListener listener) {
    lock.lock();
    try {
      return listeners.remove(listener);
    } finally {
      lock.unlock();
    }
  }

  /**
   * Get an immutable snapshot of the current manager state.
   *
   * @return the manager snapshot.
   */
  public ReverseConnectManagerSnapshot snapshot() {
    lock.lock();
    try {
      return managerSnapshotLocked();
    } finally {
      lock.unlock();
    }
  }

  private ServerBootstrap newServerBootstrap(ListenerState listenerState) {
    return new ServerBootstrap()
        .channel(NioServerSocketChannel.class)
        .group(eventLoop)
        .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel channel) {
                onAcceptedChannel(listenerState, channel);
              }
            });
  }

  private void onAcceptedChannel(ListenerState listenerState, Channel channel) {
    AcceptedCandidate acceptedCandidate = null;

    lock.lock();
    try {
      if (running) {
        Candidate candidate = new Candidate(listenerState, channel);

        candidates.put(candidate.id, candidate);

        acceptedCount++;
        listenerState.acceptedCount++;

        acceptedCandidate = new AcceptedCandidate(candidate.id, candidate.snapshot());
      }
    } finally {
      lock.unlock();
    }

    if (acceptedCandidate == null) {
      channel.close();
      return;
    }

    UUID candidateId = acceptedCandidate.candidateId();
    channel.closeFuture().addListener(future -> onCandidateChannelClosed(candidateId));
    channel.pipeline().addLast(new ReverseHelloHandler(candidateId));

    fireCandidateAccepted(acceptedCandidate.snapshot());
  }

  private void onReverseHello(UUID candidateId, ReverseHelloMessage reverseHello) {
    ReverseConnectCandidateSnapshot verificationSnapshot;

    lock.lock();
    try {
      Candidate candidate = candidates.get(candidateId);
      if (candidate == null
          || candidate.state != ReverseConnectCandidateState.WAITING_FOR_REVERSE_HELLO) {
        return;
      }

      candidate.serverUri = reverseHello.getServerUri();
      candidate.endpointUrl = reverseHello.getEndpointUrl();
      candidate.reverseHelloReceivedAt = Instant.now();

      verificationSnapshot = candidate.snapshot();
    } finally {
      lock.unlock();
    }

    ReverseConnectVerificationResult verificationResult;
    try {
      verificationResult = reverseHelloVerifier.verify(verificationSnapshot);
    } catch (Throwable t) {
      recordError(t);

      rejectCandidate(
          candidateId,
          ReverseConnectRejectionReason.VERIFIER_REJECTED,
          new StatusCode(StatusCodes.Bad_TcpInternalError),
          "ReverseHello verifier failed: " + t.getMessage());
      return;
    }

    if (!verificationResult.isAccepted()) {
      StatusCode statusCode =
          verificationResult.getStatusCode() != null
              ? verificationResult.getStatusCode()
              : new StatusCode(StatusCodes.Bad_TcpEndpointUrlInvalid);
      ReverseConnectRejectionReason reason =
          verificationResult.getRejectionReason() != null
              ? verificationResult.getRejectionReason()
              : ReverseConnectRejectionReason.VERIFIER_REJECTED;
      String diagnostic =
          verificationResult.getDiagnostic() != null
              ? verificationResult.getDiagnostic()
              : "ReverseHello rejected";

      rejectCandidate(candidateId, reason, statusCode, diagnostic);
      return;
    }

    ReverseConnectCandidateSnapshot matchingSnapshot;
    lock.lock();
    try {
      Candidate candidate = candidates.get(candidateId);
      if (candidate == null
          || candidate.state != ReverseConnectCandidateState.WAITING_FOR_REVERSE_HELLO) {
        return;
      }

      candidate.state = ReverseConnectCandidateState.PENDING;
      matchingSnapshot = candidate.snapshot();
    } finally {
      lock.unlock();
    }

    if (tryClaimCandidate(candidateId, matchingSnapshot)) {
      return;
    }

    ParkResult parkResult = parkCandidate(candidateId);
    parkResult.closeRejectedCandidate(this);
    parkResult.fireEvents(this);

    if (parkResult.pendingSnapshot != null) {
      tryClaimCandidate(candidateId, parkResult.pendingSnapshot);
    }
  }

  private void onFirstMessageTimeout(UUID candidateId) {
    rejectCandidate(
        candidateId,
        ReverseConnectRejectionReason.FIRST_MESSAGE_TIMEOUT,
        new StatusCode(StatusCodes.Bad_Timeout),
        "timed out waiting for ReverseHello",
        ReverseConnectCandidateState.WAITING_FOR_REVERSE_HELLO,
        true);
  }

  private void onFirstMessageException(UUID candidateId, Throwable cause) {
    StatusCode statusCode = statusCode(cause);
    String message = cause.getMessage() != null ? cause.getMessage() : cause.toString();

    rejectCandidate(
        candidateId, ReverseConnectRejectionReason.MALFORMED_REVERSE_HELLO, statusCode, message);
  }

  private void onCandidateChannelClosed(UUID candidateId) {
    ReverseConnectCandidateSnapshot closedSnapshot = null;

    lock.lock();
    try {
      Candidate candidate = candidates.remove(candidateId);
      if (candidate == null) {
        return;
      }

      if (candidate.state == ReverseConnectCandidateState.WAITING_FOR_REVERSE_HELLO
          || candidate.state == ReverseConnectCandidateState.PENDING) {

        cancelPendingExpiry(candidate);
        pendingCandidates.remove(candidate.id);

        candidate.state = ReverseConnectCandidateState.CLOSED;
        candidate.completedAt = Instant.now();
        candidate.rejectionReason = ReverseConnectRejectionReason.CHANNEL_CLOSED;
        candidate.rejectionStatusCode = new StatusCode(StatusCodes.Bad_ConnectionClosed);
        candidate.diagnostic = "reverse-connect channel closed before it was claimed";

        rejectedCount++;
        candidate.listenerState.rejectedCount++;

        closedSnapshot = candidate.snapshot();
        retainRejectedCandidateLocked(closedSnapshot);
      }
    } finally {
      lock.unlock();
    }

    if (closedSnapshot != null) {
      fireCandidateRejected(closedSnapshot);
    }
  }

  private boolean tryClaimCandidate(
      UUID candidateId, ReverseConnectCandidateSnapshot candidateSnapshot) {

    List<SelectorRegistration> registrations = selectorRegistrationsSnapshot();

    for (SelectorRegistration registration : registrations) {
      boolean matches;

      try {
        matches = registration.selector.matches(candidateSnapshot);
      } catch (Throwable t) {
        failSelectorRegistration(registration, t);
        continue;
      }

      if (matches) {
        ClaimResult claimResult = claimCandidate(candidateId, registration.id);

        if (claimResult.claimed()) {
          claimResult.completeAndFire(this);
          return true;
        }
      }
    }

    return false;
  }

  private void tryClaimPendingForRegistration(SelectorRegistration registration) {
    List<ReverseConnectCandidateSnapshot> snapshots;

    lock.lock();
    try {
      if (!selectorRegistrations.containsKey(registration.id)) {
        return;
      }

      snapshots = pendingCandidates.values().stream().map(Candidate::snapshot).toList();
    } finally {
      lock.unlock();
    }

    for (ReverseConnectCandidateSnapshot snapshot : snapshots) {
      boolean matches;

      try {
        matches = registration.selector.matches(snapshot);
      } catch (Throwable t) {
        failSelectorRegistration(registration, t);
        return;
      }

      if (matches) {
        ClaimResult claimResult = claimCandidate(snapshot.id(), registration.id);

        if (claimResult.claimed()) {
          claimResult.completeAndFire(this);
          return;
        }
      }
    }
  }

  private ClaimResult claimCandidate(UUID candidateId, UUID registrationId) {
    ClaimedCandidate claimedCandidate;
    SelectorRegistration registration;

    lock.lock();
    try {
      registration = selectorRegistrations.get(registrationId);
      if (registration == null) {
        return ClaimResult.notClaimed();
      }

      claimedCandidate = claimCandidateLocked(candidateId);
      if (claimedCandidate == null) {
        return ClaimResult.notClaimed();
      }

      selectorRegistrations.remove(registrationId);
    } finally {
      lock.unlock();
    }

    return new ClaimResult(
        claimedCandidate.connection(), registration, claimedCandidate.snapshot());
  }

  private @Nullable ClaimedCandidate claimCandidateLocked(UUID candidateId) {
    Candidate candidate = candidates.get(candidateId);
    if (candidate == null || candidate.state != ReverseConnectCandidateState.PENDING) {
      return null;
    }

    pendingCandidates.remove(candidateId);
    candidates.remove(candidateId);
    cancelPendingExpiry(candidate);

    candidate.state = ReverseConnectCandidateState.CLAIMED;
    candidate.completedAt = Instant.now();

    claimedCount++;
    candidate.listenerState.claimedCount++;

    ReverseConnectCandidateSnapshot claimedSnapshot = candidate.snapshot();
    retainAcceptedCandidateLocked(claimedSnapshot);

    return new ClaimedCandidate(
        new ReverseConnectConnection(candidate.channel, claimedSnapshot), claimedSnapshot);
  }

  private ParkResult parkCandidate(UUID candidateId) {
    Channel channelToClose = null;
    ReverseConnectCandidateSnapshot pendingSnapshot = null;
    ReverseConnectCandidateSnapshot rejectedSnapshot = null;
    StatusCode statusCode = null;
    String diagnostic = null;

    lock.lock();
    try {
      Candidate candidate = candidates.get(candidateId);

      if (candidate == null || candidate.state != ReverseConnectCandidateState.PENDING) {
        return ParkResult.none();
      }

      if (pendingCandidates.size() >= maxPendingCandidates) {
        candidate.state = ReverseConnectCandidateState.REJECTED;
        candidate.completedAt = Instant.now();
        candidate.rejectionReason = ReverseConnectRejectionReason.PENDING_LIMIT_EXCEEDED;
        candidate.rejectionStatusCode = new StatusCode(StatusCodes.Bad_TcpNotEnoughResources);
        candidate.diagnostic = "maximum pending reverse-connect candidates exceeded";

        rejectedCount++;
        candidate.listenerState.rejectedCount++;

        channelToClose = candidate.channel;
        statusCode = candidate.rejectionStatusCode;
        diagnostic = candidate.diagnostic;
        rejectedSnapshot = candidate.snapshot();
        candidates.remove(candidate.id);
        retainRejectedCandidateLocked(rejectedSnapshot);
      } else {
        pendingCandidates.put(candidate.id, candidate);
        candidate.pendingExpiry =
            scheduler.schedule(
                () -> expirePendingCandidate(candidate.id),
                pendingConnectionHoldTime.toMillis(),
                TimeUnit.MILLISECONDS);

        pendingSnapshot = candidate.snapshot();
      }
    } finally {
      lock.unlock();
    }

    return new ParkResult(
        channelToClose, statusCode, diagnostic, pendingSnapshot, rejectedSnapshot);
  }

  private void expirePendingCandidate(UUID candidateId) {
    Channel channelToClose;
    ReverseConnectCandidateSnapshot expiredSnapshot;

    lock.lock();
    try {
      Candidate candidate = pendingCandidates.remove(candidateId);

      if (candidate == null || candidate.state != ReverseConnectCandidateState.PENDING) {
        return;
      }

      cancelPendingExpiry(candidate);

      candidate.state = ReverseConnectCandidateState.EXPIRED;
      candidate.completedAt = Instant.now();
      candidate.rejectionReason = ReverseConnectRejectionReason.PENDING_EXPIRED;
      candidate.rejectionStatusCode = new StatusCode(StatusCodes.Bad_Timeout);
      candidate.diagnostic = "reverse-connect candidate was not claimed before hold time elapsed";

      expiredCount++;
      candidate.listenerState.expiredCount++;

      channelToClose = candidate.channel;
      expiredSnapshot = candidate.snapshot();
      candidates.remove(candidateId);
      retainRejectedCandidateLocked(expiredSnapshot);
    } finally {
      lock.unlock();
    }

    closeWithError(
        channelToClose, expiredSnapshot.rejectionStatusCode(), expiredSnapshot.diagnostic());

    fireCandidateExpired(expiredSnapshot);
  }

  private boolean rejectCandidate(
      UUID candidateId,
      ReverseConnectRejectionReason reason,
      StatusCode statusCode,
      String diagnostic) {
    return rejectCandidate(candidateId, reason, statusCode, diagnostic, null, false);
  }

  private boolean rejectCandidate(
      UUID candidateId,
      ReverseConnectRejectionReason reason,
      StatusCode statusCode,
      String diagnostic,
      @Nullable ReverseConnectCandidateState requiredState,
      boolean requireReverseHelloNotReceived) {

    Channel channelToClose;
    ReverseConnectCandidateSnapshot rejectedSnapshot;

    lock.lock();
    try {
      Candidate candidate = candidates.get(candidateId);
      if (candidate == null
          || candidate.state == ReverseConnectCandidateState.CLAIMED
          || candidate.state == ReverseConnectCandidateState.REJECTED
          || candidate.state == ReverseConnectCandidateState.EXPIRED
          || candidate.state == ReverseConnectCandidateState.CLOSED
          || (requiredState != null && candidate.state != requiredState)
          || (requireReverseHelloNotReceived && candidate.reverseHelloReceivedAt != null)) {
        return false;
      }

      pendingCandidates.remove(candidateId);
      candidates.remove(candidateId);
      cancelPendingExpiry(candidate);

      candidate.state = ReverseConnectCandidateState.REJECTED;
      candidate.completedAt = Instant.now();
      candidate.rejectionReason = reason;
      candidate.rejectionStatusCode = statusCode;
      candidate.diagnostic = diagnostic;

      rejectedCount++;
      candidate.listenerState.rejectedCount++;

      channelToClose = candidate.channel;
      rejectedSnapshot = candidate.snapshot();
      retainRejectedCandidateLocked(rejectedSnapshot);
    } finally {
      lock.unlock();
    }

    closeWithError(channelToClose, statusCode, diagnostic);

    fireCandidateRejected(rejectedSnapshot);

    return true;
  }

  private List<SelectorRegistration> selectorRegistrationsSnapshot() {
    lock.lock();
    try {
      return List.copyOf(selectorRegistrations.values());
    } finally {
      lock.unlock();
    }
  }

  private void failSelectorRegistration(SelectorRegistration registration, Throwable t) {
    boolean removed;

    lock.lock();
    try {
      removed = selectorRegistrations.remove(registration.id) != null;
      recordErrorLocked(t);
    } finally {
      lock.unlock();
    }

    if (removed) {
      registration.connectionFuture.completeExceptionally(t);
    }

    fireError(t);
  }

  private void recordError(Throwable t) {
    lock.lock();
    try {
      recordErrorLocked(t);
    } finally {
      lock.unlock();
    }

    fireError(t);
  }

  private void recordListenerError(ListenerState listenerState, Throwable t) {
    lock.lock();
    try {
      recordListenerErrorLocked(listenerState, t);
    } finally {
      lock.unlock();
    }
  }

  private void recordErrorLocked(Throwable t) {
    lastError = errorDiagnostic(t);
  }

  private void recordListenerErrorLocked(ListenerState listenerState, Throwable t) {
    listenerState.lastError = errorDiagnostic(t);
  }

  private static String errorDiagnostic(Throwable t) {
    return t.getMessage() != null ? t.getMessage() : t.toString();
  }

  private ReverseConnectManagerSnapshot managerSnapshotLocked() {
    return new ReverseConnectManagerSnapshot(
        running,
        listenerStates.stream().map(this::listenerSnapshotLocked).toList(),
        pendingCandidates.values().stream().map(Candidate::snapshot).toList(),
        new ArrayList<>(acceptedCandidates),
        new ArrayList<>(rejectedCandidates),
        acceptedCount,
        claimedCount,
        rejectedCount,
        expiredCount,
        lastError);
  }

  private void retainAcceptedCandidateLocked(ReverseConnectCandidateSnapshot snapshot) {
    retainCandidateSnapshotLocked(acceptedCandidates, snapshot);
  }

  private void retainRejectedCandidateLocked(ReverseConnectCandidateSnapshot snapshot) {
    retainCandidateSnapshotLocked(rejectedCandidates, snapshot);
  }

  private void retainCandidateSnapshotLocked(
      Deque<ReverseConnectCandidateSnapshot> snapshots, ReverseConnectCandidateSnapshot snapshot) {

    if (maxRetainedCandidateSnapshots == 0) {
      return;
    }

    while (snapshots.size() >= maxRetainedCandidateSnapshots) {
      snapshots.removeFirst();
    }

    snapshots.addLast(snapshot);
  }

  private ReverseConnectListenerSnapshot listenerSnapshotLocked(ListenerState listenerState) {
    int listenerPendingCount = 0;
    for (Candidate candidate : pendingCandidates.values()) {
      if (candidate.listenerState == listenerState) {
        listenerPendingCount++;
      }
    }

    Channel bindChannel = listenerState.bindChannel;
    boolean bound = bindChannel != null && bindChannel.isOpen();

    return new ReverseConnectListenerSnapshot(
        listenerState.bindAddress,
        listenerState.boundAddress,
        bound,
        listenerState.acceptedCount,
        listenerState.claimedCount,
        listenerState.rejectedCount,
        listenerState.expiredCount,
        listenerPendingCount,
        listenerState.lastError);
  }

  private List<ReverseConnectListener> listenersSnapshot() {
    lock.lock();
    try {
      return List.copyOf(listeners);
    } finally {
      lock.unlock();
    }
  }

  private void fireListenerBound(ReverseConnectListenerSnapshot snapshot) {
    fireEvent(listener -> listener.onListenerBound(snapshot));
  }

  private void fireListenerUnbound(ReverseConnectListenerSnapshot snapshot) {
    fireEvent(listener -> listener.onListenerUnbound(snapshot));
  }

  private void fireCandidateAccepted(ReverseConnectCandidateSnapshot snapshot) {
    fireEvent(listener -> listener.onCandidateAccepted(snapshot));
  }

  private void fireCandidatePending(ReverseConnectCandidateSnapshot snapshot) {
    fireEvent(listener -> listener.onCandidatePending(snapshot));
  }

  private void fireCandidateClaimed(ReverseConnectCandidateSnapshot snapshot) {
    fireEvent(listener -> listener.onCandidateClaimed(snapshot));
  }

  private void fireCandidateRejected(ReverseConnectCandidateSnapshot snapshot) {
    fireEvent(listener -> listener.onCandidateRejected(snapshot));
  }

  private void fireCandidateExpired(ReverseConnectCandidateSnapshot snapshot) {
    fireEvent(listener -> listener.onCandidateExpired(snapshot));
  }

  private void fireError(Throwable t) {
    fireEvent(listener -> listener.onError(t));
  }

  private void fireEvent(Consumer<ReverseConnectListener> consumer) {
    List<ReverseConnectListener> listenersSnapshot = listenersSnapshot();

    if (listenersSnapshot.isEmpty()) {
      return;
    }

    listenerQueue.submit(
        () -> {
          for (ReverseConnectListener listener : listenersSnapshot) {
            try {
              consumer.accept(listener);
            } catch (Throwable t) {
              logger.warn("ReverseConnectListener callback failed.", t);
            }
          }
        });
  }

  private void closeWithError(
      Channel channel, @Nullable StatusCode statusCode, @Nullable String diagnostic) {

    if (!channel.isOpen()) {
      return;
    }

    StatusCode actualStatusCode =
        statusCode != null ? statusCode : new StatusCode(StatusCodes.Bad_TcpInternalError);
    String actualDiagnostic = diagnostic != null ? diagnostic : "reverse-connect candidate closed";

    ByteBuf errorBuffer = null;

    try {
      errorBuffer =
          TcpMessageEncoder.encode(new ErrorMessage(actualStatusCode.value(), actualDiagnostic));

      ScheduledFuture<?> forcedCloseFuture =
          scheduler.schedule(() -> channel.close(), 2, TimeUnit.SECONDS);

      channel.closeFuture().addListener(f -> forcedCloseFuture.cancel(false));
      ChannelFuture writeFuture = channel.writeAndFlush(errorBuffer);
      errorBuffer = null;
      writeFuture.addListener((ChannelFutureListener) f -> channel.close());
    } catch (Exception e) {
      if (errorBuffer != null && errorBuffer.refCnt() > 0) {
        errorBuffer.release();
      }

      logger.debug("Error while writing reverse-connect TCP error message.", e);
      channel.close();
    }
  }

  private static void closeSynchronously(Channel channel) {
    channel.close().syncUninterruptibly();
  }

  private static StatusCode statusCode(Throwable t) {
    if (t instanceof UaException ex) {
      return ex.getStatusCode();
    } else if (t.getCause() instanceof UaException ex) {
      return ex.getStatusCode();
    } else {
      return new StatusCode(StatusCodes.Bad_TcpMessageTypeInvalid);
    }
  }

  private static void cancelPendingExpiry(Candidate candidate) {
    ScheduledFuture<?> pendingExpiry = candidate.pendingExpiry;
    if (pendingExpiry != null) {
      pendingExpiry.cancel(false);
      candidate.pendingExpiry = null;
    }
  }

  private record AcceptedCandidate(UUID candidateId, ReverseConnectCandidateSnapshot snapshot) {}

  private record ClaimedCandidate(
      ReverseConnectConnection connection, ReverseConnectCandidateSnapshot snapshot) {}

  static final class SelectorRegistration {

    final UUID id = UUID.randomUUID();
    final ReverseConnectSelector selector;
    final CompletableFuture<ReverseConnectConnection> connectionFuture = new CompletableFuture<>();

    SelectorRegistration(ReverseConnectSelector selector) {
      this.selector = selector;
    }
  }

  private static final class ListenerState {

    final InetSocketAddress bindAddress;

    @Nullable Channel bindChannel;
    @Nullable SocketAddress boundAddress;
    long acceptedCount;
    long claimedCount;
    long rejectedCount;
    long expiredCount;
    @Nullable String lastError;

    ListenerState(InetSocketAddress bindAddress) {
      this.bindAddress = bindAddress;
    }
  }

  private static final class Candidate {

    final UUID id = UUID.randomUUID();
    final ListenerState listenerState;
    final Channel channel;
    final SocketAddress remoteAddress;
    final SocketAddress localAddress;
    final Instant acceptedAt = Instant.now();

    ReverseConnectCandidateState state = ReverseConnectCandidateState.WAITING_FOR_REVERSE_HELLO;
    @Nullable String serverUri;
    @Nullable String endpointUrl;
    @Nullable Instant reverseHelloReceivedAt;
    @Nullable Instant completedAt;
    @Nullable ReverseConnectRejectionReason rejectionReason;
    @Nullable StatusCode rejectionStatusCode;
    @Nullable String diagnostic;
    @Nullable ScheduledFuture<?> pendingExpiry;

    Candidate(ListenerState listenerState, Channel channel) {
      this.listenerState = listenerState;
      this.channel = channel;
      this.remoteAddress = channel.remoteAddress();
      this.localAddress = channel.localAddress();
    }

    ReverseConnectCandidateSnapshot snapshot() {
      return new ReverseConnectCandidateSnapshot(
          id,
          state,
          serverUri,
          endpointUrl,
          remoteAddress,
          localAddress,
          acceptedAt,
          reverseHelloReceivedAt,
          completedAt,
          rejectionReason,
          rejectionStatusCode,
          diagnostic);
    }
  }

  private record ClaimResult(
      @Nullable ReverseConnectConnection connection,
      @Nullable SelectorRegistration registration,
      @Nullable ReverseConnectCandidateSnapshot snapshot) {

    private static final ClaimResult NOT_CLAIMED = new ClaimResult(null, null, null);

    static ClaimResult notClaimed() {
      return NOT_CLAIMED;
    }

    boolean claimed() {
      return connection != null && registration != null && snapshot != null;
    }

    void completeAndFire(ReverseConnectManager manager) {
      if (connection != null && registration != null && snapshot != null) {
        registration.connectionFuture.complete(connection);
        manager.fireCandidateClaimed(snapshot);
      }
    }
  }

  private record ParkResult(
      @Nullable Channel rejectedChannel,
      @Nullable StatusCode rejectedStatusCode,
      @Nullable String rejectedDiagnostic,
      @Nullable ReverseConnectCandidateSnapshot pendingSnapshot,
      @Nullable ReverseConnectCandidateSnapshot rejectedSnapshot) {

    private static final ParkResult NONE = new ParkResult(null, null, null, null, null);

    static ParkResult none() {
      return NONE;
    }

    void closeRejectedCandidate(ReverseConnectManager manager) {
      if (rejectedChannel != null) {
        manager.closeWithError(rejectedChannel, rejectedStatusCode, rejectedDiagnostic);
      }
    }

    void fireEvents(ReverseConnectManager manager) {
      if (pendingSnapshot() != null) {
        manager.fireCandidatePending(pendingSnapshot());
      }
      if (rejectedSnapshot() != null) {
        manager.fireCandidateRejected(rejectedSnapshot());
      }
    }
  }

  private final class ReverseHelloHandler extends ByteToMessageDecoder {

    private final UUID candidateId;
    private @Nullable ScheduledFuture<?> firstMessageTimeoutFuture;
    private final AtomicBoolean firstMessageReceived = new AtomicBoolean(false);

    ReverseHelloHandler(UUID candidateId) {
      this.candidateId = candidateId;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
      firstMessageTimeoutFuture =
          scheduler.schedule(
              () -> {
                if (!firstMessageReceived.get()) {
                  onFirstMessageTimeout(candidateId);
                }
              },
              firstMessageTimeout.toMillis(),
              TimeUnit.MILLISECONDS);
    }

    @Override
    protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
      firstMessageReceived.set(true);
      cancelFirstMessageTimeout();

      super.handlerRemoved0(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      firstMessageReceived.set(true);
      cancelFirstMessageTimeout();

      super.channelInactive(ctx);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
        throws Exception {

      if (buffer.readableBytes() < UACP_HEADER_LENGTH) {
        return;
      }

      int messageLength = getMessageLength(buffer);
      if (buffer.readableBytes() < messageLength) {
        return;
      }

      MessageType messageType = MessageType.fromMediumInt(buffer.getMediumLE(buffer.readerIndex()));
      char chunkType = (char) buffer.getByte(buffer.readerIndex() + 3);

      if (messageType != MessageType.ReverseHello || chunkType != 'F') {
        throw new UaException(
            StatusCodes.Bad_TcpMessageTypeInvalid,
            "expected ReverseHello RHE/F, received " + messageType + "/" + chunkType);
      }

      firstMessageReceived.set(true);
      cancelFirstMessageTimeout();

      ReverseHelloMessage reverseHello =
          TcpMessageDecoder.decodeReverseHello(buffer.readSlice(messageLength));

      ctx.pipeline().remove(this);

      onReverseHello(candidateId, reverseHello);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      firstMessageReceived.set(true);
      cancelFirstMessageTimeout();

      onFirstMessageException(candidateId, cause);
    }

    private void cancelFirstMessageTimeout() {
      ScheduledFuture<?> timeoutFuture = firstMessageTimeoutFuture;
      if (timeoutFuture != null) {
        timeoutFuture.cancel(false);
        firstMessageTimeoutFuture = null;
      }
    }

    private int getMessageLength(ByteBuf buffer) throws UaException {
      long messageLength = buffer.getUnsignedIntLE(buffer.readerIndex() + 4);

      if (messageLength < UACP_HEADER_LENGTH) {
        throw new UaException(
            StatusCodes.Bad_DecodingError, "invalid ReverseHello message length: " + messageLength);
      }

      if (messageLength > MAX_REVERSE_HELLO_MESSAGE_SIZE) {
        throw new UaException(
            StatusCodes.Bad_TcpMessageTooLarge,
            "ReverseHello message length exceeds maximum: " + messageLength);
      }

      return (int) messageLength;
    }
  }
}
