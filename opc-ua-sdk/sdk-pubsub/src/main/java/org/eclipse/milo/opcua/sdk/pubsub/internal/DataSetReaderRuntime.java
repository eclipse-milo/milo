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
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetRef;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * Runtime for one DataSetReader: carries the reader's matching filters, its resolved configured
 * metadata, its Part 14 §7.2.3 sequence-number tracking state, and the message-receive-timeout
 * watchdog.
 *
 * <p>Per Part 14 §6.2.1 the reader stays {@code PreOperational} until its first key frame or event
 * DataSetMessage is decoded (delta frames are delivered but do not complete startup); with a
 * configured {@code messageReceiveTimeout} it transitions to {@code Error} when no (new) message
 * arrives within the timeout and back to {@code Operational} on the next message (keep-alives
 * count, sequence-window drops do not).
 */
final class DataSetReaderRuntime extends AbstractComponentRuntime {

  private final PubSubServiceImpl service;
  private final ConnectionRuntime connection;
  private final DataSetReaderConfig config;
  private final DataSetReaderRef readerRef;
  private final @Nullable DataSetMetaDataConfig configuredMetaData;
  private final String mappingName;
  private final long timeoutNanos;

  private final AtomicLong lastMessageNanos = new AtomicLong(System.nanoTime());

  /**
   * Part 14 §7.2.3 sequence-number window state; reset by replacement in {@link #activate()}
   * (engine threads), read and mutated on the serialized per-connection dispatch thread. Volatile
   * for safe publication across the two.
   */
  private volatile ReaderSequenceTracker sequenceTracker;

  /** Guarded by {@code this}. */
  private @Nullable ScheduledFuture<?> watchdog;

  /** Guarded by {@code this}; invalidates timeout checks queued by an older watchdog. */
  private long watchdogSerial = 0L;

  private volatile boolean disposed = false;

  DataSetReaderRuntime(
      PubSubServiceImpl service,
      ConnectionRuntime connection,
      ReaderGroupRuntime group,
      DataSetReaderConfig config) {

    super(
        ComponentType.DATA_SET_READER,
        group.path() + "/" + config.getName(),
        group,
        config.isEnabled());

    this.service = service;
    this.connection = connection;
    this.config = config;

    this.readerRef =
        new DataSetReaderRef(
            connection.config().name(), group.config().getName(), config.getName());

    this.configuredMetaData = resolveConfiguredMetaData(service, config);
    this.mappingName = PubSubServiceImpl.mappingNameOf(config.getSettings());
    this.timeoutNanos =
        config.getMessageReceiveTimeout().isZero()
            ? 0L
            : config.getMessageReceiveTimeout().toNanos();
    this.sequenceTracker = newSequenceTracker();
  }

  private ReaderSequenceTracker newSequenceTracker() {
    // the §7.2.3 window needs the wire width N of the sequence numbers, which the widened decoded
    // model does not carry: the UADP mapping is UInt16; JSON and unknown custom mappings, whose
    // decoded slot spans UInt32, get N=32
    int bitWidth = PubSubServiceImpl.MAPPING_UADP.equals(mappingName) ? 16 : 32;

    // §7.2.3: records are discarded after two times the message receive timeout of silence;
    // 0 (timeout disabled) means no time-based discard
    return new ReaderSequenceTracker(path(), bitWidth, 2 * timeoutNanos);
  }

  DataSetReaderConfig config() {
    return config;
  }

  DataSetReaderRef readerRef() {
    return readerRef;
  }

  /**
   * The metadata configured for this reader: the reader's own metadata, or the metadata of the
   * standalone SubscribedDataSet it references, or null.
   */
  @Nullable DataSetMetaDataConfig configuredMetaData() {
    return configuredMetaData;
  }

  /** The name of the message mapping this reader's messages are decoded with. */
  String mappingName() {
    return mappingName;
  }

  /** The Part 14 §7.2.3 sequence-number tracking state of this reader. */
  ReaderSequenceTracker sequenceTracker() {
    return sequenceTracker;
  }

  /**
   * The sequence number of the last DataSetMessage accepted by this reader's §7.2.3 window, or
   * {@code null} if none since the last activation: the source for a future Part 14 §9.1.11.12
   * Table 331 {@code MessageSequenceNumber} LiveValue.
   */
  @Nullable UInteger lastDataSetMessageSequenceNumber() {
    return sequenceTracker.lastAcceptedDataSetMessageSequenceNumber();
  }

  @Override
  List<? extends AbstractComponentRuntime> children() {
    return List.of();
  }

  @Override
  boolean startupCompletesImmediately() {
    return false;
  }

