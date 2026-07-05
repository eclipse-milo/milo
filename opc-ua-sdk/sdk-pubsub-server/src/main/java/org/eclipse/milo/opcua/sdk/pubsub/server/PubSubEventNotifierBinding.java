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

import static java.util.Objects.requireNonNullElse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedEventsConfig;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.events.EventContentFilter;
import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElementResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridges the server's global event bus into the PubSub runtime: a single {@link EventListener}
 * that, for every event fired through {@code server.getEventNotifier()}, evaluates each configured
 * {@code PublishedEvents} dataset's {@code EventFilter} against the event and pushes the selected
 * fields to {@link PubSubService#publishEvent}.
 *
 * <p><b>One listener, many datasets.</b> A single instance is registered once with the server's
 * event notifier for the whole {@link ServerPubSub} lifetime; it is <em>not</em> registered per
 * dataset. The set of datasets it serves is held in a {@code volatile} map of {@link
 * DatasetBinding} keyed by dataset name, rebuilt wholesale from the applied {@link PubSubConfig}
 * both at {@link #startup(PubSubConfig)} and after every successful reconfigure — {@link
 * #onConfigurationApplied(PubSubConfig, ReconfigureResult)} runs as a post-apply {@code
 * ReconfigureHook} registered immediately after {@code ServerPubSub.rederiveBindings}.
 *
 * <p><b>Degrade-to-inactive posture.</b> Building a binding never fails startup or a reconfigure: a
 * dataset whose notifier or filter cannot be honored yields an <em>inactive</em> binding (logged
 * once at build time), which is retained in the map but skipped by {@link #onEvent}. This mirrors
 * {@link AddressSpacePublishedDataSetSource}'s missing-node tolerance. Sources of inactivity:
 *
 * <ul>
 *   <li>the {@code EventNotifier} {@code ExpandedNodeId} does not resolve against the server {@link
 *       NamespaceTable} (also caught eagerly at attach/reconfigure by {@code
 *       ServerPubSub.validateNodeFieldAddresses}, so this branch guards only reconfigure races);
 *   <li>the resolved notifier is not a managed {@code Object} node, or its {@code EventNotifier}
 *       attribute lacks the {@code SubscribeToEvents} bit ({@code (bits & 1) != 0}, the {@code
 *       SubscriptionManager} rule);
 *   <li>the {@code EventFilter} does not validate as fully Good. The exact active gate (matching
 *       {@code MonitoredEventItem.installFilter} plus two checks {@code
 *       EventContentFilter.validate} does not make) requires all of: (a) every select-clause {@link
 *       StatusCode} and every where-clause element {@link StatusCode} is Good; (b) every decoded
 *       {@link ElementOperand} index is in bounds for the where-clause element array ({@code
 *       validate} does not bounds-check, but {@code evaluate} would throw {@code
 *       ArrayIndexOutOfBounds}); (c) the where-clause root element's operator is Boolean-producing
 *       ({@code evaluate} throws {@code Bad_ContentFilterInvalid} for a non-Boolean root such as
 *       {@code Cast});
 *   <li>a zero-field dataset — {@code validate} rejects an empty select-clause list, so a legal
 *       zero-field {@code PublishedEvents} dataset is simply inactive (documented, not an error).
 * </ul>
 *
 * <p><b>Scope.</b> Each active binding carries a precomputed source scope. A notifier of the {@code
 * Server} object ({@code i=2253}) is <em>unscoped</em> (scope {@code null}): every event passes,
 * because every Milo producer fires with Server-object semantics. Any other notifier is scoped to
 * the set of nodes reachable by walking forward references whose type is a subtype of {@code
 * HasEventSource} (covering {@code HasNotifier}), recursively, plus the notifier node itself;
 * {@link #onEvent} then admits an event only when its {@code SourceNode} is in that set. The walk
 * uses managed node managers only (address spaces registered as {@code NodeManager}s). Topology
 * changes are picked up on the next reconfigure only.
 *
 * <p><b>Threading and lifecycle.</b> {@link #startup(PubSubConfig)} and {@link #shutdown()} are
 * driven by {@code ServerPubSub} under its {@code lifecycleLock} with a {@code FaceState} guard, so
 * they are serialized with each other; startup builds the initial bindings then registers the
 * listener, shutdown deregisters it and clears the bindings. {@link #onEvent} runs
 * <em>synchronously on the arbitrary producer thread</em> that called {@code fire} (the server
 * event bus has no error isolation and does not extend the event node's lifetime), so it catches
 * {@link Throwable} around its whole body, never blocks, and selects fields synchronously — the
 * transient event node is valid only for the duration of the call. {@link
 * PubSubService#publishEvent} is lock-free, so calling it from {@code onEvent} introduces no
 * lock-order hazard. A per-binding evaluate/select failure deactivates that one binding with a
 * single WARN (no per-event log spam); the binding is revived by the next reconfigure's rebuild.
 *
 * <p><b>Namespace timing.</b> Notifier nodes and custom event types living in namespaces registered
 * <em>after</em> {@code ServerPubSub.startup()} are not visible to the startup build, so their
 * bindings stay inactive until the next reconfigure rebuilds them (register such namespaces before
 * startup, or reconfigure afterward). Firing selected fields additionally requires a started server
 * (the {@code EventFactory} lifecycle), the same requirement documented for {@code
 * statusEventsEnabled}.
 */
final class PubSubEventNotifierBinding implements EventListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubEventNotifierBinding.class);

  /**
   * The where-clause root operators whose {@code evaluate} result is a {@link Boolean}. All other
   * supported operators (only {@code Cast}) return a value, for which {@code
   * EventContentFilter.evaluate} throws {@code Bad_ContentFilterInvalid} when used as the root
   * element; unsupported operators never reach this check (they fail the fully-Good gate first).
   */
  private static final Set<FilterOperator> BOOLEAN_ROOT_OPERATORS =
      Set.of(
          FilterOperator.Equals,
          FilterOperator.IsNull,
          FilterOperator.GreaterThan,
          FilterOperator.LessThan,
          FilterOperator.GreaterThanOrEqual,
          FilterOperator.LessThanOrEqual,
          FilterOperator.Not,
          FilterOperator.OfType);

  private final OpcUaServer server;
  private final PubSubService service;
  private final NamespaceTable namespaceTable;
  private final FilterContext filterContext;

  /** Guards in-flight {@link #onEvent} deliveries after {@link #shutdown()}. */
  private volatile boolean active = false;

  /**
   * The active/inactive bindings by dataset name; rebuilt wholesale (never structurally mutated in
   * place) so a reader takes a consistent snapshot with a single volatile read. The {@link
   * DatasetBinding} objects it references are themselves mutable only through {@code deactivate()}.
   */
  private volatile Map<String, DatasetBinding> bindings = Map.of();

  PubSubEventNotifierBinding(OpcUaServer server, PubSubService service) {
    this.server = server;
    this.service = service;
    this.namespaceTable = server.getNamespaceTable();

    this.filterContext =
        new FilterContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public Optional<Session> getSession() {
            return Optional.empty();
          }
        };
  }

  /**
   * Build the initial bindings from {@code config} and register the listener; reciprocal of {@link
   * #shutdown()}. Called by {@code ServerPubSub.startup()} under its {@code lifecycleLock}.
   *
   * @param config the applied configuration to derive bindings from.
   */
  void startup(PubSubConfig config) {
    rebuild(config);
    active = true;
    server.getEventNotifier().register(this);
  }

  /**
   * Deregister the listener and clear the bindings. Called by {@code ServerPubSub.shutdown()} in
   * the {@code service.shutdown().whenComplete} block; there is no drain dependency, so it may run
   * before or alongside the other faces.
   */
  void shutdown() {
    active = false;
    server.getEventNotifier().unregister(this);
    bindings = Map.of();
  }

  /**
   * Post-apply {@code ReconfigureHook}: rebuild the binding map from the newly applied
   * configuration. Registered immediately after {@code ServerPubSub.rederiveBindings}.
   *
   * @param newConfig the configuration that was just applied.
   * @param result the reconfigure result (unused; bindings are re-derived wholesale from {@code
   *     newConfig}).
   */
  void onConfigurationApplied(PubSubConfig newConfig, ReconfigureResult result) {
    rebuild(newConfig);
  }

  @Override
  public void onEvent(BaseEventTypeNode node) {
    if (!active) {
      return;
    }

    // the whole body is wrapped: the bus invokes listeners synchronously with no error isolation,
    // so nothing may escape into the producer thread
    try {
      Map<String, DatasetBinding> currentBindings = this.bindings;
      if (currentBindings.isEmpty()) {
        return;
      }

      NodeId sourceNode = node.getSourceNode();

      for (DatasetBinding binding : currentBindings.values()) {
        if (!binding.active()) {
          continue;
        }

        Set<NodeId> scope = binding.scope();
        if (scope != null && (sourceNode == null || !scope.contains(sourceNode))) {
          // event source lies outside this notifier's HasEventSource subtree
          continue;
        }

        try {
          boolean matches = EventContentFilter.evaluate(filterContext, binding.whereClause(), node);
          if (!matches) {
            continue;
          }

          // select synchronously: the transient event node is valid only during this call
          Variant[] fields =
              EventContentFilter.select(filterContext, binding.selectClauses(), node);

          service.publishEvent(binding.ref(), Arrays.asList(fields));
        } catch (Throwable t) {
          // isolate the failing binding; deactivate it and warn exactly once (no per-event spam).
          // the next reconfigure rebuild revives it if the cause was transient
          binding.deactivate();
          if (binding.warnOnRuntimeFailure()) {
            LOGGER.warn(
                "Deactivating event dataset binding '{}' after an event evaluation/selection"
                    + " failure",
                binding.ref().name(),
                t);
          }
        }
      }
    } catch (Throwable t) {
      LOGGER.warn("Unexpected error dispatching a server event to PubSub event datasets", t);
    }
  }

  /** The current binding map (active and inactive), by dataset name. Package-private for tests. */
  Map<String, DatasetBinding> bindings() {
    return bindings;
  }

  private void rebuild(PubSubConfig config) {
    var newBindings = new LinkedHashMap<String, DatasetBinding>();

    for (PublishedDataSetConfig dataSet : config.publishedDataSets()) {
      if (dataSet.getSource() instanceof PublishedEventsConfig events) {
        newBindings.put(dataSet.getName(), buildBinding(dataSet, events));
      }
    }

    this.bindings = Map.copyOf(newBindings);
  }

  /**
   * Build one dataset binding, degrading to an inactive binding (with a single WARN) on any
   * unresolvable notifier, notifier that cannot report events, or non-fully-Good filter.
   */
  private DatasetBinding buildBinding(
      PublishedDataSetConfig dataSet, PublishedEventsConfig events) {
    PublishedDataSetRef ref = dataSet.ref();

    SimpleAttributeOperand[] selectClauses =
        events.getFields().stream()
            .map(EventFieldDefinition::getSelectedField)
            .toArray(SimpleAttributeOperand[]::new);
    ContentFilter whereClause = events.getFilter();

    NodeId notifierNodeId = events.getEventNotifier().toNodeId(namespaceTable).orElse(null);
    if (notifierNodeId == null) {
      LOGGER.warn(
          "Event dataset '{}' is inactive: cannot resolve event notifier {} against the server"
              + " NamespaceTable",
          dataSet.getName(),
          events.getEventNotifier());
      return DatasetBinding.inactive(ref, null, selectClauses, whereClause);
    }

    UaNode notifierNode =
        server.getAddressSpaceManager().getManagedNode(notifierNodeId).orElse(null);
    if (!(notifierNode instanceof UaObjectNode objectNode)) {
      LOGGER.warn(
          "Event dataset '{}' is inactive: event notifier {} is not a managed Object node",
          dataSet.getName(),
          notifierNodeId);
      return DatasetBinding.inactive(ref, notifierNodeId, selectClauses, whereClause);
    }

    UByte eventNotifier = objectNode.getEventNotifier();
    if (eventNotifier == null || (eventNotifier.intValue() & 1) == 0) {
      LOGGER.warn(
          "Event dataset '{}' is inactive: event notifier {} does not have the SubscribeToEvents"
              + " bit set",
          dataSet.getName(),
          notifierNodeId);
      return DatasetBinding.inactive(ref, notifierNodeId, selectClauses, whereClause);
    }

    EventFilterResult filterResult;
    try {
      filterResult =
          EventContentFilter.validate(filterContext, new EventFilter(selectClauses, whereClause));
    } catch (UaException e) {
      // validate() throws only for an empty/absent select-clause list, i.e. a zero-field dataset
      LOGGER.warn(
          "Event dataset '{}' is inactive: event filter is invalid ({})",
          dataSet.getName(),
          e.getMessage());
      return DatasetBinding.inactive(ref, notifierNodeId, selectClauses, whereClause);
    }

    if (!filterResultFullyGood(filterResult)) {
      LOGGER.warn(
          "Event dataset '{}' is inactive: a select clause or where-clause element did not"
              + " validate as Good",
          dataSet.getName());
      return DatasetBinding.inactive(ref, notifierNodeId, selectClauses, whereClause);
    }

    if (!elementOperandsWithinBounds(whereClause)) {
      LOGGER.warn(
          "Event dataset '{}' is inactive: a where-clause ElementOperand index is out of bounds",
          dataSet.getName());
      return DatasetBinding.inactive(ref, notifierNodeId, selectClauses, whereClause);
    }

    if (!rootOperatorIsBooleanProducing(whereClause)) {
      LOGGER.warn(
          "Event dataset '{}' is inactive: the where-clause root operator is not"
              + " Boolean-producing",
          dataSet.getName());
      return DatasetBinding.inactive(ref, notifierNodeId, selectClauses, whereClause);
    }

    Set<NodeId> scope = computeScope(notifierNodeId);
    return new DatasetBinding(ref, notifierNodeId, selectClauses, whereClause, scope, true);
  }

  /**
   * The {@code MonitoredEventItem.installFilter} goodness rule: every select-clause and every
   * where-clause element result is Good.
   */
  private static boolean filterResultFullyGood(EventFilterResult result) {
    StatusCode[] selectResults =
        requireNonNullElse(result.getSelectClauseResults(), new StatusCode[0]);
    boolean selectGood = Arrays.stream(selectResults).allMatch(StatusCode::isGood);

    ContentFilterResult whereResult = result.getWhereClauseResult();
    ContentFilterElementResult[] elementResults =
        requireNonNullElse(whereResult.getElementResults(), new ContentFilterElementResult[0]);
    boolean whereGood =
        Arrays.stream(elementResults)
            .map(ContentFilterElementResult::getStatusCode)
            .allMatch(StatusCode::isGood);

    return selectGood && whereGood;
  }

  /**
   * True unless a decoded {@link ElementOperand} references a where-clause element index that is
   * out of bounds — {@code EventContentFilter.validate} does not bounds-check operand indices, but
   * {@code evaluate} would throw {@code ArrayIndexOutOfBounds} on such a filter.
   */
  private boolean elementOperandsWithinBounds(ContentFilter whereClause) {
    ContentFilterElement[] elements = whereClause.getElements();
    if (elements == null || elements.length == 0) {
      return true;
    }

    EncodingContext encodingContext = server.getStaticEncodingContext();

    for (ContentFilterElement element : elements) {
      ExtensionObject[] operands = element.getFilterOperands();
      if (operands == null) {
        continue;
      }
      for (ExtensionObject operandXo : operands) {
        Object operand;
        try {
          operand = operandXo.decode(encodingContext);
        } catch (Exception e) {
          // an undecodable operand also fails the fully-Good gate; degrade to inactive
          return false;
        }
        if (operand instanceof ElementOperand elementOperand) {
          long index = elementOperand.getIndex().longValue();
          if (index < 0 || index >= elements.length) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * True when the where-clause is empty (matches every event) or its root element's operator
   * produces a {@link Boolean} — the type {@code EventContentFilter.evaluate} requires of the root.
   */
  private static boolean rootOperatorIsBooleanProducing(ContentFilter whereClause) {
    ContentFilterElement[] elements = whereClause.getElements();
    if (elements == null || elements.length == 0) {
      return true;
    }
    return BOOLEAN_ROOT_OPERATORS.contains(elements[0].getFilterOperator());
  }

  /**
   * Compute the source scope of {@code notifierNodeId}: {@code null} (unscoped) for the {@code
   * Server} object, otherwise the set of nodes reachable via forward {@code HasEventSource}-subtype
   * references (recursively), including the notifier node itself.
   */
  private @Nullable Set<NodeId> computeScope(NodeId notifierNodeId) {
    if (NodeIds.Server.equals(notifierNodeId)) {
      return null;
    }

    var scope = new HashSet<NodeId>();
    collectScope(notifierNodeId, scope);
    return scope;
  }

  private void collectScope(NodeId nodeId, Set<NodeId> scope) {
    if (!scope.add(nodeId)) {
      return; // already visited (also breaks reference cycles)
    }

    ReferenceTypeTree referenceTypeTree = server.getReferenceTypeTree();

    List<Reference> references =
        server
            .getAddressSpaceManager()
            .getManagedReferences(
                nodeId,
                reference ->
                    reference.isForward()
                        && referenceTypeTree.isSubtypeOf(
                            reference.getReferenceTypeId(), NodeIds.HasEventSource));

    for (Reference reference : references) {
      reference
          .getTargetNodeId()
          .toNodeId(namespaceTable)
          .ifPresent(target -> collectScope(target, scope));
    }
  }

  /**
   * One resolved {@code PublishedEvents} dataset the notifier binding serves: the dataset ref, the
   * select clauses and where-clause to run per event, the precomputed source scope ({@code null} =
   * unscoped), and an active flag flipped to inactive on a runtime evaluate/select failure.
   */
  static final class DatasetBinding {

    private final PublishedDataSetRef ref;
    private final @Nullable NodeId notifierNodeId;
    private final SimpleAttributeOperand[] selectClauses;
    private final ContentFilter whereClause;
    private final @Nullable Set<NodeId> scope;
    private final AtomicBoolean runtimeFailureWarned = new AtomicBoolean(false);
    private volatile boolean active;

    DatasetBinding(
        PublishedDataSetRef ref,
        @Nullable NodeId notifierNodeId,
        SimpleAttributeOperand[] selectClauses,
        ContentFilter whereClause,
        @Nullable Set<NodeId> scope,
        boolean active) {

      this.ref = ref;
      this.notifierNodeId = notifierNodeId;
      this.selectClauses = selectClauses;
      this.whereClause = whereClause;
      this.scope = scope;
      this.active = active;
    }

    static DatasetBinding inactive(
        PublishedDataSetRef ref,
        @Nullable NodeId notifierNodeId,
        SimpleAttributeOperand[] selectClauses,
        ContentFilter whereClause) {

      return new DatasetBinding(ref, notifierNodeId, selectClauses, whereClause, null, false);
    }

    PublishedDataSetRef ref() {
      return ref;
    }

    @Nullable NodeId notifierNodeId() {
      return notifierNodeId;
    }

    SimpleAttributeOperand[] selectClauses() {
      return selectClauses;
    }

    ContentFilter whereClause() {
      return whereClause;
    }

    @Nullable Set<NodeId> scope() {
      return scope;
    }

    boolean active() {
      return active;
    }

    void deactivate() {
      active = false;
    }

    /** CAS the once-only runtime-failure warning latch; {@code true} the first time only. */
    boolean warnOnRuntimeFailure() {
      return runtimeFailureWarned.compareAndSet(false, true);
    }
  }
}
