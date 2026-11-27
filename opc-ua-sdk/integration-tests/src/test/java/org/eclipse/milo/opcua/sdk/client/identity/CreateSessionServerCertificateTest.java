/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_X509;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.server.EndpointCertificateConfig;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Part 4 (5.7.3.1): a user identity token's secret is encrypted to, and its signature covers, the
 * server certificate returned in the CreateSession response.
 *
 * <p>On an endpoint whose SecurityPolicy is {@code None}, nothing requires that certificate to
 * match the one in the client's cached EndpointDescription, and after the server renews its
 * certificate with a new key the two differ. Each test discovers the endpoint, renews the server's
 * certificate in place, and then signs in with the endpoint it discovered before the renewal.
 */
class CreateSessionServerCertificateTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:certificate-renewal";

  private final List<String> usernamesAccepted = new CopyOnWriteArrayList<>();

  private DefaultCertificateGroup certificateGroup;
  private OpcUaServer server;

  private X509Certificate identityCertificate;
  private KeyPair identityKeyPair;

  private X509Certificate originalCertificate;
  private X509Certificate renewedCertificate;
  private EndpointDescription cachedEndpoint;

  @BeforeEach
  void startServerAndRenewItsCertificate() throws Exception {
    identityKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    identityCertificate =
        SelfSignedCertificateBuilder.forRsaApplicationCertificate(identityKeyPair)
            .setCommonName("user identity")
            .setApplicationUri("urn:eclipse:milo:test:user")
            .build();

    var trustListManager = new MemoryTrustListManager();
    var quarantine = new MemoryCertificateQuarantine();
    certificateGroup =
        new DefaultCertificateGroup(
            trustListManager,
            new MemoryCertificateStore(),
            quarantine,
            new DefaultServerCertificateValidator(trustListManager, quarantine));

    originalCertificate = installNewServerCertificate();

    server = new OpcUaServer(serverConfig(TestPortAllocator.allocatePort()), this::transport);
    server.startup().get(10, TimeUnit.SECONDS);

    EndpointConfig endpointConfig = server.getConfig().getEndpoints().iterator().next();
    cachedEndpoint = discoverEndpoint(endpointConfig.getEndpointUrl());

    renewedCertificate = installNewServerCertificate();
    server.resetEndpointDescriptionCache();

    // The tests are only meaningful if the cached endpoint now names a certificate the server no
    // longer uses.
    assertAll(
        () ->
            assertEquals(
                ByteString.of(originalCertificate.getEncoded()),
                cachedEndpoint.getServerCertificate()),
        () ->
            assertEquals(
                ByteString.of(renewedCertificate.getEncoded()),
                discoverEndpoint(endpointConfig.getEndpointUrl()).getServerCertificate()),
        () -> assertNotEquals(originalCertificate, renewedCertificate));
  }

  @AfterEach
  void stopServer() throws Exception {
    if (server != null) {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // A renewal with a new key must not lock a username out of every new Session: the password has
  // to reach the server encrypted to the certificate it now holds the key for. The client trusts
  // both certificates, so only the choice between them decides the outcome.
  @Test
  void usernameTokenSignsInWithEndpointCachedBeforeRenewal() throws Exception {
    OpcUaClient client =
        createClient(
            new UsernameProvider("user1", "password"),
            validatorTrusting(originalCertificate, renewedCertificate));

    try {
      client.connect();

      assertEquals(List.of("user1"), usernamesAccepted);
    } finally {
      client.disconnect();
    }
  }

  // The server verifies the user-token signature over the certificate it returned in
  // CreateSession, so the client must sign that one rather than the cached endpoint's.
  @Test
  void certificateTokenSignsInWithEndpointCachedBeforeRenewal() throws Exception {
    OpcUaClient client =
        createClient(
            new X509IdentityProvider(identityCertificate, identityKeyPair.getPrivate()),
            validatorTrusting(originalCertificate, renewedCertificate));

    try {
      assertDoesNotThrow(client::connect, "the server must accept the user-token signature");
    } finally {
      client.disconnect();
    }
  }

  // Part 4 (7.41): on a None endpoint the client encrypts a password only to a certificate it
  // trusts. Trusting the certificate in the cached endpoint is not enough once CreateSession has
  // returned another one, because that other one is the certificate the password is encrypted to.
  @Test
  void usernameTokenRefusesRenewedCertificateTheClientDoesNotTrust() throws Exception {
    OpcUaClient client =
        createClient(
            new UsernameProvider("user1", "password"), validatorTrusting(originalCertificate));

    long rejectedSessionsBefore = rejectedSessionCount();

    try {
      UaException failure = assertThrows(UaException.class, client::connect);

      assertAll(
          () -> assertEquals(StatusCodes.Bad_CertificateUntrusted, failure.getStatusCode().value()),
          () ->
              assertEquals(
                  rejectedSessionsBefore,
                  rejectedSessionCount(),
                  "the client must not send an ActivateSession to an untrusted certificate"),
          () -> assertEquals(List.of(), usernamesAccepted));
    } finally {
      client.disconnect();
    }
  }

  private OpcUaServerConfig serverConfig(int port) {
    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/renewal")
            .setEndpointCertificateConfig(
                EndpointCertificateConfig.newBuilder()
                    .setCertificateTypeId(NodeIds.RsaSha256ApplicationCertificateType)
                    .build())
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            // Both token policies protect the token with Basic256Sha256 on this None endpoint.
            .addTokenPolicies(USER_TOKEN_POLICY_USERNAME, USER_TOKEN_POLICY_X509)
            .build();

    var usernameValidator =
        new UsernameIdentityValidator(
            challenge -> {
              boolean accepted =
                  "user1".equals(challenge.getUsername())
                      && "password".equals(challenge.getPassword());
              if (accepted) {
                usernamesAccepted.add(challenge.getUsername());
              }
              return accepted;
            });

    return OpcUaServerConfig.builder()
        .setApplicationUri(SERVER_URI)
        .setApplicationName(LocalizedText.english("Certificate renewal test server"))
        .setProductUri("urn:eclipse:milo:test")
        .setCertificateManager(new DefaultCertificateManager(certificateGroup))
        .setEndpoints(Set.of(endpoint))
        .setIdentityValidator(
            new CompositeValidator(
                usernameValidator, new X509IdentityValidator(identityCertificate::equals)))
        .build();
  }

  private OpcTcpServerTransport transport(TransportProfile profile) {
    return new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build());
  }

  /**
   * Give the server a new key pair and a certificate for it, as a renewal with a new key does.
   *
   * @return the server's new certificate.
   */
  private X509Certificate installNewServerCertificate() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
            .setCommonName("certificate renewal test server")
            .setApplicationUri(SERVER_URI)
            .addDnsName("localhost")
            .build();

    certificateGroup.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType, keyPair, new X509Certificate[] {certificate});

    return certificate;
  }

  private static EndpointDescription discoverEndpoint(String endpointUrl) throws Exception {
    return DiscoveryClient.getEndpoints(endpointUrl).get(10, TimeUnit.SECONDS).get(0);
  }

  /** Create a client that connects with the endpoint discovered before the renewal. */
  private OpcUaClient createClient(
      IdentityProvider identityProvider, CertificateValidator certificateValidator)
      throws UaException {

    return OpcUaClient.create(
        OpcUaClientConfig.builder()
            .setEndpoint(cachedEndpoint)
            .setIdentityProvider(identityProvider)
            .setCertificateValidator(certificateValidator)
            .setRequestTimeout(uint(5_000))
            .build());
  }

  private long rejectedSessionCount() {
    return server.getDiagnosticsSummary().getRejectedSessionCount().sum();
  }

  private static CertificateValidator validatorTrusting(X509Certificate... certificates) {
    var trustListManager = new MemoryTrustListManager();
    for (X509Certificate certificate : certificates) {
      trustListManager.addTrustedCertificate(certificate);
    }

    return new DefaultClientCertificateValidator(
        trustListManager, new MemoryCertificateQuarantine());
  }
}
