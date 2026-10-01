/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatch;
import org.eclipse.milo.opcua.sdk.server.NodeManagerBatchException;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionCreatedEvent;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionDeletedEvent;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * An array started while diagnostics are already enabled adds elements for the Subscriptions that
 * exist at that moment, and registers for Subscription events first so nothing created during the
 * catch-up is missed. The catch-up and an event for the same Subscription can then overlap. These
 * tests pause the catch-up right after its element's nodes are published and deliver the event in
 * that window.
 */
class SubscriptionDiagnosticsVariableArrayCatchUpTest {

  private OpcUaServer server;
  private PausingNodeManager target;
  private SubscriptionDiagnosticsArrayTypeNode arrayNode;
  private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();
  private SubscriptionDiagnosticsVariableArray array;
  private Thread catchUp;

  @BeforeEach
  void startServer() {
    var config =
        OpcUaServerConfig.builder().setCertificateManager(new DefaultCertificateManager()).build();
    server = new OpcUaServer(config, transportProfile -> null);
    target = new PausingNodeManager();
    server.getAddressSpaceManager().register(target);

    var diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    diagnosticsNode.setEnabledFlag(true);
    arrayNode = diagnosticsNode.getSubscriptionDiagnosticsArrayNode();

    array =
        new SubscriptionDiagnosticsVariableArray(arrayNode, target) {
          @Override
          protected List<Subscription> getSubscriptions() {
            return new ArrayList<>(subscriptions);
          }
        };

    catchUp = new Thread(array::startup, "catch-up");
    target.pauseFirstCommitOn(catchUp);
  }

  @AfterEach
  void stopServer() throws InterruptedException {
    target.release();
    catchUp.join(TimeUnit.SECONDS.toMillis(10));
    array.shutdown();
    server.getAddressSpaceManager().unregister(target);
  }

  // Both the catch-up and the created event pass the "no element yet" check for a Subscription
  // that exists when the array starts. Only one of them may put an element in the address space.
  @Test
  void createdEventDuringCatchUpDoesNotPublishSecondElement() throws Exception {
    Subscription subscription = subscription(1);
    subscriptions.add(subscription);

    catchUp.start();
    target.awaitPaused();
    assertEquals(1, elementCount(), "the catch-up's element is published while it is paused");

    server.getInternalEventBus().post(new SubscriptionCreatedEvent(subscription));
    assertEquals(1, elementCount(), "the created event must not publish a second element");

    target.release();
    catchUp.join(TimeUnit.SECONDS.toMillis(10));
    assertFalse(catchUp.isAlive(), "catch-up did not finish");
    assertEquals(1, elementCount(), "one element per Subscription after the catch-up completes");
  }

  // A Subscription deleted while the catch-up is creating its element has no element to remove
  // yet; the deleted event must stop the pending element from being published.
  @Test
  void deletedEventDuringCatchUpTearsDownPendingElement() throws Exception {
    Subscription subscription = subscription(1);
    subscriptions.add(subscription);

    catchUp.start();
    target.awaitPaused();
    assertEquals(1, elementCount(), "the catch-up's element is published while it is paused");

    subscriptions.remove(subscription);
    server.getInternalEventBus().post(new SubscriptionDeletedEvent(subscription));

    target.release();
    catchUp.join(TimeUnit.SECONDS.toMillis(10));
    assertFalse(catchUp.isAlive(), "catch-up did not finish");
    assertEquals(0, elementCount(), "no element remains for a Subscription deleted mid-creation");
  }

  private long elementCount() {
    return target.getReferences(arrayNode.getNodeId()).stream()
        .filter(r -> r.isForward() && r.getReferenceTypeId().equals(NodeIds.HasComponent))
        .count();
  }

  private static Subscription subscription(int id) {
    var subscription = mock(Subscription.class);
    when(subscription.getId()).thenReturn(uint(id));
    return subscription;
  }

  /** Publishes batches normally, but holds one chosen thread after its first commit. */
  private static class PausingNodeManager extends UaNodeManager {

    private final CountDownLatch paused = new CountDownLatch(1);
    private final CountDownLatch release = new CountDownLatch(1);
    private volatile Thread pauseOn;

    void pauseFirstCommitOn(Thread thread) {
      pauseOn = thread;
    }

    void awaitPaused() throws InterruptedException {
      assertTrue(paused.await(10, TimeUnit.SECONDS), "the catch-up never committed a batch");
    }

    void release() {
      release.countDown();
    }

    @Override
    public CommitResult commit(NodeManagerBatch<UaNode> batch) throws NodeManagerBatchException {
      CommitResult result = super.commit(batch);

      if (Thread.currentThread() == pauseOn) {
        pauseOn = null;
        paused.countDown();
        try {
          assertTrue(release.await(10, TimeUnit.SECONDS), "paused commit was never released");
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IllegalStateException(e);
        }
      }

      return result;
    }
  }
}
