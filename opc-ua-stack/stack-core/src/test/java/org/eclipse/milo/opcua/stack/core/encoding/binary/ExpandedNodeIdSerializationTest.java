/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.NamespaceReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId.ServerReference;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ExpandedNodeIdSerializationTest extends BinarySerializationFixture {

  public static Object[][] getExpandedNodeIds() {
    return new Object[][] {
      {ExpandedNodeId.of(ushort(0), uint(0))},
      {ExpandedNodeId.of(ushort(0), "hello, world")},
      {ExpandedNodeId.of(ushort(0), UUID.randomUUID())},
      {ExpandedNodeId.of(ushort(0), ByteString.of(new byte[] {0, 1, 2, 3}))},
      {new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(1), uint(0))},
      {new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(1), "hello, world")},
      {new ExpandedNodeId(ServerReference.of(1), NamespaceReference.of(1), UUID.randomUUID())},
      {
        new ExpandedNodeId(
            ServerReference.of(1), NamespaceReference.of(1), ByteString.of(new byte[] {0, 1, 2, 3}))
      },
      {ExpandedNodeId.of(Namespaces.OPC_UA, uint(0))},
      {ExpandedNodeId.of(Namespaces.OPC_UA, "hello, world")},
      {ExpandedNodeId.of(Namespaces.OPC_UA, UUID.randomUUID())},
      {ExpandedNodeId.of(Namespaces.OPC_UA, ByteString.of(new byte[] {0, 1, 2, 3}))},
    };
  }

  @ParameterizedTest
  @MethodSource("getExpandedNodeIds")
  @DisplayName("ExpandedNodeId is round-trip serializable.")
  public void testExpandedNodeIdRoundTrip(ExpandedNodeId nodeId) throws Exception {
    writer.encodeExpandedNodeId(nodeId);
    ExpandedNodeId decoded = reader.decodeExpandedNodeId();

    NamespaceTable namespaceTable = writer.getEncodingContext().getNamespaceTable();

    assertEquals(nodeId.getIdentifier(), decoded.getIdentifier());
    assertEquals(nodeId.getServerIndex(), decoded.getServerIndex());
    assertEquals(nodeId.getNamespaceUri(namespaceTable), decoded.getNamespaceUri(namespaceTable));
    assertEquals(
        nodeId.getNamespaceIndex(namespaceTable), decoded.getNamespaceIndex(namespaceTable));
  }
}
