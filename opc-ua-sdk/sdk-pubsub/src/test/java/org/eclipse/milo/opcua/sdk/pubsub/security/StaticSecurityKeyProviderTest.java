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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StaticSecurityKeyProvider}: {@code of(...)} validation, the pinned {@link
 * SecurityKeySet} shape (single key, token id, static ZERO/ZERO sentinel), and the S2OPC key-part
 * directory loader against the fixture directories under {@code src/test/resources/s2opc-keys/}
 * (see the README there for the fixture provenance and layout).
 */
class StaticSecurityKeyProviderTest {

  /**
   * The OPC Labs demo 52-byte static key; identical to the concatenation of the {@code
   * s2opc-keys/aes128} fixture files (signingKey.key || encryptKey.key || keyNonce.key).
   */
  private static final String KEY_52B_HEX =
      "0101010101020202020203030303030404040404050505050506060606060707"
          + "070707080808080809090909090A0A0A"
          + "0A0A0B0B";

  /**
   * The analogous 68-byte pattern; identical to the concatenation of the {@code s2opc-keys/aes256}
   * fixture files.
   */
  private static final String KEY_68B_HEX =
      "0101010101020202020203030303030404040404050505050506060606060707"
          + "070707080808080809090909090A0A0A0A0A0B0B0B0B0B0C0C0C0C0C0D0D0D0D"
          + "0D0E0E0E";

  private static ByteString key52() {
    return ByteString.of(HexFormat.of().parseHex(KEY_52B_HEX));
  }

  private static ByteString key68() {
    return ByteString.of(HexFormat.of().parseHex(KEY_68B_HEX));
  }

  private static Path fixtureDir(String name) throws Exception {
    URL url = StaticSecurityKeyProviderTest.class.getResource("/s2opc-keys/" + name);
    assertNotNull(url, "missing fixture directory: s2opc-keys/" + name);
    return Path.of(url.toURI());
  }

  private static SecurityKeySet getKeys(StaticSecurityKeyProvider provider) {
    CompletableFuture<SecurityKeySet> future = provider.getKeys("any-group", uint(0), uint(1));
    assertTrue(future.isDone(), "static provider must return an already-completed future");
    return future.join();
  }

