/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MethodDiagnosticsTest {
  private static final CallMethodRequest REQUEST =
      new CallMethodRequest(new NodeId(1, 1), new NodeId(1, 2), new Variant[] {Variant.NULL_VALUE});
  private static final StatusCode INVALID = new StatusCode(StatusCodes.Bad_InvalidArgument);
  private static final StatusCode OUT_OF_RANGE = new StatusCode(StatusCodes.Bad_OutOfRange);
  private static final int MAX_DEPTH = 128;

  private static CallMethodResult result(DiagnosticInfo... diagnostics) {
    return new CallMethodResult(
        INVALID, new StatusCode[] {OUT_OF_RANGE}, diagnostics, new Variant[0]);
  }

  private static CallMethodResult[] normalize(int mask, List<CallMethodResult> results) {
    return normalize(mask, results, MAX_DEPTH);
  }

  private static CallMethodResult[] normalize(
      int mask, List<CallMethodResult> results, int maxDepth) {
    return MethodDiagnostics.normalize(
        uint(mask), Collections.nCopies(results.size(), REQUEST), results, maxDepth);
  }

  private static DiagnosticInfo firstArgument(CallMethodResult result) {
    return result.getInputArgumentDiagnosticInfos()[0];
  }

  // Part 4 §5.12.2.2: argument diagnostics are empty unless operation-level diagnostics were
  // requested. Service-level flags do not authorize them.
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 31})
  void noOperationMaskReturnsNoArgumentDiagnostics(int mask) {
    DiagnosticInfo info = new DiagnosticInfo(-1, -1, -1, -1, "private", StatusCode.BAD, null);

    CallMethodResult[] normalized = normalize(mask, List.of(result(info)));

    assertEquals(INVALID, normalized[0].getStatusCode());
    assertEquals(0, normalized[0].getInputArgumentDiagnosticInfos().length);
  }

  @Test
  void resultsWithoutDiagnosticsPassThroughAndNullResultsBecomeInternalErrors() {
    CallMethodResult denied =
        new CallMethodResult(new StatusCode(StatusCodes.Bad_UserAccessDenied), null, null, null);
    CallMethodResult good =
        new CallMethodResult(
            StatusCode.GOOD,
            new StatusCode[0],
            new DiagnosticInfo[0],
            new Variant[] {new Variant(1)});

    CallMethodResult[] normalized = normalize(0x3e0, Arrays.asList(denied, good, null));

    assertSame(denied, normalized[0]);
    assertSame(good, normalized[1]);
    assertEquals(new StatusCode(StatusCodes.Bad_InternalError), normalized[2].getStatusCode());
  }

  // Part 4 §7.32: each ReturnDiagnostics bit selects one field, so a client that asks for one
  // field must not receive the others.
  @Test
  void eachRequestedFieldIsKeptIndependently() {
    DiagnosticInfo all = new DiagnosticInfo(-1, -1, -1, -1, "info", OUT_OF_RANGE, null);

    DiagnosticInfo additionalInfo = firstArgument(normalize(0x80, List.of(result(all)))[0]);
    DiagnosticInfo innerStatus = firstArgument(normalize(0x100, List.of(result(all)))[0]);

    assertEquals(new DiagnosticInfo(-1, -1, -1, -1, "info", null, null), additionalInfo);
    assertEquals(new DiagnosticInfo(-1, -1, -1, -1, null, OUT_OF_RANGE, null), innerStatus);
  }

  @Test
  void innerDiagnosticsAreReturnedOnlyWhenRequestedAndUseTheSameMask() {
    DiagnosticInfo inner = new DiagnosticInfo(-1, -1, -1, -1, "inner", OUT_OF_RANGE, null);
    DiagnosticInfo outer = new DiagnosticInfo(-1, -1, -1, -1, "outer", null, inner);

    DiagnosticInfo withInner = firstArgument(normalize(0x280, List.of(result(outer)))[0]);
    DiagnosticInfo withoutInner = firstArgument(normalize(0x80, List.of(result(outer)))[0]);

    assertEquals(
        new DiagnosticInfo(-1, -1, -1, -1, "inner", null, null), withInner.innerDiagnosticInfo());
    assertNull(withoutInner.innerDiagnosticInfo(), "inner entry without the InnerDiagnostics bit");
  }

  // The Method already ran, so a malformed diagnostic loses only the diagnostic. Without a
  // StringTable in the response, a requested symbolic id index has nothing to refer to.
  @Test
  void malformedIndexCardinalityAndDepthDropOnlyTheirOwnDiagnostics() {
    DiagnosticInfo indexed = new DiagnosticInfo(-1, 0, -1, -1, "indexed", null, null);
    DiagnosticInfo inner = new DiagnosticInfo(-1, -1, -1, -1, "inner", null, null);
    DiagnosticInfo outer = new DiagnosticInfo(-1, -1, -1, -1, "outer", null, inner);
    DiagnosticInfo wellFormed = new DiagnosticInfo(-1, -1, -1, -1, "kept", null, null);

    CallMethodResult[] normalized =
        normalize(
            0x2a0,
            List.of(result(indexed), result(inner, inner), result(outer), result(wellFormed)),
            1);

    for (CallMethodResult dropped : List.of(normalized).subList(0, 3)) {
      assertEquals(INVALID, dropped.getStatusCode());
      assertEquals(1, dropped.getInputArgumentResults().length);
      assertEquals(0, dropped.getInputArgumentDiagnosticInfos().length);
    }
    assertEquals(
        "kept",
        firstArgument(normalized[3]).additionalInfo(),
        "a well-formed entry in the same request keeps its diagnostics");
  }

  @Test
  void unrequestedIndexesAreClearedWithoutDroppingTheEntry() {
    DiagnosticInfo indexed = new DiagnosticInfo(0, 1, 2, 3, "info", null, null);

    DiagnosticInfo filtered = firstArgument(normalize(0x80, List.of(result(indexed)))[0]);

    assertEquals(new DiagnosticInfo(-1, -1, -1, -1, "info", null, null), filtered);
  }
}
