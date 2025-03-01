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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.6.4">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.6.4</a>
 */
public class QuantityDimension extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=32438");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=32562");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=32574");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=32586");

  private final Byte massExponent;

  private final Byte lengthExponent;

  private final Byte timeExponent;

  private final Byte electricCurrentExponent;

  private final Byte amountOfSubstanceExponent;

  private final Byte luminousIntensityExponent;

  private final Byte absoluteTemperatureExponent;

  private final Byte dimensionlessExponent;

  public QuantityDimension(
      Byte massExponent,
      Byte lengthExponent,
      Byte timeExponent,
      Byte electricCurrentExponent,
      Byte amountOfSubstanceExponent,
      Byte luminousIntensityExponent,
      Byte absoluteTemperatureExponent,
      Byte dimensionlessExponent) {
    this.massExponent = massExponent;
    this.lengthExponent = lengthExponent;
    this.timeExponent = timeExponent;
    this.electricCurrentExponent = electricCurrentExponent;
    this.amountOfSubstanceExponent = amountOfSubstanceExponent;
    this.luminousIntensityExponent = luminousIntensityExponent;
    this.absoluteTemperatureExponent = absoluteTemperatureExponent;
    this.dimensionlessExponent = dimensionlessExponent;
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

  public Byte getMassExponent() {
    return massExponent;
  }

  public Byte getLengthExponent() {
    return lengthExponent;
  }

  public Byte getTimeExponent() {
    return timeExponent;
  }

  public Byte getElectricCurrentExponent() {
    return electricCurrentExponent;
  }

  public Byte getAmountOfSubstanceExponent() {
    return amountOfSubstanceExponent;
  }

  public Byte getLuminousIntensityExponent() {
    return luminousIntensityExponent;
  }

  public Byte getAbsoluteTemperatureExponent() {
    return absoluteTemperatureExponent;
  }

  public Byte getDimensionlessExponent() {
    return dimensionlessExponent;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    QuantityDimension that = (QuantityDimension) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMassExponent(), that.getMassExponent());
    eqb.append(getLengthExponent(), that.getLengthExponent());
    eqb.append(getTimeExponent(), that.getTimeExponent());
    eqb.append(getElectricCurrentExponent(), that.getElectricCurrentExponent());
    eqb.append(getAmountOfSubstanceExponent(), that.getAmountOfSubstanceExponent());
    eqb.append(getLuminousIntensityExponent(), that.getLuminousIntensityExponent());
    eqb.append(getAbsoluteTemperatureExponent(), that.getAbsoluteTemperatureExponent());
    eqb.append(getDimensionlessExponent(), that.getDimensionlessExponent());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMassExponent());
    hcb.append(getLengthExponent());
    hcb.append(getTimeExponent());
    hcb.append(getElectricCurrentExponent());
    hcb.append(getAmountOfSubstanceExponent());
    hcb.append(getLuminousIntensityExponent());
    hcb.append(getAbsoluteTemperatureExponent());
    hcb.append(getDimensionlessExponent());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", QuantityDimension.class.getSimpleName() + "[", "]");
    joiner.add("massExponent=" + getMassExponent());
    joiner.add("lengthExponent=" + getLengthExponent());
    joiner.add("timeExponent=" + getTimeExponent());
    joiner.add("electricCurrentExponent=" + getElectricCurrentExponent());
    joiner.add("amountOfSubstanceExponent=" + getAmountOfSubstanceExponent());
    joiner.add("luminousIntensityExponent=" + getLuminousIntensityExponent());
    joiner.add("absoluteTemperatureExponent=" + getAbsoluteTemperatureExponent());
    joiner.add("dimensionlessExponent=" + getDimensionlessExponent());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 32562),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MassExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LengthExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TimeExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ElectricCurrentExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AmountOfSubstanceExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LuminousIntensityExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AbsoluteTemperatureExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DimensionlessExponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<QuantityDimension> {
    @Override
    public Class<QuantityDimension> getType() {
      return QuantityDimension.class;
    }

    @Override
    public QuantityDimension decodeType(EncodingContext context, UaDecoder decoder) {
      final Byte massExponent;
      final Byte lengthExponent;
      final Byte timeExponent;
      final Byte electricCurrentExponent;
      final Byte amountOfSubstanceExponent;
      final Byte luminousIntensityExponent;
      final Byte absoluteTemperatureExponent;
      final Byte dimensionlessExponent;
      massExponent = decoder.decodeSByte("MassExponent");
      lengthExponent = decoder.decodeSByte("LengthExponent");
      timeExponent = decoder.decodeSByte("TimeExponent");
      electricCurrentExponent = decoder.decodeSByte("ElectricCurrentExponent");
      amountOfSubstanceExponent = decoder.decodeSByte("AmountOfSubstanceExponent");
      luminousIntensityExponent = decoder.decodeSByte("LuminousIntensityExponent");
      absoluteTemperatureExponent = decoder.decodeSByte("AbsoluteTemperatureExponent");
      dimensionlessExponent = decoder.decodeSByte("DimensionlessExponent");
      return new QuantityDimension(
          massExponent,
          lengthExponent,
          timeExponent,
          electricCurrentExponent,
          amountOfSubstanceExponent,
          luminousIntensityExponent,
          absoluteTemperatureExponent,
          dimensionlessExponent);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, QuantityDimension value) {
      encoder.encodeSByte("MassExponent", value.getMassExponent());
      encoder.encodeSByte("LengthExponent", value.getLengthExponent());
      encoder.encodeSByte("TimeExponent", value.getTimeExponent());
      encoder.encodeSByte("ElectricCurrentExponent", value.getElectricCurrentExponent());
      encoder.encodeSByte("AmountOfSubstanceExponent", value.getAmountOfSubstanceExponent());
      encoder.encodeSByte("LuminousIntensityExponent", value.getLuminousIntensityExponent());
      encoder.encodeSByte("AbsoluteTemperatureExponent", value.getAbsoluteTemperatureExponent());
      encoder.encodeSByte("DimensionlessExponent", value.getDimensionlessExponent());
    }
  }
}
