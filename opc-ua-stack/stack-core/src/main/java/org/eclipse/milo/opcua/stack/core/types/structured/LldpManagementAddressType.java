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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ManAddrIfSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.3</a>
 */
public class LldpManagementAddressType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=18954");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=19080");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=19101");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=19300");

  private final UInteger addressSubtype;

  private final @Nullable String address;

  private final ManAddrIfSubtype ifSubtype;

  private final UInteger ifId;

  public LldpManagementAddressType(
      UInteger addressSubtype,
      @Nullable String address,
      ManAddrIfSubtype ifSubtype,
      UInteger ifId) {
    this.addressSubtype = addressSubtype;
    this.address = address;
    this.ifSubtype = ifSubtype;
    this.ifId = ifId;
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

  public UInteger getAddressSubtype() {
    return addressSubtype;
  }

  public @Nullable String getAddress() {
    return address;
  }

  public ManAddrIfSubtype getIfSubtype() {
    return ifSubtype;
  }

  public UInteger getIfId() {
    return ifId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    LldpManagementAddressType that = (LldpManagementAddressType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAddressSubtype(), that.getAddressSubtype());
    eqb.append(getAddress(), that.getAddress());
    eqb.append(getIfSubtype(), that.getIfSubtype());
    eqb.append(getIfId(), that.getIfId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAddressSubtype());
    hcb.append(getAddress());
    hcb.append(getIfSubtype());
    hcb.append(getIfId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LldpManagementAddressType.class.getSimpleName() + "[", "]");
    joiner.add("addressSubtype=" + getAddressSubtype());
    joiner.add("address='" + getAddress() + "'");
    joiner.add("ifSubtype=" + getIfSubtype());
    joiner.add("ifId=" + getIfId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 19080),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "AddressSubtype",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Address",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IfSubtype",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18951),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IfId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<LldpManagementAddressType> {
    @Override
    public Class<LldpManagementAddressType> getType() {
      return LldpManagementAddressType.class;
    }

    @Override
    public LldpManagementAddressType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger addressSubtype;
      final String address;
      final ManAddrIfSubtype ifSubtype;
      final UInteger ifId;
      addressSubtype = decoder.decodeUInt32("AddressSubtype");
      address = decoder.decodeString("Address");
      ifSubtype = ManAddrIfSubtype.from(decoder.decodeEnum("IfSubtype"));
      ifId = decoder.decodeUInt32("IfId");
      return new LldpManagementAddressType(addressSubtype, address, ifSubtype, ifId);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, LldpManagementAddressType value) {
      encoder.encodeUInt32("AddressSubtype", value.getAddressSubtype());
      encoder.encodeString("Address", value.getAddress());
      encoder.encodeEnum("IfSubtype", value.getIfSubtype());
      encoder.encodeUInt32("IfId", value.getIfId());
    }
  }
}
