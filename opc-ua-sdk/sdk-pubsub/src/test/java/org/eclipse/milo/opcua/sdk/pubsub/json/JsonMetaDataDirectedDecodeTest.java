/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.json;

import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.ENCODING_CONTEXT;
import static org.eclipse.milo.opcua.sdk.pubsub.json.JsonTestFixtures.MAPPING;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMetaDataResolver;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Pins the two decode improvements found against the PubSub JSON reference publisher's
 * configuration matrix:
 *
 * <ul>
 *   <li>The shape-based (metadata-less) array decode never yields {@code Bad_DecodingError} for
 *       legal wire shapes: empty arrays, arrays whose elements all decode to null, and arrays whose
 *       integral literals decode to mixed Java widths.
 *   <li>With a {@link DataSetMetaDataResolver} on the {@link DecodeContext}, shape-ambiguous
 *       Verbose values decode against the field's metadata-declared built-in type.
 * </ul>
 */
class JsonMetaDataDirectedDecodeTest {

  private static final DataSetMetaDataConfig META =
      DataSetMetaDataConfig.builder("AllTypes")
          .field("Bool", NodeIds.Boolean)
          .field("UInt32", NodeIds.UInt32)
          .field("Int64", NodeIds.Int64)
          .field("UInt64", NodeIds.UInt64)
          .field("Float", NodeIds.Float)
          .field("Status", NodeIds.StatusCode)
          .field("Text", NodeIds.LocalizedText)
          .field("BoolArray", NodeIds.Boolean, UUID.randomUUID(), 1, null)
          .field("UInt32Array", NodeIds.UInt32, UUID.randomUUID(), 1, null)
          .field("FloatArray", NodeIds.Float, UUID.randomUUID(), 1, null)
          .field("StatusArray", NodeIds.StatusCode, UUID.randomUUID(), 1, null)
          .configurationVersion(uint(7), uint(7))
          .build();

