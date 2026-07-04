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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/**
 * Captured decode-direction vectors: each committed fixture pair {@code
 * security-vectors/captured/<name>.bin} + {@code <name>.keys.json} is one secured NetworkMessage
 * captured from a third-party stack, then decoded through {@link UadpMessageMapping} with a
 * fixed-key {@link SecurityContextResolver} built from the fixture's key material.
 *
 * <p>Fixture pairs are discovered at runtime as test resources. Until they are present, the factory
 * yields a single always-visible "captures missing" marker: aborted by default, and failed under
 * {@code -Dmilo.pubsub.security.requireCapturedVectors=true}. That keeps absent decode-direction
 * evidence from looking like a green verification. The scaffolding self-tests below prove the
 * fixture machinery meanwhile. The {@code .keys.json} schema uses canonical {@code keyData}; the
 * redundant {@code signingKey}/{@code encryptingKey}/{@code keyNonce} fields, when present, are
 * cross-checked against the split.
 *
 * <p>{@code keys.json} parsing uses Gson (on this module's test classpath transitively via
 * milo-encoding-json), the same parser {@link UadpSecurityVectorFixturesTest} uses for the computed
 * fixtures — one parsing path for the shared schema.
 */
class UadpSecurityCapturedVectorTest {

  private static final String CAPTURED_RESOURCE_DIR = "/security-vectors/captured";

  /** Set (e.g. {@code -D...=true}) to make the missing-captures marker FAIL instead of abort. */
  private static final String REQUIRE_CAPTURES_PROPERTY =
      "milo.pubsub.security.requireCapturedVectors";

  private final EncodingContext encodingContext = new DefaultEncodingContext();

  // region fixture-driven dynamic tests

  @TestFactory
  Stream<DynamicTest> capturedVectorsDecodeVerifiedWithExpectedContent() throws Exception {
    URL url = UadpSecurityCapturedVectorTest.class.getResource(CAPTURED_RESOURCE_DIR);
    if (url == null) {
      return Stream.of(capturedVectorsMissingMarker());
    }

    Path directory = Path.of(url.toURI());

    List<Path> bins;
    try (Stream<Path> files = Files.list(directory)) {
      bins = files.filter(path -> path.getFileName().toString().endsWith(".bin")).sorted().toList();
    }

    if (bins.isEmpty()) {
      return Stream.of(capturedVectorsMissingMarker());
    }

    var tests = new ArrayList<DynamicTest>(bins.size());
    for (Path bin : bins) {
      String name = bin.getFileName().toString();
      Path keysJson = bin.resolveSibling(name.substring(0, name.length() - 4) + ".keys.json");

      tests.add(
          dynamicTest(
              name,
              () -> {
                assertTrue(Files.exists(keysJson), "missing keys.json for " + name);
                byte[] message = Files.readAllBytes(bin);
                CapturedVector vector =
                    CapturedVector.parse(Files.readString(keysJson, StandardCharsets.UTF_8));
                verifyVector(vector, message);
              }));
    }
    return tests.stream();
  }

  /**
   * The always-visible stand-in for absent captures. Aborted by default so routine CI reports
   * "skipped", never a silent green; strict verification runs with {@value
   * #REQUIRE_CAPTURES_PROPERTY}=true, which turns the missing captures into a failure.
   */
  private static DynamicTest capturedVectorsMissingMarker() {
    return dynamicTest(
        "captured decode-direction vectors are missing",
        () -> {
          String message =
              "no fixtures under src/test/resources/security-vectors/captured/; strict secured"
                  + " interop verification requires captured third-party vectors (see"
                  + " security-vectors/README.md)";
          if (Boolean.getBoolean(REQUIRE_CAPTURES_PROPERTY)) {
            fail(message);
          }
          Assumptions.abort(message);
        });
  }

