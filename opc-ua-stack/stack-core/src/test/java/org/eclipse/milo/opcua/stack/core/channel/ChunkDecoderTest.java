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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.testng.annotations.Test;

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
}
