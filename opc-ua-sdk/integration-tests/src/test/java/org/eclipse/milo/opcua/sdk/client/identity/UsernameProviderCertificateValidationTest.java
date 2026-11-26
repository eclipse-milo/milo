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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Part 4 (7.41): a client shall only encrypt a USERNAME token to a ServerCertificate it trusts.
 *
 * <p>The client connects to a {@code None} endpoint, where the SecureChannel checks no certificate,
 * and the username token policy encrypts the password to the endpoint's certificate. Unless a
 * validator trusts that certificate, the server stands for one the client never meant to reach, so
 * the tests look at which usernames the server receives.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsernameProviderCertificateValidationTest {

  private final List<String> usernamesReceived = new CopyOnWriteArrayList<>();

  private OpcUaServer server;
  private String endpointUrl;
  private X509Certificate serverCertificate;

  @BeforeAll
  void startServer() throws Exception {
    TestServer testServer =
        TestServer.create(
            builder ->
                builder.setIdentityValidator(
                    new UsernameIdentityValidator(
                        challenge -> {
                          usernamesReceived.add(challenge.getUsername());
                          return "password".equals(challenge.getPassword());
                        })));

    server = testServer.getServer();

    EndpointConfig endpoint = server.getConfig().getEndpoints().iterator().next();
    endpointUrl = endpoint.getEndpointUrl();
    serverCertificate = endpoint.getCertificate();

    server.startup().get(10, TimeUnit.SECONDS);
  }

  @AfterAll
  void stopServer() throws Exception {
    if (server != null) {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  @BeforeEach
  void clearUsernamesReceived() {
    usernamesReceived.clear();
  }

  // A client that set a strict validator must not send its password to a server that validator
  // does not trust just because the endpoint is None and the provider was built without one. The
  // refusal must come before the ActivateSession is sent, not only before the server can read it.
  @Test
  void providerWithoutValidatorRefusesCertificateTheClientValidatorDoesNotTrust() throws Exception {

    OpcUaClient client =
        createClient(
            builder ->
                builder
                    .setCertificateValidator(validatorTrusting(List.of()))
                    .setIdentityProvider(new UsernameProvider("user1", "password")));

    long rejectedSessionsBefore = rejectedSessionCount();

    try {
      UaException failure = assertThrows(UaException.class, client::connect);

      // An ActivateSession that reached the server either passed the identity validator, which
      // records the username, or was rejected, which counts a rejected session.
      assertAll(
          () -> assertEquals(StatusCodes.Bad_SecurityChecksFailed, failure.getStatusCode().value()),
          () ->
              assertEquals(
                  rejectedSessionsBefore,
                  rejectedSessionCount(),
                  "the server rejected an ActivateSession; the client must send none"),
          () ->
              assertEquals(
                  List.of(), usernamesReceived, "the server must receive no username token"));
    } finally {
      client.disconnect();
    }
  }

  // The client's validator must accept a server it trusts, or a client that set a validator could
  // no longer sign in with a username on a None endpoint at all.
  @Test
  void providerWithoutValidatorAcceptsCertificateTheClientValidatorTrusts() throws Exception {
    OpcUaClient client =
        createClient(
            builder ->
                builder
                    .setCertificateValidator(validatorTrusting(List.of(serverCertificate)))
                    .setIdentityProvider(new UsernameProvider("user1", "password")));

    try {
      client.connect();

      assertEquals(List.of("user1"), usernamesReceived);
    } finally {
      client.disconnect();
    }
  }

  // A validator passed to the provider is the application's choice for this check, so it takes the
  // place of the client's validator rather than adding to it.
  @Test
  void providerValidatorTakesPrecedenceOverClientValidator() throws Exception {
    OpcUaClient client =
        createClient(
            builder ->
                builder
                    .setCertificateValidator(validatorTrusting(List.of()))
                    .setIdentityProvider(
                        new UsernameProvider(
                            "user1", "password", validatorTrusting(List.of(serverCertificate)))));

    try {
      client.connect();

      assertEquals(List.of("user1"), usernamesReceived);
    } finally {
      client.disconnect();
    }
  }

  // A client that sets neither a validator nor a certificate group does not validate server
  // certificates, as setCertificateValidator documents. The provider keeps encrypting to the
  // endpoint's certificate in that case instead of failing closed.
  @Test
  void clientWithoutValidatorStillSendsPasswordToUnvalidatedCertificate() throws Exception {
    OpcUaClient client =
        createClient(
            builder -> builder.setIdentityProvider(new UsernameProvider("user1", "password")));

    try {
      client.connect();

      assertEquals(List.of("user1"), usernamesReceived);
    } finally {
      client.disconnect();
    }
  }

  private OpcUaClient createClient(Consumer<OpcUaClientConfigBuilder> configure)
      throws UaException {

    return OpcUaClient.create(
        endpointUrl,
        endpoints ->
            endpoints.stream()
                .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
                .findFirst(),
        transportBuilder -> {},
        builder -> {
          builder.setRequestTimeout(uint(5_000));
          configure.accept(builder);
        });
  }

  private long rejectedSessionCount() {
    return server.getDiagnosticsSummary().getRejectedSessionCount().sum();
  }

  private static CertificateValidator validatorTrusting(List<X509Certificate> certificates) {
    var trustListManager = new MemoryTrustListManager();
    certificates.forEach(trustListManager::addTrustedCertificate);

    return new DefaultClientCertificateValidator(
        trustListManager, new MemoryCertificateQuarantine());
  }
}
