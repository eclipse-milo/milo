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
import org.eclipse.milo.opcua.sdk.server.AddressSpace.CallContext;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.MethodServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DefaultMethodServiceSet implements MethodServiceSet {

  private final OpcUaServer server;

  public DefaultMethodServiceSet(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public CallResponse onCall(ServiceRequestContext context, CallRequest request)
      throws UaException {
    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return call(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getCallCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw e;
    } finally {
      session.getSessionDiagnostics().getCallCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  private CallResponse call(CallRequest request, Session session) throws UaException {
    List<CallMethodRequest> methodsToCall = Lists.ofNullable(request.getMethodsToCall());

    if (methodsToCall.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (methodsToCall.size()
        > server.getConfig().getLimits().getMaxNodesPerMethodCall().longValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    Map<CallMethodRequest, AccessResult> accessResults =
        server.getAccessController().checkCallAccess(session, methodsToCall);

    List<CallMethodResult> results =
        groupMapCollate(
            methodsToCall,
            accessResults::get,
            accessResult ->
                group -> {
                  if (accessResult instanceof AccessResult.Denied denied) {
                    var result = new CallMethodResult(denied.statusCode(), null, null, null);
                    return Collections.nCopies(group.size(), result);
                  } else {
                    var diagnosticsContext = new DiagnosticsContext<CallMethodRequest>();

                    var callContext =
                        new CallContext(
                            server,
                            session,
                            diagnosticsContext,
                            request.getRequestHeader().getAuditEntryId(),
                            request.getRequestHeader().getTimeoutHint(),
                            request.getRequestHeader().getAdditionalHeader());

                    return server.getAddressSpaceManager().call(callContext, methodsToCall);
                  }
                });

    ResponseHeader header = createResponseHeader(request);

    return new CallResponse(
        header, results.toArray(CallMethodResult[]::new), new DiagnosticInfo[0]);
  }
}
