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

import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XmlElementSerializationTest extends BinarySerializationFixture {

  @DataProvider(name = "XmlElementProvider")
  public Object[][] getXmlElements() {
    return new Object[][] {
      {new XmlElement(null)}, {new XmlElement("<tag>hello, world</tag>")},
    };
  }

  @Test(dataProvider = "XmlElementProvider", description = "XmlElement is round-trip serializable.")
  public void testXmlElementRoundTrip(XmlElement element) throws Exception {
    writer.encodeXmlElement(element);
    XmlElement decoded = reader.decodeXmlElement();

    assertEquals(decoded, element);
  }
}
