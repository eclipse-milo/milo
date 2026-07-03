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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBufUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Drift guard for the exported computed security-vector fixtures under {@code
 * src/test/resources/security-vectors/computed/}: every {@code <name>.bin} must be byte-identical
 * to the Milo encoder's output for the same deterministic inputs, and every {@code
 * <name>.keys.json} must state exactly the constants those bytes were produced from.
 *
 * <p>{@link UadpSecurityGoldenVectorTest} is the source of truth for these vectors (it proves the
 * encoder against a fully hand-derived worked example and an independent {@code javax.crypto}
 * recompute); the fixtures exist so the out-of-JVM recompute harness {@code
 * security-vectors/check_security_vectors.py} (K20, manually run — see {@code
 * security-vectors/README.md}) has committed inputs. This test replays that test's exact encoding
 * context — same fixed {@link MessageNonceSupplier} nonce, token id, sequential key data, and
 * 2-writer worked layout — so neither side of the fixture pair can drift from the in-test vectors.
 */
class UadpSecurityVectorFixturesTest {

  private static final String COMPUTED_RESOURCE_DIR = "/security-vectors/computed/";

  private static final PublisherId PUBLISHER_ID = PublisherId.uint16(ushort(0x1234));
  private static final UShort WRITER_GROUP_ID = ushort(258);

  /** PublisherId | GroupHeader (all four fields) | PayloadHeader | Timestamp. */
  private static final UadpNetworkMessageContentMask FULL_MASK =
      new UadpNetworkMessageContentMask(uint(0xFF));

  /** Offset of the SecurityHeader for {@link #FULL_MASK} with two writers (wire-security §12). */
  private static final int SECURITY_HEADER_OFFSET = 28;

