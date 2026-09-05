/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.EventListener;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.events.EventContentFilter;
import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.junit.jupiter.api.Test;

/**
 * Real event selection, refresh copying, and wire dispatch across Condition lifecycle boundaries.
 */
class ConditionDispatchLifecycleTest extends AbstractClientServerTest {

  @Test
  void expiredRestoreCannotChangeAnEventDuringFieldSelection() throws Exception {
    AlarmCondition alarm = restoredExpiredAlarm("event");
    List<Variant[]> events = new ArrayList<>();
    EventListener listener =
        event -> {
          if (event.getNodeId().equals(alarm.getConditionId())) {
            events.add(select(event));
          }
        };
    server.getEventNotifier().register(listener);
    try {
      alarm.setSeverity(ushort(701));
      assertFalse(events.isEmpty());
      for (Variant[] fields : events) {
        assertEquals(fields[0], fields[4], "one event must have one EventId");
        assertEquals(fields[1], fields[5], "shelving state must be stable during selection");
        assertEquals(Boolean.FALSE, fields[1].getValue());
        assertEquals("Unshelved", ((LocalizedText) fields[2].getValue()).text());
      }
    } finally {
      server.getEventNotifier().unregister(listener);
    }
  }

  @Test
  void refreshResolvesExpiredRestoreBeforeCopyingFields() throws Exception {
    AlarmCondition alarm = restoredExpiredAlarm("refresh");
    List<ConditionEventSnapshot> snapshots = alarm.createRefreshSnapshots();
    try {
      assertEquals(1, snapshots.size());
      Variant[] fields = select(snapshots.get(0).getEventNode());
      assertEquals(Boolean.FALSE, fields[1].getValue());
      assertEquals("Unshelved", ((LocalizedText) fields[2].getValue()).text());
      assertEquals(0.0, fields[3].getValue());
      assertEquals(alarm.currentBranch().getLastEventId(), fields[0].getValue());
    } finally {
      snapshots.forEach(ConditionEventSnapshot::delete);
    }
  }

  private AlarmCondition newAlarm(String name) throws Exception {
    AlarmCondition alarm =
        AlarmCondition.create(
            testNamespace.getNodeContext(),
            b ->
                b.nodeId(newNodeId(name))
                    .browseName(newQualifiedName(name))
                    .severity(ushort(700))
                    .withShelving(Duration.ofMinutes(5)));
    server.getConditionManager().register(alarm);
    return alarm;
  }

  private AlarmCondition restoredExpiredAlarm(String name) throws Exception {
    AlarmCondition alarm = newAlarm(name);
    ByteString eventId = ByteString.of(new byte[] {1, 2, 3, 4});
    DateTime eventTime = DateTime.now();
    alarm.restoreSnapshot(
        new ConditionSnapshot(
            true,
            ushort(700),
            ushort(700),
            null,
            null,
            null,
            new ConditionSnapshot.ShelvingSnapshot(
                ShelvedState.TIMED_SHELVED, new DateTime(Instant.now().minusSeconds(1))),
            List.of(
                new ConditionSnapshot.BranchSnapshot(
                    NodeId.NULL_VALUE,
                    false,
                    null,
                    true,
                    Set.of(),
                    null,
                    true,
                    eventId,
                    eventTime,
                    List.of(
                        new ConditionSnapshot.AcceptedEventId(
                            eventId, false, false, true, true))))));
    return alarm;
  }

  private Variant[] select(BaseEventTypeNode event) {
    FilterContext context =
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
    return EventContentFilter.select(
        context,
        new SimpleAttributeOperand[] {
          field("EventId"), field("SuppressedOrShelved"), field("ShelvingState", "CurrentState"),
          field("ShelvingState", "UnshelveTime"), field("EventId"), field("SuppressedOrShelved")
        },
        event);
  }

  private static SimpleAttributeOperand field(String... path) {
    return new SimpleAttributeOperand(
        NodeIds.AlarmConditionType,
        Arrays.stream(path).map(name -> new QualifiedName(0, name)).toArray(QualifiedName[]::new),
        AttributeId.Value.uid(),
        null);
  }
}
