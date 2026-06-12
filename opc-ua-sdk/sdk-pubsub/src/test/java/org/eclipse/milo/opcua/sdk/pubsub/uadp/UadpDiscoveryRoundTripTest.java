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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Encode/decode round trips for the UADP discovery codec (OPC UA Part 14 §7.2.4.6): DataSetMetaData
 * probes and announcements across every PublisherId width, Good announcements and Bad-status
 * denials, and the announcement sequence number's independence from the data-plane counters.
 *
 * <p>Round trips go through real wire bytes: each message is encoded with {@link
 * UadpMessageMapping#encodeProbe}/{@link UadpMessageMapping#encodeAnnouncement} and decoded again
 * with {@link UadpMessageMapping#decodeMessage}, asserting record equality with the original.
 */
class UadpDiscoveryRoundTripTest {

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  private static Stream<Arguments> publisherIds() {
    return Stream.of(
        Arguments.of("Byte", PublisherId.ubyte(ubyte(42))),
        Arguments.of("UInt16", PublisherId.uint16(ushort(0x1234))),
        Arguments.of("UInt32", PublisherId.uint32(uint(0xDEADBEEFL))),
        Arguments.of("UInt64", PublisherId.uint64(ulong(0x0102030405060708L))),
        Arguments.of("String", PublisherId.string("publisher-1")));
  }

  // region probes

  /** A probe round trips across every PublisherId width (ExtendedFlags1 type bits 000-100). */
  @ParameterizedTest(name = "{0}")
  @MethodSource("publisherIds")
  void probeRoundTripsAcrossPublisherIdWidths(String name, PublisherId publisherId)
      throws UaException {

    UadpDiscoveryProbe probe =
        UadpDiscoveryProbe.of(publisherId, List.of(ushort(1), ushort(100), ushort(65535)));

    UadpDecodedMessage decoded = decodeMessage(encodeProbeToBytes(probe));

    assertEquals(probe, assertInstanceOf(UadpDiscoveryProbe.class, decoded));
  }

  /** A probe with an empty DataSetWriterIds list round trips to an empty list. */
  @Test
  void probeWithEmptyIdListRoundTrips() throws UaException {
    UadpDiscoveryProbe probe = UadpDiscoveryProbe.of(PublisherId.ubyte(ubyte(7)), List.of());

    UadpDecodedMessage decoded = decodeMessage(encodeProbeToBytes(probe));

    UadpDiscoveryProbe decodedProbe = assertInstanceOf(UadpDiscoveryProbe.class, decoded);
    assertEquals(probe, decodedProbe);
    assertTrue(decodedProbe.dataSetWriterIds().isEmpty());
  }

  // endregion

  // region announcements

  /** An announcement round trips across every PublisherId width. */
  @ParameterizedTest(name = "{0}")
  @MethodSource("publisherIds")
  void announcementRoundTripsAcrossPublisherIdWidths(String name, PublisherId publisherId)
      throws UaException {

    UadpMetaDataAnnouncement announcement =
        UadpMetaDataAnnouncement.of(
            publisherId, ushort(12), ushort(100), minimalMetaData(), StatusCode.GOOD);

    UadpDecodedMessage decoded = decodeMessage(encodeAnnouncementToBytes(announcement));

    assertEquals(announcement, assertInstanceOf(UadpMetaDataAnnouncement.class, decoded));
  }

  /**
   * An announcement carrying a fully populated metadata structure -- namespaces, a FieldMetaData
   * entry with flags, array dimensions, and properties -- round trips field for field.
   */
  @Test
  void announcementWithRichMetaDataRoundTrips() throws UaException {
    var field =
        new FieldMetaData(
            "Temperature",
            new LocalizedText("en", "temperature in degrees"),
            new DataSetFieldFlags(ushort(1)), // PromotedField
            ubyte(11), // BuiltInType Double
            new NodeId(0, 11), // DataType Double
            -1, // ValueRank scalar
            null,
            uint(0),
            UUID.fromString("c496578a-0dfe-4b8f-870a-745238c6aeae"),
            new KeyValuePair[] {
              new KeyValuePair(new QualifiedName(0, "EngineeringUnits"), Variant.ofString("degC"))
            });

    var metaData =
        new DataSetMetaDataType(
            new String[] {"urn:test:namespace"},
            null,
            null,
            null,
            "RichDataSet",
            new LocalizedText("en", "a rich dataset"),
            new FieldMetaData[] {field},
            UUID.fromString("a921b04a-cf63-4377-91b3-fbf03a87f3c2"),
            new ConfigurationVersionDataType(uint(7), uint(3)));

    UadpMetaDataAnnouncement announcement =
        UadpMetaDataAnnouncement.of(
            PublisherId.uint16(ushort(0x1234)), ushort(2), ushort(10), metaData, StatusCode.GOOD);

    UadpDecodedMessage decoded = decodeMessage(encodeAnnouncementToBytes(announcement));

    assertEquals(announcement, assertInstanceOf(UadpMetaDataAnnouncement.class, decoded));
  }

  /**
   * A denial -- Bad statusCode with the placeholder metadata struct that is always present on the
   * wire (Table 170) -- round trips with the Bad status intact.
   */
  @Test
  void denialAnnouncementRoundTrips() throws UaException {
    // The metadata field is non-null even for denials: an empty placeholder struct.
    var placeholder =
        new DataSetMetaDataType(
            null,
            null,
            null,
            null,
            null,
            new LocalizedText(null, null),
            null,
            new UUID(0L, 0L),
            new ConfigurationVersionDataType(uint(0), uint(0)));

    UadpMetaDataAnnouncement denial =
        UadpMetaDataAnnouncement.of(
            PublisherId.ubyte(ubyte(7)),
            ushort(3),
            ushort(999),
            placeholder,
            new StatusCode(StatusCodes.Bad_NotFound));

    UadpDecodedMessage decoded = decodeMessage(encodeAnnouncementToBytes(denial));

    UadpMetaDataAnnouncement decodedDenial =
        assertInstanceOf(UadpMetaDataAnnouncement.class, decoded);
    assertEquals(denial, decodedDenial);
    assertTrue(decodedDenial.statusCode().isBad());
  }

  /**
   * The announcement sequence number is a separate per-PublisherId counter (Table 168): it round
   * trips on the announcement record but never leaks into the data-plane {@link
   * DecodedNetworkMessage#sequenceNumber()} of the legacy decode surface.
   */
  @Test
  void announcementSequenceNumberIsIndependentOfDataPlaneCounters() throws UaException {
    UadpMetaDataAnnouncement announcement =
        UadpMetaDataAnnouncement.of(
            PublisherId.ubyte(ubyte(7)),
            ushort(54321),
            ushort(100),
            minimalMetaData(),
            StatusCode.GOOD);

    byte[] encoded = encodeAnnouncementToBytes(announcement);

    UadpMetaDataAnnouncement decoded =
        assertInstanceOf(UadpMetaDataAnnouncement.class, decodeMessage(encoded));
    assertEquals(ushort(54321), decoded.sequenceNumber());

    DecodedNetworkMessage legacy = decodeLegacy(encoded);
    assertNull(legacy.sequenceNumber());
  }

  // endregion

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

  private UadpDecodedMessage decodeMessage(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping().decodeMessage(new DecodeContext(encodingContext), buffer);
    } finally {
      buffer.release();
    }
  }

  private DecodedNetworkMessage decodeLegacy(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping().decode(new DecodeContext(encodingContext), buffer);
    } finally {
      buffer.release();
    }
  }

  // endregion
}
