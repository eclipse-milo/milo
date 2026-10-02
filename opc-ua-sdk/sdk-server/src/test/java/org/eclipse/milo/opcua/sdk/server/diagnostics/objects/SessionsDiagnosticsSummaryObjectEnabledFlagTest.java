/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionManager;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Lock-ordering contract between the EnabledFlag observer and session listener callbacks.
 *
 * <p>The observer runs while the EnabledFlag node's monitor is held, and starting a Session's
 * diagnostics Object takes that monitor. A callback that took the summary's private lock before
 * that monitor would deadlock the Write that turns diagnostics off, and a callback that ran its
 * node work outside both would let the Write return while its Object was still appearing or
 * disappearing. These tests drive the listener directly so a callback can be held at a chosen
 * point.
 */
class SessionsDiagnosticsSummaryObjectEnabledFlagTest {

  private OpcUaServer server;
  private UaNodeManager target;
  private ServerDiagnosticsTypeNode diagnosticsNode;
  private PropertyTypeNode enabledFlagNode;
  private SessionsDiagnosticsSummaryTypeNode summaryNode;
  private SessionsDiagnosticsSummaryObject summary;
  private SessionListener listener;

  @BeforeEach
  void startSummaryObject() throws Exception {
    var config =
        OpcUaServerConfig.builder().setCertificateManager(new DefaultCertificateManager()).build();
    server = new OpcUaServer(config, transportProfile -> null);
    target = new UaNodeManager();
    server.getAddressSpaceManager().register(target);

    diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    enabledFlagNode = diagnosticsNode.getEnabledFlagNode();
    summaryNode = diagnosticsNode.getSessionsDiagnosticsSummaryNode();

    summary = new SessionsDiagnosticsSummaryObject(summaryNode, target);
    summary.startup();

    // The listener is registered with the SessionManager, which offers no way to invoke it for a
    // Session that has no transport, so drive it directly.
    listener = (SessionListener) field("sessionListener").get(summary);
  }

  @AfterEach
  void shutdownSummaryObject() {
    summary.shutdown();
    server.getAddressSpaceManager().unregister(target);
  }

  @Test
  void sessionCreatedWhileEnabledPublishesObjectAndDisableRemovesIt() {
    diagnosticsNode.setEnabledFlag(true);

    listener.onSessionCreated(session("Session1"));
    assertEquals(1, sessionObjectCount(), "Object published for a Session created while enabled");

    diagnosticsNode.setEnabledFlag(false);
    assertEquals(0, sessionObjectCount(), "Object removed when diagnostics turn off");
  }

  @Test
  void sessionCreatedWhileDisabledCreatesNoObject() {
    listener.onSessionCreated(session("Session1"));

    assertEquals(0, sessionObjectCount(), "no Object while diagnostics are disabled");
  }

  // The observer holds the EnabledFlag node's monitor and then takes the private lock. A callback
  // must take them in the same order: while another thread holds the monitor, a callback waits for
  // it without holding the private lock. Any other order can deadlock a flag Write.
  @Test
  void sessionCallbackTakesEnabledFlagMonitorBeforeItsOwnLock() throws Exception {
    diagnosticsNode.setEnabledFlag(true);
    Object lock = field("lock").get(summary);

    var callback = new Thread(() -> listener.onSessionCreated(session("Session1")), "callback");
    var lockProbe = new CountDownLatch(1);
    var prober =
        new Thread(
            () -> {
              synchronized (lock) {
                lockProbe.countDown();
              }
            },
            "lock-probe");

    synchronized (enabledFlagNode) {
      callback.start();
      awaitBlocked(callback);

      prober.start();
      assertTrue(
          lockProbe.await(10, TimeUnit.SECONDS),
          "callback holds the private lock while waiting for the EnabledFlag monitor");
    }

    callback.join(TimeUnit.SECONDS.toMillis(10));
    prober.join(TimeUnit.SECONDS.toMillis(10));
    assertFalse(callback.isAlive(), "callback did not finish once the monitor was released");
    assertEquals(1, sessionObjectCount(), "the callback completed its creation afterwards");
  }

  // A Write that turns diagnostics off while a callback is inside Object creation must wait for
  // that callback, and no Object may remain when the Write returns.
  @Test
  void disableWaitsForCallbackInsideCreationAndRemovesItsObject() throws Exception {
    diagnosticsNode.setEnabledFlag(true);

    var enteredCreation = new CountDownLatch(1);
    var finishCreation = new CountDownLatch(1);
    var holdsFlagMonitor = new AtomicBoolean();
    Session session = session("Session1");
    var subscriptionManager = session.getSubscriptionManager();
    // getSubscriptionManager() is first called by the Object's startup, after its nodes exist.
    when(session.getSubscriptionManager())
        .thenAnswer(
            invocation -> {
              holdsFlagMonitor.set(Thread.holdsLock(enabledFlagNode));
              enteredCreation.countDown();
              assertTrue(finishCreation.await(10, TimeUnit.SECONDS), "creation never released");
              return subscriptionManager;
            });

    var callback = new Thread(() -> listener.onSessionCreated(session), "callback");
    var disable = new Thread(() -> diagnosticsNode.setEnabledFlag(false), "disable");

    try {
      callback.start();
      assertTrue(enteredCreation.await(10, TimeUnit.SECONDS), "callback did not start creation");
      // Without the monitor, the disable below would deadlock against the callback instead of
      // waiting for it, so refuse to start it.
      assertTrue(
          holdsFlagMonitor.get(),
          "callback must hold the EnabledFlag monitor while it creates its Object");
      assertEquals(1, sessionObjectCount(), "the Object's nodes exist while creation is paused");

      disable.start();
      awaitBlocked(disable);
      finishCreation.countDown();

      disable.join(TimeUnit.SECONDS.toMillis(10));
      assertFalse(disable.isAlive(), "the disable Write did not complete");
      assertEquals(0, sessionObjectCount(), "no Object remains when the disable Write returns");
    } finally {
      finishCreation.countDown();
      callback.join(TimeUnit.SECONDS.toMillis(10));
      disable.join(TimeUnit.SECONDS.toMillis(10));
    }

    assertFalse(callback.isAlive(), "the callback did not complete");
    assertEquals(Boolean.FALSE, diagnosticsNode.getEnabledFlag());
  }

  /** Wait, bounded, until {@code thread} is blocked on a monitor. */
  private static void awaitBlocked(Thread thread) throws InterruptedException {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (thread.getState() != Thread.State.BLOCKED) {
      assertTrue(
          System.nanoTime() < deadline,
          thread.getName() + " never blocked on a monitor; state=" + thread.getState());
      Thread.sleep(5);
    }
  }

  private long sessionObjectCount() {
    return target.getReferences(summaryNode.getNodeId()).stream()
        .filter(r -> r.isForward() && r.getReferenceTypeId().equals(NodeIds.HasComponent))
        .count();
  }

  private static Field field(String name) throws NoSuchFieldException {
    Field field = SessionsDiagnosticsSummaryObject.class.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }

  private static Session session(String name) {
    var subscriptionManager = mock(SubscriptionManager.class);
    when(subscriptionManager.getSubscriptions()).thenReturn(List.of());

    var session = mock(Session.class);
    when(session.getSessionId()).thenReturn(new NodeId(1, name));
    when(session.getSessionName()).thenReturn(name);
    when(session.getSubscriptionManager()).thenReturn(subscriptionManager);
    return session;
  }
}
