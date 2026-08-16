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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class UserTokenPolicyIdsTest {

  private final X509Certificate certificate = mock(X509Certificate.class);

  // Part 4 §7.41 defines null and empty policy IDs as equal, but their configured wire
  // representation should remain unchanged when no remapping is required.
  @Test
  void nullAndEmptyPolicyIdsRemainEquivalentWithoutChangingRepresentation() {
    EndpointConfig nullIdEndpoint = endpoint(SecurityPolicy.None, anonymousPolicy(null));
    EndpointConfig emptyIdEndpoint = endpoint(SecurityPolicy.None, anonymousPolicy(""));

    UserTokenPolicyIds policyIds =
        UserTokenPolicyIds.assign(List.of(nullIdEndpoint, emptyIdEndpoint));

    assertNull(policyIds.policiesFor(nullIdEndpoint)[0].getPolicyId());
    assertEquals("", policyIds.policiesFor(emptyIdEndpoint)[0].getPolicyId());
  }

  // Generated IDs must not shadow configured IDs elsewhere in the Server, even when both readable
  // suffix candidates are already configured.
  @Test
  void generatedPolicyIdAvoidsConfiguredIdCollisions() {
    String configuredId = "anonymous";
    String securityPolicyCandidate = "anonymous-Basic256Sha256";
    String tokenTypeCandidate = "anonymous-Anonymous-Basic256Sha256";

    EndpointConfig noneEndpoint = endpoint(SecurityPolicy.None, anonymousPolicy(configuredId));
    EndpointConfig secureEndpoint =
        endpoint(SecurityPolicy.Basic256Sha256, anonymousPolicy(configuredId));
    EndpointConfig securityPolicyCandidateEndpoint =
        endpoint(SecurityPolicy.None, anonymousPolicy(securityPolicyCandidate));
    EndpointConfig tokenTypeCandidateEndpoint =
        endpoint(SecurityPolicy.None, anonymousPolicy(tokenTypeCandidate));

    UserTokenPolicyIds policyIds =
        UserTokenPolicyIds.assign(
            List.of(
                noneEndpoint,
                secureEndpoint,
                securityPolicyCandidateEndpoint,
                tokenTypeCandidateEndpoint));

    assertEquals(configuredId, policyIds.policiesFor(noneEndpoint)[0].getPolicyId());
    assertEquals(tokenTypeCandidate + "-2", policyIds.policiesFor(secureEndpoint)[0].getPolicyId());
    assertEquals(
        securityPolicyCandidate,
        policyIds.policiesFor(securityPolicyCandidateEndpoint)[0].getPolicyId());
    assertEquals(
        tokenTypeCandidate, policyIds.policiesFor(tokenTypeCandidateEndpoint)[0].getPolicyId());
  }

  private EndpointConfig endpoint(SecurityPolicy securityPolicy, UserTokenPolicy tokenPolicy) {
    MessageSecurityMode securityMode =
        securityPolicy == SecurityPolicy.None ? MessageSecurityMode.None : MessageSecurityMode.Sign;

    return EndpointConfig.newBuilder()
        .setSecurityPolicy(securityPolicy)
        .setSecurityMode(securityMode)
        .setCertificate(securityPolicy == SecurityPolicy.None ? null : certificate)
        .addTokenPolicy(tokenPolicy)
        .build();
  }

  private UserTokenPolicy anonymousPolicy(@Nullable String policyId) {
    return new UserTokenPolicy(policyId, UserTokenType.Anonymous, null, null, null);
  }
}
