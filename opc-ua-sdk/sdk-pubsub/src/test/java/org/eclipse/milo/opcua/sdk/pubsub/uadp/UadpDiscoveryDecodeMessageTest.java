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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Surfacing semantics of {@link UadpMessageMapping#decodeMessage}: which inputs surface as {@link
 * UadpDiscoveryProbe} or {@link UadpMetaDataAnnouncement}, and which discovery content is tolerated
 * but NOT surfaced -- yielding a header-only {@link DecodedNetworkMessage} -- per OPC UA Part 14
 * v1.05 §7.2.4.6 and the Phase 2 scope (DataSetMetaData probes and announcements only).
 *
 * <p>Like the legacy decode surface, {@code decodeMessage} is tolerant: malformed, truncated, or
 * hostile input never raises an exception to the caller.
 */
class UadpDiscoveryDecodeMessageTest {

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region probe surfacing

  /** A probe with a zero-length DataSetWriterIds array surfaces with an empty list. */
  @Test
  void probeWithEmptyIdArraySurfacesEmptyList() {
    byte[] message =
        concat(
            discoveryHeader(0x04),
            bytes(
                0x01, // ProbeType = 1
                0x02, // InformationType = 2
                0x00, 0x00, 0x00, 0x00)); // DataSetWriterIds count = 0

    UadpDiscoveryProbe probe = assertInstanceOf(UadpDiscoveryProbe.class, decodeMessage(message));

    assertEquals(PublisherId.ubyte(ubyte(7)), probe.targetPublisherId());
    assertTrue(probe.dataSetWriterIds().isEmpty());
  }

  /** A probe with a null (-1) DataSetWriterIds array also surfaces with an empty list. */
  @Test
  void probeWithNullIdArraySurfacesEmptyList() {
    byte[] message =
        concat(
            discoveryHeader(0x04),
            bytes(
                0x01, // ProbeType = 1
                0x02, // InformationType = 2
                0xFF, 0xFF, 0xFF, 0xFF)); // DataSetWriterIds count = -1 (null array)

    UadpDiscoveryProbe probe = assertInstanceOf(UadpDiscoveryProbe.class, decodeMessage(message));

    assertEquals(PublisherId.ubyte(ubyte(7)), probe.targetPublisherId());
    assertTrue(probe.dataSetWriterIds().isEmpty());
  }

  /** A count exactly matching the remaining bytes is legitimate and decodes fully. */
  @Test
  void idCountMatchingRemainingBytesDecodes() {
    byte[] message =
        concat(
            discoveryHeader(0x04),
            bytes(
                0x01, // ProbeType = 1
                0x02, // InformationType = 2
                0x02, 0x00, 0x00, 0x00, // DataSetWriterIds count = 2; exactly 4 bytes follow
                0x01, 0x00, // DataSetWriterIds[0] = 1
                0x02, 0x00)); // DataSetWriterIds[1] = 2

    UadpDiscoveryProbe probe = assertInstanceOf(UadpDiscoveryProbe.class, decodeMessage(message));

    assertEquals(List.of(ushort(1), ushort(2)), probe.dataSetWriterIds());
  }

  // endregion

  // region announcement surfacing

  /** A Good-status DataSetMetaData announcement surfaces as a {@link UadpMetaDataAnnouncement}. */
  @Test
  void goodStatusAnnouncementIsSurfaced() {
    byte[] message = metaDataAnnouncement(bytes(0x00, 0x00, 0x00, 0x00)); // StatusCode = Good

    UadpMetaDataAnnouncement announcement =
        assertInstanceOf(UadpMetaDataAnnouncement.class, decodeMessage(message));

    assertEquals(PublisherId.ubyte(ubyte(7)), announcement.publisherId());
    assertEquals(ushort(1), announcement.sequenceNumber());
    assertEquals(ushort(10), announcement.dataSetWriterId());
    assertEquals(minimalMetaData(), announcement.metaData());
    assertTrue(announcement.statusCode().isGood());
  }

