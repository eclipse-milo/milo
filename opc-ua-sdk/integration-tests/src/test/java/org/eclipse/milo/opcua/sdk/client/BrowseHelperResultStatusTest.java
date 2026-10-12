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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.client.AddressSpace.BrowseOptions;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultViewServiceSet;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * How the high-level browse methods backed by {@link BrowseHelper} handle BrowseResult StatusCodes,
 * ContinuationPoints, and ServiceFaults.
 *
 * <p>The View Service Set is the default implementation plus one-shot scripts that change how the
 * next Browse or BrowseNext is answered. Paging uses a small maxReferencesPerNode on the Server
 * Node, which has more than three pages of hierarchical references.
 */
class BrowseHelperResultStatusTest extends AbstractClientServerTest {

  private static final NodeId UNKNOWN_NODE_ID = new NodeId(2, "DoesNotExist");

  /** Small enough that browsing the Server Node takes several BrowseNext calls. */
  private static final int PAGE_SIZE = 3;

  private ScriptedViewServiceSet viewServiceSet;

  @Override
  protected TestServer createTestServer() throws Exception {
    TestServer testServer = TestServer.create();
    OpcUaServer opcUaServer = testServer.getServer();

    viewServiceSet = new ScriptedViewServiceSet(opcUaServer);

    for (EndpointConfig endpoint : opcUaServer.getConfig().getEndpoints()) {
      opcUaServer.addServiceSet(endpoint.getPath(), viewServiceSet);
    }

    return testServer;
  }

  @AfterEach
  void resetScriptAndContinuationPoints() {
    viewServiceSet.reset();
    server
        .getSessionManager()
        .getAllSessions()
        .forEach(s -> s.getBrowseContinuationPoints().clear());
  }

  /** Control: the Server reports an unknown NodeId as Bad_NodeIdUnknown (Part 4 Table 36). */
  @Test
  void lowLevelBrowseOfUnknownNodeReturnsBadNodeIdUnknown() throws UaException {
    BrowseResult result = client.browse(browseDescription(UNKNOWN_NODE_ID));

    assertEquals(StatusCodes.Bad_NodeIdUnknown, result.getStatusCode().value());
  }

  // An empty list would be indistinguishable from a Node that has no references.
  @Test
  void addressSpaceBrowseOfUnknownNodeThrowsBadNodeIdUnknown() {
    UaException e =
        assertThrows(UaException.class, () -> client.getAddressSpace().browse(UNKNOWN_NODE_ID));

    assertEquals(StatusCodes.Bad_NodeIdUnknown, e.getStatusCode().value());
  }

  @Test
  void addressSpaceBrowseNodesOfUnknownNodeThrowsBadNodeIdUnknown() {
    UaException e =
        assertThrows(
            UaException.class, () -> client.getAddressSpace().browseNodes(UNKNOWN_NODE_ID));

    assertEquals(StatusCodes.Bad_NodeIdUnknown, e.getStatusCode().value());
  }

  // A UaNode can outlive the Server Node it was created for.
  @Test
  void uaNodeBrowseAfterServerNodeIsDeletedThrowsBadNodeIdUnknown() throws Exception {
    NodeId nodeId = newNodeId("BrowseHelperResultStatusTest.Deleted");
    NodeId childId = newNodeId("BrowseHelperResultStatusTest.Deleted.Child");

    testNamespace.configure(
        (context, nodeManager) -> {
          var folder =
              new UaFolderNode(
                  context, nodeId, newQualifiedName("Deleted"), LocalizedText.english("Deleted"));
          var child =
              new UaFolderNode(
                  context, childId, newQualifiedName("Child"), LocalizedText.english("Child"));
          nodeManager.addNode(folder);
          nodeManager.addNode(child);
          folder.addOrganizes(child);
        });

    UaNode node = client.getAddressSpace().getNode(nodeId);

    assertFalse(node.browse().isEmpty(), "control: the Node's references before it is deleted");

    testNamespace.configure(
        (context, nodeManager) -> {
          nodeManager.removeNode(childId);
          nodeManager.removeNode(nodeId);
        });

    UaException e = assertThrows(UaException.class, node::browse);

    assertEquals(StatusCodes.Bad_NodeIdUnknown, e.getStatusCode().value());
  }

  /**
   * Part 4 §7.9: a Server frees a Session's ContinuationPoints when a new request needs them, and
   * answers Bad_ContinuationPointInvalid if the Client then uses one. Returning the earlier pages
   * would present a truncated result as complete.
   */
  @Test
  void badStatusOnLaterBrowseNextPageThrows() throws Exception {
    AddressSpace addressSpace = client.getAddressSpace();
    BrowseOptions paged = pagedBrowseOptions();

    int total = addressSpace.browse(NodeIds.Server).size();
    assertTrue(total > 2 * PAGE_SIZE, "precondition: the Server Node needs three or more pages");
    assertEquals(
        total,
        addressSpace.browse(NodeIds.Server, paged).size(),
        "control: unscripted paging returns every reference");

    viewServiceSet.freeContinuationPointsBeforeNextBrowseNext();

    UaException e =
        assertThrows(UaException.class, () -> addressSpace.browse(NodeIds.Server, paged));

    assertEquals(StatusCodes.Bad_ContinuationPointInvalid, e.getStatusCode().value());
  }

