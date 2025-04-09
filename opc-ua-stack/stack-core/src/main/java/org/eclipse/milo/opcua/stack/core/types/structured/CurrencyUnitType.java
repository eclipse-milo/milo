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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.2</a>
 */
public class CurrencyUnitType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23498");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23507");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23520");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23528");

  private final Short numericCode;

  private final Byte exponent;

  private final @Nullable String alphabeticCode;

  private final LocalizedText currency;

  public CurrencyUnitType(
      Short numericCode, Byte exponent, @Nullable String alphabeticCode, LocalizedText currency) {
    this.numericCode = numericCode;
    this.exponent = exponent;
    this.alphabeticCode = alphabeticCode;
    this.currency = currency;
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

  public Short getNumericCode() {
    return numericCode;
  }

  public Byte getExponent() {
    return exponent;
  }

  public @Nullable String getAlphabeticCode() {
    return alphabeticCode;
  }

  public LocalizedText getCurrency() {
    return currency;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CurrencyUnitType that = (CurrencyUnitType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNumericCode(), that.getNumericCode());
    eqb.append(getExponent(), that.getExponent());
    eqb.append(getAlphabeticCode(), that.getAlphabeticCode());
    eqb.append(getCurrency(), that.getCurrency());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNumericCode());
    hcb.append(getExponent());
    hcb.append(getAlphabeticCode());
    hcb.append(getCurrency());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", CurrencyUnitType.class.getSimpleName() + "[", "]");
    joiner.add("numericCode=" + getNumericCode());
    joiner.add("exponent=" + getExponent());
    joiner.add("alphabeticCode='" + getAlphabeticCode() + "'");
    joiner.add("currency=" + getCurrency());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23507),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NumericCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 4),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Exponent",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AlphabeticCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Currency",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<CurrencyUnitType> {
    @Override
    public Class<CurrencyUnitType> getType() {
      return CurrencyUnitType.class;
    }

    @Override
    public CurrencyUnitType decodeType(EncodingContext context, UaDecoder decoder) {
      final Short numericCode;
      final Byte exponent;
      final String alphabeticCode;
      final LocalizedText currency;
      numericCode = decoder.decodeInt16("NumericCode");
      exponent = decoder.decodeSByte("Exponent");
      alphabeticCode = decoder.decodeString("AlphabeticCode");
      currency = decoder.decodeLocalizedText("Currency");
      return new CurrencyUnitType(numericCode, exponent, alphabeticCode, currency);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, CurrencyUnitType value) {
      encoder.encodeInt16("NumericCode", value.getNumericCode());
      encoder.encodeSByte("Exponent", value.getExponent());
      encoder.encodeString("AlphabeticCode", value.getAlphabeticCode());
      encoder.encodeLocalizedText("Currency", value.getCurrency());
    }
  }
}
