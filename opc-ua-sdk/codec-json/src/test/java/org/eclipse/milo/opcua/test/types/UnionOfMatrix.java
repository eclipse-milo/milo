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
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.Union;

public class UnionOfMatrix extends Union {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3038");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5062");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5064");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5063");

  private final Type type;

  private final Object value;

  private UnionOfMatrix(Type type, Object value) {
    this.type = type;
    this.value = value;
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

  public Matrix asBoolean() {
    return (Matrix) value;
  }

  public Matrix asSByte() {
    return (Matrix) value;
  }

  public Matrix asByte() {
    return (Matrix) value;
  }

  public Matrix getBoolean() {
    return (Matrix) value;
  }

  public Matrix getSByte() {
    return (Matrix) value;
  }

  public Matrix getByte() {
    return (Matrix) value;
  }

  public boolean isBoolean() {
    return type == Type.Boolean;
  }

  public boolean isSByte() {
    return type == Type.SByte;
  }

  public boolean isByte() {
    return type == Type.Byte;
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UnionOfMatrix.class.getSimpleName() + "[", "]");
    joiner.add("_boolean=" + getBoolean());
    joiner.add("sByte=" + getSByte());
    joiner.add("_byte=" + getByte());
    return joiner.toString();
  }

  public static UnionOfMatrix ofNull() {
    return new UnionOfMatrix(Type.NULL, null);
  }

  public static UnionOfMatrix ofBoolean(Matrix value) {
    return new UnionOfMatrix(Type.Boolean, value);
  }

  public static UnionOfMatrix ofSByte(Matrix value) {
    return new UnionOfMatrix(Type.SByte, value);
  }

  public static UnionOfMatrix ofByte(Matrix value) {
    return new UnionOfMatrix(Type.Byte, value);
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5062")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12756")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.Union,
        new StructureField[] {
          new StructureField(
              "Boolean",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SByte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Byte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              2,
              new UInteger[] {UInteger.valueOf(2), UInteger.valueOf(2)},
              UInteger.valueOf(0),
              false)
        });
  }

  enum Type {
    NULL,

    Boolean,

    SByte,

    Byte
  }

  public static final class Codec extends GenericDataTypeCodec<UnionOfMatrix> {
    @Override
    public Class<UnionOfMatrix> getType() {
      return UnionOfMatrix.class;
    }

    @Override
    public UnionOfMatrix decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger switchValue = decoder.decodeUInt32("SwitchField");
      switch (switchValue.intValue()) {
        case 0:
          return UnionOfMatrix.ofNull();
        case 1:
          {
            final Matrix _boolean;
            _boolean = decoder.decodeMatrix("Boolean", OpcUaDataType.Boolean);
            return UnionOfMatrix.ofBoolean(_boolean);
          }
        case 2:
          {
            final Matrix sByte;
            sByte = decoder.decodeMatrix("SByte", OpcUaDataType.SByte);
            return UnionOfMatrix.ofSByte(sByte);
          }
        case 3:
          {
            final Matrix _byte;
            _byte = decoder.decodeMatrix("Byte", OpcUaDataType.Byte);
            return UnionOfMatrix.ofByte(_byte);
          }
        default:
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError,
              "unknown field in Union UnionOfMatrix: " + switchValue);
      }
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, UnionOfMatrix value) {
      encoder.encodeUInt32("SwitchField", Unsigned.uint(value.type.ordinal()));
      switch (value.type) {
        case NULL:
          break;
        case Boolean:
          {
            encoder.encodeMatrix("Boolean", value.getBoolean());
            break;
          }
        case SByte:
          {
            encoder.encodeMatrix("SByte", value.getSByte());
            break;
          }
        case Byte:
          {
            encoder.encodeMatrix("Byte", value.getByte());
            break;
          }
      }
    }
  }
}
