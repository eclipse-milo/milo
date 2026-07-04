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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.crypto.Mac;
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
 * Decode tolerance for SECURED UADP NetworkMessages (Part 14 §7.2.4.4.3): whatever is done to a
 * signed (and possibly encrypted) message, the decoder never throws and never surfaces payload from
 * a message whose signature did not verify. Tampering and refusals are header-only skips with a
 * typed {@link SecurityOutcome} and no {@link DecodedNetworkMessage#failure()}; only the truncation
 * signatures — a SecurityHeader NonceLength promising more bytes than arrived, or a message too
 * short for its promised SecurityFooter and Signature (an INVALID_SIGNATURE drop: no truncation can
 * verify) — also report a {@code Bad_DecodingError} failure.
 *
 * <p>All vectors use the wire-security worked layout, guarded byte-exactly by {@link
 * UadpSecurityGoldenVectorTest}: PublisherId UInt16, full GroupHeader, 2-DataSetMessage
 * PayloadHeader, Timestamp — SecurityFlags at offset 28, SecurityTokenId at 29, NonceLength at 33,
 * MessageNonce at 34, payload at 42, trailing 32-byte signature. Header-form splices re-sign with
 * {@code javax.crypto} directly, never via the production helpers.
 */
class UadpSecurityDecodeToleranceTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(0x1234));
  private static final UShort WRITER_GROUP_ID = ushort(258);

  /** PublisherId | GroupHeader (all four fields) | PayloadHeader | Timestamp. */
  private static final UadpNetworkMessageContentMask FULL_MASK =
      new UadpNetworkMessageContentMask(uint(0xFF));

  // SecurityHeader offsets of the FULL_MASK layout (wire-security §12).
  private static final int FLAGS_OFFSET = 28;
  private static final int TOKEN_OFFSET = 29;
  private static final int NONCE_LENGTH_OFFSET = 33;
  private static final int NONCE_OFFSET = 34;
  private static final int PAYLOAD_OFFSET = 42;

  private static final int SIGNATURE_SIZE = 32;

  private static final byte[] MESSAGE_NONCE = {
    (byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, 0x01, 0x00, 0x00, 0x00
  };

  private static final int TOKEN_ID = 7;

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  private static Stream<Arguments> securityCells() {
    return Stream.of(
        Arguments.of(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes256Ctr),
        Arguments.of(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes256Ctr));
  }

  // region tamper

  /**
   * Flipping one bit ANYWHERE in a secured NetworkMessage never throws and never yields a verified
   * result: every byte of the message is covered by the HMAC (the signature bytes select it), so
   * any modification either breaks verification or derails the plaintext header parse into a
   * tolerated skip. Flips that leave the header structure intact — the SecurityTokenId and
   * everything from the MessageNonce to the last signature byte — must fail verification exactly:
   * outcome {@link SecurityOutcome#INVALID_SIGNATURE}, header-only, no failure (a ciphertext flip
   * failing with INVALID_SIGNATURE rather than DECRYPT_FAILED is the verify-before-decrypt proof).
   */
  @ParameterizedTest(name = "{0} × {1}")
  @MethodSource("securityCells")
  void flippedBitAnywhereNeverThrowsAndNeverVerifies(
      MessageSecurityMode mode, PubSubSecurityPolicy policy) throws Exception {

    byte[] secured = encode(mode, policy);
    SecurityContextResolver resolver = resolverFor(keyMaterial(policy));

    for (int index = 0; index < secured.length; index++) {
      byte[] tampered = secured.clone();
      tampered[index] ^= 0x01;

      UadpDecodedMessage result = decodeMessage(tampered, resolver);

      if (result instanceof DecodedNetworkMessage decoded && decoded.security() != null) {
        assertNotEquals(SecurityOutcome.VERIFIED, decoded.security().outcome(), "index " + index);
        assertTrue(decoded.messages().isEmpty(), "index " + index);
        assertTrue(decoded.metaData().isEmpty(), "index " + index);
        assertNull(decoded.chunk(), "index " + index);
      }

      boolean structureIntact =
          (index >= TOKEN_OFFSET && index < TOKEN_OFFSET + 4) || index >= NONCE_OFFSET;
      if (structureIntact) {
        DecodedNetworkMessage decoded = assertInstanceOf(DecodedNetworkMessage.class, result);
        assertNotNull(decoded.security(), "index " + index);
        assertEquals(
            SecurityOutcome.INVALID_SIGNATURE, decoded.security().outcome(), "index " + index);
        assertNull(decoded.failure(), "a tamper drop is a skip, not a failure; index " + index);
        // the plaintext header (as received) is still surfaced
        assertEquals(WRITER_GROUP_ID, decoded.writerGroupId(), "index " + index);
      }
    }
  }

  // endregion

  // region truncation

  /**
   * Truncating a secured NetworkMessage at EVERY prefix length never throws and never yields
   * payload: the payload is only parsed after verification, and no truncation can verify.
   */
  @ParameterizedTest(name = "{0} × {1}")
  @MethodSource("securityCells")
  void truncationAtEveryPrefixNeverThrowsAndNeverYieldsPayload(
      MessageSecurityMode mode, PubSubSecurityPolicy policy) throws Exception {

    byte[] secured = encode(mode, policy);
    SecurityContextResolver resolver = resolverFor(keyMaterial(policy));

    // Sanity: the full message verifies and decodes.
    DecodedNetworkMessage full =
        assertInstanceOf(DecodedNetworkMessage.class, decodeMessage(secured, resolver));
    assertNotNull(full.security());
    assertEquals(SecurityOutcome.VERIFIED, full.security().outcome());
    assertEquals(2, full.messages().size());

    for (int length = 0; length < secured.length; length++) {
      byte[] truncated = Arrays.copyOf(secured, length);

      UadpDecodedMessage result = decodeMessage(truncated, resolver);

      assertNotNull(result, "cut at " + length);
      if (result instanceof DecodedNetworkMessage decoded) {
        assertTrue(decoded.messages().isEmpty(), "cut at " + length);
        assertTrue(decoded.metaData().isEmpty(), "cut at " + length);
        assertNull(decoded.chunk(), "cut at " + length);
        if (decoded.security() != null) {
          assertNotEquals(
              SecurityOutcome.VERIFIED, decoded.security().outcome(), "cut at " + length);
        }
      }
    }
  }

  // endregion

  // region reserved flags / footer / header forms

  /**
   * Each reserved SecurityFlags bit (4-7) skips the whole message like other reserved flag values:
   * no security record, no failure — and the resolver is never consulted.
   */
  @Test
  void reservedSecurityFlagsBitsSkipMessageWithoutReachingTheResolver() throws Exception {
    byte[] secured =
        encode(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes256Ctr);

    for (int bit : new int[] {0x10, 0x20, 0x40, 0x80}) {
      byte[] modified = secured.clone();
      modified[FLAGS_OFFSET] |= (byte) bit;

      var requests = new ArrayList<SecurityContextResolver.ResolveRequest>();
      SecurityKeyMaterial keys = keyMaterial(PubSubSecurityPolicy.PubSubAes256Ctr);
      SecurityContextResolver countingResolver =
          request -> {
            requests.add(request);
            return new SecurityContextResolver.Resolution.Keys(keys);
          };

      DecodedNetworkMessage decoded =
          assertInstanceOf(DecodedNetworkMessage.class, decodeMessage(modified, countingResolver));

      assertNull(decoded.security(), "bit 0x" + Integer.toHexString(bit));
      assertNull(decoded.failure(), "bit 0x" + Integer.toHexString(bit));
      assertTrue(decoded.messages().isEmpty(), "bit 0x" + Integer.toHexString(bit));
      assertTrue(requests.isEmpty(), "resolver reached for bit 0x" + Integer.toHexString(bit));
    }
  }

  /**
   * A flagged SecurityFooter on an ENCRYPTED message is skipped via SecurityFooterSize: the payload
   * window boundary math excludes the footer AND the signature, so the ciphertext region — which
   * sits between the SecurityHeader and the footer — still decrypts and parses. (The sign-only
   * footer form is covered by {@link UadpSecurityGoldenVectorTest}.)
   */
  @Test
  void securityFooterOnEncryptedMessageIsSkipped() throws Exception {
    byte[] secured =
        encode(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes256Ctr);

    int footerSize = 4;
    int signatureStart = secured.length - SIGNATURE_SIZE;

    // set SecurityFlags bit 2, insert SecurityFooterSize after the nonce and the footer bytes
    // between the (encrypted) payload and the signature, then re-sign: the footer is inside the
    // signed region but outside the encrypted one
    byte[] modified = new byte[secured.length + 2 + footerSize];
    System.arraycopy(secured, 0, modified, 0, PAYLOAD_OFFSET);
    modified[FLAGS_OFFSET] = 0x07; // signed | encrypted | footer
    modified[PAYLOAD_OFFSET] = (byte) footerSize; // SecurityFooterSize UInt16 LE
    modified[PAYLOAD_OFFSET + 1] = 0;
    System.arraycopy(
        secured, PAYLOAD_OFFSET, modified, PAYLOAD_OFFSET + 2, signatureStart - PAYLOAD_OFFSET);
    int footerStart = PAYLOAD_OFFSET + 2 + (signatureStart - PAYLOAD_OFFSET);
    for (int i = 0; i < footerSize; i++) {
      modified[footerStart + i] = (byte) 0xEE;
    }
    resign(modified);

    DecodedNetworkMessage decoded =
        assertInstanceOf(
            DecodedNetworkMessage.class,
            decodeMessage(request -> {}, modified, PubSubSecurityPolicy.PubSubAes256Ctr));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
    assertNull(decoded.failure());
    assertEquals(2, decoded.messages().size());
  }

  /**
   * Both sign-only SecurityHeader forms decode, with NonceLength self-describing: the Annex A
   * A.2.1.5 form the encoder emits — real token id, 8-byte nonce — and the literal Table 154
   * reading — SecurityTokenId 0, NonceLength 0, no nonce. The resolver sees the token id as
   * received either way.
   */
  @Test
  void bothSignOnlyHeaderFormsAreAccepted() throws Exception {
    // Annex A form: the encoder's own output
    byte[] annexA = encode(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes256Ctr);
    var annexARequests = new ArrayList<SecurityContextResolver.ResolveRequest>();
    DecodedNetworkMessage annexADecoded =
        assertInstanceOf(
            DecodedNetworkMessage.class,
            decodeMessage(annexARequests::add, annexA, PubSubSecurityPolicy.PubSubAes256Ctr));

    assertNotNull(annexADecoded.security());
    assertEquals(SecurityOutcome.VERIFIED, annexADecoded.security().outcome());
    assertEquals(2, annexADecoded.messages().size());
    assertEquals(1, annexARequests.size());
    assertEquals(uint(TOKEN_ID), annexARequests.get(0).securityTokenId());

    // literal Table 154 form: token 0, NonceLength 0, no nonce bytes
    byte[] literal = new byte[annexA.length - 8];
    System.arraycopy(annexA, 0, literal, 0, NONCE_OFFSET);
    for (int i = TOKEN_OFFSET; i < TOKEN_OFFSET + 4; i++) {
      literal[i] = 0; // SecurityTokenId = 0
    }
    literal[NONCE_LENGTH_OFFSET] = 0; // NonceLength = 0
    System.arraycopy(
        annexA, NONCE_OFFSET + 8, literal, NONCE_OFFSET, annexA.length - (NONCE_OFFSET + 8));
    resign(literal);

    var literalRequests = new ArrayList<SecurityContextResolver.ResolveRequest>();
    DecodedNetworkMessage literalDecoded =
        assertInstanceOf(
            DecodedNetworkMessage.class,
            decodeMessage(literalRequests::add, literal, PubSubSecurityPolicy.PubSubAes256Ctr));

    assertNotNull(literalDecoded.security());
    assertEquals(SecurityOutcome.VERIFIED, literalDecoded.security().outcome());
    assertNull(literalDecoded.failure());
    assertEquals(2, literalDecoded.messages().size());
    assertEquals(1, literalRequests.size());
    assertEquals(uint(0), literalRequests.get(0).securityTokenId());
  }

  /**
   * An ENCRYPTED message whose nonce cannot seed the cipher — NonceLength 0 here, 4 in {@link
   * UadpSecurityGoldenVectorTest} — verifies but then drops with {@link
   * SecurityOutcome#DECRYPT_FAILED}: a tolerated skip, not a failure.
   */
  @Test
  void encryptedMessageWithZeroNonceLengthIsDecryptFailed() throws Exception {
    byte[] secured =
        encode(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes256Ctr);

    byte[] modified = new byte[secured.length - 8];
    System.arraycopy(secured, 0, modified, 0, NONCE_OFFSET);
    modified[NONCE_LENGTH_OFFSET] = 0;
    System.arraycopy(
        secured, NONCE_OFFSET + 8, modified, NONCE_OFFSET, secured.length - (NONCE_OFFSET + 8));
    resign(modified);

    DecodedNetworkMessage decoded =
        assertInstanceOf(
            DecodedNetworkMessage.class,
            decodeMessage(request -> {}, modified, PubSubSecurityPolicy.PubSubAes256Ctr));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.DECRYPT_FAILED, decoded.security().outcome());
    assertTrue(decoded.messages().isEmpty());
    assertNull(decoded.failure());
    assertEquals(WRITER_GROUP_ID, decoded.writerGroupId());
  }

  /**
   * A SIGN-ONLY message with an oversized nonce (16 bytes) still verifies: NonceLength is
   * self-describing on decode, and sign-only never feeds the nonce to a cipher.
   */
  @Test
  void signOnlyMessageWithOversizedNonceStillVerifies() throws Exception {
    byte[] secured = encode(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes256Ctr);

    byte[] modified = new byte[secured.length + 8];
    System.arraycopy(secured, 0, modified, 0, PAYLOAD_OFFSET);
    modified[NONCE_LENGTH_OFFSET] = 16;
    for (int i = 0; i < 8; i++) {
      modified[PAYLOAD_OFFSET + i] = (byte) 0xCC; // 8 extra nonce bytes
    }
    System.arraycopy(
        secured, PAYLOAD_OFFSET, modified, PAYLOAD_OFFSET + 8, secured.length - PAYLOAD_OFFSET);
    resign(modified);

    DecodedNetworkMessage decoded =
        assertInstanceOf(
            DecodedNetworkMessage.class,
            decodeMessage(request -> {}, modified, PubSubSecurityPolicy.PubSubAes256Ctr));

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome());
    assertNull(decoded.failure());
    assertEquals(2, decoded.messages().size());
  }

  // endregion

  // region None-configured receiver

  /**
   * A receiver with no security infrastructure — a resolver-less {@link DecodeContext}, the
   * mode-None configuration — decodes secured bytes to a TYPED refusal on both decode surfaces:
   * outcome {@link SecurityOutcome#NO_RESOLVER}, header surfaced, no failure, no exception.
   */
  @ParameterizedTest(name = "{0} × {1}")
  @MethodSource("securityCells")
  void noneConfiguredDecodeOfSecuredBytesIsATypedRefusal(
      MessageSecurityMode mode, PubSubSecurityPolicy policy) throws Exception {

    byte[] secured = encode(mode, policy);

    // the discovery-aware surface
    DecodedNetworkMessage decoded =
        assertInstanceOf(DecodedNetworkMessage.class, decodeMessage(secured, null));
    assertRefusedNoResolver(decoded, mode);

    // the legacy surface
    ByteBuf buffer = Unpooled.wrappedBuffer(secured);
    try {
      DecodedNetworkMessage legacyDecoded =
          new UadpMessageMapping().decode(DecodeContext.of(encodingContext), buffer);
      assertRefusedNoResolver(legacyDecoded, mode);
    } finally {
      buffer.release();
    }
  }

  private static void assertRefusedNoResolver(
      DecodedNetworkMessage decoded, MessageSecurityMode mode) {

    assertNotNull(decoded.security());
    assertEquals(SecurityOutcome.NO_RESOLVER, decoded.security().outcome());
    assertEquals(mode, decoded.security().mode());
    assertEquals(uint(TOKEN_ID), decoded.security().securityTokenId());
    assertTrue(decoded.messages().isEmpty());
    assertTrue(decoded.metaData().isEmpty());
    assertNull(decoded.failure());
    // the plaintext header is still decoded
    assertEquals(PUBLISHER_ID, decoded.publisherId());
    assertEquals(WRITER_GROUP_ID, decoded.writerGroupId());
  }

  // endregion

  // region helpers

  private byte[] encode(MessageSecurityMode mode, PubSubSecurityPolicy policy) throws UaException {
    DataSetWriterConfig writer1 = variantWriter(1);
    DataSetWriterConfig writer2 = variantWriter(2);
    WriterGroupConfig group = group(List.of(writer1, writer2), mode);

    var securityContext =
        new MessageSecurityContext(mode, uint(TOKEN_ID), keyMaterial(policy), MESSAGE_NONCE::clone);

    EncodeContext context =
        EncodeContext.of(
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

    List<EncodedNetworkMessage> encoded = new UadpMessageMapping().encode(context);
    assertEquals(1, encoded.size());
    try {
      return ByteBufUtil.getBytes(encoded.get(0).data());
    } finally {
      encoded.get(0).data().release();
    }
  }

  private UadpDecodedMessage decodeMessage(
      byte[] message, @Nullable SecurityContextResolver resolver) {

    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return new UadpMessageMapping()
          .decodeMessage(DecodeContext.of(encodingContext, resolver), buffer);
    } finally {
      buffer.release();
    }
  }

  /**
   * Decode with a fixed-key resolver that also feeds each {@code ResolveRequest} to {@code spy}.
   */
  private UadpDecodedMessage decodeMessage(
      RequestSpy spy, byte[] message, PubSubSecurityPolicy policy) throws UaException {

    SecurityKeyMaterial keys = keyMaterial(policy);
    return decodeMessage(
        message,
        request -> {
          spy.accept(request);
          return new SecurityContextResolver.Resolution.Keys(keys);
        });
  }

  @FunctionalInterface
  private interface RequestSpy {
    void accept(SecurityContextResolver.ResolveRequest request);
  }

  private static SecurityKeyMaterial keyMaterial(PubSubSecurityPolicy policy) throws UaException {
    return SecurityKeyMaterial.split(policy, ByteString.of(keyData(policy)));
  }

  /** The fixed key data: {@code 00 01 02 ...} over the policy's 52- or 68-byte length. */
  private static byte[] keyData(PubSubSecurityPolicy policy) {
    byte[] bytes = new byte[policy.getKeyDataLength()];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) i;
    }
    return bytes;
  }

  private static SecurityContextResolver resolverFor(SecurityKeyMaterial keys) {
    return request -> new SecurityContextResolver.Resolution.Keys(keys);
  }

  /**
   * Recompute and replace the trailing 32-byte signature of a hand-modified Aes256 message, with
   * {@code javax.crypto} directly (independent of the production helpers).
   */
  private static void resign(byte[] message) throws Exception {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(
        new SecretKeySpec(
            Arrays.copyOfRange(keyData(PubSubSecurityPolicy.PubSubAes256Ctr), 0, 32),
            "HmacSHA256"));
    mac.update(message, 0, message.length - SIGNATURE_SIZE);
    byte[] signature = mac.doFinal();
    System.arraycopy(signature, 0, message, message.length - SIGNATURE_SIZE, SIGNATURE_SIZE);
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
      List<DataSetWriterConfig> writers, MessageSecurityMode mode) {

    WriterGroupConfig.Builder builder =
        WriterGroupConfig.builder("group")
            .writerGroupId(WRITER_GROUP_ID)
            .messageSettings(
                UadpWriterGroupSettings.builder().networkMessageContentMask(FULL_MASK).build())
            .messageSecurity(MessageSecurityConfig.builder().mode(mode).build());

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
