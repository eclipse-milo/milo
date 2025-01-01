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
 *     href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.3/#6.4.3.1">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.3/#6.4.3.1</a>
 */
public class ReadRawModifiedDetails extends HistoryReadDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=647");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=649");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=648");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15263");

  private final Boolean isReadModified;

  private final DateTime startTime;

  private final DateTime endTime;

  private final UInteger numValuesPerNode;

  private final Boolean returnBounds;

  public ReadRawModifiedDetails(
      Boolean isReadModified,
      DateTime startTime,
      DateTime endTime,
      UInteger numValuesPerNode,
      Boolean returnBounds) {
    this.isReadModified = isReadModified;
    this.startTime = startTime;
    this.endTime = endTime;
    this.numValuesPerNode = numValuesPerNode;
    this.returnBounds = returnBounds;
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

  public Boolean getIsReadModified() {
    return isReadModified;
  }

  public DateTime getStartTime() {
    return startTime;
  }

  public DateTime getEndTime() {
    return endTime;
  }

  public UInteger getNumValuesPerNode() {
    return numValuesPerNode;
  }

  public Boolean getReturnBounds() {
    return returnBounds;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadRawModifiedDetails that = (ReadRawModifiedDetails) object;
    var eqb = new EqualsBuilder();
    eqb.append(getIsReadModified(), that.getIsReadModified());
    eqb.append(getStartTime(), that.getStartTime());
    eqb.append(getEndTime(), that.getEndTime());
    eqb.append(getNumValuesPerNode(), that.getNumValuesPerNode());
    eqb.append(getReturnBounds(), that.getReturnBounds());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getIsReadModified());
    hcb.append(getStartTime());
    hcb.append(getEndTime());
    hcb.append(getNumValuesPerNode());
    hcb.append(getReturnBounds());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadRawModifiedDetails.class.getSimpleName() + "[", "]");
    joiner.add("isReadModified=" + getIsReadModified());
    joiner.add("startTime=" + getStartTime());
    joiner.add("endTime=" + getEndTime());
    joiner.add("numValuesPerNode=" + getNumValuesPerNode());
    joiner.add("returnBounds=" + getReturnBounds());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 649),
        new NodeId(0, 641),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "IsReadModified",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
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
              "NumValuesPerNode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReturnBounds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadRawModifiedDetails> {
    @Override
    public Class<ReadRawModifiedDetails> getType() {
      return ReadRawModifiedDetails.class;
    }

    @Override
    public ReadRawModifiedDetails decodeType(EncodingContext context, UaDecoder decoder) {
      Boolean isReadModified = decoder.decodeBoolean("IsReadModified");
      DateTime startTime = decoder.decodeDateTime("StartTime");
      DateTime endTime = decoder.decodeDateTime("EndTime");
      UInteger numValuesPerNode = decoder.decodeUInt32("NumValuesPerNode");
      Boolean returnBounds = decoder.decodeBoolean("ReturnBounds");
      return new ReadRawModifiedDetails(
          isReadModified, startTime, endTime, numValuesPerNode, returnBounds);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ReadRawModifiedDetails value) {
      encoder.encodeBoolean("IsReadModified", value.getIsReadModified());
      encoder.encodeDateTime("StartTime", value.getStartTime());
      encoder.encodeDateTime("EndTime", value.getEndTime());
      encoder.encodeUInt32("NumValuesPerNode", value.getNumValuesPerNode());
      encoder.encodeBoolean("ReturnBounds", value.getReturnBounds());
    }
  }
}
