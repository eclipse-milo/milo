/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.test.types;

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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class StructWithOptionalScalarFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3016");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5011");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5013");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5012");

  private final @Nullable String string;

  private final @Nullable String optionalString;

  private final Integer int32;

  private final @Nullable Integer optionalInt32;

  private final Double duration;

  private final @Nullable Double optionalDuration;

  private final ConcreteTestType concreteTestType;

  private final @Nullable ConcreteTestType optionalConcreteTestType;

  public StructWithOptionalScalarFields(
      @Nullable String string,
      @Nullable String optionalString,
      Integer int32,
      @Nullable Integer optionalInt32,
      Double duration,
      @Nullable Double optionalDuration,
      ConcreteTestType concreteTestType,
      @Nullable ConcreteTestType optionalConcreteTestType) {
    this.string = string;
    this.optionalString = optionalString;
    this.int32 = int32;
    this.optionalInt32 = optionalInt32;
    this.duration = duration;
    this.optionalDuration = optionalDuration;
    this.concreteTestType = concreteTestType;
    this.optionalConcreteTestType = optionalConcreteTestType;
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
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  public @Nullable String getString() {
    return string;
  }

  public @Nullable String getOptionalString() {
    return optionalString;
  }

  public Integer getInt32() {
    return int32;
  }

  public @Nullable Integer getOptionalInt32() {
    return optionalInt32;
  }

  public Double getDuration() {
    return duration;
  }

  public @Nullable Double getOptionalDuration() {
    return optionalDuration;
  }

  public ConcreteTestType getConcreteTestType() {
    return concreteTestType;
  }

  public @Nullable ConcreteTestType getOptionalConcreteTestType() {
    return optionalConcreteTestType;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithOptionalScalarFields that = (StructWithOptionalScalarFields) object;
    var eqb = new EqualsBuilder();
    eqb.append(getString(), that.getString());
    eqb.append(getOptionalString(), that.getOptionalString());
    eqb.append(getInt32(), that.getInt32());
    eqb.append(getOptionalInt32(), that.getOptionalInt32());
    eqb.append(getDuration(), that.getDuration());
    eqb.append(getOptionalDuration(), that.getOptionalDuration());
    eqb.append(getConcreteTestType(), that.getConcreteTestType());
    eqb.append(getOptionalConcreteTestType(), that.getOptionalConcreteTestType());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getString());
    hcb.append(getOptionalString());
    hcb.append(getInt32());
    hcb.append(getOptionalInt32());
    hcb.append(getDuration());
    hcb.append(getOptionalDuration());
    hcb.append(getConcreteTestType());
    hcb.append(getOptionalConcreteTestType());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", StructWithOptionalScalarFields.class.getSimpleName() + "[", "]");
    joiner.add("string='" + getString() + "'");
    joiner.add("optionalString='" + getOptionalString() + "'");
    joiner.add("int32=" + getInt32());
    joiner.add("optionalInt32=" + getOptionalInt32());
    joiner.add("duration=" + getDuration());
    joiner.add("optionalDuration=" + getOptionalDuration());
    joiner.add("concreteTestType=" + getConcreteTestType());
    joiner.add("optionalConcreteTestType=" + getOptionalConcreteTestType());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5011")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=22")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.StructureWithOptionalFields,
        new StructureField[] {
          new StructureField(
              "String",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalString",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "Int32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalInt32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "Duration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalDuration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ConcreteTestType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3006")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalConcreteTestType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3006")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              -1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithOptionalScalarFields> {
    @Override
    public Class<StructWithOptionalScalarFields> getType() {
      return StructWithOptionalScalarFields.class;
    }

    @Override
    public StructWithOptionalScalarFields decodeType(EncodingContext context, UaDecoder decoder) {
      final String string;
      final String optionalString;
      final Integer int32;
      final Integer optionalInt32;
      final Double duration;
      final Double optionalDuration;
      final ConcreteTestType concreteTestType;
      final ConcreteTestType optionalConcreteTestType;
      final long encodingMask = decoder.decodeUInt32("EncodingMask").longValue();
      string = decoder.decodeString("String");
      if ((encodingMask & (1L << 0)) != 0) {
        optionalString = decoder.decodeString("OptionalString");
      } else {
        optionalString = null;
      }
      int32 = decoder.decodeInt32("Int32");
      if ((encodingMask & (1L << 1)) != 0) {
        optionalInt32 = decoder.decodeInt32("OptionalInt32");
      } else {
        optionalInt32 = null;
      }
      duration = decoder.decodeDouble("Duration");
      if ((encodingMask & (1L << 2)) != 0) {
        optionalDuration = decoder.decodeDouble("OptionalDuration");
      } else {
        optionalDuration = null;
      }
      concreteTestType =
          (ConcreteTestType) decoder.decodeStruct("ConcreteTestType", ConcreteTestType.TYPE_ID);
      if ((encodingMask & (1L << 3)) != 0) {
        optionalConcreteTestType =
            (ConcreteTestType)
                decoder.decodeStruct("OptionalConcreteTestType", ConcreteTestType.TYPE_ID);
      } else {
        optionalConcreteTestType = null;
      }
      return new StructWithOptionalScalarFields(
          string,
          optionalString,
          int32,
          optionalInt32,
          duration,
          optionalDuration,
          concreteTestType,
          optionalConcreteTestType);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithOptionalScalarFields value) {
      long encodingMask = 0L;
      if (value.getOptionalString() != null) {
        encodingMask |= (1L << 0);
      }
      if (value.getOptionalInt32() != null) {
        encodingMask |= (1L << 1);
      }
      if (value.getOptionalDuration() != null) {
        encodingMask |= (1L << 2);
      }
      if (value.getOptionalConcreteTestType() != null) {
        encodingMask |= (1L << 3);
      }
      encoder.encodeUInt32("EncodingMask", Unsigned.uint(encodingMask));
      encoder.encodeString("String", value.getString());
      if (value.getOptionalString() != null) {
        encoder.encodeString("OptionalString", value.getOptionalString());
      }
      encoder.encodeInt32("Int32", value.getInt32());
      if (value.getOptionalInt32() != null) {
        encoder.encodeInt32("OptionalInt32", value.getOptionalInt32());
      }
      encoder.encodeDouble("Duration", value.getDuration());
      if (value.getOptionalDuration() != null) {
        encoder.encodeDouble("OptionalDuration", value.getOptionalDuration());
      }
      encoder.encodeStruct(
          "ConcreteTestType", value.getConcreteTestType(), ConcreteTestType.TYPE_ID);
      if (value.getOptionalConcreteTestType() != null) {
        encoder.encodeStruct(
            "OptionalConcreteTestType",
            value.getOptionalConcreteTestType(),
            ConcreteTestType.TYPE_ID);
      }
    }
  }
}
