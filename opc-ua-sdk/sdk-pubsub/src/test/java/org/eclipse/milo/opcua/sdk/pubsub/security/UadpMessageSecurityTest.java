/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.security;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import java.nio.ByteBuffer;
import java.util.HexFormat;
import java.util.Random;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link UadpMessageSecurity}: the AES-CTR transform against RFC 3686 test vectors, the
 * HMAC-SHA256 sign/verify pair, and the ByteBuf region contract.
 *
 * <p>RFC 3686's counter block construction — {@code Nonce(4) || IV(8) || BlockCounter(4)}, block
 * counter big-endian starting at 1 — is byte-for-byte the Part 14 Table 157 construction {@code
 * KeyNonce(4) || MessageNonce(8) || BlockCounter(4)} (the Table 157 note says the convention "comes
 * from the AES-CTR RFC"). The vectors below therefore map RFC "Nonce" to KeyNonce and RFC "IV" to
 * MessageNonce, and the RFC ciphertexts apply unchanged.
 */
class UadpMessageSecurityTest {

  private static final HexFormat HEX = HexFormat.of();

  /**
   * Build key material whose EncryptingKey/KeyNonce are the given values; the 32-byte SigningKey
   * part is zero-filled (unused by the CTR transform).
   */
  private static SecurityKeyMaterial ctrMaterial(
      PubSubSecurityPolicy policy, String encryptingKeyHex, String keyNonceHex) throws UaException {

    byte[] encryptingKey = HEX.parseHex(encryptingKeyHex);
    byte[] keyNonce = HEX.parseHex(keyNonceHex);
    assertEquals(policy.getEncryptingKeyLength(), encryptingKey.length);
    assertEquals(policy.getKeyNonceLength(), keyNonce.length);

    byte[] keyData = new byte[policy.getKeyDataLength()];
    System.arraycopy(encryptingKey, 0, keyData, policy.getSigningKeyLength(), encryptingKey.length);
    System.arraycopy(
        keyNonce,
        0,
        keyData,
        policy.getSigningKeyLength() + policy.getEncryptingKeyLength(),
        keyNonce.length);

    return SecurityKeyMaterial.split(policy, ByteString.of(keyData));
  }

  /**
   * Build key material whose SigningKey part is the given value; the encrypting key and key nonce
   * parts are zero-filled (unused by sign/verify).
   */
  private static SecurityKeyMaterial signingMaterial(String signingKeyHex) throws UaException {
    byte[] signingKey = HEX.parseHex(signingKeyHex);
    assertEquals(32, signingKey.length);

    byte[] keyData = new byte[PubSubSecurityPolicy.PubSubAes128Ctr.getKeyDataLength()];
    System.arraycopy(signingKey, 0, keyData, 0, signingKey.length);

    return SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(keyData));
  }

  // region AES-CTR vs RFC 3686

  /**
   * RFC 3686 §6 test vectors #1-#3 (AES-128) and #7-#9 (AES-256), with RFC "Nonce" as the KeyNonce
   * and RFC "IV" as the MessageNonce.
   *
   * <p>Hand derivation of TV#1 per Table 157, cross-checkable against the keystream printed in RFC
   * 3686 itself:
   *
   * <pre>
   *   counter block 1 = KeyNonce || MessageNonce || BlockCounter
   *                   = 00000030 || 0000000000000000 || 00000001
   *   keystream 1     = AES-128-ECB(AE6852F8121067CC4BF7A5765577F39E, counter block 1)
   *                   = B7603328DBC2931B410E16C8067E62DF          (RFC 3686 "Key Stream (1)")
   *   ciphertext      = plaintext XOR keystream 1, where the plaintext is the ASCII of
   *                     "Single block msg" = 53696E67 6C652062 6C6F636B 206D7367:
   *                     53^B7=E4, 69^60=09, 6E^33=5D, 67^28=4F, ...
   *                   = E4095D4F B7A7B379 2D6175A3 261311B8
   * </pre>
   *
   * <p>TV#2/#8 are 32 bytes (two full blocks: counter increment), TV#3/#9 are 36 bytes (partial
   * trailing block: CTR adds no padding).
   */
  private static Stream<Arguments> rfc3686Vectors() {
    String pt32 = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F";
    String pt36 = pt32 + "20212223";

    return Stream.of(
        Arguments.of(
            "TV#1 AES-128, single block",
            PubSubSecurityPolicy.PubSubAes128Ctr,
            "AE6852F8121067CC4BF7A5765577F39E",
            "00000030",
            "0000000000000000",
            HEX.formatHex("Single block msg".getBytes(US_ASCII)),
            "E4095D4FB7A7B3792D6175A3261311B8"),
        Arguments.of(
            "TV#2 AES-128, two blocks",
            PubSubSecurityPolicy.PubSubAes128Ctr,
            "7E24067817FAE0D743D6CE1F32539163",
            "006CB6DB",
            "C0543B59DA48D90B",
            pt32,
            "5104A106168A72D9790D41EE8EDAD388EB2E1EFC46DA57C8FCE630DF9141BE28"),
        Arguments.of(
            "TV#3 AES-128, partial trailing block",
            PubSubSecurityPolicy.PubSubAes128Ctr,
            "7691BE035E5020A8AC6E618529F9A0DC",
            "00E0017B",
            "27777F3F4A1786F0",
            pt36,
            "C1CF48A89F2FFDD9CF4652E9EFDB72D74540A42BDE6D7836D59A5CEAAEF3105325B2072F"),
        Arguments.of(
            "TV#7 AES-256, single block",
            PubSubSecurityPolicy.PubSubAes256Ctr,
            "776BEFF2851DB06F4C8A0542C8696F6C6A81AF1EEC96B4D37FC1D689E6C1C104",
            "00000060",
            "DB5672C97AA8F0B2",
            HEX.formatHex("Single block msg".getBytes(US_ASCII)),
            "145AD01DBF824EC7560863DC71E3E0C0"),
        Arguments.of(
            "TV#8 AES-256, two blocks",
            PubSubSecurityPolicy.PubSubAes256Ctr,
            "F6D66D6BD52D59BB0796365879EFF886C66DD51A5B6A99744B50590C87A23884",
            "00FAAC24",
            "C1585EF15A43D875",
            pt32,
            "F05E231B3894612C49EE000B804EB2A9B8306B508F839D6A5530831D9344AF1C"),
        Arguments.of(
            "TV#9 AES-256, partial trailing block",
            PubSubSecurityPolicy.PubSubAes256Ctr,
            "FF7A617CE69148E4F1726E2F43581DE2AA62D9F805532EDFF1EED687FB54153D",
            "001CC5B7",
            "51A51D70A1C11148",
            pt36,
            "EB6C52821D0BBBF7CE7594462ACA4FAAB407DF866569FD07F48CC0B583D6071F1EC0E6B8"));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("rfc3686Vectors")
  void ctrTransformMatchesRfc3686Vector(
      String name,
      PubSubSecurityPolicy policy,
      String keyHex,
      String keyNonceHex,
      String messageNonceHex,
      String plaintextHex,
      String ciphertextHex)
      throws UaException {

    SecurityKeyMaterial keys = ctrMaterial(policy, keyHex, keyNonceHex);
    byte[] messageNonce = HEX.parseHex(messageNonceHex);
    byte[] plaintext = HEX.parseHex(plaintextHex);
    byte[] ciphertext = HEX.parseHex(ciphertextHex);

    ByteBuf buffer = Unpooled.buffer(plaintext.length).writeBytes(plaintext);

    UadpMessageSecurity.ctrTransform(keys, messageNonce, buffer);

    byte[] transformed = new byte[buffer.readableBytes()];
    buffer.getBytes(buffer.readerIndex(), transformed);
    assertArrayEquals(ciphertext, transformed, name + ": encrypt");

    // CTR is symmetric: the same transform applied to the ciphertext restores the plaintext.
    UadpMessageSecurity.ctrTransform(keys, messageNonce, buffer);

    buffer.getBytes(buffer.readerIndex(), transformed);
    assertArrayEquals(plaintext, transformed, name + ": decrypt");
  }

  @Test
  void ctrTransformWorksOnHeapDirectAndPooledBuffers() throws UaException {
    byte[] plaintext = "Single block msg".getBytes(US_ASCII);
    byte[] ciphertext = HEX.parseHex("E4095D4FB7A7B3792D6175A3261311B8");

    ByteBuf[] buffers = {
      Unpooled.buffer(plaintext.length),
      Unpooled.directBuffer(plaintext.length),
      PooledByteBufAllocator.DEFAULT.heapBuffer(plaintext.length),
      PooledByteBufAllocator.DEFAULT.directBuffer(plaintext.length)
    };

    for (ByteBuf buffer : buffers) {
      try {
        SecurityKeyMaterial keys =
            ctrMaterial(
                PubSubSecurityPolicy.PubSubAes128Ctr,
                "AE6852F8121067CC4BF7A5765577F39E",
                "00000030");

        buffer.writeBytes(plaintext);

        UadpMessageSecurity.ctrTransform(keys, new byte[8], buffer);

        byte[] transformed = new byte[buffer.readableBytes()];
        buffer.getBytes(buffer.readerIndex(), transformed);
        assertArrayEquals(ciphertext, transformed, buffer.getClass().getSimpleName());
      } finally {
        buffer.release();
      }
    }
  }

  @Test
  void ctrTransformLeavesIndicesAndBytesOutsideTheReadableWindowUntouched() throws UaException {
    SecurityKeyMaterial keys =
        ctrMaterial(
            PubSubSecurityPolicy.PubSubAes128Ctr, "AE6852F8121067CC4BF7A5765577F39E", "00000030");

    byte[] plaintext = "Single block msg".getBytes(US_ASCII);
    byte[] ciphertext = HEX.parseHex("E4095D4FB7A7B3792D6175A3261311B8");

    // Layout: 8 prefix bytes (0x55), 16 payload bytes, 8 suffix bytes (0x66); then shrink the
    // readable window to just the payload. Only [readerIndex, writerIndex) may change.
    ByteBuf buffer = Unpooled.buffer(32);
    for (int i = 0; i < 8; i++) {
      buffer.writeByte(0x55);
    }
    buffer.writeBytes(plaintext);
    for (int i = 0; i < 8; i++) {
      buffer.writeByte(0x66);
    }
    buffer.readerIndex(8);
    buffer.writerIndex(24);

    UadpMessageSecurity.ctrTransform(keys, new byte[8], buffer);

    assertEquals(8, buffer.readerIndex());
    assertEquals(24, buffer.writerIndex());
    for (int i = 0; i < 8; i++) {
      assertEquals(0x55, buffer.getUnsignedByte(i), "prefix byte " + i);
      assertEquals(0x66, buffer.getUnsignedByte(24 + i), "suffix byte " + i);
    }
    byte[] transformed = new byte[16];
    buffer.getBytes(8, transformed);
    assertArrayEquals(ciphertext, transformed);
  }

  @Test
  void ctrTransformOfASliceIsVisibleInTheParentBuffer() throws UaException {
    // The documented usage: callers pass buffer.slice(payloadStart, len) to transform a
    // sub-region in place; slices share memory with the parent.
    SecurityKeyMaterial keys =
        ctrMaterial(
            PubSubSecurityPolicy.PubSubAes128Ctr, "AE6852F8121067CC4BF7A5765577F39E", "00000030");

    byte[] plaintext = "Single block msg".getBytes(US_ASCII);
    byte[] ciphertext = HEX.parseHex("E4095D4FB7A7B3792D6175A3261311B8");

    ByteBuf parent = Unpooled.buffer(24);
    parent.writeBytes(new byte[] {0x01, 0x02, 0x03, 0x04});
    parent.writeBytes(plaintext);
    parent.writeBytes(new byte[] {0x05, 0x06, 0x07, 0x08});

    UadpMessageSecurity.ctrTransform(keys, new byte[8], parent.slice(4, 16));

    assertEquals(0, parent.readerIndex());
    assertEquals(24, parent.writerIndex());
    byte[] transformed = new byte[16];
    parent.getBytes(4, transformed);
    assertArrayEquals(ciphertext, transformed);
    assertEquals(0x01, parent.getByte(0));
    assertEquals(0x08, parent.getByte(23));
  }

  @Test
  void ctrTransformOfEmptyRegionIsANoOp() throws UaException {
    SecurityKeyMaterial keys =
        ctrMaterial(
            PubSubSecurityPolicy.PubSubAes128Ctr, "AE6852F8121067CC4BF7A5765577F39E", "00000030");

    ByteBuf buffer = Unpooled.buffer(8).writeBytes(new byte[] {1, 2, 3, 4});
    buffer.readerIndex(4); // readerIndex == writerIndex: nothing readable

    UadpMessageSecurity.ctrTransform(keys, new byte[8], buffer);

    assertEquals(4, buffer.readerIndex());
    assertEquals(4, buffer.writerIndex());
    assertEquals(1, buffer.getByte(0));
    assertEquals(4, buffer.getByte(3));
  }

  @ParameterizedTest
  @MethodSource("wrongNonceLengths")
  void ctrTransformRejectsWrongMessageNonceLength(int nonceLength) throws UaException {
    SecurityKeyMaterial keys =
        ctrMaterial(
            PubSubSecurityPolicy.PubSubAes128Ctr, "AE6852F8121067CC4BF7A5765577F39E", "00000030");

    ByteBuf buffer = Unpooled.buffer(4).writeBytes(new byte[] {1, 2, 3, 4});

    UaException e =
        assertThrows(
            UaException.class,
            () -> UadpMessageSecurity.ctrTransform(keys, new byte[nonceLength], buffer));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, e.getStatusCode().value());
  }

  private static Stream<Arguments> wrongNonceLengths() {
    return Stream.of(Arguments.of(0), Arguments.of(4), Arguments.of(7), Arguments.of(9));
  }

  @Test
  void ctrEncryptThenDecryptIsIdentityForArbitraryLengths() throws UaException {
    Random random = new Random(0xF00D);

    for (PubSubSecurityPolicy policy : PubSubSecurityPolicy.values()) {
      byte[] keyData = new byte[policy.getKeyDataLength()];
      random.nextBytes(keyData);
      SecurityKeyMaterial keys = SecurityKeyMaterial.split(policy, ByteString.of(keyData));

      byte[] messageNonce = new byte[8];
      random.nextBytes(messageNonce);

      for (int length : new int[] {1, 15, 16, 17, 100}) {
        byte[] plaintext = new byte[length];
        random.nextBytes(plaintext);

        ByteBuf buffer = Unpooled.buffer(length).writeBytes(plaintext);
        UadpMessageSecurity.ctrTransform(keys, messageNonce, buffer);
        UadpMessageSecurity.ctrTransform(keys, messageNonce, buffer);

        byte[] roundTripped = new byte[length];
        buffer.getBytes(0, roundTripped);
        assertArrayEquals(plaintext, roundTripped, policy + " length " + length);
      }
    }
  }

  // endregion

  // region HMAC-SHA256 sign/verify

  /**
   * Expected value computed independently of Milo/JCE:
   *
   * <pre>
   *   python3 -c 'import hmac, hashlib;
   *     print(hmac.new(bytes(range(32)), b"UADP message security sample",
   *           hashlib.sha256).hexdigest())'
   *   printf '%s' 'UADP message security sample' | openssl dgst -sha256 -mac HMAC \
   *     -macopt hexkey:000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f
   * </pre>
   *
   * <p>Both print {@code f4dfdbe5368b5ecba95d4fc565906d0141b02adc7bfabdf03142003b273096fa}.
   */
  @Test
  void signMatchesIndependentlyComputedHmacSha256() throws UaException {
    SecurityKeyMaterial keys =
        signingMaterial("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F");

    byte[] data = "UADP message security sample".getBytes(US_ASCII);

    byte[] signature = UadpMessageSecurity.sign(keys, ByteBuffer.wrap(data));

    assertArrayEquals(
        HEX.parseHex("F4DFDBE5368B5ECBA95D4FC565906D0141B02ADC7BFABDF03142003B273096FA"),
        signature);
  }

  @Test
  void signVerifyRoundTrip() throws UaException {
    SecurityKeyMaterial keys =
        signingMaterial("101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F");

    byte[] data = new byte[100];
    new Random(0xBEEF).nextBytes(data);

    byte[] signature = UadpMessageSecurity.sign(keys, ByteBuffer.wrap(data));

    assertEquals(32, signature.length);
    assertTrue(UadpMessageSecurity.verify(keys, signature, ByteBuffer.wrap(data)));
  }

  @Test
  void verifyDetectsAFlippedDataByte() throws UaException {
    SecurityKeyMaterial keys =
        signingMaterial("101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F");

    byte[] data = new byte[100];
    new Random(0xBEEF).nextBytes(data);
    byte[] signature = UadpMessageSecurity.sign(keys, ByteBuffer.wrap(data));

    data[42] ^= 0x01;

    assertFalse(UadpMessageSecurity.verify(keys, signature, ByteBuffer.wrap(data)));
  }

  @Test
  void verifyDetectsAFlippedSignatureByte() throws UaException {
    SecurityKeyMaterial keys =
        signingMaterial("101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F");

    byte[] data = new byte[100];
    new Random(0xBEEF).nextBytes(data);
    byte[] signature = UadpMessageSecurity.sign(keys, ByteBuffer.wrap(data));

    signature[7] ^= 0x01;

    assertFalse(UadpMessageSecurity.verify(keys, signature, ByteBuffer.wrap(data)));
  }

  @Test
  void verifyRejectsTruncatedOrEmptySignatureWithoutThrowing() throws UaException {
    SecurityKeyMaterial keys =
        signingMaterial("101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F");

    byte[] data = "payload".getBytes(US_ASCII);
    byte[] signature = UadpMessageSecurity.sign(keys, ByteBuffer.wrap(data));

    byte[] truncated = new byte[31];
    System.arraycopy(signature, 0, truncated, 0, 31);

    // MessageDigest.isEqual returns false on length mismatch; verify must not throw.
    assertFalse(UadpMessageSecurity.verify(keys, truncated, ByteBuffer.wrap(data)));
    assertFalse(UadpMessageSecurity.verify(keys, new byte[0], ByteBuffer.wrap(data)));
  }

  @Test
  void signOverMultipleBuffersEqualsSignOverTheirConcatenation() throws UaException {
    SecurityKeyMaterial keys =
        signingMaterial("101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F");

    byte[] data = new byte[64];
    new Random(0xCAFE).nextBytes(data);

    byte[] whole = UadpMessageSecurity.sign(keys, ByteBuffer.wrap(data));
    byte[] split =
        UadpMessageSecurity.sign(keys, ByteBuffer.wrap(data, 0, 10), ByteBuffer.wrap(data, 10, 54));

    assertArrayEquals(whole, split);
    assertTrue(
        UadpMessageSecurity.verify(
            keys, whole, ByteBuffer.wrap(data, 0, 33), ByteBuffer.wrap(data, 33, 31)));
  }

  // endregion
}
