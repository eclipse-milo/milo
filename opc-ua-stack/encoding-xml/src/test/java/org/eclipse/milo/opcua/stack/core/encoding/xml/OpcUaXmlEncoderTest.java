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

import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
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
  void encodeBooleanArray() {
    String expected =
"""
<Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
  <uax:Boolean>false</uax:Boolean>
  <uax:Boolean>true</uax:Boolean>
  <uax:Boolean>false</uax:Boolean>
  <uax:Boolean>true</uax:Boolean>
</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeBooleanArray("Test", new Boolean[] {false, true, false, true});

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeBooleanArrayEmpty() {
    String expected =
"""
<Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeBooleanArray("Test", new Boolean[] {});

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeBooleanArrayNull() {
    String expected =
"""
<Test xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeBooleanArray("Test", null);

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
  void encodeInt16() {
    String expected =
"""
<Test>32767</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt16("Test", (short) 32767);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeInt32() {
    String expected =
"""
<Test>2147483647</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt32("Test", 2147483647);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeInt64() {
    String expected =
"""
<Test>9223372036854775807</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt64("Test", 9223372036854775807L);

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
    encoder.encodeByte("Test", UByte.MAX);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeUInt16() {
    String expected =
"""
<Test>65535</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt16("Test", UShort.MAX);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeUInt32() {
    String expected =
"""
<Test>4294967295</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt32("Test", UInteger.MAX);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeUInt64() {
    String expected =
"""
<Test>18446744073709551615</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt64("Test", ULong.MAX);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeFloat() {
    String expected =
"""
<Test>3.14</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeFloat("Test", 3.14f);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeDouble() {
    String expected =
"""
<Test>3.14159265359</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDouble("Test", 3.14159265359);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeString() {
    String expected =
"""
<Test>Hello, World!</Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeString("Test", "Hello, World!");

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeStringEmpty() {
    String expected =
"""
<Test></Test>
""";

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeString("Test", "");

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @Test
  void encodeStringNull() {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeString("Test", null);

    String actual = encoder.getDocumentXml();

    // When encoding a null string the encoder doesn't produce any XML
    assertTrue(actual.isEmpty());
  }

  @Test
  void encodeVariantOfScalar() {
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
  void encodeVariantOfArray() {
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
