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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * End-to-end Browse and BrowseNext tests for per-session continuation point limits, reclamation of
 * prior-request continuation points (OPC UA Part 4 §7.9), and unrestricted zero limits (OPC UA Part
 * 5 §6.3.2).
 */
class BrowseContinuationPointsTest {

  private static final ViewDescription EMPTY_VIEW =
      new ViewDescription(NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0));

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class BoundedLimit {

    private static final int MAX_CONTINUATION_POINTS = 2;

    private TestServer testServer;
    private OpcUaServer server;
    private TestNamespace testNamespace;
    private OpcUaClient client;

    private NodeId multiChildFolderA;
    private NodeId multiChildFolderB;
    private NodeId multiChildFolderC;
    private NodeId singleChildFolder;

    @BeforeAll
    void setUp() throws Exception {
      OpcUaServerConfigLimits limits =
          new OpcUaServerConfigLimits() {
            @Override
            public UShort getMaxBrowseContinuationPoints() {
              return ushort(MAX_CONTINUATION_POINTS);
            }

            @Override
            public UInteger getMaxNodesPerBrowse() {
              return uint(10);
            }
          };

      testServer = TestServer.create(limits);
      server = testServer.getServer();
      testNamespace = new TestNamespace(server);
      testNamespace.startup();
      server.startup().get(10, TimeUnit.SECONDS);

      multiChildFolderA = addFolderWithChildren("FolderA", 4);
      multiChildFolderB = addFolderWithChildren("FolderB", 4);
      multiChildFolderC = addFolderWithChildren("FolderC", 4);
      singleChildFolder = addFolderWithChildren("SingleChildFolder", 1);

      client = TestClient.create(server, b -> {});
      client.connectAsync().get(10, TimeUnit.SECONDS);
    }

    @AfterAll
    void tearDown() throws Exception {
      if (client != null) {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      }
      if (testNamespace != null) {
        testNamespace.shutdown();
      }
      if (server != null) {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }

    @AfterEach
    void clearContinuationPoints() {
      server.getSessionManager().getAllSessions().forEach(Session::clearBrowseContinuationPoints);
    }

    /**
     * Part 4 §7.9: abandoned continuation points from prior Browse requests must be reclaimed in
     * FIFO order when new Browse requests need them, and active count must never exceed
     * MaxBrowseContinuationPoints.
     */
    @Test
    void abandonedContinuationPointsAreReclaimedOnLaterBrowseRequests() throws Exception {
      UShort advertisedLimit =
          (UShort)
              client
                  .readValue(
                      0.0,
                      TimestampsToReturn.Neither,
                      NodeIds.Server_ServerCapabilities_MaxBrowseContinuationPoints)
                  .value()
                  .value();
      assertEquals(MAX_CONTINUATION_POINTS, advertisedLimit.intValue());

      BrowseResult first = browseSingle(multiChildFolderA, 1);
      BrowseResult second = browseSingle(multiChildFolderB, 1);
      BrowseResult third = browseSingle(multiChildFolderC, 1);

      assertHasContinuationPoint(first);
      assertHasContinuationPoint(second);
      assertHasContinuationPoint(third);

      assertEquals(
          MAX_CONTINUATION_POINTS,
          heldContinuationPoints(),
          "active continuation points must not exceed MaxBrowseContinuationPoints");

      // The oldest continuation point (first) was reclaimed; second and third remain usable.
      BrowseResult expired = browseNextSingle(first.getContinuationPoint(), false);
      assertEquals(StatusCodes.Bad_ContinuationPointInvalid, expired.getStatusCode().value());

      assertEquals(4, drainRemainingPages(second).size());
      assertEquals(4, drainRemainingPages(third).size());
      assertEquals(0, heldContinuationPoints());
    }

    /**
     * Part 4 §7.9: a single Browse request may reclaim prior requests' continuation points, but
     * once it allocates MaxBrowseContinuationPoints entries in its own response, remaining paged
     * operations in that response receive Bad_NoContinuationPoints without evicting earlier
     * operations from the same request.
     */
    @Test
    void singleBrowseRequestExceedingLimitReturnsBadNoContinuationPointsForExcess()
        throws Exception {

      BrowseResult prior1 = browseSingle(multiChildFolderA, 1);
      BrowseResult prior2 = browseSingle(multiChildFolderB, 1);
      assertHasContinuationPoint(prior1);
      assertHasContinuationPoint(prior2);
      assertEquals(2, heldContinuationPoints());

      BrowseResponse response =
          client.browse(
              EMPTY_VIEW,
              uint(1),
              List.of(
                  forwardOrganizes(multiChildFolderA),
                  forwardOrganizes(multiChildFolderB),
                  forwardOrganizes(multiChildFolderC),
                  forwardOrganizes(singleChildFolder)));

      List<BrowseResult> results = Lists.ofNullable(response.getResults());
      assertEquals(4, results.size());

      BrowseResult op0 = results.get(0);
      BrowseResult op1 = results.get(1);
      BrowseResult op2 = results.get(2);
      BrowseResult op3 = results.get(3);

      assertHasContinuationPoint(op0);
      assertHasContinuationPoint(op1);

      assertEquals(StatusCodes.Bad_NoContinuationPoints, op2.getStatusCode().value());
      assertTrue(op2.getContinuationPoint() == null || op2.getContinuationPoint().isNull());
      assertEquals(0, Lists.ofNullable(op2.getReferences()).size());

      // Unpaged operation in the same request needs no continuation point and still succeeds.
      assertTrue(op3.getStatusCode().isGood());
      assertTrue(op3.getContinuationPoint() == null || op3.getContinuationPoint().isNull());
      assertEquals(1, Lists.ofNullable(op3.getReferences()).size());

      assertEquals(MAX_CONTINUATION_POINTS, heldContinuationPoints());

      // Prior requests' continuation points were reclaimed.
      assertEquals(
          StatusCodes.Bad_ContinuationPointInvalid,
          browseNextSingle(prior1.getContinuationPoint(), false).getStatusCode().value());
      assertEquals(
          StatusCodes.Bad_ContinuationPointInvalid,
          browseNextSingle(prior2.getContinuationPoint(), false).getStatusCode().value());

      // Both continuation points returned by the multi-op request must still be intact.
      assertEquals(4, drainRemainingPages(op0).size());
      assertEquals(4, drainRemainingPages(op1).size());
      assertEquals(0, heldContinuationPoints());
    }

    /**
     * Part 4 §7.9: continuing an existing operation via BrowseNext reuses its continuation point
     * and must never fail with Bad_NoContinuationPoints even when the session is at capacity.
     */
    @Test
    void browseNextAtCapacityReusesContinuationPointAcrossAllPages() throws Exception {
      BrowseResult first = browseSingle(multiChildFolderA, 1);
      BrowseResult second = browseSingle(multiChildFolderB, 1);
      assertHasContinuationPoint(first);
      assertHasContinuationPoint(second);
      assertEquals(MAX_CONTINUATION_POINTS, heldContinuationPoints());

      List<ReferenceDescription> allFromFirst = drainRemainingPages(first);
      assertEquals(4, allFromFirst.size());
      assertEquals(1, heldContinuationPoints());

      List<ReferenceDescription> allFromSecond = drainRemainingPages(second);
      assertEquals(4, allFromSecond.size());
      assertEquals(0, heldContinuationPoints());
    }

    /**
     * Part 4 §5.9.3.2 Table 37: BrowseNext with releaseContinuationPoints = true releases all
     * supplied handles (including duplicates) and returns empty results and diagnosticInfos arrays.
     * Calling BrowseNext(false) on the released handle afterward fails with
     * Bad_ContinuationPointInvalid.
     */
    @Test
    void browseNextReleaseFreesContinuationPointAndReturnsEmptyResults() throws Exception {
      BrowseResult paged = browseSingle(multiChildFolderA, 1);
      assertHasContinuationPoint(paged);
      assertEquals(1, heldContinuationPoints());

      ByteString handle = paged.getContinuationPoint();
      BrowseNextResponse releaseResponse = client.browseNext(true, List.of(handle, handle));
      assertEquals(0, Lists.ofNullable(releaseResponse.getResults()).size());
      assertEquals(0, Lists.ofNullable(releaseResponse.getDiagnosticInfos()).size());
      assertEquals(0, heldContinuationPoints());

      BrowseResult afterRelease = browseNextSingle(handle, false);
      assertEquals(StatusCodes.Bad_ContinuationPointInvalid, afterRelease.getStatusCode().value());
    }

    /**
     * Part 5 §6.3.11: BrowseNext.continuationPoints is bounded by MaxNodesPerBrowse (10), not
     * MaxBrowseContinuationPoints (2). A request with 3 handles succeeds, while 11 handles fails
     * with Bad_TooManyOperations.
     */
    @Test
    void browseNextRequestSizeUsesMaxNodesPerBrowseNotMaxContinuationPoints() throws Exception {
      BrowseResult first = browseSingle(multiChildFolderA, 1);
      BrowseResult second = browseSingle(multiChildFolderB, 1);
      assertHasContinuationPoint(first);
      assertHasContinuationPoint(second);

      ByteString unknown = ByteString.of(new byte[] {9, 9, 9, 9});
      BrowseNextResponse response =
          client.browseNext(
              false, List.of(first.getContinuationPoint(), second.getContinuationPoint(), unknown));

      List<BrowseResult> results = Lists.ofNullable(response.getResults());
      assertEquals(3, results.size());
      assertHasContinuationPoint(results.get(0));
      assertHasContinuationPoint(results.get(1));
      assertEquals(
          StatusCodes.Bad_ContinuationPointInvalid, results.get(2).getStatusCode().value());

      UaException tooMany =
          assertThrows(
              UaException.class, () -> client.browseNext(false, Collections.nCopies(11, unknown)));
      assertEquals(StatusCodes.Bad_TooManyOperations, tooMany.getStatusCode().value());
    }

    // Part 4 §7.9: closing a Session frees any unreleased continuation points owned by it.
    @Test
    void closingSessionClearsHeldContinuationPoints() throws Exception {
      OpcUaClient transientClient = TestClient.create(server, b -> {});
      transientClient.connectAsync().get(10, TimeUnit.SECONDS);

      Session transientSession;
      try {
        BrowseResult paged =
            Lists.ofNullable(
                    transientClient
                        .browse(EMPTY_VIEW, uint(1), List.of(forwardOrganizes(multiChildFolderA)))
                        .getResults())
                .get(0);
        assertHasContinuationPoint(paged);

        transientSession =
            server.getSessionManager().getAllSessions().stream()
                .filter(s -> !s.getBrowseContinuationPoints().isEmpty())
                .findFirst()
                .orElseThrow();
        assertEquals(1, transientSession.getBrowseContinuationPoints().size());
      } finally {
        transientClient.disconnectAsync().get(10, TimeUnit.SECONDS);
      }

      assertTrue(transientSession.isClosed());
      assertTrue(
          transientSession.getBrowseContinuationPoints().isEmpty(),
          "closing a Session must clear its browse continuation points");
    }

    private NodeId addFolderWithChildren(String folderName, int childCount) {
      NodeId folderId = new NodeId(testNamespace.getNamespaceIndex(), "CP." + folderName);

      testNamespace.configure(
          (context, nodeManager) -> {
            var folder =
                new UaFolderNode(
                    context,
                    folderId,
                    new QualifiedName(testNamespace.getNamespaceIndex(), folderName),
                    LocalizedText.english(folderName));
            nodeManager.addNode(folder);

            for (int i = 0; i < childCount; i++) {
              String childName = folderName + ".Child" + i;
              var child =
                  new UaFolderNode(
                      context,
                      new NodeId(testNamespace.getNamespaceIndex(), "CP." + childName),
                      new QualifiedName(testNamespace.getNamespaceIndex(), childName),
                      LocalizedText.english(childName));
              nodeManager.addNode(child);
              folder.addOrganizes(child);
            }
          });

      return folderId;
    }

    private BrowseResult browseSingle(NodeId nodeId, int maxRefsPerNode) throws UaException {
      BrowseResponse response =
          client.browse(EMPTY_VIEW, uint(maxRefsPerNode), List.of(forwardOrganizes(nodeId)));
      return Lists.ofNullable(response.getResults()).get(0);
    }

    private BrowseResult browseNextSingle(ByteString cp, boolean release) throws UaException {
      BrowseNextResponse response = client.browseNext(release, List.of(cp));
      return Lists.ofNullable(response.getResults()).get(0);
    }

    private List<ReferenceDescription> drainRemainingPages(BrowseResult initial)
        throws UaException {

      var collected = new ArrayList<>(Lists.ofNullable(initial.getReferences()));
      ByteString cp = initial.getContinuationPoint();

      while (cp != null && cp.isNotNull()) {
        BrowseResult next = browseNextSingle(cp, false);
        assertTrue(
            next.getStatusCode().isGood(), "expected Good BrowseNext, got " + next.getStatusCode());
        collected.addAll(Lists.ofNullable(next.getReferences()));
        cp = next.getContinuationPoint();
      }

      return collected;
    }

    private int heldContinuationPoints() {
      List<Session> sessions = server.getSessionManager().getAllSessions();
      assertEquals(1, sessions.size(), "precondition: single active test session");
      return sessions.get(0).getBrowseContinuationPoints().size();
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class UnrestrictedZeroLimit {

    private static final int MAX_NODES_PER_BROWSE = 3;

    private TestServer testServer;
    private OpcUaServer server;
    private TestNamespace testNamespace;
    private OpcUaClient client;

    private NodeId folderId;

    @BeforeAll
    void setUp() throws Exception {
      OpcUaServerConfigLimits limits =
          new OpcUaServerConfigLimits() {
            @Override
            public UShort getMaxBrowseContinuationPoints() {
              return ushort(0);
            }

            @Override
            public UInteger getMaxNodesPerBrowse() {
              return uint(MAX_NODES_PER_BROWSE);
            }
          };

      testServer = TestServer.create(limits);
      server = testServer.getServer();
      testNamespace = new TestNamespace(server);
      testNamespace.startup();
      server.startup().get(10, TimeUnit.SECONDS);

      folderId = new NodeId(testNamespace.getNamespaceIndex(), "CP.UnrestrictedFolder");
      testNamespace.configure(
          (context, nodeManager) -> {
            var folder =
                new UaFolderNode(
                    context,
                    folderId,
                    new QualifiedName(testNamespace.getNamespaceIndex(), "UnrestrictedFolder"),
                    LocalizedText.english("UnrestrictedFolder"));
            nodeManager.addNode(folder);

            for (int i = 0; i < 4; i++) {
              String childName = "UnrestrictedChild" + i;
              var child =
                  new UaFolderNode(
                      context,
                      new NodeId(testNamespace.getNamespaceIndex(), "CP." + childName),
                      new QualifiedName(testNamespace.getNamespaceIndex(), childName),
                      LocalizedText.english(childName));
              nodeManager.addNode(child);
              folder.addOrganizes(child);
            }
          });

      client = TestClient.create(server, b -> {});
      client.connectAsync().get(10, TimeUnit.SECONDS);
    }

    @AfterAll
    void tearDown() throws Exception {
      if (client != null) {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      }
      if (testNamespace != null) {
        testNamespace.shutdown();
      }
      if (server != null) {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    }

    @AfterEach
    void clearContinuationPoints() {
      server.getSessionManager().getAllSessions().forEach(Session::clearBrowseContinuationPoints);
    }

    /**
     * Part 5 §6.3.2 & §6.3.11: MaxBrowseContinuationPoints == 0 removes the per-session
     * continuation point cap, while BrowseNext still enforces MaxNodesPerBrowse (3) on the
     * continuationPoints array length.
     */
    @Test
    void zeroLimitAllowsUnlimitedParallelContinuationPointsWhileEnforcingMaxNodesPerBrowse()
        throws Exception {

      UShort advertisedLimit =
          (UShort)
              client
                  .readValue(
                      0.0,
                      TimestampsToReturn.Neither,
                      NodeIds.Server_ServerCapabilities_MaxBrowseContinuationPoints)
                  .value()
                  .value();
      assertEquals(0, advertisedLimit.intValue());

      var continuationPoints = new ArrayList<ByteString>();
      for (int i = 0; i < 5; i++) {
        BrowseResult result =
            Lists.ofNullable(
                    client
                        .browse(EMPTY_VIEW, uint(1), List.of(forwardOrganizes(folderId)))
                        .getResults())
                .get(0);
        assertHasContinuationPoint(result);
        continuationPoints.add(result.getContinuationPoint());
      }

      List<Session> sessions = server.getSessionManager().getAllSessions();
      assertEquals(1, sessions.size());
      assertEquals(5, sessions.get(0).getBrowseContinuationPoints().size());

      // Passing more continuation points than MaxNodesPerBrowse (3) fails with
      // Bad_TooManyOperations even when MaxBrowseContinuationPoints == 0.
      UaException tooMany =
          assertThrows(
              UaException.class, () -> client.browseNext(false, continuationPoints.subList(0, 4)));
      assertEquals(StatusCodes.Bad_TooManyOperations, tooMany.getStatusCode().value());

      // Continuing up to MaxNodesPerBrowse (3) succeeds.
      BrowseNextResponse nextResponse =
          client.browseNext(false, continuationPoints.subList(0, MAX_NODES_PER_BROWSE));
      List<BrowseResult> nextResults = Lists.ofNullable(nextResponse.getResults());
      assertEquals(MAX_NODES_PER_BROWSE, nextResults.size());
      for (BrowseResult r : nextResults) {
        assertHasContinuationPoint(r);
      }

      // Releasing in batches within MaxNodesPerBrowse succeeds with empty results per Part 4
      // §5.9.3.2 Table 37 and frees all 5 continuation points.
      BrowseNextResponse releaseFirstBatch =
          client.browseNext(true, continuationPoints.subList(0, MAX_NODES_PER_BROWSE));
      assertEquals(0, Lists.ofNullable(releaseFirstBatch.getResults()).size());
      assertEquals(0, Lists.ofNullable(releaseFirstBatch.getDiagnosticInfos()).size());

      BrowseNextResponse releaseSecondBatch =
          client.browseNext(true, continuationPoints.subList(MAX_NODES_PER_BROWSE, 5));
      assertEquals(0, Lists.ofNullable(releaseSecondBatch.getResults()).size());
      assertEquals(0, Lists.ofNullable(releaseSecondBatch.getDiagnosticInfos()).size());

      assertEquals(0, sessions.get(0).getBrowseContinuationPoints().size());
    }
  }

  private static BrowseDescription forwardOrganizes(NodeId nodeId) {
    return new BrowseDescription(
        nodeId,
        BrowseDirection.Forward,
        NodeIds.Organizes,
        true,
        uint(NodeClass.Object.getValue()),
        uint(BrowseResultMask.All.getValue()));
  }

  private static void assertHasContinuationPoint(BrowseResult result) {
    assertTrue(
        result.getStatusCode().isGood(), "expected Good status, got " + result.getStatusCode());
    assertNotNull(result.getContinuationPoint(), "expected non-null continuation point");
    assertTrue(
        result.getContinuationPoint().isNotNull(), "expected non-null continuation point value");
    assertEquals(1, Lists.ofNullable(result.getReferences()).size());
  }
}
