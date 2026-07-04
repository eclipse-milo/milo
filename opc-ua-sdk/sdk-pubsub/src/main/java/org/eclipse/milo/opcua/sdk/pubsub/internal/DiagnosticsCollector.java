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

import java.time.Instant;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubCounter;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent.Cause;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * Per-component diagnostic counters, keyed by component path, and the {@link PubSubDiagnostics}
 * view over them.
 *
 * <p>Counters are lock-free ({@link LongAdder} plus a benign-race first-change timestamp); {@link
 * #snapshot()} produces an immutable point-in-time copy. Error recordings additionally emit a
 * {@link PubSubDiagnosticsEvent} through the {@link EventDispatcher}.
 *
 * <p>Entries are created by {@link #register(String)} only (startup and reconfigure register every
 * component path); increments for unregistered paths are dropped, so a late in-flight increment
 * cannot resurrect the entry of a component removed by reconfiguration.
 *
 * <p>Path-stable reconfigure restarts preserve counters: {@link #removePreserving(String)} parks
 * the live entry in a pending-restart map from which {@link #register(String)} revives it (with
 * counters, {@code lastError}, and TimeFirstChange intact); {@link #clearPendingRestart()} discards
 * leftovers whose re-add never materialized. While parked, the path is out of the live map, so
 * in-flight increments are dropped exactly as for a removal.
 */
final class DiagnosticsCollector implements PubSubDiagnostics {

  private final ConcurrentMap<String, Counters> countersByPath = new ConcurrentHashMap<>();

  /**
   * Entries parked by {@link #removePreserving(String)} awaiting revival by {@link
   * #register(String)}. All mutations happen under the engine lock (reconfigure/shutdown paths); a
   * concurrent map is used for symmetry with {@link #countersByPath}.
   */
  private final ConcurrentMap<String, Counters> pendingRestart = new ConcurrentHashMap<>();

  private final EventDispatcher events;

  DiagnosticsCollector(EventDispatcher events) {
    this.events = events;
  }

  /**
   * The status code of {@code e}: the extracted {@link UaException} status code, else {@code
   * Bad_InternalError}. The shared un-flattening rule for send/encode failure reporting.
   */
  static StatusCode statusCodeOf(Throwable e) {
    return UaException.extractStatusCode(e).orElse(new StatusCode(StatusCodes.Bad_InternalError));
  }

  /**
   * Ensure an entry exists for the component at {@code path}: revive a pending-restart entry
   * (preserving its counters), else create a zeroed one.
   */
  void register(String path) {
    Counters pending = pendingRestart.remove(path);
    if (pending != null) {
      countersByPath.putIfAbsent(path, pending);
    } else {
      countersByPath.computeIfAbsent(path, Counters::new);
    }
  }

  /** Remove the entry for a component removed by reconfiguration; its counters are discarded. */
  void remove(String path) {
    countersByPath.remove(path);
  }

  /**
   * Remove the entry for a component about to be restarted path-stably by reconfiguration, parking
   * it for revival by the re-add's {@link #register(String)}.
   */
  void removePreserving(String path) {
    Counters counters = countersByPath.remove(path);
    if (counters != null) {
      pendingRestart.put(path, counters);
    }
  }

  /**
   * Discard parked pending-restart entries: a CHANGED path whose re-add did not materialize is a
   * removal. Called at the end of every reconfigure apply and defensively at shutdown.
   */
  void clearPendingRestart() {
    pendingRestart.clear();
  }

  void networkMessageSent(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.networkMessagesSent.increment();
    }
  }

  void networkMessageReceived(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.networkMessagesReceived.increment();
    }
  }

  void dataSetMessagesSent(String path, int count) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.dataSetMessagesSent.add(count);
    }
  }

  void dataSetMessageReceived(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.dataSetMessagesReceived.increment();
    }
  }

  /**
   * Record NetworkMessages of a writer group that were not transmitted: async/sync send failures
   * and oversize skips, at the group path. Ticks the counter only — the caller emits the
   * accompanying {@link #error} event (or suppresses both for teardown noise).
   */
  void failedTransmissions(String path, int count) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.failedTransmissions.add(count);
    }
  }

  /**
   * Record DataSetMessages never sent due to an encode or send failure, at the contributing
   * writer's path. Ticks the counter only, like {@link #failedTransmissions}.
   */
  void failedDataSetMessages(String path, int count) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.failedDataSetMessages.add(count);
    }
  }

  /**
   * Record a state transition for the Part 14 Table 311 State* counters. Called under the engine
   * lock from the state-change listener; all ticks are lock-free.
   *
   * <ul>
   *   <li>{@code stateError}: every entry into Error;
   *   <li>{@code stateOperationalByMethod}/{@code ByParent}: Operational entries attributed by
   *       cause ({@code STARTUP} ticks neither on the initiating component);
   *   <li>{@code stateOperationalFromError}: Operational entries whose old state was Error;
   *   <li>{@code statePausedByParent}: every Paused entry (definitionally parent-caused);
   *   <li>{@code stateDisabledByMethod}: Disabled entries caused by an explicit disable — dispose
   *       ticks nothing.
   * </ul>
   */
  void stateTransition(String path, PubSubState oldState, PubSubState newState, Cause cause) {
    Counters counters = countersByPath.get(path);
    if (counters == null) {
      return;
    }

    if (newState == PubSubState.Error) {
      counters.stateError.increment();
    }
    if (newState == PubSubState.Operational) {
      if (cause == Cause.METHOD) {
        counters.stateOperationalByMethod.increment();
      } else if (cause == Cause.PARENT) {
        counters.stateOperationalByParent.increment();
      }
      if (oldState == PubSubState.Error) {
        counters.stateOperationalFromError.increment();
      }
    }
    if (newState == PubSubState.Paused) {
      counters.statePausedByParent.increment();
    }
    if (newState == PubSubState.Disabled && cause == Cause.METHOD) {
      counters.stateDisabledByMethod.increment();
    }
  }

  /**
   * Record a DataSetMessage dropped by a reader's Part 14 §7.2.3 sequence-number window as older
   * than or duplicating the last processed message. A normal-operation counter: no {@code
   * lastError} and no diagnostics event ("shall be ignored" is not an error condition).
   */
  void staleSequenceMessage(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.staleSequenceMessages.increment();
    }
  }

  /**
   * Record a DataSetMessage dropped by a reader's Part 14 §7.2.3 sequence-number window with a
   * recency result in the invalid band (neither provably newer nor older). A normal-operation
   * counter: no {@code lastError} and no diagnostics event.
   */
  void invalidSequenceMessage(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.invalidSequenceMessages.increment();
    }
  }

  /** Record a dropped message (decode failure, version mismatch, invalid message). */
  void decodeError(String path, StatusCode statusCode, String message, @Nullable Throwable error) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.decodeErrors.increment();
    }
    error(path, statusCode, message, error, PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
  }

  /**
   * Record a publish cycle of a secured WriterGroup skipped because no usable key material was
   * available. A quiet tick: the caller emits the accompanying {@link #error} event only on the
   * first skip after a successful cycle (edge-triggered) — a dead key source at a 10 ms publishing
   * interval must not emit 100 events per second.
   */
  void encryptionError(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.encryptionErrors.increment();
    }
  }

  /**
   * Record a received secured NetworkMessage whose payload failed to decrypt after its signature
   * verified. An error-class counter, mirroring {@link #decodeError}: tick plus {@code lastError}
   * plus a diagnostics event.
   */
  void decryptionError(
      String path, StatusCode statusCode, String message, @Nullable Throwable error) {

    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.decryptionErrors.increment();
    }
    error(path, statusCode, message, error, PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
  }

  /**
   * Record a received secured NetworkMessage whose signature did not verify. Sets {@code lastError}
   * but emits NO event (attack traffic must not become event-queue pressure; the {@link
   * #listenerError} lastError-without-event precedent); the dispatcher logs a rate-limited WARN
   * instead.
   */
  void invalidSignatureMessage(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.invalidSignatureMessages.increment();
      counters.lastError = new StatusCode(StatusCodes.Bad_SecurityChecksFailed);
    }
  }

  /**
   * Record a received secured NetworkMessage dropped because its SecurityTokenId is outside the
   * receiver's token window. A normal-operation counter (the single-flight key refresh is the
   * corrective action): no {@code lastError} and no diagnostics event.
   */
  void unknownTokenMessage(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.unknownTokenMessages.increment();
    }
  }

  /**
   * Record a received NetworkMessage dropped for want of usable keys (no or expired key window) or
   * by the receive-mode gate (received security mode below the configured mode, or a secured
   * message to a mode-None group). A normal-operation counter: no {@code lastError} and no
   * diagnostics event.
   */
  void staleKeyMessage(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.staleKeyMessages.increment();
    }
  }

  /** Record a {@code PublishedDataSetSource} read failure. */
  void sourceError(String path, StatusCode statusCode, String message, @Nullable Throwable error) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.sourceErrors.increment();
    }
    error(path, statusCode, message, error, PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
  }

  /**
   * Record an error and emit a diagnostics event for it, classified by {@code kind}. The event is
   * emitted even when {@code path} is no longer registered.
   */
  void error(
      String path,
      StatusCode statusCode,
      String message,
      @Nullable Throwable error,
      PubSubDiagnosticsEvent.Kind kind) {

    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.lastError = statusCode;
    }
    events.notifyDiagnostics(new PubSubDiagnosticsEvent(path, statusCode, message, error, kind));
  }

  /** Record a listener failure; no event is emitted (the failing listener may be the cause). */
  void listenerError(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.lastError = new StatusCode(StatusCodes.Bad_InternalError);
    }
  }

  @Override
  public Map<String, ComponentDiagnostics> snapshot() {
    var snapshot = new LinkedHashMap<String, ComponentDiagnostics>(countersByPath.size());
    countersByPath.forEach((path, counters) -> snapshot.put(path, counters.snapshot()));
    return Collections.unmodifiableMap(snapshot);
  }

  /**
   * Per-path snapshot without copying the whole map: attribute-level reads sample one component per
   * read, so the full-map default would be quadratic in component count.
   */
  @Override
  public Optional<ComponentDiagnostics> component(String path) {
    Counters counters = countersByPath.get(path);
    return counters != null ? Optional.of(counters.snapshot()) : Optional.empty();
  }

  @Override
  public void reset(String path) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      // per-object by construction: only this path's entry is touched; the Counters identity is
      // kept so in-flight adds keep landing harmlessly, and lastError is deliberately untouched
      counters.resetCounters();
    }
  }

  /**
   * One counter: a lock-free adder plus the first time it left zero (0 = never). The first-change
   * write race is benign — two near-simultaneous first ticks write near-identical wall-clock values
   * and the last write wins. Wall-clock epoch millis, not nanoTime: TimeFirstChange is a DateTime
   * property (Part 14 §9.1.11.5). {@link LongAdder#reset()}'s lost-concurrent-update caveat is
   * acceptable for an explicit reset.
   */
  private static final class Counter {

    private final LongAdder adder = new LongAdder();

    private volatile long firstChangeEpochMilli;

    void increment() {
      add(1);
    }

    void add(long n) {
      if (n == 0) {
        // a zero-count add is not a change: TimeFirstChange must stay null while the value is 0
        return;
      }
      if (firstChangeEpochMilli == 0) {
        firstChangeEpochMilli = System.currentTimeMillis();
      }
      adder.add(n);
    }

    long sum() {
      return adder.sum();
    }

    void reset() {
      adder.reset();
      firstChangeEpochMilli = 0;
    }

    @Nullable DateTime firstChange() {
      long epochMilli = firstChangeEpochMilli;
      return epochMilli != 0 ? new DateTime(Instant.ofEpochMilli(epochMilli)) : null;
    }
  }

  private static final class Counters {

    final Counter networkMessagesSent = new Counter();
    final Counter networkMessagesReceived = new Counter();
    final Counter dataSetMessagesSent = new Counter();
    final Counter dataSetMessagesReceived = new Counter();
    final Counter decodeErrors = new Counter();
    final Counter sourceErrors = new Counter();
    final Counter staleSequenceMessages = new Counter();
    final Counter invalidSequenceMessages = new Counter();
    final Counter encryptionErrors = new Counter();
    final Counter decryptionErrors = new Counter();
    final Counter invalidSignatureMessages = new Counter();
    final Counter unknownTokenMessages = new Counter();
    final Counter staleKeyMessages = new Counter();
    final Counter failedTransmissions = new Counter();
    final Counter failedDataSetMessages = new Counter();
    final Counter stateError = new Counter();
    final Counter stateOperationalByMethod = new Counter();
    final Counter stateOperationalByParent = new Counter();
    final Counter stateOperationalFromError = new Counter();
    final Counter statePausedByParent = new Counter();
    final Counter stateDisabledByMethod = new Counter();

    volatile @Nullable StatusCode lastError;

    private final String path;

    private Counters(String path) {
      this.path = path;
    }

    private Counter of(PubSubCounter counter) {
      return switch (counter) {
        case NETWORK_MESSAGES_SENT -> networkMessagesSent;
        case NETWORK_MESSAGES_RECEIVED -> networkMessagesReceived;
        case DATA_SET_MESSAGES_SENT -> dataSetMessagesSent;
        case DATA_SET_MESSAGES_RECEIVED -> dataSetMessagesReceived;
        case DECODE_ERRORS -> decodeErrors;
        case SOURCE_ERRORS -> sourceErrors;
        case STALE_SEQUENCE_MESSAGES -> staleSequenceMessages;
        case INVALID_SEQUENCE_MESSAGES -> invalidSequenceMessages;
        case ENCRYPTION_ERRORS -> encryptionErrors;
        case DECRYPTION_ERRORS -> decryptionErrors;
        case INVALID_SIGNATURE_MESSAGES -> invalidSignatureMessages;
        case UNKNOWN_TOKEN_MESSAGES -> unknownTokenMessages;
        case STALE_KEY_MESSAGES -> staleKeyMessages;
        case FAILED_TRANSMISSIONS -> failedTransmissions;
        case FAILED_DATA_SET_MESSAGES -> failedDataSetMessages;
        case STATE_ERROR -> stateError;
        case STATE_OPERATIONAL_BY_METHOD -> stateOperationalByMethod;
        case STATE_OPERATIONAL_BY_PARENT -> stateOperationalByParent;
        case STATE_OPERATIONAL_FROM_ERROR -> stateOperationalFromError;
        case STATE_PAUSED_BY_PARENT -> statePausedByParent;
        case STATE_DISABLED_BY_METHOD -> stateDisabledByMethod;
      };
    }

    void resetCounters() {
      for (PubSubCounter counter : PubSubCounter.values()) {
        of(counter).reset();
      }
    }

    ComponentDiagnostics snapshot() {
      var timeFirstChange = new EnumMap<PubSubCounter, DateTime>(PubSubCounter.class);
      for (PubSubCounter counter : PubSubCounter.values()) {
        DateTime firstChange = of(counter).firstChange();
        if (firstChange != null) {
          timeFirstChange.put(counter, firstChange);
        }
      }

      return new ComponentDiagnostics(
          path,
          networkMessagesSent.sum(),
          networkMessagesReceived.sum(),
          dataSetMessagesSent.sum(),
          dataSetMessagesReceived.sum(),
          decodeErrors.sum(),
          sourceErrors.sum(),
          staleSequenceMessages.sum(),
          invalidSequenceMessages.sum(),
          encryptionErrors.sum(),
          decryptionErrors.sum(),
          invalidSignatureMessages.sum(),
          unknownTokenMessages.sum(),
          staleKeyMessages.sum(),
          failedTransmissions.sum(),
          failedDataSetMessages.sum(),
          stateError.sum(),
          stateOperationalByMethod.sum(),
          stateOperationalByParent.sum(),
          stateOperationalFromError.sum(),
          statePausedByParent.sum(),
          stateDisabledByMethod.sum(),
          Collections.unmodifiableMap(timeFirstChange),
          lastError);
    }
  }
}
