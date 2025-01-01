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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.1.7">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.1/#6.3.1.1.7</a>
 */
public class UadpWriterGroupMessageDataType extends WriterGroupMessageDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15645");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15715");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16014");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16323");

  private final UInteger groupVersion;

  private final DataSetOrderingType dataSetOrdering;

  private final UadpNetworkMessageContentMask networkMessageContentMask;

  private final Double samplingOffset;

  private final Double @Nullable [] publishingOffset;

  public UadpWriterGroupMessageDataType(
      UInteger groupVersion,
      DataSetOrderingType dataSetOrdering,
      UadpNetworkMessageContentMask networkMessageContentMask,
      Double samplingOffset,
      Double @Nullable [] publishingOffset) {
    this.groupVersion = groupVersion;
    this.dataSetOrdering = dataSetOrdering;
    this.networkMessageContentMask = networkMessageContentMask;
    this.samplingOffset = samplingOffset;
    this.publishingOffset = publishingOffset;
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

  public DataSetOrderingType getDataSetOrdering() {
    return dataSetOrdering;
  }

  public UadpNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  public Double getSamplingOffset() {
    return samplingOffset;
  }

  public Double @Nullable [] getPublishingOffset() {
    return publishingOffset;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UadpWriterGroupMessageDataType that = (UadpWriterGroupMessageDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getGroupVersion(), that.getGroupVersion());
    eqb.append(getDataSetOrdering(), that.getDataSetOrdering());
    eqb.append(getNetworkMessageContentMask(), that.getNetworkMessageContentMask());
    eqb.append(getSamplingOffset(), that.getSamplingOffset());
    eqb.append(getPublishingOffset(), that.getPublishingOffset());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getGroupVersion());
    hcb.append(getDataSetOrdering());
    hcb.append(getNetworkMessageContentMask());
    hcb.append(getSamplingOffset());
    hcb.append(getPublishingOffset());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", UadpWriterGroupMessageDataType.class.getSimpleName() + "[", "]");
    joiner.add("groupVersion=" + getGroupVersion());
    joiner.add("dataSetOrdering=" + getDataSetOrdering());
    joiner.add("networkMessageContentMask=" + getNetworkMessageContentMask());
    joiner.add("samplingOffset=" + getSamplingOffset());
    joiner.add("publishingOffset=" + java.util.Arrays.toString(getPublishingOffset()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15715),
        new NodeId(0, 15616),
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
              "DataSetOrdering",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20408),
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
              "SamplingOffset",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishingOffset",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UadpWriterGroupMessageDataType> {
    @Override
    public Class<UadpWriterGroupMessageDataType> getType() {
      return UadpWriterGroupMessageDataType.class;
    }

    @Override
    public UadpWriterGroupMessageDataType decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger groupVersion = decoder.decodeUInt32("GroupVersion");
      DataSetOrderingType dataSetOrdering =
          DataSetOrderingType.from(decoder.decodeEnum("DataSetOrdering"));
      UadpNetworkMessageContentMask networkMessageContentMask =
          new UadpNetworkMessageContentMask(decoder.decodeUInt32("NetworkMessageContentMask"));
      Double samplingOffset = decoder.decodeDouble("SamplingOffset");
      Double[] publishingOffset = decoder.decodeDoubleArray("PublishingOffset");
      return new UadpWriterGroupMessageDataType(
          groupVersion,
          dataSetOrdering,
          networkMessageContentMask,
          samplingOffset,
          publishingOffset);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UadpWriterGroupMessageDataType value) {
      encoder.encodeUInt32("GroupVersion", value.getGroupVersion());
      encoder.encodeEnum("DataSetOrdering", value.getDataSetOrdering());
      encoder.encodeUInt32(
          "NetworkMessageContentMask", value.getNetworkMessageContentMask().getValue());
      encoder.encodeDouble("SamplingOffset", value.getSamplingOffset());
      encoder.encodeDoubleArray("PublishingOffset", value.getPublishingOffset());
    }
  }
}
