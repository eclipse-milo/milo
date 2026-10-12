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

import static java.util.Objects.requireNonNull;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * Endpoint discovery data returned from a consumed Reverse Connect discovery connection.
 *
 * <p>The {@link #candidate()} snapshot identifies the first reverse connection that was claimed for
 * {@code GetEndpoints}. That connection has already been consumed by discovery and is not the
 * production Session channel. Use the snapshot only as routing context when choosing an endpoint
 * and registering the later production reverse client.
 *
 * @param candidate the claim-time reverse-connect candidate snapshot.
 * @param endpoints the endpoint descriptions returned by {@code GetEndpoints}.
 */
public record ReverseConnectDiscoveryResult(
    ReverseConnectCandidateSnapshot candidate, List<EndpointDescription> endpoints) {

  /**
   * Create an immutable discovery result.
   *
   * <p>The endpoint list is defensively copied so callers can safely retain the result while they
   * build a production client configuration.
   *
   * @param candidate the claim-time reverse-connect candidate snapshot.
   * @param endpoints the endpoint descriptions returned by {@code GetEndpoints}.
   */
  public ReverseConnectDiscoveryResult {
    requireNonNull(candidate, "candidate");
    endpoints = List.copyOf(requireNonNull(endpoints, "endpoints"));
  }

  /**
   * Return the {@code ServerUri} from the discovery {@code ReverseHello}.
   *
   * @return the server URI routing hint, or {@code null} if the candidate did not carry one.
   */
  public @Nullable String serverUri() {
    return candidate.serverUri();
  }

  /**
   * Return the {@code EndpointUrl} from the discovery {@code ReverseHello}.
   *
   * @return the endpoint URL routing hint, or {@code null} if the candidate did not carry one.
   */
  public @Nullable String endpointUrl() {
    return candidate.endpointUrl();
  }
}
