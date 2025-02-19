/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.json;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Stack;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.eclipse.milo.opcua.stack.core.util.TypeUtil;
import org.jspecify.annotations.NonNull;

public class OpcUaJsonEncoder implements UaEncoder {

  enum EncoderContext {
    BUILTIN,
    STRUCT
  }

  private final Stack<EncoderContext> contextStack = new Stack<>();

  boolean reversible = true;
  JsonWriter jsonWriter;
  EncodingContext encodingContext;

  public OpcUaJsonEncoder(EncodingContext encodingContext) {
    this.encodingContext = encodingContext;
  }

  public OpcUaJsonEncoder(EncodingContext encodingContext, Writer writer) {
    this.encodingContext = encodingContext;

    reset(writer);
  }

  @Override
  public EncodingContext getEncodingContext() {
    return encodingContext;
  }

  /**
   * Reset this encoder with a new {@link Writer} to write to.
   *
   * @param writer the new {@link Writer} to write to.
   */
  public void reset(Writer writer) {
    jsonWriter = new JsonWriter(writer);
    jsonWriter.setHtmlSafe(false);
  }

  /**
   * Reset this encoder with a new {@link OutputStream} to write to.
   *
   * @param outputStream the new {@link OutputStream} to write to.
   */
  public void reset(OutputStream outputStream) {
    jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream));
    jsonWriter.setHtmlSafe(false);
  }

  private EncoderContext contextPeek() {
    return contextStack.isEmpty() ? EncoderContext.BUILTIN : contextStack.peek();
  }

  private void contextPush(EncoderContext context) {
    contextStack.push(context);
  }

  private EncoderContext contextPop() {
    return contextStack.pop();
  }

  @Override
  public void encodeBoolean(String field, Boolean value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value);
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeSByte(String field, Byte value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != 0) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value);
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeInt16(String field, Short value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != 0) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value);
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeInt32(String field, Integer value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != 0) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value);
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeInt64(String field, Long value) throws UaSerializationException {
    // Int64 and UInt64 values shall be formatted as a decimal number
    // encoded as a JSON string.

    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != 0) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value.toString());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeByte(String field, UByte value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value.intValue() != 0) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value.shortValue());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeUInt16(String field, UShort value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value.intValue() != 0) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value.intValue());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeUInt32(String field, UInteger value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value.longValue() != 0L) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value.longValue());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeUInt64(String field, ULong value) throws UaSerializationException {
    // Int64 and UInt64 values shall be formatted as a decimal number
    // encoded as a JSON string (See the XML encoding of 64-bit values
    // described in 5.3.1.3).

    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value.longValue() != 0L) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value.toString());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeFloat(String field, Float value) throws UaSerializationException {
    // Normal Float and Double values shall be encoded as a JSON number.
    // Special floating-point numbers such as positive infinity (INF),
    // negative infinity (-INF) and not-a-number (NaN) shall be
    // represented by the values "Infinity", "-Infinity" and "NaN" encoded
    // as a JSON string.

    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != 0.0f) {
        if (field != null) {
          jsonWriter.name(field);
        }
        if (value == Float.POSITIVE_INFINITY) {
          jsonWriter.value("Infinity");
        } else if (value == Float.NEGATIVE_INFINITY) {
          jsonWriter.value("-Infinity");
        } else if (value.isNaN()) {
          jsonWriter.value("NaN");
        } else {
          jsonWriter.value(value);
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeDouble(String field, Double value) throws UaSerializationException {
    // Normal Float and Double values shall be encoded as a JSON number.
    // Special floating-point numbers such as positive infinity (INF),
    // negative infinity (-INF) and not-a-number (NaN) shall be
    // represented by the values "Infinity", "-Infinity" and "NaN" encoded
    // as a JSON string.

    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != 0.0d) {
        if (field != null) {
          jsonWriter.name(field);
        }
        if (value == Double.POSITIVE_INFINITY) {
          jsonWriter.value("Infinity");
        } else if (value == Double.NEGATIVE_INFINITY) {
          jsonWriter.value("-Infinity");
        } else if (value.isNaN()) {
          jsonWriter.value("NaN");
        } else {
          jsonWriter.value(value);
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeString(String field, String value) throws UaSerializationException {
    // String values shall be encoded as JSON strings.
    // Any characters which are not allowed in JSON strings are escaped
    // using the rules defined in RFC 7159.

    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != null) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value);
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeDateTime(String field, DateTime value) throws UaSerializationException {
    // DateTime values shall be formatted as specified by ISO 8601:2004
    // and encoded as a JSON string.
    // DateTime values which exceed the minimum or maximum values supported
    // on a platform shall be encoded as "0001-01-01T00:00:00Z" or
    // "9999-12-31T23:59:59Z" respectively. During decoding, these values
    // shall be converted to the minimum or maximum values supported on the
    // platform.

    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || (value != null && value.isValid())) {
        if (field != null) {
          jsonWriter.name(field);
        }

        if (value.getJavaInstant().isBefore(DateTime.MIN_ISO_8601_INSTANT)) {
          jsonWriter.value(DateTime.MIN_ISO_8601_STRING);
        } else if (value.getJavaInstant().isAfter(DateTime.MAX_ISO_8601_INSTANT)) {
          jsonWriter.value(DateTime.MAX_ISO_8601_STRING);
        } else {
          jsonWriter.value(DateTimeFormatter.ISO_INSTANT.format(value.getJavaInstant()));
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeGuid(String field, UUID value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null
              && value.getLeastSignificantBits() != 0L
              && value.getMostSignificantBits() != 0L)) {

        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value.toString().toUpperCase());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeByteString(String field, ByteString value) throws UaSerializationException {
    // ByteString values shall be formatted as a Base64 text and encoded as
    // a JSON string.
    // Any characters which are not allowed in JSON strings are escaped
    // using the rules defined in RFC 7159.

    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(Base64.getEncoder().encodeToString(value.bytesOrEmpty()));
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeXmlElement(String field, XmlElement value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.value(value.getFragmentOrEmpty());
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeNodeId(String field, NodeId value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.beginObject();
        writeNodeIdCommonFields(
            value.getType(), value.getIdentifier(), value.getNamespaceIndex().intValue());
        jsonWriter.endObject();
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeExpandedNodeId(String field, ExpandedNodeId value)
      throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }
        jsonWriter.beginObject();
        if (value.isAbsolute()) {
          writeNodeIdCommonFields(value.getType(), value.getIdentifier(), 0);
          jsonWriter.name("Namespace").value(value.getNamespaceUri());
        } else {
          writeNodeIdCommonFields(
              value.getType(), value.getIdentifier(), value.getNamespaceIndex().intValue());
        }
        if (!value.isLocal()) {
          int serverIndex = value.getServerIndex().intValue();
          if (reversible) {
            jsonWriter.name("ServerUri").value(serverIndex);
          } else {
            String serverUri = encodingContext.getServerTable().get(serverIndex);
            if (serverUri != null) {
              jsonWriter.name("ServerUri").value(serverUri);
            } else {
              jsonWriter.name("ServerUri").value(serverIndex);
            }
          }
        }
        jsonWriter.endObject();
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  /** Writes the fields that are common to both NodeId and ExpandedNodeId. */
  private void writeNodeIdCommonFields(IdType idType, Object identifier, int namespaceIndex)
      throws IOException {
    if (idType != IdType.Numeric) {
      jsonWriter.name("IdType").value(idType.getValue());
    }
    jsonWriter.name("Id");
    switch (idType) {
      case Numeric:
        jsonWriter.value((Number) identifier);
        break;
      case String:
        jsonWriter.value((String) identifier);
        break;
      case Guid:
        {
          UUID uuid = (UUID) identifier;
          jsonWriter.value(uuid.toString().toUpperCase());
          break;
        }
      case Opaque:
        {
          ByteString bs = (ByteString) identifier;
          jsonWriter.value(Base64.getEncoder().encodeToString(bs.bytesOrEmpty()));
          break;
        }
    }
    if (namespaceIndex > 0) {
      if (reversible || namespaceIndex == 1) {
        jsonWriter.name("Namespace").value(namespaceIndex);
      } else {
        String namespaceUri = encodingContext.getNamespaceTable().get(namespaceIndex);
        if (namespaceUri != null) {
          jsonWriter.name("Namespace").value(namespaceUri);
        } else {
          jsonWriter.name("Namespace").value(namespaceIndex);
        }
      }
    }
  }

  @Override
  public void encodeStatusCode(String field, StatusCode value) throws UaSerializationException {
    // StatusCode values shall be encoded as a JSON number for the
    // reversible encoding.
    //
    // For the non-reversible form, StatusCode values shall be encoded as
    // a JSON object with the fields defined as follows:
    //
    // "Code":
    // The numeric code encoded as a JSON number.
    // The Code is omitted if the numeric code is 0 (Good).
    //
    // "Symbol":
    // The string literal associated with the numeric code encoded as JSON
    // string. e.g. 0x80AB0000 has the associated literal
    // “BadInvalidArgument”.
    // The Symbol is omitted if the numeric code is 0 (Good).
    //
    // A StatusCode of Good (0) is treated like a NULL and not encoded. If
    // it is an element of an JSON array it is encoded as the JSON literal
    // `null`.

    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || (value != null && !value.isGood())) {
        long code = value.getValue();

        if (reversible) {
          if (field != null) {
            jsonWriter.name(field);
          }

          jsonWriter.value(code);
        } else {
          if (code != 0L) {
            if (field != null) {
              jsonWriter.name(field);
            }

            String symbol = StatusCodes.lookup(code).map(ss -> ss[0]).orElse("");

            jsonWriter.beginObject();
            jsonWriter.name("Code").value(code);
            jsonWriter.name("Symbol").value(symbol);
            jsonWriter.endObject();
          }
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeQualifiedName(String field, QualifiedName value)
      throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }

        jsonWriter.beginObject();
        jsonWriter.name("Name").value(value.getName());
        int namespaceIndex = value.getNamespaceIndex().intValue();
        if (namespaceIndex > 0) {
          if (reversible || namespaceIndex == 1) {
            jsonWriter.name("Uri").value(namespaceIndex);
          } else {
            String namespaceUri = encodingContext.getNamespaceTable().get(namespaceIndex);
            if (namespaceUri != null) {
              jsonWriter.name("Uri").value(namespaceUri);
            } else {
              jsonWriter.name("Uri").value(namespaceIndex);
            }
          }
        }
        jsonWriter.endObject();
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeLocalizedText(String field, LocalizedText value)
      throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }

        if (reversible) {
          jsonWriter.beginObject();
          if (value.getLocale() != null) {
            jsonWriter.name("Locale").value(value.getLocale());
          }
          if (value.getText() != null) {
            jsonWriter.name("Text").value(value.getText());
          }
          jsonWriter.endObject();
        } else {
          jsonWriter.value(value.getText());
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeExtensionObject(String field, ExtensionObject value)
      throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != null) {
        if (field != null) {
          jsonWriter.name(field);
        }

        if (value == null || value.getBody() == null) {
          jsonWriter.nullValue();
        } else {
          if (reversible) {
            jsonWriter.beginObject();
            encodeNodeId("TypeId", value.getEncodingId());
            switch (value.getBodyType()) {
              case JsonString:
                // Encoding field omitted for JSON body
                jsonWriter.name("Body").jsonValue((String) value.getBody());
                break;
              case ByteString:
                jsonWriter.name("Encoding").value(1);
                encodeByteString("Body", (ByteString) value.getBody());
                break;
              case XmlElement:
                jsonWriter.name("Encoding").value(2);
                encodeXmlElement("Body", (XmlElement) value.getBody());
                break;
            }
            jsonWriter.endObject();
          } else {
            switch (value.getBodyType()) {
              case JsonString:
                jsonWriter.jsonValue((String) value.getBody());
                break;
              case ByteString:
                contextPush(EncoderContext.BUILTIN);
                encodeByteString(null, (ByteString) value.getBody());
                contextPop();
                break;
              case XmlElement:
                contextPush(EncoderContext.BUILTIN);
                encodeXmlElement(null, (XmlElement) value.getBody());
                contextPop();
                break;
            }
          }
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeDataValue(String field, DataValue value) throws UaSerializationException {
    try {
      if (allFieldsAreOmitted(value)) {
        return;
      }

      if (field != null) {
        jsonWriter.name(field);
      }

      contextPush(EncoderContext.BUILTIN);
      jsonWriter.beginObject();

      Variant v = value.getValue();
      if (v != null && v.isNotNull()) {
        encodeVariant("Value", v);
      }
      StatusCode s = value.getStatusCode();
      if (s != null && s.getValue() != 0L) {
        encodeStatusCode("Status", s);
      }
      DateTime sourceTime = value.getSourceTime();
      if (sourceTime != null && sourceTime.isNotNull()) {
        encodeDateTime("SourceTimestamp", sourceTime);
      }
      UShort sourcePicoseconds = value.getSourcePicoseconds();
      if (sourcePicoseconds != null && sourcePicoseconds.intValue() > 0) {
        encodeUInt16("SourcePicoseconds", sourcePicoseconds);
      }
      DateTime serverTime = value.getServerTime();
      if (serverTime != null && serverTime.isNotNull()) {
        encodeDateTime("ServerTimestamp", serverTime);
      }
      UShort serverPicoseconds = value.getServerPicoseconds();
      if (serverPicoseconds != null && serverPicoseconds.intValue() > 0) {
        encodeUInt16("ServerPicoseconds", serverPicoseconds);
      }

      jsonWriter.endObject();
      contextPop();
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  /**
   * @return {@code true} if all fields in {@code value} would be omitted from the encoding.
   */
  private static boolean allFieldsAreOmitted(DataValue value) {
    return (value.getValue() == null || value.getValue().isNull())
        && (value.getStatusCode() == null || value.getStatusCode().getValue() == 0L)
        && (value.getSourceTime() == null || value.getSourceTime().isNull())
        && (value.getSourcePicoseconds() == null || value.getSourcePicoseconds().intValue() == 0)
        && (value.getServerTime() == null || value.getServerTime().isNull())
        && (value.getServerPicoseconds() == null || value.getServerPicoseconds().intValue() == 0);
  }

  @Override
  public void encodeVariant(String field, Variant value) throws UaSerializationException {
    if (value.isNull()) {
      return;
    }

    try {
      if (field != null) {
        jsonWriter.name(field);
      }

      assert value.getValue() != null;
      encodeVariantValue(value.getValue());
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  enum TypeHint {
    BUILTIN,
    ENUM,
    STRUCT,
    OPTION_SET
  }

  private void encodeVariantValue(@NonNull Object value) throws IOException {
    Class<?> valueClass;
    if (value instanceof Matrix) {
      Matrix m = (Matrix) value;
      if (m.getElements() == null) return;
      valueClass = getClass(m.getElements());
    } else {
      valueClass = getClass(value);
    }

    TypeHint typeHint = TypeHint.BUILTIN;
    if (UaEnumeratedType.class.isAssignableFrom(valueClass)) {
      typeHint = TypeHint.ENUM;
    } else if (UaStructuredType.class.isAssignableFrom(valueClass)) {
      typeHint = TypeHint.STRUCT;
    } else if (OptionSetUInteger.class.isAssignableFrom(valueClass)) {
      typeHint = TypeHint.OPTION_SET;
    }

    int typeId;
    if (typeHint == TypeHint.ENUM) {
      typeId = OpcUaDataType.Int32.getTypeId();
    } else if (typeHint == TypeHint.STRUCT) {
      typeId = OpcUaDataType.ExtensionObject.getTypeId();
    } else if (typeHint == TypeHint.OPTION_SET) {
      // TODO this would fail on empty array
      //  would be better to have size-specific OptionSetUI subclasses, e.g. OptionSetUI8,
      // OptionSetUI16, etc...
      Object os = Array.get(value, 0);
      Object osv = ((OptionSetUInteger<?>) os).getValue();
      typeId = TypeUtil.getBuiltinTypeId(osv.getClass());
    } else {
      typeId = TypeUtil.getBuiltinTypeId(valueClass);
    }

    if (value.getClass().isArray()) {
      if (reversible) {
        jsonWriter.beginObject();
        jsonWriter.name("Type").value(typeId);
        jsonWriter.name("Body");
      }

      int length = Array.getLength(value);
      jsonWriter.beginArray();
      for (int i = 0; i < length; i++) {
        Object o = Array.get(value, i);

        encodeVariantBodyValue(o, typeHint, typeId);
      }
      jsonWriter.endArray();

      if (reversible) {
        jsonWriter.endObject();
      }
    } else if (value instanceof Matrix) {
      if (reversible) {
        jsonWriter.beginObject();
        jsonWriter.name("Type").value(typeId);
        jsonWriter.name("Body");
      }

      Matrix m = (Matrix) value;
      if (reversible) {
        Object flatArray = m.getElements();
        int length = Array.getLength(flatArray);
        jsonWriter.beginArray();
        for (int i = 0; i < length; i++) {
          Object o = Array.get(flatArray, i);

          encodeVariantBodyValue(o, typeHint, typeId);
        }
        jsonWriter.endArray();

        jsonWriter.name("Dimensions");
        jsonWriter.beginArray();
        for (int dimension : m.getDimensions()) {
          jsonWriter.value(dimension);
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
      } else {
        Object nestedArray = ArrayUtil.unflatten(m.getElements(), m.getDimensions());
        writeNestedMultiDimensionalVariantValue(typeId, nestedArray, m.getDimensions(), 0);
      }
    } else {
      if (reversible) {
        jsonWriter.beginObject();
        jsonWriter.name("Type").value(typeId);
        jsonWriter.name("Body");
      }

      encodeVariantBodyValue(value, typeHint, typeId);

      if (reversible) {
        jsonWriter.endObject();
      }
    }
  }

  private void encodeVariantBodyValue(Object value, TypeHint typeHint, int typeId) {
    switch (typeHint) {
      case BUILTIN:
        {
          encodeBuiltinTypeValue(null, typeId, value);
          break;
        }
      case ENUM:
        {
          Object enumValue = ((UaEnumeratedType) value).getValue();
          encodeBuiltinTypeValue(null, typeId, enumValue);
          break;
        }
      case STRUCT:
        {
          UaStructuredType struct = (UaStructuredType) value;
          ExtensionObject xo = ExtensionObject.encode(encodingContext, struct);
          encodeBuiltinTypeValue(null, typeId, xo);
          break;
        }
      case OPTION_SET:
        {
          Object optionSetValue = ((OptionSetUInteger<?>) value).getValue();
          encodeBuiltinTypeValue(null, typeId, optionSetValue);
          break;
        }
    }
  }

  /** Write a multidimensional value in the reversible (flattened array) format. */
  private void writeFlattenedMultiDimensionalVariantValue(
      int typeId, Object value, int[] dimensions, int dimensionIndex) throws IOException {

    if (dimensionIndex == 0) {
      jsonWriter.beginArray();
      for (int i = 0; i < dimensions[dimensionIndex]; i++) {
        Object e = Array.get(value, i);
        writeFlattenedMultiDimensionalVariantValue(typeId, e, dimensions, dimensionIndex + 1);
      }
      jsonWriter.endArray();
    } else if (dimensionIndex == dimensions.length - 1) {
      for (int i = 0; i < dimensions[dimensionIndex]; i++) {
        Object e = Array.get(value, i);
        encodeBuiltinTypeValue(null, typeId, e);
      }
    } else {
      for (int i = 0; i < dimensions[dimensionIndex]; i++) {
        Object e = Array.get(value, i);
        writeFlattenedMultiDimensionalVariantValue(typeId, e, dimensions, dimensionIndex + 1);
      }
    }
  }

  /** Write a multidimensional value in the non-reversible (nested array) format. */
  private void writeNestedMultiDimensionalVariantValue(
      int typeId, Object value, int[] dimensions, int dimensionIndex) throws IOException {

    if (dimensionIndex == dimensions.length - 1) {
      jsonWriter.beginArray();
      for (int i = 0; i < dimensions[dimensionIndex]; i++) {
        Object e = Array.get(value, i);
        encodeBuiltinTypeValue(null, typeId, e);
      }
      jsonWriter.endArray();
    } else {
      jsonWriter.beginArray();
      for (int i = 0; i < dimensions[dimensionIndex]; i++) {
        Object e = Array.get(value, i);
        writeNestedMultiDimensionalVariantValue(typeId, e, dimensions, dimensionIndex + 1);
      }
      jsonWriter.endArray();
    }
  }

  private void encodeBuiltinTypeValue(String field, int typeId, Object value)
      throws UaSerializationException {
    contextPush(EncoderContext.BUILTIN);
    switch (typeId) {
      case 1:
        encodeBoolean(field, (Boolean) value);
        break;
      case 2:
        encodeSByte(field, (Byte) value);
        break;
      case 3:
        encodeByte(field, (UByte) value);
        break;
      case 4:
        encodeInt16(field, (Short) value);
        break;
      case 5:
        encodeUInt16(field, (UShort) value);
        break;
      case 6:
        encodeInt32(field, (Integer) value);
        break;
      case 7:
        encodeUInt32(field, (UInteger) value);
        break;
      case 8:
        encodeInt64(field, (Long) value);
        break;
      case 9:
        encodeUInt64(field, (ULong) value);
        break;
      case 10:
        encodeFloat(field, (Float) value);
        break;
      case 11:
        encodeDouble(field, (Double) value);
        break;
      case 12:
        encodeString(field, (String) value);
        break;
      case 13:
        encodeDateTime(field, (DateTime) value);
        break;
      case 14:
        encodeGuid(field, (UUID) value);
        break;
      case 15:
        encodeByteString(field, (ByteString) value);
        break;
      case 16:
        encodeXmlElement(field, (XmlElement) value);
        break;
      case 17:
        encodeNodeId(field, (NodeId) value);
        break;
      case 18:
        encodeExpandedNodeId(field, (ExpandedNodeId) value);
        break;
      case 19:
        encodeStatusCode(field, (StatusCode) value);
        break;
      case 20:
        encodeQualifiedName(field, (QualifiedName) value);
        break;
      case 21:
        encodeLocalizedText(field, (LocalizedText) value);
        break;
      case 22:
        encodeExtensionObject(field, (ExtensionObject) value);
        break;
      case 23:
        encodeDataValue(field, (DataValue) value);
        break;
      case 24:
        encodeVariant(field, (Variant) value);
        break;
      case 25:
        encodeDiagnosticInfo(field, (DiagnosticInfo) value);
        break;
      default:
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "not a built-in type: " + value.getClass());
    }
    contextPop();
  }

  private static Class<?> getClass(@NonNull Object o) {
    if (o.getClass().isArray()) {
      return ArrayUtil.getType(o);
    } else {
      return o.getClass();
    }
  }

  @Override
  public void encodeDiagnosticInfo(String field, DiagnosticInfo value)
      throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible || context == EncoderContext.BUILTIN || value != null) {
        if (field != null) {
          jsonWriter.name(field);
        }

        contextPush(EncoderContext.BUILTIN);
        jsonWriter.beginObject();
        encodeInt32("SymbolicId", value.getSymbolicId());
        encodeInt32("NamespaceUri", value.getNamespaceUri());
        encodeInt32("Locale", value.getLocale());
        encodeInt32("LocalizedText", value.getLocalizedText());
        encodeString("AdditionalInfo", value.getAdditionalInfo());
        if (value.getInnerStatusCode() != null) {
          encodeStatusCode("InnerStatusCode", value.getInnerStatusCode());
        }
        if (value.getInnerDiagnosticInfo() != null) {
          encodeDiagnosticInfo("InnerDiagnosticInfo", value.getInnerDiagnosticInfo());
        }
        jsonWriter.endObject();
        contextPop();
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeMessage(String field, UaMessageType message) throws UaSerializationException {
    ExpandedNodeId xEncodingId = message.getJsonEncodingId();

    NodeId encodingId =
        xEncodingId
            .toNodeId(encodingContext.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + xEncodingId.getNamespaceUri()));

    DataTypeCodec codec = encodingContext.getDataTypeManager().getCodec(encodingId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "no codec registered: " + encodingId);
    }

    try {
      jsonWriter.beginObject();
      encodeNodeId("TypeId", encodingId);
      encodeStruct("Body", message, codec);
      jsonWriter.endObject();
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeEnum(String field, UaEnumeratedType value) throws UaSerializationException {
    if (reversible) {
      encodeInt32(field, value.getValue());
    } else {
      if (value.getName() != null) {
        encodeString(field, value.getName() + "_" + value.getValue());
      } else {
        encodeString(field, String.valueOf(value.getValue()));
      }
    }
  }

  @Override
  public void encodeStruct(String field, Object value, NodeId dataTypeId)
      throws UaSerializationException {
    DataTypeCodec codec = encodingContext.getDataTypeManager().getCodec(dataTypeId);

    if (codec != null) {
      encodeStruct(field, value, codec);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId);
    }
  }

  @Override
  public void encodeStruct(String field, Object value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(encodingContext.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError, "namespace not registered: " + dataTypeId));

    encodeStruct(field, value, localDataTypeId);
  }

  @Override
  public void encodeStruct(String field, Object value, DataTypeCodec codec)
      throws UaSerializationException {
    try {
      if (field != null) {
        jsonWriter.name(field);
      }

      contextPush(EncoderContext.STRUCT);
      jsonWriter.beginObject();
      codec.encode(encodingContext, this, value);
      jsonWriter.endObject();
      contextPop();
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeBooleanArray(String field, Boolean[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeBoolean);
  }

  @Override
  public void encodeSByteArray(String field, Byte[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeSByte);
  }

  @Override
  public void encodeInt16Array(String field, Short[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeInt16);
  }

  @Override
  public void encodeInt32Array(String field, Integer[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeInt32);
  }

  @Override
  public void encodeInt64Array(String field, Long[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeInt64);
  }

  @Override
  public void encodeByteArray(String field, UByte[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeByte);
  }

  @Override
  public void encodeUInt16Array(String field, UShort[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeUInt16);
  }

  @Override
  public void encodeUInt32Array(String field, UInteger[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeUInt32);
  }

  @Override
  public void encodeUInt64Array(String field, ULong[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeUInt64);
  }

  @Override
  public void encodeFloatArray(String field, Float[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeFloat);
  }

  @Override
  public void encodeDoubleArray(String field, Double[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeDouble);
  }

  @Override
  public void encodeStringArray(String field, String[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeString);
  }

  @Override
  public void encodeDateTimeArray(String field, DateTime[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeDateTime);
  }

  @Override
  public void encodeGuidArray(String field, UUID[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeGuid);
  }

  @Override
  public void encodeByteStringArray(String field, ByteString[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeByteString);
  }

  @Override
  public void encodeXmlElementArray(String field, XmlElement[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeXmlElement);
  }

  @Override
  public void encodeNodeIdArray(String field, NodeId[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeNodeId);
  }

  @Override
  public void encodeExpandedNodeIdArray(String field, ExpandedNodeId[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeExpandedNodeId);
  }

  @Override
  public void encodeStatusCodeArray(String field, StatusCode[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeStatusCode);
  }

  @Override
  public void encodeQualifiedNameArray(String field, QualifiedName[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeQualifiedName);
  }

  @Override
  public void encodeLocalizedTextArray(String field, LocalizedText[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeLocalizedText);
  }

  @Override
  public void encodeExtensionObjectArray(String field, ExtensionObject[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeExtensionObject);
  }

  @Override
  public void encodeDataValueArray(String field, DataValue[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeDataValue);
  }

  @Override
  public void encodeVariantArray(String field, Variant[] value) throws UaSerializationException {
    encodeArray(field, value, this::encodeVariant);
  }

  @Override
  public void encodeDiagnosticInfoArray(String field, DiagnosticInfo[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeDiagnosticInfo);
  }

  @Override
  public void encodeEnumArray(String field, UaEnumeratedType[] value)
      throws UaSerializationException {
    encodeArray(field, value, this::encodeEnum);
  }

  @Override
  public void encodeStructArray(String field, Object[] value, NodeId dataTypeId)
      throws UaSerializationException {
    encodeArray(field, value, (f, v) -> encodeStruct(null, v, dataTypeId));
  }

  @Override
  public void encodeStructArray(String field, Object[] value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {

    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(encodingContext.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "encodeStructArray: namespace not registered: " + dataTypeId));

    encodeStructArray(field, value, localDataTypeId);
  }

  @Override
  public <T> void encodeArray(String field, T[] values, BiConsumer<String, T> encoder)
      throws UaSerializationException {
    if (values == null) {
      return;
    }

    if (values.length > encodingContext.getEncodingLimits().getMaxMessageSize()) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingLimitsExceeded, "array length exceeds max message size");
    }

    try {
      if (field != null) {
        jsonWriter.name(field);
      }

      jsonWriter.beginArray();
      for (T value : values) {
        encoder.accept(null, value);
      }
      jsonWriter.endArray();
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeMatrix(String field, Matrix value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }

        Object flatArray = value.getElements();
        if (flatArray == null) {
          try {
            jsonWriter.nullValue();
          } catch (IOException e) {
            throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
          }
        } else {
          int[] dimensions = value.getDimensions();
          OpcUaDataType dataType = value.getDataType().orElseThrow();
          try {
            encodeFlatArrayAsNested(flatArray, dimensions, dataType, 0);
          } catch (IOException e) {
            throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
          }
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeEnumMatrix(String field, Matrix value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }

        Object flatArray = value.getElements();
        if (flatArray == null) {
          try {
            jsonWriter.nullValue();
          } catch (IOException e) {
            throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
          }
        } else {
          int[] dimensions = value.getDimensions();

          try {
            encodeFlatEnumArrayAsNested((UaEnumeratedType[]) flatArray, dimensions, 0);
          } catch (IOException e) {
            throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
          }
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeStructMatrix(String field, Matrix value, NodeId dataTypeId)
      throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (!reversible
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {
        if (field != null) {
          jsonWriter.name(field);
        }

        Object flatArray = value.getElements();
        if (flatArray == null) {
          try {
            jsonWriter.nullValue();
          } catch (IOException e) {
            throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
          }
        } else {
          int[] dimensions = value.getDimensions();

          try {
            encodeFlatStructArrayAsNested(flatArray, dataTypeId, dimensions, 0);
          } catch (IOException e) {
            throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
          }
        }
      }
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeStructMatrix(String field, Matrix value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(encodingContext.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "encodeStructArray: namespace not registered: " + dataTypeId));

    encodeStructMatrix(field, value, localDataTypeId);
  }

  private void encodeFlatArrayAsNested(
      Object value, int[] dimensions, OpcUaDataType dataType, int offset) throws IOException {

    if (dimensions.length == 1) {
      jsonWriter.beginArray();
      for (int i = 0; i < dimensions[0]; i++) {
        Object e = Array.get(value, offset + i);
        encodeBuiltinTypeValue(null, dataType.getTypeId(), e);
      }
      jsonWriter.endArray();
    } else {
      jsonWriter.beginArray();
      int[] tail = Arrays.copyOfRange(dimensions, 1, dimensions.length);
      for (int i = 0; i < dimensions[0]; i++) {
        encodeFlatArrayAsNested(value, tail, dataType, offset + i * length(tail));
      }
      jsonWriter.endArray();
    }
  }

  private void encodeFlatEnumArrayAsNested(UaEnumeratedType[] value, int[] dimensions, int offset)
      throws IOException {

    if (dimensions.length == 1) {
      jsonWriter.beginArray();
      for (int i = 0; i < dimensions[0]; i++) {
        Object e = Array.get(value, offset + i);
        encodeEnum(null, (UaEnumeratedType) e);
      }
      jsonWriter.endArray();
    } else {
      jsonWriter.beginArray();
      int[] tail = Arrays.copyOfRange(dimensions, 1, dimensions.length);
      for (int i = 0; i < dimensions[0]; i++) {
        encodeFlatEnumArrayAsNested(value, tail, offset + i * length(tail));
      }
      jsonWriter.endArray();
    }
  }

  private void encodeFlatStructArrayAsNested(
      Object value, NodeId dataTypeId, int[] dimensions, int offset) throws IOException {

    if (dimensions.length == 1) {
      jsonWriter.beginArray();
      for (int i = 0; i < dimensions[0]; i++) {
        Object e = Array.get(value, offset + i);
        encodeStruct(null, e, dataTypeId);
      }
      jsonWriter.endArray();
    } else {
      jsonWriter.beginArray();
      int[] tail = Arrays.copyOfRange(dimensions, 1, dimensions.length);
      for (int i = 0; i < dimensions[0]; i++) {
        encodeFlatStructArrayAsNested(value, dataTypeId, tail, offset + i * length(tail));
      }
      jsonWriter.endArray();
    }
  }

  private static int length(int[] tail) {
    int product = 1;
    for (int aTail : tail) {
      product *= aTail;
    }
    return product;
  }
}
