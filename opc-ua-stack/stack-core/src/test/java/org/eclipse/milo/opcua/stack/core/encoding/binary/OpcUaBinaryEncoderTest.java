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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OpcUaBinaryEncoderTest {

  ByteBuf buffer;
  OpcUaBinaryEncoder encoder;

  @BeforeEach
  public void initializeTest() {
    buffer = Unpooled.buffer();
    encoder = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
  }

  @Test
  public void encodeBit() throws Exception {
    {
      encoder.encodeBit(1);
      encoder.encodeBit(1);
      encoder.encodeBit(1);
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      encoder.encodeBit(0);
      encoder.encodeBit(0);
      encoder.encodeBit(0);
      assertEquals(0b00001111, buffer.readUnsignedByte());
    }
    {
      encoder.encodeBit(0);
      encoder.encodeBit(0);
      encoder.encodeBit(0);
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      encoder.encodeBit(1);
      encoder.encodeBit(1);
      encoder.encodeBit(1);
      assertEquals(0b11110000, buffer.readUnsignedByte());
    }
    {
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      assertEquals(0b10101010, buffer.readUnsignedByte());
    }
    {
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      encoder.encodeBit(1);
      encoder.encodeBit(0);
      assertEquals(0b01010101, buffer.readUnsignedByte());
    }
  }

  @Test
  public void encodeOptionSet() {
    AccessLevelType accessLevelType =
        AccessLevelType.of(AccessLevelType.Field.CurrentRead, AccessLevelType.Field.CurrentWrite);

    encoder.encodeVariant(new Variant(accessLevelType));

    assertEquals(accessLevelType.getValue(), ubyte(buffer.readUnsignedByte()));
  }

  @Test
  void encodeMatrix() {
    Matrix matrix = Matrix.ofInt32(new Integer[][] {{1, 2}, {3, 4}});

    encoder.encodeMatrix(null, matrix);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Matrix decodedMatrix = decoder.decodeMatrix(null, OpcUaDataType.Int32);

    assertEquals(matrix, decodedMatrix);
  }

  @Test
  void encodeMatrixOfUaStructuredType() {
    XVType[][] xvTypes = {
      {new XVType(1.0, 2.0f), new XVType(3.0, 4.0f)},
      {new XVType(5.0, 6.0f), new XVType(7.0, 8.0f)}
    };

    Matrix matrix = Matrix.ofStruct(xvTypes);
    encoder.encodeMatrix(null, matrix);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Matrix decodedMatrix = decoder.decodeMatrix(null, OpcUaDataType.ExtensionObject);

    assertEquals(
        matrix,
        decodedMatrix.transform(
            v -> ((ExtensionObject) v).decode(DefaultEncodingContext.INSTANCE)));
  }

  @Test
  void encodeMatrixOfUaEnumeratedType() {
    ApplicationType[][] applicationTypes = {
      {ApplicationType.Server, ApplicationType.Client},
      {ApplicationType.ClientAndServer, ApplicationType.DiscoveryServer}
    };

    Matrix matrix = Matrix.ofEnum(applicationTypes);
    encoder.encodeMatrix(null, matrix);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Matrix decodedMatrix = decoder.decodeMatrix(null, OpcUaDataType.Int32);

    assertEquals(matrix, decodedMatrix.transform(v -> ApplicationType.from((Integer) v)));
  }

  // Generic codecs (JsonStructCodec, the DTD codecs) build matrix elements as Object[]. Object[] is
  // not assignable to UaEnumeratedType[], so the encoder must not assume the concrete array type.
  @Test
  void encodeEnumMatrixBackedByObjectArray() {
    Matrix matrix =
        new Matrix(
            new Object[] {ApplicationType.Server, ApplicationType.Client},
            new int[] {1, 2},
            OpcUaDataType.Int32);

    encoder.encodeEnumMatrix(null, matrix);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Matrix decodedMatrix = decoder.decodeMatrix(null, OpcUaDataType.Int32);

    assertArrayEquals(
        new Integer[] {ApplicationType.Server.getValue(), ApplicationType.Client.getValue()},
        (Integer[]) decodedMatrix.getElements());
  }

  // As above, for UaStructuredType[].
  @Test
  void encodeStructMatrixBackedByObjectArray() throws Exception {
    XVType xv1 = new XVType(1.0, 2.0f);
    XVType xv2 = new XVType(3.0, 4.0f);

    Matrix matrix =
        new Matrix(new Object[] {xv1, xv2}, new int[] {1, 2}, OpcUaDataType.ExtensionObject);

    NodeId dataTypeId =
        XVType.TYPE_ID.toNodeIdOrThrow(DefaultEncodingContext.INSTANCE.getNamespaceTable());

    encoder.encodeStructMatrix(null, matrix, dataTypeId);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Matrix decodedMatrix = decoder.decodeStructMatrix(null, dataTypeId);

    assertArrayEquals(new XVType[] {xv1, xv2}, (XVType[]) decodedMatrix.getElements());
  }

  // An OptionSet's builtin type must be derived from the element class, not by casting the value
  // itself: for an array or a Matrix the value is the container, not an OptionSetUInteger.
  @Test
  void encodeVariantOfOptionSetArray() {
    AccessLevelType[] values = {
      AccessLevelType.of(AccessLevelType.Field.CurrentRead),
      AccessLevelType.of(AccessLevelType.Field.CurrentWrite)
    };

    encoder.encodeVariant(new Variant(values));

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Variant decoded = decoder.decodeVariant();

    // OptionSets go on the wire as their backing UInteger, so they come back as UByte here.
    assertArrayEquals(
        new UByte[] {(UByte) values[0].getValue(), (UByte) values[1].getValue()},
        (UByte[]) decoded.value());
  }

  // As above, reached through the Matrix branch of encodeVariant.
  @Test
  void encodeVariantOfOptionSetMatrix() {
    AccessLevelType[][] values = {
      {
        AccessLevelType.of(AccessLevelType.Field.CurrentRead),
        AccessLevelType.of(AccessLevelType.Field.CurrentWrite)
      }
    };

    encoder.encodeVariant(new Variant(Matrix.ofOptionSetUI(values)));

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Variant decoded = decoder.decodeVariant();

    Matrix decodedMatrix = (Matrix) decoded.value();
    assertArrayEquals(new int[] {1, 2}, decodedMatrix.getDimensions());
    assertArrayEquals(
        new UByte[] {(UByte) values[0][0].getValue(), (UByte) values[0][1].getValue()},
        (UByte[]) decodedMatrix.getElements());
  }

  // A Variant holding something that is not a builtin type cannot be encoded. Report it rather than
  // writing a bogus encoding mask that the peer will reject as a framing error.
  @Test
  void encodeVariantOfNonBuiltinTypeReportsEncodingError() {
    UaSerializationException ex =
        assertThrows(
            UaSerializationException.class, () -> encoder.encodeVariant(new Variant(new Object())));

    assertEquals(new StatusCode(StatusCodes.Bad_EncodingError), ex.getStatusCode());
  }

  @Test
  void encodeMatrixOfOptionSetUInteger() {
    AccessLevelType[][] accessLevelTypes = {
      {
        AccessLevelType.of(AccessLevelType.Field.CurrentRead),
        AccessLevelType.of(AccessLevelType.Field.CurrentWrite)
      },
      {
        AccessLevelType.of(AccessLevelType.Field.HistoryRead),
        AccessLevelType.of(AccessLevelType.Field.HistoryWrite)
      },
    };

    Matrix matrix = Matrix.ofOptionSetUI(accessLevelTypes);
    encoder.encodeMatrix(null, matrix);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Matrix decodedMatrix = decoder.decodeMatrix(null, OpcUaDataType.Byte);

    assertEquals(matrix, decodedMatrix.transform(v -> new AccessLevelType((UByte) v)));
  }

  @Test
  void encodeVariantOfEnumMatrix() {
    ApplicationType[][] applicationTypes = {
      {ApplicationType.Server, ApplicationType.Client},
      {ApplicationType.ClientAndServer, ApplicationType.DiscoveryServer}
    };

    Matrix matrix = Matrix.ofEnum(applicationTypes);
    Variant variant = Variant.ofMatrix(matrix);
    encoder.encodeVariant(variant);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Variant decodedVariant = decoder.decodeVariant();
    Matrix decodedMatrix = (Matrix) decodedVariant.value();
    assert decodedMatrix != null;

    assertEquals(matrix, decodedMatrix.transform(v -> ApplicationType.from((Integer) v)));
  }

  @Test
  void encodeVariantOfStructMatrix() {
    XVType[][] xvTypes = {
      {new XVType(1.0, 2.0f), new XVType(3.0, 4.0f)},
      {new XVType(5.0, 6.0f), new XVType(7.0, 8.0f)}
    };

    Matrix matrix = Matrix.ofStruct(xvTypes);
    Variant variant = Variant.ofMatrix(matrix);
    encoder.encodeVariant(variant);

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Variant decodedVariant = decoder.decodeVariant();
    Matrix decodedMatrix = (Matrix) decodedVariant.value();
    assert decodedMatrix != null;

    assertEquals(
        matrix,
        decodedMatrix.transform(
            v -> ((ExtensionObject) v).decode(DefaultEncodingContext.INSTANCE)));
  }

  static Stream<Arguments> matrixVariantEncodings() {
    return Stream.of(
        // Empty Matrices: mask has only the array bit, ArrayLength is 0, no ArrayDimensions.
        Arguments.of(
            "empty structure Matrix, zero first dimension",
            new Matrix(new XVType[0], new int[] {0, 2}),
            "9600000000"),
        Arguments.of(
            "empty Int32 Matrix, zero last dimension",
            new Matrix(new Integer[0], new int[] {2, 0}),
            "8600000000"),
        Arguments.of(
            "empty primitive Int32 Matrix, zero middle dimension",
            new Matrix(new int[0], new int[] {2, 0, 3}),
            "8600000000"),
        Arguments.of(
            "empty String Matrix, negative dimension",
            new Matrix(new String[0], new int[] {-1, 2}),
            "8c00000000"),
        Arguments.of(
            "empty enumeration Matrix encodes as Int32",
            new Matrix(new ApplicationType[0], new int[] {0, 1}),
            "8600000000"),
        Arguments.of(
            "empty option set Matrix encodes as Byte",
            new Matrix(new AccessLevelType[0], new int[] {2, -3}),
            "8300000000"),
        // Nonempty Matrices keep the dimensions bit and the ArrayDimensions field.
        Arguments.of(
            "2x2 Int32 Matrix keeps dimensions",
            Matrix.ofInt32(new Integer[][] {{1, 2}, {3, 4}}),
            "c6"
                + "04000000"
                + "01000000020000000300000004000000"
                + "02000000"
                + "0200000002000000"),
        Arguments.of(
            "1x2 Int32 Matrix keeps a dimension of length 1",
            Matrix.ofInt32(new Integer[][] {{1, 2}}),
            "c6" + "02000000" + "0100000002000000" + "02000000" + "0100000002000000"));
  }

  // OPC 10000-6, 5.2.2.16, Table 26: ArrayDimensions is present only when there are at least two
  // dimensions and every dimension is greater than 0, and ArrayLength is 0 when any dimension is
  // not. A Matrix with a zero or negative dimension must therefore encode as a plain empty array of
  // its element type, without the dimensions bit or the ArrayDimensions field.
  //
  // A Matrix with fewer than two dimensions cannot be constructed while assertions are enabled, so
  // the rank half of the condition is not covered here or in the rejection test below.
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixVariantEncodings")
  void encodeVariantOfMatrixWritesDimensionsOnlyWhenAllArePositive(
      String description, Matrix matrix, String expectedHex) {
    encoder.encodeVariant(new Variant(matrix));

    assertEquals(expectedHex, ByteBufUtil.hexDump(buffer), description);
  }

  // The empty-array form carries no dimensions, so a peer (and this decoder) sees a
  // one-dimensional empty array of the element type rather than a Matrix.
  // The Matrix constructors only assert consistency, so a Matrix can carry elements alongside a
  // dimension of 0. The empty-array form would silently drop those elements, and no other wire
  // form can represent the value; reject it instead.
  @Test
  void encodeVariantOfMatrixWithElementsAndNonpositiveDimensionReportsEncodingError() {
    Matrix matrix = new Matrix(new Integer[] {1, 2}, new int[] {0, 2});

    UaSerializationException ex =
        assertThrows(
            UaSerializationException.class, () -> encoder.encodeVariant(new Variant(matrix)));

    assertEquals(new StatusCode(StatusCodes.Bad_EncodingError), ex.getStatusCode());
    assertEquals(0, buffer.readableBytes(), "nothing is written for a rejected Matrix");
  }

  @Test
  void encodeVariantOfEmptyMatrixDecodesAsEmptyArray() {
    encoder.encodeVariant(new Variant(new Matrix(new XVType[0], new int[] {0, 2})));

    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    Variant decoded = decoder.decodeVariant();

    assertArrayEquals(new ExtensionObject[0], (ExtensionObject[]) decoded.value());
    assertEquals(0, buffer.readableBytes(), "decoder consumed the whole encoding");
  }

  @Test
  void dataValueEncodingMaskValueBit() {
    DataValue dataValue = new DataValue(Variant.ofBoolean(true), StatusCode.GOOD, null);
    encoder.encodeDataValue(dataValue);

    // get the EncodingMask byte out of the buffer and ensure only the Value bit is set
    byte encodingMask = buffer.getByte(buffer.readerIndex());
    assertEquals(0b00000001, encodingMask);

    // decode the DataValue and ensure we decoded the value as true
    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    DataValue decodedDataValue = decoder.decodeDataValue();

    assertEquals(Variant.ofBoolean(true), decodedDataValue.value());
  }

  @Test
  void dataValueEncodingMaskNullValueBit() {
    DataValue dataValue = new DataValue(Variant.ofNull(), StatusCode.GOOD, null);
    encoder.encodeDataValue(dataValue);

    // get the EncodingMask byte out of the buffer and ensure no bits are set
    byte encodingMask = buffer.getByte(buffer.readerIndex());
    assertEquals(0b00000000, encodingMask);

    // decode the DataValue and ensure we decoded the value as null
    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    DataValue decodedDataValue = decoder.decodeDataValue();

    assertTrue(decodedDataValue.value().isNull());
  }

  @Test
  void dataValueEncodingMaskStatusCodeBit() {
    DataValue dataValue = new DataValue(Variant.ofBoolean(true), StatusCode.GOOD, null);
    encoder.encodeDataValue(dataValue);

    // get the EncodingMask byte out of the buffer and ensure only the Value bit is set
    byte encodingMask = buffer.getByte(buffer.readerIndex());
    assertEquals(0b00000001, encodingMask);

    // decode the DataValue and ensure we decoded the StatusCode as GOOD
    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    DataValue decodedDataValue = decoder.decodeDataValue();

    assertEquals(StatusCode.GOOD, decodedDataValue.statusCode());
  }

  @Test
  void dataValueEncodingMaskStatusCodeGoodOverloadBit() {
    DataValue dataValue =
        new DataValue(Variant.ofBoolean(true), new StatusCode(StatusCodes.Good_Overload), null);
    encoder.encodeDataValue(dataValue);

    // get the EncodingMask byte out of the buffer and ensure both the Value and StatusCode bits are
    // set
    byte encodingMask = buffer.getByte(buffer.readerIndex());
    assertEquals(0b00000011, encodingMask);

    // decode the DataValue and ensure we decoded the same StatusCode
    var decoder = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    DataValue decodedDataValue = decoder.decodeDataValue();

    assertEquals(new StatusCode(StatusCodes.Good_Overload), decodedDataValue.statusCode());
  }
}
