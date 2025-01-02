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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class XmlElementSerializationTest extends BinarySerializationFixture {

  public static Object[][] getXmlElements() {
    return new Object[][] {
      {new XmlElement(null)}, {new XmlElement("<tag>hello, world</tag>")},
    };
  }

  @ParameterizedTest
  @MethodSource("getXmlElements")
  @DisplayName("XmlElement is round-trip serializable.")
  public void testXmlElementRoundTrip(XmlElement element) throws Exception {
    writer.encodeXmlElement(element);
    XmlElement decoded = reader.decodeXmlElement();

    assertEquals(element, decoded);
  }
}