  /**
   * A Bad-status announcement is a DENIAL: unlike the legacy decode surface, which drops it, {@code
   * decodeMessage} surfaces it so the subscriber can stop probing.
   */
  @Test
  void badStatusAnnouncementIsSurfacedAsDenial() {
    byte[] message = metaDataAnnouncement(bytes(0x00, 0x00, 0x3E, 0x80)); // Bad_NotFound

    UadpMetaDataAnnouncement announcement =
        assertInstanceOf(UadpMetaDataAnnouncement.class, decodeMessage(message));

    assertEquals(PublisherId.ubyte(ubyte(7)), announcement.publisherId());
    assertEquals(ushort(10), announcement.dataSetWriterId());
    assertTrue(announcement.statusCode().isBad());
    // The metadata struct is always on the wire (Table 170), even for denials.
    assertNotNull(announcement.metaData());
  }

  // endregion

  // region tolerated discovery content -> header-only DecodedNetworkMessage

  /** A FindApplications probe (ProbeType 2) is tolerated and ignored. */
  @Test
  void findApplicationsProbeYieldsHeaderOnlyResult() {
    byte[] message =
        concat(
            discoveryHeader(0x04),
            bytes(0x02)); // ProbeType = 2 (FindApplications); no additional fields

    assertHeaderOnly(decodeMessage(message));
  }

  /** The reserved ProbeType 0 is tolerated and ignored. */
  @Test
  void reservedProbeTypeYieldsHeaderOnlyResult() {
    byte[] message = concat(discoveryHeader(0x04), bytes(0x00)); // ProbeType = 0 (Reserved)

    assertHeaderOnly(decodeMessage(message));
  }

  /**
   * Publisher information probes with InformationTypes other than DataSetMetaData (2) -- endpoints
   * (1), writer config (3), group config (4), connection config (5), reserved (0) -- are tolerated
   * and ignored.
   */
  @ParameterizedTest(name = "InformationType {0}")
  @ValueSource(ints = {0, 1, 3, 4, 5})
  void unsupportedProbeInformationTypeYieldsHeaderOnlyResult(int informationType) {
    byte[] message =
        concat(
            discoveryHeader(0x04),
            bytes(
                0x01, // ProbeType = 1
                informationType,
                0x01,
                0x00,
                0x00,
                0x00, // (settings bytes; not interpreted)
                0x64,
                0x00));

    assertHeaderOnly(decodeMessage(message));
  }

  /** Announcement types other than DataSetMetaData (2) are tolerated and ignored. */
  @ParameterizedTest(name = "AnnouncementType {0}")
  @ValueSource(ints = {0, 1, 3, 4, 5, 6, 7})
  void unsupportedAnnouncementTypeYieldsHeaderOnlyResult(int announcementType) {
    byte[] message =
        concat(
            discoveryHeader(0x08),
            bytes(
                announcementType,
                0x01,
                0x00, // announcement SequenceNumber = 1
                0x00,
                0x00,
                0x00,
                0x00)); // (announcement body; not interpreted)

    assertHeaderOnly(decodeMessage(message));
  }

  /** Chunked discovery NetworkMessages (ExtendedFlags2 chunk bit) are not reassembled. */
  @ParameterizedTest(name = "ExtendedFlags2 0x{0}")
  @ValueSource(ints = {0x05, 0x09}) // probe + chunk, announcement + chunk
  void chunkedDiscoveryMessageYieldsHeaderOnlyResult(int extendedFlags2) {
    byte[] message =
        concat(
            discoveryHeader(extendedFlags2),
            bytes(0x01, 0x00, 0x02, 0x00)); // (chunk data, not decoded)

    assertHeaderOnly(decodeMessage(message));
  }

  /** Discovery messages with any non-zero SecurityFlags are skipped (mode None only). */
  @ParameterizedTest(name = "ExtendedFlags2 0x{0}")
  @ValueSource(ints = {0x04, 0x08})
  void securityFlaggedDiscoveryMessageYieldsHeaderOnlyResult(int extendedFlags2) {
    byte[] message =
        bytes(
            0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
            0x90, // ExtendedFlags1: SecurityHeader | ExtendedFlags2 (PublisherId type Byte)
            extendedFlags2,
            0x07, // PublisherId: Byte = 7
            0x01, // SecurityFlags: NetworkMessage signed; unsupported -> skip payload
            0x01,
            0x02,
            0x01,
            0x00,
            0x00,
            0x00,
            0x64,
            0x00); // (would-be payload, not decoded)

    assertHeaderOnly(decodeMessage(message));
  }

