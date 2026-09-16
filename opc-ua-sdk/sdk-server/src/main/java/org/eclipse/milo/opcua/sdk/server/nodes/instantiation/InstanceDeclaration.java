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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.jspecify.annotations.Nullable;

/**
 * One InstanceDeclaration occurrence of a {@link TypeInstantiationModel}, keyed by its {@link
 * BrowsePath}; a snapshot, not a live node.
 *
 * <p>The same declaration node reached through multiple BrowsePaths appears as a distinct
 * occurrence per path (Part 3 §6.3.3.2). Provenance is preserved for diagnostics: {@code
 * declaringTypeId} names the type whose walk contributed this entry, and {@code
 * overriddenDeclarations} is the chain of declaration NodeIds this entry overrode (nearest first,
 * base-most last) — supertype declarations replaced per Part 3 §6.3.3.3, and same-path member-type
 * defaults replaced by explicit declarations.
 *
 * @param browsePath the occurrence's path relative to the type root; the primary key.
 * @param browseName the declaration's BrowseName (the last path element).
 * @param nodeClass the declaration's NodeClass (Object, Variable, or Method).
 * @param declarationNodeId the NodeId of the declaration node this occurrence resolves to.
 * @param declaringTypeId the type in the chain that contributed this entry.
 * @param overriddenDeclarations declaration NodeIds this entry overrode; empty if none.
 * @param modellingRuleId the raw ModellingRule Object id — vendor rules preserved (Part 3
 *     §6.4.4.1).
 * @param rule the {@link ModellingRule} classification of {@code modellingRuleId}.
 * @param typeDefinitionId the member's TypeDefinition; {@code null} for Methods.
 * @param attributes the full attribute snapshot, absent/null/value distinction preserved.
 */
public record InstanceDeclaration(
    BrowsePath browsePath,
    QualifiedName browseName,
    NodeClass nodeClass,
    NodeId declarationNodeId,
    NodeId declaringTypeId,
    List<NodeId> overriddenDeclarations,
    NodeId modellingRuleId,
    ModellingRule rule,
    @Nullable NodeId typeDefinitionId,
    AttributeSnapshot attributes) {

  public InstanceDeclaration {
    overriddenDeclarations = List.copyOf(overriddenDeclarations);
  }

  /**
   * @return {@code true} if this declaration is a placeholder extension point.
   */
  public boolean isPlaceholder() {
    return rule.isPlaceholder();
  }

  /**
   * @param overriddenDeclarations the override chain to record.
   * @return a copy of this declaration with its {@link #overriddenDeclarations()} replaced.
   */
  InstanceDeclaration withOverridden(List<NodeId> overriddenDeclarations) {
    return new InstanceDeclaration(
        browsePath,
        browseName,
        nodeClass,
        declarationNodeId,
        declaringTypeId,
        overriddenDeclarations,
        modellingRuleId,
        rule,
        typeDefinitionId,
        attributes);
  }

  /**
   * @param browsePath the path to relocate this declaration to.
   * @return a copy of this declaration with its {@link #browsePath()} replaced.
   */
  InstanceDeclaration withBrowsePath(BrowsePath browsePath) {
    return new InstanceDeclaration(
        browsePath,
        browseName,
        nodeClass,
        declarationNodeId,
        declaringTypeId,
        overriddenDeclarations,
        modellingRuleId,
        rule,
        typeDefinitionId,
        attributes);
  }
}
