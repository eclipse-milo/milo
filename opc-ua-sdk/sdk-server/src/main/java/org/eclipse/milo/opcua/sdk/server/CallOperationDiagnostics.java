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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The {@link OperationDiagnostics} of a Call request, which also collects the diagnostics of each
 * Method's input arguments.
 *
 * <p>A Method handler reports through the {@link AddressSpace.CallContext} it is invoked with.
 */
@NullMarked
public interface CallOperationDiagnostics extends OperationDiagnostics<CallMethodRequest> {

  /**
   * Attach diagnostics to the input arguments of the operation for {@code request}.
   *
   * <p>The service returns them in the operation's {@code inputArgumentDiagnosticInfos}, in place
   * of any the handler put in its result. Arguments are matched by position: supply one entry per
   * input argument in the request, with {@code null} for each argument that has nothing to report.
   * A list of any other length is logged and dropped.
   *
   * @param request the request item instance this address space received.
   * @param diagnostics one diagnostic or {@code null} per input argument.
   */
  void reportArguments(CallMethodRequest request, List<? extends @Nullable Diagnostic> diagnostics);

  /**
   * Get a {@link CallOperationDiagnostics} that collects nothing.
   *
   * @return a {@link CallOperationDiagnostics} that collects nothing.
   */
  static CallOperationDiagnostics none() {
    return NoOperationDiagnostics.CALL;
  }
}
