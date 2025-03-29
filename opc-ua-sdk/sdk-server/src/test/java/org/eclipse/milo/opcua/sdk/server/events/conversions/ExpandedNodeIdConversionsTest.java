/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.eclipse.milo.opcua.sdk.server.events.conversions.ExpandedNodeIdConversions.expandedNodeIdToNodeId;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.ExpandedNodeIdConversions.expandedNodeIdToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Test;

public class ExpandedNodeIdConversionsTest {

  @Test
  public void testExpandedNodeIdToNodeId() {
    assertNull(
        expandedNodeIdToNodeId(
            new ExpandedNodeId(ServerReference.of(2), NamespaceReference.of(0), "foo")));

    NodeId nodeId = new NodeId(0, "bar");

    assertEquals(nodeId, expandedNodeIdToNodeId(nodeId.expanded()));
  }

  @Test
  public void testExpandedNodeIdToString() {
    ExpandedNodeId e1 = new ExpandedNodeId(ServerReference.of(2), NamespaceReference.of(0), "foo");
    ExpandedNodeId e2 = new NodeId(1, "bar").expanded();

    assertEquals(e1.toParseableString(), expandedNodeIdToString(e1));
    assertEquals(e2.toParseableString(), expandedNodeIdToString(e2));
  }
}
