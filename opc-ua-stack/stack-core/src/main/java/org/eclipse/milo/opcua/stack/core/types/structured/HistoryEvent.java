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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.4">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.4</a>
 */
public class HistoryEvent extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=659");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=661");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=660");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15273");

  private final HistoryEventFieldList @Nullable [] events;

  public HistoryEvent(HistoryEventFieldList @Nullable [] events) {
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

  public HistoryEventFieldList @Nullable [] getEvents() {
    return events;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryEvent that = (HistoryEvent) object;
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
    var joiner = new StringJoiner(", ", HistoryEvent.class.getSimpleName() + "[", "]");
    joiner.add("events=" + java.util.Arrays.toString(getEvents()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 661),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Events",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 920),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryEvent> {
    @Override
    public Class<HistoryEvent> getType() {
      return HistoryEvent.class;
    }

    @Override
    public HistoryEvent decodeType(EncodingContext context, UaDecoder decoder) {
      final HistoryEventFieldList[] events;
      events =
          (HistoryEventFieldList[])
              decoder.decodeStructArray("Events", HistoryEventFieldList.TYPE_ID);
      return new HistoryEvent(events);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryEvent value) {
      encoder.encodeStructArray("Events", value.getEvents(), HistoryEventFieldList.TYPE_ID);
    }
  }
}
