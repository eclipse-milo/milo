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

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(30)
class FirstProgramsTest {

  @AfterAll
  static void releaseSharedResources() {
    Stack.releaseSharedResources();
  }

  // The first-client tutorial must read the value the first-server tutorial publishes.
  @Test
  void firstClientReadsFirstServerTemperature() throws Exception {
    int port;
    try (var socket = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
      port = socket.getLocalPort();
    }

    OpcUaServer server = FirstServer.create(port);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      assertEquals(21.5, FirstClient.readTemperature("opc.tcp://127.0.0.1:" + port + "/wiki"));
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
  }
}
