/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.junit.jupiter.api.Test;

class BrowseUtilTest {

  @Test
  void testNormalizeLocalAbsolute() {
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("uri:test");

    // Local (server index 0) and absolute (namespace URI)
    ExpandedNodeId nodeId = ExpandedNodeId.of("uri:test", "test");
    assertTrue(nodeId.isLocal());
    assertTrue(nodeId.isAbsolute());

    // Normalize should convert to relative
    ExpandedNodeId normalized = BrowseUtil.normalize(nodeId, namespaceTable);
    assertTrue(normalized.isLocal());
    assertTrue(normalized.isRelative());
    assertEquals(ushort(1), normalized.getNamespaceIndex());
    assertEquals("test", normalized.getIdentifier());
  }

  @Test
  void testNormalizeLocalRelative() {
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("uri:test");

    // Local (server index 0) and relative (namespace index)
    ExpandedNodeId nodeId = ExpandedNodeId.of(ushort(1), "test");
    assertTrue(nodeId.isLocal());
    assertTrue(nodeId.isRelative());

    // Normalize should keep it unchanged
    ExpandedNodeId normalized = BrowseUtil.normalize(nodeId, namespaceTable);
    assertTrue(normalized.isLocal());
    assertTrue(normalized.isRelative());
    assertEquals(ushort(1), normalized.getNamespaceIndex());
    assertEquals("test", normalized.getIdentifier());
    assertSame(nodeId, normalized); // Should be the same instance
  }

  @Test
  void testNormalizeNonLocalRelative() {
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("uri:test");

    // Non-local (server index 1) and relative (namespace index)
    ExpandedNodeId nodeId =
        new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(ushort(1)), "test");
    assertFalse(nodeId.isLocal());
    assertTrue(nodeId.isRelative());

    // Normalize should convert to absolute
    ExpandedNodeId normalized = BrowseUtil.normalize(nodeId, namespaceTable);
    assertFalse(normalized.isLocal());
    assertTrue(normalized.isAbsolute());
    assertEquals("uri:test", normalized.getNamespaceUri());
    assertEquals("test", normalized.getIdentifier());
  }

  @Test
  void testNormalizeNonLocalAbsolute() {
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("uri:test");

    // Non-local (server index 1) and absolute (namespace URI)
    ExpandedNodeId nodeId =
        new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of("uri:test"), "test");
    assertFalse(nodeId.isLocal());
    assertTrue(nodeId.isAbsolute());

    // Normalize should keep it unchanged
    ExpandedNodeId normalized = BrowseUtil.normalize(nodeId, namespaceTable);
    assertFalse(normalized.isLocal());
    assertTrue(normalized.isAbsolute());
    assertEquals("uri:test", normalized.getNamespaceUri());
    assertEquals("test", normalized.getIdentifier());
    assertSame(nodeId, normalized); // Should be the same instance
  }
}
