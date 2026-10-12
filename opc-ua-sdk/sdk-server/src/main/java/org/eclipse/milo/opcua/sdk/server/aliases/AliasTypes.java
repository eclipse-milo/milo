/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * The Part 17 type tests shared by the alias components: type-definition resolution and
 * equals-or-subtype checks against {@code AliasNameCategoryType}, {@code AliasNameType}, and {@code
 * AliasFor}.
 *
 * <p>A Node's type definition is the target of its first {@code HasTypeDefinition} Reference; a
 * well-formed Node has exactly one. {@code TypeTree.isSubtypeOf} is strict, so every check handles
 * the equals case explicitly.
 *
 * <p>Stateless: every call reads the server's live AddressSpace and type trees.
 */
final class AliasTypes {

  private final OpcUaServer server;

  AliasTypes(OpcUaServer server) {
    this.server = server;
  }

  /** The type definition of the Node identified by {@code nodeId}, or null if it has none. */
  @Nullable NodeId getTypeDefinitionId(NodeId nodeId) {
    return server
        .getAddressSpaceManager()
        .getManagedReferences(nodeId, Reference.HAS_TYPE_DEFINITION_PREDICATE)
        .stream()
        .findFirst()
        .flatMap(reference -> reference.getTargetNodeId().toNodeId(server.getNamespaceTable()))
        .orElse(null);
  }

  /** Whether {@code typeDefinitionId} is {@code AliasNameCategoryType} or a subtype. */
  boolean isAliasNameCategoryType(NodeId typeDefinitionId) {
    return NodeIds.AliasNameCategoryType.equals(typeDefinitionId)
        || server.getObjectTypeTree().isSubtypeOf(typeDefinitionId, NodeIds.AliasNameCategoryType);
  }

  /** Whether {@code typeDefinitionId} is {@code AliasNameType} or a subtype. */
  boolean isAliasNameType(NodeId typeDefinitionId) {
    return NodeIds.AliasNameType.equals(typeDefinitionId)
        || server.getObjectTypeTree().isSubtypeOf(typeDefinitionId, NodeIds.AliasNameType);
  }

  /** Whether the Node identified by {@code nodeId} is an {@code AliasNameCategoryType} instance. */
  boolean isAliasNameCategoryInstance(NodeId nodeId) {
    NodeId typeDefinitionId = getTypeDefinitionId(nodeId);

    return typeDefinitionId != null && isAliasNameCategoryType(typeDefinitionId);
  }

  /** Whether the Node identified by {@code nodeId} is an {@code AliasNameType} instance. */
  boolean isAliasNameInstance(NodeId nodeId) {
    NodeId typeDefinitionId = getTypeDefinitionId(nodeId);

    return typeDefinitionId != null && isAliasNameType(typeDefinitionId);
  }

  /** Whether {@code referenceTypeId} is {@code AliasFor} or a subtype. */
  boolean isAliasForOrSubtype(NodeId referenceTypeId) {
    return NodeIds.AliasFor.equals(referenceTypeId)
        || server.getReferenceTypeTree().isSubtypeOf(referenceTypeId, NodeIds.AliasFor);
  }
}
