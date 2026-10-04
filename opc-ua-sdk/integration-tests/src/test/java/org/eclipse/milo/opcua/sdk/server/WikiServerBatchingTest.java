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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroup;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupFactory;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/** Verifies SDK batching and failure delivery with a deterministic local device adapter. */
public class WikiServerBatchingTest extends AbstractClientServerTest {
  private BatchNamespace namespace;
  private final AtomicBoolean unavailable = new AtomicBoolean();
  private final AtomicReference<NodeId> omittedNode = new AtomicReference<>();
  private final List<List<NodeId>> requests = new CopyOnWriteArrayList<>();

  @Override
  protected void configureTestNamespace(TestNamespace ignored) {
    namespace =
        new BatchNamespace(
            server,
            ids -> {
              requests.add(List.copyOf(ids));
              if (unavailable.get()) throw new IllegalStateException("fixture device offline");
              NodeId omitted = omittedNode.get();
              return ids.stream()
                  .filter(id -> !id.equals(omitted))
                  .collect(
                      Collectors.toMap(
                          Function.identity(),
                          id -> new DataValue(new Variant(omitted == null ? 42.0 : 43.0))));
            });
    namespace.startup();
  }

  @AfterAll
  void stopNamespace() {
    if (namespace != null) namespace.shutdown();
  }

  @Test
  void oneDeviceBatchFeedsThreeItemsAndReportsMissingOrFailedReads() throws Exception {
    var subscription = new OpcUaSubscription(client);
    subscription.setPublishingInterval(100.0);
    subscription.create();
    try {
      var values = new HashMap<NodeId, LinkedBlockingQueue<DataValue>>();
      for (NodeId id : namespace.ids) {
        var queue = new LinkedBlockingQueue<DataValue>();
        values.put(id, queue);
        var item = OpcUaMonitoredItem.newDataItem(id);
        item.setSamplingInterval(200.0);
        item.setDataValueListener((ignored, value) -> queue.add(value));
        subscription.addMonitoredItem(item);
      }
      subscription.synchronizeMonitoredItems();
      for (var item : subscription.getMonitoredItems()) {
        assertTrue(item.getCreateResult().orElseThrow().isGood());
      }
      for (NodeId id : namespace.ids) {
        DataValue value = values.get(id).poll(5, TimeUnit.SECONDS);
        assertNotNull(value, "initial value for " + id);
        assertTrue(value.statusCode().isGood());
        assertEquals(42.0, value.value().value());
      }
      var groups = namespace.getSamplingManager().getGroups();
      assertEquals(1, groups.size());
      assertEquals(3, groups.get(0).itemCount());
      assertEquals(1, groups.get(0).requestCount());
      assertTrue(requests.stream().anyMatch(ids -> ids.size() == 3));
      // Each adapter invocation receives the complete current sample, without duplicate nodes.
      assertTrue(requests.stream().allMatch(ids -> ids.size() == ids.stream().distinct().count()));
      // A partial response must fail only the missing item and still deliver the other values.
      NodeId omitted = namespace.ids.get(0);
      omittedNode.set(omitted);
      for (NodeId id : namespace.ids) {
        DataValue value = values.get(id).poll(5, TimeUnit.SECONDS);
        assertNotNull(value, "partial batch value for " + id);
        if (id.equals(omitted)) {
          assertEquals(StatusCodes.Bad_NoData, value.statusCode().getValue());
          assertTrue(value.value().isNull());
        } else {
          assertTrue(value.statusCode().isGood(), "unaffected item remains Good: " + id);
          assertEquals(43.0, value.value().value());
        }
      }
      unavailable.set(true);
      for (NodeId id : namespace.ids) {
        DataValue failure = values.get(id).poll(5, TimeUnit.SECONDS);
        assertNotNull(failure, "failed batch value for " + id);
        assertEquals(StatusCodes.Bad_CommunicationError, failure.statusCode().getValue());
        assertTrue(failure.value().isNull());
      }
    } finally {
      subscription.delete();
    }
    assertTrue(namespace.getSamplingManager().getGroups().isEmpty());
  }

  // wiki:batch-group:start
  static final class BatchGroup extends SamplingGroup {
    private final Function<List<NodeId>, Map<NodeId, DataValue>> readBatch;

    BatchGroup(
        OpcUaServer server,
        long intervalMillis,
        Function<List<NodeId>, Map<NodeId, DataValue>> readBatch) {
      super(server, intervalMillis);
      this.readBatch = readBatch;
      setRequestCount(1);
    }

    @Override
    protected @Nullable CompletionStage<@Nullable Void> sample(List<DataItem> items) {
      List<NodeId> ids =
          items.stream().map(item -> item.getReadValueId().getNodeId()).distinct().toList();
      Map<NodeId, DataValue> values;
      try {
        values = readBatch.apply(ids);
      } catch (RuntimeException failure) {
        for (DataItem item : items) {
          deliver(item, new DataValue(StatusCodes.Bad_CommunicationError));
        }
        return null;
      }
      for (DataItem item : items) {
        deliver(
            item,
            values.getOrDefault(
                item.getReadValueId().getNodeId(), new DataValue(StatusCodes.Bad_NoData)));
      }
      return null;
    }
  }

  // wiki:batch-group:end

  private static final class BatchNamespace extends ManagedNamespaceWithLifecycle {
    private final Function<List<NodeId>, Map<NodeId, DataValue>> readBatch;
    private final List<NodeId> ids = new java.util.ArrayList<>();

    BatchNamespace(OpcUaServer server, Function<List<NodeId>, Map<NodeId, DataValue>> readBatch) {
      super(server, "urn:eclipse:milo:wiki:batching");
      this.readBatch = readBatch;
      getLifecycleManager()
          .addStartupTask(
              () -> {
                for (String name : List.of("One", "Two", "Three")) {
                  NodeId id = newNodeId(name);
                  ids.add(id);
                  getNodeManager()
                      .addNode(
                          UaVariableNode.builder(getNodeContext())
                              .setNodeId(id)
                              .setBrowseName(newQualifiedName(name))
                              .setDisplayName(LocalizedText.english(name))
                              .setDataType(NodeIds.Double)
                              .setTypeDefinition(NodeIds.BaseDataVariableType)
                              .setValueRank(ValueRanks.Scalar)
                              .setAccessLevel(AccessLevel.READ_ONLY)
                              .setUserAccessLevel(AccessLevel.READ_ONLY)
                              .setMinimumSamplingInterval(0.0)
                              .setValue(new DataValue(new Variant(0.0)))
                              .build());
                }
              });
    }

    // wiki:batch-factory:start
    @Override
    protected SamplingGroupFactory samplingGroupFactory() {
      return (server, intervalMillis) -> new BatchGroup(server, intervalMillis, readBatch);
    }
    // wiki:batch-factory:end
  }
}
