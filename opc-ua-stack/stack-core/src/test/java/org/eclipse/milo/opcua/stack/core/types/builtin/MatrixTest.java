/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
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
  void matrixEquals() {
    assertEquals(primitiveMatrix2d, new Matrix(primitiveInt2d));
    assertEquals(boxedMatrix2d, new Matrix(boxedInt2d));
  }

  @Test
  void matrixToString() {
    assertEquals(
        "Matrix{builtinDataType=Int32, dimensions=[2, 2], flatArray=[1, 2, 3, 4]}",
        primitiveMatrix2d.toString());
    assertEquals(
        "Matrix{builtinDataType=Int32, dimensions=[2, 2], flatArray=[1, 2, 3, 4]}",
        boxedMatrix2d.toString());
  }

  @Test
  void getBuiltinDataType() {
    assertEquals(BuiltinDataType.Int32, primitiveMatrix2d.getBuiltinDataType().orElse(null));
    assertEquals(BuiltinDataType.ExtensionObject, vectorMatrix2d.getBuiltinDataType().orElse(null));
  }

  @Test
  void getDataTypeId() {
    assertEquals(
        BuiltinDataType.Int32.getNodeId().expanded(),
        primitiveMatrix2d.getDataTypeId().orElse(null));
    assertEquals(ThreeDVector.TYPE_ID, vectorMatrix2d.getDataTypeId().orElse(null));
  }
}
