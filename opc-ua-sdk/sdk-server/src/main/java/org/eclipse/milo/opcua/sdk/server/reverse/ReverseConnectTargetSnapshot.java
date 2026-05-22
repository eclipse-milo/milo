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

import java.time.Instant;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * Immutable runtime snapshot for a server-side Reverse Connect target.
 *
 * <p>The snapshot is safe to expose through listeners and management APIs. If a failed attempt
 * supplied a {@link Throwable}, the snapshot stores a defensive copy and returns a fresh copy from
 * {@link #lastError()} so callers cannot mutate the manager's retained diagnostic.
 *
 * @param targetId the stable target id.
 * @param clientListenerUrl the client reverse-listener URL.
 * @param endpointUrl the server endpoint URL advertised in {@code ReverseHello}.
 * @param registrationPeriod the target registration period.
 * @param connectTimeout the TCP connect timeout.
 * @param enabled whether the target is enabled.
 * @param paused whether the target is currently paused.
 * @param nextAttemptTime the next scheduled attempt time, when one is scheduled.
 * @param lastAttemptTime the last time an attempt started.
 * @param lastSuccessTime the last time an attempt handed a channel to the server path.
 * @param activeChannelCount the number of active reverse-opened channels for this target.
 * @param lastStatusCode the last failed attempt status code, when available.
 * @param lastError the last failed attempt exception to retain defensively, when available.
 */
public record ReverseConnectTargetSnapshot(
    UUID targetId,
    String clientListenerUrl,
    String endpointUrl,
    UInteger registrationPeriod,
    UInteger connectTimeout,
    boolean enabled,
    boolean paused,
    @Nullable Instant nextAttemptTime,
    @Nullable Instant lastAttemptTime,
    @Nullable Instant lastSuccessTime,
    int activeChannelCount,
    @Nullable StatusCode lastStatusCode,
    @Nullable Throwable lastError) {

  public ReverseConnectTargetSnapshot {
    lastError = copyThrowable(lastError);
  }

  /**
   * Get a defensive copy of the last failed attempt exception.
   *
   * <p>The copy preserves the original exception class name for {@link Throwable#toString()}, the
   * message, stack trace, cause chain, and suppressed exceptions, but it is not the same object or
   * necessarily the same concrete exception type as the source throwable.
   *
   * @return the copied last error, or {@code null} if no attempt error is retained.
   */
  @Override
  public @Nullable Throwable lastError() {
    return copyThrowable(lastError);
  }

  static @Nullable Throwable copyThrowable(@Nullable Throwable throwable) {
    return throwable != null ? copyThrowable(throwable, new IdentityHashMap<>()) : null;
  }

  private static Throwable copyThrowable(
      Throwable throwable, Map<Throwable, Throwable> copiedThrowables) {

    Throwable existing = copiedThrowables.get(throwable);
    if (existing != null) {
      return existing;
    }

    Throwable copy = new LastErrorCopy(originalClassName(throwable), throwable.getMessage());
    copiedThrowables.put(throwable, copy);

    Throwable cause = throwable.getCause();
    if (cause != null) {
      Throwable copiedCause = copyThrowable(cause, copiedThrowables);
      if (copiedCause != copy) {
        copy.initCause(copiedCause);
      }
    }

    for (Throwable suppressed : throwable.getSuppressed()) {
      Throwable copiedSuppressed = copyThrowable(suppressed, copiedThrowables);
      if (copiedSuppressed != copy) {
        copy.addSuppressed(copiedSuppressed);
      }
    }

    copy.setStackTrace(throwable.getStackTrace());

    return copy;
  }

  private static String originalClassName(Throwable throwable) {
    return throwable instanceof LastErrorCopy copy
        ? copy.originalClassName
        : throwable.getClass().getName();
  }

  private static final class LastErrorCopy extends Throwable {

    private final String originalClassName;

    private LastErrorCopy(String originalClassName, @Nullable String message) {
      super(message);
      this.originalClassName = originalClassName;
    }

    @Override
    public String toString() {
      String message = getLocalizedMessage();

      return message != null ? originalClassName + ": " + message : originalClassName;
    }
  }
}
