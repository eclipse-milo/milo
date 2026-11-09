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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.HistoryReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionManager;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionDiagnostics;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class DefaultAttributeServiceSetTest {

  /**
   * HistoryRead must not hand an operation the AccessController denied to the AddressSpace, whose
   * history code would otherwise be the only thing deciding who may read history. The denied
   * operation is answered with the denial's status, in its place in the results.
   */
  @Test
  void historyReadDispatchesOnlyAllowedOperationsAndPreservesResultOrder() throws Exception {
    var denied = new HistoryReadValueId(new NodeId(1, "denied"), null, null, null);
    var allowed = new HistoryReadValueId(new NodeId(1, "allowed"), null, null, null);
    var unknown = new HistoryReadValueId(new NodeId(1, "unknown"), null, null, null);

    var details =
        new ReadRawModifiedDetails(false, DateTime.MIN_VALUE, DateTime.now(), uint(0), false);

    RequestHeader requestHeader =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
    HistoryReadRequest request =
        new HistoryReadRequest(
            requestHeader,
            ExtensionObject.encode(DefaultEncodingContext.INSTANCE, details),
            TimestampsToReturn.Both,
            false,
            new HistoryReadValueId[] {denied, allowed, unknown});

    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    SessionManager sessionManager = mock(SessionManager.class);
    Session session = mock(Session.class);
    AccessController accessController = mock(AccessController.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
    ServiceRequestContext context = mock(ServiceRequestContext.class);

    when(server.getConfig()).thenReturn(config);
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});
    when(config.getEncodingLimits()).thenReturn(EncodingLimits.DEFAULT);
    when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    when(server.getSessionManager()).thenReturn(sessionManager);
    when(sessionManager.getSession(context, requestHeader)).thenReturn(session);
    when(session.getSessionDiagnostics()).thenReturn(new SessionDiagnostics(session));
    when(server.getAccessController()).thenReturn(accessController);
    when(accessController.checkHistoryReadAccess(session, List.of(denied, allowed, unknown)))
        .thenReturn(
            Map.of(
                denied, AccessResult.DENIED_USER_ACCESS,
                allowed, AccessResult.ALLOWED,
                unknown, AccessResult.NODE_UNKNOWN));
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);

    // The AddressSpace answers Good for the Node it knows and Bad_NodeIdUnknown for the other.
    var dispatched = new ArrayList<HistoryReadValueId>();
    when(addressSpaceManager.historyRead(
            any(HistoryReadContext.class), eq(details), eq(TimestampsToReturn.Both), any()))
        .thenAnswer(
            invocation -> {
              List<HistoryReadValueId> group = invocation.getArgument(3);
              dispatched.addAll(group);

              return group.stream()
                  .map(
                      id ->
                          new HistoryReadResult(
                              id == unknown
                                  ? new StatusCode(StatusCodes.Bad_NodeIdUnknown)
                                  : StatusCode.GOOD,
                              null,
                              null))
                  .toList();
            });

    HistoryReadResponse response =
        new DefaultAttributeServiceSet(server).onHistoryRead(context, request);

    assertEquals(List.of(allowed, unknown), dispatched);

    HistoryReadResult[] results = requireNonNull(response.getResults());

    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), results[0].getStatusCode());
    assertEquals(StatusCode.GOOD, results[1].getStatusCode());
    assertEquals(new StatusCode(StatusCodes.Bad_NodeIdUnknown), results[2].getStatusCode());
  }
}
