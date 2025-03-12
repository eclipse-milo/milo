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

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

public abstract sealed class ExtensionObject2
    permits ExtensionObject2.BinaryExtensionObject,
        ExtensionObject2.JsonExtensionObject,
        ExtensionObject2.XmlExtensionObject {

  private final Lazy<Object> decoded = new Lazy<>();

  public abstract Object getBody();

  public abstract NodeId getEncodingOrTypeId();

  /**
   * Decode the value contained in this ExtensionObject using the default binary encoding, if it
   * hasn't already been decoded.
   *
   * @param context an {@link EncodingContext}.
   * @return the decoded value.
   * @throws UaSerializationException if the decoding fails.
   */
  public final Object decode(EncodingContext context) throws UaSerializationException {
    return decode(context, OpcUaDefaultBinaryEncoding.getInstance());
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
   * Encode a {@link UaStructuredType} value in the default binary encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param value the {@link UaStructuredType} value to encode.
   * @return an {@link ExtensionObject2} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject2 encode(EncodingContext context, UaStructuredType value)
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
   * Encode a value in the default binary encoding.
   *
   * @param context an {@link EncodingContext}.
   * @param value the value to encode.
   * @param encodingId the {@link NodeId} of the binary datatype encoding.
   * @return an {@link ExtensionObject2} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject2 encode(EncodingContext context, Object value, NodeId encodingId)
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
   * @return an {@link ExtensionObject2} containing the encoded value.
   * @throws UaSerializationException if the encoding fails.
   */
  public static ExtensionObject2 encode(
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

  private static ExtensionObject2 encodeBinary(
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

  private static ExtensionObject2 encodeXml(
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

  private static ExtensionObject2 encodeJson(
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

  public static final class BinaryExtensionObject extends ExtensionObject2 {

    private final ByteString body;
    private final NodeId encodingId;

    public BinaryExtensionObject(ByteString body, NodeId encodingId) {
      this.body = body;
      this.encodingId = encodingId;
    }

    @Override
    public Object getBody() {
      return body;
    }

    @Override
    public NodeId getEncodingOrTypeId() {
      return encodingId;
    }
  }

  public static final class XmlExtensionObject extends ExtensionObject2 {

    private final XmlElement body;
    private final NodeId encodingId;

    public XmlExtensionObject(XmlElement body, NodeId encodingId) {
      this.body = body;
      this.encodingId = encodingId;
    }

    @Override
    public Object getBody() {
      return body;
    }

    @Override
    public NodeId getEncodingOrTypeId() {
      return encodingId;
    }
  }

  public static final class JsonExtensionObject extends ExtensionObject2 {

    private final String body;
    private final NodeId typeId;

    public JsonExtensionObject(String body, NodeId typeId) {
      this.body = body;
      this.typeId = typeId;
    }

    @Override
    public Object getBody() {
      return body;
    }

    @Override
    public NodeId getEncodingOrTypeId() {
      return typeId;
    }
  }
}
