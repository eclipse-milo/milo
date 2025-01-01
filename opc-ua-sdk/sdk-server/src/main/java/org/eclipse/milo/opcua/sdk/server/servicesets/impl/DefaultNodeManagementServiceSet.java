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

import static org.eclipse.milo.opcua.sdk.core.util.GroupMapCollate.groupMapCollate;
import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddReferencesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.DeleteNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.DeleteReferencesContext;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.NodeManagementServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResult;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DefaultNodeManagementServiceSet implements NodeManagementServiceSet {

  private final OpcUaServer server;

  public DefaultNodeManagementServiceSet(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public AddNodesResponse onAddNodes(ServiceRequestContext context, AddNodesRequest request)
      throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return addNodes(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getDeleteNodesCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();
      throw e;
    } finally {
      session.getSessionDiagnostics().getDeleteNodesCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public DeleteNodesResponse onDeleteNodes(
      ServiceRequestContext context, DeleteNodesRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return deleteNodes(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getDeleteNodesCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();
      throw e;
    } finally {
      session.getSessionDiagnostics().getDeleteNodesCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public AddReferencesResponse onAddReferences(
      ServiceRequestContext context, AddReferencesRequest request) throws UaException {
    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return addReferences(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getAddReferencesCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();
      throw e;
    } finally {
      session.getSessionDiagnostics().getAddReferencesCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public DeleteReferencesResponse onDeleteReferences(
      ServiceRequestContext context, DeleteReferencesRequest request) throws UaException {
    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return deleteReferences(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getDeleteReferencesCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();
      throw e;
    } finally {
      session.getSessionDiagnostics().getDeleteReferencesCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  private AddNodesResponse addNodes(AddNodesRequest request, Session session) throws UaException {

    List<AddNodesItem> nodesToAdd = Lists.ofNullable(request.getNodesToAdd());

    if (nodesToAdd.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (nodesToAdd.size()
        > server.getConfig().getLimits().getMaxNodesPerNodeManagement().longValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var diagnosticsContext = new DiagnosticsContext<AddNodesItem>();

    var addNodesContext =
        new AddNodesContext(
            server,
            session,
            diagnosticsContext,
            request.getRequestHeader().getAuditEntryId(),
            request.getRequestHeader().getTimeoutHint(),
            request.getRequestHeader().getAdditionalHeader());

    List<AddNodesResult> results =
        server.getAddressSpaceManager().addNodes(addNodesContext, nodesToAdd);

    DiagnosticInfo[] diagnosticInfos = diagnosticsContext.getDiagnosticInfos(nodesToAdd);

    ResponseHeader header = createResponseHeader(request);

    return new AddNodesResponse(header, results.toArray(AddNodesResult[]::new), diagnosticInfos);
  }

  private DeleteNodesResponse deleteNodes(DeleteNodesRequest request, Session session)
      throws UaException {

    List<DeleteNodesItem> nodesToDelete = Lists.ofNullable(request.getNodesToDelete());

    if (nodesToDelete.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (nodesToDelete.size()
        > server.getConfig().getLimits().getMaxNodesPerNodeManagement().longValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var diagnosticsContext = new DiagnosticsContext<DeleteNodesItem>();

    Map<DeleteNodesItem, AccessResult> accessResults =
        server.getAccessController().checkDeleteNodesAccess(session, nodesToDelete);

    List<StatusCode> results =
        groupMapCollate(
            nodesToDelete,
            accessResults::get,
            accessResult ->
                group -> {
                  if (accessResult instanceof AccessResult.Denied denied) {
                    return Collections.nCopies(group.size(), denied.statusCode());
                  } else {
                    var deleteNodesContext =
                        new DeleteNodesContext(
                            server,
                            session,
                            diagnosticsContext,
                            request.getRequestHeader().getAuditEntryId(),
                            request.getRequestHeader().getTimeoutHint(),
                            request.getRequestHeader().getAdditionalHeader());

                    return server.getAddressSpaceManager().deleteNodes(deleteNodesContext, group);
                  }
                });

    DiagnosticInfo[] diagnosticInfos = diagnosticsContext.getDiagnosticInfos(nodesToDelete);

    ResponseHeader header = createResponseHeader(request);

    return new DeleteNodesResponse(header, results.toArray(StatusCode[]::new), diagnosticInfos);
  }

  private AddReferencesResponse addReferences(AddReferencesRequest request, Session session)
      throws UaException {

    List<AddReferencesItem> referencesToAdd = Lists.ofNullable(request.getReferencesToAdd());

    if (referencesToAdd.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (referencesToAdd.size()
        > server.getConfig().getLimits().getMaxNodesPerNodeManagement().longValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var diagnosticsContext = new DiagnosticsContext<AddReferencesItem>();

    Map<AddReferencesItem, AccessResult> accessResults =
        server.getAccessController().checkAddReferencesAccess(session, referencesToAdd);

    List<StatusCode> results =
        groupMapCollate(
            referencesToAdd,
            accessResults::get,
            accessResult ->
                group -> {
                  if (accessResult instanceof AccessResult.Denied denied) {
                    return Collections.nCopies(group.size(), denied.statusCode());
                  } else {
                    var addReferencesContext =
                        new AddReferencesContext(
                            server,
                            session,
                            diagnosticsContext,
                            request.getRequestHeader().getAuditEntryId(),
                            request.getRequestHeader().getTimeoutHint(),
                            request.getRequestHeader().getAdditionalHeader());

                    return server
                        .getAddressSpaceManager()
                        .addReferences(addReferencesContext, group);
                  }
                });

    DiagnosticInfo[] diagnosticInfos = diagnosticsContext.getDiagnosticInfos(referencesToAdd);

    ResponseHeader header = createResponseHeader(request);

    return new AddReferencesResponse(header, results.toArray(new StatusCode[0]), diagnosticInfos);
  }

  private DeleteReferencesResponse deleteReferences(
      DeleteReferencesRequest request, Session session) throws UaException {

    List<DeleteReferencesItem> referencesToDelete =
        Lists.ofNullable(request.getReferencesToDelete());

    if (referencesToDelete.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (referencesToDelete.size()
        > server.getConfig().getLimits().getMaxNodesPerNodeManagement().longValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var diagnosticsContext = new DiagnosticsContext<DeleteReferencesItem>();

    Map<DeleteReferencesItem, AccessResult> accessResults =
        server.getAccessController().checkDeleteReferencesAccess(session, referencesToDelete);

    List<StatusCode> results =
        groupMapCollate(
            referencesToDelete,
            accessResults::get,
            accessResult ->
                group -> {
                  if (accessResult instanceof AccessResult.Denied denied) {
                    return Collections.nCopies(group.size(), denied.statusCode());
                  } else {
                    var deleteReferencesContext =
                        new DeleteReferencesContext(
                            server,
                            session,
                            diagnosticsContext,
                            request.getRequestHeader().getAuditEntryId(),
                            request.getRequestHeader().getTimeoutHint(),
                            request.getRequestHeader().getAdditionalHeader());

                    return server
                        .getAddressSpaceManager()
                        .deleteReferences(deleteReferencesContext, group);
                  }
                });

    DiagnosticInfo[] diagnosticInfos = diagnosticsContext.getDiagnosticInfos(referencesToDelete);

    ResponseHeader header = createResponseHeader(request);

    return new DeleteReferencesResponse(
        header, results.toArray(StatusCode[]::new), diagnosticInfos);
  }
}
