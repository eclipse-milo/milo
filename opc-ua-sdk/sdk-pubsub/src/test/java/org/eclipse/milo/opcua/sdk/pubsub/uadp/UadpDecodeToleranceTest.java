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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.Test;

/**
 * Decode tolerance tests for the UADP codec: malformed, truncated, or unsupported input must never
 * raise an exception to the caller; whatever was decoded before the problem is returned.
 *
 * <p>Failures that end decoding early (truncation — including any explicit length field exceeding
 * the remaining bytes — and chunked NetworkMessages) are additionally surfaced via {@link
 * DecodedNetworkMessage#failure()}; tolerated-and-skipped input (foreign version nibbles, reserved
 * flag values) reports no failure.
 *
 * <p>Bit positions reference OPC UA Part 14 v1.05 §7.2.4.4.2 Table 154 and §7.2.4.5.4 Table 162.
 */
class UadpDecodeToleranceTest {

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region truncation

  /** Truncating a rich NetworkMessage at EVERY byte boundary must never throw. */
  @Test
  void truncationAtEveryCutPointNeverThrows() throws UaException {
    byte[] full = richEncodedMessage();

    // Sanity: the full message decodes into two DataSetMessages.
    assertEquals(2, decode(full).messages().size());

    for (int length = 0; length < full.length; length++) {
      byte[] truncated = Arrays.copyOf(full, length);

      DecodedNetworkMessage decoded = decode(truncated);

      assertNotNull(decoded, "cut at " + length);
      // A truncated message can never yield MORE DataSetMessages than the full one.
      assertTrue(decoded.messages().size() <= 2, "cut at " + length);
    }
  }

  /** A buffer cut inside the GroupHeader yields the header values decoded so far. */
  @Test
  void truncatedGroupHeaderYieldsPartialHeader() {
    byte[] message =
        bytes(
            0x21, // byte 0: version 1 | GroupHeader 0x20
            0x0F, // GroupFlags: all four optional fields
            0x02, 0x01, // WriterGroupId = 258
            0x04, 0x03, 0x02, 0x01); // GroupVersion = 0x01020304; buffer ends here

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(ushort(258), decoded.writerGroupId());
    assertEquals(uint(0x01020304L), decoded.groupVersion());
    assertNull(decoded.networkMessageNumber());
    assertNull(decoded.sequenceNumber());
    assertTrue(decoded.messages().isEmpty());
  }

