/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.json.JsonMessageMapping;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Discovered metadata drives the JSON field decode through {@link MetadataCache#fromDataType}, the
 * conversion the reader dispatcher applies to metadata announcements.
 */
class MetadataCacheTest {

  private static final JsonMessageMapping MAPPING = new JsonMessageMapping();

  private static final DefaultEncodingContext ENCODING_CONTEXT = new DefaultEncodingContext();

  /**
   * An announcement for a UtcTime field (BuiltInType DateTime) and a ServerState field (BuiltInType
   * Int32), with the SimpleDataTypes and EnumDataTypes entries a publisher sends for them.
   */
  private static final String META_DATA =
      """
      {"MessageType":"ua-metadata","PublisherId":"offline","DataSetWriterId":13,
       "MetaData":{
        "SimpleDataTypes":[{"DataTypeId":"i=294","Name":"UtcTime","BaseDataType":"i=13",
          "BuiltInType":13}],
        "EnumDataTypes":[{"DataTypeId":"i=852","Name":"ServerState","BuiltInType":6,
          "EnumDefinition":{"Fields":[{"Name":"Running","Value":"0"},
            {"Name":"Suspended","Value":"3"}]}}],
        "Name":"Demo",
        "Fields":[
          {"Name":"UtcTime","BuiltInType":13,"DataType":"i=294","ValueRank":-1,
           "DataSetFieldId":"1184b49d-265f-48fe-9451-aa930ed4e066"},
          {"Name":"ServerStatus_State","BuiltInType":6,"DataType":"i=852","ValueRank":-1,
           "DataSetFieldId":"952da032-0eed-4580-8563-d76500c58134"}]}}
      """;

  private static final String DATA =
      """
      {"MessageType":"ua-data","PublisherId":"offline","Messages":[{
        "DataSetWriterId":13,"MessageType":"ua-keyframe","Payload":{
          "UtcTime":"2026-09-30T08:40:51.656Z","ServerStatus_State":"Running_0"}}]}
      """;

  private static DecodedNetworkMessage decode(String json, @Nullable DataSetMetaDataConfig metaData)
      throws Exception {

    ByteBuf buffer = Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8));
    try {
      return MAPPING.decode(
          DecodeContext.of(ENCODING_CONTEXT, null, metaData == null ? null : request -> metaData),
          buffer);
    } finally {
      buffer.release();
    }
  }

  private static DataSetMetaDataConfig discoveredMetaData() throws Exception {
    DataSetMetaDataType announced = decode(META_DATA, null).metaData().get(0).metaData();

    return MetadataCache.fromDataType(announced);
  }

  private static DataValue fieldValue(DecodedNetworkMessage decoded, String name) {
    for (DecodedField field : decoded.messages().get(0).fields()) {
      if (name.equals(field.fieldName())) {
        return field.value();
      }
    }
    throw new AssertionError("no field named " + name);
  }

  /** The converted fields keep the announced BuiltInType of their non-built-in DataTypes. */
  @Test
  void conversionKeepsAnnouncedBuiltInType() throws Exception {
    DataSetMetaDataConfig metaData = discoveredMetaData();

    assertEquals(OpcUaDataType.DateTime, metaData.fields().get(0).builtInType());
    assertEquals(OpcUaDataType.Int32, metaData.fields().get(1).builtInType());
  }

  /** Issue #2049: a UtcTime field decodes as DateTime, not as its ISO 8601 String. */
  @Test
  void discoveredUtcTimeFieldDecodesAsDateTime() throws Exception {
    DataValue value = fieldValue(decode(DATA, discoveredMetaData()), "UtcTime");

    assertEquals(StatusCode.GOOD, value.statusCode());
    assertEquals(new DateTime(Instant.parse("2026-09-30T08:40:51.656Z")), value.value().value());
  }

  /** Issue #2050: a Verbose ServerState value "Running_0" decodes as Int32 0, not a String. */
  @Test
  void discoveredEnumerationFieldDecodesAsInt32() throws Exception {
    DataValue value = fieldValue(decode(DATA, discoveredMetaData()), "ServerStatus_State");

    assertEquals(StatusCode.GOOD, value.statusCode());
    assertEquals(0, value.value().value());
  }
}
