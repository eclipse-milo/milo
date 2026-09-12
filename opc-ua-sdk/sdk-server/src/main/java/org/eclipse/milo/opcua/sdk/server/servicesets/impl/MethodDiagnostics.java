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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reduces the argument diagnostics returned by Method handlers to the fields the client requested
 * and builds the response StringTable those fields reference.
 *
 * <p>Handlers intern strings in the request's working string table and index it from the
 * DiagnosticInfo entries they return. Only strings referenced by a requested field are carried into
 * the response table, so every retained index is reassigned here. Malformed diagnostics are dropped
 * from their operation without changing its status or outputs, because the Method has already run.
 */
final class MethodDiagnostics {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodDiagnostics.class);

  private static final DiagnosticInfo[] NONE = new DiagnosticInfo[0];

  // OPC 10000-4, Table 1: the operation-level ReturnDiagnostics bits.
  private static final int SYMBOLIC_ID = 0x20;
  private static final int LOCALIZED_TEXT = 0x40;
  private static final int ADDITIONAL_INFO = 0x80;
  private static final int INNER_STATUS_CODE = 0x100;
  private static final int INNER_DIAGNOSTICS = 0x200;
  private static final int OPERATION_LEVEL =
      SYMBOLIC_ID | LOCALIZED_TEXT | ADDITIONAL_INFO | INNER_STATUS_CODE | INNER_DIAGNOSTICS;

  private final Map<String, Integer> table = new LinkedHashMap<>();

  private final int mask;
  private final String[] source;
  private final int maxDepth;

  private MethodDiagnostics(int mask, String[] source, int maxDepth) {
    this.mask = mask;
    this.source = source;
    this.maxDepth = maxDepth;
  }

  /**
   * Filter the argument diagnostics of each result and build the response StringTable.
   *
   * @param returnDiagnostics the request's ReturnDiagnostics mask.
   * @param requests the operations, in request order.
   * @param results one result per operation, in the same order.
   * @param source the request's working string table, as indexed by the results.
   * @param maxDepth the deepest inner DiagnosticInfo nesting accepted.
   * @return the results to send, and the StringTable their diagnostics index, or {@code null} when
   *     no string is referenced.
   */
  static Normalized normalize(
      UInteger returnDiagnostics,
      List<CallMethodRequest> requests,
      List<CallMethodResult> results,
      String[] source,
      int maxDepth) {

    var diagnostics =
        new MethodDiagnostics(returnDiagnostics.intValue() & OPERATION_LEVEL, source, maxDepth);

    var normalized = new CallMethodResult[results.size()];
    for (int i = 0; i < normalized.length; i++) {
      normalized[i] = diagnostics.normalize(requests.get(i), results.get(i));
    }

    String[] stringTable =
        diagnostics.table.isEmpty() ? null : diagnostics.table.keySet().toArray(String[]::new);

    return new Normalized(normalized, stringTable);
  }

  private CallMethodResult normalize(CallMethodRequest request, @Nullable CallMethodResult result) {
    if (result == null || result.getStatusCode() == null) {
      LOGGER.warn("Null result for methodId={}", request.getMethodId());
      return new CallMethodResult(
          new StatusCode(StatusCodes.Bad_InternalError), new StatusCode[0], NONE, new Variant[0]);
    }

    DiagnosticInfo[] original = result.getInputArgumentDiagnosticInfos();
    if (original == null || original.length == 0) {
      return result;
    }

    Variant[] inputs = request.getInputArguments();
    int supplied = inputs == null ? 0 : inputs.length;

    DiagnosticInfo[] filtered;
    if (mask == 0) {
      filtered = NONE;
    } else if (original.length != supplied) {
      LOGGER.warn(
          "Discarding argument diagnostics for methodId={}: {} entries for {} inputs",
          request.getMethodId(),
          original.length,
          supplied);
      filtered = NONE;
    } else {
      filtered = filter(request.getMethodId(), original);
    }

    return new CallMethodResult(
        result.getStatusCode(),
        result.getInputArgumentResults(),
        filtered,
        result.getOutputArguments());
  }

  /** The requested fields of each entry, or none if any entry is malformed or nothing remains. */
  private DiagnosticInfo[] filter(NodeId methodId, DiagnosticInfo[] original) {
    var filtered = new DiagnosticInfo[original.length];
    boolean any = false;
    try {
      for (int i = 0; i < original.length; i++) {
        filtered[i] = filter(original[i], 0);
        any |= !DiagnosticInfo.NULL_VALUE.equals(filtered[i]);
      }
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Discarding argument diagnostics for methodId={}: {}", methodId, e.getMessage());
      return NONE;
    }
    return any ? filtered : NONE;
  }

  private DiagnosticInfo filter(@Nullable DiagnosticInfo info, int depth) {
    if (info == null) {
      return DiagnosticInfo.NULL_VALUE;
    }
    if (depth >= maxDepth) {
      throw new IllegalArgumentException("inner DiagnosticInfo nesting exceeds " + maxDepth);
    }

    boolean symbolicId = (mask & SYMBOLIC_ID) != 0;
    boolean localizedText = (mask & LOCALIZED_TEXT) != 0;

    DiagnosticInfo inner =
        (mask & INNER_DIAGNOSTICS) != 0
            ? filter(info.innerDiagnosticInfo(), depth + 1)
            : DiagnosticInfo.NULL_VALUE;

    return new DiagnosticInfo(
        symbolicId ? index(info.namespaceUri()) : -1,
        symbolicId ? index(info.symbolicId()) : -1,
        localizedText ? index(info.locale()) : -1,
        localizedText ? index(info.localizedText()) : -1,
        (mask & ADDITIONAL_INFO) != 0 ? info.additionalInfo() : null,
        (mask & INNER_STATUS_CODE) != 0 ? info.innerStatusCode() : null,
        DiagnosticInfo.NULL_VALUE.equals(inner) ? null : inner);
  }

  /** Map an index into the working table to its index in the response table. */
  private int index(int original) {
    if (original == -1) {
      return -1;
    }
    if (original < 0 || original >= source.length) {
      throw new IllegalArgumentException(
          "string index " + original + " is not in the request's string table");
    }
    return table.computeIfAbsent(source[original], s -> table.size());
  }

  record Normalized(CallMethodResult[] results, String @Nullable [] stringTable) {}
}
