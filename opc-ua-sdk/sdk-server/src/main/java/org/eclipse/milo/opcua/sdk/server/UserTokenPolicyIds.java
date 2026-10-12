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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;

/**
 * Assigns the user token policy IDs advertised for a resolved set of server endpoints.
 *
 * <p>OPC UA clients select a user-token policy by {@code policyId} during session activation. When
 * several endpoint configurations reuse the same configured ID for policies that differ by token
 * type or security policy, the advertised endpoint descriptions need distinct IDs so the selected
 * policy can be matched unambiguously later. This helper keeps that advertisement-only
 * normalization out of {@link OpcUaServer} while leaving the original {@link EndpointConfig}
 * instances unchanged.
 */
final class UserTokenPolicyIds {

  private final Map<UserTokenPolicyKey, String> assignedPolicyIds;

  private UserTokenPolicyIds(Map<UserTokenPolicyKey, String> assignedPolicyIds) {
    this.assignedPolicyIds = assignedPolicyIds;
  }

  /**
   * Create a policy ID assignment for endpoints that will be advertised together.
   *
   * <p>The endpoints should already have passed endpoint resolution. Endpoints that are omitted
   * from discovery must not reserve IDs that clients will never see.
   *
   * @param endpoints the resolved endpoint configurations being advertised.
   * @return an assignment that can build advertised token policies for each endpoint.
   */
  static UserTokenPolicyIds assign(List<EndpointConfig> endpoints) {
    Map<String, List<UserTokenPolicyKey>> keysByPolicyId = new LinkedHashMap<>();

    for (EndpointConfig endpoint : endpoints) {
      for (UserTokenPolicy tokenPolicy : endpoint.getTokenPolicies()) {
        UserTokenPolicyKey key = UserTokenPolicyKey.from(endpoint, tokenPolicy);
        List<UserTokenPolicyKey> keys =
            keysByPolicyId.computeIfAbsent(key.policyId(), ignored -> new ArrayList<>());

        if (!keys.contains(key)) {
          keys.add(key);
        }
      }
    }

    Set<String> reservedPolicyIds = new LinkedHashSet<>(keysByPolicyId.keySet());
    Map<UserTokenPolicyKey, String> assignedPolicyIds = new HashMap<>();

    for (List<UserTokenPolicyKey> keys : keysByPolicyId.values()) {
      if (keys.size() == 1) {
        UserTokenPolicyKey key = keys.get(0);
        assignedPolicyIds.put(key, key.policyId());
      } else {
        UserTokenPolicyKey firstKey = keys.get(0);
        assignedPolicyIds.put(firstKey, firstKey.policyId());

        for (int i = 1; i < keys.size(); i++) {
          UserTokenPolicyKey key = keys.get(i);
          assignedPolicyIds.put(key, uniquePolicyId(key, reservedPolicyIds));
        }
      }
    }

    return new UserTokenPolicyIds(assignedPolicyIds);
  }

  /**
   * Return the user token policies to place in the advertised endpoint description for an endpoint.
   *
   * <p>The returned policies preserve configured values except when a reused {@code policyId} needs
   * a unique advertised ID. Empty and null configured IDs keep their wire-compatible representation
   * when no assignment change is needed.
   *
   * @param endpoint the endpoint whose advertised token policies are being built.
   * @return the token policies to advertise for the endpoint.
   */
  UserTokenPolicy[] policiesFor(EndpointConfig endpoint) {
    return endpoint.getTokenPolicies().stream()
        .map(tokenPolicy -> transform(endpoint, tokenPolicy))
        .toArray(UserTokenPolicy[]::new);
  }

  private UserTokenPolicy transform(EndpointConfig endpoint, UserTokenPolicy tokenPolicy) {
    UserTokenPolicyKey key = UserTokenPolicyKey.from(endpoint, tokenPolicy);
    String assignedPolicyId = assignedPolicyIds.getOrDefault(key, key.policyId());

    String policyId =
        policyIdChanged(tokenPolicy.getPolicyId(), assignedPolicyId)
            ? assignedPolicyId
            : tokenPolicy.getPolicyId();

    return new UserTokenPolicy(
        policyId,
        tokenPolicy.getTokenType(),
        tokenPolicy.getIssuedTokenType(),
        tokenPolicy.getIssuerEndpointUrl(),
        key.securityPolicyUri());
  }

  private static boolean policyIdChanged(
      @Nullable String configuredPolicyId, String assignedPolicyId) {
    if (Objects.equals(configuredPolicyId, assignedPolicyId)) {
      return false;
    } else {
      return !(isNullOrEmpty(configuredPolicyId) && assignedPolicyId.isEmpty());
    }
  }

  private static String uniquePolicyId(UserTokenPolicyKey key, Set<String> reservedPolicyIds) {
    String base =
        key.policyId().isEmpty() ? key.tokenType().name().toLowerCase(Locale.ROOT) : key.policyId();

    String securityPolicyName = securityPolicyName(key.securityPolicyUri());

    String candidate = base + "-" + securityPolicyName;
    if (reservedPolicyIds.add(candidate)) {
      return candidate;
    }

    candidate = base + "-" + key.tokenType().name() + "-" + securityPolicyName;
    if (reservedPolicyIds.add(candidate)) {
      return candidate;
    }

    for (int i = 2; ; i++) {
      String indexedCandidate = candidate + "-" + i;
      if (reservedPolicyIds.add(indexedCandidate)) {
        return indexedCandidate;
      }
    }
  }

  private static String securityPolicyName(String securityPolicyUri) {
    int index = securityPolicyUri.lastIndexOf('#');
    String name = index >= 0 ? securityPolicyUri.substring(index + 1) : securityPolicyUri;

    return name.replaceAll("[^A-Za-z0-9_.-]", "-");
  }

  private static boolean isNullOrEmpty(@Nullable String value) {
    return value == null || value.isEmpty();
  }

  private record UserTokenPolicyKey(
      String policyId,
      UserTokenType tokenType,
      @Nullable String issuedTokenType,
      @Nullable String issuerEndpointUrl,
      String securityPolicyUri) {

    static UserTokenPolicyKey from(EndpointConfig endpoint, UserTokenPolicy tokenPolicy) {
      String policyId = tokenPolicy.getPolicyId();
      String securityPolicyUri = tokenPolicy.getSecurityPolicyUri();

      return new UserTokenPolicyKey(
          policyId == null ? "" : policyId,
          tokenPolicy.getTokenType(),
          tokenPolicy.getIssuedTokenType(),
          tokenPolicy.getIssuerEndpointUrl(),
          isNullOrEmpty(securityPolicyUri)
              ? endpoint.getSecurityPolicy().getUri()
              : securityPolicyUri);
    }
  }
}
