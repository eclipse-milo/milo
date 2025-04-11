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

import static org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoderTest.maybePrintXml;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

public class OpcUaXmlEncoderScalarTest {

  private final EncodingContext context = new DefaultEncodingContext();

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#booleanArguments")
  void encodeBoolean(Boolean value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeBoolean("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#sByteArguments")
  void encodeSByte(Byte value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeSByte("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource("org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#byteArguments")
  void encodeByte(UByte value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeByte("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#int16Arguments")
  void encodeInt16(Short value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeInt16("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#uInt16Arguments")
  void encodeUInt16(UShort value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeUInt16("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#int32Arguments")
  void encodeInt32(Integer value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeInt32("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#uInt32Arguments")
  void encodeUInt32(UInteger value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeUInt32("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#int64Arguments")
  void encodeInt64(Long value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeInt64("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#uInt64Arguments")
  void encodeUInt64(ULong value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeUInt64("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#floatArguments")
  void encodeFloat(Float value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeFloat("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#doubleArguments")
  void encodeDouble(Double value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeDouble("Test", value);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#stringArguments")
  void encodeString(@Nullable String value, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeString("Test", value);

      actual = encoder.getOutputString();
    }

    if (value == null) {
      // When encoding a null string the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "dateTime = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#dateTimeArguments")
  void encodeDateTime(@Nullable DateTime dateTime, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeDateTime("Test", dateTime);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "guid = {0}")
  @MethodSource("org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#guidArguments")
  void encodeGuid(@Nullable UUID guid, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeGuid("Test", guid);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "byteString = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#byteStringArguments")
  void encodeByteString(@Nullable ByteString byteString, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeByteString("Test", byteString);

      actual = encoder.getOutputString();
    }

    if (byteString == null) {
      // When encoding a null ByteString the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "xmlElement = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#xmlElementArguments")
  void encodeXmlElement(@Nullable XmlElement xmlElement, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeXmlElement("Test", xmlElement);

      actual = encoder.getOutputString();
    }

    if (xmlElement == null) {
      // When encoding a null XmlElement the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "nodeId = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#nodeIdArguments")
  void encodeNodeId(NodeId nodeId, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeNodeId("Test", nodeId);

      actual = encoder.getOutputString();
    }

    if (nodeId == null) {
      // When encoding a null NodeId the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "expandedNodeId = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#expandedNodeIdArguments")
  void encodeExpandedNodeId(@Nullable ExpandedNodeId expandedNodeId, String expected)
      throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeExpandedNodeId("Test", expandedNodeId);

      actual = encoder.getOutputString();
    }

    if (expandedNodeId == null) {
      // When encoding a null ExpandedNodeId the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "statusCode = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#statusCodeArguments")
  void encodeStatusCode(@Nullable StatusCode statusCode, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeStatusCode("Test", statusCode);

      actual = encoder.getOutputString();
    }

    if (statusCode == null) {
      // When encoding a null StatusCode the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "qualifiedName = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#qualifiedNameArguments")
  void encodeQualifiedName(@Nullable QualifiedName qualifiedName, String expected)
      throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeQualifiedName("Test", qualifiedName);

      actual = encoder.getOutputString();
    }

    if (qualifiedName == null) {
      // When encoding a null QualifiedName the encoder doesn't produce any XML
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
  void encodeLocalizedText(@Nullable LocalizedText localizedText, String expected)
      throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeLocalizedText("Test", localizedText);

      actual = encoder.getOutputString();
    }

    if (localizedText == null) {
      // When encoding a null LocalizedText the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "extensionObject = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#extensionObjectArguments")
  void encodeExtensionObject(@Nullable ExtensionObject extensionObject, String expected)
      throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeExtensionObject("Test", extensionObject);

      actual = encoder.getOutputString();
    }

    if (extensionObject == null) {
      // When encoding a null ExtensionObject the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "dataValue = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#dataValueArguments")
  void encodeDataValue(@Nullable DataValue dataValue, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeDataValue("Test", dataValue);

      actual = encoder.getOutputString();
    }

    if (dataValue == null) {
      // When encoding a null DataValue the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "variant = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.VariantArguments#variantOfScalarArguments")
  void encodeVariantOfScalar(Variant variant, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeVariant("Test", variant);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "variant = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.VariantArguments#variantOfArrayArguments")
  void encodeVariantOfArray(Variant variant, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeVariant("Test", variant);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "variant = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.VariantArguments#variantOfMatrixArguments")
  void encodeVariantOfMatrix(Variant variant, String expected) throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeVariant("Test", variant);

      actual = encoder.getOutputString();
    }

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "diagnosticInfo = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#diagnosticInfoArguments")
  void encodeDiagnosticInfo(@Nullable DiagnosticInfo diagnosticInfo, String expected)
      throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeDiagnosticInfo("Test", diagnosticInfo);

      actual = encoder.getOutputString();
    }

    if (diagnosticInfo == null) {
      // When encoding a null DiagnosticInfo the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @ParameterizedTest(name = "field = {0}, struct = {1}, dataTypeId = {2}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.StructArguments#structArguments")
  void encodeStruct(String field, @Nullable UaStructuredType value, String expected)
      throws Exception {
    String actual;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      if (value != null) {
        encoder.encodeStruct(field, value, value.getTypeId());
      } else {
        encoder.encodeStruct(field, null, NodeId.NULL_VALUE);
      }

      actual = encoder.getOutputString();
    }

    if (value == null) {
      // When encoding a null struct the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }
}
