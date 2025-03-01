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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.40">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.40</a>
 */
public class UnsignedRationalNumber extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=24107");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24110");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24122");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=24134");

  private final UInteger numerator;

  private final UInteger denominator;

  public UnsignedRationalNumber(UInteger numerator, UInteger denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
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

  public UInteger getNumerator() {
    return numerator;
  }

  public UInteger getDenominator() {
    return denominator;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UnsignedRationalNumber that = (UnsignedRationalNumber) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNumerator(), that.getNumerator());
    eqb.append(getDenominator(), that.getDenominator());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNumerator());
    hcb.append(getDenominator());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UnsignedRationalNumber.class.getSimpleName() + "[", "]");
    joiner.add("numerator=" + getNumerator());
    joiner.add("denominator=" + getDenominator());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24110),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Numerator",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Denominator",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UnsignedRationalNumber> {
    @Override
    public Class<UnsignedRationalNumber> getType() {
      return UnsignedRationalNumber.class;
    }

    @Override
    public UnsignedRationalNumber decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger numerator;
      final UInteger denominator;
      numerator = decoder.decodeUInt32("Numerator");
      denominator = decoder.decodeUInt32("Denominator");
      return new UnsignedRationalNumber(numerator, denominator);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UnsignedRationalNumber value) {
      encoder.encodeUInt32("Numerator", value.getNumerator());
      encoder.encodeUInt32("Denominator", value.getDenominator());
    }
  }
}
