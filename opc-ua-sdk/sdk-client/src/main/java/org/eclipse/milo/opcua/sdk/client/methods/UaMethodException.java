/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.methods;

import static java.util.Objects.requireNonNull;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Thrown when a method call completes with a non-good {@link StatusCode}.
 *
 * <p>Contains the {@link StatusCode} for the overall call, the input argument results, and the
 * diagnostics the server returned. {@link #getDiagnostic()} explains the overall status, and {@link
 * #getInputArgumentDiagnostics()} explains each input argument. A server returns diagnostics only
 * when the client asks for operation-level fields; see {@code
 * OpcUaClientConfigBuilder.setReturnDiagnostics}.
 *
 * <pre>{@code
 * try {
 *   method.call(inputs);
 * } catch (UaMethodException e) {
 *   List<@Nullable Diagnostic> arguments = e.getInputArgumentDiagnostics();
 *
 *   for (int i = 0; i < arguments.size(); i++) {
 *     Diagnostic argument = arguments.get(i);
 *
 *     if (argument != null) {
 *       System.out.println("argument " + i + ": " + argument.localizedText());
 *     }
 *   }
 * }
 * }</pre>
 */
public class UaMethodException extends UaException {

  private final StatusCode[] inputArgumentResults;
  private final List<@Nullable Diagnostic> inputArgumentDiagnostics;

  /**
   * @param statusCode the status of the call.
   * @param inputArgumentResults the result for each input argument, or empty.
   */
  public UaMethodException(StatusCode statusCode, StatusCode[] inputArgumentResults) {
    this(statusCode, null, inputArgumentResults, List.of());
  }

  /**
   * @param statusCode the status of the call.
   * @param diagnostic the diagnostic that explains {@code statusCode}, or {@code null}.
   * @param inputArgumentResults the result for each input argument, or empty.
   * @param inputArgumentDiagnostics the diagnostic for each input argument, or empty.
   */
  public UaMethodException(
      StatusCode statusCode,
      @Nullable Diagnostic diagnostic,
      StatusCode[] inputArgumentResults,
      List<@Nullable Diagnostic> inputArgumentDiagnostics) {

    this(null, statusCode, diagnostic, inputArgumentResults, inputArgumentDiagnostics);
  }

  /**
   * @param cause the cause of this exception, or {@code null}.
   * @param statusCode the status of the call.
   * @param diagnostic the diagnostic that explains {@code statusCode}, or {@code null}.
   * @param inputArgumentResults the result for each input argument, or empty.
   * @param inputArgumentDiagnostics the diagnostic for each input argument, or empty.
   */
  public UaMethodException(
      @Nullable Throwable cause,
      StatusCode statusCode,
      @Nullable Diagnostic diagnostic,
      StatusCode[] inputArgumentResults,
      List<@Nullable Diagnostic> inputArgumentDiagnostics) {

    // Like Throwable(Throwable), take the message from the cause.
    super(statusCode, cause != null ? cause.toString() : null, diagnostic, cause);

    this.inputArgumentResults = inputArgumentResults;
    this.inputArgumentDiagnostics =
        requireNonNull(inputArgumentDiagnostics, "inputArgumentDiagnostics");
  }

  /**
   * @return {@link StatusCode}s corresponding to each input argument.
   */
  public StatusCode[] getInputArgumentResults() {
    return inputArgumentResults;
  }

  /**
   * Get the diagnostics the server returned for the input arguments.
   *
   * @return an empty list if the server returned none; otherwise one entry per input argument,
   *     {@code null} for each argument without one.
   */
  public List<@Nullable Diagnostic> getInputArgumentDiagnostics() {
    return inputArgumentDiagnostics;
  }
}
