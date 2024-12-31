/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.testng.annotations.Test;

public class ExpandedNodeIdConversionsTest {

  @Test
  public void testExpandedNodeIdToNodeId() {
    assertNull(
        expandedNodeIdToNodeId(new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "foo", uint(2))));

    NodeId nodeId = new NodeId(0, "bar");

    assertEquals(expandedNodeIdToNodeId(nodeId.expanded()), nodeId);
  }

  @Test
  public void testExpandedNodeIdToString() {
    ExpandedNodeId e1 = new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "foo", uint(2));
    ExpandedNodeId e2 = new NodeId(1, "bar").expanded();

    assertEquals(expandedNodeIdToString(e1), e1.toParseableString());
    assertEquals(expandedNodeIdToString(e2), e2.toParseableString());
  }
}
