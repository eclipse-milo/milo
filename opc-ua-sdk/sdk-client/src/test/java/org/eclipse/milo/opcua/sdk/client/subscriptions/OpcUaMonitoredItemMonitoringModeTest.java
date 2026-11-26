/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.PreparedSetMonitoringModeRequest;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.ServerState;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link OpcUaMonitoredItem#applySetMonitoringModeResult(StatusCode, MonitoringMode)}
 * keeps {@link OpcUaMonitoredItem#getServerState()} in step with {@link
 * OpcUaMonitoredItem#getMonitoringMode()}.
 */
public class OpcUaMonitoredItemMonitoringModeTest {

  /**
   * Part 4 §5.12.4: a Good SetMonitoringMode result changes the MonitoringMode on the Server
   * without touching any other parameter, so ServerState must report the new MonitoringMode while
   * preserving its remaining fields.
   */
  @Test
  void goodSetMonitoringModeResultUpdatesServerStateAndPreservesOtherFields() {
    var item =
        OpcUaMonitoredItem.newDataItem(
            NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
    item.setClientHandle(uint(1));
    item.setDiscardOldest(false);

    ExtensionObject filterResult =
        ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), NodeIds.EventFilterResult);
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(42), 250.0, uint(8), filterResult));

    item.applySetMonitoringModeResult(StatusCode.GOOD, MonitoringMode.Sampling);

    assertEquals(StatusCode.GOOD, item.getSetMonitoringModeResult().orElseThrow());
    assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());

    ServerState state = item.getServerState().orElseThrow();
    assertEquals(MonitoringMode.Sampling, state.getMonitoringMode());
    assertEquals(uint(42), state.getMonitoredItemId());
    assertEquals(250.0, state.getSamplingInterval());
    assertEquals(uint(8), state.getQueueSize());
    assertEquals(filterResult, state.getFilterResult());
    assertEquals(false, state.getDiscardOldest());
  }

  /**
   * Control: a Bad SetMonitoringMode operation result must record the status code without altering
   * either getMonitoringMode() or ServerState.getMonitoringMode().
   */
  @Test
  void badSetMonitoringModeResultLeavesMonitoringModeAndServerStateUnchanged() {
    var item =
        OpcUaMonitoredItem.newDataItem(
            NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(42), 1000.0, uint(1), null));

    StatusCode refused = new StatusCode(StatusCodes.Bad_UserAccessDenied);
    item.applySetMonitoringModeResult(refused, MonitoringMode.Disabled);

    assertEquals(refused, item.getSetMonitoringModeResult().orElseThrow());
    assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());
    assertEquals(MonitoringMode.Reporting, item.getServerState().orElseThrow().getMonitoringMode());
  }

  /**
   * A late SetMonitoringMode Good result or service fault captured before an item was deleted and
   * recreated must be ignored even if the Server reuses the same numeric MonitoredItemId.
   */
  @Test
  void staleSetMonitoringModeResponseAndServiceFaultDoNotOverwriteRecreatedItem() {
    var item =
        OpcUaMonitoredItem.newDataItem(
            NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(42), 1000.0, uint(1), null));

    PreparedSetMonitoringModeRequest stalePrepared = item.tryBeginSetMonitoringMode().orElseThrow();

    // Delete and recreate the same Java item object with the same numeric MonitoredItemId.
    item.applyDeleteResult(StatusCode.GOOD);
    item.setClientHandle(uint(2));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(42), 1000.0, uint(1), null));

    // Late Good response for the deleted item must not overwrite the replacement.
    item.applySetMonitoringModeResult(StatusCode.GOOD, MonitoringMode.Sampling, stalePrepared);

    assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());
    assertEquals(MonitoringMode.Reporting, item.getServerState().orElseThrow().getMonitoringMode());
    assertTrue(item.getSetMonitoringModeResult().isEmpty());

    // Late service fault for the deleted item must not overwrite setMonitoringModeResult either.
    item.applySetMonitoringModeResult(
        new StatusCode(StatusCodes.Bad_UnexpectedError), stalePrepared);

    assertEquals(MonitoringMode.Reporting, item.getMonitoringMode());
    assertEquals(MonitoringMode.Reporting, item.getServerState().orElseThrow().getMonitoringMode());
    assertTrue(item.getSetMonitoringModeResult().isEmpty());
  }

  /**
   * While a SetMonitoringMode request is in flight, a second concurrent attempt for the same item
   * must not claim it until the first reservation is released or applied.
   */
  @Test
  void tryBeginSetMonitoringModeClaimsOnceUntilReleased() {
    var item =
        OpcUaMonitoredItem.newDataItem(
            NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(42), 1000.0, uint(1), null));

    PreparedSetMonitoringModeRequest first = item.tryBeginSetMonitoringMode().orElseThrow();
    assertTrue(
        item.tryBeginSetMonitoringMode().isEmpty(),
        "an overlapping SetMonitoringMode call must not claim the same item");

    item.releaseSetMonitoringMode(first);

    PreparedSetMonitoringModeRequest second = item.tryBeginSetMonitoringMode().orElseThrow();
    item.applySetMonitoringModeResult(StatusCode.GOOD, MonitoringMode.Sampling, second);

    assertEquals(MonitoringMode.Sampling, item.getMonitoringMode());
    assertEquals(MonitoringMode.Sampling, item.getServerState().orElseThrow().getMonitoringMode());
  }

  /**
   * Even on the same item incarnation, a delayed Good response or fault from an earlier
   * SetMonitoringMode attempt whose reservation was released must not overwrite a newer completed
   * mode change.
   */
  @Test
  void delayedFirstGoodSetMonitoringModeResponseDoesNotOverwriteNewerMode() {
    var item =
        OpcUaMonitoredItem.newDataItem(
            NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(42), 1000.0, uint(1), null));

    // Attempt 1 (Sampling) starts and its reservation is later released (e.g. after a fault or
    // timeout), allowing Attempt 2 (Disabled) to start and complete cleanly.
    PreparedSetMonitoringModeRequest first = item.tryBeginSetMonitoringMode().orElseThrow();
    item.releaseSetMonitoringMode(first);

    PreparedSetMonitoringModeRequest second = item.tryBeginSetMonitoringMode().orElseThrow();
    item.applySetMonitoringModeResult(StatusCode.GOOD, MonitoringMode.Disabled, second);

    assertEquals(MonitoringMode.Disabled, item.getMonitoringMode());
    assertEquals(MonitoringMode.Disabled, item.getServerState().orElseThrow().getMonitoringMode());
    assertEquals(StatusCode.GOOD, item.getSetMonitoringModeResult().orElseThrow());

    // Delayed Good response and delayed fault from Attempt 1 must both be ignored.
    item.applySetMonitoringModeResult(StatusCode.GOOD, MonitoringMode.Sampling, first);
    item.applySetMonitoringModeResult(new StatusCode(StatusCodes.Bad_UnexpectedError), first);

    assertEquals(MonitoringMode.Disabled, item.getMonitoringMode());
    assertEquals(MonitoringMode.Disabled, item.getServerState().orElseThrow().getMonitoringMode());
    assertEquals(StatusCode.GOOD, item.getSetMonitoringModeResult().orElseThrow());
  }

  /**
   * Deletion must not overtake an in-flight SetMonitoringMode request: tryBeginDelete() returns
   * empty while setMonitoringModeInFlight is true and succeeds once the request finishes or
   * releases.
   */
  @Test
  void tryBeginDeleteIsDeferredWhileSetMonitoringModeIsInFlight() {
    var item =
        OpcUaMonitoredItem.newDataItem(
            NodeIds.Server_ServerStatus_CurrentTime, MonitoringMode.Reporting);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(42), 1000.0, uint(1), null));

    PreparedSetMonitoringModeRequest inFlight = item.tryBeginSetMonitoringMode().orElseThrow();

    assertTrue(
        item.tryBeginDelete().isEmpty(),
        "deletion must be deferred while SetMonitoringMode is in flight");
    assertTrue(item.needsServerDeletion());

    item.releaseSetMonitoringMode(inFlight);

    assertEquals(uint(42), item.tryBeginDelete().orElseThrow().monitoredItemId());
  }
}
