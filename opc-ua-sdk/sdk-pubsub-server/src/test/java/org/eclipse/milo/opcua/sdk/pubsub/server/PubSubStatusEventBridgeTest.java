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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCommunicationFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusEventTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * {@link PubSubStatusEventBridge} unit rows, driven through the package-private S17 entry points
 * ({@code onStateChange} / {@code onDiagnosticsEvent}) against a STARTED server (the {@code
 * EventFactory} lifecycle requires one) with events captured synchronously via a registered {@link
 * EventListener} on the server's event notifier:
 *
 * <ul>
 *   <li>the §6.2 field set of state-change events, including {@code GroupId == NodeId.NULL_VALUE}
 *       for connection-sourced events (spec rule);
 *   <li>the pinned severity bands (100 informational / 500 Error-entry and comm-failure);
 *   <li>send-failure discrimination (Kind=SEND_FAILURE only, D6) and the real un-flattened Error
 *       code;
 *   <li>first-failure-per-Error-episode suppression with re-arm on an (ancestor) Operational
 *       transition (D34);
 *   <li>dispose-cause silence (D33);
 *   <li>the leak-guard bookkeeping drop on non-dispose Disabled transitions (components removed
 *       while already Disabled get no dispose-cause event from the engine);
 *   <li>the source-set filter (untracked component types emit nothing);
 *   <li>gating: no bridge without {@code statusEventsEnabled}, and D32 independence from {@code
 *       exposeInformationModel}.
 * </ul>
 */
class PubSubStatusEventBridgeTest {

  private static final String CONNECTION = "ev-conn";
  private static final String GROUP_PATH = CONNECTION + "/grp";
  private static final String WRITER_PATH = GROUP_PATH + "/writer";

  private static SksTestServer testServer;
  private static UShort namespaceIndex;

  private final List<CapturedEvent> captured = new CopyOnWriteArrayList<>();
  private final EventListener captureListener = node -> captured.add(CapturedEvent.of(node));

  private PubSubService service;
  private PubSubStatusEventBridge bridge;

  @BeforeAll
  static void startServer() throws Exception {
    testServer = SksTestServer.create(null);
    namespaceIndex = testServer.getServer().getServerNamespace().getNamespaceIndex();
  }

  @AfterAll
  static void stopServer() throws Exception {
    testServer.close();
  }

  @BeforeEach
  void setUp() {
    service = PubSubService.create(PubSubConfig.builder().build());
    bridge = new PubSubStatusEventBridge(testServer.getServer(), service);
    bridge.startup();

    testServer.getServer().getEventNotifier().register(captureListener);
  }

  @AfterEach
  void tearDown() {
    testServer.getServer().getEventNotifier().unregister(captureListener);
    bridge.shutdown();
    captured.clear();
  }

  @Test
  void writerStateChangeCarriesTheSpecFieldSet() {
    bridge.onStateChange(
        stateChange(
            ComponentType.DATA_SET_WRITER,
            WRITER_PATH,
            PubSubState.PreOperational,
            PubSubState.Operational,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.STARTUP));

    assertEquals(1, captured.size());
    CapturedEvent event = captured.get(0);

    assertEquals(NodeIds.PubSubStatusEventType, event.eventType());
    assertEquals(fragmentNodeId(WRITER_PATH), event.sourceNode());
    assertEquals("writer", event.sourceName());
    assertEquals(fragmentNodeId(CONNECTION), event.connectionId());
    assertEquals(fragmentNodeId(GROUP_PATH), event.groupId());
    assertEquals(PubSubState.Operational, event.state());
    assertEquals(PubSubStatusEventBridge.SEVERITY_INFORMATIONAL, event.severity());
    assertNotNull(event.message());
    assertTrue(event.message().text().contains(WRITER_PATH));
    assertNull(event.error());
  }

  @Test
  void connectionStateChangeCarriesNullGroupId() {
    bridge.onStateChange(
        stateChange(
            ComponentType.CONNECTION,
            CONNECTION,
            PubSubState.Disabled,
            PubSubState.Operational,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.METHOD));

    assertEquals(1, captured.size());
    CapturedEvent event = captured.get(0);

    assertEquals(fragmentNodeId(CONNECTION), event.sourceNode());
    assertEquals(CONNECTION, event.sourceName());
    assertEquals(fragmentNodeId(CONNECTION), event.connectionId());
    assertEquals(NodeId.NULL_VALUE, event.groupId());
  }

  @Test
  void errorEntryUsesTheErrorSeverityBand() {
    var badCode = new StatusCode(StatusCodes.Bad_ServerNotConnected);

    bridge.onStateChange(
        stateChange(
            ComponentType.WRITER_GROUP,
            GROUP_PATH,
            PubSubState.Operational,
            PubSubState.Error,
            badCode,
            PubSubStateChangeEvent.Cause.ERROR));

    assertEquals(1, captured.size());
    CapturedEvent event = captured.get(0);

    assertEquals(PubSubStatusEventBridge.SEVERITY_ERROR, event.severity());
    assertEquals(PubSubState.Error, event.state());
    assertNotNull(event.message());
    assertTrue(event.message().text().contains(badCode.toString()));
  }

