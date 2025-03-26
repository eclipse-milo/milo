/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.serialization;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.serialization.codecs.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.serialization.codecs.OpcUaBinaryDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
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
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.eclipse.milo.opcua.stack.core.util.TypeUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

public class OpcUaBinaryStreamEncoder implements UaEncoder {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    private static final Charset CHARSET_UTF16 = StandardCharsets.UTF_16;

    private ByteBuf buffer;

    private int currentByte;
    private int bitCount;

    private final SerializationContext context;

    public OpcUaBinaryStreamEncoder(SerializationContext context) {
        this.context = context;
    }

    public OpcUaBinaryStreamEncoder setBuffer(ByteBuf buffer) {
        this.buffer = buffer;
        return this;
    }

    public <T> void writeArray(T[] values, Consumer<T> write) throws UaSerializationException {
        if (values == null) {
            buffer.writeIntLE(-1);
        } else {
            if (values.length > context.getEncodingLimits().getMaxMessageSize()) {
                throw new UaSerializationException(
                    StatusCodes.Bad_EncodingLimitsExceeded,
                    "array length exceeds max message size"
                );
            }

            writeInt32(values.length);

            for (T t : values) {
                write.accept(t);
            }
        }
    }

    public void writeBit(int value) throws UaSerializationException {
        currentByte = currentByte | ((value & 1) << bitCount);
        bitCount++;

        if (bitCount == 8) {
            buffer.writeByte(currentByte);
            currentByte = 0;
            bitCount = 0;
        }
    }

    public void writeBoolean(Boolean value) throws UaSerializationException {
        if (value == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(value);
        }
    }

