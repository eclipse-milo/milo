/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that any Session the Server created is closed when the client refuses the {@link
 * CreateSessionResponse} during client-side verification (OPC 10000-4 §5.7.2.1).
 *
 * <p>Previously {@code KEY_PENDING_SESSION} was only recorded on entry to {@link State#Activating},
 * after {@code verifyCreateSessionResponse} succeeded. Refused responses left unactivated Sessions
 * lingering on the Server until their session timeout expired, and retries from {@link
 * State#CreatingWait} piled up additional unactivated Sessions.
 */
class RefusedCreateSessionCloseTest {

  private TestServer testServer;
  private OpcUaServer server;
  private RecordingSessionServiceSet sessionServiceSet;
  private List<EndpointDescription> discovered;

  private final LinkedBlockingQueue<NodeId> createdSessions = new LinkedBlockingQueue<>();
  private final Set<NodeId> allCreatedSessionIds = ConcurrentHashMap.newKeySet();
  private final ConcurrentHashMap<NodeId, CompletableFuture<Void>> closedFutures =
      new ConcurrentHashMap<>();

  @BeforeEach
  void startServer() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();
    sessionServiceSet = new RecordingSessionServiceSet(server);

    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), sessionServiceSet);
    }

    server
        .getSessionManager()
        .addSessionListener(
            new SessionListener() {
              @Override
              public void onSessionCreated(Session session) {
                NodeId sessionId = session.getSessionId();
                allCreatedSessionIds.add(sessionId);
                createdSessions.add(sessionId);
              }

              @Override
              public void onSessionClosed(Session session) {
                closedFuture(session.getSessionId()).complete(null);
              }
            });

    server.startup().get(10, SECONDS);

    String endpointUrl = server.getConfig().getEndpoints().iterator().next().getEndpointUrl();
    discovered = DiscoveryClient.getEndpoints(endpointUrl).get(10, SECONDS);
  }

  @AfterEach
  void stopServer() throws Exception {
    server.shutdown().get(10, SECONDS);
  }

  // OPC 10000-4 §5.7.2.1: if the CreateSession EndpointDescriptions differ from those returned
  // during discovery, the Client shall close the Session and report an error. Retries from
  // CreatingWait continue, and every refused Session must be closed with none left on the Server.
  @Test
  void refusedEndpointMismatchClosesEveryRefusedSessionOnServer() throws Exception {
    List<EndpointDescription> stripped =
        discovered.stream()
            .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
            .toList();

    OpcUaClient client = createClient(noneEndpoint(), stripped, builder -> {});

    try {
      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> client.connectAsync().get(10, SECONDS));
      UaException cause = assertInstanceOf(UaException.class, failure.getCause());
      assertEquals(StatusCodes.Bad_SecurityChecksFailed, cause.getStatusCode().value());

      NodeId firstSessionId = createdSessions.poll(10, SECONDS);
      assertNotNull(firstSessionId, "first CreateSession should have reached the Server");
      closedFuture(firstSessionId).get(10, SECONDS);

      // Verify CreatingWait retry behavior is preserved and the retried refusal is closed too.
      NodeId secondSessionId = createdSessions.poll(10, SECONDS);
      assertNotNull(secondSessionId, "CreatingWait should have retried CreateSession");
      closedFuture(secondSessionId).get(10, SECONDS);
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }

    awaitAllCreatedSessionsClosed();

    assertEquals(0, sessionServiceSet.activated.get(), "refused Sessions must never be activated");
    assertTrue(
        sessionServiceSet.created.get() >= 2,
        "at least the initial attempt and one retry should have run");
    assertEveryCreatedSessionClosedExactlyOnce();
  }

  // OPC 10000-4 §5.7.2.1: a caller-supplied discovery endpoint with server == null must fail with
  // Bad_SecurityChecksFailed rather than NullPointerException, and still close the refused Session.
  @Test
  void discoveryEndpointWithNullServerRefusesWithBadSecurityChecksFailedAndClosesSession()
      throws Exception {

    List<EndpointDescription> withoutServer =
        discovered.stream()
            .map(
                e ->
                    new EndpointDescription(
                        e.getEndpointUrl(),
                        null,
                        e.getServerCertificate(),
                        e.getSecurityMode(),
                        e.getSecurityPolicyUri(),
                        e.getUserIdentityTokens(),
                        e.getTransportProfileUri(),
                        e.getSecurityLevel()))
            .toList();

    OpcUaClient client = createClient(noneEndpoint(), withoutServer, builder -> {});

    try {
      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> client.connectAsync().get(10, SECONDS));
      UaException cause = assertInstanceOf(UaException.class, failure.getCause());
      assertEquals(
          StatusCodes.Bad_SecurityChecksFailed,
          cause.getStatusCode().value(),
          "server == null in discovery endpoints must produce Bad_SecurityChecksFailed");

      NodeId firstSessionId = createdSessions.poll(10, SECONDS);
      assertNotNull(firstSessionId, "CreateSession should have reached the Server");
      closedFuture(firstSessionId).get(10, SECONDS);
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }

    awaitAllCreatedSessionsClosed();

    assertEquals(0, sessionServiceSet.activated.get());
    assertEveryCreatedSessionClosedExactlyOnce();
  }

  // OPC 10000-4 §5.7.2.1: when serverSignature verification fails on a secured CreateSession, the
  // client must close the newly created Session on the Server rather than leaving it unactivated.
  @Test
  void invalidServerSignatureClosesRefusedSessionOnServer() throws Exception {
    sessionServiceSet.setResponseTransform(
        response -> {
          byte[] signatureBytes = response.getServerSignature().getSignature().bytesOrEmpty();
          byte[] corrupted = signatureBytes.clone();
          if (corrupted.length > 0) {
            corrupted[0] ^= 0xFF;
          } else {
            corrupted = new byte[] {0x01};
          }

          return new CreateSessionResponse(
              response.getResponseHeader(),
              response.getSessionId(),
              response.getAuthenticationToken(),
              response.getRevisedSessionTimeout(),
              response.getServerNonce(),
              response.getServerCertificate(),
              response.getServerEndpoints(),
              response.getServerSoftwareCertificates(),
              new SignatureData(
                  response.getServerSignature().getAlgorithm(), ByteString.of(corrupted)),
              response.getMaxRequestMessageSize());
        });

    OpcUaClient client = createSecuredClient(securedEndpoint(), discovered, builder -> {});

    try {
      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> client.connectAsync().get(10, SECONDS));
      UaException cause = assertInstanceOf(UaException.class, failure.getCause());
      assertEquals(StatusCodes.Bad_SecurityChecksFailed, cause.getStatusCode().value());

      NodeId firstSessionId = createdSessions.poll(10, SECONDS);
      assertNotNull(firstSessionId, "CreateSession should have reached the Server");
      closedFuture(firstSessionId).get(10, SECONDS);
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }

    awaitAllCreatedSessionsClosed();

    assertEquals(0, sessionServiceSet.activated.get());
    assertEveryCreatedSessionClosedExactlyOnce();
  }

  // Cleanup of a received CreateSessionResponse must not depend on verification throwing a
  // UaException: an unchecked runtime exception (e.g. from a custom CertificateValidator) must
  // still close the Session on the Server.
  @Test
  void uncheckedExceptionDuringCreateSessionVerificationStillClosesSessionOnServer()
      throws Exception {

    var validatorFailure = new IllegalStateException("simulated validator failure");
    CertificateValidator failingOnCreateSessionValidator =
        new CertificateValidator() {
          @Override
          public void validateCertificateChain(
              List<X509Certificate> certificateChain,
              @Nullable String applicationUri,
              String @Nullable [] validHostnames) {}

          @Override
          public void validateCertificateChain(
              List<X509Certificate> certificateChain,
              @Nullable String applicationUri,
              String @Nullable [] validHostnames,
              @Nullable SecurityPolicyProfile securityPolicyProfile) {
            if (sessionServiceSet.created.get() > 0) {
              throw validatorFailure;
            }
          }
        };

    OpcUaClient client =
        createSecuredClient(
            securedEndpoint(),
            discovered,
            builder -> builder.setCertificateValidator(failingOnCreateSessionValidator));

    try {
      ExecutionException failure =
          assertThrows(ExecutionException.class, () -> client.connectAsync().get(10, SECONDS));
      Throwable cause = failure.getCause();
      while (cause != null && cause != validatorFailure) {
        cause = cause.getCause();
      }
      assertEquals(validatorFailure, cause, "validator runtime failure should surface to caller");

      NodeId firstSessionId = createdSessions.poll(10, SECONDS);
      assertNotNull(firstSessionId, "CreateSession should have reached the Server");
      closedFuture(firstSessionId).get(10, SECONDS);
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }

    awaitAllCreatedSessionsClosed();

    assertEquals(0, sessionServiceSet.activated.get());
    assertEveryCreatedSessionClosedExactlyOnce();
  }

  // Control: an accepted CreateSession activates normally and is closed exactly once on
  // disconnect (no premature or double CloseSession).
  @Test
  void acceptedSessionIsActivatedAndClosedOnceOnDisconnect() throws Exception {
    OpcUaClient client = createClient(noneEndpoint(), discovered, builder -> {});

    try {
      client.connectAsync().get(10, SECONDS);

      NodeId sessionId = createdSessions.poll(10, SECONDS);
      assertNotNull(sessionId);
      assertEquals(1, sessionServiceSet.created.get());
      assertEquals(1, sessionServiceSet.activated.get());
      assertEquals(0, sessionServiceSet.closeRequests.get());
      assertEquals(0, sessionServiceSet.closed.get());
      assertEquals(1, server.getSessionManager().getAllSessions().size());
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }

    awaitAllCreatedSessionsClosed();

    assertEveryCreatedSessionClosedExactlyOnce();
  }

  private void assertEveryCreatedSessionClosedExactlyOnce() {
    assertEquals(
        sessionServiceSet.created.get(),
        sessionServiceSet.closeRequests.get(),
        "every created Session must receive exactly one incoming CloseSessionRequest");
    assertEquals(
        sessionServiceSet.created.get(),
        sessionServiceSet.closed.get(),
        "every created Session must close successfully on the Server");
    for (NodeId authToken : sessionServiceSet.createdAuthTokens) {
      assertEquals(
          1,
          sessionServiceSet.closeRequestCount(authToken),
          "expected exactly one CloseSessionRequest for authToken=" + authToken);
    }
    assertEquals(
        0,
        server.getSessionManager().getAllSessions().size(),
        "no Session may remain on the Server");
  }

  private void awaitAllCreatedSessionsClosed() throws Exception {
    for (NodeId sessionId : allCreatedSessionIds) {
      closedFuture(sessionId).get(10, SECONDS);
    }
    for (NodeId authToken : sessionServiceSet.createdAuthTokens) {
      sessionServiceSet.closeCompletedFuture(authToken).get(10, SECONDS);
    }
  }

  private CompletableFuture<Void> closedFuture(NodeId sessionId) {
    return closedFutures.computeIfAbsent(sessionId, id -> new CompletableFuture<>());
  }

  private EndpointDescription noneEndpoint() {
    return discovered.stream()
        .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
        .findFirst()
        .orElseThrow();
  }

  private EndpointDescription securedEndpoint() {
    return discovered.stream()
        .filter(
            e ->
                SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri())
                    && e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
        .findFirst()
        .orElseThrow();
  }

  private OpcUaClient createClient(
      EndpointDescription endpoint,
      List<EndpointDescription> discoveryEndpoints,
      Consumer<OpcUaClientConfigBuilder> customize) {

    OpcUaClientConfigBuilder builder =
        OpcUaClientConfig.builder()
            .setApplicationName(LocalizedText.english("eclipse milo test client"))
            .setApplicationUri("urn:eclipse:milo:test:client")
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(discoveryEndpoints)
            .setSessionEndpointValidationEnabled(true);

    customize.accept(builder);

    return new OpcUaClient(
        builder.build(),
        new OpcTcpClientTransport(OpcTcpClientTransportConfig.newBuilder().build()));
  }

  private OpcUaClient createSecuredClient(
      EndpointDescription endpoint,
      List<EndpointDescription> discoveryEndpoints,
      Consumer<OpcUaClientConfigBuilder> customize) {

    return createClient(
        endpoint,
        discoveryEndpoints,
        builder -> {
          builder
              .setApplicationUri("urn:eclipse:milo:test:reverse:client")
              .setCertificateGroup(
                  DefaultCertificateGroup.forIdentity(
                      testServer.getClientKeyPair(),
                      testServer.getClientCertificateChain(),
                      new MemoryTrustListManager(),
                      new MemoryCertificateQuarantine(),
                      new CertificateValidator.InsecureCertificateValidator()))
              .setCertificateValidator(new CertificateValidator.InsecureCertificateValidator());
          customize.accept(builder);
        });
  }

  /**
   * Counts Session service calls answered by the Server (plus every incoming {@link
   * CloseSessionRequest} before delegation so duplicate close attempts are detected even if the
   * Server rejects them) and optionally transforms {@link CreateSessionResponse} after the Server
   * has created the Session.
   */
  private static final class RecordingSessionServiceSet extends DelegatingSessionServiceSet {

    final AtomicInteger created = new AtomicInteger();
    final AtomicInteger activated = new AtomicInteger();
    final AtomicInteger closeRequests = new AtomicInteger();
    final AtomicInteger closed = new AtomicInteger();
    final Set<NodeId> createdAuthTokens = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<NodeId, AtomicInteger> closeRequestsByAuthToken =
        new ConcurrentHashMap<>();
    private final ConcurrentHashMap<NodeId, CompletableFuture<Void>> closeCompletedByAuthToken =
        new ConcurrentHashMap<>();

    private volatile UnaryOperator<CreateSessionResponse> responseTransform =
        UnaryOperator.identity();

    RecordingSessionServiceSet(OpcUaServer server) {
      super(server);
    }

    void setResponseTransform(UnaryOperator<CreateSessionResponse> responseTransform) {
      this.responseTransform = responseTransform;
    }

    int closeRequestCount(NodeId authToken) {
      AtomicInteger count = closeRequestsByAuthToken.get(authToken);
      return count != null ? count.get() : 0;
    }

    CompletableFuture<Void> closeCompletedFuture(NodeId authToken) {
      return closeCompletedByAuthToken.computeIfAbsent(
          authToken, token -> new CompletableFuture<>());
    }

    @Override
    public CreateSessionResponse onCreateSession(
        ServiceRequestContext context, CreateSessionRequest request) throws UaException {

      CreateSessionResponse response = super.onCreateSession(context, request);
      createdAuthTokens.add(response.getAuthenticationToken());
      created.incrementAndGet();
      return responseTransform.apply(response);
    }

    @Override
    public ActivateSessionResponse onActivateSession(
        ServiceRequestContext context, ActivateSessionRequest request) throws UaException {

      // Count incoming requests before delegation so a rejected ActivateSessionRequest is still
      // observed.
      activated.incrementAndGet();
      return super.onActivateSession(context, request);
    }

    @Override
    public CloseSessionResponse onCloseSession(
        ServiceRequestContext context, CloseSessionRequest request) throws UaException {

      // Count incoming requests before delegation so a duplicate CloseSessionRequest rejected by
      // super.onCloseSession() is still observed.
      closeRequests.incrementAndGet();
      NodeId authToken = request.getRequestHeader().getAuthenticationToken();
      if (authToken != null) {
        closeRequestsByAuthToken
            .computeIfAbsent(authToken, token -> new AtomicInteger())
            .incrementAndGet();
      }

      CloseSessionResponse response = super.onCloseSession(context, request);
      closed.incrementAndGet();
      if (authToken != null) {
        closeCompletedFuture(authToken).complete(null);
      }
      return response;
    }
  }
}
