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
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
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
  private static final int MAX_DEPTH = 128;

  private static CallMethodResult result(DiagnosticInfo... diagnostics) {
    return new CallMethodResult(
        INVALID,
        new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
        diagnostics,
        new Variant[0]);
  }

  private static MethodDiagnostics.Normalized normalize(
      int mask, List<CallMethodResult> results, String... source) {
    return MethodDiagnostics.normalize(
        uint(mask),
        List.of(REQUEST, REQUEST, REQUEST).subList(0, results.size()),
        results,
        source,
        MAX_DEPTH);
  }

  @Test
  void compactsIndexesAcrossResultsAndDoesNotMutateRawDiagnostics() {
    DiagnosticInfo info = new DiagnosticInfo(3, 1, 2, 0, "details", StatusCode.BAD, null);
    MethodDiagnostics.Normalized normalized =
        normalize(0x20, List.of(result(info), result(info)), "text", "symbol", "en", "urn:vendor");
    assertArrayEquals(new String[] {"urn:vendor", "symbol"}, normalized.stringTable());
    DiagnosticInfo filtered = normalized.results()[1].getInputArgumentDiagnosticInfos()[0];
    assertEquals(new DiagnosticInfo(0, 1, -1, -1, null, null, null), filtered);
    assertEquals(3, info.namespaceUri());
    assertEquals("details", info.additionalInfo());
  }

  // Service-level flags do not authorize argument-level diagnostic fields.
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 31})
  void noOperationMaskReturnsNoDiagnosticsOrStrings(int mask) {
    MethodDiagnostics.Normalized normalized =
        normalize(
            mask,
            List.of(
                result(new DiagnosticInfo(999, 999, 999, 999, "private", StatusCode.BAD, null))));
    assertEquals(INVALID, normalized.results()[0].getStatusCode());
    assertEquals(0, normalized.results()[0].getInputArgumentDiagnosticInfos().length);
    assertNull(normalized.stringTable());
  }

  @Test
  void resultsWithoutDiagnosticsPassThroughUnchanged() {
    CallMethodResult denied =
        new CallMethodResult(new StatusCode(StatusCodes.Bad_UserAccessDenied), null, null, null);
    CallMethodResult good =
        new CallMethodResult(
            StatusCode.GOOD,
            new StatusCode[0],
            new DiagnosticInfo[0],
            new Variant[] {new Variant(1)});
    MethodDiagnostics.Normalized normalized = normalize(0x3e0, Arrays.asList(denied, good, null));
    assertSame(denied, normalized.results()[0]);
    assertSame(good, normalized.results()[1]);
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), normalized.results()[2].getStatusCode());
    assertNull(normalized.stringTable());
  }

  @Test
  void innerDiagnosticsUseTheSameMask() {
    DiagnosticInfo inner = new DiagnosticInfo(-1, 0, -1, -1, "inner", null, null);
    DiagnosticInfo outer = new DiagnosticInfo(-1, 1, -1, -1, "outer", null, inner);
    MethodDiagnostics.Normalized normalized =
        normalize(0x2a0, List.of(result(outer)), "inner", "outer");
    DiagnosticInfo actual = normalized.results()[0].getInputArgumentDiagnosticInfos()[0];
    String[] table = normalized.stringTable();
    assertEquals("outer", table[actual.symbolicId()]);
    assertEquals("outer", actual.additionalInfo());
    assertEquals("inner", table[actual.innerDiagnosticInfo().symbolicId()]);
    assertEquals("inner", actual.innerDiagnosticInfo().additionalInfo());
  }

  // The Method already ran, so a malformed diagnostic loses only the diagnostic.
  @Test
  void malformedIndexCardinalityAndDepthDropOnlyTheirOwnDiagnostics() {
    DiagnosticInfo malformed = new DiagnosticInfo(-1, 99, -1, -1, null, null, null);
    DiagnosticInfo inner = new DiagnosticInfo(-1, 0, -1, -1, "inner", null, null);
    DiagnosticInfo outer = new DiagnosticInfo(-1, 0, -1, -1, "outer", null, inner);
    MethodDiagnostics.Normalized normalized =
        MethodDiagnostics.normalize(
            uint(0x2a0),
            List.of(REQUEST, REQUEST, REQUEST),
            List.of(result(malformed), result(inner, inner), result(outer)),
            new String[] {"symbol"},
            1);
    for (CallMethodResult result : normalized.results()) {
      assertEquals(INVALID, result.getStatusCode());
      assertEquals(1, result.getInputArgumentResults().length);
      assertEquals(0, result.getInputArgumentDiagnosticInfos().length);
    }
  }

  @Test
  void requestedFieldMasksAreIndependent() {
    DiagnosticInfo all =
        new DiagnosticInfo(0, 1, 2, 3, "info", StatusCode.BAD, DiagnosticInfo.NULL_VALUE);
    DiagnosticInfo localized =
        normalize(0x40, List.of(result(all)), "ns", "symbol", "en", "text")
            .results()[0]
            .getInputArgumentDiagnosticInfos()[0];
    assertEquals(new DiagnosticInfo(-1, -1, 0, 1, null, null, null), localized);
    DiagnosticInfo status =
        normalize(0x100, List.of(result(all))).results()[0].getInputArgumentDiagnosticInfos()[0];
    assertEquals(new DiagnosticInfo(-1, -1, -1, -1, null, StatusCode.BAD, null), status);
  }

  @Test
  void stringInterningIsStableAndSnapshotsAreIndependent() {
    DiagnosticsContext<CallMethodRequest> context = new DiagnosticsContext<>(uint(0x3e0));
    assertEquals(0, context.addString("one"));
    assertEquals(1, context.addString("two"));
    assertEquals(0, context.addString("one"));
    String[] snapshot = context.getStringTable();
    snapshot[0] = "changed";
    assertArrayEquals(new String[] {"one", "two"}, context.getStringTable());
  }
}