  @Test
  void ofPinsKeyUnderTokenIdOne() throws UaException {
    StaticSecurityKeyProvider provider =
        StaticSecurityKeyProvider.of(PubSubSecurityPolicy.PubSubAes128Ctr, key52());

    SecurityKeySet keySet = getKeys(provider);

    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr.getUri(), keySet.securityPolicyUri());
    assertEquals(uint(1), keySet.firstTokenId());
    assertEquals(List.of(key52()), keySet.keys());
    // The static sentinel: both durations ZERO = never rotate, never expire.
    assertEquals(Duration.ZERO, keySet.timeToNextKey());
    assertEquals(Duration.ZERO, keySet.keyLifetime());
  }

  @Test
  void ofHonorsExplicitTokenId() throws UaException {
    StaticSecurityKeyProvider provider =
        StaticSecurityKeyProvider.of(PubSubSecurityPolicy.PubSubAes256Ctr, uint(7), key68());

    SecurityKeySet keySet = getKeys(provider);

    assertEquals(PubSubSecurityPolicy.PubSubAes256Ctr.getUri(), keySet.securityPolicyUri());
    assertEquals(uint(7), keySet.firstTokenId());
    assertEquals(List.of(key68()), keySet.keys());
  }

  @Test
  void getKeysIgnoresItsArguments() throws UaException {
    // The provider is bound per-SecurityGroup via PubSubBindings; group id, starting token,
    // and requested count do not change the pinned result.
    StaticSecurityKeyProvider provider =
        StaticSecurityKeyProvider.of(PubSubSecurityPolicy.PubSubAes128Ctr, key52());

    SecurityKeySet first = provider.getKeys("group-a", uint(0), uint(1)).join();
    SecurityKeySet second = provider.getKeys("group-b", uint(42), uint(10)).join();

    assertEquals(first, second);
  }

  @Test
  void ofRejectsWrongKeyLengthForPolicy() {
    // 52 bytes offered as Aes256 and 68 bytes offered as Aes128: both wrong for the policy.
    UaException e128 =
        assertThrows(
            UaException.class,
            () -> StaticSecurityKeyProvider.of(PubSubSecurityPolicy.PubSubAes128Ctr, key68()));
    assertEquals(StatusCodes.Bad_ConfigurationError, e128.getStatusCode().value());

    UaException e256 =
        assertThrows(
            UaException.class,
            () -> StaticSecurityKeyProvider.of(PubSubSecurityPolicy.PubSubAes256Ctr, key52()));
    assertEquals(StatusCodes.Bad_ConfigurationError, e256.getStatusCode().value());

    UaException eShort =
        assertThrows(
            UaException.class,
            () ->
                StaticSecurityKeyProvider.of(
                    PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(new byte[51])));
    assertEquals(StatusCodes.Bad_ConfigurationError, eShort.getStatusCode().value());
  }

  @Test
  void loadS2OpcDirectoryInfersAes128FromSixteenByteEncryptKey() throws Exception {
    StaticSecurityKeyProvider provider =
        StaticSecurityKeyProvider.loadS2OpcDirectory(fixtureDir("aes128"));

    SecurityKeySet keySet = getKeys(provider);

    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr.getUri(), keySet.securityPolicyUri());
    assertEquals(uint(1), keySet.firstTokenId());
    // Loaded key data = signingKey.key || encryptKey.key || keyNonce.key.
    assertEquals(List.of(key52()), keySet.keys());
    assertEquals(Duration.ZERO, keySet.timeToNextKey());
    assertEquals(Duration.ZERO, keySet.keyLifetime());
  }

  @Test
  void loadS2OpcDirectoryInfersAes256FromThirtyTwoByteEncryptKey() throws Exception {
    StaticSecurityKeyProvider provider =
        StaticSecurityKeyProvider.loadS2OpcDirectory(fixtureDir("aes256"));

    SecurityKeySet keySet = getKeys(provider);

    assertEquals(PubSubSecurityPolicy.PubSubAes256Ctr.getUri(), keySet.securityPolicyUri());
    assertEquals(List.of(key68()), keySet.keys());
  }

  @Test
  void loadS2OpcDirectoryRejectsTruncatedSigningKey() throws Exception {
    Path dir = fixtureDir("truncated-signing-key");

    UaException e =
        assertThrows(UaException.class, () -> StaticSecurityKeyProvider.loadS2OpcDirectory(dir));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("signingKey.key"), "message must name the file: " + e.getMessage());
  }

  @Test
  void loadS2OpcDirectoryRejectsOversizedKeyNonce() throws Exception {
    Path dir = fixtureDir("oversized-key-nonce");

    UaException e =
        assertThrows(UaException.class, () -> StaticSecurityKeyProvider.loadS2OpcDirectory(dir));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("keyNonce.key"), "message must name the file: " + e.getMessage());
  }

  @Test
  void loadS2OpcDirectoryRejectsEncryptKeyOfInvalidLength() throws Exception {
    // encryptKey.key is 20 bytes: neither 16 (Aes128) nor 32 (Aes256), so no policy can be
    // inferred.
    Path dir = fixtureDir("invalid-encrypt-key");

    UaException e =
        assertThrows(UaException.class, () -> StaticSecurityKeyProvider.loadS2OpcDirectory(dir));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("encryptKey.key"), "message must name the file: " + e.getMessage());
  }

  @Test
  void loadS2OpcDirectoryRejectsMissingKeyFile() throws Exception {
    Path dir = fixtureDir("missing-key-nonce");

    UaException e =
        assertThrows(UaException.class, () -> StaticSecurityKeyProvider.loadS2OpcDirectory(dir));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("keyNonce.key"), "message must name the file: " + e.getMessage());
  }

  @Test
  void loadS2OpcDirectoryRejectsNonexistentDirectory() throws Exception {
    Path dir = fixtureDir("aes128").getParent().resolve("no-such-directory");

    UaException e =
        assertThrows(UaException.class, () -> StaticSecurityKeyProvider.loadS2OpcDirectory(dir));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }
}
