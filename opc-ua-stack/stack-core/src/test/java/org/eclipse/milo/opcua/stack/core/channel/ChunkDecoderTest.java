/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import static org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder.LegacySequenceNumberValidator.validateSequenceNumber;
import static org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder.NonLegacySequenceNumberValidator.aeadNonceSequenceNumber;
import static org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder.NonLegacySequenceNumberValidator.expectedSequenceNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

public class ChunkDecoderTest {

  @Test
  void validateSequenceNumbers() {
    // last sequence before wrap around required
    assertTrue(validateSequenceNumber(UInteger.MAX_VALUE - 1024 - 1, UInteger.MAX_VALUE - 1024));

    // wrapped around too early, must reach at least `UInteger.MAX_VALUE - 1024` before wrapping
    assertFalse(validateSequenceNumber(UInteger.MAX_VALUE - 1024 - 1, 1));

    // wrapping around to anything < 1024 is allowed
    for (int i = 0; i < 1024; i++) {
      assertTrue(validateSequenceNumber(UInteger.MAX_VALUE - 1024, i));
    }

    // wrapping around to anything < 1024 is allowed, from UInteger.MAX_VALUE
    for (int i = 0; i < 1024; i++) {
      assertTrue(validateSequenceNumber(UInteger.MAX_VALUE, i));
    }

    // wrapping around to >= 1024 is not allowed
    for (int i = 1024; i < 2048; i++) {
      assertFalse(validateSequenceNumber(UInteger.MAX_VALUE - 1024, i));
    }

    // skipped sequence
    assertFalse(validateSequenceNumber(0, 2));

    // failed to wrap around
    assertFalse(validateSequenceNumber(UInteger.MAX_VALUE, UInteger.MAX_VALUE + 1));
  }

  // AEAD nonce construction uses the previously sent SequenceNumber, while validation still checks
  // the current SequenceHeader after tag verification exposes it.
  @Test
  void nonLegacySequenceNumbersStartAtZeroAndAdvanceExactly() {
    assertTrue(ChunkDecoder.NonLegacySequenceNumberValidator.validateSequenceNumber(-1, 0));
    assertFalse(ChunkDecoder.NonLegacySequenceNumberValidator.validateSequenceNumber(-1, 1));

    assertTrue(ChunkDecoder.NonLegacySequenceNumberValidator.validateSequenceNumber(0, 1));
    assertFalse(ChunkDecoder.NonLegacySequenceNumberValidator.validateSequenceNumber(0, 2));
    assertFalse(ChunkDecoder.NonLegacySequenceNumberValidator.validateSequenceNumber(1, 1));

    assertTrue(
        ChunkDecoder.NonLegacySequenceNumberValidator.validateSequenceNumber(
            UInteger.MAX_VALUE, 0));
    assertFalse(
        ChunkDecoder.NonLegacySequenceNumberValidator.validateSequenceNumber(
            UInteger.MAX_VALUE, 1024));

    assertEquals(0, expectedSequenceNumber(-1));
    assertEquals(42, expectedSequenceNumber(41));
    assertEquals(0, aeadNonceSequenceNumber(-1));
    assertEquals(41, aeadNonceSequenceNumber(41));
  }
}
