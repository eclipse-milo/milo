/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UaNodeAttributeLockingTest {

  private static final UaNodeContext CONTEXT = mock(UaNodeContext.class);
  private static final NodeId NODE_ID = new NodeId(1, "Node");
  private static final QualifiedName BROWSE_NAME = new QualifiedName(1, "Node");
  private static final LocalizedText DISPLAY_NAME = LocalizedText.english("Node");
  private static final UInteger MASK = uint(0);

  /**
   * The Read service and access checks read node attributes from many threads at once. A read that
   * waits for the node's monitor serializes every reader of a node behind any writer and the
   * observers it notifies. Each node class overrides getAttribute, so each one is checked with an
   * attribute of its own while another thread holds the node's monitor.
   */
  @ParameterizedTest(name = "{0}.{1}")
  @MethodSource("nodeAttributes")
  void attributeReadDoesNotWaitForTheNodeMonitor(
      String nodeClass, AttributeId attributeId, UaNode node, Object expected) throws Exception {

    assertEquals(expected, readWhileAnotherThreadHoldsTheMonitor(node, attributeId));
  }

  // The Value is what a Read returns, and UaVariableNode reads it on its own branch: a copy of the
  // stored DataValue with a new server timestamp.
  @Test
  void valueReadDoesNotWaitForTheNodeMonitor() throws Exception {
    var node = new UaVariableNode(CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK);
    var stored =
        new DataValue(
            new Variant(42), StatusCode.GOOD, new DateTime(Instant.ofEpochSecond(1_000)), null);
    node.setAttribute(AttributeId.Value, stored);

    var value = (DataValue) readWhileAnotherThreadHoldsTheMonitor(node, AttributeId.Value);

    assertEquals(stored.value(), value.value());
    assertEquals(stored.statusCode(), value.statusCode());
    assertEquals(stored.sourceTime(), value.sourceTime());
    assertNotNull(
        value.serverTime(), "the copy gets a server timestamp; the stored value had none");
  }

  /** Read {@code attributeId} on another thread while a third thread holds the node's monitor. */
  private static Object readWhileAnotherThreadHoldsTheMonitor(UaNode node, AttributeId attributeId)
      throws Exception {

    var monitorHeld = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    Thread holder =
        new Thread(
            () -> {
              synchronized (node) {
                monitorHeld.countDown();
                awaitUninterruptibly(release);
              }
            });
    holder.start();

    try {
      assertTrue(monitorHeld.await(5, TimeUnit.SECONDS), "holder thread took the node monitor");

      return CompletableFuture.supplyAsync(() -> node.getAttribute(attributeId))
          .get(5, TimeUnit.SECONDS);
    } finally {
      release.countDown();
      holder.join(TimeUnit.SECONDS.toMillis(5));
    }
  }

  static Stream<Arguments> nodeAttributes() {
    var variable =
        new UaVariableNode(CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK);
    variable.setAttribute(AttributeId.DataType, NodeIds.Int32);

    return Stream.of(
        Arguments.of("UaNode", AttributeId.BrowseName, variable, BROWSE_NAME),
        Arguments.of("UaVariableNode", AttributeId.DataType, variable, NodeIds.Int32),
        Arguments.of(
            "UaVariableTypeNode",
            AttributeId.IsAbstract,
            new UaVariableTypeNode(
                CONTEXT,
                NODE_ID,
                BROWSE_NAME,
                DISPLAY_NAME,
                null,
                MASK,
                MASK,
                new DataValue(Variant.NULL_VALUE),
                NodeIds.BaseDataType,
                -1,
                null,
                true),
            true),
        Arguments.of(
            "UaObjectNode",
            AttributeId.EventNotifier,
            new UaObjectNode(
                CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK, ubyte(1)),
            ubyte(1)),
        Arguments.of(
            "UaObjectTypeNode",
            AttributeId.IsAbstract,
            new UaObjectTypeNode(
                CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK, true),
            true),
        Arguments.of(
            "UaMethodNode",
            AttributeId.Executable,
            new UaMethodNode(
                CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK, true, false),
            true),
        Arguments.of(
            "UaDataTypeNode",
            AttributeId.IsAbstract,
            new UaDataTypeNode(CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK, true),
            true),
        Arguments.of(
            "UaReferenceTypeNode",
            AttributeId.Symmetric,
            new UaReferenceTypeNode(
                CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK, false, true, null),
            true),
        Arguments.of(
            "UaViewNode",
            AttributeId.ContainsNoLoops,
            new UaViewNode(
                CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK, true, ubyte(0)),
            true));
  }

  /**
   * Control for the cases above: writes still hold the node's monitor while observers run, so
   * writes from different threads and their notifications do not interleave.
   */
  @Test
  void attributeObserversRunWhileTheWriterHoldsTheNodeMonitor() {
    var node = new UaVariableNode(CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK);
    var heldDuringNotification = new AtomicBoolean(false);
    node.addAttributeObserver(
        (n, attributeId, value) -> heldDuringNotification.set(Thread.holdsLock(n)));

    node.setAttribute(AttributeId.DataType, NodeIds.Double);

    assertTrue(heldDuringNotification.get(), "observer ran without the node monitor");
  }

  /**
   * A reader does not wait for a write's observers, and sees the new value while they are still
   * running.
   */
  @Test
  void readDuringObserverNotificationSeesTheNewValue() throws Exception {
    var node = new UaVariableNode(CONTEXT, NODE_ID, BROWSE_NAME, DISPLAY_NAME, null, MASK, MASK);
    node.setAttribute(AttributeId.DataType, NodeIds.Int32);

    var observerRunning = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    node.addAttributeObserver(
        (n, attributeId, value) -> {
          observerRunning.countDown();
          awaitUninterruptibly(release);
        });

    Thread writer = new Thread(() -> node.setAttribute(AttributeId.DataType, NodeIds.Double));
    writer.start();

    try {
      assertTrue(observerRunning.await(5, TimeUnit.SECONDS), "writer notified the observer");

      Object dataType =
          CompletableFuture.supplyAsync(() -> node.getAttribute(AttributeId.DataType))
              .get(5, TimeUnit.SECONDS);

      assertEquals(NodeIds.Double, dataType);
    } finally {
      release.countDown();
      writer.join(TimeUnit.SECONDS.toMillis(5));
    }
  }

  private static void awaitUninterruptibly(CountDownLatch latch) {
    while (true) {
      try {
        latch.await();
        return;
      } catch (InterruptedException ignored) {
        // keep holding the monitor until the test releases it
      }
    }
  }
}
