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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Test;

public class OperationLimitsTest extends AbstractClientServerTest {

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
}
