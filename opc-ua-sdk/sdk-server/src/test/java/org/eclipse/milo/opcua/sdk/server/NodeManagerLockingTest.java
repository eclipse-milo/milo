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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NodeManagerLockingTest {

  // Attribute observers run under the node monitor and may read manager references. Calling
  // getNodeId while holding the manager monitor introduces the opposite lock order and deadlocks.
  @ParameterizedTest
  @ValueSource(strings = {"add", "conditional", "batch"})
  void nodeIdentityIsResolvedOutsideTheManagerMonitor(String operation) throws Exception {
    var manager = new AbstractNodeManager<Node>();
    var node = mock(Node.class);
    var nodeId = new NodeId(1, "Observed");
    when(node.getNodeId())
        .thenAnswer(
            invocation -> {
              assertFalse(
                  Thread.holdsLock(manager),
                  "node attribute access must not hold the manager monitor");
              return nodeId;
            });
    switch (operation) {
      case "add" -> manager.addNode(node);
      case "conditional" -> manager.addNodeIfAbsent(node);
      case "batch" -> manager.commit(NodeManagerBatch.<Node>builder().addNode(node).build());
      default -> throw new AssertionError(operation);
    }
    assertSame(node, manager.getNode(nodeId).orElseThrow());
  }
}
