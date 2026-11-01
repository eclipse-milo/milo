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
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.eclipse.milo.opcua.sdk.server.CallOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.HistoryUpdateOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.OperationDiagnostics;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects the diagnostics reported for the operations of one request, keyed by the identity of
 * each request item.
 *
 * <p>The positions of the request items are indexed once, on the request thread, before any address
 * space sees them. After that the index is only read, and each operation's diagnostics live in
 * their own atomic slot, so concurrent reports for distinct operations are safe.
 *
 * @param <T> the type of the request items.
 */
@NullMarked
class OperationDiagnosticsCollector<T> implements OperationDiagnostics<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(OperationDiagnosticsCollector.class);

  private final Map<T, int[]> positions;
  private final AtomicReferenceArray<@Nullable Diagnostic> diagnostics;
  private final AtomicReferenceArray<@Nullable List<@Nullable Diagnostic>> nested;

  /**
   * @param operations the request items, in request order.
   */
  OperationDiagnosticsCollector(List<T> operations) {
    var positions = new IdentityHashMap<T, int[]>(operations.size());

    for (int i = 0; i < operations.size(); i++) {
      int position = i;

      positions.merge(
          operations.get(i),
          new int[] {position},
          (existing, added) -> {
            int[] merged = Arrays.copyOf(existing, existing.length + 1);
            merged[existing.length] = position;
            return merged;
          });
    }

    this.positions = positions;
    this.diagnostics = new AtomicReferenceArray<>(operations.size());
    this.nested = new AtomicReferenceArray<>(operations.size());
  }

  @Override
  public boolean isRequested() {
    return true;
  }

  @Override
  public void report(T operation, Diagnostic diagnostic) {
    // Requesting diagnostics must not turn a reporting bug into a failed operation.
    if (diagnostic == null) {
      LOGGER.debug("Ignoring a null diagnostic reported for {}", operation);
      return;
    }

    for (int position : positionsOf(operation)) {
      diagnostics.set(position, diagnostic);
    }
  }

  /** Attach one diagnostic per nested element, such as an input argument, to {@code operation}. */
  void reportNested(T operation, List<? extends @Nullable Diagnostic> nestedDiagnostics) {
    if (nestedDiagnostics == null) {
      LOGGER.debug("Ignoring a null list of nested diagnostics reported for {}", operation);
      return;
    }

    List<@Nullable Diagnostic> copy =
        Collections.unmodifiableList(new ArrayList<>(nestedDiagnostics));

    for (int position : positionsOf(operation)) {
      nested.set(position, copy);
    }
  }

  /** The diagnostic reported for the operation at {@code position}, if any. */
  @Nullable Diagnostic get(int position) {
    return diagnostics.get(position);
  }

  /** The nested diagnostics reported for the operation at {@code position}, if any. */
  @Nullable List<@Nullable Diagnostic> getNested(int position) {
    return nested.get(position);
  }

  int size() {
    return diagnostics.length();
  }

  private int[] positionsOf(T operation) {
    int[] p = positions.get(operation);

    if (p == null) {
      LOGGER.debug(
          "Ignoring a diagnostic reported for an object that is not an item of this request: {}",
          operation);

      return new int[0];
    } else {
      return p;
    }
  }

  /** The collector behind {@link CallOperationDiagnostics}. */
  static final class Call extends OperationDiagnosticsCollector<CallMethodRequest>
      implements CallOperationDiagnostics {

    Call(List<CallMethodRequest> operations) {
      super(operations);
    }

    @Override
    public void reportArguments(
        CallMethodRequest request, List<? extends @Nullable Diagnostic> diagnostics) {

      reportNested(request, diagnostics);
    }
  }

  /** The collector behind {@link HistoryUpdateOperationDiagnostics}. */
  static final class HistoryUpdate extends OperationDiagnosticsCollector<HistoryUpdateDetails>
      implements HistoryUpdateOperationDiagnostics {

    HistoryUpdate(List<HistoryUpdateDetails> operations) {
      super(operations);
    }

    @Override
    public void reportResults(
        HistoryUpdateDetails details, List<? extends @Nullable Diagnostic> diagnostics) {

      reportNested(details, diagnostics);
    }
  }
}
