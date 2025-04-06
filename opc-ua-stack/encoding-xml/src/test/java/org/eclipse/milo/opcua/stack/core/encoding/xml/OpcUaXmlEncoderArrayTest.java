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

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

public class OpcUaXmlEncoderArrayTest {

  private final EncodingContext context = new DefaultEncodingContext();

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

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#sByteArrayArguments")
  void encodeSByteArray(@Nullable Byte[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeSByteArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#byteArrayArguments")
  void encodeByteArray(@Nullable UByte[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeByteArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#int16ArrayArguments")
  void encodeInt16Array(@Nullable Short[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt16Array("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#uInt16ArrayArguments")
  void encodeUInt16Array(@Nullable UShort[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt16Array("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#int32ArrayArguments")
  void encodeInt32Array(@Nullable Integer[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt32Array("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#uInt32ArrayArguments")
  void encodeUInt32Array(@Nullable UInteger[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt32Array("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#int64ArrayArguments")
  void encodeInt64Array(@Nullable Long[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeInt64Array("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#uInt64ArrayArguments")
  void encodeUInt64Array(@Nullable ULong[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt64Array("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#floatArrayArguments")
  void encodeFloatArray(@Nullable Float[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeFloatArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#doubleArrayArguments")
  void encodeDoubleArray(@Nullable Double[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDoubleArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#stringArrayArguments")
  void encodeStringArray(@Nullable String[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeStringArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#dateTimeArrayArguments")
  void encodeDateTimeArray(@Nullable DateTime[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDateTimeArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#guidArrayArguments")
  void encodeGuidArray(@Nullable UUID[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeGuidArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#byteStringArrayArguments")
  void encodeByteStringArray(@Nullable ByteString[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeByteStringArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#xmlElementArrayArguments")
  void encodeXmlElementArray(@Nullable XmlElement[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeXmlElementArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#nodeIdArrayArguments")
  void encodeNodeIdArray(@Nullable NodeId[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeNodeIdArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#expandedNodeIdArrayArguments")
  void encodeExpandedNodeIdArray(@Nullable ExpandedNodeId[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeExpandedNodeIdArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#statusCodeArrayArguments")
  void encodeStatusCodeArray(@Nullable StatusCode[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeStatusCodeArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#qualifiedNameArrayArguments")
  void encodeQualifiedNameArray(@Nullable QualifiedName[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeQualifiedNameArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#localizedTextArrayArguments")
  void encodeLocalizedTextArray(@Nullable LocalizedText[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeLocalizedTextArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#extensionObjectArrayArguments")
  void encodeExtensionObjectArray(@Nullable ExtensionObject[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeExtensionObjectArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#dataValueArrayArguments")
  void encodeDataValueArray(@Nullable DataValue[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDataValueArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#variantArrayArguments")
  void encodeVariantArray(@Nullable Variant[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeVariantArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#diagnosticInfoArrayArguments")
  void encodeDiagnosticInfoArray(@Nullable DiagnosticInfo[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDiagnosticInfoArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#enumArrayArguments")
  void encodeEnumArray(@Nullable UaEnumeratedType[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeEnumArray("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "array = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#structArrayArguments")
  void encodeStructArray(@Nullable UaStructuredType[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeStructArray("Test", array, XVType.TYPE_ID);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }
}
