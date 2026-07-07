/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.subscriptions;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElementResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventFilterWhereClauseOperatorTest extends AbstractClientServerTest {

  private OpcUaSubscription subscription;

  @BeforeEach
  void setUp() throws Exception {
    subscription = new OpcUaSubscription(client);
    subscription.create();
  }

  @AfterEach
  void tearDown() throws Exception {
    subscription.delete();
  }

  @Test
  void testWhereClauseOperatorsDeliverMatchingEvents() throws Exception {
    List<OperatorCase> cases =
        List.of(
            new OperatorCase(
                "Equals",
                where(element(FilterOperator.Equals, eventField("Severity"), literal(ushort(2))))),
            new OperatorCase("IsNull", where(element(FilterOperator.IsNull, literal(null)))),
            new OperatorCase(
                "GreaterThan",
                where(
                    element(
                        FilterOperator.GreaterThan, eventField("Severity"), literal(ushort(1))))),
            new OperatorCase(
                "LessThan",
                where(
                    element(FilterOperator.LessThan, eventField("Severity"), literal(ushort(3))))),
            new OperatorCase(
                "GreaterThanOrEqual",
                where(
                    element(
                        FilterOperator.GreaterThanOrEqual,
                        eventField("Severity"),
                        literal(ushort(2))))),
            new OperatorCase(
                "LessThanOrEqual",
                where(
                    element(
                        FilterOperator.LessThanOrEqual,
                        eventField("Severity"),
                        literal(ushort(2))))),
            new OperatorCase(
                "Like",
                where(element(FilterOperator.Like, eventField("Message"), literal("event%")))),
            new OperatorCase(
                "Not",
                where(
                    element(FilterOperator.Not, elementOperand(1)),
                    element(FilterOperator.Equals, eventField("Severity"), literal(ushort(99))))),
            new OperatorCase(
                "Between",
                where(
                    element(
                        FilterOperator.Between,
                        eventField("Severity"),
                        literal(ushort(1)),
                        literal(ushort(3))))),
            new OperatorCase(
                "InList",
                where(
                    element(
                        FilterOperator.InList,
                        eventField("Severity"),
                        literal(ushort(1)),
                        literal(ushort(2)),
                        literal(ushort(3))))),
            new OperatorCase(
                "And",
                where(
                    element(FilterOperator.And, elementOperand(1), elementOperand(2)),
                    element(
                        FilterOperator.GreaterThanOrEqual,
                        eventField("Severity"),
                        literal(ushort(1))),
                    element(
                        FilterOperator.LessThanOrEqual,
                        eventField("Severity"),
                        literal(ushort(3))))),
            new OperatorCase(
                "Or",
                where(
                    element(FilterOperator.Or, elementOperand(1), elementOperand(2)),
                    element(FilterOperator.Equals, eventField("Severity"), literal(ushort(99))),
                    element(FilterOperator.Equals, eventField("Severity"), literal(ushort(2))))),
            new OperatorCase(
                "Cast",
                where(
                    element(FilterOperator.Equals, elementOperand(1), literal(2)),
                    element(FilterOperator.Cast, eventField("Severity"), literal(NodeIds.Int32)))),
            new OperatorCase(
                "BitwiseAnd",
                where(
                    element(FilterOperator.Equals, elementOperand(1), literal(ushort(2))),
                    element(
                        FilterOperator.BitwiseAnd, eventField("Severity"), literal(ushort(2))))),
            new OperatorCase(
                "BitwiseOr",
                where(
                    element(FilterOperator.Equals, elementOperand(1), literal(ushort(6))),
                    element(FilterOperator.BitwiseOr, eventField("Severity"), literal(ushort(4))))),
            new OperatorCase(
                "OfType", where(element(FilterOperator.OfType, literal(NodeIds.BaseEventType)))));

    CountDownLatch latch = new CountDownLatch(cases.size());
    List<MonitoredCase> monitoredCases = new ArrayList<>();

    for (OperatorCase operatorCase : cases) {
      CopyOnWriteArrayList<Variant[]> events = new CopyOnWriteArrayList<>();
      OpcUaMonitoredItem item =
          OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter(operatorCase.whereClause()));

      item.setQueueSize(uint(10));
      item.setEventValueListener(
          (monitoredItem, eventFields) -> {
            events.add(eventFields);
            latch.countDown();
          });

      subscription.addMonitoredItem(item);
      monitoredCases.add(new MonitoredCase(operatorCase, item, events));
    }

    subscription.synchronizeMonitoredItems();

    for (MonitoredCase monitoredCase : monitoredCases) {
      assertGoodFilterResult(monitoredCase.item(), monitoredCase.operatorCase().whereClause());
    }

    fireBaseEvent(ushort(2), "event message!");

    assertTrue(latch.await(5, TimeUnit.SECONDS), "matching events were not delivered");

    for (MonitoredCase monitoredCase : monitoredCases) {
      assertFalse(
          monitoredCase.events().isEmpty(),
          monitoredCase.operatorCase().name() + " did not receive a matching event");

      Variant[] eventFields = monitoredCase.events().get(0);

      assertNotNull(eventFields);
      assertEquals("event message!", ((LocalizedText) eventFields[0].value()).text());
      assertEquals(ushort(2), eventFields[1].value());
    }
  }

  @Test
  void testNonMatchingWhereClauseSuppressesEvent() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    CopyOnWriteArrayList<Variant[]> events = new CopyOnWriteArrayList<>();

    ContentFilter whereClause =
        where(
            element(
                FilterOperator.InList,
                eventField("Severity"),
                literal(ushort(99)),
                literal(ushort(100))));

    OpcUaMonitoredItem item =
        OpcUaMonitoredItem.newEventItem(NodeIds.Server, eventFilter(whereClause));

    item.setEventValueListener(
        (monitoredItem, eventFields) -> {
          events.add(eventFields);
          latch.countDown();
        });

    subscription.addMonitoredItem(item);
    subscription.synchronizeMonitoredItems();

    assertGoodFilterResult(item, whereClause);

    fireBaseEvent(ushort(2), "event message!");

    assertFalse(latch.await(2, TimeUnit.SECONDS), "non-matching event was delivered");
    assertTrue(events.isEmpty());
  }

  private EventFilter eventFilter(ContentFilter whereClause) {
    return new EventFilter(
        new SimpleAttributeOperand[] {eventField("Message"), eventField("Severity")}, whereClause);
  }

  private void assertGoodFilterResult(OpcUaMonitoredItem item, ContentFilter whereClause)
      throws Exception {

    assertTrue(item.getCreateResult().orElseThrow().isGood());

    EventFilterResult result =
        (EventFilterResult)
            item.getFilterResult().orElseThrow().decode(client.getStaticEncodingContext());

    for (StatusCode selectClauseResult :
        requireNonNullElse(result.getSelectClauseResults(), new StatusCode[0])) {
      assertTrue(selectClauseResult.isGood());
    }

    ContentFilterElementResult[] elementResults =
        requireNonNullElse(
            result.getWhereClauseResult().getElementResults(), new ContentFilterElementResult[0]);
    ContentFilterElement[] elements = whereClause.getElements();

    assertEquals(elements.length, elementResults.length);

    for (ContentFilterElementResult elementResult : elementResults) {
      assertTrue(elementResult.getStatusCode().isGood());

      for (StatusCode operandStatus :
          requireNonNullElse(elementResult.getOperandStatusCodes(), new StatusCode[0])) {
        assertTrue(operandStatus.isGood());
      }
    }
  }

  private void fireBaseEvent(UShort severity, String message) throws Exception {
    UUID eventId = UUID.randomUUID();
    BaseEventTypeNode eventNode =
        server
            .getEventFactory()
            .createEvent(
                newNodeId("EventFilterWhereClauseOperatorTest/" + eventId), NodeIds.BaseEventType);

    try {
      eventNode.setBrowseName(newQualifiedName("EventFilterWhereClauseOperatorTest"));
      eventNode.setDisplayName(LocalizedText.english("EventFilterWhereClauseOperatorTest"));
      eventNode.setEventId(toByteString(eventId));
      eventNode.setEventType(NodeIds.BaseEventType);
      eventNode.setSourceNode(NodeIds.Server);
      eventNode.setSourceName("Server");
      eventNode.setTime(DateTime.now());
      eventNode.setReceiveTime(DateTime.NULL_VALUE);
      eventNode.setMessage(LocalizedText.english(message));
      eventNode.setSeverity(severity);

      server.getEventNotifier().fire(eventNode);
    } finally {
      eventNode.delete();
    }
  }

  private static ByteString toByteString(UUID uuid) {
    ByteBuffer buffer = ByteBuffer.allocate(16);
    buffer.putLong(uuid.getMostSignificantBits());
    buffer.putLong(uuid.getLeastSignificantBits());
    return ByteString.of(buffer.array());
  }

  private static ContentFilter where(ContentFilterElement... elements) {
    return new ContentFilter(elements);
  }

  private static ContentFilterElement element(FilterOperator operator, FilterOperand... operands) {
    ExtensionObject[] encodedOperands = new ExtensionObject[operands.length];

    for (int i = 0; i < operands.length; i++) {
      encodedOperands[i] = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, operands[i]);
    }

    return new ContentFilterElement(operator, encodedOperands);
  }

  private static SimpleAttributeOperand eventField(String browseName) {
    return new SimpleAttributeOperand(
        NodeIds.BaseEventType,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  private static LiteralOperand literal(Object value) {
    return new LiteralOperand(new Variant(value));
  }

  private static ElementOperand elementOperand(int index) {
    return new ElementOperand(uint(index));
  }

  private record OperatorCase(String name, ContentFilter whereClause) {}

  private record MonitoredCase(
      OperatorCase operatorCase, OpcUaMonitoredItem item, CopyOnWriteArrayList<Variant[]> events) {}
}