  @Override
  void activate() throws UaException {
    // reset the §7.2.3 sequence-number windows on every PreOperational entry (startup, enable
    // after disable; reconfigure recreates the runtime) — deliberately NOT in onEnterOperational,
    // which also runs on Error→Operational recovery and must keep the last processed numbers
    sequenceTracker = newSequenceTracker();

    if (service.resolveMappingProvider(mappingName) == null) {
      var e =
          new UaException(
              StatusCodes.Bad_ConfigurationError,
              "no MessageMappingProvider for mapping '%s' (dataset reader '%s')"
                  .formatted(mappingName, path()));
      service
          .getDiagnostics()
          .error(
              path(),
              e.getStatusCode(),
              e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      throw e;
    }

    // a REQUEST_IF_MISSING reader without effective metadata starts emitting discovery probes
    DiscoveryRuntime discovery = connection.discoveryRuntime();
    if (discovery != null) {
      discovery.onReaderActivated(this);
    }
  }

  @Override
  void onEnterOperational() {
    lastMessageNanos.set(System.nanoTime());
    armWatchdog();
  }

  @Override
  void deactivate() {
    cancelWatchdog();
    cancelProbing();
  }

  /** Release all resources of this runtime. The runtime is unusable afterwards. */
  void dispose() {
    disposed = true;
    cancelWatchdog();
    cancelProbing();
    service.getMetadataCache().remove(handle());
  }

  /** Whether {@link #dispose()} has been called. */
  boolean isDisposed() {
    return disposed;
  }

  /**
   * Record that a (new) DataSetMessage for this reader was accepted: resets the receive timeout and
   * resolves a timeout-induced {@code Error} state. Called on the transport executor.
   */
  void onMessageAccepted() {
    lastMessageNanos.set(System.nanoTime());

    PubSubState state = state();
    if (state == PubSubState.Error) {
      service.getStateMachine().recover(this);
    } else if (state == PubSubState.Operational) {
      armWatchdog();
    }
  }

  private void cancelProbing() {
    DiscoveryRuntime discovery = connection.discoveryRuntime();
    if (discovery != null) {
      discovery.onReaderDeactivated(this);
    }
  }

  private void armWatchdog() {
    if (timeoutNanos == 0L || disposed) {
      return;
    }
    synchronized (this) {
      if (watchdog == null || watchdog.isDone()) {
        schedule(timeoutNanos);
      }
    }
  }

  private void cancelWatchdog() {
    synchronized (this) {
      watchdogSerial++;
      ScheduledFuture<?> watchdog = this.watchdog;
      this.watchdog = null;
      if (watchdog != null) {
        watchdog.cancel(false);
      }
    }
  }

  private void schedule(long delayNanos) {
    long serial = ++watchdogSerial;
    watchdog =
        service
            .getScheduledExecutor()
            .schedule(() -> submitCheckTimeout(serial), delayNanos, TimeUnit.NANOSECONDS);
  }

  private void submitCheckTimeout(long serial) {
    try {
      connection.submitToDispatchQueue(() -> checkTimeout(serial));
    } catch (RejectedExecutionException e) {
      synchronized (this) {
        if (serial == watchdogSerial) {
          watchdog = null;
        }
      }
    }
  }

  private void checkTimeout(long serial) {
    boolean expired = false;

    synchronized (this) {
      if (serial != watchdogSerial) {
        return;
      }
      watchdog = null;

      if (disposed || state() != PubSubState.Operational) {
        return;
      }

      long elapsed = System.nanoTime() - lastMessageNanos.get();
      if (elapsed >= timeoutNanos) {
        expired = true;
      } else {
        schedule(timeoutNanos - elapsed);
      }
    }

    if (expired) {
      // engine lock acquired outside the monitor on `this` to keep lock ordering one-way
      service
          .getDiagnostics()
          .error(
              path(),
              new StatusCode(StatusCodes.Bad_Timeout),
              "messageReceiveTimeout expired",
              null,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      service.getStateMachine().fail(this, new StatusCode(StatusCodes.Bad_Timeout));
    }
  }

  private static @Nullable DataSetMetaDataConfig resolveConfiguredMetaData(
      PubSubServiceImpl service, DataSetReaderConfig config) {

    if (config.getDataSetMetaData() != null) {
      return config.getDataSetMetaData();
    }

    if (config.getSubscribedDataSet() instanceof StandaloneSubscribedDataSetRef ref) {
      for (StandaloneSubscribedDataSetConfig sds :
          service.getConfig().standaloneSubscribedDataSets()) {

        if (sds.getName().equals(ref.name())) {
          return sds.getMetaData();
        }
      }
    }

    return null;
  }
}
