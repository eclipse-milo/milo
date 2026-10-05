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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(30)
class WikiClientHistoryTest extends AbstractClientServerTest {
  private static final DateTime START = new DateTime(Instant.parse("2026-01-01T00:00:00Z"));
  private static final DateTime END = new DateTime(Instant.parse("2026-01-01T00:01:00Z"));
  private RawHistoryService history;

  @Override
  protected TestServer createTestServer() throws Exception {
    TestServer fixture =
        TestServer.create(
            config -> {
              int port = config.build().getEndpoints().iterator().next().getBindPort();
              // Keep the builder's empty path so the service installation must normalize it to /.
              config.setEndpoints(Set.of(EndpointConfig.newBuilder().setBindPort(port).build()));
            });
    history = installHistoryProvider(fixture.getServer());
    return fixture;
  }

  @AfterEach
  void releaseFixtureState() {
    history.clear();
  }

  // Consuming every page preserves stored quality/timestamps and leaves no server cursor.
  @Test
  void rawHistoryDrainsPagesWithoutDiscardingBadStoredValues() throws Exception {
    List<DataValue> values = readRawHistory(client, history.namespace.historyNode, START, END, 10);
    assertEquals(List.of(10, 20, 30), values.stream().map(v -> v.value().value()).toList());
    assertTrue(values.get(0).statusCode().isGood());
    assertEquals(StatusCodes.Bad_SensorFailure, values.get(1).statusCode().value());
    assertEquals(START, values.get(0).sourceTime());
    assertEquals(0, history.cursorCount());
  }

  // Part 4 §5.11.3.2: servers ignore dataEncoding, including on continuation and empty reads.
  @Test
  void historyReadIgnoresTheUnusedDataEncoding() throws Exception {
    var encoding = new QualifiedName(0, "UnusedEncoding");
    var request =
        new HistoryReadValueId(
            history.namespace.historyNode, null, encoding, ByteString.NULL_VALUE);
    HistoryReadResult first = readHistory(client, false, List.of(request))[0];
    assertTrue(first.getStatusCode().isGood());
    assertFalse(first.getContinuationPoint().isNullOrEmpty());
    HistoryData firstPage =
        (HistoryData) first.getHistoryData().decode(client.getStaticEncodingContext());
    assertEquals(
        List.of(10, 20),
        Arrays.stream(requireNonNull(firstPage.getDataValues()))
            .map(value -> value.value().value())
            .toList());

    var next =
        new HistoryReadValueId(
            history.namespace.historyNode, null, encoding, first.getContinuationPoint());
    HistoryReadResult last = readHistory(client, false, List.of(next))[0];
    assertTrue(last.getStatusCode().isGood());
    assertTrue(last.getContinuationPoint().isNullOrEmpty());
    HistoryData lastPage =
        (HistoryData) last.getHistoryData().decode(client.getStaticEncodingContext());
    assertEquals(1, requireNonNull(lastPage.getDataValues()).length);
    assertEquals(30, lastPage.getDataValues()[0].value().value());
    assertEquals(0, history.cursorCount());

    var emptyInterval =
        new ReadRawModifiedDetails(
            false, END, new DateTime(Instant.parse("2026-01-01T00:02:00Z")), uint(2), false);
    HistoryReadResult empty =
        requireNonNull(
            client
                .historyRead(emptyInterval, TimestampsToReturn.Both, false, List.of(request))
                .getResults())[0];
    assertEquals(StatusCodes.Good_NoData, empty.getStatusCode().value());
    assertTrue(empty.getHistoryData() == null || empty.getHistoryData().isNull());
    assertTrue(empty.getContinuationPoint().isNullOrEmpty());
    assertEquals(0, history.cursorCount());
  }

  // An empty endpoint path must route explicit release to the override, not consume another page.
  @Test
  void earlyStopOnAnEndpointWithoutAPathReleasesItsContinuationPoint() throws Exception {
    assertEquals("", server.getConfig().getEndpoints().iterator().next().getPath());
    int releases = history.releaseCount();
    assertEquals(2, readRawHistory(client, history.namespace.historyNode, START, END, 1).size());
    assertEquals(releases + 1, history.releaseCount());
    assertEquals(0, history.cursorCount());
  }

