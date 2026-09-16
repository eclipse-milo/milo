/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.Objects.requireNonNullElse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.server.EndpointSelectionKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An immutable index from {@link EndpointSelectionKey} to the effective endpoint it identifies.
 *
 * <p>Built once per resolved endpoint set, this index is the single definition of endpoint
 * selection shared by configuration validation and runtime channel/session resolution:
 *
 * <ul>
 *   <li>{@link #validate()} enforces at startup that no two non-equivalent endpoints share a
 *       selection key, so runtime selection can never depend on endpoint collection ordering.
 *   <li>{@link #select(EndpointSelectionKey, String)} resolves a key to exactly one {@link
 *       ResolvedEndpoint}, preferring the host/port substitution alias that matches the client's
 *       requested endpoint URL.
 * </ul>
 */
@NullMarked
final class EndpointSelectionIndex {

  private final Map<EndpointSelectionKey, List<ResolvedEndpoint>> groups;
  private final Set<EndpointSelectionKey> collidingKeys;
  private final List<String> collisions;

  private EndpointSelectionIndex(
      Map<EndpointSelectionKey, List<ResolvedEndpoint>> groups,
      Set<EndpointSelectionKey> collidingKeys,
      List<String> collisions) {

    this.groups = groups;
    this.collidingKeys = collidingKeys;
    this.collisions = collisions;
  }

  /**
   * Build an index over {@code endpoints}, recording any selection key collisions.
   *
   * <p>Endpoints sharing a key are a collision unless they are {@link
   * EndpointSelectionKey#isSessionEquivalent(EndpointDescription, EndpointDescription)
   * Session-equivalent}, i.e. host/port substitution aliases of the same effective endpoint.
   *
   * @param endpoints the resolved endpoints to index.
   * @return the index; call {@link #validate()} to fail on recorded collisions.
   */
  static EndpointSelectionIndex build(List<ResolvedEndpoint> endpoints) {
    var groups = new LinkedHashMap<EndpointSelectionKey, List<ResolvedEndpoint>>();

    for (ResolvedEndpoint endpoint : endpoints) {
      EndpointSelectionKey key = EndpointSelectionKey.of(endpoint.endpointDescription());
      groups.computeIfAbsent(key, k -> new ArrayList<>()).add(endpoint);
    }

    var collidingKeys = new HashSet<EndpointSelectionKey>();
    var collisions = new ArrayList<String>();

    groups.forEach(
        (key, group) -> {
          EndpointDescription first = group.get(0).endpointDescription();

          boolean equivalent =
              group.stream()
                  .allMatch(
                      r ->
                          EndpointSelectionKey.isSessionEquivalent(first, r.endpointDescription()));

          if (!equivalent) {
            collidingKeys.add(key);

            String colliding =
                group.stream()
                    .map(r -> describe(r.endpointDescription()))
                    .collect(Collectors.joining(", "));

            collisions.add(
                String.format(
                    "endpoints indistinguishable at OpenSecureChannel differ in"
                        + " Session-sensitive properties: selectionKey=%s, endpoints=[%s]",
                    key, colliding));
          }
        });

    return new EndpointSelectionIndex(groups, collidingKeys, collisions);
  }

  /**
   * Fail if any selection key is claimed by non-equivalent endpoints.
   *
   * <p>Endpoints intended to differ only in supported authentication methods must instead combine
   * their {@link UserTokenPolicy}s into a single endpoint configuration; endpoints intended to have
   * distinct Session policy or access behavior must be distinguishable by a wire-observable
   * selector (SecurityPolicy, MessageSecurityMode, or endpoint certificate).
   *
   * @throws UaException with {@link StatusCodes#Bad_ConfigurationError} identifying each colliding
   *     selection key and its endpoints.
   */
  void validate() throws UaException {
    if (!collisions.isEmpty()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "ambiguous endpoint configuration: " + String.join("; ", collisions));
    }
  }

  /**
   * Resolve {@code key} to the unique effective endpoint it identifies.
   *
   * <p>When multiple host/port substitution aliases share the key, the alias whose URL matches
   * {@code requestedEndpointUrl} is preferred (host and port, then host only); the first alias is
   * used otherwise. Aliases carry identical Session-sensitive state, so the choice affects only the
   * advertised URL.
   *
   * @param key the {@link EndpointSelectionKey} to resolve.
   * @param requestedEndpointUrl the endpoint URL requested by the client, if available.
   * @return the unique {@link ResolvedEndpoint} for {@code key}, or empty if there is none or the
   *     key is among the recorded collisions.
   */
  Optional<ResolvedEndpoint> select(
      EndpointSelectionKey key, @Nullable String requestedEndpointUrl) {

    List<ResolvedEndpoint> group = groups.get(key);

    if (group == null || collidingKeys.contains(key)) {
      return Optional.empty();
    }

    return Optional.of(
        EndpointSelectionKey.preferRequestedUrl(
            group, requestedEndpointUrl, r -> r.endpointDescription().getEndpointUrl()));
  }

  private static String describe(EndpointDescription endpoint) {
    String tokenPolicies =
        Stream.of(requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]))
            .map(p -> p.getTokenType() + "/" + p.getPolicyId())
            .collect(Collectors.joining(",", "[", "]"));

    return endpoint.getEndpointUrl() + " userTokenPolicies=" + tokenPolicies;
  }
}
