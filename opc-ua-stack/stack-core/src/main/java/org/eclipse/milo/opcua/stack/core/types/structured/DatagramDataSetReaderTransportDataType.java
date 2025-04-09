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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.6.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.6.5</a>
 */
public class DatagramDataSetReaderTransportDataType extends DataSetReaderTransportDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23614");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23866");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23934");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24002");

  private final NetworkAddressDataType address;

  private final @Nullable String qosCategory;

  private final ReceiveQosDataType @Nullable [] datagramQos;

  private final @Nullable String topic;

  public DatagramDataSetReaderTransportDataType(
      NetworkAddressDataType address,
      @Nullable String qosCategory,
      ReceiveQosDataType @Nullable [] datagramQos,
      @Nullable String topic) {
    this.address = address;
    this.qosCategory = qosCategory;
    this.datagramQos = datagramQos;
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

  public ReceiveQosDataType @Nullable [] getDatagramQos() {
    return datagramQos;
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
    DatagramDataSetReaderTransportDataType that = (DatagramDataSetReaderTransportDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAddress(), that.getAddress());
    eqb.append(getQosCategory(), that.getQosCategory());
    eqb.append(getDatagramQos(), that.getDatagramQos());
    eqb.append(getTopic(), that.getTopic());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAddress());
    hcb.append(getQosCategory());
    hcb.append(getDatagramQos());
    hcb.append(getTopic());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", DatagramDataSetReaderTransportDataType.class.getSimpleName() + "[", "]");
    joiner.add("address=" + getAddress());
    joiner.add("qosCategory='" + getQosCategory() + "'");
    joiner.add("datagramQos=" + java.util.Arrays.toString(getDatagramQos()));
    joiner.add("topic='" + getTopic() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23866),
        new NodeId(0, 15628),
        StructureType.StructureWithSubtypedValues,
        new StructureField[] {
          new StructureField(
              "Address",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15502),
              -1,
              null,
              UInteger.valueOf(0),
              true),
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
              new NodeId(0, 23608),
              1,
              null,
              UInteger.valueOf(0),
              true),
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
      extends GenericDataTypeCodec<DatagramDataSetReaderTransportDataType> {
    @Override
    public Class<DatagramDataSetReaderTransportDataType> getType() {
      return DatagramDataSetReaderTransportDataType.class;
    }

    @Override
    public DatagramDataSetReaderTransportDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      final NetworkAddressDataType address;
      final String qosCategory;
      final ReceiveQosDataType[] datagramQos;
      final String topic;
      {
        ExtensionObject xo = decoder.decodeExtensionObject("Address");
        address = (NetworkAddressDataType) xo.decode(context);
      }
      qosCategory = decoder.decodeString("QosCategory");
      {
        ExtensionObject[] xos = decoder.decodeExtensionObjectArray("DatagramQos");
        if (xos != null) {
          datagramQos = new ReceiveQosDataType[xos.length];
          for (int i = 0; i < xos.length; i++) {
            datagramQos[i] = (ReceiveQosDataType) xos[i].decode(context);
          }
        } else {
          datagramQos = null;
        }
      }
      topic = decoder.decodeString("Topic");
      return new DatagramDataSetReaderTransportDataType(address, qosCategory, datagramQos, topic);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DatagramDataSetReaderTransportDataType value) {
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getAddress());
        encoder.encodeExtensionObject("Address", xo);
      }
      encoder.encodeString("QosCategory", value.getQosCategory());
      {
        ExtensionObject[] xos = null;
        ReceiveQosDataType[] datagramQos = value.getDatagramQos();
        if (datagramQos != null) {
          xos = new ExtensionObject[datagramQos.length];
          for (int i = 0; i < xos.length; i++) {
            xos[i] = ExtensionObject.encode(context, datagramQos[i]);
          }
        }
        encoder.encodeExtensionObjectArray("DatagramQos", xos);
      }
      encoder.encodeString("Topic", value.getTopic());
    }
  }
}
