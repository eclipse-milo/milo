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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class StringSerializationTest extends BinarySerializationFixture {

  public static Object[][] getStrings() {
    return new Object[][] {{null}, {""}, {"Hello, world!"}, {"水Boy"}};
  }

  @ParameterizedTest
  @MethodSource("getStrings")
  public void testStringRoundTrip(String value) {
    writer.encodeString(value);
    String decoded = reader.decodeString();

    assertEquals(value, decoded);
  }
}
