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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.pubsub.PubSubCounter;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link DiagnosticsCollector} Phase 5 additions: per-counter TimeFirstChange,
 * per-object {@code reset(path)} (lastError untouched, unknown paths no-op), the State* transition
 * ticks, and the pending-restart counter preservation used by path-stable reconfigure restarts.
 */
class DiagnosticsResetAndTimeFirstChangeTest {

  private static final String GROUP = "conn/grp";
  private static final String WRITER = "conn/grp/writer";

  private final EventDispatcher eventDispatcher = new EventDispatcher(Runnable::run);
  private final DiagnosticsCollector collector = new DiagnosticsCollector(eventDispatcher);

  DiagnosticsResetAndTimeFirstChangeTest() {
    eventDispatcher.setDiagnostics(collector);
    collector.register(GROUP);
    collector.register(WRITER);
  }

  @Test
  void timeFirstChangeAppearsOnFirstTickAndStaysStable() {
    assertNull(component(GROUP).timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT));

    collector.networkMessageSent(GROUP);

    DateTime firstChange = component(GROUP).timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT);
    assertNotNull(firstChange);

    collector.networkMessageSent(GROUP);
    collector.networkMessageSent(GROUP);

    assertEquals(3, component(GROUP).networkMessagesSent());
    assertEquals(
        firstChange, component(GROUP).timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT));

    // an entry exists only for counters that left zero
    assertNull(component(GROUP).timeFirstChange(PubSubCounter.DECODE_ERRORS));
    assertTrue(component(GROUP).timeFirstChange().containsKey(PubSubCounter.NETWORK_MESSAGES_SENT));
    assertEquals(1, component(GROUP).timeFirstChange().size());
  }

  @Test
  void resetZeroesCountersAndTimeFirstChangeAtThatPathOnlyAndKeepsLastError() {
    collector.networkMessageSent(GROUP);
    collector.failedTransmissions(GROUP, 2);
    collector.dataSetMessagesSent(WRITER, 5);
    var lastError = new StatusCode(StatusCodes.Bad_ServerNotConnected);
    collector.error(
        GROUP, lastError, "send failed", null, PubSubDiagnosticsEvent.Kind.SEND_FAILURE);

    collector.reset(GROUP);

    ComponentDiagnostics group = component(GROUP);
    assertEquals(0, group.networkMessagesSent());
    assertEquals(0, group.failedTransmissions());
    assertTrue(group.timeFirstChange().isEmpty());
    // lastError is deliberately untouched by reset
    assertEquals(lastError, group.lastError());

    // per-object: the child path is untouched
    ComponentDiagnostics writer = component(WRITER);
    assertEquals(5, writer.dataSetMessagesSent());
    assertNotNull(writer.timeFirstChange(PubSubCounter.DATA_SET_MESSAGES_SENT));

    // counting resumes with a fresh TimeFirstChange baseline
    collector.networkMessageSent(GROUP);
    assertEquals(1, component(GROUP).networkMessagesSent());
    assertNotNull(component(GROUP).timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT));
  }

  @Test
  void resetOfUnknownPathIsNoOp() {
    collector.networkMessageSent(GROUP);

    collector.reset("no/such/path");

    assertEquals(1, component(GROUP).networkMessagesSent());
  }

  @Test
  void stateTransitionTicksFollowTable311Attribution() {
    // Error entry ticks stateError regardless of cause
    collector.stateTransition(
        GROUP, PubSubState.Operational, PubSubState.Error, PubSubStateChangeEvent.Cause.ERROR);
    // Operational by METHOD and by PARENT are attributed by cause; STARTUP ticks neither
    collector.stateTransition(
        GROUP, PubSubState.Error, PubSubState.Operational, PubSubStateChangeEvent.Cause.METHOD);
    collector.stateTransition(
        GROUP,
        PubSubState.PreOperational,
        PubSubState.Operational,
        PubSubStateChangeEvent.Cause.PARENT);
    collector.stateTransition(
        GROUP,
        PubSubState.PreOperational,
        PubSubState.Operational,
        PubSubStateChangeEvent.Cause.STARTUP);
    // every Paused entry ticks StatePausedByParent (definitionally parent-caused)
    collector.stateTransition(
        GROUP, PubSubState.Operational, PubSubState.Paused, PubSubStateChangeEvent.Cause.PARENT);
    // Disabled by METHOD ticks; dispose ticks nothing
    collector.stateTransition(
        GROUP, PubSubState.Paused, PubSubState.Disabled, PubSubStateChangeEvent.Cause.METHOD);
    collector.stateTransition(
        GROUP, PubSubState.Disabled, PubSubState.Disabled, PubSubStateChangeEvent.Cause.DISPOSE);

    ComponentDiagnostics group = component(GROUP);
    assertEquals(1, group.stateError());
    assertEquals(1, group.stateOperationalByMethod());
    assertEquals(1, group.stateOperationalByParent());
    // the METHOD recovery above came from Error: StateOperationalFromError is old-state keyed
    assertEquals(1, group.stateOperationalFromError());
    assertEquals(1, group.statePausedByParent());
    assertEquals(1, group.stateDisabledByMethod());
    assertNotNull(group.timeFirstChange(PubSubCounter.STATE_ERROR));
  }

  @Test
  void removePreservingParksCountersForRevivalByRegister() {
    collector.networkMessageSent(GROUP);
    var lastError = new StatusCode(StatusCodes.Bad_InternalError);
    collector.error(GROUP, lastError, "boom", null, PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
    DateTime firstChange = component(GROUP).timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT);

    collector.removePreserving(GROUP);

    // while parked, the path is out of the live map: ticks are dropped, reads see nothing
    assertTrue(collector.component(GROUP).isEmpty());
    collector.networkMessageSent(GROUP);

    collector.register(GROUP);

    // revived with counters, lastError, and TimeFirstChange intact (the in-flight tick dropped)
    ComponentDiagnostics revived = component(GROUP);
    assertEquals(1, revived.networkMessagesSent());
    assertEquals(lastError, revived.lastError());
    assertEquals(firstChange, revived.timeFirstChange(PubSubCounter.NETWORK_MESSAGES_SENT));
  }

  @Test
  void clearPendingRestartDiscardsParkedEntries() {
    collector.networkMessageSent(GROUP);

    collector.removePreserving(GROUP);
    collector.clearPendingRestart();
    collector.register(GROUP);

    // removal + (later) re-add starts from zero
    ComponentDiagnostics fresh = component(GROUP);
    assertEquals(0, fresh.networkMessagesSent());
    assertNull(fresh.lastError());
    assertTrue(fresh.timeFirstChange().isEmpty());
  }

  @Test
  void plainRemoveDiscardsCounters() {
    collector.networkMessageSent(GROUP);

    collector.remove(GROUP);
    collector.register(GROUP);

    assertEquals(0, component(GROUP).networkMessagesSent());
  }

  private ComponentDiagnostics component(String path) {
    return collector.component(path).orElseThrow();
  }
}
