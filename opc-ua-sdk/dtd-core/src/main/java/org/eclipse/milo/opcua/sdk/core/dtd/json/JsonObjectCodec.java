/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd.json;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.dtd.AbstractBsdCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.opcfoundation.opcua.binaryschema.StructuredType;

public class JsonObjectCodec extends AbstractBsdCodec<JsonObject, JsonElement> {

  private final Gson gson;

  public JsonObjectCodec(
      String namespaceUri, NodeId dataTypeId, NodeId encodingId, StructuredType structuredType) {

    super(namespaceUri, dataTypeId, encodingId, structuredType);

    gson = new GsonBuilder().setPrettyPrinting().create();
  }

  @Override
  protected JsonObject createStructure(String name, LinkedHashMap<String, JsonElement> members) {
    JsonObject object = new JsonObject();

    members.forEach(object::add);

    return object;
  }

  @Override
  protected JsonElement opcUaToMemberTypeScalar(String name, Object value, String typeName) {
    if (value == null) {
      return JsonNull.INSTANCE;
    } else if (value instanceof Number) {
      if (value instanceof UByte) {
        return new JsonPrimitive(((UByte) value).shortValue());
      } else if (value instanceof UShort) {
        return new JsonPrimitive(((UShort) value).intValue());
      } else if (value instanceof UInteger) {
        return new JsonPrimitive(((UInteger) value).longValue());
      } else if (value instanceof ULong) {
        return new JsonPrimitive(((ULong) value).toBigInteger());
      } else {
        return new JsonPrimitive((Number) value);
      }
    } else if (value instanceof Boolean) {
      return new JsonPrimitive((Boolean) value);
    } else if (value instanceof String) {
      return new JsonPrimitive((String) value);
    } else if (value instanceof Character) {
      return new JsonPrimitive((Character) value);
    } else if (value instanceof JsonElement) {
      return (JsonElement) value;
    } else if (value instanceof DateTime) {
      return new JsonPrimitive(((DateTime) value).getUtcTime());
    } else if (value instanceof UUID) {
      return new JsonPrimitive(value.toString());
    } else if (value instanceof LocalizedText) {
      return gson.toJsonTree(value);
    } else if (value instanceof QualifiedName) {
      return gson.toJsonTree(value);
    } else if (value instanceof ByteString) {
      ByteString byteString = (ByteString) value;
      byte[] bs = byteString.bytesOrEmpty();
      JsonArray array = new JsonArray();
      for (Byte b : bs) {
        array.add(new JsonPrimitive(b));
      }
      return array;
    } else if (value instanceof XmlElement) {
      String fragment = ((XmlElement) value).fragment();
      return fragment != null ? new JsonPrimitive(fragment) : JsonNull.INSTANCE;
    } else if (value instanceof NodeId) {
      String nodeId = ((NodeId) value).toParseableString();
      return new JsonPrimitive(nodeId);
    } else if (value instanceof ExpandedNodeId) {
      String xNodeId = ((ExpandedNodeId) value).toParseableString();
      return new JsonPrimitive(xNodeId);
    } else if (value instanceof StatusCode) {
      long code = ((StatusCode) value).value();
      return new JsonPrimitive(code);
    } else {
      throw new RuntimeException("could not create JsonElement for value: " + value);
    }
  }

  @Override
  protected JsonElement opcUaToMemberTypeArray(String name, Object values, String typeName) {
    if (values == null) {
      return JsonNull.INSTANCE;
    } else {
      JsonArray array = new JsonArray();

      if (values instanceof Object[]) {
        Object[] objects = (Object[]) values;

        for (Object value : objects) {
          array.add(opcUaToMemberTypeScalar(name, value, typeName));
        }
      } else if (values instanceof Number) {
        // This is a bit array...
        Number number = (Number) values;
        return new JsonPrimitive(number);
      }

      return array;
    }
  }

  @Override
  protected Object memberTypeToOpcUaScalar(JsonElement member, String typeName) {
    if (member == null || member.isJsonNull()) {
      return null;
    } else if (member.isJsonArray()) {
      JsonArray array = member.getAsJsonArray();

      switch (typeName) {
        case "ByteString":
          {
            byte[] bs = new byte[array.size()];

            for (int i = 0; i < array.size(); i++) {
              bs[i] = array.get(i).getAsByte();
            }

            return ByteString.of(bs);
          }

        default:
          return array;
      }
    } else if (member.isJsonObject()) {
      JsonObject jsonObject = member.getAsJsonObject();

      switch (typeName) {
        case "QualifiedName":
          {
            return new QualifiedName(
                jsonObject.get("namespaceIndex").getAsInt(), jsonObject.get("name").getAsString());
          }
        case "LocalizedText":
          {
            return new LocalizedText(
                jsonObject.get("locale").getAsString(), jsonObject.get("text").getAsString());
          }

        default:
          return jsonObject;
      }
    } else if (member.isJsonPrimitive()) {
      JsonPrimitive primitive = member.getAsJsonPrimitive();

      if (primitive.isBoolean()) {
        return primitive.getAsBoolean();
      } else if (primitive.isString()) {
        switch (typeName) {
          case "Guid":
            return UUID.fromString(primitive.getAsString());

          case "NodeId":
            return NodeId.parseSafe(primitive.getAsString()).orElse(NodeId.NULL_VALUE);

          case "ExpandedNodeId":
            return ExpandedNodeId.parse(primitive.getAsString());

          case "XmlElement":
            return new XmlElement(primitive.getAsString());

          default:
            return primitive.getAsString();
        }
      } else if (primitive.isNumber()) {
        switch (typeName) {
          case "Bit":
            return primitive.getAsInt();

          case "SByte":
            return primitive.getAsByte();
          case "Int16":
            return primitive.getAsShort();
          case "Int32":
            return primitive.getAsInt();
          case "Int64":
            return primitive.getAsLong();

          case "Byte":
            return ubyte(primitive.getAsShort());
          case "UInt16":
            return ushort(primitive.getAsInt());
          case "UInt32":
            return uint(primitive.getAsLong());
          case "UInt64":
            return ulong(primitive.getAsBigInteger());

          case "Float":
            return primitive.getAsFloat();
          case "Double":
            return primitive.getAsDouble();

          case "DateTime":
            return new DateTime(primitive.getAsLong());

          case "StatusCode":
            return new StatusCode(primitive.getAsLong());

          default:
            return primitive.getAsNumber();
        }
      }
    }

    return null;
  }

  @Override
  protected Object memberTypeToOpcUaArray(JsonElement member, String typeName) {
    if ("Bit".equals(typeName)) {
      return member.getAsJsonPrimitive().getAsInt();
    } else {
      JsonArray array = member.getAsJsonArray();

      Object[] values = new Object[array.size()];

      for (int i = 0; i < array.size(); i++) {
        JsonElement element = array.get(i);
        values[i] = memberTypeToOpcUaScalar(element, typeName);
      }

      return values;
    }
  }

  @Override
  protected Map<String, JsonElement> getMembers(JsonObject value) {
    LinkedHashMap<String, JsonElement> members = new LinkedHashMap<>();

    value.entrySet().forEach(e -> members.put(e.getKey(), e.getValue()));

    return members;
  }
}
