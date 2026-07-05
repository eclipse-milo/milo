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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.EventFieldDefinition;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

/**
 * Synthesizes the field values of an {@code EventQueueOverflowEventType} event (Part 14 §6.2.6.2)
 * against the dataset's configured {@link EventFieldDefinition} select clauses, entirely from
 * stack-core types.
 *
 * <p>This lives in {@code sdk-pubsub} core, which has no dependency on {@code sdk-server}, so it
 * cannot use the server-side {@code EventFactory}/{@code BaseEventTypeNode} machinery that {@code
 * MonitoredEventItem} uses to fill a real event and run its select clauses. Instead it performs a
 * self-contained micro-select: for each configured field it accepts the operand only when it
 * plainly selects the {@code Value} attribute of a standard {@code BaseEventType} field by a
 * single-element browse path, and supplies that field's overflow value; everything else — a
 * filtered attribute, an index range, a multi-element path, an unrelated type definition, or an
 * unrecognized field name — yields {@link Variant#NULL_VALUE}. The resulting list always has
 * exactly one entry per configured field, so it matches the writer's metadata field count by
 * construction.
 *
 * <p>The values mirror {@code MonitoredEventItem.generateOverflowEventFields()}: a fresh 16-byte
 * random {@code EventId}, {@code EventType} = {@code EventQueueOverflowEventType} (i=3035), {@code
 * SourceNode} = {@code Server} (i=2253), {@code SourceName} = {@code "Server"}, {@code Time} = now,
 * {@code ReceiveTime} = null, {@code Message} = "Event queue overflow", and {@code Severity} = 0.
 * Severity 0 is a deliberate Milo convention shared with {@code MonitoredEventItem}: the overflow
 * notice is not itself an alarm and carries the lowest severity so subscribers never mistake it for
 * a real high-severity event.
 *
 * <p>Thread-safe and side-effect-free (a fresh {@link SecureRandom} per call, {@link
 * DateTime#now()} captured per call), so it is safe to call from the arbitrary producer thread that
 * detects overflow inside {@link DataSetWriterRuntime#offerEvent}.
 */
final class OverflowEventFields {

  private OverflowEventFields() {}

  /**
   * Build the overflow-event field values for {@code fields}, one {@link DataValue} per configured
   * event field, in wire order.
   */
  static List<DataValue> forFields(List<EventFieldDefinition> fields) {
    byte[] eventId = new byte[16];
    new SecureRandom().nextBytes(eventId);
    DateTime now = DateTime.now();

    var values = new ArrayList<DataValue>(fields.size());
    for (EventFieldDefinition field : fields) {
      values.add(new DataValue(overflowValue(field.getSelectedField(), eventId, now)));
    }
    return values;
  }

  private static Variant overflowValue(
      SimpleAttributeOperand operand, byte[] eventId, DateTime now) {
    String fieldName = selectedBaseEventFieldName(operand);
    if (fieldName == null) {
      return Variant.NULL_VALUE;
    }
    return switch (fieldName) {
      case "EventId" -> Variant.ofByteString(ByteString.of(eventId));
      case "EventType" -> Variant.ofNodeId(NodeIds.EventQueueOverflowEventType);
      case "SourceNode" -> Variant.ofNodeId(NodeIds.Server);
      case "SourceName" -> Variant.ofString("Server");
      case "Time" -> Variant.ofDateTime(now);
      case "ReceiveTime" -> Variant.ofDateTime(DateTime.NULL_VALUE);
      case "Message" -> Variant.ofLocalizedText(LocalizedText.english("Event queue overflow"));
      case "Severity" -> Variant.ofUInt16(ushort(0));
      default -> Variant.NULL_VALUE;
    };
  }

  /**
   * The single {@code BaseEventType} field name this operand plainly selects, or {@code null} when
   * the operand does not cleanly name a scalar {@code Value}-attribute field of the event.
   *
   * <p>Accepts only {@code attributeId == Value}, a browse path of exactly one non-null {@link
   * QualifiedName}, no index range, and a {@code typeDefinitionId} that is Java-null (treated as
   * {@code BaseEventType}, matching {@code EventContentFilter}), {@code BaseEventType} (i=2041), or
   * {@code EventQueueOverflowEventType} (i=3035); {@code NodeId.NULL_VALUE} and any other type are
   * rejected.
   */
  private static @Nullable String selectedBaseEventFieldName(SimpleAttributeOperand operand) {
    if (!AttributeId.Value.uid().equals(operand.getAttributeId())) {
      return null;
    }

    String indexRange = operand.getIndexRange();
    if (indexRange != null && !indexRange.isEmpty()) {
      return null;
    }

    NodeId typeDefinitionId = operand.getTypeDefinitionId();
    boolean typeOk =
        typeDefinitionId == null
            || NodeIds.BaseEventType.equals(typeDefinitionId)
            || NodeIds.EventQueueOverflowEventType.equals(typeDefinitionId);
    if (!typeOk) {
      return null;
    }

    QualifiedName @Nullable [] browsePath = operand.getBrowsePath();
    if (browsePath == null || browsePath.length != 1) {
      return null;
    }
    QualifiedName field = browsePath[0];
    // the standard BaseEventType fields live in namespace 0; a same-named field in another
    // namespace is a different property and must not receive the BaseEventType overflow value
    if (field.getNamespaceIndex().intValue() != 0) {
      return null;
    }
    return field.getName();
  }
}
