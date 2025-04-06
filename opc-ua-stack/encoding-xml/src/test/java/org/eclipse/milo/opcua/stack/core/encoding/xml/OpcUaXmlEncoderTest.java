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

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
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
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ArrayArguments#uInt64ArrayArguments")
  void encodeUInt64Array(@Nullable ULong[] array, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeUInt64Array("Test", array);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#floatArguments")
  void encodeFloat(Float value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeFloat("Test", value);

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

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#doubleArguments")
  void encodeDouble(Double value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDouble("Test", value);

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

  @ParameterizedTest(name = "value = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#stringArguments")
  void encodeString(@Nullable String value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeString("Test", value);

    String actual = encoder.getDocumentXml();

    if (value == null) {
      // When encoding a null string the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
  }

  @Test
  void encodeExtensionObject() {
    String expected =
"""
<Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
  <uax:TypeId>
    <uax:Identifier>i=12082</uax:Identifier>
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

  @ParameterizedTest(name = "dateTime = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#dateTimeArguments")
  void encodeDateTime(@Nullable DateTime dateTime, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDateTime("Test", dateTime);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "guid = {0}")
  @MethodSource("org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#guidArguments")
  void encodeGuid(@Nullable UUID guid, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeGuid("Test", guid);

    String actual = encoder.getDocumentXml();

    Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

    maybePrintXml(diff, expected, actual);

    assertFalse(diff.hasDifferences(), diff.toString());
  }

  @ParameterizedTest(name = "byteString = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#byteStringArguments")
  void encodeByteString(@Nullable ByteString byteString, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeByteString("Test", byteString);

    String actual = encoder.getDocumentXml();

    if (byteString == null) {
      // When encoding a null ByteString the encoder doesn't produce any XML
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
  void encodeExpandedNodeId(@Nullable ExpandedNodeId expandedNodeId, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeExpandedNodeId("Test", expandedNodeId);

    String actual = encoder.getDocumentXml();

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
  void encodeStatusCode(@Nullable StatusCode statusCode, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeStatusCode("Test", statusCode);

    String actual = encoder.getDocumentXml();

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
  void encodeQualifiedName(@Nullable QualifiedName qualifiedName, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeQualifiedName("Test", qualifiedName);

    String actual = encoder.getDocumentXml();

    if (qualifiedName == null) {
      // When encoding a null QualifiedName the encoder doesn't produce any XML
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

  @ParameterizedTest(name = "dataValue = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#dataValueArguments")
  void encodeDataValue(@Nullable DataValue dataValue, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeDataValue("Test", dataValue);

    String actual = encoder.getDocumentXml();

    if (dataValue == null) {
      // When encoding a null DataValue the encoder doesn't produce any XML
      assertTrue(actual.isEmpty());
    } else {
      Diff diff = DiffBuilder.compare(expected).withTest(actual).ignoreWhitespace().build();

      maybePrintXml(diff, expected, actual);

      assertFalse(diff.hasDifferences(), diff.toString());
    }
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

  @ParameterizedTest(name = "xmlElement = {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.ScalarArguments#xmlElementArguments")
  void encodeXmlElement(@Nullable XmlElement xmlElement, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeXmlElement("Test", xmlElement);

    String actual = encoder.getDocumentXml();

    if (xmlElement == null) {
      // When encoding a null XmlElement the encoder doesn't produce any XML
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
  void encodeStruct(String field, @Nullable UaStructuredType value, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    if (value != null) {
      encoder.encodeStruct(field, value, value.getTypeId());
    } else {
      encoder.encodeStruct(field, null, NodeId.NULL_VALUE);
    }

    String actual = encoder.getDocumentXml();

    if (value == null) {
      // When encoding a null struct the encoder doesn't produce any XML
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
  void encodeVariantOfScalar(Variant variant, String expected) {
    var encoder = new OpcUaXmlEncoder(context);
    encoder.encodeVariant("Test", variant);

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
}
