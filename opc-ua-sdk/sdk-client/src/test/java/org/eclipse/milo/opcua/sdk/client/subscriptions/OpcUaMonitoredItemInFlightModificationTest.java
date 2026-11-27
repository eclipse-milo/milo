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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.PreparedCreateRequest;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.PreparedModifyRequest;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem.SyncState;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.junit.jupiter.api.Test;

/**
 * Verifies that {@link OpcUaMonitoredItem} retains parameter changes made after a
 * CreateMonitoredItems or ModifyMonitoredItems request is prepared and before its Good result is
 * applied.
 */
public class OpcUaMonitoredItemInFlightModificationTest {

  /**
   * Part 4 §5.12.2 / §5.12.3: parameter setters on an INITIAL item update only the item's fields.
   * If a setter runs after a CreateMonitoredItems request is prepared and before its Good result is
   * applied, the item must transition to UNSYNCHRONIZED with pending modifications so the next
   * ModifyMonitoredItems sends the updated values, while ServerState still reflects what Create
   * sent.
   */
  @Test
  void parameterChangeWhileCreateIsInFlightLeavesItemUnsynchronizedWithSentServerState() {
    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    item.setClientHandle(uint(1));
    item.setSamplingInterval(1000.0);
    item.setQueueSize(uint(1));
    item.setDiscardOldest(true);

    PreparedCreateRequest preparedCreate = item.tryBeginCreate().orElseThrow();

    // Mutate parameters while CreateMonitoredItems is in flight.
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);
    item.setSamplingInterval(500.0);
    item.setQueueSize(uint(5));
    item.setDiscardOldest(false);
    item.setFilter(filter);

    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(10), 1000.0, uint(1), null),
        preparedCreate);

    assertEquals(SyncState.UNSYNCHRONIZED, item.getSyncState());
    assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());
    assertEquals(1000.0, item.getRevisedSamplingInterval().orElseThrow());
    assertEquals(uint(1), item.getRevisedQueueSize().orElseThrow());
    assertTrue(
        item.getServerState().orElseThrow().getDiscardOldest(),
        "ServerState must record discardOldest sent in CreateMonitoredItems, not the unsent"
            + " change");

    PreparedModifyRequest preparedModify = item.tryBeginModify().orElseThrow();
    var modifyRequest = preparedModify.toRequest();
    assertEquals(uint(10), modifyRequest.getMonitoredItemId());
    assertEquals(500.0, modifyRequest.getRequestedParameters().getSamplingInterval());
    assertEquals(uint(5), modifyRequest.getRequestedParameters().getQueueSize());
    assertFalse(modifyRequest.getRequestedParameters().getDiscardOldest());
    assertNotNull(modifyRequest.getRequestedParameters().getFilter());

    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 500.0, uint(5), null), preparedModify);

    assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
    assertEquals(500.0, item.getRevisedSamplingInterval().orElseThrow());
    assertEquals(uint(5), item.getRevisedQueueSize().orElseThrow());
    assertFalse(item.getServerState().orElseThrow().getDiscardOldest());
    assertTrue(item.tryBeginModify().isEmpty());
  }

  /**
   * Clearing a previously set filter to {@code null} while a ModifyMonitoredItems call is in flight
   * must keep the item UNSYNCHRONIZED and send a {@code null} filter on the follow-up modify.
   */
  @Test
  void clearingFilterWhileModifyIsInFlightSendsNullFilterOnNextModify() {
    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(10), 1000.0, uint(1), null));

    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);
    item.setFilter(filter);

    PreparedModifyRequest firstModify = item.tryBeginModify().orElseThrow();
    assertNotNull(firstModify.toRequest().getRequestedParameters().getFilter());

    // Clear the filter while the first modify is in flight.
    item.setFilter(null);

    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 1000.0, uint(1), null), firstModify);

    assertEquals(SyncState.UNSYNCHRONIZED, item.getSyncState());

    PreparedModifyRequest secondModify = item.tryBeginModify().orElseThrow();
    assertNull(
        secondModify.toRequest().getRequestedParameters().getFilter(),
        "follow-up ModifyMonitoredItems must send the cleared (null) filter");

    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 1000.0, uint(1), null), secondModify);

    assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
    assertTrue(item.tryBeginModify().isEmpty());
  }

  /**
   * Control: when no parameter changes occur while Create or Modify is in flight, applying a Good
   * result leaves the item SYNCHRONIZED with no follow-up modification pending.
   */
  @Test
  void unchangedInFlightCreateAndModifyTransitionToSynchronized() {
    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    item.setClientHandle(uint(1));
    item.setSamplingInterval(250.0);

    PreparedCreateRequest preparedCreate = item.tryBeginCreate().orElseThrow();
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(1), 250.0, uint(1), null),
        preparedCreate);

    assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
    assertTrue(item.tryBeginModify().isEmpty());

    item.setQueueSize(uint(4));
    PreparedModifyRequest preparedModify = item.tryBeginModify().orElseThrow();
    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 250.0, uint(4), null), preparedModify);

    assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
    assertTrue(item.tryBeginModify().isEmpty());
  }

  /**
   * A late ModifyMonitoredItems response for an earlier MonitoredItemId must not overwrite the
   * ServerState of a replacement MonitoredItem created after a delete and re-add.
   */
  @Test
  void staleModifyResultForPreviousMonitoredItemIdIsIgnored() {
    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(1), 1000.0, uint(1), null));
    item.setSamplingInterval(250.0);

    PreparedModifyRequest staleModify = item.tryBeginModify().orElseThrow();

    // Item is deleted and recreated with MonitoredItemId 2 while the old modify is in flight.
    item.applyDeleteResult(StatusCode.GOOD);
    item.setClientHandle(uint(2));
    item.setSamplingInterval(500.0);
    PreparedCreateRequest recreated = item.tryBeginCreate().orElseThrow();
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(2), 500.0, uint(1), null), recreated);

    // Late response for MonitoredItemId 1 arrives.
    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 250.0, uint(1), null), staleModify);

    assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
    assertEquals(uint(2), item.getMonitoredItemId().orElseThrow());
    assertEquals(500.0, item.getRevisedSamplingInterval().orElseThrow());
  }

  /**
   * Overlapping ModifyMonitoredItems requests can execute on the Server in the reverse order of
   * local response application: A prepares queueSize=2, setter updates to 3, B prepares and runs on
   * the Server first, A runs on the Server second and applies locally first, then B applies locally
   * second. Neither overlapping response may mark the item SYNCHRONIZED until a final
   * non-overlapping modify reconciles the Server to 3.
   */
  @Test
  void overlappingModifiesAppliedOutOfOrderStayUnsynchronizedUntilReconciled() {
    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(10), 1000.0, uint(1), null));

    item.setQueueSize(uint(2));
    PreparedModifyRequest modifyA = item.tryBeginModify().orElseThrow();
    assertEquals(uint(2), modifyA.toRequest().getRequestedParameters().getQueueSize());

    item.setQueueSize(uint(3));
    PreparedModifyRequest modifyB = item.tryBeginModify().orElseThrow();
    assertEquals(uint(3), modifyB.toRequest().getRequestedParameters().getQueueSize());

    // A applies first while B is still in flight.
    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 1000.0, uint(2), null), modifyA);
    assertEquals(SyncState.UNSYNCHRONIZED, item.getSyncState());

    // B applies second; even though no setter ran after B was prepared, B overlapped with A so the
    // item must remain UNSYNCHRONIZED for a final reconciliation modify.
    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 1000.0, uint(3), null), modifyB);
    assertEquals(
        SyncState.UNSYNCHRONIZED,
        item.getSyncState(),
        "overlapping modifies must not mark the item SYNCHRONIZED without reconciliation");

    // Non-overlapping reconciliation modify sends 3 and transitions cleanly to SYNCHRONIZED.
    PreparedModifyRequest reconcile = item.tryBeginModify().orElseThrow();
    assertEquals(uint(3), reconcile.toRequest().getRequestedParameters().getQueueSize());
    item.applyModifyResult(
        new MonitoredItemModifyResult(StatusCode.GOOD, 1000.0, uint(3), null), reconcile);

    assertEquals(SyncState.SYNCHRONIZED, item.getSyncState());
    assertEquals(uint(3), item.getRevisedQueueSize().orElseThrow());
    assertTrue(item.tryBeginModify().isEmpty());
  }

  /**
   * Deletion must not overtake an in-flight ModifyMonitoredItems request: tryBeginDelete() returns
   * empty while activeModifies > 0 and succeeds once the in-flight modify finishes or releases.
   */
  @Test
  void tryBeginDeleteIsDeferredWhileModifyIsInFlight() {
    var item = OpcUaMonitoredItem.newDataItem(NodeIds.Server_ServerStatus_CurrentTime);
    item.setClientHandle(uint(1));
    item.applyCreateResult(
        new MonitoredItemCreateResult(StatusCode.GOOD, uint(10), 1000.0, uint(1), null));

    item.setQueueSize(uint(2));
    PreparedModifyRequest modify = item.tryBeginModify().orElseThrow();

    assertTrue(
        item.tryBeginDelete().isEmpty(),
        "deletion must be deferred while a ModifyMonitoredItems call is in flight");
    assertTrue(item.needsServerDeletion());

    item.releaseModify(modify);

    assertEquals(uint(10), item.tryBeginDelete().orElseThrow().monitoredItemId());
  }
}
