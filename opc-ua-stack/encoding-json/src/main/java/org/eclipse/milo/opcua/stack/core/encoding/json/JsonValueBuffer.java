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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;

/** Buffers JSON without discarding duplicate names or rounding numeric tokens. */
final class JsonValueBuffer {
  private JsonValueBuffer() {}

  static JsonReader newReader(Reader input, EncodingLimits limits) {
    // Reader input has no byte count. Bound UTF-16 code units, including whitespace and names,
    // before Gson allocates strings. Retain a finite buffer budget for unlimited message sizes.
    int maxCharacters = limits.getMaxMessageSize();
    if (maxCharacters <= 0) maxCharacters = EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE;
    final int limit = maxCharacters;
    return new JsonReader(
        new FilterReader(input) {
          private long remaining = limit;

          @Override
          public int read(char[] buffer, int offset, int length) throws IOException {
            int count = super.read(buffer, offset, (int) Math.min(length, remaining + 1));
            if (count > 0) {
              remaining -= count;
              if (remaining < 0) {
                throw new UaSerializationException(
                    StatusCodes.Bad_EncodingLimitsExceeded,
                    "JSON input exceeds character limit: " + limit);
              }
            }
            return count;
          }
        });
  }

  static JsonElement read(JsonReader reader, int maxDepth) throws IOException {
    return read(reader, 0, maxDepth);
  }

  private static JsonElement read(JsonReader reader, int depth, int maxDepth) throws IOException {
    JsonToken token = reader.peek();
    if (token == JsonToken.BEGIN_OBJECT || token == JsonToken.BEGIN_ARRAY) {
      if (depth >= maxDepth) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingLimitsExceeded, "JSON nesting exceeds limit: " + maxDepth);
      }
    }
    return switch (token) {
      case BEGIN_OBJECT -> {
        var object = new JsonObject();
        reader.beginObject();
        while (reader.hasNext()) {
          String name = reader.nextName();
          if (object.has(name)) {
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError, "Duplicate JSON field: " + name);
          }
          object.add(name, read(reader, depth + 1, maxDepth));
        }
        reader.endObject();
        yield object;
      }
      case BEGIN_ARRAY -> {
        var array = new JsonArray();
        reader.beginArray();
        while (reader.hasNext()) array.add(read(reader, depth + 1, maxDepth));
        reader.endArray();
        yield array;
      }
      // Parsing only the number token retains its lexeme (including -0 and large integers).
      case NUMBER -> JsonParser.parseString(reader.nextString());
      case STRING -> new JsonPrimitive(reader.nextString());
      case BOOLEAN -> new JsonPrimitive(reader.nextBoolean());
      case NULL -> {
        reader.nextNull();
        yield JsonNull.INSTANCE;
      }
      default ->
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "Expected JSON value, got: " + token);
    };
  }
}
