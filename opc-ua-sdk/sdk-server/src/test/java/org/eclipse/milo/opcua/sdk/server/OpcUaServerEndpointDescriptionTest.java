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
import static org.mockito.Mockito.when;

import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Test;

class OpcUaServerEndpointDescriptionTest {

  // Part 4 §7.41 requires policy IDs to be unique across the Server, while null or empty token
  // security policy URIs inherit their Endpoint's policy.
  @Test
  void userTokenPolicyIdsAreUniqueWhenEffectiveSecurityPolicyDiffers() throws Exception {
    X509Certificate certificate = mock(X509Certificate.class);
    when(certificate.getEncoded()).thenReturn(new byte[] {1, 2, 3});

    UserTokenPolicy anonymousPolicy = OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;

    EndpointConfig noneEndpoint =
        endpoint(SecurityPolicy.None, MessageSecurityMode.None, anonymousPolicy, null);
    EndpointConfig signEndpoint =
        endpoint(
            SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign, anonymousPolicy, certificate);
    EndpointConfig signAndEncryptEndpoint =
        endpoint(
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt,
            anonymousPolicy,
            certificate);

    Set<EndpointConfig> endpoints =
        new LinkedHashSet<>(List.of(noneEndpoint, signEndpoint, signAndEncryptEndpoint));

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
            .setEndpoints(endpoints)
            .build();

    OpcUaServer server = new OpcUaServer(config, transportProfile -> null);

    List<EndpointDescription> endpointDescriptions =
        server.getApplicationContext().getEndpointDescriptions();

    EndpointDescription noneEndpointDescription =
        getEndpointDescription(endpointDescriptions, SecurityPolicy.None, MessageSecurityMode.None);
    EndpointDescription signEndpointDescription =
        getEndpointDescription(
            endpointDescriptions, SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign);
    EndpointDescription signAndEncryptEndpointDescription =
        getEndpointDescription(
            endpointDescriptions,
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt);

    UserTokenPolicy nonePolicy = noneEndpointDescription.getUserIdentityTokens()[0];
    UserTokenPolicy signPolicy = signEndpointDescription.getUserIdentityTokens()[0];
    UserTokenPolicy signAndEncryptPolicy =
        signAndEncryptEndpointDescription.getUserIdentityTokens()[0];

    assertEquals("anonymous", nonePolicy.getPolicyId());
    assertEquals(SecurityPolicy.None.getUri(), nonePolicy.getSecurityPolicyUri());
    assertEquals("anonymous-Basic256Sha256", signPolicy.getPolicyId());
    assertEquals(SecurityPolicy.Basic256Sha256.getUri(), signPolicy.getSecurityPolicyUri());
    assertEquals("anonymous-Basic256Sha256", signAndEncryptPolicy.getPolicyId());
    assertEquals(
        SecurityPolicy.Basic256Sha256.getUri(), signAndEncryptPolicy.getSecurityPolicyUri());

    assertEquals("anonymous", noneEndpoint.getTokenPolicies().get(0).getPolicyId());
    assertNull(noneEndpoint.getTokenPolicies().get(0).getSecurityPolicyUri());
    assertEquals("anonymous", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS.getPolicyId());
    assertNull(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS.getSecurityPolicyUri());
  }

  private EndpointConfig endpoint(
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      UserTokenPolicy userTokenPolicy,
      X509Certificate certificate) {

    return EndpointConfig.newBuilder()
        .setBindPort(4840)
        .setHostname("localhost")
        .setPath("/milo")
        .setSecurityPolicy(securityPolicy)
        .setSecurityMode(securityMode)
        .setCertificate(certificate)
        .addTokenPolicy(userTokenPolicy)
        .build();
  }

  private EndpointDescription getEndpointDescription(
      List<EndpointDescription> endpointDescriptions,
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode) {

    return endpointDescriptions.stream()
        .filter(e -> securityPolicy.getUri().equals(e.getSecurityPolicyUri()))
        .filter(e -> securityMode == e.getSecurityMode())
        .findFirst()
        .orElseThrow();
  }
}
