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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HexFormat;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link SecurityKeyMaterial}: the Part 14 Table 155 split layout ({@code SigningKey ||
 * EncryptingKey || KeyNonce}), strict 52/68-byte length validation, and the {@code destroy()}
 * zero-fill contract.
 */
class SecurityKeyMaterialTest {

  /**
   * The OPC Labs demo 52-byte static key (their {@code static:?key=...} documentation example):
   * each byte value {@code 01}-{@code 0A} repeated five times, then {@code 0B 0B}. Also the key
   * material used by the s2opc-keys test fixtures.
   */
  private static final String OPC_LABS_52B_HEX =
      "0101010101020202020203030303030404040404050505050506060606060707"
          + "070707080808080809090909090A0A0A"
          + "0A0A0B0B";

  private static byte[] sequentialBytes(int length) {
    byte[] bytes = new byte[length];
    for (int i = 0; i < length; i++) {
      bytes[i] = (byte) i;
    }
    return bytes;
  }

  @Test
  void split52BytesIntoSigningEncryptingKeyNonce() throws UaException {
    // Sequential bytes make the split boundaries unambiguous:
    // SigningKey = 0x00..0x1F, EncryptingKey = 0x20..0x2F, KeyNonce = 0x30..0x33.
    byte[] keyData = sequentialBytes(52);

    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(keyData));

    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr, material.getPolicy());

    byte[] expectedSigningKey = new byte[32];
    System.arraycopy(keyData, 0, expectedSigningKey, 0, 32);
    byte[] expectedEncryptingKey = new byte[16];
    System.arraycopy(keyData, 32, expectedEncryptingKey, 0, 16);
    byte[] expectedKeyNonce = new byte[4];
    System.arraycopy(keyData, 48, expectedKeyNonce, 0, 4);

    assertArrayEquals(expectedSigningKey, material.getSigningKey());
    assertArrayEquals(expectedEncryptingKey, material.getEncryptingKey());
    assertArrayEquals(expectedKeyNonce, material.getKeyNonce());
  }

  @Test
  void split68BytesIntoSigningEncryptingKeyNonce() throws UaException {
    // SigningKey = 0x00..0x1F, EncryptingKey = 0x20..0x3F, KeyNonce = 0x40..0x43.
    byte[] keyData = sequentialBytes(68);

    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes256Ctr, ByteString.of(keyData));

    assertEquals(PubSubSecurityPolicy.PubSubAes256Ctr, material.getPolicy());

    byte[] expectedSigningKey = new byte[32];
    System.arraycopy(keyData, 0, expectedSigningKey, 0, 32);
    byte[] expectedEncryptingKey = new byte[32];
    System.arraycopy(keyData, 32, expectedEncryptingKey, 0, 32);
    byte[] expectedKeyNonce = new byte[4];
    System.arraycopy(keyData, 64, expectedKeyNonce, 0, 4);

    assertArrayEquals(expectedSigningKey, material.getSigningKey());
    assertArrayEquals(expectedEncryptingKey, material.getEncryptingKey());
    assertArrayEquals(expectedKeyNonce, material.getKeyNonce());
  }

  @Test
  void splitOpcLabsDemoKey() throws UaException {
    // Cross-checkable against the OPC Labs static-key documentation: the split parts below
    // are the documented 52-byte demo key cut at 32 and 48 bytes.
    ByteString keyData = ByteString.of(HexFormat.of().parseHex(OPC_LABS_52B_HEX));

    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, keyData);

    HexFormat hex = HexFormat.of().withUpperCase();
    assertEquals(
        "0101010101020202020203030303030404040404050505050506060606060707",
        hex.formatHex(material.getSigningKey()));
    assertEquals("070707080808080809090909090A0A0A", hex.formatHex(material.getEncryptingKey()));
    assertEquals("0A0A0B0B", hex.formatHex(material.getKeyNonce()));
  }

  private static Stream<Arguments> wrongLengths() {
    return Stream.of(
        Arguments.of(PubSubSecurityPolicy.PubSubAes128Ctr, 0),
        Arguments.of(PubSubSecurityPolicy.PubSubAes128Ctr, 51),
        Arguments.of(PubSubSecurityPolicy.PubSubAes128Ctr, 53),
        Arguments.of(PubSubSecurityPolicy.PubSubAes128Ctr, 68),
        Arguments.of(PubSubSecurityPolicy.PubSubAes256Ctr, 0),
        Arguments.of(PubSubSecurityPolicy.PubSubAes256Ctr, 52),
        Arguments.of(PubSubSecurityPolicy.PubSubAes256Ctr, 67),
        Arguments.of(PubSubSecurityPolicy.PubSubAes256Ctr, 69));
  }

  @ParameterizedTest
  @MethodSource("wrongLengths")
  void splitRejectsWrongLengthWithConfigurationError(PubSubSecurityPolicy policy, int length) {
    UaException e =
        assertThrows(
            UaException.class,
            () -> SecurityKeyMaterial.split(policy, ByteString.of(new byte[length])));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @Test
  void destroyZeroFillsAllThreeArrays() throws UaException {
    byte[] keyData = sequentialBytes(52);
    // Make every part byte non-zero so a missed fill is visible.
    for (int i = 0; i < keyData.length; i++) {
      keyData[i] = (byte) (keyData[i] | 0x80);
    }

    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(keyData));

    // Borrow the live arrays before destroy(); zero-fill must be observable through them.
    byte[] signingKey = material.getSigningKey();
    byte[] encryptingKey = material.getEncryptingKey();
    byte[] keyNonce = material.getKeyNonce();

    assertFalse(material.isDestroyed());

    material.destroy();

    assertTrue(material.isDestroyed());
    assertArrayEquals(new byte[32], signingKey);
    assertArrayEquals(new byte[16], encryptingKey);
    assertArrayEquals(new byte[4], keyNonce);
  }

  @Test
  void accessorsThrowAfterDestroy() throws UaException {
    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(
            PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(sequentialBytes(52)));

    material.destroy();

    assertThrows(IllegalStateException.class, material::getSigningKey);
    assertThrows(IllegalStateException.class, material::getEncryptingKey);
    assertThrows(IllegalStateException.class, material::getKeyNonce);

    // getPolicy carries no key bytes and stays usable, e.g. for diagnostics.
    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr, material.getPolicy());
  }

  @Test
  void destroyIsIdempotent() throws UaException {
    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(
            PubSubSecurityPolicy.PubSubAes256Ctr, ByteString.of(sequentialBytes(68)));

    material.destroy();
    material.destroy();

    assertTrue(material.isDestroyed());
  }

  @Test
  void splitCopiesKeyDataSoDestroyDoesNotTouchTheSource() throws UaException {
    byte[] source = sequentialBytes(52);
    ByteString keyData = ByteString.of(source);

    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, keyData);
    material.destroy();

    // The ByteString (a shared immutable value type) must be unaffected by the wipe.
    assertArrayEquals(sequentialBytes(52), keyData.bytesOrEmpty());
  }

  @Test
  void toStringOmitsKeyBytes() throws UaException {
    byte[] keyData = new byte[52];
    Arrays.fill(keyData, (byte) 0xAB);

    SecurityKeyMaterial material =
        SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(keyData));

    String s = material.toString();
    assertTrue(s.contains("PubSubAes128Ctr"), "unexpected toString: " + s);
    assertTrue(s.contains("destroyed=false"), "unexpected toString: " + s);
    assertFalse(s.toLowerCase().contains("abab"), "key bytes leaked into toString: " + s);

    material.destroy();
    assertTrue(material.toString().contains("destroyed=true"));
  }
}
