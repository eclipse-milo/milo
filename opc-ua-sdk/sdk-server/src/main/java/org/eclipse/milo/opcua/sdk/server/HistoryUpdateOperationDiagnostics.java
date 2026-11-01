/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The {@link OperationDiagnostics} of a HistoryUpdate request, which also collects the diagnostics
 * of the individual values each operation updates.
 */
@NullMarked
public interface HistoryUpdateOperationDiagnostics
    extends OperationDiagnostics<HistoryUpdateDetails> {

  /**
   * Attach diagnostics to the individual results of the operation for {@code details}.
   *
   * <p>The service returns them in the {@code diagnosticInfos} of the operation's {@link
   * HistoryUpdateResult}, in place of any the address space put there. Supply one entry per entry
   * of the result's {@code operationResults}, with {@code null} for each that has nothing to
   * report. A list of any other length is logged and dropped.
   *
   * @param details the request item instance this address space received.
   * @param diagnostics one diagnostic or {@code null} per entry of {@code operationResults}.
   */
  void reportResults(
      HistoryUpdateDetails details, List<? extends @Nullable Diagnostic> diagnostics);

  /**
   * Get a {@link HistoryUpdateOperationDiagnostics} that collects nothing.
   *
   * @return a {@link HistoryUpdateOperationDiagnostics} that collects nothing.
   */
  static HistoryUpdateOperationDiagnostics none() {
    return NoOperationDiagnostics.HISTORY_UPDATE;
  }
}
