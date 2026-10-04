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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RevisedDataItemParameters;
import org.eclipse.milo.opcua.sdk.server.access.AccessController;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.sampling.ManualScheduler;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupInfo;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingManagerConfig;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingTestItems;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * A {@link ManagedAddressSpace} with a lifecycle samples its data items without any sampling code
 * of its own: the base class forwards the callbacks to a SamplingManager that starts after the
 * registration and the subclass's lifecycles and stops before them.
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

  /**
   * Part 4 §7.21: the revised sampling interval is the one the server assigns, equal to or higher
   * than the requested one. The default revision reports the interval the sampling manager will
   * use, on create and on modify, so the client is never told a faster rate than it gets.
   */
  @Test
  void theDefaultRevisionReportsTheIntervalTheManagerSamplesAt() {
    ManagedAddressSpace addressSpace =
        new ManagedAddressSpaceWithLifecycle(server) {
          @Override
          protected SamplingManagerConfig samplingManagerConfig() {
            return SamplingManagerConfig.defaults().withMinimumIntervalMillis(100);
          }
        };
    var readValueId = new ReadValueId(new NodeId(2, "v"), AttributeId.Value.uid(), null, null);

    RevisedDataItemParameters created = addressSpace.onCreateDataItem(readValueId, 120.0, uint(5));
    assertEquals(125.0, created.revisedSamplingInterval(), "up to the next supported interval");
    assertEquals(uint(5), created.revisedQueueSize(), "the queue size is as requested");

    RevisedDataItemParameters modified = addressSpace.onModifyDataItem(readValueId, 0.0, uint(5));
    assertEquals(100.0, modified.revisedSamplingInterval(), "a zero request becomes the floor");
  }

  // A subclass that samples its own items keeps the intervals it had, and tells the client so, by
  // overriding the one revision hook rather than both service callbacks.
  @Test
  void aSubclassWithItsOwnSamplerRevisesBothCallbacksThroughOneHook() {
    ManagedAddressSpace addressSpace =
        new ManagedAddressSpaceWithLifecycle(server) {
          @Override
          protected double reviseSamplingInterval(double requestedSamplingInterval) {
            return requestedSamplingInterval;
          }
        };
    var readValueId = new ReadValueId(new NodeId(2, "v"), AttributeId.Value.uid(), null, null);

    assertEquals(
        10.0, addressSpace.onCreateDataItem(readValueId, 10.0, uint(5)).revisedSamplingInterval());
    assertEquals(
        75.0, addressSpace.onModifyDataItem(readValueId, 75.0, uint(5)).revisedSamplingInterval());
  }

  /**
   * A custom SamplingGroup can use a resource, such as a device connection, that a lifecycle the
   * subclass adds opens and closes. Sampling starts after that lifecycle and stops before it, so no
   * cycle runs while the resource is not open.
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("addressSpaces")
  void samplingRunsOnlyWhileTheSubclassLifecyclesAreStarted(
      String name, Function<OpcUaServer, ManagedAddressSpace> factory) {

    ManagedAddressSpace addressSpace = factory.apply(server);
    var samplingRunningSeen = new ArrayList<Boolean>();
    lifecycleManagerOf(addressSpace)
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                samplingRunningSeen.add(addressSpace.getSamplingManager().isRunning());
              }

              @Override
              public void shutdown() {
                samplingRunningSeen.add(addressSpace.getSamplingManager().isRunning());
              }
            });

    ((Lifecycle) addressSpace).startup();
    assertTrue(addressSpace.getSamplingManager().isRunning());
    ((Lifecycle) addressSpace).shutdown();

    assertEquals(
        List.of(false, false),
        samplingRunningSeen,
        "sampling was not running when the subclass lifecycle started or when it stopped");
  }

  // A startup that fails is not followed by a shutdown, so a failure to start sampling, here from
  // a subclass's configuration hook, must undo the registration that already happened.
  @Test
  void aSamplingManagerThatFailsToStartLeavesNothingRegistered() {
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);

    var addressSpace =
        new ManagedNamespaceWithLifecycle(server, "urn:test:namespace") {
          @Override
          protected SamplingManagerConfig samplingManagerConfig() {
            throw new IllegalStateException("invalid driver configuration");
          }
        };

    assertThrows(IllegalStateException.class, addressSpace::startup);

    verify(addressSpaceManager).register(addressSpace.getNodeManager());
    verify(addressSpaceManager).unregister(addressSpace.getNodeManager());
    verify(addressSpaceManager).unregister(addressSpace);
  }

  private static LifecycleManager lifecycleManagerOf(ManagedAddressSpace addressSpace) {
    if (addressSpace instanceof ManagedAddressSpaceWithLifecycle a) {
      return a.getLifecycleManager();
    } else if (addressSpace instanceof ManagedAddressSpaceFragmentWithLifecycle f) {
      return f.getLifecycleManager();
    } else {
      return ((ManagedNamespaceWithLifecycle) addressSpace).getLifecycleManager();
    }
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
