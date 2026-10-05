/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki.snippets;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

/** Client and server historical access samples from the Wiki. */
public final class HistorySnippets {

  private HistorySnippets() {}

  // snippet:history:start
  static List<DataValue> readRawHistory(
      OpcUaClient client, NodeId nodeId, DateTime start, DateTime end, int maxPages)
      throws UaException {
    if (maxPages < 1) {
      throw new IllegalArgumentException("maxPages must be positive");
    }

    // Unmodified raw values, at most 1,000 per page, without bounding values.
    var details = new ReadRawModifiedDetails(false, start, end, uint(1_000), false);
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

        values.addAll(decodeHistoryData(client, result));

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

  // snippet:history:end

  // snippet:history-request:start
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

  static List<DataValue> decodeHistoryData(OpcUaClient client, HistoryReadResult result) {
    // A Good_NoData result can omit the HistoryData body.
    ExtensionObject historyData = result.getHistoryData();
    if (historyData == null || historyData.isNull()) {
      return List.of();
    }

    var data = (HistoryData) historyData.decode(client.getStaticEncodingContext());
    DataValue[] dataValues = data.getDataValues();
    if (dataValues == null) {
      return List.of();
    }
    return Arrays.asList(dataValues);
  }

  // snippet:history-request:end

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
   * An attribute service set that handles explicit continuation-point releases for a {@link
   * RawHistoryNamespace} and passes every other request to the inherited service.
   */
  static final class RawHistoryService extends DefaultAttributeServiceSet {
    private final OpcUaServer server;
    final RawHistoryNamespace namespace;

    RawHistoryService(OpcUaServer server) {
      super(server);
      this.server = server;
      this.namespace = new RawHistoryNamespace(server);
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
  }

  /**
   * The continuation points of a raw history provider, each owned by one Session and node.
   *
   * <p>A complete provider also overrides {@code historyRead} to page its stored values and calls
   * {@link #save} for each continuation point it returns.
   */
  static final class RawHistoryNamespace extends ManagedNamespaceWithLifecycle {
    private final Map<ByteString, ContinuationPoint> continuationPoints = new HashMap<>();

    RawHistoryNamespace(OpcUaServer server) {
      super(server, "urn:eclipse:milo:wiki:history");
    }

    /** Records a continuation point returned to {@code session} for {@code nodeId}. */
    synchronized void save(ByteString point, Session session, NodeId nodeId) {
      continuationPoints.put(point, new ContinuationPoint(session.getSessionId(), nodeId));
    }

    /**
     * Releases one continuation point.
     *
     * @return Good when {@code session} owned the point for the requested node, otherwise {@code
     *     Bad_ContinuationPointInvalid}.
     */
    synchronized HistoryReadResult release(Session session, HistoryReadValueId node) {
      ContinuationPoint point = continuationPoints.get(node.getContinuationPoint());
      if (point == null
          || !point.sessionId().equals(session.getSessionId())
          || !point.nodeId().equals(node.getNodeId())) {
        return new HistoryReadResult(
            new StatusCode(StatusCodes.Bad_ContinuationPointInvalid), ByteString.NULL_VALUE, null);
      }

      continuationPoints.remove(node.getContinuationPoint());
      return new HistoryReadResult(StatusCode.GOOD, ByteString.NULL_VALUE, null);
    }

    private record ContinuationPoint(NodeId sessionId, NodeId nodeId) {}
  }
}
