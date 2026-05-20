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

/** Reason a reverse-connect candidate left the matchable lifecycle without being claimed. */
public enum ReverseConnectRejectionReason {
  /** No valid first message arrived before the first-message deadline. */
  FIRST_MESSAGE_TIMEOUT,

  /** The first message was malformed or was not an {@code RHE/F} ReverseHello message. */
  MALFORMED_REVERSE_HELLO,

  /** The configured {@link ReverseHelloVerifier} rejected the decoded {@code ReverseHello}. */
  VERIFIER_REJECTED,

  /** The manager was already holding the maximum number of pending candidates. */
  PENDING_LIMIT_EXCEEDED,

  /** The pending hold time elapsed before any selector claimed the candidate. */
  PENDING_EXPIRED,

  /** The manager shut down before the candidate was claimed. */
  MANAGER_STOPPED,

  /** The peer closed the channel before the candidate was claimed. */
  CHANNEL_CLOSED,

  /** A selector threw while evaluating the candidate. */
  SELECTOR_ERROR
}
