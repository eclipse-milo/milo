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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.junit.jupiter.api.Test;

/**
 * Engine-side coverage for the UInt32 exposure clamp: counters are 64-bit in the engine and are
 * never clamped there — a value pushed past {@code 0xFFFF_FFFF} keeps counting and the snapshot
 * returns the true long. The clamp is applied only at server-side exposure; this test guards the
 * feed's integrity underneath it.
 */
class DiagnosticsClampFeedTest {

  private static final String WRITER = "conn/grp/writer";

  private final EventDispatcher eventDispatcher = new EventDispatcher(Runnable::run);
  private final DiagnosticsCollector collector = new DiagnosticsCollector(eventDispatcher);

  DiagnosticsClampFeedTest() {
    eventDispatcher.setDiagnostics(collector);
    collector.register(WRITER);
  }

  @Test
  void countersKeepCountingPastTheUInt32BoundaryEngineSide() {
    // three max-int increments: 6_442_450_941 > 0xFFFF_FFFF (4_294_967_295)
    collector.dataSetMessagesSent(WRITER, Integer.MAX_VALUE);
    collector.dataSetMessagesSent(WRITER, Integer.MAX_VALUE);
    collector.dataSetMessagesSent(WRITER, Integer.MAX_VALUE);

    ComponentDiagnostics writer = collector.component(WRITER).orElseThrow();
    long expected = 3L * Integer.MAX_VALUE;
    assertEquals(expected, writer.dataSetMessagesSent());
    assertEquals(6_442_450_941L, expected, "the row actually crossed the UInt32 boundary");
  }
}