  /**
   * Part 4 §5.9.2.4: Uncertain_NotAllNodesAvailable means the results may be incomplete, not that
   * they are wrong. The page's references are kept and its ContinuationPoint is followed, so the
   * Server holds no ContinuationPoint afterward.
   */
  @Test
  void uncertainBrowseNextPageKeepsReferencesAndLeavesNoContinuationPoint() throws Exception {
    AddressSpace addressSpace = client.getAddressSpace();

    int total = addressSpace.browse(NodeIds.Server).size();
    assertTrue(total > 2 * PAGE_SIZE, "precondition: the Server Node needs three or more pages");

    viewServiceSet.replaceNextBrowseNextResult(
        r ->
            new BrowseResult(
                new StatusCode(StatusCodes.Uncertain_NotAllNodesAvailable),
                r.getContinuationPoint(),
                r.getReferences()));

    List<ReferenceDescription> references =
        addressSpace.browse(NodeIds.Server, pagedBrowseOptions());

    assertEquals(total, references.size(), "references when the first BrowseNext is Uncertain");
    assertEquals(0, heldContinuationPoints(), "ContinuationPoints held after the browse");
  }

  // An Uncertain final page has no ContinuationPoint; its references are the whole result.
  @Test
  void uncertainBrowseResultKeepsReferences() throws Exception {
    AddressSpace addressSpace = client.getAddressSpace();

    int total = addressSpace.browse(NodeIds.Server).size();

    viewServiceSet.replaceNextBrowseResult(
        r ->
            new BrowseResult(
                new StatusCode(StatusCodes.Uncertain_NotAllNodesAvailable),
                r.getContinuationPoint(),
                r.getReferences()));

    assertEquals(total, addressSpace.browse(NodeIds.Server).size());
  }

  /**
   * Part 4 §7.9: a ContinuationPoint stays active until the Client releases it or the Session
   * closes. A Bad result that still carries one fails the browse and releases it.
   */
  @Test
  void badBrowseNextResultWithContinuationPointThrowsAndReleasesIt() throws Exception {
    int total = client.getAddressSpace().browse(NodeIds.Server).size();
    assertTrue(
        total > 2 * PAGE_SIZE,
        "precondition: the first BrowseNext page carries a live ContinuationPoint");

    viewServiceSet.replaceNextBrowseNextResult(
        r ->
            new BrowseResult(
                new StatusCode(StatusCodes.Bad_NodeNotInView),
                r.getContinuationPoint(),
                r.getReferences()));

    UaException e =
        assertThrows(
            UaException.class,
            () -> client.getAddressSpace().browse(NodeIds.Server, pagedBrowseOptions()));

    assertEquals(StatusCodes.Bad_NodeNotInView, e.getStatusCode().value());
    assertEquals(0, heldContinuationPoints(), "ContinuationPoints held after the browse failed");
  }

  // A malformed response fails with a UaException, not an unchecked exception from indexing it.
  @Test
  void browseNextResponseWithoutResultsFailsWithBadUnexpectedError() {
    viewServiceSet.replaceNextBrowseNextResponse(
        r ->
            new BrowseNextResponse(
                r.getResponseHeader(), new BrowseResult[0], r.getDiagnosticInfos()));

    UaException e =
        assertThrows(
            UaException.class,
            () -> client.getAddressSpace().browse(NodeIds.Server, pagedBrowseOptions()));

    assertEquals(StatusCodes.Bad_UnexpectedError, e.getStatusCode().value());
    assertInstanceOf(UaException.class, e.getCause(), "cause of the failure");
  }

  /**
   * ServiceFaultListeners, including the Session FSM's own listener, must see faults from the
   * high-level browse methods the same as from {@link OpcUaClient#browse}.
   */
  @Test
  void serviceFaultOnAddressSpaceBrowseReachesFaultListeners() throws Exception {
    var faults = new CopyOnWriteArrayList<StatusCode>();
    var lowLevelFaultSeen = new CountDownLatch(1);

    ServiceFaultListener listener =
        serviceFault -> {
          StatusCode serviceResult = serviceFault.getResponseHeader().getServiceResult();
          faults.add(serviceResult);
          if (serviceResult.value() == StatusCodes.Bad_InternalError) {
            lowLevelFaultSeen.countDown();
          }
        };

    client.addFaultListener(listener);

    try {
      viewServiceSet.faultNextBrowse(StatusCodes.Bad_ResourceUnavailable);
      UaException highLevel =
          assertThrows(UaException.class, () -> client.getAddressSpace().browse(NodeIds.Server));
      assertEquals(
          StatusCodes.Bad_ResourceUnavailable,
          highLevel.getStatusCode().value(),
          "precondition: the high-level browse failed with the scripted ServiceFault");

      viewServiceSet.faultNextBrowse(StatusCodes.Bad_InternalError);
      assertThrows(UaException.class, () -> client.browse(browseDescription(NodeIds.Server)));
      assertTrue(
          lowLevelFaultSeen.await(5, TimeUnit.SECONDS),
          "control: the ServiceFault from OpcUaClient.browse reached the listener");

      // Fault notifications run in order on one ExecutionQueue, so a notification for the
      // earlier high-level fault would already be in the list.
      assertTrue(
          faults.stream().anyMatch(s -> s.value() == StatusCodes.Bad_ResourceUnavailable),
          "ServiceFault from AddressSpace.browse was not reported; listener saw " + faults);
    } finally {
      client.removeFaultListener(listener);
    }
  }

