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

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Admission result returned by a {@link ReverseHelloVerifier}.
 *
 * <p>Accepted results allow a decoded candidate to proceed to selector matching. Rejected results
 * carry the manager-visible reason, the TCP status code to report to the peer when possible, and a
 * diagnostic string for snapshots and listener callbacks.
 */
public final class ReverseConnectVerificationResult {

  private static final ReverseConnectVerificationResult ACCEPTED =
      new ReverseConnectVerificationResult(true, null, null, null);

  private final boolean accepted;
  private final @Nullable ReverseConnectRejectionReason rejectionReason;
  private final @Nullable StatusCode statusCode;
  private final @Nullable String diagnostic;

  private ReverseConnectVerificationResult(
      boolean accepted,
      @Nullable ReverseConnectRejectionReason rejectionReason,
      @Nullable StatusCode statusCode,
      @Nullable String diagnostic) {

    this.accepted = accepted;
    this.rejectionReason = rejectionReason;
    this.statusCode = statusCode;
    this.diagnostic = diagnostic;
  }

  /**
   * Accept the candidate for selector matching.
   *
   * @return an accepted result.
   */
  public static ReverseConnectVerificationResult accept() {
    return ACCEPTED;
  }

  /**
   * Reject the candidate with the default TCP endpoint URL status.
   *
   * @param diagnostic a human-readable reason.
   * @return a rejected result.
   */
  public static ReverseConnectVerificationResult reject(String diagnostic) {
    return reject(
        ReverseConnectRejectionReason.VERIFIER_REJECTED,
        new StatusCode(StatusCodes.Bad_TcpEndpointUrlInvalid),
        diagnostic);
  }

  /**
   * Reject the candidate with an explicit reason and status code.
   *
   * @param rejectionReason the observable rejection reason.
   * @param statusCode the TCP error status to report to the peer.
   * @param diagnostic a human-readable reason.
   * @return a rejected result.
   */
  public static ReverseConnectVerificationResult reject(
      ReverseConnectRejectionReason rejectionReason, StatusCode statusCode, String diagnostic) {

    return new ReverseConnectVerificationResult(false, rejectionReason, statusCode, diagnostic);
  }

  public boolean isAccepted() {
    return accepted;
  }

  /**
   * Get the manager-visible rejection reason.
   *
   * @return the rejection reason, or {@code null} when {@link #isAccepted()} is {@code true}.
   */
  public @Nullable ReverseConnectRejectionReason getRejectionReason() {
    return rejectionReason;
  }

  /**
   * Get the TCP error status associated with a rejected candidate.
   *
   * @return the status code to send to the peer, or {@code null} when {@link #isAccepted()} is
   *     {@code true}.
   */
  public @Nullable StatusCode getStatusCode() {
    return statusCode;
  }

  /**
   * Get the human-readable verifier diagnostic.
   *
   * @return the diagnostic, or {@code null} when {@link #isAccepted()} is {@code true}.
   */
  public @Nullable String getDiagnostic() {
    return diagnostic;
  }
}
