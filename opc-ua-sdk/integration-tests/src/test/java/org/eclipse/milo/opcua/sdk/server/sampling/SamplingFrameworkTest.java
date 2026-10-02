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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The sampling framework seen through a namespace that wires its own {@link SamplingManager}: a
 * cached read access policy, a floor on the sampling interval, and a sampler that fails.
 */
public class SamplingFrameworkTest extends AbstractClientServerTest {

  private static final int INITIAL_VALUE = 42;
  private static final long FLOOR_MILLIS = 200;

  private FrameworkNamespace namespace;
  private OpcUaSubscription subscription;

  @Override
  protected void configureTestNamespace(TestNamespace testNamespace) {
    namespace = new FrameworkNamespace(server);
    namespace.startup();
  }

  @AfterAll
  void shutdownNamespace() {
    if (namespace != null) {
      namespace.shutdown();
    }
  }

  @BeforeEach
  void createSubscription() throws UaException {
    subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    subscription.create();
  }

  @AfterEach
  void deleteSubscription() throws UaException {
    subscription.delete();

    namespace.failReads = false;
    namespace.readCounts.clear();
    namespace.variable.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    namespace.variable.setValue(new DataValue(new Variant(INITIAL_VALUE)));
    server.invalidateReadAccess(namespace.variable.getNodeId());
  }

  /**
   * On the cached policy a revoked right is not noticed until something posts an invalidation,
   * which is the cost the policy trades for a lookup per cycle; once posted, the denial is reported
   * on the next cycle, and the restored right the same way.
   */
  @Test
  void cachedPolicyReportsAStaleAllowedResultUntilInvalidated() throws Exception {
    var values = new LinkedBlockingQueue<DataValue>();
    monitor(namespace.variable.getNodeId(), 100.0, values);

    DataValue initial = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(initial);
    assertEquals(INITIAL_VALUE, initial.value().value());

    namespace.variable.setUserAccessLevel(AccessLevel.toValue(AccessLevel.WRITE_ONLY));

    assertNull(
        values.poll(1, TimeUnit.SECONDS),
        "the cached allowed result stays in force until an invalidation");

    server.invalidateReadAccess(namespace.variable.getNodeId());

    DataValue denied = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(denied, "the invalidation must bring the denial");
    assertEquals(StatusCodes.Bad_UserAccessDenied, denied.statusCode().getValue());

    namespace.variable.setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    namespace.variable.setValue(new DataValue(new Variant(INITIAL_VALUE + 1)));
    server.invalidateReadAccess(namespace.variable.getNodeId());

    DataValue restored = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(restored, "the invalidation must bring the restored data");
    assertEquals(StatusCode.GOOD, restored.statusCode());
    assertEquals(INITIAL_VALUE + 1, restored.value().value());
  }

  // A sampler that throws used to stop its group until the next rebuild. The group keeps cycling
  // and delivers again as soon as the sampler recovers.
  @Test
  void aSampleThatThrowsDoesNotStopTheGroup() throws Exception {
    var values = new LinkedBlockingQueue<DataValue>();
    monitor(namespace.variable.getNodeId(), 100.0, values);
    assertNotNull(values.poll(5, TimeUnit.SECONDS));

    namespace.failReads = true;
    namespace.variable.setValue(new DataValue(new Variant(INITIAL_VALUE + 1)));
    assertNull(values.poll(1, TimeUnit.SECONDS), "nothing is delivered while reads fail");

    namespace.failReads = false;

    DataValue recovered = values.poll(5, TimeUnit.SECONDS);
    assertNotNull(recovered, "the group must still be sampling once reads succeed again");
    assertEquals(INITIAL_VALUE + 1, recovered.value().value());
  }

