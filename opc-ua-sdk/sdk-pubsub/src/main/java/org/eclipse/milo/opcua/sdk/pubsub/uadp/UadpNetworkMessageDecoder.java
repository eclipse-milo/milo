/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes one UADP NetworkMessage (OPC UA Part 14 §7.2.4) into a {@link DecodedNetworkMessage}.
 *
 * <p>Decoding is tolerant by design: malformed or unsupported input never raises an exception to
 * the caller. Whatever was decoded before the problem is returned, and skipped DataSetMessages are
 * represented as entries with {@code valid == false} and no fields. A NetworkMessage whose header
 * cannot be decoded at all yields an empty {@link DecodedNetworkMessage}.
 *
 * <p>Scope and limitations:
 *
 * <ul>
 *   <li>Data Key Frame, Data Delta Frame, Event, and Keep Alive DataSetMessages with Variant or
 *       DataValue field encoding are decoded.
 *   <li>RawData field encoding requires metadata-driven offsets and is not supported; affected
 *       DataSetMessages are skipped ({@code valid == false}, empty fields).
 *   <li>Discovery announcements of type DataSetMetaData and DataSetMetaData probes (ProbeType 1,
 *       InformationType 2) are decoded; {@link #decodeMessage(DecodeContext, ByteBuf)} surfaces
 *       them as {@link UadpMetaDataAnnouncement} (any status, including Bad denials) and {@link
 *       UadpDiscoveryProbe}. The legacy {@link #decode(DecodeContext, ByteBuf)} surface folds
 *       non-Bad announcements into {@link DecodedMetaData} and drops probes and Bad-status
 *       announcements. FindApplications probes, other probe InformationTypes, and other
 *       announcement types are tolerated and skipped.
 *   <li>Only security mode None is supported: a SecurityHeader with any SecurityFlags bit set
 *       causes the payload to be skipped.
 *   <li>Chunked NetworkMessages and ActionHeaders are detected and their payloads skipped. A
 *       PromotedFields block is skipped via its Size field and the payload after it is decoded
 *       normally.
 *   <li>If the PayloadHeader is absent the payload is assumed to contain a single DataSetMessage.
 * </ul>
 *
 * <p>Stateless: a new instance is used for each NetworkMessage.
 */
final class UadpNetworkMessageDecoder {

  private static final Logger LOGGER = LoggerFactory.getLogger(UadpNetworkMessageDecoder.class);

  /** UADP NetworkMessage type values, from ExtendedFlags2 bits 2-4. */
  private static final int TYPE_DATA = 0;

  private static final int TYPE_DISCOVERY_PROBE = 1;
  private static final int TYPE_DISCOVERY_ANNOUNCEMENT = 2;

  /** Discovery announcement type values (Part 14 §7.2.4.6.3, Table 168). */
  private static final int ANNOUNCEMENT_DATA_SET_META_DATA = 2;

  /** Field Encoding values, from DataSetFlags1 bits 1-2. */
  private static final int FIELD_ENCODING_RAW_DATA = 1;

  private static final int FIELD_ENCODING_DATA_VALUE = 2;
  private static final int FIELD_ENCODING_RESERVED = 3;

  private final List<DecodedDataSetMessage> messages = new ArrayList<>();
  private final List<DecodedMetaData> metaData = new ArrayList<>();

  private @Nullable UadpDiscoveryProbe probe;
  private @Nullable UadpMetaDataAnnouncement announcement;

  private @Nullable PublisherId publisherId;
  private @Nullable UShort writerGroupId;
  private @Nullable UInteger groupVersion;
  private @Nullable UShort networkMessageNumber;
  private @Nullable UShort sequenceNumber;
  private @Nullable DateTime timestamp;

  private final EncodingContext encodingContext;
  private final ByteBuf buffer;
  private final OpcUaBinaryDecoder decoder;

  private UadpNetworkMessageDecoder(EncodingContext encodingContext, ByteBuf buffer) {
    this.encodingContext = encodingContext;
    this.buffer = buffer;

    decoder = new OpcUaBinaryDecoder(encodingContext).setBuffer(buffer);
  }

  /**
   * Decode one NetworkMessage from {@code buffer} into the legacy data-plane shape.
   *
   * <p>Discovery content is folded into the data-plane result the way it always was: a
   * DataSetMetaData announcement with a non-Bad status becomes a {@link DecodedMetaData} entry,
   * while probes and Bad-status announcements yield a header-only result. Use {@link
   * #decodeMessage(DecodeContext, ByteBuf)} to surface them.
   *
   * @param context the decode context.
   * @param buffer the buffer containing the received NetworkMessage; the caller retains ownership.
   * @return the decoded NetworkMessage; possibly partial or empty if the input was malformed or
   *     unsupported.
   */
  static DecodedNetworkMessage decode(DecodeContext context, ByteBuf buffer) {
    var decoder = new UadpNetworkMessageDecoder(context.encodingContext(), buffer);

    try {
      decoder.decodeNetworkMessage();
    } catch (Exception e) {
      LOGGER.debug("failed to fully decode NetworkMessage: {}", e.getMessage(), e);
    }

    return decoder.legacyResult();
  }

  /**
   * Decode one NetworkMessage from {@code buffer}, surfacing discovery messages.
   *
   * @param context the decode context.
   * @param buffer the buffer containing the received NetworkMessage; the caller retains ownership.
   * @return a {@link UadpDiscoveryProbe} for a DataSetMetaData probe, a {@link
   *     UadpMetaDataAnnouncement} for a DataSetMetaData announcement of any status, or a {@link
   *     DecodedNetworkMessage} otherwise — possibly partial or empty if the input was malformed,
   *     unsupported, or discovery content that is tolerated but not surfaced.
   */
  static UadpDecodedMessage decodeMessage(DecodeContext context, ByteBuf buffer) {
    var decoder = new UadpNetworkMessageDecoder(context.encodingContext(), buffer);

    try {
      decoder.decodeNetworkMessage();
    } catch (Exception e) {
      LOGGER.debug("failed to fully decode NetworkMessage: {}", e.getMessage(), e);
    }

    return decoder.result();
  }

  private UadpDecodedMessage result() {
    if (probe != null) {
      return probe;
    }
    if (announcement != null) {
      return announcement;
    }
    return dataPlaneResult();
  }

  private DecodedNetworkMessage legacyResult() {
    if (announcement != null && !announcement.statusCode().isBad()) {
      metaData.add(new DecodedMetaData(announcement.dataSetWriterId(), announcement.metaData()));
    } else if (announcement != null) {
      // The Publisher cannot provide metadata for this DataSetWriter; the legacy surface has
      // no slot for denials.
      LOGGER.debug(
          "DataSetMetaData announcement with Bad status: {} (dataSetWriterId={})",
          announcement.statusCode(),
          announcement.dataSetWriterId());
    }

    return dataPlaneResult();
  }

  private DecodedNetworkMessage dataPlaneResult() {
    return DecodedNetworkMessage.of(
        publisherId,
        writerGroupId,
        groupVersion,
        networkMessageNumber,
        sequenceNumber,
        timestamp,
        messages,
        metaData);
  }

  private void decodeNetworkMessage() {
    int byte0 = buffer.readUnsignedByte();

    int version = byte0 & 0x0F;
    if (version != 1) {
      LOGGER.debug("unsupported UADP version: {}", version);
      return;
    }

    boolean publisherIdEnabled = (byte0 & 0x10) != 0;
    boolean groupHeaderEnabled = (byte0 & 0x20) != 0;
    boolean payloadHeaderEnabled = (byte0 & 0x40) != 0;

    int extendedFlags1 = (byte0 & 0x80) != 0 ? buffer.readUnsignedByte() : 0;
    int extendedFlags2 = (extendedFlags1 & 0x80) != 0 ? buffer.readUnsignedByte() : 0;

    int publisherIdType = extendedFlags1 & 0x07;
    boolean dataSetClassIdEnabled = (extendedFlags1 & 0x08) != 0;
    boolean securityEnabled = (extendedFlags1 & 0x10) != 0;
    boolean timestampEnabled = (extendedFlags1 & 0x20) != 0;
    boolean picoSecondsEnabled = (extendedFlags1 & 0x40) != 0;

    boolean chunk = (extendedFlags2 & 0x01) != 0;
    boolean promotedFieldsEnabled = (extendedFlags2 & 0x02) != 0;
    int messageType = (extendedFlags2 >> 2) & 0x07;
    boolean actionHeaderEnabled = (extendedFlags2 & 0x20) != 0;

    if ((extendedFlags2 & 0xC0) != 0) {
      LOGGER.debug("reserved ExtendedFlags2 bits set: 0x{}", Integer.toHexString(extendedFlags2));
      return;
    }
    if (messageType > TYPE_DISCOVERY_ANNOUNCEMENT) {
      LOGGER.debug("reserved NetworkMessage type: {}", messageType);
      return;
    }

    if (publisherIdEnabled) {
      publisherId = decodePublisherId(publisherIdType);
      if (publisherId == null) {
        return;
      }
    }

    if (dataSetClassIdEnabled) {
      // Consume; the DataSetClassId is not surfaced by DecodedNetworkMessage.
      decoder.decodeGuid();
    }

    if (groupHeaderEnabled) {
      int groupFlags = buffer.readUnsignedByte();

      if ((groupFlags & 0xF0) != 0) {
        LOGGER.debug("reserved GroupFlags bits set: 0x{}", Integer.toHexString(groupFlags));
        return;
      }

      if ((groupFlags & 0x01) != 0) {
        writerGroupId = decoder.decodeUInt16();
      }
      if ((groupFlags & 0x02) != 0) {
        groupVersion = decoder.decodeUInt32();
      }
      if ((groupFlags & 0x04) != 0) {
        networkMessageNumber = decoder.decodeUInt16();
      }
      if ((groupFlags & 0x08) != 0) {
        sequenceNumber = decoder.decodeUInt16();
      }
    }

    List<UShort> dataSetWriterIds = null;

    if (payloadHeaderEnabled) {
      if (chunk) {
        // Chunked NetworkMessage payload header: a single DataSetWriterId, no Count.
        decoder.decodeUInt16();
      } else if (messageType == TYPE_DATA) {
        int count = buffer.readUnsignedByte();

        dataSetWriterIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
          dataSetWriterIds.add(decoder.decodeUInt16());
        }
      } else {
        // Discovery messages shall not have a PayloadHeader; the layout that follows is
        // undefined.
        LOGGER.debug("discovery NetworkMessage with PayloadHeader enabled");
        return;
      }
    }

    if (timestampEnabled) {
      timestamp = decoder.decodeDateTime();
    }
    if (picoSecondsEnabled) {
      // Consume; PicoSeconds are not surfaced by DecodedNetworkMessage.
      decoder.decodeUInt16();
    }

    if (promotedFieldsEnabled) {
      // Skip the PromotedFields block via its Size field; the payload that follows is still
      // decodable.
      int size = decoder.decodeUInt16().intValue();
      if (size > buffer.readableBytes()) {
        LOGGER.debug("PromotedFields size exceeds remaining bytes: {}", size);
        return;
      }
      buffer.skipBytes(size);
    }

    if (securityEnabled && !decodeSecurityHeader()) {
      return;
    }

    if (actionHeaderEnabled) {
      LOGGER.debug("ActionHeader is not supported; skipping payload");
      return;
    }

    if (chunk) {
      LOGGER.debug("chunked NetworkMessage is not supported; skipping payload");
      return;
    }

    if (messageType == TYPE_DISCOVERY_ANNOUNCEMENT) {
      decodeDiscoveryAnnouncement();
    } else if (messageType == TYPE_DISCOVERY_PROBE) {
      decodeDiscoveryProbe();
    } else {
      decodeDataPayload(payloadHeaderEnabled, dataSetWriterIds);
    }
  }

  private @Nullable PublisherId decodePublisherId(int publisherIdType) {
    switch (publisherIdType) {
      case 0x00:
        return PublisherId.ubyte(decoder.decodeByte());
      case 0x01:
        return PublisherId.uint16(decoder.decodeUInt16());
      case 0x02:
        return PublisherId.uint32(decoder.decodeUInt32());
      case 0x03:
        return PublisherId.uint64(decoder.decodeUInt64());
      case 0x04:
        String value = decoder.decodeString();
        if (value == null) {
          LOGGER.debug("null String PublisherId");
          return null;
        }
        return PublisherId.string(value);
      default:
        LOGGER.debug("reserved PublisherId type: {}", publisherIdType);
        return null;
    }
  }

  /**
   * Decode the SecurityHeader.
   *
   * @return {@code true} if the payload that follows can be processed, i.e. the SecurityFlags
   *     indicate security mode None.
   */
  private boolean decodeSecurityHeader() {
    int securityFlags = buffer.readUnsignedByte();

    if (securityFlags != 0) {
      // Signed and/or encrypted payloads, SecurityFooter, key reset, and reserved bits are
      // all unsupported; only security mode None is processed.
      LOGGER.debug(
          "unsupported SecurityFlags: 0x{}; skipping payload", Integer.toHexString(securityFlags));
      return false;
    }

    // SecurityTokenId
    decoder.decodeUInt32();

    int nonceLength = buffer.readUnsignedByte();
    if (nonceLength > buffer.readableBytes()) {
      LOGGER.debug("NonceLength exceeds remaining bytes: {}", nonceLength);
      return false;
    }
    buffer.skipBytes(nonceLength);

    // SecurityFooterSize is only present when the SecurityFooter flag is set, which was
    // rejected above.

    return true;
  }

  private void decodeDiscoveryAnnouncement() {
    int announcementType = buffer.readUnsignedByte();

    // Per-PublisherId announcement SequenceNumber, independent of the data-plane counters.
    UShort announcementSequenceNumber = decoder.decodeUInt16();

    if (announcementType != ANNOUNCEMENT_DATA_SET_META_DATA) {
      LOGGER.debug("unsupported discovery announcement type: {}", announcementType);
      return;
    }

    UShort dataSetWriterId = decoder.decodeUInt16();

    var dataSetMetaData =
        (DataSetMetaDataType) decoder.decodeStruct("MetaData", DataSetMetaDataType.TYPE_ID);

    StatusCode status = decoder.decodeStatusCode();

    if (publisherId == null) {
      // Discovery messages shall carry a PublisherId (§7.2.4.6.3); without one the
      // announcement cannot be correlated with a Publisher.
      LOGGER.debug("DataSetMetaData announcement without PublisherId");
      return;
    }

    // Surfaced regardless of status; a Bad status is a denial the subscriber needs to see.
    announcement =
        UadpMetaDataAnnouncement.of(
            publisherId, announcementSequenceNumber, dataSetWriterId, dataSetMetaData, status);
  }

  private void decodeDiscoveryProbe() {
    int probeType = buffer.readUnsignedByte();

    if (probeType != UadpDiscoveryProbe.PROBE_TYPE_PUBLISHER_INFORMATION) {
      // FindApplications probes (2) and reserved values are tolerated and ignored.
      LOGGER.debug("unsupported discovery probe type: {}", probeType);
      return;
    }

    int informationType = buffer.readUnsignedByte();

    if (informationType != UadpDiscoveryProbe.INFORMATION_TYPE_DATA_SET_META_DATA) {
      // Publisher endpoints and writer/group/connection configuration probes are tolerated
      // and ignored.
      LOGGER.debug("unsupported discovery probe InformationType: {}", informationType);
      return;
    }

    // DataSetWriter settings (Table 180): standard OPC UA Binary array of UInt16; a null
    // (-1) or empty array surfaces as an empty list.
    int count = decoder.decodeInt32();
    if (count > buffer.readableBytes() / 2) {
      LOGGER.debug("DataSetWriterIds count exceeds remaining bytes: {}", count);
      return;
    }

    List<UShort> dataSetWriterIds = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      dataSetWriterIds.add(decoder.decodeUInt16());
    }

    if (publisherId == null) {
      // The header PublisherId identifies the probed Publisher (§7.2.4.6.12.1); without one
      // the probe cannot be answered.
      LOGGER.debug("discovery probe without PublisherId");
      return;
    }

    probe = UadpDiscoveryProbe.of(publisherId, dataSetWriterIds);
  }

  private void decodeDataPayload(
      boolean payloadHeaderEnabled, @Nullable List<UShort> dataSetWriterIds) {

    // Without a PayloadHeader the number and sizes of DataSetMessages can only come from
    // reader configuration; assume a single DataSetMessage spanning the rest of the buffer.
    int count = dataSetWriterIds != null ? dataSetWriterIds.size() : 1;

    int[] sizes = null;
    if (payloadHeaderEnabled && count > 1) {
      sizes = new int[count];
      for (int i = 0; i < count; i++) {
        sizes[i] = decoder.decodeUInt16().intValue();
      }
    }

    for (int i = 0; i < count; i++) {
      UShort dataSetWriterId = dataSetWriterIds != null ? dataSetWriterIds.get(i) : null;

      if (sizes != null) {
        int size = sizes[i];
        if (size > buffer.readableBytes()) {
          LOGGER.debug("DataSetMessage size exceeds remaining bytes: {}", size);
          return;
        }

        ByteBuf slice = buffer.readSlice(size);
        OpcUaBinaryDecoder sliceDecoder = new OpcUaBinaryDecoder(encodingContext).setBuffer(slice);

        try {
          messages.add(decodeDataSetMessage(sliceDecoder, slice, dataSetWriterId));
        } catch (Exception e) {
          LOGGER.debug("failed to decode DataSetMessage {}: {}", i, e.getMessage(), e);

          messages.add(invalidDataSetMessage(dataSetWriterId));
        }
      } else {
        // Not size-bounded; an exception propagates and ends decoding of this
        // NetworkMessage with whatever was decoded so far.
        messages.add(decodeDataSetMessage(decoder, buffer, dataSetWriterId));
      }
    }
  }

  private static DecodedDataSetMessage decodeDataSetMessage(
      OpcUaBinaryDecoder decoder, ByteBuf buffer, @Nullable UShort dataSetWriterId) {

    int flags1 = buffer.readUnsignedByte();

    boolean valid = (flags1 & 0x01) != 0;
    if (!valid) {
      // "If the bit is false the rest of this DataSetMessage shall not be processed."
      return invalidDataSetMessage(dataSetWriterId);
    }

    int fieldEncoding = (flags1 >> 1) & 0x03;

    int flags2 = (flags1 & 0x80) != 0 ? buffer.readUnsignedByte() : 0;
    int messageType = flags2 & 0x0F;

    DataSetMessageKind kind =
        switch (messageType) {
          case 0x00 -> DataSetMessageKind.KEY_FRAME;
          case 0x01 -> DataSetMessageKind.DELTA_FRAME;
          case 0x02 -> DataSetMessageKind.EVENT;
          case 0x03 -> DataSetMessageKind.KEEP_ALIVE;
          default -> null;
        };

    // the DecodedDataSetMessage slot is UInt32 (JSON mapping range); UADP carries UInt16 values
    UInteger sequenceNumber = (flags1 & 0x08) != 0 ? uint(decoder.decodeUInt16().intValue()) : null;
    DateTime timestamp = (flags2 & 0x10) != 0 ? decoder.decodeDateTime() : null;
    if ((flags2 & 0x20) != 0) {
      // Consume; DataSetMessage PicoSeconds are not surfaced.
      decoder.decodeUInt16();
    }

    StatusCode status = null;
    if ((flags1 & 0x10) != 0) {
      // The header Status is the high-order 16 bits of the StatusCode.
      status = new StatusCode(decoder.decodeUInt16().longValue() << 16);
    }

    UInteger majorVersion = (flags1 & 0x20) != 0 ? decoder.decodeUInt32() : null;
    UInteger minorVersion = (flags1 & 0x40) != 0 ? decoder.decodeUInt32() : null;

    ConfigurationVersionDataType configurationVersion = null;
    if (majorVersion != null || minorVersion != null) {
      configurationVersion =
          new ConfigurationVersionDataType(
              majorVersion != null ? majorVersion : uint(0),
              minorVersion != null ? minorVersion : uint(0));
    }

    if (kind == null || fieldEncoding == FIELD_ENCODING_RESERVED) {
      LOGGER.debug(
          "reserved DataSetMessage type or field encoding: type={}, fieldEncoding={}",
          messageType,
          fieldEncoding);

      return new DecodedDataSetMessage(
          dataSetWriterId,
          DataSetMessageKind.KEY_FRAME,
          false,
          sequenceNumber,
          timestamp,
          status,
          configurationVersion,
          List.of());
    }

    if (kind == DataSetMessageKind.KEEP_ALIVE) {
      return new DecodedDataSetMessage(
          dataSetWriterId,
          kind,
          true,
          sequenceNumber,
          timestamp,
          status,
          configurationVersion,
          List.of());
    }

    if (fieldEncoding == FIELD_ENCODING_RAW_DATA) {
      // RawData decoding requires metadata-driven offsets; skip this DataSetMessage.
      LOGGER.debug("RawData field encoding is not supported; skipping DataSetMessage");

      return new DecodedDataSetMessage(
          dataSetWriterId,
          kind,
          false,
          sequenceNumber,
          timestamp,
          status,
          configurationVersion,
          List.of());
    }

    List<DecodedField> fields = new ArrayList<>();

    if (kind == DataSetMessageKind.DELTA_FRAME) {
      int fieldCount = decoder.decodeUInt16().intValue();

      for (int i = 0; i < fieldCount; i++) {
        int fieldIndex = decoder.decodeUInt16().intValue();
        fields.add(new DecodedField(fieldIndex, decodeFieldValue(decoder, fieldEncoding)));
      }
    } else if (buffer.readableBytes() > 0) {
      // Key frame or event. A key frame with no body at all is a heartbeat.
      int fieldCount = decoder.decodeUInt16().intValue();

      for (int i = 0; i < fieldCount; i++) {
        fields.add(new DecodedField(i, decodeFieldValue(decoder, fieldEncoding)));
      }
    }

    return new DecodedDataSetMessage(
        dataSetWriterId,
        kind,
        true,
        sequenceNumber,
        timestamp,
        status,
        configurationVersion,
        fields);
  }

  /**
   * Decode one field value, reversing the status propagation rules of Part 14 Table 34 for the
   * Variant field encoding: a Variant containing a Bad StatusCode is the field status, and a
   * Variant containing a DataValue carries an Uncertain value and status.
   */
  private static DataValue decodeFieldValue(OpcUaBinaryDecoder decoder, int fieldEncoding) {
    if (fieldEncoding == FIELD_ENCODING_DATA_VALUE) {
      return decoder.decodeDataValue();
    } else {
      Variant variant = decoder.decodeVariant();
      Object value = variant.value();

      if (value instanceof StatusCode statusCode && statusCode.isBad()) {
        return new DataValue(Variant.NULL_VALUE, statusCode, null, null, null, null);
      } else if (value instanceof DataValue dataValue) {
        return dataValue;
      } else {
        return new DataValue(variant, StatusCode.GOOD, null, null, null, null);
      }
    }
  }

  private static DecodedDataSetMessage invalidDataSetMessage(@Nullable UShort dataSetWriterId) {
    return new DecodedDataSetMessage(
        dataSetWriterId, DataSetMessageKind.KEY_FRAME, false, null, null, null, null, List.of());
  }
}
