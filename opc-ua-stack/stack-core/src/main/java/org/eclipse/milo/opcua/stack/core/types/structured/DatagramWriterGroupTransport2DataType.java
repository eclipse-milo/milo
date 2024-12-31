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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.3.9">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.3.9</a>
 */
public class DatagramWriterGroupTransport2DataType extends DatagramWriterGroupTransportDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=23613");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23865");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23933");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24001");

  private final NetworkAddressDataType address;

  private final @Nullable String qosCategory;

  private final TransmitQosDataType @Nullable [] datagramQos;

  private final UInteger discoveryAnnounceRate;

  private final @Nullable String topic;

  public DatagramWriterGroupTransport2DataType(
      UByte messageRepeatCount,
      Double messageRepeatDelay,
      NetworkAddressDataType address,
      @Nullable String qosCategory,
      TransmitQosDataType @Nullable [] datagramQos,
      UInteger discoveryAnnounceRate,
      @Nullable String topic) {
    super(messageRepeatCount, messageRepeatDelay);
    this.address = address;
    this.qosCategory = qosCategory;
    this.datagramQos = datagramQos;
    this.discoveryAnnounceRate = discoveryAnnounceRate;
    this.topic = topic;
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

  public NetworkAddressDataType getAddress() {
    return address;
  }

  public @Nullable String getQosCategory() {
    return qosCategory;
  }

  public TransmitQosDataType @Nullable [] getDatagramQos() {
    return datagramQos;
  }

  public UInteger getDiscoveryAnnounceRate() {
    return discoveryAnnounceRate;
  }

  public @Nullable String getTopic() {
    return topic;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DatagramWriterGroupTransport2DataType that = (DatagramWriterGroupTransport2DataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getAddress(), that.getAddress());
    eqb.append(getQosCategory(), that.getQosCategory());
    eqb.append(getDatagramQos(), that.getDatagramQos());
    eqb.append(getDiscoveryAnnounceRate(), that.getDiscoveryAnnounceRate());
    eqb.append(getTopic(), that.getTopic());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAddress());
    hcb.append(getQosCategory());
    hcb.append(getDatagramQos());
    hcb.append(getDiscoveryAnnounceRate());
    hcb.append(getTopic());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", DatagramWriterGroupTransport2DataType.class.getSimpleName() + "[", "]");
    joiner.add("address=" + getAddress());
    joiner.add("qosCategory='" + getQosCategory() + "'");
    joiner.add("datagramQos=" + java.util.Arrays.toString(getDatagramQos()));
    joiner.add("discoveryAnnounceRate=" + getDiscoveryAnnounceRate());
    joiner.add("topic='" + getTopic() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23865),
        new NodeId(0, 15532),
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
              false),
          new StructureField(
              "Address",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15502),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QosCategory",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DatagramQos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23604),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscoveryAnnounceRate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Topic",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<DatagramWriterGroupTransport2DataType> {
    @Override
    public Class<DatagramWriterGroupTransport2DataType> getType() {
      return DatagramWriterGroupTransport2DataType.class;
    }

    @Override
    public DatagramWriterGroupTransport2DataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      UByte messageRepeatCount = decoder.decodeByte("MessageRepeatCount");
      Double messageRepeatDelay = decoder.decodeDouble("MessageRepeatDelay");
      NetworkAddressDataType address =
          (NetworkAddressDataType) decoder.decodeStruct("Address", NetworkAddressDataType.TYPE_ID);
      String qosCategory = decoder.decodeString("QosCategory");
      TransmitQosDataType[] datagramQos =
          (TransmitQosDataType[])
              decoder.decodeStructArray("DatagramQos", TransmitQosDataType.TYPE_ID);
      UInteger discoveryAnnounceRate = decoder.decodeUInt32("DiscoveryAnnounceRate");
      String topic = decoder.decodeString("Topic");
      return new DatagramWriterGroupTransport2DataType(
          messageRepeatCount,
          messageRepeatDelay,
          address,
          qosCategory,
          datagramQos,
          discoveryAnnounceRate,
          topic);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DatagramWriterGroupTransport2DataType value) {
      encoder.encodeByte("MessageRepeatCount", value.getMessageRepeatCount());
      encoder.encodeDouble("MessageRepeatDelay", value.getMessageRepeatDelay());
      encoder.encodeStruct("Address", value.getAddress(), NetworkAddressDataType.TYPE_ID);
      encoder.encodeString("QosCategory", value.getQosCategory());
      encoder.encodeStructArray("DatagramQos", value.getDatagramQos(), TransmitQosDataType.TYPE_ID);
      encoder.encodeUInt32("DiscoveryAnnounceRate", value.getDiscoveryAnnounceRate());
      encoder.encodeString("Topic", value.getTopic());
    }
  }
}
