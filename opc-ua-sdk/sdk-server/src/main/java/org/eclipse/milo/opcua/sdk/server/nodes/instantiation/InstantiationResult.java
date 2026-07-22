/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * The outcome of one applied {@link InstantiationPlan}: the typed root, per-path lookups, the
 * created/reused/shared index with provenance, the skipped index, the committed references, and
 * ownership-safe cleanup — everything the caller needs, with no casts and no post-hoc mutation.
 *
 * <p>The result also reports the commit guarantee actually achieved ({@link #storageGuarantee()}):
 * {@link NodeManager.StorageGuarantee#ATOMIC} when the target overrides the storage primitives,
 * {@link NodeManager.StorageGuarantee#BEST_EFFORT} when the commit ran on the sequential emulations
 * — a degraded apply proceeds but never silently claims atomicity.
 *
 * @param <T> the Java class of the instance root.
 */
public final class InstantiationResult<T extends UaNode> {

  private final T root;
  private final Map<BrowsePath, MaterializedNode> materializedByPath;
  private final List<MaterializedNode> materializedNodes;
  private final List<SkippedDeclaration> skippedDeclarations;
  private final List<MaterializedReference> references;
  private final List<InstantiationDiagnostic> diagnostics;
  private final long modelRevision;
  private final NodeManager.StorageGuarantee storageGuarantee;

  private final NodeManager<UaNode> target;
  private final AtomicBoolean deleted = new AtomicBoolean(false);

  InstantiationResult(
      T root,
      List<MaterializedNode> materializedNodes,
      List<SkippedDeclaration> skippedDeclarations,
      List<MaterializedReference> references,
      List<InstantiationDiagnostic> diagnostics,
      long modelRevision,
      NodeManager.StorageGuarantee storageGuarantee,
      NodeManager<UaNode> target) {

    this.root = root;
    this.materializedNodes = List.copyOf(materializedNodes);
    this.skippedDeclarations = List.copyOf(skippedDeclarations);
    this.references = List.copyOf(references);
    this.diagnostics = List.copyOf(diagnostics);
    this.modelRevision = modelRevision;
    this.storageGuarantee = storageGuarantee;
    this.target = target;

    var byPath = new LinkedHashMap<BrowsePath, MaterializedNode>();
    for (MaterializedNode node : this.materializedNodes) {
      byPath.put(node.browsePath(), node);
    }
    this.materializedByPath = byPath;
  }

  /**
   * @return the instance root, already of the request's expected class — no cast.
   */
  public T root() {
    return root;
  }

  /**
   * @param path the occurrence path to look up.
   * @return the node realized at {@code path}, if one was.
   */
  public Optional<UaNode> node(BrowsePath path) {
    return Optional.ofNullable(materializedByPath.get(path)).map(MaterializedNode::node);
  }

  /**
   * Get the node realized at {@code path}, checked against an expected class.
   *
   * @param path the occurrence path to look up.
   * @param type the expected node class.
   * @param <N> the expected node class.
   * @return the node realized at {@code path}.
   * @throws UaException if no node was realized at {@code path} ({@code Bad_NotFound}) or the
   *     realized node is not a {@code type} ({@code Bad_TypeMismatch}).
   */
  public <N extends UaNode> N require(BrowsePath path, Class<N> type) throws UaException {
    UaNode node =
        node(path)
            .orElseThrow(
                () -> new UaException(StatusCodes.Bad_NotFound, "no node realized at " + path));

    if (!type.isInstance(node)) {
      throw new UaException(
          StatusCodes.Bad_TypeMismatch,
          "node at " + path + " is a " + node.getClass().getName() + ", not a " + type.getName());
    }

    return type.cast(node);
  }

  /**
   * @return every realized node with provenance, browse-path ordered, the root first.
   */
  public List<MaterializedNode> materializedNodes() {
    return materializedNodes;
  }

  /**
   * @param path the occurrence path to look up.
   * @return the realized entry at {@code path}, with provenance, if one exists.
   */
  public Optional<MaterializedNode> materializedNode(BrowsePath path) {
    return Optional.ofNullable(materializedByPath.get(path));
  }

  /**
   * @return every declaration occurrence the plan decided not to realize, with reasons.
   */
  public List<SkippedDeclaration> skippedDeclarations() {
    return skippedDeclarations;
  }

  /**
   * @return every reference row the commit touched — planned rows and their inverses — each marked
   *     added or already-present.
   */
  public List<MaterializedReference> references() {
    return references;
  }

  /**
   * @return every finding carried by the plan; a returned result never contains errors. Apply-phase
   *     failures are reported by {@link InstantiationException} instead of on a result.
   */
  public List<InstantiationDiagnostic> diagnostics() {
    return diagnostics;
  }

  /**
   * @return the model revision this instantiation was applied against.
   */
  public long modelRevision() {
    return modelRevision;
  }

  /**
   * @return the commit guarantee actually achieved, from the target's capability probe.
   */
  public NodeManager.StorageGuarantee storageGuarantee() {
    return storageGuarantee;
  }

  /**
   * Delete everything this instantiation created — the journaled node and reference additions —
   * from the target {@link NodeManager}, and nothing else.
   *
   * <p>Idempotent; subsequent calls do nothing. Removal is by exact journal, never a recursive
   * delete from the root: reused, shared, and pre-existing resources are untouched, including
   * reference occurrences that already existed before the commit.
   *
   * <p>A removal failure does not abort the cleanup: every remaining journaled item is still
   * attempted, and the first failure is rethrown afterwards with any others suppressed.
   */
  public void deleteCreated() {
    if (!deleted.compareAndSet(false, true)) {
      return;
    }

    RuntimeException failure = null;

    for (MaterializedReference reference : references) {
      if (!reference.added()) {
        continue;
      }
      try {
        target.removeReference(reference.reference());
      } catch (RuntimeException e) {
        failure = suppress(failure, e);
      }
    }

    for (MaterializedNode node : materializedNodes) {
      if (node.provenance() != MaterializedNode.Provenance.CREATED) {
        continue;
      }
      try {
        target.removeNode(node.nodeId());
      } catch (RuntimeException e) {
        failure = suppress(failure, e);
      }
    }

    if (failure != null) {
      throw failure;
    }
  }

  private static RuntimeException suppress(
      @Nullable RuntimeException failure, RuntimeException next) {
    if (failure == null) {
      return next;
    }
    failure.addSuppressed(next);
    return failure;
  }
}
