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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.OperationDiagnostics;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Field;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Level;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shapes the diagnostic lists nested in operation results: the input argument diagnostics of a Call
 * result and the diagnostics of a HistoryUpdate result.
 *
 * <p>A list reported through the request's {@link OperationDiagnostics} is resolved. It replaces
 * any list the address space put in its result.
 *
 * <p>A list an address space put in its result directly is raw: its string indexes cannot refer to
 * the response's string table, which the address space never saw. A raw list that has the wrong
 * count, nests deeper than the encoding limits allow, or sets an index for a requested symbolic id
 * or localized text is dropped from its operation without changing its status or other results,
 * because the operation has already run. Indexes for fields the client did not request are ignored.
 *
 * <p>Both kinds then pass through the {@code DiagnosticsPolicy} and the response's encoder like
 * every other diagnostic, so only the requested fields are sent.
 */
@NullMarked
final class NestedDiagnostics {

  private static final Logger LOGGER = LoggerFactory.getLogger(NestedDiagnostics.class);

  private static final DiagnosticInfo[] NONE = new DiagnosticInfo[0];

  private final ServiceDiagnostics diagnostics;
  private final Set<Field> fields;
  private final int maxDepth;

  private NestedDiagnostics(ServiceDiagnostics diagnostics, int maxDepth) {
    this.diagnostics = diagnostics;
    this.fields = diagnostics.getReturnDiagnostics().fields(Level.OPERATION);
    this.maxDepth = maxDepth;
  }

  /**
   * Shape the input argument diagnostics of each Call result.
   *
   * @param diagnostics the diagnostics of the Call response.
   * @param collected the collector for the request's operations.
   * @param requests the operations, in request order.
   * @param results one result per operation, in the same order.
   * @param maxDepth the deepest raw inner DiagnosticInfo nesting accepted.
   * @return the results to send, in request order.
   */
  static CallMethodResult[] normalizeCallResults(
      ServiceDiagnostics diagnostics,
      OperationDiagnostics<CallMethodRequest> collected,
      List<CallMethodRequest> requests,
      List<@Nullable CallMethodResult> results,
      int maxDepth) {

    var nested = new NestedDiagnostics(diagnostics, maxDepth);

    var normalized = new CallMethodResult[results.size()];
    for (int i = 0; i < normalized.length; i++) {
      normalized[i] = nested.normalize(collected, i, requests.get(i), results.get(i));
    }

    return normalized;
  }

  /**
   * Shape the diagnostics of each HistoryUpdate result.
   *
   * @param diagnostics the diagnostics of the HistoryUpdate response.
   * @param collected the collector for the request's operations.
   * @param results one result per operation, in request order.
   * @param maxDepth the deepest raw inner DiagnosticInfo nesting accepted.
   * @return the results to send, in request order.
   */
  static @Nullable HistoryUpdateResult[] normalizeHistoryUpdateResults(
      ServiceDiagnostics diagnostics,
      OperationDiagnostics<HistoryUpdateDetails> collected,
      List<@Nullable HistoryUpdateResult> results,
      int maxDepth) {

    var nested = new NestedDiagnostics(diagnostics, maxDepth);

    var normalized = new HistoryUpdateResult[results.size()];
    for (int i = 0; i < normalized.length; i++) {
      normalized[i] = nested.normalize(collected, i, results.get(i));
    }

    return normalized;
  }

  private CallMethodResult normalize(
      OperationDiagnostics<CallMethodRequest> collected,
      int position,
      CallMethodRequest request,
      @Nullable CallMethodResult result) {

    if (result == null || result.getStatusCode() == null) {
      LOGGER.warn("Null result for methodId={}", request.getMethodId());
      return new CallMethodResult(
          new StatusCode(StatusCodes.Bad_InternalError), new StatusCode[0], NONE, new Variant[0]);
    }

    Variant[] inputs = request.getInputArguments();
    int supplied = inputs == null ? 0 : inputs.length;

    List<@Nullable Diagnostic> resolved = diagnostics.nested(collected, position);
    DiagnosticInfo[] original = result.getInputArgumentDiagnosticInfos();

    DiagnosticInfo[] argumentDiagnostics;
    if (resolved != null) {
      argumentDiagnostics =
          encode(
              resolved,
              statuses(result.getInputArgumentResults(), supplied, result.getStatusCode()),
              supplied,
              request.getMethodId());
    } else if (original == null || original.length == 0) {
      return result;
    } else {
      argumentDiagnostics =
          encodeRaw(
              original,
              statuses(result.getInputArgumentResults(), supplied, result.getStatusCode()),
              supplied,
              request.getMethodId());
    }

    return new CallMethodResult(
        result.getStatusCode(),
        result.getInputArgumentResults(),
        argumentDiagnostics,
        result.getOutputArguments());
  }