  private static DecodedNetworkMessage decode(
      String json, @Nullable DataSetMetaDataResolver resolver) throws UaException {

    ByteBuf buffer = Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8));
    try {
      return MAPPING.decode(DecodeContext.of(ENCODING_CONTEXT, null, resolver), buffer);
    } finally {
      buffer.release();
    }
  }

  private static DataValue fieldValue(DecodedNetworkMessage decoded, String name) {
    for (DecodedField field : decoded.messages().get(0).fields()) {
      if (name.equals(field.fieldName())) {
        return field.value();
      }
    }
    throw new AssertionError("no field named " + name);
  }

  private static String keyFrame(String payload) {
    return """
    {"MessageType":"ua-data","PublisherId":"line-7","Messages":[{"DataSetWriterId":5,\
    "SequenceNumber":1,"MetaDataVersion":{"MajorVersion":7,"MinorVersion":7},\
    "MessageType":"ua-keyframe","Payload":%s}]}\
    """
        .formatted(payload);
  }

  // region shape-based array fallback (no metadata)

  /** An empty bare array decodes as an empty Variant[], not Bad_DecodingError. */
  @Test
  void emptyArrayDecodesAsEmptyVariantArray() throws Exception {
    DataValue value = fieldValue(decode(keyFrame("{\"A\":[]}"), null), "A");

    assertEquals(StatusCode.GOOD, value.statusCode());
    Variant[] array = assertInstanceOf(Variant[].class, value.value().value());
    assertEquals(0, array.length);
  }

  /**
   * An array whose elements all decode to null (e.g. an array of Good StatusCode objects, whose
   * Code member is omitted when Good) decodes as a Variant[] of null Variants.
   */
  @Test
  void allNullElementArrayDecodesAsVariantArray() throws Exception {
    DataValue value = fieldValue(decode(keyFrame("{\"A\":[{},{}]}"), null), "A");

    assertEquals(StatusCode.GOOD, value.statusCode());
    Variant[] array = assertInstanceOf(Variant[].class, value.value().value());
    assertArrayEquals(new Variant[] {Variant.NULL_VALUE, Variant.NULL_VALUE}, array);
  }

  /** Integral literals of mixed decoded width widen to Long[] instead of failing. */
  @Test
  void mixedWidthIntegralArrayWidensToLong() throws Exception {
    DataValue value = fieldValue(decode(keyFrame("{\"A\":[0,4294967295]}"), null), "A");

    assertEquals(StatusCode.GOOD, value.statusCode());
    assertArrayEquals(new Long[] {0L, 4294967295L}, (Long[]) value.value().value());
  }

  /** Integral and decimal literals mixed widen to Double[]. */
  @Test
  void mixedIntegralAndDecimalArrayWidensToDouble() throws Exception {
    DataValue value = fieldValue(decode(keyFrame("{\"A\":[1,2.5]}"), null), "A");

    assertEquals(StatusCode.GOOD, value.statusCode());
    assertArrayEquals(new Double[] {1.0, 2.5}, (Double[]) value.value().value());
  }

  /** A genuinely heterogeneous array decodes as Variant[], element types preserved. */
  @Test
  void heterogeneousArrayDecodesAsVariantArray() throws Exception {
    DataValue value = fieldValue(decode(keyFrame("{\"A\":[1,\"x\"]}"), null), "A");

    assertEquals(StatusCode.GOOD, value.statusCode());
    Variant[] array = assertInstanceOf(Variant[].class, value.value().value());
    assertArrayEquals(new Variant[] {Variant.ofInt32(1), Variant.ofString("x")}, array);
  }

  // endregion

  // region metadata-directed decode

  private static final DataSetMetaDataResolver RESOLVER = request -> META;

  /** A Verbose StatusCode object on a StatusCode-typed field decodes as a StatusCode VALUE. */
  @Test
  void statusCodeFieldDecodesAsValue() throws Exception {
    DecodedNetworkMessage decoded =
        decode(keyFrame("{\"Status\":{\"Code\":2147483648}}"), RESOLVER);

    DataValue value = fieldValue(decoded, "Status");
    assertEquals(StatusCode.GOOD, value.statusCode());
    assertEquals(new StatusCode(0x80000000L), value.value().value());
  }

  /** An empty object on a StatusCode-typed field is the Good code omitted: StatusCode.GOOD. */
  @Test
  void goodStatusCodeFieldDecodesAsGoodValue() throws Exception {
    DataValue value = fieldValue(decode(keyFrame("{\"Status\":{}}"), RESOLVER), "Status");

    assertEquals(StatusCode.GOOD, value.statusCode());
    assertEquals(StatusCode.GOOD, value.value().value());
  }

  /**
   * A StatusCode-shaped object on a field NOT declared StatusCode stays the Part 14 Table 35
   * footnote-c transfer: a Bad status instead of the value.
   */
  @Test
  void statusCodeShapedObjectOnOtherFieldRemainsStatusTransfer() throws Exception {
    DataValue value =
        fieldValue(decode(keyFrame("{\"UInt32\":{\"Code\":2147483648}}"), RESOLVER), "UInt32");

    assertNull(value.value().value());
    assertEquals(new StatusCode(0x80000000L), value.statusCode());
  }

  /** A collapsed Verbose LocalizedText object decodes as LocalizedText, not ExtensionObject. */
  @Test
  void localizedTextFieldDecodesTyped() throws Exception {
    DataValue value =
        fieldValue(
            decode(keyFrame("{\"Text\":{\"Locale\":\"en\",\"Text\":\"hello\"}}"), RESOLVER),
            "Text");

    assertEquals(LocalizedText.english("hello"), value.value().value());
  }

  /** Numbers and strings take their declared width, signedness, and type. */
  @Test
  void scalarsDecodeToDeclaredTypes() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            keyFrame(
                """
                {"UInt32":4294967295,"Int64":"-9223372036854775808",\
                "UInt64":"18446744073709551615","Float":"NaN"}\
                """),
            RESOLVER);

    assertEquals(uint(4294967295L), fieldValue(decoded, "UInt32").value().value());
    assertEquals(Long.MIN_VALUE, fieldValue(decoded, "Int64").value().value());
    assertEquals(
        ULong.valueOf("18446744073709551615"), fieldValue(decoded, "UInt64").value().value());
    assertEquals(Float.NaN, fieldValue(decoded, "Float").value().value());
  }

  /** Arrays — including empty ones — decode as arrays of the declared type. */
  @Test
  void arraysDecodeToDeclaredElementTypes() throws Exception {
    DecodedNetworkMessage decoded =
        decode(
            keyFrame(
                """
                {"BoolArray":[],"UInt32Array":[0,4294967295],"FloatArray":["NaN","Infinity"],\
                "StatusArray":[{},{"Code":1073741824}]}\
                """),
            RESOLVER);

    assertArrayEquals(new Boolean[0], (Boolean[]) fieldValue(decoded, "BoolArray").value().value());
    assertArrayEquals(
        new UInteger[] {uint(0), uint(4294967295L)},
        (UInteger[]) fieldValue(decoded, "UInt32Array").value().value());
    assertArrayEquals(
        new Float[] {Float.NaN, Float.POSITIVE_INFINITY},
        (Float[]) fieldValue(decoded, "FloatArray").value().value());
    assertArrayEquals(
        new StatusCode[] {StatusCode.GOOD, new StatusCode(0x40000000L)},
        (StatusCode[]) fieldValue(decoded, "StatusArray").value().value());
  }

  /** A DataValue wrapper's Value member is directed by the declared type when UaType is absent. */
  @Test
  void wrapperValueMemberIsDirectedByDeclaredType() throws Exception {
    DataValue value =
        fieldValue(
            decode(
                keyFrame(
                    """
                    {"Int64":{"Value":"42","SourceTimestamp":"2026-07-01T12:00:00Z"}}\
                    """),
                RESOLVER),
            "Int64");

    assertEquals(42L, value.value().value());
  }

  /** A wire-carried UaType wins over the declared type. */
  @Test
  void wireUaTypeWinsOverDeclaredType() throws Exception {
    DataValue value =
        fieldValue(
            decode(keyFrame("{\"Int64\":{\"UaType\":12,\"Value\":\"42\"}}"), RESOLVER), "Int64");

    assertEquals("42", value.value().value());
  }

  /**
   * The transmitted metadata version reaches the resolver, whose refusal (the engine-side resolver
   * applies the Part 14 major-version guard) suppresses the declared-type hints.
   */
  @Test
  void majorVersionMismatchSuppressesHints() throws Exception {
    DataSetMetaDataResolver resolver =
        request -> {
          ConfigurationVersionDataType version = request.configurationVersion();
          boolean mismatch =
              version != null
                  && !version.getMajorVersion().equals(META.getConfigurationVersionMajor());
          return mismatch ? null : META;
        };

    String json =
        """
        {"MessageType":"ua-data","PublisherId":"line-7","Messages":[{"DataSetWriterId":5,\
        "SequenceNumber":1,"MetaDataVersion":{"MajorVersion":99,"MinorVersion":7},\
        "MessageType":"ua-keyframe","Payload":{"Text":{"Locale":"en","Text":"hello"}}}]}\
        """;

    DataValue value = fieldValue(decode(json, resolver), "Text");

    // without the hint the object falls back to a typeless JSON ExtensionObject
    assertInstanceOf(ExtensionObject.class, value.value().value());
  }

  /** A value that does not parse as its declared type falls back to the shape-based decode. */
  @Test
  void undecodableDeclaredTypeFallsBackToShape() throws Exception {
    DataValue value =
        fieldValue(decode(keyFrame("{\"UInt32\":\"not a number\"}"), RESOLVER), "UInt32");

    assertEquals(StatusCode.GOOD, value.statusCode());
    assertEquals("not a number", value.value().value());
  }

  /** The resolver is consulted with the wire identity of the DataSetMessage. */
  @Test
  void resolverReceivesWireIdentity() throws Exception {
    var identities = new ArrayList<String>();
    DataSetMetaDataResolver resolver =
        request -> {
          var publisherId = request.publisherId();
          identities.add(
              (publisherId != null ? publisherId.toCanonicalString() : "-")
                  + "/"
                  + request.dataSetWriterId());
          return META;
        };

    decode(keyFrame("{\"Bool\":true}"), resolver);
    // headerless single DataSetMessage: PublisherId at the DataSetMessage level (Table 185)
    decode(
        """
        {"DataSetWriterId":9,"PublisherId":"line-7","MessageType":"ua-keyframe",\
        "Payload":{"Bool":true}}\
        """,
        resolver);

    assertEquals(List.of("line-7/5", "line-7/9"), identities);
  }

  // endregion
}
