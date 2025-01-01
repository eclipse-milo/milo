/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.sdk.server.ContinuationPoint;
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

      throw UaException.extract(e).orElse(new UaException(e));
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

      throw UaException.extract(e).orElse(new UaException(e));
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

      throw UaException.extract(e).orElse(new UaException(e));
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

      throw UaException.extract(e).orElse(new UaException(e));
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

      throw UaException.extract(e).orElse(new UaException(e));
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

    if (nodesToBrowse.size() > server.getConfig().getLimits().getMaxNodesPerBrowse().intValue()) {
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

    if (continuationPoints.size()
        > server.getConfig().getLimits().getMaxBrowseContinuationPoints().intValue()) {

      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var results = new ArrayList<BrowseResult>();

    for (ByteString bs : continuationPoints) {
      if (request.getReleaseContinuationPoints()) {
        results.add(release(session, bs));
      } else {
        results.add(references(session, bs));
      }
    }

    var header = createResponseHeader(request);

    return new BrowseNextResponse(
        header, results.toArray(BrowseResult[]::new), new DiagnosticInfo[0]);
  }

  private static BrowseResult release(Session session, ByteString bs) {
    ContinuationPoint c = session.getBrowseContinuationPoints().remove(bs);

    return c != null
        ? new BrowseResult(StatusCode.GOOD, null, null)
        : new BrowseResult(new StatusCode(StatusCodes.Bad_ContinuationPointInvalid), null, null);
  }

  private static BrowseResult references(Session session, ByteString bs) {
    ContinuationPoint c = session.getBrowseContinuationPoints().remove(bs);

    if (c != null) {
      int max = c.max();
      List<ReferenceDescription> references = c.references();

      if (references.size() > max) {
        List<ReferenceDescription> subList = references.subList(0, max);
        List<ReferenceDescription> current = List.copyOf(subList);
        subList.clear();

        session.getBrowseContinuationPoints().put(c.id(), c);

        return new BrowseResult(
            StatusCode.GOOD, c.id(), current.toArray(new ReferenceDescription[0]));
      } else {
        return new BrowseResult(
            StatusCode.GOOD, null, references.toArray(new ReferenceDescription[0]));
      }
    } else {
      return new BrowseResult(new StatusCode(StatusCodes.Bad_ContinuationPointInvalid), null, null);
    }
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
