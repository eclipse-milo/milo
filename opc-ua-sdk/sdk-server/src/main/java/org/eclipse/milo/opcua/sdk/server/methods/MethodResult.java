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

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The outcome of a Method call, returned by {@link
 * AbstractMethodInvocationHandler#invokeResult(AbstractMethodInvocationHandler.InvocationContext,
 * Variant[])}.
 *
 * <p>The factories follow the Call result rules of OPC 10000-4, 5.12.2.2: a Good or Uncertain
 * result carries output values, and a Bad result carries none. To reject individual input
 * arguments, throw an {@link InvalidArgumentException} instead.
 *
 * <pre>{@code
 * if (setpoint > limit) {
 *   Diagnostic diagnostic = Diagnostic.text("Setpoint clamped to " + limit);
 *
 *   return MethodResult.uncertain(
 *           new StatusCode(StatusCodes.Uncertain_EngineeringUnitsExceeded), new Variant(limit))
 *       .withDiagnostic(diagnostic);
 * }
 *
 * return MethodResult.good(new Variant(setpoint));
 * }</pre>
 *
 * <p>A {@link #withDiagnostic(Diagnostic) diagnostic} explains the result's status. The Call
 * service returns it when the client asked for operation-level diagnostics and the server's
 * diagnostics policy allows it.
 */
@NullMarked
public final class MethodResult {

  private final StatusCode statusCode;
  private final Variant @Nullable [] outputArguments;
  private final @Nullable Diagnostic diagnostic;

  private MethodResult(
      StatusCode statusCode,
      Variant @Nullable [] outputArguments,
      @Nullable Diagnostic diagnostic) {

    this.statusCode = statusCode;
    this.outputArguments = outputArguments;
    this.diagnostic = diagnostic;
  }

  /**
   * Create a Good result.
   *
   * @param outputArguments the values for the Method's output arguments, if any.
   * @return a Good {@link MethodResult}.
   */
  public static MethodResult good(Variant... outputArguments) {
    return new MethodResult(StatusCode.GOOD, copy(outputArguments), null);
  }

  /**
   * Create a Good result with a Good subcode, such as {@code Good_Clamped}.
   *
   * @param statusCode a Good status.
   * @param outputArguments the values for the Method's output arguments, if any.
   * @return a Good {@link MethodResult}.
   * @throws IllegalArgumentException if {@code statusCode} is not Good.
   */
  public static MethodResult good(StatusCode statusCode, Variant... outputArguments) {
    requireSeverity(statusCode, statusCode.isGood(), "Good");

    return new MethodResult(statusCode, copy(outputArguments), null);
  }

  /**
   * Create an Uncertain result, which carries output values whose quality is uncertain.
   *
   * @param statusCode an Uncertain status.
   * @param outputArguments the values for the Method's output arguments, if any.
   * @return an Uncertain {@link MethodResult}.
   * @throws IllegalArgumentException if {@code statusCode} is not Uncertain.
   */
  public static MethodResult uncertain(StatusCode statusCode, Variant... outputArguments) {
    requireSeverity(statusCode, statusCode.isUncertain(), "Uncertain");

    return new MethodResult(statusCode, copy(outputArguments), null);
  }

  /**
   * Create a Bad result, which carries no output values.
   *
   * @param statusCode a Bad status.
   * @return a Bad {@link MethodResult}.
   * @throws IllegalArgumentException if {@code statusCode} is not Bad.
   */
  public static MethodResult bad(StatusCode statusCode) {
    requireSeverity(statusCode, statusCode.isBad(), "Bad");

    return new MethodResult(statusCode, new Variant[0], null);
  }

  /**
   * Return a copy of this result with a diagnostic that explains its status.
   *
   * @param diagnostic the diagnostic that explains the status.
   * @return a new {@link MethodResult}.
   */
  public MethodResult withDiagnostic(Diagnostic diagnostic) {
    return new MethodResult(statusCode, outputArguments, requireNonNull(diagnostic, "diagnostic"));
  }

  /**
   * Get the status of the Method call.
   *
   * @return the status of the Method call.
   */
  public StatusCode getStatusCode() {
    return statusCode;
  }

  /**
   * Get the values for the Method's output arguments.
   *
   * @return a copy of the output values; empty for a Bad result.
   */
  public Variant[] getOutputArguments() {
    return outputArguments != null ? outputArguments.clone() : new Variant[0];
  }

  /**
   * Get the diagnostic that explains this result's status.
   *
   * @return the diagnostic, or empty if there is none.
   */
  public Optional<Diagnostic> getDiagnostic() {
    return Optional.ofNullable(diagnostic);
  }

  /**
   * A copy of the output values for one Call result; {@code null} when an {@code invoke} override
   * returned null, which the Call service has always sent as a null array.
   */
  Variant @Nullable [] outputArgumentsForResponse() {
    return outputArguments != null ? outputArguments.clone() : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MethodResult that)) {
      return false;
    }
    return statusCode.equals(that.statusCode)
        && Arrays.equals(outputArguments, that.outputArguments)
        && Objects.equals(diagnostic, that.diagnostic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, Arrays.hashCode(outputArguments), diagnostic);
  }

  @Override
  public String toString() {
    return "MethodResult{statusCode="
        + statusCode
        + ", outputArguments="
        + Arrays.toString(outputArguments)
        + ", diagnostic="
        + diagnostic
        + "}";
  }

  private static Variant @Nullable [] copy(Variant @Nullable [] outputArguments) {
    return outputArguments != null ? outputArguments.clone() : null;
  }

  private static void requireSeverity(StatusCode statusCode, boolean matches, String severity) {
    requireNonNull(statusCode, "statusCode");

    if (!matches) {
      throw new IllegalArgumentException("expected a " + severity + " status: " + statusCode);
    }
  }
}
