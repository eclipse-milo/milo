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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import io.netty.util.concurrent.Future;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubComponents;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderMessageSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterMessageSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.JsonWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetReaderSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupMessageSettings;
import org.eclipse.milo.opcua.sdk.pubsub.json.JsonMessageMappingProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.transport.udp.UdpTransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PubSubService} implementation: owns the runtime component tree, the state machine, the
 * diagnostics, and the lifecycle.
 *
 * <p>Threading: the config tree is immutable; a single engine lock guards all lifecycle and state
 * mutation (startup, shutdown, enable/disable, reconfigure, state transitions). Publish cycles run
 * on the scheduled executor and read volatile snapshots; subscriber dispatch and all listener
 * delivery run on the transport executor, never on the publish scheduler.
 */
public final class PubSubServiceImpl implements PubSubService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubServiceImpl.class);

  static final String MAPPING_UADP = "uadp";
  static final String MAPPING_JSON = "json";

  /**
   * The largest maxNetworkMessageSize configurable on an OPC UA UDP writer group: Part 14 §7.3.2.1,
   * "For OPC UA UDP the MaxNetworkMessageSize plus additional headers shall be limited to 65535
   * Byte."
   */
  private static final long MAX_UDP_NETWORK_MESSAGE_SIZE = 65535L;

  private final Object lock = new Object();

  private final PubSubServiceConfig serviceConfig;
  private final UadpMessageMapping builtinUadpMapping = new UadpMessageMapping();
  private final JsonMessageMappingProvider builtinJsonMapping = new JsonMessageMappingProvider();
  private final UdpTransportProvider builtinUdpTransport = new UdpTransportProvider();

  private final ConcurrentMap<PublishedDataSetRef, PublishedDataSetSource> sources =
      new ConcurrentHashMap<>();

  /**
   * Discovery announcement sequence counters, one per PublisherId: Part 14 §7.2.4.6.3 Table 168
   * defines the counter "in the scope of a PublisherId", which multiple connections may share, so
   * it lives on the service rather than on a per-connection {@link DiscoveryRuntime}.
   */
  private final ConcurrentMap<PublisherId, AtomicInteger> announcementSequenceNumbers =
      new ConcurrentHashMap<>();

  private final EventDispatcher eventDispatcher;
  private final DiagnosticsCollector diagnostics;
  private final MetadataCache metadataCache = new MetadataCache();
  private final HandleRegistry registry = new HandleRegistry();
  private final PubSubStateMachine stateMachine;
  private final ReaderDispatcher readerDispatcher;

  /** Guarded by the engine lock. */
  private final Map<String, ConnectionRuntime> connections = new LinkedHashMap<>();

  private volatile PubSubConfig config;

  /** Guarded by the engine lock. */
  private boolean started = false;

  /** Guarded by the engine lock. */
  private boolean startupInProgress = false;

  /** Guarded by the engine lock. */
  private @Nullable CompletableFuture<Void> shutdownFuture;

  private PubSubServiceImpl(
      PubSubConfig config, @Nullable PubSubBindings bindings, PubSubServiceConfig serviceConfig) {

    this.config = config;
    this.serviceConfig = serviceConfig;

    eventDispatcher = new EventDispatcher(serviceConfig.getTransportExecutor());
    diagnostics = new DiagnosticsCollector(eventDispatcher);
    eventDispatcher.setDiagnostics(diagnostics);

    stateMachine = new PubSubStateMachine(lock, this::onStateChange);
    readerDispatcher = new ReaderDispatcher(this);

    if (bindings != null) {
      sources.putAll(bindings.getSources());
      bindings
          .getListeners()
          .forEach(
              (ref, listeners) ->
                  listeners.forEach(listener -> eventDispatcher.addDataSetListener(ref, listener)));
      // SecurityKeyProviders are accepted but unused: only MessageSecurityMode.None is
      // supported in this version.
    }

    synchronized (lock) {
      try {
        for (PubSubConnectionConfig connectionConfig : config.connections()) {
          ConnectionRuntime runtime = new ConnectionRuntime(this, connectionConfig);
          connections.put(connectionConfig.name(), runtime);
          registerTree(runtime);
        }
      } catch (RuntimeException | Error e) {
        // unwind already-constructed runtimes so their per-connection event loops are not
        // leaked; the half-built service is never returned, so triggering shutdown suffices
        connections.values().forEach(ConnectionRuntime::dispose);
        throw e;
      }
      stateMachine.setRootOperational(false, connections.values());
    }
  }

  /**
   * Create a new {@link PubSubService}.
   *
   * @param config the {@link PubSubConfig} describing the PubSub components.
   * @param bindings the {@link PubSubBindings} for the names in {@code config}, or {@code null} to
   *     bind everything later.
   * @param serviceConfig the {@link PubSubServiceConfig} describing the runtime environment, or
   *     {@code null} to use {@link PubSubServiceConfig#defaults()}.
   * @return a new {@link PubSubService}, not yet started.
   * @throws IllegalArgumentException if {@code bindings} references names that do not exist in
   *     {@code config}.
   */
  public static PubSubService create(
      PubSubConfig config,
      @Nullable PubSubBindings bindings,
      @Nullable PubSubServiceConfig serviceConfig) {

    requireNonNull(config, "config");

    if (bindings != null) {
      validateBindings(config, bindings);
    }

    return new PubSubServiceImpl(
        config, bindings, serviceConfig != null ? serviceConfig : PubSubServiceConfig.defaults());
  }

  // region lifecycle

  @Override
  public CompletableFuture<PubSubService> startup() {
    synchronized (lock) {
      if (shutdownFuture != null) {
        return CompletableFuture.failedFuture(
            new IllegalStateException("service has been shut down"));
      }
      if (started) {
        return CompletableFuture.completedFuture(this);
      }
      if (startupInProgress) {
        return CompletableFuture.failedFuture(
            new IllegalStateException("startup already in progress"));
      }
      startupInProgress = true;
    }

    return CompletableFuture.supplyAsync(
        () -> {
          try {
            doStartup();
            return this;
          } finally {
            synchronized (lock) {
              startupInProgress = false;
            }
          }
        },
        serviceConfig.getTransportExecutor());
  }

  private void doStartup() {
    synchronized (lock) {
      if (shutdownFuture != null) {
        throw new CompletionException(new IllegalStateException("service has been shut down"));
      }

      try {
        validateStartup(config);

        if (config.isEnabled()) {
          var opened = new ArrayList<ConnectionRuntime>();
          try {
            for (ConnectionRuntime connection : connections.values()) {
              if (connection.isEnabled()) {
                // added before openChannels() so a partial open (publisher channel open,
                // subscriber channel failed) is still unwound; closeChannels() is idempotent.
                opened.add(connection);
                connection.openChannels();
              }
            }
          } catch (UaException e) {
            opened.forEach(ConnectionRuntime::closeChannels);
            throw e;
          }
        }

        started = true;
        stateMachine.setRootOperational(config.isEnabled(), connections.values());
      } catch (UaException e) {
        throw new CompletionException(e);
      }
    }
  }

  @Override
  public CompletableFuture<Void> shutdown() {
    synchronized (lock) {
      if (shutdownFuture != null) {
        return shutdownFuture;
      }

      CompletableFuture<Void> future =
          CompletableFuture.runAsync(this::doShutdown, serviceConfig.getTransportExecutor())
              // doShutdown enqueues the shutdown-induced state change events (transitions
              // into Disabled); completing only after the event queue drains guarantees no
              // listener callback for them is still in flight when shutdown resolves
              .thenCompose(v -> eventDispatcher.drain());

      shutdownFuture = future;
      return future;
    }
  }

  private void doShutdown() {
    var disposeFutures = new ArrayList<Future<?>>();

    synchronized (lock) {
      started = false;

      for (ConnectionRuntime connection : connections.values()) {
        stateMachine.disposeSubtree(connection);
        Future<?> disposeFuture = connection.dispose();
        if (disposeFuture != null) {
          disposeFutures.add(disposeFuture);
        }
      }

      // clear the root operational flag so a post-shutdown enable() can never transition a
      // component past Paused and reactivate disposed transport resources; handles themselves
      // stay valid (only reconfiguration invalidates them).
      stateMachine.setRootOperational(false, connections.values());
    }

    // per-connection event loop termination is awaited after releasing the engine lock so the
    // (up to ~3 s per connection) wait cannot stall enable/disable/dispatch on other threads
    awaitDisposeFutures(disposeFutures);
  }

  /** Await per-connection event loop termination. Must not be called holding the engine lock. */
  private static void awaitDisposeFutures(List<Future<?>> disposeFutures) {
    for (Future<?> disposeFuture : disposeFutures) {
      try {
        disposeFuture.await(3, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }

  @Override
  public void close() {
    try {
      shutdown().get(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (ExecutionException | TimeoutException e) {
      LOGGER.warn("Error during shutdown", e);
    }
  }

  // endregion

  // region runtime control

  @Override
  public PubSubComponents components() {
    return registry;
  }

  @Override
  public void enable(PubSubHandle handle) {
    AbstractComponentRuntime component = requireComponent(handle);

    stateMachine.setEnabled(component, true);
  }

  @Override
  public void disable(PubSubHandle handle) {
    AbstractComponentRuntime component = requireComponent(handle);

    stateMachine.setEnabled(component, false);
  }

  @Override
  public PubSubState state(PubSubHandle handle) {
    return requireComponent(handle).state();
  }

  private AbstractComponentRuntime requireComponent(PubSubHandle handle) {
    requireNonNull(handle, "handle");

    AbstractComponentRuntime component = registry.get(handle);
    if (component == null) {
      throw new IllegalArgumentException(
          "handle does not belong to this service or has been invalidated: " + handle);
    }
    return component;
  }

  // endregion

  // region reconfigure

  @Override
  public ReconfigureResult reconfigure(PubSubConfig newConfig, ReconfigureMode mode) {
    requireNonNull(newConfig, "newConfig");
    requireNonNull(mode, "mode");

    var disposeFutures = new ArrayList<Future<?>>();

    try {
      synchronized (lock) {
        return reconfigureInternal(newConfig, mode, disposeFutures);
      }
    } finally {
      // per-connection event loop termination is awaited after releasing the engine lock so
      // the (up to ~3 s per connection) wait cannot stall enable/disable/dispatch on other
      // threads
      awaitDisposeFutures(disposeFutures);
    }
  }

  @Override
  public ReconfigureResult update(UnaryOperator<PubSubConfig> transform) {
    requireNonNull(transform, "transform");

    var disposeFutures = new ArrayList<Future<?>>();

    try {
      synchronized (lock) {
        PubSubConfig newConfig = requireNonNull(transform.apply(config), "transformed config");
        return reconfigureInternal(newConfig, ReconfigureMode.DISABLE_AFFECTED, disposeFutures);
      }
    } finally {
      awaitDisposeFutures(disposeFutures);
    }
  }

  /** Apply a reconfiguration. Must be called holding the engine lock. */
  private ReconfigureResult reconfigureInternal(
      PubSubConfig newConfig, ReconfigureMode mode, List<Future<?>> disposeFutures) {

    if (shutdownFuture != null) {
      throw new IllegalStateException("service has been shut down");
    }

    // reject configs that startup would reject for conditions the runtime cannot degrade
    // gracefully on; accepting them here would silently bypass validateStartup (M7)
    validateReconfigure(newConfig);

    ConfigDiff.Result diff = ConfigDiff.diff(config, newConfig);

    // snapshot the discovery announcement baselines before the new config takes effect: a
    // connection rebuilt below gets a fresh DiscoveryRuntime whose channel-open baseline derives
    // from the NEW config and would mask dataset metadata changes applied by this same
    // reconfiguration (Part 14 §7.2.4.6.4 requires announcing them)
    var previousRuntimes = new LinkedHashMap<>(connections);
    var announcementBaselines = new LinkedHashMap<String, Map<UShort, DataSetMetaDataType>>();
    for (ConnectionRuntime connection : connections.values()) {
      DiscoveryRuntime discovery = connection.discoveryRuntime();
      if (discovery != null) {
        announcementBaselines.put(
            connection.config().name(), discovery.announcedMetaDataSnapshot());
      }
    }

    config = newConfig;

    var added = new ArrayList<String>();
    var removed = new ArrayList<String>();
    var restarted = new ArrayList<String>();

    if (mode == ReconfigureMode.STOP_AND_RESTART) {
      applyStopAndRestart(diff, added, removed, restarted, disposeFutures);
    } else {
      applyDisableAffected(diff, added, removed, restarted, disposeFutures);
    }

    stateMachine.setRootOperational(started && config.isEnabled(), connections.values());

    // discovery responders push an unsolicited DataSetMetaData announcement for live writers
    // whose dataset metadata changed (Part 14 §7.2.4.6.4); the check runs off the engine lock
    for (ConnectionRuntime connection : connections.values()) {
      DiscoveryRuntime discovery = connection.discoveryRuntime();
      if (discovery != null) {
        if (connection != previousRuntimes.get(connection.config().name())) {
          // rebuilt connection: restore the predecessor's baseline so the check below compares
          // against what was actually last announced rather than the post-change config
          Map<UShort, DataSetMetaDataType> baseline =
              announcementBaselines.get(connection.config().name());
          if (baseline != null) {
            discovery.seedAnnouncedMetaData(baseline);
          }
        }
        discovery.onConfigurationApplied();
      }

      // broker connections republish retained DataSetMetaData for live writers whose dataset
      // metadata changed without a writer restart (§5.2.3); restarted writers republished at
      // activation already, so the baselines are current and this is a safety net
      MetaDataPublisher metaDataPublisher = connection.metaDataPublisher();
      if (metaDataPublisher != null) {
        metaDataPublisher.onConfigurationApplied();
      }
    }

    return new ReconfigureResult(added, removed, restarted);
  }

  private void applyDisableAffected(
      ConfigDiff.Result diff,
      List<String> added,
      List<String> removed,
      List<String> restarted,
      List<Future<?>> disposeFutures) {

    for (ConfigDiff.Change change : diff.changes()) {
      if (change.kind() == ConfigDiff.Kind.REMOVED) {
        applyChange(change, added, removed, restarted, disposeFutures);
      }
    }
    for (ConfigDiff.Change change : diff.changes()) {
      if (change.kind() == ConfigDiff.Kind.CHANGED) {
        applyChange(change, added, removed, restarted, disposeFutures);
      }
    }
    for (ConfigDiff.Change change : diff.changes()) {
      if (change.kind() == ConfigDiff.Kind.ADDED) {
        applyChange(change, added, removed, restarted, disposeFutures);
      }
    }
  }

  private void applyChange(
      ConfigDiff.Change change,
      List<String> added,
      List<String> removed,
      List<String> restarted,
      List<Future<?>> disposeFutures) {

    switch (change.level()) {
      case OTHER -> {
        switch (change.kind()) {
          case ADDED -> added.add(change.path());
          case REMOVED -> removed.add(change.path());
          case CHANGED -> {
            // induced writer/reader restarts are separate entries in the diff
          }
        }
      }

      case CONNECTION -> {
        String name = requireNonNull(change.connectionName());
        switch (change.kind()) {
          case ADDED -> {
            added.add(change.path());
            rebuildConnection(name);
          }
          case REMOVED -> {
            removed.add(change.path());
            removeConnection(name, disposeFutures);
          }
          case CHANGED -> {
            restarted.add(change.path());
            removeConnection(name, disposeFutures);
            rebuildConnection(name);
          }
        }
      }

      case WRITER_GROUP, READER_GROUP -> {
        ConnectionRuntime connection = connections.get(requireNonNull(change.connectionName()));
        if (connection == null) {
          return;
        }
        boolean writerSide = change.level() == ConfigDiff.Level.WRITER_GROUP;
        String groupName = requireNonNull(change.groupName());

        switch (change.kind()) {
          case ADDED -> {
            added.add(change.path());
            addGroup(connection, groupName, writerSide);
          }
          case REMOVED -> {
            removed.add(change.path());
            removeGroup(connection, groupName, writerSide);
          }
          case CHANGED -> {
            restarted.add(change.path());
            removeGroup(connection, groupName, writerSide);
            addGroup(connection, groupName, writerSide);
          }
        }
      }

      case DATA_SET_WRITER, DATA_SET_READER -> {
        ConnectionRuntime connection = connections.get(requireNonNull(change.connectionName()));
        if (connection == null) {
          return;
        }
        boolean writerSide = change.level() == ConfigDiff.Level.DATA_SET_WRITER;
        String groupName = requireNonNull(change.groupName());
        String componentName = requireNonNull(change.componentName());

        switch (change.kind()) {
          case ADDED -> {
            added.add(change.path());
            addLeaf(connection, groupName, componentName, writerSide);
          }
          case REMOVED -> {
            removed.add(change.path());
            removeLeaf(connection, groupName, componentName, writerSide);
          }
          case CHANGED -> {
            restarted.add(change.path());
            removeLeaf(connection, groupName, componentName, writerSide);
            addLeaf(connection, groupName, componentName, writerSide);
          }
        }
      }
    }
  }

  private void applyStopAndRestart(
      ConfigDiff.Result diff,
      List<String> added,
      List<String> removed,
      List<String> restarted,
      List<Future<?>> disposeFutures) {

    var connectionsToRestart = new LinkedHashSet<String>();

    for (ConfigDiff.Change change : diff.changes()) {
      if (change.level() == ConfigDiff.Level.OTHER) {
        switch (change.kind()) {
          case ADDED -> added.add(change.path());
          case REMOVED -> removed.add(change.path());
          case CHANGED -> {
            // induced restarts are separate entries
          }
        }
      } else if (change.level() == ConfigDiff.Level.CONNECTION
          && change.kind() == ConfigDiff.Kind.ADDED) {
        added.add(change.path());
        rebuildConnection(requireNonNull(change.connectionName()));
      } else if (change.level() == ConfigDiff.Level.CONNECTION
          && change.kind() == ConfigDiff.Kind.REMOVED) {
        removed.add(change.path());
        removeConnection(requireNonNull(change.connectionName()), disposeFutures);
      } else {
        // any other change restarts the whole containing connection; nested adds/removes are
        // realized by the rebuild but still reported
        switch (change.kind()) {
          case ADDED -> added.add(change.path());
          case REMOVED -> removed.add(change.path());
          case CHANGED -> {
            // reported via the restarted connection
          }
        }
        connectionsToRestart.add(requireNonNull(change.connectionName()));
      }
    }

    for (String name : connectionsToRestart) {
      restarted.add(name);
      removeConnection(name, disposeFutures);
      rebuildConnection(name);
    }
  }

  private void rebuildConnection(String name) {
    config
        .connection(name)
        .ifPresent(
            connectionConfig -> {
              ConnectionRuntime runtime = new ConnectionRuntime(this, connectionConfig);
              connections.put(name, runtime);
              registerTree(runtime);
            });
  }

  private void removeConnection(String name, List<Future<?>> disposeFutures) {
    ConnectionRuntime runtime = connections.remove(name);
    if (runtime != null) {
      stateMachine.disposeSubtree(runtime);
      unregisterTree(runtime);
      Future<?> disposeFuture = runtime.dispose();
      if (disposeFuture != null) {
        disposeFutures.add(disposeFuture);
      }
    }
  }

  private void addGroup(ConnectionRuntime connection, String groupName, boolean writerSide) {
    PubSubConnectionConfig connectionConfig =
        config.connection(connection.config().name()).orElse(null);
    if (connectionConfig == null) {
      return;
    }

    if (writerSide) {
      connectionConfig.writerGroups().stream()
          .filter(g -> g.getName().equals(groupName))
          .findFirst()
          .ifPresent(
              groupConfig -> {
                var runtime = new WriterGroupRuntime(this, connection, groupConfig);
                connection.addWriterGroupRuntime(runtime);
                registerTree(runtime);
              });
    } else {
      connectionConfig.readerGroups().stream()
          .filter(g -> g.getName().equals(groupName))
          .findFirst()
          .ifPresent(
              groupConfig -> {
                var runtime = new ReaderGroupRuntime(this, connection, groupConfig);
                connection.addReaderGroupRuntime(runtime);
                registerTree(runtime);
              });
    }
  }

  private void removeGroup(ConnectionRuntime connection, String groupName, boolean writerSide) {
    if (writerSide) {
      WriterGroupRuntime runtime = connection.findWriterGroupRuntime(groupName);
      if (runtime != null) {
        stateMachine.disposeSubtree(runtime);
        connection.removeWriterGroupRuntime(runtime);
        unregisterTree(runtime);
        runtime.dispose();
      }
    } else {
      ReaderGroupRuntime runtime = connection.findReaderGroupRuntime(groupName);
      if (runtime != null) {
        stateMachine.disposeSubtree(runtime);
        connection.removeReaderGroupRuntime(runtime);
        unregisterTree(runtime);
        runtime.dispose();
      }
    }
  }

  private void addLeaf(
      ConnectionRuntime connection, String groupName, String componentName, boolean writerSide) {

    PubSubConnectionConfig connectionConfig =
        config.connection(connection.config().name()).orElse(null);
    if (connectionConfig == null) {
      return;
    }

    if (writerSide) {
      WriterGroupRuntime group = connection.findWriterGroupRuntime(groupName);
      if (group == null) {
        return;
      }
      connectionConfig.writerGroups().stream()
          .filter(g -> g.getName().equals(groupName))
          .flatMap(g -> g.getDataSetWriters().stream())
          .filter(w -> w.getName().equals(componentName))
          .findFirst()
          .ifPresent(
              writerConfig -> {
                var runtime = new DataSetWriterRuntime(this, group, writerConfig);
                group.addWriterRuntime(runtime);
                registerTree(runtime);
              });
    } else {
      ReaderGroupRuntime group = connection.findReaderGroupRuntime(groupName);
      if (group == null) {
        return;
      }
      connectionConfig.readerGroups().stream()
          .filter(g -> g.getName().equals(groupName))
          .flatMap(g -> g.getDataSetReaders().stream())
          .filter(r -> r.getName().equals(componentName))
          .findFirst()
          .ifPresent(
              readerConfig -> {
                var runtime = new DataSetReaderRuntime(this, connection, group, readerConfig);
                group.addReaderRuntime(runtime);
                registerTree(runtime);
                connection.refreshSubscriberMappings();
              });
    }
  }

  private void removeLeaf(
      ConnectionRuntime connection, String groupName, String componentName, boolean writerSide) {

    if (writerSide) {
      WriterGroupRuntime group = connection.findWriterGroupRuntime(groupName);
      if (group == null) {
        return;
      }
      DataSetWriterRuntime runtime = group.findWriterRuntime(componentName);
      if (runtime != null) {
        stateMachine.disposeSubtree(runtime);
        group.removeWriterRuntime(runtime);
        unregisterTree(runtime);
        runtime.dispose();
      }
    } else {
      ReaderGroupRuntime group = connection.findReaderGroupRuntime(groupName);
      if (group == null) {
        return;
      }
      DataSetReaderRuntime runtime = group.findReaderRuntime(componentName);
      if (runtime != null) {
        stateMachine.disposeSubtree(runtime);
        group.removeReaderRuntime(runtime);
        unregisterTree(runtime);
        runtime.dispose();
        connection.refreshSubscriberMappings();
      }
    }
  }

  // endregion

  // region data plumbing

  @Override
  public void bindSource(PublishedDataSetRef dataSet, PublishedDataSetSource source) {
    requireNonNull(dataSet, "dataSet");
    requireNonNull(source, "source");

    sources.put(dataSet, source);
  }

  @Override
  public void addDataSetListener(DataSetListener listener) {
    eventDispatcher.addDataSetListener(requireNonNull(listener, "listener"));
  }

  @Override
  public void addDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
    requireNonNull(reader, "reader");
    eventDispatcher.addDataSetListener(reader, requireNonNull(listener, "listener"));
  }

  @Override
  public void addStateListener(PubSubStateListener listener) {
    eventDispatcher.addStateListener(requireNonNull(listener, "listener"));
  }

  @Override
  public void addMetaDataListener(MetaDataListener listener) {
    eventDispatcher.addMetaDataListener(requireNonNull(listener, "listener"));
  }

  @Override
  public void addDiagnosticsListener(PubSubDiagnosticsListener listener) {
    eventDispatcher.addDiagnosticsListener(requireNonNull(listener, "listener"));
  }

  @Override
  public PubSubDiagnostics diagnostics() {
    return diagnostics;
  }

  // endregion

  // region engine internals

  private void onStateChange(
      AbstractComponentRuntime component,
      PubSubState oldState,
      PubSubState newState,
      StatusCode statusCode) {

    eventDispatcher.notifyStateChange(
        new PubSubStateChangeEvent(component.handle(), oldState, newState, statusCode));
  }

  private void registerTree(AbstractComponentRuntime component) {
    registry.register(component);
    diagnostics.register(component.path());
    component.children().forEach(this::registerTree);
  }

  private void unregisterTree(AbstractComponentRuntime component) {
    component.children().forEach(this::unregisterTree);
    registry.unregister(component);
    diagnostics.remove(component.path());
  }

  PubSubConfig getConfig() {
    return config;
  }

  PubSubServiceConfig getServiceConfig() {
    return serviceConfig;
  }

  EncodingContext getEncodingContext() {
    return serviceConfig.getEncodingContext();
  }

  ScheduledExecutorService getScheduledExecutor() {
    return serviceConfig.getScheduledExecutor();
  }

  ExecutorService getTransportExecutor() {
    return serviceConfig.getTransportExecutor();
  }

  DiagnosticsCollector getDiagnostics() {
    return diagnostics;
  }

  EventDispatcher getEventDispatcher() {
    return eventDispatcher;
  }

  MetadataCache getMetadataCache() {
    return metadataCache;
  }

  PubSubStateMachine getStateMachine() {
    return stateMachine;
  }

  ReaderDispatcher getReaderDispatcher() {
    return readerDispatcher;
  }

  /**
   * The built-in UADP mapping, used for the UADP-internal discovery codec regardless of any
   * user-configured "uadp" mapping override (discovery is not part of the mapping SPI).
   */
  UadpMessageMapping getUadpMapping() {
    return builtinUadpMapping;
  }

  /**
   * Get the next discovery announcement sequence number for {@code publisherId}: a UInt16 counter
   * starting at 0 and wrapping after 0xFFFF, scoped per PublisherId across all connections (Part 14
   * §7.2.4.6.3 Table 168) and surviving connection restarts for the lifetime of this service.
   */
  UShort nextAnnouncementSequenceNumber(PublisherId publisherId) {
    AtomicInteger counter =
        announcementSequenceNumbers.computeIfAbsent(publisherId, id -> new AtomicInteger());
    return ushort(counter.getAndUpdate(value -> (value + 1) & 0xFFFF));
  }

  /**
   * The built-in UDP transport provider, used to open the per-connection discovery channels against
   * the synthetic discovery {@code UdpConnectionConfig} (zero transport-SPI change).
   */
  UdpTransportProvider getUdpTransportProvider() {
    return builtinUdpTransport;
  }

  @Nullable PublishedDataSetSource getSource(PublishedDataSetRef ref) {
    return sources.get(ref);
  }

  PublishedDataSetConfig requirePublishedDataSet(PublishedDataSetRef ref) {
    return config
        .publishedDataSet(ref.name())
        .orElseThrow(() -> new IllegalStateException("PublishedDataSet not found: " + ref.name()));
  }

  /**
   * Resolve the transport provider for a connection: the first configured provider that supports
   * it, falling back to the built-in UDP provider.
   */
  @Nullable TransportProvider resolveTransportProvider(PubSubConnectionConfig connection) {
    for (TransportProvider provider : serviceConfig.getTransportProviders()) {
      if (provider.supports(connection)) {
        return provider;
      }
    }
    if (builtinUdpTransport.supports(connection)) {
      return builtinUdpTransport;
    }
    return null;
  }

  /**
   * Resolve a mapping provider by name: configured providers take precedence over the built-in UADP
   * and JSON mappings.
   */
  @Nullable MessageMappingProvider resolveMappingProvider(String mappingName) {
    for (MessageMappingProvider provider : serviceConfig.getMessageMappingProviders()) {
      if (provider.mappingName().equals(mappingName)) {
        return provider;
      }
    }
    if (builtinUadpMapping.mappingName().equals(mappingName)) {
      return builtinUadpMapping;
    }
    if (builtinJsonMapping.mappingName().equals(mappingName)) {
      return builtinJsonMapping;
    }
    return null;
  }

  static String mappingNameOf(WriterGroupMessageSettings settings) {
    if (settings instanceof UadpWriterGroupSettings) {
      return MAPPING_UADP;
    } else if (settings instanceof JsonWriterGroupSettings) {
      return MAPPING_JSON;
    } else {
      return settings.getClass().getSimpleName();
    }
  }

  static String mappingNameOf(DataSetWriterMessageSettings settings) {
    if (settings instanceof UadpDataSetWriterSettings) {
      return MAPPING_UADP;
    } else if (settings instanceof JsonDataSetWriterSettings) {
      return MAPPING_JSON;
    } else {
      return settings.getClass().getSimpleName();
    }
  }

  static String mappingNameOf(DataSetReaderMessageSettings settings) {
    if (settings instanceof UadpDataSetReaderSettings) {
      return MAPPING_UADP;
    } else if (settings instanceof JsonDataSetReaderSettings) {
      return MAPPING_JSON;
    } else {
      return settings.getClass().getSimpleName();
    }
  }

  // endregion

  // region validation

  private static void validateBindings(PubSubConfig config, PubSubBindings bindings) {
    for (PublishedDataSetRef ref : bindings.getSources().keySet()) {
      if (config.publishedDataSet(ref.name()).isEmpty()) {
        throw new IllegalArgumentException(
            "source bound to unknown PublishedDataSet '%s'".formatted(ref.name()));
      }
    }

    for (DataSetReaderRef ref : bindings.getListeners().keySet()) {
      if (!readerExists(config, ref)) {
        throw new IllegalArgumentException(
            "listener bound to unknown DataSetReader '%s/%s/%s'"
                .formatted(ref.connectionName(), ref.readerGroupName(), ref.readerName()));
      }
    }

    for (SecurityGroupRef ref : bindings.getSecurityKeyProviders().keySet()) {
      boolean exists =
          config.securityGroups().stream().anyMatch(g -> g.getName().equals(ref.name()));
      if (!exists) {
        throw new IllegalArgumentException(
            "security keys bound to unknown SecurityGroup '%s'".formatted(ref.name()));
      }
    }
  }

  private static boolean readerExists(PubSubConfig config, DataSetReaderRef ref) {
    return config
        .connection(ref.connectionName())
        .map(
            connection ->
                connection.readerGroups().stream()
                    .filter(g -> g.getName().equals(ref.readerGroupName()))
                    .flatMap(g -> g.getDataSetReaders().stream())
                    .anyMatch(r -> r.getName().equals(ref.readerName())))
        .orElse(false);
  }

  /**
   * Validate the conditions pinned to fail {@code startup()}: unsupported security modes on enabled
   * groups, missing transport or mapping providers for enabled components, missing sources for
   * enabled writers, publisher-less connections with enabled writer groups, enabled readers on
   * broker connections without a configured data queueName, and enabled writer groups on UDP
   * connections with a maxNetworkMessageSize above the Part 14 §7.3.2.1 limit of 65535.
   */
  private void validateStartup(PubSubConfig config) throws UaException {
    if (!config.isEnabled()) {
      return;
    }

    for (PubSubConnectionConfig connection : config.connections()) {
      if (!connection.enabled()) {
        continue;
      }

      boolean broker = !(connection instanceof UdpConnectionConfig);

      if (resolveTransportProvider(connection) == null) {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "no TransportProvider supports connection '%s'".formatted(connection.name()));
      }

      boolean anyEnabledWriterGroup = false;

      for (WriterGroupConfig group : connection.writerGroups()) {
        if (!group.isEnabled()) {
          continue;
        }
        anyEnabledWriterGroup = true;

        String groupPath = connection.name() + "/" + group.getName();

        checkMessageSecurity(group.getMessageSecurity(), groupPath);

        if (!broker
            && group.getMaxNetworkMessageSize().longValue() > MAX_UDP_NETWORK_MESSAGE_SIZE) {
          throw new UaException(
              StatusCodes.Bad_ConfigurationError,
              "maxNetworkMessageSize %s exceeds the OPC UA UDP limit of %d (writer group '%s')"
                  .formatted(
                      group.getMaxNetworkMessageSize(), MAX_UDP_NETWORK_MESSAGE_SIZE, groupPath));
        }

        String mappingName = mappingNameOf(group.getMessageSettings());
        if (resolveMappingProvider(mappingName) == null) {
          throw new UaException(
              StatusCodes.Bad_ConfigurationError,
              "no MessageMappingProvider for mapping '%s' (writer group '%s')"
                  .formatted(mappingName, groupPath));
        }

        for (DataSetWriterConfig writer : group.getDataSetWriters()) {
          if (!writer.isEnabled()) {
            continue;
          }
          if (!sources.containsKey(writer.getDataSet())) {
            throw new UaException(
                StatusCodes.Bad_ConfigurationError,
                "no PublishedDataSetSource bound for PublishedDataSet '%s' (dataset writer"
                        .formatted(writer.getDataSet().name())
                    + " '%s/%s')".formatted(groupPath, writer.getName()));
          }
        }
      }

      if (anyEnabledWriterGroup && connection.publisherId() == null) {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "connection '%s' has enabled writer groups but no PublisherId"
                .formatted(connection.name()));
      }

      for (ReaderGroupConfig group : connection.readerGroups()) {
        if (!group.isEnabled()) {
          continue;
        }

        String groupPath = connection.name() + "/" + group.getName();

        checkMessageSecurity(group.getMessageSecurity(), groupPath);

        for (DataSetReaderConfig reader : group.getDataSetReaders()) {
          if (!reader.isEnabled()) {
            continue;
          }
          String mappingName = mappingNameOf(reader.getSettings());
          if (resolveMappingProvider(mappingName) == null) {
            throw new UaException(
                StatusCodes.Bad_ConfigurationError,
                "no MessageMappingProvider for mapping '%s' (dataset reader '%s/%s')"
                    .formatted(mappingName, groupPath, reader.getName()));
          }

          // a broker reader subscribes its configured data queue; the Part 14 §7.3.4.7.3 topic
          // derivation needs the publisher's WriterGroup name, which the reader does not know
          if (broker && !hasDataQueueName(reader)) {
            throw new UaException(
                StatusCodes.Bad_ConfigurationError,
                "dataset reader '%s/%s' on broker connection '%s' requires a data queueName"
                    .formatted(groupPath, reader.getName(), connection.name()));
          }
        }
      }
    }
  }

  /**
   * Validate the subset of the {@link #validateStartup} conditions that reconfiguration must also
   * enforce, because the runtime cannot degrade gracefully on them: missing mapping providers for
   * enabled components, enabled readers on broker connections without a configured data queueName,
   * and enabled writer groups on UDP connections with a maxNetworkMessageSize above the Part 14
   * §7.3.2.1 limit of 65535. Throws {@link UaRuntimeException} with {@code Bad_ConfigurationError}
   * (the reconfigure API has no checked-exception surface). Startup-only conditions with a graceful
   * runtime degradation path (e.g. unbound sources, which surface as source errors) are not
   * re-checked here.
   */
  private void validateReconfigure(PubSubConfig config) {
    if (!config.isEnabled()) {
      return;
    }

    for (PubSubConnectionConfig connection : config.connections()) {
      if (!connection.enabled()) {
        continue;
      }

      boolean broker = !(connection instanceof UdpConnectionConfig);

      for (WriterGroupConfig group : connection.writerGroups()) {
        if (!group.isEnabled()) {
          continue;
        }
        if (!broker
            && group.getMaxNetworkMessageSize().longValue() > MAX_UDP_NETWORK_MESSAGE_SIZE) {
          throw new UaRuntimeException(
              StatusCodes.Bad_ConfigurationError,
              "maxNetworkMessageSize %s exceeds the OPC UA UDP limit of %d (writer group '%s/%s')"
                  .formatted(
                      group.getMaxNetworkMessageSize(),
                      MAX_UDP_NETWORK_MESSAGE_SIZE,
                      connection.name(),
                      group.getName()));
        }
        String mappingName = mappingNameOf(group.getMessageSettings());
        if (resolveMappingProvider(mappingName) == null) {
          throw new UaRuntimeException(
              StatusCodes.Bad_ConfigurationError,
              "no MessageMappingProvider for mapping '%s' (writer group '%s/%s')"
                  .formatted(mappingName, connection.name(), group.getName()));
        }
      }

      for (ReaderGroupConfig group : connection.readerGroups()) {
        if (!group.isEnabled()) {
          continue;
        }

        String groupPath = connection.name() + "/" + group.getName();

        for (DataSetReaderConfig reader : group.getDataSetReaders()) {
          if (!reader.isEnabled()) {
            continue;
          }
          String mappingName = mappingNameOf(reader.getSettings());
          if (resolveMappingProvider(mappingName) == null) {
            throw new UaRuntimeException(
                StatusCodes.Bad_ConfigurationError,
                "no MessageMappingProvider for mapping '%s' (dataset reader '%s/%s')"
                    .formatted(mappingName, groupPath, reader.getName()));
          }
          if (broker && !hasDataQueueName(reader)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_ConfigurationError,
                "dataset reader '%s/%s' on broker connection '%s' requires a data queueName"
                    .formatted(groupPath, reader.getName(), connection.name()));
          }
        }
      }
    }
  }

  private static boolean hasDataQueueName(DataSetReaderConfig reader) {
    BrokerTransportSettings settings = reader.getBrokerTransport();
    return settings != null
        && settings.getQueueName() != null
        && !settings.getQueueName().isEmpty();
  }

  private static void checkMessageSecurity(@Nullable MessageSecurityConfig security, String path)
      throws UaException {

    if (security != null && security.getMode() != MessageSecurityMode.None) {
      throw new UaException(
          StatusCodes.Bad_NotSupported,
          "MessageSecurityMode %s is not supported (group '%s'); only None is supported in"
                  .formatted(security.getMode(), path)
              + " this version");
    }
  }

  // endregion
}
