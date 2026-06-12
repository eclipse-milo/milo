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
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;

/**
 * Encodes UADP discovery NetworkMessages (OPC UA Part 14 §7.2.4.6): DataSetMetaData probes and
 * DataSetMetaData announcements, security mode None only.
 *
 * <p>Discovery NetworkMessages pin almost every header flag (§7.2.4.6.3, §7.2.4.6.12.1): the
 * PublisherId, ExtendedFlags1, a SecurityHeader, and ExtendedFlags2 are always present; the
 * GroupHeader, PayloadHeader, DataSetClassId, Timestamp, PicoSeconds, and PromotedFields never are.
 * Note the SecurityHeader is mandatory even with security mode None (6 bytes: SecurityFlags 0,
 * SecurityTokenId 0, NonceLength 0) — unlike the data-plane encoder, which never writes one.
 *
 * <p>Chunked emission is not supported; enforcing DiscoveryMaxMessageSize against the encoded
 * result is the caller's responsibility.
 *
 * <p>Stateless: the per-PublisherId announcement sequence number is supplied by the caller via
 * {@link UadpMetaDataAnnouncement#sequenceNumber()}.
 */
final class UadpDiscoveryMessageEncoder {

  private static final int UADP_VERSION = 1;

  /** Byte 0 of every discovery NetworkMessage: version 1, PublisherId, ExtendedFlags1. */
  private static final int DISCOVERY_BYTE0 = UADP_VERSION | 0x10 | 0x80;

  /** ExtendedFlags1 base of every discovery NetworkMessage: SecurityHeader, ExtendedFlags2. */
  private static final int DISCOVERY_EXTENDED_FLAGS_1 = 0x10 | 0x80;

  /** ExtendedFlags2 of a discovery probe: NetworkMessage type 001 at bits 2-4. */
  private static final int EXTENDED_FLAGS_2_PROBE = 0x04;

  /** ExtendedFlags2 of a discovery announcement: NetworkMessage type 010 at bits 2-4. */
  private static final int EXTENDED_FLAGS_2_ANNOUNCEMENT = 0x08;

  /** Discovery announcement type DataSetMetaData (Part 14 §7.2.4.6.3, Table 168). */
  private static final int ANNOUNCEMENT_DATA_SET_META_DATA = 2;

  private UadpDiscoveryMessageEncoder() {}

  /**
   * Encode one discovery probe NetworkMessage.
   *
   * @param encodingContext the {@link EncodingContext}.
   * @param probe the probe to encode; the header PublisherId is {@link
   *     UadpDiscoveryProbe#targetPublisherId()}.
   * @return the encoded NetworkMessage; the caller assumes ownership of its buffer.
   * @throws UaException with {@code Bad_NotSupported} if the probe is not a DataSetMetaData probe
   *     (ProbeType 1, InformationType 2), the only probe kind in scope.
   */
  static EncodedNetworkMessage encodeProbe(
      EncodingContext encodingContext, UadpDiscoveryProbe probe) throws UaException {

    if (probe.probeType() != UadpDiscoveryProbe.PROBE_TYPE_PUBLISHER_INFORMATION
        || probe.informationType() != UadpDiscoveryProbe.INFORMATION_TYPE_DATA_SET_META_DATA) {

      throw new UaException(
          StatusCodes.Bad_NotSupported,
          "only DataSetMetaData probes are supported: probeType="
              + probe.probeType()
              + ", informationType="
              + probe.informationType());
    }

    ByteBuf buffer = BufferUtil.pooledBuffer();
    boolean success = false;

    try {
      OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(encodingContext).setBuffer(buffer);

      encodeDiscoveryHeader(encoder, buffer, EXTENDED_FLAGS_2_PROBE, probe.targetPublisherId());

      // Probe header (Table 178) and Publisher information probe body (Table 179).
      buffer.writeByte(probe.probeType());
      buffer.writeByte(probe.informationType());

      // DataSetWriter settings (Table 180): standard OPC UA Binary array of UInt16.
      encoder.encodeInt32(probe.dataSetWriterIds().size());
      for (UShort dataSetWriterId : probe.dataSetWriterIds()) {
        encoder.encodeUInt16(dataSetWriterId);
      }

      success = true;

      return new EncodedNetworkMessage(buffer);
    } catch (UaSerializationException e) {
      throw new UaException(e.getStatusCode().value(), e.getMessage(), e);
    } finally {
      if (!success) {
        buffer.release();
      }
    }
  }

  /**
   * Encode one DataSetMetaData discovery announcement NetworkMessage.
   *
   * @param encodingContext the {@link EncodingContext} used to encode the metadata structure.
   * @param announcement the announcement to encode; the header PublisherId is {@link
   *     UadpMetaDataAnnouncement#publisherId()}.
   * @return the encoded NetworkMessage; the caller assumes ownership of its buffer.
   * @throws UaException if the metadata structure could not be encoded.
   */
  static EncodedNetworkMessage encodeAnnouncement(
      EncodingContext encodingContext, UadpMetaDataAnnouncement announcement) throws UaException {

    ByteBuf buffer = BufferUtil.pooledBuffer();
    boolean success = false;

    try {
      OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(encodingContext).setBuffer(buffer);

      encodeDiscoveryHeader(
          encoder, buffer, EXTENDED_FLAGS_2_ANNOUNCEMENT, announcement.publisherId());

      // Announcement header (Table 168).
      buffer.writeByte(ANNOUNCEMENT_DATA_SET_META_DATA);
      encoder.encodeUInt16(announcement.sequenceNumber());

      // DataSetMetaData announcement body (Table 170): the metadata structure is encoded
      // inline, with no ExtensionObject wrapper (§6.2.3.2.3).
      encoder.encodeUInt16(announcement.dataSetWriterId());
      encoder.encodeStruct("MetaData", announcement.metaData(), DataSetMetaDataType.TYPE_ID);
      encoder.encodeStatusCode(announcement.statusCode());

      success = true;

      return new EncodedNetworkMessage(buffer);
    } catch (UaSerializationException e) {
      throw new UaException(e.getStatusCode().value(), e.getMessage(), e);
    } finally {
      if (!success) {
        buffer.release();
      }
    }
  }

  /**
   * Encode the fixed discovery NetworkMessage header: the three flag bytes, the PublisherId, and
   * the mode None SecurityHeader.
   */
  private static void encodeDiscoveryHeader(
      OpcUaBinaryEncoder encoder, ByteBuf buffer, int extendedFlags2, PublisherId publisherId) {

    buffer.writeByte(DISCOVERY_BYTE0);
    buffer.writeByte(
        DISCOVERY_EXTENDED_FLAGS_1 | UadpNetworkMessageEncoder.publisherIdTypeBits(publisherId));
    buffer.writeByte(extendedFlags2);

    UadpNetworkMessageEncoder.encodePublisherId(encoder, publisherId);

    // SecurityHeader, mandatory for discovery messages even with security mode None
    // (§7.2.4.6.3, §7.2.4.6.12.1): SecurityFlags 0, SecurityTokenId 0, NonceLength 0.
    buffer.writeByte(0x00);
    encoder.encodeUInt32(uint(0));
    buffer.writeByte(0x00);
  }
}
