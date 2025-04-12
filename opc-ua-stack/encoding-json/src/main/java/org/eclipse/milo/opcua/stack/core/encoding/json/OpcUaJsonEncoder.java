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

import com.google.gson.stream.JsonWriter;
import java.io.*;
import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference.NamespaceIndex;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference.NamespaceUri;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference.ServerIndex;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference.ServerUri;
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
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.NonNull;

public class OpcUaJsonEncoder implements UaEncoder, AutoCloseable {

  enum EncoderContext {
    BUILTIN,
    STRUCT
  }

  public enum Encoding {
    COMPACT,
    VERBOSE
  }

  private final Stack<EncoderContext> contextStack = new Stack<>();

  Encoding encoding = Encoding.COMPACT;

  Writer output;
  JsonWriter jsonWriter;
  EncodingContext encodingContext;

  public OpcUaJsonEncoder(EncodingContext encodingContext) {
    this(encodingContext, new StringWriter());
  }

  public OpcUaJsonEncoder(EncodingContext encodingContext, Writer output) {
    this.encodingContext = encodingContext;

    reset(output);
  }

  @Override
  public EncodingContext getEncodingContext() {
    return encodingContext;
  }

  @Override
  public void close() throws Exception {
    if (jsonWriter != null) {
      jsonWriter.close();
    }
  }

  /** Reset this encoder and configure a new {@link StringWriter} output. */
  public void reset() {
    reset(new StringWriter());
  }

  /**
   * Reset this encoder and configure a new {@link Writer} output.
   *
   * @param output the new {@link Writer} to write to.
   */
  public void reset(Writer output) {
    this.output = output;

    jsonWriter = new JsonWriter(output);
    jsonWriter.setHtmlSafe(false);
  }

  /**
   * Get the {@link Writer} output is being written to.
   *
   * @return the {@link Writer} output is being written to.
   */
  public Writer getOutput() {
    return output;
  }