  // A nonpositive page budget is a caller error, not an empty history result.
  @Test
  void nonpositivePageBudgetIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> readRawHistory(client, history.namespace.historyNode, START, END, 0));
  }

  @Test
  void unsupportedHistoryFailsPerNodeAndEmptyRequestFailsTheService() throws Exception {
    UaException unavailable =
        assertThrows(
            UaException.class,
            () -> readRawHistory(client, NodeIds.Server_ServerStatus_CurrentTime, START, END, 1));
    assertEquals(StatusCodes.Bad_HistoryOperationUnsupported, unavailable.getStatusCode().value());
    UaException empty =
        assertThrows(
            UaException.class,
            () ->
                client.historyRead(
                    new ReadRawModifiedDetails(false, START, END, uint(2), false),
                    TimestampsToReturn.Both,
                    false,
                    List.of()));
    assertEquals(StatusCodes.Bad_NothingToDo, empty.getStatusCode().value());
  }

  // A continuation point belongs to its Session, even when another client requests the same node.
  @Test
  void cursorCannotBeUsedByAnotherSessionOrReusedAfterRelease() throws Exception {
    var details = new ReadRawModifiedDetails(false, START, END, uint(2), false);
    var first =
        new HistoryReadValueId(
            history.namespace.historyNode, null, QualifiedName.NULL_VALUE, ByteString.NULL_VALUE);
    HistoryReadResult result =
        requireNonNull(
            client
                .historyRead(details, TimestampsToReturn.Both, false, List.of(first))
                .getResults())[0];
    ByteString cursor = result.getContinuationPoint();
    assertFalse(cursor.isNullOrEmpty());
    var next =
        new HistoryReadValueId(
            history.namespace.historyNode, null, QualifiedName.NULL_VALUE, cursor);
    OpcUaClient other = TestClient.create(server, config -> {});
    try {
      other.connectAsync().get(10, TimeUnit.SECONDS);
      HistoryReadResult wrongSession =
          requireNonNull(
              other
                  .historyRead(details, TimestampsToReturn.Both, false, List.of(next))
                  .getResults())[0];
      assertEquals(StatusCodes.Bad_ContinuationPointInvalid, wrongSession.getStatusCode().value());
    } finally {
      other.disconnectAsync().get(10, TimeUnit.SECONDS);
      client.historyRead(details, TimestampsToReturn.Both, true, List.of(next));
    }
    HistoryReadResult released =
        requireNonNull(
            client
                .historyRead(details, TimestampsToReturn.Both, false, List.of(next))
                .getResults())[0];
    assertEquals(StatusCodes.Bad_ContinuationPointInvalid, released.getStatusCode().value());
    assertEquals(0, history.cursorCount());
  }

  // Part 4 §7.9: a new request reclaims prior points from its own Session when capacity is needed.
  @Test
  void newRequestReclaimsTheSessionsOldestCursor() throws Exception {
    var first = historyRequest(ByteString.NULL_VALUE);
    List<HistoryReadValueId> open = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      HistoryReadResult result = readHistory(client, false, List.of(first))[0];
      assertTrue(result.getStatusCode().isGood());
      assertFalse(result.getContinuationPoint().isNullOrEmpty());
      open.add(historyRequest(result.getContinuationPoint()));
    }
    assertEquals(8, history.cursorCount());

    HistoryReadResult replacement = readHistory(client, false, List.of(first))[0];
    assertTrue(replacement.getStatusCode().isGood());
    assertFalse(replacement.getContinuationPoint().isNullOrEmpty());
    assertEquals(8, history.cursorCount());
    HistoryReadResult reclaimed = readHistory(client, false, List.of(open.get(0)))[0];
    assertEquals(StatusCodes.Bad_ContinuationPointInvalid, reclaimed.getStatusCode().value());
    // The next-oldest token survives and can finish while the registry is at capacity.
    HistoryReadResult retained = readHistory(client, false, List.of(open.get(1)))[0];
    assertTrue(retained.getStatusCode().isGood());
    assertTrue(retained.getContinuationPoint().isNullOrEmpty());
    assertEquals(7, history.cursorCount());

    List<HistoryReadValueId> remaining = new ArrayList<>(open.subList(2, open.size()));
    remaining.add(historyRequest(replacement.getContinuationPoint()));
    for (HistoryReadResult released : readHistory(client, true, remaining)) {
      assertTrue(released.getStatusCode().isGood());
    }
    assertEquals(0, history.cursorCount());
  }

  // Part 4 §7.9: saturation within one response must not invalidate its newly returned points.
  @Test
  void oneRequestKeepsItsNewCursorsAndRejectsExcessOperations() throws Exception {
    var first = historyRequest(ByteString.NULL_VALUE);
    HistoryReadResult[] results = readHistory(client, false, Collections.nCopies(9, first));
    assertEquals(9, results.length);
    assertEquals(StatusCodes.Bad_NoContinuationPoints, results[8].getStatusCode().value());
    assertEquals(8, history.cursorCount());
    for (int i = 0; i < 8; i++) {
      assertTrue(results[i].getStatusCode().isGood());
      assertFalse(results[i].getContinuationPoint().isNullOrEmpty());
      HistoryReadResult completed =
          readHistory(client, false, List.of(historyRequest(results[i].getContinuationPoint())))[0];
      assertTrue(completed.getStatusCode().isGood());
      assertTrue(completed.getContinuationPoint().isNullOrEmpty());
    }
    assertEquals(0, history.cursorCount());
  }

  // A global memory bound must not reclaim another Session's tokens; explicit release restores
  // room.
  @Test
  void anotherSessionCannotReclaimTheOwnersCursorsAtTheGlobalLimit() throws Exception {
    var first = historyRequest(ByteString.NULL_VALUE);
    HistoryReadResult[] owned = readHistory(client, false, Collections.nCopies(8, first));
    List<HistoryReadValueId> open = new ArrayList<>();
    for (HistoryReadResult result : owned) {
      assertTrue(result.getStatusCode().isGood());
      assertFalse(result.getContinuationPoint().isNullOrEmpty());
      open.add(historyRequest(result.getContinuationPoint()));
    }
    OpcUaClient other = TestClient.create(server, config -> {});
    try {
      other.connectAsync().get(10, TimeUnit.SECONDS);
      HistoryReadResult denied = readHistory(other, false, List.of(first))[0];
      assertEquals(StatusCodes.Bad_NoContinuationPoints, denied.getStatusCode().value());
      assertEquals(8, history.cursorCount());
      assertTrue(readHistory(client, true, List.of(open.get(0)))[0].getStatusCode().isGood());

      HistoryReadResult admitted = readHistory(other, false, List.of(first))[0];
      assertTrue(admitted.getStatusCode().isGood());
      assertFalse(admitted.getContinuationPoint().isNullOrEmpty());
      assertEquals(8, history.cursorCount());
      HistoryReadResult completed =
          readHistory(other, false, List.of(historyRequest(admitted.getContinuationPoint())))[0];
      assertTrue(completed.getStatusCode().isGood());
      assertTrue(completed.getContinuationPoint().isNullOrEmpty());
      for (HistoryReadResult released : readHistory(client, true, open.subList(1, open.size()))) {
        assertTrue(released.getStatusCode().isGood());
      }
      assertEquals(0, history.cursorCount());
    } finally {
      other.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  private HistoryReadResult[] readHistory(
      OpcUaClient requester, boolean release, List<HistoryReadValueId> nodes) throws UaException {
    var details = new ReadRawModifiedDetails(false, START, END, uint(2), false);
    return requireNonNull(
        requester.historyRead(details, TimestampsToReturn.Both, release, nodes).getResults());
  }

  // The release interception must not bypass service limits or stop diagnostic accounting.
  @Test
  void normalAndReleaseReadsEnforceLimitsAndRecordServiceFailures() throws Exception {
    var details = new ReadRawModifiedDetails(false, START, END, uint(2), false);
    NodeId sessionId = client.getSession().getSessionId();
    Session session =
        server.getSessionManager().getAllSessions().stream()
            .filter(candidate -> candidate.getSessionId().equals(sessionId))
            .findFirst()
            .orElseThrow();
    var counter = session.getSessionDiagnostics().getHistoryReadCount();
    long total = counter.getServiceCounter().getTotalCount().longValue();
    long errors = counter.getServiceCounter().getErrorCount().longValue();
    int limit = server.getConfig().getLimits().getMaxNodesPerRead().intValue();
    var tooMany = Collections.nCopies(limit + 1, historyRequest(ByteString.NULL_VALUE));
    for (boolean release : List.of(false, true)) {
      UaException limitFailure =
          assertThrows(
              UaException.class,
              () -> client.historyRead(details, TimestampsToReturn.Both, release, tooMany));
      assertEquals(StatusCodes.Bad_TooManyOperations, limitFailure.getStatusCode().value());
      UaException emptyFailure =
          assertThrows(
              UaException.class,
              () -> client.historyRead(details, TimestampsToReturn.Both, release, List.of()));
      assertEquals(StatusCodes.Bad_NothingToDo, emptyFailure.getStatusCode().value());
    }
    assertEquals(total + 4, counter.getServiceCounter().getTotalCount().longValue());
    assertEquals(errors + 4, counter.getServiceCounter().getErrorCount().longValue());
    assertEquals(0, history.cursorCount());
  }

  // A second provider must still receive its operations and its operation diagnostic must survive.
  @Test
  void ordinaryReadsPreserveOtherNamespaceRoutingAndDiagnostics() throws Exception {
    var other =
        new ManagedNamespaceWithLifecycle(server, "urn:eclipse:milo:wiki:other-history") {
          @Override
          public List<HistoryReadResult> historyRead(
              HistoryReadContext context,
              HistoryReadDetails details,
              TimestampsToReturn timestamps,
              List<HistoryReadValueId> nodes) {
            context
                .getDiagnosticsContext()
                .getDiagnosticsMap()
                .put(
                    nodes.get(0),
                    new DiagnosticInfo(-1, -1, -1, -1, "other provider reached", null, null));
            var data = new HistoryData(new DataValue[] {new DataValue(new Variant(99))});
            return List.of(
                new HistoryReadResult(
                    StatusCode.GOOD,
                    ByteString.NULL_VALUE,
                    ExtensionObject.encode(server.getStaticEncodingContext(), data)));
          }
        };
    other.startup();
    try {
      var details = new ReadRawModifiedDetails(false, START, END, uint(2), false);
      var request =
          new HistoryReadValueId(
              new NodeId(other.getNamespaceIndex(), "other"),
              null,
              QualifiedName.NULL_VALUE,
              ByteString.NULL_VALUE);
      HistoryReadResponse response =
          client.historyRead(details, TimestampsToReturn.Both, false, List.of(request));
      HistoryReadResult result = requireNonNull(response.getResults())[0];
      assertTrue(result.getStatusCode().isGood());
      HistoryData data =
          (HistoryData) result.getHistoryData().decode(client.getStaticEncodingContext());
      assertEquals(99, requireNonNull(data.getDataValues())[0].value().value());
      assertEquals(
          "other provider reached",
          requireNonNull(response.getDiagnosticInfos())[0].additionalInfo());
    } finally {
      other.shutdown();
    }
  }

  // Lost clients cannot retain scarce cursors after their Session has closed.
  @Test
  void sessionCloseReclaimsUnreleasedCursors() throws Exception {
    OpcUaClient other = TestClient.create(server, config -> {});
    var closed = new CompletableFuture<Void>();
    other.connectAsync().get(10, TimeUnit.SECONDS);
    NodeId sessionId = other.getSession().getSessionId();
    SessionListener observer =
        new SessionListener() {
          @Override
          public void onSessionClosed(Session session) {
            if (session.getSessionId().equals(sessionId)) closed.complete(null);
          }
        };
    server.getSessionManager().addSessionListener(observer);
    try {
      var details = new ReadRawModifiedDetails(false, START, END, uint(2), false);
      HistoryReadResult result =
          requireNonNull(
              other
                  .historyRead(
                      details,
                      TimestampsToReturn.Both,
                      false,
                      List.of(historyRequest(ByteString.NULL_VALUE)))
                  .getResults())[0];
      assertTrue(result.getStatusCode().isGood());
      assertEquals(1, history.cursorCount());
      other.disconnectAsync().get(10, TimeUnit.SECONDS);
      closed.get(10, TimeUnit.SECONDS);
      assertEquals(0, history.cursorCount());
    } finally {
      server.getSessionManager().removeSessionListener(observer);
      other.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  private HistoryReadValueId historyRequest(ByteString cursor) {
    return new HistoryReadValueId(
        history.namespace.historyNode, null, QualifiedName.NULL_VALUE, cursor);
  }

  // Part 11 raw reads may return Good_NoData without an encoded HistoryData body.
  @Test
  void noDataWithAnAbsentBodyReturnsAnEmptyList() throws Exception {
    assertTrue(
        readRawHistory(
                client,
                history.namespace.historyNode,
                END,
                new DateTime(Instant.parse("2026-01-01T00:02:00Z")),
                10)
            .isEmpty());
    assertEquals(0, history.cursorCount());
  }

  // snippet:history:start
  static List<DataValue> readRawHistory(
      OpcUaClient client, NodeId nodeId, DateTime start, DateTime end, int maxPages)
      throws UaException {
    if (maxPages < 1) {
      throw new IllegalArgumentException("maxPages must be positive");
    }

    var details = new ReadRawModifiedDetails(false, start, end, uint(2), false);
    List<DataValue> values = new ArrayList<>();
    ByteString continuationPoint = ByteString.NULL_VALUE;

    try {
      for (int page = 0; page < maxPages; page++) {
        HistoryReadResult result =
            sendHistoryRead(client, details, nodeId, continuationPoint, false);
        continuationPoint = result.getContinuationPoint();

        if (!result.getStatusCode().isGood()) {
          throw new UaException(result.getStatusCode());
        }

        // A Good_NoData result can omit the HistoryData body.
        ExtensionObject historyData = result.getHistoryData();
        if (historyData != null && !historyData.isNull()) {
          var data = (HistoryData) historyData.decode(client.getStaticEncodingContext());
          if (data.getDataValues() != null) {
            values.addAll(Arrays.asList(data.getDataValues()));
          }
        }

        if (continuationPoint == null || continuationPoint.isNullOrEmpty()) {
          break;
        }
      }
      return values;
    } finally {
      // Release the continuation point if the loop stopped before the last page.
      if (continuationPoint != null && !continuationPoint.isNullOrEmpty()) {
        HistoryReadResult released =
            sendHistoryRead(client, details, nodeId, continuationPoint, true);
        if (!released.getStatusCode().isGood()) {
          throw new UaException(released.getStatusCode());
        }
      }
    }
  }

  static HistoryReadResult sendHistoryRead(
      OpcUaClient client,
      ReadRawModifiedDetails details,
      NodeId nodeId,
      ByteString continuationPoint,
      boolean releaseContinuationPoints)
      throws UaException {
    var nodeToRead =
        new HistoryReadValueId(nodeId, null, QualifiedName.NULL_VALUE, continuationPoint);

    HistoryReadResponse response =
        client.historyRead(
            details, TimestampsToReturn.Both, releaseContinuationPoints, List.of(nodeToRead));

    return requireNonNull(response.getResults())[0];
  }

  // snippet:history:end

  // snippet:history-install:start
  static RawHistoryService installHistoryProvider(OpcUaServer server) {
    var history = new RawHistoryService(server);
    server.addLifecycleParticipant(history.namespace);
    server.getConfig().getEndpoints().stream()
        .map(endpoint -> EndpointUtil.getPath(endpoint.getEndpointUrl()))
        .filter(path -> !path.endsWith("/discovery"))
        .distinct()
        .forEach(path -> server.addServiceSet(path, history));
    return history;
  }

  // snippet:history-install:end

  /**
   * Intercepts explicit releases for the local provider. Ordinary reads retain the SDK's routing,
   * limits, diagnostic results, and counters. Other attribute services use inherited behavior.
   */
  static final class RawHistoryService extends DefaultAttributeServiceSet {
    private final OpcUaServer server;
    final RawHistoryNamespace namespace;

    RawHistoryService(OpcUaServer server) {
      super(server);
      this.server = server;
      namespace = new RawHistoryNamespace(server);
    }

    // snippet:history-release:start
    @Override
    public HistoryReadResponse onHistoryRead(
        ServiceRequestContext context, HistoryReadRequest request) throws UaException {
      if (!request.getReleaseContinuationPoints()) {
        return super.onHistoryRead(context, request);
      }

      Session session = server.getSessionManager().getSession(context, request.getRequestHeader());
      try {
        HistoryReadValueId[] nodes = request.getNodesToRead();
        if (nodes == null || nodes.length == 0) {
          throw new UaException(StatusCodes.Bad_NothingToDo);
        }
        long maxNodesPerRead = server.getConfig().getLimits().getMaxNodesPerRead().longValue();
        if (nodes.length > maxNodesPerRead) {
          throw new UaException(StatusCodes.Bad_TooManyOperations);
        }
        if (request.getTimestampsToReturn() == null) {
          throw new UaException(StatusCodes.Bad_TimestampsToReturnInvalid);
        }

        HistoryReadResult[] results = new HistoryReadResult[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
          results[i] = namespace.release(session, nodes[i]);
        }

        var diagnostics = new DiagnosticsContext<HistoryReadValueId>();
        return new HistoryReadResponse(
            createResponseHeader(request), results, diagnostics.getDiagnosticInfos(nodes));
      } catch (UaException e) {
        session.getSessionDiagnostics().getHistoryReadCount().incrementErrorCount();
        session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();
        throw e;
      } finally {
        session.getSessionDiagnostics().getHistoryReadCount().incrementTotalCount();
        session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
      }
    }

    // snippet:history-release:end

    int cursorCount() {
      return namespace.cursorCount();
    }

    int releaseCount() {
      return namespace.releaseCount();
    }

    void clear() {
      namespace.clear();
    }
  }

  /**
   * Fixed in-memory raw history with at most eight Session-bound cursors. The fixture accepts one
   * query shape and has no database or history authorization policy.
   */
  static final class RawHistoryNamespace extends ManagedNamespaceWithLifecycle {
    final NodeId historyNode = newNodeId("wiki-history");
    private final List<DataValue> stored =
        List.of(
            new DataValue(new Variant(10), StatusCode.GOOD, START),
            new DataValue(
                new Variant(20),
                new StatusCode(StatusCodes.Bad_SensorFailure),
                new DateTime(Instant.parse("2026-01-01T00:00:01Z"))),
            new DataValue(
                new Variant(30),
                StatusCode.GOOD,
                new DateTime(Instant.parse("2026-01-01T00:00:02Z"))));
    private final Map<ByteString, Cursor> cursors = new LinkedHashMap<>();
    private int releaseCount;
    private final SessionListener listener =
        new SessionListener() {
          @Override
          public void onSessionClosed(Session session) {
            releaseSession(session.getSessionId());
          }
        };

    RawHistoryNamespace(OpcUaServer server) {
      super(server, "urn:eclipse:milo:wiki:history");
      getLifecycleManager()
          .addLifecycle(
              new Lifecycle() {
                @Override
                public void startup() {
                  server.getSessionManager().addSessionListener(listener);
                }

                @Override
                public void shutdown() {
                  server.getSessionManager().removeSessionListener(listener);
                  clear();
                }
              });
    }

    @Override
    public synchronized List<HistoryReadResult> historyRead(
        HistoryReadContext context,
        HistoryReadDetails details,
        TimestampsToReturn timestamps,
        List<HistoryReadValueId> nodes) {
      Session session = context.getSession().orElseThrow();
      List<ByteString> requestedPoints =
          nodes.stream().map(HistoryReadValueId::getContinuationPoint).toList();
      // Only prior-request points are reclaimable. Protect continuations supplied in this request.
      Iterator<ByteString> reclaimable =
          cursors.entrySet().stream()
              .filter(entry -> entry.getValue().sessionId().equals(session.getSessionId()))
              .map(Map.Entry::getKey)
              .filter(point -> !requestedPoints.contains(point))
              .toList()
              .iterator();
      List<HistoryReadResult> results = new ArrayList<>();
      for (HistoryReadValueId node : nodes) {
        if (!node.getNodeId().equals(historyNode)) {
          results.add(error(StatusCodes.Bad_NodeIdUnknown));
          continue;
        }
        if (details instanceof ReadRawModifiedDetails empty
            && !empty.getIsReadModified()
            && !empty.getReturnBounds()
            && empty.getStartTime().equals(END)
            && empty.getEndTime().equals(new DateTime(Instant.parse("2026-01-01T00:02:00Z")))
            && empty.getNumValuesPerNode().equals(uint(2))
            && timestamps == TimestampsToReturn.Both
            && (node.getIndexRange() == null || node.getIndexRange().isEmpty())
            && node.getContinuationPoint().isNullOrEmpty()) {
          results.add(
              new HistoryReadResult(
                  new StatusCode(StatusCodes.Good_NoData), ByteString.NULL_VALUE, null));
          continue;
        }
        if (!(details instanceof ReadRawModifiedDetails raw)
            || raw.getIsReadModified()
            || raw.getReturnBounds()
            || !raw.getStartTime().equals(START)
            || !raw.getEndTime().equals(END)
            || !raw.getNumValuesPerNode().equals(uint(2))
            || timestamps != TimestampsToReturn.Both
            || (node.getIndexRange() != null && !node.getIndexRange().isEmpty())) {
          results.add(error(StatusCodes.Bad_HistoryOperationUnsupported));
          continue;
        }
        // Session-close callbacks are asynchronous. Do not create new state for a closed Session.
        if (session.isClosed()) {
          results.add(error(StatusCodes.Bad_SessionClosed));
          continue;
        }
        ByteString supplied = node.getContinuationPoint();
        int offset = 0;
        if (supplied != null && !supplied.isNullOrEmpty()) {
          Cursor saved = cursors.get(supplied);
          if (saved == null || !saved.sessionId().equals(session.getSessionId())) {
            results.add(error(StatusCodes.Bad_ContinuationPointInvalid));
            continue;
          }
          cursors.remove(supplied);
          offset = saved.offset();
        }
        int end = Math.min(offset + 2, stored.size());
        ByteString next = ByteString.NULL_VALUE;
        if (end < stored.size()) {
          while (cursors.size() >= 8 && reclaimable.hasNext()) {
            cursors.remove(reclaimable.next());
          }
          if (cursors.size() >= 8) {
            results.add(error(StatusCodes.Bad_NoContinuationPoints));
            continue;
          }
          next =
              ByteString.of(
                  UUID.randomUUID().toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
          cursors.put(next, new Cursor(session.getSessionId(), end));
        }
        HistoryData data = new HistoryData(stored.subList(offset, end).toArray(DataValue[]::new));
        results.add(
            new HistoryReadResult(
                StatusCode.GOOD,
                next,
                ExtensionObject.encode(getServer().getStaticEncodingContext(), data)));
      }
      return results;
    }

    synchronized HistoryReadResult release(Session session, HistoryReadValueId node) {
      Cursor cursor = cursors.get(node.getContinuationPoint());
      if (!node.getNodeId().equals(historyNode)
          || cursor == null
          || !cursor.sessionId().equals(session.getSessionId())) {
        return error(StatusCodes.Bad_ContinuationPointInvalid);
      }
      cursors.remove(node.getContinuationPoint());
      releaseCount++;
      return new HistoryReadResult(StatusCode.GOOD, ByteString.NULL_VALUE, null);
    }

    synchronized int cursorCount() {
      return cursors.size();
    }

    synchronized int releaseCount() {
      return releaseCount;
    }

    synchronized void clear() {
      cursors.clear();
    }

    private synchronized void releaseSession(NodeId sessionId) {
      cursors.values().removeIf(cursor -> cursor.sessionId().equals(sessionId));
    }

    private static HistoryReadResult error(long code) {
      return new HistoryReadResult(new StatusCode(code), ByteString.NULL_VALUE, null);
    }

    private record Cursor(NodeId sessionId, int offset) {}
  }
}
