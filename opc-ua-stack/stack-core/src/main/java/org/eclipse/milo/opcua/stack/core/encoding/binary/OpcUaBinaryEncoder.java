/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import io.netty.buffer.ByteBuf;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference.NamespaceIndex;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference.NamespaceUri;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NonNull;
import org.slf4j.LoggerFactory;

public class OpcUaBinaryEncoder implements UaEncoder {

  private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
  private static final Charset CHARSET_UTF16 = StandardCharsets.UTF_16;

  private ByteBuf buffer;

  private int currentByte;
  private int bitCount;

  private final EncodingContext context;

  public OpcUaBinaryEncoder(EncodingContext context) {
    this.context = context;
  }

  public OpcUaBinaryEncoder setBuffer(ByteBuf buffer) {
    this.buffer = buffer;
    return this;
  }

  @Override
  public EncodingContext getEncodingContext() {
    return context;
  }

  public <T> void encodeArray(T[] values, Consumer<T> write) throws UaSerializationException {
    if (values == null) {
      buffer.writeIntLE(-1);
    } else {
      if (values.length > context.getEncodingLimits().getMaxMessageSize()) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingLimitsExceeded, "array length exceeds max message size");
      }

      encodeInt32(values.length);

      for (T t : values) {
        write.accept(t);
      }
    }
  }

  public void encodeBit(int value) throws UaSerializationException {
    currentByte = currentByte | ((value & 1) << bitCount);
    bitCount++;

    if (bitCount == 8) {
      buffer.writeByte(currentByte);
      currentByte = 0;
      bitCount = 0;
    }
  }

  public void encodeBoolean(Boolean value) throws UaSerializationException {
    if (value == null) {
      buffer.writeBoolean(false);
    } else {
      buffer.writeBoolean(value);
    }
  }

  public void encodeSByte(Byte value) throws UaSerializationException {
    if (value == null) {
      buffer.writeByte(0);
    } else {
      buffer.writeByte(value);
    }
  }

  public void encodeByte(UByte value) throws UaSerializationException {
    if (value == null) {
      buffer.writeByte(0);
    } else {
      buffer.writeByte(value.intValue());
    }
  }

  public void encodeInt16(Short value) throws UaSerializationException {
    if (value == null) {
      buffer.writeShortLE(0);
    } else {
      buffer.writeShortLE(value);
    }
  }

  public void encodeUInt16(UShort value) throws UaSerializationException {
    if (value == null) {
      buffer.writeShortLE(0);
    } else {
      buffer.writeShortLE(value.intValue());
    }
  }

  public void encodeInt32(Integer value) throws UaSerializationException {
    if (value == null) {
      buffer.writeIntLE(0);
    } else {
      buffer.writeIntLE(value);
    }
  }

  public void encodeUInt32(UInteger value) throws UaSerializationException {
    if (value == null) {
      buffer.writeIntLE(0);
    } else {
      buffer.writeIntLE(value.intValue());
    }
  }

  public void encodeInt64(Long value) throws UaSerializationException {
    if (value == null) {
      buffer.writeLongLE(0L);
    } else {
      buffer.writeLongLE(value);
    }
  }

  public void encodeUInt64(ULong value) throws UaSerializationException {
    if (value == null) {
      buffer.writeLongLE(0L);
    } else {
      buffer.writeLongLE(value.longValue());
    }
  }

  public void encodeFloat(Float value) throws UaSerializationException {
    if (value == null) {
      buffer.writeFloatLE(0f);
    } else {
      buffer.writeFloatLE(value);
    }
  }

  public void encodeDouble(Double value) throws UaSerializationException {
    if (value == null) {
      buffer.writeDoubleLE(0d);
    } else {
      buffer.writeDoubleLE(value);
    }
  }

  public void encodeCharacter(Character value) throws UaSerializationException {
    buffer.writeByte(value & 0xFF);
  }

  public void encodeWideChar(Character value) throws UaSerializationException {
    buffer.writeChar(value);
  }

  public void encodeUtf8NullTerminatedString(String value) throws UaSerializationException {
    if (value != null) {
      byte[] bytes = value.getBytes(CHARSET_UTF8);
      for (byte b : bytes) {
        buffer.writeByte(b);
        if (b == 0) return;
      }
    }
    buffer.writeByte(0);
  }

  public void encodeUtf8CharArray(String value) throws UaSerializationException {
    if (value == null) {
      encodeInt32(-1);
    } else {
      encodeLengthPrefixedString(value, CHARSET_UTF8);
    }
  }

  public void encodeUtf16NullTerminatedString(String value) throws UaSerializationException {
    if (value == null) {
      buffer.writeByte(0);
    } else {
      for (int i = 0; i < value.length(); i++) {
        int c = value.codePointAt(i);
        buffer.writeChar(c);
        if (c == 0) return;
      }
      buffer.writeChar(0);
    }
  }

  public void encodeUtf16CharArray(String value) throws UaSerializationException {
    if (value == null) {
      encodeInt32(-1);
    } else {
      encodeLengthPrefixedString(value, CHARSET_UTF16);
    }
  }

  public void encodeDateTime(DateTime value) throws UaSerializationException {
    if (value == null) {
      buffer.writeLongLE(0L);
    } else {
      buffer.writeLongLE(value.getUtcTime());
    }
  }

  public void encodeByteString(ByteString value) throws UaSerializationException {
    if (value == null || value.isNull()) {
      buffer.writeIntLE(-1);
    } else {
      byte[] bytes = value.bytes();

      assert (bytes != null);

      buffer.writeIntLE(bytes.length);
      buffer.writeBytes(bytes);
    }
  }

  public void encodeGuid(UUID value) throws UaSerializationException {
    if (value == null) {
      buffer.writeZero(16);
    } else {
      long msb = value.getMostSignificantBits();
      long lsb = value.getLeastSignificantBits();

      buffer.writeIntLE((int) (msb >>> 32));
      buffer.writeShortLE((int) (msb >>> 16) & 0xFFFF);
      buffer.writeShortLE((int) (msb) & 0xFFFF);

      buffer.writeLong(lsb); // intentionally Big Endian
    }
  }

  // region OPC Built-in Types

  public void encodeXmlElement(XmlElement value) throws UaSerializationException {
    if (value == null || value.isNull()) {
      buffer.writeIntLE(-1);
    } else {
      encodeByteString(new ByteString(value.fragment().getBytes(StandardCharsets.UTF_8)));
    }
  }

  public void encodeDataValue(DataValue value) throws UaSerializationException {
    if (value == null) {
      buffer.writeByte(0);
    } else {
      int mask = 0x00;

      if (value.value().isNotNull()) {
        mask |= 0x01;
      }

      if (value.getStatusCode().getValue() != 0L) {
        mask |= 0x02;
      }

      if (value.sourceTime() != null && !DateTime.MIN_VALUE.equals(value.sourceTime())) {

        mask |= 0x04;
      }

      if (value.serverTime() != null && !DateTime.MIN_VALUE.equals(value.serverTime())) {

        mask |= 0x08;
      }

      if (value.sourcePicoseconds() != null && value.sourcePicoseconds().intValue() != 0) {

        mask |= 0x10;
      }

      if (value.serverPicoseconds() != null && value.serverPicoseconds().intValue() != 0) {

        mask |= 0x20;
      }

      buffer.writeByte(mask);

      // Value
      if ((mask & 0x01) == 0x01) {
        encodeVariant(value.value());
      }

      // StatusCode
      if ((mask & 0x02) == 0x02) {
        encodeStatusCode(value.statusCode());
      }

      // SourceTimestamp and SourcePicoseconds
      if ((mask & 0x04) == 0x04) {
        encodeDateTime(value.sourceTime());
      }
      if ((mask & 0x10) == 0x10) {
        encodeUInt16(value.sourcePicoseconds());
      }

      // ServerTimestamp and ServerPicoseconds
      if ((mask & 0x08) == 0x08) {
        encodeDateTime(value.serverTime());
      }
      if ((mask & 0x20) == 0x20) {
        encodeUInt16(value.serverPicoseconds());
      }
    }
  }

  public void encodeDiagnosticInfo(DiagnosticInfo value) throws UaSerializationException {
    if (value == null) {
      buffer.writeByte(0);
    } else {
      int mask = 0x7F;

      if (value.symbolicId() == -1) mask ^= 0x01;
      if (value.namespaceUri() == -1) mask ^= 0x02;
      if (value.localizedText() == -1) mask ^= 0x04;
      if (value.locale() == -1) mask ^= 0x08;
      if (value.additionalInfo() == null || value.additionalInfo().isEmpty()) mask ^= 0x10;
      if (value.innerStatusCode() == null) mask ^= 0x20;
      if (value.innerDiagnosticInfo() == null) mask ^= 0x40;

      buffer.writeByte(mask);

      if ((mask & 0x01) == 0x01) encodeInt32(value.symbolicId());
      if ((mask & 0x02) == 0x02) encodeInt32(value.namespaceUri());
      if ((mask & 0x04) == 0x04) encodeInt32(value.localizedText());
      if ((mask & 0x08) == 0x08) encodeInt32(value.locale());
      if ((mask & 0x10) == 0x10) encodeString(value.additionalInfo());
      if ((mask & 0x20) == 0x20) encodeStatusCode(value.innerStatusCode());
      if ((mask & 0x40) == 0x40) encodeDiagnosticInfo(value.innerDiagnosticInfo());
    }
  }

  public void encodeExpandedNodeId(ExpandedNodeId value) throws UaSerializationException {
    if (value == null) value = ExpandedNodeId.NULL_VALUE;

    int namespaceIndex = 0;
    String namespaceUri = null;
    NamespaceReference namespace = value.namespace();
    if (namespace instanceof NamespaceUri uri) {
      namespaceUri = uri.namespaceUri();

      // If we know the namespace index, use that for the encoding.
      // It's more compact than encoding a namespace URI.
      // It's also a workaround for bugs observed in the CTT and UaExpert, where the Client ignores
      // the explicitly specified namespace URI in the ExpandedNodeId and just uses the namespace
      // index, which is always 0 when the URI is encoded.
      UShort index = context.getNamespaceTable().getIndex(namespaceUri);
      if (index != null) {
        namespaceIndex = index.intValue();
        namespaceUri = null;
      }
    } else if (value.namespace() instanceof NamespaceIndex index) {
      namespaceIndex = index.namespaceIndex().intValue();
    }

    UInteger serverIndex = UInteger.MIN;
    ServerReference server = value.server();
    if (server instanceof ServerIndex index) {
      serverIndex = index.serverIndex();
    } else if (server instanceof ServerUri uri) {
      serverIndex = context.getServerTable().getIndex(uri.serverUri());
      if (serverIndex == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError,
            "server uri not found in server table: " + uri.serverUri());
      }
    }

    int flags = 0;
    if (namespaceUri != null && !namespaceUri.isEmpty()) {
      flags |= 0x80;
    }
    if (serverIndex.longValue() > 0) {
      flags |= 0x40;
    }

    if (value.getType() == IdType.Numeric) {
      UInteger identifier = (UInteger) value.getIdentifier();
      long idv = identifier.longValue();

      if (namespaceIndex == 0 && idv >= 0 && idv <= 255) {
        /* Two-byte format */
        buffer.writeByte(flags);
        buffer.writeByte((int) idv);
      } else if (namespaceIndex >= 0 && namespaceIndex <= 255 && idv <= 65535) {
        /* Four-byte format */
        buffer.writeByte(0x01 | flags);
        buffer.writeByte(namespaceIndex);
        buffer.writeShortLE((int) idv);
      } else {
        /* Numeric format */
        buffer.writeByte(0x02 | flags);
        buffer.writeShortLE(namespaceIndex);
        buffer.writeIntLE((int) idv);
      }
    } else if (value.getType() == IdType.String) {
      String identifier = (String) value.getIdentifier();

      buffer.writeByte(0x03 | flags);
      buffer.writeShortLE(namespaceIndex);
      encodeString(identifier);
    } else if (value.getType() == IdType.Guid) {
      UUID identifier = (UUID) value.getIdentifier();

      buffer.writeByte(0x04 | flags);
      buffer.writeShortLE(namespaceIndex);
      encodeGuid(identifier);
    } else if (value.getType() == IdType.Opaque) {
      ByteString identifier = (ByteString) value.getIdentifier();

      buffer.writeByte(0x05 | flags);
      buffer.writeShortLE(namespaceIndex);
      encodeByteString(identifier);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "invalid identifier: " + value.getIdentifier());
    }

    if (namespaceUri != null && !namespaceUri.isEmpty()) {
      encodeString(namespaceUri);
    }

    if (serverIndex.longValue() > 0) {
      encodeUInt32(serverIndex);
    }
  }

  public void encodeExtensionObject(ExtensionObject value) throws UaSerializationException {
    if (value == null || value.isNull()) {
      encodeNodeId(NodeId.NULL_VALUE);
      buffer.writeByte(0); // No body is encoded
    } else {
      if (value instanceof ExtensionObject.Binary xo) {
        encodeNodeId(xo.getEncodingOrTypeId());
        buffer.writeByte(1); // Body is binary encoded
        encodeByteString(xo.getBody());
      } else if (value instanceof ExtensionObject.Xml xo) {
        encodeNodeId(xo.getEncodingOrTypeId());
        buffer.writeByte(2);
        encodeXmlElement(xo.getBody());
      } else if (value instanceof ExtensionObject.Json xo) {
        encodeNodeId(xo.getEncodingOrTypeId());
        buffer.writeByte(1);
        encodeByteString(ByteString.of(xo.getBody().getBytes(StandardCharsets.UTF_8)));
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "unknown ExtensionObject type: " + value.getClass());
      }
    }
  }

  public void encodeLocalizedText(LocalizedText value) throws UaSerializationException {
    if (value == null) value = LocalizedText.NULL_VALUE;

    String locale = value.locale();
    String text = value.text();

    int mask = 1 | 2;
    if (locale == null || locale.isEmpty()) {
      mask ^= 1;
    }
    if (text == null || text.isEmpty()) {
      mask ^= 2;
    }

    buffer.writeByte(mask);

    if (locale != null && !locale.isEmpty()) {
      encodeString(locale);
    }
    if (text != null && !text.isEmpty()) {
      encodeString(text);
    }
  }

  public void encodeNodeId(NodeId value) throws UaSerializationException {
    if (value == null) value = NodeId.NULL_VALUE;

    int namespaceIndex = value.getNamespaceIndex().intValue();

    if (value.getType() == IdType.Numeric) {
      UInteger identifier = (UInteger) value.getIdentifier();
      long idv = identifier.longValue();

      if (namespaceIndex == 0 && idv >= 0 && idv <= 255) {
        /* Two-byte format */
        buffer.writeByte(0x00);
        buffer.writeByte((int) idv);
      } else if (namespaceIndex >= 0 && namespaceIndex <= 255 && idv <= 65535) {
        /* Four-byte format */
        buffer.writeByte(0x01);
        buffer.writeByte(namespaceIndex);
        buffer.writeShortLE((int) idv);
      } else {
        /* Numeric format */
        buffer.writeByte(0x02);
        buffer.writeShortLE(namespaceIndex);
        buffer.writeIntLE((int) idv);
      }
    } else if (value.getType() == IdType.String) {
      String identifier = (String) value.getIdentifier();

      buffer.writeByte(0x03);
      buffer.writeShortLE(namespaceIndex);
      encodeString(identifier);
    } else if (value.getType() == IdType.Guid) {
      UUID identifier = (UUID) value.getIdentifier();

      buffer.writeByte(0x04);
      buffer.writeShortLE(namespaceIndex);
      encodeGuid(identifier);
    } else if (value.getType() == IdType.Opaque) {
      ByteString identifier = (ByteString) value.getIdentifier();

      buffer.writeByte(0x05);
      buffer.writeShortLE(namespaceIndex);
      encodeByteString(identifier);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "invalid identifier: " + value.getIdentifier());
    }
  }

  public void encodeQualifiedName(QualifiedName value) throws UaSerializationException {
    if (value == null) value = QualifiedName.NULL_VALUE;

    encodeUInt16(value.namespaceIndex());
    encodeString(value.name());
  }

  public void encodeString(String value) throws UaSerializationException {
    if (value == null) {
      buffer.writeIntLE(-1);
    } else {
      encodeLengthPrefixedString(value, CHARSET_UTF8);
    }
  }

  public void encodeStatusCode(StatusCode value) throws UaSerializationException {
    if (value == null) {
      buffer.writeIntLE(0);
    } else {
      buffer.writeIntLE((int) value.value());
    }
  }

  public void encodeVariant(Variant variant) throws UaSerializationException {
    Object value = variant.value();

    if (value == null) {
      buffer.writeByte(0);
    } else {
      boolean structure = false;
      boolean enumeration = false;
      boolean optionSet = false;
      Class<?> valueClass;

      if (value instanceof Matrix matrix) {
        Object elements = matrix.getElements();
        if (elements == null) {
          buffer.writeByte(0);
          return;
        }
        valueClass = getClass(elements);
      } else {
        valueClass = getClass(value);
      }

      if (UaStructuredType.class.isAssignableFrom(valueClass)) {
        valueClass = ExtensionObject.class;
        structure = true;
      } else if (UaEnumeratedType.class.isAssignableFrom(valueClass)) {
        valueClass = Integer.class;
        enumeration = true;
      } else if (OptionSetUInteger.class.isAssignableFrom(valueClass)) {
        assert value instanceof OptionSetUInteger<?>;
        valueClass = ((OptionSetUInteger<?>) value).getValue().getClass();
        optionSet = true;
      }

      int typeId = OpcUaDataType.getBuiltinTypeId(valueClass);

      if (typeId == -1) {
        LoggerFactory.getLogger(getClass())
            .warn(
                "Not a built-in type: value={}, valueClass={}", value, valueClass, new Exception());
      }

      if (value.getClass().isArray() || value instanceof Matrix) {
        if (value.getClass().isArray()) {
          buffer.writeByte(typeId | 0x80);

          int length = Array.getLength(value);
          buffer.writeIntLE(length);

          for (int i = 0; i < length; i++) {
            Object o = Array.get(value, i);

            encodeValue(o, typeId, structure, enumeration, optionSet);
          }
        } else {
          int[] dimensions = ((Matrix) value).getDimensions();

          buffer.writeByte(typeId | 0xC0);

          Object elements = ((Matrix) value).getElements();
          int length = Array.getLength(elements);
          buffer.writeIntLE(length);

          for (int i = 0; i < length; i++) {
            Object o = Array.get(elements, i);

            encodeValue(o, typeId, structure, enumeration, optionSet);
          }

          encodeInt32(dimensions.length);
          for (int dimension : dimensions) {
            encodeInt32(dimension);
          }
        }
      } else {
        buffer.writeByte(typeId);

        encodeValue(value, typeId, structure, enumeration, optionSet);
      }
    }
  }

  // endregion

  private void encodeValue(
      Object value, int typeId, boolean structure, boolean enumeration, boolean optionSet) {
    if (structure) {
      UaStructuredType struct = (UaStructuredType) value;

      ExtensionObject extensionObject = ExtensionObject.encode(context, struct);

      encodeBuiltinType(typeId, extensionObject);
    } else if (enumeration) {
      encodeBuiltinType(typeId, ((UaEnumeratedType) value).getValue());
    } else if (optionSet) {
      encodeBuiltinType(typeId, ((OptionSetUInteger<?>) value).getValue());
    } else {
      encodeBuiltinType(typeId, value);
    }
  }

  private void encodeBuiltinType(int typeId, Object value) throws UaSerializationException {
    switch (typeId) {
      case 1:
        encodeBoolean(null, (Boolean) value);
        break;
      case 2:
        encodeSByte((Byte) value);
        break;
      case 3:
        encodeByte((UByte) value);
        break;
      case 4:
        encodeInt16((Short) value);
        break;
      case 5:
        encodeUInt16((UShort) value);
        break;
      case 6:
        encodeInt32((Integer) value);
        break;
      case 7:
        encodeUInt32((UInteger) value);
        break;
      case 8:
        encodeInt64((Long) value);
        break;
      case 9:
        encodeUInt64((ULong) value);
        break;
      case 10:
        encodeFloat((Float) value);
        break;
      case 11:
        encodeDouble((Double) value);
        break;
      case 12:
        encodeString((String) value);
        break;
      case 13:
        encodeDateTime((DateTime) value);
        break;
      case 14:
        encodeGuid((UUID) value);
        break;
      case 15:
        encodeByteString((ByteString) value);
        break;
      case 16:
        encodeXmlElement((XmlElement) value);
        break;
      case 17:
        encodeNodeId((NodeId) value);
        break;
      case 18:
        encodeExpandedNodeId((ExpandedNodeId) value);
        break;
      case 19:
        encodeStatusCode((StatusCode) value);
        break;
      case 20:
        encodeQualifiedName((QualifiedName) value);
        break;
      case 21:
        encodeLocalizedText((LocalizedText) value);
        break;
      case 22:
        encodeExtensionObject((ExtensionObject) value);
        break;
      case 23:
        encodeDataValue((DataValue) value);
        break;
      case 24:
        encodeVariant((Variant) value);
        break;
      case 25:
        encodeDiagnosticInfo((DiagnosticInfo) value);
        break;
      default:
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "unknown builtin type: " + typeId);
    }
  }

  private Class<?> getClass(@NonNull Object o) {
    if (o.getClass().isArray()) {
      return ArrayUtil.getType(o);
    } else {
      return o.getClass();
    }
  }

  private void encodeLengthPrefixedString(String value, Charset charset)
      throws UaSerializationException {
    byte[] bytes = value.getBytes(charset);
    int length = bytes.length;

    if (length > context.getEncodingLimits().getMaxMessageSize()) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingLimitsExceeded,
          String.format(
              "string length exceeds max message size (length=%s, max=%s)",
              length, context.getEncodingLimits().getMaxMessageSize()));
    }

    encodeInt32(length);
    buffer.writeBytes(bytes);
  }

  @Override
  public void encodeBoolean(String field, Boolean value) throws UaSerializationException {
    encodeBoolean(value);
  }

  @Override
  public void encodeSByte(String field, Byte value) throws UaSerializationException {
    encodeSByte(value);
  }

  @Override
  public void encodeInt16(String field, Short value) throws UaSerializationException {
    encodeInt16(value);
  }

  @Override
  public void encodeInt32(String field, Integer value) throws UaSerializationException {
    encodeInt32(value);
  }

  @Override
  public void encodeInt64(String field, Long value) throws UaSerializationException {
    encodeInt64(value);
  }

  @Override
  public void encodeByte(String field, UByte value) throws UaSerializationException {
    encodeByte(value);
  }

  @Override
  public void encodeUInt16(String field, UShort value) throws UaSerializationException {
    encodeUInt16(value);
  }

  @Override
  public void encodeUInt32(String field, UInteger value) throws UaSerializationException {
    encodeUInt32(value);
  }

  @Override
  public void encodeUInt64(String field, ULong value) throws UaSerializationException {
    encodeUInt64(value);
  }

  @Override
  public void encodeFloat(String field, Float value) throws UaSerializationException {
    encodeFloat(value);
  }

  @Override
  public void encodeDouble(String field, Double value) throws UaSerializationException {
    encodeDouble(value);
  }

  @Override
  public void encodeString(String field, String value) throws UaSerializationException {
    encodeString(value);
  }

  @Override
  public void encodeDateTime(String field, DateTime value) throws UaSerializationException {
    encodeDateTime(value);
  }

  @Override
  public void encodeGuid(String field, UUID value) throws UaSerializationException {
    encodeGuid(value);
  }

  @Override
  public void encodeByteString(String field, ByteString value) throws UaSerializationException {
    encodeByteString(value);
  }

  @Override
  public void encodeXmlElement(String field, XmlElement value) throws UaSerializationException {
    encodeXmlElement(value);
  }

  @Override
  public void encodeNodeId(String field, NodeId value) throws UaSerializationException {
    encodeNodeId(value);
  }

  @Override
  public void encodeExpandedNodeId(String field, ExpandedNodeId value)
      throws UaSerializationException {
    encodeExpandedNodeId(value);
  }

  @Override
  public void encodeStatusCode(String field, StatusCode value) throws UaSerializationException {
    encodeStatusCode(value);
  }

  @Override
  public void encodeQualifiedName(String field, QualifiedName value)
      throws UaSerializationException {
    encodeQualifiedName(value);
  }

  @Override
  public void encodeLocalizedText(String field, LocalizedText value)
      throws UaSerializationException {
    encodeLocalizedText(value);
  }

  @Override
  public void encodeExtensionObject(String field, ExtensionObject value)
      throws UaSerializationException {
    encodeExtensionObject(value);
  }

  @Override
  public void encodeDataValue(String field, DataValue value) throws UaSerializationException {
    encodeDataValue(value);
  }

  @Override
  public void encodeVariant(String field, Variant value) throws UaSerializationException {
    encodeVariant(value);
  }

  @Override
  public void encodeDiagnosticInfo(String field, DiagnosticInfo value)
      throws UaSerializationException {
    encodeDiagnosticInfo(value);
  }

  @Override
  public void encodeMessage(String field, UaMessageType message) throws UaSerializationException {
    ExpandedNodeId xBinaryEncodingId = message.getBinaryEncodingId();

    NodeId encodingId =
        xBinaryEncodingId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + xBinaryEncodingId.namespace()));

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "no codec registered: " + encodingId);
    }

    encodeNodeId(encodingId);

    codec.encode(context, this, message);
  }

  @Override
  public void encodeEnum(String field, UaEnumeratedType value) {
    encodeInt32(field, value.getValue());
  }

  @Override
  public void encodeStruct(String field, UaStructuredType value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {

    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not not registered: " + dataTypeId));

    encodeStruct(field, value, localDataTypeId);
  }

  @Override
  public void encodeStruct(String field, UaStructuredType value, NodeId dataTypeId)
      throws UaSerializationException {

    try {
      DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

      if (codec == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId);
      }

      codec.encode(context, this, value);
    } catch (ClassCastException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public void encodeStruct(String field, UaStructuredType value, DataTypeCodec codec)
      throws UaSerializationException {

    codec.encode(context, this, value);
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
    encodeArray(value, v -> encodeEnum(field, v));
  }

  @Override
  public void encodeStructArray(String field, UaStructuredType[] values, NodeId dataTypeId)
      throws UaSerializationException {

    encodeArray(values, o -> encodeStruct(field, o, dataTypeId));
  }

  @Override
  public void encodeStructArray(String field, UaStructuredType[] value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {

    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError, "no codec registered: " + dataTypeId));

    encodeStructArray(field, value, localDataTypeId);
  }

  private <T> void encodeArray(String field, T[] values, BiConsumer<String, T> encoder)
      throws UaSerializationException {

    if (values == null) {
      buffer.writeIntLE(-1);
    } else {
      if (values.length > context.getEncodingLimits().getMaxMessageSize()) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingLimitsExceeded, "array length exceeds max message size");
      }

      encodeInt32(values.length);

      for (T t : values) {
        encoder.accept(field, t);
      }
    }
  }

  @Override
  public void encodeMatrix(String field, Matrix value) throws UaSerializationException {
    Object elements = value.getElements();

    if (elements == null) {
      buffer.writeIntLE(-1);
      return;
    }

    int[] dimensions = value.getDimensions();
    assert dimensions.length > 1;

    boolean noZeroDimensions = true;
    buffer.writeIntLE(dimensions.length);
    for (int d : dimensions) {
      if (d <= 0) noZeroDimensions = false;
      buffer.writeIntLE(d);
    }

    if (noZeroDimensions) {
      int length = Array.getLength(elements);
      assert length == Arrays.stream(dimensions).reduce(1, (left, right) -> left * right);

      int typeId =
          value.getDataType().orElseThrow().getTypeId(); // won't throw, we checked for null

      for (int i = 0; i < length; i++) {
        Object o = Array.get(elements, i);

        encodeValue(
            o,
            typeId,
            o instanceof UaStructuredType,
            o instanceof UaEnumeratedType,
            o instanceof OptionSetUInteger);
      }
    }
  }

  @Override
  public void encodeEnumMatrix(String field, Matrix value) throws UaSerializationException {
    Object elements = value.getElements();

    if (elements == null) {
      buffer.writeIntLE(-1);
      return;
    }

    int[] dimensions = value.getDimensions();
    assert dimensions.length > 1;

    boolean noZeroDimensions = true;
    buffer.writeIntLE(dimensions.length);
    for (int d : dimensions) {
      if (d <= 0) noZeroDimensions = false;
      buffer.writeIntLE(d);
    }

    if (noZeroDimensions) {
      int length = Array.getLength(elements);
      assert length == Arrays.stream(dimensions).reduce(1, (left, right) -> left * right);

      for (int i = 0; i < length; i++) {
        Object o = Array.get(elements, i);

        encodeEnum(null, (UaEnumeratedType) o);
      }
    }
  }

  @Override
  public void encodeStructMatrix(String field, Matrix value, NodeId dataTypeId)
      throws UaSerializationException {

    Object elements = value.getElements();

    if (elements == null) {
      buffer.writeIntLE(-1);
      return;
    }

    int[] dimensions = value.getDimensions();
    assert dimensions.length > 1;

    boolean noZeroDimensions = true;
    buffer.writeIntLE(dimensions.length);
    for (int d : dimensions) {
      if (d <= 0) noZeroDimensions = false;
      buffer.writeIntLE(d);
    }

    if (noZeroDimensions) {
      int length = Array.getLength(elements);
      assert length == Arrays.stream(dimensions).reduce(1, (left, right) -> left * right);

      for (int i = 0; i < length; i++) {
        Object o = Array.get(elements, i);

        encodeStruct(null, (UaStructuredType) o, dataTypeId);
      }
    }
  }

  @Override
  public void encodeStructMatrix(String field, Matrix value, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "encodeStructArray: namespace not registered: " + dataTypeId));

    encodeStructMatrix(field, value, localDataTypeId);
  }
}
