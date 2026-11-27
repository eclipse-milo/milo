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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener.AttemptStep;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

public class SessionValidationTest extends AbstractClientServerTest {

  private static final String EXPECTED_MISMATCH_MESSAGE =
      "endpoints returned during discovery do not match session endpoints";

  private record AttemptFailure(AttemptStep step, Throwable failure) {}

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder configBuilder) {
    configBuilder.setSessionEndpointValidationEnabled(true);
  }

  @Test
  void clientConnectsWithSessionValidationEnabled() throws UaException {
    assertNotNull(client.connect());
    assertNotNull(client.getSession());
  }

  // Callers using blocking connect() or SessionActivityListener need to distinguish an endpoint
  // list mismatch from other CreateSession Bad_SecurityChecksFailed errors without parsing
  // exception messages.
  @Test
  void endpointMismatchSurfacesSessionEndpointMismatchExceptionOnConnectAndListener()
      throws Exception {

    OpcUaClient mismatchedClient = createMismatchedClient();
    var attemptFailureFuture = new CompletableFuture<AttemptFailure>();

    mismatchedClient.addSessionActivityListener(
        new SessionActivityListener() {
          @Override
          public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
            attemptFailureFuture.complete(new AttemptFailure(step, failure));
          }
        });

    try {
      UaException connectFailure = assertThrows(UaException.class, mismatchedClient::connect);

      assertEquals(StatusCodes.Bad_SecurityChecksFailed, connectFailure.getStatusCode().value());

      SessionEndpointMismatchException extracted =
          UaException.extract(connectFailure, SessionEndpointMismatchException.class).orElseThrow();
      assertEquals(StatusCodes.Bad_SecurityChecksFailed, extracted.getStatusCode().value());
      assertEquals(EXPECTED_MISMATCH_MESSAGE, extracted.getMessage());

      AttemptFailure attemptFailure = attemptFailureFuture.get(10, SECONDS);
      assertEquals(AttemptStep.CREATE_SESSION, attemptFailure.step());

      SessionEndpointMismatchException listenerFailure =
          assertInstanceOf(SessionEndpointMismatchException.class, attemptFailure.failure());
      assertEquals(StatusCodes.Bad_SecurityChecksFailed, listenerFailure.getStatusCode().value());
      assertEquals(EXPECTED_MISMATCH_MESSAGE, listenerFailure.getMessage());
      assertSame(
          listenerFailure,
          UaException.extract(attemptFailure.failure(), SessionEndpointMismatchException.class)
              .orElseThrow());
    } finally {
      mismatchedClient.disconnectAsync().get(10, SECONDS);
    }
  }

  // connectAsync() completes exceptionally with SessionEndpointMismatchException as its cause so
  // both direct inspection and UaException.extract(...) work consistently.
  @Test
  void endpointMismatchSurfacesSessionEndpointMismatchExceptionOnConnectAsync() throws Exception {
    OpcUaClient mismatchedClient = createMismatchedClient();

    try {
      ExecutionException connectFailure =
          assertThrows(
              ExecutionException.class, () -> mismatchedClient.connectAsync().get(10, SECONDS));

      SessionEndpointMismatchException cause =
          assertInstanceOf(SessionEndpointMismatchException.class, connectFailure.getCause());
      assertEquals(StatusCodes.Bad_SecurityChecksFailed, cause.getStatusCode().value());
      assertEquals(EXPECTED_MISMATCH_MESSAGE, cause.getMessage());
      assertSame(
          cause,
          UaException.extract(connectFailure, SessionEndpointMismatchException.class)
              .orElseThrow());
    } finally {
      mismatchedClient.disconnectAsync().get(10, SECONDS);
    }
  }

  // Negative control: other CreateSession security failures that carry Bad_SecurityChecksFailed
  // (such as a missing server certificate in CreateSessionResponse on a secured endpoint) must
  // remain plain UaException and never surface SessionEndpointMismatchException.
  @Test
  void otherCreateSessionSecurityFailureDoesNotContainSessionEndpointMismatchException()
      throws Exception {

    TestServer localTestServer = TestServer.create();
    OpcUaServer localServer = localTestServer.getServer();

    var sessionServiceSet =
        new DelegatingSessionServiceSet(localServer) {
          @Override
          public CreateSessionResponse onCreateSession(
              ServiceRequestContext context, CreateSessionRequest request) throws UaException {

            CreateSessionResponse response = super.onCreateSession(context, request);

            return new CreateSessionResponse(
                response.getResponseHeader(),
                response.getSessionId(),
                response.getAuthenticationToken(),
                response.getRevisedSessionTimeout(),
                response.getServerNonce(),
                ByteString.NULL_VALUE,
                response.getServerEndpoints(),
                response.getServerSoftwareCertificates(),
                response.getServerSignature(),
                response.getMaxRequestMessageSize());
          }
        };

    for (EndpointConfig endpoint : localServer.getConfig().getEndpoints()) {
      localServer.addServiceSet(endpoint.getPath(), sessionServiceSet);
    }

    localServer.startup().get(10, SECONDS);

    OpcUaClient securedClient = null;
    try {
      EndpointConfig secured =
          localServer.getConfig().getEndpoints().stream()
              .filter(e -> e.getSecurityPolicy() == SecurityPolicy.Basic256Sha256)
              .findFirst()
              .orElseThrow();

      var serverTrustList = new MemoryTrustListManager();
      serverTrustList.addTrustedCertificate(secured.getCertificate());

      String clientApplicationUri =
          CertificateUtil.getSanUri(localTestServer.getClientCertificate()).orElseThrow();

      securedClient =
          OpcUaClient.create(
              secured.getEndpointUrl(),
              endpoints ->
                  endpoints.stream()
                      .filter(
                          e ->
                              Objects.equals(
                                      e.getSecurityPolicyUri(),
                                      SecurityPolicy.Basic256Sha256.getUri())
                                  && e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                      .findFirst(),
              transportConfig -> {},
              config ->
                  config
                      .setApplicationUri(clientApplicationUri)
                      .setCertificateIdentity(
                          localTestServer.getClientKeyPair(),
                          localTestServer.getClientCertificateChain())
                      .setCertificateValidator(
                          new DefaultClientCertificateValidator(
                              serverTrustList, new MemoryCertificateQuarantine()))
                      .setSessionEndpointValidationEnabled(true)
                      .setRequestTimeout(uint(5_000)));

      var attemptFailureFuture = new CompletableFuture<AttemptFailure>();
      securedClient.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
              attemptFailureFuture.complete(new AttemptFailure(step, failure));
            }
          });

      UaException connectFailure = assertThrows(UaException.class, securedClient::connect);

      assertEquals(StatusCodes.Bad_SecurityChecksFailed, connectFailure.getStatusCode().value());
      assertTrue(
          UaException.extract(connectFailure, SessionEndpointMismatchException.class).isEmpty(),
          "missing server certificate must not surface SessionEndpointMismatchException");

      AttemptFailure attemptFailure = attemptFailureFuture.get(10, SECONDS);
      assertEquals(AttemptStep.CREATE_SESSION, attemptFailure.step());
      assertEquals(
          UaException.class,
          attemptFailure.failure().getClass(),
          "missing server certificate must remain a plain UaException");
      assertEquals(
          StatusCodes.Bad_SecurityChecksFailed,
          UaException.extractStatusCode(attemptFailure.failure()).orElseThrow().value());
      assertTrue(
          UaException.extract(attemptFailure.failure(), SessionEndpointMismatchException.class)
              .isEmpty(),
          "listener failure for missing server certificate must not contain"
              + " SessionEndpointMismatchException");
    } finally {
      try {
        if (securedClient != null) {
          securedClient.disconnectAsync().get(10, SECONDS);
        }
      } finally {
        localServer.shutdown().get(10, SECONDS);
      }
    }
  }

  private OpcUaClient createMismatchedClient() throws UaException {
    List<EndpointDescription> staleDiscoveryEndpoints =
        client.getConfig().getDiscoveryEndpoints().stream()
            .map(e -> EndpointUtil.updateUrl(e, "stale-discovery-host"))
            .toList();

    OpcUaClientConfig mismatchedConfig =
        OpcUaClientConfig.copy(
            client.getConfig(),
            builder ->
                builder
                    .setDiscoveryEndpoints(staleDiscoveryEndpoints)
                    .setSessionEndpointValidationEnabled(true));

    return OpcUaClient.create(mismatchedConfig);
  }
}