    public void writeSByte(Byte value) throws UaSerializationException {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(value);
        }
    }

    public void writeByte(UByte value) throws UaSerializationException {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(value.intValue());
        }
    }

    public void writeInt16(Short value) throws UaSerializationException {
        if (value == null) {
            buffer.writeShortLE(0);
        } else {
            buffer.writeShortLE(value);
        }
    }

    public void writeUInt16(UShort value) throws UaSerializationException {
        if (value == null) {
            buffer.writeShortLE(0);
        } else {
            buffer.writeShortLE(value.intValue());
        }
    }

    public void writeInt32(Integer value) throws UaSerializationException {
        if (value == null) {
            buffer.writeIntLE(0);
        } else {
            buffer.writeIntLE(value);
        }
    }

    public void writeUInt32(UInteger value) throws UaSerializationException {
        if (value == null) {
            buffer.writeIntLE(0);
        } else {
            buffer.writeIntLE(value.intValue());
        }
    }

    public void writeInt64(Long value) throws UaSerializationException {
        if (value == null) {
            buffer.writeLongLE(0L);
        } else {
            buffer.writeLongLE(value);
        }
    }

    public void writeUInt64(ULong value) throws UaSerializationException {
        if (value == null) {
            buffer.writeLongLE(0L);
        } else {
            buffer.writeLongLE(value.longValue());
        }
    }

    public void writeFloat(Float value) throws UaSerializationException {
        if (value == null) {
            buffer.writeFloatLE(0f);
        } else {
            buffer.writeFloatLE(value);
        }
    }

    public void writeDouble(Double value) throws UaSerializationException {
        if (value == null) {
            buffer.writeDoubleLE(0d);
        } else {
            buffer.writeDoubleLE(value);
        }
    }

    public void writeCharacter(Character value) throws UaSerializationException {
        buffer.writeByte(value & 0xFF);
    }

    public void writeWideChar(Character value) throws UaSerializationException {
        buffer.writeChar(value);
    }

    public void writeUtf8NullTerminatedString(String value) throws UaSerializationException {
        if (value != null) {
            byte[] bytes = value.getBytes(CHARSET_UTF8);
            for (byte b : bytes) {
                buffer.writeByte(b);
                if (b == 0) return;
            }
        }
        buffer.writeByte(0);
    }

    public void writeUtf8CharArray(String value) throws UaSerializationException {
        if (value == null) {
            writeInt32(-1);
        } else {
            writeLengthPrefixedString(value, CHARSET_UTF8);
        }
    }

    public void writeUtf16NullTerminatedString(String value) throws UaSerializationException {
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

    public void writeUtf16CharArray(String value) throws UaSerializationException {
        if (value == null) {
            writeInt32(-1);
        } else {
            writeLengthPrefixedString(value, CHARSET_UTF16);
        }
    }

    public void writeDateTime(DateTime value) throws UaSerializationException {
        if (value == null) {
            buffer.writeLongLE(0L);
        } else {
            buffer.writeLongLE(value.getUtcTime());
        }
    }

    public void writeByteString(ByteString value) throws UaSerializationException {
        if (value == null || value.isNull()) {
            buffer.writeIntLE(-1);
        } else {
            byte[] bytes = value.bytes();

            assert (bytes != null);

            buffer.writeIntLE(bytes.length);
            buffer.writeBytes(bytes);
        }
    }

    public void writeGuid(UUID value) throws UaSerializationException {
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

    public void writeXmlElement(XmlElement value) throws UaSerializationException {
        if (value == null || value.isNull()) {
            buffer.writeIntLE(-1);
        } else {
            writeByteString(new ByteString(value.getFragment().getBytes(StandardCharsets.UTF_8)));
        }
    }

    public void writeDataValue(DataValue value) throws UaSerializationException {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            int mask = 0x00;

            if (value.getValue() != null && value.getValue().isNotNull()) {
                mask |= 0x01;
            }

            if (value.getStatusCode() != null &&
                !StatusCode.GOOD.equals(value.getStatusCode())) {

                mask |= 0x02;
            }

            if (value.getSourceTime() != null &&
                !DateTime.MIN_VALUE.equals(value.getSourceTime())) {

                mask |= 0x04;
            }

            if (value.getServerTime() != null &&
                !DateTime.MIN_VALUE.equals(value.getServerTime())) {

                mask |= 0x08;
            }

            if (value.getSourcePicoseconds() != null &&
                value.getSourcePicoseconds().intValue() != 0) {

                mask |= 0x10;
            }

            if (value.getServerPicoseconds() != null &&
                value.getServerPicoseconds().intValue() != 0) {

                mask |= 0x20;
            }

            buffer.writeByte(mask);

            // Value
            if ((mask & 0x01) == 0x01) {
                writeVariant(value.getValue());
            }

            // StatusCode
            if ((mask & 0x02) == 0x02) {
                writeStatusCode(value.getStatusCode());
            }

            // SourceTimestamp and SourcePicoseconds
            if ((mask & 0x04) == 0x04) {
                writeDateTime(value.getSourceTime());
            }
            if ((mask & 0x10) == 0x10) {
                writeUInt16(value.getSourcePicoseconds());
            }

            // ServerTimestamp and ServerPicoseconds
            if ((mask & 0x08) == 0x08) {
                writeDateTime(value.getServerTime());
            }
            if ((mask & 0x20) == 0x20) {
                writeUInt16(value.getServerPicoseconds());
            }
        }
    }

    public void writeDiagnosticInfo(DiagnosticInfo value) throws UaSerializationException {
        if (value == null) {
            buffer.writeByte(0);
        } else {
            int mask = 0x7F;

            if (value.getSymbolicId() == -1) mask ^= 0x01;
            if (value.getNamespaceUri() == -1) mask ^= 0x02;
            if (value.getLocalizedText() == -1) mask ^= 0x04;
            if (value.getLocale() == -1) mask ^= 0x08;
            if (value.getAdditionalInfo() == null || value.getAdditionalInfo().isEmpty()) mask ^= 0x10;
            if (value.getInnerStatusCode() == null) mask ^= 0x20;
            if (value.getInnerDiagnosticInfo() == null) mask ^= 0x40;

            buffer.writeByte(mask);

            if ((mask & 0x01) == 0x01) writeInt32(value.getSymbolicId());
            if ((mask & 0x02) == 0x02) writeInt32(value.getNamespaceUri());
            if ((mask & 0x04) == 0x04) writeInt32(value.getLocalizedText());
            if ((mask & 0x08) == 0x08) writeInt32(value.getLocale());
            if ((mask & 0x10) == 0x10) writeString(value.getAdditionalInfo());
            if ((mask & 0x20) == 0x20) writeStatusCode(value.getInnerStatusCode());
            if ((mask & 0x40) == 0x40) writeDiagnosticInfo(value.getInnerDiagnosticInfo());
        }
    }

    public void writeExpandedNodeId(ExpandedNodeId value) throws UaSerializationException {
        if (value == null) value = ExpandedNodeId.NULL_VALUE;

        int flags = 0;
        String namespaceUri = value.getNamespaceUri();
        UInteger serverIndex = value.getServerIndex();

        if (namespaceUri != null && namespaceUri.length() > 0) {
            flags |= 0x80;
        }

        if (serverIndex.longValue() > 0) {
            flags |= 0x40;
        }

        UShort index = value.getNamespaceIndex();
        if (index == null) index = UShort.MIN;
        int namespaceIndex = index.intValue();

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
            writeString(identifier);
        } else if (value.getType() == IdType.Guid) {
            UUID identifier = (UUID) value.getIdentifier();

            buffer.writeByte(0x04 | flags);
            buffer.writeShortLE(namespaceIndex);
            writeGuid(identifier);
        } else if (value.getType() == IdType.Opaque) {
            ByteString identifier = (ByteString) value.getIdentifier();

            buffer.writeByte(0x05 | flags);
            buffer.writeShortLE(namespaceIndex);
            writeByteString(identifier);
        } else {
            throw new UaSerializationException(
                StatusCodes.Bad_EncodingError,
                "invalid identifier: " + value.getIdentifier());
        }

        if (namespaceUri != null && namespaceUri.length() > 0) {
            writeString(namespaceUri);
        }

        if (serverIndex.longValue() > 0) {
            writeUInt32(serverIndex);
        }
    }

    public void writeExtensionObject(ExtensionObject value) throws UaSerializationException {
        if (value == null || value.getBody() == null) {
            writeNodeId(NodeId.NULL_VALUE);
            buffer.writeByte(0); // No body is encoded
        } else {
            Object body = value.getBody();

            switch (value.getBodyType()) {
                case ByteString: {
                    ByteString byteString = (ByteString) body;

                    writeNodeId(value.getEncodingId());
                    buffer.writeByte(1); // Body is binary encoded

                    writeByteString(byteString);

                    break;
                }
                case XmlElement: {
                    XmlElement xmlElement = (XmlElement) body;

                    writeNodeId(value.getEncodingId());
                    buffer.writeByte(2);

                    writeXmlElement(xmlElement);

                    break;
                }

                default:
                    throw new IllegalStateException("unknown body type: " + value.getBodyType());
            }
        }
    }

    public void writeLocalizedText(LocalizedText value) throws UaSerializationException {
        if (value == null) value = LocalizedText.NULL_VALUE;

        String locale = value.getLocale();
        String text = value.getText();

        int mask = 1 | 2;
        if (locale == null || locale.isEmpty()) {
            mask ^= 1;
        }
        if (text == null || text.isEmpty()) {
            mask ^= 2;
        }

        buffer.writeByte(mask);

        if (locale != null && !locale.isEmpty()) {
            writeString(locale);
        }
        if (text != null && !text.isEmpty()) {
            writeString(text);
        }
    }

    public void writeNodeId(NodeId value) throws UaSerializationException {
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
            writeString(identifier);
        } else if (value.getType() == IdType.Guid) {
            UUID identifier = (UUID) value.getIdentifier();

            buffer.writeByte(0x04);
            buffer.writeShortLE(namespaceIndex);
            writeGuid(identifier);
        } else if (value.getType() == IdType.Opaque) {
            ByteString identifier = (ByteString) value.getIdentifier();

            buffer.writeByte(0x05);
            buffer.writeShortLE(namespaceIndex);
            writeByteString(identifier);
        } else {
            throw new UaSerializationException(
                StatusCodes.Bad_EncodingError,
                "invalid identifier: " + value.getIdentifier());
        }
    }

    public void writeQualifiedName(QualifiedName value) throws UaSerializationException {
        if (value == null) value = QualifiedName.NULL_VALUE;

        writeUInt16(value.getNamespaceIndex());
        writeString(value.getName());
    }

    public void writeString(String value) throws UaSerializationException {
        if (value == null) {
            buffer.writeIntLE(-1);
        } else {
            writeLengthPrefixedString(value, CHARSET_UTF8);
        }
    }

    public void writeStatusCode(StatusCode value) throws UaSerializationException {
        if (value == null) {
            buffer.writeIntLE(0);
        } else {
            buffer.writeIntLE((int) value.getValue());
        }
    }

    public void writeVariant(Variant variant) throws UaSerializationException {
        Object value = variant.getValue();

        if (value == null) {
            buffer.writeByte(0);
        } else {
            boolean structure = false;
            boolean enumeration = false;
            Class<?> valueClass = getClass(value);

            if (UaStructure.class.isAssignableFrom(valueClass)) {
                valueClass = ExtensionObject.class;
                structure = true;
            } else if (UaEnumeration.class.isAssignableFrom(valueClass)) {
                valueClass = Integer.class;
                enumeration = true;
            }

            int typeId = TypeUtil.getBuiltinTypeId(valueClass);

            if (typeId == -1) {
                LoggerFactory.getLogger(getClass())
                    .warn("Not a built-in type: {}", valueClass);
            }

            if (value.getClass().isArray()) {
                int[] dimensions = ArrayUtil.getDimensions(value);

                if (dimensions.length == 1) {
                    buffer.writeByte(typeId | 0x80);

                    int length = Array.getLength(value);
                    buffer.writeIntLE(length);

                    for (int i = 0; i < length; i++) {
                        Object o = Array.get(value, i);

                        writeValue(o, typeId, structure, enumeration);
                    }
                } else {
                    buffer.writeByte(typeId | 0xC0);

                    Object flattened = ArrayUtil.flatten(value);
                    int length = Array.getLength(flattened);
                    buffer.writeIntLE(length);

                    for (int i = 0; i < length; i++) {
                        Object o = Array.get(flattened, i);

                        writeValue(o, typeId, structure, enumeration);
                    }

                    writeInt32(dimensions.length);
                    for (int dimension : dimensions) {
                        writeInt32(dimension);
                    }
                }
            } else {
                buffer.writeByte(typeId);

                writeValue(value, typeId, structure, enumeration);
            }
        }
    }

    // endregion

    private void writeValue(Object value, int typeId, boolean structure, boolean enumeration) {
        if (structure) {
            UaStructure struct = (UaStructure) value;

            ExtensionObject extensionObject = ExtensionObject.encode(context, struct);

            writeBuiltinType(typeId, extensionObject);
        } else if (enumeration) {
            writeBuiltinType(typeId, ((UaEnumeration) value).getValue());
        } else {
            writeBuiltinType(typeId, value);
        }
    }

    private void writeBuiltinType(int typeId, Object value) throws UaSerializationException {
        switch (typeId) {
            case 1:
                writeBoolean(null, (Boolean) value);
                break;
            case 2:
                writeSByte((Byte) value);
                break;
            case 3:
                writeByte((UByte) value);
                break;
            case 4:
                writeInt16((Short) value);
                break;
            case 5:
                writeUInt16((UShort) value);
                break;
            case 6:
                writeInt32((Integer) value);
                break;
            case 7:
                writeUInt32((UInteger) value);
                break;
            case 8:
                writeInt64((Long) value);
                break;
            case 9:
                writeUInt64((ULong) value);
                break;
            case 10:
                writeFloat((Float) value);
                break;
            case 11:
                writeDouble((Double) value);
                break;
            case 12:
                writeString((String) value);
                break;
            case 13:
                writeDateTime((DateTime) value);
                break;
            case 14:
                writeGuid((UUID) value);
                break;
            case 15:
                writeByteString((ByteString) value);
                break;
            case 16:
                writeXmlElement((XmlElement) value);
                break;
            case 17:
                writeNodeId((NodeId) value);
                break;
            case 18:
                writeExpandedNodeId((ExpandedNodeId) value);
                break;
            case 19:
                writeStatusCode((StatusCode) value);
                break;
            case 20:
                writeQualifiedName((QualifiedName) value);
                break;
            case 21:
                writeLocalizedText((LocalizedText) value);
                break;
            case 22:
                writeExtensionObject((ExtensionObject) value);
                break;
            case 23:
                writeDataValue((DataValue) value);
                break;
            case 24:
                writeVariant((Variant) value);
                break;
            case 25:
                writeDiagnosticInfo((DiagnosticInfo) value);
                break;
            default:
                throw new UaSerializationException(
                    StatusCodes.Bad_EncodingError,
                    "unknown builtin type: " + typeId);
        }
    }

    private Class<?> getClass(@NotNull Object o) {
        if (o.getClass().isArray()) {
            return ArrayUtil.getType(o);
        } else {
            return o.getClass();
        }
    }

    private void writeLengthPrefixedString(String value, Charset charset) throws UaSerializationException {
        byte[] bytes = value.getBytes(charset);
        int length = bytes.length;

        if (length > context.getEncodingLimits().getMaxMessageSize()) {
            throw new UaSerializationException(
                StatusCodes.Bad_EncodingLimitsExceeded,
                String.format(
                    "string length exceeds max message size (length=%s, max=%s)",
                    length, context.getEncodingLimits().getMaxMessageSize())
            );
        }

        writeInt32(length);
        buffer.writeBytes(bytes);
    }

    @Override
    public void writeBoolean(String field, Boolean value) throws UaSerializationException {
        writeBoolean(value);
    }

    @Override
    public void writeSByte(String field, Byte value) throws UaSerializationException {
        writeSByte(value);
    }

    @Override
    public void writeInt16(String field, Short value) throws UaSerializationException {
        writeInt16(value);
    }

    @Override
    public void writeInt32(String field, Integer value) throws UaSerializationException {
        writeInt32(value);
    }

    @Override
    public void writeInt64(String field, Long value) throws UaSerializationException {
        writeInt64(value);
    }

    @Override
    public void writeByte(String field, UByte value) throws UaSerializationException {
        writeByte(value);
    }

    @Override
    public void writeUInt16(String field, UShort value) throws UaSerializationException {
        writeUInt16(value);
    }

    @Override
    public void writeUInt32(String field, UInteger value) throws UaSerializationException {
        writeUInt32(value);
    }

    @Override
    public void writeUInt64(String field, ULong value) throws UaSerializationException {
        writeUInt64(value);
    }

    @Override
    public void writeFloat(String field, Float value) throws UaSerializationException {
        writeFloat(value);
    }

    @Override
    public void writeDouble(String field, Double value) throws UaSerializationException {
        writeDouble(value);
    }

    @Override
    public void writeString(String field, String value) throws UaSerializationException {
        writeString(value);
    }

    @Override
    public void writeDateTime(String field, DateTime value) throws UaSerializationException {
        writeDateTime(value);
    }

    @Override
    public void writeGuid(String field, UUID value) throws UaSerializationException {
        writeGuid(value);
    }

    @Override
    public void writeByteString(String field, ByteString value) throws UaSerializationException {
        writeByteString(value);
    }

    @Override
    public void writeXmlElement(String field, XmlElement value) throws UaSerializationException {
        writeXmlElement(value);
    }

    @Override
    public void writeNodeId(String field, NodeId value) throws UaSerializationException {
        writeNodeId(value);
    }

    @Override
    public void writeExpandedNodeId(String field, ExpandedNodeId value) throws UaSerializationException {
        writeExpandedNodeId(value);
    }

    @Override
    public void writeStatusCode(String field, StatusCode value) throws UaSerializationException {
        writeStatusCode(value);
    }

    @Override
    public void writeQualifiedName(String field, QualifiedName value) throws UaSerializationException {
        writeQualifiedName(value);
    }

    @Override
    public void writeLocalizedText(String field, LocalizedText value) throws UaSerializationException {
        writeLocalizedText(value);
    }

    @Override
    public void writeExtensionObject(String field, ExtensionObject value) throws UaSerializationException {
        writeExtensionObject(value);
    }

    @Override
    public void writeDataValue(String field, DataValue value) throws UaSerializationException {
        writeDataValue(value);
    }

    @Override
    public void writeVariant(String field, Variant value) throws UaSerializationException {
        writeVariant(value);
    }

    @Override
    public void writeDiagnosticInfo(String field, DiagnosticInfo value) throws UaSerializationException {
        writeDiagnosticInfo(value);
    }

    @Override
    public void writeMessage(String field, UaMessage message) throws UaSerializationException {
        ExpandedNodeId xBinaryEncodingId = message.getBinaryEncodingId();

        NodeId encodingId = xBinaryEncodingId.toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + xBinaryEncodingId.getNamespaceUri())
            );

        @SuppressWarnings("unchecked")
        OpcUaBinaryDataTypeCodec<UaMessage> binaryCodec = (OpcUaBinaryDataTypeCodec<UaMessage>)
            context.getDataTypeManager()
                .getCodec(encodingId);

        if (binaryCodec == null) {
            throw new UaSerializationException(
                StatusCodes.Bad_DecodingError,
                "no codec registered: " + encodingId
            );
        }

        writeNodeId(encodingId);

        binaryCodec.encode(context, this, message);
    }

    @Override
    public void writeEnum(String field, UaEnumeration value) {
        writeInt32(value.getValue());
    }

    @Override
    public void writeStruct(String field, Object value, NodeId dataTypeId) throws UaSerializationException {
        try {
            @SuppressWarnings("unchecked")
            OpcUaBinaryDataTypeCodec<Object> codec = (OpcUaBinaryDataTypeCodec<Object>)
                context.getDataTypeManager()
                    .getCodec(OpcUaDefaultBinaryEncoding.ENCODING_NAME, dataTypeId);

            if (codec == null) {
                throw new UaSerializationException(
                    StatusCodes.Bad_EncodingError,
                    "no codec registered: " + dataTypeId
                );
            }

            codec.encode(context, this, value);
        } catch (ClassCastException e) {
            throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
        }
    }

    @Override
    public void writeStruct(String field, Object value, ExpandedNodeId dataTypeId) throws UaSerializationException {
        NodeId localDateTypeId = dataTypeId.toNodeId(context.getNamespaceTable())
            .orElseThrow(() -> new UaSerializationException(
                StatusCodes.Bad_EncodingError,
                "no codec registered: " + dataTypeId
            ));

        writeStruct(field, value, localDateTypeId);
    }

    @Override
    public void writeStruct(String field, Object value, DataTypeCodec codec) throws UaSerializationException {
        if (codec instanceof OpcUaBinaryDataTypeCodec) {
            @SuppressWarnings("unchecked")
            OpcUaBinaryDataTypeCodec<Object> binaryCodec = (OpcUaBinaryDataTypeCodec<Object>) codec;

            binaryCodec.encode(context, this, value);
        } else {
            throw new UaSerializationException(
                StatusCodes.Bad_EncodingError,
                new IllegalArgumentException("codec: " + codec)
            );
        }
    }

    @Override
    public void writeBooleanArray(String field, Boolean[] value) throws UaSerializationException {
        writeArray(field, value, this::writeBoolean);
    }

    @Override
    public void writeSByteArray(String field, Byte[] value) throws UaSerializationException {
        writeArray(field, value, this::writeSByte);
    }

    @Override
    public void writeInt16Array(String field, Short[] value) throws UaSerializationException {
        writeArray(field, value, this::writeInt16);
    }

    @Override
    public void writeInt32Array(String field, Integer[] value) throws UaSerializationException {
        writeArray(field, value, this::writeInt32);
    }

    @Override
    public void writeInt64Array(String field, Long[] value) throws UaSerializationException {
        writeArray(field, value, this::writeInt64);
    }

    @Override
    public void writeByteArray(String field, UByte[] value) throws UaSerializationException {
        writeArray(field, value, this::writeByte);
    }

    @Override
    public void writeUInt16Array(String field, UShort[] value) throws UaSerializationException {
        writeArray(field, value, this::writeUInt16);
    }

    @Override
    public void writeUInt32Array(String field, UInteger[] value) throws UaSerializationException {
        writeArray(field, value, this::writeUInt32);
    }

    @Override
    public void writeUInt64Array(String field, ULong[] value) throws UaSerializationException {
        writeArray(field, value, this::writeUInt64);
    }

    @Override
    public void writeFloatArray(String field, Float[] value) throws UaSerializationException {
        writeArray(field, value, this::writeFloat);
    }

    @Override
    public void writeDoubleArray(String field, Double[] value) throws UaSerializationException {
        writeArray(field, value, this::writeDouble);
    }

    @Override
    public void writeStringArray(String field, String[] value) throws UaSerializationException {
        writeArray(field, value, this::writeString);
    }

    @Override
    public void writeDateTimeArray(String field, DateTime[] value) throws UaSerializationException {
        writeArray(field, value, this::writeDateTime);
    }

    @Override
    public void writeGuidArray(String field, UUID[] value) throws UaSerializationException {
        writeArray(field, value, this::writeGuid);
    }

    @Override
    public void writeByteStringArray(String field, ByteString[] value) throws UaSerializationException {
        writeArray(field, value, this::writeByteString);
    }

    @Override
    public void writeXmlElementArray(String field, XmlElement[] value) throws UaSerializationException {
        writeArray(field, value, this::writeXmlElement);
    }

    @Override
    public void writeNodeIdArray(String field, NodeId[] value) throws UaSerializationException {
        writeArray(field, value, this::writeNodeId);
    }

    @Override
    public void writeExpandedNodeIdArray(String field, ExpandedNodeId[] value) throws UaSerializationException {
        writeArray(field, value, this::writeExpandedNodeId);
    }

    @Override
    public void writeStatusCodeArray(String field, StatusCode[] value) throws UaSerializationException {
        writeArray(field, value, this::writeStatusCode);
    }

    @Override
    public void writeQualifiedNameArray(String field, QualifiedName[] value) throws UaSerializationException {
        writeArray(field, value, this::writeQualifiedName);
    }

    @Override
    public void writeLocalizedTextArray(String field, LocalizedText[] value) throws UaSerializationException {
        writeArray(field, value, this::writeLocalizedText);
    }

    @Override
    public void writeExtensionObjectArray(String field, ExtensionObject[] value) throws UaSerializationException {
        writeArray(field, value, this::writeExtensionObject);
    }

    @Override
    public void writeDataValueArray(String field, DataValue[] value) throws UaSerializationException {
        writeArray(field, value, this::writeDataValue);
    }

    @Override
    public void writeVariantArray(String field, Variant[] value) throws UaSerializationException {
        writeArray(field, value, this::writeVariant);
    }

    @Override
    public void writeDiagnosticInfoArray(String field, DiagnosticInfo[] value) throws UaSerializationException {
        writeArray(field, value, this::writeDiagnosticInfo);
    }

    @Override
    public void writeEnumArray(String field, UaEnumeration[] value) throws UaSerializationException {
        writeArray(value, v -> writeEnum(field, v));
    }

    @Override
    public void writeStructArray(
        String field, Object[] values, NodeId dataTypeId) throws UaSerializationException {

        writeArray(values, o -> writeStruct(field, o, dataTypeId));
    }

    @Override
    public void writeStructArray(
        String field,
        Object[] value,
        ExpandedNodeId dataTypeId
    ) throws UaSerializationException {

        NodeId localDateTypeId = dataTypeId.toNodeId(context.getNamespaceTable())
            .orElseThrow(() -> new UaSerializationException(
                StatusCodes.Bad_EncodingError,
                "no codec registered: " + dataTypeId
            ));

        writeStructArray(field, value, localDateTypeId);
    }

    @Override
    public <T> void writeArray(
        String field, T[] values, BiConsumer<String, T> encoder) throws UaSerializationException {

        if (values == null) {
            buffer.writeIntLE(-1);
        } else {
            if (values.length > context.getEncodingLimits().getMaxMessageSize()) {
                throw new UaSerializationException(
                    StatusCodes.Bad_EncodingLimitsExceeded,
                    "array length exceeds max message size"
                );
            }

            writeInt32(values.length);

            for (T t : values) {
                encoder.accept(field, t);
            }
        }
    }

}
