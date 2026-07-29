/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class VariantSerializationTest extends BinarySerializationFixture {

  public static Object[][] getVariants() {
    return new Object[][] {
      {new Variant(null)},
      {new Variant("hello, world")},
      {new Variant(42)},
      {new Variant(new Integer[] {0, 1, 2, 3})},
      {new Variant(Matrix.ofInt32(new Integer[][] {{0, 1}, {2, 3}}))},
      {new Variant(new Long[] {0L, 1L, 2L, 3L})},
      {new Variant(Matrix.ofInt64(new Long[][] {{0L, 1L}, {2L, 3L}}))},
      {new Variant(new UInteger[] {uint(0), uint(1), uint(2), uint(3)})},
      {new Variant(Matrix.ofUInt32(new UInteger[][] {{uint(0), uint(1)}, {uint(2), uint(3)}}))},
      {new Variant(new Variant[] {new Variant(0), new Variant(1), new Variant(2)})}
    };
  }

  @ParameterizedTest
  @MethodSource("getVariants")
  public void testVariantRoundTrip(Variant variant) {
    writer.encodeVariant(variant);
    Variant decoded = reader.decodeVariant();

    assertEquals(variant, decoded);
  }

  @Test
  public void testVariant_UaStructure() {
    ServiceCounterDataType sc1 = new ServiceCounterDataType(uint(1), uint(2));

    Variant v = new Variant(sc1);
    writer.encodeVariant(v);
    Variant decoded = reader.decodeVariant();

    ExtensionObject extensionObject = (ExtensionObject) decoded.value();
    ServiceCounterDataType sc2 =
        (ServiceCounterDataType) extensionObject.decode(DefaultEncodingContext.INSTANCE);

    assertEquals(sc1.getTotalCount(), sc2.getTotalCount());
    assertEquals(sc1.getErrorCount(), sc2.getErrorCount());
  }

  public static Object[][] getPrimitiveArrayVariants() {
    return new Object[][] {
      {new Variant(new int[] {0, 1, 2, 3}), new Variant(new Integer[] {0, 1, 2, 3})},
      {
        new Variant(Matrix.ofInt32(new int[][] {{0, 1}, {2, 3}})),
        new Variant(Matrix.ofInt32(new Integer[][] {{0, 1}, {2, 3}}))
      },
      {new Variant(new long[] {0L, 1L, 2L, 3L}), new Variant(new Long[] {0L, 1L, 2L, 3L})},
      {
        new Variant(Matrix.ofInt64(new long[][] {{0L, 1L}, {2L, 3L}})),
        new Variant(Matrix.ofInt64(new Long[][] {{0L, 1L}, {2L, 3L}}))
      }
    };
  }

  @ParameterizedTest
  @MethodSource("getPrimitiveArrayVariants")
  @DisplayName(
      "Test that after primitive array types given to variants come out as expected after"
          + " encoding/decoding.")
  public void testPrimitiveArrayVariantRoundTrip(Variant variant, Variant expected) {
    writer.encodeVariant(variant);
    Variant decoded = reader.decodeVariant();

    assertEquals(expected, decoded);
  }

  public static Object[][] getBuiltinArrayVariants() {
    return new Object[][] {
      {new Variant(new Boolean[] {true, false})},
      {new Variant(new Byte[] {(byte) -1, (byte) 2})},
      {new Variant(new UByte[] {ubyte(1), ubyte(255)})},
      {new Variant(new Short[] {(short) -1, (short) 2})},
      {new Variant(new UShort[] {ushort(1), ushort(65535)})},
      {new Variant(new Integer[] {-1, 2})},
      {new Variant(new UInteger[] {uint(1), uint(4294967295L)})},
      {new Variant(new Long[] {-1L, 2L})},
      {new Variant(new ULong[] {ulong(1), ulong(2)})},
      {new Variant(new Float[] {-1.5f, 2.5f})},
      {new Variant(new Double[] {-1.5d, 2.5d})},
      {new Variant(new String[] {"a", null})},
      {new Variant(new DateTime[] {new DateTime(1L), new DateTime(2L)})},
      {
        new Variant(
            new UUID[] {
              UUID.fromString("00000000-0000-0000-0000-000000000001"),
              UUID.fromString("0f0e0d0c-0b0a-0908-0706-050403020100")
            })
      },
      {new Variant(new ByteString[] {ByteString.of(new byte[] {1, 2}), ByteString.NULL_VALUE})},
      {new Variant(new XmlElement[] {new XmlElement("<a/>"), new XmlElement("<b/>")})},
      {new Variant(new NodeId[] {new NodeId(0, 1), new NodeId(2, "s")})},
      {
        new Variant(
            new ExpandedNodeId[] {new NodeId(0, 1).expanded(), new NodeId(2, "s").expanded()})
      },
      {
        new Variant(
            new StatusCode[] {StatusCode.GOOD, new StatusCode(StatusCodes.Bad_InternalError)})
      },
      {new Variant(new QualifiedName[] {new QualifiedName(0, "a"), new QualifiedName(2, "b")})},
      {new Variant(new LocalizedText[] {LocalizedText.english("a"), new LocalizedText("b")})},
      {
        new Variant(
            new ExtensionObject[] {
              ExtensionObject.of(ByteString.of(new byte[] {1, 2}), new NodeId(0, 1))
            })
      },
      {
        new Variant(
            new DataValue[] {new DataValue(new Variant(1)), new DataValue(new Variant("x"))})
      }
    };
  }

  // Exercises one switch case per builtin type in the decoder's array path. The component-type
  // assertion catches a case that allocates the wrong array type; the equality assertion catches a
  // case that allocates the right array but calls the wrong element decoder (e.g. Int32 for Int16),
  // which the empty-array coverage in OpcUaBinaryDecoderTest cannot see.
  @ParameterizedTest
  @MethodSource("getBuiltinArrayVariants")
  public void builtinArrayVariantRoundTripPreservesValuesAndComponentType(Variant variant) {
    writer.encodeVariant(variant);
    Variant decoded = reader.decodeVariant();

    assertEquals(variant, decoded);
    assertEquals(
        requireNonNull(variant.value()).getClass().getComponentType(),
        requireNonNull(decoded.value()).getClass().getComponentType());
  }

  @Test
  @DisplayName(
      "Test that a Variant containing a null array encoded with a negative array size to indicate a"
          + " null value decodes properly.")
  public void testNullArrayEncodedWithNegativeArraySize() {
    ByteBuf buffer = Unpooled.buffer();

    buffer.writeByte(OpcUaDataType.Int16.getTypeId() | (1 << 7));
    buffer.writeIntLE(-1);

    OpcUaBinaryDecoder reader = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    reader.setBuffer(buffer);

    Variant v = reader.decodeVariant();

    assertNotNull(v);
    assertNull(v.value());
  }
}
