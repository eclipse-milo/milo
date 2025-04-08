/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.jspecify.annotations.NullMarked;
import org.slf4j.LoggerFactory;

@NullMarked
public abstract sealed class ExtensionObject
    permits ExtensionObject.Binary, ExtensionObject.Json, ExtensionObject.Xml {

  private final Lazy<UaStructuredType> decoded = new Lazy<>();

  /**
   * Get the body of this ExtensionObject.
   *
   * <p>The body is the encoded value of the ExtensionObject. The type of the body depends on the
   * encoding that was used to encode the ExtensionObject. The body is one of:
   *
   * <ul>
   *   <li>{@link ByteString} for Binary encoding
   *   <li>{@link XmlElement} for XML encoding
   *   <li>{@link String} for JSON encoding
   * </ul>
   *
   * @return the body of this ExtensionObject.
   */
  public abstract Object getBody();

  /**
   * Get the {@link NodeId} of the datatype encoding or datatype of the encoded value contained by
   * this ExtensionObject.
   *
   * <p>The NodeId returned is the encoding id if the encoding is Binary or XML, or the datatype id
   * if the encoding is JSON.
   *
   * @return the {@link NodeId} of the datatype encoding or datatype.
   */
  public abstract NodeId getEncodingOrTypeId();

  /**
   * Check if this ExtensionObject is null.
   *
   * @return true if this ExtensionObject is null, false otherwise.
   */
  public abstract boolean isNull();

  /**
   * Decode the value contained in this ExtensionObject using the datatype encoding that matches its
   * body type.
   *
   * @param context an {@link EncodingContext}.
   * @return the decoded value.
   * @throws UaSerializationException if the decoding fails.
   */
  public final UaStructuredType decode(EncodingContext context) throws UaSerializationException {
    if (this instanceof ExtensionObject.Binary) {
      return decode(context, OpcUaDefaultBinaryEncoding.getInstance());
    } else if (this instanceof ExtensionObject.Xml) {
      DataTypeEncoding encoding =
          context.getEncodingManager().getEncoding(DataTypeEncoding.XML_ENCODING_NAME);

      if (encoding != null) {
        return decode(context, encoding);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError,
            "encoding not registered: " + DataTypeEncoding.XML_ENCODING_NAME);
      }
    } else if (this instanceof ExtensionObject.Json) {
      DataTypeEncoding encoding =
          context.getEncodingManager().getEncoding(DataTypeEncoding.JSON_ENCODING_NAME);

      if (encoding != null) {
        return decode(context, encoding);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError,
            "encoding not registered: " + DataTypeEncoding.JSON_ENCODING_NAME);
      }
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "unexpected body: " + getBody().getClass().getName());
    }
  }

  /**
   * Decode the value contained in this ExtensionObject using the specified encoding, if it hasn't
   * already been decoded.
   *
   * @param context an {@link EncodingContext}.
   * @param encoding the {@link DataTypeEncoding} to use.
   * @return the decoded value.
   * @throws UaSerializationException if the decoding fails.
   */
  public final UaStructuredType decode(EncodingContext context, DataTypeEncoding encoding)
      throws UaSerializationException {

    return decoded.get(() -> encoding.decode(context, this, getEncodingOrTypeId()));
  }

  /**
   * Transcode this ExtensionObject to a new encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param newEncodingId the {@link NodeId} of the new encoding.
   * @param newEncoding the {@link DataTypeEncoding} to use.
   * @return a new ExtensionObject with a value encoded in the specified encoding.
   * @throws UaSerializationException if the transcoding fails.
   */
  public final ExtensionObject transcode(
      EncodingContext context, NodeId newEncodingId, DataTypeEncoding newEncoding)
      throws UaSerializationException {

    if (getEncodingOrTypeId().equals(newEncodingId)) {
      return this;
    }

    try {
      UaStructuredType decoded = decode(context);

      return newEncoding.encode(context, decoded, newEncodingId);
    } catch (UaSerializationException e) {
      LoggerFactory.getLogger(ExtensionObject.class)
          .warn("Transcoding failed: {}", e.getMessage(), e);
      return this;
    }
  }

  /**
   * Create a new ExtensionObject with the specified ByteString body and encoding id.
   *
   * @param body the ByteString body of the ExtensionObject
   * @param encodingId the NodeId of the datatype encoding.
   * @return a new ExtensionObject with the specified body and encoding id.
   */
  public static ExtensionObject of(ByteString body, NodeId encodingId) {
    return new ExtensionObject.Binary(body, encodingId);
  }

  /**
   * Create a new ExtensionObject with the specified XmlElement body and encoding id.
   *
   * @param body the XmlElement body of the ExtensionObject
   * @param encodingId the NodeId of the datatype encoding.
   * @return a new ExtensionObject with the specified body and encoding id.
   */
  public static ExtensionObject of(XmlElement body, NodeId encodingId) {
    return new ExtensionObject.Xml(body, encodingId);
  }

  /**
   * Create a new ExtensionObject with the specified String body and type id.
   *
   * @param body the String body of the ExtensionObject
   * @param typeId the NodeId of the datatype.
   * @return a new ExtensionObject with the specified body and type id.
   */
  public static ExtensionObject of(String body, NodeId typeId) {
    return new ExtensionObject.Json(body, typeId);
  }

  /**
   * Encode a {@link UaStructuredType} value in the default binary encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param value the {@link UaStructuredType} value to encode.
   * @return an {@link ExtensionObject} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject encode(EncodingContext context, UaStructuredType value)
      throws UaSerializationException {

    NodeId encodingId =
        value
            .getBinaryEncodingId()
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + value.getBinaryEncodingId().namespace()));

    return encode(context, value, encodingId, OpcUaDefaultBinaryEncoding.getInstance());
  }

  /**
   * Encode an array of {@link UaStructuredType} values in the default binary encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param values the {@link UaStructuredType} values to encode.
   * @return an array of {@link ExtensionObject} containing the encoded values.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject[] encodeArray(EncodingContext context, UaStructuredType[] values)
      throws UaSerializationException {

    var xos = new ExtensionObject[values.length];

    for (int i = 0; i < values.length; i++) {
      xos[i] = encode(context, values[i]);
    }

    return xos;
  }

  /**
   * Encode a value in the default binary encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param value the value to encode.
   * @param encodingId the {@link NodeId} of the binary datatype encoding.
   * @return an {@link ExtensionObject} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject encode(
      EncodingContext context, UaStructuredType value, NodeId encodingId)
      throws UaSerializationException {

    return encodeBinary(context, value, encodingId, OpcUaDefaultBinaryEncoding.getInstance());
  }

  /**
   * Encode a value in the specified datatype encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param struct the value to encode.
   * @param encodingOrTypeId the {@link NodeId} the datatype encoding if using Binary or XML
   *     encoding, or the {@link NodeId} of the datatype if using JSON encoding.
   * @param encoding the {@link DataTypeEncoding} to use.
   * @return an {@link ExtensionObject} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject encode(
      EncodingContext context,
      UaStructuredType struct,
      NodeId encodingOrTypeId,
      DataTypeEncoding encoding)
      throws UaSerializationException {

    if (encoding.getEncodingName().equals(DataTypeEncoding.BINARY_ENCODING_NAME)) {
      return encodeBinary(context, struct, encodingOrTypeId, encoding);
    } else if (encoding.getEncodingName().equals(DataTypeEncoding.XML_ENCODING_NAME)) {
      return encodeXml(context, struct, encodingOrTypeId, encoding);
    } else if (encoding.getEncodingName().equals(DataTypeEncoding.JSON_ENCODING_NAME)) {
      return encodeJson(context, struct, encodingOrTypeId, encoding);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "unsupported encoding: " + encoding.getEncodingName());
    }
  }

  /**
   * Encode a value in the specified datatype encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param struct the value to encode.
   * @param encodingOrTypeId the {@link ExpandedNodeId} the datatype encoding if using Binary or XML
   *     encoding, or the {@link ExpandedNodeId} of the datatype if using JSON encoding.
   * @param encoding the {@link DataTypeEncoding} to use.
   * @return an {@link ExtensionObject} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject encode(
      EncodingContext context,
      UaStructuredType struct,
      ExpandedNodeId encodingOrTypeId,
      DataTypeEncoding encoding)
      throws UaSerializationException {

    NodeId localEncodingOrTypeId =
        encodingOrTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + encodingOrTypeId.namespace()));

    return encode(context, struct, localEncodingOrTypeId, encoding);
  }

  private static ExtensionObject encodeBinary(
      EncodingContext context,
      UaStructuredType struct,
      NodeId encodingId,
      DataTypeEncoding encoding)
      throws UaSerializationException {

    return encoding.encode(context, struct, encodingId);
  }

  private static ExtensionObject encodeXml(
      EncodingContext context,
      UaStructuredType struct,
      NodeId encodingId,
      DataTypeEncoding encoding)
      throws UaSerializationException {

    return encoding.encode(context, struct, encodingId);
  }

  private static ExtensionObject encodeJson(
      EncodingContext context, UaStructuredType struct, NodeId typeId, DataTypeEncoding encoding)
      throws UaSerializationException {

    return encoding.encode(context, struct, typeId);
  }

  /** An ExtensionObject that contains a {@link ByteString} body, used with Binary encoding. */
  public static final class Binary extends ExtensionObject {

    private final ByteString body;
    private final NodeId encodingId;

    private Binary(ByteString body, NodeId encodingId) {
      this.body = body;
      this.encodingId = encodingId;
    }

    @Override
    public ByteString getBody() {
      return body;
    }

    @Override
    public NodeId getEncodingOrTypeId() {
      return encodingId;
    }

    @Override
    public boolean isNull() {
      return body.isNull();
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      ExtensionObject.Binary that = (ExtensionObject.Binary) o;
      return Objects.equals(body, that.body) && Objects.equals(encodingId, that.encodingId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(body, encodingId);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", ExtensionObject.class.getSimpleName() + "[", "]")
          .add("body=" + body)
          .add("encodingId=" + encodingId)
          .toString();
    }
  }

  /** An ExtensionObject that contains an {@link XmlElement} body, used with XML encoding. */
  public static final class Xml extends ExtensionObject {

    private final XmlElement body;
    private final NodeId encodingId;

    private Xml(XmlElement body, NodeId encodingId) {
      this.body = body;
      this.encodingId = encodingId;
    }

    @Override
    public XmlElement getBody() {
      return body;
    }

    @Override
    public NodeId getEncodingOrTypeId() {
      return encodingId;
    }

    @Override
    public boolean isNull() {
      return body.isNull();
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      ExtensionObject.Xml that = (ExtensionObject.Xml) o;
      return Objects.equals(body, that.body) && Objects.equals(encodingId, that.encodingId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(body, encodingId);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", ExtensionObject.class.getSimpleName() + "[", "]")
          .add("body=" + body)
          .add("encodingId=" + encodingId)
          .toString();
    }
  }

  /** An ExtensionObject that contains a {@link String} body, used with JSON encoding. */
  public static final class Json extends ExtensionObject {

    private final String body;
    private final NodeId typeId;

    private Json(String body, NodeId typeId) {
      this.body = body;
      this.typeId = typeId;
    }

    @Override
    public String getBody() {
      return body;
    }

    @Override
    public NodeId getEncodingOrTypeId() {
      return typeId;
    }

    @Override
    public boolean isNull() {
      return body.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      ExtensionObject.Json that = (ExtensionObject.Json) o;
      return Objects.equals(body, that.body) && Objects.equals(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(body, typeId);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", ExtensionObject.class.getSimpleName() + "[", "]")
          .add("body='" + body + "'")
          .add("typeId=" + typeId)
          .toString();
    }
  }
}
