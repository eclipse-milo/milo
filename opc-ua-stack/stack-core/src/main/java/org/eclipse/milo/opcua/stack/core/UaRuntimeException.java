/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

public class UaRuntimeException extends RuntimeException implements UaExceptionStatus {

  private final StatusCode statusCode;

  private final @Nullable Diagnostic diagnostic;

  public UaRuntimeException(Throwable cause) {
    super(cause);

    this.statusCode = new StatusCode(StatusCodes.Bad_InternalError);
    this.diagnostic = null;
  }

  public UaRuntimeException(long statusCode) {
    this.statusCode = new StatusCode(statusCode);
    this.diagnostic = null;
  }

  public UaRuntimeException(long statusCode, String message) {
    super(message);

    this.statusCode = new StatusCode(statusCode);
    this.diagnostic = null;
  }

  public UaRuntimeException(long statusCode, Throwable cause) {
    super(cause);

    this.statusCode = new StatusCode(statusCode);
    this.diagnostic = null;
  }

  /**
   * Creates a new UaRuntimeException that carries a {@link Diagnostic} explaining its status.
   *
   * <p>The message is for logs and stays on the server. The diagnostic is for the client.
   *
   * @param statusCode the status of the failure.
   * @param message the message for logs, or {@code null}.
   * @param diagnostic the diagnostic that explains {@code statusCode} to the client.
   */
  public UaRuntimeException(long statusCode, @Nullable String message, Diagnostic diagnostic) {
    this(new StatusCode(statusCode), message, diagnostic);
  }

  /**
   * Creates a new UaRuntimeException that carries a {@link Diagnostic} explaining its status.
   *
   * <p>The message is for logs and stays on the server. The diagnostic is for the client.
   *
   * @param statusCode the status of the failure.
   * @param message the message for logs, or {@code null}.
   * @param diagnostic the diagnostic that explains {@code statusCode} to the client.
   * @see UaException#UaException(StatusCode, String, Diagnostic)
   */
  public UaRuntimeException(
      StatusCode statusCode, @Nullable String message, Diagnostic diagnostic) {
    super(message);

    this.statusCode = statusCode;
    this.diagnostic = requireNonNull(diagnostic, "diagnostic");
  }

  @Override
  public StatusCode getStatusCode() {
    return statusCode;
  }

  /**
   * Get the {@link Diagnostic} that explains this exception's status.
   *
   * @return the diagnostic, or empty if this exception carries none.
   */
  @Override
  public Optional<Diagnostic> getDiagnostic() {
    return Optional.ofNullable(diagnostic);
  }
}
