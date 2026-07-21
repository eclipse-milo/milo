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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public interface NodeManager<T extends Node> {

  /**
   * Return {@code true} if this {@link NodeManager} contains the Node identified by {@code nodeId}.
   *
   * @param nodeId the {@link NodeId}.
   * @return {@code true} if this {@link NodeManager} contains the Node identified by {@code
   *     nodeId}.
   */
  boolean containsNode(NodeId nodeId);

  /**
   * Return {@code true} if this {@link NodeManager} contains {@code nodeId}.
   *
   * <p>Returns {@code false} if {@code nodeId} is non-local.
   *
   * @param nodeId the {@link ExpandedNodeId} to check.
   * @param namespaceTable the {@link NamespaceTable}.
   * @return {@code true} if this {@link NodeManager} contains {@code nodeId}.
   */
  boolean containsNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable);

  /**
   * Add {@code node} to this {@link NodeManager}, replacing a previous Node identified by the same
   * {@link NodeId} if it exists.
   *
   * @param node the {@link Node} to add.
   * @return the previous Node identified by the {@link NodeId} of {@code node}, if it exists.
   */
  Optional<T> addNode(T node);

  /**
   * Get the Node identified by {@code nodeId} from this {@link NodeManager}, if it exists.
   *
   * @param nodeId the {@link NodeId} identifying the Node to get.
   * @return the Node identified by {@code nodeId} from this {@link NodeManager}, if it exists.
   */
  Optional<T> getNode(NodeId nodeId);

  /**
   * Get the {@link Node} identified by {@code nodeId} from this {@link NodeManager}, or {@code
   * null} if it does not exist.
   *
   * <p>Returns {@code false} if {@code nodeId} is non-local.
   *
   * @param nodeId the {@link ExpandedNodeId} identifying the {@link Node} to get.
   * @param namespaceTable the {@link NamespaceTable}.
   * @return the {@link Node} identified by {@code nodeId} from this {@link NodeManager} or {@code
   *     null} if it does not exist.
   */
  Optional<T> getNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable);

  /**
   * Remove the Node identified by {@code nodeId} from this {@link NodeManager}, if it exists.
   *
   * @param nodeId the {@link NodeId} identifying the Node to remove.
   * @return the Node removed from this {@link NodeManager}, if it exists.
   */
  Optional<T> removeNode(NodeId nodeId);

  /**
   * Get the {@link Node} identified by {@code nodeId} from this {@link NodeManager}, if it exists.
   *
   * <p>Returns {@link Optional#empty()} if {@code nodeId} is non-local.
   *
   * @param nodeId the {@link ExpandedNodeId} identifying the Node to get.
   * @param namespaceTable the {@link NamespaceTable}.
   * @return the {@link Node} identified by {@code nodeId} from this {@link NodeManager}, if it
   *     exists.
   */
  Optional<T> removeNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable);

  /**
   * Add {@code reference} to this {@link NodeManager}.
   *
   * @param reference the {@link Reference} to add.
   */
  void addReference(Reference reference);

  /**
   * Add {@code reference} and its inverse to this {@link NodeManager}.
   *
   * @param reference the {@link Reference} to add.
   * @param namespaceTable the {@link NamespaceTable}.
   */
  void addReferences(Reference reference, NamespaceTable namespaceTable);

  /**
   * Remove {@code reference} from this {@link NodeManager}.
   *
   * @param reference the {@link Reference} to remove.
   */
  void removeReference(Reference reference);

  /**
   * Remove {@code reference} and its inverse from this {@link NodeManager}.
   *
   * @param reference the {@link Reference} to remove.
   * @param namespaceTable the {@link NamespaceTable}.
   */
  void removeReferences(Reference reference, NamespaceTable namespaceTable);

  /**
   * Get all {@link Reference}s that have {@code nodeId} as their source {@link NodeId}.
   *
   * @param nodeId the source {@link NodeId}.
   * @return all {@link Reference}s that have {@code nodeId} as their source {@link NodeId}.
   */
  List<Reference> getReferences(NodeId nodeId);

  /**
   * Get all {@link Reference}s that have {@code nodeId} as their source {@link NodeId}, filtered by
   * {@code filter}.
   *
   * @param nodeId the source {@link NodeId}.
   * @param filter a {@link Predicate} to filter {@link Reference}s.
   * @return all {@link Reference}s that have {@code nodeId} as their source {@link NodeId},
   *     filtered by {@code filter}.
   */
  List<Reference> getReferences(NodeId nodeId, Predicate<Reference> filter);

  /**
   * Return {@code true} if this {@link NodeManager} contains {@code node}.
   *
   * @param node the {@link Node} to check.
   * @return {@code true} if this {@link NodeManager} contains {@code node}.
   */
  default boolean containsNode(Node node) {
    return containsNode(node.getNodeId());
  }

  /**
   * Get the {@link Node} identified by {@code nodeId} from this {@link NodeManager}, or {@code
   * null} if it does not exist.
   *
   * @param nodeId the {@link NodeId} identifying the {@link Node} to get.
   * @return the Node identified by {@code nodeId} from this {@link NodeManager} or {@code null} if
   *     it does not exist.
   */
  @Nullable
  default T get(NodeId nodeId) {
    return getNode(nodeId).orElse(null);
  }

  /**
   * Get the {@link Node} identified by {@code nodeId} from this {@link NodeManager}, or {@code
   * null} if it does not exist.
   *
   * @param nodeId the {@link NodeId} identifying the Node to get.
   * @param namespaceTable the {@link NamespaceTable}.
   * @return the {@link Node} identified by {@code nodeId} from this {@link NodeManager} or {@code
   *     null} if it does not exist.
   */
  @Nullable
  default T get(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
    return getNode(nodeId, namespaceTable).orElse(null);
  }

  /**
   * Remove {@code node} from this {@link NodeManager}, if it exists.
   *
   * @param node the {@link Node} to remove.
   * @return the {@link Node} removed from this {@link NodeManager}, if it exists.
   */
  default Optional<T> removeNode(T node) {
    return removeNode(node.getNodeId());
  }

  /**
   * Add {@code node} to this {@link NodeManager} only if no Node identified by the same {@link
   * NodeId} is already present.
   *
   * <p>Unlike {@link #addNode(Node)}, this method never replaces an existing Node, making
   * fail-on-collision semantics possible without a separate check-then-act race at the storage
   * layer.
   *
   * <p>The default implementation is a sequential composition of {@link #containsNode(NodeId)} and
   * {@link #addNode(Node)}. It is correct when this manager has a single writer, but it is not
   * atomic: a concurrent direct write between the check and the add can be replaced.
   * Implementations that can perform the check and add atomically should override this method and
   * report {@link StorageGuarantee#ATOMIC} from {@link #getStorageGuarantee()}.
   *
   * @param node the {@link Node} to add.
   * @return {@code true} if {@code node} was added, or {@code false} if a Node identified by the
   *     same {@link NodeId} was already present (in which case nothing changed).
   */
  default boolean addNodeIfAbsent(T node) {
    if (containsNode(node.getNodeId())) {
      return false;
    } else {
      addNode(node);
      return true;
    }
  }

  /**
   * Apply the Node and {@link Reference} additions in {@code batch} to this {@link NodeManager} as
   * one unit.
   *
   * <p>Semantics common to all implementations:
   *
   * <ul>
   *   <li>Node additions are conditional: a batch fails with {@link StatusCodes#Bad_NodeIdExists}
   *       rather than replacing an existing Node.
   *   <li>Reused Nodes declared via {@link NodeManagerBatch.Builder#reuseNode(NodeId)} must be
   *       present, otherwise the batch fails with {@link StatusCodes#Bad_NodeIdUnknown}.
   *   <li>Reference additions are <em>logically</em> deduplicated: a Reference that is already
   *       present (by equality) is not added again and does not appear in the returned journal.
   *       References are stored as a multiset, so duplicate-occurrence prevention must happen here
   *       — after the fact, a cleanup pass cannot tell which duplicate occurrence it owns.
   *   <li>If {@code batch} carries an expected generation and it does not match this manager's
   *       current generation, the batch fails with {@link StatusCodes#Bad_InvalidState} before
   *       anything is applied.
   *   <li>The returned {@link CommitResult} is the exact journal of what was applied; callers roll
   *       a committed batch back by removing exactly those additions and nothing else.
   * </ul>
   *
   * <p>The default implementation applies the batch as a sequential composition of the existing
   * methods. It is correct when this manager has a single writer, but it is <em>not atomic</em>: a
   * failure mid-batch leaves the additions applied so far in place, reported by {@link
   * NodeManagerBatchException#getApplied()} so callers can attempt best-effort removal.
   * Implementations that can apply a batch all-or-nothing should override this method, guarantee
   * that a failed commit leaves storage exactly as it was, and report {@link
   * StorageGuarantee#ATOMIC} from {@link #getStorageGuarantee()}.
   *
   * @param batch the {@link NodeManagerBatch} to apply.
   * @return a {@link CommitResult} journaling exactly what was applied.
   * @throws NodeManagerBatchException if the batch could not be applied in full.
   */
  default CommitResult commit(NodeManagerBatch<T> batch) throws NodeManagerBatchException {
    List<NodeId> addedNodes = new ArrayList<>();
    List<Reference> addedReferences = new ArrayList<>();

    OptionalLong expectedGeneration = batch.getExpectedGeneration();
    if (expectedGeneration.isPresent()) {
      long currentGeneration = getGeneration().value();
      if (expectedGeneration.getAsLong() != currentGeneration) {
        throw NodeManagerBatchException.staleGeneration(
            expectedGeneration.getAsLong(),
            currentGeneration,
            new CommitResult(addedNodes, addedReferences));
      }
    }

    for (NodeId reusedNodeId : batch.getReusedNodeIds()) {
      if (!containsNode(reusedNodeId)) {
        throw NodeManagerBatchException.reusedNodeUnknown(
            reusedNodeId, new CommitResult(addedNodes, addedReferences));
      }
    }

    for (T node : batch.getNodeAdditions()) {
      if (addNodeIfAbsent(node)) {
        addedNodes.add(node.getNodeId());
      } else {
        throw NodeManagerBatchException.nodeExists(
            node.getNodeId(), new CommitResult(addedNodes, addedReferences));
      }
    }

    for (Reference reference : batch.getReferenceAdditions()) {
      if (!getReferences(reference.getSourceNodeId()).contains(reference)) {
        addReference(reference);
        addedReferences.add(reference);
      }
    }

    return new CommitResult(addedNodes, addedReferences);
  }

  /**
   * Get a {@link Generation} handle over this {@link NodeManager}'s write generation, captured at
   * the time of the call.
   *
   * <p>The handle supports staleness detection: {@link Generation#isCurrent()} returns {@code
   * false} once this manager has been mutated after the handle was acquired. Passing the handle's
   * {@link Generation#value()} to {@link NodeManagerBatch.Builder#expectGeneration(long)} makes the
   * staleness check part of {@link #commit(NodeManagerBatch)} itself, so a supporting manager
   * checks and commits atomically.
   *
   * <p>The default implementation performs no tracking: {@code value()} is always {@code 0} and
   * {@code isCurrent()} is always {@code true}, which is correct only when this manager has a
   * single writer. Implementations that track mutations should override this method.
   *
   * @return a {@link Generation} handle captured at the time of the call.
   */
  default Generation getGeneration() {
    return new Generation() {
      @Override
      public long value() {
        return 0L;
      }

      @Override
      public boolean isCurrent() {
        return true;
      }
    };
  }

  /**
   * Report the guarantee this {@link NodeManager}'s storage primitives provide.
   *
   * <p>The default implementations of {@link #addNodeIfAbsent(Node)}, {@link
   * #commit(NodeManagerBatch)}, and {@link #getGeneration()} are sequential emulations that are
   * correct only with a single writer, so the default answer is {@link
   * StorageGuarantee#BEST_EFFORT}. Implementations overriding the primitives with atomic semantics
   * must also override this method; callers use it to report the commit guarantee they actually
   * achieved rather than silently assuming atomicity.
   *
   * @return the {@link StorageGuarantee} this manager's primitives provide.
   */
  default StorageGuarantee getStorageGuarantee() {
    return StorageGuarantee.BEST_EFFORT;
  }

  /**
   * A snapshot of a {@link NodeManager}'s write generation, used to detect that a manager was
   * mutated between the time the snapshot was acquired and the time it is checked.
   */
  interface Generation {

    /**
     * Get the generation value captured when this handle was acquired.
     *
     * @return the generation value captured when this handle was acquired.
     */
    long value();

    /**
     * Return {@code true} if the {@link NodeManager} has not been mutated since this handle was
     * acquired.
     *
     * @return {@code true} if the {@link NodeManager} has not been mutated since this handle was
     *     acquired.
     */
    boolean isCurrent();
  }

  /** The guarantee a {@link NodeManager}'s storage primitives provide. */
  enum StorageGuarantee {

    /**
     * The primitives are atomic: {@link #addNodeIfAbsent(Node)} is check-and-act without a race
     * window, a failed {@link #commit(NodeManagerBatch)} leaves storage exactly as it was, and
     * {@link #getGeneration()} tracks mutations.
     */
    ATOMIC,

    /**
     * The primitives are sequential emulations: correct with a single writer, unprotected against
     * concurrent direct writes, and a failed {@link #commit(NodeManagerBatch)} may leave a partial
     * batch applied.
     */
    BEST_EFFORT
  }

  /**
   * The journal of what a {@link #commit(NodeManagerBatch)} actually applied: the {@link NodeId}s
   * of the Nodes added and the {@link Reference}s physically added (batch References that were
   * already logically present are excluded). Rolling back a committed batch means removing exactly
   * these additions.
   *
   * @param addedNodes the {@link NodeId}s of the Nodes added by the commit.
   * @param addedReferences the {@link Reference}s added by the commit.
   */
  record CommitResult(List<NodeId> addedNodes, List<Reference> addedReferences) {

    public CommitResult {
      addedNodes = List.copyOf(addedNodes);
      addedReferences = List.copyOf(addedReferences);
    }
  }
}
