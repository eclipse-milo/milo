/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.server.sampling;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroup;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SamplingGroup} that reads the registers behind its items with one {@link
 * SimulatedDevice} request per cycle, however many items share a register.
 *
 * <p>The register list is rebuilt in {@link #onItemsChanged} when the group's membership changes,
 * not on every cycle. Items that are not a register's Value, such as another attribute of a
 * register Node or a Node the device knows nothing about, are read through the AddressSpace
 * instead, so the device path never has to answer for them.
 */
@NullMarked
public final class DeviceSamplingGroup extends SamplingGroup {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private volatile List<String> registers = List.of();

  private final SimulatedDevice device;
  private final AddressSpace addressSpace;
  private final Function<NodeId, @Nullable String> registerOf;

  /**
   * Create a group that reads register Values from {@code device} and everything else through
   * {@code addressSpace}.
   *
   * @param server the server whose executors run the cycle.
   * @param intervalMillis the sampling interval, in milliseconds.
   * @param device the device to read registers from.
   * @param addressSpace the AddressSpace that reads everything else.
   * @param registerOf the register a Node stands for, or {@code null} for a Node that is not one.
   */
  public DeviceSamplingGroup(
      OpcUaServer server,
      long intervalMillis,
      SimulatedDevice device,
      AddressSpace addressSpace,
      Function<NodeId, @Nullable String> registerOf) {

    super(server, intervalMillis);

    this.device = device;
    this.addressSpace = addressSpace;
    this.registerOf = registerOf;
  }

  @Override
  protected void onItemsChanged(List<DataItem> items) {
    registers = items.stream().map(this::registerOf).filter(Objects::nonNull).distinct().toList();

    setRequestCount(registers.isEmpty() ? 0 : 1);
  }

  @Override
  protected @Nullable CompletionStage<@Nullable Void> sample(List<DataItem> items) {
    var deviceItems = new ArrayList<DataItem>();
    var otherItems = new ArrayList<DataItem>();

    for (DataItem item : items) {
      (registerOf(item) != null ? deviceItems : otherItems).add(item);
    }

    readThroughAddressSpace(otherItems);

    if (deviceItems.isEmpty()) {
      return null;
    }

    // One request for the whole group. The results are delivered only to this turn's items, which
    // may be fewer than the group's members when some Sessions may not read their registers.
    return device
        .read(registers)
        .handle(
            (values, failure) -> {
              for (DataItem item : deviceItems) {
                DataValue value = failure == null ? values.get(registerOf(item)) : null;

                deliver(
                    item,
                    value != null ? value : new DataValue(StatusCodes.Bad_CommunicationError));
              }

              if (failure != null) {
                // Delivered bad quality above; let the group log the failure too.
                throw new CompletionException(failure);
              }
              return null;
            });
  }

  private @Nullable String registerOf(DataItem item) {
    ReadValueId readValueId = item.getReadValueId();

    if (!AttributeId.Value.isEqual(readValueId.getAttributeId())) {
      return null;
    }

    return registerOf.apply(readValueId.getNodeId());
  }

  /** Items the device cannot answer for are read the way the default group reads everything. */
  private void readThroughAddressSpace(List<DataItem> items) {
    var bySession = new LinkedHashMap<Session, List<DataItem>>();
    for (DataItem item : items) {
      bySession.computeIfAbsent(item.getSession(), s -> new ArrayList<>()).add(item);
    }

    for (Map.Entry<Session, List<DataItem>> entry : bySession.entrySet()) {
      List<DataItem> sessionItems = entry.getValue();
      List<ReadValueId> readValueIds = sessionItems.stream().map(DataItem::getReadValueId).toList();

      try {
        List<DataValue> values =
            addressSpace.read(
                new ReadContext(getServer(), entry.getKey()),
                0d,
                TimestampsToReturn.Both,
                readValueIds);

        for (int i = 0; i < sessionItems.size() && i < values.size(); i++) {
          deliver(sessionItems.get(i), values.get(i));
        }
      } catch (Exception e) {
        logger.warn("Read failed for the {} ms group", getIntervalMillis(), e);
      }
    }
  }
}
