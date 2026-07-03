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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Chunk NetworkMessage decode (Part 14 §7.2.4.4.4, Tables 158/159): the discovery-aware {@code
 * decodeMessage} surface parses the chunk fields — from inside the decrypted region when the
 * message is secured — and surfaces them as {@link DecodedNetworkMessage#chunk()}; the legacy
 * {@code decode} surface and chunked discovery messages keep the detect-and-drop {@code
 * Bad_NotSupported} failure.
 */
class UadpChunkDecodeTest {

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  /** A mode-None data chunk NM with PayloadHeader surfaces the chunk fields, copied. */
  @Test
  void dataChunkIsSurfacedOnDecodeMessage() {
    byte[] message =
        bytes(
            0xE1, // byte 0: version 1 | GroupHeader 0x20 | PayloadHeader 0x40 | ExtFlags1 0x80
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk, type Data
            0x09, // GroupFlags: WriterGroupId | SequenceNumber
            0x02, 0x01, // WriterGroupId = 258
            0x10, 0x00, // NetworkMessage SequenceNumber = 16
            0x05, 0x00, // PayloadHeader (chunk form, Table 158): DataSetWriterId = 5
            0x07, 0x00, // MessageSequenceNumber = 7
            0x04, 0x00, 0x00, 0x00, // ChunkOffset = 4
            0x0A, 0x00, 0x00, 0x00, // TotalSize = 10
            0x03, 0x00, 0x00, 0x00, // ChunkData length = 3
            0xAA, 0xBB, 0xCC); // ChunkData

    DecodedNetworkMessage decoded = decodeMessage(message);

    assertNull(decoded.failure());
    assertTrue(decoded.messages().isEmpty());
    assertEquals(ushort(258), decoded.writerGroupId());
    assertEquals(ushort(16), decoded.sequenceNumber());

    DecodedChunk chunk = decoded.chunk();
    assertNotNull(chunk);
    assertEquals(ushort(5), chunk.dataSetWriterId());
    assertEquals(ushort(7), chunk.messageSequenceNumber());
    assertEquals(uint(4), chunk.chunkOffset());
    assertEquals(uint(10), chunk.totalSize());
    assertArrayEquals(bytes(0xAA, 0xBB, 0xCC), chunk.chunkData());
  }

  /** Without a PayloadHeader the chunk has no DataSetWriterId. */
  @Test
  void dataChunkWithoutPayloadHeaderHasNullWriterId() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk
            0x01, 0x00, // MessageSequenceNumber = 1
            0x00, 0x00, 0x00, 0x00, // ChunkOffset = 0
            0x02, 0x00, 0x00, 0x00, // TotalSize = 2
            0x02, 0x00, 0x00, 0x00, // ChunkData length = 2
            0x11, 0x22); // ChunkData

    DecodedNetworkMessage decoded = decodeMessage(message);

    DecodedChunk chunk = decoded.chunk();
    assertNotNull(chunk);
    assertNull(chunk.dataSetWriterId());
    assertArrayEquals(bytes(0x11, 0x22), chunk.chunkData());
  }

  /** The legacy decode surface keeps the historic detect-and-drop failure. */
  @Test
  void legacySurfaceKeepsDetectAndDrop() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk
            0x01, 0x00, // MessageSequenceNumber = 1
            0x00, 0x00, 0x00, 0x00, // ChunkOffset = 0
            0x02, 0x00, 0x00, 0x00, // TotalSize = 2
            0x02, 0x00, 0x00, 0x00, // ChunkData length = 2
            0x11, 0x22); // ChunkData

    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      DecodedNetworkMessage decoded =
          new UadpMessageMapping().decode(DecodeContext.of(encodingContext), buffer);

      assertNull(decoded.chunk());
      assertNotNull(decoded.failure());
      assertEquals(StatusCodes.Bad_NotSupported, decoded.failure().statusCode().value());
    } finally {
      buffer.release();
    }
  }

  /** Chunked discovery messages stay detect-and-drop on every surface. */
  @Test
  void chunkedDiscoveryMessageStaysDetectAndDrop() {
    byte[] message =
        bytes(
            0x91, // byte 0: version 1 | PublisherId | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present (PublisherId type Byte)
            0x09, // ExtendedFlags2: chunk | type discovery announcement (bits 2-4 = 010)
            0x07, // PublisherId: Byte = 7
            0x01, 0x00, 0x00, 0x00); // (chunk data, not decoded)

    DecodedNetworkMessage decoded = decodeMessage(message);

    assertNull(decoded.chunk());
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_NotSupported, decoded.failure().statusCode().value());
  }

  /** A ChunkData length promising more bytes than arrived is a truncation signature. */
  @Test
  void chunkDataLengthExceedingBufferSurfacesFailure() {
    byte[] message =
        bytes(
            0x81, // byte 0: version 1 | ExtendedFlags1
            0x80, // ExtendedFlags1: ExtendedFlags2 present
            0x01, // ExtendedFlags2: chunk
            0x01, 0x00, // MessageSequenceNumber = 1
            0x00, 0x00, 0x00, 0x00, // ChunkOffset = 0
            0x10, 0x00, 0x00, 0x00, // TotalSize = 16
            0x0F, 0x00, 0x00, 0x00, // ChunkData length = 15: more than the buffer holds
            0x11, 0x22); // only 2 bytes follow

    DecodedNetworkMessage decoded = decodeMessage(message);

    assertNull(decoded.chunk());
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  /**
   * A SignAndEncrypt chunk NM: the Table 159 fields are inside the encrypted region, so the chunk
   * is only parseable after verification and decryption of the payload copy.
   */
  @Test
  void securedChunkIsVerifiedDecryptedAndSurfaced() throws Exception {
    byte[] keyData = new byte[68];
    for (int i = 0; i < keyData.length; i++) {
      keyData[i] = (byte) (i + 1);
    }
    byte[] signingKey = new byte[32];
    byte[] encryptingKey = new byte[32];
    byte[] keyNonce = new byte[4];
    System.arraycopy(keyData, 0, signingKey, 0, 32);
    System.arraycopy(keyData, 32, encryptingKey, 0, 32);
    System.arraycopy(keyData, 64, keyNonce, 0, 4);

    byte[] messageNonce = bytes(0xB1, 0xB2, 0xB3, 0xB4, 0x01, 0x00, 0x00, 0x00);

    byte[] header =
        bytes(
            0xC1, // byte 0: version 1 | PayloadHeader 0x40 | ExtFlags1 0x80
            0x90, // ExtendedFlags1: SecurityHeader 0x10 | ExtendedFlags2 present 0x80
            0x01, // ExtendedFlags2: chunk, type Data
            0x05, 0x00, // PayloadHeader (chunk form): DataSetWriterId = 5
            0x03, // SecurityFlags: signed | encrypted
            0x07, 0x00, 0x00, 0x00, // SecurityTokenId = 7
            0x08, // NonceLength = 8
            0xB1, 0xB2, 0xB3, 0xB4, 0x01, 0x00, 0x00, 0x00); // MessageNonce

    byte[] chunkPayload =
        bytes(
            0x07, 0x00, // MessageSequenceNumber = 7
            0x00, 0x00, 0x00, 0x00, // ChunkOffset = 0
            0x03, 0x00, 0x00, 0x00, // TotalSize = 3
            0x03, 0x00, 0x00, 0x00, // ChunkData length = 3
            0xAA, 0xBB, 0xCC); // ChunkData

    // encrypt the payload: AES-256-CTR, IV = KeyNonce || MessageNonce || 00 00 00 01
    byte[] iv = new byte[16];
    System.arraycopy(keyNonce, 0, iv, 0, 4);
    System.arraycopy(messageNonce, 0, iv, 4, 8);
    iv[15] = 0x01;
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(
        Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptingKey, "AES"), new IvParameterSpec(iv));
    byte[] encrypted = cipher.doFinal(chunkPayload);

    byte[] unsigned = new byte[header.length + encrypted.length];
    System.arraycopy(header, 0, unsigned, 0, header.length);
    System.arraycopy(encrypted, 0, unsigned, header.length, encrypted.length);

    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(signingKey, "HmacSHA256"));
    byte[] signature = mac.doFinal(unsigned);

    byte[] message = new byte[unsigned.length + signature.length];
    System.arraycopy(unsigned, 0, message, 0, unsigned.length);
    System.arraycopy(signature, 0, message, unsigned.length, signature.length);

    SecurityKeyMaterial keys =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes256Ctr, ByteString.of(keyData));
    SecurityContextResolver resolver = request -> new SecurityContextResolver.Resolution.Keys(keys);

    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      UadpDecodedMessage result =
          new UadpMessageMapping()
              .decodeMessage(DecodeContext.of(encodingContext, resolver), buffer);
      DecodedNetworkMessage decoded = assertInstanceOf(DecodedNetworkMessage.class, result);

      assertNull(decoded.failure());
      assertNotNull(decoded.security());
      assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
      assertEquals(MessageSecurityMode.SignAndEncrypt, decoded.security().mode());

      DecodedChunk chunk = decoded.chunk();
      assertNotNull(chunk);
      assertEquals(ushort(5), chunk.dataSetWriterId());
      assertEquals(ushort(7), chunk.messageSequenceNumber());
      assertEquals(uint(0), chunk.chunkOffset());
      assertEquals(uint(3), chunk.totalSize());
      assertArrayEquals(bytes(0xAA, 0xBB, 0xCC), chunk.chunkData());

      // the shared transport buffer was never mutated: the payload region is still ciphertext
      byte[] observed = new byte[encrypted.length];
      buffer.getBytes(header.length, observed);
      assertArrayEquals(encrypted, observed);
    } finally {
      buffer.release();
    }
  }

  private DecodedNetworkMessage decodeMessage(byte[] message) {
    return decodeMessage(message, null);
  }

  private DecodedNetworkMessage decodeMessage(
      byte[] message, @Nullable SecurityContextResolver resolver) {

    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      UadpDecodedMessage decoded =
          new UadpMessageMapping()
              .decodeMessage(DecodeContext.of(encodingContext, resolver), buffer);
      return assertInstanceOf(DecodedNetworkMessage.class, decoded);
    } finally {
      buffer.release();
    }
  }

  private static byte[] bytes(int... values) {
    byte[] bs = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bs[i] = (byte) values[i];
    }
    return bs;
  }
}
