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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.EventTestSupport;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Client-visible tests for the WP5 limit-alarm family: the exclusive evaluate sweep with per-limit
 * severities and the LimitState machine, deadband hysteresis, independent non-exclusive limit
 * states, builder validation, and the Deviation/RateOfChange/Level variant smoke assertions.
 */
public class LimitAlarmTest extends AbstractClientServerTest {

  private static final EUInformation DEGREES_PER_SECOND =
      new EUInformation(
          "http://www.opcfoundation.org/UA/units/un/cefact",
          4408652,
          LocalizedText.english("°/s"),
          LocalizedText.english("degrees per second"));

  private UaNodeContext nodeContext;

  private ExclusiveLevelAlarm sweepAlarm;
  private ExclusiveLevelAlarm deadbandAlarm;
  private NonExclusiveLevelAlarm nonExclusiveAlarm;
  private ExclusiveDeviationAlarm deviationAlarm;
  private NonExclusiveDeviationAlarm nonExclusiveDeviationAlarm;
  private ExclusiveRateOfChangeAlarm exclusiveRateOfChangeAlarm;
  private NonExclusiveRateOfChangeAlarm rateOfChangeAlarm;
  private ExclusiveLimitAlarm appDrivenAlarm;
  private ExclusiveLevelAlarm runtimeSeverityAlarm;
  private ExclusiveLevelAlarm baseSeverityAlarm;
  private ExclusiveLevelAlarm acknowledgementAlarm;
  private ExclusiveLevelAlarm shelvedExclusiveAlarm;
  private NonExclusiveLevelAlarm shelvedNonExclusiveAlarm;

  private NodeId setpointNodeId;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          nodeContext = context;

          var source =
              new UaObjectNode(
                  context,
                  newNodeId("LimitAlarmTest/Source"),
                  newQualifiedName("LimitAlarmTest/Source"),
                  LocalizedText.english("LimitAlarmTest/Source"),
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

          setpointNodeId = newNodeId("LimitAlarmTest/Setpoint");

          try {
            sweepAlarm =
                ExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/Sweep"))
                            .browseName(newQualifiedName("ExclusiveSweep"))
                            .conditionSource(source)
                            .severity(ushort(100))
                            .highHighLimit(95.0, ushort(800))
                            .highLimit(90.0, ushort(600))
                            .lowLimit(10.0, ushort(500))
                            .lowLowLimit(5.0, ushort(700)));

            deadbandAlarm =
                ExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/Deadband"))
                            .browseName(newQualifiedName("Deadband"))
                            .conditionSource(source)
                            .highLimit(90.0)
                            .highDeadband(1.5));

