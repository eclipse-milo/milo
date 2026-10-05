/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki.snippets;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.time.Instant;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaDefaultJsonEncoding;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.jspecify.annotations.Nullable;

/** Data type and encoding samples from the Wiki. */
public final class DataSnippets {

  private DataSnippets() {}

  static DataValue unsignedMatrixSample() {
    // snippet:matrix:start
    Matrix grid =
        new Matrix(
            new UInteger[] {uint(1), uint(2), uint(3), uint(4_000_000_000L)},
            new int[] {2, 2},
            OpcUaDataType.UInt32);
    DataValue sample =
        new DataValue(
            new Variant(grid),
            StatusCode.GOOD,
            new DateTime(Instant.parse("2026-01-01T00:00:00Z")));
    // snippet:matrix:end
    return sample;
  }

  // snippet:dynamic-structure:start
  static UaStructuredType readStructure(OpcUaClient client, NodeId variableId) throws UaException {
    DataValue value = client.readValue(0, TimestampsToReturn.Both, variableId);
    if (!value.statusCode().isGood()) {
      throw new UaException(value.statusCode());
    }
    if (!(value.value().value() instanceof ExtensionObject encoded)) {
      throw new IllegalArgumentException("Expected a structured value");
    }
    return encoded.decode(client.getDynamicEncodingContext());
  }

  // snippet:dynamic-structure:end

  static void registerCodec(
      OpcUaClient client,
      NodeId dataTypeId,
      DataTypeCodec codec,
      NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      NodeId jsonEncodingId) {
    // snippet:register-codec:start
    client
        .getStaticDataTypeManager()
        .registerType(dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId);
    // snippet:register-codec:end
  }

  static Range roundTrip(EncodingContext context, DataTypeEncoding encoding) {
    // snippet:extension-roundtrip:start
    Range original = new Range(0.0, 100.0);
    ExtensionObject encoded = ExtensionObject.encode(context, original, encoding);
    Range decoded = (Range) encoded.decode(context, encoding);
    // snippet:extension-roundtrip:end
    return decoded;
  }

  static DataValue jsonRoundTrip(EncodingContext context) throws Exception {
    // snippet:json-value:start
    String json;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, DataValue.valueOnly(new Variant(42)));
      json = encoder.getOutputString();
    }
    DataValue decoded = new OpcUaJsonDecoder(context, json).decodeDataValue(null);
    // snippet:json-value:end
    return decoded;
  }

  static String encodeStructureAsJson(EncodingContext context, Range range) throws Exception {
    // snippet:json-inline:start
    ExtensionObject jsonStructure =
        ExtensionObject.encode(context, range, OpcUaDefaultJsonEncoding.getInstance());
    String inline;
    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeDataValue(null, DataValue.valueOnly(new Variant(jsonStructure)));
      inline = encoder.getOutputString();
    }
    // snippet:json-inline:end
    return inline;
  }
}
