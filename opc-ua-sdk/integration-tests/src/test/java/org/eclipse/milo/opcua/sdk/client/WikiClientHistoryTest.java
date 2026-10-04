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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
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
    TestServer fixture = TestServer.create();
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

  // A page budget is an intentional early stop; its finally block must issue a release request.
  @Test
  void earlyStopReleasesItsContinuationPoint() throws Exception {
    int releases = history.releaseCount;
    assertEquals(2, readRawHistory(client, history.namespace.historyNode, START, END, 1).size());
    assertEquals(releases + 1, history.releaseCount);
    assertEquals(0, history.cursorCount());
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

  // Exhaustion must leave existing cursors usable and explicit release must recover capacity.
  @Test
  void ninthCursorIsRejectedAndReleasingEightRestoresCapacity() throws Exception {
    var details = new ReadRawModifiedDetails(false, START, END, uint(2), false);
    var first = historyRequest(ByteString.NULL_VALUE);
    List<HistoryReadValueId> open = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      HistoryReadResult result =
          requireNonNull(
              client
                  .historyRead(details, TimestampsToReturn.Both, false, List.of(first))
                  .getResults())[0];
      assertTrue(result.getStatusCode().isGood());
      assertFalse(result.getContinuationPoint().isNullOrEmpty());
      open.add(historyRequest(result.getContinuationPoint()));
    }
    assertEquals(8, history.cursorCount());
    HistoryReadResult ninth =
        requireNonNull(
            client
                .historyRead(details, TimestampsToReturn.Both, false, List.of(first))
                .getResults())[0];
    assertEquals(StatusCodes.Bad_NoContinuationPoints, ninth.getStatusCode().value());
    for (HistoryReadValueId cursor : open) {
      HistoryReadResult released =
          requireNonNull(
              client
                  .historyRead(details, TimestampsToReturn.Both, true, List.of(cursor))
                  .getResults())[0];
      assertTrue(released.getStatusCode().isGood());
    }
    assertEquals(0, history.cursorCount());
    assertEquals(3, readRawHistory(client, history.namespace.historyNode, START, END, 10).size());
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
    if (maxPages < 1) throw new IllegalArgumentException("Page limit must be positive");
    var details = new ReadRawModifiedDetails(false, start, end, uint(2), false);
    ByteString cursor = ByteString.NULL_VALUE;
    List<DataValue> values = new ArrayList<>();
    try {
      for (int page = 0; page < maxPages; page++) {
        var request = new HistoryReadValueId(nodeId, null, QualifiedName.NULL_VALUE, cursor);
        HistoryReadResult[] results =
            client
                .historyRead(details, TimestampsToReturn.Both, false, List.of(request))
                .getResults();
        if (results == null || results.length != 1) {
          throw new IllegalStateException("Missing history result");
        }
        HistoryReadResult result = results[0];
        cursor = result.getContinuationPoint();
        if (!result.getStatusCode().isGood()) throw new UaException(result.getStatusCode());
        ExtensionObject encoded = result.getHistoryData();
        if (encoded != null && !encoded.isNull()) {
          Object body = encoded.decode(client.getStaticEncodingContext());
          if (!(body instanceof HistoryData data)) {
            throw new IllegalStateException("Expected HistoryData");
          }
          if (data.getDataValues() != null) values.addAll(Arrays.asList(data.getDataValues()));
        }
        if (cursor == null || cursor.isNullOrEmpty()) break;
      }
      return values;
    } finally {
      if (cursor != null && !cursor.isNullOrEmpty()) {
        var release = new HistoryReadValueId(nodeId, null, QualifiedName.NULL_VALUE, cursor);
        HistoryReadResult[] released =
            client
                .historyRead(details, TimestampsToReturn.Both, true, List.of(release))
                .getResults();
        if (released == null || released.length != 1) {
          throw new IllegalStateException("Missing release result");
        }
        if (!released[0].getStatusCode().isGood()) {
          throw new UaException(released[0].getStatusCode());
        }
      }
    }
  }

  // snippet:history:end

  // snippet:history-install:start
  static RawHistoryService installHistoryProvider(OpcUaServer server) {
    var history = new RawHistoryService(server);
    server.addLifecycleParticipant(history.namespace);
    server.getConfig().getEndpoints().stream()
        .map(endpoint -> endpoint.getPath())
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
    volatile int releaseCount;

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
        if (nodes == null || nodes.length == 0) throw new UaException(StatusCodes.Bad_NothingToDo);
        if (nodes.length > server.getConfig().getLimits().getMaxNodesPerRead().longValue()) {
          throw new UaException(StatusCodes.Bad_TooManyOperations);
        }
        if (request.getTimestampsToReturn() == null) {
          throw new UaException(StatusCodes.Bad_TimestampsToReturnInvalid);
        }
        var diagnostics = new DiagnosticsContext<HistoryReadValueId>();
        HistoryReadResult[] results = new HistoryReadResult[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
          results[i] = namespace.release(session, nodes[i]);
          if (results[i].getStatusCode().isGood()) releaseCount++;
        }
        return new HistoryReadResponse(
            createResponseHeader(request), results, diagnostics.getDiagnosticInfos(nodes));
      } catch (UaException failure) {
        session.getSessionDiagnostics().getHistoryReadCount().incrementErrorCount();
        session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();
        throw failure;
      } finally {
        session.getSessionDiagnostics().getHistoryReadCount().incrementTotalCount();
        session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
      }
    }

    // snippet:history-release:end

    int cursorCount() {
      return namespace.cursorCount();
    }

    void clear() {
      namespace.clear();
    }
  }

  /**
   * Fixed in-memory raw history with at most eight Session-bound cursors. The fixture accepts one
   * query shape and has no database, automatic expiry, or history authorization policy.
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
    private final Map<ByteString, Cursor> cursors = new HashMap<>();
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
            && node.getDataEncoding().isNull()
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
            || (node.getIndexRange() != null && !node.getIndexRange().isEmpty())
            || !node.getDataEncoding().isNull()) {
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
        if (cursors.size() >= 8) {
          results.add(error(StatusCodes.Bad_NoContinuationPoints));
          continue;
        }
        int end = Math.min(offset + 2, stored.size());
        ByteString next = ByteString.NULL_VALUE;
        if (end < stored.size()) {
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
      return new HistoryReadResult(StatusCode.GOOD, ByteString.NULL_VALUE, null);
    }

    synchronized int cursorCount() {
      return cursors.size();
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
