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

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * Per-call options for a Method call: the ReturnDiagnostics mask and the request timeout.
 *
 * <p>An unset timeout falls back to the client's configured request timeout. An unset
 * ReturnDiagnostics mask requests no diagnostics.
 *
 * <pre>{@code
 * MethodCallOptions options =
 *     MethodCallOptions.builder()
 *         .returnDiagnostics(ReturnDiagnostics.of(ReturnDiagnostics.OPERATION_LEVEL_ALL))
 *         .timeout(Duration.ofSeconds(30))
 *         .build();
 * }</pre>
 *
 * Instances are immutable.
 */
public final class MethodCallOptions {

  /** Options that request no diagnostics and use the client's configured request timeout. */
  public static final MethodCallOptions DEFAULT = new MethodCallOptions(null, null);

  /** The longest timeout a RequestHeader can carry. */
  private static final Duration MAX_TIMEOUT = Duration.ofMillis(UInteger.MAX_VALUE);

  private final @Nullable ReturnDiagnostics returnDiagnostics;
  private final @Nullable Duration timeout;

  private MethodCallOptions(
      @Nullable ReturnDiagnostics returnDiagnostics, @Nullable Duration timeout) {
    this.returnDiagnostics = returnDiagnostics;
    this.timeout = timeout;
  }

  /**
   * The ReturnDiagnostics mask to send in the request header.
   *
   * @return the mask, or empty to request no diagnostics.
   */
  public Optional<ReturnDiagnostics> returnDiagnostics() {
    return Optional.ofNullable(returnDiagnostics);
  }

  /**
   * The timeout hint to send in the request header.
   *
   * @return the timeout, or empty to use the client's configured request timeout.
   */
  public Optional<Duration> timeout() {
    return Optional.ofNullable(timeout);
  }

  /**
   * Create a new builder with nothing set.
   *
   * @return a new builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public boolean equals(@Nullable Object o) {
    return o instanceof MethodCallOptions other
        && Objects.equals(returnDiagnostics, other.returnDiagnostics)
        && Objects.equals(timeout, other.timeout);
  }

  @Override
  public int hashCode() {
    return Objects.hash(returnDiagnostics, timeout);
  }

  @Override
  public String toString() {
    return "MethodCallOptions{returnDiagnostics="
        + returnDiagnostics
        + ", timeout="
        + timeout
        + "}";
  }

  /** Builds {@link MethodCallOptions}. */
  public static final class Builder {
    private @Nullable ReturnDiagnostics returnDiagnostics;
    private @Nullable Duration timeout;

    private Builder() {}

    /**
     * Request diagnostics for the call.
     *
     * @param returnDiagnostics the mask to send; {@code null} requests no diagnostics.
     * @return this builder.
     */
    public Builder returnDiagnostics(@Nullable ReturnDiagnostics returnDiagnostics) {
      this.returnDiagnostics = returnDiagnostics;
      return this;
    }

    /**
     * Set the request timeout hint.
     *
     * <p>The hint is sent in whole milliseconds. A zero timeout is sent as a TimeoutHint of 0,
     * which disables the client-side timeout for the request.
     *
     * @param timeout the timeout; {@code null} restores the client's configured request timeout.
     * @return this builder.
     * @throws IllegalArgumentException if {@code timeout} is negative or longer than a UInt32
     *     millisecond count can represent.
     */
    public Builder timeout(@Nullable Duration timeout) {
      if (timeout != null && (timeout.isNegative() || timeout.compareTo(MAX_TIMEOUT) > 0)) {
        throw new IllegalArgumentException(
            "timeout must be between 0 and " + MAX_TIMEOUT + ": " + timeout);
      }
      this.timeout = timeout;
      return this;
    }

    /**
     * Build the options.
     *
     * @return the options.
     */
    public MethodCallOptions build() {
      return new MethodCallOptions(returnDiagnostics, timeout);
    }
  }
}
