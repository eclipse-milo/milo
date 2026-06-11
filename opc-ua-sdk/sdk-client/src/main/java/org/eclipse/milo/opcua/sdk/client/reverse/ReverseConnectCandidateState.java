/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

/** Lifecycle state for a server-opened reverse-connect socket observed by the client manager. */
public enum ReverseConnectCandidateState {
  /** The TCP socket is accepted and the manager is waiting for the first {@code ReverseHello}. */
  WAITING_FOR_REVERSE_HELLO,

  /** {@code ReverseHello} was decoded and verified, but no selector has claimed the socket yet. */
  PENDING,

  /** A selector claimed the socket and ownership has been transferred to the caller. */
  CLAIMED,

  /** The manager rejected the socket before it could be claimed. */
  REJECTED,

  /** The candidate stayed pending until its configured hold time elapsed. */
  EXPIRED,

  /** The peer closed the socket before the manager could finish matching it. */
  CLOSED
}