  /** A field count that promises more fields than the buffer holds ends without an exception. */
  @Test
  void truncatedFieldDataYieldsNoMessages() {
    byte[] message =
        bytes(
            0x01, // byte 0: version 1
            0x01, // DataSetFlags1: valid | Variant encoding
            0x02, 0x00, // FieldCount = 2, but only one field follows
            0x06, 0x2A, 0x00, 0x00, 0x00); // Variant Int32 = 42

    DecodedNetworkMessage decoded = decode(message);

    // The unbounded (no Sizes) DataSetMessage failed mid-decode and is not surfaced.
    assertTrue(decoded.messages().isEmpty());

    // The failure that ended decoding is observable.
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /** With Sizes, a corrupt DataSetMessage is surfaced invalid and decoding continues. */
  @Test
  void corruptSizedDataSetMessageIsSkippedAndDecodingContinues() {
    byte[] message =
        bytes(
            0x41, // byte 0: version 1 | PayloadHeader 0x40
            0x02, // PayloadHeader: Count = 2
            0x01, 0x00, // DataSetWriterIds[0] = 1
            0x02, 0x00, // DataSetWriterIds[1] = 2
            0x02, 0x00, // Sizes[0] = 2
            0x05, 0x00, // Sizes[1] = 5
            0x09, 0x09, // DSM 1: flags declare a sequence number but the slice is too short
            0x01, 0x01, 0x00, 0x01, 0x01); // DSM 2: valid key frame, Boolean true

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(2, decoded.messages().size());

    DecodedDataSetMessage first = decoded.messages().get(0);
    assertEquals(ushort(1), first.dataSetWriterId());
    assertFalse(first.valid());
    assertTrue(first.fields().isEmpty());

    DecodedDataSetMessage second = decoded.messages().get(1);
    assertEquals(ushort(2), second.dataSetWriterId());
    assertTrue(second.valid());
    assertEquals(1, second.fields().size());
  }

  /** A Sizes entry larger than the remaining buffer ends decoding without an exception. */
  @Test
  void sizeExceedingBufferYieldsNoMessages() {
    byte[] message =
        bytes(
            0x41, // byte 0: version 1 | PayloadHeader
            0x02, // Count = 2
            0x01, 0x00, 0x02, 0x00, // DataSetWriterIds
            0x64, 0x00, // Sizes[0] = 100: more than the buffer holds
            0x05, 0x00, // Sizes[1] = 5
            0x01, 0x01, 0x00); // truncated payload

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());

    // The Sizes/buffer mismatch is the truncated-datagram signature and is surfaced as a failure.
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /**
   * A buffer cut exactly at a DataSetMessage boundary: everything decoded before the cut is
   * delivered AND the failure is surfaced (partial-delivery posture with observability).
   */
  @Test
  void cutAtDataSetMessageBoundaryDeliversPartialAndSurfacesFailure() {
    byte[] full =
        bytes(
            0x41, // byte 0: version 1 | PayloadHeader 0x40
            0x02, // PayloadHeader: Count = 2
            0x01, 0x00, // DataSetWriterIds[0] = 1
            0x02, 0x00, // DataSetWriterIds[1] = 2
            0x05, 0x00, // Sizes[0] = 5
            0x05, 0x00, // Sizes[1] = 5
            0x01, 0x01, 0x00, 0x01, 0x01, // DSM 1: valid key frame, Boolean true
            0x01, 0x01, 0x00, 0x01, 0x00); // DSM 2: valid key frame, Boolean false

    // Sanity: the full message decodes both DataSetMessages without a failure.
    DecodedNetworkMessage fullDecoded = decode(full);
    assertEquals(2, fullDecoded.messages().size());
    assertNull(fullDecoded.failure());

    // Cut exactly at the DSM 1 / DSM 2 boundary: Sizes[1] exceeds the zero remaining bytes.
    DecodedNetworkMessage decoded = decode(Arrays.copyOf(full, full.length - 5));

    assertEquals(1, decoded.messages().size());
    assertEquals(ushort(1), decoded.messages().get(0).dataSetWriterId());
    assertTrue(decoded.messages().get(0).valid());
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /** A buffer that ends exactly at the NetworkMessage header boundary surfaces a failure. */
  @Test
  void cutAtNetworkMessageHeaderBoundarySurfacesFailure() {
    byte[] message =
        bytes(
            0x11, // byte 0: version 1 | PublisherId 0x10
            0x07); // PublisherId: Byte = 7; the payload is missing entirely

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /** One byte short of a complete message surfaces a failure; nothing partial is fabricated. */
  @Test
  void oneByteShortSurfacesFailure() {
    byte[] full =
        bytes(
            0x01, // byte 0: version 1
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x2A, 0x00, 0x00, 0x00); // Variant Int32 = 42

    // Sanity: the full message decodes cleanly.
    DecodedNetworkMessage fullDecoded = decode(full);
    assertEquals(1, fullDecoded.messages().size());
    assertNull(fullDecoded.failure());

    DecodedNetworkMessage decoded = decode(Arrays.copyOf(full, full.length - 1));

    assertTrue(decoded.messages().isEmpty());
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /** One trailing garbage byte after a complete message is tolerated: full decode, no failure. */
  @Test
  void oneByteLongWithTrailingGarbageDecodesWithoutFailure() {
    byte[] full =
        bytes(
            0x01, // byte 0: version 1
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x2A, 0x00, 0x00, 0x00); // Variant Int32 = 42

    byte[] oneLong = Arrays.copyOf(full, full.length + 1);
    oneLong[full.length] = (byte) 0xFF; // trailing garbage, never read

    DecodedNetworkMessage decoded = decode(oneLong);

    assertEquals(1, decoded.messages().size());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))),
        decoded.messages().get(0).fields());
    assertNull(decoded.failure());
  }

