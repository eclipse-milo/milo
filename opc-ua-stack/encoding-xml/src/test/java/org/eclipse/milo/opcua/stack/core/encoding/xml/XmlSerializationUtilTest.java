/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class XmlSerializationUtilTest {

  @ParameterizedTest
  @MethodSource("encodeXmlNameTestCases")
  void encodeXmlName(String name, String expected) {
    assertEquals(expected, XmlSerializationUtil.encodeXmlName(name));
  }

  static Stream<Arguments> encodeXmlNameTestCases() {
    return Stream.of(
        Arguments.of("Hello", "Hello"),
        // Starts with allowed char '_', but not a <letter>
        Arguments.of("_Hello", "__Hello"),
        // Starts with Digit
        Arguments.of("3DHello", "_3DHello"),
        // Starts with Unicode Letter
        Arguments.of("冷水", "冷水"),
        // Starts with Digit, contains spaces, parentheses, CJK period
        Arguments.of("3 (冷水。)-Hello", "_3__冷水__-Hello"),
        // Starts with xml (lowercase)
        Arguments.of("xmlStarts", "_xmlStarts"),
        // Starts with Xml (mixed case)
        Arguments.of("XmlStarts", "_XmlStarts"),
        // Starts with XML (uppercase)
        Arguments.of("XMLStarts", "_XMLStarts"),
        // Contains allowed special chars
        Arguments.of("a-b.c_d", "a-b.c_d"),
        // Contains spaces
        Arguments.of(" space ", "__space_"),
        // Invalid chars
        Arguments.of("???", "____"),
        // Starts with allowed char '-', but not a <letter>
        Arguments.of("-HyphenStart", "_-HyphenStart"));
  }
}
