/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public class NodeCache {

  private final Cache<NodeId, UaNode> cachedNodes;
  private final ConcurrentMap<NodeId, UaNode> canonicalNodes;

  public NodeCache() {
    this(
        builder -> {
          builder.expireAfterWrite(2, TimeUnit.MINUTES);
          builder.maximumSize(16384);
          builder.recordStats();
        });
  }

  public NodeCache(Consumer<CacheBuilder<Object, Object>> consumer) {
    CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

    consumer.accept(builder);

    this.cachedNodes = builder.build();
    this.canonicalNodes = new ConcurrentHashMap<>();
  }

  /**
   * Canonicalize {@code node} in the NodeCache and return it.
   *
   * <p>If a canonical {@link UaNode} by the same {@link NodeId} already exists that instance is
   * returned.
   *
   * @param node the {@link UaNode} to canonicalize.
   * @return the canonical node instance.
   */
  public UaNode canonicalize(UaNode node) {
    NodeId nodeId = node.getNodeId();

    UaNode prev = canonicalNodes.putIfAbsent(nodeId, node);

    return prev != null ? prev : node;
  }

  @Nullable
  public UaNode getIfPresent(NodeId nodeId) {
    return canonicalNodes.getOrDefault(nodeId, cachedNodes.getIfPresent(nodeId));
  }

  public void put(NodeId nodeId, UaNode node) {
    cachedNodes.put(nodeId, node);
  }

  public void invalidate(NodeId nodeId) {
    canonicalNodes.remove(nodeId);
    cachedNodes.invalidate(nodeId);
  }
}
