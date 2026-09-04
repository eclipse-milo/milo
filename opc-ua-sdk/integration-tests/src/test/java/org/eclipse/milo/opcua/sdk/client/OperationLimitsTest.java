/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class OperationLimitsTest extends AbstractClientServerTest {

  private static final List<NodeId> OPERATION_LIMIT_NODES =
      List.of(
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRead,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes,
          NodeIds
              .Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData,
          NodeIds.Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents);

  @Test
  void readOperationLimits() throws UaException {
    OperationLimits operationLimits = client.readOperationLimits();

    assertTrue(operationLimits.maxNodesPerRead().isPresent());
    assertTrue(operationLimits.maxNodesPerHistoryReadData().isPresent());
    assertTrue(operationLimits.maxNodesPerHistoryReadEvents().isPresent());
    assertTrue(operationLimits.maxNodesPerWrite().isPresent());
    assertTrue(operationLimits.maxNodesPerHistoryUpdateData().isPresent());
    assertTrue(operationLimits.maxNodesPerHistoryUpdateEvents().isPresent());
    assertTrue(operationLimits.maxNodesPerMethodCall().isPresent());
    assertTrue(operationLimits.maxNodesPerBrowse().isPresent());
    assertTrue(operationLimits.maxNodesPerRegisterNodes().isPresent());
    assertTrue(operationLimits.maxNodesPerTranslateBrowsePathsToNodeIds().isPresent());
    assertTrue(operationLimits.maxNodesPerNodeManagement().isPresent());
    assertTrue(operationLimits.maxMonitoredItemsPerCall().isPresent());
  }

  @Test
  void readThrowsWhenDisconnected() throws UaException {
    client.disconnect();

    assertThrows(UaException.class, () -> client.readOperationLimits());
  }

  @Test
  void readHandlesUShortValues() throws UaException {
    // Create a mock client that returns UShort values instead of UInteger
    var mockClient = mock(OpcUaClient.class);

    // Create DataValues with UShort values (simulating a server that returns the wrong type)
    var values =
        List.of(
            new DataValue(new Variant(UShort.valueOf(100))), // maxNodesPerRead
            new DataValue(new Variant(UShort.valueOf(200))), // maxNodesPerWrite
            new DataValue(new Variant(UShort.valueOf(300))), // maxNodesPerMethodCall
            new DataValue(new Variant(UShort.valueOf(400))), // maxNodesPerBrowse
            new DataValue(new Variant(UShort.valueOf(500))), // maxNodesPerRegisterNodes
            new DataValue(
                new Variant(UShort.valueOf(600))), // maxNodesPerTranslateBrowsePathsToNodeIds
            new DataValue(new Variant(UShort.valueOf(700))), // maxNodesPerNodeManagement
            new DataValue(new Variant(UShort.valueOf(800))), // maxMonitoredItemsPerCall
            new DataValue(new Variant(UShort.valueOf(900))), // maxNodesPerHistoryReadData
            new DataValue(new Variant(UShort.valueOf(1000))), // maxNodesPerHistoryReadEvents
            new DataValue(new Variant(UShort.valueOf(1100))), // maxNodesPerHistoryUpdateData
            new DataValue(new Variant(UShort.valueOf(1200)))); // maxNodesPerHistoryUpdateEvents

    when(mockClient.readValues(anyDouble(), any(), anyList())).thenReturn(values);

    // Call the static read method with our mock
    var operationLimits = OperationLimits.read(mockClient);

    // Verify all values are present and correctly converted from UShort to UInteger
    assertTrue(operationLimits.maxNodesPerRead().isPresent());
    assertEquals(100, operationLimits.maxNodesPerRead().get().intValue());

    assertTrue(operationLimits.maxNodesPerWrite().isPresent());
    assertEquals(200, operationLimits.maxNodesPerWrite().get().intValue());

    assertTrue(operationLimits.maxNodesPerMethodCall().isPresent());
    assertEquals(300, operationLimits.maxNodesPerMethodCall().get().intValue());

    assertTrue(operationLimits.maxNodesPerBrowse().isPresent());
    assertEquals(400, operationLimits.maxNodesPerBrowse().get().intValue());

    assertTrue(operationLimits.maxNodesPerRegisterNodes().isPresent());
    assertEquals(500, operationLimits.maxNodesPerRegisterNodes().get().intValue());

    assertTrue(operationLimits.maxNodesPerTranslateBrowsePathsToNodeIds().isPresent());
    assertEquals(600, operationLimits.maxNodesPerTranslateBrowsePathsToNodeIds().get().intValue());

    assertTrue(operationLimits.maxNodesPerNodeManagement().isPresent());
    assertEquals(700, operationLimits.maxNodesPerNodeManagement().get().intValue());

    assertTrue(operationLimits.maxMonitoredItemsPerCall().isPresent());
    assertEquals(800, operationLimits.maxMonitoredItemsPerCall().get().intValue());

    assertTrue(operationLimits.maxNodesPerHistoryReadData().isPresent());
    assertEquals(900, operationLimits.maxNodesPerHistoryReadData().get().intValue());

    assertTrue(operationLimits.maxNodesPerHistoryReadEvents().isPresent());
    assertEquals(1000, operationLimits.maxNodesPerHistoryReadEvents().get().intValue());

    assertTrue(operationLimits.maxNodesPerHistoryUpdateData().isPresent());
    assertEquals(1100, operationLimits.maxNodesPerHistoryUpdateData().get().intValue());

    assertTrue(operationLimits.maxNodesPerHistoryUpdateEvents().isPresent());
    assertEquals(1200, operationLimits.maxNodesPerHistoryUpdateEvents().get().intValue());
  }

  // A server may enforce a hidden Read limit of one, while every OperationLimits property remains
  // individually readable. Discovery must preserve the declared property mapping in this case.
  @Test
  void bulkTooManyOperationsReadsEveryLimitIndividuallyInPropertyOrder() throws UaException {
    OpcUaClient mockClient = mock(OpcUaClient.class);
    List<DataValue> values = operationLimitValues();

    when(mockClient.readValues(anyDouble(), any(), anyList()))
        .thenThrow(new UaException(StatusCodes.Bad_TooManyOperations));
    when(mockClient.readValue(anyDouble(), any(), any(NodeId.class)))
        .thenAnswer(
            invocation -> {
              NodeId nodeId = invocation.getArgument(2);
              return values.get(OPERATION_LIMIT_NODES.indexOf(nodeId));
            });

    OperationLimits operationLimits = OperationLimits.read(mockClient);

    assertEquals(
        List.of(
            uint(1), uint(2), uint(3), uint(4), uint(5), uint(6), uint(7), uint(8), uint(9),
            uint(10), uint(11), uint(12)),
        presentValues(operationLimits));

    ArgumentCaptor<NodeId> nodeIds = ArgumentCaptor.forClass(NodeId.class);
    verify(mockClient, times(OPERATION_LIMIT_NODES.size()))
        .readValue(eq(0.0), eq(TimestampsToReturn.Neither), nodeIds.capture());
    assertEquals(OPERATION_LIMIT_NODES, nodeIds.getAllValues());
  }

  // Only Bad_TooManyOperations says that reducing the number of operations can make the same
  // request safe to replay. Other service failures must remain visible to the caller.
  @Test
  void nonTooManyBulkServiceFailurePropagatesWithoutSingletonReads() throws UaException {
    OpcUaClient mockClient = mock(OpcUaClient.class);
    when(mockClient.readValues(anyDouble(), any(), anyList()))
        .thenThrow(new UaException(StatusCodes.Bad_Timeout));

    UaException exception = assertThrows(UaException.class, () -> OperationLimits.read(mockClient));

    assertEquals(StatusCodes.Bad_Timeout, exception.getStatusCode().value());
    verify(mockClient, never()).readValue(anyDouble(), any(), any(NodeId.class));
  }

  // A singleton service failure does not prove that an optional node is absent. Propagating it
  // leaves the lazy cache retryable instead of caching a partial set of limits.
  @Test
  void singletonServiceFailureAbortsDiscovery() throws UaException {
    OpcUaClient mockClient = mock(OpcUaClient.class);
    when(mockClient.readValues(anyDouble(), any(), anyList()))
        .thenThrow(new UaException(StatusCodes.Bad_TooManyOperations));
    when(mockClient.readValue(anyDouble(), any(), any(NodeId.class)))
        .thenReturn(operationLimitValues().get(0))
        .thenThrow(new UaException(StatusCodes.Bad_Timeout));

    UaException exception = assertThrows(UaException.class, () -> OperationLimits.read(mockClient));

    assertEquals(StatusCodes.Bad_Timeout, exception.getStatusCode().value());
    verify(mockClient, times(2)).readValue(anyDouble(), any(), any(NodeId.class));
  }

  // Optional OperationLimits nodes report absence through per-operation data, so bad and null
  // singleton values must not turn an otherwise valid discovery into a service failure.
  @Test
  void badAndNullSingletonValuesRemainAbsent() throws UaException {
    OpcUaClient mockClient = mock(OpcUaClient.class);
    List<DataValue> values = operationLimitValues();

    when(mockClient.readValues(anyDouble(), any(), anyList()))
        .thenThrow(new UaException(StatusCodes.Bad_TooManyOperations));
    when(mockClient.readValue(anyDouble(), any(), any(NodeId.class)))
        .thenAnswer(
            invocation -> {
              NodeId nodeId = invocation.getArgument(2);
              int index = OPERATION_LIMIT_NODES.indexOf(nodeId);
              return switch (index) {
                case 0 -> new DataValue(StatusCodes.Bad_NodeIdUnknown);
                case 1 -> null;
                default -> values.get(index);
              };
            });

    OperationLimits operationLimits = OperationLimits.read(mockClient);

    assertTrue(operationLimits.maxNodesPerRead().isEmpty());
    assertTrue(operationLimits.maxNodesPerWrite().isEmpty());
    assertEquals(uint(3), operationLimits.maxNodesPerMethodCall().orElseThrow());
    verify(mockClient, times(OPERATION_LIMIT_NODES.size()))
        .readValue(anyDouble(), any(), any(NodeId.class));
  }

  // A successful service response with the wrong number of per-operation results is malformed and
  // must fail deterministically before any property is indexed or a fallback is attempted.
  @Test
  void bulkResponseWithWrongResultCountThrowsBadUnexpectedError() throws UaException {
    OpcUaClient mockClient = mock(OpcUaClient.class);
    when(mockClient.readValues(anyDouble(), any(), anyList()))
        .thenReturn(operationLimitValues().subList(0, OPERATION_LIMIT_NODES.size() - 1));

    UaException exception = assertThrows(UaException.class, () -> OperationLimits.read(mockClient));

    assertEquals(StatusCodes.Bad_UnexpectedError, exception.getStatusCode().value());
    verify(mockClient, never()).readValue(anyDouble(), any(), any(NodeId.class));
  }

  private static List<DataValue> operationLimitValues() {
    return List.of(
        new DataValue(new Variant(uint(1))),
        new DataValue(new Variant(uint(2))),
        new DataValue(new Variant(uint(3))),
        new DataValue(new Variant(uint(4))),
        new DataValue(new Variant(uint(5))),
        new DataValue(new Variant(uint(6))),
        new DataValue(new Variant(uint(7))),
        new DataValue(new Variant(uint(8))),
        new DataValue(new Variant(uint(9))),
        new DataValue(new Variant(uint(10))),
        new DataValue(new Variant(uint(11))),
        new DataValue(new Variant(uint(12))));
  }

  private static List<UInteger> presentValues(OperationLimits limits) {
    return List.of(
        limits.maxNodesPerRead().orElseThrow(),
        limits.maxNodesPerWrite().orElseThrow(),
        limits.maxNodesPerMethodCall().orElseThrow(),
        limits.maxNodesPerBrowse().orElseThrow(),
        limits.maxNodesPerRegisterNodes().orElseThrow(),
        limits.maxNodesPerTranslateBrowsePathsToNodeIds().orElseThrow(),
        limits.maxNodesPerNodeManagement().orElseThrow(),
        limits.maxMonitoredItemsPerCall().orElseThrow(),
        limits.maxNodesPerHistoryReadData().orElseThrow(),
        limits.maxNodesPerHistoryReadEvents().orElseThrow(),
        limits.maxNodesPerHistoryUpdateData().orElseThrow(),
        limits.maxNodesPerHistoryUpdateEvents().orElseThrow());
  }
}
