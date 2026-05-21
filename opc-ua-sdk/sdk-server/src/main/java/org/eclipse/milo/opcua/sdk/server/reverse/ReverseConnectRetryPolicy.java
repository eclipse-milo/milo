/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * Calculates the delay before a server reverse-connect target retries after an unsuccessful
 * attempt.
 *
 * <p>Policies are called after terminal attempt events and return the delay before the next
 * scheduled attempt. Implementations should be deterministic from the immutable target
 * configuration and the attempt event; scheduling and lifecycle ownership remain with {@link
 * ReverseConnectTargetManager}.
 */
@FunctionalInterface
public interface ReverseConnectRetryPolicy {

  /**
   * Get the retry delay in milliseconds.
   *
   * @param target the immutable target configuration.
   * @param event the terminal attempt event that triggered the retry.
   * @return the retry delay in milliseconds.
   */
  long getRetryDelayMillis(ReverseConnectTarget target, ReverseConnectAttemptEvent event);

  /**
   * Retry using the target's registration period.
   *
   * @return a retry policy using the target registration period.
   */
  static ReverseConnectRetryPolicy registrationPeriod() {
    return RegistrationPeriodRetryPolicy.INSTANCE;
  }

  /**
   * Retry using a fixed delay.
   *
   * @param delay the retry delay.
   * @return a retry policy using {@code delay}.
   * @throws IllegalArgumentException if {@code delay} is zero.
   */
  static ReverseConnectRetryPolicy fixedDelay(UInteger delay) {
    return new FixedDelayRetryPolicy(delay);
  }

  final class RegistrationPeriodRetryPolicy implements ReverseConnectRetryPolicy {

    private static final RegistrationPeriodRetryPolicy INSTANCE =
        new RegistrationPeriodRetryPolicy();

    private RegistrationPeriodRetryPolicy() {}

    @Override
    public long getRetryDelayMillis(ReverseConnectTarget target, ReverseConnectAttemptEvent event) {

      return target.getRegistrationPeriod().longValue();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      return obj instanceof RegistrationPeriodRetryPolicy;
    }

    @Override
    public int hashCode() {
      return RegistrationPeriodRetryPolicy.class.hashCode();
    }

    @Override
    public String toString() {
      return "ReverseConnectRetryPolicy.registrationPeriod()";
    }
  }

  record FixedDelayRetryPolicy(UInteger delay) implements ReverseConnectRetryPolicy {

    public FixedDelayRetryPolicy {
      if (delay.longValue() <= 0) {
        throw new IllegalArgumentException("delay must be greater than 0");
      }
    }

    @Override
    public long getRetryDelayMillis(ReverseConnectTarget target, ReverseConnectAttemptEvent event) {

      return delay.longValue();
    }
  }
}