  private static final byte[] MESSAGE_NONCE = {
    (byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, 0x01, 0x00, 0x00, 0x00
  };

  private static final int TOKEN_ID = 7;

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  private static Stream<Arguments> computedVectors() {
    return Stream.of(
        Arguments.of(
            "milo-aes128ctr-sign", MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(
            "milo-aes128ctr-se",
            MessageSecurityMode.SignAndEncrypt,
            PubSubSecurityPolicy.PubSubAes128Ctr),
        Arguments.of(
            "milo-aes256ctr-sign", MessageSecurityMode.Sign, PubSubSecurityPolicy.PubSubAes256Ctr),
        Arguments.of(
            "milo-aes256ctr-se",
            MessageSecurityMode.SignAndEncrypt,
            PubSubSecurityPolicy.PubSubAes256Ctr));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("computedVectors")
  void fixtureBytesMatchEncoderOutput(
      String name, MessageSecurityMode mode, PubSubSecurityPolicy policy) throws Exception {

    byte[] fixture = readResource(name + ".bin");
    byte[] encoded = encode(mode, policy);

    assertArrayEquals(
        encoded,
        fixture,
        name + ".bin drifted from the encoder output pinned by UadpSecurityGoldenVectorTest");
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("computedVectors")
  void fixtureKeysJsonMatchesTestConstants(
      String name, MessageSecurityMode mode, PubSubSecurityPolicy policy) throws Exception {

    JsonObject keys =
        JsonParser.parseString(
                new String(readResource(name + ".keys.json"), StandardCharsets.UTF_8))
            .getAsJsonObject();

    assertEquals("computed", keys.get("direction").getAsString());
    assertEquals(policy.getUri(), keys.get("securityPolicyUri").getAsString());
    assertEquals(mode.name(), keys.get("securityMode").getAsString());
    assertEquals(TOKEN_ID, keys.get("securityTokenId").getAsInt());

    // keyData is canonical; the redundant split fields must agree with the production splitter
    byte[] keyData = keyData(policy);
    assertEquals(ByteBufUtil.hexDump(keyData), keys.get("keyData").getAsString());
    SecurityKeyMaterial material = SecurityKeyMaterial.split(policy, ByteString.of(keyData));
    assertEquals(
        ByteBufUtil.hexDump(material.getSigningKey()), keys.get("signingKey").getAsString());
    assertEquals(
        ByteBufUtil.hexDump(material.getEncryptingKey()), keys.get("encryptingKey").getAsString());
    assertEquals(ByteBufUtil.hexDump(material.getKeyNonce()), keys.get("keyNonce").getAsString());
    assertEquals(ByteBufUtil.hexDump(MESSAGE_NONCE), keys.get("messageNonce").getAsString());

    JsonObject publisherId = keys.get("publisherId").getAsJsonObject();
    assertEquals("UInt16", publisherId.get("type").getAsString());
    assertEquals(0x1234, publisherId.get("value").getAsInt());
    assertEquals(WRITER_GROUP_ID.intValue(), keys.get("writerGroupId").getAsInt());
    JsonArray dataSetWriterIds = keys.get("dataSetWriterIds").getAsJsonArray();
    assertEquals(2, dataSetWriterIds.size());
    assertEquals(1, dataSetWriterIds.get(0).getAsInt());
    assertEquals(2, dataSetWriterIds.get(1).getAsInt());

    // expectedPlaintext = the payload region before encryption = the mode-None encoding
    // of the same context from the SecurityHeader offset onward
    byte[] plain = encode(MessageSecurityMode.None, policy);
    byte[] payloadPlaintext = Arrays.copyOfRange(plain, SECURITY_HEADER_OFFSET, plain.length);
    assertEquals(
        ByteBufUtil.hexDump(payloadPlaintext), keys.get("expectedPlaintext").getAsString());
  }

  /** A fixture added without registration here would dodge the drift guard — forbid that. */
  @Test
  void computedFixtureSetIsExactlyTheRegisteredVectors() throws Exception {
    Set<String> expected =
        computedVectors()
            .map(arguments -> (String) arguments.get()[0])
            .flatMap(name -> Stream.of(name + ".bin", name + ".keys.json"))
            .collect(Collectors.toSet());

    URL url = UadpSecurityVectorFixturesTest.class.getResource(COMPUTED_RESOURCE_DIR);
    assertNotNull(url, COMPUTED_RESOURCE_DIR + " missing from the test classpath");
    try (Stream<Path> files = Files.list(Path.of(url.toURI()))) {
      Set<String> actual =
          files
              .map(path -> path.getFileName().toString())
              .filter(file -> file.endsWith(".bin") || file.endsWith(".keys.json"))
              .collect(Collectors.toSet());
      assertEquals(expected, actual);
    }
  }

  // region encoding helpers (constants and context identical to UadpSecurityGoldenVectorTest)

  private byte[] encode(MessageSecurityMode mode, PubSubSecurityPolicy policy) throws UaException {
    MessageSecurityContext securityContext = null;
    if (mode != MessageSecurityMode.None) {
      SecurityKeyMaterial keys = SecurityKeyMaterial.split(policy, ByteString.of(keyData(policy)));
      securityContext =
          new MessageSecurityContext(mode, uint(TOKEN_ID), keys, MESSAGE_NONCE::clone);
    }

    DataSetWriterConfig writer1 = variantWriter(1);
    DataSetWriterConfig writer2 = variantWriter(2);
    WriterGroupConfig group = group(List.of(writer1, writer2), mode);

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

  /** The fixed key data: {@code 00 01 02 ...} over the policy's 52- or 68-byte length. */
  private static byte[] keyData(PubSubSecurityPolicy policy) {
    byte[] bytes = new byte[policy.getKeyDataLength()];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) i;
    }
    return bytes;
  }

  private static byte[] readResource(String name) throws IOException {
    try (InputStream stream =
        UadpSecurityVectorFixturesTest.class.getResourceAsStream(COMPUTED_RESOURCE_DIR + name)) {
      assertNotNull(stream, COMPUTED_RESOURCE_DIR + name + " missing from the test classpath");
      return stream.readAllBytes();
    }
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
                UadpWriterGroupSettings.builder().networkMessageContentMask(FULL_MASK).build());

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
