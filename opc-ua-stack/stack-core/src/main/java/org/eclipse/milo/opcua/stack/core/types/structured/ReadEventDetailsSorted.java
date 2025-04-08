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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.5">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.5</a>
 */
public class ReadEventDetailsSorted extends ReadEventDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18649");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=18651");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=18653");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=18655");

  private final SortRuleElement @Nullable [] sortClause;

  public ReadEventDetailsSorted(
      UInteger numValuesPerNode,
      DateTime startTime,
      DateTime endTime,
      EventFilter filter,
      SortRuleElement @Nullable [] sortClause) {
    super(numValuesPerNode, startTime, endTime, filter);
    this.sortClause = sortClause;
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

  public SortRuleElement @Nullable [] getSortClause() {
    return sortClause;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadEventDetailsSorted that = (ReadEventDetailsSorted) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getSortClause(), that.getSortClause());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSortClause());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadEventDetailsSorted.class.getSimpleName() + "[", "]");
    joiner.add("sortClause=" + java.util.Arrays.toString(getSortClause()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18651),
        new NodeId(0, 644),
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
              false),
          new StructureField(
              "SortClause",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18648),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadEventDetailsSorted> {
    @Override
    public Class<ReadEventDetailsSorted> getType() {
      return ReadEventDetailsSorted.class;
    }

    @Override
    public ReadEventDetailsSorted decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger numValuesPerNode;
      final DateTime startTime;
      final DateTime endTime;
      final EventFilter filter;
      final SortRuleElement[] sortClause;
      numValuesPerNode = decoder.decodeUInt32("NumValuesPerNode");
      startTime = decoder.decodeDateTime("StartTime");
      endTime = decoder.decodeDateTime("EndTime");
      filter = (EventFilter) decoder.decodeStruct("Filter", EventFilter.TYPE_ID);
      sortClause =
          (SortRuleElement[]) decoder.decodeStructArray("SortClause", SortRuleElement.TYPE_ID);
      return new ReadEventDetailsSorted(numValuesPerNode, startTime, endTime, filter, sortClause);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ReadEventDetailsSorted value) {
      encoder.encodeUInt32("NumValuesPerNode", value.getNumValuesPerNode());
      encoder.encodeDateTime("StartTime", value.getStartTime());
      encoder.encodeDateTime("EndTime", value.getEndTime());
      encoder.encodeStruct("Filter", value.getFilter(), EventFilter.TYPE_ID);
      encoder.encodeStructArray("SortClause", value.getSortClause(), SortRuleElement.TYPE_ID);
    }
  }
}
