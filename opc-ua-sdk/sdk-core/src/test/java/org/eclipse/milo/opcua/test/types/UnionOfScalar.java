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

public class UnionOfScalar extends Union {
  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3020");

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5029");

  public static final ExpandedNodeId XML_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5030");

  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5031");

  private final Type type;

  private final Object value;

  private UnionOfScalar(Type type, Object value) {
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

  public Boolean asBoolean() {
    return (Boolean) value;
  }

  public Byte asSByte() {
    return (Byte) value;
  }

  public UByte asByte() {
    return (UByte) value;
  }

  public Boolean getBoolean() {
    return (Boolean) value;
  }

  public Byte getSByte() {
    return (Byte) value;
  }

  public UByte getByte() {
    return (UByte) value;
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
      case NULL -> "UnionOfScalar[null]";
      case Boolean -> "UnionOfScalar[Boolean=" + value + "]";
      case SByte -> "UnionOfScalar[SByte=" + value + "]";
      case Byte -> "UnionOfScalar[Byte=" + value + "]";
    };
  }

  public static UnionOfScalar ofNull() {
    return new UnionOfScalar(Type.NULL, null);
  }

  public static UnionOfScalar ofBoolean(Boolean value) {
    return new UnionOfScalar(Type.Boolean, value);
  }

  public static UnionOfScalar ofSByte(Byte value) {
    return new UnionOfScalar(Type.SByte, value);
  }

  public static UnionOfScalar ofByte(UByte value) {
    return new UnionOfScalar(Type.Byte, value);
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5029")
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
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SByte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 2),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Byte",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
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

  public static final class Codec extends GenericDataTypeCodec<UnionOfScalar> {
    @Override
    public Class<UnionOfScalar> getType() {
      return UnionOfScalar.class;
    }

    @Override
    public UnionOfScalar decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger switchValue = decoder.decodeUInt32("SwitchField");
      switch (switchValue.intValue()) {
        case 0:
          return UnionOfScalar.ofNull();
        case 1:
          {
            final Boolean _boolean;
            _boolean = decoder.decodeBoolean("Boolean");
            return UnionOfScalar.ofBoolean(_boolean);
          }
        case 2:
          {
            final Byte sByte;
            sByte = decoder.decodeSByte("SByte");
            return UnionOfScalar.ofSByte(sByte);
          }
        case 3:
          {
            final UByte _byte;
            _byte = decoder.decodeByte("Byte");
            return UnionOfScalar.ofByte(_byte);
          }
        default:
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError,
              "unknown field in Union UnionOfScalar: " + switchValue);
      }
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, UnionOfScalar value) {
      encoder.encodeUInt32("SwitchField", Unsigned.uint(value.type.ordinal()));
      switch (value.type) {
        case NULL:
          break;
        case Boolean:
          {
            encoder.encodeBoolean("Boolean", value.getBoolean());
            break;
          }
        case SByte:
          {
            encoder.encodeSByte("SByte", value.getSByte());
            break;
          }
        case Byte:
          {
            encoder.encodeByte("Byte", value.getByte());
            break;
          }
      }
    }
  }
}
