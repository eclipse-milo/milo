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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.Test;

public class ExpandedNodeIdTest {

  private final NamespaceTable namespaceTable = new NamespaceTable();

  @Test
  public void testLocal() {
    namespaceTable.add("uri:test");

    ExpandedNodeId xni0 = new ExpandedNodeId(ushort(0), null, "test");
    assertTrue(xni0.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni1 = new ExpandedNodeId(ushort(1), null, "test");
    assertTrue(xni1.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni2 = new ExpandedNodeId(ushort(99), namespaceTable.get(0), "test");
    assertTrue(xni2.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni3 = new ExpandedNodeId(ushort(99), namespaceTable.get(1), "test");
    assertTrue(xni3.toNodeId(namespaceTable).isPresent());

    ExpandedNodeId xni4 = new ExpandedNodeId(ushort(99), "uri:notpresent", "test");
    assertFalse(xni4.toNodeId(namespaceTable).isPresent());
  }

  @Test
  public void testIsNull() {
    assertTrue(ExpandedNodeId.newBuilder().setIdentifier(uint(0)).build().isNull());
    assertTrue(ExpandedNodeId.newBuilder().setIdentifier((String) null).build().isNull());
    assertTrue(ExpandedNodeId.newBuilder().setIdentifier("").build().isNull());
    assertTrue(ExpandedNodeId.newBuilder().setIdentifier(new UUID(0, 0)).build().isNull());
    assertTrue(ExpandedNodeId.newBuilder().setIdentifier(ByteString.NULL_VALUE).build().isNull());
    assertTrue(
        ExpandedNodeId.newBuilder().setIdentifier(ByteString.of(new byte[0])).build().isNull());
  }

  @Test
  public void testEqualityWithNodeId() {
    {
      ExpandedNodeId xni =
          ExpandedNodeId.newBuilder().setNamespaceIndex(0).setIdentifier("foo").build();

      NodeId nodeId = new NodeId(0, "foo");

      assertTrue(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni =
          ExpandedNodeId.newBuilder()
              .setNamespaceUri(Namespaces.OPC_UA)
              .setIdentifier("foo")
              .build();

      NodeId nodeId = new NodeId(0, "foo");

      assertTrue(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni =
          ExpandedNodeId.newBuilder()
              .setNamespaceIndex(0)
              .setIdentifier("foo")
              .setServerIndex(1)
              .build();

      NodeId nodeId = new NodeId(0, "foo");

      assertFalse(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni =
          ExpandedNodeId.newBuilder().setNamespaceIndex(1).setIdentifier("foo").build();

      NodeId nodeId = new NodeId(0, "foo");

      assertFalse(xni.equalTo(nodeId));
    }

    {
      ExpandedNodeId xni =
          ExpandedNodeId.newBuilder().setNamespaceUri("uri:not:found").setIdentifier("foo").build();

      NodeId nodeId = new NodeId(0, "foo");

      assertFalse(xni.equalTo(nodeId));
    }
  }

  @Test
  public void testToParseableString() {
    String withoutNamespaceUri = new ExpandedNodeId(ushort(1), null, uint(0)).toParseableString();
    assertEquals("ns=1;i=0", withoutNamespaceUri);

    String withNamespaceUri =
        new ExpandedNodeId(ushort(0), "urn:test", uint(0)).toParseableString();
    assertEquals("nsu=urn:test;i=0", withNamespaceUri);

    String withServerIndex =
        new ExpandedNodeId(ushort(0), "urn:test", uint(0), uint(1)).toParseableString();
    assertEquals("svr=1;nsu=urn:test;i=0", withServerIndex);
  }

  @Test
  public void absolute() {
    ExpandedNodeId xni = new ExpandedNodeId(ushort(0), null, "foo");
    assertTrue(xni.isRelative());

    ExpandedNodeId absolute = xni.absolute(new NamespaceTable()).orElseThrow(RuntimeException::new);
    assertTrue(absolute.isAbsolute());
    assertEquals(Namespaces.OPC_UA, absolute.getNamespaceUri());
  }

  @Test
  public void relative() {
    ExpandedNodeId xni = new ExpandedNodeId(ushort(99), Namespaces.OPC_UA, "foo");
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
    ExpandedNodeId xni = new ExpandedNodeId(ushort(0), null, "test");

    assertEquals("ns=0;s=test", xni.toParseableString());
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

    ExpandedNodeId xni1 = new ExpandedNodeId(ushort(1), null, "test");

    ExpandedNodeId xni2 = xni1.reindex(namespaceTable, "test2");

    assertEquals(ushort(2), xni2.getNamespaceIndex());
  }
}
