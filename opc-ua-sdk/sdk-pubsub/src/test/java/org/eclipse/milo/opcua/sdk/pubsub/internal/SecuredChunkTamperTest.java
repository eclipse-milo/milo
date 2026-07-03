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
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodeContext;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedDataSetMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedField;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.SecurityContextResolver;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.SecurityOutcome;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpDecodedMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.UadpMessageMapping;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;

/**
 * The composed N20 row (K19 × K5): a secured multi-chunk set with one tampered middle chunk is
 * discarded as a WHOLE — the tampered chunk fails verification ({@code INVALID_SIGNATURE},
 * header-only, no chunk surfaced), so it is never fed to the {@link ChunkReassembler} (mirroring
 * the dispatcher, which drops every non-{@code VERIFIED} security outcome before reassembly) and
 * its set can never complete — while a subsequent clean set with a newer MessageSequenceNumber
 * reassembles and decodes fine.
 *
 * <p>Chunk construction is hand-derived per Part 14 Tables 158/159 with {@code javax.crypto}
 * directly (chunk EMISSION is out of scope, so there is no Milo encoder to produce these bytes);
 * the per-chunk security envelope matches the worked construction in {@code UadpChunkDecodeTest}.
 */
class SecuredChunkTamperTest {

  private static final PubSubSecurityPolicy POLICY = PubSubSecurityPolicy.PubSubAes256Ctr;

  private static final int TOKEN_ID = 7;

  /** One valid key-frame DataSetMessage: 1 field, Variant Int32. Split across three chunks. */
  private static byte[] dataSetMessage(int value) {
    return bytes(0x01, 0x01, 0x00, 0x06, value & 0xFF, 0x00, 0x00, 0x00);
  }

  private final UadpMessageMapping mapping = new UadpMessageMapping();
  private final DecodeContext context = DecodeContext.of(new DefaultEncodingContext(), resolver());

  private final ChunkReassembler reassembler = new ChunkReassembler();

  @Test
  void tamperedMiddleChunkDiscardsOnlyItsSetAndACleanSetStillReassembles() throws Exception {
    byte[] payload = dataSetMessage(42);

    // the tampered set: MessageSequenceNumber 7, three chunks covering [0,3) [3,5) [5,8)
    byte[] chunk1 = securedChunk(1, 7, 0, payload.length, Arrays.copyOfRange(payload, 0, 3), 1);
    byte[] chunk2 = securedChunk(2, 7, 3, payload.length, Arrays.copyOfRange(payload, 3, 5), 2);
    byte[] chunk3 = securedChunk(3, 7, 5, payload.length, Arrays.copyOfRange(payload, 5, 8), 3);

    // tamper one ciphertext byte of the MIDDLE chunk (inside the encrypted payload region)
    byte[] tampered = chunk2.clone();
    tampered[headerLength() + 1] ^= 0x01;

    // first chunk verifies, decrypts, surfaces, and starts the stream
    DecodedNetworkMessage first = decode(chunk1);
    assertEquals(SecurityOutcome.VERIFIED, security(first));
    assertNotNull(first.chunk());
    assertNull(reassembler.accept(first, 0));
    assertEquals(1, reassembler.streamCount());

    // the tampered middle chunk fails verification: header-only, NO chunk surfaced — the
    // dispatcher counts it as invalidSignatureMessages and never feeds reassembly
    DecodedNetworkMessage middle = decode(tampered);
    assertEquals(SecurityOutcome.INVALID_SIGNATURE, security(middle));
    assertNull(middle.chunk());
    assertTrue(middle.messages().isEmpty());
    assertNull(middle.failure());

    // the trailing clean chunk still verifies, but the set stays incomplete forever: no
    // reassembly completion for the tampered stream
    DecodedNetworkMessage last = decode(chunk3);
    assertEquals(SecurityOutcome.VERIFIED, security(last));
    assertNull(reassembler.accept(last, 0));
    assertEquals(1, reassembler.streamCount());

    // a subsequent CLEAN set (newer MessageSequenceNumber 8) discards the incomplete
    // predecessor and reassembles fine
    byte[] cleanPayload = dataSetMessage(43);
    byte[] clean1 =
        securedChunk(4, 8, 0, cleanPayload.length, Arrays.copyOfRange(cleanPayload, 0, 3), 4);
    byte[] clean2 =
        securedChunk(5, 8, 3, cleanPayload.length, Arrays.copyOfRange(cleanPayload, 3, 5), 5);
    byte[] clean3 =
        securedChunk(6, 8, 5, cleanPayload.length, Arrays.copyOfRange(cleanPayload, 5, 8), 6);

    assertNull(reassembler.accept(decode(clean1), 0));
    assertNull(reassembler.accept(decode(clean2), 0));

    DecodedNetworkMessage completing = decode(clean3);
    ChunkReassembler.ReassembledMessage reassembled = reassembler.accept(completing, 0);

    assertNotNull(reassembled);
    assertEquals(0, reassembler.streamCount());
    assertArrayEquals(cleanPayload, reassembled.payload());
    assertEquals(ushort(5), reassembled.dataSetWriterId());

    // the reassembled payload re-enters decode and yields the clean DataSetMessage, with the
    // completing chunk's VERIFIED security inherited
    ByteBuf reassembledPayload = Unpooled.wrappedBuffer(reassembled.payload());
    try {
      DecodedNetworkMessage decoded =
          mapping.decodeReassembled(
              context, reassembled.header(), reassembled.dataSetWriterId(), reassembledPayload);

      assertNull(decoded.failure());
      assertEquals(reassembled.header().security(), decoded.security());

      assertEquals(1, decoded.messages().size());
      DecodedDataSetMessage message = decoded.messages().get(0);
      assertEquals(ushort(5), message.dataSetWriterId());
      assertTrue(message.valid());
      assertEquals(
          List.of(
              new DecodedField(
                  0, new DataValue(Variant.ofInt32(43), StatusCode.GOOD, null, null, null, null))),
          message.fields());
    } finally {
      reassembledPayload.release();
    }
  }

