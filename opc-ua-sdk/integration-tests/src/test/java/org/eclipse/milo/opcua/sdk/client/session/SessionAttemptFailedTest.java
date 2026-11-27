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
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseTcpClientTransport;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.junit.jupiter.api.Test;

/**
 * Failed attempts to establish or recover a Session are reported to {@link
 * SessionActivityListener#onSessionAttemptFailed(SessionActivityListener.AttemptStep, Throwable)}.
 *
 * <p>The client retries a failed attempt for as long as it stays connected. When the Server stops
 * accepting the client's identity every retry is another rejected login, which can lock the
 * account, and the callback is the only way the application learns why the Session isn't coming
 * back and that it should disconnect.
 *
 * <p>Each test runs a Server whose identity validator the test switches between accepting and
 * rejecting logins, the way a Server behaves once the client's password has been changed or
 * revoked. The password travels unencrypted, which is when Milo's Server rejects it with {@code
 * Bad_UserAccessDenied}; it answers {@code Bad_IdentityTokenInvalid} to a rejected password that
 * arrived encrypted.
 *
 * <p>The tests that check that nothing is reported end with a Session the client activates
 * afterward. Listeners hear about it after anything reported before it, so its {@code
 * onSessionActive} shows, without waiting for a timeout, that nothing else was reported.
 */
class SessionAttemptFailedTest {

  /** How a listener records the rejection of the client's identity by the Server. */
  private static final String REJECTED =
      "onSessionAttemptFailed(UaServiceFaultException, Bad_UserAccessDenied)";

  // An application can stop retries that cannot succeed only if it sees the Server's StatusCode,
  // without unwrapping the future chain, and in order after the onSessionInactive it follows.
  @Test
  void rejectedReactivationIsReportedWithTheServerStatusAfterSessionInactive() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.acceptLogins();
      fixture.client.connect();

      fixture.rejectLogins();
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
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.rejectLogins();

      UaException connectFailure = assertThrows(UaException.class, fixture.client::connect);

