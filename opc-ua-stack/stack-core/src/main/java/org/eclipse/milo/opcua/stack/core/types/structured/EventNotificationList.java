/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3</a>
 */
public class EventNotificationList extends NotificationData implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=914");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=916");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=915");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15347");

  private final EventFieldList @Nullable [] events;

  public EventNotificationList(EventFieldList @Nullable [] events) {
    this.events = events;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public EventFieldList @Nullable [] getEvents() {
    return events;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EventNotificationList that = (EventNotificationList) object;
    var eqb = new EqualsBuilder();
    eqb.append(getEvents(), that.getEvents());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEvents());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EventNotificationList.class.getSimpleName() + "[", "]");
    joiner.add("events=" + java.util.Arrays.toString(getEvents()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 916),
        new NodeId(0, 945),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Events",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 917),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EventNotificationList> {
    @Override
    public Class<EventNotificationList> getType() {
      return EventNotificationList.class;
    }

    @Override
    public EventNotificationList decodeType(EncodingContext context, UaDecoder decoder) {
      final EventFieldList[] events;
      events = (EventFieldList[]) decoder.decodeStructArray("Events", EventFieldList.TYPE_ID);
      return new EventNotificationList(events);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, EventNotificationList value) {
      encoder.encodeStructArray("Events", value.getEvents(), EventFieldList.TYPE_ID);
    }
  }
}
