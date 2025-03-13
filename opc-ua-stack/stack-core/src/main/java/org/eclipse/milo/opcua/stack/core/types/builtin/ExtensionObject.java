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
    permits ExtensionObject.BinaryExtensionObject,
        ExtensionObject.JsonExtensionObject,
        ExtensionObject.XmlExtensionObject {

  private final Lazy<Object> decoded = new Lazy<>();

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
  public final Object decode(EncodingContext context) throws UaSerializationException {
    if (this instanceof BinaryExtensionObject) {
      return decode(context, OpcUaDefaultBinaryEncoding.getInstance());
    } else if (this instanceof XmlExtensionObject) {
      DataTypeEncoding encoding =
          context.getEncodingManager().getEncoding(DataTypeEncoding.XML_ENCODING_NAME);

      if (encoding != null) {
        return decode(context, encoding);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError,
            "encoding not registered: " + DataTypeEncoding.XML_ENCODING_NAME);
      }
    } else if (this instanceof JsonExtensionObject) {
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
  public final Object decode(EncodingContext context, DataTypeEncoding encoding)
      throws UaSerializationException {

    return decoded.get(() -> encoding.decode(context, getBody(), getEncodingOrTypeId()));
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
      Object decoded = decode(context);
      Object encoded = newEncoding.encode(context, decoded, newEncodingId);

      if (encoded instanceof ByteString bs) {
        return new BinaryExtensionObject(bs, newEncodingId);
      } else if (encoded instanceof XmlElement xml) {
        return new XmlExtensionObject(xml, newEncodingId);
      } else if (encoded instanceof String json) {
        return new JsonExtensionObject(json, newEncodingId);
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "unexpected body: " + encoded.getClass().getName());
      }
    } catch (UaSerializationException e) {
      LoggerFactory.getLogger(ExtensionObject.class)
          .warn("Transcoding failed: {}", e.getMessage(), e);
      return this;
    }
  }

  /**
   * Create a new ExtensionObject2 with the specified ByteString body and encoding id.
   *
   * @param body the ByteString body of the ExtensionObject2
   * @param encodingId the NodeId of the datatype encoding.
   * @return a new ExtensionObject2 with the specified body and encoding id.
   */
  public static ExtensionObject of(ByteString body, NodeId encodingId) {
    return new BinaryExtensionObject(body, encodingId);
  }

  /**
   * Create a new ExtensionObject2 with the specified XmlElement body and encoding id.
   *
   * @param body the XmlElement body of the ExtensionObject2
   * @param encodingId the NodeId of the datatype encoding.
   * @return a new ExtensionObject2 with the specified body and encoding id.
   */
  public static ExtensionObject of(XmlElement body, NodeId encodingId) {
    return new XmlExtensionObject(body, encodingId);
  }

  /**
   * Create a new ExtensionObject2 with the specified String body and type id.
   *
   * @param body the String body of the ExtensionObject2
   * @param typeId the NodeId of the datatype.
   * @return a new ExtensionObject2 with the specified body and type id.
   */
  public static ExtensionObject of(String body, NodeId typeId) {
    return new JsonExtensionObject(body, typeId);
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
                        "namespace not registered: "
                            + value.getBinaryEncodingId().getNamespaceUri()));

    return encode(context, OpcUaDefaultBinaryEncoding.getInstance(), value, encodingId);
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
  public static ExtensionObject encode(EncodingContext context, Object value, NodeId encodingId)
      throws UaSerializationException {

    return encode(context, OpcUaDefaultBinaryEncoding.getInstance(), value, encodingId);
  }

  /**
   * Encode a value in the specified datatype encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param encoding the {@link DataTypeEncoding} to use.
   * @param struct the value to encode.
   * @param encodingOrTypeId the {@link NodeId} the datatype encoding if using Binary or XML
   *     encoding, or the {@link NodeId} of the datatype if using JSON encoding.
   * @return an {@link ExtensionObject} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject encode(
      EncodingContext context, DataTypeEncoding encoding, Object struct, NodeId encodingOrTypeId)
      throws UaSerializationException {

    if (encoding.getEncodingName().equals(DataTypeEncoding.BINARY_ENCODING_NAME)) {
      return encodeBinary(context, encoding, struct, encodingOrTypeId);
    } else if (encoding.getEncodingName().equals(DataTypeEncoding.XML_ENCODING_NAME)) {
      return encodeXml(context, encoding, struct, encodingOrTypeId);
    } else if (encoding.getEncodingName().equals(DataTypeEncoding.JSON_ENCODING_NAME)) {
      return encodeJson(context, encoding, struct, encodingOrTypeId);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "unsupported encoding: " + encoding.getEncodingName());
    }
  }

  /**
   * Encode a value in the specified datatype encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param encoding the {@link DataTypeEncoding} to use.
   * @param struct the value to encode.
   * @param encodingOrTypeId the {@link ExpandedNodeId} the datatype encoding if using Binary or XML
   *     encoding, or the {@link ExpandedNodeId} of the datatype if using JSON encoding.
   * @return an {@link ExtensionObject} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject encode(
      EncodingContext context,
      DataTypeEncoding encoding,
      Object struct,
      ExpandedNodeId encodingOrTypeId)
      throws UaSerializationException {

    NodeId localEncodingOrTypeId =
        encodingOrTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + encodingOrTypeId.getNamespaceUri()));

    return encode(context, encoding, struct, localEncodingOrTypeId);
  }

  private static ExtensionObject encodeBinary(
      EncodingContext context, DataTypeEncoding encoding, Object struct, NodeId encodingId)
      throws UaSerializationException {

    Object body = encoding.encode(context, struct, encodingId);

    if (body instanceof ByteString bs) {
      return new BinaryExtensionObject(bs, encodingId);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "expected ByteString, got " + body.getClass().getName());
    }
  }

  private static ExtensionObject encodeXml(
      EncodingContext context, DataTypeEncoding encoding, Object struct, NodeId encodingId)
      throws UaSerializationException {

    Object body = encoding.encode(context, struct, encodingId);

    if (body instanceof XmlElement xml) {
      return new XmlExtensionObject(xml, encodingId);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "expected XmlElement, got " + body.getClass().getName());
    }
  }

  private static ExtensionObject encodeJson(
      EncodingContext context, DataTypeEncoding encoding, Object struct, NodeId typeId)
      throws UaSerializationException {

    Object body = encoding.encode(context, struct, typeId);

    if (body instanceof String json) {
      return new JsonExtensionObject(json, typeId);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "expected String, got " + body.getClass().getName());
    }
  }

  /** An ExtensionObject that contains a {@link ByteString} body, used with Binary encoding. */
  public static final class BinaryExtensionObject extends ExtensionObject {

    private final ByteString body;
    private final NodeId encodingId;

    public BinaryExtensionObject(ByteString body, NodeId encodingId) {
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
      BinaryExtensionObject that = (BinaryExtensionObject) o;
      return Objects.equals(body, that.body) && Objects.equals(encodingId, that.encodingId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(body, encodingId);
    }
  }

  /** An ExtensionObject that contains an {@link XmlElement} body, used with XML encoding. */
  public static final class XmlExtensionObject extends ExtensionObject {

    private final XmlElement body;
    private final NodeId encodingId;

    public XmlExtensionObject(XmlElement body, NodeId encodingId) {
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
      XmlExtensionObject that = (XmlExtensionObject) o;
      return Objects.equals(body, that.body) && Objects.equals(encodingId, that.encodingId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(body, encodingId);
    }
  }

  /** An ExtensionObject that contains a {@link String} body, used with JSON encoding. */
  public static final class JsonExtensionObject extends ExtensionObject {

    private final String body;
    private final NodeId typeId;

    public JsonExtensionObject(String body, NodeId typeId) {
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
      JsonExtensionObject that = (JsonExtensionObject) o;
      return Objects.equals(body, that.body) && Objects.equals(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(body, typeId);
    }
  }
}
