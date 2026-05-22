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
 *
 * <p>Verifier acceptance does not guarantee that a matching {@code pending} listener event will
 * fire. The verifier runs after the manager lock is released, so a peer that closes the channel
 * before the manager re-acquires the lock will be rejected by the channel-close path (emitting a
 * {@code rejected} event with reason {@code CHANNEL_CLOSED}) even though the verifier already
 * accepted. Verifiers with observable side effects (logging, metrics, rate limiting) should not
 * assume a successful return implies the candidate later transitioned to {@code PENDING}.
 *
 * <p>A verifier is useful for cheap admission checks before a candidate consumes pending capacity:
 *
 * <pre>{@code
 * ReverseHelloVerifier verifier =
 *     candidate -> {
 *       if (!expectedServerUri.equals(candidate.serverUri())) {
 *         return ReverseConnectVerificationResult.reject("unexpected ServerUri");
 *       }
 *       if (!expectedEndpointUrl.equals(candidate.endpointUrl())) {
 *         return ReverseConnectVerificationResult.reject("unexpected EndpointUrl");
 *       }
 *       return ReverseConnectVerificationResult.accept();
 *     };
 *
 * ReverseConnectManager manager =
 *     ReverseConnectManager.builder()
 *         .addBindAddress(new InetSocketAddress("0.0.0.0", 48060))
 *         .setReverseHelloVerifier(verifier)
 *         .build();
 * }</pre>
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
