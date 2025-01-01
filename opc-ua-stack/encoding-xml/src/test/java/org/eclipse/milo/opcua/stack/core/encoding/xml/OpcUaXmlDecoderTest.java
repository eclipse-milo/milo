/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.StringReader;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.junit.jupiter.api.Test;

public class OpcUaXmlDecoderTest {
  @Test
  public void testReadVariantValue() throws Exception {
    String xml =
        "<ListOfExtensionObject xmlns=\"http://opcfoundation.org/UA/2008/02/Types.xsd\""
            + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ExtensionObject><TypeId><Identifier>i=297</Identifier>\n"
            + "         "
            + " </TypeId><Body><Argument><Name>BreakLockStatus</Name><DataType><Identifier>i=6</Identifier>\n"
            + "              </DataType><ValueRank>-1</ValueRank><ArrayDimensions/><Description"
            + " xmlns:p5=\"http://www.w3.org/2001/XMLSchema-instance\" p5:nil=\"true\"/>\n"
            + "            </Argument>\n"
            + "          </Body>\n"
            + "        </ExtensionObject>\n"
            + "      </ListOfExtensionObject>\n";

    OpcUaXmlDecoder decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE).setInput(new StringReader(xml));

    assertNotNull(decoder.readVariantValue());
  }
}
