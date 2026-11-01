/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Test;

class InvalidArgumentExceptionTest {

  private static final StatusCode OUT_OF_RANGE = new StatusCode(StatusCodes.Bad_OutOfRange);

  // Part 4 §5.12.2.2: inputArgumentResults has one entry per input argument, and arguments that
  // were not rejected are Good.
  @Test
  void builderReturnsOneResultPerInputWithUnrejectedArgumentsGood() {
    InvalidArgumentException e =
        InvalidArgumentException.builder(3).reject(1, StatusCodes.Bad_OutOfRange).build();

    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), e.getStatusCode());
    assertArrayEquals(
        new StatusCode[] {StatusCode.GOOD, OUT_OF_RANGE, StatusCode.GOOD},
        e.getInputArgumentResults());
  }

  // Part 4 §5.12.2.2: the argument diagnostics are empty if there are none, otherwise one per
  // input argument.
  @Test
  void diagnosticsAreOnePerInputWhenAnyArgumentHasOne() {
    Diagnostic diagnostic = Diagnostic.text("Speed must not exceed 3000 rpm");

    InvalidArgumentException e =
        InvalidArgumentException.builder(2).reject(1, OUT_OF_RANGE, diagnostic).build();

    assertEquals(Arrays.asList(null, diagnostic), e.getInputArgumentDiagnostics());
  }

  @Test
  void diagnosticsAreEmptyWhenNoArgumentHasOne() {
    InvalidArgumentException e =
        InvalidArgumentException.builder(2).reject(0, OUT_OF_RANGE).build();

    assertEquals(List.of(), e.getInputArgumentDiagnostics());
    assertEquals(
        List.of(),
        new InvalidArgumentException(new StatusCode[] {OUT_OF_RANGE})
            .getInputArgumentDiagnostics());
  }

  // A rejection must say why the argument is invalid.
  @Test
  void rejectRequiresABadStatus() {
    assertThrows(
        IllegalArgumentException.class,
        () -> InvalidArgumentException.builder(1).reject(0, StatusCode.GOOD));
  }

  @Test
  void rejectRequiresASuppliedArgument() {
    assertThrows(
        IndexOutOfBoundsException.class,
        () -> InvalidArgumentException.builder(1).reject(1, OUT_OF_RANGE));
  }

  // A builder may be reused; an exception it already built must not change.
  @Test
  void builtExceptionDoesNotChangeWhenTheBuilderDoes() {
    InvalidArgumentException.Builder builder = InvalidArgumentException.builder(1);
    InvalidArgumentException first = builder.build();

    builder.reject(0, OUT_OF_RANGE, Diagnostic.text("later"));

    assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, first.getInputArgumentResults());
    assertEquals(List.of(), first.getInputArgumentDiagnostics());
  }
}
