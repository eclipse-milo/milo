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
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** {@link OperationDiagnostics} that collect nothing, one per interface. */
@NullMarked
final class NoOperationDiagnostics {

  static final OperationDiagnostics<Object> OPERATIONS =
      new OperationDiagnostics<>() {
        @Override
        public boolean isRequested() {
          return false;
        }

        @Override
        public void report(Object operation, Diagnostic diagnostic) {}
      };

  static final CallOperationDiagnostics CALL =
      new CallOperationDiagnostics() {
        @Override
        public boolean isRequested() {
          return false;
        }

        @Override
        public void report(CallMethodRequest operation, Diagnostic diagnostic) {}

        @Override
        public void reportArguments(
            CallMethodRequest request, List<? extends @Nullable Diagnostic> diagnostics) {}
      };

  static final HistoryUpdateOperationDiagnostics HISTORY_UPDATE =
      new HistoryUpdateOperationDiagnostics() {
        @Override
        public boolean isRequested() {
          return false;
        }

        @Override
        public void report(HistoryUpdateDetails operation, Diagnostic diagnostic) {}

        @Override
        public void reportResults(
            HistoryUpdateDetails details, List<? extends @Nullable Diagnostic> diagnostics) {}
      };

  private NoOperationDiagnostics() {}
}
