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

import static java.util.Objects.requireNonNull;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

/**
 * One member realized from a placeholder declaration by {@link
 * InstantiationRequest.Builder#expandPlaceholder}: the BrowseName the realization gets, an optional
 * pinned {@link NodeId}, and an optional concrete subtype of the placeholder's declared type.
 *
 * <p>A realization is planned as a sibling of the placeholder path — the placeholder's path with
 * its last element replaced by {@link #browseName()} — carrying the placeholder declaration's
 * attributes over its (concrete-resolved) type's, with the full member hierarchy of that type
 * realized beneath it through the same selection, allocation, and reference machinery as every
 * other planned node.
 *
 * @param browseName the BrowseName of the realized member; also its default DisplayName.
 * @param nodeId the pinned {@link NodeId} of the realized member, or {@code null} to derive one
 *     like any other planned node (request pins at the realized path are superseded by this one).
 * @param typeDefinitionId the concrete type to instantiate — the placeholder's declared type or a
 *     subtype of it — or {@code null} for the declared type itself.
 */
public record PlaceholderRealization(
    QualifiedName browseName, @Nullable NodeId nodeId, @Nullable NodeId typeDefinitionId) {

  public PlaceholderRealization {
    requireNonNull(browseName, "browseName");
  }

  /**
   * Create a realization named {@code browseName}, its NodeId derived like any other planned node
   * and its type the placeholder's declared type.
   *
   * @param browseName the BrowseName of the realized member.
   * @return the realization.
   */
  public static PlaceholderRealization of(QualifiedName browseName) {
    return new PlaceholderRealization(browseName, null, null);
  }

  /**
   * @param nodeId the {@link NodeId} to pin the realized member to.
   * @return a copy of this realization with its {@link #nodeId()} pinned.
   */
  public PlaceholderRealization withNodeId(NodeId nodeId) {
    return new PlaceholderRealization(
        browseName, requireNonNull(nodeId, "nodeId"), typeDefinitionId);
  }

  /**
   * @param typeDefinitionId the concrete type to instantiate; must be the placeholder's declared
   *     type or a subtype of it, validated at plan time.
   * @return a copy of this realization with its {@link #typeDefinitionId()} set.
   */
  public PlaceholderRealization withConcreteType(NodeId typeDefinitionId) {
    return new PlaceholderRealization(
        browseName, nodeId, requireNonNull(typeDefinitionId, "typeDefinitionId"));
  }
}