  /** Discovery messages shall not carry a PayloadHeader (§7.2.4.6.3); such input is dropped. */
  @ParameterizedTest(name = "ExtendedFlags2 0x{0}")
  @ValueSource(ints = {0x04, 0x08})
  void discoveryMessageWithPayloadHeaderYieldsHeaderOnlyResult(int extendedFlags2) {
    byte[] message =
        bytes(
            0xD1, // byte 0: version 1 | PublisherId 0x10 | PayloadHeader 0x40 | ExtendedFlags1
            0x90, // ExtendedFlags1: SecurityHeader | ExtendedFlags2
            extendedFlags2,
            0x07, // PublisherId: Byte = 7
            0x01, // (whatever follows is undefined for discovery + PayloadHeader)
            0x01,
            0x00);

    assertHeaderOnly(decodeMessage(message));
  }

  /** A hostile DataSetWriterIds count promising more ids than the buffer holds is skipped. */
  @Test
  void hostileIdCountYieldsHeaderOnlyResult() {
    byte[] message =
        concat(
            discoveryHeader(0x04),
            bytes(
                0x01, // ProbeType = 1
                0x02, // InformationType = 2
                0xFF, 0xFF, 0xFF, 0x7F)); // DataSetWriterIds count = Int32.MAX_VALUE, 0 bytes left

    assertHeaderOnly(decodeMessage(message));
  }

  /** A count lying just past the remaining bytes (off-by-a-few) is also skipped, not partial. */
  @Test
  void idCountExceedingRemainingBytesYieldsHeaderOnlyResult() {
    byte[] message =
        concat(
            discoveryHeader(0x04),
            bytes(
                0x01, // ProbeType = 1
                0x02, // InformationType = 2
                0x05, 0x00, 0x00, 0x00, // DataSetWriterIds count = 5, but only 2 ids follow
                0x01, 0x00, 0x02, 0x00));

    assertHeaderOnly(decodeMessage(message));
  }

