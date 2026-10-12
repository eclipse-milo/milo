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
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.digitalpetri.opcua.test.types.StructWithBuiltinMatrixFields;
import com.digitalpetri.opcua.test.types.StructWithBuiltinMatrixFieldsEx;
import java.util.Map;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.types.util.DynamicEncodingContext;
import org.eclipse.milo.opcua.sdk.core.types.util.StaticEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Binary round trips of structures whose Matrix fields are all {@link Matrix#ofNull()}.
 *
 * <p>Part 6 writes each null Matrix field as a Dimensions array of length -1. The decoders must
 * read that back as {@link Matrix#ofNull()}: generated codecs transform enum and option-set Matrix
 * fields without a null check, and a structure holding Java {@code null} cannot be encoded again.
 */
class NullMatrixFieldBinaryRoundTripTest {

  private static final Matrix N = Matrix.ofNull();

  private final StaticEncodingContext staticEncodingContext = new StaticEncodingContext();
  private final DynamicEncodingContext dynamicEncodingContext = new DynamicEncodingContext();

  static Stream<UaStructuredType> structsWithNullMatrixFields() {
    return Stream.of(
        // 23 built-in Matrix fields
        new StructWithBuiltinMatrixFields(
            N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N),
        // the same 23, then Duration, two enums, four structures, and four option sets
        new StructWithBuiltinMatrixFieldsEx(
            N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N,
            N, N, N, N, N));
  }

  // A generated codec must give back a value equal to the one that was encoded, and that value
  // must encode again. Enum and option-set fields exercise transform on the decoded Matrix.
  @ParameterizedTest
  @MethodSource("structsWithNullMatrixFields")
  void generatedCodecRoundTripsNullMatrixFields(UaStructuredType struct) {
    ExtensionObject encoded = ExtensionObject.encode(staticEncodingContext, struct);

    UaStructuredType decoded = (UaStructuredType) encoded.decode(staticEncodingContext);

    assertEquals(struct, decoded);
    assertEquals(encoded, ExtensionObject.encode(staticEncodingContext, decoded));
  }

  // DynamicStructCodec transforms enum Matrix fields the same way a generated codec does, and
  // the dynamic value must hold Matrix.ofNull() members so that it can be encoded again.
  @Test
  void dynamicCodecRoundTripsNullMatrixFields() {
    UaStructuredType struct =
        new StructWithBuiltinMatrixFieldsEx(
            N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N, N,
            N, N, N, N, N);
    ExtensionObject encoded = ExtensionObject.encode(staticEncodingContext, struct);

    DynamicStructType decoded = (DynamicStructType) encoded.decode(dynamicEncodingContext);

    Map<String, Object> members = decoded.getMembers();
    assertFalse(members.isEmpty());
    members.forEach((name, value) -> assertEquals(Matrix.ofNull(), value, name));
    assertEquals(encoded, ExtensionObject.encode(dynamicEncodingContext, decoded));
  }
}