  private BrowseOptions pagedBrowseOptions() {
    return client
        .getAddressSpace()
        .getBrowseOptions()
        .copy(b -> b.setMaxReferencesPerNode(uint(PAGE_SIZE)));
  }

  private BrowseDescription browseDescription(NodeId nodeId) {
    BrowseOptions options = client.getAddressSpace().getBrowseOptions();

    return new BrowseDescription(
        nodeId,
        options.getBrowseDirection(),
        options.getReferenceTypeId(),
        options.isIncludeSubtypes(),
        options.getNodeClassMask(),
        options.getResultMask());
  }

  /** The number of browse ContinuationPoints the Server holds for the test client's Session. */
  private int heldContinuationPoints() {
    List<Session> sessions = server.getSessionManager().getAllSessions();
    assertEquals(1, sessions.size(), "precondition: only the test client has a Session");

    return sessions.get(0).getBrowseContinuationPoints().size();
  }

  /**
   * The default View Service Set, plus one-shot scripts that change how the next Browse or
   * BrowseNext is answered.
   */
  private static final class ScriptedViewServiceSet extends DefaultViewServiceSet {

    private final OpcUaServer server;

    private final AtomicReference<Long> nextBrowseFault = new AtomicReference<>();
    private final AtomicReference<UnaryOperator<BrowseResult>> nextBrowseResult =
        new AtomicReference<>();
    private final AtomicBoolean freeContinuationPoints = new AtomicBoolean(false);
    private final AtomicReference<UnaryOperator<BrowseNextResponse>> nextBrowseNextResponse =
        new AtomicReference<>();

    ScriptedViewServiceSet(OpcUaServer server) {
      super(server);
      this.server = server;
    }

    void faultNextBrowse(long statusCode) {
      nextBrowseFault.set(statusCode);
    }

    void replaceNextBrowseResult(UnaryOperator<BrowseResult> replace) {
      nextBrowseResult.set(replace);
    }

    void freeContinuationPointsBeforeNextBrowseNext() {
      freeContinuationPoints.set(true);
    }

    void replaceNextBrowseNextResponse(UnaryOperator<BrowseNextResponse> replace) {
      nextBrowseNextResponse.set(replace);
    }

    void replaceNextBrowseNextResult(UnaryOperator<BrowseResult> replace) {
      replaceNextBrowseNextResponse(
          response ->
              new BrowseNextResponse(
                  response.getResponseHeader(),
                  new BrowseResult[] {replace.apply(requireNonNull(response.getResults())[0])},
                  response.getDiagnosticInfos()));
    }

    void reset() {
      nextBrowseFault.set(null);
      nextBrowseResult.set(null);
      freeContinuationPoints.set(false);
      nextBrowseNextResponse.set(null);
    }

    @Override
    public BrowseResponse onBrowse(ServiceRequestContext context, BrowseRequest request)
        throws UaException {

      Long fault = nextBrowseFault.getAndSet(null);
      if (fault != null) {
        throw new UaException(fault);
      }

      BrowseResponse response = super.onBrowse(context, request);

      UnaryOperator<BrowseResult> replace = nextBrowseResult.getAndSet(null);
      if (replace != null) {
        return new BrowseResponse(
            response.getResponseHeader(),
            new BrowseResult[] {replace.apply(requireNonNull(response.getResults())[0])},
            response.getDiagnosticInfos());
      }

      return response;
    }

    @Override
    public BrowseNextResponse onBrowseNext(ServiceRequestContext context, BrowseNextRequest request)
        throws UaException {

      if (freeContinuationPoints.getAndSet(false)) {
        Session session =
            server.getSessionManager().getSession(context, request.getRequestHeader());
        session.getBrowseContinuationPoints().clear();
      }

      BrowseNextResponse response = super.onBrowseNext(context, request);

      // Release requests pass through unchanged so the script applies to the next page.
      if (Boolean.TRUE.equals(request.getReleaseContinuationPoints())) {
        return response;
      }

      UnaryOperator<BrowseNextResponse> replace = nextBrowseNextResponse.getAndSet(null);
      if (replace != null) {
        return replace.apply(response);
      }

      return response;
    }
  }
}
