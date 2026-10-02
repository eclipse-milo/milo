/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.items;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitoredDataItemTest {

  private MonitoredDataItem item;

  @BeforeEach
  void setUp() throws Exception {
    item =
        new MonitoredDataItem(
            mock(OpcUaServer.class),
            mock(Session.class),
            uint(1),
            uint(1),
            new ReadValueId(new NodeId(2, "v"), AttributeId.Value.uid(), null, null),
            MonitoringMode.Reporting,
            TimestampsToReturn.Both,
            uint(1),
            100.0,
            uint(10),
            true);

    item.installFilter(MonitoredDataItem.DEFAULT_FILTER);
  }

  /**
   * Part 4 §5.13.2.1: an item the Session may not read reports the denial in the Publish response.
   * The denial is queued as soon as it is known, so it reaches the client even if nothing samples
   * the item.
   */
  @Test
  void transitionToDeniedQueuesTheDenialStatus() {
    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);

    List<DataValue> queued = drain();
    assertEquals(1, queued.size());
    assertEquals(StatusCodes.Bad_UserAccessDenied, queued.get(0).statusCode().getValue());
  }

  // A sampler that keeps pushing values while access is denied must not leak them, and the
  // unchanged denial must not be queued again.
  @Test
  void valuesSetWhileDeniedAreNotReported() {
    item.setReadAccessResult(AccessResult.DENIED_NOT_READABLE);
    drain();

    item.setValue(new DataValue(new Variant(1)));
    item.setValue(new DataValue(new Variant(2)));
    item.setReadAccessResult(AccessResult.DENIED_NOT_READABLE);

    assertTrue(drain().isEmpty(), "values set while read access is denied must not be queued");
  }

  // Part 4 §5.13.2.1: "If the access rights change to read rights, the Server shall start sending
  // data for the MonitoredItem."
  @Test
  void nextValueAfterAccessIsAllowedAgainIsReported() {
    item.setValue(new DataValue(new Variant(1)));
    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    drain();

    item.setReadAccessResult(AccessResult.ALLOWED);
    item.setValue(new DataValue(new Variant(2)));

    List<DataValue> queued = drain();
    assertEquals(1, queued.size());
    assertEquals(StatusCode.GOOD, queued.get(0).statusCode());
    assertEquals(2, queued.get(0).value().value());
  }

  // After a transfer with sendInitialValues, the last value is re-sent. Once access is allowed
  // again the denial must not be what gets re-sent.
  @Test
  void denialIsNotTheLastValueOnceAccessIsAllowedAgain() {
    item.setValue(new DataValue(new Variant(1)));
    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    drain();

    item.setReadAccessResult(AccessResult.ALLOWED);
    item.maybeSendLastValue();

    assertTrue(drain().isEmpty(), "the denial must not be re-sent as the last value");
  }

  // Part 4 §7.23: a Disabled item generates and queues no Notifications, so an access change
  // that arrives while Disabled must not leave a stale denial behind for when monitoring resumes.
  @Test
  void accessChangesWhileDisabledQueueNothing() {
    item.setMonitoringMode(MonitoringMode.Disabled);

    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    item.setReadAccessResult(AccessResult.ALLOWED);
    item.setMonitoringMode(MonitoringMode.Reporting);

    assertTrue(drain().isEmpty(), "nothing may be queued for an access change while Disabled");
  }

  // A denial that arrived while Disabled is reported by the first refresh after monitoring
  // resumes, even though the refresh carries the same result as before.
  @Test
  void denialThatArrivedWhileDisabledIsReportedByTheFirstRefreshAfterResume() {
    item.setMonitoringMode(MonitoringMode.Disabled);
    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    item.setMonitoringMode(MonitoringMode.Reporting);

    assertTrue(drain().isEmpty(), "resuming must not queue a result cached while Disabled");

    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);

    List<DataValue> queued = drain();
    assertEquals(1, queued.size());
    assertEquals(StatusCodes.Bad_UserAccessDenied, queued.get(0).statusCode().getValue());
  }

  // Access granted while Disabled must not surface the denial cached before it on resume.
  @Test
  void accessGrantedWhileDisabledQueuesNoStaleDenialOnResume() {
    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    drain();
    item.setMonitoringMode(MonitoringMode.Disabled);
    item.setMonitoringMode(MonitoringMode.Reporting);

    item.setReadAccessResult(AccessResult.ALLOWED);
    item.setValue(new DataValue(new Variant(7)));

    List<DataValue> queued = drain();
    assertEquals(1, queued.size(), "only the value may be queued, not the stale denial");
    assertEquals(StatusCode.GOOD, queued.get(0).statusCode());
    assertEquals(7, queued.get(0).value().value());
  }

  // A sampler that leaves denied items out of its own reads, or a component that refreshes
  // results on its own schedule, needs to see the result the item is enforcing.
  @Test
  void getReadAccessResultReflectsTheLastResultApplied() {
    assertEquals(
        AccessResult.ALLOWED, item.getReadAccessResult(), "allowed until a result is applied");

    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    assertEquals(AccessResult.DENIED_USER_ACCESS, item.getReadAccessResult());

    item.setReadAccessResult(AccessResult.ALLOWED);
    assertEquals(AccessResult.ALLOWED, item.getReadAccessResult());
  }

  // The result is tracked while Disabled even though nothing is queued, so a refresher reading
  // it back sees the denial it applied rather than the last reported state.
  @Test
  void getReadAccessResultTracksResultsAppliedWhileDisabled() {
    item.setMonitoringMode(MonitoringMode.Disabled);

    item.setReadAccessResult(AccessResult.DENIED_NOT_READABLE);

    assertEquals(AccessResult.DENIED_NOT_READABLE, item.getReadAccessResult());
    assertTrue(drain().isEmpty(), "nothing may be queued while Disabled");
  }

  // A Node the AddressSpace no longer knows is no decision about access. The item keeps the result
  // it had, so a denial stays enforced and an allowed item keeps reporting what its sampler
  // delivers, typically the AddressSpace's own Bad_NodeIdUnknown.
  @Test
  void unknownNodeResultLeavesTheLastResultInPlace() {
    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    drain();

    item.setReadAccessResult(AccessResult.NODE_UNKNOWN);
    item.setValue(new DataValue(new Variant(1)));

    assertEquals(AccessResult.DENIED_USER_ACCESS, item.getReadAccessResult());
    assertTrue(drain().isEmpty(), "the denial stays enforced and is not queued again");

    item.setReadAccessResult(AccessResult.ALLOWED);
    item.setReadAccessResult(AccessResult.NODE_UNKNOWN);
    item.setValue(new DataValue(new StatusCode(StatusCodes.Bad_NodeIdUnknown)));

    assertEquals(AccessResult.ALLOWED, item.getReadAccessResult());
    List<DataValue> queued = drain();
    assertEquals(1, queued.size(), "an allowed item reports what its sampler delivers");
    assertEquals(StatusCodes.Bad_NodeIdUnknown, queued.get(0).statusCode().getValue());
  }

  @Test
  void changedDenialReasonIsReported() {
    item.setReadAccessResult(AccessResult.DENIED_USER_ACCESS);
    drain();

    item.setReadAccessResult(AccessResult.DENIED_NOT_READABLE);

    List<DataValue> queued = drain();
    assertEquals(1, queued.size());
    assertEquals(StatusCodes.Bad_NotReadable, queued.get(0).statusCode().getValue());
  }

  private List<DataValue> drain() {
    var notifications = new ArrayList<UaStructuredType>();
    item.getNotifications(notifications, Integer.MAX_VALUE);

    return notifications.stream().map(n -> ((MonitoredItemNotification) n).getValue()).toList();
  }
}
