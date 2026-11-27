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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
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

  // OPC 10000-4 §7.16: a target addressed by this server's own ServerUri (index 0) must normalize
  // to a local ServerIndex(0) and relative namespace index so downstream local reads and access
  // checks treat it identically to serverIndex == 0.
  @Test
  void testNormalizeLocalServerUriResolvesToLocalRelative() {
    ServerTable serverTable = new ServerTable();
    serverTable.add("urn:example:local:server");
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("urn:example:local:ns");

    ExpandedNodeId absoluteLocalByUri =
        new ExpandedNodeId(
            ServerReference.of("urn:example:local:server"),
            NamespaceReference.of("urn:example:local:ns"),
            "localNode");
    ExpandedNodeId relativeLocalByUri =
        new ExpandedNodeId(
            ServerReference.of("urn:example:local:server"),
            NamespaceReference.of(ushort(1)),
            "localNode");

    ExpandedNodeId normalizedFromAbsolute =
        BrowseUtil.normalize(absoluteLocalByUri, serverTable, namespaceTable).orElseThrow();
    ExpandedNodeId normalizedFromRelative =
        BrowseUtil.normalize(relativeLocalByUri, serverTable, namespaceTable).orElseThrow();

    assertTrue(normalizedFromAbsolute.isLocal());
    assertTrue(normalizedFromAbsolute.isRelative());
    assertEquals(uint(0), normalizedFromAbsolute.getServerIndex());
    assertEquals(ushort(1), normalizedFromAbsolute.getNamespaceIndex());
    assertEquals("localNode", normalizedFromAbsolute.getIdentifier());
    assertEquals(normalizedFromAbsolute, normalizedFromRelative);
  }

  // OPC 10000-4 §7.29: a local target with an unregistered namespace URI cannot normalize to a
  // local namespace index and must return empty rather than emitting an invalid target or
  // bypassing local access checks.
  @Test
  void testNormalizeLocalAbsoluteUnregisteredNamespaceReturnsEmpty() {
    ServerTable serverTable = new ServerTable();
    serverTable.add("urn:example:local:server");
    NamespaceTable namespaceTable = new NamespaceTable();

    ExpandedNodeId byIndex = ExpandedNodeId.of("urn:example:unregistered", "missing");
    ExpandedNodeId byLocalUri =
        new ExpandedNodeId(
            ServerReference.of("urn:example:local:server"),
            NamespaceReference.of("urn:example:unregistered"),
            "missing");

    assertTrue(BrowseUtil.normalize(byIndex, serverTable, namespaceTable).isEmpty());
    assertTrue(BrowseUtil.normalize(byLocalUri, serverTable, namespaceTable).isEmpty());
  }

  // A local relative target whose numeric namespace index is absent from NamespaceArray must
  // return empty rather than leaking an invalid namespace index to Browse clients.
  @Test
  void testNormalizeLocalRelativeUnregisteredNamespaceIndexReturnsEmpty() {
    ServerTable serverTable = new ServerTable();
    serverTable.add("urn:example:local:server");
    NamespaceTable namespaceTable = new NamespaceTable();

    ExpandedNodeId byIndex = ExpandedNodeId.of(ushort(42), "Pump");
    ExpandedNodeId byLocalUri =
        new ExpandedNodeId(
            ServerReference.of("urn:example:local:server"),
            NamespaceReference.of(ushort(42)),
            "Pump");

    assertTrue(BrowseUtil.normalize(byIndex, serverTable, namespaceTable).isEmpty());
    assertTrue(BrowseUtil.normalize(byLocalUri, serverTable, namespaceTable).isEmpty());
  }

  // OPC 10000-4 §7.29: a remote target with a relative namespace index absent from the local
  // NamespaceTable cannot be converted to an absolute namespace URI and must return empty.
  @Test
  void testNormalizeNonLocalRelativeUnknownIndexReturnsEmpty() {
    ServerTable serverTable = new ServerTable();
    serverTable.add("urn:example:local:server");
    serverTable.add("urn:example:remote:server");
    NamespaceTable namespaceTable = new NamespaceTable();

    ExpandedNodeId nodeId =
        new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(ushort(99)), "remote");

    assertEquals(Optional.empty(), BrowseUtil.normalize(nodeId, serverTable, namespaceTable));
  }

  // A remote reference identified by a registered ServerUri and a mapped namespace index resolves
  // to ServerIndex(1) and an absolute namespace URI.
  @Test
  void testNormalizeNonLocalRegisteredServerUriRelative() {
    ServerTable serverTable = new ServerTable();
    serverTable.add("urn:example:local:server");
    serverTable.add("urn:example:remote:server");
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("uri:test");

    ExpandedNodeId nodeId =
        new ExpandedNodeId(
            ServerReference.of("urn:example:remote:server"),
            NamespaceReference.of(ushort(1)),
            "remote");

    ExpandedNodeId normalized =
        BrowseUtil.normalize(nodeId, serverTable, namespaceTable).orElseThrow();

    assertFalse(normalized.isLocal());
    assertTrue(normalized.isAbsolute());
    assertEquals(uint(1), normalized.getServerIndex());
    assertEquals("uri:test", normalized.getNamespaceUri());
    assertEquals("remote", normalized.getIdentifier());
  }

  // An unregistered remote ServerUri (which would fail binary encoding with Bad_EncodingError) or
  // an unregistered remote ServerIndex must return empty so Browse can skip it cleanly.
  @Test
  void testNormalizeUnregisteredRemoteServerReturnsEmpty() {
    ServerTable serverTable = new ServerTable();
    serverTable.add("urn:example:local:server");
    NamespaceTable namespaceTable = new NamespaceTable();

    ExpandedNodeId unregisteredUri =
        new ExpandedNodeId(
            ServerReference.of("urn:example:unknown:server"),
            NamespaceReference.of("urn:example:remote:ns"),
            "remote");
    ExpandedNodeId unregisteredIndex =
        new ExpandedNodeId(
            ServerReference.of(2), NamespaceReference.of("urn:example:remote:ns"), "remote");

    assertTrue(BrowseUtil.normalize(unregisteredUri, serverTable, namespaceTable).isEmpty());
    assertTrue(BrowseUtil.normalize(unregisteredIndex, serverTable, namespaceTable).isEmpty());
  }
}
