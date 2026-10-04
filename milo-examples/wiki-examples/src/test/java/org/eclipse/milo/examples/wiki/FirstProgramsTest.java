/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(45)
class FirstProgramsTest {

  @AfterAll
  static void releaseResources() {
    Stack.releaseSharedResources();
  }

  // The first two tutorials must work together, expose a browsable read-only Variable, and stop.
  @Test
  void firstProgramsReadBrowseRejectWritesAndReleaseTheirPort() throws Exception {
    int port;
    try (var reservation = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      port = reservation.getLocalPort();
    }
    OpcUaServer server = FirstServer.create(port);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      String url = "opc.tcp://127.0.0.1:" + port + "/wiki";
      assertEquals(21.5, FirstClient.readTemperature(url));

      OpcUaClient client = OpcUaClient.create(url);
      try {
        client.connect();
        UShort namespaceIndex = client.getNamespaceTable().getIndex(FirstServer.NAMESPACE_URI);
        assertNotNull(namespaceIndex);
        NodeId temperatureId = new NodeId(namespaceIndex, "Temperature");
        assertTrue(
            client.getAddressSpace().browseNodes(NodeIds.ObjectsFolder).stream()
                .anyMatch(node -> temperatureId.equals(node.getNodeId())));
        assertEquals(
            StatusCodes.Bad_NotWritable,
            client
                .writeValues(List.of(temperatureId), List.of(new DataValue(new Variant(22.0))))
                .get(0)
                .getValue());
        assertEquals(
            StatusCodes.Bad_NodeIdUnknown,
            client
                .readValue(0.0, TimestampsToReturn.Both, new NodeId(namespaceIndex, "Missing"))
                .getStatusCode()
                .getValue());
        assertEquals(
            21.5,
            client.readValue(0.0, TimestampsToReturn.Both, temperatureId).getValue().getValue());
      } finally {
        client.disconnect();
      }
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
    try (var rebound = new ServerSocket()) {
      rebound.setReuseAddress(true);
      rebound.bind(new InetSocketAddress("127.0.0.1", port));
      assertEquals(port, rebound.getLocalPort());
    }
  }

  // A common first-run error must complete exceptionally and still permit the shutdown path.
  @Test
  void occupiedPortFailsStartupAndCanBeShutDown() throws Exception {
    try (var occupied = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      OpcUaServer server = FirstServer.create(occupied.getLocalPort());
      try {
        assertThrows(ExecutionException.class, () -> server.startup().get(10, TimeUnit.SECONDS));
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }
}
