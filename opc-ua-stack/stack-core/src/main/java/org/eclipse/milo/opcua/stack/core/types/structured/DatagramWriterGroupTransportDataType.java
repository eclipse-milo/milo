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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.3.3</a>
 */
public class DatagramWriterGroupTransportDataType extends WriterGroupTransportDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15532");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=21155");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=21179");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=21203");

  private final UByte messageRepeatCount;

  private final Double messageRepeatDelay;

  public DatagramWriterGroupTransportDataType(UByte messageRepeatCount, Double messageRepeatDelay) {
    this.messageRepeatCount = messageRepeatCount;
    this.messageRepeatDelay = messageRepeatDelay;
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

  public UByte getMessageRepeatCount() {
    return messageRepeatCount;
  }

  public Double getMessageRepeatDelay() {
    return messageRepeatDelay;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DatagramWriterGroupTransportDataType that = (DatagramWriterGroupTransportDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMessageRepeatCount(), that.getMessageRepeatCount());
    eqb.append(getMessageRepeatDelay(), that.getMessageRepeatDelay());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMessageRepeatCount());
    hcb.append(getMessageRepeatDelay());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", DatagramWriterGroupTransportDataType.class.getSimpleName() + "[", "]");
    joiner.add("messageRepeatCount=" + getMessageRepeatCount());
    joiner.add("messageRepeatDelay=" + getMessageRepeatDelay());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 21155),
        new NodeId(0, 15611),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MessageRepeatCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MessageRepeatDelay",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<DatagramWriterGroupTransportDataType> {
    @Override
    public Class<DatagramWriterGroupTransportDataType> getType() {
      return DatagramWriterGroupTransportDataType.class;
    }

    @Override
    public DatagramWriterGroupTransportDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      UByte messageRepeatCount = decoder.decodeByte("MessageRepeatCount");
      Double messageRepeatDelay = decoder.decodeDouble("MessageRepeatDelay");
      return new DatagramWriterGroupTransportDataType(messageRepeatCount, messageRepeatDelay);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DatagramWriterGroupTransportDataType value) {
      encoder.encodeByte("MessageRepeatCount", value.getMessageRepeatCount());
      encoder.encodeDouble("MessageRepeatDelay", value.getMessageRepeatDelay());
    }
  }
}
