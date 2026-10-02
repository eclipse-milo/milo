/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

/**
 * Real {@link MonitoredDataItem}s for the sampling tests, so the read access gate is the real one.
 */
final class SamplingTestItems {

  private static final AtomicLong IDS = new AtomicLong(1);

  private SamplingTestItems() {}

  static MonitoredDataItem item(OpcUaServer server, Session session, String name) {
    return item(server, session, name, 100.0);
  }

  static MonitoredDataItem item(
      OpcUaServer server, Session session, String name, double samplingInterval) {

    long id = IDS.getAndIncrement();

    var item =
        new MonitoredDataItem(
            server,
            session,
            uint(id),
            uint(1),
            new ReadValueId(new NodeId(2, name), AttributeId.Value.uid(), null, null),
            MonitoringMode.Reporting,
            TimestampsToReturn.Both,
            uint(id),
            samplingInterval,
            uint(10),
            true);

    try {
      item.installFilter(MonitoredDataItem.DEFAULT_FILTER);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }

    return item;
  }

  static List<DataValue> drain(MonitoredDataItem item) {
    var notifications = new ArrayList<UaStructuredType>();
    item.getNotifications(notifications, Integer.MAX_VALUE);

    return notifications.stream().map(n -> ((MonitoredItemNotification) n).getValue()).toList();
  }
}
