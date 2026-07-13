/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.CallContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionManager;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionDiagnostics;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class DefaultMethodServiceSetTest {

  @Test
  void mixedBatchDispatchesOnlyAllowedMethodsAndPreservesResultOrder() throws Exception {
    CallMethodRequest deniedRequest =
        new CallMethodRequest(new NodeId(1, "denied-object"), new NodeId(1, "denied-method"), null);
    CallMethodRequest allowedRequest =
        new CallMethodRequest(
            new NodeId(1, "allowed-object"), new NodeId(1, "allowed-method"), null);

    RequestHeader requestHeader =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
    CallRequest request =
        new CallRequest(requestHeader, new CallMethodRequest[] {deniedRequest, allowedRequest});

    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    SessionManager sessionManager = mock(SessionManager.class);
    Session session = mock(Session.class);
    AccessController accessController = mock(AccessController.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
    ServiceRequestContext context = mock(ServiceRequestContext.class);

    when(server.getConfig()).thenReturn(config);
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});
    when(server.getSessionManager()).thenReturn(sessionManager);
    when(sessionManager.getSession(context, requestHeader)).thenReturn(session);
    when(session.getSessionDiagnostics()).thenReturn(new SessionDiagnostics(session));
    when(server.getAccessController()).thenReturn(accessController);
    when(accessController.checkCallAccess(session, List.of(deniedRequest, allowedRequest)))
        .thenReturn(
            Map.of(
                deniedRequest, AccessResult.DENIED_USER_ACCESS,
                allowedRequest, AccessResult.ALLOWED));
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);

    AtomicBoolean deniedMethodInvoked = new AtomicBoolean();
    when(addressSpaceManager.call(any(CallContext.class), any()))
        .thenAnswer(
            invocation -> {
              List<CallMethodRequest> dispatched = invocation.getArgument(1);
              if (dispatched.contains(deniedRequest)) {
                deniedMethodInvoked.set(true);
              }

              return dispatched.stream()
                  .map(ignored -> new CallMethodResult(StatusCode.GOOD, null, null, null))
                  .toList();
            });

    CallResponse response = new DefaultMethodServiceSet(server).onCall(context, request);

    assertFalse(deniedMethodInvoked.get());
    verify(addressSpaceManager).call(any(CallContext.class), eq(List.of(allowedRequest)));
    verifyNoMoreInteractions(addressSpaceManager);
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied), response.getResults()[0].getStatusCode());
    assertEquals(StatusCode.GOOD, response.getResults()[1].getStatusCode());
  }
}
