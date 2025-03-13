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

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ExtensionObjectSerializationTest extends BinarySerializationFixture {

  public static Object[][] getExtensionObjects() {
    return new Object[][] {
      {ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3, 4}), new NodeId(1, 2))},
      {ExtensionObject.of(XmlElement.of("<a>hello</a>"), new NodeId(1, 2))},
    };
  }

  @ParameterizedTest
  @MethodSource("getExtensionObjects")
  @DisplayName("ExtensionObject is round-trip serializable.")
  public void testExtensionObjectRoundTrip(ExtensionObject xo) throws Exception {
    writer.encodeExtensionObject(xo);
    ExtensionObject decoded = reader.decodeExtensionObject();

    assertEquals(xo, decoded);
  }
}
