/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Attaches an OPC UA Part 14 PubSub runtime to an {@link OpcUaServer}, integrating it with the
 * server's address space.
 *
 * <p>Behavior on {@link #attach}:
 *
 * <ul>
 *   <li>PublishedDataSets whose fields are all addressed by {@link NodeFieldAddress} are
 *       automatically bound to an address-space-backed source that pulls a snapshot at publish
 *       time; a caller-supplied source in {@link ServerPubSubOptions#getBindings()} wins over the
 *       automatic binding for the same dataset. Datasets with mixed or key-only field addresses are
 *       not auto-bound.
 *   <li>DataSetReaders whose SubscribedDataSet is a {@link TargetVariablesConfig} automatically
 *       write received fields to the mapped address-space variables, per Part 14 §6.2.11.1 Table
 *       80. Target variables must allow {@code AccessLevel.CurrentWrite}: the server enforces the
 *       AccessLevel even for internal writes, and writes to non-writable targets fail and are
 *       counted in {@link #targetWriteErrors()}. Readers whose SubscribedDataSet is a {@link
 *       StandaloneSubscribedDataSetRef} do <em>not</em> get automatic TargetVariables writes in
 *       this version, even when the referenced standalone dataset carries TargetVariables (a
 *       warning is logged at attach); their targets and index ranges are still validated.
 *   <li>Every {@link NodeFieldAddress} in the configuration (published dataset sources and
 *       TargetVariables targets) is eagerly resolved against the server's {@link NamespaceTable};
 *       an unresolvable namespace URI fails attach with {@link PubSubConfigValidationException}.
 *       Node <em>existence</em> is not checked until publish or write time: a missing source node
 *       publishes a {@code Bad_NodeIdUnknown} value, and a missing target node counts a write
 *       error.
 *   <li>If a {@link PubSubConfigurationStore} is configured and {@link
 *       PubSubConfigurationStore#load()} returns a stored configuration, the stored configuration
 *       wins and the attach configuration is ignored; otherwise the attach configuration is used
 *       and saved once. Every later successful configuration apply through {@link #runtime()} —
 *       including remote {@code CloseAndUpdate} edits — is saved too; a save failure is WARN-logged
 *       and surfaced via {@link #lastConfigurationSaveError()}.
 * </ul>
 *
 * <p>Attach timing: {@code attach} is legal any time after {@link OpcUaServer} construction — the
 * server's address space, including the ns0 PublishSubscribe subtree, is loaded by the constructor.
 * {@link #startup()} does not require {@code server.startup()} and ignores endpoint state entirely;
 * PubSub operates independently of the server's client-facing transports. One exception: with
 * {@link ServerPubSubOptions#isStatusEventsEnabled()} the status-event bridge fires events through
 * the server's {@code EventFactory}, whose lifecycle expects {@code server.startup()} first —
 * events emitted before then are dropped with a WARN. The caller owns shutdown ordering: {@code
 * OpcUaServer.shutdown()} does not shut down an attached {@link ServerPubSub}.
 */
public final class ServerPubSub implements AutoCloseable {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerPubSub.class);

  private final ConcurrentMap<String, AtomicLong> writeErrorCounters = new ConcurrentHashMap<>();

  /**
   * Guards the lifecycle state of the optional server-side integrations: one state machine per
   * integration, so a {@code close()} racing {@code startup()} marks it {@code STOPPED} before
   * startup can register it. Leaf lock: startup/shutdown is synchronous node-manager work and never
   * touches the engine lock.
   */
  private final Object lifecycleLock = new Object();

  /** Guarded by {@link #lifecycleLock}. */
  private FaceState fragmentState = FaceState.NEW;

  /** Guarded by {@link #lifecycleLock}. */
  private FaceState sksFaceState = FaceState.NEW;

  /** Guarded by {@link #lifecycleLock}. */
  private FaceState configurationFaceState = FaceState.NEW;

  /** Guarded by {@link #lifecycleLock}. */
  private FaceState statusEventBridgeState = FaceState.NEW;

  /** Guarded by {@link #lifecycleLock}. */
  private FaceState eventNotifierBindingState = FaceState.NEW;

  /** The raw engine service; internal use only — API callers get {@link #managedService}. */
  private final PubSubService service;

  private final OpcUaServer server;
  private final ManagedPubSubService managedService;
  private final PubSubMethodAuthorizer methodAuthorizer;
  private final @Nullable PubSubInfoModelFragment fragment;
  private final @Nullable SksServerFace sksServerFace;
  private final @Nullable PubSubConfigurationFace configurationFace;
  private final @Nullable PubSubConfigurationStore configurationStore;
  private final @Nullable PubSubStatusEventBridge statusEventBridge;

  /**
   * Bridges the server's event bus into the PubSub runtime, publishing configured {@code
   * PublishedEvents} datasets. Constructed unconditionally (its binding map is empty when no event
   * datasets are configured); registered as a server event listener from {@link #startup()}.
   */
  private final PubSubEventNotifierBinding eventNotifierBinding;

  /**
   * The SecurityGroups of the current configuration, by SecurityGroupId; the default authorizer's
   * {@code checkKeyAccess} group lookup reads this so remote-edited groups govern. Swapped per
   * apply by {@link #onMediatorApplied}.
   */
  private volatile Map<String, SecurityGroupConfig> securityGroupsById;

  /**
   * The last {@link PubSubConfigurationStore#save} failure, cleared on the next successful save see
   * {@link #lastConfigurationSaveError()}.
   */
  private volatile @Nullable Exception lastConfigurationSaveError;

  /** The shared address-space-backed source used for the automatic dataset bindings. */
  private final AddressSpacePublishedDataSetSource addressSpaceSource;

  /**
   * The dataset refs whose sources were supplied by the caller — at attach via {@link
   * ServerPubSubOptions#getBindings()} or later via the managed runtime's {@code bindSource} —
   * which the automatic re-derivation must never override.
   */
  private final Set<PublishedDataSetRef> userBoundSources = ConcurrentHashMap.newKeySet();

  /**
   * The automatic TargetVariables writers, keyed by reader path; re-derived per reconfiguration and
   * deactivated at shutdown.
   */
  private final ConcurrentMap<String, TargetVariablesWriter> writers = new ConcurrentHashMap<>();

  /**
   * Serializes {@link #shutdown()}'s deactivation pass with the hook-driven writer re-derivation
   * (registering a new writer in {@link #writers}), so a reconfigure racing shutdown can never
   * register — and later seed — a writer the deactivation pass did not cover; that would issue
   * TargetVariables writes after the shutdown future resolved, breaking the {@link #shutdown()}
   * contract. Ordering discipline (NOT a leaf lock): {@code writersLock} &rarr; writer monitor is
   * permitted — the shutdown pass calls the synchronized {@code TargetVariablesWriter.deactivate()}
   * under this lock and may transitively wait for an in-flight address-space write to finish — but
   * the reverse order is forbidden: nothing may acquire {@code writersLock} while holding a writer
   * monitor ({@code TargetVariablesWriter} never touches this lock, and the re-derivation
   * deactivates a replaced writer <em>before</em> taking it).
   */
  private final Object writersLock = new Object();

  /** Set by {@link #shutdown()}'s deactivation pass; guarded by {@link #writersLock}. */
  private boolean writersShutdown = false;

  /** The configuration the bindings re-derivation last observed; swapped per apply. */
  private volatile PubSubConfig currentConfig;

  private ServerPubSub(
      OpcUaServer server,
      PubSubConfig config,
      ServerPubSubOptions options,
      UInteger initialConfigurationVersion) {

    this.server = server;
    this.currentConfig = config;
    this.addressSpaceSource = new AddressSpacePublishedDataSetSource(server);

    userBoundSources.addAll(options.getBindings().getSources().keySet());

    PubSubBindings bindings = buildBindings(server, config, options, writers);

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .encodingContext(server.getStaticEncodingContext())
            .scheduledExecutor(server.getScheduledExecutorService())
            .transportExecutor(server.getExecutorService())
            .build();

    this.service = PubSubService.create(config, bindings, serviceConfig);

    // registered unconditionally: readers with TargetVariables may be added by reconfiguration
    // even when the attach-time configuration has none; the listener looks up by path per event
    service.addStateListener(
        event -> {
          if (event.component().componentType() == ComponentType.DATA_SET_READER) {
            TargetVariablesWriter writer = writers.get(event.component().path());
            if (writer != null) {
              writer.onStateChange(event.newState());
            }
          }
        });

    // one effective authorizer per attachment: the options-configured instance, else the
    // shared default; every PubSub Method handler (the SKS helper, the fragment's Enable/Disable
    // handlers, the remote-configuration helper, and the diagnostics Reset handlers) consults
    // this same instance
    this.securityGroupsById = securityGroupsById(config);

    PubSubMethodAuthorizer configuredAuthorizer = options.getMethodAuthorizer();
    if (configuredAuthorizer != null) {
      this.methodAuthorizer = configuredAuthorizer;
    } else {
      // the lookup reads the volatile current-config index, not an attach-time snapshot, so
      // checkKeyAccess decisions see remotely edited SecurityGroups
      this.methodAuthorizer = new DefaultPubSubMethodAuthorizer(id -> securityGroupsById.get(id));
    }

    this.fragment =
        options.isExposeInformationModel()
            ? new PubSubInfoModelFragment(
                server,
                config,
                service,
                options,
                methodAuthorizer,
                // deferred read of the mediator-owned ConfigurationVersion: managedService
                // is assigned below, before this supplier can ever be invoked (fragment startup)
                this::currentConfigurationVersion)
            : null;

    this.sksServerFace =
        options.isSecurityKeyServerEnabled()
            ? new SksServerFace(server, config, methodAuthorizer)
            : null;

    // the remote-configuration file helper: all remote mutations are applied through the
    // mediator, never the raw service; the mediator-dependent collaborators are supplied
    // deferred (managedService is assigned below, before any handler can run)
    this.configurationStore = options.getConfigurationStore();
    this.configurationFace =
        options.isAllowRemoteConfiguration()
            ? new PubSubConfigurationFace(
                server,
                config,
                // seeds the helper's atomically-swapped (config, version) pair with the same
                // value the mediator is seeded with below
                initialConfigurationVersion,
                methodAuthorizer,
                fragment,
                this::currentConfigurationVersion,
                this::managedService)
            : null;

    // the status-event bridge: engine state changes and send failures become OPC UA events
    // fired with Server-object semantics; independent of the exposed information model
    this.statusEventBridge =
        options.isStatusEventsEnabled() ? new PubSubStatusEventBridge(server, service) : null;

    // the event-notifier binding: server events matching a PublishedEvents dataset's filter are
    // pushed to the runtime. Constructed unconditionally — its binding map is empty (and its
    // listener a no-op) until an event dataset is configured
    this.eventNotifierBinding = new PubSubEventNotifierBinding(server, service);

    // Post-apply hooks, in fixed registration order; registered before the mediator is
    // constructed because the hook list is fixed at construction:
    //   1. fragment rebuild         — PubSubInfoModelFragment.applyReconfigure keeps the exposed
    //      model in sync with the engine
    //   2. bindings re-derivation   — TargetVariables writers + automatic dataset sources
    //      after configuration changes
    //   3. event-notifier rebuild   — PubSubEventNotifierBinding re-derives its PublishedEvents
    //      bindings from the applied config (right after bindings re-derivation)
    //   4. SKS key-store refresh    — SksServerFace.onConfigurationApplied
    //   5. remote file refresh + persistence; registered LAST so a
    //      save failure never blocks the key refresh or the model rebuild
    var hooks = new ArrayList<ManagedPubSubService.ReconfigureHook>();

    if (fragment != null) {
      hooks.add(fragment::applyReconfigure);
    }
    hooks.add(this::rederiveBindings);
    hooks.add(eventNotifierBinding::onConfigurationApplied);
    if (sksServerFace != null) {
      hooks.add(sksServerFace::onConfigurationApplied);
    }
    hooks.add(this::onMediatorApplied);

    this.managedService =
        new ManagedPubSubService(
            service,
            server.getNamespaceTable(),
            hooks,
            initialConfigurationVersion,
            userBoundSources::add);
  }

  /**
   * Attach a PubSub runtime configured by {@code config} to {@code server}, using default {@link
   * ServerPubSubOptions}.
   *
   * @param server the server to attach to.
   * @param config the PubSub configuration.
   * @return a new {@link ServerPubSub}, not yet started.
   * @throws PubSubConfigValidationException if a {@link NodeFieldAddress} in the configuration
   *     cannot be resolved against the server's {@link NamespaceTable}, or a TargetVariables index
   *     range cannot be parsed.
   */
  public static ServerPubSub attach(OpcUaServer server, PubSubConfig config) {
    return attach(server, config, ServerPubSubOptions.builder().build());
  }

  /**
   * Attach a PubSub runtime configured by {@code config} to {@code server}.
   *
   * <p>Readers whose SubscribedDataSet is a {@link StandaloneSubscribedDataSetRef} do not get
   * automatic TargetVariables writes in this version; the referenced targets are validated but not
   * written.
   *
   * @param server the server to attach to.
   * @param config the PubSub configuration, used unless a configured {@link
   *     PubSubConfigurationStore} supplies a stored configuration.
   * @param options the {@link ServerPubSubOptions} governing the attachment.
   * @return a new {@link ServerPubSub}, not yet started.
   * @throws PubSubConfigValidationException if a {@link NodeFieldAddress} in the effective
   *     configuration cannot be resolved against the server's {@link NamespaceTable}, a
   *     TargetVariables index range cannot be parsed, a stored configuration does not map to a
   *     valid config, or the enabled Security Key Service face rejects a SecurityGroup (an
   *     unsupported SecurityPolicy URI or a duplicate SecurityGroupId).
   */
  public static ServerPubSub attach(
      OpcUaServer server, PubSubConfig config, ServerPubSubOptions options) {

    if (options.isDiagnosticsEnabled() && !options.isExposeInformationModel()) {
      // documented no-op, never a throw: the §9.1.11 diagnostics exposure lives in the
      // information model fragment, which only exists when the model is exposed
      LOGGER.warn(
          "diagnosticsEnabled has no effect without exposeInformationModel;"
              + " no diagnostics will be exposed");
    }

    NamespaceTable namespaceTable = server.getNamespaceTable();

    PubSubConfig effectiveConfig = config;
    boolean loaded = false;
    UInteger storedVersion = null;

    PubSubConfigurationStore store = options.getConfigurationStore();
    if (store != null) {
      PubSubConfiguration2DataType stored = store.load();
      if (stored != null) {
        effectiveConfig = PubSubConfig.fromDataType(stored, namespaceTable);
        loaded = true;
        storedVersion = stored.getConfigurationVersion();
      }
    }

    validateNodeFieldAddresses(effectiveConfig, namespaceTable);

    // seed the ConfigurationVersion single source: a non-zero store-loaded version is
    // authoritative, otherwise the attach instant
    UInteger initialConfigurationVersion =
        storedVersion != null && storedVersion.longValue() != 0L
            ? storedVersion
            : ManagedPubSubService.versionTimeNow();

    var serverPubSub =
        new ServerPubSub(server, effectiveConfig, options, initialConfigurationVersion);

    if (store != null && !loaded) {
      // the attach-time save carries the seeded version too: the uint(0) mapper placeholder is
      // retired from every observable surface
      serverPubSub.persistWireConfiguration(
          PubSubConfigurationFace.withConfigurationVersion(
              effectiveConfig.toDataType(namespaceTable), initialConfigurationVersion));
    }

    return serverPubSub;
  }

  /**
   * Start the attached PubSub runtime and, if configured, expose the information model.
   *
   * <p>Legal regardless of the server's own lifecycle state: the server need not be started, and
   * endpoint state is ignored.
   *
   * <p>If exposing the information model fails, the half-built exposure is unregistered before the
   * returned future completes exceptionally. If the runtime itself fails to start after the
   * information model was exposed, the exposed model (reflecting the attach-time configuration)
   * remains registered until {@link #shutdown()} or {@link #close()} is called; always close a
   * {@link ServerPubSub} whose startup failed.
   *
   * @return a {@link CompletableFuture} that completes with this {@link ServerPubSub} once startup
   *     has finished, or completes exceptionally if startup fails.
   */
  public CompletableFuture<ServerPubSub> startup() {
    synchronized (lifecycleLock) {
      // optional components start only from NEW: STOPPED means close won the close-vs-startup race
      // (close ran first and marked it STOPPED) and must never register
      if (fragment != null && fragmentState == FaceState.NEW) {
        try {
          fragment.startup();
          fragmentState = FaceState.STARTED;
        } catch (Exception e) {
          // unregister the half-built fragment (its lifecycle reached RUNNING before the
          // failure, so shutdown is legal and unregisters cleanly) and surface the failure
          // on the returned future rather than throwing synchronously
          fragmentState = FaceState.STOPPED;
          fragment.shutdown();
          return CompletableFuture.failedFuture(e);
        }
      }

      if (sksServerFace != null && sksFaceState == FaceState.NEW) {
        try {
          sksServerFace.startup();
          sksFaceState = FaceState.STARTED;
        } catch (Exception e) {
          sksFaceState = FaceState.STOPPED;
          sksServerFace.shutdown();
          return CompletableFuture.failedFuture(e);
        }
      }

      if (configurationFace != null && configurationFaceState == FaceState.NEW) {
        try {
          configurationFace.startup();
          configurationFaceState = FaceState.STARTED;
        } catch (Exception e) {
          configurationFaceState = FaceState.STOPPED;
          configurationFace.shutdown();
          return CompletableFuture.failedFuture(e);
        }
      }

      if (statusEventBridge != null && statusEventBridgeState == FaceState.NEW) {
        // registers the engine listeners (removed again at shutdown); event emission
        // additionally expects a STARTED server — the EventFactory lifecycle — and drops
        // events with a WARN otherwise (documented on statusEventsEnabled)
        statusEventBridge.startup();
        statusEventBridgeState = FaceState.STARTED;
      }

      if (eventNotifierBindingState == FaceState.NEW) {
        // builds the initial PublishedEvents bindings from the attach-time config and registers
        // the server event listener; notifier nodes in namespaces registered after this point
        // stay inactive until the next reconfigure (documented on PubSubEventNotifierBinding)
        eventNotifierBinding.startup(currentConfig);
        eventNotifierBindingState = FaceState.STARTED;
      }
    }

    return service.startup().thenApply(s -> this);
  }

  /**
   * Stop the attached PubSub runtime and tear down the information model, if it was exposed.
   *
   * <p>The returned future completes only after the shutdown-induced reader state change events
   * have been delivered and the automatic TargetVariables writers have been deactivated: the Part
   * 14 Table 80 transition-into-Disabled writes land before shutdown resolves, and no further
   * TargetVariables writes are issued (and no further {@link #targetWriteErrors()} entries
   * recorded) afterward, so the server's address space may be safely torn down next.
   *
   * @return a {@link CompletableFuture} that completes once shutdown has finished.
   */
  public CompletableFuture<Void> shutdown() {
    return service
        .shutdown()
        .whenComplete(
            (v, ex) -> {
              // the service shutdown future resolves only after its event queue has drained,
              // so the Table 80 into-Disabled writes have been issued; deactivation joins any
              // write still in flight on the DataSet dispatch path and prevents stragglers
              // from racing a subsequent server or namespace teardown. writersLock + the
              // writersShutdown flag close the race with a concurrent reconfigure's writer
              // re-derivation: a writer registered before this pass is deactivated by it, and
              // once the flag is set no new writer is ever registered or seeded
              synchronized (writersLock) {
                writersShutdown = true;
                writers.values().forEach(TargetVariablesWriter::deactivate);
              }
              // no drain dependency: deregister the event listener and clear its bindings so no
              // further server event is dispatched to the runtime
              shutdownEventNotifierBinding();
              shutdownFragment();
              shutdownSksServerFace();
              shutdownConfigurationFace();
              // after the queue drain: shutdown-induced dispose events have reached the bridge
              // (and were silenced by cause) before its listeners are deregistered
              shutdownStatusEventBridge();
            });
  }

  private void shutdownFragment() {
    if (fragment == null) {
      return;
    }
    synchronized (lifecycleLock) {
      // idempotent: shut down only from STARTED, but always leave STOPPED behind so a
      // close() that runs before startup() prevents the fragment from ever registering
      if (fragmentState == FaceState.STARTED) {
        fragment.shutdown();
      }
      fragmentState = FaceState.STOPPED;
    }
  }

  private void shutdownSksServerFace() {
    if (sksServerFace == null) {
      return;
    }
    synchronized (lifecycleLock) {
      if (sksFaceState == FaceState.STARTED) {
        sksServerFace.shutdown();
      }
      sksFaceState = FaceState.STOPPED;
    }
  }

  private void shutdownConfigurationFace() {
    if (configurationFace == null) {
      return;
    }
    synchronized (lifecycleLock) {
      if (configurationFaceState == FaceState.STARTED) {
        configurationFace.shutdown();
      }
      configurationFaceState = FaceState.STOPPED;
    }
  }

  private void shutdownStatusEventBridge() {
    if (statusEventBridge == null) {
      return;
    }
    synchronized (lifecycleLock) {
      if (statusEventBridgeState == FaceState.STARTED) {
        statusEventBridge.shutdown();
      }
      statusEventBridgeState = FaceState.STOPPED;
    }
  }

  private void shutdownEventNotifierBinding() {
    synchronized (lifecycleLock) {
      // idempotent: shut down only from STARTED, but always leave STOPPED behind so a close()
      // that runs before startup() prevents the listener from ever registering
      if (eventNotifierBindingState == FaceState.STARTED) {
        eventNotifierBinding.shutdown();
      }
      eventNotifierBindingState = FaceState.STOPPED;
    }
  }

  /**
   * Get the managed {@link PubSubService} runtime; the full standalone API remains available.
   *
   * <p>The returned service delegates to the underlying runtime and intercepts {@link
   * PubSubService#reconfigure} and {@link PubSubService#update}: the new configuration is
   * additionally validated against the attach-time rules — every {@link NodeFieldAddress} must
   * resolve against the server's {@link NamespaceTable}, every TargetVariables index range must
   * parse, and the configuration must map to its wire form — throwing {@link
   * PubSubConfigValidationException} before anything is applied. After a successful apply the
   * exposed ConfigurationVersion advances and the registered post-apply hooks run synchronously
   * before the call returns, so concurrent reconfigure/update calls serialize (see {@link
   * ManagedPubSubService}).
   *
   * <p>The hooks keep the attachment in sync with the applied configuration: the exposed
   * information model (when exposed) incrementally rebuilds the affected subtrees, and the
   * automatic bindings are re-derived — affected readers configured with TargetVariables get their
   * writers replaced, and added or changed fully node-addressed datasets are bound to the automatic
   * address-space source unless a caller-supplied source was bound for them (a source bound through
   * this runtime's {@code bindSource} counts as caller-supplied and is never overridden). Removed
   * datasets keep an inert stale source binding. A dataset changed from fully node-addressed to
   * mixed or key-addressed also keeps its earlier automatic source binding (the engine has no
   * unbind), and that binding stays live: the dataset's writers keep publishing address-space
   * snapshots, unlike an identical attach-time configuration, where such a dataset is never
   * auto-bound. Readers referencing standalone subscribed datasets still do not get automatic
   * TargetVariables writes.
   *
   * @return the managed {@link PubSubService}.
   */
  public PubSubService runtime() {
    return managedService;
  }

  /**
   * Get the {@link ConfigurationObjectIds} lookup backed by the exposed information model fragment,
   * used to assemble the {@code CloseAndUpdate} ConfigurationObjects output.
   *
   * <p>Slot policy: the ConfigurationObjects array is length-matched to the update's references;
   * {@link NodeId#NULL_VALUE} fills the slots of Remove references, failed references, and element
   * kinds the fragment never materializes (SecurityGroups, standalone SubscribedDataSets,
   * PushTargets); the EMPTY array is returned only when this accessor returns {@code null}.
   *
   * @return the fragment-backed lookup, or {@code null} when the information model is not exposed.
   */
  @Nullable ConfigurationObjectIds configurationObjectIds() {
    return fragment;
  }

  /**
   * Get the info model fragment, or {@code null} when the information model is not exposed.
   * Package-private for diagnostics exposure wiring and tests — e.g. registering a {@link
   * ComponentNodeListener} before {@link #startup()}.
   */
  @Nullable PubSubInfoModelFragment infoModelFragment() {
    return fragment;
  }

  /**
   * Get the remote-configuration helper, or {@code null} when {@link
   * ServerPubSubOptions#isAllowRemoteConfiguration()} is {@code false}. Package-private for tests.
   */
  @Nullable PubSubConfigurationFace configurationFace() {
    return configurationFace;
  }

  /**
   * Get the status-event bridge, or {@code null} when {@link
   * ServerPubSubOptions#isStatusEventsEnabled()} is {@code false}. Package-private for tests.
   */
  @Nullable PubSubStatusEventBridge statusEventBridge() {
    return statusEventBridge;
  }

  /** Get the PubSub event-notifier binding. Package-private for tests. */
  PubSubEventNotifierBinding eventNotifierBinding() {
    return eventNotifierBinding;
  }

  /**
   * Get the effective {@link PubSubMethodAuthorizer} of this attachment: the options-configured
   * authorizer, else the shared default instance. One instance is consulted by every PubSub Method
   * handler.
   *
   * @return the effective {@link PubSubMethodAuthorizer}.
   */
  PubSubMethodAuthorizer methodAuthorizer() {
    return methodAuthorizer;
  }

  /**
   * The mediator-owned ConfigurationVersion, read deferred by the fragment's ns0 refresh. Never
   * invoked before construction completes (fragment startup and post-apply hooks).
   */
  private UInteger currentConfigurationVersion() {
    return managedService.configurationVersion();
  }

  /**
   * The mediator, read deferred by the remote-configuration helper (assigned in the constructor
   * before any handler can run).
   */
  private ManagedPubSubService managedService() {
    return managedService;
  }

  /**
   * The last post-apply hook: rebind the default authorizer's SecurityGroup index, refresh the
   * remote-configuration helper (its retained config, the file {@code Size}, and {@code
   * LastModifiedTime}), and persist the applied configuration.
   *
   * <p>Persistence contract: with a {@link PubSubConfigurationStore} configured, {@code save} runs
   * after EVERY successful configuration apply through the managed runtime — a remote {@code
   * CloseAndUpdate} with changes applied and an owner {@code runtime()} reconfigure alike.
   * Enable/Disable, ReserveIds, and match-only CloseAndUpdate calls never reach the mediator's
   * apply path, so they never save. The saved wire form carries the mediator-owned
   * ConfigurationVersion. A save failure is isolated: the apply already happened and its results
   * are still reported; the failure is WARN-logged, surfaced via {@link
   * #lastConfigurationSaveError()}, and retried at the next successful apply.
   */
  private void onMediatorApplied(PubSubConfig newConfig, ReconfigureResult result) {
    securityGroupsById = securityGroupsById(newConfig);

    if (configurationFace != null) {
      configurationFace.onConfigurationApplied(newConfig);
    }

    if (configurationStore != null) {
      persistWireConfiguration(
          PubSubConfigurationFace.withConfigurationVersion(
              newConfig.toDataType(server.getNamespaceTable()),
              managedService.configurationVersion()));
    }
  }

  /**
   * Save {@code wire} to the configured store, recording (or clearing) {@link
   * #lastConfigurationSaveError()}; failures are WARN-logged, never thrown.
   */
  private void persistWireConfiguration(PubSubConfiguration2DataType wire) {
    PubSubConfigurationStore store = this.configurationStore;
    if (store == null) {
      return;
    }
    try {
      store.save(wire);
      lastConfigurationSaveError = null;
    } catch (Exception e) {
      lastConfigurationSaveError = e;
      LOGGER.warn("Error saving configuration to the configuration store", e);
    }
  }

  /**
   * Get the last {@link PubSubConfigurationStore#save} failure, or {@code null} if the most recent
   * save succeeded (or none was attempted).
   *
   * <p>A save failure never fails the operation that triggered it — the configuration change was
   * already applied and its results reported; the failed save is WARN-logged, surfaced here, and
   * retried at the next successful configuration apply. Cleared by the next successful save.
   *
   * @return the last save failure, or {@code null}.
   */
  public @Nullable Exception lastConfigurationSaveError() {
    return lastConfigurationSaveError;
  }

  private static Map<String, SecurityGroupConfig> securityGroupsById(PubSubConfig config) {
    var groupsById = new HashMap<String, SecurityGroupConfig>();
    for (SecurityGroupConfig group : config.securityGroups()) {
      groupsById.putIfAbsent(group.getSecurityGroupId(), group);
    }
    return groupsById;
  }

  /**
   * Get a snapshot of the per-target TargetVariables write error counts, keyed by {@code
   * "<reader-path>/<targetNodeId>"}, e.g. {@code "conn/group/reader/ns=2;s=Commands.Speed"}.
   *
   * <p>Targets with no failed writes have no entry.
   *
   * @return an unmodifiable snapshot of the write error counts.
   */
  public Map<String, Long> targetWriteErrors() {
    var snapshot = new LinkedHashMap<String, Long>();
    writeErrorCounters.forEach((key, count) -> snapshot.put(key, count.get()));
    return Collections.unmodifiableMap(snapshot);
  }

  /**
   * Shut down synchronously, waiting up to 10 seconds for {@link #shutdown()} to complete.
   * Idempotent.
   */
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

  /**
   * Build the effective bindings: caller-supplied bindings, plus automatic address-space sources
   * for fully node-addressed datasets the caller did not bind, plus a {@link TargetVariablesWriter}
   * listener per reader configured with TargetVariables.
   *
   * <p>{@code writers} is populated with the created writers, keyed by reader path.
   */
  private PubSubBindings buildBindings(
      OpcUaServer server,
      PubSubConfig config,
      ServerPubSubOptions options,
      Map<String, TargetVariablesWriter> writers) {

    PubSubBindings userBindings = options.getBindings();
    PubSubBindings.Builder bindings = userBindings.toBuilder();

    Map<PublishedDataSetRef, PublishedDataSetSource> userSources = userBindings.getSources();

    for (PublishedDataSetConfig dataSet : config.publishedDataSets()) {
      if (userSources.containsKey(dataSet.ref())) {
        // a caller-supplied source wins; skip the automatic binding
        continue;
      }

      if (isFullyNodeBacked(dataSet)) {
        bindings.source(dataSet.ref(), addressSpaceSource);
      }
    }

    for (PubSubConnectionConfig connection : config.connections()) {
      for (ReaderGroupConfig group : connection.readerGroups()) {
        for (DataSetReaderConfig reader : group.getDataSetReaders()) {
          String path = connection.name() + "/" + group.getName() + "/" + reader.getName();

          if (reader.getSubscribedDataSet() instanceof TargetVariablesConfig targetVariables
              && !targetVariables.getMappings().isEmpty()) {

            var writer =
                new TargetVariablesWriter(server, path, targetVariables, writeErrorCounters);

            writers.put(path, writer);
            bindings.listener(
                new DataSetReaderRef(connection.name(), group.getName(), reader.getName()), writer);
          } else if (reader.getSubscribedDataSet() instanceof StandaloneSubscribedDataSetRef ref
              && refersToTargetVariables(config, ref)) {

            LOGGER.warn(
                "DataSetReader '{}' references StandaloneSubscribedDataSet '{}' carrying"
                    + " TargetVariables; automatic TargetVariables writes are not applied for"
                    + " standalone references in this version",
                path,
                ref.name());
          }
        }
      }
    }

    return bindings.build();
  }

  /**
   * True if {@code ref} resolves to a standalone subscribed dataset carrying a non-empty {@link
   * TargetVariablesConfig}.
   */
  private static boolean refersToTargetVariables(
      PubSubConfig config, StandaloneSubscribedDataSetRef ref) {

    return config.standaloneSubscribedDataSets().stream()
        .filter(dataSet -> dataSet.getName().equals(ref.name()))
        .anyMatch(
            dataSet ->
                dataSet.getSubscribedDataSet() instanceof TargetVariablesConfig targetVariables
                    && !targetVariables.getMappings().isEmpty());
  }

  /** True if the dataset has at least one field and every field is node-addressed. */
  private static boolean isFullyNodeBacked(PublishedDataSetConfig dataSet) {
    List<FieldDefinition> fields = dataSet.getFields();
    return !fields.isEmpty()
        && fields.stream().allMatch(field -> field.getSource() instanceof NodeFieldAddress);
  }

  // region bindings re-derivation (post-apply hook #2)

  /**
   * Re-derive the automatic bindings after a successful apply, so remotely added or restarted
   * components behave like attach-time ones: the {@link TargetVariablesWriter} of every affected
   * reader is replaced (removed, or re-created against the new configuration and re-seeded with the
   * component's current state), and added or changed fully-node-backed datasets get the automatic
   * address-space source unless the caller bound one. Removed datasets keep their stale source
   * binding — the engine has no unbind API — which is inert. A dataset changed from fully
   * node-backed to mixed or key-addressed keeps its stale automatic binding too, and that one stays
   * live (its writers keep pulling address-space snapshots), diverging from the attach-time
   * semantics under which such a dataset is never auto-bound; fixing it needs an engine unbind seam
   * (documented divergence, see {@code runtime()}).
   *
   * <p>Runs as the second {@link ManagedPubSubService.ReconfigureHook}, after the fragment rebuild.
   * Uses the raw service on purpose: hook-created bindings are automatic, not user-supplied, so
   * they must not be recorded by the mediator's bind observer.
   */
  private void rederiveBindings(PubSubConfig newConfig, ReconfigureResult result) {
    PubSubConfig oldConfig = this.currentConfig;

    // readers affected by the apply: named directly in the typed changes, plus every reader
    // under an added/removed/restarted connection or reader group, expanded from name
    // components against both the old config (stale writers) and the new one (new writers)
    var affectedReaders = new LinkedHashMap<String, DataSetReaderRef>();

    for (ReconfigureResult.Change change : result.changes()) {
      String connectionName = change.connectionName();

      switch (change.scope()) {
        case DATA_SET_READER -> {
          String groupName = change.groupName();
          String readerName = change.componentName();
          if (connectionName != null && groupName != null && readerName != null) {
            var ref = new DataSetReaderRef(connectionName, groupName, readerName);
            affectedReaders.putIfAbsent(readerPath(ref), ref);
          }
        }
        case READER_GROUP -> {
          if (connectionName != null) {
            collectReaders(oldConfig, connectionName, change.groupName(), affectedReaders);
            collectReaders(newConfig, connectionName, change.groupName(), affectedReaders);
          }
        }
        case CONNECTION -> {
          if (connectionName != null) {
            collectReaders(oldConfig, connectionName, null, affectedReaders);
            collectReaders(newConfig, connectionName, null, affectedReaders);
          }
        }
        default -> {}
      }
    }

    affectedReaders.forEach((path, ref) -> rederiveWriter(newConfig, path, ref));

    rederiveAutoSources(newConfig, result);

    this.currentConfig = newConfig;
  }

  /** Collect the reader refs of {@code connectionName} (all groups, or just {@code groupName}). */
  private static void collectReaders(
      PubSubConfig config,
      String connectionName,
      @Nullable String groupName,
      Map<String, DataSetReaderRef> collected) {

    config
        .connection(connectionName)
        .ifPresent(
            connection -> {
              for (ReaderGroupConfig group : connection.readerGroups()) {
                if (groupName != null && !group.getName().equals(groupName)) {
                  continue;
                }
                for (DataSetReaderConfig reader : group.getDataSetReaders()) {
                  var ref = new DataSetReaderRef(connectionName, group.getName(), reader.getName());
                  collected.putIfAbsent(readerPath(ref), ref);
                }
              }
            });
  }

  private static String readerPath(DataSetReaderRef ref) {
    return ref.connectionName() + "/" + ref.readerGroupName() + "/" + ref.readerName();
  }

  /**
   * Replace the automatic TargetVariables writer of one affected reader: deactivate and detach the
   * old writer, and when the new configuration gives the reader a non-empty {@link
   * TargetVariablesConfig}, create, register, and state-seed a new one. Registration is serialized
   * with {@link #shutdown()}'s deactivation pass ({@link #writersLock}): after the pass has run, no
   * new writer is registered or seeded — a resurrected writer would issue TargetVariables writes
   * after the shutdown future resolved — and a writer registered before the pass is deactivated by
   * it, making the pending seed a no-op.
   */
  private void rederiveWriter(PubSubConfig newConfig, String path, DataSetReaderRef ref) {
    TargetVariablesWriter oldWriter = writers.remove(path);
    if (oldWriter != null) {
      oldWriter.deactivate();
      service.removeDataSetListener(ref, oldWriter);
    }

    DataSetReaderConfig reader = findReader(newConfig, ref);
    if (reader == null) {
      return;
    }

    if (reader.getSubscribedDataSet() instanceof TargetVariablesConfig targetVariables
        && !targetVariables.getMappings().isEmpty()) {

      var writer = new TargetVariablesWriter(server, path, targetVariables, writeErrorCounters);

      synchronized (writersLock) {
        if (writersShutdown) {
          LOGGER.debug("skipping writer re-derivation for '{}': shutdown deactivation ran", path);
          return;
        }
        writers.put(path, writer);
      }

      service.addDataSetListener(ref, writer);

      try {
        service
            .components()
            .dataSetReader(ref.connectionName(), ref.readerGroupName(), ref.readerName())
            .ifPresent(handle -> writer.onStateChange(service.state(handle)));
      } catch (IllegalArgumentException e) {
        // reconfigured concurrently; the unconditional state listener seeds the writer on the
        // reader's next transition
      }
    } else if (reader.getSubscribedDataSet() instanceof StandaloneSubscribedDataSetRef sdsRef
        && refersToTargetVariables(newConfig, sdsRef)) {

      LOGGER.warn(
          "DataSetReader '{}' references StandaloneSubscribedDataSet '{}' carrying"
              + " TargetVariables; automatic TargetVariables writes are not applied for"
              + " standalone references in this version",
          path,
          sdsRef.name());
    }
  }

  /**
   * Bind the automatic address-space source to added or changed fully-node-backed datasets the
   * caller has not bound a source for.
   */
  private void rederiveAutoSources(PubSubConfig newConfig, ReconfigureResult result) {
    for (ReconfigureResult.Change change : result.changes()) {
      if (change.scope() != ReconfigureResult.Scope.PUBLISHED_DATA_SET
          || change.kind() == ReconfigureResult.Kind.REMOVED) {
        continue;
      }

      newConfig.publishedDataSets().stream()
          .filter(dataSet -> dataSet.getName().equals(change.path()))
          .findFirst()
          .ifPresent(
              dataSet -> {
                if (isFullyNodeBacked(dataSet) && !userBoundSources.contains(dataSet.ref())) {
                  service.bindSource(dataSet.ref(), addressSpaceSource);
                }
              });
    }
  }

  private static @Nullable DataSetReaderConfig findReader(
      PubSubConfig config, DataSetReaderRef ref) {

    return config
        .connection(ref.connectionName())
        .flatMap(
            connection ->
                connection.readerGroups().stream()
                    .filter(group -> group.getName().equals(ref.readerGroupName()))
                    .flatMap(group -> group.getDataSetReaders().stream())
                    .filter(reader -> reader.getName().equals(ref.readerName()))
                    .findFirst())
        .orElse(null);
  }

  // endregion

  /**
   * Eagerly resolve every {@link NodeFieldAddress} in {@code config} — published dataset field
   * sources and TargetVariables targets, including those of standalone subscribed datasets —
   * against the server's {@link NamespaceTable}, and eagerly parse every TargetVariables receiver
   * and write index range.
   *
   * <p>Every {@code PublishedEvents} dataset's {@code EventNotifier} {@link ExpandedNodeId} is
   * resolved too: an unresolvable notifier URI attaches fine but then makes the {@code
   * PubSubConfiguration} file surface throw on every Open/Read (the wire export resolves it to a
   * local NodeId), so it is rejected here at attach and per reconfigure. Notifier node
   * <em>existence</em> and its {@code SubscribeToEvents} bit are not checked here — the adapter
   * degrades to an inactive binding for those (see {@link PubSubEventNotifierBinding}).
   *
   * <p>Package-private: also enforced per reconfigure by {@link ManagedPubSubService}'s
   * pre-validation.
   */
  static void validateNodeFieldAddresses(PubSubConfig config, NamespaceTable namespaceTable) {

    for (PublishedDataSetConfig dataSet : config.publishedDataSets()) {
      for (FieldDefinition field : dataSet.getFields()) {
        if (field.getSource() instanceof NodeFieldAddress address) {
          requireResolvable(
              address,
              namespaceTable,
              "PublishedDataSet '%s' field '%s'".formatted(dataSet.getName(), field.getName()));
        }
      }

      if (dataSet.getSource() instanceof PublishedEventsConfig events) {
        requireResolvableNotifier(
            events.getEventNotifier(),
            namespaceTable,
            "PublishedDataSet '%s'".formatted(dataSet.getName()));
      }
    }

    for (PubSubConnectionConfig connection : config.connections()) {
      for (ReaderGroupConfig group : connection.readerGroups()) {
        for (DataSetReaderConfig reader : group.getDataSetReaders()) {
          if (reader.getSubscribedDataSet() instanceof TargetVariablesConfig targetVariables) {
            validateTargets(
                targetVariables,
                namespaceTable,
                "DataSetReader '%s/%s/%s'"
                    .formatted(connection.name(), group.getName(), reader.getName()));
          }
        }
      }
    }

    for (StandaloneSubscribedDataSetConfig dataSet : config.standaloneSubscribedDataSets()) {
      if (dataSet.getSubscribedDataSet() instanceof TargetVariablesConfig targetVariables) {
        validateTargets(
            targetVariables,
            namespaceTable,
            "StandaloneSubscribedDataSet '%s'".formatted(dataSet.getName()));
      }
    }
  }

  private static void validateTargets(
      TargetVariablesConfig config, NamespaceTable namespaceTable, String element) {

    for (TargetVariablesConfig.Mapping mapping : config.getMappings()) {
      TargetVariableConfig target = mapping.target();

      requireResolvable(target.getTarget(), namespaceTable, element);
      target.getReceiverIndexRange().ifPresent(range -> requireParseable(range, element));
      target.getWriteIndexRange().ifPresent(range -> requireParseable(range, element));
    }
  }

  private static void requireParseable(String range, String element) {
    try {
      NumericRange.parse(range);
    } catch (UaException e) {
      throw new PubSubConfigValidationException(
          "%s: invalid index range '%s'".formatted(element, range), e);
    }
  }

  private static void requireResolvable(
      NodeFieldAddress address, NamespaceTable namespaceTable, String element) {

    if (address.nodeId().toNodeId(namespaceTable).isEmpty()) {
      throw new PubSubConfigValidationException(
          "%s: cannot resolve %s against the server NamespaceTable"
              .formatted(element, address.nodeId()));
    }
  }

  private static void requireResolvableNotifier(
      ExpandedNodeId eventNotifier, NamespaceTable namespaceTable, String element) {

    if (eventNotifier.toNodeId(namespaceTable).isEmpty()) {
      throw new PubSubConfigValidationException(
          "%s: cannot resolve event notifier %s against the server NamespaceTable"
              .formatted(element, eventNotifier));
    }
  }

  /**
   * Lifecycle of an optional server-side component (the info model fragment, the SKS helper, the
   * remote-configuration helper, the status-event bridge); guarded by {@link #lifecycleLock}.
   * {@code STOPPED} is terminal: a component never restarts, and a component marked {@code STOPPED}
   * before it started (close won the race) never starts at all.
   */
  private enum FaceState {
    NEW,
    STARTED,
    STOPPED
  }
}
