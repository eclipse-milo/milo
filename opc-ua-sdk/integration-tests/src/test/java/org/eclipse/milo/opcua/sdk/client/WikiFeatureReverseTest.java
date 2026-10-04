/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectCandidateSnapshot;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectListener;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTargetHandle;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(30)
class WikiFeatureReverseTest extends AbstractClientServerTest {
  // Establish a Session over a server-opened socket and remove the target after use.
  @Test
  void reverseConnectionReadsAndRemovesItsServerTarget() throws Exception {
    DataValue value = reverseRead(server, client.getConfig());
    assertTrue(value.statusCode().isGood());
    assertInstanceOf(DateTime.class, value.value().value());
    assertTrue(server.getReverseConnectTargetSnapshots().isEmpty());
  }

  // The caller deadline cancels nothing; disconnect removes the selector and manager close unbinds.
  @Test
  void noInboundServerTimesOutAndDisconnectReleasesTheListener() throws Exception {
    InetSocketAddress bound;
    try (var manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
            .build()) {
      manager.startup();
      bound = (InetSocketAddress) manager.snapshot().listeners().get(0).boundAddress();
      String endpointUrl = client.getConfig().getEndpoint().getEndpointUrl();
      String serverUri = client.getConfig().getEndpoint().getServer().getApplicationUri();
      OpcUaClient pending =
          OpcUaClient.createReverseConnect(
              client.getConfig(),
              manager,
              ReverseConnectSelector.byServerUriAndEndpointUrl(serverUri, endpointUrl));
      try {
        assertThrows(
            TimeoutException.class, () -> pending.connectAsync().get(100, TimeUnit.MILLISECONDS));
      } finally {
        pending.disconnectAsync().get(10, TimeUnit.SECONDS);
      }
      var candidatePending = new CompletableFuture<ReverseConnectCandidateSnapshot>();
      manager.addListener(
          new ReverseConnectListener() {
            @Override
            public void onCandidatePending(ReverseConnectCandidateSnapshot candidate) {
              candidatePending.complete(candidate);
            }
          });
      var target =
          server.addReverseConnectTarget(
              ReverseConnectTarget.builder()
                  .setClientListenerUrl("opc.tcp://127.0.0.1:" + bound.getPort())
                  .setEndpointUrl(endpointUrl)
                  .build());
      try {
        var candidate = candidatePending.get(10, TimeUnit.SECONDS);
        assertEquals(0, manager.snapshot().claimedCount());
        assertTrue(
            manager.snapshot().pendingCandidates().stream()
                .anyMatch(value -> value.id().equals(candidate.id())));
      } finally {
        target.remove().get(10, TimeUnit.SECONDS);
      }
    }
    try (var rebound = new ServerSocket()) {
      rebound.setReuseAddress(true);
      rebound.bind(bound);
      assertTrue(rebound.isBound());
    }
  }

  // snippet:reverse:start
  static DataValue reverseRead(OpcUaServer server, OpcUaClientConfig config) throws Exception {
    try (var manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
            .build()) {
      manager.startup();
      InetSocketAddress bound =
          (InetSocketAddress) manager.snapshot().listeners().get(0).boundAddress();
      String endpointUrl = config.getEndpoint().getEndpointUrl();
      String serverUri = config.getEndpoint().getServer().getApplicationUri();
      OpcUaClient reverse =
          OpcUaClient.createReverseConnect(
              config,
              manager,
              ReverseConnectSelector.byServerUriAndEndpointUrl(serverUri, endpointUrl));
      ReverseConnectTargetHandle target =
          server.addReverseConnectTarget(
              ReverseConnectTarget.builder()
                  .setClientListenerUrl("opc.tcp://127.0.0.1:" + bound.getPort())
                  .setEndpointUrl(endpointUrl)
                  .setRegistrationPeriod(uint(1_000))
                  .setConnectTimeout(uint(5_000))
                  .build());
      try {
        reverse.connectAsync().get(10, TimeUnit.SECONDS);
        DataValue value =
            reverse.readValue(
                0.0, TimestampsToReturn.Both, NodeIds.Server_ServerStatus_CurrentTime);
        if (!value.statusCode().isGood()) throw new UaException(value.statusCode());
        return value;
      } finally {
        try {
          reverse.disconnectAsync().get(10, TimeUnit.SECONDS);
        } finally {
          target.remove().get(10, TimeUnit.SECONDS);
        }
      }
    }
  }
  // snippet:reverse:end
}
