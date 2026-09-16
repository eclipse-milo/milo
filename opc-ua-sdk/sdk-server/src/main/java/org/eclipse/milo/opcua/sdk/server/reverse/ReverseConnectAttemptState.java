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

/**
 * Lifecycle state for one SDK-managed server reverse-connect attempt.
 *
 * <p>The SDK maps the low-level transport state stream directly: {@link #CONNECTING}, {@link
 * #CONNECTED}, {@link #REVERSE_HELLO_SENT}, and {@link #HELLO_HANDLER_INSTALLED} are non-terminal.
 * {@link #HANDOFF}, {@link #CLIENT_ERROR}, {@link #FAILED}, {@link #CANCELLED}, and {@link #CLOSED}
 * are terminal for the attempt. After handoff, the target manager tracks the reverse-opened channel
 * separately from the completed attempt.
 */
public enum ReverseConnectAttemptState {

  /** The server has started an outbound TCP connect to the client listener. */
  CONNECTING,

  /** The outbound TCP socket connected successfully. */
  CONNECTED,

  /** {@code ReverseHello} was written and flushed to the client listener. */
  REVERSE_HELLO_SENT,

  /** The normal server-side Hello handler has been installed on the active channel. */
  HELLO_HANDLER_INSTALLED,

  /** The client sent {@code Hello} and the channel has entered the normal server UASC path. */
  HANDOFF,

  /** The client responded with {@code ErrorMessage} instead of {@code Hello}. */
  CLIENT_ERROR,

  /** The attempt failed before the channel entered the normal server UASC path. */
  FAILED,

  /** The attempt was cancelled by server target lifecycle control. */
  CANCELLED,

  /** The attempt or reverse-opened channel was closed. */
  CLOSED
}
