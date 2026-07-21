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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.EventTestSupport;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Client-visible tests for the WP6 discrete-alarm family: the OffNormalAlarm
 * evaluate/acknowledge/deactivate lifecycle with the NormalState Property, and the smoke assertion
 * that each subtype fires as its own event type.
 */
public class DiscreteAlarmTest extends AbstractClientServerTest {

  /** One builder-created alarm of each discrete type and the EventType its events must carry. */
  private record SmokeAlarm(String conditionName, NodeId eventTypeId, DiscreteAlarm alarm) {}

  private OffNormalAlarm lifecycleAlarm;
  private NodeId conditionId;
  private NodeId inputNodeId;
  private NodeId normalStateNodeId;

  private final List<SmokeAlarm> smokeAlarms = new ArrayList<>();

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          var source =
              new UaObjectNode(
                  context,
                  newNodeId("DiscreteAlarmTest/Source"),
                  newQualifiedName("DiscreteAlarmTest/Source"),
                  LocalizedText.english("DiscreteAlarmTest/Source"),
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

          inputNodeId = newNodeId("DiscreteAlarmTest/Input");
          normalStateNodeId = newNodeId("DiscreteAlarmTest/NormalState");

          try {
            lifecycleAlarm =
                OffNormalAlarm.create(
                    context,
                    b ->
                        b.nodeId(newNodeId("DiscreteAlarmTest/OffNormalLifecycle"))
                            .browseName(newQualifiedName("OffNormalLifecycle"))
                            .conditionSource(source)
                            .inputNode(inputNodeId)
                            .normalState(normalStateNodeId));

            smokeAlarms.add(
                new SmokeAlarm(
                    "SmokeDiscreteAlarm",
                    NodeIds.DiscreteAlarmType,
                    DiscreteAlarm.create(context, smokeConfig("SmokeDiscreteAlarm", source))));
            smokeAlarms.add(
                new SmokeAlarm(
                    "SmokeOffNormalAlarm",
                    NodeIds.OffNormalAlarmType,
                    OffNormalAlarm.create(context, smokeConfig("SmokeOffNormalAlarm", source))));
            smokeAlarms.add(
                new SmokeAlarm(
                    "SmokeSystemOffNormalAlarm",
                    NodeIds.SystemOffNormalAlarmType,
                    SystemOffNormalAlarm.create(
                        context, smokeConfig("SmokeSystemOffNormalAlarm", source))));
            smokeAlarms.add(
                new SmokeAlarm(
                    "SmokeTripAlarm",
                    NodeIds.TripAlarmType,
                    TripAlarm.create(context, smokeConfig("SmokeTripAlarm", source))));
            smokeAlarms.add(
                new SmokeAlarm(
                    "SmokeInstrumentDiagnosticAlarm",
                    NodeIds.InstrumentDiagnosticAlarmType,
                    InstrumentDiagnosticAlarm.create(
                        context, smokeConfig("SmokeInstrumentDiagnosticAlarm", source))));
            smokeAlarms.add(
                new SmokeAlarm(
                    "SmokeSystemDiagnosticAlarm",
                    NodeIds.SystemDiagnosticAlarmType,
                    SystemDiagnosticAlarm.create(
                        context, smokeConfig("SmokeSystemDiagnosticAlarm", source))));
          } catch (UaException e) {
            throw new RuntimeException(e);
          }

          server.getConditionManager().register(lifecycleAlarm);
          smokeAlarms.forEach(smoke -> server.getConditionManager().register(smoke.alarm()));

          conditionId = lifecycleAlarm.getConditionId();
        });
  }

  private Consumer<ConditionBuilder> smokeConfig(String name, UaNode source) {
    return b ->
        b.nodeId(newNodeId("DiscreteAlarmTest/" + name))
            .browseName(newQualifiedName(name))
            .conditionSource(source);
  }

  @BeforeEach
  void resetAlarmState() {
    lifecycleAlarm.setActive(false);
    lifecycleAlarm.setAcked(true);

    for (SmokeAlarm smoke : smokeAlarms) {
      smoke.alarm().setActive(false);
      smoke.alarm().setAcked(true);
    }
  }

  @Test
  void offNormalLifecycle() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      EventFilter eventFilter =
          conditionNameFilter(
              "OffNormalLifecycle",
              EventTestSupport.eventField(NodeIds.BaseEventType, "Message"),
              EventTestSupport.eventField(NodeIds.AcknowledgeableConditionType, "AckedState", "Id"),
              EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"),
              EventTestSupport.eventField(NodeIds.ConditionType, "Retain"));

      List<Variant[]> events =
          EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter);

      // The input differs from the normal state: active, needing acknowledgement, retained.
      lifecycleAlarm.evaluate("Stopped", "Running");
      EventTestSupport.awaitEvent(
          events,
          "activation event",
          e -> "Active".equals(messageOf(e)) && activeIdOf(e) && !ackedIdOf(e) && retainOf(e));
      assertTrue(lifecycleAlarm.isActive());
      assertTrue(lifecycleAlarm.isRetained());

      // Acknowledge with the activation EventId; the alarm stays retained while active.
      ByteString eventId = lifecycleAlarm.currentBranch().getLastEventId();
      CallMethodResult ack = acknowledge(eventId);
      assertTrue(requireNonNull(ack.getStatusCode()).isGood());
      EventTestSupport.awaitEvent(
          events,
          "acknowledged while active",
          e -> "Acknowledged".equals(messageOf(e)) && ackedIdOf(e) && retainOf(e));

      // The input returns to the normal state: inactive and acknowledged, so Retain retracts.
      lifecycleAlarm.evaluate("Running", "Running");
      EventTestSupport.awaitEvent(
          events,
          "deactivation retraction",
          e -> "Inactive".equals(messageOf(e)) && !activeIdOf(e) && !retainOf(e));
      assertFalse(lifecycleAlarm.isActive());
      assertFalse(lifecycleAlarm.isRetained());
    } finally {
      subscription.delete();
    }
  }

  @Test
  void evaluateUsesVariantEqualitySemantics() {
    // A primitive array and a boxed array with equal elements are the same UA value; they must
    // compare equal (no activation), matching Variant equality semantics.
    lifecycleAlarm.evaluate(new int[] {1, 2, 3}, new Integer[] {1, 2, 3});
    assertFalse(lifecycleAlarm.isActive());

    // Variant wrappers are unwrapped before comparison.
    lifecycleAlarm.evaluate(new Variant("Running"), "Running");
    assertFalse(lifecycleAlarm.isActive());

    // Values that differ under Variant semantics still activate.
    lifecycleAlarm.evaluate(new int[] {1, 2, 3}, new Integer[] {1, 2, 4});
    assertTrue(lifecycleAlarm.isActive());
  }

  @Test
  void normalStateIsReadable() throws Exception {
    PropertyTypeNode normalStateNode =
        requireNonNull(lifecycleAlarm.getNode().getNormalStateNode());

    DataValue value = client.readValue(0.0, TimestampsToReturn.Both, normalStateNode.getNodeId());

    assertTrue(requireNonNull(value.getStatusCode()).isGood());
    assertEquals(normalStateNodeId, value.getValue().value());
  }

  @Test
  void eachSubtypeFiresAsItsOwnEventType() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    try {
      for (SmokeAlarm smoke : smokeAlarms) {
        EventFilter eventFilter =
            conditionNameFilter(
                smoke.conditionName(),
                EventTestSupport.eventField(NodeIds.BaseEventType, "EventType"),
                EventTestSupport.eventField(NodeIds.AlarmConditionType, "ActiveState", "Id"));

        List<Variant[]> events =
            EventTestSupport.monitorEvents(subscription, NodeIds.Server, eventFilter);

        smoke.alarm().setActive(true);

        EventTestSupport.awaitEvent(
            events,
            smoke.conditionName() + " activation with EventType " + smoke.eventTypeId(),
            e -> smoke.eventTypeId().equals(e[0].value()) && Boolean.TRUE.equals(e[1].value()));
      }
    } finally {
      subscription.delete();
    }
  }

  private CallMethodResult acknowledge(ByteString eventId) throws Exception {
    return call(
        conditionId,
        NodeIds.AcknowledgeableConditionType_Acknowledge,
        new Variant(eventId),
        new Variant(LocalizedText.NULL_VALUE));
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

  private static boolean ackedIdOf(Variant[] eventFields) {
    return Boolean.TRUE.equals(eventFields[1].value());
  }

  private static boolean activeIdOf(Variant[] eventFields) {
    return Boolean.TRUE.equals(eventFields[2].value());
  }

  private static boolean retainOf(Variant[] eventFields) {
    return Boolean.TRUE.equals(eventFields[3].value());
  }
}
