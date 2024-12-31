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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.testng.Assert.assertEquals;

import io.netty.buffer.ByteBuf;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataValueSerializationTest {

  private final OpcUaBinaryEncoder encoder =
      new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE);
  private final OpcUaBinaryDecoder decoder =
      new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE);

  @Test(dataProvider = "getValues")
  public void testDataValueRoundTrip(DataValue value) {
    ByteBuf buffer = BufferUtil.pooledBuffer();
    encoder.setBuffer(buffer);
    encoder.encodeDataValue(value);

    decoder.setBuffer(buffer);
    DataValue decodedValue = decoder.decodeDataValue();

    assertEquals(decodedValue, value);
  }

  @DataProvider
  public Object[][] getValues() {
    return new Object[][] {
      {
        new DataValue(
            Variant.NULL_VALUE,
            StatusCode.GOOD,
            DateTime.now(),
            ushort(1),
            DateTime.now(),
            ushort(1))
      },
      {
        new DataValue(
            Variant.NULL_VALUE, StatusCode.GOOD, DateTime.now(), null, DateTime.now(), ushort(1))
      },
      {
        new DataValue(
            Variant.NULL_VALUE, StatusCode.GOOD, DateTime.now(), ushort(1), DateTime.now(), null)
      },
      {new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, DateTime.now(), DateTime.now())},
      {new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, DateTime.MIN_VALUE, DateTime.now())},
      {new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, DateTime.now(), DateTime.MIN_VALUE)},
      {new DataValue(new Variant(1), StatusCode.GOOD, DateTime.now(), DateTime.MIN_VALUE)},
      {new DataValue(new Variant(1), StatusCode.BAD, DateTime.now(), DateTime.MIN_VALUE)},
      {new DataValue(Variant.NULL_VALUE, StatusCode.BAD, DateTime.now(), DateTime.MIN_VALUE)}
    };
  }
}
