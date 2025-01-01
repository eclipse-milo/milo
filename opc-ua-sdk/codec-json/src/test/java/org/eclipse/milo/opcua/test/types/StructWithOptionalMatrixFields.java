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
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
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

public class StructWithOptionalMatrixFields extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3035");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5059");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5061");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5060");

  private final @Nullable Matrix int32;

  private final @Nullable Matrix optionalInt32;

  private final @Nullable Matrix string;

  private final @Nullable Matrix optionalString;

  private final @Nullable Matrix duration;

  private final @Nullable Matrix optionalDuration;

  private final @Nullable Matrix concreteTestType;

  private final @Nullable Matrix optionalConcreteTestType;

  public StructWithOptionalMatrixFields(
      @Nullable Matrix int32,
      @Nullable Matrix optionalInt32,
      @Nullable Matrix string,
      @Nullable Matrix optionalString,
      @Nullable Matrix duration,
      @Nullable Matrix optionalDuration,
      @Nullable Matrix concreteTestType,
      @Nullable Matrix optionalConcreteTestType) {
    this.int32 = int32;
    this.optionalInt32 = optionalInt32;
    this.string = string;
    this.optionalString = optionalString;
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

  public @Nullable Matrix getInt32() {
    return int32;
  }

  public @Nullable Matrix getOptionalInt32() {
    return optionalInt32;
  }

  public @Nullable Matrix getString() {
    return string;
  }

  public @Nullable Matrix getOptionalString() {
    return optionalString;
  }

  public @Nullable Matrix getDuration() {
    return duration;
  }

  public @Nullable Matrix getOptionalDuration() {
    return optionalDuration;
  }

  public @Nullable Matrix getConcreteTestType() {
    return concreteTestType;
  }

  public @Nullable Matrix getOptionalConcreteTestType() {
    return optionalConcreteTestType;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StructWithOptionalMatrixFields that = (StructWithOptionalMatrixFields) object;
    var eqb = new EqualsBuilder();
    eqb.append(getInt32(), that.getInt32());
    eqb.append(getOptionalInt32(), that.getOptionalInt32());
    eqb.append(getString(), that.getString());
    eqb.append(getOptionalString(), that.getOptionalString());
    eqb.append(getDuration(), that.getDuration());
    eqb.append(getOptionalDuration(), that.getOptionalDuration());
    eqb.append(getConcreteTestType(), that.getConcreteTestType());
    eqb.append(getOptionalConcreteTestType(), that.getOptionalConcreteTestType());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getInt32());
    hcb.append(getOptionalInt32());
    hcb.append(getString());
    hcb.append(getOptionalString());
    hcb.append(getDuration());
    hcb.append(getOptionalDuration());
    hcb.append(getConcreteTestType());
    hcb.append(getOptionalConcreteTestType());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", StructWithOptionalMatrixFields.class.getSimpleName() + "[", "]");
    joiner.add("int32=" + getInt32());
    joiner.add("optionalInt32=" + getOptionalInt32());
    joiner.add("string=" + getString());
    joiner.add("optionalString=" + getOptionalString());
    joiner.add("duration=" + getDuration());
    joiner.add("optionalDuration=" + getOptionalDuration());
    joiner.add("concreteTestType=" + getConcreteTestType());
    joiner.add("optionalConcreteTestType=" + getOptionalConcreteTestType());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5059")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=22")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.StructureWithOptionalFields,
        new StructureField[] {
          new StructureField(
              "Int32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalInt32",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              true),
          new StructureField(
              "String",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalString",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              true),
          new StructureField(
              "Duration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalDuration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ConcreteTestType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3006")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OptionalConcreteTestType",
              LocalizedText.NULL_VALUE,
              ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3006")
                  .toNodeId(namespaceTable)
                  .orElseThrow(),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StructWithOptionalMatrixFields> {
    @Override
    public Class<StructWithOptionalMatrixFields> getType() {
      return StructWithOptionalMatrixFields.class;
    }

    @Override
    public StructWithOptionalMatrixFields decodeType(EncodingContext context, UaDecoder decoder) {
      final Matrix int32;
      final Matrix optionalInt32;
      final Matrix string;
      final Matrix optionalString;
      final Matrix duration;
      final Matrix optionalDuration;
      final Matrix concreteTestType;
      final Matrix optionalConcreteTestType;
      final long encodingMask = decoder.decodeUInt32("EncodingMask").longValue();
      int32 = decoder.decodeMatrix("Int32", BuiltinDataType.Int32);
      if ((encodingMask & (1L << 0)) != 0) {
        optionalInt32 = decoder.decodeMatrix("OptionalInt32", BuiltinDataType.Int32);
      } else {
        optionalInt32 = null;
      }
      string = decoder.decodeMatrix("String", BuiltinDataType.String);
      if ((encodingMask & (1L << 1)) != 0) {
        optionalString = decoder.decodeMatrix("OptionalString", BuiltinDataType.String);
      } else {
        optionalString = null;
      }
      duration = decoder.decodeMatrix("Duration", BuiltinDataType.Double);
      if ((encodingMask & (1L << 2)) != 0) {
        optionalDuration = decoder.decodeMatrix("OptionalDuration", BuiltinDataType.Double);
      } else {
        optionalDuration = null;
      }
      concreteTestType =
          (Matrix) decoder.decodeStructMatrix("ConcreteTestType", ConcreteTestType.TYPE_ID);
      if ((encodingMask & (1L << 3)) != 0) {
        optionalConcreteTestType =
            (Matrix)
                decoder.decodeStructMatrix("OptionalConcreteTestType", ConcreteTestType.TYPE_ID);
      } else {
        optionalConcreteTestType = null;
      }
      return new StructWithOptionalMatrixFields(
          int32,
          optionalInt32,
          string,
          optionalString,
          duration,
          optionalDuration,
          concreteTestType,
          optionalConcreteTestType);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StructWithOptionalMatrixFields value) {
      long encodingMask = 0L;
      if (value.getOptionalInt32() != null) {
        encodingMask |= (1L << 0);
      }
      if (value.getOptionalString() != null) {
        encodingMask |= (1L << 1);
      }
      if (value.getOptionalDuration() != null) {
        encodingMask |= (1L << 2);
      }
      if (value.getOptionalConcreteTestType() != null) {
        encodingMask |= (1L << 3);
      }
      encoder.encodeUInt32("EncodingMask", Unsigned.uint(encodingMask));
      encoder.encodeMatrix("Int32", value.getInt32());
      if (value.getOptionalInt32() != null) {
        encoder.encodeMatrix("OptionalInt32", value.getOptionalInt32());
      }
      encoder.encodeMatrix("String", value.getString());
      if (value.getOptionalString() != null) {
        encoder.encodeMatrix("OptionalString", value.getOptionalString());
      }
      encoder.encodeMatrix("Duration", value.getDuration());
      if (value.getOptionalDuration() != null) {
        encoder.encodeMatrix("OptionalDuration", value.getOptionalDuration());
      }
      encoder.encodeStructMatrix(
          "ConcreteTestType", value.getConcreteTestType(), ConcreteTestType.TYPE_ID);
      if (value.getOptionalConcreteTestType() != null) {
        encoder.encodeStructMatrix(
            "OptionalConcreteTestType",
            value.getOptionalConcreteTestType(),
            ConcreteTestType.TYPE_ID);
      }
    }
  }
}
