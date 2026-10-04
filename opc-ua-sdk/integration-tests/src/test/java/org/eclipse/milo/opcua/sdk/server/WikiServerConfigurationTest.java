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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Checks advertised endpoint settings, Session authentication, limits, and partial startup. */
@Timeout(40)
public class WikiServerConfigurationTest {
  @Test
  void advertisedUsernameEndpointAuthenticatesAndEnforcesReadLimit() throws Exception {
    int port = TestPortAllocator.allocatePort();
    String applicationUri = "urn:eclipse:milo:wiki:configuration";
    KeyPair keys = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keys)
            .setCommonName("Wiki configuration fixture")
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .addIpAddress("127.0.0.1")
            .build();
    // The fixture secret is local test data, not a deployment credential store.
    var usernameValidator =
        new UsernameIdentityValidator(
            challenge ->
                "reader".equals(challenge.getUsername())
                    && "fixture-secret".equals(challenge.getPassword()));
    TestServer fixture =
        TestServer.create(
            port,
            new OpcUaServerConfigLimits() {},
            builder -> {
              var certificateManager = builder.build().getCertificateManager();
              try {
                certificateManager
                    .getDefaultApplicationGroup()
                    .orElseThrow()
                    .updateCertificate(
                        NodeIds.RsaSha256ApplicationCertificateType,
                        keys,
                        new X509Certificate[] {certificate});
              } catch (Exception e) {
                throw new IllegalStateException(e);
              }
              // wiki:server-config:start
              EndpointConfig endpoint =
                  EndpointConfig.newBuilder()
                      .setBindAddress("127.0.0.1")
                      .setHostname("localhost")
                      .setBindPort(port)
                      .setPath("/wiki")
                      .setCertificate(certificate)
                      .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
                      .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                      .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME)
                      .build();
              builder
                  .setApplicationUri(applicationUri)
                  .setCertificateManager(certificateManager)
                  .setEndpoints(Set.of(endpoint))
                  .setIdentityValidator(usernameValidator)
                  .setLimits(
                      new OpcUaServerConfigLimits() {
                        @Override
                        public UInteger getMaxNodesPerRead() {
                          return uint(2);
                        }
                      });
              // wiki:server-config:end
            });
    OpcUaServer server = fixture.getServer();
    var trust = new MemoryTrustListManager();
    trust.addTrustedCertificate(certificate);
    var validator =
        new DefaultClientCertificateValidator(
            trust, ValidationCheck.ALL_OPTIONAL_CHECKS, new MemoryCertificateQuarantine());
    String url = "opc.tcp://localhost:" + port + "/wiki";
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      assertEquals(1, server.getBoundEndpoints().size());
      var endpoints = DiscoveryClient.getEndpoints(url).get(10, TimeUnit.SECONDS);
      assertEquals(1, endpoints.size());
      assertEquals(url, endpoints.get(0).getEndpointUrl());
      assertTrue(
          Arrays.stream(endpoints.get(0).getUserIdentityTokens())
              .anyMatch(token -> token.getTokenType() == UserTokenType.UserName));
      for (String password : List.of("wrong", "fixture-secret")) {
        OpcUaClient client =
            OpcUaClient.create(
                url,
                descriptions -> descriptions.stream().findFirst(),
                transport -> {},
                config ->
                    config
                        .setCertificateIdentity(
                            fixture.getClientKeyPair(), fixture.getClientCertificateChain())
                        .setCertificateValidator(validator)
                        .setIdentityProvider(new UsernameProvider("reader", password)));
        try {
          if (password.equals("wrong")) {
            Exception failure =
                assertThrows(
                    Exception.class, () -> client.connectAsync().get(10, TimeUnit.SECONDS));
            assertEquals(
                StatusCodes.Bad_IdentityTokenInvalid,
                UaException.extract(failure).orElseThrow().getStatusCode().getValue());
          } else {
            client.connectAsync().get(10, TimeUnit.SECONDS);
            assertTrue(
                client
                    .readValue(0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State)
                    .statusCode()
                    .isGood());
            UaException limit =
                assertThrows(
                    UaException.class,
                    () ->
                        client.readValues(
                            0,
                            TimestampsToReturn.Neither,
                            List.of(
                                NodeIds.Server_ServerStatus_State,
                                NodeIds.Server_ServerStatus_State,
                                NodeIds.Server_ServerStatus_State)));
            assertEquals(StatusCodes.Bad_TooManyOperations, limit.getStatusCode().getValue());
          }
        } finally {
          client.disconnectAsync().get(10, TimeUnit.SECONDS);
        }
      }
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
    assertTrue(server.getBoundEndpoints().isEmpty());
  }

  @Test
  void startupCanSucceedAfterOmittingAnUnusableSecureEndpoint() throws Exception {
    int port = TestPortAllocator.allocatePort();
    OpcUaServer server =
        TestServer.create(
                port,
                new OpcUaServerConfigLimits() {},
                builder -> {
                  var endpoint =
                      EndpointConfig.newBuilder()
                          .setBindAddress("127.0.0.1")
                          .setHostname("localhost")
                          .setBindPort(port);
                  builder
                      .setCertificateManager(new DefaultCertificateManager())
                      .setEndpoints(
                          Set.of(
                              endpoint.copy().build(),
                              endpoint
                                  .copy()
                                  .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
                                  .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                                  .build()));
                })
            .getServer();
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      assertEquals(1, server.getBoundEndpoints().size());
      assertEquals(SecurityPolicy.None, server.getBoundEndpoints().get(0).getSecurityPolicy());
      var advertised =
          DiscoveryClient.getEndpoints("opc.tcp://localhost:" + port).get(10, TimeUnit.SECONDS);
      assertEquals(1, advertised.size());
      assertEquals(SecurityPolicy.None.getUri(), advertised.get(0).getSecurityPolicyUri());
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }
}
