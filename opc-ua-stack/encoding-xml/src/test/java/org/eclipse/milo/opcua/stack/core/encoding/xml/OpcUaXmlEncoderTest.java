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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

class OpcUaXmlEncoderTest {

  private static final boolean DEBUG = true;

  EncodingContext context = new DefaultEncodingContext();

  @Test
  void encodeBoolean() {
    String expected =
"""
<Test>false</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeBoolean("Test", false);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeSByte() {
    String expected =
"""
<Test>-1</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeSByte("Test", (byte) -1);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeByte() {
    String expected =
"""
<Test>255</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeByte("Test", ubyte(255));

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeVariant() {
    String expected =
"""
<Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
  <uax:Value>
    <uax:Boolean>false</uax:Boolean>
  </uax:Value>
</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeVariant("Test", Variant.of(false));

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeVariantArray() {
    String expected =
"""
<Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
  <uax:Value>
    <uax:ListOfBoolean>
      <uax:Boolean>false</uax:Boolean>
      <uax:Boolean>true</uax:Boolean>
      <uax:Boolean>false</uax:Boolean>
      <uax:Boolean>true</uax:Boolean>
    </uax:ListOfBoolean>
  </uax:Value>
</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeVariant("Test", Variant.ofBooleanArray(new Boolean[] {false, true, false, true}));

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  private static void maybePrintXml(Diff diff, String expected, String actual) {
    if (diff.hasDifferences() || DEBUG) {
      System.out.printf("Expected:%n%s%n", expected);
      System.out.printf("Actual:%n%s%n", actual);
    }
  }
}
