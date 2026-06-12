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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetRef;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * Runtime for one DataSetReader: carries the reader's matching filters, its resolved configured
 * metadata, and the message-receive-timeout watchdog.
 *
 * <p>Per Part 14 §6.2.1 the reader stays {@code PreOperational} until its first key frame or event
 * DataSetMessage is decoded (delta frames are delivered but do not complete startup); with a
 * configured {@code messageReceiveTimeout} it transitions to {@code Error} when no (new) message
 * arrives within the timeout and back to {@code Operational} on the next message (keep-alives
 * count).
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

  /** Guarded by {@code this}. */
  private @Nullable ScheduledFuture<?> watchdog;

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
    if (service.resolveMappingProvider(mappingName) == null) {
      var e =
          new UaException(
              StatusCodes.Bad_ConfigurationError,
              "no MessageMappingProvider for mapping '%s' (dataset reader '%s')"
                  .formatted(mappingName, path()));
      service.getDiagnostics().error(path(), e.getStatusCode(), e.getMessage(), e);
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
      ScheduledFuture<?> watchdog = this.watchdog;
      this.watchdog = null;
      if (watchdog != null) {
        watchdog.cancel(false);
      }
    }
  }

  private void schedule(long delayNanos) {
    watchdog =
        service
            .getScheduledExecutor()
            .schedule(this::checkTimeout, delayNanos, TimeUnit.NANOSECONDS);
  }

  private void checkTimeout() {
    boolean expired = false;

    synchronized (this) {
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
              null);
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
