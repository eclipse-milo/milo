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
 * Callback invoked when a {@link ReverseConnectAcceptor} cannot complete a discovery-first client
 * flow.
 *
 * <p>The candidate argument is the pending candidate that started the failed flow. The failure may
 * come from discovery, endpoint selection, client configuration, or the later production connection
 * attempt.
 */
@FunctionalInterface
public interface ReverseConnectAcceptorErrorListener {

  /**
   * Observe a failed discovery, endpoint-selection, client-creation, or connection step.
   *
   * @param candidate the candidate being processed when the failure occurred.
   * @param failure the failure.
   */
  void onError(ReverseConnectCandidateSnapshot candidate, Throwable failure);
}