  // endregion

  // region security

  /** Non-zero SecurityFlags: the payload is skipped but the header is still decoded. */
  @Test
  void nonZeroSecurityFlagsSkipPayloadButKeepHeader() {
    byte[] message =
        bytes(
            0xB1, // byte 0: version 1 | PublisherId 0x10 | GroupHeader 0x20 | ExtendedFlags1 0x80
            0x10, // ExtendedFlags1: SecurityHeader 0x10 (PublisherId type Byte)
            0x07, // PublisherId: Byte = 7
            0x01, // GroupFlags: WriterGroupId
            0x39, 0x05, // WriterGroupId = 1337
            0x01, // SecurityFlags: NetworkMessage signed; unsupported -> skip payload
            0x01, 0x01, 0x00, 0x06, 0x2A, 0x00, 0x00, 0x00); // (would-be payload, not decoded)

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertEquals(ushort(1337), decoded.writerGroupId());
    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  /** SecurityFlags == 0 (mode None): the SecurityHeader is consumed and decoding continues. */
  @Test
  void securityHeaderWithModeNoneIsConsumed() {
    byte[] message =
        bytes(
            0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
            0x10, // ExtendedFlags1: SecurityHeader (PublisherId type Byte)
            0x07, // PublisherId: Byte = 7
            0x00, // SecurityFlags = 0 (None)
            0x2A, 0x00, 0x00, 0x00, // SecurityTokenId = 42
            0x04, // NonceLength = 4
            0xAA, 0xBB, 0xCC, 0xDD, // MessageNonce
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x2A, 0x00, 0x00, 0x00); // Variant Int32 = 42

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertEquals(1, decoded.messages().size());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))),
        decoded.messages().get(0).fields());
  }

  /** A NonceLength larger than the remaining buffer ends decoding without an exception. */
  @Test
  void nonceLengthExceedingBufferSurfacesFailure() {
    byte[] message =
        bytes(
            0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
            0x10, // ExtendedFlags1: SecurityHeader
            0x07, // PublisherId: Byte = 7
            0x00, // SecurityFlags = 0
            0x00, 0x00, 0x00, 0x00, // SecurityTokenId = 0
            0xFF, // NonceLength = 255: more than the buffer holds
            0x01); // (lone trailing byte)

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());

    // the NonceLength/buffer mismatch is a truncation signature and is surfaced as a failure
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  // endregion

  // region chunk / promoted fields / action header

  /** The chunk flag (ExtendedFlags2 bit 0) causes the payload to be skipped. */
  @Test
  void chunkedNetworkMessageIsSkipped() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk
            0x01, 0x00, 0x05, 0x00, 0x00, 0x00); // (chunk data, not decoded)

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());

    // The skip is no longer silent: chunk reassembly is unsupported, surfaced as a failure.
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_NotSupported, decoded.failure().statusCode().value());
  }

  /**
   * A chunked NetworkMessage with PayloadHeader: the header is the single-DataSetWriterId form
   * (Table 158, no Count byte); the payload is still skipped.
   */
  @Test
  void chunkedNetworkMessageWithPayloadHeaderIsSkipped() {
    byte[] message =
        bytes(
            0xC1, // byte 0: version 1 | PayloadHeader 0x40 | ExtendedFlags1 0x80
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk
            0x05, 0x00, // PayloadHeader (chunk form): DataSetWriterId = 5
            0x01, 0x00, // chunk MessageSequenceNumber (not decoded)
            0x00, 0x00, 0x00, 0x00); // (more chunk data, not decoded)

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_NotSupported, decoded.failure().statusCode().value());
  }

  /** A PromotedFields block is skipped via its Size field; the payload after it IS decoded. */
  @Test
  void promotedFieldsBlockSkippedPayloadDecoded() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x02, // ExtendedFlags2: PromotedFields
            0x07, 0x00, // PromotedFields Size = 7 bytes
            0xDE, 0xAD, 0xBE, 0xEF, 0x01, 0x02, 0x03, // promoted fields block (skipped)
            0x01, // DataSetFlags1: valid | Variant encoding
            0x01, 0x00, // FieldCount = 1
            0x06, 0x2A, 0x00, 0x00, 0x00); // Variant Int32 = 42

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage dsm = decoded.messages().get(0);
    assertTrue(dsm.valid());
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))), dsm.fields());
  }

  /** A PromotedFields Size larger than the remaining buffer drops the message, no exception. */
  @Test
  void promotedFieldsSizeExceedingBufferSurfacesFailure() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x02, // ExtendedFlags2: PromotedFields
            0xFF, 0xFF, // PromotedFields Size = 65535
            0x01, 0x02); // far fewer bytes remain

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());

    // the Size/buffer mismatch is a truncation signature and is surfaced as a failure
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /** An ActionHeader (ExtendedFlags2 bit 5) is unsupported; the payload is skipped. */
  @Test
  void actionHeaderSkipsPayload() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x20, // ExtendedFlags2: ActionHeader enabled
            0x01, 0x01, 0x00, 0x01, 0x01); // (would-be payload, not decoded)

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());
  }

  // endregion

  // region per-DataSetMessage skips

  /** A DataSetMessage with the wire valid bit clear is surfaced with {@code valid == false}. */
  @Test
  void wireValidBitClearSurfacedAsInvalidMessage() {
    byte[] message =
        bytes(
            0x01, // byte 0: version 1
            0x00); // DataSetFlags1: valid bit CLEAR; the rest shall not be processed

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage dsm = decoded.messages().get(0);
    assertFalse(dsm.valid());
    assertEquals(DataSetMessageKind.KEY_FRAME, dsm.kind());
    assertTrue(dsm.fields().isEmpty());
  }

  /** RawData field encoding needs metadata offsets: skipped, header fields still surfaced. */
  @Test
  void rawDataFieldEncodingSkippedWithHeaderSurfaced() {
    byte[] message =
        bytes(
            0x01, // byte 0: version 1
            0x0B, // DataSetFlags1: valid 0x01 | encoding 01 = RawData | seq 0x08
            0x09, 0x00, // DataSetMessageSequenceNumber = 9
            0xDE, 0xAD); // raw field bytes (not decodable without metadata)

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(1, decoded.messages().size());
    DecodedDataSetMessage dsm = decoded.messages().get(0);
    assertFalse(dsm.valid());
    assertEquals(DataSetMessageKind.KEY_FRAME, dsm.kind());
    assertEquals(uint(9), dsm.sequenceNumber());
    assertTrue(dsm.fields().isEmpty());
  }

  /** The reserved field encoding 11 marks the DataSetMessage invalid. */
  @Test
  void reservedFieldEncodingSkipped() {
    byte[] message =
        bytes(
            0x01, // byte 0: version 1
            0x07, // DataSetFlags1: valid 0x01 | encoding 11 = Reserved
            0x01, 0x00, 0x01, 0x01); // (undecodable body)

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(1, decoded.messages().size());
    assertFalse(decoded.messages().get(0).valid());
    assertTrue(decoded.messages().get(0).fields().isEmpty());
  }

  /** Reserved DataSetMessage types (DataSetFlags2 bits 0-3) mark the message invalid. */
  @Test
  void reservedDataSetMessageTypeSkipped() {
    // Type 0111 is reserved; type 0100 is absent from the v1.05.07 table, also reserved.
    for (int type : new int[] {0x07, 0x04}) {
      byte[] message =
          bytes(
              0x01, // byte 0: version 1
              0x81, // DataSetFlags1: valid | DataSetFlags2 present
              type); // DataSetFlags2: reserved type

      DecodedNetworkMessage decoded = decode(message);

      assertEquals(1, decoded.messages().size(), "type " + type);
      assertFalse(decoded.messages().get(0).valid(), "type " + type);
      assertTrue(decoded.messages().get(0).fields().isEmpty(), "type " + type);
    }
  }

  // endregion

  // region whole-message drops (reserved values, bad version, garbage)

  /** Any UADP version other than 1 drops the whole NetworkMessage. */
  @Test
  void unsupportedVersionYieldsEmptyResult() {
    for (int version : new int[] {0x00, 0x02, 0x0F}) {
      byte[] message = bytes(version, 0x01, 0x01, 0x00); // version nibble != 1

      DecodedNetworkMessage decoded = decode(message);

      assertNull(decoded.publisherId(), "version " + version);
      assertTrue(decoded.messages().isEmpty(), "version " + version);

      // foreign input (e.g. another mapping's document) is tolerated, not a decode failure
      assertNull(decoded.failure(), "version " + version);
    }
  }

  /** A reserved PublisherId type (ExtendedFlags1 bits 0-2 >= 101) drops the message. */
  @Test
  void reservedPublisherIdTypeYieldsEmptyResult() {
    byte[] message =
        bytes(
            0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
            0x05, // ExtendedFlags1: PublisherId type 101 = Reserved
            0x07, 0x01, 0x01, 0x00); // (undecodable from here)

    DecodedNetworkMessage decoded = decode(message);

    assertNull(decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
  }

  /** A null String PublisherId is invalid (§6.2.7.1) and drops the message. */
  @Test
  void nullStringPublisherIdYieldsEmptyResult() {
    byte[] message =
        bytes(
            0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
            0x04, // ExtendedFlags1: PublisherId type 100 = String
            0xFF, 0xFF, 0xFF, 0xFF, // String length = -1 (null)
            0x01, 0x01, 0x00); // (payload never reached)

    DecodedNetworkMessage decoded = decode(message);

    assertNull(decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
  }

  /** Reserved GroupFlags bits (4-7) drop the whole NetworkMessage. */
  @Test
  void reservedGroupFlagsYieldEmptyResult() {
    byte[] message =
        bytes(
            0x21, // byte 0: version 1 | GroupHeader
            0x10, // GroupFlags: reserved bit 4 set
            0x01, 0x00); // (undecodable from here)

    DecodedNetworkMessage decoded = decode(message);

    assertNull(decoded.writerGroupId());
    assertTrue(decoded.messages().isEmpty());
  }

  /** Reserved ExtendedFlags2 bits (6-7) drop the whole NetworkMessage. */
  @Test
  void reservedExtendedFlags2BitsYieldEmptyResult() {
    for (int flags2 : new int[] {0x40, 0x80}) {
      byte[] message =
          bytes(
              0x81, // byte 0: version 1 | ExtendedFlags1
              0x80, // ExtendedFlags1: ExtendedFlags2 present
              flags2, // ExtendedFlags2: reserved bit set
              0x01, 0x01, 0x00);

      DecodedNetworkMessage decoded = decode(message);

      assertTrue(decoded.messages().isEmpty(), "flags2 " + flags2);
    }
  }

  /** A reserved NetworkMessage type (ExtendedFlags2 bits 2-4 >= 011) drops the message. */
  @Test
  void reservedNetworkMessageTypeYieldsEmptyResult() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x0C, // ExtendedFlags2: NetworkMessage type 011 = Reserved
            0x01, 0x01, 0x00);

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());
  }

  /** Deterministic pseudo-random garbage never raises an exception out of the decoder. */
  @Test
  void garbageBytesNeverThrow() {
    var random = new Random(0x5EED);

    for (int i = 0; i < 256; i++) {
      byte[] garbage = new byte[random.nextInt(64)];
      random.nextBytes(garbage);

      DecodedNetworkMessage decoded = decode(garbage);

      assertNotNull(decoded, "iteration " + i);
    }
  }

  /** Fixed garbage with an unsupported version nibble yields an empty result. */
  @Test
  void fixedGarbageYieldsEmptyResult() {
    byte[] garbage = bytes(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF); // version nibble 15

    DecodedNetworkMessage decoded = decode(garbage);

    assertNull(decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  // endregion

  // region discovery skips

  /** Discovery probes (NetworkMessage type 001) are not supported and are skipped. */
  @Test
  void discoveryProbeIsSkipped() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x04, // ExtendedFlags2: NetworkMessage type 001 = discovery probe
            0x02, 0x00, 0x00); // (probe payload, not decoded)

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  /** Discovery announcement types other than DataSetMetaData (2) are skipped. */
  @Test
  void unknownAnnouncementTypeIsSkipped() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x08, // ExtendedFlags2: NetworkMessage type 010 = discovery announcement
            0x01, // AnnouncementType = 1 (PublisherEndpoints, unsupported)
            0x01, 0x00, // announcement SequenceNumber = 1
            0x00, 0x00, 0x00, 0x00); // (announcement payload, not decoded)

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  /** A discovery probe DataSetWriterIds count overrunning the buffer surfaces a failure. */
  @Test
  void discoveryProbeWriterIdsCountExceedingBufferSurfacesFailure() {
    byte[] message =
        bytes(
            0x91, // byte 0: version 1 | PublisherId 0x10 | ExtendedFlags1 0x80
            0x80, // ExtendedFlags1: ExtendedFlags2 present (PublisherId type Byte)
            0x04, // ExtendedFlags2: NetworkMessage type 001 = discovery probe
            0x07, // PublisherId: Byte = 7
            0x01, // ProbeType = 1 (PublisherInformation)
            0x02, // InformationType = 2 (DataSetMetaData)
            0x10, 0x00, 0x00, 0x00, // DataSetWriterIds count = 16: more than the buffer holds
            0x01, 0x00); // only one UInt16 follows

    DecodedNetworkMessage decoded = decode(message);

    assertEquals(PublisherId.ubyte(ubyte(7)), decoded.publisherId());
    assertTrue(decoded.messages().isEmpty());

    // the count/buffer mismatch is a truncation signature and is surfaced as a failure
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /** Discovery messages shall not carry a PayloadHeader; such a message is dropped. */
  @Test
  void discoveryAnnouncementWithPayloadHeaderIsDropped() {
    byte[] message =
        bytes(
            0xC1, // byte 0: version 1 | PayloadHeader 0x40 | ExtendedFlags1 0x80
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x08, // ExtendedFlags2: NetworkMessage type 010 = discovery announcement
            0x02, 0x01, 0x00); // (announcement bytes, never reached)

    DecodedNetworkMessage decoded = decode(message);

    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
  }

  // endregion

  // region no-payload-header rule

  /**
   * Without a PayloadHeader the payload is assumed to contain exactly ONE DataSetMessage; the bytes
   * of additional messages encoded back-to-back are not interpreted.
   */
  @Test
  void noPayloadHeaderDecodesSingleDataSetMessage() throws UaException {
    UadpDataSetMessageContentMask dataSetMask = new UadpDataSetMessageContentMask(uint(0));
    DataSetWriterConfig writer1 = writer(1, dataSetMask);
    DataSetWriterConfig writer2 = writer(2, dataSetMask);

    // NM mask 0x01: PublisherId only, no PayloadHeader.
    WriterGroupConfig group =
        group(new UadpNetworkMessageContentMask(uint(0x01)), writer1, writer2);

    EncodeContext context =
        new EncodeContext(
            encodingContext,
            PublisherId.ubyte(ubyte(1)),
            group,
            uint(0),
            ushort(1),
            ushort(1),
            null,
            List.of(
                keyFrame(writer1, 1, goodValue(Variant.ofInt32(42))),
                keyFrame(writer2, 2, goodValue(Variant.ofInt32(43)))));

    DecodedNetworkMessage decoded = decode(encodeToBytes(context));

    assertEquals(1, decoded.messages().size());

    DecodedDataSetMessage dsm = decoded.messages().get(0);
    assertNull(dsm.dataSetWriterId());
    assertEquals(List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))), dsm.fields());
  }

  // endregion

  // region helpers

  /**
   * Encode a "rich" NetworkMessage exercising every supported header: PublisherId, full
   * GroupHeader, PayloadHeader with two writers and Sizes, Timestamp, PicoSeconds, DataSetClassId,
   * plus full DataSetMessage headers and DataValue-encoded fields.
   */
  private byte[] richEncodedMessage() throws UaException {
    // NM mask 0x3FF: all bits 0-9 (everything except PromotedFields).
    // DSM mask 0x3F: all six optional header fields.
    // Field mask 0x1F: DataValue encoding with all five members.
    UadpDataSetMessageContentMask dataSetMask = new UadpDataSetMessageContentMask(uint(0x3F));
    DataSetFieldContentMask fieldMask = new DataSetFieldContentMask(uint(0x1F));

    DataSetWriterConfig writer1 = writer(1, dataSetMask, fieldMask);
    DataSetWriterConfig writer2 = writer(2, dataSetMask, fieldMask);
    WriterGroupConfig group =
        group(new UadpNetworkMessageContentMask(uint(0x3FF)), writer1, writer2);

    DataValue field =
        new DataValue(
            Variant.ofString("value"),
            new StatusCode(0x40950000L),
            new DateTime(101L),
            ushort(1),
            new DateTime(201L),
            ushort(2));

    DataSetMessageDraft draft1 =
        new DataSetMessageDraft(
            writer1,
            ushort(7),
            new DateTime(1_000L),
            new StatusCode(0x40950000L),
            new ConfigurationVersionDataType(uint(5), uint(6)),
            false,
            List.of(field));
    DataSetMessageDraft draft2 =
        new DataSetMessageDraft(
            writer2,
            ushort(8),
            new DateTime(2_000L),
            null,
            new ConfigurationVersionDataType(uint(5), uint(6)),
            false,
            List.of(field));

    EncodeContext context =
        new EncodeContext(
            encodingContext,
            PublisherId.uint64(ulong(99)),
            group,
            uint(0x01020304L),
            ushort(1),
            ushort(2),
            new DateTime(3_000L),
            List.of(draft1, draft2));

    return encodeToBytes(context);
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }

  private static DataValue goodValue(Variant value) {
    return new DataValue(value, StatusCode.GOOD, null, null, null, null);
  }

  private static DataSetWriterConfig writer(
      int dataSetWriterId, UadpDataSetMessageContentMask dataSetMessageContentMask) {
    return writer(dataSetWriterId, dataSetMessageContentMask, new DataSetFieldContentMask(uint(0)));
  }

  private static DataSetWriterConfig writer(
      int dataSetWriterId,
      UadpDataSetMessageContentMask dataSetMessageContentMask,
      DataSetFieldContentMask fieldContentMask) {

    return DataSetWriterConfig.builder("writer-" + dataSetWriterId)
        .dataSet(new PublishedDataSetRef("ds"))
        .dataSetWriterId(ushort(dataSetWriterId))
        .fieldContentMask(fieldContentMask)
        .settings(
            UadpDataSetWriterSettings.builder()
                .dataSetMessageContentMask(dataSetMessageContentMask)
                .build())
        .build();
  }

  private static WriterGroupConfig group(
      UadpNetworkMessageContentMask networkMessageContentMask, DataSetWriterConfig... writers) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder("group")
            .writerGroupId(ushort(258))
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(networkMessageContentMask)
                    .build());
    for (DataSetWriterConfig writer : writers) {
      builder.dataSetWriter(writer);
    }
    return builder.build();
  }

  private static DataSetMessageDraft keyFrame(
      DataSetWriterConfig writer, int sequenceNumber, DataValue... fields) {

    return new DataSetMessageDraft(
        writer,
        ushort(sequenceNumber),
        null,
        null,
        new ConfigurationVersionDataType(uint(0), uint(0)),
        false,
        List.of(fields));
  }

  private byte[] encodeToBytes(EncodeContext context) throws UaException {
    List<EncodedNetworkMessage> encoded = new UadpMessageMapping().encode(context);
    assertEquals(1, encoded.size(), "UADP encode returns a singleton list");
    try {
      return ByteBufUtil.getBytes(encoded.get(0).data());
    } finally {
      encoded.get(0).data().release();
    }
  }

  private DecodedNetworkMessage decode(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping().decode(new DecodeContext(encodingContext), buffer);
    } finally {
      buffer.release();
    }
  }

  // endregion
}
