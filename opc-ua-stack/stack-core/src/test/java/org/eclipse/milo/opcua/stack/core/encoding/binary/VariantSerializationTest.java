/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VariantSerializationTest extends BinarySerializationFixture {

  @DataProvider(name = "VariantProvider")
  public Object[][] getVariants() {
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

  @Test(dataProvider = "VariantProvider")
  public void testVariantRoundTrip(Variant variant) {
    writer.encodeVariant(variant);
    Variant decoded = reader.decodeVariant();

    assertEquals(decoded, variant);
  }

  @Test
  public void testVariant_UaStructure() {
    ServiceCounterDataType sc1 = new ServiceCounterDataType(uint(1), uint(2));

    Variant v = new Variant(sc1);
    writer.encodeVariant(v);
    Variant decoded = reader.decodeVariant();

    ExtensionObject extensionObject = (ExtensionObject) decoded.getValue();
    ServiceCounterDataType sc2 =
        (ServiceCounterDataType) extensionObject.decode(DefaultEncodingContext.INSTANCE);

    Assert.assertEquals(sc1.getTotalCount(), sc2.getTotalCount());
    Assert.assertEquals(sc1.getErrorCount(), sc2.getErrorCount());
  }

  @DataProvider(name = "PrimitiveArrayVariantProvider")
  public Object[][] getPrimitiveArrayVariants() {
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

  @Test(
      dataProvider = "PrimitiveArrayVariantProvider",
      description =
          "Test that after primitive array types given to variants come out as expected after"
              + " encoding/decoding.")
  public void testPrimitiveArrayVariantRoundTrip(Variant variant, Variant expected) {
    writer.encodeVariant(variant);
    Variant decoded = reader.decodeVariant();

    assertEquals(decoded, expected);
  }

  @Test(
      description =
          "Test that a Variant containing a null array encoded with a negative array size to"
              + " indicate a null value decodes properly.")
  public void testNullArrayEncodedWithNegativeArraySize() {
    ByteBuf buffer = Unpooled.buffer();

    buffer.writeByte(BuiltinDataType.Int16.getTypeId() | (1 << 7));
    buffer.writeIntLE(-1);

    OpcUaBinaryDecoder reader = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);
    reader.setBuffer(buffer);

    Variant v = reader.decodeVariant();

    assertNotNull(v);
    assertNull(v.getValue());
  }
}
