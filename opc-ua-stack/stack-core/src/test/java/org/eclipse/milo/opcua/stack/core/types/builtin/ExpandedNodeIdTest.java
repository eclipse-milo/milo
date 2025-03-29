/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ExpandedNodeIdTest {

  private final NamespaceTable namespaceTable = new NamespaceTable();

  @Test
  public void testLocal() {
    namespaceTable.add("uri:test");

    ExpandedNodeId xni0 = ExpandedNodeId.of(ushort(0), "test");
    assertTrue(xni0.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni1 = ExpandedNodeId.of(ushort(1), "test");
    assertTrue(xni1.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni2 = ExpandedNodeId.of(namespaceTable.get(0), "test");
    assertTrue(xni2.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni3 = ExpandedNodeId.of(namespaceTable.get(1), "test");
    assertTrue(xni3.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni4 = ExpandedNodeId.of("uri:notpresent", "test");
    assertFalse(xni4.toNodeId(namespaceTable).isPresent());
  }

  @Test
  public void testIsNull() {
    assertTrue(ExpandedNodeId.of(ushort(0), uint(0)).isNull());
    assertTrue(ExpandedNodeId.of(ushort(0), "").isNull());
    assertTrue(ExpandedNodeId.of(ushort(0), (String) null).isNull());
    assertTrue(ExpandedNodeId.of(ushort(0), new UUID(0, 0)).isNull());
    assertTrue(ExpandedNodeId.of(ushort(0), ByteString.NULL_VALUE).isNull());
    assertTrue(ExpandedNodeId.of(ushort(0), ByteString.of(new byte[0])).isNull());
  }

  @Test
  public void testEqualityWithNodeId() {
    {
      ExpandedNodeId xni = ExpandedNodeId.of("foo");

      NodeId nodeId = new NodeId(0, "foo");

      assertTrue(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni = ExpandedNodeId.of(Namespaces.OPC_UA, "foo");

      NodeId nodeId = new NodeId(0, "foo");

      assertTrue(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni =
          new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(0), "foo");

      NodeId nodeId = new NodeId(0, "foo");

      assertFalse(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni = ExpandedNodeId.of(ushort(1), "foo");

      NodeId nodeId = new NodeId(0, "foo");

      assertFalse(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni = ExpandedNodeId.of("uri:not:found", "foo");

      NodeId nodeId = new NodeId(0, "foo");

      assertFalse(xni.equalTo(nodeId));
    }
  }

  @Test
  public void testToParseableString() {
    String withoutNamespaceUri = ExpandedNodeId.of(ushort(1), uint(0)).toParseableString();
    assertEquals("ns=1;i=0", withoutNamespaceUri);

    String withNamespaceUri = ExpandedNodeId.of("urn:test", uint(0)).toParseableString();
    assertEquals("nsu=urn:test;i=0", withNamespaceUri);

    String withServerIndex =
        new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of("urn:test"), uint(0))
            .toParseableString();
    assertEquals("svr=1;nsu=urn:test;i=0", withServerIndex);
  }

  @Test
  public void absolute() {
    ExpandedNodeId xni = ExpandedNodeId.of(ushort(0), "foo");
    assertTrue(xni.isRelative());

    ExpandedNodeId absolute = xni.absolute(new NamespaceTable()).orElseThrow(RuntimeException::new);
    assertTrue(absolute.isAbsolute());

    NamespaceReference namespace = absolute.namespace();
    assertInstanceOf(NamespaceReference.NamespaceUri.class, namespace);
    assertEquals(Namespaces.OPC_UA, ((NamespaceReference.NamespaceUri) namespace).namespaceUri());
  }

  @Test
  public void relative() {
    ExpandedNodeId xni = ExpandedNodeId.of(Namespaces.OPC_UA, "foo");
    assertTrue(xni.isAbsolute());

    ExpandedNodeId relative = xni.relative(new NamespaceTable()).orElseThrow(RuntimeException::new);
    assertTrue(relative.isRelative());
    assertNull(relative.getNamespaceUri());
    assertEquals(ushort(0), relative.getNamespaceIndex());
  }

  @Test
  public void parseImplicitNamespaceZero() {
    ExpandedNodeId xni = ExpandedNodeId.parse("i=2256");

    assertNotNull(xni);
    assertEquals(ushort(0), xni.getNamespaceIndex());
  }

  @Test
  public void toParseableExplicitNamespaceZero() {
    ExpandedNodeId xni = ExpandedNodeId.of(ushort(0), "test");

    assertEquals("s=test", xni.toParseableString());
  }

  @Test
  public void parseNamespaceUriContainingEquals() {
    ExpandedNodeId xni =
        ExpandedNodeId.parse(
            "nsu=http://softing.com/dataFEEDSIS/nsuri?conn=Demo&uri=http://opcfoundation.org/UA/;i=85");

    assertEquals(
        "http://softing.com/dataFEEDSIS/nsuri?conn=Demo&uri=http://opcfoundation.org/UA/",
        xni.getNamespaceUri());

    assertEquals(uint(85), xni.getIdentifier());
  }

  @Test
  public void parseIdentifierContainingSemiColons() {
    ExpandedNodeId xni = ExpandedNodeId.parse("nsu=http://foo.com/bar;s=O=::/#pc;B=::/#pc;S=pc;");

    assertEquals("http://foo.com/bar", xni.getNamespaceUri());
    assertEquals("O=::/#pc;B=::/#pc;S=pc;", xni.getIdentifier());
  }

  @Test
  public void reindex() {
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("test1");
    namespaceTable.add("test2");

    ExpandedNodeId xni1 = ExpandedNodeId.of(ushort(1), "test");

    ExpandedNodeId xni2 = xni1.reindex(namespaceTable, "test2");

    assertEquals(ushort(2), xni2.getNamespaceIndex());
  }

  @ParameterizedTest
  @MethodSource("isLocalProvider")
  public void isLocal(ExpandedNodeId expandedNodeId, boolean expected, String message) {
    assertEquals(expected, expandedNodeId.isLocal(), message);
  }

  @ParameterizedTest
  @MethodSource("isLocalServerTableProvider")
  public void isLocalServerTable(
      ExpandedNodeId expandedNodeId, ServerTable serverTable, boolean expected, String message) {
    assertEquals(expected, expandedNodeId.isLocal(serverTable), message);
  }

  public static Object[][] isLocalProvider() {
    return new Object[][] {
      // Test isLocal() without ServerTable
      {
        ExpandedNodeId.of(ushort(0), "test"),
        true,
        "ExpandedNodeId with server index 0 should be local"
      },
      {
        new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(0), "test"),
        false,
        "ExpandedNodeId with server index > 0 should not be local"
      },
      {
        new ExpandedNodeId(ServerReference.of("uri:server"), NamespaceReference.of(0), "test"),
        false,
        "ExpandedNodeId with server URI should not be local"
      }
    };
  }

  public static Object[][] isLocalServerTableProvider() {
    var serverTable = new ServerTable();
    serverTable.add("uri:server1");
    serverTable.add("uri:server2");

    return new Object[][] {
      // Test isLocal(ServerTable)
      {
        ExpandedNodeId.of(ushort(0), "test"),
        serverTable,
        true,
        "ExpandedNodeId with server index 0 should be local with ServerTable"
      },
      {
        new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(0), "test"),
        serverTable,
        false,
        "ExpandedNodeId with server index > 0 should not be local with ServerTable"
      },
      {
        new ExpandedNodeId(ServerReference.of("uri:server1"), NamespaceReference.of(0), "test"),
        serverTable,
        true,
        "ExpandedNodeId with server URI at index 0 should be local"
      },
      {
        new ExpandedNodeId(ServerReference.of("uri:server2"), NamespaceReference.of(0), "test"),
        serverTable,
        false,
        "ExpandedNodeId with server URI at index > 0 should not be local"
      }
    };
  }
}