  /**
   * The per-fixture verification routine: split the canonical key data (cross-checking the
   * redundant fields), decode with a fixed-key resolver, and assert the {@code VERIFIED} outcome
   * and the expected DataSetMessages.
   */
  private void verifyVector(CapturedVector vector, byte[] message) throws Exception {
    SecurityKeyMaterial keys = vector.splitKeyMaterial();

    var requests = new ArrayList<SecurityContextResolver.ResolveRequest>();
    SecurityContextResolver resolver =
        request -> {
          requests.add(request);
          if (request.securityTokenId().longValue() == vector.securityTokenId) {
            return new SecurityContextResolver.Resolution.Keys(keys);
          }
          return new SecurityContextResolver.Resolution.Refused(SecurityOutcome.UNKNOWN_TOKEN);
        };

    ByteBuf buffer = Unpooled.wrappedBuffer(message);
    DecodedNetworkMessage decoded;
    try {
      decoded =
          assertInstanceOf(
              DecodedNetworkMessage.class,
              new UadpMessageMapping()
                  .decodeMessage(DecodeContext.of(encodingContext, resolver), buffer));
    } finally {
      buffer.release();
    }

    // the resolver saw the fixture's wire identity
    assertEquals(1, requests.size());
    assertEquals(vector.securityMode, requests.get(0).receivedMode());
    assertEquals(vector.securityTokenId, requests.get(0).securityTokenId().longValue());

    // verified (and, for SignAndEncrypt, decrypted): never a failure, never a skip
    assertNull(decoded.failure(), vector.description);
    assertNotNull(decoded.security(), vector.description);
    assertEquals(SecurityOutcome.VERIFIED, decoded.security().outcome(), vector.description);
    assertEquals(vector.securityMode, decoded.security().mode());
    assertEquals(vector.securityTokenId, decoded.security().securityTokenId().longValue());

    // the expected plaintext header identifiers and DataSetMessages
    if (vector.publisherId != null) {
      assertEquals(vector.publisherId, decoded.publisherId());
    }
    if (vector.writerGroupId != null) {
      assertEquals(ushort(vector.writerGroupId.intValue()), decoded.writerGroupId());
    }

    assertFalse(decoded.messages().isEmpty(), "no DataSetMessages decoded");
    for (DecodedDataSetMessage dataSetMessage : decoded.messages()) {
      assertTrue(dataSetMessage.valid(), "invalid DataSetMessage in " + vector.description);
    }

    if (!vector.dataSetWriterIds.isEmpty()) {
      List<UShort> expected =
          vector.dataSetWriterIds.stream().map(id -> ushort(id.intValue())).toList();
      List<@Nullable UShort> actual =
          decoded.messages().stream().map(DecodedDataSetMessage::dataSetWriterId).toList();
      assertEquals(expected, actual);
    }
  }

  // endregion

  // region scaffolding self-tests (runnable before any capture lands)

  /** The §2.2 schema parses, and the splitter cross-check catches the redundant key fields. */
  @Test
  void keysJsonParserExtractsTheSchemaFields() throws Exception {
    CapturedVector vector = CapturedVector.parse(sampleKeysJson());

    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr, vector.policy());
    assertEquals(MessageSecurityMode.SignAndEncrypt, vector.securityMode);
    assertEquals(1L, vector.securityTokenId);
    assertEquals(PublisherId.uint16(ushort(2234)), vector.publisherId);
    assertEquals(100L, vector.writerGroupId);
    assertEquals(List.of(62541L), vector.dataSetWriterIds);

