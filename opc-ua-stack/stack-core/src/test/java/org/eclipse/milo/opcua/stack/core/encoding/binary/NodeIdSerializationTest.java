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

import static org.testng.Assert.assertEquals;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NodeIdSerializationTest extends BinarySerializationFixture {

  @DataProvider
  public Object[][] getNodeIds() {
    return new Object[][] {
      {new NodeId(0, 0)},
      {new NodeId(0, 1)},
      {new NodeId(0, 255)},
      {new NodeId(0, 256)},
      {new NodeId(1, 65535)},
      {new NodeId(1, 65536)},
      {new NodeId(255, 65535)},
      {new NodeId(255, 65536)},
      {new NodeId(1234, 567890)},
      {new NodeId(0, "hello, world")},
      {new NodeId(1, "hello, world")},
      {new NodeId(0, UUID.randomUUID())},
      {new NodeId(1, UUID.randomUUID())},
      {new NodeId(0, new ByteString(new byte[] {1, 2, 3, 4}))},
      {new NodeId(1, new ByteString(new byte[] {1, 2, 3, 4}))},
      {NodeId.NULL_NUMERIC},
      {NodeId.NULL_STRING},
      {NodeId.NULL_GUID},
      {NodeId.NULL_OPAQUE}
    };
  }

  @Test(dataProvider = "getNodeIds", description = "NodeId is round-trip serializable.")
  public void testNodeIdRoundTrip(NodeId nodeId) throws Exception {
    writer.encodeNodeId(nodeId);
    NodeId decoded = reader.decodeNodeId();

    assertEquals(decoded, nodeId);
  }
}
