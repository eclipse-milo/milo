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

/**
 * Synchronous pre-SecureChannel admission hook for decoded {@code ReverseHello} messages.
 *
 * <p>Acceptance by this verifier is not authentication. It only decides whether a server-opened
 * socket should be allowed to proceed to selector matching before normal SecureChannel and Session
 * validation can authenticate the peer.
 */
@FunctionalInterface
public interface ReverseHelloVerifier {

  /**
   * Verify a decoded reverse-connect candidate.
   *
   * @param candidate the candidate containing {@code ServerUri}, {@code EndpointUrl}, and socket
   *     metadata.
   * @return the admission decision.
   */
  ReverseConnectVerificationResult verify(ReverseConnectCandidateSnapshot candidate);

  /**
   * Allow every decoded {@code ReverseHello}.
   *
   * @return a verifier that accepts every candidate.
   */
  static ReverseHelloVerifier acceptAll() {
    return candidate -> ReverseConnectVerificationResult.accept();
  }
}
