/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReverseConnectTargetSnapshotTest {

  @Test
  void lastErrorIsDefensivelyCopied() {
    StackTraceElement[] originalStackTrace = {
      new StackTraceElement("ExampleTarget", "connect", "ExampleTarget.java", 42)
    };
    IllegalStateException cause = new IllegalStateException("socket closed");
    RuntimeException suppressed = new RuntimeException("cleanup failed");
    IllegalArgumentException lastError = new IllegalArgumentException("connection failed", cause);
    lastError.setStackTrace(originalStackTrace);
    lastError.addSuppressed(suppressed);

    ReverseConnectTargetSnapshot snapshot = snapshot(lastError);

    lastError.setStackTrace(new StackTraceElement[0]);
    lastError.addSuppressed(new RuntimeException("external mutation"));

    Throwable exposed = snapshot.lastError();
    assertNotNull(exposed);
    assertNotSame(lastError, exposed);
    assertEquals("connection failed", exposed.getMessage());
    assertEquals(
        IllegalArgumentException.class.getName() + ": connection failed", exposed.toString());
    assertArrayEquals(originalStackTrace, exposed.getStackTrace());
    Throwable exposedCause = exposed.getCause();
    assertNotNull(exposedCause);
    assertNotSame(cause, exposedCause);
    assertEquals(cause.toString(), exposedCause.toString());
    assertEquals(1, exposed.getSuppressed().length);
    assertNotSame(suppressed, exposed.getSuppressed()[0]);
    assertEquals(suppressed.toString(), exposed.getSuppressed()[0].toString());

    exposed.setStackTrace(new StackTraceElement[0]);
    exposed.addSuppressed(new RuntimeException("snapshot mutation"));

    Throwable exposedAgain = snapshot.lastError();
    assertNotNull(exposedAgain);
    assertNotSame(exposed, exposedAgain);
    assertArrayEquals(originalStackTrace, exposedAgain.getStackTrace());
    assertEquals(1, exposedAgain.getSuppressed().length);
  }

  private static ReverseConnectTargetSnapshot snapshot(Throwable lastError) {
    return new ReverseConnectTargetSnapshot(
        UUID.randomUUID(),
        "opc.tcp://localhost:12687",
        "opc.tcp://localhost:12686/reverse-target-test",
        uint(1_000),
        uint(100),
        true,
        false,
        null,
        null,
        null,
        0,
        null,
        lastError);
  }
}
