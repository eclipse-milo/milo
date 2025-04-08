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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.4/#6.5.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.4/#6.5.4.1</a>
 */
public class AggregateConfiguration extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=948");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=950");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=949");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15304");

  private final Boolean useServerCapabilitiesDefaults;

  private final Boolean treatUncertainAsBad;

  private final UByte percentDataBad;

  private final UByte percentDataGood;

  private final Boolean useSlopedExtrapolation;

  public AggregateConfiguration(
      Boolean useServerCapabilitiesDefaults,
      Boolean treatUncertainAsBad,
      UByte percentDataBad,
      UByte percentDataGood,
      Boolean useSlopedExtrapolation) {
    this.useServerCapabilitiesDefaults = useServerCapabilitiesDefaults;
    this.treatUncertainAsBad = treatUncertainAsBad;
    this.percentDataBad = percentDataBad;
    this.percentDataGood = percentDataGood;
    this.useSlopedExtrapolation = useSlopedExtrapolation;
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

  public Boolean getUseServerCapabilitiesDefaults() {
    return useServerCapabilitiesDefaults;
  }

  public Boolean getTreatUncertainAsBad() {
    return treatUncertainAsBad;
  }

  public UByte getPercentDataBad() {
    return percentDataBad;
  }

  public UByte getPercentDataGood() {
    return percentDataGood;
  }

  public Boolean getUseSlopedExtrapolation() {
    return useSlopedExtrapolation;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AggregateConfiguration that = (AggregateConfiguration) object;
    var eqb = new EqualsBuilder();
    eqb.append(getUseServerCapabilitiesDefaults(), that.getUseServerCapabilitiesDefaults());
    eqb.append(getTreatUncertainAsBad(), that.getTreatUncertainAsBad());
    eqb.append(getPercentDataBad(), that.getPercentDataBad());
    eqb.append(getPercentDataGood(), that.getPercentDataGood());
    eqb.append(getUseSlopedExtrapolation(), that.getUseSlopedExtrapolation());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getUseServerCapabilitiesDefaults());
    hcb.append(getTreatUncertainAsBad());
    hcb.append(getPercentDataBad());
    hcb.append(getPercentDataGood());
    hcb.append(getUseSlopedExtrapolation());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AggregateConfiguration.class.getSimpleName() + "[", "]");
    joiner.add("useServerCapabilitiesDefaults=" + getUseServerCapabilitiesDefaults());
    joiner.add("treatUncertainAsBad=" + getTreatUncertainAsBad());
    joiner.add("percentDataBad=" + getPercentDataBad());
    joiner.add("percentDataGood=" + getPercentDataGood());
    joiner.add("useSlopedExtrapolation=" + getUseSlopedExtrapolation());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 950),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "UseServerCapabilitiesDefaults",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TreatUncertainAsBad",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PercentDataBad",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PercentDataGood",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UseSlopedExtrapolation",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AggregateConfiguration> {
    @Override
    public Class<AggregateConfiguration> getType() {
      return AggregateConfiguration.class;
    }

    @Override
    public AggregateConfiguration decodeType(EncodingContext context, UaDecoder decoder) {
      final Boolean useServerCapabilitiesDefaults;
      final Boolean treatUncertainAsBad;
      final UByte percentDataBad;
      final UByte percentDataGood;
      final Boolean useSlopedExtrapolation;
      useServerCapabilitiesDefaults = decoder.decodeBoolean("UseServerCapabilitiesDefaults");
      treatUncertainAsBad = decoder.decodeBoolean("TreatUncertainAsBad");
      percentDataBad = decoder.decodeByte("PercentDataBad");
      percentDataGood = decoder.decodeByte("PercentDataGood");
      useSlopedExtrapolation = decoder.decodeBoolean("UseSlopedExtrapolation");
      return new AggregateConfiguration(
          useServerCapabilitiesDefaults,
          treatUncertainAsBad,
          percentDataBad,
          percentDataGood,
          useSlopedExtrapolation);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, AggregateConfiguration value) {
      encoder.encodeBoolean(
          "UseServerCapabilitiesDefaults", value.getUseServerCapabilitiesDefaults());
      encoder.encodeBoolean("TreatUncertainAsBad", value.getTreatUncertainAsBad());
      encoder.encodeByte("PercentDataBad", value.getPercentDataBad());
      encoder.encodeByte("PercentDataGood", value.getPercentDataGood());
      encoder.encodeBoolean("UseSlopedExtrapolation", value.getUseSlopedExtrapolation());
    }
  }
}