  @Test
  void disposeCauseTransitionsAreSilent() {
    bridge.onStateChange(
        stateChange(
            ComponentType.DATA_SET_WRITER,
            WRITER_PATH,
            PubSubState.Operational,
            PubSubState.Disabled,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.DISPOSE));

    assertTrue(captured.isEmpty(), "dispose-cause transitions must emit no events (D33)");
  }

  @Test
  void disabledTransitionDropsTheBookkeepingOfThePath() {
    var sendError = new StatusCode(StatusCodes.Bad_ConnectionClosed);

    // seed the last-state map and open an outage episode
    bridge.onStateChange(
        stateChange(
            ComponentType.WRITER_GROUP,
            GROUP_PATH,
            PubSubState.PreOperational,
            PubSubState.Operational,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.STARTUP));
    bridge.onDiagnosticsEvent(sendFailure(GROUP_PATH, sendError, "send failed"));
    assertEquals(2, captured.size());

    // a method-disable must drop the path's bookkeeping: the engine emits NO dispose-cause
    // event for a component removed while already Disabled, so retained entries would leak
    bridge.onStateChange(
        stateChange(
            ComponentType.WRITER_GROUP,
            GROUP_PATH,
            PubSubState.Operational,
            PubSubState.Disabled,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.METHOD));
    assertEquals(3, captured.size(), "the Disabled transition itself still emits");
    assertEquals(PubSubState.Disabled, captured.get(2).state());

    // synthetic straggler failure to observe the drop: it emits (the suppression episode
    // was closed by the disable, not left open until a future Operational)...
    bridge.onDiagnosticsEvent(sendFailure(GROUP_PATH, sendError, "send failed anew"));
    assertEquals(4, captured.size());
    CapturedEvent event = captured.get(3);
    assertEquals(NodeIds.PubSubCommunicationFailureEventType, event.eventType());
    // ...and its State field serves the documented never-observed fallback (Operational;
    // the empty registry has no such component), not a stale Disabled map entry
    assertEquals(PubSubState.Operational, event.state());
  }

  @Test
  void untrackedComponentTypesAreSilent() {
    for (ComponentType type :
        List.of(
            ComponentType.PUBLISHED_DATA_SET,
            ComponentType.STANDALONE_SUBSCRIBED_DATA_SET,
            ComponentType.SECURITY_GROUP)) {

      bridge.onStateChange(
          stateChange(
              type,
              "some-component",
              PubSubState.Disabled,
              PubSubState.Operational,
              StatusCode.GOOD,
              PubSubStateChangeEvent.Cause.STARTUP));
    }

    assertTrue(captured.isEmpty(), "the §9.1.13.1 source set excludes PDS/SDS/SecurityGroup");
  }

  @Test
  void sendFailureEmitsACommunicationFailureEvent() {
    // seed the bridge's last-state map with a real transition first
    bridge.onStateChange(
        stateChange(
            ComponentType.WRITER_GROUP,
            GROUP_PATH,
            PubSubState.PreOperational,
            PubSubState.Operational,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.STARTUP));
    captured.clear();

    var sendError = new StatusCode(StatusCodes.Bad_ConnectionClosed);
    bridge.onDiagnosticsEvent(sendFailure(GROUP_PATH, sendError, "send failed"));

    assertEquals(1, captured.size());
    CapturedEvent event = captured.get(0);

    assertEquals(NodeIds.PubSubCommunicationFailureEventType, event.eventType());
    assertEquals(fragmentNodeId(GROUP_PATH), event.sourceNode());
    assertEquals("grp", event.sourceName());
    assertEquals(fragmentNodeId(CONNECTION), event.connectionId());
    assertEquals(fragmentNodeId(GROUP_PATH), event.groupId());
    assertEquals(PubSubState.Operational, event.state());
    assertEquals(sendError, event.error());
    assertEquals(PubSubStatusEventBridge.SEVERITY_ERROR, event.severity());
    assertNotNull(event.message());
    assertEquals("send failed", event.message().text());
  }

  @Test
  void connectionLevelSendFailureCarriesNullGroupId() {
    bridge.onDiagnosticsEvent(
        sendFailure(CONNECTION, new StatusCode(StatusCodes.Bad_Timeout), "discovery send failed"));

    assertEquals(1, captured.size());
    CapturedEvent event = captured.get(0);

    assertEquals(fragmentNodeId(CONNECTION), event.connectionId());
    assertEquals(NodeId.NULL_VALUE, event.groupId());
    // the path was never observed by the state listener and the registry has no such
    // component: the documented fallback serves Operational
    assertEquals(PubSubState.Operational, event.state());
  }

  @Test
  void otherErrorKindsAreSilent() {
    bridge.onDiagnosticsEvent(
        new PubSubDiagnosticsEvent(
            GROUP_PATH,
            new StatusCode(StatusCodes.Bad_DecodingError),
            "decode failed",
            null,
            PubSubDiagnosticsEvent.Kind.OTHER_ERROR));

    assertTrue(captured.isEmpty(), "only Kind=SEND_FAILURE feeds i=15563 (D6)");
  }

