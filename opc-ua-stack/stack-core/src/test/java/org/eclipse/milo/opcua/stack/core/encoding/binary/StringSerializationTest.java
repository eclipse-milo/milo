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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StringSerializationTest extends BinarySerializationFixture {

  @DataProvider(name = "StringProvider")
  public Object[][] getStrings() {
    return new Object[][] {{null}, {""}, {"Hello, world!"}, {"水Boy"}};
  }

  @Test(dataProvider = "StringProvider")
  public void testStringRoundTrip(String value) {
    writer.encodeString(value);
    String decoded = reader.decodeString();

    assertEquals(decoded, value);
  }
}
