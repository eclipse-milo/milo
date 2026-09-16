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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.sdk.test.EventTestSupport.conditionNameFilter;
import static org.eclipse.milo.opcua.sdk.test.EventTestSupport.localizedTextOf;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.AbstractSet;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.EventTestSupport;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Client-visible coverage for application-computed non-exclusive limit-alarm states: atomic tuple
 * replacement, caller-selected presentation, acknowledgement and timing rules, validation, and
 * interaction with the standard Condition lifecycle.
 */
// Fields below are assigned in configureTestNamespace(), which the nullability inspection does not
// model.
@SuppressWarnings("NotNullFieldNotInitialized")
public class AppDrivenLimitAlarmStateTest extends AbstractClientServerTest {

  private static final int BASE_SEVERITY = 100;
  private static final int HIGH_HIGH_SEVERITY = 800;
  private static final int HIGH_SEVERITY = 600;
  private static final int LOW_SEVERITY = 500;
  private static final int LOW_LOW_SEVERITY = 700;

  private UaNodeContext nodeContext;
  private UaObjectNode source;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          nodeContext = context;

          source =
              new UaObjectNode(
                  context,
                  newNodeId("AppDrivenLimitAlarmStateTest/Source"),
                  newQualifiedName("AppDrivenLimitAlarmStateTest/Source"),
                  LocalizedText.english("AppDrivenLimitAlarmStateTest/Source"),
                  LocalizedText.NULL_VALUE,
                  uint(0),
                  uint(0),
                  ubyte(0));

          source.addReference(
              new Reference(
                  source.getNodeId(),
                  NodeIds.HasTypeDefinition,
                  NodeIds.BaseObjectType.expanded(),
                  true));
          source.addReference(
              new Reference(
                  source.getNodeId(),
                  NodeIds.HasComponent,
                  NodeIds.ObjectsFolder.expanded(),
                  Reference.Direction.INVERSE));

