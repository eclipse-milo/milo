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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.8">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.8</a>
 */
public class XVType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=12080");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=12090");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=12082");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15380");

  private final Double x;

  private final Float value;

  public XVType(Double x, Float value) {
    this.x = x;
    this.value = value;
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

  public Double getX() {
    return x;
  }

  public Float getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    XVType that = (XVType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getX(), that.getX());
    eqb.append(getValue(), that.getValue());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getX());
    hcb.append(getValue());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", XVType.class.getSimpleName() + "[", "]");
    joiner.add("x=" + getX());
    joiner.add("value=" + getValue());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12090),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "X",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Value",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 10),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<XVType> {
    @Override
    public Class<XVType> getType() {
      return XVType.class;
    }

    @Override
    public XVType decodeType(EncodingContext context, UaDecoder decoder) {
      final Double x;
      final Float value;
      x = decoder.decodeDouble("X");
      value = decoder.decodeFloat("Value");
      return new XVType(x, value);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, XVType value) {
      encoder.encodeDouble("X", value.getX());
      encoder.encodeFloat("Value", value.getValue());
    }
  }
}
