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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubCounter;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.sdk.pubsub.SecurityKeyInfo;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsWriterGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Part 14 §9.1.11 PubSub diagnostics exposure (pinned decisions R13, R14, R15, R18, R20 rider):
 * backs the loader-built ns0 root Diagnostics subtree ({@code i=17409}) and mints one {@code
 * Diagnostics} object per exposed connection, writer/reader group, and dataset writer/reader.
 *
 * <p>Created by {@link PubSubInfoModelFragment} when {@link
 * ServerPubSubOptions#isDiagnosticsEnabled()} is {@code true} and driven by the fragment's
 * lifecycle; per-component minting rides the fragment's {@link ComponentNodeListener} seam (builds
 * fire nested-before-container; the mint has no cross-component dependencies), so incremental
 * reconfiguration rebuilds (R10) carry the Diagnostics objects for free. Because component name
 * paths cannot be split back into name components (names may contain {@code '/'}, see {@link
 * PubSubNodeIds}), the fragment's builders stage each component's name components via {@link
 * #stageComponent} immediately before the build notification fires.
 *
 * <p><b>Values are served pull-model</b>: every counter, Total*, SubError, and non-static LiveValue
 * node carries an {@link AttributeFilters#getValue} filter computing the {@link DataValue} at
 * read/sample time from {@link PubSubService#diagnostics()} (one per-path snapshot per read, D49)
 * and this exposure's registries. Filter bodies run on session/sampler threads and are non-blocking
 * and lock-free: map reads, one {@code component(path)} call, and the engine's handle-keyed
 * accessors only; DNS resolution for {@code ResolvedAddress} happens once at node-build time (R15),
 * never at read. Handles for the R13 LiveValue feeds ({@link PubSubService#securityKeyInfo}, {@link
 * PubSubService#nextDataSetMessageSequenceNumber}) are resolved per read via {@code components()}
 * lookups and never cached; a vanished component ({@code IllegalArgumentException}) serves a
 * null-value {@link DataValue}.
 *
 * <p><b>Numeric posture</b> (§9.1.11.5): counters clamp to UInt32 at {@code 0xFFFFFFFF} with the
 * served SourceTimestamp still advancing at the cap ({@link #counterValue}); counts clamp to UInt16
 * at 65535; DataSetMessage sequence numbers narrow to 16 bits (the JSON mapping counts 32-bit on
 * the wire — documented narrowing, D12); {@code TimeToNextTokenID} serves Duration as Double
 * milliseconds.
 *
 * <p><b>DiagnosticsLevel posture</b> (R13/D36): the object-level DiagnosticsLevel is read-only
 * {@code Basic} and there is no level-switching or activation machinery. The Advanced/Info-level
 * rows (WG EncryptionErrors, RG DecryptionErrors, the token and sequence LiveValues) are
 * nonetheless served with {@code Active=true} — documented surplus provision beyond what
 * level=Basic demands; they are counting either way.
 *
 * <p><b>Reset</b> (§9.1.11.3, D41/D38/D29): every Diagnostics object carries a zero-argument {@code
 * Reset} Method gated by the effective {@link PubSubMethodAuthorizer#checkConfigure} (bad codes
 * surfaced verbatim; session-less invocations answer {@code Bad_UserAccessDenied}; no
 * channel-security minimum). Per-component Reset delegates to {@link
 * org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics#reset(String)} — per-object scope, {@code
 * lastError} and {@code ServerPubSub.targetWriteErrors()} untouched. The PublishSubscribe root has
 * no engine path (D10): the root's six State* counters are fragment-local ({@link
 * #tickRootStateCounter} — nothing ticks them in this version because the ns0 root Status
 * Enable/Disable pair is not minted, D23, and fragment lifecycle transitions are service lifecycle,
 * not §6.2.1 causes) and the root Reset zeroes exactly those fragment-local counters, never calling
 * the engine.
 *
 * <p><b>ns0 discipline</b> (R18): the {@code i=17409} subtree is fully loader-built — this class
 * sets values, attaches filters, and attaches the {@code i=17421} Reset invocation handler
 * (warn-if-occupied, restore-if-still-ours at shutdown, the {@code SksServerFace} precedent); it
 * never mints ns0 nodes. {@link #restoreNs0Root()} removes every attached filter, nulls every
 * statically set value, and restores {@link MethodInvocationHandler#NOT_IMPLEMENTED}, so ns0 never
 * serves values from a dead service. Fragment-minted nodes are attached via HasChild-subtype
 * references so {@code UaNode.delete()} cascades over them, and they disappear with the fragment's
 * node manager.
 *
 * <p>Registry hygiene: {@link #unregisterComponent} — invoked from the fragment's removal path via
 * {@link #onComponentRemoving} — prunes all per-path registries by exact key and {@code "<path>/"}
 * prefix, so reconfigure-heavy servers do not leak entries. Path-stable restarted components keep
 * their NodeIds and (post-R14 engine preservation) their counter values; the pull model makes
 * rebuilt nodes self-consistent.
 */
final class PubSubDiagnosticsExposure implements ComponentNodeListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubDiagnosticsExposure.class);

  /** The six inherited Table 311 State* counters, exposed on every diagnostics object. */
  private static final List<CounterSpec> STATE_COUNTER_SPECS =
      List.of(
          new CounterSpec(
              "StateError",
              PubSubCounter.STATE_ERROR,
              PubSubDiagnosticsCounterClassification.Error,
              DiagnosticsLevel.Basic),
          new CounterSpec(
              "StateOperationalByMethod",
              PubSubCounter.STATE_OPERATIONAL_BY_METHOD,
              PubSubDiagnosticsCounterClassification.Information,
              DiagnosticsLevel.Basic),
          new CounterSpec(
              "StateOperationalByParent",
              PubSubCounter.STATE_OPERATIONAL_BY_PARENT,
              PubSubDiagnosticsCounterClassification.Information,
              DiagnosticsLevel.Basic),
          new CounterSpec(
              "StateOperationalFromError",
              PubSubCounter.STATE_OPERATIONAL_FROM_ERROR,
              PubSubDiagnosticsCounterClassification.Information,
              DiagnosticsLevel.Basic),
          new CounterSpec(
              "StatePausedByParent",
              PubSubCounter.STATE_PAUSED_BY_PARENT,
              PubSubDiagnosticsCounterClassification.Information,
              DiagnosticsLevel.Basic),
          new CounterSpec(
              "StateDisabledByMethod",
              PubSubCounter.STATE_DISABLED_BY_METHOD,
              PubSubDiagnosticsCounterClassification.Information,
              DiagnosticsLevel.Basic));

  private final PubSubInfoModelFragment fragment;
  private final OpcUaServer server;
  private final PubSubService service;
  private final PubSubMethodAuthorizer authorizer;

  /** Guards listener callbacks against in-flight deliveries after removal. */
  private volatile boolean active = false;

  /**
   * The wire configuration the current build pass builds from, recorded by {@link #onBuildPass}
   * before the fragment's builders run (initial build and every R10 rebuild); the mint resolves
   * per-component wire data (security, addresses, metadata versions) from it by name components.
   */
  private volatile @Nullable PubSubConfiguration2DataType buildingConfiguration;

  /**
   * Name components staged by the fragment's builders, keyed by name path, consumed by {@link
   * #onComponentBuilt} (paths cannot be split; coordinates must be carried explicitly, R11).
   */
  private final ConcurrentMap<String, StagedCoordinates> staged = new ConcurrentHashMap<>();

  /** Every component with a minted Diagnostics object, keyed by name path. */
  private final ConcurrentMap<String, RegisteredComponent> components = new ConcurrentHashMap<>();

  /**
   * Last observed {@link PubSubState} per registered component path: seeded from the runtime at
   * mint, maintained by the state listener. Feeds the Operational* LiveValue counts.
   */
  private final ConcurrentMap<String, PubSubState> statesByPath = new ConcurrentHashMap<>();

  /** Last received DataSetMessage sequence number per reader path (DataSetListener feed). */
  private final ConcurrentMap<String, UInteger> lastDsrSeq = new ConcurrentHashMap<>();

  /**
   * Effective metadata ConfigurationVersion per reader path: seeded from the configured metadata at
   * mint, updated by the MetaDataListener feed.
   */
  private final ConcurrentMap<String, ConfigurationVersionDataType> dsrVersions =
      new ConcurrentHashMap<>();

  /**
   * The fragment-local root State* counters (D10: the PublishSubscribe root has no engine path).
   */
  private final ConcurrentMap<PubSubCounter, AtomicLong> rootCounters = new ConcurrentHashMap<>();

  /** First-left-zero times of the root counters; entry present only once non-zero. */
  private final ConcurrentMap<PubSubCounter, DateTime> rootTimeFirstChange =
      new ConcurrentHashMap<>();

  /** Filters attached to ns0 loader nodes, removed by {@link #restoreNs0Root()}. */
  private final List<AttachedFilter> ns0Filters = new CopyOnWriteArrayList<>();

  /** ns0 loader nodes whose values were set statically, nulled by {@link #restoreNs0Root()}. */
  private final List<UaVariableNode> ns0StaticNodes = new CopyOnWriteArrayList<>();

  private @Nullable UaMethodNode ns0ResetNode;
  private @Nullable RootResetHandler ns0ResetHandler;

  private final PubSubStateListener stateListener;
  private final DataSetListener dataSetListener;
  private final MetaDataListener metaDataListener;

  PubSubDiagnosticsExposure(
      PubSubInfoModelFragment fragment,
      OpcUaServer server,
      PubSubService service,
      PubSubMethodAuthorizer authorizer) {

    this.fragment = fragment;
    this.server = server;
    this.service = service;
    this.authorizer = authorizer;

    for (CounterSpec spec : STATE_COUNTER_SPECS) {
      rootCounters.put(spec.counter(), new AtomicLong());
    }

    // registered only while the exposure is active (trap: the DataSet listener runs on the
    // dataset dispatch path — one guarded map put, nothing more)
    this.stateListener =
        event -> {
          if (active && isTrackedComponentType(event.component().componentType())) {
            // write-if-registered via compute, not computeIfPresent: a delivery that outruns
            // the mint's statesByPath seed must still land, or the seed (read before the
            // transition) would stay stale until the path's next transition. Gating on
            // components membership inside the compute keeps removed paths from being
            // re-created by in-flight deliveries (unregisterComponent prunes components
            // first); a non-member's existing value is left for its pending removeIf.
            statesByPath.compute(
                event.component().path(),
                (path, state) -> components.containsKey(path) ? event.newState() : state);
          }
        };

    this.dataSetListener =
        event -> {
          if (active) {
            UInteger sequenceNumber = event.dataSetMessageSequenceNumber();
            String path = event.reader().path();
            if (sequenceNumber != null && components.containsKey(path)) {
              lastDsrSeq.put(path, sequenceNumber);
            }
          }
        };

    this.metaDataListener =
        event -> {
          if (active) {
            String path = event.reader().path();
            if (components.containsKey(path)) {
              dsrVersions.put(path, event.configurationVersion());
            }
          }
        };
  }

  // region lifecycle (driven by the fragment)

  /**
   * Record the wire configuration the imminent build pass builds from; called by the fragment
   * before the initial startup build and before every reconfigure rebuild's build phase.
   */
  void onBuildPass(PubSubConfiguration2DataType configuration) {
    this.buildingConfiguration = configuration;
  }

  /**
   * Register the engine listeners (state, DataSet sequence, metadata). Called from the fragment's
   * {@code registerListeners}; reciprocal of {@link #removeListeners()}.
   */
  void registerListeners() {
    active = true;
    service.addStateListener(stateListener);
    service.addDataSetListener(dataSetListener);
    service.addMetaDataListener(metaDataListener);
  }

  /** Remove the engine listeners (R12 removal API); called at fragment shutdown. */
  void removeListeners() {
    active = false;
    service.removeStateListener(stateListener);
    service.removeDataSetListener(dataSetListener);
    service.removeMetaDataListener(metaDataListener);
  }

  // endregion

  // region per-component minting (ComponentNodeListener)

  /**
   * Stage the name components of a component about to be built, so {@link #onComponentBuilt} can
   * resolve engine handles and wire-form data without parsing the name path. Called by the
   * fragment's per-component builders immediately before the build notification.
   *
   * @param namePath the component's joined name path (the notification key).
   * @param connectionName the connection name; for connections, equal to {@code namePath}.
   * @param groupName the writer/reader group name, or {@code null} for connections.
   * @param componentName the writer/reader name, or {@code null} for connections and groups.
   */
  void stageComponent(
      String namePath,
      String connectionName,
      @Nullable String groupName,
      @Nullable String componentName) {

    staged.put(namePath, new StagedCoordinates(connectionName, groupName, componentName));
  }

  @Override
  public void onComponentBuilt(ComponentType type, String namePath, UaObjectNode componentNode) {
    StagedCoordinates coordinates = staged.remove(namePath);
    PubSubConfiguration2DataType configuration = buildingConfiguration;

    if (coordinates == null || configuration == null) {
      LOGGER.warn("no staged coordinates for built component; skipping diagnostics: {}", namePath);
      return;
    }

    mintDiagnostics(type, namePath, componentNode, coordinates, configuration);
  }

  @Override
  public void onComponentRemoving(ComponentType type, String namePath) {
    // the reciprocal registry hook (S7): the fragment's reconfigure removal path notifies each
    // removed component (children before parents) before deleting its subtree
    unregisterComponent(namePath);
  }

  /**
   * Prune every per-path registry entry equal to {@code namePath} or under {@code namePath + "/"}.
   * Called from the fragment's removal path (via {@link #onComponentRemoving}); without it,
   * reconfigure-heavy servers would leak registry entries.
   */
  void unregisterComponent(String namePath) {
    String prefix = namePath + "/";

    components.keySet().removeIf(key -> key.equals(namePath) || key.startsWith(prefix));
    statesByPath.keySet().removeIf(key -> key.equals(namePath) || key.startsWith(prefix));
    lastDsrSeq.keySet().removeIf(key -> key.equals(namePath) || key.startsWith(prefix));
    dsrVersions.keySet().removeIf(key -> key.equals(namePath) || key.startsWith(prefix));
    staged.keySet().removeIf(key -> key.equals(namePath) || key.startsWith(prefix));
  }

  /**
   * Mint the Diagnostics object of one built component: the type-appropriate Diagnostics instance
   * under the component node plus DiagnosticsLevel, TotalInformation/TotalError, Reset, SubError,
   * Counters, and LiveValues members per §9.1.11.7–9.1.11.12 (all Mandatory rows + the R13
   * Optional-row set).
   */
  private void mintDiagnostics(
      ComponentType type,
      String namePath,
      UaObjectNode componentNode,
      StagedCoordinates coordinates,
      PubSubConfiguration2DataType configuration) {

    var component =
        new RegisteredComponent(
            type,
            namePath,
            coordinates.connectionName(),
            coordinates.groupName(),
            coordinates.componentName());

    components.put(namePath, component);
    // putIfAbsent, not put: once components carries the path, a concurrent state-listener
    // delivery may install a state newer than the currentState() read above; the seed must
    // not clobber it (either write order then converges to the live state)
    statesByPath.putIfAbsent(namePath, currentState(component));

    UaObjectNode diagnosticsNode =
        fragment.addObjectNode(
            objectNodeConstructor(type),
            fragment.childNodeId(componentNode, "Diagnostics"),
            new QualifiedName(0, "Diagnostics"),
            typeDefinitionId(type),
            NodeIds.HasComponent,
            componentNode.getNodeId());

    // DiagnosticsLevel: read-only Basic, no level machinery (R13)
    fragment.addVariableNode(
        diagnosticsNode,
        "DiagnosticsLevel",
        NodeIds.DiagnosticsLevel,
        new Variant(DiagnosticsLevel.Basic));

    // Total* computed at exposure from the object's own spec-exposed counters (R18), with the
    // D37-pinned properties (Classification per polarity, level Basic, Active=true,
    // TimeFirstChange = earliest contributing counter)
    fragment.addCounterNode(
        diagnosticsNode,
        "TotalInformation",
        PubSubDiagnosticsCounterClassification.Information,
        DiagnosticsLevel.Basic,
        totalValueFilter(type, namePath, PubSubDiagnosticsCounterClassification.Information),
        totalTimeFirstChangeFilter(
            type, namePath, PubSubDiagnosticsCounterClassification.Information));

    fragment.addCounterNode(
        diagnosticsNode,
        "TotalError",
        PubSubDiagnosticsCounterClassification.Error,
        DiagnosticsLevel.Basic,
        totalValueFilter(type, namePath, PubSubDiagnosticsCounterClassification.Error),
        totalTimeFirstChangeFilter(type, namePath, PubSubDiagnosticsCounterClassification.Error));

    UaMethodNode resetNode = fragment.addMethodNode(diagnosticsNode, "Reset");
    resetNode.setInvocationHandler(new ComponentResetHandler(resetNode, namePath));

    // SubError rolls up the DIRECT child layer's TotalError (§9.1.11.2); writers/readers have
    // no child layer and serve a static false
    BaseDataVariableTypeNode subErrorNode =
        fragment.addVariableNode(diagnosticsNode, "SubError", NodeIds.Boolean, new Variant(false));
    if (type == ComponentType.CONNECTION
        || type == ComponentType.WRITER_GROUP
        || type == ComponentType.READER_GROUP) {
      subErrorNode
          .getFilterChain()
          .addLast(AttributeFilters.getValue(ctx -> booleanValue(subError(component))));
    }

    UaObjectNode countersNode =
        fragment.addObjectNode(
            BaseObjectTypeNode::new,
            fragment.childNodeId(diagnosticsNode, "Counters"),
            new QualifiedName(0, "Counters"),
            NodeIds.BaseObjectType,
            NodeIds.HasComponent,
            diagnosticsNode.getNodeId());

    for (CounterSpec spec : counterSpecs(type)) {
      fragment.addCounterNode(
          countersNode,
          spec.browseName(),
          spec.classification(),
          spec.level(),
          counterValueFilter(namePath, spec.counter()),
          counterTimeFirstChangeFilter(namePath, spec.counter()));
    }

    UaObjectNode liveValuesNode =
        fragment.addObjectNode(
            BaseObjectTypeNode::new,
            fragment.childNodeId(diagnosticsNode, "LiveValues"),
            new QualifiedName(0, "LiveValues"),
            NodeIds.BaseObjectType,
            NodeIds.HasComponent,
            diagnosticsNode.getNodeId());

    switch (type) {
      case CONNECTION -> mintConnectionLiveValues(liveValuesNode, component, configuration);
      case WRITER_GROUP -> mintWriterGroupLiveValues(liveValuesNode, component, configuration);
      case READER_GROUP -> mintReaderGroupLiveValues(liveValuesNode, component, configuration);
      case DATA_SET_WRITER -> mintDataSetWriterLiveValues(liveValuesNode, component, configuration);
      case DATA_SET_READER -> mintDataSetReaderLiveValues(liveValuesNode, component, configuration);
      default -> LOGGER.warn("unexpected component type for diagnostics: {}", type);
    }
  }

  /** §9.1.11.8: ResolvedAddress only, computed once at build time (R15), served statically. */
  private void mintConnectionLiveValues(
      UaObjectNode liveValuesNode,
      RegisteredComponent component,
      PubSubConfiguration2DataType configuration) {

    PubSubConnectionDataType connection =
        PubSubInfoModelFragment.findConnection(configuration, component.connectionName());

    String url = null;
    if (connection != null
        && connection.getAddress() instanceof NetworkAddressUrlDataType urlAddress) {
      url = urlAddress.getUrl();
    }

    addLiveValueNode(
        liveValuesNode,
        "ResolvedAddress",
        NodeIds.String,
        DiagnosticsLevel.Basic,
        null,
        new Variant(resolvedAddress(url)));
  }

  /** §9.1.11.9: writer counts plus the D47 token pair for secured groups. */
  private void mintWriterGroupLiveValues(
      UaObjectNode liveValuesNode,
      RegisteredComponent component,
      PubSubConfiguration2DataType configuration) {

    addLiveValueNode(
        liveValuesNode,
        "ConfiguredDataSetWriters",
        NodeIds.UInt16,
        DiagnosticsLevel.Basic,
        AttributeFilters.getValue(
            ctx -> countValue(countChildren(ComponentType.DATA_SET_WRITER, component, false))),
        Variant.NULL_VALUE);

    addLiveValueNode(
        liveValuesNode,
        "OperationalDataSetWriters",
        NodeIds.UInt16,
        DiagnosticsLevel.Basic,
        AttributeFilters.getValue(
            ctx -> countValue(countChildren(ComponentType.DATA_SET_WRITER, component, true))),
        Variant.NULL_VALUE);

    WriterGroupDataType group =
        PubSubInfoModelFragment.findWriterGroup(
            PubSubInfoModelFragment.findConnection(configuration, component.connectionName()),
            component.groupName());

    if (group != null && secured(group.getSecurityMode(), group.getSecurityGroupId())) {
      mintTokenLiveValues(liveValuesNode, writerGroupHandleLookup(component));
    }
  }

  /** §9.1.11.10: reader counts (no token pair at RG level — Table-verified). */
  private void mintReaderGroupLiveValues(
      UaObjectNode liveValuesNode,
      RegisteredComponent component,
      PubSubConfiguration2DataType configuration) {

    addLiveValueNode(
        liveValuesNode,
        "ConfiguredDataSetReaders",
        NodeIds.UInt16,
        DiagnosticsLevel.Basic,
        AttributeFilters.getValue(
            ctx -> countValue(countChildren(ComponentType.DATA_SET_READER, component, false))),
        Variant.NULL_VALUE);

    addLiveValueNode(
        liveValuesNode,
        "OperationalDataSetReaders",
        NodeIds.UInt16,
        DiagnosticsLevel.Basic,
        AttributeFilters.getValue(
            ctx -> countValue(countChildren(ComponentType.DATA_SET_READER, component, true))),
        Variant.NULL_VALUE);
  }

  /**
   * §9.1.11.11: MessageSequenceNumber always minted, serving the writer's NEXT DataSetMessage
   * sequence number — what the next data message will carry and keep-alives advertise (D4, Part 14
   * §7.2.4.5.8), 16-bit narrowed; Major/MinorVersion static from the referenced PublishedDataSet's
   * metadata version, refreshed by rebuilds.
   */
  private void mintDataSetWriterLiveValues(
      UaObjectNode liveValuesNode,
      RegisteredComponent component,
      PubSubConfiguration2DataType configuration) {

    Supplier<Optional<PubSubHandle>> handleLookup =
        () ->
            service
                .components()
                .dataSetWriter(
                    component.connectionName(),
                    requireName(component.groupName()),
                    requireName(component.componentName()));

    addLiveValueNode(
        liveValuesNode,
        "MessageSequenceNumber",
        NodeIds.UInt16,
        DiagnosticsLevel.Info,
        AttributeFilters.getValue(
            ctx -> {
              try {
                return handleLookup
                    .get()
                    .map(
                        handle ->
                            sequenceNumberValue(service.nextDataSetMessageSequenceNumber(handle)))
                    .orElseGet(PubSubDiagnosticsExposure::nullValue);
              } catch (IllegalArgumentException e) {
                return nullValue();
              }
            }),
        Variant.NULL_VALUE);

    ConfigurationVersionDataType version = writerDataSetVersion(component, configuration);

    addLiveValueNode(
        liveValuesNode,
        "MajorVersion",
        NodeIds.UInt32,
        DiagnosticsLevel.Info,
        null,
        version != null ? new Variant(version.getMajorVersion()) : Variant.NULL_VALUE);

    addLiveValueNode(
        liveValuesNode,
        "MinorVersion",
        NodeIds.UInt32,
        DiagnosticsLevel.Info,
        null,
        version != null ? new Variant(version.getMinorVersion()) : Variant.NULL_VALUE);
  }

  /**
   * §9.1.11.12: MessageSequenceNumber from the last delivered DataSet (null before the first
   * delivery), Major/MinorVersion from the effective metadata version (configured seed, updated by
   * received metadata), and the D47 token pair when the owning ReaderGroup is secured (the runtime
   * routes security by group; the served values reflect the owning group's key state).
   */
  private void mintDataSetReaderLiveValues(
      UaObjectNode liveValuesNode,
      RegisteredComponent component,
      PubSubConfiguration2DataType configuration) {

    String namePath = component.namePath();

    addLiveValueNode(
        liveValuesNode,
        "MessageSequenceNumber",
        NodeIds.UInt16,
        DiagnosticsLevel.Info,
        AttributeFilters.getValue(
            ctx -> {
              UInteger sequenceNumber = lastDsrSeq.get(namePath);
              return sequenceNumber != null ? sequenceNumberValue(sequenceNumber) : nullValue();
            }),
        Variant.NULL_VALUE);

    addLiveValueNode(
        liveValuesNode,
        "MajorVersion",
        NodeIds.UInt32,
        DiagnosticsLevel.Info,
        AttributeFilters.getValue(
            ctx -> {
              ConfigurationVersionDataType version = dsrVersions.get(namePath);
              return version != null
                  ? goodValue(new Variant(version.getMajorVersion()))
                  : nullValue();
            }),
        Variant.NULL_VALUE);

    addLiveValueNode(
        liveValuesNode,
        "MinorVersion",
        NodeIds.UInt32,
        DiagnosticsLevel.Info,
        AttributeFilters.getValue(
            ctx -> {
              ConfigurationVersionDataType version = dsrVersions.get(namePath);
              return version != null
                  ? goodValue(new Variant(version.getMinorVersion()))
                  : nullValue();
            }),
        Variant.NULL_VALUE);

    ReaderGroupDataType group =
        PubSubInfoModelFragment.findReaderGroup(
            PubSubInfoModelFragment.findConnection(configuration, component.connectionName()),
            component.groupName());

    DataSetReaderDataType reader =
        group != null
            ? PubSubInfoModelFragment.findDataSetReader(group, component.componentName())
            : null;

    if (reader != null) {
      DataSetMetaDataType metaData = reader.getDataSetMetaData();
      if (metaData != null && metaData.getConfigurationVersion() != null) {
        dsrVersions.put(namePath, metaData.getConfigurationVersion());
      }
    }

    if (group != null && secured(group.getSecurityMode(), group.getSecurityGroupId())) {
      mintTokenLiveValues(liveValuesNode, readerGroupHandleLookup(component));
    }
  }

  /**
   * The D47 SecurityTokenID/TimeToNextTokenID pair, fed by {@link PubSubService#securityKeyInfo}:
   * null values while the group holds no key state (not yet fetched, detached) or the component
   * vanished. Token metadata only — never key material.
   */
  private void mintTokenLiveValues(
      UaObjectNode liveValuesNode, Supplier<Optional<PubSubHandle>> groupHandleLookup) {

    addLiveValueNode(
        liveValuesNode,
        "SecurityTokenID",
        NodeIds.UInt32,
        DiagnosticsLevel.Info,
        AttributeFilters.getValue(
            ctx -> {
              SecurityKeyInfo info = securityKeyInfo(groupHandleLookup);
              return info != null ? goodValue(new Variant(info.securityTokenId())) : nullValue();
            }),
        Variant.NULL_VALUE);

    addLiveValueNode(
        liveValuesNode,
        "TimeToNextTokenID",
        NodeIds.Duration,
        DiagnosticsLevel.Info,
        AttributeFilters.getValue(
            ctx -> {
              SecurityKeyInfo info = securityKeyInfo(groupHandleLookup);
              return info != null && info.timeToNextKey() != null
                  ? goodValue(new Variant((double) info.timeToNextKey().toMillis()))
                  : nullValue();
            }),
        Variant.NULL_VALUE);
  }

  private @Nullable SecurityKeyInfo securityKeyInfo(
      Supplier<Optional<PubSubHandle>> groupHandleLookup) {
    try {
      return groupHandleLookup.get().map(service::securityKeyInfo).orElse(null);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /** A LiveValue variable with its DiagnosticsLevel property; filter-served unless static. */
  private void addLiveValueNode(
      UaObjectNode parent,
      String name,
      NodeId dataTypeId,
      DiagnosticsLevel level,
      @Nullable AttributeFilter valueFilter,
      Variant staticValue) {

    BaseDataVariableTypeNode node = fragment.addVariableNode(parent, name, dataTypeId, staticValue);

    if (valueFilter != null) {
      node.getFilterChain().addLast(valueFilter);
    }

    fragment.addPropertyNode(
        node, "DiagnosticsLevel", NodeIds.DiagnosticsLevel, ValueRanks.Scalar, new Variant(level));
  }

  // endregion

  // region ns0 root backing (i=17409, R18: back values, never mint)

  /**
   * Back the loader-built ns0 root Diagnostics subtree: static values on DiagnosticsLevel and the
   * counter properties, value/TimeFirstChange filters over the fragment-local root counters, the
   * Total*, SubError, and LiveValues filters, and the {@code i=17421} Reset handler.
   */
  void backNs0Root() {
    setNs0Static(
        NodeIds.PublishSubscribe_Diagnostics_DiagnosticsLevel, new Variant(DiagnosticsLevel.Basic));

    backNs0Total(
        PubSubDiagnosticsCounterClassification.Information,
        NodeIds.PublishSubscribe_Diagnostics_TotalInformation,
        NodeIds.PublishSubscribe_Diagnostics_TotalInformation_Active,
        NodeIds.PublishSubscribe_Diagnostics_TotalInformation_Classification,
        NodeIds.PublishSubscribe_Diagnostics_TotalInformation_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_TotalInformation_TimeFirstChange);

    backNs0Total(
        PubSubDiagnosticsCounterClassification.Error,
        NodeIds.PublishSubscribe_Diagnostics_TotalError,
        NodeIds.PublishSubscribe_Diagnostics_TotalError_Active,
        NodeIds.PublishSubscribe_Diagnostics_TotalError_Classification,
        NodeIds.PublishSubscribe_Diagnostics_TotalError_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_TotalError_TimeFirstChange);

    attachNs0Filter(
        NodeIds.PublishSubscribe_Diagnostics_SubError,
        AttributeFilters.getValue(ctx -> booleanValue(rootSubError())));

    backNs0StateCounter(
        PubSubCounter.STATE_ERROR,
        PubSubDiagnosticsCounterClassification.Error,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateError,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_Active,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_Classification,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateError_TimeFirstChange);

    backNs0StateCounter(
        PubSubCounter.STATE_OPERATIONAL_BY_METHOD,
        PubSubDiagnosticsCounterClassification.Information,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_Active,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_Classification,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange);

    backNs0StateCounter(
        PubSubCounter.STATE_OPERATIONAL_BY_PARENT,
        PubSubDiagnosticsCounterClassification.Information,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByParent,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_Active,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_Classification,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange);

    backNs0StateCounter(
        PubSubCounter.STATE_OPERATIONAL_FROM_ERROR,
        PubSubDiagnosticsCounterClassification.Information,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalFromError,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_Active,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_Classification,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange);

    backNs0StateCounter(
        PubSubCounter.STATE_PAUSED_BY_PARENT,
        PubSubDiagnosticsCounterClassification.Information,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StatePausedByParent,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StatePausedByParent_Active,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StatePausedByParent_Classification,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StatePausedByParent_TimeFirstChange);

    backNs0StateCounter(
        PubSubCounter.STATE_DISABLED_BY_METHOD,
        PubSubDiagnosticsCounterClassification.Information,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_Active,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_Classification,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel,
        NodeIds.PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange);

    backNs0LiveValue(
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetWriters,
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel,
        AttributeFilters.getValue(
            ctx -> countValue(countByType(ComponentType.DATA_SET_WRITER, false))));

    backNs0LiveValue(
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetReaders,
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel,
        AttributeFilters.getValue(
            ctx -> countValue(countByType(ComponentType.DATA_SET_READER, false))));

    backNs0LiveValue(
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetWriters,
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetWriters_DiagnosticsLevel,
        AttributeFilters.getValue(
            ctx -> countValue(countByType(ComponentType.DATA_SET_WRITER, true))));

    backNs0LiveValue(
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetReaders,
        NodeIds.PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetReaders_DiagnosticsLevel,
        AttributeFilters.getValue(
            ctx -> countValue(countByType(ComponentType.DATA_SET_READER, true))));

    attachNs0ResetHandler();
  }

  /**
   * Undo {@link #backNs0Root()}: remove every attached filter (reads fall back to the loader-null
   * stored values), null every statically set value, and restore {@link
   * MethodInvocationHandler#NOT_IMPLEMENTED} on {@code i=17421} unless another handler was attached
   * after ours. ns0 nodes outlive the fragment; they must never serve values from a dead service.
   */
  void restoreNs0Root() {
    for (AttachedFilter attached : ns0Filters) {
      attached.node().getFilterChain().remove(attached.filter());
    }
    ns0Filters.clear();

    for (UaVariableNode node : ns0StaticNodes) {
      node.setValue(new DataValue(Variant.NULL_VALUE));
    }
    ns0StaticNodes.clear();

    UaMethodNode resetNode = this.ns0ResetNode;
    if (resetNode != null) {
      if (resetNode.getInvocationHandler() == ns0ResetHandler) {
        resetNode.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED);
      } else {
        LOGGER.warn("ns0 Diagnostics Reset node no longer carries our handler; not restoring");
      }
    }
    this.ns0ResetNode = null;
    this.ns0ResetHandler = null;
  }

  private void backNs0Total(
      PubSubDiagnosticsCounterClassification classification,
      NodeId valueNodeId,
      NodeId activeNodeId,
      NodeId classificationNodeId,
      NodeId levelNodeId,
      NodeId timeFirstChangeNodeId) {

    attachNs0Filter(
        valueNodeId,
        AttributeFilters.getValue(ctx -> counterValue(rootClassificationSum(classification))));
    attachNs0OptionalFilter(
        timeFirstChangeNodeId,
        AttributeFilters.getValue(ctx -> timeValue(rootEarliestTimeFirstChange(classification))));

    setNs0Static(activeNodeId, new Variant(true));
    setNs0Static(classificationNodeId, new Variant(classification));
    setNs0Static(levelNodeId, new Variant(DiagnosticsLevel.Basic));
  }

  private void backNs0StateCounter(
      PubSubCounter counter,
      PubSubDiagnosticsCounterClassification classification,
      NodeId valueNodeId,
      NodeId activeNodeId,
      NodeId classificationNodeId,
      NodeId levelNodeId,
      NodeId timeFirstChangeNodeId) {

    attachNs0Filter(
        valueNodeId, AttributeFilters.getValue(ctx -> counterValue(rootCounterValue(counter))));
    attachNs0OptionalFilter(
        timeFirstChangeNodeId,
        AttributeFilters.getValue(ctx -> timeValue(rootTimeFirstChange.get(counter))));

    setNs0Static(activeNodeId, new Variant(true));
    setNs0Static(classificationNodeId, new Variant(classification));
    setNs0Static(levelNodeId, new Variant(DiagnosticsLevel.Basic));
  }

  private void backNs0LiveValue(NodeId valueNodeId, NodeId levelNodeId, AttributeFilter filter) {
    attachNs0Filter(valueNodeId, filter);
    setNs0Static(levelNodeId, new Variant(DiagnosticsLevel.Basic));
  }

  private void attachNs0ResetHandler() {
    Optional<UaNode> node =
        server.getAddressSpaceManager().getManagedNode(NodeIds.PublishSubscribe_Diagnostics_Reset);

    if (node.orElse(null) instanceof UaMethodNode resetNode) {
      if (resetNode.getInvocationHandler() != MethodInvocationHandler.NOT_IMPLEMENTED) {
        LOGGER.warn("ns0 Diagnostics Reset node already has an invocation handler; replacing it");
      }

      var handler = new RootResetHandler(resetNode);
      resetNode.setInvocationHandler(handler);

      this.ns0ResetNode = resetNode;
      this.ns0ResetHandler = handler;
    } else {
      LOGGER.warn(
          "ns0 Diagnostics Reset UaMethodNode not found: {}",
          NodeIds.PublishSubscribe_Diagnostics_Reset);
    }
  }

  /** Attach a value filter to an existing ns0 node, remembered for removal; never creates. */
  private void attachNs0Filter(NodeId nodeId, AttributeFilter filter) {
    Optional<UaNode> node = server.getAddressSpaceManager().getManagedNode(nodeId);

    if (node.orElse(null) instanceof UaVariableNode variableNode) {
      variableNode.getFilterChain().addLast(filter);
      ns0Filters.add(new AttachedFilter(variableNode, filter));
    } else {
      LOGGER.warn("ns0 diagnostics node not found: {}", nodeId);
    }
  }

  /**
   * Like {@link #attachNs0Filter} for ns0 nodes the loader legitimately omits: the loader-built
   * {@code i=17409} instance carries no Optional TimeFirstChange counter properties (verified
   * against {@code VariableNodeLoader}). R18 forbids minting them, so the value simply has no ns0
   * surface — the fragment-minted per-component counter nodes still expose TimeFirstChange.
   */
  private void attachNs0OptionalFilter(NodeId nodeId, AttributeFilter filter) {
    Optional<UaNode> node = server.getAddressSpaceManager().getManagedNode(nodeId);

    if (node.orElse(null) instanceof UaVariableNode variableNode) {
      variableNode.getFilterChain().addLast(filter);
      ns0Filters.add(new AttachedFilter(variableNode, filter));
    } else {
      LOGGER.debug("optional ns0 diagnostics node not loader-built; skipping: {}", nodeId);
    }
  }

  /** Set the value of an existing ns0 node, remembered for null-restore; never creates. */
  private void setNs0Static(NodeId nodeId, Variant value) {
    Optional<UaNode> node = server.getAddressSpaceManager().getManagedNode(nodeId);

    if (node.orElse(null) instanceof UaVariableNode variableNode) {
      variableNode.setValue(new DataValue(value));
      ns0StaticNodes.add(variableNode);
    } else {
      LOGGER.warn("ns0 diagnostics node not found: {}", nodeId);
    }
  }

  // endregion

  // region root-local counters (D10)

  /**
   * Tick one of the fragment-local root State* counters. Nothing calls this in the current version:
   * the ns0 root Status Enable/Disable pair is not minted (D23) and fragment lifecycle transitions
   * are service lifecycle, not Part 14 §6.2.1 causes — the hook exists for tests and for a future
   * root Enable/Disable wiring (seam S8).
   */
  void tickRootStateCounter(PubSubCounter counter) {
    AtomicLong value = rootCounters.get(counter);
    if (value == null) {
      throw new IllegalArgumentException("not a root State* counter: " + counter);
    }
    value.incrementAndGet();
    rootTimeFirstChange.putIfAbsent(counter, DateTime.now());
  }

  /** Zero the fragment-local root counters and their TimeFirstChange baselines (root Reset). */
  void resetRootCounters() {
    rootCounters.values().forEach(value -> value.set(0));
    rootTimeFirstChange.clear();
  }

  private long rootCounterValue(PubSubCounter counter) {
    AtomicLong value = rootCounters.get(counter);
    return value != null ? value.get() : 0L;
  }

  private long rootClassificationSum(PubSubDiagnosticsCounterClassification classification) {
    long sum = 0;
    for (CounterSpec spec : STATE_COUNTER_SPECS) {
      if (spec.classification() == classification) {
        sum += rootCounterValue(spec.counter());
      }
    }
    return sum;
  }

  private @Nullable DateTime rootEarliestTimeFirstChange(
      PubSubDiagnosticsCounterClassification classification) {

    DateTime earliest = null;
    for (CounterSpec spec : STATE_COUNTER_SPECS) {
      if (spec.classification() == classification) {
        DateTime time = rootTimeFirstChange.get(spec.counter());
        if (time != null && (earliest == null || time.getUtcTime() < earliest.getUtcTime())) {
          earliest = time;
        }
      }
    }
    return earliest;
  }

  private boolean rootSubError() {
    for (RegisteredComponent component : components.values()) {
      if (component.type() == ComponentType.CONNECTION) {
        ComponentDiagnostics diagnostics = diagnosticsOf(component.namePath());
        if (diagnostics != null
            && classificationSum(
                    component.type(), diagnostics, PubSubDiagnosticsCounterClassification.Error)
                > 0) {
          return true;
        }
      }
    }
    return false;
  }

  // endregion

  // region value computation (pull model; pure functions are the S17 test seams)

  /**
   * Saturate a 64-bit engine counter to UInt32 (§9.1.11.5: a counter "shall not be incremented
   * anymore when the maximum is reached").
   */
  static UInteger clampUInt32(long raw) {
    if (raw <= 0) {
      return UInteger.MIN;
    }
    return raw >= UInteger.MAX_VALUE ? UInteger.MAX : uint(raw);
  }

  /**
   * The served {@link DataValue} of a counter read: the clamped value with a SourceTimestamp of
   * now, so the timestamp keeps advancing while the value sits pinned at the cap (§9.1.11.5; the
   * serve-at-read model makes the timestamp the read time — a documented approximation).
   */
  static DataValue counterValue(long raw) {
    return new DataValue(new Variant(clampUInt32(raw)), StatusCode.GOOD, DateTime.now());
  }

  /** A UInt16 LiveValue count, clamped at 65535. */
  static DataValue countValue(long count) {
    UShort value = count >= UShort.MAX_VALUE ? UShort.MAX : ushort((int) Math.max(count, 0L));
    return new DataValue(new Variant(value), StatusCode.GOOD, DateTime.now());
  }

  /**
   * A DataSetMessage sequence number narrowed to the UInt16 LiveValue (Table 329/331); JSON
   * mappings count 32-bit on the wire and are narrowed to the low 16 bits (documented, D12).
   */
  static DataValue sequenceNumberValue(UInteger sequenceNumber) {
    return new DataValue(
        new Variant(ushort(sequenceNumber.intValue() & 0xFFFF)), StatusCode.GOOD, DateTime.now());
  }

  static DataValue timeValue(@Nullable DateTime time) {
    return new DataValue(
        time != null ? new Variant(time) : Variant.NULL_VALUE, StatusCode.GOOD, DateTime.now());
  }

  static DataValue booleanValue(boolean value) {
    return new DataValue(new Variant(value), StatusCode.GOOD, DateTime.now());
  }

  private static DataValue goodValue(Variant value) {
    return new DataValue(value, StatusCode.GOOD, DateTime.now());
  }

  /** A null-value read: the feed is empty or the component vanished (S5). */
  private static DataValue nullValue() {
    return new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, DateTime.now());
  }

  /**
   * The spec-exposed counters of one component subtype (§4.3 mapping): the six State* counters on
   * every object, plus the per-subtype rows. Vendor counters stay SDK-API-only (R14); the DSR
   * FailedDataSetMessages row maps to the engine's per-reader {@code decodeErrors} substance.
   */
  static List<CounterSpec> counterSpecs(ComponentType type) {
    var specs = new ArrayList<>(STATE_COUNTER_SPECS);

    switch (type) {
      case WRITER_GROUP -> {
        specs.add(
            new CounterSpec(
                "SentNetworkMessages",
                PubSubCounter.NETWORK_MESSAGES_SENT,
                PubSubDiagnosticsCounterClassification.Information,
                DiagnosticsLevel.Basic));
        specs.add(
            new CounterSpec(
                "FailedTransmissions",
                PubSubCounter.FAILED_TRANSMISSIONS,
                PubSubDiagnosticsCounterClassification.Error,
                DiagnosticsLevel.Basic));
        specs.add(
            new CounterSpec(
                "EncryptionErrors",
                PubSubCounter.ENCRYPTION_ERRORS,
                PubSubDiagnosticsCounterClassification.Error,
                DiagnosticsLevel.Advanced));
      }
      case READER_GROUP -> {
        specs.add(
            new CounterSpec(
                "ReceivedNetworkMessages",
                PubSubCounter.NETWORK_MESSAGES_RECEIVED,
                PubSubDiagnosticsCounterClassification.Information,
                DiagnosticsLevel.Basic));
        specs.add(
            new CounterSpec(
                "DecryptionErrors",
                PubSubCounter.DECRYPTION_ERRORS,
                PubSubDiagnosticsCounterClassification.Error,
                DiagnosticsLevel.Advanced));
      }
      case DATA_SET_WRITER ->
          specs.add(
              new CounterSpec(
                  "FailedDataSetMessages",
                  PubSubCounter.FAILED_DATA_SET_MESSAGES,
                  PubSubDiagnosticsCounterClassification.Error,
                  DiagnosticsLevel.Basic));
      case DATA_SET_READER ->
          specs.add(
              new CounterSpec(
                  "FailedDataSetMessages",
                  PubSubCounter.DECODE_ERRORS,
                  PubSubDiagnosticsCounterClassification.Error,
                  DiagnosticsLevel.Basic));
      default -> {}
    }

    return specs;
  }

  /** The engine field of one exposed counter identity. */
  static long counterOf(ComponentDiagnostics diagnostics, PubSubCounter counter) {
    return switch (counter) {
      case NETWORK_MESSAGES_SENT -> diagnostics.networkMessagesSent();
      case NETWORK_MESSAGES_RECEIVED -> diagnostics.networkMessagesReceived();
      case DECODE_ERRORS -> diagnostics.decodeErrors();
      case ENCRYPTION_ERRORS -> diagnostics.encryptionErrors();
      case DECRYPTION_ERRORS -> diagnostics.decryptionErrors();
      case FAILED_TRANSMISSIONS -> diagnostics.failedTransmissions();
      case FAILED_DATA_SET_MESSAGES -> diagnostics.failedDataSetMessages();
      case STATE_ERROR -> diagnostics.stateError();
      case STATE_OPERATIONAL_BY_METHOD -> diagnostics.stateOperationalByMethod();
      case STATE_OPERATIONAL_BY_PARENT -> diagnostics.stateOperationalByParent();
      case STATE_OPERATIONAL_FROM_ERROR -> diagnostics.stateOperationalFromError();
      case STATE_PAUSED_BY_PARENT -> diagnostics.statePausedByParent();
      case STATE_DISABLED_BY_METHOD -> diagnostics.stateDisabledByMethod();
      default -> throw new IllegalArgumentException("not an exposed counter: " + counter);
    };
  }

  /**
   * TotalInformation/TotalError substance: the sum of the object's OWN spec-exposed counters with
   * {@code classification} (§9.1.11.2 — own Counters folder only, never descendants; vendor
   * counters do not participate, R14).
   */
  static long classificationSum(
      ComponentType type,
      ComponentDiagnostics diagnostics,
      PubSubDiagnosticsCounterClassification classification) {

    long sum = 0;
    for (CounterSpec spec : counterSpecs(type)) {
      if (spec.classification() == classification) {
        sum += counterOf(diagnostics, spec.counter());
      }
    }
    return sum;
  }

  /** D37: Total* TimeFirstChange = the earliest contributing counter's, null while all zero. */
  static @Nullable DateTime earliestTimeFirstChange(
      ComponentType type,
      ComponentDiagnostics diagnostics,
      PubSubDiagnosticsCounterClassification classification) {

    return counterSpecs(type).stream()
        .filter(spec -> spec.classification() == classification)
        .map(spec -> diagnostics.timeFirstChange(spec.counter()))
        .filter(Objects::nonNull)
        .min(Comparator.comparingLong(DateTime::getUtcTime))
        .orElse(null);
  }

  /**
   * True iff {@code child} is in {@code parent}'s DIRECT child layer (§9.1.11.2 "the next PubSub
   * layer"): connection &rarr; its groups, group &rarr; its writers/readers. Matching is by name
   * components, never by path parsing.
   */
  static boolean isDirectChildOf(RegisteredComponent child, RegisteredComponent parent) {
    return switch (parent.type()) {
      case CONNECTION ->
          (child.type() == ComponentType.WRITER_GROUP || child.type() == ComponentType.READER_GROUP)
              && child.connectionName().equals(parent.connectionName());
      case WRITER_GROUP ->
          child.type() == ComponentType.DATA_SET_WRITER
              && child.connectionName().equals(parent.connectionName())
              && Objects.equals(child.groupName(), parent.groupName());
      case READER_GROUP ->
          child.type() == ComponentType.DATA_SET_READER
              && child.connectionName().equals(parent.connectionName())
              && Objects.equals(child.groupName(), parent.groupName());
      default -> false;
    };
  }

  /**
   * R15: the ResolvedAddress LiveValue. Datagram URLs ({@code opc.udp://...}) get a best-effort
   * hostname-to-IP substitution — resolution runs at node-build time, never on sampler threads;
   * failure serves the configured URL as-is. Broker URLs (detected by scheme string, never by
   * importing transport modules) and everything else are served verbatim. A documented
   * approximation; a transport-SPI resolved-address accessor is a later follow-up.
   */
  static @Nullable String resolvedAddress(@Nullable String url) {
    if (url == null || !url.regionMatches(true, 0, "opc.udp://", 0, "opc.udp://".length())) {
      return url;
    }

    try {
      var uri = new URI(url);
      String host = uri.getHost();
      if (host == null) {
        return url;
      }

      String resolved = InetAddress.getByName(host).getHostAddress();
      if (resolved.equals(host)) {
        return url;
      }

      return new URI(
              uri.getScheme(),
              uri.getUserInfo(),
              resolved,
              uri.getPort(),
              uri.getPath(),
              uri.getQuery(),
              uri.getFragment())
          .toString();
    } catch (Exception e) {
      return url;
    }
  }

  // endregion

  // region filters and registry queries

  /** One per-path snapshot per read (D49); null while the engine holds no entry for the path. */
  private @Nullable ComponentDiagnostics diagnosticsOf(String path) {
    return service.diagnostics().component(path).orElse(null);
  }

  private AttributeFilter counterValueFilter(String path, PubSubCounter counter) {
    return AttributeFilters.getValue(
        ctx -> {
          ComponentDiagnostics diagnostics = diagnosticsOf(path);
          return counterValue(diagnostics != null ? counterOf(diagnostics, counter) : 0L);
        });
  }

  private AttributeFilter counterTimeFirstChangeFilter(String path, PubSubCounter counter) {
    return AttributeFilters.getValue(
        ctx -> {
          ComponentDiagnostics diagnostics = diagnosticsOf(path);
          return timeValue(diagnostics != null ? diagnostics.timeFirstChange(counter) : null);
        });
  }

  private AttributeFilter totalValueFilter(
      ComponentType type, String path, PubSubDiagnosticsCounterClassification classification) {

    return AttributeFilters.getValue(
        ctx -> {
          ComponentDiagnostics diagnostics = diagnosticsOf(path);
          return counterValue(
              diagnostics != null ? classificationSum(type, diagnostics, classification) : 0L);
        });
  }

  private AttributeFilter totalTimeFirstChangeFilter(
      ComponentType type, String path, PubSubDiagnosticsCounterClassification classification) {

    return AttributeFilters.getValue(
        ctx -> {
          ComponentDiagnostics diagnostics = diagnosticsOf(path);
          return timeValue(
              diagnostics != null
                  ? earliestTimeFirstChange(type, diagnostics, classification)
                  : null);
        });
  }

  /** SubError of a container object: any direct child's TotalError &gt; 0. */
  private boolean subError(RegisteredComponent parent) {
    for (RegisteredComponent child : components.values()) {
      if (isDirectChildOf(child, parent)) {
        ComponentDiagnostics diagnostics = diagnosticsOf(child.namePath());
        if (diagnostics != null
            && classificationSum(
                    child.type(), diagnostics, PubSubDiagnosticsCounterClassification.Error)
                > 0) {
          return true;
        }
      }
    }
    return false;
  }

  /** Registry count of all components of {@code type}, optionally Operational-only. */
  private long countByType(ComponentType type, boolean operationalOnly) {
    long count = 0;
    for (RegisteredComponent component : components.values()) {
      if (component.type() == type && (!operationalOnly || isOperational(component.namePath()))) {
        count++;
      }
    }
    return count;
  }

  /** Registry count of {@code parent}'s children of {@code type}, optionally Operational-only. */
  private long countChildren(
      ComponentType type, RegisteredComponent parent, boolean operationalOnly) {
    long count = 0;
    for (RegisteredComponent component : components.values()) {
      if (component.type() == type
          && isDirectChildOf(component, parent)
          && (!operationalOnly || isOperational(component.namePath()))) {
        count++;
      }
    }
    return count;
  }

  private boolean isOperational(String path) {
    return statesByPath.get(path) == PubSubState.Operational;
  }

  private PubSubState currentState(RegisteredComponent component) {
    try {
      return handleLookup(component).get().map(service::state).orElse(PubSubState.Disabled);
    } catch (IllegalArgumentException e) {
      return PubSubState.Disabled;
    }
  }

  private Supplier<Optional<PubSubHandle>> handleLookup(RegisteredComponent component) {
    return switch (component.type()) {
      case CONNECTION -> () -> service.components().connection(component.connectionName());
      case WRITER_GROUP -> writerGroupHandleLookup(component);
      case READER_GROUP -> readerGroupHandleLookup(component);
      case DATA_SET_WRITER ->
          () ->
              service
                  .components()
                  .dataSetWriter(
                      component.connectionName(),
                      requireName(component.groupName()),
                      requireName(component.componentName()));
      case DATA_SET_READER ->
          () ->
              service
                  .components()
                  .dataSetReader(
                      component.connectionName(),
                      requireName(component.groupName()),
                      requireName(component.componentName()));
      default -> Optional::empty;
    };
  }

  private Supplier<Optional<PubSubHandle>> writerGroupHandleLookup(RegisteredComponent component) {
    return () ->
        service
            .components()
            .writerGroup(component.connectionName(), requireName(component.groupName()));
  }

  private Supplier<Optional<PubSubHandle>> readerGroupHandleLookup(RegisteredComponent component) {
    return () ->
        service
            .components()
            .readerGroup(component.connectionName(), requireName(component.groupName()));
  }

  private static String requireName(@Nullable String name) {
    if (name == null) {
      throw new IllegalArgumentException("missing name component");
    }
    return name;
  }

  private @Nullable ConfigurationVersionDataType writerDataSetVersion(
      RegisteredComponent component, PubSubConfiguration2DataType configuration) {

    WriterGroupDataType group =
        PubSubInfoModelFragment.findWriterGroup(
            PubSubInfoModelFragment.findConnection(configuration, component.connectionName()),
            component.groupName());

    DataSetWriterDataType writer =
        group != null
            ? PubSubInfoModelFragment.findDataSetWriter(group, component.componentName())
            : null;

    if (writer == null || writer.getDataSetName() == null) {
      return null;
    }

    PublishedDataSetDataType dataSet =
        PubSubInfoModelFragment.findPublishedDataSet(configuration, writer.getDataSetName());

    DataSetMetaDataType metaData = dataSet != null ? dataSet.getDataSetMetaData() : null;
    return metaData != null ? metaData.getConfigurationVersion() : null;
  }

  /** Secured per D47: a Sign/SignAndEncrypt mode together with a SecurityGroup reference. */
  static boolean secured(@Nullable MessageSecurityMode mode, @Nullable String securityGroupId) {
    return (mode == MessageSecurityMode.Sign || mode == MessageSecurityMode.SignAndEncrypt)
        && securityGroupId != null;
  }

  private static boolean isTrackedComponentType(ComponentType componentType) {
    return switch (componentType) {
      case CONNECTION, WRITER_GROUP, DATA_SET_WRITER, READER_GROUP, DATA_SET_READER -> true;
      default -> false;
    };
  }

  private static NodeId typeDefinitionId(ComponentType type) {
    return switch (type) {
      case CONNECTION -> NodeIds.PubSubDiagnosticsConnectionType;
      case WRITER_GROUP -> NodeIds.PubSubDiagnosticsWriterGroupType;
      case READER_GROUP -> NodeIds.PubSubDiagnosticsReaderGroupType;
      case DATA_SET_WRITER -> NodeIds.PubSubDiagnosticsDataSetWriterType;
      case DATA_SET_READER -> NodeIds.PubSubDiagnosticsDataSetReaderType;
      default -> throw new IllegalArgumentException("no diagnostics type for: " + type);
    };
  }

  private static PubSubInfoModelFragment.ObjectNodeConstructor<? extends UaObjectNode>
      objectNodeConstructor(ComponentType type) {
    return switch (type) {
      case CONNECTION -> PubSubDiagnosticsConnectionTypeNode::new;
      case WRITER_GROUP -> PubSubDiagnosticsWriterGroupTypeNode::new;
      case READER_GROUP -> PubSubDiagnosticsReaderGroupTypeNode::new;
      case DATA_SET_WRITER -> PubSubDiagnosticsDataSetWriterTypeNode::new;
      case DATA_SET_READER -> PubSubDiagnosticsDataSetReaderTypeNode::new;
      default -> throw new IllegalArgumentException("no diagnostics type for: " + type);
    };
  }

  // endregion

  // region Reset handlers

  /**
   * The shared session &rarr; authorization prefix (D29/D41/D38: session-less &rarr; {@code
   * Bad_UserAccessDenied}; the effective authorizer's code surfaced verbatim; no channel-security
   * minimum). With a RoleMapper configured, the ns0 {@code i=17421} node's loader RolePermissions
   * (ConfigureAdmin) additionally gate the Call in the AccessController before the handler runs;
   * fragment-minted Reset nodes carry no RolePermissions — the authorizer is their gate.
   */
  private void checkResetAuthorized(InvocationContext context) throws UaException {

    Session session =
        context.getSession().orElseThrow(() -> new UaException(StatusCodes.Bad_UserAccessDenied));

    StatusCode checkResult = authorizer.checkConfigure(session);
    if (checkResult.isBad()) {
      throw new UaException(checkResult);
    }
  }

  /**
   * Per-component Reset: delegates to the engine's per-object {@code reset(path)} — that path's
   * counters and TimeFirstChange baselines only; {@code lastError}, {@code targetWriteErrors()},
   * and other objects' counters untouched (§9.1.11.3, R12).
   */
  private final class ComponentResetHandler extends PubSubDiagnosticsType.ResetMethod {

    private final String path;

    ComponentResetHandler(UaMethodNode node, String path) {
      super(node);
      this.path = path;
    }

    @Override
    protected void invoke(InvocationContext context) throws UaException {
      checkResetAuthorized(context);
      service.diagnostics().reset(path);
    }
  }

  /**
   * Root Reset ({@code i=17421}): zeroes the fragment-local root counters only — the root has no
   * engine path and the engine collector is never called (D10).
   */
  private final class RootResetHandler extends PubSubDiagnosticsType.ResetMethod {

    RootResetHandler(UaMethodNode node) {
      super(node);
    }

    @Override
    protected void invoke(InvocationContext context) throws UaException {
      checkResetAuthorized(context);
      resetRootCounters();
    }
  }

  // endregion

  /** One exposed counter row: browse name, engine identity, and D37/§4.3 properties. */
  record CounterSpec(
      String browseName,
      PubSubCounter counter,
      PubSubDiagnosticsCounterClassification classification,
      DiagnosticsLevel level) {}

  /** Name components staged by a fragment builder for the imminent build notification. */
  private record StagedCoordinates(
      String connectionName, @Nullable String groupName, @Nullable String componentName) {}

  /**
   * A component with a minted Diagnostics object. Coordinates are carried as name components (a
   * connection's {@code groupName}/{@code componentName} are null; a group's {@code componentName}
   * is null) — paths are never parsed (R11).
   */
  record RegisteredComponent(
      ComponentType type,
      String namePath,
      String connectionName,
      @Nullable String groupName,
      @Nullable String componentName) {}

  private record AttachedFilter(UaVariableNode node, AttributeFilter filter) {}
}
