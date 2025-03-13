/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.json;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;

public class OpcUaJsonDecoder implements UaDecoder {

  private String peekedNextName = null;

  JsonReader jsonReader;
  EncodingContext context;

  public OpcUaJsonDecoder(EncodingContext context) {
    this.context = context;
  }

  public OpcUaJsonDecoder(EncodingContext context, String s) {
    this(context, new StringReader(s));
  }

  public OpcUaJsonDecoder(EncodingContext context, Reader reader) {
    this.context = context;

    reset(reader);
  }

  @Override
  public EncodingContext getEncodingContext() {
    return context;
  }

  public void reset(Reader reader) {
    jsonReader = new JsonReader(reader);
  }

  @Override
  public Boolean decodeBoolean(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return false;
        }
      }

      if (jsonReader.peek() == JsonToken.BOOLEAN) {
        return jsonReader.nextBoolean();
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readBoolean: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Byte decodeSByte(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return (byte) 0;
        }
      }

      if (jsonReader.peek() == JsonToken.NUMBER) {
        return (byte) jsonReader.nextInt();
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readSByte: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Short decodeInt16(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return (short) 0;
        }
      }

      if (jsonReader.peek() == JsonToken.NUMBER) {
        return (short) jsonReader.nextInt();
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readInt16: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Integer decodeInt32(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return 0;
        }
      }

      if (jsonReader.peek() == JsonToken.NUMBER) {
        return jsonReader.nextInt();
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readInt32: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Long decodeInt64(String field) throws UaSerializationException {
    // Int64 and UInt64 values shall be formatted as a decimal number
    // encoded as a JSON string (See the XML encoding of 64-bit values
    // described in 5.3.1.3).

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return 0L;
        }
      }

      if (jsonReader.peek() == JsonToken.STRING) {
        try {
          return Long.valueOf(jsonReader.nextString());
        } catch (NumberFormatException e) {
          throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
        }
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readInt64: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public UByte decodeByte(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return UByte.MIN;
        }
      }

      if (jsonReader.peek() == JsonToken.NUMBER) {
        return ubyte(jsonReader.nextInt());
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readByte: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public UShort decodeUInt16(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return UShort.MIN;
        }
      }

      if (jsonReader.peek() == JsonToken.NUMBER) {
        return ushort(jsonReader.nextInt());
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readUInt16: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public UInteger decodeUInt32(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return UInteger.MIN;
        }
      }

      if (jsonReader.peek() == JsonToken.NUMBER) {
        return uint(jsonReader.nextLong());
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readUInt32: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public ULong decodeUInt64(String field) throws UaSerializationException {
    // Int64 and UInt64 values shall be formatted as a decimal number
    // encoded as a JSON string (See the XML encoding of 64-bit values
    // described in 5.3.1.3).

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return ULong.MIN;
        }
      }

      if (jsonReader.peek() == JsonToken.STRING) {
        try {
          return ULong.valueOf(jsonReader.nextString());
        } catch (NumberFormatException e) {
          throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
        }
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readUInt64: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Float decodeFloat(String field) throws UaSerializationException {
    // Normal Float and Double values shall be encoded as a JSON number.
    // Special floating-point numbers such as positive infinity (INF),
    // negative infinity (-INF) and not-a-number (NaN) shall be
    // represented by the values "Infinity", "-Infinity" and "NaN" encoded
    // as a JSON string.

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return 0.0f;
        }
      }

      switch (jsonReader.peek()) {
        case NUMBER:
          return (float) jsonReader.nextDouble();
        case STRING:
          {
            String s = jsonReader.nextString();
            switch (s) {
              case "Infinity":
                return Float.POSITIVE_INFINITY;
              case "-Infinity":
                return Float.NEGATIVE_INFINITY;
              case "NaN":
                return Float.NaN;
              default:
                throw new UaSerializationException(
                    StatusCodes.Bad_DecodingError, "readFloat: unexpected string value: " + s);
            }
          }
        default:
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "readFloat: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Double decodeDouble(String field) throws UaSerializationException {
    // Normal Float and Double values shall be encoded as a JSON number.
    // Special floating-point numbers such as positive infinity (INF),
    // negative infinity (-INF) and not-a-number (NaN) shall be
    // represented by the values "Infinity", "-Infinity" and "NaN" encoded
    // as a JSON string.

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return 0.0;
        }
      }

      switch (jsonReader.peek()) {
        case NUMBER:
          return jsonReader.nextDouble();
        case STRING:
          {
            String s = jsonReader.nextString();
            switch (s) {
              case "Infinity":
                return Double.POSITIVE_INFINITY;
              case "-Infinity":
                return Double.NEGATIVE_INFINITY;
              case "NaN":
                return Double.NaN;
              default:
                throw new UaSerializationException(
                    StatusCodes.Bad_DecodingError, "readDouble: unexpected string value: " + s);
            }
          }
        default:
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "readDouble: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public String decodeString(String field) throws UaSerializationException {
    // String values shall be encoded as JSON strings.
    // Any characters which are not allowed in JSON strings are escaped
    // using the rules defined in RFC 7159.

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      if (jsonReader.peek() == JsonToken.STRING) {
        return jsonReader.nextString();
      } else if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readString: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public DateTime decodeDateTime(String field) throws UaSerializationException {
    // DateTime values shall be formatted as specified by ISO 8601:2004
    // and encoded as a JSON string.
    // DateTime values which exceed the minimum or maximum values supported
    // on a platform shall be encoded as "0001-01-01T00:00:00Z" or
    // "9999-12-31T23:59:59Z" respectively. During decoding, these values
    // shall be converted to the minimum or maximum values supported on the
    // platform.

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return DateTime.MIN_VALUE;
        }
      }

      if (jsonReader.peek() == JsonToken.STRING) {
        String s = jsonReader.nextString();
        try {
          Instant instant = Instant.parse(s);

          if (instant.isBefore(DateTime.MIN_ISO_8601_INSTANT)) {
            return DateTime.MIN_DATE_TIME;
          } else if (instant.isAfter(DateTime.MAX_ISO_8601_INSTANT)) {
            return DateTime.MAX_DATE_TIME;
          } else {
            return new DateTime(instant);
          }
        } catch (DateTimeParseException e) {
          throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
        }
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readDateTime: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public UUID decodeGuid(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      if (jsonReader.peek() == JsonToken.STRING) {
        String s = jsonReader.nextString();

        return UUID.fromString(s);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readGuid: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public ByteString decodeByteString(String field) throws UaSerializationException {
    // ByteString values shall be formatted as a Base64 text and encoded as
    // a JSON string.
    // Any characters which are not allowed in JSON strings are escaped
    // using the rules defined in RFC 7159.

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      if (jsonReader.peek() == JsonToken.STRING) {
        String s = jsonReader.nextString();
        byte[] bs = Base64.getDecoder().decode(s);

        return ByteString.of(bs);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            "readByteString: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public XmlElement decodeXmlElement(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      if (jsonReader.peek() == JsonToken.STRING) {
        String s = jsonReader.nextString();

        return new XmlElement(s);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            "readXmlElement: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public NodeId decodeNodeId(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return NodeId.NULL_VALUE;
        }
      }

      IdType idType = IdType.Numeric;
      int namespaceIndex = 0;
      Object id = null;

      try {
        jsonReader.beginObject();

        while (jsonReader.peek() == JsonToken.NAME) {
          String propertyName = nextName();
          if (propertyName == null) continue;

          switch (propertyName) {
            case "IdType":
              {
                int value = jsonReader.nextInt();
                idType = IdType.from(value);
                if (idType == null) {
                  throw new UaSerializationException(
                      StatusCodes.Bad_DecodingError, "readNodeId: invalid IdType: " + value);
                }
                break;
              }
            case "Id":
              {
                id = readIdObject(jsonReader, idType);
                break;
              }
            case "Namespace":
              {
                namespaceIndex = jsonReader.nextInt();
                break;
              }
          }
        }

        jsonReader.endObject();

        if (id == null) {
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "readNodeId: id == null");
        } else {
          switch (idType) {
            case Numeric:
              assert id instanceof UInteger;
              return new NodeId(namespaceIndex, (UInteger) id);
            case String:
              assert id instanceof String;
              return new NodeId(namespaceIndex, (String) id);
            case Guid:
              assert id instanceof UUID;
              return new NodeId(namespaceIndex, (UUID) id);
            case Opaque:
              assert id instanceof ByteString;
              return new NodeId(namespaceIndex, (ByteString) id);
            default:
              throw new UaSerializationException(
                  StatusCodes.Bad_DecodingError, "readNodeId: idType: " + idType);
          }
        }
      } catch (IOException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public ExpandedNodeId decodeExpandedNodeId(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return ExpandedNodeId.NULL_VALUE;
        }
      }

      IdType idType = IdType.Numeric;
      Object id = null;
      int namespaceIndex = 0;
      String namespaceUri = null;
      int serverIndex = 0;

      try {
        jsonReader.beginObject();

        while (jsonReader.peek() == JsonToken.NAME) {
          String propertyName = nextName();
          if (propertyName == null) continue;

          switch (propertyName) {
            case "IdType":
              {
                int value = jsonReader.nextInt();
                idType = IdType.from(value);
                if (idType == null) {
                  throw new UaSerializationException(
                      StatusCodes.Bad_DecodingError,
                      "readExpandedNodeId: invalid IdType: " + value);
                }
                break;
              }
            case "Id":
              {
                id = readIdObject(jsonReader, idType);
                break;
              }
            case "Namespace":
              {
                switch (jsonReader.peek()) {
                  case NUMBER:
                    namespaceIndex = jsonReader.nextInt();
                    break;
                  case STRING:
                    namespaceUri = jsonReader.nextString();
                    break;
                  default:
                    throw new UaSerializationException(
                        StatusCodes.Bad_DecodingError,
                        "readExpandedNodeId: unexpected Namespace token: " + jsonReader.peek());
                }
                break;
              }
            case "ServerUri":
              {
                serverIndex = jsonReader.nextInt();
                break;
              }
          }
        }

        jsonReader.endObject();

        if (id == null) {
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "readExpandedNodeId: id == null");
        } else {
          if (namespaceUri == null) {
            return new ExpandedNodeId(ushort(namespaceIndex), null, id, uint(serverIndex));
          } else {
            return new ExpandedNodeId(ushort(namespaceIndex), namespaceUri, id, uint(serverIndex));
          }
        }
      } catch (IOException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  /**
   * Read the {@code id} Object for a {@link NodeId} or {@link ExpandedNodeId}.
   *
   * @param jsonReader the {@link JsonReader} to read from.
   * @param idType the expected {@link IdType}.
   * @return the {@code id} Object read from {@code jsonReader}.
   * @throws IOException if {@code jsonReader} throws while reading the next token.
   */
  private static Object readIdObject(JsonReader jsonReader, IdType idType) throws IOException {
    Object id = null;
    switch (idType) {
      case Numeric:
        id = uint(jsonReader.nextInt());
        break;
      case String:
        id = jsonReader.nextString();
        break;
      case Guid:
        id = UUID.fromString(jsonReader.nextString());
        break;
      case Opaque:
        id = ByteString.of(Base64.getDecoder().decode(jsonReader.nextString()));
        break;
    }
    return id;
  }

  @Override
  public StatusCode decodeStatusCode(String field) throws UaSerializationException {
    // StatusCode values shall be encoded as a JSON number for the
    // reversible encoding.

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return StatusCode.GOOD;
        }
      }

      if (jsonReader.peek() == JsonToken.NUMBER) {
        long value = jsonReader.nextLong();

        return new StatusCode(value);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            "readStatusCode: unexpected token: " + jsonReader.peek());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public QualifiedName decodeQualifiedName(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return QualifiedName.NULL_VALUE;
        }
      }

      jsonReader.beginObject();

      String name = null;
      int namespaceIndex = 0;

      while (jsonReader.peek() == JsonToken.NAME) {
        String nextName = nextName();
        if (nextName == null) continue;

        switch (nextName) {
          case "Name":
            {
              if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
              } else {
                name = jsonReader.nextString();
              }
              break;
            }
          case "Uri":
            namespaceIndex = jsonReader.nextInt();
            break;
          default:
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                String.format("readQualifiedName: unexpected field: " + nextName));
        }
      }

      jsonReader.endObject();

      return new QualifiedName(namespaceIndex, name);
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public LocalizedText decodeLocalizedText(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return LocalizedText.NULL_VALUE;
        }
      }

      jsonReader.beginObject();

      String locale = null;
      String text = null;

      while (jsonReader.peek() == JsonToken.NAME) {
        String nextName = nextName();
        if (nextName == null) continue;

        switch (nextName) {
          case "Locale":
            locale = jsonReader.nextString();
            break;
          case "Text":
            text = jsonReader.nextString();
            break;
          default:
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                String.format("readLocalizedText: unexpected field: " + nextName));
        }
      }

      jsonReader.endObject();

      return new LocalizedText(locale, text);
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public ExtensionObject decodeExtensionObject(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }

      jsonReader.beginObject();

      NodeId encodingId = null;
      int encoding = 0;
      Object body = null;

      while (jsonReader.peek() == JsonToken.NAME) {
        String nextName = nextName();
        if (nextName == null) continue;

        switch (nextName) {
          case "TypeId":
            encodingId = decodeNodeId(null);
            break;
          case "Encoding":
            encoding = jsonReader.nextInt();
            break;
          case "Body":
            body =
                switch (encoding) {
                  case 0 -> JsonParser.parseReader(jsonReader);
                  case 1 -> decodeByteString(null);
                  case 2 -> decodeXmlElement(null);
                  default -> body;
                };
            break;
          default:
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                String.format("readExtensionObject: unexpected field: " + nextName));
        }
      }

      jsonReader.endObject();

      if (encodingId == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "readExtensionObject: encodingId == null");
      } else {
        return switch (encoding) {
          case 0 -> {
            assert body instanceof JsonElement;
            yield ExtensionObject.of(body.toString(), encodingId);
          }
          case 1 -> {
            assert body instanceof ByteString;
            yield ExtensionObject.of((ByteString) body, encodingId);
          }
          case 2 -> {
            assert body instanceof XmlElement;
            yield ExtensionObject.of((XmlElement) body, encodingId);
          }
          default ->
              throw new UaSerializationException(
                  StatusCodes.Bad_DecodingError,
                  "readExtensionObject: unexpected encoding: " + encoding);
        };
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public DataValue decodeDataValue(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      jsonReader.beginObject();

      DataValue.Builder b = DataValue.newValue();
      b.setStatus(StatusCode.GOOD);

      while (jsonReader.peek() == JsonToken.NAME) {
        String nextName = nextName();
        if (nextName == null) continue;

        switch (nextName) {
          case "Value":
            b.setValue(decodeVariant(null));
            break;

          case "Status":
            b.setStatus(decodeStatusCode(null));
            break;

          case "SourceTimestamp":
            b.setSourceTime(decodeDateTime(null));
            break;

          case "SourcePicoseconds":
            b.setSourcePicoseconds(decodeUInt16(null));
            break;

          case "ServerTimestamp":
            b.setServerTime(decodeDateTime(null));
            break;

          case "ServerPicoseconds":
            b.setServerPicoseconds(decodeUInt16(null));
            break;
        }
      }

      jsonReader.endObject();

      return b.build();
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Variant decodeVariant(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return Variant.NULL_VALUE;
        }
      }

      jsonReader.beginObject();

      int typeId = 0;
      JsonElement bodyElement = null;
      int[] dimensions = null;

      while (jsonReader.peek() == JsonToken.NAME) {
        String nextName = nextName();
        if (nextName == null) continue;

        switch (nextName) {
          case "Type":
            typeId = jsonReader.nextInt();
            break;
          case "Body":
            bodyElement = JsonParser.parseReader(jsonReader);
            break;
          case "Dimensions":
            var dims = new ArrayList<Integer>();
            jsonReader.beginArray();
            while (jsonReader.peek() == JsonToken.NUMBER) {
              dims.add(jsonReader.nextInt());
            }
            jsonReader.endArray();
            dimensions = new int[dims.size()];
            for (int i = 0; i < dims.size(); i++) {
              dimensions[i] = dims.get(i);
            }
            break;
        }
      }

      jsonReader.endObject();

      if (bodyElement == null) {
        return Variant.NULL_VALUE;
      } else if (dimensions == null) {
        // scalar or one-dimensional array value
        JsonReader reader = jsonReader;
        try {
          jsonReader = new JsonReader(new StringReader(bodyElement.toString()));

          if (bodyElement.isJsonArray()) {
            var elements = new ArrayList<>();
            jsonReader.beginArray();
            while (jsonReader.peek() != JsonToken.END_ARRAY) {
              elements.add(readBuiltinTypeValue(null, typeId));
            }
            jsonReader.endArray();
            Object value =
                Array.newInstance(OpcUaDataType.getPrimitiveBackingClass(typeId), elements.size());
            for (int i = 0; i < elements.size(); i++) {
              Array.set(value, i, elements.get(i));
            }
            return new Variant(value);
          } else {
            Object value = readBuiltinTypeValue(null, typeId);
            return new Variant(value);
          }
        } finally {
          jsonReader = reader;
        }
      } else {
        // multi-dimensional array value
        JsonReader reader = jsonReader;
        try {
          jsonReader = new JsonReader(new StringReader(bodyElement.toString()));

          var elements = new ArrayList<>();
          jsonReader.beginArray();
          while (jsonReader.peek() != JsonToken.END_ARRAY) {
            elements.add(readBuiltinTypeValue(null, typeId));
          }
          jsonReader.endArray();

          Object flatArray =
              Array.newInstance(OpcUaDataType.getPrimitiveBackingClass(typeId), elements.size());
          for (int i = 0; i < elements.size(); i++) {
            Array.set(flatArray, i, elements.get(i));
          }

          var matrix = new Matrix(flatArray, dimensions, OpcUaDataType.fromTypeId(typeId));

          return new Variant(matrix);
        } finally {
          jsonReader = reader;
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  private Object readFlattenedMultiDimensionalVariantValue(int typeId, int[] dimensions)
      throws IOException {
    jsonReader.beginArray();
    try {
      return readFlattenedMultiDimensionalVariantValue(typeId, dimensions, 0);
    } finally {
      jsonReader.endArray();
    }
  }

  private Object readFlattenedMultiDimensionalVariantValue(
      int typeId, int[] dimensions, int dimensionIndex) {
    Object value;

    if (dimensionIndex == dimensions.length - 1) {
      value =
          Array.newInstance(
              OpcUaDataType.getPrimitiveBackingClass(typeId), dimensions[dimensionIndex]);
      for (int i = 0; i < dimensions[dimensionIndex]; i++) {
        Object e = readBuiltinTypeValue(null, typeId);
        Array.set(value, i, e);
      }
    } else {
      value =
          Array.newInstance(
              OpcUaDataType.getPrimitiveBackingClass(typeId),
              Arrays.copyOfRange(dimensions, dimensionIndex, dimensions.length));
      for (int i = 0; i < dimensions[dimensionIndex]; i++) {
        Object e =
            readFlattenedMultiDimensionalVariantValue(typeId, dimensions, dimensionIndex + 1);
        Array.set(value, i, e);
      }
    }

    return value;
  }

  private Object readBuiltinTypeValue(String field, int typeId) throws UaSerializationException {
    switch (typeId) {
      case 1:
        return decodeBoolean(field);
      case 2:
        return decodeSByte(field);
      case 3:
        return decodeByte(field);
      case 4:
        return decodeInt16(field);
      case 5:
        return decodeUInt16(field);
      case 6:
        return decodeInt32(field);
      case 7:
        return decodeUInt32(field);
      case 8:
        return decodeInt64(field);
      case 9:
        return decodeUInt64(field);
      case 10:
        return decodeFloat(field);
      case 11:
        return decodeDouble(field);
      case 12:
        return decodeString(field);
      case 13:
        return decodeDateTime(field);
      case 14:
        return decodeGuid(field);
      case 15:
        return decodeByteString(field);
      case 16:
        return decodeXmlElement(field);
      case 17:
        return decodeNodeId(field);
      case 18:
        return decodeExpandedNodeId(field);
      case 19:
        return decodeStatusCode(field);
      case 20:
        return decodeQualifiedName(field);
      case 21:
        return decodeLocalizedText(field);
      case 22:
        return decodeExtensionObject(field);
      case 23:
        return decodeDataValue(field);
      case 24:
        return decodeVariant(field);
      case 25:
        return decodeDiagnosticInfo(field);

      default:
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "not a built-in type: " + typeId);
    }
  }

  @Override
  public DiagnosticInfo decodeDiagnosticInfo(String field) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return DiagnosticInfo.NULL_VALUE;
        }
      }

      jsonReader.beginObject();

      int symbolicId = 0;
      int namespaceUri = 0;
      int locale = 0;
      int localizedText = 0;
      String additionalInfo = null;
      StatusCode innerStatusCode = null;
      DiagnosticInfo innerDiagnosticInfo = null;

      while (jsonReader.peek() == JsonToken.NAME) {
        String nextName = nextName();
        if (nextName == null) continue;

        switch (nextName) {
          case "SymbolicId":
            symbolicId = jsonReader.nextInt();
            break;
          case "NamespaceUri":
            namespaceUri = jsonReader.nextInt();
            break;
          case "Locale":
            locale = jsonReader.nextInt();
            break;
          case "LocalizedText":
            localizedText = jsonReader.nextInt();
            break;
          case "AdditionalInfo":
            additionalInfo = jsonReader.nextString();
            break;
          case "InnerStatusCode":
            innerStatusCode = decodeStatusCode(null);
            break;
          case "InnerDiagnosticInfo":
            innerDiagnosticInfo = decodeDiagnosticInfo(null);
            break;
        }
      }

      jsonReader.endObject();

      return new DiagnosticInfo(
          namespaceUri,
          symbolicId,
          locale,
          localizedText,
          additionalInfo,
          innerStatusCode,
          innerDiagnosticInfo);
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public UaMessageType decodeMessage(String field) throws UaSerializationException {
    try {
      ExtensionObject xo = decodeExtensionObject(field);

      return (UaMessageType) xo.decode(context);
    } catch (ClassCastException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Integer decodeEnum(String field) {
    return decodeInt32(field);
  }

  @Override
  public Object decodeStruct(String field, NodeId dataTypeId) throws UaSerializationException {
    DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

    if (codec != null) {
      try {
        if (field != null) {
          String nextName = nextName();
          if (!field.equals(nextName)) {
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                String.format("readStruct: %s != %s", field, nextName));
          }
        }

        Object value;

        jsonReader.beginObject();
        value = codec.decode(context, this);
        jsonReader.endObject();

        return value;
      } catch (IOException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "readStruct: no codec registered: " + dataTypeId);
    }
  }

  @Override
  public Object decodeStruct(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_DecodingError,
                        "readStruct: namespace not registered: " + dataTypeId));

    return decodeStruct(field, localDataTypeId);
  }

  @Override
  public Object decodeStruct(String field, DataTypeCodec codec) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError,
              String.format("readStruct: %s != %s", field, nextName));
        }
      }

      Object value;

      jsonReader.beginObject();
      value = codec.decode(context, this);
      jsonReader.endObject();

      return value;
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Boolean[] decodeBooleanArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeBoolean, Boolean.class);
  }

  @Override
  public Byte[] decodeSByteArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeSByte, Byte.class);
  }

  @Override
  public Short[] decodeInt16Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeInt16, Short.class);
  }

  @Override
  public Integer[] decodeInt32Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeInt32, Integer.class);
  }

  @Override
  public Long[] decodeInt64Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeInt64, Long.class);
  }

  @Override
  public UByte[] decodeByteArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeByte, UByte.class);
  }

  @Override
  public UShort[] decodeUInt16Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeUInt16, UShort.class);
  }

  @Override
  public UInteger[] decodeUInt32Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeUInt32, UInteger.class);
  }

  @Override
  public ULong[] decodeUInt64Array(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeUInt64, ULong.class);
  }

  @Override
  public Float[] decodeFloatArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeFloat, Float.class);
  }

  @Override
  public Double[] decodeDoubleArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDouble, Double.class);
  }

  @Override
  public String[] decodeStringArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeString, String.class);
  }

  @Override
  public DateTime[] decodeDateTimeArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDateTime, DateTime.class);
  }

  @Override
  public UUID[] decodeGuidArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeGuid, UUID.class);
  }

  @Override
  public ByteString[] decodeByteStringArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeByteString, ByteString.class);
  }

  @Override
  public XmlElement[] decodeXmlElementArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeXmlElement, XmlElement.class);
  }

  @Override
  public NodeId[] decodeNodeIdArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeNodeId, NodeId.class);
  }

  @Override
  public ExpandedNodeId[] decodeExpandedNodeIdArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeExpandedNodeId, ExpandedNodeId.class);
  }

  @Override
  public StatusCode[] decodeStatusCodeArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeStatusCode, StatusCode.class);
  }

  @Override
  public QualifiedName[] decodeQualifiedNameArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeQualifiedName, QualifiedName.class);
  }

  @Override
  public LocalizedText[] decodeLocalizedTextArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeLocalizedText, LocalizedText.class);
  }

  @Override
  public ExtensionObject[] decodeExtensionObjectArray(String field)
      throws UaSerializationException {
    return decodeArray(field, this::decodeExtensionObject, ExtensionObject.class);
  }

  @Override
  public DataValue[] decodeDataValueArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDataValue, DataValue.class);
  }

  @Override
  public Variant[] decodeVariantArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeVariant, Variant.class);
  }

  @Override
  public DiagnosticInfo[] decodeDiagnosticInfoArray(String field) throws UaSerializationException {
    return decodeArray(field, this::decodeDiagnosticInfo, DiagnosticInfo.class);
  }

  @Override
  public Integer[] decodeEnumArray(String field) throws UaSerializationException {
    return decodeInt32Array(field);
  }

  @Override
  public Object[] decodeStructArray(String field, NodeId dataTypeId)
      throws UaSerializationException {
    DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "readStructArray: no codec registered: " + dataTypeId);
    }

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      var elements = new ArrayList<>();

      jsonReader.beginArray();
      while (jsonReader.peek() != JsonToken.END_ARRAY) {
        elements.add(decodeStruct(null, dataTypeId));
      }
      jsonReader.endArray();

      Object array = Array.newInstance(codec.getType(), elements.size());
      for (int i = 0; i < elements.size(); i++) {
        Array.set(array, i, elements.get(i));
      }

      return (Object[]) array;
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Object[] decodeStructArray(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_DecodingError,
                        "readStructArray: no codec registered: " + dataTypeId));

    return decodeStructArray(field, localDataTypeId);
  }

  @Override
  public <T> T[] decodeArray(String field, Function<String, T> decoder, Class<T> clazz)
      throws UaSerializationException {

    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return null;
        }
      }

      var elements = new ArrayList<T>();

      jsonReader.beginArray();
      while (jsonReader.peek() != JsonToken.END_ARRAY) {
        elements.add(decoder.apply(null));
      }
      jsonReader.endArray();

      Object array = Array.newInstance(clazz, elements.size());
      for (int i = 0; i < elements.size(); i++) {
        Array.set(array, i, elements.get(i));
      }

      //noinspection unchecked
      return (T[]) array;
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Matrix decodeMatrix(String field, OpcUaDataType dataType) throws UaSerializationException {
    try {
      if (field != null) {
        String nextName = nextName();
        if (!field.equals(nextName)) {
          this.peekedNextName = nextName;
          return Matrix.ofNull();
        }
      }

      Object nestedArray = decodeNestedMultiDimensionalArrayBuiltinValue(dataType.getTypeId());

      return new Matrix(nestedArray);
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
    }
  }

  @Override
  public Matrix decodeEnumMatrix(String field) throws UaSerializationException {
    return decodeMatrix(field, OpcUaDataType.Int32);
  }

  @Override
  public Matrix decodeStructMatrix(String field, NodeId dataTypeId)
      throws UaSerializationException {
    DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

    if (codec != null) {
      try {
        if (field != null) {
          String nextName = nextName();
          if (!field.equals(nextName)) {
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                String.format("readStruct: %s != %s", field, nextName));
          }
        }

        Object nestedArray = decodeNestedMultiDimensionalArrayStructValue(codec);

        return new Matrix(nestedArray);
      } catch (IOException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "decodeStructMatrix: no codec registered: " + dataTypeId);
    }
  }

  @Override
  public Matrix decodeStructMatrix(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_DecodingError,
                        "decodeStructMatrix: namespace not registered: " + dataTypeId));

    return decodeStructMatrix(field, localDataTypeId);
  }

  private Object decodeNestedMultiDimensionalArrayBuiltinValue(int typeId) throws IOException {
    if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
      jsonReader.beginArray();
      List<Object> elements = new ArrayList<>();
      while (jsonReader.peek() != JsonToken.END_ARRAY) {
        elements.add(decodeNestedMultiDimensionalArrayBuiltinValue(typeId));
      }
      jsonReader.endArray();

      Object array = Array.newInstance(elements.get(0).getClass(), elements.size());
      for (int i = 0; i < elements.size(); i++) {
        Array.set(array, i, elements.get(i));
      }
      return array;
    } else {
      return readBuiltinTypeValue(null, typeId);
    }
  }

  private Object decodeNestedMultiDimensionalArrayStructValue(DataTypeCodec codec)
      throws IOException {
    if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
      jsonReader.beginArray();
      List<Object> elements = new ArrayList<>();
      while (jsonReader.peek() != JsonToken.END_ARRAY) {
        elements.add(decodeNestedMultiDimensionalArrayStructValue(codec));
      }
      jsonReader.endArray();

      Object array = Array.newInstance(elements.get(0).getClass(), elements.size());
      for (int i = 0; i < elements.size(); i++) {
        Array.set(array, i, elements.get(i));
      }
      return array;
    } else {
      return decodeStruct(null, codec);
    }
  }

  /**
   * This ugly hack is so `readArray` can "push back" the next name it reads if it doesn't match the
   * expected field name, because that probably means it was omitted due to being null.
   */
  private String nextName() throws IOException {
    if (peekedNextName != null) {
      String next = peekedNextName;
      peekedNextName = null;
      return next;
    } else {
      return jsonReader.peek() == JsonToken.NAME ? jsonReader.nextName() : null;
    }
  }
}
