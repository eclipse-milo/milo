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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.3</a>
 */
public class ReadEventDetails2 extends ReadEventDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=32799");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=32800");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=32801");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=32802");

  private final Boolean readModified;

  public ReadEventDetails2(
      UInteger numValuesPerNode,
      DateTime startTime,
      DateTime endTime,
      EventFilter filter,
      Boolean readModified) {
    super(numValuesPerNode, startTime, endTime, filter);
    this.readModified = readModified;
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

  public Boolean getReadModified() {
    return readModified;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadEventDetails2 that = (ReadEventDetails2) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getReadModified(), that.getReadModified());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getReadModified());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadEventDetails2.class.getSimpleName() + "[", "]");
    joiner.add("readModified=" + getReadModified());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 32800),
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
              "ReadModified",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadEventDetails2> {
    @Override
    public Class<ReadEventDetails2> getType() {
      return ReadEventDetails2.class;
    }

    @Override
    public ReadEventDetails2 decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger numValuesPerNode;
      final DateTime startTime;
      final DateTime endTime;
      final EventFilter filter;
      final Boolean readModified;
      numValuesPerNode = decoder.decodeUInt32("NumValuesPerNode");
      startTime = decoder.decodeDateTime("StartTime");
      endTime = decoder.decodeDateTime("EndTime");
      filter = (EventFilter) decoder.decodeStruct("Filter", EventFilter.TYPE_ID);
      readModified = decoder.decodeBoolean("ReadModified");
      return new ReadEventDetails2(numValuesPerNode, startTime, endTime, filter, readModified);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReadEventDetails2 value) {
      encoder.encodeUInt32("NumValuesPerNode", value.getNumValuesPerNode());
      encoder.encodeDateTime("StartTime", value.getStartTime());
      encoder.encodeDateTime("EndTime", value.getEndTime());
      encoder.encodeStruct("Filter", value.getFilter(), EventFilter.TYPE_ID);
      encoder.encodeBoolean("ReadModified", value.getReadModified());
    }
  }
}
