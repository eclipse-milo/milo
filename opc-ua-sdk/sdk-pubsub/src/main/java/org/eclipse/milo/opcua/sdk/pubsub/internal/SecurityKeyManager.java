/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageNonceSupplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The PubSub key lifecycle engine (Part 14 §8.3.2, §6.2.12.2): one {@link GroupKeyState} per {@link
 * SecurityGroupRef} in use, fed by the {@link SecurityKeyProvider} bound to the ref.
 *
 * <p>Lifecycle (publisher role; a state attached by both roles runs one shared fetch loop):
 *
 * <ul>
 *   <li>fetch on first attach ({@code StartingTokenId} always 0 — never a past token), refresh
 *       every KeyLifetime/2, switch current→next at TimeToNextKey and then every KeyLifetime. All
 *       tasks are ONE-SHOT and rescheduled from each provider response, because TimeToNextKey and
 *       KeyLifetime are re-learned per response.
 *   <li>attached groups stay {@code PreOperational} until the first successful fetch; a fetch that
 *       never succeeds within 2×(configured)KeyLifetime fails them into {@code Error}.
 *   <li>if the last available key expires and no replacement arrives within 2×KeyLifetime, the
 *       publisher snapshot is nulled FIRST (hard-stopping in-flight publish cycles), then every
 *       attached group is failed with {@code Bad_SecurityChecksFailed} (subscriber side included —
 *       symmetric with the publisher rule). The fetch loop keeps retrying; the next success
 *       recovers the groups the manager failed.
 *   <li>a {@link SecurityKeySet} with {@code timeToNextKey == ZERO && keyLifetime == ZERO} is a
 *       static key set: installed once, no refresh/switch/expiry scheduling. Exactly one of the two
 *       being zero is malformed and the fetch is treated as failed.
 *   <li>K8: the provider-returned {@code securityPolicyUri} is authoritative — it must be a
 *       supported {@link PubSubSecurityPolicy} and, when an attached group pins a non-null
 *       configured policy URI, must equal it; otherwise the fetch FAILED (never downgrade).
 * </ul>
 *
 * <p>Subscriber token window: {prev, current, futures}, published as an immutable {@link
 * SubscriberKeyWindow} snapshot. An unknown token triggers a SINGLE-FLIGHT refresh with a 1 s floor
 * (messages keep dropping, counted, meanwhile — no buffering); force-key-reset (SecurityFlags bit
 * 3) triggers the same single-flight refresh. Fetch responses are merged on {@code FirstTokenId}:
 * overlap keeps existing entries, a disconnected {@code FirstTokenId} discards and replaces the
 * window (§8.3.2 / K6).
 *
 * <p>Key material ownership (zeroization posture): each key is split ONCE per token into a {@link
 * SecurityKeyMaterial} when it enters the window and RETIRED exactly when its token leaves the
 * {prev, current, futures} window, on last detach, and on shutdown. Retirement drops every manager
 * reference immediately but defers the zeroizing {@code destroy()} by {@link
 * #RETIRED_KEY_DESTROY_DELAY_NANOS}: the hot-path borrowers — a publish cycle mid-encode on a
 * scheduler thread, a decode on a transport dispatch thread — never coordinate with the manager, so
 * the grace period is what guarantees no {@code destroy()} races an active borrow (the S3 borrow
 * contract). Material that was never installed into a window (merge duplicates, responses arriving
 * after close) has no borrowers and is destroyed inline.
 *
 * <p>Threading: {@code attach*}/{@code detach}/{@code shutdown} are called under the engine lock
 * and only mutate maps and schedule tasks — the fetch itself always runs on the scheduler, never
 * synchronously under the engine lock. All mutable window state is guarded by the {@link
 * GroupKeyState} monitor; provider, state-machine, and diagnostics calls are NEVER made while
 * holding it (the {@code DataSetReaderRuntime.checkTimeout} one-way lock-ordering discipline).
 * Provider futures complete on arbitrary threads and are hopped to the scheduler via {@code
 * whenCompleteAsync}. Hot paths ({@link #currentPublisherKeys}, {@link #subscriberKeyWindow}) are
 * volatile snapshot reads.
 */
final class SecurityKeyManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityKeyManager.class);

  /** RequestedKeyCount (future keys beyond the current one) when maxFutureKeyCount is 0 (D2). */
  private static final int DEFAULT_REQUESTED_KEY_COUNT = 2;

  /**
   * Floor between unknown-token/force-reset triggered refreshes: attacker-crafted token ids must
   * not turn the provider into a DoS amplifier.
   */
  private static final long UNKNOWN_TOKEN_REFRESH_FLOOR_NANOS = TimeUnit.SECONDS.toNanos(1);

  /** Cap on the fetch retry delay; retries must be faster than the key lifetime (§5.4.5.3). */
  private static final long MAX_RETRY_DELAY_NANOS = TimeUnit.SECONDS.toNanos(10);

  /** The largest NonceSequenceNumber; the sequence never wraps within one token (K5). */
  private static final long NONCE_SEQUENCE_MAX = 0xFFFF_FFFFL;

  /**
   * Grace between key material leaving the window (rotation eviction, window discard/replace,
   * expiry, last detach, shutdown) and its zeroization: lock-free borrowers — a publish cycle
   * mid-encode, a decode on a transport thread — do not coordinate with the manager, so retired
   * material is destroyed only after any borrow that could still hold it has drained.
   */
  static final long RETIRED_KEY_DESTROY_DELAY_NANOS = TimeUnit.SECONDS.toNanos(30);

  private final ConcurrentMap<SecurityGroupRef, GroupKeyState> states = new ConcurrentHashMap<>();

  private final DiagnosticsCollector diagnostics;
  private final PubSubStateMachine stateMachine;
  private final Map<SecurityGroupRef, SecurityKeyProvider> providers;
  private final ScheduledExecutorService scheduler;
  private final LongSupplier nanoTime;
  private final Supplier<byte[]> nonceRandom;

  private volatile boolean shutdown = false;

  SecurityKeyManager(
      DiagnosticsCollector diagnostics,
      PubSubStateMachine stateMachine,
      Map<SecurityGroupRef, SecurityKeyProvider> providers,
      ScheduledExecutorService scheduler) {

    this(
        diagnostics,
        stateMachine,
        providers,
        scheduler,
        System::nanoTime,
        () -> NonceUtil.generateNonce(4).bytesOrEmpty());
  }

  /** Test seam: injectable clock and 4-byte nonce random supplier. */
  SecurityKeyManager(
      DiagnosticsCollector diagnostics,
      PubSubStateMachine stateMachine,
      Map<SecurityGroupRef, SecurityKeyProvider> providers,
      ScheduledExecutorService scheduler,
      LongSupplier nanoTime,
      Supplier<byte[]> nonceRandom) {

    this.diagnostics = diagnostics;
    this.stateMachine = stateMachine;
    this.providers = Map.copyOf(providers);
    this.scheduler = scheduler;
    this.nanoTime = nanoTime;
    this.nonceRandom = nonceRandom;
  }

  // region engine-lock callers (activate/deactivate paths)

  /**
   * Attach a publisher-role group to the key state of {@code securityGroup}, starting the fetch
   * loop if this is the first attachment. Called under the engine lock; never blocks — the first
   * fetch is initiated asynchronously and the attached group's startup completes from its callback.
   *
   * @throws UaException if no {@link SecurityKeyProvider} is bound for the SecurityGroup.
   */
  void attachPublisher(
      AbstractComponentRuntime group,
      MessageSecurityConfig security,
      SecurityGroupConfig securityGroup)
      throws UaException {

    attach(group, true, security, securityGroup);
  }

  /** Subscriber-role counterpart of {@link #attachPublisher}; same contract. */
  void attachSubscriber(
      AbstractComponentRuntime group,
      MessageSecurityConfig security,
      SecurityGroupConfig securityGroup)
      throws UaException {

    attach(group, false, security, securityGroup);
  }

  private void attach(
      AbstractComponentRuntime group,
      boolean publisher,
      MessageSecurityConfig security,
      SecurityGroupConfig securityGroup)
      throws UaException {

    if (shutdown) {
      return;
    }

    SecurityGroupRef ref = securityGroup.ref();

    SecurityKeyProvider provider = providers.get(ref);
    if (provider == null) {
      // the startup/reconfigure/activation gates reject this before attach; defensive backstop
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "no SecurityKeyProvider bound for SecurityGroup '%s'".formatted(ref.name()));
    }

    String configuredPolicyUri =
        security.getSecurityPolicyUri() != null
            ? security.getSecurityPolicyUri()
            : securityGroup.getSecurityPolicyUri();

    GroupKeyState state = states.get(ref);

    if (state != null && !state.securityGroup.equals(securityGroup)) {
      // reconfiguration changed the SecurityGroup while other referencing groups still hold the
      // state: rebuild it from the new config. The ConfigDiff-induced restart bounces every
      // referencing group, so surviving attachments migrate briefly and re-attach right after.
      Map<AbstractComponentRuntime, Attachment> survivors = Map.copyOf(state.attachments);
      synchronized (state) {
        closeState(state);
      }
      states.remove(ref, state);

      state = new GroupKeyState(ref, securityGroup, provider, nanoTime.getAsLong());
      state.attachments.putAll(survivors);
      states.put(ref, state);
      scheduleStartupDeadline(state);
    }

    if (state == null) {
      state = new GroupKeyState(ref, securityGroup, provider, nanoTime.getAsLong());
      states.put(ref, state);
      scheduleStartupDeadline(state);
    }

    state.attachments.put(group, new Attachment(publisher, configuredPolicyUri));

    GroupKeyState attachedState = state;
    scheduler.execute(() -> onAttached(attachedState));
  }

  /**
   * Detach a group from the key state of {@code ref}; the last detach cancels every scheduled task
   * and retires the key material. Called under the engine lock (deactivate/dispose paths).
   */
  void detach(AbstractComponentRuntime group, SecurityGroupRef ref) {
    GroupKeyState state = states.get(ref);
    if (state == null) {
      return;
    }

    boolean last;
    synchronized (state) {
      state.attachments.remove(group);
      last = state.attachments.isEmpty();
      if (last) {
        closeState(state);
      }
    }
    if (last) {
      states.remove(ref, state);
    }
  }

  /** Cancel every task and retire all key material. Called under the engine lock at shutdown. */
  void shutdown() {
    shutdown = true;
    for (GroupKeyState state : states.values()) {
      synchronized (state) {
        closeState(state);
      }
    }
    states.clear();
  }

  // endregion

  // region hot paths (volatile snapshot reads)

  /**
   * The publisher key snapshot for {@code ref}, or {@code null} when no usable key material is
   * available (not yet fetched, expired, nonce-exhausted): the caller skips its publish cycle.
   */
  @Nullable PublisherKeys currentPublisherKeys(SecurityGroupRef ref) {
    GroupKeyState state = states.get(ref);
    return state != null ? state.publisherSnapshot : null;
  }

  /**
   * The subscriber token window snapshot for {@code ref}, or {@code null} when no key material is
   * available: every secured message for the ref then drops as a stale-key/no-keys refusal.
   */
  @Nullable SubscriberKeyWindow subscriberKeyWindow(SecurityGroupRef ref) {
    GroupKeyState state = states.get(ref);
    return state != null ? state.subscriberSnapshot : null;
  }

  // endregion

  // region subscriber events (transport executor, via the resolver)

  /**
   * A received SecurityTokenId was outside the token window: trigger at most one refresh
   * (single-flight, floored at {@value #UNKNOWN_TOKEN_REFRESH_FLOOR_NANOS} ns between triggers).
   * Messages keep dropping — counted by the dispatcher — until the refresh lands; nothing is
   * buffered, and past tokens are never re-fetched.
   */
  void onUnknownToken(SecurityGroupRef ref, long tokenId) {
    triggerRefresh(ref, null);
  }

  /**
   * SecurityFlags bit 3 (force key reset) was observed: the publisher is about to invalidate all
   * keys — proactively refetch through the same single-flight, floored path.
   */
  void onForceKeyReset(SecurityGroupRef ref) {
    triggerRefresh(
        ref,
        "force key reset flagged by publisher for SecurityGroup '%s': refetching keys"
            .formatted(ref.name()));
  }

  private void triggerRefresh(SecurityGroupRef ref, @Nullable String warnOnTrigger) {
    GroupKeyState state = states.get(ref);
    if (state == null || shutdown) {
      return;
    }

    // unsynchronized pre-check: attack traffic must not queue one scheduler task per dropped
    // message; the authoritative floor/single-flight check repeats under the monitor below
    long now = nanoTime.getAsLong();
    if (state.fetchInFlight
        || state.staticKeys
        || now - state.lastTriggeredRefreshNanos < UNKNOWN_TOKEN_REFRESH_FLOOR_NANOS) {
      return;
    }

    scheduler.execute(
        () -> {
          synchronized (state) {
            long n = nanoTime.getAsLong();
            if (state.closed
                || shutdown
                || state.fetchInFlight
                || state.staticKeys
                || n - state.lastTriggeredRefreshNanos < UNKNOWN_TOKEN_REFRESH_FLOOR_NANOS) {
              return;
            }
            state.lastTriggeredRefreshNanos = n;
          }
          if (warnOnTrigger != null) {
            LOGGER.warn(warnOnTrigger);
          }
          initiateFetch(state);
        });
  }

  // endregion

  // region Phase 5 LiveValue feed

  /**
   * A point-in-time view of the key state of {@code ref} — current token id and time to the next
   * key switch — or {@code null} if the ref has no key state. Feeds the Part 14 §9.1.11 {@code
   * SecurityTokenID}/{@code TimeToNextTokenID} LiveValues in Phase 5.
   *
   * @param securityGroupId the id of the SecurityGroup as known to the Security Key Service.
   * @param securityPolicyUri the URI of the policy the current keys were generated for, or {@code
   *     null} before the first successful fetch.
   * @param currentTokenId the id of the current security token; 0 before the first successful fetch
   *     (real token ids start at 1).
   * @param timeToNextKey the time until the current key is replaced, or {@code null} when unknown
   *     (no keys yet) or when the key set is static.
   */
  record SecurityGroupKeyView(
      String securityGroupId,
      @Nullable String securityPolicyUri,
      long currentTokenId,
      @Nullable Duration timeToNextKey) {}

  @Nullable SecurityGroupKeyView view(SecurityGroupRef ref) {
    GroupKeyState state = states.get(ref);
    if (state == null) {
      return null;
    }
    synchronized (state) {
      String policyUri = state.policy != null ? state.policy.getUri() : null;
      Duration timeToNextKey = null;
      if (state.currentKey != null && !state.staticKeys) {
        timeToNextKey =
            Duration.ofNanos(Math.max(0L, state.switchDeadlineNanos - nanoTime.getAsLong()));
      }
      return new SecurityGroupKeyView(
          state.securityGroupId, policyUri, state.currentTokenId, timeToNextKey);
    }
  }

  // endregion

  // region snapshots

  /**
   * The lock-free publisher-side view of the current token: the split key material and the shared
   * per-token nonce state. One instance per token — rebuilt (counter reset to 1) exactly on token
   * switch, preserved across refreshes that do not switch tokens.
   *
   * @param tokenId the id of the current security token.
   * @param keys the split key material of the current token, borrowed from the manager (valid while
   *     the token is within the {prev, current, futures} retention window, and through the {@link
   *     #RETIRED_KEY_DESTROY_DELAY_NANOS} grace after it leaves).
   * @param nonceCounter the NonceSequenceNumber source, SHARED by every writer group publishing on
   *     the same SecurityGroupRef: (key, nonce) uniqueness must hold across all of them.
   * @param nonceSupplier the {@link MessageNonceSupplier} over {@code nonceCounter}; hard-stops
   *     (throws, and drops the publisher snapshot) instead of wrapping the sequence.
   */
  record PublisherKeys(
      long tokenId,
      SecurityKeyMaterial keys,
      AtomicLong nonceCounter,
      MessageNonceSupplier nonceSupplier) {}

  /**
   * The lock-free subscriber-side token window: {prev, current, futures} with token-id lookup
   * (token = firstTokenId + index, Part 14 §8.3.2). Immutable; rebuilt on every window change.
   */
  record SubscriberKeyWindow(
      long previousTokenId,
      @Nullable SecurityKeyMaterial previousKey,
      long currentTokenId,
      SecurityKeyMaterial currentKey,
      List<SecurityKeyMaterial> futureKeys) {

    SubscriberKeyWindow {
      futureKeys = List.copyOf(futureKeys);
    }

    /** The key material for {@code tokenId}, or {@code null} if outside the window. */
    @Nullable SecurityKeyMaterial keyFor(long tokenId) {
      if (tokenId == currentTokenId) {
        return currentKey;
      }
      if (previousKey != null && tokenId == previousTokenId) {
        return previousKey;
      }
      long index = tokenId - currentTokenId - 1;
      if (index >= 0 && index < futureKeys.size()) {
        return futureKeys.get((int) index);
      }
      return null;
    }
  }

  // endregion

  // region state machine + fetch loop (scheduler threads)

  private void onAttached(GroupKeyState state) {
    List<AbstractComponentRuntime> toComplete = List.of();
    synchronized (state) {
      if (state.closed || shutdown) {
        return;
      }
      if (state.currentKey != null) {
        // keys already present (another group attached first): complete startup immediately
        toComplete = List.copyOf(state.attachments.keySet());
      }
    }

    if (toComplete.isEmpty()) {
      initiateFetch(state);
    } else {
      for (AbstractComponentRuntime group : toComplete) {
        if (group.state() == PubSubState.PreOperational) {
          stateMachine.startupCompleted(group);
        }
      }
    }
  }

  private void initiateFetch(GroupKeyState state) {
    synchronized (state) {
      if (state.closed || shutdown || state.fetchInFlight || state.staticKeys) {
        return;
      }
      state.fetchInFlight = true;
    }

    CompletableFuture<SecurityKeySet> future;
    try {
      // StartingTokenId is always 0 (the current token): publishers SHALL, subscribers should,
      // and past tokens are never re-fetched (K6)
      future = state.provider.getKeys(state.securityGroupId, uint(0), state.requestedKeyCount);
    } catch (RuntimeException e) {
      future = CompletableFuture.failedFuture(e);
    }

    // the provider may complete on any thread: hop to the scheduler before touching state
    future.whenCompleteAsync((keySet, ex) -> onFetchComplete(state, keySet, ex), scheduler);
  }

  private void onFetchComplete(
      GroupKeyState state, @Nullable SecurityKeySet keySet, @Nullable Throwable ex) {

    if (ex != null || keySet == null) {
      StatusCode status =
          ex != null
              ? UaException.extractStatusCode(ex)
                  .orElse(new StatusCode(StatusCodes.Bad_InternalError))
              : new StatusCode(StatusCodes.Bad_InternalError);
      onFetchFailed(
          state,
          status,
          "failed to fetch security keys for SecurityGroup '%s': %s"
              .formatted(state.ref.name(), ex != null ? ex.getMessage() : "no key set"),
          ex);
      return;
    }

    // K8: the provider-returned policy URI is authoritative — it must be a supported policy and
    // match every attacher's non-null configured URI; a mismatch fails the fetch (never downgrade)
    PubSubSecurityPolicy policy =
        PubSubSecurityPolicy.fromUri(keySet.securityPolicyUri()).orElse(null);
    if (policy == null) {
      onFetchFailed(
          state,
          new StatusCode(StatusCodes.Bad_ConfigurationError),
          "SecurityGroup '%s': provider returned unsupported security policy '%s'"
              .formatted(state.ref.name(), keySet.securityPolicyUri()),
          null);
      return;
    }
    for (Attachment attachment : state.attachments.values()) {
      String configuredUri = attachment.configuredPolicyUri();
      if (configuredUri != null && !configuredUri.equals(keySet.securityPolicyUri())) {
        onFetchFailed(
            state,
            new StatusCode(StatusCodes.Bad_ConfigurationError),
            "SecurityGroup '%s': provider returned security policy '%s' but '%s' is configured"
                .formatted(state.ref.name(), keySet.securityPolicyUri(), configuredUri),
            null);
        return;
      }
    }

    // static sentinel: both durations zero = static key set; exactly one zero = malformed
    boolean staticKeys = keySet.timeToNextKey().isZero() && keySet.keyLifetime().isZero();
    if (!staticKeys && (keySet.timeToNextKey().isZero() || keySet.keyLifetime().isZero())) {
      onFetchFailed(
          state,
          new StatusCode(StatusCodes.Bad_ConfigurationError),
          "SecurityGroup '%s': malformed key set durations (timeToNextKey=%s, keyLifetime=%s)"
              .formatted(state.ref.name(), keySet.timeToNextKey(), keySet.keyLifetime()),
          null);
      return;
    }

    // split once per token; strict 52/68-byte validation
    var materials = new ArrayList<SecurityKeyMaterial>(keySet.keys().size());
    try {
      for (ByteString keyData : keySet.keys()) {
        materials.add(SecurityKeyMaterial.split(policy, keyData));
      }
    } catch (UaException e) {
      materials.forEach(SecurityKeyMaterial::destroy);
      onFetchFailed(
          state,
          e.getStatusCode(),
          "SecurityGroup '%s': invalid key data: %s".formatted(state.ref.name(), e.getMessage()),
          e);
      return;
    }

    install(state, policy, keySet.firstTokenId().longValue(), materials, staticKeys, keySet);
  }

  private void install(
      GroupKeyState state,
      PubSubSecurityPolicy policy,
      long firstTokenId,
      List<SecurityKeyMaterial> materials,
      boolean staticKeys,
      SecurityKeySet keySet) {

    List<AbstractComponentRuntime> attached;
    boolean recovered;

    synchronized (state) {
      state.fetchInFlight = false;
      if (state.closed || shutdown) {
        materials.forEach(SecurityKeyMaterial::destroy);
        return;
      }

      state.everFetched = true;
      state.consecutiveFetchFailures = 0;
      state.lastFetchFailureStatus = null;
      state.policy = policy;
      cancelTask(state.startupDeadlineTask);
      state.startupDeadlineTask = null;

      long now = nanoTime.getAsLong();

      if (staticKeys) {
        state.staticKeys = true;
        retireWindow(state);
        installWindow(state, firstTokenId, materials);
        // no refresh, no switch, no expiry: static keys never rotate (C9)
        cancelTask(state.refreshTask);
        state.refreshTask = null;
        cancelTask(state.switchTask);
        state.switchTask = null;
        cancelTask(state.expiryTask);
        state.expiryTask = null;
      } else {
        mergeWindow(state, firstTokenId, materials);
        state.keyLifetimeNanos = keySet.keyLifetime().toNanos();
        state.switchDeadlineNanos = now + keySet.timeToNextKey().toNanos();
        rescheduleRefresh(state, state.keyLifetimeNanos / 2);
        rescheduleSwitch(state, now);
        rescheduleExpiry(state, now);
      }

      rebuildSnapshots(state);

      recovered = state.failedByManager;
      state.failedByManager = false;
      attached = List.copyOf(state.attachments.keySet());
    }

    // state-machine calls after releasing the monitor (one-way lock ordering); the state
    // pre-checks only avoid engine-lock churn on routine refreshes — both transitions re-check
    // authoritatively under the engine lock
    for (AbstractComponentRuntime group : attached) {
      if (group.state() == PubSubState.PreOperational) {
        stateMachine.startupCompleted(group);
      }
      if (recovered && group.state() == PubSubState.Error) {
        stateMachine.recover(group);
      }
    }
  }

  private void onFetchFailed(
      GroupKeyState state, StatusCode status, String message, @Nullable Throwable ex) {

    List<AbstractComponentRuntime> attached;
    synchronized (state) {
      state.fetchInFlight = false;
      if (state.closed || shutdown) {
        return;
      }
      state.consecutiveFetchFailures++;
      state.lastFetchFailureStatus = status;
      attached = List.copyOf(state.attachments.keySet());

      // retry faster than the key lifetime (§5.4.5.3); the learned lifetime once fetched, the
      // configured one before
      long lifetimeNanos =
          state.keyLifetimeNanos > 0 ? state.keyLifetimeNanos : state.configuredKeyLifetimeNanos;
      long retryDelay = Math.min(Math.max(1L, lifetimeNanos / 2), MAX_RETRY_DELAY_NANOS);
      rescheduleRefresh(state, retryDelay);
    }

    for (AbstractComponentRuntime group : attached) {
      diagnostics.error(group.path(), status, message, ex);
    }
  }

  /** Startup deadline: a state that never fetched within 2×(configured)KeyLifetime fails (§7). */
  private void onStartupDeadline(GroupKeyState state) {
    StatusCode status;
    List<AbstractComponentRuntime> attached;
    synchronized (state) {
      if (state.closed || shutdown || state.everFetched) {
        return;
      }
      state.failedByManager = true;
      status =
          state.lastFetchFailureStatus != null
              ? state.lastFetchFailureStatus
              : new StatusCode(StatusCodes.Bad_InternalError);
      attached = List.copyOf(state.attachments.keySet());
    }

    String message =
        "no security keys obtained for SecurityGroup '%s' within 2×KeyLifetime"
            .formatted(state.ref.name());
    for (AbstractComponentRuntime group : attached) {
      diagnostics.error(group.path(), status, message, null);
      stateMachine.fail(group, status);
    }
  }

  private void onSwitchDeadline(GroupKeyState state) {
    synchronized (state) {
      if (state.closed || shutdown || state.staticKeys || state.currentKey == null) {
        return;
      }
      long now = nanoTime.getAsLong();
      if (now - state.switchDeadlineNanos < 0) {
        // the deadline was re-learned from a newer response; realign
        rescheduleSwitch(state, now);
        return;
      }
      boolean rotated = false;
      while (now - state.switchDeadlineNanos >= 0 && !state.futureKeys.isEmpty()) {
        rotateOnce(state);
        state.switchDeadlineNanos += state.keyLifetimeNanos;
        rotated = true;
      }
      if (rotated) {
        rebuildSnapshots(state);
        rescheduleSwitch(state, now);
        rescheduleExpiry(state, now);
      }
      // futures empty: nothing to switch to. The current key is outdated once its deadline
      // passes, but sending continues until the 2×KeyLifetime expiry task fires (§6.2.12.2);
      // a fetch that lands meanwhile advances the window and reschedules.
    }
  }

  private void onExpiryDeadline(GroupKeyState state) {
    List<AbstractComponentRuntime> attached;
    synchronized (state) {
      if (state.closed || shutdown || state.staticKeys || state.currentKey == null) {
        return;
      }
      long now = nanoTime.getAsLong();
      long deadline = expiryDeadlineNanos(state);
      if (now - deadline < 0) {
        // the window grew since this task was scheduled; realign
        cancelTask(state.expiryTask);
        state.expiryTask =
            scheduler.schedule(() -> onExpiryDeadline(state), deadline - now, TimeUnit.NANOSECONDS);
        return;
      }

      // NULL the publisher snapshot FIRST: an in-flight publish cycle that already passed its
      // state check resolves null and skips; then retire the window and fail the groups
      state.publisherSnapshot = null;
      state.subscriberSnapshot = null;
      retireWindow(state);
      state.failedByManager = true;
      attached = List.copyOf(state.attachments.keySet());
      // the refresh loop keeps running: a later successful fetch reinstalls and recovers
    }

    var status = new StatusCode(StatusCodes.Bad_SecurityChecksFailed);
    String message =
        "security keys for SecurityGroup '%s' expired without replacement (2×KeyLifetime)"
            .formatted(state.ref.name());
    for (AbstractComponentRuntime group : attached) {
      diagnostics.error(group.path(), status, message, null);
      stateMachine.fail(group, status);
    }
  }

  // endregion

  // region window maintenance (GroupKeyState monitor held)

  /** Install a fresh window: {@code materials[0]} is the current key at {@code firstTokenId}. */
  private void installWindow(
      GroupKeyState state, long firstTokenId, List<SecurityKeyMaterial> materials) {

    state.previousTokenId = 0;
    state.previousKey = null;
    state.currentTokenId = firstTokenId;
    state.currentKey = materials.get(0);
    state.futureKeys.clear();
    for (int i = 1; i < materials.size(); i++) {
      state.futureKeys.addLast(materials.get(i));
    }
  }

  /**
   * Merge a fetch response into the window (K6/§8.3.2): overlap on {@code FirstTokenId} keeps the
   * existing entries and appends the new tail; a {@code FirstTokenId} that does not connect to the
   * window discards and replaces it. Because {@code StartingTokenId} is always 0, the response's
   * first token IS the SKS's current token, so the window is advanced to it afterwards (retaining
   * one previous key for the §8.3.2 overlap tolerance).
   */
  private void mergeWindow(
      GroupKeyState state, long firstTokenId, List<SecurityKeyMaterial> materials) {

    if (state.currentKey == null) {
      installWindow(state, firstTokenId, materials);
      return;
    }

    // "connects" spans the retained previous token too: a response fetched just before a local
    // switch may legally report the pre-switch token as current, and must not discard the window
    long windowStart = state.previousKey != null ? state.previousTokenId : state.currentTokenId;
    long windowEnd = state.currentTokenId + state.futureKeys.size();
    boolean connects = firstTokenId >= windowStart && firstTokenId <= windowEnd + 1;

    if (!connects) {
      retireWindow(state);
      installWindow(state, firstTokenId, materials);
      return;
    }

    for (int i = 0; i < materials.size(); i++) {
      long tokenId = firstTokenId + i;
      if (tokenId <= windowEnd) {
        // duplicate of an entry already held: dedup keeps the existing entry
        materials.get(i).destroy();
      } else {
        state.futureKeys.addLast(materials.get(i));
      }
    }

    while (state.currentTokenId < firstTokenId && !state.futureKeys.isEmpty()) {
      rotateOnce(state);
    }
  }

  /**
   * Rotate current→previous and promote the next future key; the old previous leaves the window and
   * is retired.
   */
  private void rotateOnce(GroupKeyState state) {
    SecurityKeyMaterial evicted = state.previousKey;
    if (evicted != null) {
      // an in-flight decode may still hold this material (a late message under the evicted
      // token resolved it moments ago): never destroy inline, retire instead
      retire(evicted);
    }
    state.previousKey = state.currentKey;
    state.previousTokenId = state.currentTokenId;
    state.currentKey = state.futureKeys.poll();
    state.currentTokenId++;
  }

  /** Retire every window entry and empty the window. */
  private void retireWindow(GroupKeyState state) {
    if (state.previousKey != null) {
      retire(state.previousKey);
      state.previousKey = null;
    }
    if (state.currentKey != null) {
      retire(state.currentKey);
      state.currentKey = null;
    }
    state.futureKeys.forEach(this::retire);
    state.futureKeys.clear();
    state.previousTokenId = 0;
    state.currentTokenId = 0;
  }

  /**
   * Retire key material that was installed in a window: drop it from the manager now, zeroize it
   * only after {@link #RETIRED_KEY_DESTROY_DELAY_NANOS}, when no borrow taken from a snapshot
   * before the retirement can still be in flight. Falls back to an inline destroy if the scheduler
   * is already shut down — no borrower can be scheduled then either.
   */
  private void retire(SecurityKeyMaterial material) {
    try {
      scheduler.schedule(material::destroy, RETIRED_KEY_DESTROY_DELAY_NANOS, TimeUnit.NANOSECONDS);
    } catch (RejectedExecutionException e) {
      material.destroy();
    }
  }

  /**
   * Rebuild the volatile snapshots from the window. The publisher snapshot is preserved when the
   * current token did not change, so the nonce counter is NEVER reset by a mere refresh — only a
   * token switch resets it to 1 (K5: never reuse a (key, nonce) pair).
   */
  private void rebuildSnapshots(GroupKeyState state) {
    SecurityKeyMaterial currentKey = state.currentKey;
    if (currentKey == null) {
      state.publisherSnapshot = null;
      state.subscriberSnapshot = null;
      return;
    }

    PublisherKeys existing = state.publisherSnapshot;
    if (state.currentTokenId == state.nonceExhaustedTokenId) {
      // the sequence for this token is spent; publishing stays stopped until the next switch
      state.publisherSnapshot = null;
    } else if (existing == null
        || existing.tokenId() != state.currentTokenId
        || existing.keys() != currentKey) {
      var nonceCounter = new AtomicLong(1);
      state.publisherSnapshot =
          new PublisherKeys(
              state.currentTokenId,
              currentKey,
              nonceCounter,
              newNonceSupplier(state, state.currentTokenId, nonceCounter));
    }

    state.subscriberSnapshot =
        new SubscriberKeyWindow(
            state.previousTokenId,
            state.previousKey,
            state.currentTokenId,
            currentKey,
            List.copyOf(state.futureKeys));
  }

  /**
   * The 8-byte MessageNonce supplier for one token: {@code Random[4] || NonceSequenceNumber}
   * (UInt32, little-endian), sequence starting at 1. Thread-safe — one SecurityGroupRef can back
   * several concurrently publishing writer groups. On sequence exhaustion the publisher snapshot is
   * dropped (stop) and the attached publisher groups are failed — the sequence never wraps.
   */
  private MessageNonceSupplier newNonceSupplier(
      GroupKeyState state, long tokenId, AtomicLong nonceCounter) {

    return () -> {
      long sequence = nonceCounter.getAndIncrement();
      if (sequence > NONCE_SEQUENCE_MAX) {
        onNonceExhausted(state, tokenId);
        throw new UaException(
            StatusCodes.Bad_InternalError,
            "MessageNonce sequence exhausted for SecurityGroup '%s' token %d"
                .formatted(state.ref.name(), tokenId));
      }
      byte[] random = nonceRandom.get();
      if (random.length != 4) {
        throw new UaException(
            StatusCodes.Bad_InternalError,
            "nonce random supplier must produce 4 bytes, got " + random.length);
      }
      byte[] nonce = new byte[8];
      System.arraycopy(random, 0, nonce, 0, 4);
      nonce[4] = (byte) sequence;
      nonce[5] = (byte) (sequence >>> 8);
      nonce[6] = (byte) (sequence >>> 16);
      nonce[7] = (byte) (sequence >>> 24);
      return nonce;
    };
  }

  /** Runs on a publish thread: stop first (drop the snapshot), then fail via the scheduler. */
  private void onNonceExhausted(GroupKeyState state, long tokenId) {
    List<AbstractComponentRuntime> publishers = new ArrayList<>();
    synchronized (state) {
      if (state.closed || state.nonceExhaustedTokenId == tokenId) {
        return;
      }
      state.nonceExhaustedTokenId = tokenId;
      if (state.currentTokenId == tokenId) {
        state.publisherSnapshot = null;
      }
      state.failedByManager = true;
      state.attachments.forEach(
          (group, attachment) -> {
            if (attachment.publisher()) {
              publishers.add(group);
            }
          });
    }

    var status = new StatusCode(StatusCodes.Bad_InternalError);
    String message =
        "MessageNonce sequence exhausted for SecurityGroup '%s'; publishing stopped until the next"
                .formatted(state.ref.name())
            + " key switch";
    scheduler.execute(
        () -> {
          for (AbstractComponentRuntime group : publishers) {
            diagnostics.error(group.path(), status, message, null);
            stateMachine.fail(group, status);
          }
        });
  }

  // endregion

  // region scheduling (GroupKeyState monitor held)

  private void scheduleStartupDeadline(GroupKeyState state) {
    state.startupDeadlineTask =
        scheduler.schedule(
            () -> onStartupDeadline(state),
            2 * state.configuredKeyLifetimeNanos,
            TimeUnit.NANOSECONDS);
  }

  private void rescheduleRefresh(GroupKeyState state, long delayNanos) {
    cancelTask(state.refreshTask);
    state.refreshTask =
        scheduler.schedule(() -> initiateFetch(state), delayNanos, TimeUnit.NANOSECONDS);
  }

  private void rescheduleSwitch(GroupKeyState state, long nowNanos) {
    cancelTask(state.switchTask);
    state.switchTask =
        scheduler.schedule(
            () -> onSwitchDeadline(state),
            Math.max(0L, state.switchDeadlineNanos - nowNanos),
            TimeUnit.NANOSECONDS);
  }

  private void rescheduleExpiry(GroupKeyState state, long nowNanos) {
    cancelTask(state.expiryTask);
    state.expiryTask =
        scheduler.schedule(
            () -> onExpiryDeadline(state),
            Math.max(0L, expiryDeadlineNanos(state) - nowNanos),
            TimeUnit.NANOSECONDS);
  }

  /**
   * When the group must fail if no new key arrived: the last available key expires at the switch
   * deadline plus one lifetime per remaining future key, plus the 2×KeyLifetime grace (§6.2.12.2).
   */
  private static long expiryDeadlineNanos(GroupKeyState state) {
    return state.switchDeadlineNanos
        + state.futureKeys.size() * state.keyLifetimeNanos
        + 2 * state.keyLifetimeNanos;
  }

  /** Cancel tasks, drop snapshots, retire material. Monitor held; idempotent. */
  private void closeState(GroupKeyState state) {
    state.closed = true;
    cancelTask(state.refreshTask);
    state.refreshTask = null;
    cancelTask(state.switchTask);
    state.switchTask = null;
    cancelTask(state.expiryTask);
    state.expiryTask = null;
    cancelTask(state.startupDeadlineTask);
    state.startupDeadlineTask = null;
    state.publisherSnapshot = null;
    state.subscriberSnapshot = null;
    retireWindow(state);
  }

  private static void cancelTask(@Nullable ScheduledFuture<?> task) {
    if (task != null) {
      task.cancel(false);
    }
  }

  // endregion

  /** How a group is attached to a key state: its role and its pinned policy URI, if any. */
  private record Attachment(boolean publisher, @Nullable String configuredPolicyUri) {}

  /** The key lifecycle state of one SecurityGroupRef. */
  private final class GroupKeyState {

    final SecurityGroupRef ref;
    final SecurityGroupConfig securityGroup;
    final SecurityKeyProvider provider;
    final String securityGroupId;
    final long configuredKeyLifetimeNanos;
    final UInteger requestedKeyCount;

    /**
     * The attached groups; mutated only under the engine lock (attach/detach), read from scheduler
     * threads for state-machine cascades.
     */
    final ConcurrentMap<AbstractComponentRuntime, Attachment> attachments =
        new ConcurrentHashMap<>();

    // mutable window/fetch state, guarded by the monitor on this; fetchInFlight and
    // lastTriggeredRefreshNanos are additionally volatile for the dispatch-thread pre-check
    volatile boolean fetchInFlight = false;
    volatile long lastTriggeredRefreshNanos;
    boolean everFetched = false;
    boolean staticKeys = false;
    boolean failedByManager = false;
    boolean closed = false;
    int consecutiveFetchFailures = 0;
    @Nullable StatusCode lastFetchFailureStatus;

    long keyLifetimeNanos = 0;
    long switchDeadlineNanos = 0;
    long nonceExhaustedTokenId = -1;

    @Nullable PubSubSecurityPolicy policy;
    long previousTokenId = 0;
    @Nullable SecurityKeyMaterial previousKey;
    long currentTokenId = 0;
    @Nullable SecurityKeyMaterial currentKey;
    final ArrayDeque<SecurityKeyMaterial> futureKeys = new ArrayDeque<>();

    @Nullable ScheduledFuture<?> refreshTask;
    @Nullable ScheduledFuture<?> switchTask;
    @Nullable ScheduledFuture<?> expiryTask;
    @Nullable ScheduledFuture<?> startupDeadlineTask;

    volatile @Nullable PublisherKeys publisherSnapshot;
    volatile @Nullable SubscriberKeyWindow subscriberSnapshot;

    GroupKeyState(
        SecurityGroupRef ref,
        SecurityGroupConfig securityGroup,
        SecurityKeyProvider provider,
        long nowNanos) {

      this.ref = ref;
      this.securityGroup = securityGroup;
      this.provider = provider;
      this.securityGroupId = securityGroup.getSecurityGroupId();
      this.configuredKeyLifetimeNanos = Math.max(1L, securityGroup.getKeyLifeTime().toNanos());
      // RequestedKeyCount = future keys beyond the current one; pinned default 2 when the
      // config's maxFutureKeyCount is 0 (D2)
      long maxFutureKeyCount = securityGroup.getMaxFutureKeyCount().longValue();
      this.requestedKeyCount =
          uint(maxFutureKeyCount > 0 ? maxFutureKeyCount : DEFAULT_REQUESTED_KEY_COUNT);
      this.lastTriggeredRefreshNanos = nowNanos - UNKNOWN_TOKEN_REFRESH_FLOOR_NANOS;
    }
  }
}
