/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki.snippets;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroup;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingGroupFactory;
import org.eclipse.milo.opcua.sdk.server.sampling.SamplingManagerConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/** Sampling configuration and device batching samples from the Wiki. */
public final class SamplingSnippets {

  private SamplingSnippets() {}

  /** A namespace that samples no faster than 100 ms and caches read-access decisions. */
  static final class CachedAccessNamespace extends ManagedNamespaceWithLifecycle {

    CachedAccessNamespace(OpcUaServer server) {
      super(server, "urn:eclipse:milo:wiki:sampling");
    }

    // snippet:sampling:start
    @Override
    protected SamplingManagerConfig samplingManagerConfig() {
      return SamplingManagerConfig.defaults()
          .withMinimumIntervalMillis(100)
          .withReadAccessPolicy(ReadAccessPolicy.cached());
    }
    // snippet:sampling:end
  }

  // snippet:batch-group:start
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
      List<NodeId> nodeIds =
          items.stream().map(item -> item.getReadValueId().getNodeId()).distinct().toList();

      Map<NodeId, DataValue> values;
      try {
        values = readBatch.apply(nodeIds);
      } catch (RuntimeException e) {
        for (DataItem item : items) {
          deliver(item, new DataValue(StatusCodes.Bad_CommunicationError));
        }
        return null;
      }

      for (DataItem item : items) {
        NodeId nodeId = item.getReadValueId().getNodeId();
        DataValue value = values.getOrDefault(nodeId, new DataValue(StatusCodes.Bad_NoData));
        deliver(item, value);
      }
      return null;
    }
  }

  // snippet:batch-group:end

  /** A namespace whose Variables are sampled through one device read per group turn. */
  static final class BatchNamespace extends ManagedNamespaceWithLifecycle {
    private final Function<List<NodeId>, Map<NodeId, DataValue>> readBatch;

    BatchNamespace(OpcUaServer server, Function<List<NodeId>, Map<NodeId, DataValue>> readBatch) {
      super(server, "urn:eclipse:milo:wiki:batching");
      this.readBatch = readBatch;
    }

    // snippet:batch-factory:start
    @Override
    protected SamplingGroupFactory samplingGroupFactory() {
      return (server, intervalMillis) -> new BatchGroup(server, intervalMillis, readBatch);
    }
    // snippet:batch-factory:end
  }
}
