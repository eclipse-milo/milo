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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.2.7">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.2.7</a>
 */
public class DatagramConnectionTransport2DataType extends DatagramConnectionTransportDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23612");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23864");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23932");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24000");

  private final UInteger discoveryAnnounceRate;

  private final UInteger discoveryMaxMessageSize;

  private final @Nullable String qosCategory;

  private final QosDataType @Nullable [] datagramQos;

  public DatagramConnectionTransport2DataType(
      NetworkAddressDataType discoveryAddress,
      UInteger discoveryAnnounceRate,
      UInteger discoveryMaxMessageSize,
      @Nullable String qosCategory,
      QosDataType @Nullable [] datagramQos) {
    super(discoveryAddress);
    this.discoveryAnnounceRate = discoveryAnnounceRate;
    this.discoveryMaxMessageSize = discoveryMaxMessageSize;
    this.qosCategory = qosCategory;
    this.datagramQos = datagramQos;
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

  public UInteger getDiscoveryAnnounceRate() {
    return discoveryAnnounceRate;
  }

  public UInteger getDiscoveryMaxMessageSize() {
    return discoveryMaxMessageSize;
  }

  public @Nullable String getQosCategory() {
    return qosCategory;
  }

  public QosDataType @Nullable [] getDatagramQos() {
    return datagramQos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DatagramConnectionTransport2DataType that = (DatagramConnectionTransport2DataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getDiscoveryAnnounceRate(), that.getDiscoveryAnnounceRate());
    eqb.append(getDiscoveryMaxMessageSize(), that.getDiscoveryMaxMessageSize());
    eqb.append(getQosCategory(), that.getQosCategory());
    eqb.append(getDatagramQos(), that.getDatagramQos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDiscoveryAnnounceRate());
    hcb.append(getDiscoveryMaxMessageSize());
    hcb.append(getQosCategory());
    hcb.append(getDatagramQos());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", DatagramConnectionTransport2DataType.class.getSimpleName() + "[", "]");
    joiner.add("discoveryAnnounceRate=" + getDiscoveryAnnounceRate());
    joiner.add("discoveryMaxMessageSize=" + getDiscoveryMaxMessageSize());
    joiner.add("qosCategory='" + getQosCategory() + "'");
    joiner.add("datagramQos=" + java.util.Arrays.toString(getDatagramQos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23864),
        new NodeId(0, 17467),
        StructureType.StructureWithSubtypedValues,
        new StructureField[] {
          new StructureField(
              "DiscoveryAddress",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15502),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "DiscoveryAnnounceRate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscoveryMaxMessageSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
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
              new NodeId(0, 23603),
              1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<DatagramConnectionTransport2DataType> {
    @Override
    public Class<DatagramConnectionTransport2DataType> getType() {
      return DatagramConnectionTransport2DataType.class;
    }

    @Override
    public DatagramConnectionTransport2DataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      final NetworkAddressDataType discoveryAddress;
      final UInteger discoveryAnnounceRate;
      final UInteger discoveryMaxMessageSize;
      final String qosCategory;
      final QosDataType[] datagramQos;
      {
        ExtensionObject xo = decoder.decodeExtensionObject("DiscoveryAddress");
        discoveryAddress = (NetworkAddressDataType) xo.decode(context);
      }
      discoveryAnnounceRate = decoder.decodeUInt32("DiscoveryAnnounceRate");
      discoveryMaxMessageSize = decoder.decodeUInt32("DiscoveryMaxMessageSize");
      qosCategory = decoder.decodeString("QosCategory");
      {
        ExtensionObject[] xos = decoder.decodeExtensionObjectArray("DatagramQos");
        if (xos != null) {
          datagramQos = new QosDataType[xos.length];
          for (int i = 0; i < xos.length; i++) {
            datagramQos[i] = (QosDataType) xos[i].decode(context);
          }
        } else {
          datagramQos = null;
        }
      }
      return new DatagramConnectionTransport2DataType(
          discoveryAddress,
          discoveryAnnounceRate,
          discoveryMaxMessageSize,
          qosCategory,
          datagramQos);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DatagramConnectionTransport2DataType value) {
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getDiscoveryAddress());
        encoder.encodeExtensionObject("DiscoveryAddress", xo);
      }
      encoder.encodeUInt32("DiscoveryAnnounceRate", value.getDiscoveryAnnounceRate());
      encoder.encodeUInt32("DiscoveryMaxMessageSize", value.getDiscoveryMaxMessageSize());
      encoder.encodeString("QosCategory", value.getQosCategory());
      {
        ExtensionObject[] xos = null;
        QosDataType[] datagramQos = value.getDatagramQos();
        if (datagramQos != null) {
          xos = new ExtensionObject[datagramQos.length];
          for (int i = 0; i < xos.length; i++) {
            xos[i] = ExtensionObject.encode(context, datagramQos[i]);
          }
        }
        encoder.encodeExtensionObjectArray("DatagramQos", xos);
      }
    }
  }
}
