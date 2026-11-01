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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;

class MethodResultTest {

  private static final StatusCode CLAMPED = new StatusCode(StatusCodes.Good_Clamped);
  private static final StatusCode UNCERTAIN =
      new StatusCode(StatusCodes.Uncertain_NoCommunicationLastUsableValue);
  private static final StatusCode BAD = new StatusCode(StatusCodes.Bad_DeviceFailure);

  // Part 4 §5.12.2.2: each factory's name states the severity of its result, so a status of
  // another severity is a programming error caught where it is made.
  @Test
  void factoriesRejectAStatusOfAnotherSeverity() {
    assertThrows(IllegalArgumentException.class, () -> MethodResult.good(BAD));
    assertThrows(IllegalArgumentException.class, () -> MethodResult.uncertain(CLAMPED));
    assertThrows(IllegalArgumentException.class, () -> MethodResult.bad(UNCERTAIN));
  }

  @Test
  void factoriesAcceptSubcodesOfTheirSeverity() {
    assertEquals(CLAMPED, MethodResult.good(CLAMPED).getStatusCode());
    assertEquals(UNCERTAIN, MethodResult.uncertain(UNCERTAIN).getStatusCode());
    assertEquals(BAD, MethodResult.bad(BAD).getStatusCode());
  }

  // Part 4 §5.12.2.2: a Bad result carries no output arguments.
  @Test
  void badResultHasNoOutputs() {
    assertEquals(0, MethodResult.bad(BAD).getOutputArguments().length);
  }

  // The result must not change when the caller's array does, and vice versa.
  @Test
  void outputsAreCopied() {
    Variant[] outputs = {new Variant(1)};
    MethodResult result = MethodResult.good(outputs);

    outputs[0] = new Variant(2);
    result.getOutputArguments()[0] = new Variant(3);

    assertArrayEquals(new Variant[] {new Variant(1)}, result.getOutputArguments());
  }

  @Test
  void withDiagnosticKeepsStatusAndOutputs() {
    Diagnostic diagnostic = Diagnostic.text("clamped to 3000");

    MethodResult result = MethodResult.good(CLAMPED, new Variant(3000)).withDiagnostic(diagnostic);

    assertEquals(CLAMPED, result.getStatusCode());
    assertArrayEquals(new Variant[] {new Variant(3000)}, result.getOutputArguments());
    assertEquals(diagnostic, result.getDiagnostic().orElseThrow());
    assertTrue(MethodResult.good().getDiagnostic().isEmpty(), "control: none unless attached");
  }
}
