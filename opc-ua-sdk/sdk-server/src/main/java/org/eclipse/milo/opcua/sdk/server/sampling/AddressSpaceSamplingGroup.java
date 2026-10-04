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

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SamplingGroup} that samples through {@link AddressSpace#read}.
 *
 * <p>Each cycle reads the items of each Session with one Read per Session, split into requests of
 * at most the server's {@code MaxNodesPerRead}, and delivers each value with the timestamps the
 * item asked for. A Read that throws is logged and the other Sessions are still read.
 */
public class AddressSpaceSamplingGroup extends SamplingGroup {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AddressSpace addressSpace;

  /**
   * Create a group that reads its items from {@code addressSpace}.
   *
   * @param server the server whose executors run the cycle.
   * @param addressSpace the AddressSpace to read from.
   * @param intervalMillis the sampling interval, in milliseconds.
   */
  public AddressSpaceSamplingGroup(
      OpcUaServer server, AddressSpace addressSpace, long intervalMillis) {

    super(server, intervalMillis);

    this.addressSpace = addressSpace;
  }

  @Override
  protected CompletionStage<@Nullable Void> sample(List<DataItem> items) {
    int maxNodesPerRead = getServer().getConfig().getLimits().getMaxNodesPerRead().intValue();
    if (maxNodesPerRead <= 0) {
      maxNodesPerRead = Integer.MAX_VALUE;
    }

    var bySession = new LinkedHashMap<Session, List<DataItem>>();
    for (DataItem item : items) {
      bySession.computeIfAbsent(item.getSession(), s -> new ArrayList<>()).add(item);
    }

    int requestCount = 0;

    for (Map.Entry<Session, List<DataItem>> entry : bySession.entrySet()) {
      Session session = entry.getKey();

      for (List<DataItem> request : Lists.partition(entry.getValue(), maxNodesPerRead)) {
        requestCount++;

        try {
          read(session, request);
        } catch (Throwable t) {
          logger.warn(
              "Read failed for the {} ms group, Session {}: {}",
              getIntervalMillis(),
              session.getSessionId(),
              t.getMessage(),
              t);
        }
      }
    }

    setRequestCount(requestCount);

    return CompletableFuture.completedFuture(null);
  }

  private void read(Session session, List<DataItem> items) {
    List<ReadValueId> readValueIds = items.stream().map(MonitoredItem::getReadValueId).toList();

    List<DataValue> values =
        addressSpace.read(
            new ReadContext(getServer(), session), 0d, TimestampsToReturn.Both, readValueIds);

    Iterator<DataItem> ii = items.iterator();
    Iterator<DataValue> vi = values.iterator();

    while (ii.hasNext() && vi.hasNext()) {
      deliver(ii.next(), vi.next());
    }
  }
}
