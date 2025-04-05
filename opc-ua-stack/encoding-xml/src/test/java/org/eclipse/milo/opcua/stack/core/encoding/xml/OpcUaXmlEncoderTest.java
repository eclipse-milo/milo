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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

class OpcUaXmlEncoderTest {

  private static final boolean DEBUG = true;

  EncodingContext context = new DefaultEncodingContext();

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#booleanArguments")
  void encodeBoolean(Boolean value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeBoolean("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#booleanArrayArguments")
  void encodeBooleanArray(@Nullable Boolean[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeBooleanArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#sByteArguments")
  void encodeSByte(Byte value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeSByte("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#int16Arguments")
  void encodeInt16(Short value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt16("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#int32Arguments")
  void encodeInt32(Integer value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt32("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#int64Arguments")
  void encodeInt64(Long value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt64("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource("org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#byteArguments")
  void encodeByte(UByte value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeByte("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#uInt16Arguments")
  void encodeUInt16(UShort value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt16("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#uInt32Arguments")
  void encodeUInt32(UInteger value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt32("Test", value);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#uInt64Arguments")
  void encodeUInt64(ULong value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt64("Test", value);

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
  void encodeExtensionObject() {
    String expected =
"""
<Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
  <uax:TypeId>
    <uax:Identifier>ns=0;i=12082</uax:Identifier>
  </uax:TypeId>
  <uax:Body>
    <XVType>
      <X>1.0</X>
      <Value>2.0</Value>
    </XVType>
  </uax:Body>
</Test>
""";

    var xo =
        ExtensionObject.encode(
            context,
            new XVType(1.0, 2.0f),
            XVType.XML_ENCODING_ID,
            OpcUaDefaultXmlEncoding.getInstance());

    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeExtensionObject("Test", xo);

    String actual = encoder.getDocumentXml();

    System.out.println(actual);

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

  @ParameterizedTest(name = "nodeId = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#nodeIdArguments")
  void encodeNodeId(NodeId nodeId, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeNodeId("Test", nodeId);

    String actual = encoder.getDocumentXml();

    if (nodeId == null) {
      // When encoding a null NodeId the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "localizedText = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#localizedTextArguments")
  void encodeLocalizedText(@Nullable LocalizedText localizedText, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeLocalizedText("Test", localizedText);

    String actual = encoder.getDocumentXml();

    if (localizedText == null) {
      // When encoding a null LocalizedText the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  private static void maybePrintXml(Diff diff, String expected, String actual) {
    if (diff.hasDifferences() || DEBUG) {
      System.out.printf("Expected:%n%s%n", expected);
      System.out.printf("Actual:%n%s%n", actual);
    }
  }
}
