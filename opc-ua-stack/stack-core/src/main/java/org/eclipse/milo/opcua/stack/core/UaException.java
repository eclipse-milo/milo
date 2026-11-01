/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

public class UaException extends Exception implements UaExceptionStatus {

  private final StatusCode statusCode;

  private final @Nullable Diagnostic diagnostic;

  public UaException(long statusCode) {
    this.statusCode = new StatusCode(statusCode);
    this.diagnostic = null;
  }

  public UaException(StatusCode statusCode) {
    this.statusCode = statusCode;
    this.diagnostic = null;
  }

  /**
   * Creates a new UaException with a cause.
   *
   * <p>The StatusCode is extracted from the cause or any of its nested causes by looking for an
   * implementation of {@link UaExceptionStatus}, otherwise {@link StatusCodes#Bad_UnexpectedError}
   * is used. The {@link Diagnostic}, if any, is taken from that same exception, so it always
   * explains this exception's status.
   *
   * @param cause the cause of this exception.
   */
  public UaException(Throwable cause) {
    super(cause);

    UaExceptionStatus status = UaExceptionStatus.extract(cause).orElse(null);

    if (status != null) {
      this.statusCode = status.getStatusCode();
      this.diagnostic = status.getDiagnostic().orElse(null);
    } else {
      this.statusCode = new StatusCode(StatusCodes.Bad_UnexpectedError);
      this.diagnostic = null;
    }
  }

  public UaException(long statusCode, Throwable cause) {
    super(cause);

    this.statusCode = new StatusCode(statusCode);
    this.diagnostic = null;
  }

  public UaException(long statusCode, String message) {
    super(message);

    this.statusCode = new StatusCode(statusCode);
    this.diagnostic = null;
  }

  public UaException(long statusCode, String message, Throwable cause) {
    super(message, cause);

    this.statusCode = new StatusCode(statusCode);
    this.diagnostic = null;
  }

  public UaException(StatusCode statusCode, String message) {
    super(message);

    this.statusCode = statusCode;
    this.diagnostic = null;
  }

  /**
   * Creates a new UaException that carries a {@link Diagnostic} explaining its status.
   *
   * <p>The message is for logs and stays on the server. The diagnostic is for the client: when this
   * exception fails an operation or a service, the server returns the diagnostic if the client
   * asked for diagnostics and the server's diagnostics policy allows it.
   *
   * <pre>{@code
   * Diagnostic diagnostic =
   *     Diagnostic.symbolic("urn:acme:plc", "E_RUN_MODE")
   *         .withText("Writes are blocked while the controller is in RUN mode");
   *
   * throw new UaException(StatusCodes.Bad_NotWritable, "write blocked: RUN mode", diagnostic);
   * }</pre>
   *
   * @param statusCode the status of the failure.
   * @param message the message for logs, or {@code null}.
   * @param diagnostic the diagnostic that explains {@code statusCode} to the client.
   */
  public UaException(long statusCode, @Nullable String message, Diagnostic diagnostic) {
    this(new StatusCode(statusCode), message, diagnostic);
  }

  /**
   * Creates a new UaException that carries a {@link Diagnostic} explaining its status.
   *
   * <p>The message is for logs and stays on the server. The diagnostic is for the client: when this
   * exception fails an operation or a service, the server returns the diagnostic if the client
   * asked for diagnostics and the server's diagnostics policy allows it.
   *
   * @param statusCode the status of the failure.
   * @param message the message for logs, or {@code null}.
   * @param diagnostic the diagnostic that explains {@code statusCode} to the client.
   */
  public UaException(StatusCode statusCode, @Nullable String message, Diagnostic diagnostic) {
    this(statusCode, message, requireNonNull(diagnostic, "diagnostic"), null);
  }

  /**
   * Creates a new UaException for a subclass whose diagnostic may be absent.
   *
   * @param statusCode the status of the failure.
   * @param message the message for logs, or {@code null}.
   * @param diagnostic the diagnostic that explains {@code statusCode}, or {@code null}.
   * @param cause the cause of this exception, or {@code null}.
   */
  protected UaException(
      StatusCode statusCode,
      @Nullable String message,
      @Nullable Diagnostic diagnostic,
      @Nullable Throwable cause) {

    super(message);

    if (cause != null) {
      initCause(cause);
    }

    this.statusCode = statusCode;
    this.diagnostic = diagnostic;
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

  @Override
  public String getMessage() {
    String message = super.getMessage();

    if (message == null || message.isEmpty()) {
      Optional<String[]> lookup = StatusCodes.lookup(statusCode.value());

      String status = lookup.map(nd -> nd[0]).orElse(statusCode.toString());
      String description = lookup.map(nd -> nd[1]).orElse("");

      if (description.isEmpty()) {
        message = String.format("status=%s", status);
      } else {
        message = String.format("status=%s, description=%s", status, description);
      }
    }

    return message;
  }

  @Override
  public String toString() {
    String clazzName = getClass().getSimpleName();

    Optional<String[]> lookup = StatusCodes.lookup(statusCode.value());

    String status = lookup.map(nd -> nd[0]).orElse(statusCode.toString());

    String message = super.getMessage();
    if (message == null) {
      message = lookup.map(nd -> nd[1]).orElse("");
    }

    if (message.isEmpty()) {
      return String.format("%s: status=%s", clazzName, status);
    } else {
      return String.format("%s: status=%s, message=%s", clazzName, status, message);
    }
  }

  /**
   * If {@code ex} is a {@link UaException}, or if a {@link UaException} can be found by walking up
   * the exception cause chain, return it.
   *
   * @param ex the {@link Throwable} to extract from.
   * @return a {@link UaException} if one was present in the exception chain.
   */
  public static Optional<UaException> extract(Throwable ex) {
    if (ex instanceof UaException uaException) {
      return Optional.of(uaException);
    } else {
      Throwable cause = ex.getCause();
      return cause != null ? extract(cause) : Optional.empty();
    }
  }

  /**
   * Find the {@link Diagnostic} that explains the status {@link #extractStatusCode(Throwable)}
   * finds: the diagnostic of {@code ex}, or of the nearest {@link UaExceptionStatus} in its cause
   * chain.
   *
   * @param ex the {@link Throwable} to extract from.
   * @return the diagnostic, if that exception carries one.
   */
  public static Optional<Diagnostic> extractDiagnostic(Throwable ex) {
    return UaExceptionStatus.extract(ex).flatMap(UaExceptionStatus::getDiagnostic);
  }

  public static Optional<StatusCode> extractStatusCode(Throwable ex) {
    if (ex instanceof UaExceptionStatus uaExceptionStatus) {
      return Optional.of(uaExceptionStatus.getStatusCode());
    } else {
      Throwable cause = ex.getCause();
      return cause != null ? extractStatusCode(cause) : Optional.empty();
    }
  }
}
