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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Test;

public class OpcUaServerEndpointDescriptionTest {

  @Test
  public void userTokenPolicyIdsAreUniqueWhenEffectiveSecurityPolicyDiffers() throws Exception {
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

    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    endpoints.add(noneEndpoint);
    endpoints.add(signEndpoint);
    endpoints.add(signAndEncryptEndpoint);

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

    UserTokenPolicy nonePolicy = getAnonymousPolicy(noneEndpointDescription);
    UserTokenPolicy signPolicy = getAnonymousPolicy(signEndpointDescription);
    UserTokenPolicy signAndEncryptPolicy = getAnonymousPolicy(signAndEncryptEndpointDescription);

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

    Session session =
        new Session(
            server,
            new NodeId(1, "session"),
            "session",
            Duration.ofMinutes(1),
            clientDescription(),
            "urn:eclipse:milo:test",
            uint(0),
            signEndpointDescription,
            1L,
            new SecurityConfiguration(
                SecurityPolicy.Basic256Sha256,
                MessageSecurityMode.Sign,
                null,
                null,
                null,
                null,
                null));

    try {
      UserTokenPolicy validatedPolicy =
          validatePolicyId(
              server.getSessionManager(),
              session,
              new AnonymousIdentityToken(signPolicy.getPolicyId()));

      assertEquals(signPolicy, validatedPolicy);
    } finally {
      session.close(false);
    }
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

  private UserTokenPolicy getAnonymousPolicy(EndpointDescription endpointDescription) {
    return List.of(endpointDescription.getUserIdentityTokens()).stream()
        .filter(t -> t.getTokenType() == UserTokenType.Anonymous)
        .findFirst()
        .orElseThrow();
  }

  private ApplicationDescription clientDescription() {
    return new ApplicationDescription(
        "urn:eclipse:milo:test:client",
        "urn:eclipse:milo:test",
        LocalizedText.english("test client"),
        ApplicationType.Client,
        null,
        null,
        null);
  }

  private UserTokenPolicy validatePolicyId(
      SessionManager sessionManager, Session session, AnonymousIdentityToken token)
      throws Exception {

    Method method =
        SessionManager.class.getDeclaredMethod("validatePolicyId", Session.class, Object.class);
    method.setAccessible(true);

    return (UserTokenPolicy) method.invoke(sessionManager, session, token);
  }
}
