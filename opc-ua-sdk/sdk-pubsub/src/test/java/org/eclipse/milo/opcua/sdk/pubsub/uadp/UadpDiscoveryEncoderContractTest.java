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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

/**
 * Error and buffer-ownership contracts of the UADP discovery encoder: {@code encodeProbe} rejects
 * anything but the DataSetMetaData probe (ProbeType 1, InformationType 2) with {@code
 * Bad_NotSupported}, successful encodes hand buffer ownership to the caller, and failed encodes
 * release the pooled buffer instead of leaking it.
 *
 * <p>Failure-path release is observed by substituting {@link BufferUtil#pooledBuffer()} with a
 * buffer the test owns and asserting its refCnt dropped to zero.
 */
class UadpDiscoveryEncoderContractTest {

  /**
   * An {@link EncodingContext} whose {@code maxMessageSize} is far below any realistic value,
   * forcing a deterministic {@code Bad_EncodingLimitsExceeded} from any string longer than 16
   * bytes, mid-encode, after the pooled buffer was allocated.
   */
  private static final EncodingContext TINY_LIMITS_CONTEXT =
      new DefaultEncodingContext() {
        @Override
        public EncodingLimits getEncodingLimits() {
          return new EncodingLimits(
              EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
              EncodingLimits.DEFAULT_MAX_CHUNK_COUNT,
              16,
              EncodingLimits.DEFAULT_MAX_RECURSION_DEPTH);
        }
      };

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region probe kind rejection

  private static Stream<Arguments> unsupportedProbeKinds() {
    return Stream.of(
        Arguments.of("FindApplications", 2, 2),
        Arguments.of("reserved ProbeType", 0, 2),
        Arguments.of("reserved InformationType", 1, 0),
        Arguments.of("publisher endpoints", 1, 1),
        Arguments.of("writer configuration", 1, 3),
        Arguments.of("group configuration", 1, 4),
        Arguments.of("connection configuration", 1, 5));
  }

  /** Only the DataSetMetaData probe (1, 2) is in scope; everything else is Bad_NotSupported. */
  @ParameterizedTest(name = "{0} ({1}, {2})")
  @MethodSource("unsupportedProbeKinds")
  void encodeProbeRejectsUnsupportedProbeKinds(String name, int probeType, int informationType) {
    UadpDiscoveryProbe probe =
        UadpDiscoveryProbe.of(
            probeType, informationType, PublisherId.ubyte(ubyte(7)), List.of(ushort(100)));

    UaException e =
        assertThrows(
            UaException.class, () -> new UadpMessageMapping().encodeProbe(encodingContext, probe));

    assertEquals(StatusCodes.Bad_NotSupported, e.getStatusCode().value());
  }

  /** The Bad_NotSupported rejection happens before any buffer is allocated. */
  @Test
  void encodeProbeRejectionAllocatesNoBuffer() {
    UadpDiscoveryProbe probe =
        UadpDiscoveryProbe.of(2, 2, PublisherId.ubyte(ubyte(7)), List.of(ushort(100)));

    try (MockedStatic<BufferUtil> bufferUtil = mockStatic(BufferUtil.class)) {
      assertThrows(
          UaException.class, () -> new UadpMessageMapping().encodeProbe(encodingContext, probe));

      bufferUtil.verify(BufferUtil::pooledBuffer, never());
    }
  }

  // endregion

  // region buffer ownership on success

  /** A successful probe encode returns a buffer with refCnt 1, owned by the caller. */
  @Test
  void encodedProbeBufferIsOwnedByCaller() throws UaException {
    EncodedNetworkMessage encoded =
        new UadpMessageMapping()
            .encodeProbe(
                encodingContext,
                UadpDiscoveryProbe.of(PublisherId.ubyte(ubyte(7)), List.of(ushort(100))));

    assertEquals(1, encoded.data().refCnt());
    // The caller's release is the buffer's last: it is fully deallocated.
    assertTrue(encoded.data().release());
  }

  /** A successful announcement encode returns a buffer with refCnt 1, owned by the caller. */
  @Test
  void encodedAnnouncementBufferIsOwnedByCaller() throws UaException {
    EncodedNetworkMessage encoded =
        new UadpMessageMapping()
            .encodeAnnouncement(
                encodingContext,
                UadpMetaDataAnnouncement.of(
                    PublisherId.ubyte(ubyte(7)),
                    ushort(1),
                    ushort(100),
                    metaDataNamed("DS"),
                    StatusCode.GOOD));

    assertEquals(1, encoded.data().refCnt());
    assertTrue(encoded.data().release());
  }

  // endregion

  // region buffer release on failure

  /**
   * A probe encode that fails mid-encode -- here on a String PublisherId exceeding the encoding
   * limits -- surfaces as UaException and releases the buffer it allocated.
   */
  @Test
  void probeEncodeFailureReleasesBuffer() {
    UadpDiscoveryProbe probe =
        UadpDiscoveryProbe.of(PublisherId.string("p".repeat(64)), List.of(ushort(100)));

    ByteBuf buffer = Unpooled.buffer();

    try (MockedStatic<BufferUtil> bufferUtil = mockStatic(BufferUtil.class)) {
      bufferUtil.when(BufferUtil::pooledBuffer).thenReturn(buffer);

      UaException e =
          assertThrows(
              UaException.class,
              () -> new UadpMessageMapping().encodeProbe(TINY_LIMITS_CONTEXT, probe));

      assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, e.getStatusCode().value());
    }

    assertEquals(0, buffer.refCnt());
  }

  /**
   * An announcement encode that fails inside the metadata structure -- here on a Name exceeding the
   * encoding limits -- surfaces as UaException and releases the buffer it allocated.
   */
  @Test
  void announcementEncodeFailureReleasesBuffer() {
    UadpMetaDataAnnouncement announcement =
        UadpMetaDataAnnouncement.of(
            PublisherId.ubyte(ubyte(7)),
            ushort(1),
            ushort(100),
            metaDataNamed("N".repeat(64)),
            StatusCode.GOOD);

    ByteBuf buffer = Unpooled.buffer();

    try (MockedStatic<BufferUtil> bufferUtil = mockStatic(BufferUtil.class)) {
      bufferUtil.when(BufferUtil::pooledBuffer).thenReturn(buffer);

      UaException e =
          assertThrows(
              UaException.class,
              () -> new UadpMessageMapping().encodeAnnouncement(TINY_LIMITS_CONTEXT, announcement));

      assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, e.getStatusCode().value());
    }

    assertEquals(0, buffer.refCnt());
  }

  // endregion

  // region helpers

  private static DataSetMetaDataType metaDataNamed(String name) {
    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        name,
        new LocalizedText(null, null),
        null,
        new UUID(0L, 0L),
        new ConfigurationVersionDataType(uint(1), uint(1)));
  }

  // endregion
}
