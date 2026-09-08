/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class EventContentFilterTest {

  @Test
  public void testSupportedOperatorsDoNotReportUnsupported() throws Exception {
    ContentFilterElement[] elements =
        new ContentFilterElement[] {
          element(FilterOperator.Equals, literal(1), literal(1)),
          element(FilterOperator.IsNull, literal(null)),
          element(FilterOperator.GreaterThan, literal(2), literal(1)),
          element(FilterOperator.LessThan, literal(1), literal(2)),
          element(FilterOperator.GreaterThanOrEqual, literal(2), literal(2)),
          element(FilterOperator.LessThanOrEqual, literal(2), literal(2)),
          element(FilterOperator.Like, literal("event message"), literal("event%")),
          element(FilterOperator.Not, literal(false)),
          element(
              FilterOperator.Between, literal(ushort(2)), literal(ushort(1)), literal(ushort(3))),
          element(
              FilterOperator.InList, literal(ushort(2)), literal(ushort(1)), literal(ushort(2))),
          element(FilterOperator.And, literal(true), literal(true)),
          element(FilterOperator.Or, literal(false), literal(true)),
          element(FilterOperator.Cast, literal("2"), literal(NodeIds.Int32)),
          element(FilterOperator.BitwiseAnd, literal(ushort(2)), literal(ushort(2))),
          element(FilterOperator.BitwiseOr, literal(ushort(2)), literal(ushort(4))),
          element(FilterOperator.OfType, literal(NodeIds.BaseEventType))
        };

    EventFilterResult result = EventContentFilter.validate(filterContext(), eventFilter(elements));

    ContentFilterElementResult[] elementResults = result.getWhereClauseResult().getElementResults();

    assertEquals(elements.length, elementResults.length);

    for (ContentFilterElementResult elementResult : elementResults) {
      assertTrue(elementResult.getStatusCode().isGood());
    }
  }

  @Test
  public void testUnsupportedOperatorsStillReportUnsupported() throws Exception {
    EventFilterResult result =
        EventContentFilter.validate(
            filterContext(),
            eventFilter(
                new ContentFilterElement[] {
                  element(FilterOperator.InView, literal(uint(1))),
                  element(FilterOperator.RelatedTo, literal(uint(1)))
                }));

    ContentFilterElementResult[] elementResults = result.getWhereClauseResult().getElementResults();

    assertEquals(
        StatusCodes.Bad_FilterOperatorUnsupported, elementResults[0].getStatusCode().value());
    assertEquals(
        StatusCodes.Bad_FilterOperatorUnsupported, elementResults[1].getStatusCode().value());
  }

  @Nested
  class OperandDecoding {

    // Operands are decoded again on every event. An operand that decodes to a structure other
    // than a FilterOperand must be reported as a filter error, not as an unchecked
    // ClassCastException
    // escaping the event delivery path.
    @Test
    void operandThatIsNotAFilterOperandFailsEvaluationWithBadFilterOperandInvalid() {
      ExtensionObject notAFilterOperand =
          ExtensionObject.encode(
              DefaultEncodingContext.INSTANCE,
              new ReadValueId(NodeIds.Server, AttributeId.Value.uid(), null, null));

      ContentFilter whereClause =
          new ContentFilter(
              new ContentFilterElement[] {
                new ContentFilterElement(
                    FilterOperator.IsNull, new ExtensionObject[] {notAFilterOperand})
              });

      UaException e =
          assertThrows(
              UaException.class,
              () ->
                  EventContentFilter.evaluate(
                      filterContext(), whereClause, mock(BaseEventTypeNode.class)));

      assertEquals(StatusCodes.Bad_FilterOperandInvalid, e.getStatusCode().value());
    }

    // An operand with an encoding id the server cannot decode must likewise be reported as a
    // filter error rather than an unchecked UaSerializationException.
    @Test
    void operandThatCannotBeDecodedFailsEvaluationWithBadFilterOperandInvalid() {
      ExtensionObject undecodable =
          ExtensionObject.of(ByteString.of(new byte[] {0}), new NodeId(2, 999));

      ContentFilter whereClause =
          new ContentFilter(
              new ContentFilterElement[] {
                new ContentFilterElement(FilterOperator.IsNull, new ExtensionObject[] {undecodable})
              });

      UaException e =
          assertThrows(
              UaException.class,
              () ->
                  EventContentFilter.evaluate(
                      filterContext(), whereClause, mock(BaseEventTypeNode.class)));

      assertEquals(StatusCodes.Bad_FilterOperandInvalid, e.getStatusCode().value());
    }
  }

  @Nested
  class ElementOperandValidation {

    /**
     * Part 4 §7.7.4.2: an ElementOperand index is valid only if it is greater than the index of the
     * element it is part of and it references an existing element.
     */
    @ParameterizedTest(name = "element {0} referencing element {1}")
    @CsvSource({"0, 0", "1, 0", "1, 1", "0, 2", "1, 2"})
    void backwardSelfOrOutOfRangeElementOperandIsRejected(int elementIndex, int referencedIndex)
        throws Exception {

      ContentFilterElement[] elements = new ContentFilterElement[2];
      elements[elementIndex] = element(FilterOperator.Not, elementOperand(referencedIndex));
      elements[1 - elementIndex] = element(FilterOperator.IsNull, literal(null));

      EventFilterResult result =
          EventContentFilter.validate(filterContext(), eventFilter(elements));

      ContentFilterElementResult elementResult =
          result.getWhereClauseResult().getElementResults()[elementIndex];

      assertEquals(
          StatusCodes.Bad_FilterOperandInvalid,
          elementResult.getOperandStatusCodes()[0].value(),
          "operand status code");
    }

    @Test
    void forwardElementOperandIsAccepted() throws Exception {
      ContentFilterElement[] elements = {
        element(FilterOperator.Not, elementOperand(1)),
        element(FilterOperator.IsNull, literal(null))
      };

      EventFilterResult result =
          EventContentFilter.validate(filterContext(), eventFilter(elements));

      ContentFilterElementResult elementResult =
          result.getWhereClauseResult().getElementResults()[0];

      assertTrue(elementResult.getStatusCode().isGood());
      assertTrue(elementResult.getOperandStatusCodes()[0].isGood());
    }
  }

  private static FilterContext filterContext() {
    OpcUaServer server = mock(OpcUaServer.class);
    when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);

    FilterContext context = mock(FilterContext.class);
    when(context.getServer()).thenReturn(server);

    return context;
  }

  private static EventFilter eventFilter(ContentFilterElement[] elements) {
    SimpleAttributeOperand selectClause =
        new SimpleAttributeOperand(
            NodeIds.BaseEventType,
            new QualifiedName[] {new QualifiedName(0, "Message")},
            AttributeId.Value.uid(),
            null);

    return new EventFilter(
        new SimpleAttributeOperand[] {selectClause}, new ContentFilter(elements));
  }

  private static ContentFilterElement element(FilterOperator operator, FilterOperand... operands) {
    ExtensionObject[] encodedOperands = new ExtensionObject[operands.length];

    for (int i = 0; i < operands.length; i++) {
      encodedOperands[i] = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, operands[i]);
    }

    return new ContentFilterElement(operator, encodedOperands);
  }

  private static LiteralOperand literal(Object value) {
    return new LiteralOperand(new Variant(value));
  }

  private static ElementOperand elementOperand(int index) {
    return new ElementOperand(uint(index));
  }
}