  /**
   * A revised sampling interval of 0 asks for the fastest the server supports, which used to be a 1
   * ms poll. The manager floors it at the configured minimum. The revised interval the client is
   * told is unchanged; this is a sampling floor, not a protocol change.
   */
  @Test
  void aZeroRevisedIntervalSamplesAtTheFloor() throws Exception {
    var values = new LinkedBlockingQueue<DataValue>();
    OpcUaMonitoredItem item = monitor(namespace.variable.getNodeId(), 0.0, values);

    assertEquals(0.0, item.getRevisedSamplingInterval().orElseThrow());
    assertNotNull(values.poll(5, TimeUnit.SECONDS));

    AtomicInteger reads = namespace.readCounts.get(namespace.variable.getNodeId());
    int before = reads.get();
    Thread.sleep(1_000);
    int during = reads.get() - before;

    assertTrue(during >= 2, "the item is sampled: " + during + " reads in a second");
    assertTrue(
        during <= 2 * (1_000 / FLOOR_MILLIS),
        "the item is sampled no faster than the floor allows: " + during + " reads in a second");
  }

  private OpcUaMonitoredItem monitor(
      NodeId nodeId, double samplingInterval, Queue<DataValue> values) throws Exception {

    OpcUaMonitoredItem item = OpcUaMonitoredItem.newDataItem(nodeId);
    item.setSamplingInterval(samplingInterval);
    item.setDataValueListener((i, v) -> values.add(v));

    subscription.addMonitoredItem(item);
    subscription.synchronizeMonitoredItems();

    assertEquals(StatusCode.GOOD, item.getCreateResult().orElseThrow());

    return item;
  }

  /** A namespace on the cached policy with a 200 ms floor, whose reads can be made to fail. */
  private static final class FrameworkNamespace extends ManagedNamespaceWithLifecycle {

    private final SamplingManager samplingManager;
    private final Map<NodeId, AtomicInteger> readCounts = new ConcurrentHashMap<>();

    private volatile boolean failReads = false;
    private UaVariableNode variable;

    FrameworkNamespace(OpcUaServer server) {
      super(server, "urn:eclipse:milo:test:sampling-framework");

      samplingManager =
          new SamplingManager(
              server,
              (s, intervalMillis) -> new AddressSpaceSamplingGroup(s, this, intervalMillis),
              SamplingManagerConfig.defaults()
                  .withMinimumIntervalMillis(FLOOR_MILLIS)
                  .withReadAccessPolicy(ReadAccessPolicy.cached()));

      getLifecycleManager().addLifecycle(samplingManager);

      getLifecycleManager()
          .addStartupTask(
              () ->
                  variable =
                      UaVariableNode.build(
                          getNodeContext(),
                          b -> {
                            b.setNodeId(newNodeId("Variable"));
                            b.setBrowseName(new QualifiedName(getNamespaceIndex(), "Variable"));
                            b.setDisplayName(LocalizedText.english("Variable"));
                            b.setDataType(NodeIds.Int32);
                            b.setAccessLevel(AccessLevel.READ_WRITE);
                            b.setUserAccessLevel(AccessLevel.READ_WRITE);
                            // Report-by-exception is allowed, so a requested 0 is revised to 0.
                            b.setMinimumSamplingInterval(0.0);
                            b.setValue(new DataValue(new Variant(INITIAL_VALUE)));
                            return b.buildAndAdd();
                          }));
    }

    @Override
    public List<DataValue> read(
        ReadContext context,
        Double maxAge,
        TimestampsToReturn timestamps,
        List<ReadValueId> readValueIds) {

      if (failReads) {
        throw new IllegalStateException("reads are failing");
      }

      // The access check reads other attributes through here too; count only value samples.
      readValueIds.stream()
          .filter(id -> AttributeId.Value.uid().equals(id.getAttributeId()))
          .forEach(
              id ->
                  readCounts
                      .computeIfAbsent(id.getNodeId(), n -> new AtomicInteger())
                      .incrementAndGet());

      return super.read(context, maxAge, timestamps, readValueIds);
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {
      samplingManager.onDataItemsCreated(dataItems);
    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {
      samplingManager.onDataItemsModified(dataItems);
    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {
      samplingManager.onDataItemsDeleted(dataItems);
    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
      samplingManager.onMonitoringModeChanged(monitoredItems);
    }
  }
}
