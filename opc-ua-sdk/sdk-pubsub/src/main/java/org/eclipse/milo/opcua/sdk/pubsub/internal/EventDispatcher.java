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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataListener;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener registry and safe dispatch for all user-facing events.
 *
 * <p>User code never runs on the publish scheduler or transport event loop threads: DataSet and
 * metadata events are dispatched inline by tasks already running on the transport executor, and
 * state change and diagnostics events (which can originate from engine threads) are re-dispatched
 * onto it through a serializing queue, so they are delivered in emission order, one at a time.
 * Listener exceptions are caught, logged, and recorded in diagnostics; they never propagate into
 * engine threads.
 */
final class EventDispatcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);

  /**
   * Serializes state change and diagnostics event delivery on the transport executor: events are
   * delivered in the order they were emitted, never concurrently.
   */
  private final ExecutionQueue executionQueue;

  private final List<DataSetListener> dataSetListeners = new CopyOnWriteArrayList<>();
  private final ConcurrentMap<DataSetReaderRef, List<DataSetListener>> readerListeners =
      new ConcurrentHashMap<>();
  private final List<PubSubStateListener> stateListeners = new CopyOnWriteArrayList<>();
  private final List<MetaDataListener> metaDataListeners = new CopyOnWriteArrayList<>();
  private final List<PubSubDiagnosticsListener> diagnosticsListeners = new CopyOnWriteArrayList<>();

  private volatile @Nullable DiagnosticsCollector diagnostics;

  EventDispatcher(Executor executor) {
    this.executionQueue = new ExecutionQueue(executor);
  }

  /** Late-bound to break the construction cycle between dispatcher and collector. */
  void setDiagnostics(DiagnosticsCollector diagnostics) {
    this.diagnostics = diagnostics;
  }

  void addDataSetListener(DataSetListener listener) {
    dataSetListeners.add(listener);
  }

  /** Remove {@code listener} (identity-based, first occurrence); never-added is a no-op. */
  void removeDataSetListener(DataSetListener listener) {
    dataSetListeners.remove(listener);
  }

  void addDataSetListener(DataSetReaderRef ref, DataSetListener listener) {
    // a single compute keeps the add atomic with the empty-entry drop in the removal below:
    // computeIfAbsent(...).add(...) could land the listener on a list a concurrent removal
    // just unmapped, silently orphaning the registration
    readerListeners.compute(
        ref,
        (k, listeners) -> {
          List<DataSetListener> list = listeners != null ? listeners : new CopyOnWriteArrayList<>();
          list.add(listener);
          return list;
        });
  }

  /**
   * Remove {@code listener} from the per-reader list of {@code ref} (identity-based, first
   * occurrence); never-added is a no-op. The map entry is dropped when its list empties; a racing
   * {@link #notifyDataSet} iterating a just-emptied list is benign (it delivers to no one).
   */
  void removeDataSetListener(DataSetReaderRef ref, DataSetListener listener) {
    readerListeners.computeIfPresent(
        ref,
        (k, listeners) -> {
          listeners.remove(listener);
          return listeners.isEmpty() ? null : listeners;
        });
  }

  void addStateListener(PubSubStateListener listener) {
    stateListeners.add(listener);
  }

  /** Remove {@code listener} (identity-based, first occurrence); never-added is a no-op. */
  void removeStateListener(PubSubStateListener listener) {
    stateListeners.remove(listener);
  }

  void addMetaDataListener(MetaDataListener listener) {
    metaDataListeners.add(listener);
  }

  /** Remove {@code listener} (identity-based, first occurrence); never-added is a no-op. */
  void removeMetaDataListener(MetaDataListener listener) {
    metaDataListeners.remove(listener);
  }

  void addDiagnosticsListener(PubSubDiagnosticsListener listener) {
    diagnosticsListeners.add(listener);
  }

  /** Remove {@code listener} (identity-based, first occurrence); never-added is a no-op. */
  void removeDiagnosticsListener(PubSubDiagnosticsListener listener) {
    diagnosticsListeners.remove(listener);
  }

  /**
   * Dispatch a DataSet event to the per-reader and global listeners.
   *
   * <p>Must be invoked on the transport executor (the subscriber dispatch path).
   */
  void notifyDataSet(DataSetReaderRef ref, DataSetReceivedEvent event) {
    String path = event.reader().path();

    List<DataSetListener> perReader = readerListeners.get(ref);
    if (perReader != null) {
      for (DataSetListener listener : perReader) {
        invokeSafely(path, () -> listener.onDataSet(event));
      }
    }
    for (DataSetListener listener : dataSetListeners) {
      invokeSafely(path, () -> listener.onDataSet(event));
    }
  }

  /**
   * Dispatch a metadata event to the metadata listeners.
   *
   * <p>Must be invoked on the transport executor (the subscriber dispatch path).
   */
  void notifyMetaData(MetaDataReceivedEvent event) {
    String path = event.reader().path();

    for (MetaDataListener listener : metaDataListeners) {
      invokeSafely(path, () -> listener.onMetaData(event));
    }
  }

  /**
   * Dispatch a state change event to the state listeners, on the transport executor, serialized
   * with all other state change and diagnostics events.
   */
  void notifyStateChange(PubSubStateChangeEvent event) {
    if (stateListeners.isEmpty()) {
      return;
    }
    execute(
        () -> {
          String path = event.component().path();
          for (PubSubStateListener listener : stateListeners) {
            invokeSafely(path, () -> listener.onStateChange(event));
          }
        });
  }

  /**
   * Dispatch a diagnostics event to the diagnostics listeners, on the transport executor,
   * serialized with all other state change and diagnostics events.
   */
  void notifyDiagnostics(PubSubDiagnosticsEvent event) {
    if (diagnosticsListeners.isEmpty()) {
      return;
    }
    execute(
        () -> {
          for (PubSubDiagnosticsListener listener : diagnosticsListeners) {
            try {
              listener.onDiagnosticsEvent(event);
            } catch (Exception e) {
              // logged only: recording it in diagnostics would recurse
              LOGGER.warn("PubSubDiagnosticsListener failed for '{}'", event.path(), e);
            }
          }
        });
  }

  /**
   * Get a future that completes once every state change and diagnostics event enqueued before this
   * call has been delivered.
   *
   * <p>Used at service shutdown so the shutdown future does not complete while shutdown-induced
   * state change events are still queued for delivery.
   */
  CompletableFuture<Void> drain() {
    var drained = new CompletableFuture<Void>();
    try {
      executionQueue.submit(() -> drained.complete(null));
    } catch (RejectedExecutionException e) {
      // the executor is shut down: nothing still queued can be delivered anymore
      LOGGER.debug("Event queue drain barrier rejected; executor is shut down", e);
      drained.complete(null);
    }
    return drained;
  }

  private void execute(Runnable task) {
    try {
      executionQueue.submit(task);
    } catch (RejectedExecutionException e) {
      LOGGER.debug("Event dispatch rejected; executor is shut down", e);
    }
  }

  private void invokeSafely(String path, Runnable invocation) {
    try {
      invocation.run();
    } catch (Exception e) {
      LOGGER.warn("Listener failed for '{}'", path, e);
      DiagnosticsCollector diagnostics = this.diagnostics;
      if (diagnostics != null) {
        diagnostics.listenerError(path);
      }
    }
  }
}
