/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NullMatrixSerializationTest {

  private static final String NULL_ARRAY_LENGTH = "ffffffff";

  ByteBuf buffer;
  OpcUaBinaryEncoder encoder;

  @BeforeEach
  void initializeTest() {
    buffer = Unpooled.buffer();
    encoder = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
  }

  static Stream<Arguments> matrixEncoders() {
    return Stream.of(
        Arguments.of(
            "encodeMatrix",
            (BiConsumer<OpcUaBinaryEncoder, Matrix>) (e, m) -> e.encodeMatrix("Values", m)),
        Arguments.of(
            "encodeEnumMatrix",
            (BiConsumer<OpcUaBinaryEncoder, Matrix>) (e, m) -> e.encodeEnumMatrix("Values", m)),
        Arguments.of(
            "encodeStructMatrix with NodeId",
            (BiConsumer<OpcUaBinaryEncoder, Matrix>)
                (e, m) -> e.encodeStructMatrix("Values", m, NodeIds.XVType)),
        Arguments.of(
            "encodeStructMatrix with ExpandedNodeId",
            (BiConsumer<OpcUaBinaryEncoder, Matrix>)
                (e, m) -> e.encodeStructMatrix("Values", m, XVType.TYPE_ID)));
  }

  // OPC 10000-6, 5.2.5: a null array is encoded as a length of -1. Generated codecs pass their
  // @Nullable Matrix fields straight to the encoder, so a Java null must take the same wire form
  // as Matrix.ofNull() instead of throwing.
  @ParameterizedTest(name = "{0}")
  @MethodSource("matrixEncoders")
  void javaNullWritesNullArrayLength(
      String description, BiConsumer<OpcUaBinaryEncoder, Matrix> encode) {
    encode.accept(encoder, Matrix.ofNull());
    assertEquals(NULL_ARRAY_LENGTH, ByteBufUtil.hexDump(buffer), "Matrix.ofNull() baseline");
    buffer.clear();

    encode.accept(encoder, null);

    assertEquals(NULL_ARRAY_LENGTH, ByteBufUtil.hexDump(buffer), description);
  }
}
