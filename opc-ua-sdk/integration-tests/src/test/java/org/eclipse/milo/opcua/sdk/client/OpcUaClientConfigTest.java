/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.junit.jupiter.api.Test;

public class OpcUaClientConfigTest {

  private final EndpointDescription endpoint =
      new EndpointDescription(
          "opc.tcp://localhost:62541",
          null,
          null,
          null,
          null,
          new UserTokenPolicy[] {
            new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
          },
          null,
          null);

  @Test
  public void copyPreservesConfiguredValues() throws Exception {
    CertificateManager certificateManager =
        new DefaultCertificateManager(new MemoryCertificateQuarantine());
    CertificateIdentitySelector certificateIdentitySelector = context -> Optional.empty();
    NodeId certificateGroupId =
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup;
    NodeId certificateTypeId = NodeIds.EccNistP256ApplicationCertificateType;

    OpcUaClientConfig original =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setCertificateManager(certificateManager)
            .setCertificateIdentitySelector(certificateIdentitySelector)
            .setCertificateGroupId(certificateGroupId)
            .setCertificateTypeId(certificateTypeId)
            .setSessionEndpointValidationEnabled(true)
            .setSessionName(() -> "testSessionName")
            .setSessionTimeout(uint(60000 * 60))
            .setMaxResponseMessageSize(UInteger.MAX)
            .setMaxPendingPublishRequests(uint(2))
            .setIdentityProvider(new AnonymousProvider())
            .setSessionLocaleIds(new String[] {"en", "es"})
            .build();

    OpcUaClientConfig copy = OpcUaClientConfig.copy(original).build();

    assertEquals(original.getSessionName(), copy.getSessionName());
    assertEquals(original.getSessionTimeout(), copy.getSessionTimeout());
    assertEquals(original.getMaxResponseMessageSize(), copy.getMaxResponseMessageSize());
    assertEquals(original.getMaxPendingPublishRequests(), copy.getMaxPendingPublishRequests());
    assertEquals(original.getIdentityProvider(), copy.getIdentityProvider());
    assertEquals(original.getKeepAliveFailuresAllowed(), copy.getKeepAliveFailuresAllowed());
    assertEquals(original.getKeepAliveInterval(), copy.getKeepAliveInterval());
    assertEquals(original.getKeepAliveTimeout(), copy.getKeepAliveTimeout());
    assertEquals(original.getSessionLocaleIds(), copy.getSessionLocaleIds());
    assertEquals(original.getDiscoveryEndpoints(), copy.getDiscoveryEndpoints());
    assertEquals(
        original.isSessionEndpointValidationEnabled(), copy.isSessionEndpointValidationEnabled());
    assertEquals(original.getCertificateManager(), copy.getCertificateManager());
    assertSame(original.getCertificateIdentitySelector(), copy.getCertificateIdentitySelector());
    assertEquals(original.getCertificateGroupId(), copy.getCertificateGroupId());
    assertEquals(original.getCertificateTypeId(), copy.getCertificateTypeId());
    assertTrue(copy.getCertificateIdentity(SecurityPolicy.None.getProfile()).isEmpty());
  }

  @Test
  public void copyAndModifyOverridesConfiguredValues() {
    OpcUaClientConfig original =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(List.of(endpoint))
            .setSessionEndpointValidationEnabled(false)
            .setSessionName(() -> "testSessionName")
            .setSessionTimeout(uint(60000 * 60))
            .setMaxResponseMessageSize(UInteger.MAX)
            .setMaxPendingPublishRequests(uint(2))
            .setIdentityProvider(new AnonymousProvider())
            .build();

    EndpointDescription endpoint2 =
        new EndpointDescription(
            "opc.tcp://localhost:4840",
            null,
            null,
            null,
            null,
            new UserTokenPolicy[] {
              new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
            },
            null,
            null);

    OpcUaClientConfig copy =
        OpcUaClientConfig.copy(
            original,
            builder ->
                builder
                    .setSessionName(() -> "foo")
                    .setSessionTimeout(uint(0))
                    .setMaxResponseMessageSize(uint(0))
                    .setMaxPendingPublishRequests(uint(0))
                    .setIdentityProvider(new AnonymousProvider())
                    .setKeepAliveFailuresAllowed(uint(2))
                    .setKeepAliveInterval(uint(10000))
                    .setKeepAliveTimeout(uint(15000))
                    .setSessionLocaleIds(new String[] {"en", "es"})
                    .setDiscoveryEndpoints(List.of(endpoint2))
                    .setSessionEndpointValidationEnabled(true));

    assertNotEquals(original.getSessionName(), copy.getSessionName());
    assertNotEquals(original.getIdentityProvider(), copy.getIdentityProvider());
    assertNotEquals(original.getSessionLocaleIds(), copy.getSessionLocaleIds());

    assertEquals(uint(0), copy.getSessionTimeout());
    assertEquals(uint(0), copy.getMaxResponseMessageSize());
    assertEquals(uint(0), copy.getMaxPendingPublishRequests());
    assertEquals(uint(2), copy.getKeepAliveFailuresAllowed());
    assertEquals(uint(10000), copy.getKeepAliveInterval());
    assertEquals(uint(15000), copy.getKeepAliveTimeout());
    assertArrayEquals(new String[] {"en", "es"}, copy.getSessionLocaleIds());

    assertNotEquals(original.getDiscoveryEndpoints(), copy.getDiscoveryEndpoints());
    assertEquals(List.of(endpoint2), copy.getDiscoveryEndpoints());
    assertNotEquals(
        original.isSessionEndpointValidationEnabled(), copy.isSessionEndpointValidationEnabled());
    assertTrue(copy.isSessionEndpointValidationEnabled());
  }

  // SecureChannel and Session setup must reuse one selected client identity for a connection.
  @Test
  public void clientCachesSelectedCertificateIdentity() throws Exception {
    CertificateManager certificateManager =
        new DefaultCertificateManager(new MemoryCertificateQuarantine());
    AtomicInteger selections = new AtomicInteger();
    CertificateIdentitySelector certificateIdentitySelector =
        context -> {
          selections.incrementAndGet();
          return Optional.empty();
        };
    OpcClientTransportConfig transportConfig = mock(OpcClientTransportConfig.class);
    when(transportConfig.getExecutor()).thenReturn(Stack.sharedExecutor());
    OpcClientTransport transport = mock(OpcClientTransport.class);
    when(transport.getConfig()).thenReturn(transportConfig);

    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setCertificateManager(certificateManager)
            .setCertificateIdentitySelector(certificateIdentitySelector)
            .build();
    OpcUaClient client = new OpcUaClient(config, transport);

    assertTrue(client.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).isEmpty());
    assertTrue(client.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).isEmpty());
    assertEquals(1, selections.get());
  }
}
