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
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Selects the endpoint used for the production client after Reverse Connect discovery.
 *
 * <p>Selectors receive both the pre-SecureChannel {@code ReverseHello} snapshot and the endpoints
 * returned by {@code GetEndpoints}. This keeps routing hints and endpoint selection together while
 * preserving the security boundary: the returned endpoint is still validated by the normal client
 * SecureChannel and Session path when the production client connects.
 */
@FunctionalInterface
public interface ReverseConnectEndpointSelector {

  /**
   * Select an endpoint from the discovery response.
   *
   * @param candidate the discovery connection's claim-time snapshot.
   * @param endpoints the endpoints returned by {@code GetEndpoints}.
   * @return the selected endpoint, or empty if this selector cannot choose one.
   */
  Optional<EndpointDescription> select(
      ReverseConnectCandidateSnapshot candidate, List<EndpointDescription> endpoints);

  /**
   * Select an endpoint from a discovery result.
   *
   * @param discovery the discovery result.
   * @return the selected endpoint, or empty if this selector cannot choose one.
   */
  default Optional<EndpointDescription> select(ReverseConnectDiscoveryResult discovery) {
    requireNonNull(discovery, "discovery");

    return select(discovery.candidate(), discovery.endpoints());
  }

  /**
   * Use {@code fallback} when this selector does not choose an endpoint.
   *
   * @param fallback the selector to evaluate second.
   * @return a selector that prefers this selector and falls back to {@code fallback}.
   */
  default ReverseConnectEndpointSelector orElse(ReverseConnectEndpointSelector fallback) {
    requireNonNull(fallback, "fallback");

    return (candidate, endpoints) ->
        select(candidate, endpoints).or(() -> fallback.select(candidate, endpoints));
  }
}
