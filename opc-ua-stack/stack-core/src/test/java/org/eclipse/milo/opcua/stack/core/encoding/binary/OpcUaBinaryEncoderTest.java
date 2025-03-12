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
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
