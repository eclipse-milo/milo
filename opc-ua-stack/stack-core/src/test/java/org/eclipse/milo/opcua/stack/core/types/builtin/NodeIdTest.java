/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.testng.annotations.Test;

public class NodeIdTest {

  @Test
  public void testParseLargerThanIntMax() {
    long i = Integer.MAX_VALUE + 1L;
    NodeId nodeId = NodeId.parse("ns=1;i=" + i);

    assertNotNull(nodeId);
    assertEquals(nodeId.getIdentifier(), uint(i));
  }

  @Test
  public void testParseInvalid() {
    assertNull(NodeId.parseOrNull(""));
    assertNull(NodeId.parseOrNull("n"));
    assertNull(NodeId.parseOrNull("ns"));
    assertNull(NodeId.parseOrNull("ns="));
    assertNull(NodeId.parseOrNull("ns=0"));
    assertNull(NodeId.parseOrNull("ns=0;"));
    assertNull(NodeId.parseOrNull("ns=0;s"));
  }

  @Test
  public void testParseInvalidInt() {
    assertThrows(UaRuntimeException.class, () -> NodeId.parse("ns=0;i=" + UInteger.MAX_VALUE + 1));
    assertThrows(UaRuntimeException.class, () -> NodeId.parse("ns=0;i=" + -1));
  }

  @Test
  public void testParseNamespaceIndex() {
    for (int i = 0; i < UShort.MAX_VALUE; i++) {
      NodeId nodeId = NodeId.parseOrNull("ns=" + i + ";i=" + i);
      assertNotNull(nodeId);
      assertEquals(nodeId.getNamespaceIndex(), ushort(i));
      assertEquals(nodeId.getIdentifier(), uint(i));
    }
  }

  @Test
  public void testParseInt() {
    long[] is = new long[] {0, UByte.MAX_VALUE, UShort.MAX_VALUE, UInteger.MAX_VALUE};

    for (long i : is) {
      assertNotNull(NodeId.parseOrNull("i=" + i));
      assertNotNull(NodeId.parseOrNull("ns=0;i=" + i));
    }
  }

  @Test
  public void testParseString() {
    assertNotNull(NodeId.parseOrNull("s="));
    assertNotNull(NodeId.parseOrNull("s=foo"));
    assertNotNull(NodeId.parseOrNull("ns=0;s="));
    assertNotNull(NodeId.parseOrNull("ns=0;s=foo"));
  }

  @Test
  public void testParseGuid() {
    for (int i = 0; i < 100; i++) {
      UUID uuid = UUID.randomUUID();
      {
        NodeId nodeId = NodeId.parseOrNull("g=" + uuid.toString());
        assertNotNull(nodeId);
        assertEquals(nodeId.getIdentifier(), uuid);
      }
      {
        NodeId nodeId = NodeId.parseOrNull("ns=0;g=" + uuid.toString());
        assertNotNull(nodeId);
        assertEquals(nodeId.getIdentifier(), uuid);
      }
    }
  }

  @Test
  public void testParseByteString() {
    Random r = new Random();

    for (int i = 0; i < 100; i++) {
      byte[] bs = new byte[r.nextInt(64) + 1];
      r.nextBytes(bs);
      String bss = Base64.getEncoder().encodeToString(bs);

      {
        NodeId nodeId = NodeId.parseOrNull("b=" + bss);
        assertNotNull(nodeId);
        assertEquals(nodeId.getIdentifier(), ByteString.of(bs));
      }
      {
        NodeId nodeId = NodeId.parseOrNull("ns=0;b=" + bss);
        assertNotNull(nodeId);
        assertEquals(nodeId.getIdentifier(), ByteString.of(bs));
      }
    }
  }

  @Test
  public void testEqualityWithExpandedNodeId() {
    NodeId nodeId = new NodeId(0, "foo");

    {
      ExpandedNodeId xni = nodeId.expanded();

      assertTrue(nodeId.equalTo(xni));
    }

    {
      ExpandedNodeId xni = new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "foo");

      assertTrue(nodeId.equalTo(xni));
    }

    {
      ExpandedNodeId xni = new ExpandedNodeId(ushort(1), Namespaces.OPC_UA, "foo");

      assertTrue(nodeId.equalTo(xni));
    }

    {
      ExpandedNodeId xni = new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "foo", uint(1));

      assertFalse(nodeId.equalTo(xni));
    }

    {
      ExpandedNodeId xni = new ExpandedNodeId(ushort(0), "uri:foo:bar", "foo");

      assertFalse(nodeId.equalTo(xni));
    }
  }

  @Test
  public void testStringWithNewlineCharacters() {
    NodeId nodeId1 = NodeId.parse("s=foo\nbar\nbaz");
    assertNotNull(nodeId1);
    assertNotNull(NodeId.parse(nodeId1.toParseableString()));

    NodeId nodeId2 = NodeId.parse("ns=2;s=foo\n\bar\nbaz");
    assertNotNull(nodeId2);
    assertNotNull(NodeId.parse(nodeId2.toParseableString()));
  }

  @Test
  public void testExpandedWithNamespaceTable() {
    NamespaceTable namespaceTable = new NamespaceTable();
    namespaceTable.add("urn:test");

    NodeId nodeId = new NodeId(1, "foo");
    ExpandedNodeId xni = nodeId.expanded(namespaceTable);

    assertEquals(xni.getNamespaceUri(), "urn:test");
  }

  @Test
  public void parseIdentifierContainingSemiColons() {
    NodeId nodeId = NodeId.parse("ns=14;s=O=::/#pc;B=::/#pc;S=pc;");

    assertEquals(nodeId.getNamespaceIndex(), ushort(14));
    assertEquals(nodeId.getIdentifier(), "O=::/#pc;B=::/#pc;S=pc;");
  }
}
