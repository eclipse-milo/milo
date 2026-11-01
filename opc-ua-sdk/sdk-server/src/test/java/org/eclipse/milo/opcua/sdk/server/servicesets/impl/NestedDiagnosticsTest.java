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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.CallOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsPolicy;
import org.eclipse.milo.opcua.sdk.server.HistoryUpdateOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.ResponseDiagnostics;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NestedDiagnosticsTest {

  private static final StatusCode INVALID = new StatusCode(StatusCodes.Bad_InvalidArgument);
  private static final StatusCode OUT_OF_RANGE = new StatusCode(StatusCodes.Bad_OutOfRange);
  private static final int MAX_DEPTH = 128;

  private static final DiagnosticsPolicy PASS_THROUGH = (request, status, diagnostic) -> diagnostic;

  /** Raw argument diagnostics: an address space put them in its result itself. */
  @Nested
  class RawCallArguments {

    private static final CallMethodRequest REQUEST = request();

    // Part 4 §5.12.2.2: argument diagnostics are empty unless operation-level diagnostics were
    // requested. Service-level flags do not authorize them.
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 31})
    void noOperationMaskReturnsNoArgumentDiagnostics(int mask) {
      DiagnosticInfo info = new DiagnosticInfo(-1, -1, -1, -1, "private", StatusCode.BAD, null);

      CallMethodResult[] normalized = normalize(mask, List.of(invalid(info)), MAX_DEPTH);

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

      CallMethodResult[] normalized =
          normalize(0x3e0, Arrays.asList(denied, good, null), MAX_DEPTH);

      assertSame(denied, normalized[0]);
      assertSame(good, normalized[1]);
      assertEquals(new StatusCode(StatusCodes.Bad_InternalError), normalized[2].getStatusCode());
    }

    // Part 4 §7.32: each ReturnDiagnostics bit selects one field, so a client that asks for one
    // field must not receive the others.
    @Test
    void eachRequestedFieldIsKeptIndependently() {
      DiagnosticInfo all = new DiagnosticInfo(-1, -1, -1, -1, "info", OUT_OF_RANGE, null);

      DiagnosticInfo additionalInfo =
          firstArgument(normalize(0x80, List.of(invalid(all)), MAX_DEPTH)[0]);
      DiagnosticInfo innerStatus =
          firstArgument(normalize(0x100, List.of(invalid(all)), MAX_DEPTH)[0]);

      assertEquals(new DiagnosticInfo(-1, -1, -1, -1, "info", null, null), additionalInfo);
      assertEquals(new DiagnosticInfo(-1, -1, -1, -1, null, OUT_OF_RANGE, null), innerStatus);
    }

    @Test
    void innerDiagnosticsAreReturnedOnlyWhenRequestedAndUseTheSameMask() {
      DiagnosticInfo inner = new DiagnosticInfo(-1, -1, -1, -1, "inner", OUT_OF_RANGE, null);
      DiagnosticInfo outer = new DiagnosticInfo(-1, -1, -1, -1, "outer", null, inner);

      DiagnosticInfo withInner =
          firstArgument(normalize(0x280, List.of(invalid(outer)), MAX_DEPTH)[0]);
      DiagnosticInfo withoutInner =
          firstArgument(normalize(0x80, List.of(invalid(outer)), MAX_DEPTH)[0]);

      assertEquals(
          new DiagnosticInfo(-1, -1, -1, -1, "inner", null, null), withInner.innerDiagnosticInfo());
      assertNull(
          withoutInner.innerDiagnosticInfo(), "inner entry without the InnerDiagnostics bit");
    }

    // The Method already ran, so a malformed diagnostic loses only the diagnostic. A raw index
    // cannot refer to the response's string table, which the handler never saw.
    @Test
    void malformedIndexCardinalityAndDepthDropOnlyTheirOwnDiagnostics() {
      DiagnosticInfo indexed = new DiagnosticInfo(-1, 0, -1, -1, "indexed", null, null);
      DiagnosticInfo inner = new DiagnosticInfo(-1, -1, -1, -1, "inner", null, null);
      DiagnosticInfo outer = new DiagnosticInfo(-1, -1, -1, -1, "outer", null, inner);
      DiagnosticInfo wellFormed = new DiagnosticInfo(-1, -1, -1, -1, "kept", null, null);

      CallMethodResult[] normalized =
          normalize(
              0x2a0,
              List.of(invalid(indexed), invalid(inner, inner), invalid(outer), invalid(wellFormed)),
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

      DiagnosticInfo filtered =
          firstArgument(normalize(0x80, List.of(invalid(indexed)), MAX_DEPTH)[0]);

      assertEquals(new DiagnosticInfo(-1, -1, -1, -1, "info", null, null), filtered);
    }

    // Raw diagnostics pass through the DiagnosticsPolicy like every other diagnostic.
    @Test
    void policyAppliesToRawArgumentDiagnostics() {
      DiagnosticInfo info = new DiagnosticInfo(-1, -1, -1, -1, "info", null, null);
      DiagnosticsPolicy dropOutOfRange =
          (request, status, diagnostic) -> status.equals(OUT_OF_RANGE) ? null : diagnostic;

      ServiceDiagnostics diagnostics = diagnostics(0x80, dropOutOfRange);

      CallMethodResult[] normalized =
          NestedDiagnostics.normalizeCallResults(
              diagnostics,
              CallOperationDiagnostics.none(),
              List.of(REQUEST),
              List.of(invalid(info)),
              MAX_DEPTH);

      assertEquals(0, normalized[0].getInputArgumentDiagnosticInfos().length);
    }

    // The default policy drops a diagnostic whose inner status is security-related. Requesting only
    // additional info must not hide that inner status from it, or a client could read text that a
    // request for every field would not get.
    @ParameterizedTest
    @ValueSource(ints = {0x080, 0x380})
    void policySeesInnerStatusesWhateverTheMask(int mask) {
      CallMethodResult[] normalized =
          normalizeWithDefaultPolicy(mask, StatusCodes.Bad_UserAccessDenied);

      assertEquals(0, normalized[0].getInputArgumentDiagnosticInfos().length);
    }

    @Test
    void innerStatusesThatAreNotSecurityRelatedKeepTheDiagnostic() {
      CallMethodResult[] normalized = normalizeWithDefaultPolicy(0x080, StatusCodes.Bad_Timeout);

      assertEquals(
          new DiagnosticInfo(-1, -1, -1, -1, "account locked", null, null),
          firstArgument(normalized[0]));
    }

    private CallMethodResult[] normalizeWithDefaultPolicy(int mask, long innerStatus) {
      DiagnosticInfo inner =
          new DiagnosticInfo(-1, -1, -1, -1, null, new StatusCode(innerStatus), null);
      DiagnosticInfo outer = new DiagnosticInfo(-1, -1, -1, -1, "account locked", null, inner);

      var diagnostics =
          new ServiceDiagnostics(
              ReturnDiagnostics.decode(uint(mask)),
              DiagnosticsPolicy.defaults(),
              mock(Session.class),
              Service.METHOD_CALL);

      return NestedDiagnostics.normalizeCallResults(
          diagnostics,
          CallOperationDiagnostics.none(),
          List.of(REQUEST),
          List.of(invalid(outer)),
          MAX_DEPTH);
    }

    private CallMethodResult[] normalize(
        int mask, List<@Nullable CallMethodResult> results, int maxDepth) {

      return NestedDiagnostics.normalizeCallResults(
          diagnostics(mask, PASS_THROUGH),
          CallOperationDiagnostics.none(),
          Collections.nCopies(results.size(), REQUEST),
          results,
          maxDepth);
    }
  }

  /** Resolved argument diagnostics: reported through the request's collector. */
  @Nested
  class ResolvedCallArguments {

    @Test
    void reportedArgumentDiagnosticsReplaceRawOnesAndResolveThroughTheResponse() {
      CallMethodRequest request = request();
      ServiceDiagnostics diagnostics = diagnostics(0x3e0, PASS_THROUGH);
      CallOperationDiagnostics collector = diagnostics.collectCall(List.of(request));

      collector.reportArguments(request, List.of(Diagnostic.text("Speed must not exceed 3000")));

      DiagnosticInfo raw = new DiagnosticInfo(-1, -1, -1, -1, "raw", null, null);
      CallMethodResult[] normalized =
          NestedDiagnostics.normalizeCallResults(
              diagnostics, collector, List.of(request), List.of(invalid(raw)), MAX_DEPTH);

      ResponseDiagnostics resolver =
          ResponseDiagnostics.of(diagnostics.responseHeader(callRequest()));

      assertEquals(
          Collections.singletonList(Diagnostic.text("Speed must not exceed 3000")),
          resolver.resolve(normalized[0].getInputArgumentDiagnosticInfos()));
    }

    // Arguments are matched by position, so a list of the wrong length cannot be attributed.
    @Test
    void reportedListOfTheWrongLengthIsDropped() {
      CallMethodRequest request = request();
      ServiceDiagnostics diagnostics = diagnostics(0x3e0, PASS_THROUGH);
      CallOperationDiagnostics collector = diagnostics.collectCall(List.of(request));

      collector.reportArguments(
          request, List.of(Diagnostic.text("first"), Diagnostic.text("second")));

      CallMethodResult[] normalized =
          NestedDiagnostics.normalizeCallResults(
              diagnostics, collector, List.of(request), List.of(invalid()), MAX_DEPTH);

      assertEquals(INVALID, normalized[0].getStatusCode());
      assertEquals(0, normalized[0].getInputArgumentDiagnosticInfos().length);
    }

    // Each argument diagnostic is checked against the status it explains, the argument's own
    // result, so the default policy's security filter applies per argument.
    @Test
    void policySeesEachArgumentResult() {
      CallMethodRequest request = request();
      DiagnosticsPolicy dropOutOfRange =
          (r, status, diagnostic) -> status.equals(OUT_OF_RANGE) ? null : diagnostic;
      ServiceDiagnostics diagnostics = diagnostics(0x3e0, dropOutOfRange);
      CallOperationDiagnostics collector = diagnostics.collectCall(List.of(request));

      collector.reportArguments(request, List.of(Diagnostic.text("out of range")));

      CallMethodResult[] normalized =
          NestedDiagnostics.normalizeCallResults(
              diagnostics, collector, List.of(request), List.of(invalid()), MAX_DEPTH);

      assertEquals(0, normalized[0].getInputArgumentDiagnosticInfos().length);
    }
  }

  @Nested
  class HistoryUpdateResults {

    @Test
    void reportedResultDiagnosticsAreEncodedPerOperationResult() {
      HistoryUpdateDetails details = details();
      ServiceDiagnostics diagnostics = diagnostics(0x3e0, PASS_THROUGH);
      HistoryUpdateOperationDiagnostics collector =
          diagnostics.collectHistoryUpdate(List.of(details));

      collector.reportResults(details, Arrays.asList(null, Diagnostic.text("no data at time")));

      HistoryUpdateResult[] normalized =
          NestedDiagnostics.normalizeHistoryUpdateResults(
              diagnostics, collector, List.of(historyUpdateResult(null)), MAX_DEPTH);

      ResponseDiagnostics resolver =
          ResponseDiagnostics.of(diagnostics.responseHeader(callRequest()));

      assertEquals(
          Arrays.asList(null, Diagnostic.text("no data at time")),
          resolver.resolve(normalized[0].getDiagnosticInfos()));
    }

    // Part 4 §5.11.5.2: the result diagnostics are empty unless diagnostics were requested. An
    // address space's raw list is held to the same mask as Call's argument diagnostics.
    @Test
    void rawResultDiagnosticsAreNotSentWithoutOperationLevelDiagnostics() {
      DiagnosticInfo raw = new DiagnosticInfo(-1, -1, -1, -1, "raw", null, null);

      HistoryUpdateResult[] normalized =
          NestedDiagnostics.normalizeHistoryUpdateResults(
              diagnostics(0, PASS_THROUGH),
              HistoryUpdateOperationDiagnostics.none(),
              List.of(historyUpdateResult(new DiagnosticInfo[] {null, raw})),
              MAX_DEPTH);

      assertEquals(0, normalized[0].getDiagnosticInfos().length);
    }

    @Test
    void resultsWithoutDiagnosticsPassThrough() {
      HistoryUpdateResult result = historyUpdateResult(null);

      HistoryUpdateResult[] normalized =
          NestedDiagnostics.normalizeHistoryUpdateResults(
              diagnostics(0x3e0, PASS_THROUGH),
              HistoryUpdateOperationDiagnostics.none(),
              List.of(result),
              MAX_DEPTH);

      assertSame(result, normalized[0]);
    }

    private static HistoryUpdateResult historyUpdateResult(
        DiagnosticInfo @Nullable [] diagnosticInfos) {

      return new HistoryUpdateResult(
          StatusCode.GOOD,
          new StatusCode[] {StatusCode.GOOD, new StatusCode(StatusCodes.Bad_NoData)},
          diagnosticInfos);
    }

    private static HistoryUpdateDetails details() {
      return new DeleteRawModifiedDetails(new NodeId(1, "history"), false, null, null);
    }
  }

  private static ServiceDiagnostics diagnostics(int mask, DiagnosticsPolicy policy) {
    return new ServiceDiagnostics(
        ReturnDiagnostics.decode(uint(mask)), policy, null, Service.METHOD_CALL);
  }

  private static CallMethodRequest request() {
    return new CallMethodRequest(
        new NodeId(1, 1), new NodeId(1, 2), new Variant[] {Variant.NULL_VALUE});
  }

  private static CallMethodResult invalid(DiagnosticInfo... diagnostics) {
    return new CallMethodResult(
        INVALID, new StatusCode[] {OUT_OF_RANGE}, diagnostics, new Variant[0]);
  }

  private static DiagnosticInfo firstArgument(CallMethodResult result) {
    return result.getInputArgumentDiagnosticInfos()[0];
  }

  private static CallRequest callRequest() {
    return new CallRequest(
        new RequestHeader(NodeId.NULL_VALUE, null, uint(1), uint(0x3e0), null, uint(0), null),
        new CallMethodRequest[0]);
  }
}