  /** A probe without a PublisherId cannot be answered (§7.2.4.6.12.1) and is not surfaced. */
  @Test
  void probeWithoutPublisherIdYieldsHeaderOnlyResult() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1; PublisherId bit NOT set
            0x90, // ExtendedFlags1: SecurityHeader | ExtendedFlags2
            0x04, // ExtendedFlags2: discovery probe
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // SecurityHeader (None)
            0x01, // ProbeType = 1
            0x02, // InformationType = 2
            0x01, 0x00, 0x00, 0x00, // DataSetWriterIds count = 1
            0x64, 0x00); // DataSetWriterIds[0] = 100

    DecodedNetworkMessage result = assertHeaderOnly(decodeMessage(message));
    assertNull(result.publisherId());
  }

  /** An announcement without a PublisherId cannot be correlated and is not surfaced. */
  @Test
  void announcementWithoutPublisherIdYieldsHeaderOnlyResult() {
    byte[] message =
        concat(
            bytes(
                0x81, // byte 0: version 1 | ExtendedFlags1; PublisherId bit NOT set
                0x90, // ExtendedFlags1: SecurityHeader | ExtendedFlags2
                0x08, // ExtendedFlags2: discovery announcement
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // SecurityHeader (None)
                0x02, // AnnouncementType = 2
                0x01, 0x00, // SequenceNumber = 1
                0x0A, 0x00), // DataSetWriterId = 10
            minimalMetaDataStructBytes(),
            bytes(0x00, 0x00, 0x00, 0x00)); // StatusCode = Good

    DecodedNetworkMessage result = assertHeaderOnly(decodeMessage(message));
    assertNull(result.publisherId());
  }

  // endregion

  // region data-plane pass-through and tolerance

  /** Data-plane NetworkMessages pass through {@code decodeMessage} fully decoded. */
  @Test
  void dataPlaneMessageSurfacesAsDecodedNetworkMessage() {
    byte[] message =
        bytes(
            0x11, // byte 0: version 1 | PublisherId 0x10
            0x2A, // PublisherId: Byte = 42
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x2A, 0x00, 0x00, 0x00); // Variant Int32 = 42

    DecodedNetworkMessage decoded =
        assertInstanceOf(DecodedNetworkMessage.class, decodeMessage(message));

    assertEquals(PublisherId.ubyte(ubyte(42)), decoded.publisherId());
    assertEquals(1, decoded.messages().size());
    assertTrue(decoded.messages().get(0).valid());
    assertTrue(decoded.metaData().isEmpty());
  }

  /** Deterministic pseudo-random garbage never raises an exception out of decodeMessage. */
  @Test
  void garbageBytesNeverThrow() {
    var random = new Random(0x5EED);

    for (int i = 0; i < 256; i++) {
      byte[] garbage = new byte[random.nextInt(64)];
      random.nextBytes(garbage);

      UadpDecodedMessage decoded = decodeMessage(garbage);

      assertNotNull(decoded, "iteration " + i);
    }
  }

  /** Truncating a discovery announcement at every byte boundary never throws. */
  @Test
  void truncationAtEveryCutPointNeverThrows() {
    byte[] full = metaDataAnnouncement(bytes(0x00, 0x00, 0x00, 0x00));

    // Sanity: the full message surfaces as an announcement.
    assertInstanceOf(UadpMetaDataAnnouncement.class, decodeMessage(full));

    for (int length = 0; length < full.length; length++) {
      byte[] truncated = new byte[length];
      System.arraycopy(full, 0, truncated, 0, length);

      UadpDecodedMessage decoded = decodeMessage(truncated);

      assertNotNull(decoded, "cut at " + length);
    }
  }

  // endregion

  // region helpers

  /**
   * The fixed discovery header with a Byte PublisherId 7 and a mode None SecurityHeader; {@code
   * extendedFlags2} selects probe (0x04), announcement (0x08), or chunked variants.
   */
  private static byte[] discoveryHeader(int extendedFlags2) {
    return bytes(
        0x91, // byte 0: version 1 | PublisherId 0x10 | ExtendedFlags1 0x80
        0x90, // ExtendedFlags1: SecurityHeader 0x10 | ExtendedFlags2 0x80 (PublisherId type Byte)
        extendedFlags2,
        0x07, // PublisherId: Byte = 7
        0x00, // SecurityFlags = 0 (None)
        0x00,
        0x00,
        0x00,
        0x00, // SecurityTokenId = 0
        0x00); // NonceLength = 0
  }

  /** A complete DataSetMetaData announcement for writer id 10 with the given trailing status. */
  private static byte[] metaDataAnnouncement(byte[] statusCodeBytes) {
    return concat(
        discoveryHeader(0x08),
        bytes(
            0x02, // AnnouncementType = 2 (DataSetMetaData)
            0x01, 0x00, // announcement SequenceNumber = 1
            0x0A, 0x00), // DataSetWriterId = 10
        minimalMetaDataStructBytes(),
        statusCodeBytes);
  }

  /** The inline encoding of {@link #minimalMetaData()}. */
  private static byte[] minimalMetaDataStructBytes() {
    return bytes(
        0xFF, 0xFF, 0xFF, 0xFF, // Namespaces: null array
        0xFF, 0xFF, 0xFF, 0xFF, // StructureDataTypes: null array
        0xFF, 0xFF, 0xFF, 0xFF, // EnumDataTypes: null array
        0xFF, 0xFF, 0xFF, 0xFF, // SimpleDataTypes: null array
        0x02, 0x00, 0x00, 0x00, 0x44, 0x53, // Name = "DS"
        0x00, // Description: null LocalizedText (encoding mask 0)
        0xFF, 0xFF, 0xFF, 0xFF, // Fields: null array
        0x00, 0x00, 0x00, 0x00, // DataSetClassId: null Guid (16 zero bytes)
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00,
        0x00, // ConfigurationVersion.MajorVersion = 7
        0x03, 0x00, 0x00, 0x00); // ConfigurationVersion.MinorVersion = 3
  }

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

  /**
   * Assert that {@code decoded} is the tolerated-but-not-surfaced shape: a {@link
   * DecodedNetworkMessage} with no DataSetMessages and no metadata.
   */
  private static DecodedNetworkMessage assertHeaderOnly(UadpDecodedMessage decoded) {
    DecodedNetworkMessage message = assertInstanceOf(DecodedNetworkMessage.class, decoded);
    assertTrue(message.messages().isEmpty());
    assertTrue(message.metaData().isEmpty());
    return message;
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }

  private static byte[] concat(byte[]... arrays) {
    int length = 0;
    for (byte[] array : arrays) {
      length += array.length;
    }
    byte[] result = new byte[length];
    int offset = 0;
    for (byte[] array : arrays) {
      System.arraycopy(array, 0, result, offset, array.length);
      offset += array.length;
    }
    return result;
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
