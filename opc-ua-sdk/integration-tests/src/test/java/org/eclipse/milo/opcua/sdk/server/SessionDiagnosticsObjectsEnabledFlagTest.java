/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Per-Session diagnostics Objects beneath SessionsDiagnosticsSummary must exist only while the
 * server's diagnostics EnabledFlag is true (Part 5 §6.3.3: dynamic diagnostic Nodes such as the
 * Session Nodes do not appear in the AddressSpace while diagnostics are turned off).
 *
 * <p>Session listener callbacks run on a queue, so a test that needs a callback to arrive after a
 * flag change stalls the queue with a listener of its own, registered after the server starts so it
 * runs behind the diagnostics listener for the same Session.
 */
class SessionDiagnosticsObjectsEnabledFlagTest {

  private static final String SESSION_A = "SessionA";
  private static final String SESSION_B = "SessionB";

  private OpcUaServer server;
  private ServerDiagnosticsTypeNode diagnosticsNode;
  private SessionListenerProbe probe;
  private OpcUaClient clientA;
  private OpcUaClient clientB;

  @BeforeEach
  void startServer() throws Exception {
    server = TestServer.create().getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();

    probe = new SessionListenerProbe();
    server.getSessionManager().addSessionListener(probe);
  }

  @AfterEach
  void stopServer() throws Exception {
    probe.releaseAll();

    for (OpcUaClient client : Arrays.asList(clientA, clientB)) {
      if (client != null) {
        try {
          client.disconnectAsync().get(5, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // The assertion path reports failures.
        }
      }
    }

    server.getSessionManager().removeSessionListener(probe);
    server.shutdown().get(10, TimeUnit.SECONDS);
  }

  @Test
  void sessionObjectsExistOnlyWhileDiagnosticsAreEnabled() throws Exception {
    assertEquals(Boolean.FALSE, diagnosticsNode.getEnabledFlag(), "diagnostics start disabled");

    clientA = connect(SESSION_A);
    clientB = connect(SESSION_B);
    probe.awaitCreated(SESSION_A);
    probe.awaitCreated(SESSION_B);

    assertEquals(Set.of(), sessionObjectNames(clientA), "no Session Objects while disabled");
    assertEquals(
        Set.of("SessionDiagnosticsArray", "SessionSecurityDiagnosticsArray"),
        summaryVariableNames(clientA),
        "the static arrays stay in place while disabled");

    diagnosticsNode.setEnabledFlag(true);

    Map<String, NodeId> objects = sessionObjects(clientA);
    assertEquals(
        Set.of(SESSION_A, SESSION_B), objects.keySet(), "both Objects appear once enabled");
    for (NodeId object : objects.values()) {
      DataValue value = readSessionDiagnosticsValue(clientA, object);
      assertTrue(
          value.statusCode().isGood(),
          "SessionDiagnostics reads Good once enabled, was " + value.statusCode());
    }

    diagnosticsNode.setEnabledFlag(false);

    assertEquals(Set.of(), sessionObjectNames(clientA), "Objects are removed when disabled again");
  }

  // A recreated Object starts its SubscriptionDiagnosticsArray with the flag already on, so it must
  // add elements for the Subscriptions the Session created while diagnostics were off.
  @Test
  void subscriptionCreatedWhileDisabledHasElementAfterEnable() throws Exception {
    clientA = connect(SESSION_A);
    probe.awaitCreated(SESSION_A);

    var subscription = new OpcUaSubscription(clientA);
    subscription.create();
    String subscriptionId = subscription.getSubscriptionId().orElseThrow().toString();

    diagnosticsNode.setEnabledFlag(true);

    NodeId object = sessionObjects(clientA).get(SESSION_A);
    NodeId subscriptionArray = child(clientA, object, "SubscriptionDiagnosticsArray");
    List<ReferenceDescription> elements = browse(clientA, subscriptionArray);

    assertEquals(
        List.of(subscriptionId),
        elements.stream().map(r -> r.getBrowseName().name()).toList(),
        "one element for the Subscription created while disabled");

    NodeId element =
        elements.get(0).getNodeId().toNodeId(clientA.getNamespaceTable()).orElseThrow();
    DataValue value = clientA.readValue(0.0, TimestampsToReturn.Neither, element);
    assertTrue(value.statusCode().isGood(), "element reads Good, was " + value.statusCode());
  }

  // A session-created callback can be queued behind another listener and run after the flag turns
  // off. It must check the flag instead of recreating an Object that was just removed.
  @Test
  void sessionCreatedCallbackRunningAfterDisableCreatesNoObject() throws Exception {
    diagnosticsNode.setEnabledFlag(true);

    probe.blockCreated(SESSION_A);
    clientA = connect(SESSION_A);
    probe.awaitBlocked(SESSION_A);

    // SessionB's callbacks are now queued behind the blocked SessionA callback.
    clientB = connect(SESSION_B);

    diagnosticsNode.setEnabledFlag(false);

    probe.releaseAll();
    probe.awaitCreated(SESSION_B);

    assertEquals(
        Set.of(),
        sessionObjectNames(clientA),
        "a callback that runs after the flag turned off must not create an Object");
  }

  // Turning the flag on creates Objects for all current Sessions, including one whose created
  // callback is still queued. That callback must not add a second Object for the same Session.
  @Test
  void sessionCreatedCallbackRunningAfterEnableDoesNotDuplicateObject() throws Exception {
    probe.blockCreated(SESSION_A);
    clientA = connect(SESSION_A);
    probe.awaitBlocked(SESSION_A);

    clientB = connect(SESSION_B);

    diagnosticsNode.setEnabledFlag(true);

    probe.releaseAll();
    probe.awaitCreated(SESSION_B);

    List<String> names =
        browse(clientA, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
            .filter(r -> r.getNodeClass() == NodeClass.Object)
            .map(r -> r.getBrowseName().name())
            .sorted()
            .toList();

    assertEquals(List.of(SESSION_A, SESSION_B), names, "exactly one Object per Session");
  }

  private OpcUaClient connect(String sessionName) throws Exception {
    OpcUaClient client = TestClient.create(server, cfg -> cfg.setSessionName(() -> sessionName));
    client.connectAsync().get(10, TimeUnit.SECONDS);
    return client;
  }

  private static Set<String> sessionObjectNames(OpcUaClient client) throws UaException {
    return sessionObjects(client).keySet();
  }

  private static Map<String, NodeId> sessionObjects(OpcUaClient client) throws UaException {
    return browse(client, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
        .filter(r -> r.getNodeClass() == NodeClass.Object)
        .collect(
            Collectors.toMap(
                r -> r.getBrowseName().name(),
                r -> r.getNodeId().toNodeId(client.getNamespaceTable()).orElseThrow()));
  }

  private static Set<String> summaryVariableNames(OpcUaClient client) throws UaException {
    return browse(client, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
        .filter(r -> r.getNodeClass() == NodeClass.Variable)
        .map(r -> r.getBrowseName().name())
        .collect(Collectors.toSet());
  }

  private static DataValue readSessionDiagnosticsValue(OpcUaClient client, NodeId object)
      throws UaException {

    NodeId variable = child(client, object, "SessionDiagnostics");

    return client.readValue(0.0, TimestampsToReturn.Neither, variable);
  }

  private static NodeId child(OpcUaClient client, NodeId parent, String browseName)
      throws UaException {

    return browse(client, parent).stream()
        .filter(r -> r.getBrowseName().name().equals(browseName))
        .findFirst()
        .flatMap(r -> r.getNodeId().toNodeId(client.getNamespaceTable()))
        .orElseThrow(() -> new AssertionError("no child named " + browseName));
  }

  private static List<ReferenceDescription> browse(OpcUaClient client, NodeId nodeId)
      throws UaException {

    var description =
        new BrowseDescription(
            nodeId,
            BrowseDirection.Forward,
            NodeIds.HierarchicalReferences,
            true,
            uint(0),
            uint(BrowseResultMask.All.getValue()));

    ReferenceDescription[] references = client.browse(description).getReferences();

    return references == null ? List.of() : List.of(references);
  }

  /**
   * Records session-created callbacks by Session name and can hold the listener queue inside the
   * callback for a chosen Session. Registered after server startup, so for each Session it runs
   * after the diagnostics listener has handled that Session.
   */
  private static class SessionListenerProbe implements SessionListener {

    private final Map<String, CountDownLatch> created = new ConcurrentHashMap<>();
    private final Map<String, CountDownLatch> blocked = new ConcurrentHashMap<>();
    private final Map<String, CountDownLatch> release = new ConcurrentHashMap<>();

    @Override
    public void onSessionCreated(Session session) {
      String name = session.getSessionName();

      CountDownLatch releaseLatch = release.get(name);
      if (releaseLatch != null) {
        latch(blocked, name).countDown();
        try {
          if (!releaseLatch.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("blocked session callback was never released");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IllegalStateException(e);
        }
      }

      latch(created, name).countDown();
    }

    void blockCreated(String sessionName) {
      release.put(sessionName, new CountDownLatch(1));
    }

    void awaitBlocked(String sessionName) throws InterruptedException {
      assertTrue(
          latch(blocked, sessionName).await(10, TimeUnit.SECONDS),
          sessionName + " callback did not reach the probe");
    }

    void awaitCreated(String sessionName) throws InterruptedException {
      assertTrue(
          latch(created, sessionName).await(10, TimeUnit.SECONDS),
          sessionName + " created callback did not complete");
    }

    void releaseAll() {
      release.values().forEach(CountDownLatch::countDown);
    }

    private static CountDownLatch latch(Map<String, CountDownLatch> latches, String name) {
      return latches.computeIfAbsent(name, k -> new CountDownLatch(1));
    }
  }
}
