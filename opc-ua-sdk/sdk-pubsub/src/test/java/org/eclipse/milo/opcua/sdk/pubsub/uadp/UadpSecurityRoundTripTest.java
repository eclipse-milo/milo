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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Encode/decode round trips for the UADP codec with message security on, across the {@link
 * UadpRoundTripTest} network-content-mask matrix × {Sign, SignAndEncrypt} × {PubSub-Aes128-CTR,
 * PubSub-Aes256-CTR} (K2, K5).
 *
 * <p>Each cell asserts three things:
 *
 * <ul>
 *   <li>Decoded model equality: the secured bytes decode — through a fixed-key {@link
 *       SecurityContextResolver} — to the same DataSetMessages and header values as the mode-None
 *       encoding of the identical context, with outcome {@link SecurityOutcome#VERIFIED}.
 *   <li>Input-buffer immutability: after decoding, the shared input buffer is BYTE-IDENTICAL to the
 *       encoder's output — the decrypt-a-COPY regression guard. The dispatcher hands the same
 *       underlying memory to every mapping provider, so an in-place decrypt "optimization" would
 *       corrupt every decode after the first.
 *   <li>Second-decode equivalence: decoding a second slice of the same buffer — exactly what the
 *       dispatcher does with multiple mapping providers — yields the same verified result.
 * </ul>
 */
class UadpSecurityRoundTripTest {

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(4660));

  private static final UShort WRITER_GROUP_ID = ushort(258);
  private static final UInteger GROUP_VERSION = uint(123456);
  private static final UShort NETWORK_MESSAGE_NUMBER = ushort(3);
  private static final UShort NETWORK_MESSAGE_SEQUENCE = ushort(99);
  private static final DateTime NETWORK_MESSAGE_TIMESTAMP = new DateTime(987654321L);

  private static final DateTime DSM_TIMESTAMP = new DateTime(1_000_000L);

  /** Uncertain_SubNormal (0x40950000); the wire carries only the high 16 bits. */
  private static final StatusCode DSM_STATUS = new StatusCode(0x40950000L);

  private static final ConfigurationVersionDataType CONFIGURATION_VERSION =
      new ConfigurationVersionDataType(uint(5), uint(6));

  /** All six optional DataSetMessage header fields (Table 101). */
  private static final UadpDataSetMessageContentMask DSM_MASK =
      new UadpDataSetMessageContentMask(uint(0x3F));

  private static final byte[] MESSAGE_NONCE = {
    (byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, 0x01, 0x00, 0x00, 0x00
  };

  private static final UInteger TOKEN_ID = uint(7);

  /** Field values of varied builtin types, including an array and a null value. */
  private static final List<DataValue> FIELDS =
      List.of(
          new DataValue(
              Variant.ofInt32(42),
              StatusCode.GOOD,
              new DateTime(101L),
              ushort(1),
              new DateTime(201L),
              ushort(2)),
          new DataValue(
              Variant.ofString("hello"),
              StatusCode.GOOD,
              new DateTime(102L),
              null,
              new DateTime(202L),
              null),
          new DataValue(
              Variant.ofInt32Array(new Integer[] {1, 2, 3}),
              StatusCode.GOOD,
              new DateTime(103L),
              null,
              new DateTime(203L),
              null),
          new DataValue(Variant.NULL_VALUE, StatusCode.GOOD, null, null, null, null),
          new DataValue(
              Variant.ofDouble(3.5),
              StatusCode.GOOD,
              new DateTime(104L),
              null,
              new DateTime(204L),
              null));

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  private static Stream<Arguments> securedMaskMatrix() {
    // UadpNetworkMessageContentMask combinations, mirroring UadpRoundTripTest (Part 14 Table 97);
    // they vary everything the security transform interacts with: presence of ExtendedFlags1,
    // the PayloadHeader (and thus the encrypted Sizes array), and the header length in front of
    // the SecurityHeader.
    List<Long> networkMasks =
        List.of(
            0x000L, // nothing: ExtendedFlags1 previously ABSENT (+1 byte under security)
            0x001L, // PublisherId
            0x041L, // PublisherId | PayloadHeader (Annex A.2.2 "UADP-Dynamic")
            0x006L, // GroupHeader | WriterGroupId
            0x02AL, // GroupHeader | GroupVersion | SequenceNumber
            0x07FL, // PublisherId | full GroupHeader | PayloadHeader (multi-writer Sizes)
            0x0C1L, // PublisherId | PayloadHeader | Timestamp
            0x1C1L, // PublisherId | PayloadHeader | Timestamp | PicoSeconds
            0x241L, // PublisherId | PayloadHeader | DataSetClassId
            0x3FFL); // everything supported (all bits except PromotedFields)

    // DataSetFieldContentMask: Variant encoding vs DataValue with all five members (Table 32).
    List<Long> fieldMasks = List.of(0x00L, 0x1FL);

    record Cell(MessageSecurityMode mode, PubSubSecurityPolicy policy) {}
    List<Cell> cells =
        List.of(
            new Cell(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes128Ctr),
            new Cell(MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes256Ctr),
            new Cell(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes128Ctr),
            new Cell(MessageSecurityMode.SignAndEncrypt, PubSubSecurityPolicy.PubSubAes256Ctr));

    List<Arguments> combinations = new ArrayList<>();
    for (long networkMask : networkMasks) {
      for (long fieldMask : fieldMasks) {
        for (Cell cell : cells) {
          combinations.add(
              Arguments.of(
                  String.format(
                      "nm=0x%03X field=0x%02X %s %s",
                      networkMask, fieldMask, cell.mode(), cell.policy()),
                  new UadpNetworkMessageContentMask(uint(networkMask)),
                  new DataSetFieldContentMask(uint(fieldMask)),
                  cell.mode(),
                  cell.policy()));
        }
      }
    }
    return combinations.stream();
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("securedMaskMatrix")
  void roundTripAcrossMaskMatrixWithSecurity(
      String label,
      UadpNetworkMessageContentMask networkMask,
      DataSetFieldContentMask fieldMask,
      MessageSecurityMode mode,
      PubSubSecurityPolicy policy)
      throws Exception {

    // Without a PayloadHeader the decoder assumes a single DataSetMessage; with one, use two
    // writers so the Sizes array is present — inside the encrypted region (Table 161).
    int writerCount = networkMask.getPayloadHeader() ? 2 : 1;

    byte[] plain = encode(networkMask, fieldMask, writerCount, MessageSecurityMode.None, null);

    SecurityKeyMaterial keys = keyMaterial(policy);
    var securityContext = new MessageSecurityContext(mode, TOKEN_ID, keys, MESSAGE_NONCE::clone);
    byte[] secured = encode(networkMask, fieldMask, writerCount, mode, securityContext);

    // The per-message security overhead (wire-security §5.3): 14-byte SecurityHeader + 32-byte
    // signature, +1 when the mode-None message had no ExtendedFlags1 byte to set bit 4 in.
    boolean plainHasExtendedFlags1 =
        networkMask.getPublisherId() // the UInt16 PublisherId type bits are non-zero
            || networkMask.getTimestamp() // PicoSeconds only counts alongside Timestamp
            || networkMask.getDataSetClassId();
    assertEquals(plain.length + 46 + (plainHasExtendedFlags1 ? 0 : 1), secured.length, label);

    DecodedNetworkMessage plainDecoded = decode(plain, null);

    SecurityContextResolver resolver = request -> new SecurityContextResolver.Resolution.Keys(keys);

    // Decode from a shared buffer the way the dispatcher does: the same underlying memory is
    // offered to every mapping provider as a slice.
    byte[] original = secured.clone();
    ByteBuf buffer = Unpooled.wrappedBuffer(secured);
    try {
      DecodedNetworkMessage first = decodeSlice(buffer, resolver);
      DecodedNetworkMessage second = decodeSlice(buffer, resolver);

      // the decrypt-a-copy guard: the shared input buffer is byte-identical after decoding
      assertArrayEquals(original, ByteBufUtil.getBytes(buffer, 0, buffer.writerIndex()), label);

      for (DecodedNetworkMessage decoded : List.of(first, second)) {
        assertNotNull(decoded.security(), label);
        assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome(), label);
        assertEquals(mode, decoded.security().mode(), label);
        assertEquals(TOKEN_ID, decoded.security().securityTokenId(), label);
        assertNull(decoded.failure(), label);

        // decoded field equality with the mode-None encoding of the identical context
        assertEquals(plainDecoded.messages(), decoded.messages(), label);
        assertEquals(plainDecoded.publisherId(), decoded.publisherId(), label);
        assertEquals(plainDecoded.writerGroupId(), decoded.writerGroupId(), label);
        assertEquals(plainDecoded.groupVersion(), decoded.groupVersion(), label);
        assertEquals(plainDecoded.networkMessageNumber(), decoded.networkMessageNumber(), label);
        assertEquals(plainDecoded.sequenceNumber(), decoded.sequenceNumber(), label);
        assertEquals(plainDecoded.timestamp(), decoded.timestamp(), label);
      }
    } finally {
      buffer.release();
    }
  }

  // region helpers

  private byte[] encode(
      UadpNetworkMessageContentMask networkMask,
      DataSetFieldContentMask fieldMask,
      int writerCount,
      MessageSecurityMode mode,
      @Nullable MessageSecurityContext securityContext)
      throws UaException {

    List<DataSetWriterConfig> writers = new ArrayList<>(writerCount);
    for (int i = 1; i <= writerCount; i++) {
      writers.add(writer(i, fieldMask));
    }

    WriterGroupConfig.Builder groupBuilder =
        WriterGroupConfig.builder("group")
            .writerGroupId(WRITER_GROUP_ID)
            .messageSettings(
                UadpWriterGroupSettings.builder().networkMessageContentMask(networkMask).build());
    if (mode != MessageSecurityMode.None) {
      groupBuilder.messageSecurity(MessageSecurityConfig.builder().mode(mode).build());
    }
    writers.forEach(groupBuilder::dataSetWriter);
    WriterGroupConfig group = groupBuilder.build();

    List<DataSetMessageDraft> drafts = new ArrayList<>(writerCount);
    for (int i = 0; i < writerCount; i++) {
      drafts.add(
          DataSetMessageDraft.of(
              writers.get(i),
              uint(11 + i),
              DSM_TIMESTAMP,
              DSM_STATUS,
              CONFIGURATION_VERSION,
              false,
              FIELDS));
    }

    EncodeContext context =
        EncodeContext.of(
            encodingContext,
            PUBLISHER_ID,
            group,
            GROUP_VERSION,
            NETWORK_MESSAGE_NUMBER,
            NETWORK_MESSAGE_SEQUENCE,
            NETWORK_MESSAGE_TIMESTAMP,
            drafts,
            securityContext);

    List<EncodedNetworkMessage> encoded = new UadpMessageMapping().encode(context);
    assertEquals(1, encoded.size());
    try {
      return ByteBufUtil.getBytes(encoded.get(0).data());
    } finally {
      encoded.get(0).data().release();
    }
  }

  private static DataSetWriterConfig writer(
      int dataSetWriterId, DataSetFieldContentMask fieldMask) {

    return DataSetWriterConfig.builder("writer-" + dataSetWriterId)
        .dataSet(new PublishedDataSetRef("ds"))
        .dataSetWriterId(ushort(dataSetWriterId))
        .fieldContentMask(fieldMask)
        .settings(UadpDataSetWriterSettings.builder().dataSetMessageContentMask(DSM_MASK).build())
        .build();
  }

  private DecodedNetworkMessage decode(byte[] message, @Nullable SecurityContextResolver resolver) {
    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    try {
      return decodeSlice(buffer, resolver);
    } finally {
      buffer.release();
    }
  }

  private DecodedNetworkMessage decodeSlice(
      ByteBuf buffer, @Nullable SecurityContextResolver resolver) {

    UadpDecodedMessage decoded =
        new UadpMessageMapping()
            .decodeMessage(DecodeContext.of(encodingContext, resolver), buffer.slice());
    return assertInstanceOf(DecodedNetworkMessage.class, decoded);
  }

  private static SecurityKeyMaterial keyMaterial(PubSubSecurityPolicy policy) throws UaException {
    byte[] keyData = new byte[policy.getKeyDataLength()];
    for (int i = 0; i < keyData.length; i++) {
      keyData[i] = (byte) i;
    }
    return SecurityKeyMaterial.split(policy, ByteString.of(keyData));
  }

  // endregion
}
