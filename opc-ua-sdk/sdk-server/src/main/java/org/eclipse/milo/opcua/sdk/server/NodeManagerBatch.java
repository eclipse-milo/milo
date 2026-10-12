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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.OptionalLong;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * An immutable batch of Node and {@link Reference} additions to be applied to a {@link NodeManager}
 * as one unit via {@link NodeManager#commit(NodeManagerBatch)}.
 *
 * <p>A batch distinguishes Nodes it <em>adds</em> from Nodes it <em>reuses</em>: added Nodes must
 * not exist yet and become part of the commit's journal (they are what a rollback removes), while
 * reused Nodes must already exist and are never touched — the distinction is what lets a caller
 * that reused an existing Node roll back or delete its own additions without deleting Nodes it does
 * not own.
 *
 * <p>Reference additions are deduplicated by equality at build time; {@link
 * NodeManager#commit(NodeManagerBatch)} additionally skips References already present in the
 * manager, so committing a batch never creates a duplicate Reference occurrence.
 *
 * @param <T> the type of {@link Node} the target {@link NodeManager} manages.
 */
public final class NodeManagerBatch<T extends Node> {

  private final List<T> nodeAdditions;
  private final List<NodeId> reusedNodeIds;
  private final List<Reference> referenceAdditions;
  private final @Nullable Long expectedGeneration;

  private NodeManagerBatch(
      List<T> nodeAdditions,
      List<NodeId> reusedNodeIds,
      List<Reference> referenceAdditions,
      @Nullable Long expectedGeneration) {

    this.nodeAdditions = nodeAdditions;
    this.reusedNodeIds = reusedNodeIds;
    this.referenceAdditions = referenceAdditions;
    this.expectedGeneration = expectedGeneration;
  }

  /**
   * Get the Nodes this batch adds, in insertion order.
   *
   * @return the Nodes this batch adds, in insertion order.
   */
  public List<T> getNodeAdditions() {
    return nodeAdditions;
  }

  /**
   * Get the {@link NodeId}s of existing Nodes this batch reuses, in insertion order.
   *
   * @return the {@link NodeId}s of existing Nodes this batch reuses, in insertion order.
   */
  public List<NodeId> getReusedNodeIds() {
    return reusedNodeIds;
  }

  /**
   * Get the {@link Reference}s this batch adds, deduplicated by equality, in insertion order.
   *
   * @return the {@link Reference}s this batch adds, deduplicated by equality, in insertion order.
   */
  public List<Reference> getReferenceAdditions() {
    return referenceAdditions;
  }

  /**
   * Get the {@link NodeManager} generation this batch expects at commit time, if one was set.
   *
   * @return the {@link NodeManager} generation this batch expects at commit time, if one was set.
   * @see Builder#expectGeneration(long)
   */
  public OptionalLong getExpectedGeneration() {
    return expectedGeneration != null ? OptionalLong.of(expectedGeneration) : OptionalLong.empty();
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param <T> the type of {@link Node} the target {@link NodeManager} manages.
   * @return a new {@link Builder}.
   */
  public static <T extends Node> Builder<T> builder() {
    return new Builder<>();
  }

  /**
   * A builder of {@link NodeManagerBatch} instances.
   *
   * @param <T> the type of {@link Node} the target {@link NodeManager} manages.
   */
  public static final class Builder<T extends Node> {

    private final LinkedHashMap<NodeId, T> nodeAdditions = new LinkedHashMap<>();
    private final LinkedHashSet<NodeId> reusedNodeIds = new LinkedHashSet<>();
    private final LinkedHashSet<Reference> referenceAdditions = new LinkedHashSet<>();
    private @Nullable Long expectedGeneration;

    private Builder() {}

    /**
     * Add {@code node} to the batch. At commit time no Node identified by the same {@link NodeId}
     * may exist.
     *
     * @param node the {@link Node} to add.
     * @return this {@link Builder}.
     * @throws IllegalArgumentException if the batch already adds or reuses a Node identified by the
     *     same {@link NodeId}.
     */
    public Builder<T> addNode(T node) {
      NodeId nodeId = node.getNodeId();
      if (nodeAdditions.containsKey(nodeId)) {
        throw new IllegalArgumentException("batch already adds Node: " + nodeId);
      }
      if (reusedNodeIds.contains(nodeId)) {
        throw new IllegalArgumentException("batch already reuses Node: " + nodeId);
      }
      nodeAdditions.put(nodeId, node);
      return this;
    }

    /**
     * Declare that the batch reuses the existing Node identified by {@code nodeId}. At commit time
     * the Node must exist; it is not modified, and it is not part of the commit's journal.
     *
     * @param nodeId the {@link NodeId} of the existing Node.
     * @return this {@link Builder}.
     * @throws IllegalArgumentException if the batch already adds a Node identified by {@code
     *     nodeId}.
     */
    public Builder<T> reuseNode(NodeId nodeId) {
      if (nodeAdditions.containsKey(nodeId)) {
        throw new IllegalArgumentException("batch already adds Node: " + nodeId);
      }
      reusedNodeIds.add(nodeId);
      return this;
    }

    /**
     * Add {@code reference} to the batch. Equal References are collapsed to a single addition.
     *
     * @param reference the {@link Reference} to add.
     * @return this {@link Builder}.
     */
    public Builder<T> addReference(Reference reference) {
      referenceAdditions.add(reference);
      return this;
    }

    /**
     * Require the target {@link NodeManager}'s generation to equal {@code generation} at commit
     * time, failing the commit before anything is applied otherwise.
     *
     * <p>Capture the value from {@link NodeManager#getGeneration()} when the batch's contents are
     * decided; the commit then detects any mutation of the manager in between.
     *
     * @param generation the expected generation, from {@link NodeManager.Generation#value()}.
     * @return this {@link Builder}.
     */
    public Builder<T> expectGeneration(long generation) {
      this.expectedGeneration = generation;
      return this;
    }

    /**
     * Build the immutable {@link NodeManagerBatch}.
     *
     * @return the immutable {@link NodeManagerBatch}.
     */
    public NodeManagerBatch<T> build() {
      return new NodeManagerBatch<>(
          List.copyOf(nodeAdditions.values()),
          List.copyOf(reusedNodeIds),
          List.copyOf(referenceAdditions),
          expectedGeneration);
    }
  }
}
