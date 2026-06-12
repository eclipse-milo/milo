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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Per-component diagnostic counters, keyed by component path, and the {@link PubSubDiagnostics}
 * view over them.
 *
 * <p>Counters are lock-free ({@link LongAdder}); {@link #snapshot()} produces an immutable
 * point-in-time copy. Error recordings additionally emit a {@link PubSubDiagnosticsEvent} through
 * the {@link EventDispatcher}.
 *
 * <p>Entries are created by {@link #register(String)} only (startup and reconfigure register every
 * component path); increments for unregistered paths are dropped, so a late in-flight increment
 * cannot resurrect the entry of a component removed by reconfiguration.
 */
final class DiagnosticsCollector implements PubSubDiagnostics {

  private final ConcurrentMap<String, Counters> countersByPath = new ConcurrentHashMap<>();

  private final EventDispatcher events;

  DiagnosticsCollector(EventDispatcher events) {
    this.events = events;
  }

  /** Ensure a (zeroed) entry exists for the component at {@code path}. */
  void register(String path) {
    countersByPath.computeIfAbsent(path, Counters::new);
  }

  /** Remove the entry for a component removed by reconfiguration. */
  void remove(String path) {
    countersByPath.remove(path);
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
    error(path, statusCode, message, error);
  }

  /** Record a {@code PublishedDataSetSource} read failure. */
  void sourceError(String path, StatusCode statusCode, String message, @Nullable Throwable error) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.sourceErrors.increment();
    }
    error(path, statusCode, message, error);
  }

  /**
   * Record an error and emit a diagnostics event for it. The event is emitted even when {@code
   * path} is no longer registered.
   */
  void error(String path, StatusCode statusCode, String message, @Nullable Throwable error) {
    Counters counters = countersByPath.get(path);
    if (counters != null) {
      counters.lastError = statusCode;
    }
    events.notifyDiagnostics(new PubSubDiagnosticsEvent(path, statusCode, message, error));
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

  private static final class Counters {

    final LongAdder networkMessagesSent = new LongAdder();
    final LongAdder networkMessagesReceived = new LongAdder();
    final LongAdder dataSetMessagesSent = new LongAdder();
    final LongAdder dataSetMessagesReceived = new LongAdder();
    final LongAdder decodeErrors = new LongAdder();
    final LongAdder sourceErrors = new LongAdder();
    final LongAdder staleSequenceMessages = new LongAdder();
    final LongAdder invalidSequenceMessages = new LongAdder();

    volatile @Nullable StatusCode lastError;

    private final String path;

    private Counters(String path) {
      this.path = path;
    }

    ComponentDiagnostics snapshot() {
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
          lastError);
    }
  }
}
