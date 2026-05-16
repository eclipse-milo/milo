/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HexFormat;
import org.junit.jupiter.api.Test;

class HkdfUtilTest {

  // RFC 5869 vectors make the generic HKDF implementation independent of any OPC UA salt layout.
  @Test
  void hkdfSha256MatchesRfc5869Vector() throws Exception {
    byte[] ikm = hex("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b");
    byte[] salt = hex("000102030405060708090a0b0c");
    byte[] info = hex("f0f1f2f3f4f5f6f7f8f9");

    byte[] okm = HkdfUtil.hkdfSha256(ikm, salt, info, 42);

    assertArrayEquals(
        hex(
            "3cb25f25faacd57a90434f64d0362f2a"
                + "2d2d0a90cf1a5a4c5db02d56ecc4c5bf"
                + "34007208d5b887185865"),
        okm);
  }

  // HKDF expand can emit at most 255 hash-length blocks; longer requests would repeat counters.
  @Test
  void hkdfRejectsOutputLongerThanRfcLimit() {
    assertThrows(
        IllegalArgumentException.class,
        () -> HkdfUtil.hkdfSha256(new byte[] {1}, new byte[] {2}, new byte[] {3}, 255 * 32 + 1));
  }

  private static byte[] hex(String s) {
    return HexFormat.of().parseHex(s.replaceAll("\\s+", ""));
  }
}
