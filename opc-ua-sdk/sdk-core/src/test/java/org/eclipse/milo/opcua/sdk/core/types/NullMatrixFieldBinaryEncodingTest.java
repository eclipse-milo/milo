/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.digitalpetri.opcua.test.types.StructWithBuiltinMatrixFieldsEx;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.types.util.StaticEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NullMatrixFieldBinaryEncodingTest {

  /** StructWithBuiltinMatrixFieldsEx has 34 Matrix fields and nothing else. */
  private static final int MATRIX_FIELD_COUNT = 34;

  /** OPC 10000-6, 5.2.5: a null array is encoded as a length of -1. */
  private static final String ALL_FIELDS_NULL = "ffffffff".repeat(MATRIX_FIELD_COUNT);

  private final StaticEncodingContext context = new StaticEncodingContext();

  static Stream<Arguments> structsWithOneNullField() {
    return Stream.of(
        Arguments.of(
            "builtin field, encodeMatrix",
            (Function<Matrix, StructWithBuiltinMatrixFieldsEx>)
                v -> struct(v, Matrix.ofNull(), Matrix.ofNull())),
        Arguments.of(
            "enumeration field, encodeEnumMatrix",
            (Function<Matrix, StructWithBuiltinMatrixFieldsEx>)
                v -> struct(Matrix.ofNull(), v, Matrix.ofNull())),
        Arguments.of(
            "structure field, encodeStructMatrix",
            (Function<Matrix, StructWithBuiltinMatrixFieldsEx>)
                v -> struct(Matrix.ofNull(), Matrix.ofNull(), v)));
  }

  // The generated codec passes each @Nullable Matrix field straight to the encoder, so a field
  // left null by application code has to encode as a null Matrix, the same as Matrix.ofNull().
  @ParameterizedTest(name = "{0}")
  @MethodSource("structsWithOneNullField")
  void nullMatrixFieldEncodesAsNullMatrix(
      String description, Function<Matrix, StructWithBuiltinMatrixFieldsEx> withField) {
    assertEquals(
        ALL_FIELDS_NULL, encode(withField.apply(Matrix.ofNull())), "Matrix.ofNull() baseline");

    assertEquals(ALL_FIELDS_NULL, encode(withField.apply(null)), description);
  }

  private String encode(StructWithBuiltinMatrixFieldsEx struct) {
    ByteBuf buffer = Unpooled.buffer();
    try {
      new OpcUaBinaryEncoder(context)
          .setBuffer(buffer)
          .encodeStruct("Value", struct, StructWithBuiltinMatrixFieldsEx.TYPE_ID);
      return ByteBufUtil.hexDump(buffer);
    } finally {
      buffer.release();
    }
  }

  /**
   * Every field is {@link Matrix#ofNull()} except the Boolean, ApplicationType, and XVType ones.
   */
  private static StructWithBuiltinMatrixFieldsEx struct(
      Matrix booleanField, Matrix applicationType, Matrix xvType) {
    Matrix n = Matrix.ofNull();
    return new StructWithBuiltinMatrixFieldsEx(
        booleanField,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        n,
        applicationType,
        n,
        xvType,
        n,
        n,
        n,
        n,
        n,
        n,
        n);
  }
}
