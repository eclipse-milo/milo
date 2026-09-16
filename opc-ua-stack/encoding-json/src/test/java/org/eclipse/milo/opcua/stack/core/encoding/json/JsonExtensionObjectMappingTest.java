/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.json;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import java.io.StringReader;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder.Encoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class JsonExtensionObjectMappingTest {
  private final DefaultEncodingContext context = new DefaultEncodingContext();
  private final EUInformation units =
      new EUInformation("urn:units", 42, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE);

  // Part 6 1.05.07 §5.4.2.16 inserts UaTypeId into the native structure object.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void encodesNativeBodyInline(Encoding mode) throws Exception {
    ExtensionObject value = OpcUaDefaultJsonEncoding.getInstance().encode(context, units);
    assertEquals(
        "{\"UaTypeId\":\"i=887\",\"NamespaceUri\":\"urn:units\",\"UnitId\":42}",
        encode(value, mode));
  }

  // Table 40 requires the DataType ID even when the body is already encoded as Binary.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void encodesBinaryBodyWithDataTypeId(Encoding mode) throws Exception {
    assertEquals(
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,"
            + "\"UaBody\":\"CQAAAHVybjp1bml0cyoAAAAAAA==\"}",
        encode(ExtensionObject.encode(context, units), mode));
  }

  // Identity conversion uses registration metadata even when the opaque body cannot be decoded.
  @Test
  void encodesOpaqueBinaryBodyWithoutDecoding() throws Exception {
    ExtensionObject value =
        ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), new NodeId(0, 889));
    assertEquals(
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaBody\":\"AQID\"}",
        encode(value, Encoding.COMPACT));
  }

  // Table 40 uses a ByteString for XML, preserving its UTF-8 bytes as base64.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void encodesXmlBodyAsUtf8ByteString(Encoding mode) throws Exception {
    assertEquals(
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":2,\"UaBody\":\"PHg+w6k8L3g+\"}",
        encode(ExtensionObject.of(new XmlElement("<x>é</x>"), new NodeId(0, 888)), mode));
  }

  // §5.4.2.16 requires UaTypeId in any position and preserves all native fields.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"UaTypeId\":\"i=887\",\"NamespaceUri\":\"urn:units\",\"UnitId\":42}",
        "{\"NamespaceUri\":\"urn:units\",\"UnitId\":42,\"UaTypeId\":\"i=887\"}"
      })
  void decodesNativeBodyWithTypeIdInAnyPosition(String json) {
    ExtensionObject value = new OpcUaJsonDecoder(context, json).decodeExtensionObject(null);
    assertEquals(units, value.decode(context));
  }

  // Binary wrappers must retain their encoding identity for later binary serialization.
  @Test
  void decodesBinaryDataTypeIdToEncodingId() {
    String json = "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaBody\":\"AQID\"}";
    ExtensionObject.Binary value =
        assertInstanceOf(
            ExtensionObject.Binary.class,
            new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
    assertEquals(new NodeId(0, 889), value.getEncodingOrTypeId());
    assertEquals(ByteString.of(new byte[] {1, 2, 3}), value.getBody());
  }

  // Base64 is the XML envelope representation, not the XML fragment returned to callers.
  @Test
  void decodesXmlBytesAndRestoresEncodingId() {
    String json = "{\"UaTypeId\":\"i=887\",\"UaEncoding\":2,\"UaBody\":\"PHg+w6k8L3g+\"}";
    ExtensionObject.Xml value =
        assertInstanceOf(
            ExtensionObject.Xml.class,
            new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
    assertEquals(new NodeId(0, 888), value.getEncodingOrTypeId());
    assertEquals("<x>é</x>", value.getBody().getFragment());
  }

  // §5.4.2.16 rejects duplicates before an object representation can discard them.
  @Test
  void rejectsDuplicateTypeId() {
    String json = "{\"UaTypeId\":\"i=887\",\"UaTypeId\":\"i=888\"}";
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
  }

  // Table 40 permits all header orders, including the body before its encoding selector.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"UaBody\":\"AQID\",\"UaEncoding\":1,\"UaTypeId\":\"i=887\"}",
        "{\"UaEncoding\":1,\"UaTypeId\":\"i=887\",\"UaBody\":\"AQID\"}",
        "{\"UaTypeId\":\"i=887\",\"UaBody\":\"AQID\",\"UaEncoding\":1}"
      })
  void decodesReorderedBinaryEnvelope(String json) {
    assertEquals(
        ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), new NodeId(0, 889)),
        new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
  }

  // Native field names do not acquire legacy header semantics, even for an unknown type.
  @Test
  void preservesUnknownNativeBodyAndLegacyHeaderNames() throws Exception {
    String body =
        "{\"UaBody\":{\"Value\":18446744073709551615},"
            + "\"TypeId\":\"payload\",\"Encoding\":7,\"Body\":false}";
    ExtensionObject value = ExtensionObject.of(body, new NodeId(0, 70000));
    String json = "{\"UaTypeId\":\"i=70000\"," + body.substring(1);
    assertEquals(json, encode(value, Encoding.COMPACT));
    assertEquals(value, new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
  }

  // §5.4.2.10 retains the NodeId fallback when the namespace URI is not in the local table.
  @Test
  void retainsUnknownNamespaceNodeIdFallback() {
    String id = "nsu=urn:missing;i=42";
    ExtensionObject value =
        new OpcUaJsonDecoder(context, "{\"UaTypeId\":\"" + id + "\",\"X\":1}")
            .decodeExtensionObject(null);
    assertEquals(new NodeId(0, id), value.getEncodingOrTypeId());
    assertEquals("{\"X\":1}", value.getBody());
  }

  // Old nested bodies must not be guessed away; known codecs reject that extra field on decode.
  @Test
  void doesNotUnwrapLegacyNativeBody() {
    ExtensionObject value =
        new OpcUaJsonDecoder(context, "{\"UaTypeId\":\"i=887\",\"UaBody\":{\"UnitId\":42}}")
            .decodeExtensionObject(null);
    assertEquals("{\"UaBody\":{\"UnitId\":42}}", value.getBody());
    assertThrows(UaSerializationException.class, () -> value.decode(context));
  }

  // Whole-envelope validation must reject duplicates, mixed formats, and invalid headers.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"UaTypeId\":\"i=887\",\"UnitId\":1,\"UnitId\":2}",
        "{\"UaTypeId\":\"i=887\",\"Nested\":{\"X\":1,\"X\":2}}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaEncoding\":2,\"UaBody\":\"\"}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaBody\":\"\",\"UaBody\":\"AQ==\"}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaBody\":\"\",\"UnitId\":42}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":0}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":3,\"UaBody\":\"\"}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1.5,\"UaBody\":\"\"}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":4294967297,\"UaBody\":\"\"}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":\"1\",\"UaBody\":\"\"}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaBody\":{}}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaBody\":5}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":1,\"UaBody\":\"%%%\"}",
        "{\"UaTypeId\":\"i=887\",\"UaEncoding\":2,\"UaBody\":\"/w==\"}",
        "{\"UaTypeId\":887}",
        "{\"UaTypeId\":null}",
        "{\"UaTypeId\":\"i=0\"}",
        "{\"UaTypeId\":\"not-a-node-id\"}",
        "{\"UnitId\":42}",
        "{\"TypeId\":\"i=887\",\"Body\":{}}",
        "[]",
        "42",
        "true"
      })
  void rejectsInvalidEnvelope(String json) {
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
    assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
  }

  // Native bodies cannot inject an envelope header or lose duplicate values during flattening.
  @ParameterizedTest
  @ValueSource(
      strings = {
        "{\"UaTypeId\":\"i=887\"}",
        "{\"UaEncoding\":1}",
        "{\"X\":1,\"X\":2}",
        "{\"Nested\":{\"X\":1,\"X\":2}}",
        "null",
        "[]",
        "42",
        "{} {}",
        "{"
      })
  void rejectsInvalidNativeBody(String body) {
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () -> encode(ExtensionObject.of(body, new NodeId(0, 70000)), Encoding.COMPACT));
    assertEquals(StatusCodes.Bad_EncodingError, error.getStatusCode().getValue());
  }

  // An opaque body cannot reveal an unregistered encoding/DataType association.
  @ParameterizedTest
  @ValueSource(ints = {1, 2})
  void rejectsUnknownBinaryOrXmlIdentity(int bodyFormat) {
    String json =
        "{\"UaTypeId\":\"i=70000\",\"UaEncoding\":" + bodyFormat + ",\"UaBody\":\"AQID\"}";
    UaSerializationException readError =
        assertThrows(
            UaSerializationException.class,
            () -> new OpcUaJsonDecoder(context, json).decodeExtensionObject(null));
    assertEquals(StatusCodes.Bad_DecodingError, readError.getStatusCode().getValue());
    ExtensionObject value =
        bodyFormat == 1
            ? ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), new NodeId(0, 70001))
            : ExtensionObject.of(new XmlElement("<x/>"), new NodeId(0, 70002));
    UaSerializationException writeError =
        assertThrows(UaSerializationException.class, () -> encode(value, Encoding.COMPACT));
    assertEquals(StatusCodes.Bad_EncodingError, writeError.getStatusCode().getValue());
  }

  // Replacing an encoding must not relabel an old opaque body's bytes with the new encoding ID.
  @ParameterizedTest
  @ValueSource(ints = {1, 2})
  void rejectsStaleOpaqueIdentityAfterReplacement(int bodyFormat) throws Exception {
    var manager = new DefaultDataTypeManager();
    var localContext =
        new DefaultEncodingContext() {
          @Override
          public DataTypeManager getDataTypeManager() {
            return manager;
          }
        };
    NodeId typeId = new NodeId(0, 70000);
    NodeId oldId = new NodeId(0, 70001);
    NodeId newId = new NodeId(0, 70002);
    manager.registerType(
        typeId,
        new EUInformation.Codec(),
        bodyFormat == 1 ? oldId : null,
        bodyFormat == 2 ? oldId : null,
        null);
    manager.registerType(
        typeId,
        new EUInformation.Codec(),
        bodyFormat == 1 ? newId : null,
        bodyFormat == 2 ? newId : null,
        null);
    ExtensionObject oldValue =
        bodyFormat == 1
            ? ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), oldId)
            : ExtensionObject.of(new XmlElement("<x/>"), oldId);
    UaSerializationException error =
        assertThrows(
            UaSerializationException.class,
            () -> {
              try (var encoder = new OpcUaJsonEncoder(localContext)) {
                encoder.encodeExtensionObject(null, oldValue);
              }
            });
    assertEquals(StatusCodes.Bad_EncodingError, error.getStatusCode().getValue());
    ExtensionObject newValue =
        bodyFormat == 1
            ? ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), newId)
            : ExtensionObject.of(new XmlElement("<x/>"), newId);
    try (var encoder = new OpcUaJsonEncoder(localContext)) {
      encoder.encodeExtensionObject(null, newValue);
      assertEquals(
          newValue,
          new OpcUaJsonDecoder(localContext, encoder.getOutputString())
              .decodeExtensionObject(null));
    }
  }

  // Nested arrays and Variants use the same envelope, and must leave the following field intact.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void preservesEnvelopeInArraysAndVariantsAndConsumesFollowingField(Encoding mode)
      throws Exception {
    ExtensionObject value = OpcUaDefaultJsonEncoding.getInstance().encode(context, units);
    String nativeJson = "{\"UaTypeId\":\"i=887\",\"NamespaceUri\":\"urn:units\",\"UnitId\":42}";
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(mode);
      encoder.jsonWriter.beginObject();
      encoder.encodeExtensionObjectArray("Objects", new ExtensionObject[] {value, null});
      encoder.encodeVariant("Variant", new Variant(value));
      encoder.encodeInt32("Tail", 37);
      encoder.jsonWriter.endObject();
      json = encoder.getOutputString();
    }
    assertEquals(
        "{\"Objects\":["
            + nativeJson
            + ",null],\"Variant\":{\"UaType\":22,\"Value\":"
            + nativeJson
            + "},\"Tail\":37}",
        json);
    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(mode);
    decoder.jsonReader.beginObject();
    assertArrayEquals(
        new ExtensionObject[] {value, null}, decoder.decodeExtensionObjectArray("Objects"));
    assertEquals(new Variant(value), decoder.decodeVariant("Variant"));
    assertEquals(37, decoder.decodeInt32("Tail"));
    decoder.jsonReader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  // §5.4.9 applies the ExtensionObject mapping to messages, including VERBOSE enum fields.
  @ParameterizedTest
  @EnumSource(Encoding.class)
  void roundTripsMessageWithDataTypeIdentityAndFollowingField(Encoding mode) throws Exception {
    var header =
        new RequestHeader(
            NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0), uint(0), "audit", uint(0), null);
    var message = new ReadRequest(header, 17.0, TimestampsToReturn.Both, null);
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(mode);
      encoder.jsonWriter.beginObject();
      encoder.encodeMessage("Message", message);
      encoder.encodeInt32("Tail", 37);
      encoder.jsonWriter.endObject();
      json = encoder.getOutputString();
    }
    JsonObject envelope = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("Message");
    assertEquals("i=629", envelope.get("UaTypeId").getAsString());
    assertFalse(envelope.has("UaBody"));
    assertEquals(
        "audit", envelope.getAsJsonObject("RequestHeader").get("AuditEntryId").getAsString());
    assertEquals(17.0, envelope.get("MaxAge").getAsDouble());
    assertEquals(
        JsonParser.parseString(mode == Encoding.COMPACT ? "2" : "\"Both_2\""),
        envelope.get("TimestampsToReturn"));
    var decoder = new OpcUaJsonDecoder(context, json);
    decoder.setEncoding(mode);
    decoder.jsonReader.beginObject();
    assertEquals(message, decoder.decodeMessage("Message"));
    assertEquals(37, decoder.decodeInt32("Tail"));
    decoder.jsonReader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, decoder.jsonReader.peek());
  }

  // Whole-object buffering must preserve input/depth limits and allow reset after rejection.
  @Test
  void preservesLimitsAndReset() throws Exception {
    String json = "{\"UaTypeId\":\"i=70000\",\"Nested\":{\"X\":1}}";
    var exact = new OpcUaJsonDecoder(limitedContext(json.length(), 2), json);
    assertEquals("{\"Nested\":{\"X\":1}}", exact.decodeExtensionObject(null).getBody());
    assertEquals(JsonToken.END_DOCUMENT, exact.jsonReader.peek());
    var tooLong = new OpcUaJsonDecoder(limitedContext(json.length() - 1, 2), json);
    assertEquals(
        StatusCodes.Bad_EncodingLimitsExceeded,
        assertThrows(UaSerializationException.class, () -> tooLong.decodeExtensionObject(null))
            .getStatusCode()
            .getValue());
    var tooDeep = new OpcUaJsonDecoder(limitedContext(json.length(), 1), json);
    assertEquals(
        StatusCodes.Bad_EncodingLimitsExceeded,
        assertThrows(UaSerializationException.class, () -> tooDeep.decodeExtensionObject(null))
            .getStatusCode()
            .getValue());
    tooDeep.reset(new StringReader("{}"));
    assertNull(tooDeep.decodeExtensionObject(null));
  }

  private static DefaultEncodingContext limitedContext(int maxCharacters, int maxDepth) {
    return new DefaultEncodingContext() {
      @Override
      public EncodingLimits getEncodingLimits() {
        return new EncodingLimits(8196, 1, maxCharacters, maxDepth);
      }
    };
  }

  private String encode(ExtensionObject value, Encoding mode) throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(mode);
      encoder.encodeExtensionObject(null, value);
      return encoder.getOutputString();
    }
  }
}
