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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.2.2</a>
 */
public class DatagramConnectionTransportDataType extends ConnectionTransportDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=17467");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=17468");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=17472");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=17476");

  private final NetworkAddressDataType discoveryAddress;

  public DatagramConnectionTransportDataType(NetworkAddressDataType discoveryAddress) {
    this.discoveryAddress = discoveryAddress;
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

  public NetworkAddressDataType getDiscoveryAddress() {
    return discoveryAddress;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DatagramConnectionTransportDataType that = (DatagramConnectionTransportDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDiscoveryAddress(), that.getDiscoveryAddress());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDiscoveryAddress());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", DatagramConnectionTransportDataType.class.getSimpleName() + "[", "]");
    joiner.add("discoveryAddress=" + getDiscoveryAddress());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 17468),
        new NodeId(0, 15618),
        StructureType.StructureWithSubtypedValues,
        new StructureField[] {
          new StructureField(
              "DiscoveryAddress",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15502),
              -1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<DatagramConnectionTransportDataType> {
    @Override
    public Class<DatagramConnectionTransportDataType> getType() {
      return DatagramConnectionTransportDataType.class;
    }

    @Override
    public DatagramConnectionTransportDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      final NetworkAddressDataType discoveryAddress;
      {
        ExtensionObject xo = decoder.decodeExtensionObject("DiscoveryAddress");
        discoveryAddress = (NetworkAddressDataType) xo.decode(context);
      }
      return new DatagramConnectionTransportDataType(discoveryAddress);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DatagramConnectionTransportDataType value) {
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getDiscoveryAddress());
        encoder.encodeExtensionObject("DiscoveryAddress", xo);
      }
    }
  }
}
