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

import java.util.List;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MethodDiagnosticsTest {
  private static final CallMethodRequest REQUEST =
      new CallMethodRequest(new NodeId(1, 1), new NodeId(1, 2), new Variant[] {Variant.NULL_VALUE});
  private static final StatusCode INVALID = new StatusCode(StatusCodes.Bad_InvalidArgument);

  private static CallMethodResult result(DiagnosticInfo... diagnostics) {
    return new CallMethodResult(
        INVALID,
        new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
        diagnostics,
        new Variant[0]);
  }

  @Test
  void compactsIndexesAcrossResultsAndDoesNotMutateRawDiagnostics() {
    DiagnosticInfo info = new DiagnosticInfo(3, 1, 2, 0, "details", StatusCode.BAD, null);
    MethodDiagnostics.Normalized normalized =
        MethodDiagnostics.normalize(
            uint(0x20),
            List.of(REQUEST, REQUEST),
            List.of(result(info), result(info)),
            new String[] {"text", "symbol", "en", "urn:vendor"});
    assertArrayEquals(new String[] {"urn:vendor", "symbol"}, normalized.stringTable());
    DiagnosticInfo filtered = normalized.results().get(1).getInputArgumentDiagnosticInfos()[0];
    assertEquals(new DiagnosticInfo(0, 1, -1, -1, null, null, null), filtered);
    assertEquals(3, info.namespaceUri());
    assertEquals("details", info.additionalInfo());
  }

  // Service-level flags do not authorize argument-level diagnostic fields.
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 31})
  void noOperationMaskReturnsNoDiagnosticsOrStrings(int mask) {
    MethodDiagnostics.Normalized normalized =
        MethodDiagnostics.normalize(
            uint(mask),
            List.of(REQUEST),
            List.of(
                result(new DiagnosticInfo(999, 999, 999, 999, "private", StatusCode.BAD, null))),
            new String[0]);
    assertEquals(INVALID, normalized.results().get(0).getStatusCode());
    assertEquals(0, normalized.results().get(0).getInputArgumentDiagnosticInfos().length);
    assertEquals(0, normalized.stringTable().length);
  }

  @Test
  void nestedDiagnosticsUseSameMaskAndMalformedOperationDoesNotPolluteTable() {
    DiagnosticInfo inner = new DiagnosticInfo(-1, 0, -1, -1, "inner", null, null);
    DiagnosticInfo outer = new DiagnosticInfo(-1, 1, -1, -1, "outer", null, inner);
    DiagnosticInfo malformed = new DiagnosticInfo(2, 99, -1, -1, null, null, null);
    MethodDiagnostics.Normalized normalized =
        MethodDiagnostics.normalize(
            uint(0x2a0),
            List.of(REQUEST, REQUEST),
            List.of(result(malformed), result(outer)),
            new String[] {"inner", "outer", "discard"});
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), normalized.results().get(0).getStatusCode());
    assertArrayEquals(new String[] {"inner", "outer"}, normalized.stringTable());
    DiagnosticInfo actual = normalized.results().get(1).getInputArgumentDiagnosticInfos()[0];
    assertEquals(1, actual.symbolicId());
    assertEquals(0, actual.innerDiagnosticInfo().symbolicId());
    assertEquals("inner", actual.innerDiagnosticInfo().additionalInfo());
  }

  @Test
  void invalidCardinalityAndDepthFailOnlyTheirOperation() {
    DiagnosticInfo inner = new DiagnosticInfo(-1, -1, -1, -1, "inner", null, null);
    DiagnosticInfo outer = new DiagnosticInfo(-1, -1, -1, -1, "outer", null, inner);
    MethodDiagnostics.Normalized normalized =
        MethodDiagnostics.normalize(
            uint(0x280),
            List.of(REQUEST, REQUEST, REQUEST),
            List.of(result(inner, inner), result(outer), result(inner)),
            new String[0],
            new EncodingLimits(8196, 1, 8196, 1));
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), normalized.results().get(0).getStatusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), normalized.results().get(1).getStatusCode());
    assertEquals(INVALID, normalized.results().get(2).getStatusCode());
  }

  @Test
  void requestedFieldMasksAreIndependent() {
    DiagnosticInfo all =
        new DiagnosticInfo(0, 1, 2, 3, "info", StatusCode.BAD, DiagnosticInfo.NULL_VALUE);
    DiagnosticInfo localized =
        MethodDiagnostics.normalize(
                uint(0x40),
                List.of(REQUEST),
                List.of(result(all)),
                new String[] {"ns", "symbol", "en", "text"})
            .results()
            .get(0)
            .getInputArgumentDiagnosticInfos()[0];
    assertEquals(new DiagnosticInfo(-1, -1, 0, 1, null, null, null), localized);
    DiagnosticInfo status =
        MethodDiagnostics.normalize(
                uint(0x100), List.of(REQUEST), List.of(result(all)), new String[0])
            .results()
            .get(0)
            .getInputArgumentDiagnosticInfos()[0];
    assertEquals(new DiagnosticInfo(-1, -1, -1, -1, null, StatusCode.BAD, null), status);
  }

  @Test
  void stringInterningIsStableAndBoundedAndSnapshotsAreIndependent() {
    DiagnosticsContext<CallMethodRequest> context =
        new DiagnosticsContext<>(uint(0x3e0), new EncodingLimits(8196, 1, 16, 128));
    assertEquals(0, context.addString("one"));
    assertEquals(0, context.addString("one"));
    String[] snapshot = context.getStringTable();
    snapshot[0] = "changed";
    assertArrayEquals(new String[] {"one"}, context.getStringTable());
    assertThrows(UaRuntimeException.class, () -> context.addString("two"));
    DiagnosticsContext<CallMethodRequest> shortStrings =
        new DiagnosticsContext<>(uint(0x20), EncodingLimits.DEFAULT, uint(2));
    assertThrows(UaRuntimeException.class, () -> shortStrings.addString("three"));
  }
}
