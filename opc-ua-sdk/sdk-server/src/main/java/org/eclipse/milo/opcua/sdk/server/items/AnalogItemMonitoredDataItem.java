/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.items;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.util.DataChangeMonitoringFilter;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

/**
 * A {@link MonitoredDataItem} extension for AnalogItems that supports percent deadband filtering.
 */
public class AnalogItemMonitoredDataItem extends MonitoredDataItem {

  private final Range euRange;

  public AnalogItemMonitoredDataItem(
      OpcUaServer server,
      Session session,
      UInteger id,
      UInteger subscriptionId,
      ReadValueId readValueId,
      MonitoringMode monitoringMode,
      TimestampsToReturn timestamps,
      UInteger clientHandle,
      double samplingInterval,
      UInteger queueSize,
      boolean discardOldest,
      Range euRange) {

    super(
        server,
        session,
        id,
        subscriptionId,
        readValueId,
        monitoringMode,
        timestamps,
        clientHandle,
        samplingInterval,
        queueSize,
        discardOldest);

    this.euRange = euRange;
  }

  @Override
  public synchronized void setValue(DataValue value) {

    boolean valuePassesFilter =
        DataChangeMonitoringFilter.filter(getLastValue(), value, getFilter(), euRange);

    if (valuePassesFilter) {
      super.setLastValue(value);
      enqueue(value);

      if (getTriggeredItems() != null) {
        getTriggeredItems().values().forEach(item -> item.triggered = true);
      }
    }
  }

  @Override
  public void installFilter(MonitoringFilter filter) throws UaException {
    if (filter instanceof DataChangeFilter dataChangeFilter) {

      DeadbandType deadbandType = DeadbandType.from(dataChangeFilter.getDeadbandType().intValue());

      if (deadbandType == DeadbandType.Percent) {
        Double deadBandPercent = dataChangeFilter.getDeadbandValue();
        if (deadBandPercent < 0.0 || deadBandPercent > 100.0) {
          throw new UaException(StatusCodes.Bad_DeadbandFilterInvalid);
        }
        if (euRange == null) {
          throw new UaException(
              StatusCodes.Bad_MonitoredItemFilterUnsupported,
              "EURange property not found for AnalogItemType node");
        }
      }
      setFilter(dataChangeFilter);
    } else {
      throw new UaException(StatusCodes.Bad_MonitoredItemFilterUnsupported);
    }
  }
}
