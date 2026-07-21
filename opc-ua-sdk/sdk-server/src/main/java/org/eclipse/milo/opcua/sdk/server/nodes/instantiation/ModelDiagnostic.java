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
 * A structured finding produced while compiling a {@link TypeInstantiationModel}.
 *
 * <p>The severity dividing line is operational: a diagnostic is an {@link Severity#ERROR} only when
 * the model admits no correct instantiation of the affected declaration (missing hierarchy targets,
 * cycles, duplicate sibling BrowseNames, unresolvable types) — the compiler then rejects the whole
 * compilation rather than returning a silently truncated hierarchy. Everything the walk can
 * classify and carry — vendor rules, contested rule tightenings, abstract member types, narrowing
 * violations — is a {@link Severity#WARNING} on a model that remains usable. {@link Severity#INFO}
 * records traceable exclusions that are not defects (e.g. a rule-less node, which Part 3 §6.2.2
 * assigns to the type node only).
 *
 * @param severity the finding's severity.
 * @param code the machine-readable finding code.
 * @param message a human-readable description.
 * @param browsePath the affected declaration's path, if the finding is path-specific.
 * @param nodeId the affected node, if identifiable.
 */
public record ModelDiagnostic(
    Severity severity,
    Code code,
    String message,
    @Nullable BrowsePath browsePath,
    @Nullable NodeId nodeId) {

  /** Severity of a {@link ModelDiagnostic}. */
  public enum Severity {
    /** A traceable exclusion or note; not a defect. */
    INFO,
    /** A malformed-but-usable finding; the model compiles with the declaration classified. */
    WARNING,
    /** The model admits no correct instantiation; compilation is rejected. */
    ERROR
  }

  /** Machine-readable codes for {@link ModelDiagnostic}s. */
  public enum Code {
    /** The type definition node (root, supertype, or member type) was not found or is remote. */
    TYPE_NOT_FOUND,
    /** A node referenced as a type definition is not an ObjectType or VariableType node. */
    TYPE_NODE_CLASS_INVALID,
    /** The subtype chain or member-type recursion re-entered a type being compiled. */
    TYPE_CYCLE,
    /** A type has more than one resolvable supertype; single inheritance is required. */
    MULTIPLE_SUPERTYPES,
    /** A declaration walk re-entered a declaration node already on the current path. */
    DECLARATION_CYCLE,
    /** A forward hierarchical reference target could not be resolved. */
    REFERENCE_TARGET_MISSING,
    /** Two sibling declarations share a BrowseName (violates Part 3 §4.6.4 / §6.2.6). */
    DUPLICATE_BROWSE_NAME,
    /** An Object or Variable declaration has no HasTypeDefinition reference. */
    TYPE_DEFINITION_MISSING,
    /** An Object or Variable declaration has more than one HasTypeDefinition reference. */
    MULTIPLE_TYPE_DEFINITIONS,
    /**
     * A NodeClass incompatibility: an override changes NodeClass (Part 3 §6.3.3.3), a declaration's
     * TypeDefinition has the wrong NodeClass, or a supertype is in a different type family.
     */
    NODE_CLASS_MISMATCH,
    /**
     * An override's TypeDefinition is not the overridden declaration's type or a subtype (Part 3
     * §6.3.3.3).
     */
    TYPE_INCOMPATIBLE,
    /**
     * An override's ModellingRule transition violates Part 3 §6.4.4.2 Table 21, or is the contested
     * OptionalPlaceholder to MandatoryPlaceholder transition (a warning, not an error, because the
     * spec does not settle it).
     */
    MODELLING_RULE_TIGHTENING,
    /** An override violates the Variable narrowing rules of Part 3 §6.2.8. */
    VARIABLE_NARROWING,
    /** A Method override's argument lists are not compatible (Part 3 §6.3.3.3). */
    METHOD_SIGNATURE_MISMATCH,
    /**
     * A non-placeholder member's TypeDefinition is abstract; instantiation requires a concrete
     * subtype choice.
     */
    ABSTRACT_MEMBER_TYPE,
    /** A declaration carries a non-standard ModellingRule, classified as {@code OTHER}. */
    VENDOR_MODELLING_RULE,
    /**
     * An Object/Variable/Method under the type has no ModellingRule and is excluded (Part 3
     * §6.2.2).
     */
    NOT_AN_INSTANCE_DECLARATION,
    /** A declaration carries more than one HasModellingRule reference (Part 3 §7.12 allows one). */
    MULTIPLE_MODELLING_RULES,
    /**
     * A HasModellingRule target is unresolvable, missing, or not a ModellingRuleType Object.
     * Unresolvable-only targets reject the compile (the declaration cannot be classified); a
     * missing or wrong-class target node is a warning on the classified declaration.
     */
    MODELLING_RULE_INVALID,
    /** The type or declaration graph exceeds the compiler's depth bound; compilation rejects. */
    DEPTH_LIMIT_EXCEEDED,
    /** A reference target matches more than one declaration occurrence; the row stays absolute. */
    AMBIGUOUS_REFERENCE_TARGET,
    /** A mandatory interface member has no same-path declaration. */
    INTERFACE_MEMBER_MISSING,
    /** A same-path declaration is incompatible with a mandatory interface member. */
    INTERFACE_MEMBER_INCOMPATIBLE,
    /** A placeholder declaration's TypeDefinition could not be resolved. */
    PLACEHOLDER_TYPE_MISSING
  }

  /**
   * Create an {@link Severity#ERROR} diagnostic.
   *
   * @param code the finding code.
   * @param message a human-readable description.
   * @param browsePath the affected path, or {@code null}.
   * @param nodeId the affected node, or {@code null}.
   * @return the diagnostic.
   */
  public static ModelDiagnostic error(
      Code code, String message, @Nullable BrowsePath browsePath, @Nullable NodeId nodeId) {
    return new ModelDiagnostic(Severity.ERROR, code, message, browsePath, nodeId);
  }

  /**
   * Create a {@link Severity#WARNING} diagnostic.
   *
   * @param code the finding code.
   * @param message a human-readable description.
   * @param browsePath the affected path, or {@code null}.
   * @param nodeId the affected node, or {@code null}.
   * @return the diagnostic.
   */
  public static ModelDiagnostic warning(
      Code code, String message, @Nullable BrowsePath browsePath, @Nullable NodeId nodeId) {
    return new ModelDiagnostic(Severity.WARNING, code, message, browsePath, nodeId);
  }

  /**
   * Create an {@link Severity#INFO} diagnostic.
   *
   * @param code the finding code.
   * @param message a human-readable description.
   * @param browsePath the affected path, or {@code null}.
   * @param nodeId the affected node, or {@code null}.
   * @return the diagnostic.
   */
  public static ModelDiagnostic info(
      Code code, String message, @Nullable BrowsePath browsePath, @Nullable NodeId nodeId) {
    return new ModelDiagnostic(Severity.INFO, code, message, browsePath, nodeId);
  }

  /**
   * @return {@code true} if this diagnostic's severity is {@link Severity#ERROR}.
   */
  public boolean isError() {
    return severity == Severity.ERROR;
  }
}
