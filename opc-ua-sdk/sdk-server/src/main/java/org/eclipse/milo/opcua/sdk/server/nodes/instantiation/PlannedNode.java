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

import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.jspecify.annotations.Nullable;

/**
 * One node an {@link InstantiationPlan} decided to realize: identity resolved, Java class and
 * constructor chosen, effective attributes computed — everything apply needs, decided before
 * anything is constructed.
 *
 * @param browsePath the occurrence path; {@link BrowsePath#root()} for the instance root.
 * @param declaration the declaration occurrence realized; {@code null} for the instance root, which
 *     realizes the type itself.
 * @param nodeId the resolved instance {@link NodeId} (for {@link Materialization#SHARE}, the shared
 *     declaration node's id).
 * @param nodeClass the instance NodeClass.
 * @param typeDefinitionId the instance's TypeDefinition — the declared member type, or the
 *     request's {@code concreteType} selection; {@code null} for Methods.
 * @param javaClass the Java class the node is constructed as (or expected to be, for {@link
 *     Materialization#REUSE}).
 * @param constructorTypeId the registry key the constructor was resolved from — the type itself, or
 *     the nearest registered ancestor under {@link
 *     InstantiationRequest.ClassResolution#NEAREST_ANCESTOR}; {@code null} if no registration
 *     applies (default construction).
 * @param effectiveAttributes the effective attributes: declaration over member type, absent /
 *     explicit-null distinction preserved (for the root: type attributes per Part 3 §6.4.2, then
 *     request overrides).
 * @param materialization how the node is realized at apply time.
 */
public record PlannedNode(
    BrowsePath browsePath,
    @Nullable InstanceDeclaration declaration,
    NodeId nodeId,
    NodeClass nodeClass,
    @Nullable NodeId typeDefinitionId,
    Class<? extends UaNode> javaClass,
    @Nullable NodeId constructorTypeId,
    AttributeSnapshot effectiveAttributes,
    Materialization materialization) {

  /**
   * @return {@code true} if this is the instance root.
   */
  public boolean isRoot() {
    return browsePath.isRoot();
  }

  /** How a {@link PlannedNode} is realized at apply time. */
  public enum Materialization {

    /** A new node is constructed and committed. */
    CREATE,

    /**
     * A compatible existing node in the target is adopted ({@link
     * ConflictPolicy#REUSE_COMPATIBLE}); it is never modified and never deleted by cleanup.
     */
    REUSE,

    /**
     * The type's own Method node is referenced instead of copied ({@link
     * MethodInstantiation#SHARE}); nothing is constructed.
     */
    SHARE
  }
}
