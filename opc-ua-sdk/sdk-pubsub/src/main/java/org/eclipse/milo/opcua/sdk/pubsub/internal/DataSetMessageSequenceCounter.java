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

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * The per-DataSetWriter DataSetMessage sequence counter of Part 14 §7.2.3: starts at 0, incremented
 * by exactly one for each message, rolling over at the maximum of the WIRE DataType of the writer's
 * mapping (Table 152) — 2^16 for the UADP mapping's UInt16 (§7.2.4.5.4 Table 162), 2^32 for the
 * JSON mapping's UInt32 (§7.2.5.4.1 Table 185). Custom mappings count with the 32-bit width,
 * mirroring the subscriber-side window width choice in {@code DataSetReaderRuntime} (the decoded
 * slot spans UInt32).
 *
 * <p>The width matters beyond the wire format: a subscriber's §7.2.3 recency window runs N=32 math
 * on JSON streams, so a JSON publisher wrapping at 2^16 would jump "backwards" by 2^32 - 2^16 at
 * the wrap and every subsequent message would classify as stale — the counter must keep counting
 * through 65535 → 65536 and roll over only at 2^32.
 *
 * <p>Data messages consume the next value via {@link #next()}; keep-alives carry the next expected
 * value WITHOUT consuming it via {@link #peek()} (§7.2.4.5.8, Table 185).
 *
 * <p>Not thread safe; confine each instance to its writer group's publish task thread.
 */
final class DataSetMessageSequenceCounter {

  private final long valueMask;

  /** The next value {@link #next()} returns; always within {@code valueMask}. */
  private long value = 0;

  /**
   * @param bitWidth the wire width N of the mapping's DataSetMessage sequence number (16 or 32);
   *     the counter rolls over from 2^N - 1 to 0.
   */
  DataSetMessageSequenceCounter(int bitWidth) {
    this.valueMask = (1L << bitWidth) - 1;
  }

  /** Consume and return the next sequence number (data messages, §7.2.3). */
  UInteger next() {
    long next = value;
    value = (next + 1) & valueMask;
    return uint(next);
  }

  /**
   * The next expected sequence number, without consuming it (keep-alive messages, §7.2.4.5.8 and
   * Table 185).
   */
  UInteger peek() {
    return uint(value);
  }
}
