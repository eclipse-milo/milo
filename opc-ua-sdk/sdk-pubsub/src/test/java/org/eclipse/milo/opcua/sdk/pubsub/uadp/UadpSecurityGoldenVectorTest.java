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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Computed golden vectors for UADP message security (Part 14 §7.2.4.4.3), built on the
 * wire-security worked layout: PublisherId UInt16, full GroupHeader, 2-DataSetMessage
 * PayloadHeader, Timestamp — SecurityHeader at offset 28, (encrypted) payload at 42, HMAC-SHA256
 * signature over everything before it.
 *
 * <p>Encoding is made deterministic by fixed key data and a fixed-nonce {@link
 * MessageNonceSupplier}. The first vector ({@link #handDerivedWorkedExampleVector()}) is derived
 * fully BY HAND from the Part 14 tables, byte by byte, so it is independent of the code under test;
 * the {policy} × {mode} matrix is then recomputed independently with {@code javax.crypto}
 * primitives (never via the production crypto helpers) spliced around the mode-None encoding of the
 * same context. Decode-side negatives assert the tolerance contract: security drops are header-only
 * results with a {@link SecurityOutcome}, never exceptions and never {@code failure}s (except
 * truncation signatures).
 */
class UadpSecurityGoldenVectorTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(0x1234));
  private static final UShort WRITER_GROUP_ID = ushort(258);

  /** PublisherId | GroupHeader (all four fields) | PayloadHeader | Timestamp. */
  private static final UadpNetworkMessageContentMask FULL_MASK =
      new UadpNetworkMessageContentMask(uint(0xFF));

  /** Offset of the SecurityHeader for {@link #FULL_MASK} with two writers (wire-security §12). */
  private static final int SECURITY_HEADER_OFFSET = 28;

  private static final int SECURITY_HEADER_SIZE = 14;
  private static final int SIGNATURE_SIZE = 32;

  private static final byte[] MESSAGE_NONCE = {
    (byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, 0x01, 0x00, 0x00, 0x00
  };

  private static final int TOKEN_ID = 7;

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region computed encode vectors

  /**
   * The first vector, derived fully BY HAND from the wire-security worked example (SignAndEncrypt,
   * PubSub-Aes256-CTR) so it is independent of the code under test: every plaintext byte below
   * comes straight from the Part 14 tables (Table 154 header order, Table 161 payload), the AES-CTR
   * counter block is assembled per Table 157, and only the AES keystream and the HMAC-SHA256 digest
   * are computed — with {@code javax.crypto} directly, never via the production helpers.
   */
  @Test
  void handDerivedWorkedExampleVector() throws Exception {
    // The complete NetworkMessage BEFORE encryption and signing, hand-derived byte by byte.
    // Layout (wire-security §12): plaintext header [0, 28) | SecurityHeader [28, 42) |
    // payload = Sizes + 2 DataSetMessage bodies [42, 62).
    byte[] plaintext =
        bytes(
            // -- plaintext NetworkMessage header, offsets 0-27 (signed, never encrypted) --
            0xF1, // 0: UADPVersion 1 | PublisherId 0x10 | GroupHeader 0x20 | PayloadHeader 0x40
            //      | ExtendedFlags1 0x80
            0x31, // 1: ExtendedFlags1: PublisherIdType 001 = UInt16 | SecurityHeader 0x10
            //      | Timestamp 0x20 (no ExtendedFlags2: type Data, no chunk)
            0x34,
            0x12, // 2-3: PublisherId UInt16 LE = 0x1234
            0x0F, // 4: GroupFlags: WriterGroupId | GroupVersion | NetworkMessageNumber | SeqNum
            0x02,
            0x01, // 5-6: WriterGroupId UInt16 LE = 258
            0x05,
            0x00,
            0x00,
            0x00, // 7-10: GroupVersion UInt32 LE = 5
            0x01,
            0x00, // 11-12: NetworkMessageNumber UInt16 LE = 1
            0x10,
            0x00, // 13-14: NetworkMessage SequenceNumber UInt16 LE = 16
            0x02, // 15: PayloadHeader Count = 2
            0x01,
            0x00,
            0x02,
            0x00, // 16-19: DataSetWriterIds[0] = 1, [1] = 2 (UInt16 LE)
            0xC0,
            0xC6,
            0x2D,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00, // 20-27: Timestamp Int64 LE = 3000000
            // -- SecurityHeader, offsets 28-41 (signed, never encrypted; Table 154) --
            0x03, // 28: SecurityFlags: bit 0 signed | bit 1 encrypted (no footer, no key reset)
            0x07,
            0x00,
            0x00,
            0x00, // 29-32: SecurityTokenId UInt32 LE = 7
            0x08, // 33: NonceLength = 8 (AES-CTR, Table 157)
            0xA1,
            0xA2,
            0xA3,
            0xA4, // 34-37: MessageNonce Random[4] (the injected fixed nonce)
            0x01,
            0x00,
            0x00,
            0x00, // 38-41: MessageNonce NonceSequenceNumber UInt32 LE = 1
            // -- payload region, offsets 42-61 (ENCRYPTED, then signed; Table 161) --
            0x08,
            0x00,
            0x08,
            0x00, // 42-45: Sizes[2] (UInt16 LE): both bodies are 8 bytes
            0x01, // 46: DSM 1 DataSetFlags1: valid | Variant encoding | no optional fields
            0x01,
            0x00, // 47-48: DSM 1 FieldCount = 1
            0x06,
            0x2A,
            0x00,
            0x00,
            0x00, // 49-53: DSM 1 field 0: Variant Int32 = 42
            0x01, // 54: DSM 2 DataSetFlags1: valid | Variant encoding
            0x01,
            0x00, // 55-56: DSM 2 FieldCount = 1
            0x06,
            0x2B,
            0x00,
            0x00,
            0x00); // 57-61: DSM 2 field 0: Variant Int32 = 43

    int securityHeaderStart = 28;
    int payloadStart = 42;
    int payloadEnd = 62;
    assertEquals(payloadEnd, plaintext.length);

    // Encrypt FIRST (§7.2.4.4.1): AES-256-CTR over the payload region only. Counter block
    // (Table 157) = KeyNonce(4) = keyData[64..68) = 40 41 42 43 || MessageNonce(8) =
    // A1 A2 A3 A4 01 00 00 00 || BlockCounter(4, BIG-endian, starts at 1) = 00 00 00 01.
    byte[] ciphertext =
        ctr(
            PubSubSecurityPolicy.PubSubAes256Ctr,
            Arrays.copyOfRange(plaintext, payloadStart, payloadEnd));

    byte[] expected = new byte[payloadEnd + SIGNATURE_SIZE];
    System.arraycopy(plaintext, 0, expected, 0, payloadStart);
    System.arraycopy(ciphertext, 0, expected, payloadStart, payloadEnd - payloadStart);

    // Sign AFTER encrypting (§7.2.4.4.3.2): HMAC-SHA256 with SigningKey = keyData[0..32) over
    // the ENTIRE NetworkMessage including the encrypted data, [0, 62); the 32-byte signature is
    // appended after the signed region and is not part of it.
    byte[] signature = hmac(PubSubSecurityPolicy.PubSubAes256Ctr, expected, payloadEnd);
    System.arraycopy(signature, 0, expected, payloadEnd, SIGNATURE_SIZE);

    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);
    assertArrayEquals(expected, secured);

    // sanity spot checks on the worked-layout offsets
    assertEquals(0x03, secured[securityHeaderStart] & 0xFF);
    assertEquals(8, secured[securityHeaderStart + 5] & 0xFF);

    // and the decoder round-trips it (decrypt + parse only after verification)
    DecodedNetworkMessage decoded = decode(secured, resolverFor(keyMaterial()));
    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
    assertNull(decoded.failure());
    assertEquals(2, decoded.messages().size());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))),
        decoded.messages().get(0).fields());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofInt32(43)))),
        decoded.messages().get(1).fields());
  }

  private static Stream<Arguments> securityCells() {
    return Stream.of(
        Arguments.of(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes256Ctr),
        Arguments.of(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes256Ctr));
  }

  /**
   * The full {mode} × {policy} matrix: the encoder's output equals an independent {@code
   * javax.crypto} recompute spliced around the mode-None encoding of the same context, and decodes
   * back — through a fixed-key resolver — to the same model as the mode-None bytes. The
   * multi-writer payload puts the Sizes array inside the encrypted region (Table 161).
   */
  @ParameterizedTest(name = "{0} × {1}")
  @MethodSource("securityCells")
  void computedVectorMatchesIndependentRecompute(
      MessageSecurityMode mode, PubSubSecurityPolicy policy) throws Exception {

    byte[] plain = encode(MessageSecurityMode.None, policy);
    byte[] secured = encode(mode, policy);

    boolean encrypt = mode == MessageSecurityMode.SignAndEncrypt;
    byte[] expected =
        spliceSecured(plain, SECURITY_HEADER_OFFSET, encrypt ? 0x03 : 0x01, encrypt, policy);

    assertArrayEquals(expected, secured);

    // spot-check the worked layout: SecurityFlags at 28, token at 29, NonceLength at 33 —
    // sign-only also carries the real token id and the 8-byte nonce (the Annex A form)
    assertEquals(encrypt ? 0x03 : 0x01, secured[SECURITY_HEADER_OFFSET] & 0xFF);
    assertEquals(TOKEN_ID, secured[SECURITY_HEADER_OFFSET + 1] & 0xFF);
    assertEquals(8, secured[SECURITY_HEADER_OFFSET + 5] & 0xFF);
    // ExtendedFlags1 was already present (PublisherId UInt16 + Timestamp), so the overhead is
    // exactly the 14-byte SecurityHeader plus the 32-byte signature
    assertEquals(plain.length + SECURITY_HEADER_SIZE + SIGNATURE_SIZE, secured.length);

    if (!encrypt) {
      // the sign-only payload region is NOT encrypted
      assertArrayEquals(
          Arrays.copyOfRange(plain, SECURITY_HEADER_OFFSET, plain.length),
          Arrays.copyOfRange(
              secured,
              SECURITY_HEADER_OFFSET + SECURITY_HEADER_SIZE,
              secured.length - SIGNATURE_SIZE));
    }

    // decode back: model equality against the mode-None decode of the same context
    DecodedNetworkMessage plainDecoded = decode(plain, null);
    DecodedNetworkMessage securedDecoded = decode(secured, resolverFor(keyMaterial(policy)));

    assertNotNull(securedDecoded.security());
    assertEquals(SecurityOutcome.VERIFIED, securedDecoded.security().outcome());
    assertEquals(mode, securedDecoded.security().mode());
    assertEquals(uint(TOKEN_ID), securedDecoded.security().securityTokenId());
    assertNull(securedDecoded.failure());

    assertEquals(plainDecoded.messages(), securedDecoded.messages());
    assertEquals(plainDecoded.publisherId(), securedDecoded.publisherId());
    assertEquals(plainDecoded.writerGroupId(), securedDecoded.writerGroupId());
    assertEquals(plainDecoded.sequenceNumber(), securedDecoded.sequenceNumber());
  }

  @Test
  void nonceSupplierInvokedExactlyOncePerNetworkMessage() throws Exception {
    var invocations = new AtomicInteger(0);
    MessageNonceSupplier countingSupplier =
        () -> {
          invocations.incrementAndGet();
          return MESSAGE_NONCE.clone();
        };

    var context =
        new MessageSecurityContext(
            MessageSecurityMode.SignAndEncrypt, uint(TOKEN_ID), keyMaterial(), countingSupplier);

    encodeToBytes(encodeContext(MessageSecurityMode.SignAndEncrypt, context));

    assertEquals(1, invocations.get());
  }

  // endregion

  // region round trips

  /**
   * Sign-only without a PayloadHeader: the single DataSetMessage "spans the rest of the buffer",
   * which under security must exclude the trailing signature — otherwise the key-frame field
   * decoding would misread signature bytes as fields.
   */
  @Test
  void signOnlyWithoutPayloadHeaderBoundsThePayloadWindow() throws Exception {
    // PublisherId | Timestamp only: no PayloadHeader, no GroupHeader
    var mask = new UadpNetworkMessageContentMask(uint(0x81));
    DataSetWriterConfig writer = variantWriter(1);
    WriterGroupConfig group = group(mask, List.of(writer), MessageSecurityMode.Sign);

    var securityContext =
        new MessageSecurityContext(
            MessageSecurityMode.Sign, uint(TOKEN_ID), keyMaterial(), MESSAGE_NONCE::clone);

    byte[] secured =
        encodeToBytes(
            EncodeContext.of(
                encodingContext,
                PUBLISHER_ID,
                group,
                uint(0),
                ushort(1),
                ushort(1),
                new DateTime(3_000_000L),
                List.of(keyFrame(writer, 1, goodValue(Variant.ofInt32(42)))),
                securityContext));

    DecodedNetworkMessage decoded = decode(secured, resolverFor(keyMaterial()));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
    assertNull(decoded.failure());
    assertEquals(1, decoded.messages().size());
    assertEquals(
        List.of(new DecodedField(0, goodValue(Variant.ofInt32(42)))),
        decoded.messages().get(0).fields());
  }

  // endregion

  // region decode negatives

  @Test
  void tamperedBytesFailVerificationHeaderOnly() throws Exception {
    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);

    // tamper value-carrying bytes, not flag bytes: a flipped flag would change the parse
    // structure instead of exercising the verification path
    int headerByte = 2; // inside the PublisherId
    int payloadByte = SECURITY_HEADER_OFFSET + SECURITY_HEADER_SIZE + 2; // inside the payload
    int signatureByte = secured.length - 1;

    for (int index : new int[] {headerByte, payloadByte, signatureByte}) {
      byte[] tampered = secured.clone();
      tampered[index] ^= 0x01;

      DecodedNetworkMessage decoded = decode(tampered, resolverFor(keyMaterial()));

      assertNotNull(decoded.security(), "tampered index " + index);
      assertEquals(
          SecurityOutcome.INVALID_SIGNATURE, decoded.security().outcome(), "index " + index);
      assertTrue(decoded.messages().isEmpty(), "index " + index);
      assertNull(decoded.failure(), "a security drop is a skip, not a failure; index " + index);
      // the plaintext header (as received) is still surfaced
      assertEquals(WRITER_GROUP_ID, decoded.writerGroupId(), "index " + index);
    }
  }

  @Test
  void wrongKeyFailsVerificationBeforeAnyPayloadParse() throws Exception {
    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);

    byte[] otherKeyData = sequentialBytes(68);
    otherKeyData[0] ^= 0x55;
    SecurityKeyMaterial otherKeys =
        SecurityKeyMaterial.split(
            PubSubSecurityPolicy.PubSubAes256Ctr, ByteString.of(otherKeyData));

    DecodedNetworkMessage decoded = decode(secured, resolverFor(otherKeys));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.INVALID_SIGNATURE, decoded.security().outcome());
    assertTrue(decoded.messages().isEmpty());
    assertNull(decoded.failure());
  }

  @Test
  void truncationBelowSignatureSurfacesFailure() throws Exception {
    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);

    // cut inside the signature: fewer bytes remain than the signature requires
    byte[] truncated = Arrays.copyOf(secured, SECURITY_HEADER_OFFSET + SECURITY_HEADER_SIZE + 10);

    DecodedNetworkMessage decoded = decode(truncated, resolverFor(keyMaterial()));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.INVALID_SIGNATURE, decoded.security().outcome());
    assertNotNull(decoded.failure());
    assertEquals(StatusCodes.Bad_DecodingError, decoded.failure().statusCode().value());
  }

  @Test
  void securedMessageWithoutResolverIsSkippedNoResolver() throws Exception {
    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);

    DecodedNetworkMessage decoded = decode(secured, null);

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.NO_RESOLVER, decoded.security().outcome());
    assertEquals(uint(TOKEN_ID), decoded.security().securityTokenId());
    assertTrue(decoded.messages().isEmpty());
    assertNull(decoded.failure());
    // the plaintext header is still decoded
    assertEquals(PUBLISHER_ID, decoded.publisherId());
    assertEquals(WRITER_GROUP_ID, decoded.writerGroupId());
  }

  @Test
  void resolverRefusalsSurfaceTheirOutcome() throws Exception {
    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);

    for (SecurityOutcome reason :
        new SecurityOutcome[] {SecurityOutcome.UNKNOWN_TOKEN, SecurityOutcome.NO_KEYS}) {

      SecurityContextResolver refusingResolver =
          request -> new SecurityContextResolver.Resolution.Refused(reason);

      DecodedNetworkMessage decoded = decode(secured, refusingResolver);

      assertNotNull(decoded.security());
      assertEquals(reason, decoded.security().outcome());
      assertTrue(decoded.messages().isEmpty());
      assertNull(decoded.failure());
    }
  }

  /** The resolver sees the plaintext header identifiers and the SecurityHeader values. */
  @Test
  void resolverRequestCarriesHeaderIdentifiers() throws Exception {
    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);

    SecurityKeyMaterial keys = keyMaterial();
    var requests = new ArrayList<SecurityContextResolver.ResolveRequest>();
    SecurityContextResolver capturingResolver =
        request -> {
          requests.add(request);
          return new SecurityContextResolver.Resolution.Keys(keys);
        };

    decode(secured, capturingResolver);

    assertEquals(1, requests.size());
    SecurityContextResolver.ResolveRequest request = requests.get(0);
    assertEquals(PUBLISHER_ID, request.publisherId());
    assertEquals(WRITER_GROUP_ID, request.writerGroupId());
    assertEquals(List.of(ushort(1), ushort(2)), request.dataSetWriterIds());
    assertEquals(MessageSecurityMode.SignAndEncrypt, request.receivedMode());
    assertEquals(uint(TOKEN_ID), request.securityTokenId());
    assertFalse(request.forceKeyReset());
  }

  /**
   * Positive force-key-reset wire coverage: a secured NetworkMessage with SecurityFlags bit 3 set
   * decodes {@code VERIFIED} — bit 3 is not a reserved bit and must not skip the message — and the
   * {@link SecurityContextResolver.ResolveRequest} carries {@code forceKeyReset=true} so the
   * resolver can trigger its proactive refetch (the manager reaction is covered by the
   * SecurityKeyManager tests).
   */
  @Test
  void forceKeyResetBitDecodesVerifiedAndSurfacesInResolveRequest() throws Exception {
    byte[] secured = encode(MessageSecurityMode.Sign);

    // set SecurityFlags bit 3 (force key reset); the flags byte is inside the signed region,
    // so the message must be re-signed
    byte[] modified = secured.clone();
    modified[SECURITY_HEADER_OFFSET] |= 0x08;
    resign(modified);
    assertEquals(0x09, modified[SECURITY_HEADER_OFFSET] & 0xFF); // signed | force key reset

    SecurityKeyMaterial keys = keyMaterial();
    var requests = new ArrayList<SecurityContextResolver.ResolveRequest>();
    SecurityContextResolver capturingResolver =
        request -> {
          requests.add(request);
          return new SecurityContextResolver.Resolution.Keys(keys);
        };

    DecodedNetworkMessage decoded = decode(modified, capturingResolver);

    assertEquals(1, requests.size());
    assertTrue(requests.get(0).forceKeyReset());

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
    assertEquals(MessageSecurityMode.Sign, decoded.security().mode());
    assertNull(decoded.failure());
    assertEquals(2, decoded.messages().size());
  }

  // endregion

  // region tolerated header forms

  /** The literal Table 154 sign-only form — NonceLength 0, no nonce — decodes and verifies. */
  @Test
  void nonceLengthZeroSignOnlyFormIsAccepted() throws Exception {
    byte[] secured = encode(MessageSecurityMode.Sign);

    // splice out the 8 nonce bytes and set NonceLength to 0, then re-sign
    int nonceLengthOffset = SECURITY_HEADER_OFFSET + 5;
    byte[] modified = new byte[secured.length - 8];
    System.arraycopy(secured, 0, modified, 0, nonceLengthOffset);
    modified[nonceLengthOffset] = 0;
    System.arraycopy(
        secured,
        nonceLengthOffset + 1 + 8,
        modified,
        nonceLengthOffset + 1,
        secured.length - (nonceLengthOffset + 1 + 8));
    resign(modified);

    DecodedNetworkMessage decoded = decode(modified, resolverFor(keyMaterial()));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
    assertEquals(2, decoded.messages().size());
  }

  /** A flagged SecurityFooter is skipped via SecurityFooterSize; we never emit one. */
  @Test
  void securityFooterIsSkippedOnDecode() throws Exception {
    byte[] secured = encode(MessageSecurityMode.Sign);

    int footerSize = 4;
    int nonceEnd = SECURITY_HEADER_OFFSET + SECURITY_HEADER_SIZE;
    int signatureStart = secured.length - SIGNATURE_SIZE;

    // set SecurityFlags bit 2, insert SecurityFooterSize after the nonce and the footer bytes
    // between the payload and the signature, then re-sign (the footer is inside the signed
    // region)
    byte[] modified = new byte[secured.length + 2 + footerSize];
    System.arraycopy(secured, 0, modified, 0, nonceEnd);
    modified[SECURITY_HEADER_OFFSET] = 0x05; // signed | footer
    modified[nonceEnd] = (byte) footerSize; // SecurityFooterSize UInt16 LE
    modified[nonceEnd + 1] = 0;
    System.arraycopy(secured, nonceEnd, modified, nonceEnd + 2, signatureStart - nonceEnd);
    int footerStart = nonceEnd + 2 + (signatureStart - nonceEnd);
    for (int i = 0; i < footerSize; i++) {
      modified[footerStart + i] = (byte) 0xEE;
    }
    resign(modified);

    DecodedNetworkMessage decoded = decode(modified, resolverFor(keyMaterial()));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
    assertNull(decoded.failure());
    assertEquals(2, decoded.messages().size());
  }

  /** An encrypted message whose NonceLength is not 8 cannot seed the cipher: DECRYPT_FAILED. */
  @Test
  void encryptedMessageWithUnusableNonceLengthIsDecryptFailed() throws Exception {
    byte[] secured = encode(MessageSecurityMode.SignAndEncrypt);

    // splice the 8-byte nonce down to 4 bytes and re-sign: the signature verifies but the
    // cipher cannot be seeded
    int nonceLengthOffset = SECURITY_HEADER_OFFSET + 5;
    byte[] modified = new byte[secured.length - 4];
    System.arraycopy(secured, 0, modified, 0, nonceLengthOffset);
    modified[nonceLengthOffset] = 4;
    System.arraycopy(
        secured, nonceLengthOffset + 1, modified, nonceLengthOffset + 1, 4); // 4 nonce bytes
    System.arraycopy(
        secured,
        nonceLengthOffset + 1 + 8,
        modified,
        nonceLengthOffset + 1 + 4,
        secured.length - (nonceLengthOffset + 1 + 8));
    resign(modified);

    DecodedNetworkMessage decoded = decode(modified, resolverFor(keyMaterial()));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.DECRYPT_FAILED, decoded.security().outcome());
    assertTrue(decoded.messages().isEmpty());
    assertNull(decoded.failure());
  }

  // endregion

  // region helpers

  private byte[] encode(MessageSecurityMode mode) throws UaException {
    return encode(mode, PubSubSecurityPolicy.PubSubAes256Ctr);
  }

  private byte[] encode(MessageSecurityMode mode, PubSubSecurityPolicy policy) throws UaException {
    MessageSecurityContext securityContext = null;
    if (mode != MessageSecurityMode.None) {
      securityContext =
          new MessageSecurityContext(
              mode, uint(TOKEN_ID), keyMaterial(policy), MESSAGE_NONCE::clone);
    }
    return encodeToBytes(encodeContext(mode, securityContext));
  }

  private EncodeContext encodeContext(
      MessageSecurityMode mode, @Nullable MessageSecurityContext securityContext) {

    DataSetWriterConfig writer1 = variantWriter(1);
    DataSetWriterConfig writer2 = variantWriter(2);
    WriterGroupConfig group = group(FULL_MASK, List.of(writer1, writer2), mode);

    return EncodeContext.of(
        encodingContext,
        PUBLISHER_ID,
        group,
        uint(5),
        ushort(1),
        ushort(16),
        new DateTime(3_000_000L),
        List.of(
            keyFrame(writer1, 11, goodValue(Variant.ofInt32(42))),
            keyFrame(writer2, 12, goodValue(Variant.ofInt32(43)))),
        securityContext);
  }

  private byte[] encodeToBytes(EncodeContext context) throws UaException {
    List<EncodedNetworkMessage> encoded = new UadpMessageMapping().encode(context);
    assertEquals(1, encoded.size());
    try {
      return ByteBufUtil.getBytes(encoded.get(0).data());
    } finally {
      encoded.get(0).data().release();
    }
  }

  private DecodedNetworkMessage decode(byte[] message, @Nullable SecurityContextResolver resolver) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      UadpDecodedMessage decoded =
          new UadpMessageMapping()
              .decodeMessage(DecodeContext.of(encodingContext, resolver), buffer);
      assertTrue(decoded instanceof DecodedNetworkMessage);
      return (DecodedNetworkMessage) decoded;
    } finally {
      buffer.release();
    }
  }

  private static SecurityKeyMaterial keyMaterial() throws UaException {
    return keyMaterial(PubSubSecurityPolicy.PubSubAes256Ctr);
  }

  private static SecurityKeyMaterial keyMaterial(PubSubSecurityPolicy policy) throws UaException {
    return SecurityKeyMaterial.split(policy, ByteString.of(keyData(policy)));
  }

  /** The fixed key data: {@code 00 01 02 ...} over the policy's 52- or 68-byte length. */
  private static byte[] keyData(PubSubSecurityPolicy policy) {
    return sequentialBytes(policy.getKeyDataLength());
  }

  private static SecurityContextResolver resolverFor(SecurityKeyMaterial keys) {
    return request -> new SecurityContextResolver.Resolution.Keys(keys);
  }

  /**
   * Build the expected secured bytes from the mode-None encoding: set ExtendedFlags1 bit 4, insert
   * the SecurityHeader at {@code securityHeaderOffset}, optionally AES-CTR the payload, and append
   * the HMAC-SHA256 signature — all with {@code javax.crypto} directly, independent of the
   * production helpers.
   */
  private static byte[] spliceSecured(
      byte[] plain,
      int securityHeaderOffset,
      int securityFlags,
      boolean encrypt,
      PubSubSecurityPolicy policy)
      throws Exception {

    byte[] payload = Arrays.copyOfRange(plain, securityHeaderOffset, plain.length);
    if (encrypt) {
      payload = ctr(policy, payload);
    }

    byte[] expected = new byte[plain.length + SECURITY_HEADER_SIZE + SIGNATURE_SIZE];
    System.arraycopy(plain, 0, expected, 0, securityHeaderOffset);
    expected[1] |= 0x10; // ExtendedFlags1: SecurityHeader enabled

    int i = securityHeaderOffset;
    expected[i++] = (byte) securityFlags;
    expected[i++] = TOKEN_ID; // SecurityTokenId UInt32 LE
    expected[i++] = 0;
    expected[i++] = 0;
    expected[i++] = 0;
    expected[i++] = 8; // NonceLength
    System.arraycopy(MESSAGE_NONCE, 0, expected, i, 8);
    i += 8;
    System.arraycopy(payload, 0, expected, i, payload.length);
    i += payload.length;

    byte[] signature = hmac(policy, expected, i);
    System.arraycopy(signature, 0, expected, i, SIGNATURE_SIZE);

    return expected;
  }

  /**
   * AES-CTR (128 or 256 per the policy's EncryptingKey = keyData[32, 32+len)) with IV = KeyNonce(4)
   * || MessageNonce(8) || 00 00 00 01 (big-endian block counter, Table 157).
   */
  private static byte[] ctr(PubSubSecurityPolicy policy, byte[] data) throws Exception {
    byte[] keyData = keyData(policy);
    byte[] encryptingKey = Arrays.copyOfRange(keyData, 32, 32 + policy.getEncryptingKeyLength());
    byte[] keyNonce =
        Arrays.copyOfRange(keyData, 32 + policy.getEncryptingKeyLength(), keyData.length);

    byte[] iv = new byte[16];
    System.arraycopy(keyNonce, 0, iv, 0, 4);
    System.arraycopy(MESSAGE_NONCE, 0, iv, 4, 8);
    iv[15] = 0x01;

    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(
        Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptingKey, "AES"), new IvParameterSpec(iv));
    return cipher.doFinal(data);
  }

  /** HMAC-SHA256 with SigningKey = keyData[0, 32) over {@code data[0, length)}. */
  private static byte[] hmac(PubSubSecurityPolicy policy, byte[] data, int length)
      throws Exception {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(Arrays.copyOfRange(keyData(policy), 0, 32), "HmacSHA256"));
    mac.update(data, 0, length);
    return mac.doFinal();
  }

  /** Recompute and replace the trailing 32-byte signature of a hand-modified Aes256 message. */
  private static void resign(byte[] message) throws Exception {
    byte[] signature =
        hmac(PubSubSecurityPolicy.PubSubAes256Ctr, message, message.length - SIGNATURE_SIZE);
    System.arraycopy(signature, 0, message, message.length - SIGNATURE_SIZE, SIGNATURE_SIZE);
  }

  private static byte[] sequentialBytes(int length) {
    byte[] bytes = new byte[length];
    for (int i = 0; i < length; i++) {
      bytes[i] = (byte) i;
    }
    return bytes;
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

  private static DataSetWriterConfig variantWriter(int dataSetWriterId) {
    return DataSetWriterConfig.builder("writer-" + dataSetWriterId)
        .dataSet(new PublishedDataSetRef("ds"))
        .dataSetWriterId(ushort(dataSetWriterId))
        .fieldContentMask(new DataSetFieldContentMask(uint(0)))
        .settings(
            UadpDataSetWriterSettings.builder()
                .dataSetMessageContentMask(new UadpDataSetMessageContentMask(uint(0)))
                .build())
        .build();
  }

  private static WriterGroupConfig group(
      UadpNetworkMessageContentMask networkMessageContentMask,
      List<DataSetWriterConfig> writers,
      MessageSecurityMode mode) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder("group")
            .writerGroupId(WRITER_GROUP_ID)
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(networkMessageContentMask)
                    .build());

    if (mode != MessageSecurityMode.None) {
      builder.messageSecurity(MessageSecurityConfig.builder().mode(mode).build());
    }

    for (DataSetWriterConfig writer : writers) {
      builder.dataSetWriter(writer);
    }
    return builder.build();
  }

  private static DataSetMessageDraft keyFrame(
      DataSetWriterConfig writer, int sequenceNumber, DataValue... fields) {

    return DataSetMessageDraft.of(
        writer,
        uint(sequenceNumber),
        null,
        null,
        new ConfigurationVersionDataType(uint(0), uint(0)),
        false,
        List.of(fields));
  }

  // endregion
}
