/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OpcUaXmlDecoderWhitespaceTest {

  // NodeSet identifiers must decode independently of indentation and comments.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- identifier -->\n  "})
  void decodesNodeId(String gap) throws Exception {
    try (var decoder =
        decoder("<NodeId>" + gap + "<Identifier>ns=1;i=5392</Identifier>" + gap + "</NodeId>")) {
      assertEquals(new NodeId(1, 5392), decoder.decodeNodeId("NodeId"));
    }
  }

  // Expanded identifiers use the same XML layout as NodeIds.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- identifier -->\n  "})
  void decodesExpandedNodeId(String gap) throws Exception {
    try (var decoder =
        decoder(
            "<ExpandedNodeId>"
                + gap
                + "<Identifier>ns=1;i=5392</Identifier>"
                + gap
                + "</ExpandedNodeId>")) {
      assertEquals(
          ExpandedNodeId.parse("ns=1;i=5392"), decoder.decodeExpandedNodeId("ExpandedNodeId"));
    }
  }

  // Keep TypeId compact to isolate Body selection from identifier decoding.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- body -->\n  "})
  void retainsExtensionObjectBody(String gap) throws Exception {
    try (var decoder =
        decoder(
            "<ExtensionObject><TypeId><Identifier>i=12082</Identifier></TypeId><Body>"
                + gap
                + "<XVType><X>1.5</X><Value>2.5</Value></XVType>"
                + gap
                + "</Body></ExtensionObject>")) {
      var value = decoder.decodeExtensionObject("ExtensionObject");
      assertEquals(new XVType(1.5, 2.5f), value.decode(DefaultEncodingContext.INSTANCE));
    }
  }

  // Leading indentation must not silently replace every structure field with a default.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- field -->\n  "})
  void decodesStructureWithExplicitCodec(String gap) throws Exception {
    try (var decoder = decoder(structure(gap))) {
      assertEquals(new XVType(1.5, 2.5f), decoder.decodeStruct("XVType", new XVType.Codec()));
    }
  }

  // Registered codecs must behave like explicitly supplied codecs.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- field -->\n  "})
  void decodesStructureWithRegisteredCodec(String gap) throws Exception {
    try (var decoder = decoder(structure(gap))) {
      assertEquals(new XVType(1.5, 2.5f), decoder.decodeStruct("XVType", NodeIds.XVType));
    }
  }

  // A compact first field isolates sibling advancement from first-child selection.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- next field -->\n  "})
  void advancesBetweenStructureFields(String gap) throws Exception {
    try (var decoder = decoder("<XVType><X>1.5</X>" + gap + "<Value>2.5</Value></XVType>")) {
      assertEquals(new XVType(1.5, 2.5f), decoder.decodeStruct("XVType", new XVType.Codec()));
    }
  }

  // Part 6 §5.3.4: whitespace between direct array members must not change the values.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- item -->\n  "})
  void decodesNodeIdArray(String gap) throws Exception {
    try (var decoder =
        decoder(
            "<Nodes>"
                + gap
                + "<NodeId>"
                + gap
                + "<Identifier>ns=1;i=5392</Identifier></NodeId>"
                + gap
                + "<NodeId><Identifier>i=6</Identifier></NodeId></Nodes>")) {
      assertArrayEquals(
          new NodeId[] {new NodeId(1, 5392), NodeIds.Int32}, decoder.decodeNodeIdArray("Nodes"));
    }
  }

  // XML payload content belongs to the caller, including whitespace between nested elements.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  ", "\n  <!-- payload -->\n  "})
  void preservesXmlPayloadWhitespace(String gap) throws Exception {
    String payload = "<Payload>  <Child/>\n  <Child/>  </Payload>";
    try (var decoder = decoder("<XmlElement>" + gap + payload + "</XmlElement>")) {
      XmlElement value = decoder.decodeXmlElement("XmlElement");
      assertEquals(payload, value.getFragment());
    }
  }

  // Advancing over formatting must leave text-only values untouched.
  @ParameterizedTest
  @ValueSource(strings = {"  value  ", "\n  ", ""})
  void preservesStringWhitespace(String text) throws Exception {
    try (var decoder = decoder("<String>" + text + "</String>")) {
      assertEquals(text, decoder.decodeString("String"));
    }
  }

  private static String structure(String gap) {
    return "<XVType>" + gap + "<X>1.5</X>" + gap + "<Value>2.5</Value>" + gap + "</XVType>";
  }

  private static OpcUaXmlDecoder decoder(String xml) throws Exception {
    return new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml);
  }
}