  /**
   * Get the value of {@link #toString()} called on the current {@link Writer}.
   *
   * <p>If not set explicitly to something else, this will be a {@link StringWriter}, and the value
   * will be a String containing JSON.
   *
   * @return the value of {@link #toString()} called on the current {@link Writer}.
   */
  public String getOutputString() {
    try {
      jsonWriter.flush();
      return output.toString();
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  /**
   * Set the encoding to use.
   *
   * <p>{@link Encoding#COMPACT} is the default, and is used for serialization between OPC UA
   * applications.
   *
   * <p>{@link Encoding#VERBOSE} is used when the consumer of the encoded JSON is something like a
   * "cloud application", or otherwise not an OPC UA application, and cannot be deserialized by
   * another OPC UA application's JSON decoder.
   *
   * @param encoding the encoding to use.
   */
  public void setEncoding(Encoding encoding) {
    this.encoding = encoding;
  }

  private EncoderContext contextPeek() {
    return contextStack.isEmpty() ? EncoderContext.BUILTIN : contextStack.peek();
  }

  private void contextPush(EncoderContext context) {
    contextStack.push(context);
  }

  private void contextPop() {
    contextStack.pop();
  }

  @Override
  public void encodeBoolean(String field, Boolean value) throws UaSerializationException {
    try {
      EncoderContext context = contextPeek();
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value) {
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != 0) {
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != 0) {
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != 0) {
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != 0) {
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || value.intValue() != 0) {
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || value.intValue() != 0) {
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || value.longValue() != 0L) {
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || value.longValue() != 0L) {
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != 0.0f) {
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != 0.0d) {
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != null) {
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || (value != null && value.isValid())) {
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
      if (encoding == Encoding.VERBOSE
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
      if (encoding == Encoding.VERBOSE
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
      if (encoding == Encoding.VERBOSE
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {

        if (field != null) {
          jsonWriter.name(field);
        }

        ExpandedNodeId expanded;
        try {
          expanded = value.expanded(encodingContext.getNamespaceTable());
        } catch (IllegalStateException e) {
          // This is kind of similar to what's required of the decoder when it encounters a
          // namespace URI it can't map to a namespace index:
          // "When decoders need to replace a NamespaceUri with a NamespaceIndex and the
          // NamespaceUri cannot be mapped to a NamespaceIndex, then decoders shall use 0 for the
          // NamespaceIndex, String for the IdType and the JSON string as the Identifier."
          expanded = ExpandedNodeId.of(value.toParseableString());
        }
        jsonWriter.value(expanded.toParseableString());
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {

        if (field != null) {
          jsonWriter.name(field);
        }

        var sb = new StringBuilder();

        if (value.server() instanceof ServerIndex index) {
          if (index.serverIndex().intValue() != 0) {
            String uri = encodingContext.getServerTable().get(index.serverIndex().intValue());
            if (uri != null) {
              sb.append("svu=").append(uri).append(";");
            } else {
              throw new UaSerializationException(
                  StatusCodes.Bad_EncodingError,
                  "server index=" + index.serverIndex() + " not found");
            }
          }
        } else if (value.server() instanceof ServerUri uri) {
          UInteger index = encodingContext.getServerTable().getIndex(uri.serverUri());
          if (index == null || index.intValue() != 0) {
            sb.append("svu=").append(uri.serverUri()).append(";");
          }
        }

        if (value.namespace() instanceof NamespaceIndex index) {
          if (index.namespaceIndex().intValue() != 0) {
            String uri = encodingContext.getNamespaceTable().get(index.namespaceIndex().intValue());
            if (uri != null) {
              sb.append("nsu=").append(uri).append(";");
            } else {
              throw new UaSerializationException(
                  StatusCodes.Bad_EncodingError,
                  "namespace index=" + index.namespaceIndex() + " not found");
            }
          }
        } else if (value.namespace() instanceof NamespaceUri uri) {
          if (!uri.namespaceUri().equals(Namespaces.OPC_UA)) {
            sb.append("nsu=").append(uri.namespaceUri()).append(";");
          }
        }

        switch (value.getType()) {
          case Numeric:
            sb.append("i=").append(value.identifier());
            break;
          case String:
            sb.append("s=").append(value.identifier());
            break;
          case Guid:
            sb.append("g=").append(value.identifier().toString().toUpperCase());
            break;
          case Opaque:
            ByteString bs = (ByteString) value.identifier();
            if (bs.isNull()) sb.append("b=");
            else sb.append("b=").append(Base64.getEncoder().encodeToString(bs.bytes()));
            break;

          default:
            throw new IllegalStateException("IdType " + value.getType());
        }

        jsonWriter.value(sb.toString());
      }
    } catch (Exception e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeStatusCode(String field, StatusCode value) throws UaSerializationException {
    // # Compact
    // StatusCode values shall be encoded as a JSON number.
    //
    // # Verbose
    // StatusCode values shall be encoded as a JSON object with the fields defined as follows:
    // - "Code"
    // The numeric code encoded as a JSON number.
    // The Code is omitted if the numeric code is 0 (Good).
    // - "Symbol"
    // The string literal associated with the numeric code encoded as JSON string. e.g. 0x80AB0000
    // has the associated literal "BadInvalidArgument".
    // The Symbol is omitted if the numeric code is 0 (Good).
    // If the string literal is not known to the encoder the field is omitted.

    try {
      EncoderContext context = contextPeek();
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || (value != null && !value.isGood())) {

        long code = value.value();

        if (encoding == Encoding.COMPACT) {
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {

        if (field != null) {
          jsonWriter.name(field);
        }

        int index = value.namespaceIndex().intValue();

        if (index == 0) {
          jsonWriter.value(value.name());
        } else {
          String namespaceUri = encodingContext.getNamespaceTable().get(index);
          if (namespaceUri != null) {
            jsonWriter.value("nsu=%s;%s".formatted(namespaceUri, value.name()));
          } else {
            throw new UaSerializationException(
                StatusCodes.Bad_EncodingError, "namespace index=" + index + " not found");
          }
        }
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
      if (encoding == Encoding.VERBOSE
          || context == EncoderContext.BUILTIN
          || (value != null && value.isNotNull())) {

        if (field != null) {
          jsonWriter.name(field);
        }

        if (value == null || value.isNull()) {
          jsonWriter.nullValue();
          return;
        }

        jsonWriter.beginObject();
        if (value.locale() != null) {
          jsonWriter.name("Locale").value(value.locale());
        }
        if (value.text() != null) {
          jsonWriter.name("Text").value(value.text());
        }
        jsonWriter.endObject();
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
      if (context == EncoderContext.BUILTIN || value != null) {
        if (field != null) {
          jsonWriter.name(field);
        }

        if (value == null) {
          jsonWriter.nullValue();
        } else {
          value.getBody();
          jsonWriter.beginObject();

          encodeNodeId("TypeId", value.getEncodingOrTypeId());
          if (value instanceof ExtensionObject.Json xo) {
            jsonWriter.name("Body").jsonValue(xo.getBody());
          } else if (value instanceof ExtensionObject.Binary xo) {
            jsonWriter.name("Encoding").value(1);
            encodeByteString("Body", xo.getBody());
          } else if (value instanceof ExtensionObject.Xml xo) {
            jsonWriter.name("Encoding").value(2);
            encodeXmlElement("Body", xo.getBody());
          }

          jsonWriter.endObject();
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

      Variant v = value.value();
      if (v.isNotNull()) {
        encodeVariant("Value", v);
      }
      StatusCode s = value.statusCode();
      if (s.value() != 0L) {
        encodeStatusCode("Status", s);
      }
      DateTime sourceTime = value.sourceTime();
      if (sourceTime != null && sourceTime.isNotNull()) {
        encodeDateTime("SourceTimestamp", sourceTime);
      }
      UShort sourcePicoseconds = value.sourcePicoseconds();
      if (sourcePicoseconds != null && sourcePicoseconds.intValue() > 0) {
        encodeUInt16("SourcePicoseconds", sourcePicoseconds);
      }
      DateTime serverTime = value.serverTime();
      if (serverTime != null && serverTime.isNotNull()) {
        encodeDateTime("ServerTimestamp", serverTime);
      }
      UShort serverPicoseconds = value.serverPicoseconds();
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
    return value.value().isNull()
        && value.statusCode().value() == 0L
        && (value.sourceTime() == null || value.sourceTime().isNull())
        && (value.sourcePicoseconds() == null || value.sourcePicoseconds().intValue() == 0)
        && (value.serverTime() == null || value.serverTime().isNull())
        && (value.serverPicoseconds() == null || value.serverPicoseconds().intValue() == 0);
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

      assert value.value() != null;
      encodeVariantValue(value.value());
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
    if (value instanceof Matrix m) {
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
      typeId = OpcUaDataType.getBuiltinTypeId(osv.getClass());
    } else {
      typeId = OpcUaDataType.getBuiltinTypeId(valueClass);
    }

    if (value.getClass().isArray()) {
      jsonWriter.beginObject();

      jsonWriter.name("Type").value(typeId);
      jsonWriter.name("Body");
      int length = Array.getLength(value);

      jsonWriter.beginArray();
      for (int i = 0; i < length; i++) {
        Object o = Array.get(value, i);

        encodeVariantBodyValue(o, typeHint, typeId);
      }
      jsonWriter.endArray();

      jsonWriter.endObject();
    } else if (value instanceof Matrix m) {
      jsonWriter.beginObject();

      jsonWriter.name("Type").value(typeId);
      jsonWriter.name("Body");

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
      jsonWriter.beginObject();
      jsonWriter.name("Type").value(typeId);
      jsonWriter.name("Body");

      encodeVariantBodyValue(value, typeHint, typeId);

      jsonWriter.endObject();
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
      if (encoding == Encoding.VERBOSE || context == EncoderContext.BUILTIN || value != null) {
        if (field != null) {
          jsonWriter.name(field);
        }

        contextPush(EncoderContext.BUILTIN);
        jsonWriter.beginObject();
        encodeInt32("SymbolicId", value.symbolicId());
        encodeInt32("NamespaceUri", value.namespaceUri());
        encodeInt32("Locale", value.locale());
        encodeInt32("LocalizedText", value.localizedText());
        encodeString("AdditionalInfo", value.additionalInfo());
        if (value.innerStatusCode() != null) {
          encodeStatusCode("InnerStatusCode", value.innerStatusCode());
        }
        if (value.innerDiagnosticInfo() != null) {
          encodeDiagnosticInfo("InnerDiagnosticInfo", value.innerDiagnosticInfo());
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

    try {
      jsonWriter.beginObject();
      encodeNodeId("TypeId", encodingId);
      encodeStruct("Body", message, encodingId);
      jsonWriter.endObject();
    } catch (IOException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeEnum(String field, UaEnumeratedType value) throws UaSerializationException {
    if (encoding == Encoding.COMPACT) {
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
  public void encodeStruct(String field, UaStructuredType value, ExpandedNodeId dataTypeId)
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
  public void encodeStruct(String field, UaStructuredType value, NodeId dataTypeId)
      throws UaSerializationException {

    DataTypeCodec codec = encodingContext.getDataTypeManager().getCodec(dataTypeId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId);
    }

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
  public void encodeStruct(String field, UaStructuredType value, DataTypeCodec codec)
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
  public void encodeStructArray(String field, UaStructuredType[] value, NodeId dataTypeId)
      throws UaSerializationException {
    encodeArray(field, value, (f, v) -> encodeStruct(null, v, dataTypeId));
  }

  @Override
  public void encodeStructArray(String field, UaStructuredType[] value, ExpandedNodeId dataTypeId)
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

  private <T> void encodeArray(String field, T[] values, BiConsumer<String, T> encoder)
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
      if (encoding == Encoding.VERBOSE
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
          OpcUaDataType dataType = value.getDataType().orElseThrow();

          int[] dimensions = value.getDimensions();

          jsonWriter.beginObject();
          try {
            // "Array" member
            jsonWriter.name("Array");
            jsonWriter.beginArray();
            for (int i = 0; i < Array.getLength(flatArray); i++) {
              Object e = Array.get(flatArray, i);
              encodeBuiltinTypeValue(null, dataType.getTypeId(), e);
            }
            jsonWriter.endArray();

            // "Dimensions" member
            jsonWriter.name("Dimensions");
            jsonWriter.beginArray();
            for (int dimension : dimensions) {
              jsonWriter.value(dimension);
            }
            jsonWriter.endArray();
          } finally {
            jsonWriter.endObject();
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
      if (encoding == Encoding.VERBOSE
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

          jsonWriter.beginObject();
          try {
            // "Array" member
            jsonWriter.name("Array");
            jsonWriter.beginArray();
            for (int i = 0; i < Array.getLength(flatArray); i++) {
              Object e = Array.get(flatArray, i);
              encodeEnum(null, (UaEnumeratedType) e);
            }
            jsonWriter.endArray();

            // "Dimensions" member
            jsonWriter.name("Dimensions");
            jsonWriter.beginArray();
            for (int dimension : dimensions) {
              jsonWriter.value(dimension);
            }
            jsonWriter.endArray();
          } finally {
            jsonWriter.endObject();
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
      if (encoding == Encoding.VERBOSE
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

          jsonWriter.beginObject();
          try {
            // "Array" member
            jsonWriter.name("Array");
            jsonWriter.beginArray();
            for (int i = 0; i < Array.getLength(flatArray); i++) {
              UaStructuredType e = (UaStructuredType) Array.get(flatArray, i);
              encodeStruct(null, e, dataTypeId);
            }
            jsonWriter.endArray();

            // "Dimensions" member
            jsonWriter.name("Dimensions");
            jsonWriter.beginArray();
            for (int dimension : dimensions) {
              jsonWriter.value(dimension);
            }
            jsonWriter.endArray();
          } finally {
            jsonWriter.endObject();
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
}
