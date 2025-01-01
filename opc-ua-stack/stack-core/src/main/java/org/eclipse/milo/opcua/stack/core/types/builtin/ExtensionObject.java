/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class ExtensionObject {

  public enum BodyType {
    ByteString,
    XmlElement,
    JsonString
  }

  private final Lazy<Object> decoded = new Lazy<>();

  private final BodyType bodyType;

  private final Object body;
  private final NodeId encodingId;

  public ExtensionObject(@NonNull ByteString body, @NonNull NodeId encodingId) {
    this((Object) body, encodingId);
  }

  public ExtensionObject(@NonNull XmlElement body, @NonNull NodeId encodingId) {
    this((Object) body, encodingId);
  }

  public ExtensionObject(@Nullable String body, @NonNull NodeId encodingId) {
    this((Object) body, encodingId);
  }

  private ExtensionObject(@Nullable Object body, @NonNull NodeId encodingId) {
    this.body = body;
    this.encodingId = encodingId;

    if (body instanceof ByteString) {
      bodyType = BodyType.ByteString;
    } else if (body instanceof XmlElement) {
      bodyType = BodyType.XmlElement;
    } else if (body instanceof String) {
      bodyType = BodyType.JsonString;
    } else {
      throw new IllegalArgumentException("body: " + body);
    }
  }

  public Object getBody() {
    return body;
  }

  public BodyType getBodyType() {
    return bodyType;
  }

  public NodeId getEncodingId() {
    return encodingId;
  }

  public boolean isNull() {
    switch (bodyType) {
      case ByteString:
        return body == null || ((ByteString) body).isNull();
      case XmlElement:
        return body == null || ((XmlElement) body).isNull();
      case JsonString:
        return body == null;
      default:
        throw new IllegalStateException("BodyType: " + bodyType);
    }
  }

  public Object decode(EncodingContext context) throws UaSerializationException {
    switch (bodyType) {
      case ByteString:
        {
          return decode(context, OpcUaDefaultBinaryEncoding.getInstance());
        }
      case XmlElement:
        {
          DataTypeEncoding encoding =
              context.getEncodingManager().getEncoding(DataTypeEncoding.XML_ENCODING_NAME);

          if (encoding != null) {
            return decode(context, encoding);
          } else {
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                "encoding not registered: " + DataTypeEncoding.XML_ENCODING_NAME);
          }
        }
      case JsonString:
        {
          DataTypeEncoding encoding =
              context.getEncodingManager().getEncoding(DataTypeEncoding.JSON_ENCODING_NAME);

          if (encoding != null) {
            return decode(context, encoding);
          } else {
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                "encoding not registered: " + DataTypeEncoding.JSON_ENCODING_NAME);
          }
        }
      default:
        throw new IllegalStateException("BodyType: " + bodyType);
    }
  }

  public Object decode(EncodingContext context, DataTypeEncoding encoding)
      throws UaSerializationException {
    return decoded.get(() -> encoding.decode(context, body, encodingId));
  }

  public @Nullable Object decodeOrNull(EncodingContext context) {
    try {
      return decode(context);
    } catch (UaSerializationException e) {
      return null;
    }
  }

  public ExtensionObject transcode(
      EncodingContext context, NodeId newEncodingId, DataTypeEncoding newEncoding) {

    if (this.encodingId.equals(newEncodingId)) {
      // Requested encoding is the current encoding, nothing to do here.
      return this;
    } else {
      // First try to just decode the body because it's likely encoded
      // in one of the default encodings, not some custom encoding.
      Object struct = decodeOrNull(context);

      if (struct != null) {
        // Successful decode, re-encode in the new encoding.
        Object encoded = newEncoding.encode(context, struct, newEncodingId);

        return new ExtensionObject(encoded, newEncodingId);
      } else {
        // TODO Decode failed... custom encoding? Look it up and try again...
        return this;
      }
    }
  }

  public static ExtensionObject encode(EncodingContext context, UaStructuredType struct)
      throws UaSerializationException {

    NodeId encodingId =
        struct
            .getBinaryEncodingId()
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: "
                            + struct.getBinaryEncodingId().getNamespaceUri()));

    return encodeDefaultBinary(context, struct, encodingId);
  }

  public static ExtensionObject[] encodeArray(
      EncodingContext context, UaStructuredType[] structArray) throws UaSerializationException {

    ExtensionObject[] xos = new ExtensionObject[structArray.length];

    for (int i = 0; i < xos.length; i++) {
      xos[i] = encode(context, structArray[i]);
    }

    return xos;
  }

  public static ExtensionObject encodeDefaultBinary(
      EncodingContext context, Object object, NodeId encodingId) throws UaSerializationException {

    return encode(context, object, encodingId, OpcUaDefaultBinaryEncoding.getInstance());
  }

  public static ExtensionObject encode(
      EncodingContext context, Object object, ExpandedNodeId xEncodingId, DataTypeEncoding encoding)
      throws UaSerializationException {

    NodeId encodingId =
        xEncodingId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + xEncodingId.getNamespaceUri()));

    return encode(context, object, encodingId, encoding);
  }

  public static ExtensionObject encode(
      EncodingContext context, Object object, NodeId encodingId, DataTypeEncoding encoding)
      throws UaSerializationException {

    Object body = encoding.encode(context, object, encodingId);

    return new ExtensionObject(body, encodingId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ExtensionObject that = (ExtensionObject) o;

    return Objects.equal(body, that.body) && Objects.equal(encodingId, that.encodingId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(body, encodingId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("encoded", body)
        .add("encodingId", encodingId)
        .toString();
  }
}
