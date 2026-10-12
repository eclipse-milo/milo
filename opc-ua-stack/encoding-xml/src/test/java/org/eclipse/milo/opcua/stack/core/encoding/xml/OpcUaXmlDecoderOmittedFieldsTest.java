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
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.util.SecureXmlUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.InputSource;

class OpcUaXmlDecoderOmittedFieldsTest {

  // Part 6 §5.3.5: omitted fields take defaults, including at the end of a structure.
  @ParameterizedTest
  @ValueSource(
      strings = {"<Range><Low>1</Low></Range>", "<Range>\n <Low>1</Low>\n <!-- end -->\n</Range>"})
  void defaultsTrailingPrimitiveWithEveryCodecEntryPoint(String xml) throws Exception {
    try (var decoder = decoder(xml)) {
      assertEquals(new Range(1.0, 0.0), decoder.decodeStruct(null, new Range.Codec()));
      decoder.setInput(new StringReader(xml));
      assertEquals(new Range(1.0, 0.0), decoder.decodeStruct(null, NodeIds.Range));
      decoder.setInput(new StringReader(xml));
      assertEquals(new Range(1.0, 0.0), decoder.decodeStruct(null, NodeIds.Range.expanded()));
    }
  }

  // An empty structure is a present value with all fields defaulted.
  @ParameterizedTest
  @ValueSource(strings = {"<Range/>", "<Range>\n <!-- empty -->\n</Range>"})
  void defaultsEntirelyEmptyStructure(String xml) throws Exception {
    try (var decoder = decoder(xml)) {
      assertEquals(new Range(0.0, 0.0), decoder.decodeStruct(null, new Range.Codec()));
    }
  }

  // Published Onboarding NodeSet Argument ns=1;i=5042 omits Description.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  "})
  void defaultsPublishedArgumentDescription(String gap) throws Exception {
    String xml =
        "<Argument>"
            + gap
            + "<Name>Rule</Name>"
            + gap
            + "<DataType><Identifier>i=15634</Identifier></DataType>"
            + gap
            + "<ValueRank>-1</ValueRank>"
            + gap
            + "<ArrayDimensions/>"
            + gap
            + "</Argument>";
    try (var decoder = decoder(xml)) {
      assertEquals(
          new Argument("Rule", new NodeId(0, 15634), -1, new UInteger[0], LocalizedText.NULL_VALUE),
          decoder.decodeStruct(null, new Argument.Codec()));
    }
  }

  // A missing array stays null; a present empty array stays empty.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  "})
  void defaultsTrailingArrayAndLocalizedText(String gap) throws Exception {
    try (var decoder = decoder("<Argument>" + gap + "<Name>Rule</Name>" + gap + "</Argument>")) {
      assertEquals(
          new Argument("Rule", NodeId.NULL_VALUE, 0, null, LocalizedText.NULL_VALUE),
          decoder.decodeStruct(null, new Argument.Codec()));
    }
  }

  // Missing nested structures use the same defaults as missing fields before the cursor ends.
  @ParameterizedTest
  @ValueSource(strings = {"<ReadRequest/>", "<ReadRequest>\n </ReadRequest>"})
  void defaultsMissingNestedStructureFields(String xml) throws Exception {
    try (var decoder = decoder(xml)) {
      ReadRequest request = (ReadRequest) decoder.decodeStruct(null, new ReadRequest.Codec());
      assertNull(request.getRequestHeader());
      assertEquals(0.0, request.getMaxAge());
      assertNull(request.getNodesToRead());
    }
  }

  // Nested exhaustion must restore the outer cursor before decoding subsequent fields.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  "})
  void resumesAfterEmptyNestedStructure(String gap) throws Exception {
    try (var decoder =
        decoder(
            "<ReadRequest>"
                + gap
                + "<RequestHeader/>"
                + gap
                + "<MaxAge>42</MaxAge>"
                + gap
                + "</ReadRequest>")) {
      ReadRequest request = (ReadRequest) decoder.decodeStruct(null, new ReadRequest.Codec());
      assertNotNull(request.getRequestHeader());
      assertEquals(42.0, request.getMaxAge());
      assertNull(request.getNodesToRead());
    }
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "<Range><Low>invalid</Low></Range>",
        "<Range><Low>1</Low><High>invalid</High></Range>"
      })
  void rejectsMalformedPresentFieldsAndRestoresScope(String xml) throws Exception {
    try (var decoder = decoder(xml)) {
      UaSerializationException error =
          assertThrows(
              UaSerializationException.class, () -> decoder.decodeStruct(null, new Range.Codec()));
      assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
      assertThrows(UaSerializationException.class, () -> decoder.decodeDouble("High"));
      decoder.setInput(new StringReader("<Range/>"));
      assertEquals(new Range(0.0, 0.0), decoder.decodeStruct(null, new Range.Codec()));
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"High", ""})
  void rejectsReadsWithoutInputAndAfterTopLevelValue(String field) throws Exception {
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE)) {
      assertThrows(UaSerializationException.class, () -> decoder.decodeDouble(field));
      assertThrows(UaSerializationException.class, () -> decoder.decodeDouble(null));
      decoder.setInput(new StringReader("<Range/>"));
      decoder.decodeStruct(null, new Range.Codec());
      assertThrows(UaSerializationException.class, () -> decoder.decodeDouble(field));
      assertThrows(UaSerializationException.class, () -> decoder.decodeDouble(null));
    }
  }

  // Skipping formatting and supplying defaults must not rewrite the caller's DOM.
  @ParameterizedTest
  @ValueSource(strings = {"", "\n  <!-- field -->\n  "})
  void preservesSourceDom(String gap) throws Exception {
    var builder = SecureXmlUtil.SHARED_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
    var document =
        builder.parse(
            new InputSource(new StringReader("<Range>" + gap + "<Low>1</Low>" + gap + "</Range>")));
    var original = document.cloneNode(true);
    try (var decoder = new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE)) {
      decoder.setInput(document);
      assertEquals(new Range(1.0, 0.0), decoder.decodeStruct(null, new Range.Codec()));
      assertTrue(document.isEqualNode(original));
    }
  }

  private static OpcUaXmlDecoder decoder(String xml) throws Exception {
    return new OpcUaXmlDecoder(DefaultEncodingContext.INSTANCE, xml);
  }
}
