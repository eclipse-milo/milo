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

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.channel.Channel;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.DelegatingSubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

/**
 * Each failed attempt to establish or recover a Session is reported with the step that failed, to
 * {@link SessionActivityListener#onSessionAttemptFailed(SessionActivityListener.AttemptStep,
 * Throwable)}.
 *
 * <p>The failure alone does not say which request the Server refused: a refused request arrives as
 * a {@code UaServiceFaultException} that carries only a StatusCode, and a Server can answer
 * different requests with the same one. An application that decides whether retrying can succeed,
 * or what to tell its user, needs the step as well.
 *
 * <p>Each test makes Milo's Server refuse one request once, so the attempt after it succeeds.
 * Listeners hear about that Session after anything reported before it, so its {@code
 * onSessionActive} shows, without waiting for a timeout, that nothing else was reported.
 */
class SessionAttemptStepTest {

  // A Server can refuse a client application it does not trust at CreateSession, with
  // Bad_CertificateUntrusted, instead of refusing its SecureChannel.
  @Test
  void refusedCreateSessionIsReportedAsCreateSession() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.createSession.refuseNext(StatusCodes.Bad_CertificateUntrusted);

      assertThrows(UaException.class, fixture.client::connect);

      assertEquals(
          List.of(
              "onSessionAttemptFailed(CREATE_SESSION, UaServiceFaultException,"
                  + " Bad_CertificateUntrusted)",
              "onSessionActive"),
          listener.next(2));
    }
  }

  @Test
  void refusedActivateSessionIsReportedAsActivateSession() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.activateSession.refuseNext(StatusCodes.Bad_UserAccessDenied);

      assertThrows(UaException.class, fixture.client::connect);

      assertEquals(
          List.of(
              "onSessionAttemptFailed(ACTIVATE_SESSION, UaServiceFaultException,"
                  + " Bad_UserAccessDenied)",
              "onSessionActive"),
          listener.next(2));
    }
  }

  // A reactivation sends ActivateSession too. Reporting it as its own step tells the application
  // that the Session it had was refused, not one it had just created.
  @Test
  void refusedReactivationIsReportedAsReactivateSession() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.client.connect();

      fixture.activateSession.refuseNext(StatusCodes.Bad_UserAccessDenied);
      fixture.loseConnection();

      assertEquals(
          List.of(
              "onSessionActive",
              "onSessionInactive",
              "onSessionAttemptFailed(REACTIVATE_SESSION, UaServiceFaultException,"
                  + " Bad_UserAccessDenied)",
              "onSessionActive"),
          listener.next(4),
          "the refused reactivation must be reported, and the new Session that replaces it"
              + " activated");
    }
  }

  // The client transfers its Subscriptions only to a new Session, so the Server refuses the
  // reactivation first, which makes the next attempt create one.
  @Test
  void refusedTransferSubscriptionsIsReportedAsTransferSubscriptions() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.client.connect();
      new OpcUaSubscription(fixture.client).create();

      fixture.activateSession.refuseNext(StatusCodes.Bad_SessionIdInvalid);
      fixture.transferSubscriptions.refuseNext(StatusCodes.Bad_TooManyOperations);
      fixture.loseConnection();

      assertEquals(
          List.of(
              "onSessionActive",
              "onSessionInactive",
              "onSessionAttemptFailed(REACTIVATE_SESSION, UaServiceFaultException,"
                  + " Bad_SessionIdInvalid)",
              "onSessionAttemptFailed(TRANSFER_SUBSCRIPTIONS, UaServiceFaultException,"
                  + " Bad_TooManyOperations)",
              "onSessionActive"),
          listener.next(5));
    }
  }

  // An initializer whose future fails is logged and ignored; one that throws fails the step.
  @Test
  void failedInitializationIsReportedAsInitialize() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      var throwOnce = new AtomicBoolean(true);
      fixture.client.addSessionInitializer(
          (client, session) -> {
            if (throwOnce.getAndSet(false)) {
              throw new IllegalStateException("thrown by the test's initializer");
            }
            return completedFuture(Unit.VALUE);
          });

      assertThrows(UaException.class, fixture.client::connect);

      assertEquals(
          List.of(
              "onSessionAttemptFailed(INITIALIZE, IllegalStateException, no StatusCode)",
              "onSessionActive"),
          listener.next(2));
    }
  }

  // CreateSession is the first request that needs a SecureChannel, so a SecureChannel the Server
  // refuses fails that step. The handshake exception in the cause chain tells it apart from a
  // CreateSession the Server refused, which can carry the same StatusCode.
  @Test
  void refusedSecureChannelIsReportedAsCreateSession() throws Exception {
    try (var fixture = new UntrustedClientFixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      UaException connectFailure = assertThrows(UaException.class, fixture.client::connect);

      assertEquals(
          List.of("onSessionAttemptFailed(CREATE_SESSION, handshake, Bad_SecurityChecksFailed)"),
          listener.next(1),
          "connect() failed with: " + connectFailure);
    }
  }

  // A listener written before the step was reported overrides only the method without it. It must
  // still hear about every failed attempt, with the same failure.
  @Test
  void listenerThatOverridesOnlyTheFailureFormHearsEveryFailedAttempt() throws Exception {
    try (var fixture = new Fixture()) {
      var failures = new LinkedBlockingQueue<String>();
      fixture.client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionAttemptFailed(Throwable failure) {
              failures.add(RecordingListener.describe(failure));
            }
          });
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.activateSession.refuseNext(StatusCodes.Bad_UserAccessDenied);

      assertThrows(UaException.class, fixture.client::connect);

      assertEquals(
          List.of(
              "onSessionAttemptFailed(ACTIVATE_SESSION, UaServiceFaultException,"
                  + " Bad_UserAccessDenied)",
              "onSessionActive"),
          listener.next(2));

      // Each notification reaches the listeners in the order they were added, so by the time the
      // second one heard of the Session, the first has heard of every failure before it.
      assertEquals(
          List.of("UaServiceFaultException, Bad_UserAccessDenied"),
          new ArrayList<>(failures),
          "a listener overriding only onSessionAttemptFailed(Throwable) must hear the attempt");
    }
  }

  // region helpers

  /** Records each callback it receives, in the order they arrive. */
  private static final class RecordingListener implements SessionActivityListener {

    private final BlockingQueue<String> events = new LinkedBlockingQueue<>();

    @Override
    public void onSessionActive(UaSession session) {
      events.add("onSessionActive");
    }

    @Override
    public void onSessionInactive(UaSession session) {
      events.add("onSessionInactive");
    }

    @Override
    public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
      events.add("onSessionAttemptFailed(%s, %s)".formatted(step, describe(failure)));
    }

    /**
     * Describe {@code failure} by its type, or by {@code handshake} when a {@link
     * SecureChannelHandshakeException} is in its cause chain, and by its StatusCode.
     */
    static String describe(Throwable failure) {
      String type = failure.getClass().getSimpleName();

      for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
        if (cause instanceof SecureChannelHandshakeException) {
          type = "handshake";
          break;
        }
      }

      String status =
          UaException.extractStatusCode(failure)
              .flatMap(statusCode -> StatusCodes.lookup(statusCode.value()))
              .map(nameAndDescription -> nameAndDescription[0])
              .orElse("no StatusCode");

      return "%s, %s".formatted(type, status);
    }

    /**
     * Take the next {@code count} events, waiting up to 10 seconds for each; an event that doesn't
     * arrive in time is {@code null}.
     */
    List<String> next(int count) throws InterruptedException {
      var next = new ArrayList<String>();

      while (next.size() < count) {
        next.add(events.poll(10, SECONDS));
      }

      return next;
    }
  }

  /** Makes the Server refuse the next request of one kind with a StatusCode the test chooses. */
  private static final class Refusal {

    private final AtomicReference<Long> next = new AtomicReference<>();

    /** Refuse the next request, and only that one, with {@code statusCode}. */
    void refuseNext(long statusCode) {
      next.set(statusCode);
    }

    void throwIfRefused() throws UaException {
      Long statusCode = next.getAndSet(null);

      if (statusCode != null) {
        throw new UaException(statusCode, "refused by the test");
      }
    }
  }

  /**
   * A running Server whose Session and Subscription services the test can make refuse a request,
   * and a client, not yet connected, that logs in anonymously.
   */
  private static final class Fixture implements AutoCloseable {

    final Refusal createSession = new Refusal();
    final Refusal activateSession = new Refusal();
    final Refusal transferSubscriptions = new Refusal();

    final OpcUaServer server;
    final OpcUaClient client;

    Fixture() throws Exception {
      server = TestServer.create().getServer();

      var sessionServiceSet =
          new DelegatingSessionServiceSet(server) {
            @Override
            public CreateSessionResponse onCreateSession(
                ServiceRequestContext context, CreateSessionRequest request) throws UaException {

              createSession.throwIfRefused();
              return super.onCreateSession(context, request);
            }

            @Override
            public ActivateSessionResponse onActivateSession(
                ServiceRequestContext context, ActivateSessionRequest request) throws UaException {

              activateSession.throwIfRefused();
              return super.onActivateSession(context, request);
            }
          };

      var subscriptionServiceSet =
          new DelegatingSubscriptionServiceSet(server) {
            @Override
            public TransferSubscriptionsResponse onTransferSubscriptions(
                ServiceRequestContext context, TransferSubscriptionsRequest request)
                throws UaException {

              transferSubscriptions.throwIfRefused();
              return super.onTransferSubscriptions(context, request);
            }
          };

      for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
        server.addServiceSet(endpoint.getPath(), sessionServiceSet);
        server.addServiceSet(endpoint.getPath(), subscriptionServiceSet);
      }

      server.startup().get(10, SECONDS);

      client =
          TestClient.create(
              server,
              config ->
                  // No keep-alive within the test's bounds: a keep-alive failure would leave
                  // Active by a different route than the connection loss being scripted.
                  config.setKeepAliveInterval(uint(60_000)));
    }

    /** Drop the SecureChannel, which moves the Session from Active to ReactivatingWait. */
    void loseConnection() throws Exception {
      var transport = (OpcTcpClientTransport) client.getTransport();

      Channel channel = transport.getChannelFsm().getChannel().get(10, SECONDS);
      channel.close().syncUninterruptibly();
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(10, SECONDS);
      } finally {
        server.shutdown().get(10, SECONDS);
      }
    }
  }

  /**
   * A running Server and a client, not yet connected, that connects to its Basic256Sha256 endpoint
   * with an application certificate the Server does not trust. The client trusts the Server, so
   * only the Server refuses.
   */
  private static final class UntrustedClientFixture implements AutoCloseable {

    final OpcUaServer server;
    final OpcUaClient client;

    UntrustedClientFixture() throws Exception {
      server = TestServer.create().getServer();
      server.startup().get(10, SECONDS);

      EndpointConfig secured =
          server.getConfig().getEndpoints().stream()
              .filter(e -> e.getSecurityPolicy() == SecurityPolicy.Basic256Sha256)
              .findFirst()
              .orElseThrow();

      var serverTrustList = new MemoryTrustListManager();
      serverTrustList.addTrustedCertificate(secured.getCertificate());

      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      X509Certificate certificate =
          new SelfSignedCertificateBuilder(keyPair)
              .setCommonName("UntrustedClient")
              .setApplicationUri("urn:eclipse:milo:test:untrusted-client")
              .build();

      client =
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
                      .setApplicationUri("urn:eclipse:milo:test:untrusted-client")
                      .setCertificateIdentity(keyPair, new X509Certificate[] {certificate})
                      .setCertificateValidator(
                          new DefaultClientCertificateValidator(
                              serverTrustList, new MemoryCertificateQuarantine()))
                      .setRequestTimeout(uint(5_000)));
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(10, SECONDS);
      } finally {
        server.shutdown().get(10, SECONDS);
      }
    }
  }

  // endregion
}