  private @Nullable HistoryUpdateResult normalize(
      OperationDiagnostics<HistoryUpdateDetails> collected,
      int position,
      @Nullable HistoryUpdateResult result) {

    if (result == null) {
      return null;
    }

    StatusCode[] operationResults = result.getOperationResults();
    int count = operationResults == null ? 0 : operationResults.length;

    List<@Nullable Diagnostic> resolved = diagnostics.nested(collected, position);
    DiagnosticInfo[] original = result.getDiagnosticInfos();

    DiagnosticInfo[] resultDiagnostics;
    if (resolved != null) {
      resultDiagnostics =
          encode(
              resolved,
              statuses(operationResults, count, result.getStatusCode()),
              count,
              "HistoryUpdate operation " + position);
    } else if (original == null || original.length == 0) {
      return result;
    } else {
      resultDiagnostics =
          encodeRaw(
              original,
              statuses(operationResults, count, result.getStatusCode()),
              count,
              "HistoryUpdate operation " + position);
    }

    return new HistoryUpdateResult(result.getStatusCode(), operationResults, resultDiagnostics);
  }

  /** Encode a resolved nested list, or drop it if it has the wrong count. */
  private DiagnosticInfo[] encode(
      List<@Nullable Diagnostic> resolved,
      List<@Nullable StatusCode> statuses,
      int expected,
      Object owner) {

    if (resolved.size() != expected) {
      LOGGER.warn(
          "Discarding nested diagnostics for {}: {} entries for {} elements",
          owner,
          resolved.size(),
          expected);
      return NONE;
    }

    return diagnostics.encodeAll(resolved, statuses);
  }

  /**
   * The status each nested diagnostic explains: the element's own result when there is one per
   * element, otherwise the operation's status.
   */
  private static List<@Nullable StatusCode> statuses(
      StatusCode @Nullable [] elementResults, int count, @Nullable StatusCode operationStatus) {

    var statuses = new ArrayList<@Nullable StatusCode>(count);
    boolean perElement = elementResults != null && elementResults.length == count;

    for (int i = 0; i < count; i++) {
      statuses.add(perElement ? elementResults[i] : operationStatus);
    }

    return statuses;
  }

  /**
   * Validate and encode a raw nested list, or drop it if it is malformed.
   *
   * @param original the raw list from the result.
   * @param statuses the status each entry explains.
   * @param expected the number of entries the list must have.
   * @param owner what the list belongs to, for logs.
   * @return the encoded list: empty, or one entry per element.
   */
  private DiagnosticInfo[] encodeRaw(
      DiagnosticInfo[] original, List<@Nullable StatusCode> statuses, int expected, Object owner) {

    if (fields.isEmpty()) {
      return NONE;
    }

    if (original.length != expected) {
      LOGGER.warn(
          "Discarding nested diagnostics for {}: {} entries for {} elements",
          owner,
          original.length,
          expected);
      return NONE;
    }

    var resolved = new ArrayList<@Nullable Diagnostic>(original.length);
    try {
      for (DiagnosticInfo info : original) {
        resolved.add(resolve(info, 0));
      }
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Discarding nested diagnostics for {}: {}", owner, e.getMessage());
      return NONE;
    }

    return diagnostics.encodeAll(resolved, statuses);
  }

  /** The index-free content of a raw entry, after checking it carries no requested index. */
  private @Nullable Diagnostic resolve(@Nullable DiagnosticInfo info, int depth) {
    if (info == null) {
      return null;
    }
    if (depth >= maxDepth) {
      throw new IllegalArgumentException("inner DiagnosticInfo nesting exceeds " + maxDepth);
    }
    if (fields.contains(Field.SYMBOLIC_ID)) {
      requireNoIndex(info.namespaceUri(), "namespaceUri");
      requireNoIndex(info.symbolicId(), "symbolicId");
    }
    if (fields.contains(Field.LOCALIZED_TEXT)) {
      requireNoIndex(info.locale(), "locale");
      requireNoIndex(info.localizedText(), "localizedText");
    }

    Diagnostic inner =
        fields.contains(Field.INNER_DIAGNOSTICS)
            ? resolve(info.innerDiagnosticInfo(), depth + 1)
            : null;

    return new Diagnostic(null, null, null, info.additionalInfo(), info.innerStatusCode(), inner);
  }

  private static void requireNoIndex(int index, String field) {
    if (index != -1) {
      throw new IllegalArgumentException(
          field + " index " + index + " was set without the response's string table");
    }
  }
}
