/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;

public class JsonOptionSetCodec extends GenericDataTypeCodec<JsonStruct> {

  private final DataType dataType;

  public JsonOptionSetCodec(DataType dataType) {
    this.dataType = dataType;
  }

  @Override
  public Class<JsonStruct> getType() {
    return JsonStruct.class;
  }

  @Override
  public JsonStruct decodeType(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {
    ByteString value = decoder.decodeByteString("Value");
    ByteString validBits = decoder.decodeByteString("ValidBits");

    var jsonObject = new JsonObject();
    jsonObject.add("Value", JsonConversions.fromByteString(value));
    jsonObject.add("ValidBits", JsonConversions.fromByteString(validBits));

    return new JsonStruct(dataType, jsonObject);
  }

  @Override
  public void encodeType(EncodingContext context, UaEncoder encoder, JsonStruct value)
      throws UaSerializationException {
    JsonElement valueElement = value.getJsonObject().get("Value");
    JsonElement validBitsElement = value.getJsonObject().get("ValidBits");

    encoder.encodeByteString("Value", JsonConversions.toByteString(valueElement));
    encoder.encodeByteString("ValidBits", JsonConversions.toByteString(validBitsElement));
  }
}
