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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.1</a>
 */
public class ReadEventDetails extends HistoryReadDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=644");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=646");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=645");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15262");

  private final UInteger numValuesPerNode;

  private final DateTime startTime;

  private final DateTime endTime;

  private final EventFilter filter;

  public ReadEventDetails(
      UInteger numValuesPerNode, DateTime startTime, DateTime endTime, EventFilter filter) {
    this.numValuesPerNode = numValuesPerNode;
    this.startTime = startTime;
    this.endTime = endTime;
    this.filter = filter;
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

  public UInteger getNumValuesPerNode() {
    return numValuesPerNode;
  }

  public DateTime getStartTime() {
    return startTime;
  }

  public DateTime getEndTime() {
    return endTime;
  }

  public EventFilter getFilter() {
    return filter;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadEventDetails that = (ReadEventDetails) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNumValuesPerNode(), that.getNumValuesPerNode());
    eqb.append(getStartTime(), that.getStartTime());
    eqb.append(getEndTime(), that.getEndTime());
    eqb.append(getFilter(), that.getFilter());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNumValuesPerNode());
    hcb.append(getStartTime());
    hcb.append(getEndTime());
    hcb.append(getFilter());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadEventDetails.class.getSimpleName() + "[", "]");
    joiner.add("numValuesPerNode=" + getNumValuesPerNode());
    joiner.add("startTime=" + getStartTime());
    joiner.add("endTime=" + getEndTime());
    joiner.add("filter=" + getFilter());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 646),
        new NodeId(0, 641),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NumValuesPerNode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StartTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EndTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadEventDetails> {
    @Override
    public Class<ReadEventDetails> getType() {
      return ReadEventDetails.class;
    }

    @Override
    public ReadEventDetails decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger numValuesPerNode;
      final DateTime startTime;
      final DateTime endTime;
      final EventFilter filter;
      numValuesPerNode = decoder.decodeUInt32("NumValuesPerNode");
      startTime = decoder.decodeDateTime("StartTime");
      endTime = decoder.decodeDateTime("EndTime");
      filter = (EventFilter) decoder.decodeStruct("Filter", EventFilter.TYPE_ID);
      return new ReadEventDetails(numValuesPerNode, startTime, endTime, filter);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReadEventDetails value) {
      encoder.encodeUInt32("NumValuesPerNode", value.getNumValuesPerNode());
      encoder.encodeDateTime("StartTime", value.getStartTime());
      encoder.encodeDateTime("EndTime", value.getEndTime());
      encoder.encodeStruct("Filter", value.getFilter(), EventFilter.TYPE_ID);
    }
  }
}
