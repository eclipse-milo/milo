/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import com.google.gson.JsonArray;
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

    var valueArray = new JsonArray();
    for (byte b : value.bytesOrEmpty()) {
      valueArray.add(JsonConversions.fromSByte(b));
    }
    var validBitsArray = new JsonArray();
    for (byte b : validBits.bytesOrEmpty()) {
      validBitsArray.add(JsonConversions.fromSByte(b));
    }

    var jsonObject = new JsonObject();
    jsonObject.add("Value", valueArray);
    jsonObject.add("ValidBits", validBitsArray);

    return new JsonStruct(dataType, jsonObject);
  }

  @Override
  public void encodeType(EncodingContext context, UaEncoder encoder, JsonStruct value)
      throws UaSerializationException {
    JsonArray valueArray = value.getJsonObject().getAsJsonArray("Value");
    JsonArray validBitsArray = value.getJsonObject().getAsJsonArray("ValidBits");

    byte[] valueBs = new byte[valueArray.size()];
    for (int i = 0; i < valueArray.size(); i++) {
      valueBs[i] = JsonConversions.toSByte(valueArray.get(i));
    }

    byte[] validBitsBs = new byte[validBitsArray.size()];
    for (int i = 0; i < validBitsArray.size(); i++) {
      validBitsBs[i] = JsonConversions.toSByte(validBitsArray.get(i));
    }

    encoder.encodeByteString("Value", new ByteString(valueBs));
    encoder.encodeByteString("ValidBits", new ByteString(validBitsBs));
  }
}
