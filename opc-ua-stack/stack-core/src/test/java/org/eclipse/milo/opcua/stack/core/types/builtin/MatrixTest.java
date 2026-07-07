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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.junit.jupiter.api.Test;

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
