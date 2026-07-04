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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCommunicationFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusEventTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Part 14 §9.1.13 PubSub status-event bridge (pinned decision R17): bridges the runtime's
 * {@link PubSubStateListener} and {@link PubSubDiagnosticsListener} feeds to OPC UA events fired
 * through the server's {@code EventFactory} and event notifier.
 *
 * <p><b>Event types.</b> State changes of the §9.1.13.1 source set — connections, writer/reader
 * groups, and dataset writers/readers; never the PublishSubscribe root, PublishedDataSets,
 * standalone SubscribedDataSets, or SecurityGroups — fire events typed {@code
 * PubSubStatusEventType} ({@code i=15535}). The type is abstract; abstract EventTypes are
 * reportable (Part 3 §4.7.2, only address-space instantiation is forbidden) and the D35 spike
 * verified end-to-end delivery through server-side {@code OfType} filters. NetworkMessage
 * transmission failures — {@link PubSubDiagnosticsEvent}s of kind {@link
 * PubSubDiagnosticsEvent.Kind#SEND_FAILURE} only (D6); transport-down edges arrive as state
 * transitions, not diagnostics events — fire {@code PubSubCommunicationFailureEventType} ({@code
 * i=15563}) events carrying the real channel status code. Both type ids are single constants
 * ({@link #STATE_CHANGE_EVENT_TYPE_ID}, {@link #COMMUNICATION_FAILURE_EVENT_TYPE_ID}) so the
 * concrete-vendor-subtype fallback, should a client ever reject abstract-typed notifications, is a
 * one-line flip (D35).
 *
 * <p><b>Field derivation.</b> {@code SourceNode}, {@code ConnectionId}, and {@code GroupId} carry
 * the deterministic {@link PubSubNodeIds} scheme ids ({@code "PubSub/<path>"} in the server
 * application namespace) whether or not the information model is exposed (D32 — without the exposed
 * model they reference non-materialized NodeIds, documented on {@link
 * ServerPubSubOptions.Builder#statusEventsEnabled}). {@code GroupId} is {@link NodeId#NULL_VALUE}
 * for connection-sourced events (spec rule). {@code SourceName} is the last path segment and {@code
 * ConnectionId}/{@code GroupId} take the first one/two segments — a best-effort split; component
 * names containing {@code '/'} make segment derivation approximate (the {@link PubSubNodeIds}
 * caveat).
 *
 * <p><b>Severity bands</b> (R17): informational transitions (newState Operational, PreOperational,
 * Paused, Disabled) carry {@link #SEVERITY_INFORMATIONAL} (100, within the &le;333 band); Error
 * entries and communication failures carry {@link #SEVERITY_ERROR} (500, within the 334-666 band).
 *
 * <p><b>Suppression</b> (first-failure-per-Error-episode, D34): the first send failure for a path
 * emits; subsequent failures for the same path are suppressed until a state-change event for the
 * path <em>or an ancestor</em> (connection-level restarts) arrives with newState Operational —
 * covering Error&rarr;Operational recovery, broker-reconnect recovery, enable-after-disable, and
 * reconfigure restarts. A non-dispose Disabled transition of the path also closes its episode (the
 * bookkeeping-drop rule below); the emitted-event set is unchanged because any path from Disabled
 * back to sending passes through Operational, which re-arms anyway. Consequence: a component that
 * fails every publish cycle while remaining Operational emits exactly one event per outage episode
 * (deliberate anti-storm posture). The engine's own event postures compose: {@code
 * encryptionErrors} events are already edge-triggered and the signature/token drop counters emit no
 * events at all.
 *
 * <p><b>Dispose silence and the leak guard</b> (D33): transitions with cause {@link
 * PubSubStateChangeEvent.Cause#DISPOSE} (shutdown, reconfigure removal) emit nothing and drop the
 * path's bookkeeping — teardown is not reportable state news, symmetric with R12's
 * dispose&ne;ByMethod counter rule. The dispose drop alone does NOT guard reconfigure-heavy servers
 * against bookkeeping leaks, because the engine emits no dispose-cause event for a component
 * removed while already Disabled (the state machine skips the notification when the state is
 * unchanged) — a deviation from the WP-Z brief's trap-7 assumption that dispose events arrive for
 * every removed path. The bridge therefore also drops bookkeeping on every non-dispose Disabled
 * transition (which still emits its informational event): a Disabled component cannot send, so the
 * entries carry no information, and together the two drop rules cover all removals — a component
 * removed in any other state gets a dispose-cause event.
 *
 * <p><b>State field of communication failures.</b> The mandatory {@code State} field serves the
 * last state observed for the path by this bridge (both listeners ride the same serialized queue,
 * so state and failure events arrive in emission order — as fresh as observable); for a path never
 * observed, a best-effort {@code components()} lookup runs, defaulting to {@code Operational} (a
 * component attempting transmission was enabled; exact event-time state is unobtainable).
 *
 * <p><b>Emission target</b> (D42): events fire with Server-object semantics via {@code
 * server.getEventNotifier().fire(...)}; clients monitor the Server object {@code i=2253}. No
 * EventNotifier bit is minted or mutated on PublishSubscribe {@code i=14443} (Part 14 mandates no
 * component-level notifier hierarchy).
 *
 * <p><b>Threading and lifecycle.</b> Owned by {@link ServerPubSub}, created iff {@link
 * ServerPubSubOptions#isStatusEventsEnabled()}. {@link #startup()} registers the two listeners;
 * {@link #shutdown()} deregisters them (R12 removal API) and clears the bookkeeping — {@code
 * ServerPubSub.shutdown()} runs it after the engine's event queue has drained, so shutdown-induced
 * dispose events reach (and are silenced by) the bridge first. Listener deliveries ride the
 * engine's serialized {@code ExecutionQueue} on the transport executor, in emission order and NOT
 * under the engine lock: {@code createEvent} + {@code fire} are safe there, the per-path maps need
 * no synchronization, and the bridge stays allocation-light (no I/O, no engine mutators). A
 * volatile {@code active} flag guards in-flight deliveries after shutdown. Event construction
 * failures (e.g. the server not yet started — the {@code EventFactory} lifecycle requires {@code
 * OpcUaServer.startup()}) are caught and WARN-logged so the dispatch queue is never disturbed.
 *
 * <p>The package-private {@link #onStateChange} / {@link #onDiagnosticsEvent} entry points are the
 * S17 test seams: tests drive deterministic stimuli directly, bypassing the engine.
 */
final class PubSubStatusEventBridge {

  /**
   * The EventType of state-change events: abstract {@code PubSubStatusEventType} ({@code i=15535}),
   * fired directly (spike-verified). Kept as a single constant so the concrete-subtype fallback is
   * a one-line flip (D35).
   */
  static final NodeId STATE_CHANGE_EVENT_TYPE_ID = NodeIds.PubSubStatusEventType;

  /**
   * The EventType of send-failure events: {@code PubSubCommunicationFailureEventType} ({@code
   * i=15563}). Kept as a single constant per D35.
   */
  static final NodeId COMMUNICATION_FAILURE_EVENT_TYPE_ID =
      NodeIds.PubSubCommunicationFailureEventType;

  /** Severity of informational state transitions (the &le;333 band, pinned value). */
  static final UShort SEVERITY_INFORMATIONAL = ushort(100);

  /** Severity of Error entries and communication failures (the 334-666 band, pinned value). */
  static final UShort SEVERITY_ERROR = ushort(500);

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubStatusEventBridge.class);

  private final OpcUaServer server;
  private final PubSubService service;
  private final UShort namespaceIndex;

  /** Guards event emission against in-flight listener deliveries after {@link #shutdown()}. */
  private volatile boolean active = false;

  /**
   * Last observed state per tracked component path, for the mandatory {@code State} field of
   * communication-failure events. Touched only on the serialized listener queue (or the S17 test
   * seams) — no synchronization needed; entries dropped on dispose-cause and Disabled transitions
   * (the class-level leak-guard rule).
   */
  private final Map<String, PubSubState> lastState = new HashMap<>();

  /**
   * Paths whose current outage episode has already emitted a communication-failure event (D34).
   * Same single-threaded discipline as {@link #lastState}.
   */
  private final Set<String> suppressed = new HashSet<>();

  private final PubSubStateListener stateListener = this::onStateChange;
  private final PubSubDiagnosticsListener diagnosticsListener = this::onDiagnosticsEvent;

  PubSubStatusEventBridge(OpcUaServer server, PubSubService service) {
    this.server = server;
    this.service = service;

    this.namespaceIndex = server.getServerNamespace().getNamespaceIndex();
  }

  /** Register the engine listeners and start emitting; reciprocal of {@link #shutdown()}. */
  void startup() {
    active = true;
    service.addStateListener(stateListener);
    service.addDiagnosticsListener(diagnosticsListener);
  }

  /**
   * Deregister the engine listeners (R12 removal API) and clear the bookkeeping. Called after the
   * engine's event queue has drained, so no delivery races the map clears; the {@code active} guard
   * silences any straggler.
   */
  void shutdown() {
    active = false;
    service.removeStateListener(stateListener);
    service.removeDiagnosticsListener(diagnosticsListener);
    lastState.clear();
    suppressed.clear();
  }

  /**
   * State-change delivery (S17 entry point): fire an {@code i=15535} event for tracked component
   * types, silence dispose-cause transitions (D33), maintain the last-state map (dropping the
   * path's bookkeeping on Disabled transitions — the class-level leak-guard rule), and re-arm the
   * send-failure suppression of the path and its descendants on Operational transitions (D34).
   */
  void onStateChange(PubSubStateChangeEvent event) {
    if (!active || !isTracked(event.component().componentType())) {
      return;
    }

    String path = event.component().path();

    if (event.cause() == PubSubStateChangeEvent.Cause.DISPOSE) {
      // the path is going away (shutdown or reconfigure removal): no event, drop bookkeeping
      lastState.remove(path);
      suppressed.remove(path);
      return;
    }

    if (event.newState() == PubSubState.Disabled) {
      // drop bookkeeping instead of tracking Disabled: the engine emits NO dispose-cause
      // event for a component removed while already Disabled (the state machine skips the
      // notification), so a retained entry would leak until shutdown. Behavior-neutral: a
      // Disabled component cannot send, and any path back to sending passes through
      // Operational, which re-seeds the state and re-arms suppression anyway.
      lastState.remove(path);
      suppressed.remove(path);
    } else {
      lastState.put(path, event.newState());

      if (event.newState() == PubSubState.Operational) {
        // close the outage episode of the path and its descendants: recovery, enable-after-
        // disable, and reconfigure/broker restarts all pass through Operational
        String prefix = path + "/";
        suppressed.removeIf(p -> p.equals(path) || p.startsWith(prefix));
      }
    }

    fireStateChangeEvent(event, path);
  }

  /**
   * Diagnostics delivery (S17 entry point): fire an {@code i=15563} event for the first {@link
   * PubSubDiagnosticsEvent.Kind#SEND_FAILURE} of the path's outage episode (D6/D34); everything
   * else is not a communication failure.
   */
  void onDiagnosticsEvent(PubSubDiagnosticsEvent event) {
    if (!active || event.kind() != PubSubDiagnosticsEvent.Kind.SEND_FAILURE) {
      return;
    }

    if (!suppressed.add(event.path())) {
      return; // this outage episode already emitted; re-armed on Operational
    }

    fireCommunicationFailureEvent(event);
  }

  /** The §9.1.13.1 source-set filter: root, PDS, SDS, and SecurityGroup are never sources. */
  private static boolean isTracked(ComponentType type) {
    return switch (type) {
      case CONNECTION, WRITER_GROUP, DATA_SET_WRITER, READER_GROUP, DATA_SET_READER -> true;
      default -> false;
    };
  }

  // region event emission

  private void fireStateChangeEvent(PubSubStateChangeEvent event, String path) {
    boolean connectionSourced = event.component().componentType() == ComponentType.CONNECTION;

    String message = "PubSub component '%s' state changed to %s".formatted(path, event.newState());
    if (!event.statusCode().isGood()) {
      message += " (%s)".formatted(event.statusCode());
    }

    UShort severity =
        event.newState() == PubSubState.Error ? SEVERITY_ERROR : SEVERITY_INFORMATIONAL;

    try {
      NodeId eventNodeId = new NodeId(namespaceIndex, UUID.randomUUID());
      BaseEventTypeNode eventNode =
          server.getEventFactory().createEvent(eventNodeId, STATE_CHANGE_EVENT_TYPE_ID);
      try {
        setCommonFields(
            eventNode,
            STATE_CHANGE_EVENT_TYPE_ID,
            path,
            connectionSourced,
            message,
            severity,
            event.newState());

        server.getEventNotifier().fire(eventNode);
      } finally {
        eventNode.delete();
      }
    } catch (Exception e) {
      LOGGER.warn("Error firing PubSub state change event for '{}'", path, e);
    }
  }

  private void fireCommunicationFailureEvent(PubSubDiagnosticsEvent event) {
    String path = event.path();
    // send failures originate at connection (discovery), group (data), or writer (metadata)
    // paths; a single-segment path is connection-sourced (best-effort, see the class caveat)
    boolean connectionSourced = path.indexOf('/') < 0;

    try {
      NodeId eventNodeId = new NodeId(namespaceIndex, UUID.randomUUID());
      BaseEventTypeNode eventNode =
          server.getEventFactory().createEvent(eventNodeId, COMMUNICATION_FAILURE_EVENT_TYPE_ID);
      try {
        setCommonFields(
            eventNode,
            COMMUNICATION_FAILURE_EVENT_TYPE_ID,
            path,
            connectionSourced,
            event.message(),
            SEVERITY_ERROR,
            stateOf(path));

        if (eventNode instanceof PubSubCommunicationFailureEventTypeNode failureEvent) {
          failureEvent.setError(event.statusCode());
        }

        server.getEventNotifier().fire(eventNode);
      } finally {
        eventNode.delete();
      }
    } catch (Exception e) {
      LOGGER.warn("Error firing PubSub communication failure event for '{}'", path, e);
    }
  }

  /**
   * Populate the inherited mandatory fields (Part 5 §6.4.2 with the §9.1.13.1 Source* overrides)
   * plus the ConnectionId/GroupId/State triple shared by both event types.
   */
  private void setCommonFields(
      BaseEventTypeNode eventNode,
      NodeId typeId,
      String path,
      boolean connectionSourced,
      String message,
      UShort severity,
      PubSubState state) {

    String sourceName = lastSegment(path);

    eventNode.setBrowseName(new QualifiedName(namespaceIndex, sourceName));
    eventNode.setDisplayName(LocalizedText.english(sourceName));
    eventNode.setEventId(NonceUtil.generateNonce(16));
    eventNode.setEventType(typeId);
    eventNode.setSourceNode(PubSubNodeIds.componentNodeId(namespaceIndex, path));
    eventNode.setSourceName(sourceName);
    eventNode.setTime(DateTime.now());
    eventNode.setReceiveTime(DateTime.NULL_VALUE);
    eventNode.setMessage(LocalizedText.english(message));
    eventNode.setSeverity(severity);

    if (eventNode instanceof PubSubStatusEventTypeNode statusEvent) {
      statusEvent.setConnectionId(
          PubSubNodeIds.componentNodeId(namespaceIndex, firstSegments(path, 1)));
      statusEvent.setGroupId(
          connectionSourced
              ? NodeId.NULL_VALUE
              : PubSubNodeIds.componentNodeId(namespaceIndex, firstSegments(path, 2)));
      statusEvent.setState(state);
    }
  }

  // endregion

  // region path derivation and state lookup

  /**
   * The last state observed for {@code path}, else a best-effort registry lookup, else {@code
   * Operational} (see the class Javadoc).
   */
  private PubSubState stateOf(String path) {
    PubSubState state = lastState.get(path);
    if (state != null) {
      return state;
    }

    try {
      Optional<PubSubHandle> handle = lookupByPath(path);
      if (handle.isPresent()) {
        return service.state(handle.get());
      }
    } catch (IllegalArgumentException e) {
      // reconfigured concurrently; fall through
    }

    return PubSubState.Operational;
  }

  /**
   * Best-effort handle lookup by send-failure source path: one segment is a connection, two a
   * writer group, three a dataset writer (metadata sends). Approximate when names contain {@code
   * '/'}.
   */
  private Optional<PubSubHandle> lookupByPath(String path) {
    int first = path.indexOf('/');
    if (first < 0) {
      return service.components().connection(path);
    }

    String connectionName = path.substring(0, first);
    int second = path.indexOf('/', first + 1);
    if (second < 0) {
      return service.components().writerGroup(connectionName, path.substring(first + 1));
    }

    return service
        .components()
        .dataSetWriter(
            connectionName, path.substring(first + 1, second), path.substring(second + 1));
  }

  /** The last {@code '/'}-separated segment of {@code path} (the SourceName rule). */
  private static String lastSegment(String path) {
    int index = path.lastIndexOf('/');
    return index < 0 ? path : path.substring(index + 1);
  }

  /** The first {@code n} {@code '/'}-separated segments of {@code path}, joined. */
  private static String firstSegments(String path, int n) {
    int index = -1;
    for (int i = 0; i < n; i++) {
      index = path.indexOf('/', index + 1);
      if (index < 0) {
        return path;
      }
    }
    return path.substring(0, index);
  }

  // endregion
}
