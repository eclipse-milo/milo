/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

class UaExceptionTest {

  private static final class MarkerUaException extends UaException {
    MarkerUaException(String message) {
      super(StatusCodes.Bad_SecurityChecksFailed, message);
    }

    MarkerUaException(Throwable cause) {
      super(cause);
    }
  }

  private static final class OtherUaException extends UaException {
    OtherUaException() {
      super(StatusCodes.Bad_SecurityChecksFailed, "other");
    }
  }

  @Test
  void extractTypedReturnsDirectMatch() {
    var marker = new MarkerUaException("direct");

    assertSame(marker, UaException.extract(marker, MarkerUaException.class).orElseThrow());
  }

  // Blocking client methods wrap failed futures in new UaException(cause), while async paths wrap
  // them in ExecutionException or CompletionException. Typed extraction must look past all of
  // those wrappers to find a specific UaException subtype.
  @Test
  void extractTypedFindsSubtypeWrappedInPlainUaExceptionAndAsyncWrappers() {
    var marker = new MarkerUaException("wrapped");
    var wrapped = new UaException(new ExecutionException(new CompletionException(marker)));

    assertSame(marker, UaException.extract(wrapped, MarkerUaException.class).orElseThrow());
    assertSame(
        wrapped,
        UaException.extract(wrapped).orElseThrow(),
        "untyped extract(Throwable) must continue returning the outermost UaException");
  }

  @Test
  void extractTypedReturnsNearestMatchWhenMultipleExist() {
    var inner = new MarkerUaException("inner");
    var outer = new MarkerUaException(new RuntimeException(inner));

    assertSame(outer, UaException.extract(outer, MarkerUaException.class).orElseThrow());
  }

  @Test
  void extractTypedReturnsEmptyWhenSubtypeAbsent() {
    var plain = new UaException(StatusCodes.Bad_SecurityChecksFailed, "plain");
    var other = new UaException(new ExecutionException(new OtherUaException()));

    assertTrue(UaException.extract(plain, MarkerUaException.class).isEmpty());
    assertTrue(UaException.extract(other, MarkerUaException.class).isEmpty());
    assertTrue(
        UaException.extract(new IllegalStateException("no ua"), UaException.class).isEmpty());
  }

  @Test
  void extractHandlesNullInput() {
    assertTrue(UaException.extract(null, MarkerUaException.class).isEmpty());
    assertTrue(UaException.extract(null).isEmpty());
    assertThrows(
        NullPointerException.class,
        () -> UaException.extract(new UaException(StatusCodes.Bad_UnexpectedError), null));
  }

  // Malformed or custom exception chains can form cycles; extraction must terminate instead of
  // looping forever or overflowing the stack.
  @Test
  void extractTerminatesOnSelfCycle() {
    Throwable selfCycle =
        new Throwable("self") {
          @Override
          public synchronized Throwable getCause() {
            return this;
          }
        };

    assertTrue(UaException.extract(selfCycle, MarkerUaException.class).isEmpty());
    assertTrue(UaException.extract(selfCycle).isEmpty());
  }

  // A multi-node cycle reached after a non-cyclic stem must terminate without matching and still
  // find a matching subtype that lies on the cycle.
  @Test
  void extractHandlesMultiNodeCyclesWithAndWithoutMatch() {
    var a = new RuntimeException("a");
    var b = new RuntimeException("b", a);
    a.initCause(b);
    var stem = new RuntimeException("stem", a);

    assertTrue(UaException.extract(a, MarkerUaException.class).isEmpty());
    assertTrue(UaException.extract(stem, MarkerUaException.class).isEmpty());
    assertTrue(UaException.extract(stem).isEmpty());

    var marker = new MarkerUaException("in-cycle");
    var cycleHead = new RuntimeException("head", marker);
    marker.initCause(cycleHead);
    var matchingStem = new RuntimeException("matching-stem", cycleHead);

    assertSame(marker, UaException.extract(matchingStem, MarkerUaException.class).orElseThrow());
  }
}
