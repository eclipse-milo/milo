/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MatrixTest {
  private final int[][] primitiveInt2d = {{1, 2}, {3, 4}};
  private final Integer[][] boxedInt2d = {{1, 2}, {3, 4}};

  private final ThreeDVector[][] vectors2d = {
    {new ThreeDVector(1.0, 2.0, 3.0), new ThreeDVector(4.0, 5.0, 6.0)},
    {new ThreeDVector(7.0, 8.0, 9.0), new ThreeDVector(10.0, 11.0, 12.0)}
  };

  private final Matrix primitiveMatrix2d = new Matrix(primitiveInt2d);
  private final Matrix boxedMatrix2d = new Matrix(boxedInt2d);
  private final Matrix vectorMatrix2d = new Matrix(vectors2d);

  @Test
  void transform() {
    Matrix m =
        Matrix.ofInt32(
            new int[][] {
              {0, 1},
              {2, 3}
            });

    Matrix transformed = m.transform(Object::toString);

    Matrix expected =
        Matrix.ofString(
            new String[][] {
              new String[] {"0", "1"},
              new String[] {"2", "3"}
            });

    assertEquals(expected, transformed);
  }

  @Test
  void transformEmptyMatrixWithExplicitType() {
    Matrix m = new Matrix(new Integer[0], new int[] {0, 2}, OpcUaDataType.Int32);

    Matrix transformed = m.transform(Object::toString, String.class, OpcUaDataType.String);

    assertArrayEquals(new int[] {0, 2}, transformed.getDimensions());
    assertEquals(0, Array.getLength(transformed.getElements()));
    assertEquals(String.class, transformed.getElementType().orElseThrow());
    assertEquals(OpcUaDataType.String, transformed.getDataType().orElseThrow());
    assertEquals(
        OpcUaDataType.String.getNodeId().expanded(), transformed.getDataTypeId().orElseThrow());
  }

  @Test
  void emptyStructuredMatrixCanCarryExplicitDataTypeId() {
    Matrix m =
        new Matrix(
            new ThreeDVector[0],
            new int[] {0, 2},
            OpcUaDataType.ExtensionObject,
            ThreeDVector.TYPE_ID);

    assertArrayEquals(new int[] {0, 2}, m.getDimensions());
    assertEquals(0, Array.getLength(m.getElements()));
    assertEquals(ThreeDVector.class, m.getElementType().orElseThrow());
    assertEquals(OpcUaDataType.ExtensionObject, m.getDataType().orElseThrow());
    assertEquals(ThreeDVector.TYPE_ID, m.getDataTypeId().orElseThrow());
  }

  @Test
  void emptyStructuredMatrixWithoutExplicitDataTypeIdDoesNotReadElementZero() {
    Matrix m = new Matrix(new ThreeDVector[0], new int[] {0, 2}, OpcUaDataType.ExtensionObject);

    assertArrayEquals(new int[] {0, 2}, m.getDimensions());
    assertEquals(0, Array.getLength(m.getElements()));
    assertEquals(OpcUaDataType.ExtensionObject, m.getDataType().orElseThrow());
    assertTrue(m.getDataTypeId().isEmpty());
  }

  @Test
  void emptyEnumeratedMatrixCanCarryExplicitDataTypeId() {
    Matrix m =
        new Matrix(
            new ApplicationType[0],
            new int[] {0, 2},
            OpcUaDataType.Int32,
            ApplicationType.TypeInfo.TYPE_ID);

    assertArrayEquals(new int[] {0, 2}, m.getDimensions());
    assertEquals(0, Array.getLength(m.getElements()));
    assertEquals(ApplicationType.class, m.getElementType().orElseThrow());
    assertEquals(OpcUaDataType.Int32, m.getDataType().orElseThrow());
    assertEquals(ApplicationType.TypeInfo.TYPE_ID, m.getDataTypeId().orElseThrow());
  }

  @Test
  void nestedArrayValue() {
    String[][] value = {
      new String[] {"0", "1"},
      new String[] {"2", "3"}
    };

    Matrix m = Matrix.ofString(value);

    assertTrue(Arrays.deepEquals(new String[] {"0", "1", "2", "3"}, (Object[]) m.getElements()));
    assertTrue(Arrays.deepEquals(value, (String[][]) m.nestedArrayValue()));
  }

  @Test
  void primitiveBoxedEquality() {
    int[][] primitive = new int[][] {{1, 2}, {3, 4}};
    Integer[][] boxed = new Integer[][] {{1, 2}, {3, 4}};

    assertEquals(new Matrix(primitive), new Matrix(boxed));
    assertEquals(new Matrix(boxed), new Matrix(primitive));
  }

  // Independently created values must remain interchangeable in hash collections, including
  // when wrapped in the Variant and DataValue containers used by the SDK.
  @ParameterizedTest
  @MethodSource("equalMatrixValues")
  void equalMatricesHashTheSame(Matrix m1, Matrix m2) {
    assertEquals(m1, m2);
    assertEquals(m2, m1);
    assertEquals(m1.hashCode(), m2.hashCode());

    Set<Matrix> set = new HashSet<>();
    set.add(m1);
    assertTrue(set.contains(m2));

    Map<Variant, String> variants = new HashMap<>();
    variants.put(new Variant(m1), "found");
    assertEquals("found", variants.get(new Variant(m2)));

    Set<DataValue> values = new HashSet<>();
    values.add(new DataValue(new Variant(m1), StatusCode.GOOD, null, null));
    assertTrue(values.contains(new DataValue(new Variant(m2), StatusCode.GOOD, null, null)));
  }

  private static Stream<Arguments> equalMatrixValues() {
    return Stream.of(
        Arguments.of(
            new Matrix(new int[][] {{1, 2}, {3, 4}}), new Matrix(new int[][] {{1, 2}, {3, 4}})),
        Arguments.of(Matrix.ofNull(), Matrix.ofNull()),
        Arguments.of(
            new Matrix(new int[0], new int[] {0, 2}), new Matrix(new Integer[0], new int[] {0, 2})),
        Arguments.of(
            Matrix.ofString(new String[][] {{"value", null}}),
            Matrix.ofString(new String[][] {{"value", null}})),
        Arguments.of(
            Matrix.ofByteString(new ByteString[][] {{ByteString.of(new byte[] {1, 2})}}),
            Matrix.ofByteString(new ByteString[][] {{ByteString.of(new byte[] {1, 2})}})),
        Arguments.of(
            Matrix.ofStruct(new ThreeDVector[][] {{new ThreeDVector(1.0, 2.0, 3.0)}}),
            Matrix.ofStruct(new ThreeDVector[][] {{new ThreeDVector(1.0, 2.0, 3.0)}})),
        Arguments.of(
            Matrix.ofVariant(new Variant[][] {{new Variant(Matrix.ofInt32(new int[][] {{1, 2}}))}}),
            Matrix.ofVariant(
                new Variant[][] {{new Variant(Matrix.ofInt32(new Integer[][] {{1, 2}}))}})));
  }

  // Primitive hashing must preserve the existing mixed primitive/boxed equality contract,
  // including distinct NaN representations and both signs of zero.
  @ParameterizedTest
  @MethodSource("primitiveAndBoxedArrays")
  void primitiveBoxedHashEquality(Object primitive, Object boxed) {
    Matrix primitiveMatrix = new Matrix(primitive);
    Matrix boxedMatrix = new Matrix(boxed);

    assertEquals(primitiveMatrix, boxedMatrix);
    assertEquals(boxedMatrix, primitiveMatrix);
    assertEquals(primitiveMatrix.hashCode(), boxedMatrix.hashCode());
  }

  private static Stream<Arguments> primitiveAndBoxedArrays() {
    return Stream.of(
        Arguments.of(new boolean[][] {{true, false}}, new Boolean[][] {{true, false}}),
        Arguments.of(new byte[][] {{-128, 127}}, new Byte[][] {{-128, 127}}),
        Arguments.of(new short[][] {{-32768, 32767}}, new Short[][] {{-32768, 32767}}),
        Arguments.of(
            new int[][] {{Integer.MIN_VALUE, Integer.MAX_VALUE}},
            new Integer[][] {{Integer.MIN_VALUE, Integer.MAX_VALUE}}),
        Arguments.of(
            new long[][] {{Long.MIN_VALUE, Long.MAX_VALUE}},
            new Long[][] {{Long.MIN_VALUE, Long.MAX_VALUE}}),
        Arguments.of(
            new float[][] {
              {
                Float.intBitsToFloat(0x7fc00001),
                -0.0f,
                0.0f,
                Float.NEGATIVE_INFINITY,
                Float.POSITIVE_INFINITY
              }
            },
            new Float[][] {
              {Float.NaN, -0.0f, 0.0f, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY}
            }),
        Arguments.of(
            new double[][] {
              {
                Double.longBitsToDouble(0x7ff8000000000001L),
                -0.0,
                0.0,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY
              }
            },
            new Double[][] {
              {Double.NaN, -0.0, 0.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}
            }));
  }

  // Floating-point equality distinguishes signed zero even across primitive/boxed arrays.
  @Test
  void signedZerosRemainUnequal() {
    assertNotEquals(
        Matrix.ofFloat(new float[][] {{-0.0f}}), Matrix.ofFloat(new Float[][] {{0.0f}}));
    assertNotEquals(
        Matrix.ofDouble(new double[][] {{-0.0}}), Matrix.ofDouble(new Double[][] {{0.0}}));
  }

  // Matrix does not copy the elements it is given, so two Matrices can share a backing array and
  // still describe different values.
  @Test
  void sharedElementsWithDifferentDimensionsAreNotEqual() {
    int[] elements = {1, 2, 3, 4};

    assertNotEquals(new Matrix(elements, new int[] {2, 2}), new Matrix(elements, new int[] {4, 1}));
  }

  @Test
  void sharedElementsWithDifferentDataTypesAreNotEqual() {
    int[] elements = {1, 2, 3, 4};

    assertNotEquals(
        new Matrix(elements, new int[] {2, 2}, OpcUaDataType.Int32),
        new Matrix(elements, new int[] {2, 2}, OpcUaDataType.UInt32));
  }

  @Test
  void matrixToString() {
    assertEquals(
        "Matrix{dataType=Int32, " + "dataTypeId=i=6, dimensions=[2, 2], flatArray=[1, 2, 3, 4]}",
        primitiveMatrix2d.toString());
    assertEquals(
        "Matrix{dataType=Int32, " + "dataTypeId=i=6, dimensions=[2, 2], flatArray=[1, 2, 3, 4]}",
        boxedMatrix2d.toString());
  }

  @Test
  void nullMatrixToString() {
    // A null Matrix has no flatArray; toString() must not throw (previously NPE'd in
    // ArrayUtil.getType).
    assertEquals(
        "Matrix{dataType=null, dataTypeId=null, dimensions=[], flatArray=null}",
        Matrix.ofNull().toString());
  }

  @Test
  void getDataType() {
    assertEquals(OpcUaDataType.Int32, primitiveMatrix2d.getDataType().orElse(null));
    assertEquals(OpcUaDataType.ExtensionObject, vectorMatrix2d.getDataType().orElse(null));
  }

  @Test
  void getDataTypeId() {
    assertEquals(
        OpcUaDataType.Int32.getNodeId().expanded(), primitiveMatrix2d.getDataTypeId().orElse(null));
    assertEquals(ThreeDVector.TYPE_ID, vectorMatrix2d.getDataTypeId().orElse(null));
  }

  @Test
  void getElementType() {
    assertEquals(Integer.class, boxedMatrix2d.getElementType().orElseThrow());
    assertEquals(int.class, primitiveMatrix2d.getElementType().orElseThrow());
    assertEquals(ThreeDVector.class, vectorMatrix2d.getElementType().orElseThrow());

    Matrix nullMatrix = Matrix.ofNull();
    assertTrue(nullMatrix.getElementType().isEmpty());
  }
}