      assertEquals(StatusCodes.Bad_UserAccessDenied, connectFailure.getStatusCode().value());
      assertEquals(List.of(REJECTED), listener.next(1));
    }
  }

  // The Server can refuse the Session itself, before the client's identity is ever checked.
  @Test
  void createSessionRejectedByTheServerIsReported() throws Exception {
    OpcUaServerConfigLimits noSessions =
        new OpcUaServerConfigLimits() {
          @Override
          public UInteger getMaxSessions() {
            return uint(0);
          }
        };

    try (var fixture = new Fixture(noSessions)) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      assertThrows(UaException.class, fixture.client::connect);

      assertEquals(
          List.of("onSessionAttemptFailed(UaServiceFaultException, Bad_TooManySessions)"),
          listener.next(1));
    }
  }

  // Listener code belongs to the application. A listener that throws must not stop the client
  // from recovering, or keep the listeners after it from hearing about the failed attempts.
  @Test
  void listenerThatThrowsDoesNotStopTheRetriesOrTheOtherListeners() throws Exception {
    try (var fixture = new Fixture()) {
      var throwingListener =
          new SessionActivityListener() {
            @Override
            public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
              throw new IllegalStateException("thrown by the test's listener");
            }
          };
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(throwingListener);
      fixture.client.addSessionActivityListener(listener);

      fixture.acceptLogins();
      fixture.client.connect();

      fixture.rejectLogins();
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
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.rejectLogins();
      // The retry is due a second after connect() fails. Should the test take longer than that to
      // start the disconnect, the retry waits on the Server until the disconnect has started.
      fixture.holdLoginsAfter(1);

      assertThrows(UaException.class, fixture.client::connect);

      CompletableFuture<OpcUaClient> disconnected = fixture.client.disconnectAsync();
      fixture.releaseHeldLogins();
      disconnected.get(10, SECONDS);

      fixture.acceptLogins();
      fixture.client.connect();

      assertEquals(
          List.of(REJECTED, "onSessionActive"),
          listener.next(2),
          "the disconnect after the failed connect must not be reported as a failed attempt");
    }
  }

  // A disconnect that starts while an attempt is in progress waits for the attempt to end, and no
  // retry follows it however the attempt ends. Reporting the attempt's failure would hand the
  // application a failure, caused by nothing it can act on, after its disconnect had returned.
  @Test
  void attemptInProgressWhenTheDisconnectStartsIsNotReported() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.rejectLogins();
      fixture.holdLoginsAfter(0);

      CompletableFuture<OpcUaClient> connected = fixture.client.connectAsync();
      fixture.awaitHeldLogin();

      CompletableFuture<OpcUaClient> disconnected = fixture.client.disconnectAsync();
      fixture.releaseHeldLogins();
      disconnected.get(10, SECONDS);

      ExecutionException attemptFailure =
          assertThrows(ExecutionException.class, () -> connected.get(10, SECONDS));
      assertEquals(
          StatusCodes.Bad_UserAccessDenied,
          UaException.extractStatusCode(attemptFailure).orElseThrow().value(),
          "the attempt in progress must have failed with the Server's rejection");

      fixture.acceptLogins();
      fixture.client.connect();

      assertEquals(
          List.of("onSessionActive"),
          listener.next(1),
          "the attempt that failed after the disconnect started must not be reported");
    }
  }

  // Disconnecting the client while connect() is still connecting the transport leaves the client
  // creating Sessions over a transport that nothing will connect again. Those attempts cannot
  // succeed until the application connects, and it has disconnected, so none is reported. The
  // test opens the Session over a transport that is not connected, which is where connect()
  // continues once that disconnect has run.
  @Test
  void attemptsOverATransportThatIsDisconnectedForGoodAreNotReported() throws Exception {
    try (var fixture = new Fixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      CompletableFuture<OpcUaSession> opened = fixture.client.getSessionFsm().openSession();

      assertThrows(
          ExecutionException.class,
          () -> opened.get(10, SECONDS),
          "an attempt over the disconnected transport must have failed");

      // Connecting the transport lets a later attempt succeed, whichever attempt connectAsync()
      // joins; its onSessionActive is all that may be reported.
      fixture.acceptLogins();
      fixture.client.connectAsync();

      assertEquals(
          List.of("onSessionActive"),
          listener.next(1),
          "attempts over a transport the application disconnected must not be reported");
    }
  }

  // A reverse transport the application disconnected waits for no further connection from the
  // Server until the application connects it again, so no attempt over it can succeed before then.
  // Disconnecting the transport, not the client, leaves the client to reactivate the Session over
  // it.
  @Test
  void attemptsOverAReverseTransportTheApplicationDisconnectedAreNotReported() throws Exception {
    try (var fixture = new ReverseConnectFixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      fixture.startReverseConnect();
      fixture.client.connect();

      fixture.client.getTransport().disconnect().get(10, SECONDS);

      assertEquals(List.of("onSessionActive", "onSessionInactive"), listener.next(2));

      CompletableFuture<OpcUaSession> reactivated = fixture.client.getSessionFsm().getSession();

      assertThrows(
          ExecutionException.class,
          () -> reactivated.get(10, SECONDS),
          "the reactivation over the disconnected transport must have failed");

      fixture.client.connect();

      assertEquals(
          List.of("onSessionActive"),
          listener.next(1),
          "attempts over a reverse transport the application disconnected must not be reported");
    }
  }

  // connect() waits for the Server to connect to the client. An application that stops waiting
  // and disconnects the client has ended the attempts, even though connect() then opens the
  // Session over the transport the disconnect left behind.
  @Test
  void disconnectWhileConnectWaitsForAReverseConnectionIsNotReported() throws Exception {
    try (var fixture = new ReverseConnectFixture()) {
      var listener = new RecordingListener();
      fixture.client.addSessionActivityListener(listener);

      CompletableFuture<OpcUaClient> connected = fixture.client.connectAsync();
      fixture.client.disconnectAsync().get(10, SECONDS);

      assertThrows(
          ExecutionException.class,
          () -> connected.get(10, SECONDS),
          "connect() must have failed once the client disconnected");

      fixture.startReverseConnect();
      fixture.client.connect();

      assertEquals(
          List.of("onSessionActive"),
          listener.next(1),
          "attempts after the client disconnected must not be reported");
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
   * A running Server whose logins the test controls, and a client, not yet connected, that logs in
   * with a user name and password.
   *
   * <p>The Server rejects logins with {@code Bad_UserAccessDenied} until the test makes it accept
   * them. The test can also make it hold logins, which keeps an attempt in progress, with its
   * ActivateSession waiting for the Server's answer, until the test releases them.
   */
  private static final class Fixture implements AutoCloseable {

    private static final UserTokenPolicy USERNAME_UNENCRYPTED =
        new UserTokenPolicy(
            "username", UserTokenType.UserName, null, null, SecurityPolicy.None.getUri());

    private final AtomicInteger logins = new AtomicInteger(0);
    private final Semaphore heldLogins = new Semaphore(0);
    private final CountDownLatch heldLoginsReleased = new CountDownLatch(1);

    private volatile boolean acceptingLogins = false;
    private volatile int loginsAnsweredAtOnce = Integer.MAX_VALUE;

    private final OpcUaServer server;
    private final OpcUaClient client;

    Fixture() throws Exception {
      this(new OpcUaServerConfigLimits() {});
    }

    Fixture(OpcUaServerConfigLimits limits) throws Exception {
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
                  limits,
                  config ->
                      config
                          .setEndpoints(Set.of(endpoint))
                          .setIdentityValidator(
                              new UsernameIdentityValidator(challenge -> answerLogin())))
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

    /** Make the Server accept every login it answers from now on. */
    void acceptLogins() {
      acceptingLogins = true;
    }

    /** Make the Server reject every login it answers from now on. */
    void rejectLogins() {
      acceptingLogins = false;
    }

    /**
     * Make the Server answer only the first {@code count} logins at once, and hold every later one
     * until {@link #releaseHeldLogins()}.
     */
    void holdLoginsAfter(int count) {
      loginsAnsweredAtOnce = count;
    }

    /** Wait until the Server holds a login, i.e. an attempt waits for the Server's answer. */
    void awaitHeldLogin() throws InterruptedException {
      assertTrue(heldLogins.tryAcquire(10, SECONDS), "no login reached the Server");
    }

    /** Answer the held logins, and every later one at once, as the Server answers them now. */
    void releaseHeldLogins() {
      heldLoginsReleased.countDown();
    }

    /** Drop the SecureChannel, which moves the Session from Active to ReactivatingWait. */
    void loseConnection() throws Exception {
      var transport = (OpcTcpClientTransport) client.getTransport();

      Channel channel = transport.getChannelFsm().getChannel().get(10, SECONDS);
      channel.close().syncUninterruptibly();
    }

    @Override
    public void close() throws Exception {
      // A held login would keep the disconnect waiting for the attempt it belongs to.
      releaseHeldLogins();

      try {
        client.disconnectAsync().get(10, SECONDS);
      } finally {
        server.shutdown().get(10, SECONDS);
      }
    }

    private boolean answerLogin() {
      if (logins.incrementAndGet() > loginsAnsweredAtOnce) {
        heldLogins.release();

        try {
          heldLoginsReleased.await();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return false;
        }
      }

      return acceptingLogins;
    }
  }

  /**
   * A running Server and a client, not yet connected, that waits for the Server to connect to it
   * with Reverse Connect.
   *
   * <p>Once the test starts Reverse Connect, the Server connects to the client's listener, and
   * connects again shortly after each connection closes. A connection that arrives while the client
   * isn't waiting for one is held until the client connects.
   */
  private static final class ReverseConnectFixture implements AutoCloseable {

    private final OpcUaServer server;
    private final ReverseConnectManager manager;
    private final OpcUaClient client;

    ReverseConnectFixture() throws Exception {
      server = TestServer.create().getServer();
      server.startup().get(10, SECONDS);

      manager =
          ReverseConnectManager.builder()
              .addBindAddress(new InetSocketAddress("localhost", 0))
              .build();
      manager.startup();

      client =
          TestClient.createWithTransport(
              server,
              transportConfig ->
                  new ReverseTcpClientTransport(
                      transportConfig, manager, ReverseConnectSelector.any()),
              // No keep-alive within the test's bounds, as in Fixture.
              config -> config.setKeepAliveInterval(uint(60_000)));
    }

    /** Make the Server connect to the client's listener, now and after each connection closes. */
    void startReverseConnect() {
      var listenerAddress =
          (InetSocketAddress) manager.snapshot().listeners().get(0).boundAddress();

      server.addReverseConnectTarget(
          ReverseConnectTarget.builder()
              .setClientListenerUrl(
                  "opc.tcp://%s:%d"
                      .formatted(listenerAddress.getHostString(), listenerAddress.getPort()))
              .setEndpointUrl(client.getConfig().getEndpoint().getEndpointUrl())
              .setRegistrationPeriod(uint(100))
              .build());
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(10, SECONDS);
      } finally {
        manager.shutdown();
        server.shutdown().get(10, SECONDS);
      }
    }
  }

  // endregion
}
