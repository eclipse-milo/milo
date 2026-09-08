/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.items;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElementResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MonitoredEventItemTest {

  private final OpcUaServer server = mock(OpcUaServer.class);
  private final BaseEventTypeNode eventNode = mock(BaseEventTypeNode.class);

  private MonitoredEventItem item;

  @BeforeEach
  void setUp() {
    when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    when(server.getReferenceTypeTree()).thenReturn(mock(ReferenceTypeTree.class));

    UaNodeContext nodeContext = mock(UaNodeContext.class);
    when(nodeContext.getServer()).thenReturn(server);
    when(eventNode.getNodeContext()).thenReturn(nodeContext);

    item =
        new MonitoredEventItem(
            server,
            mock(Session.class),
            uint(1),
            uint(1),
            new ReadValueId(NodeIds.Server, AttributeId.EventNotifier.uid(), null, null),
            MonitoringMode.Reporting,
            TimestampsToReturn.Neither,
            uint(1),
            0.0,
            uint(10),
            true);
  }

  @Nested
  class FilterArming {

    /**
     * Element 1's operand is a structure that is not a FilterOperand, so validation reports the
     * element as Good but its operand as Bad_FilterOperandInvalid. Element 0 is Or(true, Element
     * 1), which never resolves element 1, so the where clause evaluates to true if it is armed.
     */
    private final ContentFilter badOperandWhereClause =
        where(
            element(FilterOperator.Or, literal(true), elementOperand(1)),
            element(FilterOperator.IsNull, notAFilterOperand()));

    @Test
    void filterWithBadOperandStatusIsNotArmed() throws Exception {
      item.installFilter(eventFilter(badOperandWhereClause));

      EventFilterResult filterResult = decodeFilterResult();
      ContentFilterElementResult elementResult =
          filterResult.getWhereClauseResult().getElementResults()[1];
      assertTrue(elementResult.getStatusCode().isGood(), "element status");
      assertEquals(
          StatusCodes.Bad_FilterOperandInvalid,
          elementResult.getOperandStatusCodes()[0].value(),
          "operand status");

      item.onEvent(eventNode);

      assertEquals(
          0, drainNotifications().size(), "event delivered by a filter with a bad operand");
    }

    // Control for the test above: the same where clause shape with a valid operand is armed.
    @Test
    void filterWithGoodOperandStatusIsArmed() throws Exception {
      item.installFilter(
          eventFilter(
              where(
                  element(FilterOperator.Or, literal(true), elementOperand(1)),
                  element(FilterOperator.IsNull, literal(null)))));

      item.onEvent(eventNode);

      assertEquals(1, drainNotifications().size());
    }
  }

  private EventFilterResult decodeFilterResult() {
    return (EventFilterResult) item.getFilterResult().decode(DefaultEncodingContext.INSTANCE);
  }

  private List<UaStructuredType> drainNotifications() {
    var notifications = new ArrayList<UaStructuredType>();
    item.getNotifications(notifications, Integer.MAX_VALUE);
    return notifications;
  }

  private static EventFilter eventFilter(ContentFilter whereClause) {
    SimpleAttributeOperand selectClause =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "Message")},
            AttributeId.Value.uid(),
            null);

    return new EventFilter(new SimpleAttributeOperand[] {selectClause}, whereClause);
  }

  private static ContentFilter where(ContentFilterElement... elements) {
    return new ContentFilter(elements);
  }

  private static ContentFilterElement element(FilterOperator operator, Object... operands) {
    var encodedOperands = new ExtensionObject[operands.length];

    for (int i = 0; i < operands.length; i++) {
      encodedOperands[i] =
          operands[i] instanceof ExtensionObject xo
              ? xo
              : ExtensionObject.encode(
                  DefaultEncodingContext.INSTANCE, (FilterOperand) operands[i]);
    }

    return new ContentFilterElement(operator, encodedOperands);
  }

  private static ExtensionObject notAFilterOperand() {
    return ExtensionObject.encode(
        DefaultEncodingContext.INSTANCE,
        new ReadValueId(NodeIds.Server, AttributeId.Value.uid(), null, null));
  }

  private static LiteralOperand literal(Object value) {
    return new LiteralOperand(new Variant(value));
  }

  private static ElementOperand elementOperand(int index) {
    return new ElementOperand(uint(index));
  }
}
