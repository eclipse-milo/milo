/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the per-writer DataSetMessage sequence counter: the Part 14 §7.2.3 rollover
 * happens at the wire DataType maximum of the mapping (Table 152) — 2^16 for UADP's UInt16, 2^32
 * for JSON's UInt32 — and keep-alive peeks never consume a value.
 */
class DataSetMessageSequenceCounterTest {

  @Test
  void startsAtZeroAndIncrementsByOne() {
    var counter = new DataSetMessageSequenceCounter(16);

    assertEquals(uint(0), counter.next());
    assertEquals(uint(1), counter.next());
    assertEquals(uint(2), counter.next());
  }

  /** The UADP (UInt16) counter still wraps 65535 → 0 (§7.2.3 rollover at the DataType maximum). */
  @Test
  void uadpWidthWrapsAtUInt16Maximum() {
    var counter = new DataSetMessageSequenceCounter(16);

    for (int i = 0; i < 65535; i++) {
      counter.next();
    }

    assertEquals(uint(65535), counter.next());
    assertEquals(uint(0), counter.next());
    assertEquals(uint(1), counter.next());
  }

  /**
   * The JSON (UInt32) counter keeps counting through the UInt16 boundary — 65535 is followed by
   * 65536, NOT by 0: wrapping there would jump backwards by 2^32 - 2^16 under the subscriber's N=32
   * recency window (§7.2.3, Table 152) and wedge the stream permanently stale.
   */
  @Test
  void jsonWidthCrossesTheUInt16BoundaryWithoutWrapping() {
    var counter = new DataSetMessageSequenceCounter(32);

    for (int i = 0; i < 65535; i++) {
      counter.next();
    }

    assertEquals(uint(65535), counter.next());
    assertEquals(uint(65536), counter.next());
    assertEquals(uint(65537), counter.next());
  }

  /** Keep-alives carry the next expected number without consuming it (§7.2.4.5.8, Table 185). */
  @Test
  void peekDoesNotConsume() {
    var counter = new DataSetMessageSequenceCounter(32);

    assertEquals(uint(0), counter.next());
    assertEquals(uint(1), counter.peek());
    assertEquals(uint(1), counter.peek());
    assertEquals(uint(1), counter.next());
  }
}
