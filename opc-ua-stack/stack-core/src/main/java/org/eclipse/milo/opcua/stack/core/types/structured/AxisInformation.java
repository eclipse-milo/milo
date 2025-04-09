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
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.6">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.6</a>
 */
public class AxisInformation extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=12079");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=12089");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=12081");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15379");

  private final EUInformation engineeringUnits;

  private final Range euRange;

  private final LocalizedText title;

  private final AxisScaleEnumeration axisScaleType;

  private final Double @Nullable [] axisSteps;

  public AxisInformation(
      EUInformation engineeringUnits,
      Range euRange,
      LocalizedText title,
      AxisScaleEnumeration axisScaleType,
      Double @Nullable [] axisSteps) {
    this.engineeringUnits = engineeringUnits;
    this.euRange = euRange;
    this.title = title;
    this.axisScaleType = axisScaleType;
    this.axisSteps = axisSteps;
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

  public EUInformation getEngineeringUnits() {
    return engineeringUnits;
  }

  public Range getEuRange() {
    return euRange;
  }

  public LocalizedText getTitle() {
    return title;
  }

  public AxisScaleEnumeration getAxisScaleType() {
    return axisScaleType;
  }

  public Double @Nullable [] getAxisSteps() {
    return axisSteps;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AxisInformation that = (AxisInformation) object;
    var eqb = new EqualsBuilder();
    eqb.append(getEngineeringUnits(), that.getEngineeringUnits());
    eqb.append(getEuRange(), that.getEuRange());
    eqb.append(getTitle(), that.getTitle());
    eqb.append(getAxisScaleType(), that.getAxisScaleType());
    eqb.append(getAxisSteps(), that.getAxisSteps());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEngineeringUnits());
    hcb.append(getEuRange());
    hcb.append(getTitle());
    hcb.append(getAxisScaleType());
    hcb.append(getAxisSteps());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AxisInformation.class.getSimpleName() + "[", "]");
    joiner.add("engineeringUnits=" + getEngineeringUnits());
    joiner.add("euRange=" + getEuRange());
    joiner.add("title=" + getTitle());
    joiner.add("axisScaleType=" + getAxisScaleType());
    joiner.add("axisSteps=" + java.util.Arrays.toString(getAxisSteps()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12089),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "EngineeringUnits",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 887),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EURange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 884),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Title",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AxisScaleType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12077),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AxisSteps",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AxisInformation> {
    @Override
    public Class<AxisInformation> getType() {
      return AxisInformation.class;
    }

    @Override
    public AxisInformation decodeType(EncodingContext context, UaDecoder decoder) {
      final EUInformation engineeringUnits;
      final Range euRange;
      final LocalizedText title;
      final AxisScaleEnumeration axisScaleType;
      final Double[] axisSteps;
      engineeringUnits =
          (EUInformation) decoder.decodeStruct("EngineeringUnits", EUInformation.TYPE_ID);
      euRange = (Range) decoder.decodeStruct("EURange", Range.TYPE_ID);
      title = decoder.decodeLocalizedText("Title");
      axisScaleType = AxisScaleEnumeration.from(decoder.decodeEnum("AxisScaleType"));
      axisSteps = decoder.decodeDoubleArray("AxisSteps");
      return new AxisInformation(engineeringUnits, euRange, title, axisScaleType, axisSteps);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AxisInformation value) {
      encoder.encodeStruct("EngineeringUnits", value.getEngineeringUnits(), EUInformation.TYPE_ID);
      encoder.encodeStruct("EURange", value.getEuRange(), Range.TYPE_ID);
      encoder.encodeLocalizedText("Title", value.getTitle());
      encoder.encodeEnum("AxisScaleType", value.getAxisScaleType());
      encoder.encodeDoubleArray("AxisSteps", value.getAxisSteps());
    }
  }
}
