/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.test.types;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.Union;

public class UnionOfArray extends Union {
  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3023");

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5032");

  public static final ExpandedNodeId XML_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5033");

  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5034");

  private final Type type;

  private final Object value;

  private UnionOfArray(Type type, Object value) {
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

  public Boolean[] asBoolean() {
    return (Boolean[]) value;
  }

  public Byte[] asSByte() {
    return (Byte[]) value;
  }

  public UByte[] asByte() {
    return (UByte[]) value;
  }

  public Boolean[] getBoolean() {
    return (Boolean[]) value;
  }

  public Byte[] getSByte() {
    return (Byte[]) value;
  }

  public UByte[] getByte() {
    return (UByte[]) value;
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
    return switch (type) {
      case NULL -> "UnionOfArray[null]";
      case Boolean -> "UnionOfArray[Boolean=" + value + "]";
      case SByte -> "UnionOfArray[SByte=" + value + "]";
      case Byte -> "UnionOfArray[Byte=" + value + "]";
    };
  }

  public static UnionOfArray ofNull() {
    return new UnionOfArray(Type.NULL, null);
  }

  public static UnionOfArray ofBoolean(Boolean[] value) {
    return new UnionOfArray(Type.Boolean, value);
  }

  public static UnionOfArray ofSByte(Byte[] value) {
    return new UnionOfArray(Type.SByte, value);
  }

  public static UnionOfArray ofByte(UByte[] value) {
    return new UnionOfArray(Type.Byte, value);
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5032")
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
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SByte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              1,
              new UInteger[] {UInteger.valueOf(3)},
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Byte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              1,
              new UInteger[] {UInteger.valueOf(3)},
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

  public static final class Codec extends GenericDataTypeCodec<UnionOfArray> {
    @Override
    public Class<UnionOfArray> getType() {
      return UnionOfArray.class;
    }

    @Override
    public UnionOfArray decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger switchValue = decoder.decodeUInt32("SwitchField");
      switch (switchValue.intValue()) {
        case 0:
          return UnionOfArray.ofNull();
        case 1:
          {
            final Boolean[] _boolean;
            _boolean = decoder.decodeBooleanArray("Boolean");
            return UnionOfArray.ofBoolean(_boolean);
          }
        case 2:
          {
            final Byte[] sByte;
            sByte = decoder.decodeSByteArray("SByte");
            return UnionOfArray.ofSByte(sByte);
          }
        case 3:
          {
            final UByte[] _byte;
            _byte = decoder.decodeByteArray("Byte");
            return UnionOfArray.ofByte(_byte);
          }
        default:
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "unknown field in Union UnionOfArray: " + switchValue);
      }
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, UnionOfArray value) {
      encoder.encodeUInt32("SwitchField", Unsigned.uint(value.type.ordinal()));
      switch (value.type) {
        case NULL:
          break;
        case Boolean:
          {
            encoder.encodeBooleanArray("Boolean", value.getBoolean());
            break;
          }
        case SByte:
          {
            encoder.encodeSByteArray("SByte", value.getSByte());
            break;
          }
        case Byte:
          {
            encoder.encodeByteArray("Byte", value.getByte());
            break;
          }
      }
    }
  }
}
