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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.junit.jupiter.api.Test;

/**
 * The legacy {@link UadpMessageMapping#decode} surface is unchanged by the discovery codec work:
 * probes yield a header-only result, Bad-status (denial) announcements are dropped, and Good
 * announcements fold into {@link DecodedNetworkMessage#metaData()} with the announcement sequence
 * number kept out of the data-plane header slots.
 *
 * <p>Inputs are produced by the discovery encoder itself, cross-checking the encoder against the
 * legacy decoder through real wire bytes.
 */
class UadpDiscoveryLegacyDecodeTest {

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  /** A probe decodes to a header-only result on the legacy surface: no slot to surface it. */
  @Test
  void probeYieldsHeaderOnlyResultOnLegacyDecode() throws UaException {
    UadpDiscoveryProbe probe =
        UadpDiscoveryProbe.of(PublisherId.ubyte(ubyte(7)), List.of(ushort(100)));

    DecodedNetworkMessage decoded = decodeLegacy(encodeProbeToBytes(probe));

    // The header PublisherId (the probed publisher) is still decoded.
    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  /** A Good announcement folds into {@code metaData()}, exactly as before. */
  @Test
  void goodAnnouncementFoldsIntoMetaDataOnLegacyDecode() throws UaException {
    UadpMetaDataAnnouncement announcement =
        UadpMetaDataAnnouncement.of(
            PublisherId.ubyte(ubyte(7)),
            ushort(12),
            ushort(100),
            minimalMetaData(),
            StatusCode.GOOD);

    DecodedNetworkMessage decoded = decodeLegacy(encodeAnnouncementToBytes(announcement));

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
    assertEquals(List.of(new DecodedMetaData(ushort(100), minimalMetaData())), decoded.metaData());

    // The per-PublisherId announcement SequenceNumber never surfaces as a data-plane header.
    assertNull(decoded.sequenceNumber());
    assertNull(decoded.writerGroupId());
    assertNull(decoded.groupVersion());
    assertNull(decoded.networkMessageNumber());
    assertNull(decoded.timestamp());
  }

  /** A Bad-status (denial) announcement is dropped on the legacy surface. */
  @Test
  void badAnnouncementIsDroppedOnLegacyDecode() throws UaException {
    UadpMetaDataAnnouncement denial =
        UadpMetaDataAnnouncement.of(
            PublisherId.ubyte(ubyte(7)),
            ushort(12),
            ushort(100),
            minimalMetaData(),
            new StatusCode(StatusCodes.Bad_NotFound));

    DecodedNetworkMessage decoded = decodeLegacy(encodeAnnouncementToBytes(denial));

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  /** For data-plane NetworkMessages the legacy decode and decodeMessage results are identical. */
  @Test
  void dataPlaneDecodeMatchesDecodeMessage() {
    byte[] message =
        bytes(
            0x11, // byte 0: version 1 | PublisherId 0x10
            0x2A, // PublisherId: Byte = 42
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x2A, 0x00, 0x00, 0x00); // Variant Int32 = 42

    DecodedNetworkMessage legacy = decodeLegacy(message);
    UadpDecodedMessage surfaced = decodeMessage(message);

    assertEquals(legacy, surfaced);
    assertEquals(1, legacy.messages().size());
  }

  // region helpers

  private static DataSetMetaDataType minimalMetaData() {
    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        "DS",
        new LocalizedText(null, null),
        null,
        new UUID(0L, 0L),
        new ConfigurationVersionDataType(uint(7), uint(3)));
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }

  private byte[] encodeProbeToBytes(UadpDiscoveryProbe probe) throws UaException {
    EncodedNetworkMessage encoded = new UadpMessageMapping().encodeProbe(encodingContext, probe);
    try {
      return ByteBufUtil.getBytes(encoded.data());
    } finally {
      encoded.data().release();
    }
  }

  private byte[] encodeAnnouncementToBytes(UadpMetaDataAnnouncement announcement)
      throws UaException {

    EncodedNetworkMessage encoded =
        new UadpMessageMapping().encodeAnnouncement(encodingContext, announcement);
    try {
      return ByteBufUtil.getBytes(encoded.data());
    } finally {
      encoded.data().release();
    }
  }

  private DecodedNetworkMessage decodeLegacy(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping().decode(DecodeContext.of(encodingContext), buffer);
    } finally {
      buffer.release();
    }
  }

  private UadpDecodedMessage decodeMessage(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping().decodeMessage(DecodeContext.of(encodingContext), buffer);
    } finally {
      buffer.release();
    }
  }

  // endregion
}
