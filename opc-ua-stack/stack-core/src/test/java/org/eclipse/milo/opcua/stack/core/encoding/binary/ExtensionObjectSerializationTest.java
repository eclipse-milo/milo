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

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExtensionObjectSerializationTest extends BinarySerializationFixture {

  @DataProvider
  public Object[][] getExtensionObjects() {
    return new Object[][] {
      {new ExtensionObject(ByteString.of(new byte[] {1, 2, 3, 4}), new NodeId(1, 2))},
      {new ExtensionObject(XmlElement.of("<a>hello</a>"), new NodeId(1, 2))},
    };
  }

  @Test(
      dataProvider = "getExtensionObjects",
      description = "ExtensionObject is round-trip serializable.")
  public void testExtensionObjectRoundTrip(ExtensionObject xo) throws Exception {
    writer.encodeExtensionObject(xo);
    ExtensionObject decoded = reader.decodeExtensionObject();

    assertEquals(decoded, xo);
  }
}
