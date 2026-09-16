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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * The pure-data result of joining one {@link InstantiationRequest} with the type's {@link
 * TypeInstantiationModel}: every declaration in the model is accounted for — planned, skipped with
 * a machine-readable reason, or a placeholder extension point — with all NodeIds allocated and
 * collision-checked and every reference resolved to instance ids, before anything exists.
 *
 * <p>Computing a plan mutates nothing. A plan carrying {@link #errors()} is returned inspectable
 * but refused by apply. Plans are deterministic: the same model and request produce the same
 * planned nodes, in the same order, with the same references.
 *
 * @param <T> the expected Java class of the instance root.
 */
public final class InstantiationPlan<T extends UaNode> {

  private final InstantiationRequest<T> request;
  private final TypeInstantiationModel model;
  private final List<PlannedNode> plannedNodes;
  private final Map<BrowsePath, PlannedNode> plannedByPath;
  private final List<SkippedDeclaration> skippedDeclarations;
  private final List<Reference> references;
  private final List<InstantiationDiagnostic> diagnostics;
  private final Map<NodeId, Long> consultedModelRevisions;

  InstantiationPlan(
      InstantiationRequest<T> request,
      TypeInstantiationModel model,
      List<PlannedNode> plannedNodes,
      List<SkippedDeclaration> skippedDeclarations,
      List<Reference> references,
      List<InstantiationDiagnostic> diagnostics,
      Map<NodeId, Long> consultedModelRevisions) {

    this.request = request;
    this.model = model;
    this.plannedNodes = List.copyOf(plannedNodes);
    this.skippedDeclarations = List.copyOf(skippedDeclarations);
    this.references = List.copyOf(references);
    this.diagnostics = List.copyOf(diagnostics);
    this.consultedModelRevisions = Map.copyOf(consultedModelRevisions);

    var byPath = new LinkedHashMap<BrowsePath, PlannedNode>();
    for (PlannedNode node : this.plannedNodes) {
      byPath.put(node.browsePath(), node);
    }
    this.plannedByPath = byPath;
  }

  /**
   * @return the request this plan was computed from.
   */
  public InstantiationRequest<T> request() {
    return request;
  }

  /**
   * @return the model snapshot this plan was computed against; apply revalidates its {@link
   *     TypeInstantiationModel#modelRevision()} before constructing anything.
   */
  public TypeInstantiationModel model() {
    return model;
  }

  /**
   * @return the model revision this plan was computed against.
   */
  public long modelRevision() {
    return model.modelRevision();
  }

  /**
   * Every type model this plan was computed from, with the revision consulted: the root type plus
   * any member models resolved separately (a {@code concreteType} selection outside the root
   * model's dependency closure has its own cache entry, so the root revision alone cannot vouch for
   * it). Apply revalidates each entry before constructing anything.
   *
   * @return the consulted type models, keyed by type definition {@link NodeId}, mapped to revision.
   */
  public Map<NodeId, Long> consultedModelRevisions() {
    return consultedModelRevisions;
  }

  /**
   * @return the planned instance root.
   */
  public PlannedNode root() {
    return plannedNodes.get(0);
  }

  /**
   * @return every planned node, browse-path ordered, the root first.
   */
  public List<PlannedNode> plannedNodes() {
    return plannedNodes;
  }

  /**
   * @param path the occurrence path to look up.
   * @return the planned node at {@code path}, if one was planned.
   */
  public Optional<PlannedNode> plannedNode(BrowsePath path) {
    return Optional.ofNullable(plannedByPath.get(path));
  }

  /**
   * @return every declaration occurrence this plan decided not to realize, with reasons,
   *     browse-path ordered.
   */
  public List<SkippedDeclaration> skippedDeclarations() {
    return skippedDeclarations;
  }

  /**
   * @return every planned reference, fully resolved to instance ids: the root's HasTypeDefinition,
   *     the parent attachment (if requested), the realized hierarchy edges, and the replicated
   *     declaration rows — in that deterministic order. The commit additionally derives and stores
   *     each row's inverse, so the committed row set is larger than this list.
   */
  public List<Reference> references() {
    return references;
  }

  /**
   * @return every plan finding, errors and warnings.
   */
  public List<InstantiationDiagnostic> diagnostics() {
    return diagnostics;
  }

  /**
   * @return the error findings; a plan carrying any is refused by apply.
   */
  public List<InstantiationDiagnostic> errors() {
    return diagnostics.stream().filter(InstantiationDiagnostic::isError).toList();
  }

  /**
   * @return the warning findings.
   */
  public List<InstantiationDiagnostic> warnings() {
    return diagnostics.stream().filter(d -> !d.isError()).toList();
  }

  /**
   * @return {@code true} if this plan carries at least one error finding.
   */
  public boolean hasErrors() {
    return diagnostics.stream().anyMatch(InstantiationDiagnostic::isError);
  }
}
