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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.StandaloneSubscribedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariableConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.TargetVariablesConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
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
 *       and saved once. Changes made later via {@link #runtime()} are not saved automatically.
 * </ul>
 *
 * <p>Attach timing: {@code attach} is legal any time after {@link OpcUaServer} construction — the
 * server's address space, including the ns0 PublishSubscribe subtree, is loaded by the constructor.
 * {@link #startup()} does not require {@code server.startup()} and ignores endpoint state entirely;
 * PubSub operates independently of the server's client-facing transports. The caller owns shutdown
 * ordering: {@code OpcUaServer.shutdown()} does not shut down an attached {@link ServerPubSub}.
 */
public final class ServerPubSub implements AutoCloseable {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerPubSub.class);

  private final ConcurrentMap<String, AtomicLong> writeErrorCounters = new ConcurrentHashMap<>();
  private final AtomicBoolean fragmentStarted = new AtomicBoolean(false);
  private final AtomicBoolean fragmentStopped = new AtomicBoolean(false);
  private final AtomicBoolean sksFaceStarted = new AtomicBoolean(false);
  private final AtomicBoolean sksFaceStopped = new AtomicBoolean(false);

  private final PubSubService service;
  private final @Nullable PubSubInfoModelFragment fragment;
  private final @Nullable SksServerFace sksServerFace;

  /** The automatic TargetVariables writers, keyed by reader path; deactivated at shutdown. */
  private final Map<String, TargetVariablesWriter> writers;

  private ServerPubSub(OpcUaServer server, PubSubConfig config, ServerPubSubOptions options) {
    var writers = new HashMap<String, TargetVariablesWriter>();

    PubSubBindings bindings = buildBindings(server, config, options, writers);

    this.writers = Map.copyOf(writers);

    PubSubServiceConfig serviceConfig =
        PubSubServiceConfig.builder()
            .encodingContext(server.getStaticEncodingContext())
            .scheduledExecutor(server.getScheduledExecutorService())
            .transportExecutor(server.getExecutorService())
            .build();

    this.service = PubSubService.create(config, bindings, serviceConfig);

    if (!writers.isEmpty()) {
      service.addStateListener(
          event -> {
            if (event.component().componentType() == ComponentType.DATA_SET_READER) {
              TargetVariablesWriter writer = writers.get(event.component().path());
              if (writer != null) {
                writer.onStateChange(event.newState());
              }
            }
          });
    }

    this.fragment =
        options.isExposeInformationModel()
            ? new PubSubInfoModelFragment(server, config, service, options)
            : null;

    this.sksServerFace =
        options.isSecurityKeyServerEnabled() ? new SksServerFace(server, config, options) : null;
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
   * @throws UnsupportedOperationException if {@code options} enables remote configuration, which is
   *     not supported in this version.
   * @throws PubSubConfigValidationException if a {@link NodeFieldAddress} in the effective
   *     configuration cannot be resolved against the server's {@link NamespaceTable}, a
   *     TargetVariables index range cannot be parsed, a stored configuration does not map to a
   *     valid config, or the enabled Security Key Service face rejects a SecurityGroup (an
   *     unsupported SecurityPolicy URI or a duplicate SecurityGroupId).
   */
  public static ServerPubSub attach(
      OpcUaServer server, PubSubConfig config, ServerPubSubOptions options) {

    if (options.isAllowRemoteConfiguration()) {
      throw new UnsupportedOperationException(
          "allowRemoteConfiguration is not supported in this version");
    }

    NamespaceTable namespaceTable = server.getNamespaceTable();

    PubSubConfig effectiveConfig = config;
    boolean loaded = false;

    PubSubConfigurationStore store = options.getConfigurationStore();
    if (store != null) {
      PubSubConfiguration2DataType stored = store.load();
      if (stored != null) {
        effectiveConfig = PubSubConfig.fromDataType(stored, namespaceTable);
        loaded = true;
      }
    }

    validateNodeFieldAddresses(effectiveConfig, namespaceTable);

    var serverPubSub = new ServerPubSub(server, effectiveConfig, options);

    if (store != null && !loaded) {
      try {
        store.save(effectiveConfig.toDataType(namespaceTable));
      } catch (Exception e) {
        LOGGER.warn("Error saving configuration to the configuration store", e);
      }
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
    if (fragment != null && fragmentStarted.compareAndSet(false, true)) {
      try {
        fragment.startup();
      } catch (Exception e) {
        // unregister the half-built fragment (its lifecycle reached RUNNING before the
        // failure, so shutdown is legal and unregisters cleanly) and surface the failure
        // on the returned future rather than throwing synchronously
        shutdownFragment();
        return CompletableFuture.failedFuture(e);
      }
    }

    if (sksServerFace != null && sksFaceStarted.compareAndSet(false, true)) {
      sksServerFace.startup();
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
              // from racing a subsequent server or namespace teardown
              writers.values().forEach(TargetVariablesWriter::deactivate);
              shutdownFragment();
              shutdownSksServerFace();
            });
  }

  private void shutdownFragment() {
    if (fragment != null && fragmentStarted.get() && fragmentStopped.compareAndSet(false, true)) {
      fragment.shutdown();
    }
  }

  private void shutdownSksServerFace() {
    if (sksServerFace != null
        && sksFaceStarted.get()
        && sksFaceStopped.compareAndSet(false, true)) {
      sksServerFace.shutdown();
    }
  }

  /**
   * Get the underlying {@link PubSubService} runtime; the full standalone API remains available.
   *
   * <p>Note: automatic bindings and the exposed information model reflect the attach-time
   * configuration. Reconfiguring via the runtime does not re-derive address-space sources or
   * TargetVariables writers, and does not rebuild configuration-derived information model nodes.
   *
   * @return the underlying {@link PubSubService}.
   */
  public PubSubService runtime() {
    return service;
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
    var addressSpaceSource = new AddressSpacePublishedDataSetSource(server);

    for (PublishedDataSetConfig dataSet : config.publishedDataSets()) {
      if (userSources.containsKey(dataSet.ref())) {
        // a caller-supplied source wins; skip the automatic binding
        continue;
      }

      List<FieldDefinition> fields = dataSet.getFields();
      boolean allNodeBacked =
          !fields.isEmpty()
              && fields.stream().allMatch(field -> field.getSource() instanceof NodeFieldAddress);

      if (allNodeBacked) {
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

  /**
   * Eagerly resolve every {@link NodeFieldAddress} in {@code config} — published dataset field
   * sources and TargetVariables targets, including those of standalone subscribed datasets —
   * against the server's {@link NamespaceTable}, and eagerly parse every TargetVariables receiver
   * and write index range.
   */
  private static void validateNodeFieldAddresses(
      PubSubConfig config, NamespaceTable namespaceTable) {

    for (PublishedDataSetConfig dataSet : config.publishedDataSets()) {
      for (FieldDefinition field : dataSet.getFields()) {
        if (field.getSource() instanceof NodeFieldAddress address) {
          requireResolvable(
              address,
              namespaceTable,
              "PublishedDataSet '%s' field '%s'".formatted(dataSet.getName(), field.getName()));
        }
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
}
