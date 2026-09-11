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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Request-local diagnostic strings and operation diagnostic storage.
 *
 * <p>Call handlers can intern strings for argument diagnostics. The Call service filters requested
 * fields and compacts those indexes into the response StringTable. Interning and snapshots are
 * thread-safe. The legacy operation-diagnostic producer methods remain unimplemented.
 */
public class DiagnosticsContext<T> {

  private final Map<T, DiagnosticInfo> diagnosticsMap = new ConcurrentHashMap<>();
  private final Map<String, Integer> strings = new LinkedHashMap<>();
  private final UInteger returnDiagnostics;
  private final int maxStringBytes;
  private final long maxStringLength;
  private long stringBytes;

  /** Create a context with no requested diagnostics and default encoding limits. */
  public DiagnosticsContext() {
    this(uint(0));
  }

  /**
   * @param returnDiagnostics the request's ReturnDiagnostics mask.
   */
  public DiagnosticsContext(UInteger returnDiagnostics) {
    this(returnDiagnostics, EncodingLimits.DEFAULT);
  }

  /**
   * @param returnDiagnostics the request's ReturnDiagnostics mask.
   * @param limits the response encoding limits used to bound diagnostic string storage.
   */
  public DiagnosticsContext(UInteger returnDiagnostics, EncodingLimits limits) {
    this(returnDiagnostics, limits, UInteger.MAX);
  }

  /**
   * @param returnDiagnostics the request's ReturnDiagnostics mask.
   * @param limits the response encoding limits used to bound diagnostic string storage.
   * @param maxStringLength the maximum string character count; zero means unspecified.
   */
  public DiagnosticsContext(
      UInteger returnDiagnostics, EncodingLimits limits, UInteger maxStringLength) {
    this.returnDiagnostics = requireNonNull(returnDiagnostics);
    this.maxStringLength = requireNonNull(maxStringLength).longValue();
    int configured = requireNonNull(limits).getMaxMessageSize();
    maxStringBytes = configured > 0 ? configured : EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE;
  }

  /**
   * @return the request's ReturnDiagnostics mask.
   */
  public UInteger getReturnDiagnostics() {
    return returnDiagnostics;
  }

  /**
   * Intern a string for use in a DiagnosticInfo index in this request.
   *
   * @param value the diagnostic string.
   * @return the stable request-local index of the string.
   * @throws UaRuntimeException with Bad_InternalError if the response string budget is exceeded.
   */
  public synchronized int addString(String value) {
    requireNonNull(value);
    Integer existing = strings.get(value);
    if (existing != null) {
      return existing;
    }
    // Four bytes for the encoded length and a conservative UTF-8 bound per UTF-16 code unit.
    long encodedBytes = 4L + 3L * value.length();
    if ((maxStringLength != 0 && value.length() > maxStringLength)
        || stringBytes + encodedBytes > maxStringBytes) {
      throw new UaRuntimeException(
          StatusCodes.Bad_InternalError, "Diagnostic string budget exceeded");
    }
    int index = strings.size();
    strings.put(value, index);
    stringBytes += encodedBytes;
    return index;
  }

  /**
   * @return a defensive snapshot of the request's working StringTable.
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
