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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

public class OpcUaBinaryDecoder implements UaDecoder {

  private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
  private static final Charset CHARSET_UTF16 = StandardCharsets.UTF_16;

  private ByteBuf buffer;

  private int currentByte = 0;
  private int bitsRemaining = 0;

  private final AtomicInteger depth = new AtomicInteger(0);

  private final EncodingContext context;

  public OpcUaBinaryDecoder(EncodingContext context) {
    this.context = context;
  }

  public OpcUaBinaryDecoder setBuffer(ByteBuf buffer) {
    this.buffer = buffer;
    return this;
  }

  @Override
  public EncodingContext getEncodingContext() {
    return context;
  }

  public <T> T[] decodeArray(Supplier<T> read, Class<T> clazz) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      @SuppressWarnings("unchecked")
      T[] array = (T[]) Array.newInstance(clazz, length);

      for (int i = 0; i < length; i++) {
        array[i] = read.get();
      }

      return array;
    }
  }

  public int decodeBit() throws UaSerializationException {
    if (bitsRemaining == 0) {
      currentByte = buffer.readUnsignedByte();
      bitsRemaining = 8;
    }

    int bit = currentByte & 1;
    currentByte >>= 1;
    bitsRemaining--;
    return bit;
  }

  public Character decodeCharacter() throws UaSerializationException {
    return (char) (buffer.readUnsignedByte());
  }

  public Character decodeWideChar() throws UaSerializationException {
    return buffer.readChar();
  }

  public String decodeUtf8NullTerminatedString() throws UaSerializationException {
    return decodeNullTerminatedString(CHARSET_UTF8);
  }

  public String decodeUtf8CharArray() throws UaSerializationException {
    return decodeLengthPrefixedString(CHARSET_UTF8);
  }

  public String decodeUtf16NullTerminatedString() throws UaSerializationException {
    return decodeNullTerminatedString(CHARSET_UTF16);
  }

  public String decodeUtf16CharArray() throws UaSerializationException {
    return decodeLengthPrefixedString(CHARSET_UTF16);
  }

  public Boolean decodeBoolean() throws UaSerializationException {
    return buffer.readBoolean();
  }

  public Byte decodeSByte() throws UaSerializationException {
    return buffer.readByte();
  }

  public UByte decodeByte() throws UaSerializationException {
    return ubyte(buffer.readUnsignedByte());
  }

  public Short decodeInt16() throws UaSerializationException {
    return buffer.readShortLE();
  }

  public UShort decodeUInt16() throws UaSerializationException {
    return ushort(buffer.readUnsignedShortLE());
  }

  public Integer decodeInt32() throws UaSerializationException {
    return buffer.readIntLE();
  }

  public UInteger decodeUInt32() throws UaSerializationException {
    return uint(buffer.readUnsignedIntLE());
  }

  public Long decodeInt64() throws UaSerializationException {
    return buffer.readLongLE();
  }

  public ULong decodeUInt64() throws UaSerializationException {
    return ulong(buffer.readLongLE());
  }

  public Float decodeFloat() throws UaSerializationException {
    return buffer.readFloatLE();
  }

  public Double decodeDouble() throws UaSerializationException {
    return buffer.readDoubleLE();
  }

  public DateTime decodeDateTime() throws UaSerializationException {
    return new DateTime(buffer.readLongLE());
  }

  public ByteString decodeByteString() throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return ByteString.NULL_VALUE;
    } else {
      checkArrayLength(length);

      byte[] bs = new byte[length];
      buffer.readBytes(bs);
      return new ByteString(bs);
    }
  }

  public UUID decodeGuid() throws UaSerializationException {
    long part1 = buffer.readUnsignedIntLE();
    long part2 = buffer.readUnsignedShortLE();
    long part3 = buffer.readUnsignedShortLE();
    long part4 = buffer.readLong(); // intentionally Big Endian

    long msb = (part1 << 32) | (part2 << 16) | part3;

    return new UUID(msb, part4);
  }

  public XmlElement decodeXmlElement() throws UaSerializationException {
    ByteString byteString = decodeByteString();
    byte[] bs = byteString.bytes();

    if (bs == null) {
      return new XmlElement(null);
    } else {
      String fragment = new String(bs, StandardCharsets.UTF_8);
      return new XmlElement(fragment);
    }
  }

  public DataValue decodeDataValue() throws UaSerializationException {
    int mask = buffer.readByte() & 0xFF;

    Variant value = ((mask & 0x01) != 0) ? decodeVariant() : Variant.NULL_VALUE;
    StatusCode status = ((mask & 0x02) != 0) ? decodeStatusCode() : StatusCode.GOOD;
    DateTime sourceTime = ((mask & 0x04) != 0) ? decodeDateTime() : DateTime.MIN_VALUE;
    UShort sourcePicoseconds = ((mask & 0x10) != 0) ? decodeUInt16() : null;
    DateTime serverTime = ((mask & 0x08) != 0) ? decodeDateTime() : DateTime.MIN_VALUE;
    UShort serverPicoseconds = ((mask & 0x20) != 0) ? decodeUInt16() : null;

    return new DataValue(
        value, status, sourceTime, sourcePicoseconds, serverTime, serverPicoseconds);
  }

  public DiagnosticInfo decodeDiagnosticInfo() throws UaSerializationException {
    if (depth.get() >= context.getEncodingLimits().getMaxRecursionDepth()) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingLimitsExceeded,
          "max recursion depth exceeded: " + context.getEncodingLimits().getMaxRecursionDepth());
    }

    depth.incrementAndGet();
    try {
      int mask = buffer.readByte();

      if (mask == 0) {
        return null;
      } else {
        int symbolicId = ((mask & 0x01) == 0x01) ? buffer.readIntLE() : -1;
        int namespaceUri = ((mask & 0x02) == 0x02) ? buffer.readIntLE() : -1;
        int localizedText = ((mask & 0x04) == 0x04) ? buffer.readIntLE() : -1;
        int locale = ((mask & 0x08) == 0x08) ? buffer.readIntLE() : -1;
        String additionalInfo = ((mask & 0x10) == 0x10) ? decodeString() : null;
        StatusCode innerStatusCode = ((mask & 0x20) == 0x20) ? decodeStatusCode() : null;
        DiagnosticInfo innerDiagnosticInfo =
            ((mask & 0x40) == 0x40) ? decodeDiagnosticInfo() : null;

        return new DiagnosticInfo(
            namespaceUri,
            symbolicId,
            locale,
            localizedText,
            additionalInfo,
            innerStatusCode,
            innerDiagnosticInfo);
      }
    } finally {
      depth.decrementAndGet();
    }
  }

  public ExpandedNodeId decodeExpandedNodeId() throws UaSerializationException {
    int flags = buffer.getByte(buffer.readerIndex());

    NodeId nodeId = decodeNodeId();

    String namespaceUri = null;
    UInteger serverIndex = UInteger.MIN;

    if ((flags & 0x80) == 0x80) {
      namespaceUri = decodeString();
    }

    if ((flags & 0x40) == 0x40) {
      serverIndex = decodeUInt32();
    }

    ExpandedNodeId.ServerReference server = ExpandedNodeId.ServerReference.of(serverIndex);

    ExpandedNodeId.NamespaceReference namespace;
    if (namespaceUri == null) {
      namespace = ExpandedNodeId.NamespaceReference.of(nodeId.getNamespaceIndex());
    } else {
      namespace = ExpandedNodeId.NamespaceReference.of(namespaceUri);
    }

    return new ExpandedNodeId(server, namespace, nodeId.getIdentifier());
  }

  public ExtensionObject decodeExtensionObject() throws UaSerializationException {
    NodeId encodingTypeId = decodeNodeId();
    int encoding = buffer.readByte();

    if (encoding == 0) {
      return ExtensionObject.of(ByteString.NULL_VALUE, encodingTypeId);
    } else if (encoding == 1) {
      ByteString byteString = decodeByteString();

      return ExtensionObject.of(byteString, encodingTypeId);
    } else if (encoding == 2) {
      XmlElement xmlElement = decodeXmlElement();

      return ExtensionObject.of(xmlElement, encodingTypeId);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "unknown ExtensionObject encoding: " + encoding);
    }
  }

  public LocalizedText decodeLocalizedText() throws UaSerializationException {
    int mask = buffer.readByte();

    String locale = null;
    String text = null;

    if ((mask & 1) == 1) {
      locale = decodeString();
    }

    if ((mask & 2) == 2) {
      text = decodeString();
    }

    return new LocalizedText(locale, text);
  }

  public NodeId decodeNodeId() throws UaSerializationException {
    int format = buffer.readByte() & 0x0F;

    if (format == 0x00) {
      /* Two-byte format */
      return new NodeId(UShort.MIN, uint(buffer.readUnsignedByte()));
    } else if (format == 0x01) {
      /* Four-byte format */
      return new NodeId(ushort(buffer.readUnsignedByte()), uint(buffer.readUnsignedShortLE()));
    } else if (format == 0x02) {
      /* Numeric format */
      return new NodeId(decodeUInt16(), decodeUInt32());
    } else if (format == 0x03) {
      /* String format */
      return new NodeId(decodeUInt16(), decodeString());
    } else if (format == 0x04) {
      /* Guid format */
      return new NodeId(decodeUInt16(), decodeGuid());
    } else if (format == 0x05) {
      /* Opaque format */
      return new NodeId(decodeUInt16(), decodeByteString());
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "invalid NodeId format: " + format);
    }
  }

  public QualifiedName decodeQualifiedName() throws UaSerializationException {
    UShort namespaceIndex = decodeUInt16();
    String name = decodeString();

    // invalid QualifiedNames become "null" QualifiedNames
    if (name != null && name.length() > 512) {
      name = null;
    }

    return new QualifiedName(namespaceIndex, name);
  }

  public StatusCode decodeStatusCode() throws UaSerializationException {
    return new StatusCode(buffer.readUnsignedIntLE());
  }

  public String decodeString() throws UaSerializationException {
    return decodeLengthPrefixedString(CHARSET_UTF8);
  }

  public Variant decodeVariant() throws UaSerializationException {
    if (depth.get() >= context.getEncodingLimits().getMaxRecursionDepth()) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingLimitsExceeded,
          "max recursion depth exceeded: " + context.getEncodingLimits().getMaxRecursionDepth());
    }

    depth.incrementAndGet();
    try {
      int encodingMask = buffer.readByte();

      if (encodingMask == 0) {
        return new Variant(null);
      } else {
        int typeId = encodingMask & 0x3F;
        boolean dimensionsEncoded = (encodingMask & 0x40) == 0x40;
        boolean arrayEncoded = (encodingMask & 0x80) == 0x80;

        if (arrayEncoded) {
          int length = buffer.readIntLE();

          if (length == -1) {
            return new Variant(null);
          } else {
            checkArrayLength(length);

            Object flatArray = decodeBuiltinTypeArray(typeId, length);

            int[] dimensions = dimensionsEncoded ? decodeDimensions() : new int[] {length};

            Object value;
            if (dimensionsEncoded && dimensions.length > 0) {
              int dimensionsLength = calculateMatrixLength(dimensions);
              if (dimensionsLength != length) {
                throw new UaSerializationException(
                    StatusCodes.Bad_DecodingError,
                    String.format(
                        "matrix dimensions do not match array length (dimensionsLength=%s,"
                            + " arrayLength=%s)",
                        dimensionsLength, length));
              }
            }

            if (dimensions.length > 1) {
              value = new Matrix(flatArray, dimensions, OpcUaDataType.fromTypeId(typeId));
            } else {
              value = flatArray;
            }

            return new Variant(value);
          }
        } else {
          Object value = decodeBuiltinType(typeId);

          return new Variant(value);
        }
      }
    } finally {
      depth.decrementAndGet();
    }
  }

  @Nullable
  private String decodeLengthPrefixedString(Charset charset) {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      if (length > context.getEncodingLimits().getMaxMessageSize()) {
        throw new UaSerializationException(
            StatusCodes.Bad_EncodingLimitsExceeded,
            String.format(
                "string length exceeds max message size (length=%s, max=%s)",
                length, context.getEncodingLimits().getMaxMessageSize()));
      }

      String str = buffer.toString(buffer.readerIndex(), length, charset);
      buffer.skipBytes(length);
      return str;
    }
  }

  private String decodeNullTerminatedString(Charset charset) {
    int indexOfNull = buffer.forEachByte(ByteProcessor.FIND_NUL);

    if (indexOfNull == -1) {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "null terminator not found");
    }

    int index = buffer.readerIndex();
    int length = indexOfNull - index;
    String str = buffer.toString(index, length, charset);
    buffer.skipBytes(length + 1);

    return str;
  }

  private int[] decodeDimensions() {
    int length = buffer.readIntLE();

    if (length == -1) {
      return new int[0];
    } else {
      checkArrayLength(length);

      int[] is = new int[length];
      for (int i = 0; i < length; i++) {
        is[i] = buffer.readIntLE();
      }
      return is;
    }
  }

  private Object decodeBuiltinType(int typeId) throws UaSerializationException {
    return switch (typeId) {
      case 1 -> decodeBoolean();
      case 2 -> decodeSByte();
      case 3 -> decodeByte();
      case 4 -> decodeInt16();
      case 5 -> decodeUInt16();
      case 6 -> decodeInt32();
      case 7 -> decodeUInt32();
      case 8 -> decodeInt64();
      case 9 -> decodeUInt64();
      case 10 -> decodeFloat();
      case 11 -> decodeDouble();
      case 12 -> decodeString();
      case 13 -> decodeDateTime();
      case 14 -> decodeGuid();
      case 15 -> decodeByteString();
      case 16 -> decodeXmlElement();
      case 17 -> decodeNodeId();
      case 18 -> decodeExpandedNodeId();
      case 19 -> decodeStatusCode();
      case 20 -> decodeQualifiedName();
      case 21 -> decodeLocalizedText();
      case 22 -> decodeExtensionObject();
      case 23 -> decodeDataValue();
      case 24 -> decodeVariant();
      case 25 -> decodeDiagnosticInfo();
      default ->
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "unknown builtin type: " + typeId);
    };
  }

  /**
   * Decode {@code length} elements of the builtin type identified by {@code typeId} into a new
   * array.
   *
   * <p>The component type of the returned array is the builtin type's backing class, i.e. the same
   * {@link Class} that {@link OpcUaDataType#getBackingClass(int)} reports for {@code typeId}.
   * Callers rely on this: {@link Matrix} and {@link Variant} both derive their DataType from the
   * component type, including when the array is empty.
   *
   * @param typeId the id of the builtin type to decode.
   * @param length the number of elements to decode.
   * @return an array of {@code length} decoded elements.
   * @throws UaSerializationException if {@code typeId} is not a builtin type id.
   */
  private Object decodeBuiltinTypeArray(int typeId, int length) throws UaSerializationException {
    return switch (typeId) {
      case 1 -> {
        Boolean[] values = new Boolean[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeBoolean();
        }
        yield values;
      }
      case 2 -> {
        Byte[] values = new Byte[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeSByte();
        }
        yield values;
      }
      case 3 -> {
        UByte[] values = new UByte[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeByte();
        }
        yield values;
      }
      case 4 -> {
        Short[] values = new Short[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeInt16();
        }
        yield values;
      }
      case 5 -> {
        UShort[] values = new UShort[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeUInt16();
        }
        yield values;
      }
      case 6 -> {
        Integer[] values = new Integer[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeInt32();
        }
        yield values;
      }
      case 7 -> {
        UInteger[] values = new UInteger[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeUInt32();
        }
        yield values;
      }
      case 8 -> {
        Long[] values = new Long[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeInt64();
        }
        yield values;
      }
      case 9 -> {
        ULong[] values = new ULong[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeUInt64();
        }
        yield values;
      }
      case 10 -> {
        Float[] values = new Float[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeFloat();
        }
        yield values;
      }
      case 11 -> {
        Double[] values = new Double[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeDouble();
        }
        yield values;
      }
      case 12 -> {
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeString();
        }
        yield values;
      }
      case 13 -> {
        DateTime[] values = new DateTime[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeDateTime();
        }
        yield values;
      }
      case 14 -> {
        UUID[] values = new UUID[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeGuid();
        }
        yield values;
      }
      case 15 -> {
        ByteString[] values = new ByteString[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeByteString();
        }
        yield values;
      }
      case 16 -> {
        XmlElement[] values = new XmlElement[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeXmlElement();
        }
        yield values;
      }
      case 17 -> {
        NodeId[] values = new NodeId[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeNodeId();
        }
        yield values;
      }
      case 18 -> {
        ExpandedNodeId[] values = new ExpandedNodeId[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeExpandedNodeId();
        }
        yield values;
      }
      case 19 -> {
        StatusCode[] values = new StatusCode[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeStatusCode();
        }
        yield values;
      }
      case 20 -> {
        QualifiedName[] values = new QualifiedName[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeQualifiedName();
        }
        yield values;
      }
      case 21 -> {
        LocalizedText[] values = new LocalizedText[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeLocalizedText();
        }
        yield values;
      }
      case 22 -> {
        ExtensionObject[] values = new ExtensionObject[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeExtensionObject();
        }
        yield values;
      }
      case 23 -> {
        DataValue[] values = new DataValue[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeDataValue();
        }
        yield values;
      }
      case 24 -> {
        Variant[] values = new Variant[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeVariant();
        }
        yield values;
      }
      case 25 -> {
        DiagnosticInfo[] values = new DiagnosticInfo[length];
        for (int i = 0; i < length; i++) {
          values[i] = decodeDiagnosticInfo();
        }
        yield values;
      }
      default ->
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError, "unknown builtin type: " + typeId);
    };
  }

  @Override
  public Boolean decodeBoolean(String field) throws UaSerializationException {
    return decodeBoolean();
  }

  @Override
  public Byte decodeSByte(String field) throws UaSerializationException {
    return decodeSByte();
  }

  @Override
  public Short decodeInt16(String field) throws UaSerializationException {
    return decodeInt16();
  }

  @Override
  public Integer decodeInt32(String field) throws UaSerializationException {
    return decodeInt32();
  }

  @Override
  public Long decodeInt64(String field) throws UaSerializationException {
    return decodeInt64();
  }

  @Override
  public UByte decodeByte(String field) throws UaSerializationException {
    return decodeByte();
  }

  @Override
  public UShort decodeUInt16(String field) throws UaSerializationException {
    return decodeUInt16();
  }

  @Override
  public UInteger decodeUInt32(String field) throws UaSerializationException {
    return decodeUInt32();
  }

  @Override
  public ULong decodeUInt64(String field) throws UaSerializationException {
    return decodeUInt64();
  }

  @Override
  public Float decodeFloat(String field) throws UaSerializationException {
    return decodeFloat();
  }

  @Override
  public Double decodeDouble(String field) throws UaSerializationException {
    return decodeDouble();
  }

  @Override
  public String decodeString(String field) throws UaSerializationException {
    return decodeString();
  }

  @Override
  public DateTime decodeDateTime(String field) throws UaSerializationException {
    return decodeDateTime();
  }

  @Override
  public UUID decodeGuid(String field) throws UaSerializationException {
    return decodeGuid();
  }

  @Override
  public ByteString decodeByteString(String field) throws UaSerializationException {
    return decodeByteString();
  }

  @Override
  public XmlElement decodeXmlElement(String field) throws UaSerializationException {
    return decodeXmlElement();
  }

  @Override
  public NodeId decodeNodeId(String field) throws UaSerializationException {
    return decodeNodeId();
  }

  @Override
  public ExpandedNodeId decodeExpandedNodeId(String field) throws UaSerializationException {
    return decodeExpandedNodeId();
  }

  @Override
  public StatusCode decodeStatusCode(String field) throws UaSerializationException {
    return decodeStatusCode();
  }

  @Override
  public QualifiedName decodeQualifiedName(String field) throws UaSerializationException {
    return decodeQualifiedName();
  }

  @Override
  public LocalizedText decodeLocalizedText(String field) throws UaSerializationException {
    return decodeLocalizedText();
  }

  @Override
  public ExtensionObject decodeExtensionObject(String field) throws UaSerializationException {
    return decodeExtensionObject();
  }

  @Override
  public DataValue decodeDataValue(String field) throws UaSerializationException {
    return decodeDataValue();
  }

  @Override
  public Variant decodeVariant(String field) throws UaSerializationException {
    return decodeVariant();
  }

  @Override
  public DiagnosticInfo decodeDiagnosticInfo(String field) throws UaSerializationException {
    return decodeDiagnosticInfo();
  }

  @Override
  public UaMessageType decodeMessage(String field) throws UaSerializationException {
    NodeId encodingId = decodeNodeId();

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec != null) {
      return (UaMessageType) codec.decode(context, this);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "no codec registered: " + encodingId);
    }
  }

  @Override
  public Integer decodeEnum(String field) {
    return decodeInt32(field);
  }

  @Override
  public UaStructuredType decodeStruct(String field, NodeId dataTypeId)
      throws UaSerializationException {

    DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

    if (codec != null) {
      return codec.decode(context, this);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "no codec registered: " + dataTypeId);
    }
  }

  @Override
  public UaStructuredType decodeStruct(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException {

    NodeId localDataTypeId =
        dataTypeId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_DecodingError, "namespace not registered: " + dataTypeId));

    return decodeStruct(field, localDataTypeId);
  }

  @Override
  public UaStructuredType decodeStruct(String field, DataTypeCodec codec)
      throws UaSerializationException {

    return codec.decode(context, this);
  }

  @Override
  public Boolean[] decodeBooleanArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Boolean[] values = new Boolean[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeBoolean(field);
      }
      return values;
    }
  }

  @Override
  public Byte[] decodeSByteArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Byte[] values = new Byte[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeSByte(field);
      }
      return values;
    }
  }

  @Override
  public Short[] decodeInt16Array(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Short[] values = new Short[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeInt16(field);
      }
      return values;
    }
  }

  @Override
  public Integer[] decodeInt32Array(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Integer[] values = new Integer[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeInt32(field);
      }
      return values;
    }
  }

  @Override
  public Long[] decodeInt64Array(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Long[] values = new Long[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeInt64(field);
      }
      return values;
    }
  }

  @Override
  public UByte[] decodeByteArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      UByte[] values = new UByte[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeByte(field);
      }
      return values;
    }
  }

  @Override
  public UShort[] decodeUInt16Array(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      UShort[] values = new UShort[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeUInt16(field);
      }
      return values;
    }
  }

  @Override
  public UInteger[] decodeUInt32Array(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      UInteger[] values = new UInteger[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeUInt32(field);
      }
      return values;
    }
  }

  @Override
  public ULong[] decodeUInt64Array(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      ULong[] values = new ULong[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeUInt64(field);
      }
      return values;
    }
  }

  @Override
  public Float[] decodeFloatArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Float[] values = new Float[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeFloat(field);
      }
      return values;
    }
  }

  @Override
  public Double[] decodeDoubleArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Double[] values = new Double[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeDouble(field);
      }
      return values;
    }
  }

  @Override
  public String[] decodeStringArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      String[] values = new String[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeString(field);
      }
      return values;
    }
  }

  @Override
  public DateTime[] decodeDateTimeArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      DateTime[] values = new DateTime[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeDateTime(field);
      }
      return values;
    }
  }

  @Override
  public UUID[] decodeGuidArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      UUID[] values = new UUID[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeGuid(field);
      }
      return values;
    }
  }

  @Override
  public ByteString[] decodeByteStringArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      ByteString[] values = new ByteString[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeByteString(field);
      }
      return values;
    }
  }

  @Override
  public XmlElement[] decodeXmlElementArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      XmlElement[] values = new XmlElement[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeXmlElement(field);
      }
      return values;
    }
  }

  @Override
  public NodeId[] decodeNodeIdArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      NodeId[] values = new NodeId[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeNodeId(field);
      }
      return values;
    }
  }

  @Override
  public ExpandedNodeId[] decodeExpandedNodeIdArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      ExpandedNodeId[] values = new ExpandedNodeId[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeExpandedNodeId(field);
      }
      return values;
    }
  }

  @Override
  public StatusCode[] decodeStatusCodeArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      StatusCode[] values = new StatusCode[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeStatusCode(field);
      }
      return values;
    }
  }

  @Override
  public QualifiedName[] decodeQualifiedNameArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      QualifiedName[] values = new QualifiedName[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeQualifiedName(field);
      }
      return values;
    }
  }

  @Override
  public LocalizedText[] decodeLocalizedTextArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      LocalizedText[] values = new LocalizedText[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeLocalizedText(field);
      }
      return values;
    }
  }

  @Override
  public ExtensionObject[] decodeExtensionObjectArray(String field)
      throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      ExtensionObject[] values = new ExtensionObject[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeExtensionObject(field);
      }
      return values;
    }
  }

  @Override
  public DataValue[] decodeDataValueArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      DataValue[] values = new DataValue[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeDataValue(field);
      }
      return values;
    }
  }

  @Override
  public Variant[] decodeVariantArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      Variant[] values = new Variant[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeVariant(field);
      }
      return values;
    }
  }

  @Override
  public DiagnosticInfo[] decodeDiagnosticInfoArray(String field) throws UaSerializationException {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      DiagnosticInfo[] values = new DiagnosticInfo[length];
      for (int i = 0; i < length; i++) {
        values[i] = decodeDiagnosticInfo(field);
      }
      return values;
    }
  }

  @Override
  public Integer[] decodeEnumArray(String field) throws UaSerializationException {
    return decodeInt32Array(field);
  }

  @Override
  public UaStructuredType @Nullable [] decodeStructArray(String field, NodeId dataTypeId)
      throws UaSerializationException {

    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

      if (codec == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "no codec registered: " + dataTypeId);
      }

      UaStructuredType[] array = (UaStructuredType[]) Array.newInstance(codec.getType(), length);

      for (int i = 0; i < length; i++) {
        array[i] = codec.decode(context, this);
      }

      return array;
    }
  }

  @Override
  public UaStructuredType @Nullable [] decodeStructArray(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException {
    NodeId dataTypeNodeId = dataTypeId.toNodeId(context.getNamespaceTable()).orElse(null);

    if (dataTypeNodeId != null) {
      return decodeStructArray(field, dataTypeNodeId);
    } else {
      if (dataTypeId.isLocal()) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "namespace not registered: " + dataTypeId.namespace());
      } else {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "ExpandedNodeId not local: " + dataTypeId);
      }
    }
  }

  private void checkArrayLength(int length) throws UaSerializationException {
    if (length < 0) {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError,
          String.format("array length must not be negative (length=%s)", length));
    }

    if (length > context.getEncodingLimits().getMaxMessageSize()) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingLimitsExceeded,
          String.format(
              "array length exceeds max message size (length=%s, max=%s)",
              length, context.getEncodingLimits().getMaxMessageSize()));
    }
  }

  private int calculateMatrixLength(int[] dimensions) throws UaSerializationException {
    int length = 1;

    for (int dimension : dimensions) {
      if (dimension < 0) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            String.format("matrix dimension must not be negative (dimension=%s)", dimension));
      }

      checkArrayLength(dimension);

      if (length == 0 || dimension == 0) {
        length = 0;
      } else {
        try {
          length = Math.multiplyExact(length, dimension);
        } catch (ArithmeticException e) {
          throw new UaSerializationException(
              StatusCodes.Bad_EncodingLimitsExceeded,
              String.format(
                  "matrix length exceeds Integer.MAX_VALUE (dimensions=%s)",
                  Arrays.toString(dimensions)));
        }
      }
    }

    checkArrayLength(length);

    return length;
  }

  private <T> T[] decodeArray(String field, Function<String, T> decoder, Class<T> clazz)
      throws UaSerializationException {

    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      @SuppressWarnings("unchecked")
      T[] array = (T[]) Array.newInstance(clazz, length);

      for (int i = 0; i < length; i++) {
        array[i] = decoder.apply(field);
      }

      return array;
    }
  }

  @Override
  public Matrix decodeMatrix(String field, OpcUaDataType dataType) throws UaSerializationException {
    int[] dimensions = decodeMatrixDimensions();
    if (dimensions == null) return null;

    int length = calculateMatrixLength(dimensions);

    Object flatArray = decodeBuiltinTypeArray(dataType.getTypeId(), length);

    return new Matrix(flatArray, dimensions, dataType);
  }

  @Override
  public Matrix decodeEnumMatrix(String field) throws UaSerializationException {
    return decodeMatrix(field, OpcUaDataType.Int32);
  }

  @Override
  public Matrix decodeStructMatrix(String field, NodeId dataTypeId)
      throws UaSerializationException {
    int[] dimensions = decodeMatrixDimensions();
    if (dimensions == null) return null;

    int length = calculateMatrixLength(dimensions);

    DataTypeCodec codec = context.getDataTypeManager().getCodec(dataTypeId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "no codec registered: " + dataTypeId);
    }

    UaStructuredType[] flatArray = (UaStructuredType[]) Array.newInstance(codec.getType(), length);

    for (int i = 0; i < length; i++) {
      flatArray[i] = codec.decode(context, this);
    }

    return new Matrix(flatArray, dimensions, OpcUaDataType.ExtensionObject, dataTypeId.expanded());
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

  private int @Nullable [] decodeMatrixDimensions() {
    int length = buffer.readIntLE();

    if (length == -1) {
      return null;
    } else {
      checkArrayLength(length);

      int[] dimensions = new int[length];
      for (int i = 0; i < length; i++) {
        dimensions[i] = buffer.readIntLE();
      }

      return dimensions;
    }
  }
}
