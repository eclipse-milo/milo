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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
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
    history = new RawHistoryService(fixture.getServer());
    fixture
        .getServer()
        .getConfig()
        .getEndpoints()
        .forEach(endpoint -> fixture.getServer().addServiceSet(endpoint.getPath(), history));
    return fixture;
  }

  @AfterEach
  void releaseFixtureState() {
    history.clear();
  }

  // Consuming every page preserves stored quality/timestamps and leaves no server cursor.
  @Test
  void rawHistoryDrainsPagesWithoutDiscardingBadStoredValues() throws Exception {
    List<DataValue> values = readRawHistory(client, RawHistoryService.HISTORY_NODE, START, END, 10);
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
    assertEquals(2, readRawHistory(client, RawHistoryService.HISTORY_NODE, START, END, 1).size());
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
            RawHistoryService.HISTORY_NODE, null, QualifiedName.NULL_VALUE, ByteString.NULL_VALUE);
    HistoryReadResult result =
        requireNonNull(
            client
                .historyRead(details, TimestampsToReturn.Both, false, List.of(first))
                .getResults())[0];
    ByteString cursor = result.getContinuationPoint();
    assertFalse(cursor.isNullOrEmpty());
    var next =
        new HistoryReadValueId(
            RawHistoryService.HISTORY_NODE, null, QualifiedName.NULL_VALUE, cursor);
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
        Object body = result.getHistoryData().decode(client.getStaticEncodingContext());
        if (!(body instanceof HistoryData data)) {
          throw new IllegalStateException("Expected HistoryData");
        }
        if (data.getDataValues() != null) values.addAll(Arrays.asList(data.getDataValues()));
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

  /**
   * A bounded local raw-history provider for client examples. It accepts one fixed interval and
   * two-value pages, preserves stored quality and timestamps, and binds opaque cursors to a
   * Session. It implements the full service boundary because the baseline AddressSpace history hook
   * does not receive the releaseContinuationPoints request flag. This is not a production
   * historian.
   */
  static final class RawHistoryService extends DefaultAttributeServiceSet {
    static final NodeId HISTORY_NODE = new NodeId(1, "wiki-history");
    private final OpcUaServer server;
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
    volatile int releaseCount;

    RawHistoryService(OpcUaServer server) {
      super(server);
      this.server = server;
    }

    @Override
    public synchronized HistoryReadResponse onHistoryRead(
        ServiceRequestContext context, HistoryReadRequest request) throws UaException {
      Session session = server.getSessionManager().getSession(context, request.getRequestHeader());
      HistoryReadValueId[] nodes = request.getNodesToRead();
      if (nodes == null || nodes.length == 0) throw new UaException(StatusCodes.Bad_NothingToDo);
      Object decoded = request.getHistoryReadDetails().decode(server.getStaticEncodingContext());
      List<HistoryReadResult> results = new ArrayList<>();
      for (HistoryReadValueId node : nodes) {
        if (!node.getNodeId().equals(HISTORY_NODE)) {
          results.add(error(StatusCodes.Bad_HistoryOperationUnsupported));
          continue;
        }
        if (!(decoded instanceof ReadRawModifiedDetails raw)
            || raw.getIsReadModified()
            || raw.getReturnBounds()
            || !raw.getStartTime().equals(START)
            || !raw.getEndTime().equals(END)
            || !raw.getNumValuesPerNode().equals(uint(2))
            || request.getTimestampsToReturn() != TimestampsToReturn.Both
            || (node.getIndexRange() != null && !node.getIndexRange().isEmpty())
            || !node.getDataEncoding().isNull()) {
          results.add(error(StatusCodes.Bad_HistoryOperationUnsupported));
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
        if (request.getReleaseContinuationPoints()) {
          releaseCount++;
          results.add(new HistoryReadResult(StatusCode.GOOD, ByteString.NULL_VALUE, null));
          continue;
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
                ExtensionObject.encode(server.getStaticEncodingContext(), data)));
      }
      return new HistoryReadResponse(
          createResponseHeader(request), results.toArray(HistoryReadResult[]::new), null);
    }

    synchronized int cursorCount() {
      return cursors.size();
    }

    synchronized void clear() {
      cursors.clear();
    }

    private static HistoryReadResult error(long code) {
      return new HistoryReadResult(new StatusCode(code), ByteString.NULL_VALUE, null);
    }

    private record Cursor(NodeId sessionId, int offset) {}
  }
}
