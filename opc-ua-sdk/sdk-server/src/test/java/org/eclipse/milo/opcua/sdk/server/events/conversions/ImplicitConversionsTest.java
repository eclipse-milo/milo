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
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.junit.jupiter.api.Test;

public class ImplicitConversionsTest {

  @Test
  public void testConvert() {
    assertEquals(ubyte(0), convert(false, OpcUaDataType.Byte));
    assertEquals(ubyte(1), convert(true, OpcUaDataType.Byte));
  }
}
