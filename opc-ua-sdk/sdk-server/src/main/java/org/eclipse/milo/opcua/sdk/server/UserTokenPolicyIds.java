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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

/**
 * Resolves configured user token policies into the values advertised in {@link EndpointDescription
 * EndpointDescriptions}.
 *
 * <p>An {@link EndpointConfig} can reuse a configured policy across endpoint variants, and a policy
 * with a null or empty security policy URI inherits the security policy of its endpoint. The same
 * configured policy ID can therefore describe different effective policies. This type assigns IDs
 * across the complete endpoint set and creates endpoint-specific policy copies without modifying
 * the server configuration.
 *
 * <p>Each instance is scoped to the endpoint set supplied to {@link #assign(Set)}. It must only be
 * used to build descriptions for endpoints from that set.
 *
 * @see <a href="https://reference.opcfoundation.org/specs/OPC-10000-4/7.41">OPC UA Part 4 §7.41</a>
 */
final class UserTokenPolicyIds {

  private final Map<UserTokenPolicy, String> assignedPolicyIds;

  private UserTokenPolicyIds(Map<UserTokenPolicy, String> assignedPolicyIds) {
    this.assignedPolicyIds = assignedPolicyIds;
  }

  /**
   * Computes the policy ID assignment shared by a set of endpoint descriptions.
   *
   * <p>When a configured ID represents more than one effective policy, the first policy encountered
   * retains that ID and subsequent policies receive unique IDs. The iteration order of {@code
   * endpoints} therefore determines which effective policy retains a conflicting configured ID.
   *
   * @param endpoints the complete set of endpoints that will be advertised together.
   * @return the policy ID assignment for the endpoint set.
   */
  static UserTokenPolicyIds assign(Set<EndpointConfig> endpoints) {
    Map<String, Set<UserTokenPolicy>> policiesById = new LinkedHashMap<>();

    for (EndpointConfig endpoint : endpoints) {
      for (UserTokenPolicy tokenPolicy : endpoint.getTokenPolicies()) {
        UserTokenPolicy effectivePolicy = effectivePolicy(endpoint, tokenPolicy);
        policiesById
            .computeIfAbsent(effectivePolicy.getPolicyId(), ignored -> new LinkedHashSet<>())
            .add(effectivePolicy);
      }
    }

    Set<String> reservedPolicyIds = new LinkedHashSet<>(policiesById.keySet());
    Map<UserTokenPolicy, String> assignedPolicyIds = new HashMap<>();

    for (Set<UserTokenPolicy> policies : policiesById.values()) {
      boolean preserveConfiguredId = true;

      for (UserTokenPolicy policy : policies) {
        assignedPolicyIds.put(
            policy,
            preserveConfiguredId
                ? policy.getPolicyId()
                : uniquePolicyId(policy, reservedPolicyIds));
        preserveConfiguredId = false;
      }
    }

    return new UserTokenPolicyIds(assignedPolicyIds);
  }

  /**
   * Builds the user token policies to advertise for an endpoint.
   *
   * <p>The returned policies contain effective security policy URIs and server-wide assigned IDs.
   * The configured policies in {@code endpoint} remain unchanged.
   *
   * @param endpoint an endpoint from the set supplied to {@link #assign(Set)}.
   * @return a new array of policies for the endpoint description.
   */
  UserTokenPolicy[] policiesFor(EndpointConfig endpoint) {
    return endpoint.getTokenPolicies().stream()
        .map(tokenPolicy -> advertisedPolicy(endpoint, tokenPolicy))
        .toArray(UserTokenPolicy[]::new);
  }

  private UserTokenPolicy advertisedPolicy(
      EndpointConfig endpoint, UserTokenPolicy configuredPolicy) {

    UserTokenPolicy effectivePolicy = effectivePolicy(endpoint, configuredPolicy);
    String assignedPolicyId = assignedPolicyIds.get(effectivePolicy);

    String policyId =
        assignedPolicyId.equals(effectivePolicy.getPolicyId())
            ? configuredPolicy.getPolicyId()
            : assignedPolicyId;

    return new UserTokenPolicy(
        policyId,
        effectivePolicy.getTokenType(),
        effectivePolicy.getIssuedTokenType(),
        effectivePolicy.getIssuerEndpointUrl(),
        effectivePolicy.getSecurityPolicyUri());
  }

  private static UserTokenPolicy effectivePolicy(
      EndpointConfig endpoint, UserTokenPolicy tokenPolicy) {

    return new UserTokenPolicy(
        Objects.requireNonNullElse(tokenPolicy.getPolicyId(), ""),
        tokenPolicy.getTokenType(),
        tokenPolicy.getIssuedTokenType(),
        tokenPolicy.getIssuerEndpointUrl(),
        endpoint.getEffectiveTokenSecurityPolicyUri(tokenPolicy));
  }

  private static String uniquePolicyId(UserTokenPolicy policy, Set<String> reservedPolicyIds) {
    String base =
        policy.getPolicyId().isEmpty()
            ? policy.getTokenType().name().toLowerCase(Locale.ROOT)
            : policy.getPolicyId();

    String securityPolicyName = securityPolicyName(policy.getSecurityPolicyUri());

    String candidate = base + "-" + securityPolicyName;
    if (reservedPolicyIds.add(candidate)) {
      return candidate;
    }

    candidate = base + "-" + policy.getTokenType().name() + "-" + securityPolicyName;
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
}
