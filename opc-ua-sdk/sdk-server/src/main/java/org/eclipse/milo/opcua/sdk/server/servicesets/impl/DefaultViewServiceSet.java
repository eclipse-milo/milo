/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RegisterNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.UnregisterNodesContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.ViewServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowseHelper;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowsePathsHelper;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesResponse;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DefaultViewServiceSet implements ViewServiceSet {

  private final OpcUaServer server;

  public DefaultViewServiceSet(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public BrowseResponse onBrowse(ServiceRequestContext context, BrowseRequest request)
      throws UaException {
    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return browse(request, session).get();
    } catch (Exception e) {
      session.getSessionDiagnostics().getBrowseCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw new UaException(e);
    } finally {
      session.getSessionDiagnostics().getBrowseCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public BrowseNextResponse onBrowseNext(ServiceRequestContext context, BrowseNextRequest request)
      throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return browseNext(request, session);
    } catch (Exception e) {
      session.getSessionDiagnostics().getBrowseNextCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw new UaException(e);
    } finally {
      session.getSessionDiagnostics().getBrowseNextCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public TranslateBrowsePathsToNodeIdsResponse onTranslateBrowsePaths(
      ServiceRequestContext context, TranslateBrowsePathsToNodeIdsRequest request)
      throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      var browsePathsHelper = new BrowsePathsHelper(() -> Optional.ofNullable(session), server);

      return browsePathsHelper.translateBrowsePaths(request);
    } catch (Exception e) {
      session.getSessionDiagnostics().getTranslateBrowsePathsToNodeIdsCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw new UaException(e);
    } finally {
      session.getSessionDiagnostics().getTranslateBrowsePathsToNodeIdsCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public RegisterNodesResponse onRegisterNodes(
      ServiceRequestContext context, RegisterNodesRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return registerNodes(request, session);
    } catch (Exception e) {
      session.getSessionDiagnostics().getRegisterNodesCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw new UaException(e);
    } finally {
      session.getSessionDiagnostics().getRegisterNodesCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public UnregisterNodesResponse onUnregisterNodes(
      ServiceRequestContext context, UnregisterNodesRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return unregisterNodes(request, session);
    } catch (Exception e) {
      session.getSessionDiagnostics().getUnregisterNodesCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw new UaException(e);
    } finally {
      session.getSessionDiagnostics().getUnregisterNodesCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  private CompletableFuture<BrowseResponse> browse(BrowseRequest request, Session session) {
    List<BrowseDescription> nodesToBrowse = Lists.ofNullable(request.getNodesToBrowse());

    if (nodesToBrowse.isEmpty()) {
      return failedUaFuture(StatusCodes.Bad_NothingToDo);
    }

    if (exceedsMaxNodesPerBrowse(nodesToBrowse.size())) {
      return failedUaFuture(StatusCodes.Bad_TooManyOperations);
    }

    if (request.getView().getViewId().isNotNull()
        && !server.getRegisteredViews().contains(request.getView().getViewId())) {

      return failedUaFuture(StatusCodes.Bad_ViewIdUnknown);
    }

    List<BrowseResult> results = BrowseHelper.browse(server, () -> Optional.of(session), request);

    ResponseHeader header = createResponseHeader(request);

    var response =
        new BrowseResponse(header, results.toArray(BrowseResult[]::new), new DiagnosticInfo[0]);

    return CompletableFuture.completedFuture(response);
  }

  private BrowseNextResponse browseNext(BrowseNextRequest request, Session session)
      throws UaException {

    List<ByteString> continuationPoints = Lists.ofNullable(request.getContinuationPoints());

    if (continuationPoints.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    // OPC 10000-5 §6.3.11: MaxNodesPerBrowse bounds both Browse.nodesToBrowse and
    // BrowseNext.continuationPoints; 0 indicates no server-imposed limit.
    if (exceedsMaxNodesPerBrowse(continuationPoints.size())) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var header = createResponseHeader(request);

    if (Boolean.TRUE.equals(request.getReleaseContinuationPoints())) {
      // OPC 10000-4 §5.9.3.2 Table 37: when releaseContinuationPoints is TRUE, release all passed
      // continuation points and return empty results and diagnosticInfos arrays.
      for (ByteString bs : continuationPoints) {
        session.releaseBrowseContinuationPoint(bs);
      }

      return new BrowseNextResponse(header, new BrowseResult[0], new DiagnosticInfo[0]);
    }

    var results = new ArrayList<BrowseResult>(continuationPoints.size());

    for (ByteString bs : continuationPoints) {
      results.add(references(session, bs));
    }

    return new BrowseNextResponse(
        header, results.toArray(BrowseResult[]::new), new DiagnosticInfo[0]);
  }

  private boolean exceedsMaxNodesPerBrowse(int count) {
    long maxNodesPerBrowse = server.getConfig().getLimits().getMaxNodesPerBrowse().longValue();

    return maxNodesPerBrowse > 0L && count > maxNodesPerBrowse;
  }

  private static BrowseResult references(Session session, ByteString bs) {
    return session
        .nextBrowseContinuationPoint(bs)
        .map(
            page ->
                new BrowseResult(
                    StatusCode.GOOD,
                    page.continuationPoint(),
                    page.references().toArray(new ReferenceDescription[0])))
        .orElseGet(
            () ->
                new BrowseResult(
                    new StatusCode(StatusCodes.Bad_ContinuationPointInvalid), null, null));
  }

  private RegisterNodesResponse registerNodes(RegisterNodesRequest request, Session session)
      throws UaException {

    List<NodeId> nodeIds = Lists.ofNullable(request.getNodesToRegister());

    if (nodeIds.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (nodeIds.size() > server.getConfig().getLimits().getMaxNodesPerRegisterNodes().intValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var registerNodesContext = new RegisterNodesContext(server, session);

    List<NodeId> registeredNodeIds =
        server.getAddressSpaceManager().registerNodes(registerNodesContext, nodeIds);

    ResponseHeader header = createResponseHeader(request);

    return new RegisterNodesResponse(header, registeredNodeIds.toArray(new NodeId[0]));
  }

  private UnregisterNodesResponse unregisterNodes(UnregisterNodesRequest request, Session session)
      throws UaException {

    List<NodeId> nodeIds = Lists.ofNullable(request.getNodesToUnregister());

    if (nodeIds.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (nodeIds.size() > server.getConfig().getLimits().getMaxNodesPerRegisterNodes().intValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var unregisterNodesContext = new UnregisterNodesContext(server, session);

    server.getAddressSpaceManager().unregisterNodes(unregisterNodesContext, nodeIds);

    ResponseHeader header = createResponseHeader(request);

    return new UnregisterNodesResponse(header);
  }
}
