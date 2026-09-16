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

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.SecureXmlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.InputSource;

class OpcUaXmlDecoderNilTest {
  private static String xml(String type, String attributes, String content) {
    return "<"
        + type
        + " xmlns='http://opcfoundation.org/UA/2008/02/Types.xsd' "
        + "xmlns:n='http://www.w3.org/2001/XMLSchema-instance' "
        + attributes
        + ">"
        + content
        + "</"
        + type
        + ">";
  }

  // Stored XML must preserve null separately from empty for every affected builtin value.
  @ParameterizedTest
  @ValueSource(strings = {"true", "1", "  true  ", " 1 "})
  void topLevelNilValuesRetainNull(String lexical) throws Exception {
    String attribute = "n:nil='" + lexical + "'";
    assertNull(decode("String", attribute, ""));
    assertNull(decode("ListOfString", attribute, ""));
    assertNull(decode("ListOfInt32", attribute, ""));
    assertEquals(ByteString.NULL_VALUE, decode("ByteString", attribute, ""));
    assertEquals(XmlElement.of(null), decode("XmlElement", attribute, ""));
  }

  // Namespace identity and false lexical forms must not turn empty values into nulls.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "",
        "n:nil='false'",
        "n:nil='0'",
        "n:nil=' 0 '",
        "nil='true'",
        "xmlns:xsi='urn:other' xsi:nil='true'"
      })
  void nonNilValuesRetainEmpty(String attributes) throws Exception {
    assertEquals("", decode("String", attributes, ""));
    assertArrayEquals(new String[0], (String[]) decode("ListOfString", attributes, ""));
    assertEquals(ByteString.of(new byte[0]), decode("ByteString", attributes, ""));
    assertEquals(XmlElement.of(""), decode("XmlElement", attributes, ""));
  }

  @ParameterizedTest
  @ValueSource(strings = {"TRUE", "yes", "2", ""})
  void invalidNilBooleanIsRejected(String lexical) {
    UaSerializationException exception =
        assertThrows(
            UaSerializationException.class, () -> decode("String", "n:nil='" + lexical + "'", ""));
    assertEquals(StatusCodes.Bad_DecodingError, exception.getStatusCode().getValue());
  }

  // Null array fields must consume their element so the following structure field stays readable.
  @ParameterizedTest
  @MethodSource("arrayDecoders")
  void nilArrayFieldAdvancesToFollowingField(
      Function<OpcUaXmlDecoder, Object> read, String fieldXml) throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(
            DefaultEncodingContext.INSTANCE,
            xml("Root", "", fieldXml + "<Following>ok</Following>"))) {
      var codec =
          new GenericDataTypeCodec<Argument>() {
            @Override
            public Class<Argument> getType() {
              return Argument.class;
            }

            @Override
            public Argument decodeType(EncodingContext context, UaDecoder input) {
              assertNull(read.apply((OpcUaXmlDecoder) input));
              assertEquals("ok", input.decodeString("Following"));
              return new Argument(null, NodeIds.String, -1, null, LocalizedText.NULL_VALUE);
            }

            @Override
            public void encodeType(EncodingContext context, UaEncoder encoder, Argument value) {
              throw new UnsupportedOperationException();
            }
          };
      decoder.decodeStruct("Root", codec);
    }
  }

  static Stream<Arguments> arrayDecoders() {
    return Stream.<Function<OpcUaXmlDecoder, Object>>of(
            d -> d.decodeStringArray("Items"), d -> d.decodeInt32Array("Items"),
            d -> d.decodeEnumArray("Items"), d -> d.decodeStructArray("Items", NodeIds.Argument))
        .map(read -> Arguments.of(read, "<Items n:nil='1'/>"));
  }

  @Test
  void arrayElementsRetainNullEmptyAndNonemptyValues() throws Exception {
    assertArrayEquals(
        new String[] {null, "", "text"},
        (String[])
            decode("ListOfString", "", "<String n:nil='true'/><String/><String>text</String>"));
    assertArrayEquals(
        new ByteString[] {
          ByteString.NULL_VALUE, ByteString.of(new byte[0]), ByteString.of(new byte[] {1})
        },
        (ByteString[])
            decode(
                "ListOfByteString",
                "",
                "<ByteString n:nil='1'/><ByteString/><ByteString>AQ==</ByteString>"));
    XmlElement[] elements =
        (XmlElement[])
            decode(
                "ListOfXmlElement",
                "",
                "<XmlElement n:nil='true'/><XmlElement/><XmlElement><Payload"
                    + " n:nil='true'/></XmlElement>");
    assertEquals(XmlElement.of(null), elements[0]);
    assertEquals(XmlElement.of(""), elements[1]);
    assertTrue(elements[2].getFragment().contains("nil=\"true\""));
  }

  // Reading a caller-owned DOM must not strip attributes or reinterpret an opaque payload's nil.
  @Test
  void opaquePayloadAndSourceDomRemainUnchanged() throws Exception {
    var document =
        SecureXmlUtil.SHARED_DOCUMENT_BUILDER_FACTORY
            .newDocumentBuilder()
            .parse(
                new InputSource(
                    new StringReader(
                        xml("XmlElement", "", "<Payload n:nil='true'>  text  </Payload>"))));
    var original = document.cloneNode(true);
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE)) {
      XmlElement value = decoder.setInput(document).decodeXmlElement("XmlElement");
      assertTrue(value.getFragment().contains("nil=\"true\""));
      assertTrue(value.getFragment().contains("  text  "));
      assertTrue(document.isEqualNode(original));
    }
  }

  // Encoding must retain both array positions and distinguish null fragments from empty fragments.
  @Test
  void xmlElementVariantArrayRoundTripsNullAndEmptyFragments() throws Exception {
    XmlElement[] values = {XmlElement.NULL_VALUE, XmlElement.of("")};
    String encoded;
    try (var encoder = new OpcUaXmlEncoder(DefaultEncodingContext.INSTANCE)) {
      encoder.encodeVariant("Test", new Variant(values));
      encoded = encoder.getOutputString();
    }
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, encoded)) {
      assertArrayEquals(values, (XmlElement[]) decoder.decodeVariant("Test").value());
    }
  }

  private static Object decode(String type, String attributes, String content) throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml(type, attributes, content))) {
      return decoder.decodeVariantValue();
    }
  }
}
