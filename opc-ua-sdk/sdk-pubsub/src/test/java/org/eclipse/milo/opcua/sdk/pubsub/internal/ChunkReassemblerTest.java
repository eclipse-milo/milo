/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedChunk;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDecodedMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ChunkReassembler}: out-of-order assembly, duplicate ranges, stream
 * interleaving and identity, the newer-MessageSequenceNumber discard rule (§7.2.4.4.4), and the DoS
 * caps (TotalSize, stream count, idle eviction) with a deterministic injected clock — plus the
 * decode → reassemble → {@link UadpMessageMapping#decodeReassembled} re-entry pipeline: the
 * reassembled result carries {@code sequenceNumber == null} (every chunk NetworkMessage already
 * consumed its own), and chunk bytes are copies that survive the source datagram's release.
 */
class ChunkReassemblerTest {

  private static final PublisherId PUBLISHER_A = PublisherId.uint16(ushort(1));
  private static final PublisherId PUBLISHER_B = PublisherId.uint16(ushort(2));

  private final ChunkReassembler reassembler = new ChunkReassembler();

  private long nowNanos = 0;

  @Test
  void inOrderChunksComplete() {
    assertNull(accept(PUBLISHER_A, 1, 7, 0, 6, bytes(1, 2, 3, 4)));
    ChunkReassembler.ReassembledMessage reassembled = accept(PUBLISHER_A, 1, 7, 4, 6, bytes(5, 6));

    assertNotNull(reassembled);
    assertEquals(ushort(1), reassembled.dataSetWriterId());
    assertArrayEquals(bytes(1, 2, 3, 4, 5, 6), reassembled.payload());
    assertEquals(0, reassembler.streamCount());
  }

  @Test
  void outOfOrderChunksComplete() {
    assertNull(accept(PUBLISHER_A, 1, 7, 4, 6, bytes(5, 6)));
    ChunkReassembler.ReassembledMessage reassembled =
        accept(PUBLISHER_A, 1, 7, 0, 6, bytes(1, 2, 3, 4));

    assertNotNull(reassembled);
    assertArrayEquals(bytes(1, 2, 3, 4, 5, 6), reassembled.payload());
  }

  @Test
  void singleChunkCoveringWholePayloadCompletesImmediately() {
    ChunkReassembler.ReassembledMessage reassembled =
        accept(PUBLISHER_A, 1, 7, 0, 3, bytes(9, 8, 7));

    assertNotNull(reassembled);
    assertArrayEquals(bytes(9, 8, 7), reassembled.payload());
  }

  @Test
  void duplicateRangeOverwritesAndDoesNotDoubleCount() {
    assertNull(accept(PUBLISHER_A, 1, 7, 0, 6, bytes(1, 2, 3, 4)));
    // the duplicate range alone must not complete the 6-byte payload
    assertNull(accept(PUBLISHER_A, 1, 7, 0, 6, bytes(9, 9, 9, 9)));

    ChunkReassembler.ReassembledMessage reassembled = accept(PUBLISHER_A, 1, 7, 4, 6, bytes(5, 6));

    assertNotNull(reassembled);
    assertArrayEquals(bytes(9, 9, 9, 9, 5, 6), reassembled.payload());
  }

  @Test
  void interleavedStreamsReassembleIndependently() {
    assertNull(accept(PUBLISHER_A, 1, 7, 0, 6, bytes(1, 2, 3, 4)));
    assertNull(accept(PUBLISHER_B, 2, 3, 0, 4, bytes(7, 8)));
    assertEquals(2, reassembler.streamCount());

    ChunkReassembler.ReassembledMessage b = accept(PUBLISHER_B, 2, 3, 2, 4, bytes(9, 10));
    assertNotNull(b);
    assertArrayEquals(bytes(7, 8, 9, 10), b.payload());

    ChunkReassembler.ReassembledMessage a = accept(PUBLISHER_A, 1, 7, 4, 6, bytes(5, 6));
    assertNotNull(a);
    assertArrayEquals(bytes(1, 2, 3, 4, 5, 6), a.payload());
  }

  /** A chunk with a NEWER MessageSequenceNumber abandons the incomplete predecessor. */
  @Test
  void newerSequenceNumberDiscardsInProgressPayload() {
    assertNull(accept(PUBLISHER_A, 1, 7, 0, 6, bytes(1, 2, 3, 4)));

    // seq 8 starts a new 2-byte payload; the incomplete seq-7 payload is discarded
    ChunkReassembler.ReassembledMessage reassembled = accept(PUBLISHER_A, 1, 8, 0, 2, bytes(5, 6));

    assertNotNull(reassembled);
    assertArrayEquals(bytes(5, 6), reassembled.payload());

    // the seq-7 completing chunk now starts over instead of completing
    assertNull(accept(PUBLISHER_A, 1, 7, 4, 6, bytes(5, 6)));
  }

  /** An OLDER MessageSequenceNumber chunk is dropped; the in-progress payload survives. */
  @Test
  void olderSequenceNumberChunkIsDropped() {
    assertNull(accept(PUBLISHER_A, 1, 7, 0, 6, bytes(1, 2, 3, 4)));
    assertNull(accept(PUBLISHER_A, 1, 6, 0, 2, bytes(9, 9)));

    ChunkReassembler.ReassembledMessage reassembled = accept(PUBLISHER_A, 1, 7, 4, 6, bytes(5, 6));

    assertNotNull(reassembled);
    assertArrayEquals(bytes(1, 2, 3, 4, 5, 6), reassembled.payload());
  }

  /** The §7.2.3 N=16 wrap: sequence 0 is NEWER than 0xFFFF and discards its payload. */
  @Test
  void sequenceNumberWrapClassifiesAsNewer() {
    assertNull(accept(PUBLISHER_A, 1, 0xFFFF, 0, 6, bytes(1, 2, 3, 4)));

    ChunkReassembler.ReassembledMessage reassembled = accept(PUBLISHER_A, 1, 0, 0, 2, bytes(5, 6));

    assertNotNull(reassembled);
    assertArrayEquals(bytes(5, 6), reassembled.payload());
  }

  @Test
  void totalSizeAboveCapIsDropped() {
    assertNull(accept(PUBLISHER_A, 1, 7, 0, ChunkReassembler.MAX_TOTAL_SIZE + 1L, bytes(1, 2, 3)));
    assertEquals(0, reassembler.streamCount());
  }

  @Test
  void rangeBeyondTotalSizeIsDropped() {
    // offset + length exceeds TotalSize: bounds-checked before any copy
    assertNull(accept(PUBLISHER_A, 1, 7, 4, 6, bytes(1, 2, 3, 4)));
    assertEquals(0, reassembler.streamCount());
  }

  @Test
  void idleStreamsAreEvicted() {
    assertNull(accept(PUBLISHER_A, 1, 7, 0, 6, bytes(1, 2, 3, 4)));
    assertEquals(1, reassembler.streamCount());

    // another stream's chunk arriving 11 seconds later sweeps the idle payload
    nowNanos += TimeUnit.SECONDS.toNanos(11);
    assertNull(accept(PUBLISHER_B, 2, 3, 0, 6, bytes(1, 2, 3, 4)));
    assertEquals(1, reassembler.streamCount());

    // the evicted stream's completing chunk starts a new (incomplete) payload
    assertNull(accept(PUBLISHER_A, 1, 7, 4, 6, bytes(5, 6)));
  }

  @Test
  void streamCapEvictsLeastRecentlyActive() {
    for (int i = 0; i < ChunkReassembler.MAX_STREAMS; i++) {
      nowNanos += 1;
      assertNull(accept(PublisherId.uint16(ushort(1000 + i)), 1, 7, 0, 6, bytes(1, 2, 3, 4)));
    }
    assertEquals(ChunkReassembler.MAX_STREAMS, reassembler.streamCount());

    // one more stream evicts the least recently active (the first) instead of growing
    nowNanos += 1;
    assertNull(accept(PUBLISHER_B, 2, 3, 0, 6, bytes(1, 2, 3, 4)));
    assertEquals(ChunkReassembler.MAX_STREAMS, reassembler.streamCount());

    // the evicted stream's completing chunk no longer completes
    assertNull(accept(PublisherId.uint16(ushort(1000)), 1, 7, 4, 6, bytes(5, 6)));
  }

  /** The payload of the reassembled message is the assembly buffer, not an alias of any chunk. */
  @Test
  void reassembledPayloadDoesNotAliasChunkData() {
    byte[] chunkData = bytes(1, 2, 3);
    ChunkReassembler.ReassembledMessage reassembled = accept(PUBLISHER_A, 1, 7, 0, 3, chunkData);

    assertNotNull(reassembled);
    chunkData[0] = 99;
    assertArrayEquals(bytes(1, 2, 3), reassembled.payload());
  }

  /**
   * The full inbound pipeline: two decoded chunk NetworkMessages reassemble into one
   * DataSetMessage, and the {@link UadpMessageMapping#decodeReassembled} re-entry result carries
   * {@code sequenceNumber == null} — each chunk NetworkMessage already consumed its own
   * NetworkMessage sequence number through the per-reader recency windows, so the reassembled
   * message must not observe another one — with the header values and security inherited from the
   * completing chunk, and the DataSetMessage decoded from the reassembled payload.
   */
  @Test
  void reassembledReEntryDecodesWithNullSequenceNumber() {
    var mapping = new UadpMessageMapping();
    DecodeContext context = DecodeContext.of(new DefaultEncodingContext());

    // the reassembled payload: ONE DataSetMessage — valid key frame, Variant encoding, Int32 42
    byte[] dataSetMessage = bytes(0x01, 0x01, 0x00, 0x06, 0x2A, 0x00, 0x00, 0x00);

    DecodedNetworkMessage first =
        decodeChunkMessage(
            mapping,
            context,
            chunkNetworkMessage(
                16, 7, 0, dataSetMessage.length, Arrays.copyOfRange(dataSetMessage, 0, 4)));
    DecodedNetworkMessage second =
        decodeChunkMessage(
            mapping,
            context,
            chunkNetworkMessage(
                17, 7, 4, dataSetMessage.length, Arrays.copyOfRange(dataSetMessage, 4, 8)));

    // the chunk NetworkMessages themselves carry their own NM sequence numbers
    assertEquals(ushort(16), first.sequenceNumber());
    assertEquals(ushort(17), second.sequenceNumber());

    assertNull(reassembler.accept(first, 0));
    ChunkReassembler.ReassembledMessage reassembled = reassembler.accept(second, 0);
    assertNotNull(reassembled);
    assertArrayEquals(dataSetMessage, reassembled.payload());
    assertEquals(ushort(5), reassembled.dataSetWriterId());

    ByteBuf payload = Unpooled.wrappedBuffer(reassembled.payload());
    try {
      DecodedNetworkMessage decoded =
          mapping.decodeReassembled(
              context, reassembled.header(), reassembled.dataSetWriterId(), payload);

      assertNull(decoded.sequenceNumber());
      assertNull(decoded.chunk());
      assertNull(decoded.failure());
      assertEquals(ushort(258), decoded.writerGroupId());
      assertEquals(reassembled.header().security(), decoded.security());

      assertEquals(1, decoded.messages().size());
      DecodedDataSetMessage message = decoded.messages().get(0);
      assertEquals(ushort(5), message.dataSetWriterId());
      assertTrue(message.valid());
      assertEquals(
          List.of(
              new DecodedField(
                  0, new DataValue(Variant.ofInt32(42), StatusCode.GOOD, null, null, null, null))),
          message.fields());
    } finally {
      payload.release();
    }
  }

  /**
   * {@link DecodedChunk#chunkData()} is a copy: the source datagram buffer does not outlive the
   * dispatch call, so zeroing and releasing it before reassembly must not affect the payload.
   */
  @Test
  void chunkDataIsIndependentOfTheSourceBuffer() {
    var mapping = new UadpMessageMapping();
    DecodeContext context = DecodeContext.of(new DefaultEncodingContext());

    byte[] payloadBytes = bytes(1, 2, 3, 4, 5, 6);

    DecodedNetworkMessage first;
    ByteBuf source1 =
        Unpooled.buffer()
            .writeBytes(chunkNetworkMessage(16, 7, 0, 6, Arrays.copyOfRange(payloadBytes, 0, 4)));
    try {
      first = decodeChunkMessage(mapping, context, source1);
    } finally {
      // the datagram does not outlive the dispatch call: corrupt it, then release it
      source1.setZero(0, source1.capacity());
      source1.release();
    }

    DecodedNetworkMessage second;
    ByteBuf source2 =
        Unpooled.buffer()
            .writeBytes(chunkNetworkMessage(17, 7, 4, 6, Arrays.copyOfRange(payloadBytes, 4, 6)));
    try {
      second = decodeChunkMessage(mapping, context, source2);
    } finally {
      source2.setZero(0, source2.capacity());
      source2.release();
    }

    assertNull(reassembler.accept(first, 0));
    ChunkReassembler.ReassembledMessage reassembled = reassembler.accept(second, 0);

    assertNotNull(reassembled);
    assertArrayEquals(payloadBytes, reassembled.payload());
  }

  /**
   * One data-plane Chunk NetworkMessage (Part 14 Tables 158/159): GroupHeader with WriterGroupId
   * 258 and the given NetworkMessage SequenceNumber, chunk PayloadHeader DataSetWriterId 5, mode
   * None.
   */
  private static byte[] chunkNetworkMessage(
      int networkMessageSequence,
      int messageSequenceNumber,
      int chunkOffset,
      int totalSize,
      byte[] chunkData) {

    byte[] header =
        bytes(
            0xE1, // byte 0: version 1 | GroupHeader 0x20 | PayloadHeader 0x40 | ExtFlags1 0x80
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk, type Data
            0x09, // GroupFlags: WriterGroupId | SequenceNumber
            0x02,
            0x01, // WriterGroupId = 258
            networkMessageSequence & 0xFF,
            (networkMessageSequence >> 8) & 0xFF,
            0x05,
            0x00, // PayloadHeader (chunk form, Table 158): DataSetWriterId = 5
            messageSequenceNumber & 0xFF,
            (messageSequenceNumber >> 8) & 0xFF,
            chunkOffset & 0xFF,
            (chunkOffset >> 8) & 0xFF,
            (chunkOffset >> 16) & 0xFF,
            (chunkOffset >> 24) & 0xFF,
            totalSize & 0xFF,
            (totalSize >> 8) & 0xFF,
            (totalSize >> 16) & 0xFF,
            (totalSize >> 24) & 0xFF,
            chunkData.length & 0xFF,
            (chunkData.length >> 8) & 0xFF,
            (chunkData.length >> 16) & 0xFF,
            (chunkData.length >> 24) & 0xFF);

    byte[] message = Arrays.copyOf(header, header.length + chunkData.length);
    System.arraycopy(chunkData, 0, message, header.length, chunkData.length);
    return message;
  }

  private static DecodedNetworkMessage decodeChunkMessage(
      UadpMessageMapping mapping, DecodeContext context, byte[] message) {

    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return decodeChunkMessage(mapping, context, buffer);
    } finally {
      buffer.release();
    }
  }

  private static DecodedNetworkMessage decodeChunkMessage(
      UadpMessageMapping mapping, DecodeContext context, ByteBuf buffer) {

    UadpDecodedMessage result = mapping.decodeMessage(context, buffer);
    DecodedNetworkMessage decoded = assertInstanceOf(DecodedNetworkMessage.class, result);
    assertNull(decoded.failure());
    assertNotNull(decoded.chunk());
    return decoded;
  }

  private ChunkReassembler.@Nullable ReassembledMessage accept(
      PublisherId publisherId,
      int dataSetWriterId,
      int messageSequenceNumber,
      long chunkOffset,
      long totalSize,
      byte[] chunkData) {

    var chunk =
        new DecodedChunk(
            ushort(dataSetWriterId),
            ushort(messageSequenceNumber),
            uint(chunkOffset),
            uint(totalSize),
            chunkData);

    DecodedNetworkMessage message =
        DecodedNetworkMessage.of(
            publisherId,
            ushort(100),
            null,
            null,
            ushort(1),
            null,
            List.of(),
            List.of(),
            null,
            null,
            chunk);

    return reassembler.accept(message, nowNanos);
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }
}
