/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Request-local diagnostic state: the requested ReturnDiagnostics mask, a string table for
 * DiagnosticInfo indexes, and operation diagnostic storage.
 *
 * <p>Operation handlers intern strings with {@link #addString(String)} and reference them by index
 * from the DiagnosticInfo entries they return. The service that builds the response decides which
 * of those entries and strings the client asked for; see the Call service for an example. Interning
 * and snapshots are thread-safe.
 */
public class DiagnosticsContext<T> {

  private final Map<T, DiagnosticInfo> diagnosticsMap = new ConcurrentHashMap<>();
  private final Map<String, Integer> strings = new LinkedHashMap<>();
  private final UInteger returnDiagnostics;

  /** Create a context for a request with no requested diagnostics. */
  public DiagnosticsContext() {
    this(uint(0));
  }

  /**
   * Create a context for a request.
   *
   * @param returnDiagnostics the request's ReturnDiagnostics mask.
   */
  public DiagnosticsContext(UInteger returnDiagnostics) {
    this.returnDiagnostics = requireNonNull(returnDiagnostics);
  }

  /**
   * @return the request's ReturnDiagnostics mask.
   */
  public UInteger getReturnDiagnostics() {
    return returnDiagnostics;
  }

  /**
   * Intern a string for use as a DiagnosticInfo index in this request.
   *
   * @param value the diagnostic string.
   * @return the index of {@code value} in this request's string table; the same string always
   *     receives the same index.
   */
  public synchronized int addString(String value) {
    return strings.computeIfAbsent(requireNonNull(value), v -> strings.size());
  }

  /**
   * @return a snapshot of this request's string table, indexed as returned by {@link
   *     #addString(String)}.
   */
  public synchronized String[] getStringTable() {
    return strings.keySet().toArray(String[]::new);
  }

  public EnumSet<OperationDiagnostic> getRequestedOperationDiagnostics(T t) {
    return null;
  }

  public void setOperationDiagnostic(T t, DiagnosticInfo diagnosticInfo) {}

  public Map<T, DiagnosticInfo> getDiagnosticsMap() {
    return diagnosticsMap;
  }

  public DiagnosticInfo[] getDiagnosticInfos(T[] ts) {
    return getDiagnosticInfos(Arrays.asList(ts));
  }

  public DiagnosticInfo[] getDiagnosticInfos(List<T> ts) {
    if (diagnosticsMap.isEmpty()) {
      return new DiagnosticInfo[0];
    } else {
      DiagnosticInfo[] diagnostics = new DiagnosticInfo[ts.size()];

      for (int i = 0; i < ts.size(); i++) {
        DiagnosticInfo diagnosticInfo =
            diagnosticsMap.getOrDefault(ts.get(i), DiagnosticInfo.NULL_VALUE);

        diagnostics[i] = diagnosticInfo;
      }

      return diagnostics;
    }
  }

  public enum OperationDiagnostic {
    SymbolicId,
    LocalizedText,
    InnerStatusCode,
    InnerDiagnostics
  }
}