    // splitting the canonical keyData exercises the key-data splitter and must agree with the
    // redundant fields
    SecurityKeyMaterial keys = vector.splitKeyMaterial();
    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr, keys.getPolicy());
  }

  /**
   * End-to-end scaffolding proof with an in-memory fixture: a secured NetworkMessage produced by
   * the Milo encoder (fixed nonce) plus a hand-written keys.json run through the exact routine the
   * fixture-driven tests use. When real captures land they flow through the same code path.
   */
  @Test
  void verificationRoutineAcceptsAnInMemoryVector() throws Exception {
    byte[] keyData = new byte[52];
    for (int i = 0; i < keyData.length; i++) {
      keyData[i] = (byte) (i + 1);
    }

    SecurityKeyMaterial keys =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(keyData));

    byte[] nonce = {(byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, 1, 0, 0, 0};

    var securityContext =
        new MessageSecurityContext(MessageSecurityMode.SignAndEncrypt, uint(1), keys, nonce::clone);

    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("writer")
            .dataSet(new PublishedDataSetRef("ds"))
            .dataSetWriterId(ushort(62541))
            .fieldContentMask(new DataSetFieldContentMask(uint(0)))
            .settings(
                UadpDataSetWriterSettings.builder()
                    .dataSetMessageContentMask(new UadpDataSetMessageContentMask(uint(0)))
                    .build())
            .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder("group")
            .writerGroupId(ushort(100))
            .messageSettings(
                UadpWriterGroupSettings.builder()
                    .networkMessageContentMask(
                        UadpNetworkMessageContentMask.of(
                            UadpNetworkMessageContentMask.Field.PublisherId,
                            UadpNetworkMessageContentMask.Field.GroupHeader,
                            UadpNetworkMessageContentMask.Field.WriterGroupId,
                            UadpNetworkMessageContentMask.Field.PayloadHeader))
                    .build())
            .messageSecurity(
                MessageSecurityConfig.builder().mode(MessageSecurityMode.SignAndEncrypt).build())
            .dataSetWriter(writer)
            .build();

    EncodeContext encodeContext =
        EncodeContext.of(
            encodingContext,
            PublisherId.uint16(ushort(2234)),
            group,
            uint(0),
            ushort(1),
            ushort(1),
            null,
            List.of(
                DataSetMessageDraft.of(
                    writer,
                    uint(1),
                    null,
                    null,
                    new ConfigurationVersionDataType(uint(0), uint(0)),
                    false,
                    List.of(
                        new DataValue(
                            Variant.ofInt32(42), StatusCode.GOOD, null, null, null, null)))),
            securityContext);

    List<EncodedNetworkMessage> encoded = new UadpMessageMapping().encode(encodeContext);
    assertEquals(1, encoded.size());
    byte[] message;
    try {
      message = ByteBufUtil.getBytes(encoded.get(0).data());
    } finally {
      encoded.get(0).data().release();
    }

    CapturedVector vector = CapturedVector.parse(inMemoryKeysJson(keyData));

    verifyVector(vector, message);
  }

  /** A sample captured-vector descriptor, with keyData chosen so the redundant fields agree. */
  private static String sampleKeysJson() {
    StringBuilder keyData = new StringBuilder();
    for (int i = 0; i < 52; i++) {
      keyData.append("%02x".formatted(i + 1));
    }
    String keyDataHex = keyData.toString();

    return """
    {
      "description": "open62541 pubsub_publish_encrypted, SIGNANDENCRYPT, Aes128-CTR",
      "source": "open62541 @ 751d08213d24, examples/pubsub/pubsub_publish_encrypted.c",
      "direction": "captured",
      "securityPolicyUri": "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes128-CTR",
      "securityMode": "SignAndEncrypt",
      "securityTokenId": 1,
      "keyData": "%s",
      "signingKey": "%s",
      "encryptingKey": "%s",
      "keyNonce": "%s",
      "messageNonce": "a1a2a3a401000000",
      "publisherId": { "type": "UInt16", "value": 2234 },
      "writerGroupId": 100,
      "dataSetWriterIds": [62541],
      "notes": "schema self-test"
    }
    """
        .formatted(
            keyDataHex,
            keyDataHex.substring(0, 64),
            keyDataHex.substring(64, 96),
            keyDataHex.substring(96, 104));
  }

  private static String inMemoryKeysJson(byte[] keyData) {
    StringBuilder hex = new StringBuilder();
    for (byte b : keyData) {
      hex.append("%02x".formatted(b));
    }

    return """
    {
      "description": "in-memory scaffolding self-test",
      "direction": "computed",
      "securityPolicyUri": "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes128-CTR",
      "securityMode": "SignAndEncrypt",
      "securityTokenId": 1,
      "keyData": "%s",
      "publisherId": { "type": "UInt16", "value": 2234 },
      "writerGroupId": 100,
      "dataSetWriterIds": [62541]
    }
    """
        .formatted(hex);
  }

  // endregion

  // region keys.json model

  /** One parsed {@code <name>.keys.json} fixture descriptor. */
  private static final class CapturedVector {

    final String description;
    final String securityPolicyUri;
    final MessageSecurityMode securityMode;
    final long securityTokenId;
    final byte[] keyData;
    final byte @Nullable [] signingKey;
    final byte @Nullable [] encryptingKey;
    final byte @Nullable [] keyNonce;
    final @Nullable PublisherId publisherId;
    final @Nullable Long writerGroupId;
    final List<Long> dataSetWriterIds;

    private CapturedVector(JsonObject json) {
      this.description = optionalString(json, "description", "(no description)");
      this.securityPolicyUri = requiredString(json, "securityPolicyUri");
      this.securityMode = parseMode(requiredString(json, "securityMode"));
      this.securityTokenId = requiredNumber(json, "securityTokenId");
      this.keyData = hex(requiredString(json, "keyData"));
      this.signingKey = optionalHex(json, "signingKey");
      this.encryptingKey = optionalHex(json, "encryptingKey");
      this.keyNonce = optionalHex(json, "keyNonce");
      this.publisherId = parsePublisherId(member(json, "publisherId"));
      JsonElement writerGroupId = member(json, "writerGroupId");
      this.writerGroupId = writerGroupId != null ? writerGroupId.getAsLong() : null;
      this.dataSetWriterIds = numberList(member(json, "dataSetWriterIds"));
    }

    static CapturedVector parse(String json) {
      JsonElement root = JsonParser.parseString(json);
      if (!root.isJsonObject()) {
        throw new IllegalArgumentException("keys.json root must be an object");
      }
      return new CapturedVector(root.getAsJsonObject());
    }

    PubSubSecurityPolicy policy() {
      return PubSubSecurityPolicy.fromUri(securityPolicyUri)
          .orElseThrow(
              () -> new IllegalArgumentException("unsupported policy: " + securityPolicyUri));
    }

    /**
     * Split the canonical {@code keyData} (the key-data splitter under test on every fixture load)
     * and cross-check the redundant split fields when the fixture carries them.
     */
    SecurityKeyMaterial splitKeyMaterial() throws Exception {
      SecurityKeyMaterial keys = SecurityKeyMaterial.split(policy(), ByteString.of(keyData));

      if (signingKey != null) {
        assertArrayEquals(signingKey, keys.getSigningKey(), "signingKey != split(keyData)");
      }
      if (encryptingKey != null) {
        assertArrayEquals(
            encryptingKey, keys.getEncryptingKey(), "encryptingKey != split(keyData)");
      }
      if (keyNonce != null) {
        assertArrayEquals(keyNonce, keys.getKeyNonce(), "keyNonce != split(keyData)");
      }

      return keys;
    }

    private static MessageSecurityMode parseMode(String mode) {
      return switch (mode) {
        case "Sign" -> MessageSecurityMode.Sign;
        case "SignAndEncrypt" -> MessageSecurityMode.SignAndEncrypt;
        default -> throw new IllegalArgumentException("securityMode: " + mode);
      };
    }

    private static @Nullable PublisherId parsePublisherId(@Nullable JsonElement value) {
      if (value == null) {
        return null;
      }
      if (!value.isJsonObject()) {
        throw new IllegalArgumentException("publisherId must be an object");
      }
      JsonObject object = value.getAsJsonObject();
      String type = requiredString(object, "type");

      if ("String".equals(type)) {
        return PublisherId.string(object.get("value").getAsString());
      }
      long numeric = object.get("value").getAsLong();
      return switch (type) {
        case "Byte" -> PublisherId.ubyte(ubyte((short) numeric));
        case "UInt16" -> PublisherId.uint16(ushort((int) numeric));
        case "UInt32" -> PublisherId.uint32(uint(numeric));
        case "UInt64" -> PublisherId.uint64(ulong(numeric));
        default -> throw new IllegalArgumentException("publisherId.type: " + type);
      };
    }

    /** The member, with absent and JSON-null both normalized to {@code null}. */
    private static @Nullable JsonElement member(JsonObject json, String name) {
      JsonElement element = json.get(name);
      return element == null || element.isJsonNull() ? null : element;
    }

    private static String requiredString(JsonObject json, String name) {
      JsonElement element = member(json, name);
      if (element == null
          || !element.isJsonPrimitive()
          || !element.getAsJsonPrimitive().isString()) {
        throw new IllegalArgumentException("missing required string field: " + name);
      }
      return element.getAsString();
    }

    private static String optionalString(JsonObject json, String name, String fallback) {
      JsonElement element = member(json, name);
      return element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()
          ? element.getAsString()
          : fallback;
    }

    private static long requiredNumber(JsonObject json, String name) {
      JsonElement element = member(json, name);
      if (element == null
          || !element.isJsonPrimitive()
          || !element.getAsJsonPrimitive().isNumber()) {
        throw new IllegalArgumentException("missing required number field: " + name);
      }
      return element.getAsLong();
    }

    private static byte @Nullable [] optionalHex(JsonObject json, String name) {
      JsonElement element = member(json, name);
      return element != null ? hex(element.getAsString()) : null;
    }

    private static List<Long> numberList(@Nullable JsonElement value) {
      if (value == null) {
        return List.of();
      }
      JsonArray array = value.getAsJsonArray();
      var ids = new ArrayList<Long>(array.size());
      for (JsonElement element : array) {
        ids.add(element.getAsLong());
      }
      return List.copyOf(ids);
    }

    private static byte[] hex(String hex) {
      if (hex.length() % 2 != 0) {
        throw new IllegalArgumentException("odd-length hex string");
      }
      byte[] bytes = new byte[hex.length() / 2];
      for (int i = 0; i < bytes.length; i++) {
        bytes[i] = (byte) Integer.parseInt(hex, i * 2, i * 2 + 2, 16);
      }
      return bytes;
    }
  }

  // endregion
}
