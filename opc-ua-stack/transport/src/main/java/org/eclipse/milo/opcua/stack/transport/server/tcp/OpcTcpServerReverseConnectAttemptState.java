/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

/**
 * Lifecycle state for one server-initiated reverse-connect attempt.
 *
 * <p>A successful attempt progresses through {@link #CONNECTING}, {@link #CONNECTED}, {@link
 * #REVERSE_HELLO_SENT}, {@link #HELLO_HANDLER_INSTALLED}, and terminal {@link #HANDOFF}. Terminal
 * non-handoff states are {@link #CLIENT_ERROR}, {@link #FAILED}, {@link #CANCELLED}, and {@link
 * #CLOSED}; once any terminal state is recorded, later asynchronous callbacks cannot move the
 * attempt back to a non-terminal state.
 */
public enum OpcTcpServerReverseConnectAttemptState {

  /** The connector has started an outbound TCP connect to the client listener. */
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

  /** The attempt was cancelled by its caller before handoff. */
  CANCELLED,

  /** The attempt or connector was closed before handoff. */
  CLOSED
}
