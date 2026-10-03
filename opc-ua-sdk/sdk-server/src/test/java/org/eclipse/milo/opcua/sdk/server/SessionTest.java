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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.server.diagnostics.ServerDiagnosticsSummary;
import org.eclipse.milo.opcua.sdk.server.identity.DefaultAnonymousIdentity;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests session lifecycle behavior that is local to {@link Session}. */
class SessionTest {

  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final ScheduledExecutorService scheduledExecutor = mock(ScheduledExecutorService.class);
  private final ScheduledFuture<?> timeoutFuture = mock(ScheduledFuture.class);

  /** The timeout checks the Session scheduled, in order, so a test can run one on demand. */
  private final List<Runnable> timeoutChecks = new ArrayList<>();

  private OpcUaServer server;

  @BeforeEach
  void setUp() {
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(executor);

    server = mock(OpcUaServer.class);
    when(server.getConfig()).thenReturn(config);
    when(server.getScheduledExecutorService()).thenReturn(scheduledExecutor);
    when(server.getDiagnosticsSummary()).thenReturn(new ServerDiagnosticsSummary(server));
    doAnswer(
            invocation -> {
              timeoutChecks.add(invocation.getArgument(0));
              return timeoutFuture;
            })
        .when(scheduledExecutor)
        .schedule(any(Runnable.class), anyLong(), eq(TimeUnit.NANOSECONDS));
  }

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
  }

  @Test
  void closeNotifiesLifecycleListenersOnce() {
    Session session = newSession(Duration.ofMinutes(1));

    AtomicInteger closed = new AtomicInteger();
    session.addLifecycleListener((s, subscriptionsDeleted) -> closed.incrementAndGet());

    session.close(false);
    session.close(false);

    assertEquals(1, closed.get());
    verify(timeoutFuture, times(1)).cancel(false);
  }

  // A component that holds Sessions it learned of from a listener needs to tell an open Session
  // from one that has closed since, without waiting for the close notification.
  @Test
  void isClosedFlipsWhenTheSessionIsClosed() {
    Session session = newSession(Duration.ofMinutes(1));
    assertFalse(session.isClosed(), "a Session is open until something closes it");

    session.close(false);

    assertTrue(session.isClosed());
  }

  // Timeout is a close path no client request triggers, so it must be reflected the same way.
  @Test
  void isClosedFlipsWhenTheSessionTimesOut() throws Exception {
    Session session = newSession(Duration.ofMillis(1));
    assertEquals(1, timeoutChecks.size(), "the Session schedules its timeout check on creation");

    // Let more than the timeout pass since the Session's last activity, then run the check as
    // the scheduler would.
    long start = System.nanoTime();
    while (System.nanoTime() - start < Duration.ofMillis(2).toNanos()) {
      Thread.onSpinWait();
    }
    timeoutChecks.get(0).run();
    executor.submit(() -> {}).get(10, SECONDS);

    assertTrue(session.isClosed());
    assertEquals(1, server.getDiagnosticsSummary().getSessionTimeoutCount().sum());
  }

  // A component that checks access for a Session discards answers that straddle a change of what
  // they are made from. Both the identity and the endpoint feed them, so each moves the epoch.
  @Test
  void identityAndEndpointChangesEachMoveTheAccessEpoch() {
    Session session = newSession(Duration.ofMinutes(1));
    long initial = session.getAccessEpoch();

    session.setIdentity(new DefaultAnonymousIdentity(), new AnonymousIdentityToken("anonymous"));
    long afterIdentity = session.getAccessEpoch();

    session.setEndpoint(endpointDescription());
    long afterEndpoint = session.getAccessEpoch();

    assertNotEquals(initial, afterIdentity, "an identity change moves the epoch");
    assertNotEquals(afterIdentity, afterEndpoint, "an endpoint change moves the epoch");
  }

  private Session newSession(Duration sessionTimeout) {
    return new Session(
        server,
        new NodeId(1, "session"),
        "session",
        sessionTimeout,
        clientDescription(),
        "urn:eclipse:milo:test",
        uint(0),
        endpointDescription(),
        1L,
        new SecurityConfiguration(
            SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null));
  }

  private static ApplicationDescription clientDescription() {
    return new ApplicationDescription(
        "urn:eclipse:milo:test:client",
        "urn:eclipse:milo:test",
        LocalizedText.english("test client"),
        ApplicationType.Client,
        null,
        null,
        null);
  }

  private static EndpointDescription endpointDescription() {
    return new EndpointDescription(
        "opc.tcp://localhost:12685",
        clientDescription(),
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[0],
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }
}
