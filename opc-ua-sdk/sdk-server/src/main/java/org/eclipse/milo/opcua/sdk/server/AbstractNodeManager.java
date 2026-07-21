/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import com.google.common.collect.LinkedHashMultiset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class AbstractNodeManager<T extends Node> implements NodeManager<T> {

  private final ConcurrentMap<NodeId, T> nodeMap = new ConcurrentHashMap<>();
  private final ConcurrentMap<NodeId, LinkedHashMultiset<Reference>> referenceMap =
      new ConcurrentHashMap<>();

  // Bumped by every mutation under the instance monitor; read lock-free (volatile) by
  // getGeneration().
  private volatile long generation = 0L;

  /**
   * Get the backing {@link ConcurrentMap} holding this {@link NodeManager}'s Nodes.
   *
   * @return the backing {@link ConcurrentMap} holding this {@link NodeManager}'s Nodes.
   */
  protected ConcurrentMap<NodeId, T> getNodeMap() {
    return nodeMap;
  }

  /**
   * Get the backing {@link ConcurrentMap} holding this {@link NodeManager}'s References.
   *
   * @return the backing {@link ConcurrentMap} holding this {@link NodeManager}'s References.
   */
  public ConcurrentMap<NodeId, LinkedHashMultiset<Reference>> getReferenceMap() {
    return referenceMap;
  }

  /**
   * Get a copied List of the Nodes being managed.
   *
   * @return a copied List of the Nodes being managed.
   */
  public List<T> getNodes() {
    return new ArrayList<>(nodeMap.values());
  }

  /**
   * Get a copied List of the {@link NodeId}s being managed.
   *
   * @return a copied List of the {@link NodeId}s being managed.
   */
  public List<NodeId> getNodeIds() {
    return new ArrayList<>(nodeMap.keySet());
  }

  @Override
  public boolean containsNode(NodeId nodeId) {
    return nodeMap.containsKey(nodeId);
  }

  @Override
  public boolean containsNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    return nodeId.toNodeId(namespaceTable).map(this::containsNode).orElse(false);
  }

  @Override
  public synchronized Optional<T> addNode(T node) {
    generation++;
    return Optional.ofNullable(nodeMap.put(node.getNodeId(), node));
  }

  @Override
  public synchronized boolean addNodeIfAbsent(T node) {
    if (nodeMap.containsKey(node.getNodeId())) {
      return false;
    } else {
      nodeMap.put(node.getNodeId(), node);
      generation++;
      return true;
    }
  }

  @Override
  public Optional<T> getNode(NodeId nodeId) {
    return Optional.ofNullable(nodeMap.get(nodeId));
  }

  @Override
  public Optional<T> getNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    return nodeId.toNodeId(namespaceTable).flatMap(this::getNode);
  }

  @Override
  public synchronized Optional<T> removeNode(NodeId nodeId) {
    T removed = nodeMap.remove(nodeId);
    if (removed != null) {
      generation++;
    }
    return Optional.ofNullable(removed);
  }

  @Override
  public Optional<T> removeNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    return nodeId.toNodeId(namespaceTable).flatMap(this::removeNode);
  }

  @Override
  public synchronized void addReference(Reference reference) {
    LinkedHashMultiset<Reference> references =
        referenceMap.computeIfAbsent(
            reference.getSourceNodeId(), nodeId -> LinkedHashMultiset.create());

    references.add(reference);
    generation++;
  }

  @Override
  public synchronized void addReferences(Reference reference, NamespaceTable namespaceTable) {
    addReference(reference);

    reference.invert(namespaceTable).ifPresent(this::addReference);
  }

  @Override
  public synchronized void removeReference(Reference reference) {
    if (removeReferenceOccurrence(reference)) {
      generation++;
    }
  }

  @Override
  public synchronized void removeReferences(Reference reference, NamespaceTable namespaceTable) {
    removeReference(reference);

    reference.invert(namespaceTable).ifPresent(this::removeReference);
  }

  /**
   * Remove one occurrence of {@code reference} from the reference map, dropping the source Node's
   * multiset once it becomes empty. Does not bump the generation, so callers own that decision.
   *
   * @param reference the {@link Reference} occurrence to remove.
   * @return {@code true} if an occurrence was removed.
   */
  private boolean removeReferenceOccurrence(Reference reference) {
    LinkedHashMultiset<Reference> references = referenceMap.get(reference.getSourceNodeId());
    if (references == null) {
      return false;
    }

    boolean removed = references.remove(reference);
    if (references.isEmpty()) {
      referenceMap.remove(reference.getSourceNodeId());
    }
    return removed;
  }

  @Override
  public synchronized List<Reference> getReferences(NodeId nodeId) {
    LinkedHashMultiset<Reference> references = referenceMap.get(nodeId);

    if (references != null) {
      return new ArrayList<>(references);
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public synchronized List<Reference> getReferences(NodeId nodeId, Predicate<Reference> filter) {
    LinkedHashMultiset<Reference> references = referenceMap.get(nodeId);

    return references != null
        ? references.stream().filter(filter).toList()
        : Collections.emptyList();
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation is atomic: the batch is validated in full before anything is applied,
   * and a failed commit leaves the maps exactly as they were before the call. Synchronizing on the
   * instance makes the commit exclusive with the other mutation methods, which share the same
   * monitor.
   */
  @Override
  public synchronized CommitResult commit(NodeManagerBatch<T> batch)
      throws NodeManagerBatchException {

    CommitResult nothingApplied = new CommitResult(List.of(), List.of());

    OptionalLong expectedGeneration = batch.getExpectedGeneration();
    if (expectedGeneration.isPresent() && expectedGeneration.getAsLong() != generation) {
      throw NodeManagerBatchException.staleGeneration(
          expectedGeneration.getAsLong(), generation, nothingApplied);
    }

    for (NodeId reusedNodeId : batch.getReusedNodeIds()) {
      if (!nodeMap.containsKey(reusedNodeId)) {
        throw NodeManagerBatchException.reusedNodeUnknown(reusedNodeId, nothingApplied);
      }
    }

    for (T node : batch.getNodeAdditions()) {
      if (nodeMap.containsKey(node.getNodeId())) {
        throw NodeManagerBatchException.nodeExists(node.getNodeId(), nothingApplied);
      }
    }

    // Validation is complete; apply, undoing all applied additions if a mutation fails.
    List<NodeId> addedNodes = new ArrayList<>();
    List<Reference> addedReferences = new ArrayList<>();
    try {
      for (T node : batch.getNodeAdditions()) {
        nodeMap.put(node.getNodeId(), node);
        addedNodes.add(node.getNodeId());
      }
      // Logical deduplication: only References not already present get a new occurrence.
      for (Reference reference : batch.getReferenceAdditions()) {
        LinkedHashMultiset<Reference> references =
            referenceMap.computeIfAbsent(
                reference.getSourceNodeId(), nodeId -> LinkedHashMultiset.create());
        if (!references.contains(reference)) {
          references.add(reference);
          addedReferences.add(reference);
        }
      }
    } catch (RuntimeException e) {
      for (Reference reference : addedReferences) {
        removeReferenceOccurrence(reference);
      }
      for (NodeId nodeId : addedNodes) {
        nodeMap.remove(nodeId);
      }
      throw new NodeManagerBatchException(
          StatusCodes.Bad_InternalError,
          "batch commit failed and was rolled back",
          e,
          nothingApplied);
    }

    // Only a commit that actually changed state bumps the generation; a no-op commit (empty batch,
    // reuse-only, or References all already present) must leave outstanding handles current, like
    // the other mutation methods.
    if (!addedNodes.isEmpty() || !addedReferences.isEmpty()) {
      generation++;
    }

    return new CommitResult(addedNodes, addedReferences);
  }

  @Override
  public Generation getGeneration() {
    long value = generation;

    return new Generation() {
      @Override
      public long value() {
        return value;
      }

      @Override
      public boolean isCurrent() {
        return value == generation;
      }
    };
  }

  @Override
  public StorageGuarantee getStorageGuarantee() {
    return StorageGuarantee.ATOMIC;
  }
}
