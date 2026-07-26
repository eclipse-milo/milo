/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.digitalpetri.netty.fsm.ChannelFsm;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.client.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.junit.jupiter.api.Test;

public class SessionFsmTest {

  @Test
  public void testCloseSessionWhileInactive() throws Exception {
    OpcUaClientConfig clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(
                new EndpointDescription(
                    "opc.tcp://localhost:12685",
                    null,
                    null,
                    MessageSecurityMode.None,
                    SecurityPolicy.None.getUri(),
                    null,
                    TransportProfile.TCP_UASC_UABINARY.getUri(),
                    null))
            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
            .setApplicationUri("urn:eclipse:milo:examples:client")
            .build();

    OpcUaClient client = OpcUaClient.create(clientConfig);

    SessionFsm sessionFsm = SessionFsmFactory.newSessionFsm(client);

    assertNotNull(sessionFsm.closeSession().get());
  }

  /**
   * Verify that SessionFuture instances are properly completed with an exception when
   * closeSession() is called in the CreatingWait state.
   */
  @Test
  public void testCloseSessionCompletesSessionFutureInCreatingWait() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    server.startup().get();

    OpcUaClient client = TestClient.create(server, cfg -> {});

    server.shutdown().get();
    client.connectAsync();

    SessionFsm sessionFsm = client.getSessionFsm();
    while (sessionFsm.getState() != State.CreatingWait) {
      //noinspection BusyWait
      Thread.sleep(100);
    }

    CompletableFuture<OpcUaSession> sessionFuture = sessionFsm.getSession();
    sessionFsm.closeSession();