          nodeManager.addNode(source);
        });
  }

  @Nested
  class StateApplication {

    // Caller-computed compound states must not inherit the scalar evaluator's excursion order.
    @Test
    void callerSelectedEffectiveStateDrivesOneCoherentCrossSideEvent() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("CrossSideSelection", false);
      var subscription = new OpcUaSubscription(client);
      subscription.create();

      try {
        EventFilter filter =
            conditionNameFilter(
                "CrossSideSelection",
                EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
                EventTestSupport.eventField(NodeIds.BaseEventType, "Severity"),
                EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"),
                EventTestSupport.eventField(
                    NodeIds.AlarmConditionType, "ActiveState", "EffectiveDisplayName"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "HighHighState", "Id"),
                EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "HighState", "Id"),
                EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "LowState", "Id"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "LowLowState", "Id"));

        List<Variant[]> events =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, filter);

        alarm.setLimitStates(
            EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);

        Variant[] event =
            EventTestSupport.awaitEvent(
                events,
                "cross-side state with caller-selected Low presentation",
                fields ->
                    "Active/Low".equals(messageOf(fields))
                        && ushort(LOW_SEVERITY).equals(fields[1].value()));

        assertEquals(Boolean.TRUE, event[2].value());
        assertEquals("Active/Low", localizedTextOf(event[3]));
        assertEquals(Boolean.FALSE, event[4].value());
        assertEquals(Boolean.TRUE, event[5].value());
        assertEquals(Boolean.TRUE, event[6].value());
        assertEquals(Boolean.FALSE, event[7].value());
        assertOnlyEvents(events, "cross-side reduced-state transition", event);

        assertActiveStates(alarm, EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW));
        assertEquals(ExclusiveLimitState.LOW, alarm.getEffectiveLimitState());
        assertEquals("Active/Low", effectiveDisplayName(alarm));
        assertEquals(ushort(LOW_SEVERITY), alarm.getNode().getSeverity());
        assertTrue(alarm.isActive());
      } finally {
        subscription.delete();
      }
    }

    // Full replacement prevents consumers from observing transient delta-update combinations.
    @Test
    void replacementClearsOmittedStatesWithOneSharedTransitionTime() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("FullReplacement", false);
      var subscription = new OpcUaSubscription(client);
      subscription.create();

      try {
        EventFilter filter =
            conditionNameFilter(
                "FullReplacement",
                EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "HighHighState", "Id"),
                EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "HighState", "Id"),
                EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "LowState", "Id"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "LowLowState", "Id"),
                EventTestSupport.eventField(NodeIds.BaseEventType, "Time"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "HighHighState", "TransitionTime"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "HighState", "TransitionTime"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "LowState", "TransitionTime"),
                EventTestSupport.eventField(
                    NodeIds.NonExclusiveLimitAlarmType, "LowLowState", "TransitionTime"),
                EventTestSupport.eventField(
                    NodeIds.AlarmConditionType, "ActiveState", "TransitionTime"),
                EventTestSupport.eventField(
                    NodeIds.AlarmConditionType, "ActiveState", "EffectiveTransitionTime"));

        List<Variant[]> events =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, filter);

        alarm.setLimitStates(
            EnumSet.allOf(ExclusiveLimitState.class), ExclusiveLimitState.HIGH_HIGH);
        Variant[] allStates =
            EventTestSupport.awaitEvent(
                events,
                "all limit states active",
                fields -> "Active/HighHigh".equals(messageOf(fields)));

        DateTime highTime = (DateTime) allStates[7].value();
        DateTime lowTime = (DateTime) allStates[8].value();
        DateTime activeTime = (DateTime) allStates[10].value();

        alarm.setLimitStates(
            EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);

        Variant[] replacement =
            EventTestSupport.awaitEvent(
                events,
                "omitted outer states cleared in the Low-selected replacement",
                fields ->
                    "Active/Low".equals(messageOf(fields))
                        && Boolean.FALSE.equals(fields[1].value())
                        && Boolean.TRUE.equals(fields[2].value())
                        && Boolean.TRUE.equals(fields[3].value())
                        && Boolean.FALSE.equals(fields[4].value()));

        DateTime eventTime = (DateTime) replacement[5].value();
        assertEquals(eventTime, replacement[6].value(), "HighHigh cleared at event time");
        assertEquals(highTime, replacement[7].value(), "High remained active without transition");
        assertEquals(lowTime, replacement[8].value(), "Low remained active without transition");
        assertEquals(eventTime, replacement[9].value(), "LowLow cleared at event time");
        assertEquals(activeTime, replacement[10].value(), "Active stayed true");
        assertEquals(
            eventTime,
            replacement[11].value(),
            "membership change advances EffectiveTransitionTime");
        assertOnlyEvents(events, "complete-set replacement", allStates, replacement);
      } finally {
        subscription.delete();
      }
    }

    @Test
    void callerMutationAfterNormalizationCannotChangeTheAppliedDefensiveSnapshot()
        throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("DefensiveCopy", false);
      var traversalComplete = new CountDownLatch(1);
      var callerStates =
          new TraversalSignallingSet(
              EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW), traversalComplete);
      var setter = new AtomicReference<@Nullable Future<?>>();
      ExecutorService executor = Executors.newSingleThreadExecutor();

      try {
        // Hold the mutation boundary while the worker defensively normalizes the caller's set.
        alarm.runLocked(
            () -> {
              setter.set(
                  executor.submit(
                      () -> alarm.setLimitStates(callerStates, ExclusiveLimitState.LOW)));

              assertTrue(
                  traversalComplete.await(5, TimeUnit.SECONDS),
                  "setter did not finish traversing its input set");
              assertFalse(
                  requireNonNull(setter.get()).isDone(),
                  "setter must still be waiting on the held lock");

              callerStates.clear();
              callerStates.add(ExclusiveLimitState.HIGH_HIGH);
            });

        requireNonNull(setter.get()).get(5, TimeUnit.SECONDS);
      } finally {
        executor.shutdownNow();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
      }

      assertActiveStates(alarm, EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW));
      assertEquals(ExclusiveLimitState.LOW, alarm.getEffectiveLimitState());
    }

    @Test
    void scalarEvaluationReassertsItsMembershipPolicyAndClearResetsEffectiveReadback()
        throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("ScalarReassertion", false);
      alarm.setLimitStates(
          EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);

      alarm.evaluate(96.0);

      assertActiveStates(
          alarm, EnumSet.of(ExclusiveLimitState.HIGH_HIGH, ExclusiveLimitState.HIGH));
      assertEquals(ExclusiveLimitState.HIGH_HIGH, alarm.getEffectiveLimitState());
      assertEquals("Active/HighHigh", message(alarm));
      assertEquals("Active/HighHigh", effectiveDisplayName(alarm));
      assertEquals(ushort(HIGH_HIGH_SEVERITY), alarm.getNode().getSeverity());

      alarm.setActive(false);

      assertActiveStates(alarm, Set.of());
      assertNull(alarm.getEffectiveLimitState());
      assertFalse(alarm.isActive());
    }

    // Scalar precedence must run even when evaluation does not change any modeled limit bit.
    @Test
    void scalarEvaluationReselectsItsFixedPolicyWhenMembershipAlreadyMatches() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("ScalarReselection", false);
      Set<ExclusiveLimitState> scalarMembership =
          EnumSet.of(ExclusiveLimitState.HIGH_HIGH, ExclusiveLimitState.HIGH);
      alarm.setLimitStates(scalarMembership, ExclusiveLimitState.HIGH);

      ByteString directEventId = alarm.currentBranch().getLastEventId();
      DateTime effectiveTime = activeState(alarm).getEffectiveTransitionTime();
      DateTime highHighTime = stateNode(alarm, ExclusiveLimitState.HIGH_HIGH).getTransitionTime();
      DateTime highTime = stateNode(alarm, ExclusiveLimitState.HIGH).getTransitionTime();

      alarm.evaluate(96.0);

      ByteString scalarEventId = alarm.currentBranch().getLastEventId();
      assertActiveStates(alarm, scalarMembership);
      assertEquals(ExclusiveLimitState.HIGH_HIGH, alarm.getEffectiveLimitState());
      assertEquals("Active/HighHigh", message(alarm));
      assertEquals(ushort(HIGH_HIGH_SEVERITY), alarm.getNode().getSeverity());
      assertNotEquals(directEventId, scalarEventId);
      assertFalse(alarm.currentBranch().isAcknowledgeable(directEventId));
      assertTrue(alarm.currentBranch().isAcknowledgeable(scalarEventId));
      assertEquals(effectiveTime, activeState(alarm).getEffectiveTransitionTime());
      assertEquals(
          highHighTime, stateNode(alarm, ExclusiveLimitState.HIGH_HIGH).getTransitionTime());
      assertEquals(highTime, stateNode(alarm, ExclusiveLimitState.HIGH).getTransitionTime());
    }

    @Test
    void scalarSeverityCorrectionPreservesTheComputedReducedStateAndAcknowledgement()
        throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("ScalarSeverityCorrection", false);
      alarm.evaluate(96.0);
      acknowledge(alarm);

      TwoStateVariableTypeNode ackedState = requireNonNull(alarm.getNode().getAckedStateNode());
      DateTime ackedTime = ackedState.getTransitionTime();
      DateTime effectiveTime = activeState(alarm).getEffectiveTransitionTime();
      ByteString acknowledgedEventId = alarm.currentBranch().getLastEventId();

      alarm.getNode().setSeverityHighHigh(ushort(HIGH_HIGH_SEVERITY + 1));
      alarm.evaluate(96.0);

      assertActiveStates(
          alarm, EnumSet.of(ExclusiveLimitState.HIGH_HIGH, ExclusiveLimitState.HIGH));
      assertEquals(ExclusiveLimitState.HIGH_HIGH, alarm.getEffectiveLimitState());
      assertEquals(ushort(HIGH_HIGH_SEVERITY + 1), alarm.getNode().getSeverity());
      assertEquals(ushort(HIGH_HIGH_SEVERITY), alarm.getNode().getLastSeverity());
      assertTrue(alarm.isAcked());
      assertEquals(ackedTime, ackedState.getTransitionTime());
      assertEquals(effectiveTime, activeState(alarm).getEffectiveTransitionTime());
      assertNotEquals(acknowledgedEventId, alarm.currentBranch().getLastEventId());
    }
  }

  @Nested
  class Validation {

    // Invalid input must not lazily expire shelving before reporting the caller error.
    @Test
    @SuppressWarnings("DataFlowIssue")
    void invalidTuplesFailBeforeAnyStateOrShelvingMutation() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("InvalidTuples", true);
      alarm.setLimitStates(Set.of(ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);
      makeShelvingExpiryDue(alarm);

      ByteString eventId = alarm.currentBranch().getLastEventId();
      DateTime eventTime = alarm.currentBranch().getLastEventTime();
      DateTime activeTime = activeState(alarm).getTransitionTime();
      DateTime lowTime = stateNode(alarm, ExclusiveLimitState.LOW).getTransitionTime();

      assertThrows(NullPointerException.class, () -> alarm.setLimitStates(null, null));

      var nullMember = new HashSet<ExclusiveLimitState>();
      nullMember.add(ExclusiveLimitState.LOW);
      nullMember.add(null);
      assertThrows(
          NullPointerException.class,
          () -> alarm.setLimitStates(nullMember, ExclusiveLimitState.LOW));

      assertThrows(
          IllegalArgumentException.class,
          () -> alarm.setLimitStates(Set.of(), ExclusiveLimitState.LOW));
      assertThrows(
          IllegalArgumentException.class,
          () -> alarm.setLimitStates(Set.of(ExclusiveLimitState.LOW), null));
      assertThrows(
          IllegalArgumentException.class,
          () -> alarm.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.LOW));

      assertActiveStates(alarm, Set.of(ExclusiveLimitState.LOW));
      assertEquals(ExclusiveLimitState.LOW, alarm.getEffectiveLimitState());
      assertEquals(eventId, alarm.currentBranch().getLastEventId());
      assertEquals(eventTime, alarm.currentBranch().getLastEventTime());
      assertEquals(activeTime, activeState(alarm).getTransitionTime());
      assertEquals(lowTime, stateNode(alarm, ExclusiveLimitState.LOW).getTransitionTime());
      assertEquals("TimedShelved", shelvingStateName(alarm));
    }

    @Test
    void missingStateNodeAndMissingLimitPropertyAreRejectedIndependently() throws Exception {
      NonExclusiveLimitAlarm missingState =
          createPartiallyLoadedAlarm("MissingStateNode", true, true);
      NonExclusiveLimitAlarm missingLimit =
          createPartiallyLoadedAlarm("MissingLimitProperty", false, true);

      assertUnsupportedStateRejectedBeforeShelvingTouch(missingState);
      assertUnsupportedStateRejectedBeforeShelvingTouch(missingLimit);
    }
  }

  @Nested
  class AcknowledgementAndTiming {

    // Configuration correction is observable, but it is not a new alarm state needing ack.
    @Test
    void identicalTupleIsSilentAndSeverityCorrectionDoesNotRenewAcknowledgement() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("SeverityCorrection", false);
      Set<ExclusiveLimitState> states =
          EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW);

      alarm.setLimitStates(states, ExclusiveLimitState.LOW);
      acknowledge(alarm);

      TwoStateVariableTypeNode ackedState = alarm.getNode().getAckedStateNode();
      DateTime ackedTime = requireNonNull(ackedState).getTransitionTime();
      ByteString acknowledgedEventId = alarm.currentBranch().getLastEventId();
      DateTime acknowledgedEventTime = alarm.currentBranch().getLastEventTime();

      var subscription = new OpcUaSubscription(client);
      subscription.create();

      try {
        EventFilter filter =
            conditionNameFilter(
                "SeverityCorrection",
                EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
                EventTestSupport.eventField(NodeIds.BaseEventType, "Severity"),
                EventTestSupport.eventField(NodeIds.ConditionType, "LastSeverity"),
                EventTestSupport.eventField(
                    NodeIds.AcknowledgeableConditionType, "AckedState", "Id"));
        List<Variant[]> events =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, filter);

        alarm.setLimitStates(states, ExclusiveLimitState.LOW);

        assertEquals(acknowledgedEventId, alarm.currentBranch().getLastEventId());
        assertEquals(acknowledgedEventTime, alarm.currentBranch().getLastEventTime());
        EventTestSupport.assertNoEvent(events, "exact tuple no-op", fields -> true);

        alarm.getNode().setSeverityLow(ushort(LOW_SEVERITY + 1));
        alarm.setLimitStates(states, ExclusiveLimitState.LOW);

        Variant[] correction =
            EventTestSupport.awaitEvent(
                events,
                "selected Low Severity correction",
                fields -> ushort(LOW_SEVERITY + 1).equals(fields[1].value()));

        assertEquals("Active/Low", messageOf(correction));
        assertEquals(ushort(LOW_SEVERITY), correction[2].value());
        assertEquals(Boolean.TRUE, correction[3].value());
        assertTrue(alarm.isAcked());
        assertEquals(ackedTime, ackedState.getTransitionTime());
        assertNotEquals(acknowledgedEventId, alarm.currentBranch().getLastEventId());
        assertOnlyEvents(events, "selected Severity correction", correction);
      } finally {
        subscription.delete();
      }
    }

    // Selecting a different primary state is semantic, but no modeled substate entered or left.
    @Test
    void effectiveReselectionRenewsAckWithoutChangingModeledTransitionTimes() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("EffectiveReselection", false);
      Set<ExclusiveLimitState> states =
          EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW);
      alarm.setLimitStates(states, ExclusiveLimitState.HIGH);

      TwoStateVariableTypeNode activeState = activeState(alarm);
      TwoStateVariableTypeNode ackedState = requireNonNull(alarm.getNode().getAckedStateNode());
      DateTime activeTime = activeState.getTransitionTime();
      DateTime sentinel = new DateTime(1L);
      activeState.setEffectiveTransitionTime(sentinel);
      DateTime highTime = stateNode(alarm, ExclusiveLimitState.HIGH).getTransitionTime();
      DateTime lowTime = stateNode(alarm, ExclusiveLimitState.LOW).getTransitionTime();
      DateTime ackedTime = ackedState.getTransitionTime();
      ByteString firstEventId = alarm.currentBranch().getLastEventId();

      var subscription = new OpcUaSubscription(client);
      subscription.create();

      try {
        EventFilter filter =
            conditionNameFilter(
                "EffectiveReselection",
                EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
                EventTestSupport.eventField(NodeIds.BaseEventType, "Severity"),
                EventTestSupport.eventField(
                    NodeIds.AlarmConditionType, "ActiveState", "EffectiveDisplayName"),
                EventTestSupport.eventField(
                    NodeIds.AcknowledgeableConditionType, "AckedState", "Id"));
        List<Variant[]> events =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, filter);

        alarm.setLimitStates(states, ExclusiveLimitState.LOW);

        Variant[] reselection =
            EventTestSupport.awaitEvent(
                events,
                "same-membership Low reselection",
                fields -> "Active/Low".equals(messageOf(fields)));

        ByteString secondEventId = alarm.currentBranch().getLastEventId();
        assertEquals(ushort(LOW_SEVERITY), reselection[1].value());
        assertEquals("Active/Low", localizedTextOf(reselection[2]));
        assertEquals(Boolean.FALSE, reselection[3].value());
        assertEquals(ExclusiveLimitState.LOW, alarm.getEffectiveLimitState());
        assertNotEquals(firstEventId, secondEventId);
        assertFalse(alarm.currentBranch().isAcknowledgeable(firstEventId));
        assertTrue(alarm.currentBranch().isAcknowledgeable(secondEventId));
        assertEquals(ackedTime, ackedState.getTransitionTime());
        assertEquals(activeTime, activeState.getTransitionTime());
        assertEquals(sentinel, activeState.getEffectiveTransitionTime());
        assertEquals(highTime, stateNode(alarm, ExclusiveLimitState.HIGH).getTransitionTime());
        assertEquals(lowTime, stateNode(alarm, ExclusiveLimitState.LOW).getTransitionTime());
        assertOnlyEvents(events, "effective-state reselection", reselection);
      } finally {
        subscription.delete();
      }
    }

    // Part 9 allows a server to renew the acknowledgement obligation for a changed alarm state.
    @Test
    void membershipChangeRenewsAckAndAdvancesOnlyMembershipTiming() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("MembershipRenewal", false);
      alarm.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.HIGH);

      TwoStateVariableTypeNode activeState = activeState(alarm);
      TwoStateVariableTypeNode ackedState = requireNonNull(alarm.getNode().getAckedStateNode());
      DateTime activeTime = activeState.getTransitionTime();
      DateTime sentinel = new DateTime(1L);
      activeState.setEffectiveTransitionTime(sentinel);
      DateTime highTime = stateNode(alarm, ExclusiveLimitState.HIGH).getTransitionTime();
      DateTime ackedTime = ackedState.getTransitionTime();
      ByteString firstEventId = alarm.currentBranch().getLastEventId();

      alarm.setLimitStates(
          EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW), ExclusiveLimitState.HIGH);

      ByteString secondEventId = alarm.currentBranch().getLastEventId();
      DateTime lowTime = stateNode(alarm, ExclusiveLimitState.LOW).getTransitionTime();

      assertNotEquals(firstEventId, secondEventId);
      assertFalse(alarm.currentBranch().isAcknowledgeable(firstEventId));
      assertTrue(alarm.currentBranch().isAcknowledgeable(secondEventId));
      assertFalse(alarm.isAcked());
      assertEquals(ackedTime, ackedState.getTransitionTime());
      assertEquals(activeTime, activeState.getTransitionTime());
      assertEquals(highTime, stateNode(alarm, ExclusiveLimitState.HIGH).getTransitionTime());
      assertNotEquals(sentinel, activeState.getEffectiveTransitionTime());
      assertEquals(lowTime, activeState.getEffectiveTransitionTime());
    }

    @Test
    void createdLimitAlarmsExposeInitializedEffectiveTransitionTime() throws Exception {
      NonExclusiveLimitAlarm nonExclusive =
          createNonExclusiveAlarm("InitializedEffectiveTime", false);
      ExclusiveLimitAlarm exclusive = createExclusiveAlarm("ExclusiveEffectiveTime");

      TwoStateVariableTypeNode nonExclusiveActive = activeState(nonExclusive);
      TwoStateVariableTypeNode exclusiveActive = activeState(exclusive);

      assertNotNull(nonExclusiveActive.getEffectiveTransitionTimeNode());
      assertNotNull(nonExclusiveActive.getEffectiveTransitionTime());
      assertEquals(
          nonExclusiveActive.getTransitionTime(), nonExclusiveActive.getEffectiveTransitionTime());
      assertNotNull(exclusiveActive.getEffectiveTransitionTimeNode());
      assertNotNull(exclusiveActive.getEffectiveTransitionTime());
      assertEquals(
          exclusiveActive.getTransitionTime(), exclusiveActive.getEffectiveTransitionTime());

      exclusive.setLimitState(ExclusiveLimitState.HIGH);
      DateTime sentinel = new DateTime(1L);
      exclusiveActive.setEffectiveTransitionTime(sentinel);
      exclusive.setLimitState(ExclusiveLimitState.HIGH_HIGH);

      assertNotEquals(sentinel, exclusiveActive.getEffectiveTransitionTime());
    }
  }

  @Nested
  class ConditionLifecycle {

    @Test
    void clearRetainBehaviorTracksTheOutstandingAcknowledgement() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("RetainLifecycle", false);

      alarm.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.HIGH);
      alarm.setLimitStates(Set.of(), null);

      assertFalse(alarm.isActive());
      assertFalse(alarm.isAcked());
      assertTrue(alarm.isRetained(), "inactive but unacknowledged state must remain retained");
      acknowledge(alarm);
      assertFalse(alarm.isRetained(), "acknowledging the inactive state retracts Retain");

      alarm.setLimitStates(Set.of(ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);
      acknowledge(alarm);

      assertTrue(alarm.isActive());
      assertTrue(alarm.isAcked());
      assertTrue(alarm.isRetained());

      alarm.setLimitStates(Set.of(), null);

      assertFalse(alarm.isActive());
      assertTrue(alarm.isAcked());
      assertFalse(alarm.isRetained(), "inactive and acknowledged state retracts immediately");
      assertNull(alarm.getEffectiveLimitState());
    }

    // Disabled Conditions keep processing internal alarm state for a coherent re-enable event.
    @Test
    void disabledApplicationSurvivesEnableReassertion() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("DisabledApplication", false);
      assertGood(call(alarm.getConditionId(), NodeIds.ConditionType_Disable));
      ByteString disabledEventId = alarm.currentBranch().getLastEventId();

      alarm.setLimitStates(
          EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);

      assertTrue(alarm.isActive());
      assertActiveStates(alarm, EnumSet.of(ExclusiveLimitState.HIGH, ExclusiveLimitState.LOW));
      assertEquals(ExclusiveLimitState.LOW, alarm.getEffectiveLimitState());
      assertFalse(alarm.isRetained());
      assertEquals(disabledEventId, alarm.currentBranch().getLastEventId());

      var subscription = new OpcUaSubscription(client);
      subscription.create();

      try {
        EventFilter filter =
            conditionNameFilter(
                "DisabledApplication",
                EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
                EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"),
                EventTestSupport.eventField(
                    NodeIds.AlarmConditionType, "ActiveState", "EffectiveDisplayName"),
                EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "HighState", "Id"),
                EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "LowState", "Id"));
        List<Variant[]> events =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, filter);

        assertGood(call(alarm.getConditionId(), NodeIds.ConditionType_Enable));

        Variant[] enabled =
            EventTestSupport.awaitEvent(
                events,
                "enabled Condition reasserts the silently applied tuple",
                fields -> "Enabled".equals(messageOf(fields)));

        assertEquals(Boolean.TRUE, enabled[1].value());
        assertEquals("Active/Low", localizedTextOf(enabled[2]));
        assertEquals(Boolean.TRUE, enabled[3].value());
        assertEquals(Boolean.TRUE, enabled[4].value());
        assertTrue(alarm.isRetained());
        assertOnlyEvents(events, "enable reassertion", enabled);
      } finally {
        subscription.delete();
      }
    }

    // A valid no-op is still a shelving touch, so a lost expiry timer cannot pin suppression.
    @Test
    void identicalTupleAppliesDueLazyShelvingExpiryBeforeNoOpDetection() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("LazyShelving", true);
      Set<ExclusiveLimitState> states = Set.of(ExclusiveLimitState.HIGH);
      alarm.setLimitStates(states, ExclusiveLimitState.HIGH);
      makeShelvingExpiryDue(alarm);
      ByteString shelvedEventId = alarm.currentBranch().getLastEventId();

      var subscription = new OpcUaSubscription(client);
      subscription.create();

      try {
        EventFilter filter =
            conditionNameFilter(
                "LazyShelving", EventTestSupport.eventField(NodeIds.BaseEventType, "Message"));
        List<Variant[]> events =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, filter);

        alarm.setLimitStates(states, ExclusiveLimitState.HIGH);

        Variant[] unshelved =
            EventTestSupport.awaitEvent(
                events,
                "due lazy shelving expiry",
                fields -> "Unshelved".equals(messageOf(fields)));

        assertEquals("Unshelved", shelvingStateName(alarm));
        assertEquals(Boolean.FALSE, alarm.getNode().getSuppressedOrShelved());
        assertActiveStates(alarm, states);
        assertEquals(ExclusiveLimitState.HIGH, alarm.getEffectiveLimitState());
        assertNotEquals(shelvedEventId, alarm.currentBranch().getLastEventId());
        assertOnlyEvents(events, "lazy shelving expiry", unshelved);
      } finally {
        subscription.delete();
      }
    }

    @Test
    void clearingReleasesOneShotShelvingButPreservesTimedShelving() throws Exception {
      NonExclusiveLimitAlarm alarm = createNonExclusiveAlarm("ShelvingModes", true);
      alarm.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.HIGH);

      ShelvedStateMachineTypeNode shelving = requireNonNull(alarm.getShelvingState());
      assertGood(
          call(
              shelving.getNodeId(),
              requireNonNull(shelving.getOneShotShelveMethodNode()).getNodeId()));
      assertEquals("OneShotShelved", shelvingStateName(alarm));

      alarm.setLimitStates(Set.of(), null);
      assertEquals("Unshelved", shelvingStateName(alarm));

      alarm.setLimitStates(Set.of(ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);
      assertGood(
          call(
              shelving.getNodeId(),
              requireNonNull(shelving.getTimedShelveMethodNode()).getNodeId(),
              new Variant(30_000.0)));

      alarm.setLimitStates(Set.of(), null);

      assertEquals("TimedShelved", shelvingStateName(alarm));
      assertEquals(Boolean.TRUE, alarm.getNode().getSuppressedOrShelved());
      assertFalse(alarm.isActive());
      assertNull(alarm.getEffectiveLimitState());
    }
  }

  @Nested
  class LoadedStateCompatibility {

    @Test
    void firstSetterRepairsIncoherentActiveButSeedsCoherentLegacySelectionAsNoOp()
        throws Exception {
      NonExclusiveLimitAlarm incoherent = createLoadedAlarm("IncoherentLoaded", false);
      NonExclusiveLimitAlarm coherent = createLoadedAlarm("CoherentLoaded", true);

      var subscription = new OpcUaSubscription(client);
      subscription.create();

      try {
        EventFilter incoherentFilter =
            conditionNameFilter(
                "IncoherentLoaded",
                EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
                EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"),
                EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "HighState", "Id"));
        List<Variant[]> incoherentEvents =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, incoherentFilter);

        EventFilter coherentFilter =
            conditionNameFilter(
                "CoherentLoaded", EventTestSupport.eventField(NodeIds.BaseEventType, "Message"));
        List<Variant[]> coherentEvents =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, coherentFilter);

        ByteString coherentEventId = coherent.currentBranch().getLastEventId();
        DateTime coherentEventTime = coherent.currentBranch().getLastEventTime();

        incoherent.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.HIGH);
        coherent.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.HIGH);

        Variant[] repaired =
            EventTestSupport.awaitEvent(
                incoherentEvents,
                "loaded High bit repairs false ActiveState",
                fields ->
                    "Active/High".equals(messageOf(fields))
                        && Boolean.TRUE.equals(fields[1].value())
                        && Boolean.TRUE.equals(fields[2].value()));

        assertNotNull(repaired);
        assertTrue(incoherent.isActive());
        assertEquals(ExclusiveLimitState.HIGH, incoherent.getEffectiveLimitState());
        assertOnlyEvents(incoherentEvents, "loaded ActiveState repair", repaired);

        EventTestSupport.assertNoEvent(coherentEvents, "coherent first setter", fields -> true);
        assertTrue(coherent.isActive());
        assertEquals(ExclusiveLimitState.HIGH, coherent.getEffectiveLimitState());
        assertEquals(coherentEventId, coherent.currentBranch().getLastEventId());
        assertEquals(coherentEventTime, coherent.currentBranch().getLastEventTime());
      } finally {
        subscription.delete();
      }
    }
  }

  private NonExclusiveLimitAlarm createNonExclusiveAlarm(String name, boolean shelving)
      throws UaException {
    NonExclusiveLimitAlarm alarm = buildNonExclusiveAlarm(name, shelving);
    server.getConditionManager().register(alarm);
    return alarm;
  }

  private NonExclusiveLimitAlarm buildNonExclusiveAlarm(String name, boolean shelving)
      throws UaException {
    return NonExclusiveLimitAlarm.create(
        nodeContext,
        builder -> {
          builder
              .nodeId(newNodeId("AppDrivenLimitAlarmStateTest/" + name))
              .browseName(newQualifiedName(name))
              .conditionSource(source)
              .severity(ushort(BASE_SEVERITY))
              .highHighLimit(95.0, ushort(HIGH_HIGH_SEVERITY))
              .highLimit(90.0, ushort(HIGH_SEVERITY))
              .lowLimit(10.0, ushort(LOW_SEVERITY))
              .lowLowLimit(5.0, ushort(LOW_LOW_SEVERITY));

          if (shelving) {
            builder.withShelving(Duration.ofMinutes(1));
          }
        });
  }

  private ExclusiveLimitAlarm createExclusiveAlarm(String name) throws UaException {
    ExclusiveLimitAlarm alarm =
        ExclusiveLimitAlarm.create(
            nodeContext,
            builder ->
                builder
                    .nodeId(newNodeId("AppDrivenLimitAlarmStateTest/" + name))
                    .browseName(newQualifiedName(name))
                    .conditionSource(source)
                    .highHighLimit(95.0)
                    .highLimit(90.0));
    server.getConditionManager().register(alarm);
    return alarm;
  }

  private NonExclusiveLimitAlarm createPartiallyLoadedAlarm(
      String name, boolean deleteStateNode, boolean shelving) throws Exception {
    NonExclusiveLimitAlarm created = buildNonExclusiveAlarm(name, shelving);
    NonExclusiveLimitAlarmTypeNode node = created.getNode();

    if (deleteStateNode) {
      requireNonNull(node.getHighStateNode()).delete();
      assertNull(node.getHighStateNode());
      assertNotNull(node.getHighLimitNode());
    } else {
      requireNonNull(node.getHighLimitNode()).delete();
      assertNotNull(node.getHighStateNode());
      assertNull(node.getHighLimitNode());
    }

    NonExclusiveLimitAlarm attached =
        NonExclusiveLimitAlarm.attach(node, options -> options.conditionSource(source));
    server.getConditionManager().register(attached);
    return attached;
  }

  private NonExclusiveLimitAlarm createLoadedAlarm(String name, boolean coherent) throws Exception {
    NonExclusiveLimitAlarm created = buildNonExclusiveAlarm(name, false);
    created.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.HIGH);

    if (!coherent) {
      TwoStateVariableTypeNode activeState = activeState(created);
      activeState.setId(false);
      activeState.setValue(new DataValue(new Variant(LocalizedText.english("Inactive"))));
    }

    NonExclusiveLimitAlarm attached =
        NonExclusiveLimitAlarm.attach(
            created.getNode(), options -> options.conditionSource(source));
    server.getConditionManager().register(attached);
    return attached;
  }

  private void assertUnsupportedStateRejectedBeforeShelvingTouch(NonExclusiveLimitAlarm alarm)
      throws Exception {
    alarm.setLimitStates(Set.of(ExclusiveLimitState.LOW), ExclusiveLimitState.LOW);
    makeShelvingExpiryDue(alarm);

    ByteString eventId = alarm.currentBranch().getLastEventId();
    DateTime eventTime = alarm.currentBranch().getLastEventTime();
    DateTime activeTime = activeState(alarm).getTransitionTime();
    DateTime lowTime = stateNode(alarm, ExclusiveLimitState.LOW).getTransitionTime();

    assertThrows(
        IllegalArgumentException.class,
        () -> alarm.setLimitStates(Set.of(ExclusiveLimitState.HIGH), ExclusiveLimitState.HIGH));

    assertActiveStates(alarm, Set.of(ExclusiveLimitState.LOW));
    assertEquals(ExclusiveLimitState.LOW, alarm.getEffectiveLimitState());
    assertEquals(eventId, alarm.currentBranch().getLastEventId());
    assertEquals(eventTime, alarm.currentBranch().getLastEventTime());
    assertEquals(activeTime, activeState(alarm).getTransitionTime());
    assertEquals(lowTime, stateNode(alarm, ExclusiveLimitState.LOW).getTransitionTime());
    assertEquals("TimedShelved", shelvingStateName(alarm));
  }

  private void makeShelvingExpiryDue(NonExclusiveLimitAlarm alarm) throws Exception {
    ShelvedStateMachineTypeNode shelving = requireNonNull(alarm.getShelvingState());
    assertGood(
        call(
            shelving.getNodeId(),
            requireNonNull(shelving.getTimedShelveMethodNode()).getNodeId(),
            new Variant(30_000.0)));

    ShelvingRuntime runtime = requireNonNull(alarm.getShelvingRuntime());
    runtime.setExpiryTimerSuppressedForTesting(true);
    runtime.makeExpiryDueForTesting();
  }

  private void acknowledge(NonExclusiveLimitAlarm alarm) throws Exception {
    CallMethodResult result =
        call(
            alarm.getConditionId(),
            NodeIds.AcknowledgeableConditionType_Acknowledge,
            new Variant(alarm.currentBranch().getLastEventId()),
            new Variant(LocalizedText.NULL_VALUE));
    assertGood(result);
  }

  private CallMethodResult call(NodeId objectId, NodeId methodId, Variant... inputs)
      throws Exception {
    CallResponse response = client.call(List.of(new CallMethodRequest(objectId, methodId, inputs)));
    return requireNonNull(response.getResults())[0];
  }

  private static void assertGood(CallMethodResult result) {
    assertTrue(requireNonNull(result.getStatusCode()).isGood());
  }

  private static void assertOnlyEvents(
      List<Variant[]> events, String description, Variant[]... expectedEvents)
      throws InterruptedException {
    for (Variant[] expectedEvent : expectedEvents) {
      assertTrue(events.remove(expectedEvent), "expected event was not collected: " + description);
    }
    EventTestSupport.assertNoEvent(events, "additional " + description, fields -> true);
  }

  private static TwoStateVariableTypeNode activeState(LimitAlarm alarm) {
    return requireNonNull(alarm.getNode().getActiveStateNode());
  }

  private static TwoStateVariableTypeNode stateNode(
      NonExclusiveLimitAlarm alarm, ExclusiveLimitState state) {
    TwoStateVariableTypeNode node =
        switch (state) {
          case HIGH_HIGH -> alarm.getNode().getHighHighStateNode();
          case HIGH -> alarm.getNode().getHighStateNode();
          case LOW -> alarm.getNode().getLowStateNode();
          case LOW_LOW -> alarm.getNode().getLowLowStateNode();
        };
    return requireNonNull(node);
  }

  private static void assertActiveStates(
      NonExclusiveLimitAlarm alarm, Set<ExclusiveLimitState> expected) {
    for (ExclusiveLimitState state : ExclusiveLimitState.values()) {
      assertEquals(
          expected.contains(state), alarm.isLimitActive(state), state.stateName() + " membership");
    }
  }

  private static String shelvingStateName(NonExclusiveLimitAlarm alarm) {
    LocalizedText currentState =
        requireNonNull(requireNonNull(alarm.getShelvingState()).getCurrentState());
    return requireNonNull(currentState.text());
  }

  private static String effectiveDisplayName(NonExclusiveLimitAlarm alarm) {
    LocalizedText displayName = requireNonNull(activeState(alarm).getEffectiveDisplayName());
    return requireNonNull(displayName.text());
  }

  private static String message(NonExclusiveLimitAlarm alarm) {
    LocalizedText message = requireNonNull(alarm.getNode().getMessage());
    return requireNonNull(message.text());
  }

  private static @Nullable String messageOf(Variant[] eventFields) {
    return localizedTextOf(eventFields[0]);
  }

  /** A mutable caller set that signals once a defensive-copy traversal has captured its values. */
  private static final class TraversalSignallingSet extends AbstractSet<ExclusiveLimitState> {

    private final Set<ExclusiveLimitState> delegate;
    private final CountDownLatch traversalComplete;

    private TraversalSignallingSet(
        Set<ExclusiveLimitState> delegate, CountDownLatch traversalComplete) {
      this.delegate = delegate;
      this.traversalComplete = traversalComplete;
    }

    @Override
    public Iterator<ExclusiveLimitState> iterator() {
      Iterator<ExclusiveLimitState> iterator = delegate.iterator();

      return new Iterator<>() {
        @Override
        public boolean hasNext() {
          boolean hasNext = iterator.hasNext();
          if (!hasNext) {
            traversalComplete.countDown();
          }
          return hasNext;
        }

        @Override
        public ExclusiveLimitState next() {
          ExclusiveLimitState next = iterator.next();
          if (!iterator.hasNext()) {
            traversalComplete.countDown();
          }
          return next;
        }
      };
    }

    @Override
    public int size() {
      return delegate.size();
    }

    @Override
    public boolean add(ExclusiveLimitState state) {
      return delegate.add(state);
    }

    @Override
    public void clear() {
      delegate.clear();
    }

    @Override
    public Object[] toArray() {
      Object[] snapshot = delegate.toArray();
      traversalComplete.countDown();
      return snapshot;
    }

    @Override
    public <T> T[] toArray(T[] array) {
      T[] snapshot = delegate.toArray(array);
      traversalComplete.countDown();
      return snapshot;
    }
  }
}
