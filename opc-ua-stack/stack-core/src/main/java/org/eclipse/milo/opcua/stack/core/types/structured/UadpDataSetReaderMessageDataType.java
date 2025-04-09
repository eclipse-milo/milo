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
import java.util.UUID;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.4.10">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.4.10</a>
 */
public class UadpDataSetReaderMessageDataType extends DataSetReaderMessageDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15653");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15718");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16016");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16392");

  private final UInteger groupVersion;

  private final UShort networkMessageNumber;

  private final UShort dataSetOffset;

  private final UUID dataSetClassId;

  private final UadpNetworkMessageContentMask networkMessageContentMask;

  private final UadpDataSetMessageContentMask dataSetMessageContentMask;

  private final Double publishingInterval;

  private final Double receiveOffset;

  private final Double processingOffset;

  public UadpDataSetReaderMessageDataType(
      UInteger groupVersion,
      UShort networkMessageNumber,
      UShort dataSetOffset,
      UUID dataSetClassId,
      UadpNetworkMessageContentMask networkMessageContentMask,
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      Double publishingInterval,
      Double receiveOffset,
      Double processingOffset) {
    this.groupVersion = groupVersion;
    this.networkMessageNumber = networkMessageNumber;
    this.dataSetOffset = dataSetOffset;
    this.dataSetClassId = dataSetClassId;
    this.networkMessageContentMask = networkMessageContentMask;
    this.dataSetMessageContentMask = dataSetMessageContentMask;
    this.publishingInterval = publishingInterval;
    this.receiveOffset = receiveOffset;
    this.processingOffset = processingOffset;
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

  public UInteger getGroupVersion() {
    return groupVersion;
  }

  public UShort getNetworkMessageNumber() {
    return networkMessageNumber;
  }

  public UShort getDataSetOffset() {
    return dataSetOffset;
  }

  public UUID getDataSetClassId() {
    return dataSetClassId;
  }

  public UadpNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  public UadpDataSetMessageContentMask getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  public Double getPublishingInterval() {
    return publishingInterval;
  }

  public Double getReceiveOffset() {
    return receiveOffset;
  }

  public Double getProcessingOffset() {
    return processingOffset;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UadpDataSetReaderMessageDataType that = (UadpDataSetReaderMessageDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getGroupVersion(), that.getGroupVersion());
    eqb.append(getNetworkMessageNumber(), that.getNetworkMessageNumber());
    eqb.append(getDataSetOffset(), that.getDataSetOffset());
    eqb.append(getDataSetClassId(), that.getDataSetClassId());
    eqb.append(getNetworkMessageContentMask(), that.getNetworkMessageContentMask());
    eqb.append(getDataSetMessageContentMask(), that.getDataSetMessageContentMask());
    eqb.append(getPublishingInterval(), that.getPublishingInterval());
    eqb.append(getReceiveOffset(), that.getReceiveOffset());
    eqb.append(getProcessingOffset(), that.getProcessingOffset());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getGroupVersion());
    hcb.append(getNetworkMessageNumber());
    hcb.append(getDataSetOffset());
    hcb.append(getDataSetClassId());
    hcb.append(getNetworkMessageContentMask());
    hcb.append(getDataSetMessageContentMask());
    hcb.append(getPublishingInterval());
    hcb.append(getReceiveOffset());
    hcb.append(getProcessingOffset());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", UadpDataSetReaderMessageDataType.class.getSimpleName() + "[", "]");
    joiner.add("groupVersion=" + getGroupVersion());
    joiner.add("networkMessageNumber=" + getNetworkMessageNumber());
    joiner.add("dataSetOffset=" + getDataSetOffset());
    joiner.add("dataSetClassId=" + getDataSetClassId());
    joiner.add("networkMessageContentMask=" + getNetworkMessageContentMask());
    joiner.add("dataSetMessageContentMask=" + getDataSetMessageContentMask());
    joiner.add("publishingInterval=" + getPublishingInterval());
    joiner.add("receiveOffset=" + getReceiveOffset());
    joiner.add("processingOffset=" + getProcessingOffset());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15718),
        new NodeId(0, 15629),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "GroupVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
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
              false),
          new StructureField(
              "DataSetClassId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NetworkMessageContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15642),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetMessageContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15646),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReceiveOffset",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ProcessingOffset",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UadpDataSetReaderMessageDataType> {
    @Override
    public Class<UadpDataSetReaderMessageDataType> getType() {
      return UadpDataSetReaderMessageDataType.class;
    }

    @Override
    public UadpDataSetReaderMessageDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger groupVersion;
      final UShort networkMessageNumber;
      final UShort dataSetOffset;
      final UUID dataSetClassId;
      final UadpNetworkMessageContentMask networkMessageContentMask;
      final UadpDataSetMessageContentMask dataSetMessageContentMask;
      final Double publishingInterval;
      final Double receiveOffset;
      final Double processingOffset;
      groupVersion = decoder.decodeUInt32("GroupVersion");
      networkMessageNumber = decoder.decodeUInt16("NetworkMessageNumber");
      dataSetOffset = decoder.decodeUInt16("DataSetOffset");
      dataSetClassId = decoder.decodeGuid("DataSetClassId");
      networkMessageContentMask =
          new UadpNetworkMessageContentMask(decoder.decodeUInt32("NetworkMessageContentMask"));
      dataSetMessageContentMask =
          new UadpDataSetMessageContentMask(decoder.decodeUInt32("DataSetMessageContentMask"));
      publishingInterval = decoder.decodeDouble("PublishingInterval");
      receiveOffset = decoder.decodeDouble("ReceiveOffset");
      processingOffset = decoder.decodeDouble("ProcessingOffset");
      return new UadpDataSetReaderMessageDataType(
          groupVersion,
          networkMessageNumber,
          dataSetOffset,
          dataSetClassId,
          networkMessageContentMask,
          dataSetMessageContentMask,
          publishingInterval,
          receiveOffset,
          processingOffset);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UadpDataSetReaderMessageDataType value) {
      encoder.encodeUInt32("GroupVersion", value.getGroupVersion());
      encoder.encodeUInt16("NetworkMessageNumber", value.getNetworkMessageNumber());
      encoder.encodeUInt16("DataSetOffset", value.getDataSetOffset());
      encoder.encodeGuid("DataSetClassId", value.getDataSetClassId());
      encoder.encodeUInt32(
          "NetworkMessageContentMask", value.getNetworkMessageContentMask().getValue());
      encoder.encodeUInt32(
          "DataSetMessageContentMask", value.getDataSetMessageContentMask().getValue());
      encoder.encodeDouble("PublishingInterval", value.getPublishingInterval());
      encoder.encodeDouble("ReceiveOffset", value.getReceiveOffset());
      encoder.encodeDouble("ProcessingOffset", value.getProcessingOffset());
    }
  }
}
