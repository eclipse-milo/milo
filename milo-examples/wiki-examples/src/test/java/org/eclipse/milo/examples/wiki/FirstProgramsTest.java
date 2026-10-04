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

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

@Timeout(45)
class FirstProgramsTest {

  @TempDir Path directory;

  @AfterAll
  static void releaseResources() {
    Stack.releaseSharedResources();
  }

  // The first two tutorials must work together, expose a browsable read-only Variable, and stop.
  @Test
  void firstProgramsReadBrowseRejectWritesAndStopListening() throws Exception {
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
    // Test listener closure without requiring immediate rebinding after connected TCP traffic.
    var address = new InetSocketAddress("127.0.0.1", port);
    try (var probe = new Socket()) {
      assertThrows(
          ConnectException.class,
          () -> probe.connect(address, 1_000),
          "Tutorial listener still accepts connections at " + address);
    }
  }

  // A common first-run error must complete exceptionally and still permit the shutdown path.
  @Test
  void occupiedPortFailsStartupAndCanBeShutDown() throws Exception {
    try (var occupied = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      OpcUaServer server = FirstServer.create(occupied.getLocalPort());
      try {
        ExecutionException failure =
            assertThrows(
                ExecutionException.class, () -> server.startup().get(10, TimeUnit.SECONDS));
        assertEquals(
            StatusCodes.Bad_ConfigurationError,
            UaException.extract(failure).orElseThrow().getStatusCode().getValue());
        assertEquals("No endpoints bound", UaException.extract(failure).orElseThrow().getMessage());
      } finally {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }
  }

  // Discovery failure is observable before a client Session exists, including the fallback URL.
  @Test
  void stoppedServerAndMismatchedEndpointReportTheirActualStatuses() throws Exception {
    int port;
    try (var reservation = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      port = reservation.getLocalPort();
    }
    Exception refused =
        assertThrows(
            Exception.class,
            () -> FirstClient.readTemperature("opc.tcp://127.0.0.1:" + port + "/wiki"));
    assertEquals(
        StatusCodes.Bad_ConnectionRejected,
        UaException.extract(refused).orElseThrow().getStatusCode().getValue());
    OpcUaServer server = FirstServer.create(port);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      Exception mismatch =
          assertThrows(
              Exception.class,
              () -> FirstClient.readTemperature("opc.tcp://localhost:" + port + "/wiki"));
      UaException cause = UaException.extract(mismatch).orElseThrow();
      assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, cause.getStatusCode().getValue());
      assertTrue(cause.getMessage().contains("/wiki/discovery"));
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // A reachable endpoint with no tutorial namespace takes the displayed helper's own error path.
  @Test
  void missingNamespaceFailsTheTutorialHelperAndClosesItsSession() throws Exception {
    int port;
    try (var reservation = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      port = reservation.getLocalPort();
    }
    OpcUaServer template = FirstServer.create(port);
    OpcUaServer server =
        new OpcUaServer(
            template.getConfig(),
            profile -> new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()));
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      IllegalStateException failure =
          assertThrows(
              IllegalStateException.class,
              () -> FirstClient.readTemperature("opc.tcp://127.0.0.1:" + port + "/wiki"));
      assertEquals("Server does not expose the tutorial namespace", failure.getMessage());
      assertTrue(server.getSessionManager().getAllSessions().isEmpty());
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
      template.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // The displayed helper must reject sample quality and payload type, not just connection errors.
  @Test
  void unexpectedTypeAndBadQualityTakeTheTutorialHelpersFailurePaths() throws Exception {
    int port;
    try (var reservation = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      port = reservation.getLocalPort();
    }
    OpcUaServer server = FirstServer.create(port);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      NodeId id =
          new NodeId(server.getNamespaceTable().getIndex(FirstServer.NAMESPACE_URI), "Temperature");
      UaVariableNode node =
          (UaVariableNode) server.getAddressSpaceManager().getManagedNode(id).orElseThrow();
      String url = "opc.tcp://127.0.0.1:" + port + "/wiki";
      node.setValue(new DataValue(new Variant("unexpected")));
      IllegalStateException wrongType =
          assertThrows(IllegalStateException.class, () -> FirstClient.readTemperature(url));
      assertTrue(wrongType.getMessage().startsWith("Expected a Double"));
      node.setValue(new DataValue(new StatusCode(StatusCodes.Bad_OutOfService)));
      IllegalStateException badQuality =
          assertThrows(IllegalStateException.class, () -> FirstClient.readTemperature(url));
      assertTrue(badQuality.getMessage().contains("Read failed"));
      assertTrue(badQuality.getMessage().contains("Bad_OutOfService"));
      assertTrue(server.getSessionManager().getAllSessions().isEmpty());
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }

  // EOF must take the same graceful stop path as Enter, including when launched without a terminal.
  @Test
  void closedStandardInputStopsTheServerProcessAndReleasesItsPort() throws Exception {
    int port;
    try (var reservation = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      port = reservation.getLocalPort();
    }
    Path output = directory.resolve("first-server.log");
    Process process =
        new ProcessBuilder(
                Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                "-cp",
                System.getProperty("java.class.path"),
                FirstServer.class.getName(),
                Integer.toString(port))
            .redirectErrorStream(true)
            .redirectOutput(output.toFile())
            .start();
    try {
      process.getOutputStream().close();
      assertTrue(process.waitFor(30, TimeUnit.SECONDS));
      assertEquals(0, process.exitValue(), Files.readString(output));
      assertTrue(Files.readString(output).contains("press Enter to stop"));
    } finally {
      if (process.isAlive()) {
        process.destroyForcibly();
        process.waitFor(10, TimeUnit.SECONDS);
      }
    }
    // The tutorial listener must be gone, independent of the process's exit status.
    try (var listener = new ServerSocket()) {
      listener.setReuseAddress(true);
      listener.bind(new InetSocketAddress("127.0.0.1", port));
      assertEquals(port, listener.getLocalPort());
    }
  }
}
