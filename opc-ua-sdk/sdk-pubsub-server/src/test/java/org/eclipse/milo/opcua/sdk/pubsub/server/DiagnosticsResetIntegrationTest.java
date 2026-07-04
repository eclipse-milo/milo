/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubCounter;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The client-driven Reset round-trip: a real Call on a per-component Diagnostics Reset zeroes
 * exactly THAT object's counters — node values and collector snapshot alike — clears
 * TimeFirstChange, leaves the child layer untouched (per-object scope), and the counters
 * re-accumulate with a FRESH TimeFirstChange afterwards. The counter ticks are produced
 * deterministically through the minted Status Enable/Disable methods (no traffic needed).
 *
 * <p>The Reset authorization deny coverage lives in {@link
 * RemoteConfigAuthorizationIntegrationTest}; the direct per-object-scope unit coverage lives in
 * {@code PubSubDiagnosticsResetTest} — this class adds the real-wire layer.
 */
class DiagnosticsResetIntegrationTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "rs-conn";
  private static final String WRITER_GROUP = "rs-wg";

  private static SksTestServer testServer;
  private static ServerPubSub serverPubSub;
  private static OpcUaClient client;

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);

    serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config(),
            ServerPubSubOptions.builder()
                .exposeInformationModel(true)
                .allowRemoteConfiguration(true)
                .diagnosticsEnabled(true)
                .build());
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

    client = connect();
  }

  @AfterAll
  static void stopEverything() throws Exception {
    if (client != null) {
      client.disconnect();
    }
    if (serverPubSub != null) {
      serverPubSub.close();
    }
    if (testServer != null) {
      testServer.close();
    }
  }

  @Test
  void resetZeroesThePerObjectCountersAndTheyReaccumulate() throws Exception {
    // tick deterministically: Disable then Enable the CONNECTION by the Status methods —
    // the connection ticks the ByMethod counters, the enabled child group ticks the
    // ByParent counters (Table 311 attribution)
    assertEquals(StatusCode.GOOD, callStatus(CONNECTION, "Disable").getStatusCode());
    assertEquals(StatusCode.GOOD, callStatus(CONNECTION, "Enable").getStatusCode());

    // the Operational-by-method tick lands on the connection's (possibly deferred) final hop
    awaitCounter(CONNECTION, ComponentDiagnostics::stateOperationalByMethod, 1);

    ComponentDiagnostics connection = snapshot(CONNECTION);
    assertEquals(1, connection.stateDisabledByMethod());
    assertNotNull(connection.timeFirstChange(PubSubCounter.STATE_DISABLED_BY_METHOD));

    ComponentDiagnostics group = snapshot(CONNECTION + "/" + WRITER_GROUP);
    assertTrue(group.statePausedByParent() >= 1, "the child group paused with the parent");

    // both surfaces agree pre-reset
    assertEquals(uint(1), goodValue(counterNodeId(CONNECTION, "StateDisabledByMethod")));
    long childPausedBefore = group.statePausedByParent();

    // the real Call: Reset on the CONNECTION's Diagnostics object
    CallMethodResult reset = callReset(CONNECTION);
    assertEquals(StatusCode.GOOD, reset.getStatusCode());

    // that object's counters are zero on BOTH surfaces and TimeFirstChange is cleared
    ComponentDiagnostics afterReset = snapshot(CONNECTION);
    assertEquals(0, afterReset.stateDisabledByMethod());
    assertEquals(0, afterReset.stateOperationalByMethod());
    assertNull(afterReset.timeFirstChange(PubSubCounter.STATE_DISABLED_BY_METHOD));
    assertEquals(uint(0), goodValue(counterNodeId(CONNECTION, "StateDisabledByMethod")));
    assertEquals(uint(0), goodValue(counterNodeId(CONNECTION, "StateOperationalByMethod")));

    // per-object scope: the CHILD group's counters are untouched
    ComponentDiagnostics childAfterReset = snapshot(CONNECTION + "/" + WRITER_GROUP);
    assertEquals(childPausedBefore, childAfterReset.statePausedByParent());
    assertEquals(
        uint((int) childPausedBefore),
        goodValue(counterNodeId(CONNECTION + "/" + WRITER_GROUP, "StatePausedByParent")));

    // counters re-accumulate with a FRESH TimeFirstChange
    assertEquals(StatusCode.GOOD, callStatus(CONNECTION, "Disable").getStatusCode());
    ComponentDiagnostics reaccumulated = snapshot(CONNECTION);
    assertEquals(1, reaccumulated.stateDisabledByMethod());
    assertNotNull(reaccumulated.timeFirstChange(PubSubCounter.STATE_DISABLED_BY_METHOD));
    assertEquals(uint(1), goodValue(counterNodeId(CONNECTION, "StateDisabledByMethod")));

    assertEquals(StatusCode.GOOD, callStatus(CONNECTION, "Enable").getStatusCode());
  }

  // region helpers

  private static ComponentDiagnostics snapshot(String path) {
    ComponentDiagnostics diagnostics = serverPubSub.runtime().diagnostics().snapshot().get(path);
    assertNotNull(diagnostics, path);
    return diagnostics;
  }

  /** Deadline-poll until the path's {@code counter} reaches {@code expected}. */
  private static void awaitCounter(
      String path, java.util.function.ToLongFunction<ComponentDiagnostics> counter, long expected) {

    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (counter.applyAsLong(snapshot(path)) != expected) {
      if (System.nanoTime() > deadline) {
        assertEquals(expected, counter.applyAsLong(snapshot(path)), "counter at " + path);
      }
      java.util.concurrent.locks.LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(25));
    }
  }

  private static CallMethodResult callStatus(String componentPath, String methodName)
      throws UaException {
    return call(
        fragmentNodeId(componentPath + "/Status"),
        fragmentNodeId(componentPath + "/Status/" + methodName));
  }

  private static CallMethodResult callReset(String componentPath) throws UaException {
    return call(
        fragmentNodeId(componentPath + "/Diagnostics"),
        fragmentNodeId(componentPath + "/Diagnostics/Reset"));
  }

  private static CallMethodResult call(NodeId objectId, NodeId methodId) throws UaException {
    CallResponse response =
        client.call(List.of(new CallMethodRequest(objectId, methodId, new Variant[0])));
    CallMethodResult[] results = response.getResults();
    assertTrue(results != null && results.length == 1);
    return results[0];
  }

  private static NodeId counterNodeId(String componentPath, String counter) {
    return fragmentNodeId(componentPath + "/Diagnostics/Counters/" + counter);
  }

  private static NodeId fragmentNodeId(String identifier) {
    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    return new NodeId(idx, "PubSub/" + identifier);
  }

  private static Object goodValue(NodeId nodeId) throws UaException {
    DataValue value = client.readValues(0.0, TimestampsToReturn.Both, List.of(nodeId)).get(0);
    assertTrue(value.getStatusCode().isGood(), nodeId + ": " + value.getStatusCode());
    Object result = value.getValue().getValue();
    assertNotNull(result, nodeId.toString());
    return result;
  }

  /** One enabled connection with one enabled (writer-less) group: no sources, no key material. */
  private static PubSubConfig config() throws SocketException {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4891)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .writerGroup(
                    WriterGroupConfig.builder(WRITER_GROUP)
                        .writerGroupId(ushort(23))
                        .publishingInterval(Duration.ofMillis(200))
                        .build())
                .build())
        .build();
  }

  private static OpcUaClient connect() throws UaException {
    OpcUaClient newClient =
        OpcUaClient.create(
            testServer.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e -> Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))
                    .findFirst(),
            transportConfigBuilder -> {},
            clientConfigBuilder ->
                clientConfigBuilder
                    .setApplicationName(LocalizedText.english("diagnostics reset test client"))
                    .setApplicationUri("urn:eclipse:milo:test:diagnostics-reset-client")
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(5_000)));

    newClient.connect();

    return newClient;
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}
