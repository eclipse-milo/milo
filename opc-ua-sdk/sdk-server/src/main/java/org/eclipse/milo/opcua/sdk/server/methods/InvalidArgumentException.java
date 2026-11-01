/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Thrown to reject one or more input arguments of a Method call.
 *
 * <p>The call fails with {@code Bad_InvalidArgument} and returns one result per supplied input
 * argument. Build one with {@link #builder(int)}, which also attaches a {@link Diagnostic} to each
 * rejected argument:
 *
 * <pre>{@code
 * throw InvalidArgumentException.builder(inputs.length)
 *     .reject(0, StatusCodes.Bad_OutOfRange, Diagnostic.text("Speed must not exceed 3000 rpm"))
 *     .build();
 * }</pre>
 *
 * <p>The Call service returns argument diagnostics when the client asked for operation-level
 * diagnostics and the server's diagnostics policy allows it.
 */
public class InvalidArgumentException extends UaException {

  private final StatusCode[] inputArgumentResults;
  private final List<@Nullable Diagnostic> inputArgumentDiagnostics;

  /**
   * @param inputArgumentResults one result per supplied input argument; Good for each argument that
   *     is valid.
   */
  public InvalidArgumentException(StatusCode[] inputArgumentResults) {
    this(inputArgumentResults, List.of());
  }

  private InvalidArgumentException(
      StatusCode[] inputArgumentResults, List<@Nullable Diagnostic> inputArgumentDiagnostics) {

    super(StatusCodes.Bad_InvalidArgument, "one or more of the provided arguments is invalid");

    this.inputArgumentResults = inputArgumentResults;
    this.inputArgumentDiagnostics = inputArgumentDiagnostics;
  }

  /**
   * Start building an exception for a call with {@code inputCount} supplied input arguments.
   *
   * @param inputCount the number of input arguments supplied in the call.
   * @return a new {@link Builder}; every argument starts out Good.
   */
  public static Builder builder(int inputCount) {
    return new Builder(inputCount);
  }

  /**
   * Get the corresponding {@link StatusCode}s for each input argument value.
   *
   * @return the corresponding {@link StatusCode}s for each input argument value.
   */
  public StatusCode[] getInputArgumentResults() {
    return inputArgumentResults;
  }

  /**
   * Get the diagnostics for the input arguments.
   *
   * @return an empty list if no argument has a diagnostic; otherwise one entry per supplied input
   *     argument, {@code null} for each argument without one.
   */
  public List<@Nullable Diagnostic> getInputArgumentDiagnostics() {
    return inputArgumentDiagnostics;
  }

  /** Builds an {@link InvalidArgumentException}. */
  public static final class Builder {

    private final StatusCode[] results;
    private final @Nullable Diagnostic[] diagnostics;

    private boolean anyDiagnostic = false;

    private Builder(int inputCount) {
      if (inputCount < 0) {
        throw new IllegalArgumentException("inputCount must not be negative: " + inputCount);
      }

      this.results = new StatusCode[inputCount];
      this.diagnostics = new Diagnostic[inputCount];

      Arrays.fill(results, StatusCode.GOOD);
    }

    /**
     * Reject the input argument at {@code index}.
     *
     * @param index the position of the argument in the call's input arguments.
     * @param statusCode why the argument is invalid, a Bad status such as {@code Bad_OutOfRange}.
     * @return this builder.
     * @throws IndexOutOfBoundsException if {@code index} is not a supplied argument.
     * @throws IllegalArgumentException if {@code statusCode} is not Bad.
     */
    public Builder reject(int index, long statusCode) {
      return reject(index, new StatusCode(statusCode));
    }

    /**
     * Reject the input argument at {@code index}.
     *
     * @param index the position of the argument in the call's input arguments.
     * @param statusCode why the argument is invalid, a Bad status such as {@code Bad_OutOfRange}.
     * @return this builder.
     * @throws IndexOutOfBoundsException if {@code index} is not a supplied argument.
     * @throws IllegalArgumentException if {@code statusCode} is not Bad.
     */
    public Builder reject(int index, StatusCode statusCode) {
      requireNonNull(statusCode, "statusCode");

      if (!statusCode.isBad()) {
        throw new IllegalArgumentException("expected a Bad status: " + statusCode);
      }

      results[index] = statusCode;

      return this;
    }

    /**
     * Reject the input argument at {@code index} with a diagnostic that explains why.
     *
     * @param index the position of the argument in the call's input arguments.
     * @param statusCode why the argument is invalid, a Bad status such as {@code Bad_OutOfRange}.
     * @param diagnostic the diagnostic that explains {@code statusCode}.
     * @return this builder.
     * @throws IndexOutOfBoundsException if {@code index} is not a supplied argument.
     * @throws IllegalArgumentException if {@code statusCode} is not Bad.
     */
    public Builder reject(int index, long statusCode, Diagnostic diagnostic) {
      return reject(index, new StatusCode(statusCode), diagnostic);
    }

    /**
     * Reject the input argument at {@code index} with a diagnostic that explains why.
     *
     * @param index the position of the argument in the call's input arguments.
     * @param statusCode why the argument is invalid, a Bad status such as {@code Bad_OutOfRange}.
     * @param diagnostic the diagnostic that explains {@code statusCode}.
     * @return this builder.
     * @throws IndexOutOfBoundsException if {@code index} is not a supplied argument.
     * @throws IllegalArgumentException if {@code statusCode} is not Bad.
     */
    public Builder reject(int index, StatusCode statusCode, Diagnostic diagnostic) {
      reject(index, statusCode);

      diagnostics[index] = requireNonNull(diagnostic, "diagnostic");
      anyDiagnostic = true;

      return this;
    }

    /**
     * Build the exception.
     *
     * @return a new {@link InvalidArgumentException}.
     */
    public InvalidArgumentException build() {
      List<@Nullable Diagnostic> diagnosticList =
          anyDiagnostic
              ? Collections.unmodifiableList(Arrays.asList(diagnostics.clone()))
              : List.of();

      return new InvalidArgumentException(results.clone(), diagnosticList);
    }
  }
}
