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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.testng.annotations.BeforeMethod;

public abstract class BinarySerializationFixture {

  ByteBuf buffer;
  OpcUaBinaryEncoder writer;
  OpcUaBinaryDecoder reader;

  @BeforeMethod
  public void setUp() {
    buffer = Unpooled.buffer();

    writer = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
    reader = new OpcUaBinaryDecoder(DefaultEncodingContext.INSTANCE).setBuffer(buffer);
  }
}
