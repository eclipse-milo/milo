/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.sampling.ManualScheduler;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupInfo;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingTestItems;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * A {@link ManagedAddressSpace} with a lifecycle samples its data items without any sampling code
 * of its own: the base class forwards the callbacks to a SamplingManager that starts with the
 * registration and stops before it is undone.
 */
class ManagedAddressSpaceSamplingTest {

  private final ManualScheduler scheduler = new ManualScheduler();
  private final OpcUaServer server = mock(OpcUaServer.class);
  private final Session session = mock(Session.class);

  @BeforeEach
  void setUp() {
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getLimits()).thenReturn(new OpcUaServerConfigLimits() {});
    when(server.getConfig()).thenReturn(config);
    when(server.getExecutorService()).thenReturn(scheduler.executor());
    when(server.getScheduledExecutorService()).thenReturn(scheduler.executor());
    when(server.getAddressSpaceManager()).thenReturn(mock(AddressSpaceManager.class));
    when(server.getNamespaceTable()).thenReturn(new NamespaceTable());

    AccessController accessController = mock(AccessController.class);
    when(accessController.checkReadAccess(eq(session), anyList())).thenReturn(Map.of());
    when(server.getAccessController()).thenReturn(accessController);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("addressSpaces")
  void theDefaultCallbacksSampleWhileTheAddressSpaceIsRegistered(
      String name, Function<OpcUaServer, ManagedAddressSpace> factory) {

    ManagedAddressSpace addressSpace = factory.apply(server);
    Lifecycle lifecycle = (Lifecycle) addressSpace;
    MonitoredDataItem item = SamplingTestItems.item(server, session, "unknown");

    lifecycle.startup();
    assertTrue(addressSpace.getSamplingManager().isRunning());

    addressSpace.onDataItemsCreated(List.of(item));
    assertEquals(
        List.of(new SamplingGroupInfo(100, 1, 0)), addressSpace.getSamplingManager().getGroups());

    scheduler.run(delay -> delay <= 100); // the initial sample reads through the AddressSpace

    List<DataValue> delivered = SamplingTestItems.drain(item);
    assertEquals(1, delivered.size(), "the item is sampled with no sampling code in the subclass");
    assertEquals(StatusCodes.Bad_NodeIdUnknown, delivered.get(0).statusCode().getValue());

    lifecycle.shutdown();
    assertFalse(addressSpace.getSamplingManager().isRunning());
    assertTrue(addressSpace.getSamplingManager().getDataItems().isEmpty());
  }

  private static Stream<Arguments> addressSpaces() {
    return Stream.of(
        Arguments.of(
            "ManagedAddressSpaceWithLifecycle",
            (Function<OpcUaServer, ManagedAddressSpace>)
                server -> new ManagedAddressSpaceWithLifecycle(server) {}),
        Arguments.of(
            "ManagedAddressSpaceFragmentWithLifecycle",
            (Function<OpcUaServer, ManagedAddressSpace>)
                server ->
                    new ManagedAddressSpaceFragmentWithLifecycle(server) {
                      @Override
                      public AddressSpaceFilter getFilter() {
                        return SimpleAddressSpaceFilter.create(getNodeManager()::containsNode);
                      }
                    }),
        Arguments.of(
            "ManagedNamespaceWithLifecycle",
            (Function<OpcUaServer, ManagedAddressSpace>)
                server -> new ManagedNamespaceWithLifecycle(server, "urn:test:namespace") {}));
  }
}
