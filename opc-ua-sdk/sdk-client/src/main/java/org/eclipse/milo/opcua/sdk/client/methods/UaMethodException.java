/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.methods;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.Nullable;

/**
 * A Method call completed with an operation-level status that is not Good.
 *
 * <p>Carries the operation status, the per-argument statuses and diagnostics the server returned,
 * and the response header whose string table those diagnostics index.
 */
public class UaMethodException extends UaException {

  private final StatusCode[] inputArgumentResults;
  private final DiagnosticInfo[] inputArgumentDiagnostics;
  private final @Nullable ResponseHeader responseHeader;

  public UaMethodException(
      StatusCode statusCode,
      StatusCode[] inputArgumentResults,
      DiagnosticInfo[] inputArgumentDiagnostics) {
    this(statusCode, inputArgumentResults, inputArgumentDiagnostics, null);
  }

  public UaMethodException(
      StatusCode statusCode,
      StatusCode[] inputArgumentResults,
      DiagnosticInfo[] inputArgumentDiagnostics,
      @Nullable ResponseHeader responseHeader) {

    super(statusCode);

    this.inputArgumentResults = inputArgumentResults;
    this.inputArgumentDiagnostics = inputArgumentDiagnostics;
    this.responseHeader = responseHeader;
  }

  public UaMethodException(
      Throwable cause,
      StatusCode statusCode,
      StatusCode[] inputArgumentResults,
      DiagnosticInfo[] inputArgumentDiagnostics) {
    this(cause, statusCode, inputArgumentResults, inputArgumentDiagnostics, null);
  }

  public UaMethodException(
      Throwable cause,
      StatusCode statusCode,
      StatusCode[] inputArgumentResults,
      DiagnosticInfo[] inputArgumentDiagnostics,
      @Nullable ResponseHeader responseHeader) {

    super(statusCode.value(), cause);

    this.inputArgumentResults = inputArgumentResults;
    this.inputArgumentDiagnostics = inputArgumentDiagnostics;
    this.responseHeader = responseHeader;
  }

  public UaMethodException(
      long statusCode,
      StatusCode[] inputArgumentResults,
      DiagnosticInfo[] inputArgumentDiagnostics) {

    super(statusCode);

    this.inputArgumentResults = inputArgumentResults;
    this.inputArgumentDiagnostics = inputArgumentDiagnostics;
    this.responseHeader = null;
  }

  /**
   * @return {@link StatusCode}s corresponding to each input argument.
   */
  public StatusCode[] getInputArgumentResults() {
    return inputArgumentResults;
  }

  /**
   * @return {@link DiagnosticInfo}s corresponding to each input argument.
   */
  public DiagnosticInfo[] getInputArgumentDiagnostics() {
    return inputArgumentDiagnostics;
  }

  /**
   * Get the header of the Call response, whose string table the argument diagnostics index.
   *
   * @return the response header, or {@code null} if the failure was not produced from a response.
   */
  public @Nullable ResponseHeader getResponseHeader() {
    return responseHeader;
  }
}
