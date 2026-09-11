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
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.jspecify.annotations.Nullable;

/**
 * Filters argument diagnostics and assigns response-wide indexes after all Call groups complete.
 */
final class MethodDiagnostics {
  private MethodDiagnostics() {}

  static Normalized normalize(
      UInteger mask,
      List<CallMethodRequest> requests,
      List<CallMethodResult> results,
      String[] workingTable) {
    return normalize(mask, requests, results, workingTable, EncodingLimits.DEFAULT);
  }

  static Normalized normalize(
      UInteger mask,
      List<CallMethodRequest> requests,
      List<CallMethodResult> results,
      String[] workingTable,
      EncodingLimits limits) {
    Map<String, Integer> table = new LinkedHashMap<>();
    List<CallMethodResult> normalized = new ArrayList<>(requests.size());
    int selected = mask.intValue() & 0x3e0;
    for (int i = 0; i < requests.size(); i++) {
      CallMethodResult result = i < results.size() ? results.get(i) : null;
      Variant[] supplied = requests.get(i).getInputArguments();
      int count = supplied == null ? 0 : supplied.length;
      // Do not retain table entries from a rejected operation.
      Map<String, Integer> candidate = new LinkedHashMap<>(table);
      try {
        if (result == null || result.getStatusCode() == null) {
          throw new IllegalArgumentException();
        }
        DiagnosticInfo[] original = result.getInputArgumentDiagnosticInfos();
        if (original != null && original.length != 0 && original.length != count) {
          throw new IllegalArgumentException();
        }
        DiagnosticInfo[] filtered = new DiagnosticInfo[original == null ? 0 : original.length];
        boolean any = false;
        for (int j = 0; j < filtered.length; j++) {
          filtered[j] =
              filter(
                  original[j],
                  selected,
                  workingTable,
                  candidate,
                  new IdentityHashMap<>(),
                  0,
                  limits);
          any |= !DiagnosticInfo.NULL_VALUE.equals(filtered[j]);
        }
        normalized.add(
            new CallMethodResult(
                result.getStatusCode(),
                copy(result.getInputArgumentResults()),
                any ? filtered : new DiagnosticInfo[0],
                copy(result.getOutputArguments())));
        table = candidate;
      } catch (IllegalArgumentException e) {
        normalized.add(
            new CallMethodResult(
                new StatusCode(StatusCodes.Bad_InternalError),
                new StatusCode[0],
                new DiagnosticInfo[0],
                new Variant[0]));
      }
    }
    return new Normalized(List.copyOf(normalized), table.keySet().toArray(String[]::new));
  }

  private static <T> T @Nullable [] copy(T @Nullable [] values) {
    return values == null ? null : values.clone();
  }

  private static DiagnosticInfo filter(
      @Nullable DiagnosticInfo info,
      int mask,
      String[] source,
      Map<String, Integer> table,
      IdentityHashMap<DiagnosticInfo, Boolean> seen,
      int depth,
      EncodingLimits limits) {
    if (info == null || mask == 0) {
      return DiagnosticInfo.NULL_VALUE;
    }
    int maximum =
        limits.getMaxRecursionDepth() > 0
            ? limits.getMaxRecursionDepth()
            : EncodingLimits.DEFAULT_MAX_RECURSION_DEPTH;
    if (depth >= maximum || seen.put(info, Boolean.TRUE) != null) {
      throw new IllegalArgumentException();
    }
    DiagnosticInfo inner =
        (mask & 0x200) != 0
            ? filter(info.innerDiagnosticInfo(), mask, source, table, seen, depth + 1, limits)
            : DiagnosticInfo.NULL_VALUE;
    seen.remove(info);
    int namespace = (mask & 0x20) != 0 ? index(info.namespaceUri(), source, table, limits) : -1;
    int symbolic = (mask & 0x20) != 0 ? index(info.symbolicId(), source, table, limits) : -1;
    int locale = (mask & 0x40) != 0 ? index(info.locale(), source, table, limits) : -1;
    int text = (mask & 0x40) != 0 ? index(info.localizedText(), source, table, limits) : -1;
    return new DiagnosticInfo(
        namespace,
        symbolic,
        locale,
        text,
        (mask & 0x80) != 0 ? info.additionalInfo() : null,
        (mask & 0x100) != 0 ? info.innerStatusCode() : null,
        DiagnosticInfo.NULL_VALUE.equals(inner) ? null : inner);
  }

  private static int index(
      int original, String[] source, Map<String, Integer> table, EncodingLimits limits) {
    if (original == -1) return -1;
    if (original < 0 || original >= source.length || source[original] == null) {
      throw new IllegalArgumentException();
    }
    Integer existing = table.get(source[original]);
    if (existing != null) return existing;
    int budget =
        limits.getMaxMessageSize() > 0
            ? limits.getMaxMessageSize()
            : EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE;
    // Even an empty encoded string consumes four bytes.
    if (table.size() >= budget / 4) throw new IllegalArgumentException();
    int result = table.size();
    table.put(source[original], result);
    return result;
  }

  record Normalized(List<CallMethodResult> results, String[] stringTable) {}
}
