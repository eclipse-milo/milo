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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.junit.jupiter.api.Test;

/**
 * Failed attempts to establish or recover a Session are reported to {@link
 * SessionActivityListener#onSessionAttemptFailed(Throwable)}.
 *
 * <p>The client retries a failed attempt for as long as it stays connected. When the Server stops
 * accepting the client's identity every retry is another rejected login, which can lock the
 * account, and the callback is the only way the application learns why the Session isn't coming
 * back and that it should disconnect.
 *
 * <p>Each test runs a Server whose identity validator accepts a fixed number of logins and rejects
 * the rest, the way a Server does after the client's password has been changed or revoked. The
 * password travels unencrypted, which is when Milo's Server rejects it with {@code
 * Bad_UserAccessDenied}; it answers {@code Bad_IdentityTokenInvalid} to a rejected password that
 * arrived encrypted.
 */
class SessionAttemptFailedTest {

  /** How a listener records the rejection of the client's identity by the Server. */
  private static final String REJECTED =
      "onSessionAttemptFailed(UaServiceFaultException, Bad_UserAccessDenied)";

  // An application can stop retries that cannot succeed only if it sees the Server's StatusCode,
  // without unwrapping the future chain, and in order after the onSessionInactive it follows.
  @Test
  void rejectedReactivationIsReportedWithTheServerStatusAfterSessionInactive() throws Exception {
    try (var fixture = new Fixture(1)) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.client.connect();
      fixture.loseConnection();

      assertEquals(
          List.of("onSessionActive", "onSessionInactive", REJECTED),
          listener.next(3),
          "the reactivation the Server rejected must be reported after onSessionInactive");
    }
  }

  // connect() fails when its first attempt does, but the client keeps retrying after it; a
  // listener that acts on every failure must not miss the first one.
  @Test
  void failedFirstAttemptMadeByConnectIsReported() throws Exception {
    try (var fixture = new Fixture(0)) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      UaException connectFailure = assertThrows(UaException.class, fixture.client::connect);

      assertEquals(StatusCodes.Bad_UserAccessDenied, connectFailure.getStatusCode().value());
      assertEquals(List.of(REJECTED), listener.next(1));
    }
  }

  // Listener code belongs to the application. A listener that throws must not stop the client
  // from recovering, or keep the listeners after it from hearing about the failed attempts.
  @Test
  void listenerThatThrowsDoesNotStopTheRetriesOrTheOtherListeners() throws Exception {
    try (var fixture = new Fixture(1)) {
      var throwingListener =
          new SessionActivityListener() {
            @Override
            public void onSessionAttemptFailed(Throwable failure) {
              throw new IllegalStateException("thrown by the test's listener");
            }
          };
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(throwingListener);
      fixture.client.addSessionActivityListener(listener);

      fixture.client.connect();
      fixture.loseConnection();

      assertEquals(
          List.of("onSessionActive", "onSessionInactive", REJECTED, REJECTED),
          listener.next(4),
          "the client must retry after a listener threw, and later listeners must hear each"
              + " failed attempt");
    }
  }

  // Disconnecting ends the attempts on purpose. Reporting that as a failure would hand an
  // application that disconnects on a failure a failure caused by its own disconnect.
  @Test
  void disconnectWhileWaitingToRetryIsNotReported() throws Exception {
    try (var fixture = new Fixture(0)) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      assertThrows(UaException.class, fixture.client::connect);
      fixture.client.disconnectAsync().get(10, SECONDS);

      // A later failed attempt is queued behind anything the disconnect reported, so its arrival
      // shows that nothing was reported in between.
      assertThrows(UaException.class, fixture.client::connect);

      assertEquals(
          List.of(REJECTED, REJECTED),
          listener.next(2),
          "the disconnect between two failed connects must not be reported as a failed attempt");
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
    public void onSessionAttemptFailed(Throwable failure) {
      String status =
          UaException.extractStatusCode(failure)
              .flatMap(statusCode -> StatusCodes.lookup(statusCode.value()))
              .map(nameAndDescription -> nameAndDescription[0])
              .orElse("no StatusCode");

      events.add(
          "onSessionAttemptFailed(%s, %s)".formatted(failure.getClass().getSimpleName(), status));
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

  /**
   * A running Server that accepts the first {@code acceptedLogins} logins and rejects every later
   * one with {@code Bad_UserAccessDenied}, and a client, not yet connected, that logs in with a
   * user name and password.
   */
  private static final class Fixture implements AutoCloseable {

    private static final UserTokenPolicy USERNAME_UNENCRYPTED =
        new UserTokenPolicy(
            "username", UserTokenType.UserName, null, null, SecurityPolicy.None.getUri());

    private final AtomicInteger logins = new AtomicInteger(0);

    private final OpcUaServer server;
    private final OpcUaClient client;

    Fixture(int acceptedLogins) throws Exception {
      int port = TestPortAllocator.allocatePort();

      EndpointConfig endpoint =
          EndpointConfig.newBuilder()
              .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
              .setBindAddress("localhost")
              .setHostname("localhost")
              .setBindPort(port)
              .setPath("/test")
              .setSecurityPolicy(SecurityPolicy.None)
              .setSecurityMode(MessageSecurityMode.None)
              .addTokenPolicy(USERNAME_UNENCRYPTED)
              .build();

      server =
          TestServer.create(
                  port,
                  new OpcUaServerConfigLimits() {},
                  config ->
                      config
                          .setEndpoints(Set.of(endpoint))
                          .setIdentityValidator(
                              new UsernameIdentityValidator(
                                  challenge -> logins.incrementAndGet() <= acceptedLogins)))
              .getServer();
      server.startup().get(10, SECONDS);

      client =
          TestClient.create(
              server,
              config ->
                  config
                      .setIdentityProvider(new UsernameProvider("user1", "password"))
                      // No keep-alive within the test's bounds: a keep-alive failure would leave
                      // Active by a different route than the connection loss being scripted.
                      .setKeepAliveInterval(uint(60_000)));
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

  // endregion
}
