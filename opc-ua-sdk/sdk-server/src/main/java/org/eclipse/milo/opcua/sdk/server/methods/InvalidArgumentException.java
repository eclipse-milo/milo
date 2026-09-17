/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import java.util.Arrays;
import java.util.TreeMap;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * One or more input argument values of a Method call are invalid.
 *
 * <p>The operation status is Bad_InvalidArgument and each input argument carries its own status.
 * Build one with the {@link #builder()} to reject individual arguments by index:
 *
 * <pre>{@code
 * throw InvalidArgumentException.builder()
 *     .argument(0, StatusCodes.Bad_OutOfRange, "mode must be 0..3")
 *     .build();
 * }</pre>
 *
 * Arguments the builder does not mention are Good. When such an exception is thrown from a handler,
 * Milo sizes the argument results to the number of inputs the caller supplied and reports the
 * diagnostic text as the {@code additionalInfo} of that argument's {@link DiagnosticInfo}, subject
 * to the request's ReturnDiagnostics mask.
 */
public class InvalidArgumentException extends UaException {

  private final StatusCode[] inputArgumentResults;
  private final DiagnosticInfo[] inputArgumentDiagnosticInfos;
  private final boolean sparse;

  public InvalidArgumentException(StatusCode[] inputArgumentResults) {
    this(inputArgumentResults, new DiagnosticInfo[0]);
  }

  public InvalidArgumentException(
      StatusCode[] inputArgumentResults, DiagnosticInfo[] inputArgumentDiagnosticInfos) {
    this(inputArgumentResults, inputArgumentDiagnosticInfos, false);
  }

  private InvalidArgumentException(
      StatusCode[] inputArgumentResults,
      DiagnosticInfo[] inputArgumentDiagnosticInfos,
      boolean sparse) {

    super(StatusCodes.Bad_InvalidArgument, "one or more of the provided arguments is invalid");

    this.inputArgumentResults = inputArgumentResults;
    this.inputArgumentDiagnosticInfos = inputArgumentDiagnosticInfos;
    this.sparse = sparse;
  }

  /**
   * Create a builder that records a status, and optionally diagnostic text, per argument index.
   *
   * @return a new builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Get the corresponding {@link StatusCode}s for each input argument value.
   *
   * <p>For an exception created by the {@link #builder()} the array extends only to the highest
   * index mentioned; see {@link #isSparse()}.
   *
   * @return the corresponding {@link StatusCode}s for each input argument value.
   */
  public StatusCode[] getInputArgumentResults() {
    return inputArgumentResults;
  }

  /**
   * Get the corresponding {@link DiagnosticInfo}s for each input argument value, if supported.
   *
   * @return the corresponding {@link DiagnosticInfo}s for each input argument value, if supported.
   */
  public DiagnosticInfo[] getInputArgumentDiagnosticInfos() {
    return inputArgumentDiagnosticInfos;
  }

  /**
   * Whether this exception was created by the {@link #builder()} and so describes only the
   * arguments it mentions, rather than one entry per supplied input.
   *
   * <p>{@link AbstractMethodInvocationHandler} resizes the arrays of a sparse exception to the
   * number of inputs the caller supplied, filling unmentioned arguments with Good and no
   * diagnostics. The arrays of an exception built with a constructor are reported as given.
   *
   * @return {@code true} if the argument arrays should be resized to the supplied input count.
   */
  public boolean isSparse() {
    return sparse;
  }

  /** Builds an {@link InvalidArgumentException} one argument at a time. */
  public static final class Builder {

    private final TreeMap<Integer, StatusCode> statuses = new TreeMap<>();
    private final TreeMap<Integer, String> texts = new TreeMap<>();

    private Builder() {}

    /**
     * Reject the argument at {@code index}.
     *
     * @param index the zero-based position of the argument in the Method's InputArguments.
     * @param statusCode the status for that argument, usually Bad.
     * @return this builder.
     */
    public Builder argument(int index, long statusCode) {
      return argument(index, new StatusCode(statusCode), null);
    }

    /**
     * Reject the argument at {@code index} with diagnostic text.
     *
     * @param index the zero-based position of the argument in the Method's InputArguments.
     * @param statusCode the status for that argument, usually Bad.
     * @param diagnosticText text explaining the rejection, or {@code null} for none.
     * @return this builder.
     */
    public Builder argument(int index, long statusCode, @Nullable String diagnosticText) {
      return argument(index, new StatusCode(statusCode), diagnosticText);
    }

    /**
     * Reject the argument at {@code index}.
     *
     * @param index the zero-based position of the argument in the Method's InputArguments.
     * @param statusCode the status for that argument, usually Bad.
     * @return this builder.
     */
    public Builder argument(int index, StatusCode statusCode) {
      return argument(index, statusCode, null);
    }

    /**
     * Reject the argument at {@code index} with diagnostic text.
     *
     * @param index the zero-based position of the argument in the Method's InputArguments.
     * @param statusCode the status for that argument, usually Bad.
     * @param diagnosticText text explaining the rejection, or {@code null} for none.
     * @return this builder.
     * @throws IllegalArgumentException if {@code index} is negative.
     */
    public Builder argument(int index, StatusCode statusCode, @Nullable String diagnosticText) {
      if (index < 0) {
        throw new IllegalArgumentException("argument index must not be negative: " + index);
      }
      statuses.put(index, statusCode);
      if (diagnosticText != null) {
        texts.put(index, diagnosticText);
      } else {
        texts.remove(index);
      }
      return this;
    }

    /**
     * Build the exception.
     *
     * <p>The argument arrays extend to the highest index mentioned; arguments in between are Good.
     * Diagnostic text becomes the {@code additionalInfo} of that argument's {@link DiagnosticInfo}.
     *
     * @return the exception.
     */
    public InvalidArgumentException build() {
      int length = statuses.isEmpty() ? 0 : statuses.lastKey() + 1;

      StatusCode[] results = new StatusCode[length];
      Arrays.fill(results, StatusCode.GOOD);
      statuses.forEach((index, status) -> results[index] = status);

      DiagnosticInfo[] diagnostics = new DiagnosticInfo[texts.isEmpty() ? 0 : length];
      Arrays.fill(diagnostics, DiagnosticInfo.NULL_VALUE);
      texts.forEach(
          (index, text) ->
              diagnostics[index] = new DiagnosticInfo(-1, -1, -1, -1, text, null, null));

      return new InvalidArgumentException(results, diagnostics, true);
    }
  }
}
