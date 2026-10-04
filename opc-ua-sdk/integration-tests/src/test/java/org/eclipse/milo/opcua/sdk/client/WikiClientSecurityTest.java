/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(30)
class WikiClientSecurityTest extends AbstractClientServerTest {
  @Override
  protected TestServer createTestServer() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    String applicationUri = "urn:eclipse:milo:wiki:security-server";
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("Wiki local server")
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .addIpAddress("127.0.0.1")
            .build();
    return TestServer.create(
        builder -> {
          OpcUaServerConfig defaults = builder.build();
          try {
            defaults
                .getCertificateManager()
                .getDefaultApplicationGroup()
                .orElseThrow()
                .updateCertificate(
                    NodeIds.RsaSha256ApplicationCertificateType,
                    keyPair,
                    new X509Certificate[] {certificate});
          } catch (Exception e) {
            throw new IllegalStateException(e);
          }
          int port = defaults.getEndpoints().iterator().next().getBindPort();
          EndpointConfig.Builder endpoint =
              EndpointConfig.newBuilder()
                  .setBindAddress("127.0.0.1")
                  .setHostname("localhost")
                  .setBindPort(port)
                  .setPath("/test")
                  .setCertificate(certificate)
                  .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);
          builder
              .setApplicationUri(applicationUri)
              .setEndpoints(
                  Set.of(
                      endpoint.copy().build(),
                      endpoint
                          .copy()
                          .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
                          .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                          .build()));
        });
  }

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder builder) {
    // The inherited fixture client supplies test setup only. Its endpoint must not depend on
    // Set iteration order; the separately configured client below exercises secured connections.
    builder.setEndpoint(
        builder.build().getDiscoveryEndpoints().stream()
            .filter(
                endpoint -> SecurityPolicy.None.getUri().equals(endpoint.getSecurityPolicyUri()))
            .findFirst()
            .orElseThrow());
  }

  // The example must establish a real secured channel with mutual application trust.
  @Test
  void explicitTrustConnectsAndEmptyTrustRejectsTheSameServer() throws Exception {
    var endpoint = server.getConfig().getEndpoints().iterator().next();
    X509Certificate trustedServer = endpoint.getCertificate();
    OpcUaClient secured =
        secureClient(
            endpoint.getEndpointUrl(),
            testServer.getClientKeyPair(),
            testServer.getClientCertificateChain(),
            trustedServer);
    try {
      secured.connectAsync().get(10, TimeUnit.SECONDS);
      DataValue value =
          secured.readValue(0.0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State);
      assertTrue(value.statusCode().isGood());
      assertEquals(
          MessageSecurityMode.SignAndEncrypt, secured.getConfig().getEndpoint().getSecurityMode());
    } finally {
      secured.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
    var quarantine = new MemoryCertificateQuarantine();
    var rejectingValidator =
        new DefaultClientCertificateValidator(
            new MemoryTrustListManager(), ValidationCheck.ALL_OPTIONAL_CHECKS, quarantine);
    UaException rejected =
        assertThrows(
            UaException.class,
            () ->
                rejectingValidator.validateCertificateChain(
                    List.of(trustedServer),
                    server.getConfig().getApplicationUri(),
                    new String[] {"localhost"}));
    // With zero anchors, PKIXBuilderParameters fails before path trust evaluation.
    // CertificateValidationUtil.buildCertPath preserves that failure as Bad_SecurityChecksFailed.
    assertEquals(StatusCodes.Bad_SecurityChecksFailed, rejected.getStatusCode().value());
    assertInstanceOf(InvalidAlgorithmParameterException.class, rejected.getCause());
    assertTrue(quarantine.getRejectedCertificates().contains(trustedServer));
  }

  // snippet:security:start
  static OpcUaClient secureClient(
      String discoveryUrl,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      X509Certificate trustedServer)
      throws UaException {
    var trust = new MemoryTrustListManager();
    trust.addTrustedCertificate(trustedServer);
    var quarantine = new MemoryCertificateQuarantine();
    var validator =
        new DefaultClientCertificateValidator(
            trust, ValidationCheck.ALL_OPTIONAL_CHECKS, quarantine);
    return OpcUaClient.create(
        discoveryUrl,
        endpoints ->
            endpoints.stream()
                .filter(
                    e -> SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
                .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                .findFirst(),
        transport -> {},
        config ->
            config
                .setCertificateIdentity(keyPair, certificateChain)
                .setCertificateValidator(validator)
                .setSessionEndpointValidationEnabled(true)
                .setIdentityProvider(new AnonymousProvider()));
  }
  // snippet:security:end
}
