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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.3.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.3.6</a>
 */
public class UadpDataSetWriterMessageDataType extends DataSetWriterMessageDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15652");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15717");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16015");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16391");

  private final UadpDataSetMessageContentMask dataSetMessageContentMask;

  private final UShort configuredSize;

  private final UShort networkMessageNumber;

  private final UShort dataSetOffset;

  public UadpDataSetWriterMessageDataType(
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      UShort configuredSize,
      UShort networkMessageNumber,
      UShort dataSetOffset) {
    this.dataSetMessageContentMask = dataSetMessageContentMask;
    this.configuredSize = configuredSize;
    this.networkMessageNumber = networkMessageNumber;
    this.dataSetOffset = dataSetOffset;
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

  public UadpDataSetMessageContentMask getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  public UShort getConfiguredSize() {
    return configuredSize;
  }

  public UShort getNetworkMessageNumber() {
    return networkMessageNumber;
  }

  public UShort getDataSetOffset() {
    return dataSetOffset;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UadpDataSetWriterMessageDataType that = (UadpDataSetWriterMessageDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDataSetMessageContentMask(), that.getDataSetMessageContentMask());
    eqb.append(getConfiguredSize(), that.getConfiguredSize());
    eqb.append(getNetworkMessageNumber(), that.getNetworkMessageNumber());
    eqb.append(getDataSetOffset(), that.getDataSetOffset());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDataSetMessageContentMask());
    hcb.append(getConfiguredSize());
    hcb.append(getNetworkMessageNumber());
    hcb.append(getDataSetOffset());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", UadpDataSetWriterMessageDataType.class.getSimpleName() + "[", "]");
    joiner.add("dataSetMessageContentMask=" + getDataSetMessageContentMask());
    joiner.add("configuredSize=" + getConfiguredSize());
    joiner.add("networkMessageNumber=" + getNetworkMessageNumber());
    joiner.add("dataSetOffset=" + getDataSetOffset());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15717),
        new NodeId(0, 15605),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataSetMessageContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15646),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConfiguredSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NetworkMessageNumber",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetOffset",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UadpDataSetWriterMessageDataType> {
    @Override
    public Class<UadpDataSetWriterMessageDataType> getType() {
      return UadpDataSetWriterMessageDataType.class;
    }

    @Override
    public UadpDataSetWriterMessageDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UadpDataSetMessageContentMask dataSetMessageContentMask;
      final UShort configuredSize;
      final UShort networkMessageNumber;
      final UShort dataSetOffset;
      dataSetMessageContentMask =
          new UadpDataSetMessageContentMask(decoder.decodeUInt32("DataSetMessageContentMask"));
      configuredSize = decoder.decodeUInt16("ConfiguredSize");
      networkMessageNumber = decoder.decodeUInt16("NetworkMessageNumber");
      dataSetOffset = decoder.decodeUInt16("DataSetOffset");
      return new UadpDataSetWriterMessageDataType(
          dataSetMessageContentMask, configuredSize, networkMessageNumber, dataSetOffset);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UadpDataSetWriterMessageDataType value) {
      encoder.encodeUInt32(
          "DataSetMessageContentMask", value.getDataSetMessageContentMask().getValue());
      encoder.encodeUInt16("ConfiguredSize", value.getConfiguredSize());
      encoder.encodeUInt16("NetworkMessageNumber", value.getNetworkMessageNumber());
      encoder.encodeUInt16("DataSetOffset", value.getDataSetOffset());
    }
  }
}