  @Test
  void secondFailureOfAnEpisodeIsSuppressedAndOperationalRearms() {
    var sendError = new StatusCode(StatusCodes.Bad_ConnectionClosed);

    // first failure of the episode emits
    bridge.onDiagnosticsEvent(sendFailure(GROUP_PATH, sendError, "send failed"));
    assertEquals(1, captured.size());

    // every further failure of the same episode is suppressed
    bridge.onDiagnosticsEvent(sendFailure(GROUP_PATH, sendError, "send failed again"));
    bridge.onDiagnosticsEvent(sendFailure(GROUP_PATH, sendError, "send failed again"));
    assertEquals(1, captured.size());

    // an Operational transition of an ANCESTOR (the connection) closes the episode...
    bridge.onStateChange(
        stateChange(
            ComponentType.CONNECTION,
            CONNECTION,
            PubSubState.Error,
            PubSubState.Operational,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.ERROR_RECOVERY));
    assertEquals(2, captured.size()); // the state-change event itself

    // ...so the next failure emits again
    bridge.onDiagnosticsEvent(sendFailure(GROUP_PATH, sendError, "send failed anew"));
    assertEquals(3, captured.size());
    assertEquals(NodeIds.PubSubCommunicationFailureEventType, captured.get(2).eventType());
  }

  @Test
  void inactiveBridgeIsSilent() {
    var idle = new PubSubStatusEventBridge(testServer.getServer(), service);
    // never started: both entry points must be inert
    idle.onStateChange(
        stateChange(
            ComponentType.CONNECTION,
            CONNECTION,
            PubSubState.Disabled,
            PubSubState.Operational,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.METHOD));
    idle.onDiagnosticsEvent(
        sendFailure(CONNECTION, new StatusCode(StatusCodes.Bad_Timeout), "send failed"));

    assertTrue(captured.isEmpty());

    // and a shut-down bridge is inert again
    bridge.shutdown();
    bridge.onStateChange(
        stateChange(
            ComponentType.CONNECTION,
            CONNECTION,
            PubSubState.Disabled,
            PubSubState.Operational,
            StatusCode.GOOD,
            PubSubStateChangeEvent.Cause.METHOD));

    assertTrue(captured.isEmpty());
  }

  @Test
  void bridgeIsGatedByStatusEventsEnabledIndependentOfExposure() throws Exception {
    try (TestPubSubServer plainServer = TestPubSubServer.create()) {
      OpcUaServer server = plainServer.getServer();
      PubSubConfig config = PubSubConfig.builder().build();

      try (ServerPubSub disabled = ServerPubSub.attach(server, config)) {
        assertNull(disabled.statusEventBridge(), "default options must not construct the bridge");
      }

      // D32: statusEventsEnabled is independent of exposeInformationModel
      ServerPubSubOptions options = ServerPubSubOptions.builder().statusEventsEnabled(true).build();
      try (ServerPubSub enabled = ServerPubSub.attach(server, config, options)) {
        assertNotNull(enabled.statusEventBridge(), "statusEventsEnabled constructs the bridge");
      }
    }
  }

  // region fixtures

  private static PubSubStateChangeEvent stateChange(
      ComponentType type,
      String path,
      PubSubState oldState,
      PubSubState newState,
      StatusCode statusCode,
      PubSubStateChangeEvent.Cause cause) {

    return new PubSubStateChangeEvent(
        new PubSubHandle(type, path), oldState, newState, statusCode, cause);
  }

  private static PubSubDiagnosticsEvent sendFailure(
      String path, StatusCode statusCode, String message) {

    return new PubSubDiagnosticsEvent(
        path, statusCode, message, null, PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
  }

  private static NodeId fragmentNodeId(String namePath) {
    return PubSubNodeIds.componentNodeId(namespaceIndex, namePath);
  }

  /**
   * Field snapshot taken synchronously during {@link EventListener#onEvent} — the bridge deletes
   * the event node right after firing, so fields must be read at capture time.
   */
  private record CapturedEvent(
      @Nullable NodeId eventType,
      @Nullable NodeId sourceNode,
      @Nullable String sourceName,
      @Nullable LocalizedText message,
      @Nullable UShort severity,
      @Nullable NodeId connectionId,
      @Nullable NodeId groupId,
      @Nullable PubSubState state,
      @Nullable StatusCode error) {

    static CapturedEvent of(BaseEventTypeNode node) {
      NodeId connectionId = null;
      NodeId groupId = null;
      PubSubState state = null;
      StatusCode error = null;

      if (node instanceof PubSubStatusEventTypeNode statusEvent) {
        connectionId = statusEvent.getConnectionId();
        groupId = statusEvent.getGroupId();
        state = statusEvent.getState();
      }
      if (node instanceof PubSubCommunicationFailureEventTypeNode failureEvent) {
        error = failureEvent.getError();
      }

      return new CapturedEvent(
          node.getEventType(),
          node.getSourceNode(),
          node.getSourceName(),
          node.getMessage(),
          node.getSeverity(),
          connectionId,
          groupId,
          state,
          error);
    }
  }

  // endregion
}
