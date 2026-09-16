/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.HexFormat;
import java.util.Map;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaXmlEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.xml.XmlDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class XmlDataTypeCodecCompatibilityTest {
  private static final String MODEL = "urn:test:child";
  private static final String BASE = "urn:test:base";
  private static final Value VALUE = new Value(UInteger.valueOf(1L << 21), "result", "child");
  private final EncodingContext context = context();

  // XML metadata must not add fields or masks to Binary. Golden bytes also protect the mask bits,
  // UTF-8 string lengths, and the following field independently of Milo's decoder.
  @ParameterizedTest
  @MethodSource("codecs")
  void adapterPreservesBinaryBytesAndDecoderPosition(DataTypeCodec codec) {
    DataTypeCodec adapted = adapt(codec);
    ByteBuf buffer = Unpooled.buffer();
    ByteBuf original = Unpooled.buffer();
    try {
      var encoder = new OpcUaBinaryEncoder(context).setBuffer(buffer);
      encoder.encodeStruct(null, VALUE, adapted);
      encoder.encodeInt32("After", 42);
      var baseline = new OpcUaBinaryEncoder(context).setBuffer(original);
      baseline.encodeStruct(null, VALUE, codec);
      baseline.encodeInt32("After", 42);
      assertArrayEquals(ByteBufUtil.getBytes(original), ByteBufUtil.getBytes(buffer));
      assertArrayEquals(
          HexFormat.of().parseHex("0000200006000000726573756c74050000006368696c642a000000"),
          ByteBufUtil.getBytes(buffer));
      var decoder = new OpcUaBinaryDecoder(context).setBuffer(buffer);
      assertEquals(VALUE, decoder.decodeStruct(null, adapted));
      assertEquals(42, decoder.decodeInt32("After"));
      assertEquals(0, buffer.readableBytes());
    } finally {
      buffer.release();
      original.release();
    }
  }

  // The adapter passes the actual encoder/decoder to the delegate, preserving both legacy raw
  // masks and the newer header APIs (VERBOSE JSON derives mask bits from field presence).
  @ParameterizedTest(name = "{0} {1}")
  @MethodSource("jsonCases")
  void adapterPreservesJsonOutputAndFollowingField(
      String name, OpcUaJsonEncoder.Encoding encoding, DataTypeCodec codec, String expected)
      throws Exception {
    DataTypeCodec adapted = adapt(codec);
    String output = encodeJson(encoding, adapted);
    assertEquals(encodeJson(encoding, codec), output);
    assertEquals(expected, output);
    var decoder = new OpcUaJsonDecoder(context, output);
    decoder.setEncoding(encoding);
    assertEquals(VALUE, decoder.decodeStruct(null, envelope(adapted)));
  }

  private static Stream<Arguments> jsonCases() {
    String numeric =
        "{\"Value\":{\"EncodingMask\":2097152,\"ResultId\":\"result\",\"Name\":\"child\"},\"After\":42}";
    String implicit = "{\"Value\":{\"ResultId\":\"result\",\"Name\":\"child\"},\"After\":42}";
    return Stream.of(
        Arguments.of("raw mask", OpcUaJsonEncoder.Encoding.COMPACT, CODEC, numeric),
        Arguments.of("raw mask", OpcUaJsonEncoder.Encoding.VERBOSE, CODEC, numeric),
        Arguments.of("header API", OpcUaJsonEncoder.Encoding.COMPACT, HEADER_CODEC, numeric),
        Arguments.of("header API", OpcUaJsonEncoder.Encoding.VERBOSE, HEADER_CODEC, implicit));
  }

  private static Stream<DataTypeCodec> codecs() {
    return Stream.of(CODEC, HEADER_CODEC);
  }

  // The adapter must delegate XML decoding without consuming the enclosing structure's next field.
  @ParameterizedTest
  @MethodSource("codecs")
  void adapterDecodesXmlWithTheExistingCodec(DataTypeCodec codec) throws Exception {
    DataTypeCodec adapted = adapt(codec);
    String output;
    try (var encoder = new OpcUaXmlEncoder(context)) {
      encoder.encodeStruct("Envelope", VALUE, envelope(adapted));
      output = encoder.getOutputString();
    }
    try (var decoder = new OpcUaXmlDecoder(context, output)) {
      assertEquals(VALUE, decoder.decodeStruct("Envelope", envelope(adapted)));
    }
  }

  private String encodeJson(OpcUaJsonEncoder.Encoding encoding, DataTypeCodec codec)
      throws Exception {
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.setEncoding(encoding);
      encoder.encodeStruct(null, VALUE, envelope(codec));
      return encoder.getOutputString();
    }
  }

  private static DataTypeCodec envelope(DataTypeCodec codec) {
    return new DataTypeCodec() {
      @Override
      public Class<?> getType() {
        return Value.class;
      }

      @Override
      public UaStructuredType decode(EncodingContext context, UaDecoder decoder) {
        UaStructuredType value = decoder.decodeStruct("Value", codec);
        assertEquals(42, decoder.decodeInt32("After"));
        return value;
      }

      @Override
      public void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value) {
        encoder.encodeStruct("Value", value, codec);
        encoder.encodeInt32("After", 42);
      }
    };
  }

  private static EncodingContext context() {
    var context = new DefaultEncodingContext();
    context.getNamespaceTable().add(MODEL);
    return context.withXmlNamespaceUris(Map.of(MODEL, MODEL + ":xml", BASE, BASE + ":xml"));
  }

  private record Value(UInteger mask, String resultId, String name) implements UaStructuredType {
    @Override
    public String getTypeName() {
      return "Value";
    }

    @Override
    public ExpandedNodeId getTypeId() {
      return ExpandedNodeId.parse("nsu=" + MODEL + ";s=Value");
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }
  }

  private static final DataTypeCodec CODEC = codec(false);
  private static final DataTypeCodec HEADER_CODEC = codec(true);

  private static DataTypeCodec codec(boolean headers) {
    return new DataTypeCodec() {
      @Override
      public Class<?> getType() {
        return Value.class;
      }

      @Override
      public UaStructuredType decode(EncodingContext context, UaDecoder decoder) {
        return new Value(
            headers
                ? decoder.decodeEncodingMask(OPTIONAL_FIELDS)
                : decoder.decodeUInt32("EncodingMask"),
            decoder.decodeString("ResultId"),
            decoder.decodeString("Name"));
      }

      @Override
      public void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value) {
        Value v = (Value) value;
        if (headers) {
          encoder.encodeEncodingMask(v.mask());
        } else {
          encoder.encodeUInt32("EncodingMask", v.mask());
        }
        encoder.encodeString("ResultId", v.resultId());
        encoder.encodeString("Name", v.name());
      }
    };
  }

  private static DataTypeCodec adapt(DataTypeCodec codec) {
    return new XmlDataTypeCodec(codec, Map.of("EncodingMask", BASE, "ResultId", BASE));
  }

  // Optional-field order in the published Result/IJT model, through the selected Name field.
  private static final String[] OPTIONAL_FIELDS = {
    "HasTransferableDataOnFile",
    "IsPartial",
    "IsSimulated",
    "ResultState",
    "StepId",
    "PartId",
    "ExternalRecipeId",
    "InternalRecipeId",
    "ProductId",
    "ExternalConfigurationId",
    "InternalConfigurationId",
    "JobId",
    "CreationTime",
    "ProcessingTimes",
    "ResultUri",
    "ResultEvaluation",
    "ResultEvaluationCode",
    "ResultEvaluationDetails",
    "FileFormat",
    "JoiningTechnology",
    "SequenceNumber",
    "Name"
  };
}
