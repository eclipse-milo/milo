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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
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
}
