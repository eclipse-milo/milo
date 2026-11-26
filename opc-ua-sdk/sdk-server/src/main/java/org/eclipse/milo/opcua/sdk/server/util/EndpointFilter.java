/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Filters a server's advertised {@link EndpointDescription}s and discovery URLs by the host in a
 * client request URL, with a deterministic per-transport-profile fallback.
 *
 * <p>OPC UA Part 4 (§5.5.2.2, §5.5.4.2, §5.7.2.2) specifies that when a server does not recognize
 * the HostName in a request's {@code endpointUrl}, it should return a suitable default URL. Sharing
 * one filter across {@code GetEndpoints}, {@code FindServers}, and {@code CreateSession} ensures
 * discovery and session creation return matching endpoint lists for the negotiated transport
 * profile, as required by Part 4 §5.5.1.
 */
@NullMarked
public final class EndpointFilter {

  private EndpointFilter() {}

  /**
   * Return whether {@code endpoint} is a dedicated discovery endpoint whose normalized path ends
   * with {@code "/discovery"}.
   *
   * <p>Uses {@link EndpointUtil#getPath(String)} so trailing slashes (for example {@code
   * "/app/discovery/"}) are normalized the same way {@code OpcUaServer} registers discovery-only
   * service paths.
   *
   * @param endpoint the {@link EndpointDescription} to inspect.
   * @return {@code true} if {@code endpoint}'s URL is non-null and its normalized path ends with
   *     {@code "/discovery"}.
   */
  public static boolean isDiscoveryEndpoint(EndpointDescription endpoint) {
    String endpointUrl = endpoint.getEndpointUrl();
    return endpointUrl != null && EndpointUtil.getPath(endpointUrl).endsWith("/discovery");
  }

  /**
   * Filter {@code candidates} to those matching the host of {@code requestedEndpointUrl}, falling
   * back to one default host per transport profile when none match.
   *
   * <p>Host comparison is case-insensitive and requires a parseable host in both URLs. When no
   * candidate matches (including when {@code requestedEndpointUrl} is {@code null} or unparseable),
   * the default host for each {@code transportProfileUri} is the first candidate endpoint's
   * non-null host for that profile, preserving candidate order. If a transport profile has no
   * candidate with a parseable host, all candidates for that profile are retained.
   *
   * @param candidates the candidate {@link EndpointDescription}s in configuration order.
   * @param requestedEndpointUrl the endpoint URL supplied in the client request, or {@code null}.
   * @return the matching or fallback {@link EndpointDescription}s in candidate order.
   */
  public static List<EndpointDescription> filterByHostOrDefault(
      List<EndpointDescription> candidates, @Nullable String requestedEndpointUrl) {

    if (candidates.isEmpty()) {
      return List.of();
    }

    String requestedHost = extractHost(requestedEndpointUrl);

    if (requestedHost != null) {
      List<EndpointDescription> matching =
          candidates.stream()
              .filter(ed -> requestedHost.equalsIgnoreCase(extractHost(ed.getEndpointUrl())))
              .toList();

      if (!matching.isEmpty()) {
        return matching;
      }
    }

    Map<@Nullable String, String> defaultHostsByProfile = defaultHostsByProfile(candidates);

    return filterByProfileHosts(candidates, defaultHostsByProfile);
  }

  /**
   * Select discovery URLs for {@code requestedEndpointUrl} from the server's advertised endpoints.
   *
   * <p>When {@code requestedEndpointUrl}'s host matches at least one endpoint, URLs are selected
   * from the matching endpoints. Otherwise, one default host per transport profile is chosen from
   * non-discovery endpoints first (filling in any profile that only advertises {@code "/discovery"}
   * endpoints), and URLs are selected from the endpoints matching those per-profile default hosts.
   *
   * <p>Within each selected transport profile independently, dedicated {@code "/discovery"}
   * endpoints are preferred when present; otherwise that profile's regular endpoint URLs are
   * emitted so one transport's {@code "/discovery"} endpoint does not suppress another transport's
   * usable URL.
   *
   * @param allEndpoints the candidate {@link EndpointDescription}s to select discovery URLs from.
   * @param requestedEndpointUrl the endpoint URL supplied in the client request, or {@code null}.
   * @return the distinct discovery URLs for the matched or default host(s) in candidate order.
   */
  public static List<String> filterDiscoveryUrls(
      List<EndpointDescription> allEndpoints, @Nullable String requestedEndpointUrl) {

    if (allEndpoints.isEmpty()) {
      return List.of();
    }

    String requestedHost = extractHost(requestedEndpointUrl);

    if (requestedHost != null) {
      List<EndpointDescription> matchingEndpoints =
          allEndpoints.stream()
              .filter(ed -> requestedHost.equalsIgnoreCase(extractHost(ed.getEndpointUrl())))
              .toList();

      if (!matchingEndpoints.isEmpty()) {
        return selectDiscoveryUrlsPerProfile(matchingEndpoints);
      }
    }

    List<EndpointDescription> discoveryEndpoints =
        allEndpoints.stream().filter(EndpointFilter::isDiscoveryEndpoint).toList();

    List<EndpointDescription> nonDiscoveryEndpoints =
        allEndpoints.stream().filter(ed -> !isDiscoveryEndpoint(ed)).toList();

    // Derive per-profile default hosts from non-discovery endpoints first so discovery URLs stay
    // aligned with GetEndpoints and CreateSession even if /discovery endpoints were registered in
    // a different host order.
    Map<@Nullable String, String> defaultHostsByProfile =
        defaultHostsByProfile(nonDiscoveryEndpoints);

    for (EndpointDescription endpoint : discoveryEndpoints) {
      String profileUri = endpoint.getTransportProfileUri();
      if (!defaultHostsByProfile.containsKey(profileUri)) {
        String host = extractHost(endpoint.getEndpointUrl());
        if (host != null) {
          defaultHostsByProfile.put(profileUri, host);
        }
      }
    }

    List<EndpointDescription> fallbackEndpoints =
        filterByProfileHosts(allEndpoints, defaultHostsByProfile);

    return selectDiscoveryUrlsPerProfile(fallbackEndpoints);
  }

