/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * Pure endpoint filtering/ranking for Table 40 resolution.
 *
 * <p>The primary key of the lookup is the SKS ApplicationUri; only {@code SignAndEncrypt} endpoints
 * are eligible. When the config entry constrains the SecurityPolicyUri the match is exact;
 * otherwise candidates are ranked by the <b>discovered</b> endpoints' {@code securityLevel} ("best
 * available") — the entry's own securityLevel is normatively 0 and never consulted.
 */
final class SksEndpointSelector {

  private SksEndpointSelector() {}

  /**
   * Select the endpoint to connect to from a discovered endpoint list.
   *
   * @param endpoints the endpoints returned by GetEndpoints.
   * @param applicationUri the SKS ApplicationUri from the config entry.
   * @param securityPolicyUri the config entry's SecurityPolicyUri constraint; null or empty means
   *     "best available".
   * @return the selected endpoint, or empty if no endpoint matches.
   */
  static Optional<EndpointDescription> selectEndpoint(
      List<EndpointDescription> endpoints,
      String applicationUri,
      @Nullable String securityPolicyUri) {

    Stream<EndpointDescription> candidates =
        endpoints.stream()
            .filter(e -> e.getServer() != null)
            .filter(e -> applicationUri.equals(e.getServer().getApplicationUri()))
            .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt);

    if (securityPolicyUri != null && !securityPolicyUri.isEmpty()) {
      return candidates.filter(e -> securityPolicyUri.equals(e.getSecurityPolicyUri())).findFirst();
    } else {
      return candidates.max(
          Comparator.comparingInt(
              e -> e.getSecurityLevel() != null ? e.getSecurityLevel().intValue() : 0));
    }
  }

  /**
   * The discovery URLs to run GetEndpoints at for a config entry.
   *
   * <p>Spec path: the non-empty elements of {@code server.discoveryUrls}. As a tolerance fallback,
   * when that list is empty, a filled {@code endpointUrl} is used as the discovery target.
   *
   * @param entry the SecurityKeyServices entry.
   * @return the discovery targets; {@code urls} is empty if the entry provides none.
   */
  static DiscoveryTargets discoveryTargets(EndpointDescription entry) {
    var urls = new ArrayList<String>();

    ApplicationDescription server = entry.getServer();
    String[] discoveryUrls = server != null ? server.getDiscoveryUrls() : null;
    if (discoveryUrls != null) {
      for (String url : discoveryUrls) {
        if (url != null && !url.isEmpty()) {
          urls.add(url);
        }
      }
    }
    if (!urls.isEmpty()) {
      return new DiscoveryTargets(List.copyOf(urls), false);
    }

    String endpointUrl = entry.getEndpointUrl();
    if (endpointUrl != null && !endpointUrl.isEmpty()) {
      return new DiscoveryTargets(List.of(endpointUrl), true);
    }

    return new DiscoveryTargets(List.of(), false);
  }

  /**
   * Discovery targets derived from a config entry.
   *
   * @param urls the discovery URLs, in order.
   * @param fromEndpointUrlFallback true if {@code urls} came from the non-conformant {@code
   *     endpointUrl} tolerance fallback rather than {@code server.discoveryUrls}.
   */
  record DiscoveryTargets(List<String> urls, boolean fromEndpointUrlFallback) {}
}