            nonExclusiveAlarm =
                NonExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/NonExclusive"))
                            .browseName(newQualifiedName("NonExclusive"))
                            .conditionSource(source)
                            .severity(ushort(100))
                            .highHighLimit(95.0, ushort(800))
                            .highLimit(90.0, ushort(600)));

            deviationAlarm =
                ExclusiveDeviationAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/Deviation"))
                            .browseName(newQualifiedName("Deviation"))
                            .conditionSource(source)
                            .setpointNode(setpointNodeId)
                            .highLimit(10.0));

            nonExclusiveDeviationAlarm =
                NonExclusiveDeviationAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/NonExclusiveDeviation"))
                            .browseName(newQualifiedName("NonExclusiveDeviation"))
                            .conditionSource(source)
                            .setpointNode(setpointNodeId)
                            .highLimit(10.0)
                            .lowLimit(-10.0));

            exclusiveRateOfChangeAlarm =
                ExclusiveRateOfChangeAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/ExclusiveRateOfChange"))
                            .browseName(newQualifiedName("ExclusiveRateOfChange"))
                            .conditionSource(source)
                            .engineeringUnits(DEGREES_PER_SECOND)
                            .highLimit(5.0));

            rateOfChangeAlarm =
                NonExclusiveRateOfChangeAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/RateOfChange"))
                            .browseName(newQualifiedName("RateOfChange"))
                            .conditionSource(source)
                            .engineeringUnits(DEGREES_PER_SECOND)
                            .highLimit(5.0));

            appDrivenAlarm =
                ExclusiveLimitAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/AppDriven"))
                            .browseName(newQualifiedName("AppDriven"))
                            .conditionSource(source)
                            .highLimit(90.0)
                            .lowLimit(10.0));

            runtimeSeverityAlarm =
                ExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/RuntimeSeverity"))
                            .browseName(newQualifiedName("RuntimeSeverity"))
                            .conditionSource(source)
                            .severity(ushort(100))
                            .highHighLimit(95.0, ushort(800))
                            .highLimit(90.0, ushort(600))
                            .lowLimit(10.0, ushort(500))
                            .lowLowLimit(5.0, ushort(700)));

            baseSeverityAlarm =
                ExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/BaseSeverity"))
                            .browseName(newQualifiedName("BaseSeverity"))
                            .conditionSource(source)
                            .severity(ushort(100))
                            .highLimit(90.0, ushort(600))
                            .lowLimit(10.0));

            acknowledgementAlarm =
                ExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/Acknowledgement"))
                            .browseName(newQualifiedName("Acknowledgement"))
                            .conditionSource(source)
                            .highHighLimit(95.0)
                            .highLimit(90.0));

            shelvedExclusiveAlarm =
                ExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/ShelvedExclusive"))
                            .browseName(newQualifiedName("ShelvedExclusive"))
                            .conditionSource(source)
                            .withShelving(Duration.ofMinutes(1))
                            .highLimit(90.0));

            shelvedNonExclusiveAlarm =
                NonExclusiveLevelAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("LimitAlarmTest/ShelvedNonExclusive"))
                            .browseName(newQualifiedName("ShelvedNonExclusive"))
                            .conditionSource(source)
                            .withShelving(Duration.ofMinutes(1))
                            .highLimit(90.0));
          } catch (UaException e) {
            throw new RuntimeException(e);
          }

          for (Condition condition :
              List.of(
                  sweepAlarm,
                  deadbandAlarm,
                  nonExclusiveAlarm,
                  deviationAlarm,
                  nonExclusiveDeviationAlarm,
                  exclusiveRateOfChangeAlarm,
                  rateOfChangeAlarm,
                  appDrivenAlarm,
                  runtimeSeverityAlarm,
                  baseSeverityAlarm,
                  acknowledgementAlarm,
                  shelvedExclusiveAlarm,
                  shelvedNonExclusiveAlarm)) {
            server.getConditionManager().register(condition);
          }
        });
  }

  @Test
  void exclusiveEvaluateSweep() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      EventFilter eventFilter =
          conditionNameFilter(
              "ExclusiveSweep",
              EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
              EventTestSupport.eventField(NodeIds.BaseEventType, "Severity"),
              EventTestSupport.eventField(NodeIds.ConditionType, "LastSeverity"),
              EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"),
              EventTestSupport.eventField(
                  NodeIds.ExclusiveLimitAlarmType, "LimitState", "CurrentState"),
              EventTestSupport.eventField(NodeIds.BaseEventType, "EventType"));

      List<Variant[]> events =
          EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter);

      sweepAlarm.evaluate(92.0);
      sweepAlarm.evaluate(96.0);
      sweepAlarm.evaluate(50.0);
      sweepAlarm.evaluate(8.0);
      sweepAlarm.evaluate(3.0);

      assertEquals(ExclusiveLimitState.LOW_LOW, sweepAlarm.getLimitState());
      assertTrue(sweepAlarm.isActive());

      // One event per transition, each carrying the per-limit severity, the previous severity as
      // LastSeverity, and the LimitState CurrentState of the violated limit.
      Variant[] high =
          EventTestSupport.awaitEvent(
              events, "High transition", e -> "Active/High".equals(messageOf(e)));
      assertEquals(ushort(600), high[1].value());
      assertEquals(ushort(100), high[2].value());
      assertTrue(activeIdOf(high));
      assertEquals("High", limitStateOf(high));
      assertEquals(NodeIds.ExclusiveLevelAlarmType, high[5].value());

      Variant[] highHigh =
          EventTestSupport.awaitEvent(
              events, "HighHigh escalation", e -> "Active/HighHigh".equals(messageOf(e)));
      assertEquals(ushort(800), highHigh[1].value());
      assertEquals(ushort(600), highHigh[2].value());
      assertEquals("HighHigh", limitStateOf(highHigh));

      // §5.8.19.3: while inactive the LimitState is unavailable and its fields extract as NULL.
      Variant[] inactive =
          EventTestSupport.awaitEvent(
              events, "return to normal", e -> "Inactive".equals(messageOf(e)));
      assertFalse(activeIdOf(inactive));
      assertNull(limitStateOf(inactive));

      Variant[] low =
          EventTestSupport.awaitEvent(
              events, "Low transition", e -> "Active/Low".equals(messageOf(e)));
      assertEquals(ushort(500), low[1].value());

      Variant[] lowLow =
          EventTestSupport.awaitEvent(
              events, "LowLow escalation", e -> "Active/LowLow".equals(messageOf(e)));
      assertEquals(ushort(700), lowLow[1].value());
      assertEquals(ushort(500), lowLow[2].value());
      assertEquals("LowLow", limitStateOf(lowLow));

      assertEquals(5, events.size(), "expected exactly one event per transition");
    } finally {
      subscription.delete();
    }
  }

  @Test
  void deadbandHysteresis() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      EventFilter eventFilter =
          conditionNameFilter(
              "Deadband",
              EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
              EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"));

      List<Variant[]> events =
          EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter);

      // Below the limit, no activation; crossing activates once.
      deadbandAlarm.evaluate(89.0);
      deadbandAlarm.evaluate(91.0);
      EventTestSupport.awaitEvent(
          events,
          "activation",
          e -> "Active/High".equals(messageOf(e)) && Boolean.TRUE.equals(e[1].value()));

      // Oscillation inside the deadband (HighLimit 90, HighDeadband 1.5): no chatter.
      deadbandAlarm.evaluate(89.2);
      deadbandAlarm.evaluate(89.9);
      deadbandAlarm.evaluate(88.6);
      EventTestSupport.assertNoEvent(
          events, "transition inside the deadband", e -> "Inactive".equals(messageOf(e)));
      assertEquals(1, events.size());
      assertTrue(deadbandAlarm.isActive());

      // Retreating within the limit by the deadband deactivates: exactly one transition.
      deadbandAlarm.evaluate(88.4);
      EventTestSupport.awaitEvent(
          events,
          "deactivation",
          e -> "Inactive".equals(messageOf(e)) && Boolean.FALSE.equals(e[1].value()));
      assertEquals(2, events.size(), "expected exactly one transition per deadband crossing");
      assertFalse(deadbandAlarm.isActive());
    } finally {
      subscription.delete();
    }
  }

  @Test
  void nonExclusiveIndependentStates() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      EventFilter eventFilter =
          conditionNameFilter(
              "NonExclusive",
              EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
              EventTestSupport.eventField(NodeIds.BaseEventType, "Severity"),
              EventTestSupport.eventField(NodeIds.ConditionType, "LastSeverity"),
              EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"),
              EventTestSupport.eventField(
                  NodeIds.NonExclusiveLimitAlarmType, "HighHighState", "Id"),
              EventTestSupport.eventField(NodeIds.NonExclusiveLimitAlarmType, "HighState", "Id"));

      List<Variant[]> events =
          EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter);

      // Above HighHigh both limits are violated simultaneously — one event.
      nonExclusiveAlarm.evaluate(96.0);
      assertTrue(nonExclusiveAlarm.isLimitActive(ExclusiveLimitState.HIGH_HIGH));
      assertTrue(nonExclusiveAlarm.isLimitActive(ExclusiveLimitState.HIGH));
      assertTrue(nonExclusiveAlarm.isActive());

      Variant[] both =
          EventTestSupport.awaitEvent(
              events, "HighHigh and High active", e -> "Active/HighHigh".equals(messageOf(e)));
      assertTrue(Boolean.TRUE.equals(both[4].value()) && Boolean.TRUE.equals(both[5].value()));
      assertEquals(ushort(800), both[1].value());

      // Between the limits HighHigh retracts independently while High stays active.
      nonExclusiveAlarm.evaluate(92.0);
      assertFalse(nonExclusiveAlarm.isLimitActive(ExclusiveLimitState.HIGH_HIGH));
      assertTrue(nonExclusiveAlarm.isLimitActive(ExclusiveLimitState.HIGH));

      Variant[] highOnly =
          EventTestSupport.awaitEvent(
              events, "independent HighHigh retraction", e -> "Active/High".equals(messageOf(e)));
      assertTrue(Boolean.FALSE.equals(highOnly[4].value()));
      assertTrue(Boolean.TRUE.equals(highOnly[5].value()));
      assertTrue(activeIdOf(highOnly));
      assertEquals(ushort(600), highOnly[1].value());
      assertEquals(ushort(800), highOnly[2].value());

      // Below both limits everything retracts.
      nonExclusiveAlarm.evaluate(50.0);
      Variant[] inactive =
          EventTestSupport.awaitEvent(
              events, "return to normal", e -> "Inactive".equals(messageOf(e)));
      assertFalse(activeIdOf(inactive));
      assertFalse(nonExclusiveAlarm.isActive());

      assertEquals(
          3, events.size(), "expected exactly one event per evaluation that changed state");
    } finally {
      subscription.delete();
    }
  }

  @Test
  void builderValidationRejectsMisconfiguration() {
    // No limits at all.
    assertThrows(
        IllegalArgumentException.class, () -> ExclusiveLevelAlarm.create(nodeContext, b -> {}));

    // Limit-order violation: Low > High.
    assertThrows(
        IllegalArgumentException.class,
        () -> ExclusiveLevelAlarm.create(nodeContext, b -> b.highLimit(10.0).lowLimit(90.0)));

    // A deadband requires its limit.
    assertThrows(
        IllegalArgumentException.class,
        () -> ExclusiveLevelAlarm.create(nodeContext, b -> b.highLimit(90.0).lowDeadband(1.0)));

    // Non-exclusive types require at least a High or Low limit.
    assertThrows(
        IllegalArgumentException.class,
        () -> NonExclusiveLevelAlarm.create(nodeContext, b -> b.highHighLimit(95.0)));

    // Deviation types require the setpointNode.
    assertThrows(
        IllegalArgumentException.class,
        () -> ExclusiveDeviationAlarm.create(nodeContext, b -> b.highLimit(10.0)));
  }

  @Test
  void builderValidationRejectsAdjacentDeadbandOverlapAndAcceptsSeparatedBoundaries()
      throws Exception {

    // Each deadband boundary must remain strictly inside its own region.
    assertInvalidExclusiveLimits(
        b -> b.highHighLimit(100.0).highHighDeadband(10.0).highLimit(90.0));
    assertInvalidExclusiveLimits(b -> b.highLimit(90.0).highDeadband(80.0).lowLimit(10.0));
    assertInvalidExclusiveLimits(b -> b.highLimit(90.0).lowLimit(10.0).lowDeadband(80.0));
    assertInvalidExclusiveLimits(b -> b.lowLimit(10.0).lowLowLimit(0.0).lowLowDeadband(10.0));

    // When an optional inner limit is absent, the next configured limit is the neighbor.
    assertInvalidExclusiveLimits(b -> b.highHighLimit(100.0).highHighDeadband(90.0).lowLimit(10.0));
    assertInvalidExclusiveLimits(b -> b.highLimit(90.0).lowLowLimit(0.0).lowLowDeadband(90.0));

    NonExclusiveLevelAlarm valid =
        NonExclusiveLevelAlarm.create(
            nodeContext,
            b ->
                b.nodeId(newNodeId("LimitAlarmTest/ValidAdjacentDeadbands"))
                    .browseName(newQualifiedName("ValidAdjacentDeadbands"))
                    .highLimit(90.0)
                    .highDeadband(79.9)
                    .lowLimit(10.0)
                    .lowDeadband(79.9));

    valid.evaluate(100.0);
    assertTrue(valid.isLimitActive(ExclusiveLimitState.HIGH));
    assertFalse(valid.isLimitActive(ExclusiveLimitState.LOW));

    valid.evaluate(0.0);
    assertFalse(valid.isLimitActive(ExclusiveLimitState.HIGH));
    assertTrue(valid.isLimitActive(ExclusiveLimitState.LOW));

    valid.evaluate(100.0);
    assertTrue(valid.isLimitActive(ExclusiveLimitState.HIGH));
    assertFalse(valid.isLimitActive(ExclusiveLimitState.LOW));
  }

  @Test
  void deviationAlarmValidationRequiresSignedNonZeroLimitsForBothVariants() {
    assertInvalidDeviation(true, b -> b.highHighLimit(0.0), "HighHigh");
    assertInvalidDeviation(true, b -> b.highLimit(-1.0), "High");
    assertInvalidDeviation(true, b -> b.lowLimit(1.0), "Low");
    assertInvalidDeviation(true, b -> b.lowLowLimit(0.0), "LowLow");

    assertInvalidDeviation(false, b -> b.highHighLimit(0.0).lowLimit(-1.0), "HighHigh");
    assertInvalidDeviation(false, b -> b.highLimit(0.0), "High");
    assertInvalidDeviation(false, b -> b.lowLimit(0.0), "Low");
    assertInvalidDeviation(false, b -> b.highLimit(1.0).lowLowLimit(0.0), "LowLow");
  }

  @Test
  void everyPerLimitSeverityMustBeInTheOpcUaRange() {
    assertInvalidSeverity(b -> b.highHighLimit(95.0, ushort(0)), "SeverityHighHigh");
    assertInvalidSeverity(b -> b.highHighLimit(95.0, ushort(1001)), "SeverityHighHigh");
    assertInvalidSeverity(b -> b.highLimit(90.0, ushort(0)), "SeverityHigh");
    assertInvalidSeverity(b -> b.highLimit(90.0, ushort(1001)), "SeverityHigh");
    assertInvalidSeverity(b -> b.lowLimit(10.0, ushort(0)), "SeverityLow");
    assertInvalidSeverity(b -> b.lowLimit(10.0, ushort(1001)), "SeverityLow");
    assertInvalidSeverity(b -> b.lowLowLimit(5.0, ushort(0)), "SeverityLowLow");
    assertInvalidSeverity(b -> b.lowLowLimit(5.0, ushort(1001)), "SeverityLowLow");
  }

  @Test
  void activeStateReferencesOnlyInstantiatedLimitSubstates() throws Exception {
    TwoStateVariableTypeNode exclusiveActive = appDrivenAlarm.getNode().getActiveStateNode();
    ExclusiveLimitStateMachineTypeNode exclusiveLimitState =
        appDrivenAlarm.getNode().getLimitStateNode();

    assertEquals(
        Set.of(exclusiveLimitState.getNodeId().expanded()),
        browseTrueSubStates(exclusiveActive.getNodeId(), BrowseDirection.Forward));
    assertEquals(
        Set.of(exclusiveActive.getNodeId().expanded()),
        browseTrueSubStates(exclusiveLimitState.getNodeId(), BrowseDirection.Inverse));
    assertEquals(
        Set.of(), browseTrueSubStates(exclusiveLimitState.getNodeId(), BrowseDirection.Forward));

    TwoStateVariableTypeNode nonExclusiveActive = nonExclusiveAlarm.getNode().getActiveStateNode();
    TwoStateVariableTypeNode highHighState = nonExclusiveAlarm.getNode().getHighHighStateNode();
    TwoStateVariableTypeNode highState = nonExclusiveAlarm.getNode().getHighStateNode();

    assertNull(nonExclusiveAlarm.getNode().getLowStateNode());
    assertNull(nonExclusiveAlarm.getNode().getLowLowStateNode());
    assertEquals(
        Set.of(highHighState.getNodeId().expanded(), highState.getNodeId().expanded()),
        browseTrueSubStates(nonExclusiveActive.getNodeId(), BrowseDirection.Forward));
    assertEquals(
        Set.of(nonExclusiveActive.getNodeId().expanded()),
        browseTrueSubStates(highHighState.getNodeId(), BrowseDirection.Inverse));
    assertEquals(
        Set.of(nonExclusiveActive.getNodeId().expanded()),
        browseTrueSubStates(highState.getNodeId(), BrowseDirection.Inverse));
    assertEquals(Set.of(), browseTrueSubStates(highHighState.getNodeId(), BrowseDirection.Forward));
    assertEquals(Set.of(), browseTrueSubStates(highState.getNodeId(), BrowseDirection.Forward));
  }

  @Test
  void runtimePerLimitSeverityWritesAreObservedWithoutDuplicateEvents() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      EventFilter eventFilter =
          conditionNameFilter(
              "RuntimeSeverity",
              EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
              EventTestSupport.eventField(NodeIds.BaseEventType, "Severity"),
              EventTestSupport.eventField(NodeIds.ConditionType, "LastSeverity"));

      List<Variant[]> events =
          EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter);

      assertRuntimeSeverityUpdate(
          96.0, ushort(800), ushort(801), runtimeSeverityAlarm.getNode()::setSeverityHighHigh);
      assertRuntimeSeverityUpdate(
          92.0, ushort(600), ushort(601), runtimeSeverityAlarm.getNode()::setSeverityHigh);
      assertRuntimeSeverityUpdate(
          8.0, ushort(500), ushort(501), runtimeSeverityAlarm.getNode()::setSeverityLow);
      assertRuntimeSeverityUpdate(
          3.0, ushort(700), ushort(701), runtimeSeverityAlarm.getNode()::setSeverityLowLow);

      EventTestSupport.awaitEvent(
          events,
          "HighHigh runtime severity update",
          e -> ushort(801).equals(e[1].value()) && ushort(800).equals(e[2].value()));
      EventTestSupport.awaitEvent(
          events,
          "High runtime severity update",
          e -> ushort(601).equals(e[1].value()) && ushort(600).equals(e[2].value()));
      EventTestSupport.awaitEvent(
          events,
          "Low runtime severity update",
          e -> ushort(501).equals(e[1].value()) && ushort(500).equals(e[2].value()));
      EventTestSupport.awaitEvent(
          events,
          "LowLow runtime severity update",
          e -> ushort(701).equals(e[1].value()) && ushort(700).equals(e[2].value()));

      assertEquals(8, events.size(), "four state changes plus four effective severity changes");
    } finally {
      subscription.delete();
    }
  }

  @Test
  void runtimeBaseSeverityRemainsAuthoritativeAcrossLimitTransitions() {
    baseSeverityAlarm.setSeverity(ushort(200));
    assertEquals(ushort(200), baseSeverityAlarm.getNode().getSeverity());

    baseSeverityAlarm.evaluate(95.0);
    assertEquals(ushort(600), baseSeverityAlarm.getNode().getSeverity());

    // Updating the public/base Severity while a per-limit override is in effect is remembered.
    baseSeverityAlarm.setSeverity(ushort(300));
    assertEquals(ushort(300), baseSeverityAlarm.getNode().getSeverity());
    baseSeverityAlarm.evaluate(95.0);
    assertEquals(ushort(600), baseSeverityAlarm.getNode().getSeverity());

    // Returning to normal and entering a limit with no override both use the new base Severity.
    baseSeverityAlarm.evaluate(50.0);
    assertEquals(ushort(300), baseSeverityAlarm.getNode().getSeverity());
    baseSeverityAlarm.evaluate(5.0);
    assertEquals(ushort(300), baseSeverityAlarm.getNode().getSeverity());
    baseSeverityAlarm.evaluate(50.0);
    assertEquals(ushort(300), baseSeverityAlarm.getNode().getSeverity());
  }

  @Test
  void activeLimitTransitionRenewsAckCycleWithoutFalseToFalseTransition() throws Exception {
    acknowledgementAlarm.evaluate(92.0);

    TwoStateVariableTypeNode ackedState = acknowledgementAlarm.getNode().getAckedStateNode();
    assertFalse(requireNonNull(ackedState.getId()));
    DateTime firstTransitionTime = ackedState.getTransitionTime();
    ByteString firstEventId = acknowledgementAlarm.currentBranch().getLastEventId();
    assertTrue(acknowledgementAlarm.currentBranch().isAcknowledgeable(firstEventId));

    Thread.sleep(5L);
    acknowledgementAlarm.evaluate(96.0);

    ByteString secondEventId = acknowledgementAlarm.currentBranch().getLastEventId();
    assertFalse(requireNonNull(ackedState.getId()));
    assertEquals(firstTransitionTime, ackedState.getTransitionTime());
    assertNotEquals(firstEventId, secondEventId);
    assertFalse(acknowledgementAlarm.currentBranch().isAcknowledgeable(firstEventId));
    assertTrue(acknowledgementAlarm.currentBranch().isAcknowledgeable(secondEventId));
  }

  @Test
  void nonFiniteEvaluationStillAppliesLazyShelvingExpiry() throws Exception {
    shelvedExclusiveAlarm.evaluate(95.0);
    makeShelvingExpiryDue(shelvedExclusiveAlarm);
    ByteString exclusiveShelvedEvent = shelvedExclusiveAlarm.currentBranch().getLastEventId();

    shelvedExclusiveAlarm.evaluate(Double.NaN);

    assertTrue(shelvedExclusiveAlarm.isActive());
    assertEquals(ExclusiveLimitState.HIGH, shelvedExclusiveAlarm.getLimitState());
    assertUnshelved(shelvedExclusiveAlarm);
    assertNotEquals(exclusiveShelvedEvent, shelvedExclusiveAlarm.currentBranch().getLastEventId());

    shelvedNonExclusiveAlarm.evaluate(95.0);
    makeShelvingExpiryDue(shelvedNonExclusiveAlarm);
    ByteString nonExclusiveShelvedEvent = shelvedNonExclusiveAlarm.currentBranch().getLastEventId();

    shelvedNonExclusiveAlarm.evaluate(Double.POSITIVE_INFINITY);

    assertTrue(shelvedNonExclusiveAlarm.isActive());
    assertTrue(shelvedNonExclusiveAlarm.isLimitActive(ExclusiveLimitState.HIGH));
    assertUnshelved(shelvedNonExclusiveAlarm);
    assertNotEquals(
        nonExclusiveShelvedEvent, shelvedNonExclusiveAlarm.currentBranch().getLastEventId());
  }

  @Test
  void deviationAlarmExposesSetpointNode() throws Exception {
    assertEquals(setpointNodeId, deviationAlarm.getNode().getSetpointNode());

    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      EventFilter eventFilter =
          conditionNameFilter(
              "Deviation",
              EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
              EventTestSupport.eventField(NodeIds.ExclusiveDeviationAlarmType, "SetpointNode"),
              EventTestSupport.eventField(NodeIds.BaseEventType, "EventType"));

      List<Variant[]> events =
          EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter);

      // The application computes the deviation from the setpoint and passes it to evaluate.
      deviationAlarm.evaluate(15.0);

      Variant[] active =
          EventTestSupport.awaitEvent(
              events, "deviation activation", e -> "Active/High".equals(messageOf(e)));

      // SetpointNode is present and selectable, and the event fires as the variant's own type.
      assertEquals(setpointNodeId, active[1].value());
      assertEquals(NodeIds.ExclusiveDeviationAlarmType, active[2].value());
    } finally {
      subscription.delete();
      deviationAlarm.evaluate(0.0);
    }
  }

  @Test
  void rateOfChangeAlarmExposesEngineeringUnits() {
    assertEquals(DEGREES_PER_SECOND, rateOfChangeAlarm.getNode().getEngineeringUnits());
    assertEquals(DEGREES_PER_SECOND, exclusiveRateOfChangeAlarm.getNode().getEngineeringUnits());

    // The application computes the rate and passes it to evaluate.
    rateOfChangeAlarm.evaluate(6.0);
    assertTrue(rateOfChangeAlarm.isActive());
    assertTrue(rateOfChangeAlarm.isLimitActive(ExclusiveLimitState.HIGH));

    rateOfChangeAlarm.evaluate(0.0);
    assertFalse(rateOfChangeAlarm.isActive());

    exclusiveRateOfChangeAlarm.evaluate(6.0);
    assertTrue(exclusiveRateOfChangeAlarm.isActive());
    assertEquals(ExclusiveLimitState.HIGH, exclusiveRateOfChangeAlarm.getLimitState());

    exclusiveRateOfChangeAlarm.evaluate(0.0);
    assertFalse(exclusiveRateOfChangeAlarm.isActive());
  }

  @Test
  void nonExclusiveDeviationAlarmIsInstantiatedAndEvaluated() {
    assertEquals(setpointNodeId, nonExclusiveDeviationAlarm.getNode().getSetpointNode());

    nonExclusiveDeviationAlarm.evaluate(15.0);
    assertTrue(nonExclusiveDeviationAlarm.isActive());
    assertTrue(nonExclusiveDeviationAlarm.isLimitActive(ExclusiveLimitState.HIGH));
    assertFalse(nonExclusiveDeviationAlarm.isLimitActive(ExclusiveLimitState.LOW));

    nonExclusiveDeviationAlarm.evaluate(0.0);
    assertFalse(nonExclusiveDeviationAlarm.isActive());
  }

  @Test
  void setActiveRemainsTheAppDrivenAlternative() {
    ExclusiveLimitStateMachineTypeNode limitStateNode =
        appDrivenAlarm.getNode().getLimitStateNode();
    FiniteStateVariableTypeNode currentState = limitStateNode.getCurrentStateNode();

    appDrivenAlarm.setActive(true, ExclusiveLimitState.HIGH);
    assertTrue(appDrivenAlarm.isActive());
    assertEquals(ExclusiveLimitState.HIGH, appDrivenAlarm.getLimitState());
    assertEquals(NodeIds.ExclusiveLimitStateMachineType_High, currentState.getId());

    // Active and limit state must agree.
    assertThrows(IllegalArgumentException.class, () -> appDrivenAlarm.setActive(true, null));
    assertThrows(
        IllegalArgumentException.class,
        () -> appDrivenAlarm.setActive(false, ExclusiveLimitState.LOW));
    assertThrows(IllegalArgumentException.class, () -> appDrivenAlarm.setActive(true));

    // A named state must have a corresponding configured limit Property.
    assertThrows(
        IllegalArgumentException.class,
        () -> appDrivenAlarm.setActive(true, ExclusiveLimitState.HIGH_HIGH));
    assertThrows(
        IllegalArgumentException.class,
        () -> appDrivenAlarm.setActive(true, ExclusiveLimitState.LOW_LOW));
    assertEquals(ExclusiveLimitState.HIGH, appDrivenAlarm.getLimitState());

    // Deactivation clears the LimitState machine: CurrentState reads NULL (§5.8.19.3).
    appDrivenAlarm.setActive(false);
    assertFalse(appDrivenAlarm.isActive());
    assertNull(appDrivenAlarm.getLimitState());
    assertTrue(currentState.getId().isNull());
  }

  private void assertInvalidExclusiveLimits(Consumer<ConditionBuilder> configure) {
    assertThrows(
        IllegalArgumentException.class, () -> ExclusiveLevelAlarm.create(nodeContext, configure));
  }

  private void assertInvalidDeviation(
      boolean exclusive, Consumer<ConditionBuilder> configure, String limitName) {

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              if (exclusive) {
                ExclusiveDeviationAlarm.create(
                    nodeContext,
                    b -> {
                      b.setpointNode(setpointNodeId);
                      configure.accept(b);
                    });
              } else {
                NonExclusiveDeviationAlarm.create(
                    nodeContext,
                    b -> {
                      b.setpointNode(setpointNodeId);
                      configure.accept(b);
                    });
              }
            });

    assertTrue(
        requireNonNull(exception.getMessage()).contains(limitName + " deviation"),
        exception.getMessage());
  }

  private void assertInvalidSeverity(Consumer<ConditionBuilder> configure, String propertyName) {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> ExclusiveLevelAlarm.create(nodeContext, configure));

    String message = requireNonNull(exception.getMessage());
    assertTrue(message.contains(propertyName), message);
    assertTrue(message.contains("1..1000"), message);
  }

  private Set<ExpandedNodeId> browseTrueSubStates(NodeId nodeId, BrowseDirection direction)
      throws UaException {

    AddressSpace.BrowseOptions options =
        AddressSpace.BrowseOptions.builder()
            .setBrowseDirection(direction)
            .setReferenceType(NodeIds.HasTrueSubState)
            .setIncludeSubtypes(false)
            .build();

    return client.getAddressSpace().browse(nodeId, options).stream()
        .map(ReferenceDescription::getNodeId)
        .collect(Collectors.toUnmodifiableSet());
  }

  private void assertRuntimeSeverityUpdate(
      double value, UShort initial, UShort updated, Consumer<UShort> writeSeverity) {

    runtimeSeverityAlarm.evaluate(value);
    assertEquals(initial, runtimeSeverityAlarm.getNode().getSeverity());
    ByteString stateEventId = runtimeSeverityAlarm.currentBranch().getLastEventId();

    writeSeverity.accept(updated);
    assertEquals(stateEventId, runtimeSeverityAlarm.currentBranch().getLastEventId());

    runtimeSeverityAlarm.evaluate(value);
    assertEquals(updated, runtimeSeverityAlarm.getNode().getSeverity());
    assertEquals(initial, runtimeSeverityAlarm.getNode().getLastSeverity());
    ByteString configurationEventId = runtimeSeverityAlarm.currentBranch().getLastEventId();
    assertNotEquals(stateEventId, configurationEventId);

    // Neither the unchanged sample nor unchanged configuration produces another event.
    runtimeSeverityAlarm.evaluate(value);
    assertEquals(configurationEventId, runtimeSeverityAlarm.currentBranch().getLastEventId());
  }

  private void makeShelvingExpiryDue(LimitAlarm alarm) throws Exception {
    ShelvedStateMachineTypeNode shelvingState = requireNonNull(alarm.getShelvingState());
    NodeId timedShelveMethodId =
        requireNonNull(shelvingState.getTimedShelveMethodNode()).getNodeId();

    CallMethodResult result =
        call(shelvingState.getNodeId(), timedShelveMethodId, new Variant(30_000.0));
    assertTrue(requireNonNull(result.getStatusCode()).isGood());

    ShelvingRuntime shelvingRuntime = requireNonNull(alarm.getShelvingRuntime());
    shelvingRuntime.setExpiryTimerSuppressedForTesting(true);
    shelvingRuntime.makeExpiryDueForTesting();
  }

  private static void assertUnshelved(LimitAlarm alarm) {
    ShelvedStateMachineTypeNode shelvingState = requireNonNull(alarm.getShelvingState());
    LocalizedText currentState = requireNonNull(shelvingState.getCurrentState());

    assertEquals("Unshelved", currentState.text());
    assertEquals(Boolean.FALSE, alarm.getNode().getSuppressedOrShelved());
  }

  private CallMethodResult call(NodeId objectId, NodeId methodId, Variant... inputs)
      throws Exception {

    CallResponse response = client.call(List.of(new CallMethodRequest(objectId, methodId, inputs)));

    return requireNonNull(response.getResults())[0];
  }

  /**
   * Create an {@link EventFilter} with the given select clauses, matching only events whose
   * ConditionName equals {@code conditionName} — scoping each test to its own alarm's events.
   */
  private static EventFilter conditionNameFilter(
      String conditionName, SimpleAttributeOperand... selectClauses) {

    var whereClause =
        new ContentFilter(
            new ContentFilterElement[] {
              new ContentFilterElement(
                  FilterOperator.Equals,
                  new ExtensionObject[] {
                    ExtensionObject.encode(
                        DefaultEncodingContext.INSTANCE,
                        EventTestSupport.eventField(NodeIds.ConditionType, "ConditionName")),
                    ExtensionObject.encode(
                        DefaultEncodingContext.INSTANCE,
                        new LiteralOperand(new Variant(conditionName)))
                  })
            });

    return new EventFilter(selectClauses, whereClause);
  }

  private static @Nullable String messageOf(Variant[] eventFields) {
    return eventFields[0].value() instanceof LocalizedText text ? text.text() : null;
  }

  private static boolean activeIdOf(Variant[] eventFields) {
    return Boolean.TRUE.equals(eventFields[3].value());
  }

  private static @Nullable String limitStateOf(Variant[] eventFields) {
    return eventFields[4].value() instanceof LocalizedText text ? text.text() : null;
  }
}