  /**
   * Select discovery URLs from {@code candidateEndpoints} for the transport profiles and hosts
   * present in {@code selectedEndpoints}.
   *
   * <p>Each {@code (transportProfileUri, host)} pair present in {@code selectedEndpoints} (matched
   * case-insensitively on host) retains the corresponding entries from {@code candidateEndpoints}.
   * Within each transport profile independently, dedicated {@code "/discovery"} endpoints are
   * preferred when present; otherwise that profile's regular endpoint URLs are emitted, preserving
   * candidate encounter order.
   *
   * @param candidateEndpoints all candidate {@link EndpointDescription}s (including {@code
   *     "/discovery"} endpoints) within the active profile scope.
   * @param selectedEndpoints the endpoints selected for the response.
   * @return the distinct discovery URLs for the selected profiles and hosts in candidate order.
   */
  public static List<String> filterDiscoveryUrlsForEndpoints(
      List<EndpointDescription> candidateEndpoints, List<EndpointDescription> selectedEndpoints) {

    if (candidateEndpoints.isEmpty() || selectedEndpoints.isEmpty()) {
      return List.of();
    }

    Map<@Nullable String, Set<@Nullable String>> selectedHostsByProfile = new HashMap<>();

    for (EndpointDescription endpoint : selectedEndpoints) {
      String host = extractHost(endpoint.getEndpointUrl());
      String normalizedHost = host != null ? host.toLowerCase(Locale.ROOT) : null;

      selectedHostsByProfile
          .computeIfAbsent(endpoint.getTransportProfileUri(), ignored -> new HashSet<>())
          .add(normalizedHost);
    }

    List<EndpointDescription> matchingCandidates = new ArrayList<>();

    for (EndpointDescription candidate : candidateEndpoints) {
      Set<@Nullable String> hosts = selectedHostsByProfile.get(candidate.getTransportProfileUri());
      if (hosts == null) {
        continue;
      }

      String host = extractHost(candidate.getEndpointUrl());
      String normalizedHost = host != null ? host.toLowerCase(Locale.ROOT) : null;

      if (hosts.contains(normalizedHost)) {
        matchingCandidates.add(candidate);
      }
    }

    return selectDiscoveryUrlsPerProfile(matchingCandidates);
  }

  private static List<String> selectDiscoveryUrlsPerProfile(List<EndpointDescription> endpoints) {
    Set<@Nullable String> profilesWithDedicatedDiscovery = new HashSet<>();

    for (EndpointDescription endpoint : endpoints) {
      if (isDiscoveryEndpoint(endpoint) && endpoint.getEndpointUrl() != null) {
        profilesWithDedicatedDiscovery.add(endpoint.getTransportProfileUri());
      }
    }

    Set<String> discoveryUrls = new LinkedHashSet<>();

    for (EndpointDescription endpoint : endpoints) {
      String url = endpoint.getEndpointUrl();
      if (url == null) {
        continue;
      }

      if (isDiscoveryEndpoint(endpoint)
          || !profilesWithDedicatedDiscovery.contains(endpoint.getTransportProfileUri())) {
        discoveryUrls.add(url);
      }
    }

    return List.copyOf(discoveryUrls);
  }

  private static Map<@Nullable String, String> defaultHostsByProfile(
      List<EndpointDescription> endpoints) {

    Map<@Nullable String, String> defaultHostsByProfile = new LinkedHashMap<>();

    for (EndpointDescription endpoint : endpoints) {
      String profileUri = endpoint.getTransportProfileUri();
      if (!defaultHostsByProfile.containsKey(profileUri)) {
        String host = extractHost(endpoint.getEndpointUrl());
        if (host != null) {
          defaultHostsByProfile.put(profileUri, host);
        }
      }
    }

    return defaultHostsByProfile;
  }

  private static List<EndpointDescription> filterByProfileHosts(
      List<EndpointDescription> endpoints, Map<@Nullable String, String> defaultHostsByProfile) {

    List<EndpointDescription> filtered = new ArrayList<>();

    for (EndpointDescription endpoint : endpoints) {
      String defaultHost = defaultHostsByProfile.get(endpoint.getTransportProfileUri());
      if (defaultHost == null
          || defaultHost.equalsIgnoreCase(extractHost(endpoint.getEndpointUrl()))) {
        filtered.add(endpoint);
      }
    }

    return filtered;
  }

  private static @Nullable String extractHost(@Nullable String endpointUrl) {
    if (endpointUrl == null) {
      return null;
    }

    String host = EndpointUtil.getHost(endpointUrl);

    return host != null && !host.isEmpty() ? host : null;
  }
}
