/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import static org.testng.Assert.assertEquals;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExpandedNodeIdSerializationTest extends BinarySerializationFixture {

  @DataProvider
  public Object[][] getExpandedNodeIds() {
    return new Object[][] {
      {new ExpandedNodeId(ushort(0), null, uint(0))},
      {new ExpandedNodeId(ushort(0), null, "hello, world")},
      {new ExpandedNodeId(ushort(0), null, UUID.randomUUID())},
      {new ExpandedNodeId(ushort(0), null, ByteString.of(new byte[] {0, 1, 2, 3}))},
      {new ExpandedNodeId(ushort(1), null, uint(0), uint(1))},
      {new ExpandedNodeId(ushort(1), null, "hello, world", uint(1))},
      {new ExpandedNodeId(ushort(1), null, UUID.randomUUID(), uint(1))},
      {new ExpandedNodeId(ushort(1), null, ByteString.of(new byte[] {0, 1, 2, 3}), uint(1))},
      {new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, uint(0))},
      {new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "hello, world")},
      {new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, UUID.randomUUID())},
      {new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, ByteString.of(new byte[] {0, 1, 2, 3}))},
    };
  }

  @Test(
      dataProvider = "getExpandedNodeIds",
      description = "ExpandedNodeId is round-trip serializable.")
  public void testExpandedNodeIdRoundTrip(ExpandedNodeId nodeId) throws Exception {
    writer.encodeExpandedNodeId(nodeId);
    ExpandedNodeId decoded = reader.decodeExpandedNodeId();

    assertEquals(decoded, nodeId);
  }
}
