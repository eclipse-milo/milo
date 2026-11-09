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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.AddNodesContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionManager;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionDiagnostics;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class DefaultNodeManagementServiceSetTest {

  /**
   * AddNodes must not hand the AddressSpace an item the AccessController denied: the denied item is
   * answered with the status of its denial and a null AddedNodeId (Part 4 §5.8.2), and the allowed
   * items keep their position in the results.
   */
  @Test
  void addNodesDispatchesOnlyAllowedItemsAndPreservesResultOrder() throws Exception {
    AddNodesItem firstAllowedItem = addNodesItem("first-allowed");
    AddNodesItem deniedItem = addNodesItem("denied");
    AddNodesItem secondAllowedItem = addNodesItem("second-allowed");

    RequestHeader requestHeader =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
    AddNodesRequest request =
        new AddNodesRequest(
            requestHeader, new AddNodesItem[] {firstAllowedItem, deniedItem, secondAllowedItem});

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
    when(server.getSessionManager()).thenReturn(sessionManager);
    when(sessionManager.getSession(context, requestHeader)).thenReturn(session);
    when(session.getSessionDiagnostics()).thenReturn(new SessionDiagnostics(session));
    when(server.getAccessController()).thenReturn(accessController);
    when(accessController.checkAddNodesAccess(
            session, List.of(firstAllowedItem, deniedItem, secondAllowedItem)))
        .thenReturn(
            Map.of(
                firstAllowedItem, AccessResult.ALLOWED,
                deniedItem, AccessResult.DENIED_USER_ACCESS,
                secondAllowedItem, AccessResult.ALLOWED));
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);

    when(addressSpaceManager.addNodes(any(AddNodesContext.class), any()))
        .thenAnswer(
            invocation -> {
              List<AddNodesItem> dispatched = invocation.getArgument(1);

              return dispatched.stream()
                  .map(
                      item ->
                          new AddNodesResult(
                              StatusCode.GOOD, new NodeId(1, item.getBrowseName().getName())))
                  .toList();
            });

    AddNodesResponse response =
        new DefaultNodeManagementServiceSet(server).onAddNodes(context, request);

    verify(addressSpaceManager)
        .addNodes(any(AddNodesContext.class), eq(List.of(firstAllowedItem, secondAllowedItem)));
    verifyNoMoreInteractions(addressSpaceManager);

    AddNodesResult[] results = requireNonNull(response.getResults());

    assertEquals(
        new AddNodesResult(StatusCode.GOOD, new NodeId(1, "first-allowed")),
        results[0],
        "an allowed item keeps the AddressSpace's result");
    assertEquals(
        new AddNodesResult(new StatusCode(StatusCodes.Bad_UserAccessDenied), NodeId.NULL_VALUE),
        results[1]);
    assertEquals(new AddNodesResult(StatusCode.GOOD, new NodeId(1, "second-allowed")), results[2]);
  }

  private static AddNodesItem addNodesItem(String name) {
    return new AddNodesItem(
        new NodeId(1, "parent").expanded(),
        NodeIds.HasComponent,
        ExpandedNodeId.NULL_VALUE,
        new QualifiedName(1, name),
        NodeClass.Object,
        null,
        NodeIds.BaseObjectType.expanded());
  }
}