  // region secured chunk construction (Part 14 Tables 154/158/159, javax.crypto directly)

  /**
   * The plaintext header of every chunk: UADPFlags | ExtendedFlags1 | ExtendedFlags2 | GroupHeader
   * (WriterGroupId 258, NetworkMessage SequenceNumber) | chunk PayloadHeader (DataSetWriterId 5) |
   * SecurityHeader (signed+encrypted, token {@value #TOKEN_ID}, 8-byte nonce).
   */
  private static int headerLength() {
    return 3 + 1 + 2 + 2 + 2 + 1 + 4 + 1 + 8;
  }

  private static byte[] securedChunk(
      int networkMessageSequence,
      int messageSequenceNumber,
      int chunkOffset,
      int totalSize,
      byte[] chunkData,
      int nonceSequence)
      throws Exception {

    byte[] messageNonce = bytes(0xB1, 0xB2, 0xB3, 0xB4, nonceSequence & 0xFF, 0x00, 0x00, 0x00);

    byte[] header =
        bytes(
            0xE1, // byte 0: version 1 | GroupHeader 0x20 | PayloadHeader 0x40 | ExtFlags1 0x80
            0x90, // ExtendedFlags1: SecurityHeader 0x10 | ExtendedFlags2 present 0x80
            0x01, // ExtendedFlags2: chunk, type Data
            0x09, // GroupFlags: WriterGroupId | SequenceNumber
            0x02,
            0x01, // WriterGroupId = 258
            networkMessageSequence & 0xFF,
            (networkMessageSequence >> 8) & 0xFF, // NetworkMessage SequenceNumber
            0x05,
            0x00, // PayloadHeader (chunk form, Table 158): DataSetWriterId = 5
            0x03, // SecurityFlags: signed | encrypted
            TOKEN_ID,
            0x00,
            0x00,
            0x00, // SecurityTokenId UInt32 LE
            0x08, // NonceLength = 8
            messageNonce[0],
            messageNonce[1],
            messageNonce[2],
            messageNonce[3],
            messageNonce[4],
            messageNonce[5],
            messageNonce[6],
            messageNonce[7]);

    byte[] chunkPayload =
        concat(
            bytes(
                messageSequenceNumber & 0xFF,
                (messageSequenceNumber >> 8) & 0xFF, // MessageSequenceNumber (Table 159)
                chunkOffset & 0xFF,
                (chunkOffset >> 8) & 0xFF,
                (chunkOffset >> 16) & 0xFF,
                (chunkOffset >> 24) & 0xFF, // ChunkOffset
                totalSize & 0xFF,
                (totalSize >> 8) & 0xFF,
                (totalSize >> 16) & 0xFF,
                (totalSize >> 24) & 0xFF, // TotalSize
                chunkData.length & 0xFF,
                (chunkData.length >> 8) & 0xFF,
                (chunkData.length >> 16) & 0xFF,
                (chunkData.length >> 24) & 0xFF), // ChunkData length
            chunkData);

    // each chunk is its OWN secured NetworkMessage: encrypt the Table 159 fields, sign the whole
    byte[] encrypted = ctr(chunkPayload, messageNonce);
    byte[] unsigned = concat(header, encrypted);
    byte[] signature = hmac(unsigned);

    return concat(unsigned, signature);
  }

  /** AES-256-CTR with IV = KeyNonce(4) || MessageNonce(8) || 00 00 00 01 (Table 157). */
  private static byte[] ctr(byte[] data, byte[] messageNonce) throws Exception {
    byte[] keyData = keyData();
    byte[] encryptingKey = Arrays.copyOfRange(keyData, 32, 64);
    byte[] keyNonce = Arrays.copyOfRange(keyData, 64, 68);

    byte[] iv = new byte[16];
    System.arraycopy(keyNonce, 0, iv, 0, 4);
    System.arraycopy(messageNonce, 0, iv, 4, 8);
    iv[15] = 0x01;

    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(
        Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptingKey, "AES"), new IvParameterSpec(iv));
    return cipher.doFinal(data);
  }

  /** HMAC-SHA256 over the whole (encrypted) NetworkMessage with SigningKey = keyData[0, 32). */
  private static byte[] hmac(byte[] data) throws Exception {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(Arrays.copyOfRange(keyData(), 0, 32), "HmacSHA256"));
    return mac.doFinal(data);
  }

  /** The fixed 68-byte key data, {@code 01 02 03 ...} (Table 155 layout). */
  private static byte[] keyData() {
    byte[] keyData = new byte[POLICY.getKeyDataLength()];
    for (int i = 0; i < keyData.length; i++) {
      keyData[i] = (byte) (i + 1);
    }
    return keyData;
  }

  private static SecurityContextResolver resolver() {
    SecurityKeyMaterial keys;
    try {
      keys = SecurityKeyMaterial.split(POLICY, ByteString.of(keyData()));
    } catch (Exception e) {
      throw new AssertionError(e);
    }
    return request -> new SecurityContextResolver.Resolution.Keys(keys);
  }

  // endregion

  // region helpers

  private DecodedNetworkMessage decode(byte[] message) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      UadpDecodedMessage decoded = mapping.decodeMessage(context, buffer);
      return assertInstanceOf(DecodedNetworkMessage.class, decoded);
    } finally {
      buffer.release();
    }
  }

  private static SecurityOutcome security(DecodedNetworkMessage decoded) {
    assertNotNull(decoded.security());
    return decoded.security().outcome();
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

  // endregion
}
