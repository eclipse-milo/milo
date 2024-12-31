/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.eclipse.milo.opcua.sdk.server.events.conversions.ImplicitConversions.convert;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.testng.Assert.assertEquals;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.testng.annotations.Test;

public class ImplicitConversionsTest {

  @Test
  public void testConvert() {
    assertEquals(convert(false, BuiltinDataType.Byte), ubyte(0));
    assertEquals(convert(true, BuiltinDataType.Byte), ubyte(1));
  }
}