    assertThrows(ExecutionException.class, () -> sessionFuture.get(5, TimeUnit.SECONDS));
  }

  /**
   * Verify that SessionFuture instances are properly completed with an exception when
   * closeSession() is called in the ReactivatingWait state.
   */
  @Test
  public void testCloseSessionCompletesSessionFutureInReactivatingWait() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    server.startup().get();

    OpcUaClient client = TestClient.create(server, cfg -> {});
    client.connect();

    Thread.sleep(1000);

    server.shutdown().get();

    SessionFsm sessionFsm = client.getSessionFsm();
    while (sessionFsm.getState() != State.ReactivatingWait) {
      //noinspection BusyWait
      Thread.sleep(100);
    }

    CompletableFuture<OpcUaSession> sessionFuture = sessionFsm.getSession();
    sessionFsm.closeSession();

    assertThrows(ExecutionException.class, () -> sessionFuture.get(5, TimeUnit.SECONDS));
  }

  /**
   * Verify that a Session which CreateSession already established on the Server is closed when the
   * rest of the establishment sequence fails.
   *
   * <p>ActivateSession failing moves the FSM from Activating to CreatingWait, where it retries on a
   * backoff. Each retry establishes another Server-side Session, so unless the abandoned one is
   * closed the Server accumulates orphans that survive until their Session timeout expires (120s
   * with the default configuration).
   */
  @Test
  public void testSessionClosedWhenActivateSessionFails() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    server.startup().get();

    var sessionsCreated = new LinkedBlockingQueue<NodeId>();
    Set<NodeId> sessionsClosed = ConcurrentHashMap.newKeySet();

    server
        .getSessionManager()
        .addSessionListener(
            new SessionListener() {
              @Override
              public void onSessionCreated(Session session) {
                sessionsCreated.add(session.getSessionId());
              }

              @Override
              public void onSessionClosed(Session session) {
                sessionsClosed.add(session.getSessionId());
              }
            });

    // An identity certificate the Server does not trust, so ActivateSession is rejected after
    // CreateSession has already succeeded.
    KeyPair untrustedKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    var certificateBuilder = new SelfSignedCertificateBuilder(untrustedKeyPair);
    certificateBuilder.setCommonName("UntrustedIdentity");
    certificateBuilder.setApplicationUri("urn:eclipse:milo:test:untrusted");
    X509Certificate untrustedCertificate = certificateBuilder.build();

    OpcUaClient client =
        TestClient.create(
            server,
            cfg ->
                cfg.setIdentityProvider(
                    new X509IdentityProvider(untrustedCertificate, untrustedKeyPair.getPrivate())));

    try {
      client.connectAsync();

      NodeId firstSessionId = sessionsCreated.poll(30, TimeUnit.SECONDS);
      assertNotNull(firstSessionId, "Server never created a Session");

      // Observing the Session created by the next attempt proves the first attempt has already
      // failed and been abandoned.
      NodeId secondSessionId = sessionsCreated.poll(30, TimeUnit.SECONDS);
      assertNotNull(secondSessionId, "Server never created a second Session");

      // The close is sent before the retry is scheduled, but the Server dispatches requests onto
      // an executor, so the two are not strictly ordered; wait for the close rather than requiring
      // it to have been observed already.
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
      while (!sessionsClosed.contains(firstSessionId) && System.nanoTime() < deadline) {
        //noinspection BusyWait
        Thread.sleep(100);
      }

      assertTrue(
          sessionsClosed.contains(firstSessionId),
          "Session "
              + firstSessionId
              + " was left open on the Server after ActivateSession failed");
    } finally {
      client.disconnect();
      server.shutdown().get();
    }
  }

  /**
   * Verify that a SecureChannel lost while the SessionInitializers are running does not leave the
   * FSM sitting in Active with a Session bound to a dead channel.
   *
   * <p>The ChannelFsm.TransitionListener that turns a channel drop into Event.ConnectionLost is
   * only registered by the Initializing to Active transition action, and it reacts solely to the
   * edge leaving Connected. netty-channel-fsm notifies listeners synchronously, from a
   * TransitionAction, over the snapshot of its listener list taken while the event is evaluated, so
   * a Connected to ReconnectWait edge evaluated before the listener is registered is lost
   * permanently: the rest of the reconnect cycle (ReconnectWait to Reconnecting to Connected) has
   * no edge leaving Connected. Initializer failures are swallowed, so InitializeSuccess fires and
   * the FSM enters Active regardless.
   *
   * <p>Each reconnect establishes a brand new SecureChannel, so the Session the FSM is holding is
   * no longer usable; the Server answers it with Bad_SecureChannelIdInvalid (Part 4 5.6.3).
   */
  @Test
  public void testConnectionLostDuringInitializationIsNotMissed() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    server.startup().get();

    // A keep-alive interval well beyond this test's own bounds: the keep-alive is the fallback that
    // eventually notices the dead channel, and it would mask the transition being pinned here.
    OpcUaClient client = TestClient.create(server, cfg -> cfg.setKeepAliveInterval(uint(60_000)));

    var sessionInactive = new CountDownLatch(1);

    client.addSessionActivityListener(
        new SessionActivityListener() {
          @Override
          public void onSessionInactive(UaSession session) {
            sessionInactive.countDown();
          }
        });

    // Added last, so it runs after the built-in initializers: closes the channel and does not
    // complete until the ChannelFsm has evaluated the Connected -> ReconnectWait edge and notified
    // its listeners. That orders the drop strictly before the SDK registers its own listener,
    // rather than relying on the race being won.
    client.addSessionInitializer(
        (c, session) -> {
          ChannelFsm channelFsm = ((OpcTcpClientTransport) c.getTransport()).getChannelFsm();

          var leftConnected = new CompletableFuture<Unit>();

          channelFsm.addTransitionListener(
              (from, to, via) -> {
                if (from == com.digitalpetri.netty.fsm.State.Connected
                    && to != com.digitalpetri.netty.fsm.State.Connected) {

                  leftConnected.complete(Unit.VALUE);
                }
              });

          channelFsm
              .getChannel()
              .whenComplete(
                  (channel, ex) -> {
                    if (channel != null) {
                      channel.close();
                    } else {
                      leftConnected.completeExceptionally(ex);
                    }
                  });

          // Hand the continuation to another thread so the rest of the establishment sequence does
          // not run inline on the ChannelFsm's evaluation thread.
          return leftConnected.thenApplyAsync(u -> u);
        });

    try {
      client.connectAsync().get(30, TimeUnit.SECONDS);

      assertTrue(
          sessionInactive.await(10, TimeUnit.SECONDS),
          "SessionFsm remained Active after the channel was lost during Session initialization");
    } finally {
      client.disconnect();
      server.shutdown().get();
    }
  }
}
