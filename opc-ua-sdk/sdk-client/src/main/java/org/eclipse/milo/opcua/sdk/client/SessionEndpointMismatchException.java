/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Thrown during {@code CreateSession} when session endpoint validation is enabled and the endpoints
 * returned in {@code CreateSessionResponse} do not match the discovery endpoints for the current
 * transport profile.
 *
 * <p>This exception carries {@link StatusCodes#Bad_SecurityChecksFailed} and distinguishes an
 * endpoint list mismatch from other {@code CreateSession} security failures that share the same
 * status code. Blocking client methods such as {@link OpcUaClient#connect()} and {@link
 * OpcUaClient#getSession()} wrap failures in a plain {@link UaException}, so callers can detect
 * this failure uniformly across blocking, async, and {@link SessionActivityListener} callbacks with
 * {@link UaException#extract(Throwable, Class)}:
 *
 * <pre>{@code
 * try {
 *   client.connect();
 * } catch (UaException e) {
 *   if (UaException.extract(e, SessionEndpointMismatchException.class).isEmpty()) {
 *     throw e;
 *   }
 *   // Rediscover endpoints and reconnect with the updated configuration.
 * }
 * }</pre>
 *
 * @see OpcUaClientConfig#isSessionEndpointValidationEnabled()
 */
public final class SessionEndpointMismatchException extends UaException {

  private static final String DEFAULT_MESSAGE =
      "endpoints returned during discovery do not match session endpoints";

  /** Creates a session endpoint mismatch failure with the default message. */
  public SessionEndpointMismatchException() {
    this(DEFAULT_MESSAGE);
  }

  /**
   * Creates a session endpoint mismatch failure with the given message.
   *
   * @param message the detail message describing the mismatch.
   */
  public SessionEndpointMismatchException(String message) {
    super(StatusCodes.Bad_SecurityChecksFailed, message);
  }
}
