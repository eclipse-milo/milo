/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.sampling.ManualScheduler;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingTestItems;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** The deprecated {@link SubscriptionModel} as an adapter over the sampling framework. */
@SuppressWarnings("deprecation")
class SubscriptionModelTest {

  private final ManualScheduler scheduler = new ManualScheduler();
  private final OpcUaServer server = mock(OpcUaServer.class);
  private final AccessController accessController = mock(AccessController.class);
  private final AddressSpace addressSpace = mock(AddressSpace.class);
  private final Session session = mock(Session.class);

  private SubscriptionModel model;

  @BeforeEach
  void setUp() {
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});
    when(server.getConfig()).thenReturn(config);
    when(server.getExecutorService()).thenReturn(scheduler.executor());
    when(server.getScheduledExecutorService()).thenReturn(scheduler.executor());
    when(server.getAccessController()).thenReturn(accessController);

    model = new SubscriptionModel(server, addressSpace);
  }

  // What every namespace on SubscriptionModel got before: a cycle that refreshes read access and
  // reads the allowed items from the AddressSpace, now done by the framework underneath.
  @Test
  void forwardsItemsToAFrameworkThatRefreshesAndReadsThem() {
    MonitoredDataItem allowed = SamplingTestItems.item(server, session, "allowed");
    MonitoredDataItem denied = SamplingTestItems.item(server, session, "denied");
    when(accessController.checkReadAccess(eq(session), anyList()))
        .thenReturn(
            Map.of(
                allowed.getReadValueId(),
                AccessResult.ALLOWED,
                denied.getReadValueId(),
                AccessResult.DENIED_USER_ACCESS));
    when(addressSpace.read(any(), eq(0d), eq(TimestampsToReturn.Both), anyList()))
        .thenReturn(List.of(new DataValue(new Variant(1))));

    model.startup();
    model.onDataItemsCreated(List.of(allowed, denied));
    scheduler.run(delay -> delay <= 100); // the initial sample

    verify(addressSpace)
        .read(any(), eq(0d), eq(TimestampsToReturn.Both), eq(List.of(allowed.getReadValueId())));
    assertEquals(1, SamplingTestItems.drain(allowed).get(0).value().value());
    assertEquals(AccessResult.DENIED_USER_ACCESS, denied.getReadAccessResult());
    assertEquals(List.of(allowed, denied), model.getDataItems());

    model.shutdown();
    assertFalse(model.getSamplingManager().isRunning());
    assertTrue(model.getDataItems().isEmpty());
  }
}
