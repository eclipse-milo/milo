/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.8.4/#6.8.4.1">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.8.4/#6.8.4.1</a>
 */
public class UpdateEventDetails extends HistoryUpdateDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=683");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=685");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=684");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15282");

  private final PerformUpdateType performInsertReplace;

  private final EventFilter filter;

  private final HistoryEventFieldList @Nullable [] eventData;

  public UpdateEventDetails(
      NodeId nodeId,
      PerformUpdateType performInsertReplace,
      EventFilter filter,
      HistoryEventFieldList @Nullable [] eventData) {
    super(nodeId);
    this.performInsertReplace = performInsertReplace;
    this.filter = filter;
    this.eventData = eventData;
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

  public PerformUpdateType getPerformInsertReplace() {
    return performInsertReplace;
  }

  public EventFilter getFilter() {
    return filter;
  }

  public HistoryEventFieldList @Nullable [] getEventData() {
    return eventData;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UpdateEventDetails that = (UpdateEventDetails) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getPerformInsertReplace(), that.getPerformInsertReplace());
    eqb.append(getFilter(), that.getFilter());
    eqb.append(getEventData(), that.getEventData());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPerformInsertReplace());
    hcb.append(getFilter());
    hcb.append(getEventData());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UpdateEventDetails.class.getSimpleName() + "[", "]");
    joiner.add("performInsertReplace=" + getPerformInsertReplace());
    joiner.add("filter=" + getFilter());
    joiner.add("eventData=" + java.util.Arrays.toString(getEventData()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 685),
        new NodeId(0, 677),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PerformInsertReplace",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11293),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Filter",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 725),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 920),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UpdateEventDetails> {
    @Override
    public Class<UpdateEventDetails> getType() {
      return UpdateEventDetails.class;
    }

    @Override
    public UpdateEventDetails decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId nodeId = decoder.decodeNodeId("NodeId");
      PerformUpdateType performInsertReplace =
          PerformUpdateType.from(decoder.decodeEnum("PerformInsertReplace"));
      EventFilter filter = (EventFilter) decoder.decodeStruct("Filter", EventFilter.TYPE_ID);
      HistoryEventFieldList[] eventData =
          (HistoryEventFieldList[])
              decoder.decodeStructArray("EventData", HistoryEventFieldList.TYPE_ID);
      return new UpdateEventDetails(nodeId, performInsertReplace, filter, eventData);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, UpdateEventDetails value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeEnum("PerformInsertReplace", value.getPerformInsertReplace());
      encoder.encodeStruct("Filter", value.getFilter(), EventFilter.TYPE_ID);
      encoder.encodeStructArray("EventData", value.getEventData(), HistoryEventFieldList.TYPE_ID);
    }
  }
}
