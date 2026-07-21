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

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * A structured finding produced while planning or applying one instantiation, carried on the {@link
 * InstantiationPlan}. Compile-time findings about the <em>type</em> are {@link ModelDiagnostic}s
 * instead; these are per-request.
 *
 * <p>A plan carrying any {@link Severity#ERROR} diagnostic is returned inspectable but refused by
 * apply — a healthy instantiation is explainable per declaration, and a failed one names its phase
 * and path.
 *
 * @param severity the finding's severity.
 * @param phase the lifecycle phase that produced the finding.
 * @param code the machine-readable finding code.
 * @param message a human-readable description.
 * @param browsePath the affected occurrence path, if the finding is path-specific.
 * @param nodeId the affected or attempted {@link NodeId}, if identifiable.
 */
public record InstantiationDiagnostic(
    Severity severity,
    Phase phase,
    Code code,
    String message,
    @Nullable BrowsePath browsePath,
    @Nullable NodeId nodeId) {

  /** Severity of an {@link InstantiationDiagnostic}. */
  public enum Severity {
    /** A finding on a plan that remains applicable. */
    WARNING,
    /** The plan (or apply) cannot proceed as requested. */
    ERROR
  }

  /** The lifecycle phase a diagnostic was produced in. */
  public enum Phase {
    /** Produced by {@code NodeInstantiator.plan}. */
    PLAN,
    /** Produced by {@code NodeInstantiator.apply}. */
    APPLY
  }

  /** Machine-readable codes for {@link InstantiationDiagnostic}s. */
  public enum Code {
    /**
     * The registered (or resolved) Java class of the root type is not assignable to the request's
     * expected root class.
     */
    INVALID_ROOT_CLASS,
    /** The requested root type is abstract; request a concrete subtype instead. */
    ABSTRACT_TYPE,
    /**
     * A planned member's TypeDefinition is abstract and the request supplies no {@code
     * concreteType} resolution for its path.
     */
    ABSTRACT_MEMBER_TYPE,
    /**
     * A {@code concreteType} selection is invalid: not the declared member type or a subtype of it,
     * abstract itself, or of the wrong NodeClass.
     */
    CONCRETE_TYPE_INVALID,
    /**
     * A {@code concreteType} selection declares members beyond the declared member type's; those
     * additional members are not expanded by this release (the declared type's members are planned;
     * the instance's HasTypeDefinition references the concrete type).
     */
    CONCRETE_TYPE_NOT_EXPANDED,
    /**
     * A Mandatory declaration whose parent path exists was excluded — the one omission Part 3
     * §6.4.4.4.2 does not permit (pruning an omitted ancestor's subtree is not an error).
     */
    MANDATORY_OMITTED,
    /**
     * A planned {@link NodeId} collides with another planned node or, under {@link
     * ConflictPolicy#FAIL}, with an existing node in the target.
     */
    NODE_ID_COLLISION,
    /**
     * Under {@link ConflictPolicy#REUSE_COMPATIBLE}, an existing node at a planned id is not
     * compatible (BrowseName, NodeClass, TypeDefinition, or expected Java class).
     */
    INCOMPATIBLE_REUSE,
    /** A request override violates the Variable narrowing rules of Part 3 §6.2.8. */
    VARIABLE_NARROWING,
    /** A planned Method's declaration carries a Method signature finding from the model. */
    METHOD_SIGNATURE_MISMATCH,
    /**
     * A request path ({@code includeOptional}, {@code excludeOptional}, {@code assignNodeId},
     * {@code concreteType}, {@code bindMethod}) matches nothing it can affect in this plan.
     */
    UNMATCHED_PATH,
    /**
     * An {@code ExposesItsArray} declaration is visible in the model but never materialized;
     * per-element exposure is out of scope.
     */
    EXPOSES_ITS_ARRAY_NOT_MATERIALIZED,
    /** A MandatoryPlaceholder's ≥1-realization obligation is unsatisfied by the request. */
    MANDATORY_PLACEHOLDER_UNSATISFIED,
    /** The type (or a dependency) changed between plan and apply; the plan is stale. */
    MODEL_CHANGED,
    /** A node constructor failed during apply. */
    CONSTRUCTOR_FAILED,
    /** An {@code onNode} or {@code bindMethod} hook threw during apply. */
    CUSTOMIZATION_FAILED,
    /**
     * The batch commit into the target {@link org.eclipse.milo.opcua.sdk.server.NodeManager}
     * failed.
     */
    COMMIT_FAILED,
    /**
     * Rollback of journaled additions failed; the diagnostic carries what could not be removed. The
     * one state requiring operator attention.
     */
    ROLLBACK_FAILED
  }

  /**
   * Create a plan-phase {@link Severity#ERROR} diagnostic.
   *
   * @param code the finding code.
   * @param message a human-readable description.
   * @param browsePath the affected path, or {@code null}.
   * @param nodeId the affected node, or {@code null}.
   * @return the diagnostic.
   */
  public static InstantiationDiagnostic planError(
      Code code, String message, @Nullable BrowsePath browsePath, @Nullable NodeId nodeId) {
    return new InstantiationDiagnostic(
        Severity.ERROR, Phase.PLAN, code, message, browsePath, nodeId);
  }

  /**
   * Create a plan-phase {@link Severity#WARNING} diagnostic.
   *
   * @param code the finding code.
   * @param message a human-readable description.
   * @param browsePath the affected path, or {@code null}.
   * @param nodeId the affected node, or {@code null}.
   * @return the diagnostic.
   */
  public static InstantiationDiagnostic planWarning(
      Code code, String message, @Nullable BrowsePath browsePath, @Nullable NodeId nodeId) {
    return new InstantiationDiagnostic(
        Severity.WARNING, Phase.PLAN, code, message, browsePath, nodeId);
  }

  /**
   * @return {@code true} if this diagnostic's severity is {@link Severity#ERROR}.
   */
  public boolean isError() {
    return severity == Severity.ERROR;
  }
}
